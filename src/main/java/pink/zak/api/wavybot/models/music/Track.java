package pink.zak.api.wavybot.models.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
public class Track {
    @Id
    private String id;
    @TextIndexed
    private String name;
    private String albumId;
    private Set<String> artistIds;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private int discNumber;
    private int durationMs;
    private String previewUrl;
    private int trackNumber;

    @JsonIgnore
    public boolean isRich() {
        return this.previewUrl != null;
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

    public String getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public Set<String> getArtistIds() {
        return this.artistIds;
    }

    public void setArtistIds(Set<String> artistIds) {
        this.artistIds = artistIds;
    }

    public long getLastSpotifyUpdate() {
        return this.lastSpotifyUpdate;
    }

    public void setLastSpotifyUpdate(long lastSpotifyUpdate) {
        this.lastSpotifyUpdate = lastSpotifyUpdate;
    }

    public int getDiscNumber() {
        return this.discNumber;
    }

    public void setDiscNumber(int discNumber) {
        this.discNumber = discNumber;
    }

    public int getDurationMs() {
        return this.durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public String getPreviewUrl() {
        return this.previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public int getTrackNumber() {
        return this.trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }
}
