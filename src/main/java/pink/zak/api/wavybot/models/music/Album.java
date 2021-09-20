package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wrapper.spotify.enums.AlbumType;
import com.wrapper.spotify.enums.ReleaseDatePrecision;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Album {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @NonNull
    private String id;

    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    @Column(name = "artists", nullable = false)
    @ManyToMany
    @NonNull
    private Set<Artist> artists;

    @NonNull
    @Column(name = "album_images", nullable = false)
    @OneToMany
    private Set<SpotifyImage> albumImages;

    // retrieved from spotify so only present if it is enriched.
    @Column(name = "last_spotify_update")
    private long lastSpotifyUpdate;

    @Column(name = "album_type")
    private AlbumType albumType;

    @Column(name = "label")
    private String label;

    @Column(name = "release_date")
    private Date releaseDate;

    @Column(name = "release_date_precision")
    private ReleaseDatePrecision releaseDatePrecision;

    @ElementCollection
    @Column(name = "genres")
    private Set<String> genres;

    @Column(name = "tracks")
    @ManyToMany
    @NotNull
    private List<Track> tracks;

    public Album(@NonNull String id, @NonNull String name, @NonNull Set<Artist> artists, @NonNull Set<SpotifyImage> albumImages) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.albumImages = albumImages;
    }

    @Transient
    @JsonIgnore
    public boolean isRich() {
        return this.albumType != null;
    }
}
