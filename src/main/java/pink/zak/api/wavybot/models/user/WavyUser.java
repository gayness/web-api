package pink.zak.api.wavybot.models.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Document
public class WavyUser {
    @Id
    @NonNull
    private UUID uuid;
    @Indexed(unique = true)
    @NonNull
    private String username;
    private String spotifyId;
    private String spotifyDisplayName;
    private long lastUpdate;

    @DBRef(lazy = true)
    private User user;

    public WavyUser(@NonNull UUID uuid, @NonNull String username, String spotifyId, String spotifyDisplayName) {
        this.uuid = uuid;
        this.username = username;
        this.spotifyId = spotifyId;
        this.spotifyDisplayName = spotifyDisplayName;
        this.lastUpdate = System.currentTimeMillis();
    }

    @NonNull
    public String getProfileLink() {
        return "https://wavy.fm/user/".concat(this.username).concat("/");
    }
}