package pink.zak.api.wavybot.helpers;

import com.mongodb.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pink.zak.api.wavybot.models.dto.wavy.music.WavyArtistDto;
import pink.zak.api.wavybot.models.dto.wavy.music.WavyTrackDto;
import pink.zak.api.wavybot.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.wavybot.models.dto.wavy.music.listens.WavyListenDto;
import pink.zak.api.wavybot.repositories.music.AlbumRepository;
import pink.zak.api.wavybot.repositories.music.ArtistRepository;
import pink.zak.api.wavybot.repositories.music.TrackRepository;

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
    public void processListen(@NonNull WavyListenDto wavyListenDto) {
        this.processAlbum(wavyListenDto.getTrack().getAlbum());
        for (WavyArtistDto wavyArtistDto : wavyListenDto.getTrack().getArtists()) {
            this.processArtist(wavyArtistDto);
        }
        this.processTrack(wavyListenDto.getTrack());
    }

    @Async
    protected void processAlbum(@NonNull WavyAlbumDto wavyAlbumDto) {
        if (!this.albumStorage.existsById(wavyAlbumDto.getId())) {
            this.albumStorage.insert(wavyAlbumDto.toAlbum());
            this.spotifyHelper.enrichAlbum(wavyAlbumDto.getId());
        }
    }

    @Async
    protected void processArtist(@NonNull WavyArtistDto wavyArtistDto) {
        if (!this.artistStorage.existsById(wavyArtistDto.getId())) {
            this.artistStorage.insert(wavyArtistDto.toArtist());
            this.spotifyHelper.enrichArtist(wavyArtistDto.getId());
        }
    }

    @Async
    protected void processTrack(@NonNull WavyTrackDto wavyTrackDto) {
        if (!this.trackStorage.existsById(wavyTrackDto.getId())) {
            this.trackStorage.insert(wavyTrackDto.toTrack());
            this.spotifyHelper.enrichTrack(wavyTrackDto.getId());
        }
    }
}
