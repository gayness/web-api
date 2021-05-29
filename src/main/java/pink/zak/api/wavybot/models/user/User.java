package pink.zak.api.wavybot.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import pink.zak.api.wavybot.models.user.music.MusicData;

@Data
@Document
@NoArgsConstructor
public class User {
    @Id
    private long discordId;
    @NonNull
    private MusicData musicData;
    @Nullable
    @DBRef(lazy = true)
    private WavyUser wavyUser;

    public User(long discordId) {
        this.discordId = discordId;
        this.musicData = new MusicData(true);
    }
}
