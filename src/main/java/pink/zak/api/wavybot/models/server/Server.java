package pink.zak.api.wavybot.models.server;

import com.google.common.collect.Sets;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.util.Set;

@Data
@Document
public class Server {
    @Id
    private long serverId;
    @NonNull
    private Set<Long> linkedUsers;

    public Server(long serverId) {
        this.serverId = serverId;
        this.linkedUsers = Sets.newConcurrentHashSet();
    }
}
