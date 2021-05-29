package pink.zak.api.wavybot.models.user.music;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrackListen implements Comparable<TrackListen> {
    private String playId;
    private String spotifyId;
    private long listenTime;

    @Override
    public int compareTo(@NonNull TrackListen o) {
        return Long.compare(o.listenTime, this.listenTime);
    }
}
