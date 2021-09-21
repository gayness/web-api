package pink.zak.api.riptide.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.riptide.models.server.Server;
import pink.zak.api.riptide.repositories.ServerRepository;

import java.util.Optional;

@Service
public class ServerService {
    private final ServerRepository serverRepository;

    @Autowired
    public ServerService(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    //@Cacheable("server")
    public Server getServerById(long serverId) throws ResponseStatusException {
        Optional<Server> optionalServer = this.serverRepository.findById(serverId);
        if (optionalServer.isPresent())
            return optionalServer.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Server not found");
    }

    public Server getServerOrNullById(long serverId) {
        Optional<Server> optionalServer = this.serverRepository.findById(serverId);
        return optionalServer.orElse(null);
    }

    //@CachePut("server")
    public Server save(Server server) {
        return this.serverRepository.save(server);
    }
}
