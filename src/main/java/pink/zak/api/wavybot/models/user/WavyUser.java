package pink.zak.api.wavybot.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import pink.zak.api.wavybot.models.user.music.MusicData;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class WavyUser {

    @Id
    @Column(name = "wavy_uuid")
    @Type(type = "uuid-char")
    @NotNull
    private UUID wavyUuid;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MusicData musicData = new MusicData(this);

    @Column(name = "wavy_username", unique = true, nullable = false)
    @NotNull
    private String wavyUsername;

    @Column(name = "spotify_id", unique = true)
    @Nullable
    private String spotifyId;

    @Column(name = "spotify_display_name")
    @Nullable
    private String spotifyDisplayName;

    @Column(name = "last_update", nullable = false)
    private long lastUpdate;

    public WavyUser(@NotNull UUID wavyUuid, User user, @NotNull String username, @Nullable String spotifyId, @Nullable String spotifyDisplayName) {
        this.wavyUuid = wavyUuid;
        this.user = user;
        this.wavyUsername = username;
        this.spotifyId = spotifyId;
        this.spotifyDisplayName = spotifyDisplayName;
        this.lastUpdate = System.currentTimeMillis();
    }
}