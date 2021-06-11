package pink.zak.api.wavybot.models.dto.wavy.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pink.zak.api.wavybot.models.user.WavyUser;

import java.util.UUID;

@Data
public class WavyUserDto {
    @JsonProperty("user_id")
    private UUID uuid;
    @JsonProperty("username")
    private String username;
    @JsonProperty("spotify_id")
    private String spotifyId;
    @JsonProperty("spotify_display_name")
    private String spotifyDisplayName;
    @JsonProperty("discord_id")
    private long discordId;
    @JsonProperty("discord_display_name")
    private String discordUsername;
    @JsonProperty("twitter")
    private String twitterUsername;
    @JsonProperty("instagram")
    private String instagramUsername;
    @JsonProperty("$access")
    private WavyUserAccessDto userAccess;

    public WavyUser toUser(long userId) {
        return new WavyUser(this.uuid, userId, this.username, this.spotifyId, this.spotifyDisplayName);
    }
}
