package pink.zak.api.wavybot.models.user.music;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MusicData {
    @NonNull
    private final List<TrackListen> listens;
    @NonNull
    private final Map<String, AtomicInteger> trackPlays;
    @NonNull
    private final Map<String, AtomicInteger> albumPlays;
    @NonNull
    private final Map<String, AtomicInteger> artistPlays;
    @NonNull
    private final Map<Long, SuspiciousDay> suspiciousDays; // The long is the start long of the day

    public MusicData(@NonNull List<TrackListen> listens, @NonNull Map<String, AtomicInteger> trackPlays, @NonNull Map<String, AtomicInteger> albumPlays, @NonNull Map<String, AtomicInteger> artistPlays, @NonNull Map<Long, SuspiciousDay> suspiciousDays) {
        this.listens = listens;
        this.trackPlays = trackPlays;
        this.albumPlays = albumPlays;
        this.artistPlays = artistPlays;
        this.suspiciousDays = suspiciousDays;
    }

    public MusicData() {
        this(new CopyOnWriteArrayList<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>(), new ConcurrentHashMap<>());
    }

    @NonNull
    public List<TrackListen> getListens() {
        return this.listens;
    }

    @NonNull
    public Map<String, AtomicInteger> getTrackPlays() {
        return this.trackPlays;
    }

    @NonNull
    public Map<String, AtomicInteger> getAlbumPlays() {
        return this.albumPlays;
    }

    @NonNull
    public Map<String, AtomicInteger> getArtistPlays() {
        return this.artistPlays;
    }

    public Map<Long, SuspiciousDay> getSuspiciousDays() {
        return this.suspiciousDays;
    }
}
