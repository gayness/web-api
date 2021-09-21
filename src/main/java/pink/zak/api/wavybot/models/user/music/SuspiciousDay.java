package pink.zak.api.wavybot.models.user.music;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class SuspiciousDay { // todo finish
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "start_timestamp")
    private long startTimestamp;

    @Column(name = "plays")
    private int plays;

    @Column(name = "playtime")
    private int playtime;
}
