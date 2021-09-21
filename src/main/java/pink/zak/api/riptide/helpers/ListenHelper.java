package pink.zak.api.riptide.helpers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pink.zak.api.riptide.models.dto.wavy.music.WavyArtistDto;
import pink.zak.api.riptide.models.dto.wavy.music.WavyTrackDto;
import pink.zak.api.riptide.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.riptide.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.riptide.models.music.Album;
import pink.zak.api.riptide.models.music.Artist;
import pink.zak.api.riptide.models.music.Track;
import pink.zak.api.riptide.repositories.music.AlbumRepository;
import pink.zak.api.riptide.repositories.music.ArtistRepository;
import pink.zak.api.riptide.repositories.music.TrackRepository;

@Component
public class ListenHelper {
    private final AlbumRepository albumStorage;
    private final ArtistRepository artistStorage;
    private final TrackRepository trackStorage;

    private final SpotifyHelper spotifyHelper;

    @Autowired
    public ListenHelper(AlbumRepository albumStorage, ArtistRepository artistStorage, TrackRepository trackStorage, SpotifyHelper spotifyHelper) {
        this.albumStorage = albumStorage;
        this.artistStorage = artistStorage;
        this.trackStorage = trackStorage;
        this.spotifyHelper = spotifyHelper;
    }

    /**
     * Adds data such as album, track and artist to the database
     * for a listen if it is not currently in the database.
     */
    public void processListen(@NotNull WavyListenDto wavyListenDto) {
        this.processAlbum(wavyListenDto.getTrack().getAlbum());
        for (WavyArtistDto wavyArtistDto : wavyListenDto.getTrack().getArtists()) {
            this.processArtist(wavyArtistDto);
        }
        this.processTrack(wavyListenDto.getTrack());
    }

    @Async
    protected void processAlbum(@NotNull WavyAlbumDto wavyAlbumDto) {
        if (!this.albumStorage.existsById(wavyAlbumDto.getId())) {
            Album album = this.albumStorage.save(wavyAlbumDto.toAlbum());
            this.spotifyHelper.enrichAlbum(album);
        }
    }

    @Async
    protected void processArtist(@NotNull WavyArtistDto wavyArtistDto) {
        if (!this.artistStorage.existsById(wavyArtistDto.getId())) {
            Artist artist = this.artistStorage.save(wavyArtistDto.toArtist());
            this.spotifyHelper.enrichArtist(artist);
        }
    }

    @Async
    protected void processTrack(@NotNull WavyTrackDto wavyTrackDto) {
        if (!this.trackStorage.existsById(wavyTrackDto.getId())) {
            Track track = this.trackStorage.save(wavyTrackDto.toTrack());
            this.spotifyHelper.enrichTrack(track);
        }
    }
}
