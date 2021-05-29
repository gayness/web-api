package pink.zak.api.wavybot.models.music;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class Track {
    @Id
    @NonNull
    private String id;
    @TextIndexed
    @NonNull
    private String name;
    @NonNull
    private String albumId;
    @NonNull
    private Set<String> artistIds;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private int discNumber;
    private int durationMs;
    private String previewUrl;
    private int trackNumber;

    public Track(@NonNull String id, @NonNull String name, @NonNull String albumId, @NonNull Set<String> artistIds) {
        this.id = id;
        this.name = name;
        this.albumId = albumId;
        this.artistIds = artistIds;
    }

    @Transient
    public boolean isRich() {
        return this.previewUrl != null;
    }
}
