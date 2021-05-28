package pink.zak.api.wavybot.models.music;

import com.wrapper.spotify.enums.AlbumType;
import com.wrapper.spotify.enums.ReleaseDatePrecision;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Document
public class Album {
    @Id
    @NonNull
    private String id;
    @NonNull
    private Set<String> artistIds;
    @NonNull
    private Set<SpotifyImage> albumImages;
    @NonNull
    private String name;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private AlbumType albumType;
    private String label;
    private Date releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private String[] genres;
    private List<String> trackIds;

    @Transient
    public boolean isRich() {
        return this.albumType != null;
    }
}
