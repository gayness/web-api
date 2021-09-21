package pink.zak.api.wavybot.helpers;

import com.google.common.collect.Sets;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import pink.zak.api.wavybot.models.music.Album;
import pink.zak.api.wavybot.models.music.Artist;
import pink.zak.api.wavybot.models.music.SpotifyImage;
import pink.zak.api.wavybot.models.music.Track;
import pink.zak.api.wavybot.services.AlbumService;
import pink.zak.api.wavybot.services.ArtistService;
import pink.zak.api.wavybot.services.TrackService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class SpotifyHelper {
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final TrackService trackService;
    private final SpotifyApi spotifyApi;

    private static final ThreadLocal<SimpleDateFormat> DAY_PRECISION_FORMAT = ThreadLocal.withInitial(() -> makeSimpleDateFormat("yyyy-MM-dd", "GMT"));
    private static final ThreadLocal<SimpleDateFormat> MONTH_PRECISION_FORMAT = ThreadLocal.withInitial(() -> makeSimpleDateFormat("yyyy-MM", "GMT"));
    private static final ThreadLocal<SimpleDateFormat> YEAR_PRECISION_FORMAT = ThreadLocal.withInitial(() -> makeSimpleDateFormat("yyyy", "GMT"));

    private final Queue<String> albumQueue = new LinkedBlockingQueue<>();
    private final Queue<String> artistQueue = new LinkedBlockingQueue<>();
    private final Queue<String> trackQueue = new LinkedBlockingQueue<>();

    private final Executor executor;

    // todo use service
    @Autowired
    public SpotifyHelper(AlbumService albumService, ArtistService artistService, TrackService trackService, SpotifyApi spotifyApi, ThreadPoolTaskExecutor executor) {
        this.albumService = albumService;
        this.artistService = artistService;
        this.trackService = trackService;
        this.spotifyApi = spotifyApi;
        this.executor = executor;
    }

    public void enrichAlbum(@NotNull Album album) {
        if (!album.isRich()) {
            synchronized (this.albumQueue) {
                if (!this.albumQueue.contains(album.getId()))
                    this.albumQueue.add(album.getId());
            }
        }
    }

    private void enrichAlbum(com.wrapper.spotify.model_objects.specification.Album retrievedAlbum) {
        Album album = this.albumService.getAlbumOrNullById(retrievedAlbum.getId());
        if (album == null) {
            album = new Album();
            album.setId(retrievedAlbum.getId());
            album.setName(retrievedAlbum.getName());
            album.setArtists(Arrays.stream(retrievedAlbum.getArtists()).map(this.artistService::getArtistOrCreateBySimplified).collect(Collectors.toSet()));
            album.setAlbumImages(Arrays.stream(retrievedAlbum.getImages()).map(retrievedImage -> {
                return new SpotifyImage(retrievedImage.getUrl(), retrievedImage.getHeight(), retrievedImage.getWidth());
            }).collect(Collectors.toSet()));
        } else if (album.isRich())
            return;
        album.setAlbumType(retrievedAlbum.getAlbumType());
        album.setLabel(retrievedAlbum.getLabel());
        album.setGenres(Sets.newHashSet(retrievedAlbum.getGenres()));
        album.setTracks(Arrays.stream(retrievedAlbum.getTracks().getItems()).map(this.trackService::getTrackOrCreateBySimplified).collect(Collectors.toList()));
        album.setReleaseDate(this.parseDate(retrievedAlbum.getReleaseDate()));
        album.setReleaseDatePrecision(retrievedAlbum.getReleaseDatePrecision());
        album.setLastSpotifyUpdate(System.currentTimeMillis());
        album.getTracks().forEach(this::enrichTrack);
        this.albumService.save(album);
    }

    public void enrichArtist(@NotNull Artist artist) {
        if (!artist.isRich()) {
            synchronized (this.artistQueue) {
                if (!this.artistQueue.contains(artist.getId()))
                    this.artistQueue.add(artist.getId());
            }
        }
    }

    private void enrichArtist(com.wrapper.spotify.model_objects.specification.Artist retrievedArtist) {
        Artist artist = this.artistService.getArtistOrNullById(retrievedArtist.getId());
        if (artist == null) {
            artist = new Artist();
            artist.setId(retrievedArtist.getId());
            artist.setName(retrievedArtist.getName());
        } else if (artist.isRich())
            return;

        artist.setGenres(Sets.newHashSet(retrievedArtist.getGenres()));
        artist.setArtistImages(Arrays.stream(retrievedArtist.getImages()).map(spotifyImage -> {
            SpotifyImage image = new SpotifyImage();
            image.setHeight(spotifyImage.getHeight());
            image.setWidth(spotifyImage.getWidth());
            image.setUrl(spotifyImage.getUrl());
            return image;
        }).collect(Collectors.toSet()));
        artist.setLastSpotifyUpdate(System.currentTimeMillis());
        this.artistService.save(artist);
    }

    public void enrichTrack(@NotNull Track track) {
        if (!track.isRich()) {
            synchronized (this.trackQueue) {
                if (!this.trackQueue.contains(track.getId()))
                    this.trackQueue.add(track.getId());
            }
        }
    }

    private void enrichTrack(com.wrapper.spotify.model_objects.specification.Track retrievedTrack) {
        Track track = this.trackService.getTrackOrNullById(retrievedTrack.getId());
        if (track == null) {
            track = new Track();
            track.setId(retrievedTrack.getId());
            track.setName(retrievedTrack.getName());
            track.setAlbumId(retrievedTrack.getAlbum().getId());
            track.setArtists(Arrays.stream(retrievedTrack.getArtists()).map(this.artistService::getArtistOrCreateBySimplified).collect(Collectors.toSet()));
            track.getArtists().forEach(this::enrichArtist);
        } else if (track.isRich())
            return;

        track.setDiscNumber(retrievedTrack.getDiscNumber());
        track.setDurationMs(retrievedTrack.getDurationMs());
        track.setPreviewUrl(retrievedTrack.getPreviewUrl());
        track.setTrackNumber(retrievedTrack.getTrackNumber());
        track.setLastSpotifyUpdate(System.currentTimeMillis());
        this.trackService.save(track);
    }

    @Scheduled(fixedRate = 200, initialDelay = 5000)
    protected void checkQueue() {
        int bulkRequestTracksCount = this.bulkRequestTracks(true);
        if (bulkRequestTracksCount < 20 // there is not a full tracks bulk request
                && this.bulkRequestAlbums() <= 0 // an album request was not done
                && this.bulkRequestArtists() <= 0 // an artist request was not done
                && bulkRequestTracksCount >= 0) { // there is more than one track to be requested
            this.bulkRequestTracks(false);
        }
    }

    private int bulkRequestAlbums() {
        String[] albumIds = this.getIdsForQueue(this.albumQueue);
        if (albumIds.length > 0) {
            this.executor.execute(() -> {
                try {
                    Arrays.asList(this.spotifyApi.getSeveralAlbums(albumIds).build().execute()).forEach(this::enrichAlbum);
                } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        return albumIds.length;
    }

    private int bulkRequestArtists() {
        String[] artistIds = this.getIdsForQueue(this.artistQueue);
        if (artistIds.length > 0) {
            this.executor.execute(() -> {
                try {
                    Arrays.asList(this.spotifyApi.getSeveralArtists(artistIds).build().execute()).forEach(this::enrichArtist);
                } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        return artistIds.length;
    }

    private int bulkRequestTracks(boolean requireFull) {
        String[] trackIds = this.getIdsForQueue(this.trackQueue);
        if (trackIds.length > 0 && (!requireFull || trackIds.length >= 20)) {
            this.executor.execute(() -> {
                try {
                    Arrays.asList(this.spotifyApi.getSeveralTracks(trackIds).build().execute()).forEach(this::enrichTrack);
                } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                    e.printStackTrace();
                }
            });
        }
        return trackIds.length;
    }

    private String[] getIdsForQueue(Queue<String> queue) {
        if (!queue.isEmpty()) {
            int max = Math.min(queue.size(), 20);
            String[] albumIds = new String[max];
            for (int i = 1; i <= max && !queue.isEmpty(); i++) {
                albumIds[i - 1] = queue.poll();
            }
            return albumIds;
        }
        return new String[]{};
    }

    private Date parseDate(String spotifyDate) {
        int dashCount = 0;
        for (char c : spotifyDate.toCharArray()) {
            if (c == '-')
                dashCount++;
        }
        try {
            if (dashCount == 2) {
                return DAY_PRECISION_FORMAT.get().parse(spotifyDate);
            } else if (dashCount == 1) {
                return MONTH_PRECISION_FORMAT.get().parse(spotifyDate);
            } else if (dashCount == 0) {
                return YEAR_PRECISION_FORMAT.get().parse(spotifyDate);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static SimpleDateFormat makeSimpleDateFormat(String pattern, String id) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(id));

        return simpleDateFormat;
    }
}
