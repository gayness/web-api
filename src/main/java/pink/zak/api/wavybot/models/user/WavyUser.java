package pink.zak.api.wavybot.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
public class WavyUser {
    @Id
    @NonNull
    private UUID wavyUuid;
    @Indexed(unique = true)
    @NonNull
    private String username;
    private String spotifyId;
    private String spotifyDisplayName;
    private long lastUpdate;

    private long userId;

    public WavyUser(@NonNull UUID wavyUuid, @NonNull String username, String spotifyId, String spotifyDisplayName, long userId) {
        this.wavyUuid = wavyUuid;
        this.username = username;
        this.spotifyId = spotifyId;
        this.spotifyDisplayName = spotifyDisplayName;
        this.lastUpdate = System.currentTimeMillis();

        this.userId = userId;
    }
}