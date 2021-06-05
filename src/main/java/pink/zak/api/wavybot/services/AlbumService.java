package pink.zak.api.wavybot.services;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.music.Album;
import pink.zak.api.wavybot.repositories.music.AlbumRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @Cacheable("album")
    public Album getAlbumById(String spotifyId) throws ResponseStatusException {
        Optional<Album> optionalAlbum = this.albumRepository.findById(spotifyId);
        if (optionalAlbum.isPresent())
            return optionalAlbum.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
    }

    public Album getAlbumOrNullById(String spotifyId) {
        try {
            return this.getAlbumById(spotifyId);
        } catch (ResponseStatusException ex) {
            return null;
        }
    }

    public Map<String, Album> getAlbumsById(Collection<String> spotifyIds) {
        Map<String, Album> retrievedAlbums = Maps.newHashMap();
        try {
            for (String spotifyId : spotifyIds)
                retrievedAlbums.put(spotifyId, this.getAlbumById(spotifyId));
        } catch (ResponseStatusException ignored) {
        }
        return retrievedAlbums;
    }

    @CachePut("album")
    public Album save(Album album) {
        return this.albumRepository.save(album);
    }
}
