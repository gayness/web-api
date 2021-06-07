package pink.zak.api.wavybot.models.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Document
@NoArgsConstructor
public class User {
    @Id
    private long discordId;
    @Nullable
    @Indexed(unique = true)
    private UUID wavyUuid;

    public User(long discordId) {
        this.discordId = discordId;
    }
}
