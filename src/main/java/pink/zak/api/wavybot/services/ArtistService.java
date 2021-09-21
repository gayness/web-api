package pink.zak.api.wavybot.services;

import com.google.common.collect.Maps;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.music.Artist;
import pink.zak.api.wavybot.repositories.music.ArtistRepository;
import pink.zak.api.wavybot.utils.SimplifiedUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    //@Cacheable("artist")
    public Artist getArtistById(String spotifyId) throws ResponseStatusException {
        Optional<Artist> optionalArtist = this.artistRepository.findById(spotifyId);
        if (optionalArtist.isPresent())
            return optionalArtist.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
    }

    public Artist getArtistOrNullById(String spotifyId) {
        Optional<Artist> optionalArtist = this.artistRepository.findById(spotifyId);
        return optionalArtist.orElse(null);
    }

    public Artist getArtistOrCreateBySimplified(ArtistSimplified artistSimplified) {
        Optional<Artist> optionalArtist = this.artistRepository.findById(artistSimplified.getId());
        return optionalArtist.orElseGet(() -> {
            Artist artist = SimplifiedUtils.createArtist(artistSimplified);
            return this.artistRepository.save(artist);
        });
    }

    public Map<String, Artist> getArtistsById(String[] spotifyIds) {
        Map<String, Artist> retrievedArtists = Maps.newHashMap();
        try {
            for (String spotifyId : spotifyIds)
                retrievedArtists.put(spotifyId, this.getArtistById(spotifyId));
        } catch (ResponseStatusException ignored) {
        }
        return retrievedArtists;
    }

    //@CachePut("artist")
    public Artist save(Artist artist) {
        return this.artistRepository.save(artist);
    }
}
