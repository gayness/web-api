package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Artist {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @NonNull
    private String id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    // retrieved from spotify so only present if it is enriched.
    @Column(name = "last_spotify_update")
    private long lastSpotifyUpdate;

    @Column(name = "artist_images")
    @OneToMany
    private Set<SpotifyImage> artistImages;

    @ElementCollection
    @Column(name = "genres")
    private Set<String> genres;

    public Artist(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @Transient
    @JsonIgnore
    public boolean isRich() {
        return this.artistImages != null;
    }
}
