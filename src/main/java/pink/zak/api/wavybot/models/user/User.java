package pink.zak.api.wavybot.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(name = "discord_id")
    private long discordId;

    @OneToOne(fetch = FetchType.LAZY)
    private WavyUser wavyUser;

    public User(long discordId) {
        this.discordId = discordId;
    }
}
