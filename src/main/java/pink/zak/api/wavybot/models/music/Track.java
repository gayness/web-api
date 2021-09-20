package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
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
    @NonNull
    @Column(name = "track_id", unique = true, nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "album_id", unique = true, nullable = false)
    @NonNull
    private String albumId;

    @ManyToMany
    @NonNull
    private Set<Artist> artists;

    // retrieved from spotify so only present if it is enriched.
    @Column(name = "last_spotify_update")
    private long lastSpotifyUpdate;

    @Column(name = "disc_number")
    private int discNumber;

    @Column(name = "duration")
    private int durationMs;

    @Column(name = "preview_url")
    private String previewUrl;

    @Column(name = "track_number")
    private int trackNumber;

    public Track(@NonNull String id, @NonNull String name, @NonNull String albumId, @NonNull Set<Artist> artists) {
        this.id = id;
        this.name = name;
        this.albumId = albumId;
        this.artists = artists;
    }

    @Transient
    @JsonIgnore
    public boolean isRich() {
        return this.previewUrl != null;
    }
}
