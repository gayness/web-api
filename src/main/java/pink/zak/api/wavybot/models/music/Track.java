package pink.zak.api.wavybot.models.music;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Data
@Document
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

    @Transient
    public boolean isRich() {
        return this.previewUrl != null;
    }
}
