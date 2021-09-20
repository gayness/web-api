package pink.zak.api.wavybot.models.user.music;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import pink.zak.api.wavybot.models.user.WavyUser;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
public class MusicData {
    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private WavyUser wavyUser;

    @ManyToMany(fetch = FetchType.LAZY)
    @NonNull
    private List<TrackListen> listens;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_track_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_user", referencedColumnName = "wavy_user")})
    @MapKeyColumn(name = "track_id")
    @Column(name = "track_plays")
    @NonNull
    private Map<String, AtomicInteger> trackPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_album_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_user", referencedColumnName = "wavy_user")})
    @MapKeyColumn(name = "album_id")
    @Column(name = "album_plays")
    @NonNull
    private Map<String, AtomicInteger> albumPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_artist_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_user", referencedColumnName = "wavy_user")})
    @MapKeyColumn(name = "artist_id")
    @Column(name = "artist_plays")
    @NonNull
    private Map<String, AtomicInteger> artistPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_suspicious_days_mapping", joinColumns = {@JoinColumn(name = "wavy_user", referencedColumnName = "wavy_user")})
    @MapKeyColumn(name = "day_start_time")
    @Column(name = "suspicious_days")
    @NonNull
    private Map<Long, SuspiciousDay> suspiciousDays; // The long is the start long of the day

    public MusicData(@NonNull WavyUser wavyUser) {
        this.wavyUser = wavyUser;
        this.listens = new CopyOnWriteArrayList<>();
        this.trackPlays = new ConcurrentHashMap<>();
        this.albumPlays = new ConcurrentHashMap<>();
        this.artistPlays = new ConcurrentHashMap<>();
        this.suspiciousDays = new ConcurrentHashMap<>();
    }
}
