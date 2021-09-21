package pink.zak.api.riptide.services;

import com.google.common.collect.Maps;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.riptide.models.music.Album;
import pink.zak.api.riptide.repositories.music.AlbumRepository;
import pink.zak.api.riptide.utils.SimplifiedUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    //@Cacheable("album")
    public Album getAlbumById(String spotifyId) throws ResponseStatusException {
        Optional<Album> optionalAlbum = this.albumRepository.findById(spotifyId);
        if (optionalAlbum.isPresent())
            return optionalAlbum.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
    }

    public Album getAlbumOrNullById(String spotifyId) {
        Optional<Album> optionalAlbum = this.albumRepository.findById(spotifyId);
        return optionalAlbum.orElse(null);
    }

    public Album getArtistOrCreateBySimplified(AlbumSimplified albumSimplified) {
        Optional<Album> optionalAlbum = this.albumRepository.findById(albumSimplified.getId());
        return optionalAlbum.orElseGet(() -> {
            Album album = SimplifiedUtils.createAlbum(albumSimplified);
            return this.albumRepository.save(album);
        });
    }

    public Map<String, Album> getAlbumsById(String[] spotifyIds) {
        Map<String, Album> retrievedAlbums = Maps.newHashMap();
        try {
            for (String spotifyId : spotifyIds)
                retrievedAlbums.put(spotifyId, this.getAlbumById(spotifyId));
        } catch (ResponseStatusException ignored) {
        }
        return retrievedAlbums;
    }

    //@CachePut("album")
    public Album save(Album album) {
        return this.albumRepository.save(album);
    }
}
