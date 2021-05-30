package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.server.Server;
import pink.zak.api.wavybot.repositories.ServerRepository;

import java.util.Optional;

@Service
public class ServerService {
    private final ServerRepository serverRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Cacheable("server")
    public Server getServerById(long serverId) throws ResponseStatusException {
        Optional<Server> optionalServer = this.serverRepository.findById(serverId);
        if (optionalServer.isPresent())
            return optionalServer.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Server not found");
    }

    public Server getServerOrNullById(long serverId) {
        try {
            return this.getServerById(serverId);
        } catch (ResponseStatusException ex) {
            return null;
        }
    }

    @CachePut("server")
    public void save(Server server) {
        this.serverRepository.save(server);
    }
}
