package pink.zak.api.wavybot.models.user.music;


import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class TrackListen implements Comparable<TrackListen> {
    private String playId;
    private String spotifyId;
    private long listenTime;

    @Override
    public int compareTo(@NonNull TrackListen o) {
        return Long.compare(o.listenTime, this.listenTime);
    }
}
