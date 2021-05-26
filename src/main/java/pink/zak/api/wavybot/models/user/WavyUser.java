package pink.zak.api.wavybot.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

import java.util.UUID;

public class WavyUser {
    @Id
    private UUID uuid;
    private String username;
    private String spotifyId;
    private String spotifyDisplayName;
    private long lastUpdate;

    public void setUuid(@NonNull UUID uuid) {
        this.uuid = uuid;
    }

    @NonNull
    public UUID getUuid() {
        return this.uuid;
    }

    @NonNull
    public String getUsername() {
        return this.username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    @JsonIgnore
    public String getProfileLink() {
        return "https://wavy.fm/user/".concat(this.username).concat("/");
    }

    @NonNull
    public String getSpotifyId() {
        return this.spotifyId;
    }

    public void setSpotifyId(@NonNull String spotifyId) {
        this.spotifyId = spotifyId;
    }

    @NonNull
    public String getSpotifyDisplayName() {
        return this.spotifyDisplayName;
    }

    public void setSpotifyDisplayName(@NonNull String spotifyDisplayName) {
        this.spotifyDisplayName = spotifyDisplayName;
    }

    public long getLastUpdate() {
        return this.lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}