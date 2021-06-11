package pink.zak.api.wavybot.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
public class WavyUser {
    @Id
    @NonNull
    private UUID wavyUuid;
    private long discordId;
    @Indexed(unique = true)
    @NonNull
    private String username;
    @Nullable
    private String spotifyId;
    @Nullable
    private String spotifyDisplayName;
    private long lastUpdate;

    public WavyUser(@NonNull UUID wavyUuid, long discordId, @NonNull String username, @Nullable String spotifyId, @Nullable String spotifyDisplayName) {
        this.wavyUuid = wavyUuid;
        this.discordId = discordId;
        this.username = username;
        this.spotifyId = spotifyId;
        this.spotifyDisplayName = spotifyDisplayName;
        this.lastUpdate = System.currentTimeMillis();
    }
}