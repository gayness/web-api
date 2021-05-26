package pink.zak.api.wavybot.models.user;

import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import pink.zak.api.wavybot.models.user.music.MusicData;

public class User {
    @Id
    private final long discordId;
    @NonNull
    private final MusicData musicData;
    @Nullable
    private WavyUser wavyUser;

    public User(long discordId) {
        this.discordId = discordId;
        this.musicData = new MusicData();
    }

    public User(long discordId, @Nullable WavyUser wavyUser, @NonNull MusicData musicData) {
        this.discordId = discordId;
        this.wavyUser = wavyUser;
        this.musicData = musicData;
    }

    public long getDiscordId() {
        return this.discordId;
    }

    @Nullable
    public WavyUser getWavyUser() {
        return this.wavyUser;
    }

    public void setWavyUser(@Nullable WavyUser wavyUser) {
        this.wavyUser = wavyUser;
    }

    @NonNull
    public MusicData getMusicData() {
        return this.musicData;
    }

    @Override
    public String toString() {
        return "User{" +
                "discordId=" + discordId +
                ", musicData=" + musicData +
                ", wavyUser=" + wavyUser +
                '}';
    }
}
