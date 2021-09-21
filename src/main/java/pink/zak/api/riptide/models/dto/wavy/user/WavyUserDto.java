package pink.zak.api.riptide.models.dto.wavy.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pink.zak.api.riptide.models.user.User;
import pink.zak.api.riptide.models.user.WavyUser;

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

    public WavyUser toUser(User user) {
        return new WavyUser(this.uuid, user, this.username, this.spotifyId, this.spotifyDisplayName);
    }
}
