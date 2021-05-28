package pink.zak.api.wavybot.models.user.music;

import lombok.Data;

@Data
public class SuspiciousDay {
    private long startTimestamp;
    private int plays;
    private int playtime;
}
