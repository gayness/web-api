package pink.zak.api.wavybot.models.server;

import com.google.common.collect.Sets;
import org.springframework.data.annotation.Id;

import java.util.Set;

public class Server {
    @Id
    private long serverId;
    private Set<Long> linkedUsers = Sets.newConcurrentHashSet();

    public long getServerId() {
        return this.serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public Set<Long> getLinkedUsers() {
        return this.linkedUsers;
    }

    public void setLinkedUsers(Set<Long> linkedUsers) {
        this.linkedUsers = linkedUsers;
    }
}
