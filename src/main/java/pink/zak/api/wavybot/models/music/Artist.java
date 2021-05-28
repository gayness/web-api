package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class Artist {
    @Id
    private String id;
    private String name;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private Set<SpotifyImage> artistImages;
    private String[] genres;

    @JsonIgnore
    public boolean isRich() {
        return this.artistImages != null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastSpotifyUpdate() {
        return this.lastSpotifyUpdate;
    }

    public void setLastSpotifyUpdate(long lastSpotifyUpdate) {
        this.lastSpotifyUpdate = lastSpotifyUpdate;
    }

    public Set<SpotifyImage> getArtistImages() {
        return this.artistImages;
    }

    public void setArtistImages(Set<SpotifyImage> artistImages) {
        this.artistImages = artistImages;
    }

    public String[] getGenres() {
        return this.genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
}
