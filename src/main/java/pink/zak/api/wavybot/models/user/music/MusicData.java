package pink.zak.api.wavybot.models.user.music;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class MusicData {
    @NonNull
    private List<TrackListen> listens;
    @NonNull
    private Map<String, AtomicInteger> trackPlays;
    @NonNull
    private Map<String, AtomicInteger> albumPlays;
    @NonNull
    private Map<String, AtomicInteger> artistPlays;
    @NonNull
    private Map<Long, SuspiciousDay> suspiciousDays; // The long is the start long of the day

    public MusicData(boolean isNew) {
        if (isNew) {
            this.listens = new CopyOnWriteArrayList<>();
            this.trackPlays = new ConcurrentHashMap<>();
            this.albumPlays = new ConcurrentHashMap<>();
            this.artistPlays = new ConcurrentHashMap<>();
            this.suspiciousDays = new ConcurrentHashMap<>();
        }
    }
}
