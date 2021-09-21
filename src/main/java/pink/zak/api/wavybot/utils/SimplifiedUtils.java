package pink.zak.api.wavybot.utils;

import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import pink.zak.api.wavybot.models.music.Album;
import pink.zak.api.wavybot.models.music.Artist;
import pink.zak.api.wavybot.models.music.Track;

@UtilityClass
public class SimplifiedUtils {

    @NotNull
    public static Artist createArtist(@NotNull ArtistSimplified artistSimplified) {
        return new Artist(artistSimplified.getId(), artistSimplified.getName());
    }

    @NotNull
    public static Album createAlbum(@NotNull AlbumSimplified albumSimplified) {
        return new Album(albumSimplified.getId(), albumSimplified.getName());
    }

    @NotNull
    public static Track createTrack(@NotNull TrackSimplified trackSimplified) {
        return new Track(trackSimplified.getId(), trackSimplified.getName());
    }
}
