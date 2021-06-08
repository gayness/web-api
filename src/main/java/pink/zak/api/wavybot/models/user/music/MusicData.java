package pink.zak.api.wavybot.models.user.music;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
public class MusicData {
    @NonNull
    @Id
    private UUID wavyUuid;
    @Indexed(unique = true)
    private long discordId;
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

    public MusicData(@NonNull UUID wavyUuid, long discordId) {
        this.wavyUuid = wavyUuid;
        this.discordId = discordId;
        this.listens = new CopyOnWriteArrayList<>();
        this.trackPlays = new ConcurrentHashMap<>();
        this.albumPlays = new ConcurrentHashMap<>();
        this.artistPlays = new ConcurrentHashMap<>();
        this.suspiciousDays = new ConcurrentHashMap<>();
    }
}
