package pink.zak.api.wavybot.models.user.music;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrackListen implements Comparable<TrackListen> {
    @Id
    @Column(name = "play_id", unique = true, nullable = false)
    @NotNull
    private String playId;

    @Column(name = "spotify_id", nullable = false)
    @NotNull
    private String spotifyId;

    @Column(name = "listen_time", nullable = false)
    private long listenTime;

    @Override
    public int compareTo(@NotNull TrackListen o) {
        return Long.compare(o.listenTime, this.listenTime);
    }
}
