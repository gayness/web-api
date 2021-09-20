package pink.zak.api.wavybot.models.server;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import pink.zak.api.wavybot.models.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Server {
    @Id
    @Column(name = "server_id", unique = true, nullable = false)
    private long serverId;

    @Column(name = "linked_users", nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
    @NotNull
    private Set<User> linkedUsers;

    public Server(long serverId) {
        this.serverId = serverId;
        this.linkedUsers = Sets.newConcurrentHashSet();
    }
}
