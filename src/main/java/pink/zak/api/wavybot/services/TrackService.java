package pink.zak.api.wavybot.services;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.music.Track;
import pink.zak.api.wavybot.repositories.music.TrackRepository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class TrackService {
    private final TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    @Cacheable("track")
    public Track getTrackById(String spotifyId) throws ResponseStatusException {
        Optional<Track> optionalTrack = this.trackRepository.findById(spotifyId);
        if (optionalTrack.isPresent())
            return optionalTrack.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
    }

    public Track getTrackOrNullById(String spotifyId) {
        try {
            return this.getTrackById(spotifyId);
        } catch (ResponseStatusException ex) {
            return null;
        }
    }

    public Map<String, Track> getTracksById(Collection<String> spotifyIds) {
        Map<String, Track> retrievedTracks = Maps.newHashMap();
        try {
            for (String spotifyId : spotifyIds)
                retrievedTracks.put(spotifyId, this.getTrackById(spotifyId));
        } catch (ResponseStatusException ignored) {
        }
        return retrievedTracks;
    }

    @CachePut("track")
    public void save(Track track) {
        this.trackRepository.save(track);
    }
}
