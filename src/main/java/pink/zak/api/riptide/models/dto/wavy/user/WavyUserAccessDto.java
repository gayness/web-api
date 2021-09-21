package pink.zak.api.riptide.models.dto.wavy.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WavyUserAccessDto {
    @JsonProperty("view")
    private boolean view;
    @JsonProperty("view_spotify")
    private boolean viewSpotify;
    @JsonProperty("view_discord")
    private boolean viewDiscord;
    @JsonProperty("friends")
    private boolean displayFriends;
    @JsonProperty("quarantined")
    private boolean quarantined;
    @JsonProperty("profile_comments")
    private String profileComments;
}
