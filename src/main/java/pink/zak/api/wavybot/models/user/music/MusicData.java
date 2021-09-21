package pink.zak.api.wavybot.models.user.music;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import pink.zak.api.wavybot.models.user.WavyUser;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MusicData {
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @NotNull
    private WavyUser wavyUser;

    @Id
    @Column(name = "wavy_id")
    @Type(type = "uuid-char")
    private UUID wavyId;

    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    private List<TrackListen> listens;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_track_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_id")})
    @MapKeyColumn(name = "track_id")
    @Column(name = "track_plays")
    @NotNull
    private Map<String, AtomicInteger> trackPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_album_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_id")})
    @MapKeyColumn(name = "album_id")
    @Column(name = "album_plays")
    @NotNull
    private Map<String, AtomicInteger> albumPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_artist_plays_mapping", joinColumns = {@JoinColumn(name = "wavy_id")})
    @MapKeyColumn(name = "artist_id")
    @Column(name = "artist_plays")
    @NotNull
    private Map<String, AtomicInteger> artistPlays;

    @ElementCollection
    // todo this is probably not gonna work as wavy_user? IDK
    @CollectionTable(name = "user_suspicious_days_mapping", joinColumns = {@JoinColumn(name = "wavy_id")})
    @MapKeyColumn(name = "day_start_time")
    @Column(name = "suspicious_days")
    @NotNull
    private Map<Long, SuspiciousDay> suspiciousDays; // The long is the start long of the day

    public MusicData(@NotNull WavyUser wavyUser) {
        this.wavyUser = wavyUser;
        this.listens = new CopyOnWriteArrayList<>();
        this.trackPlays = new ConcurrentHashMap<>();
        this.albumPlays = new ConcurrentHashMap<>();
        this.artistPlays = new ConcurrentHashMap<>();
        this.suspiciousDays = new ConcurrentHashMap<>();
    }
}
