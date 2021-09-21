package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Track {
    @Id
    @NotNull
    @Column(name = "track_id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    // retrieved from spotify so only present if it is enriched.
    @Column(name = "last_spotify_update")
    private long lastSpotifyUpdate;

    @Column(name = "album_id")
    @NotNull
    private String albumId;

    @ManyToMany
    @NotNull
    private Set<Artist> artists;

    @Column(name = "disc_number")
    private int discNumber;

    @Column(name = "duration")
    private int durationMs;

    @Column(name = "preview_url")
    private String previewUrl;

    @Column(name = "track_number")
    private int trackNumber;

    public Track(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    @JsonIgnore
    public boolean isRich() {
        return this.previewUrl != null;
    }
}
