package pink.zak.api.wavybot.models.music;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class Artist {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String name;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private Set<SpotifyImage> artistImages;
    private String[] genres;

    public Artist(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    public boolean isRich() {
        return this.artistImages != null;
    }
}
