package pink.zak.api.wavybot.services;

import com.google.common.collect.Maps;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.music.Track;
import pink.zak.api.wavybot.repositories.music.TrackRepository;
import pink.zak.api.wavybot.utils.SimplifiedUtils;

import java.util.Map;
import java.util.Optional;

@Service
public class TrackService {
    private final TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    //@Cacheable("track")
    public Track getTrackById(String spotifyId) throws ResponseStatusException {
        Optional<Track> optionalTrack = this.trackRepository.findById(spotifyId);
        if (optionalTrack.isPresent())
            return optionalTrack.get();
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found");
    }

    public Track getTrackOrNullById(String spotifyId) {
        Optional<Track> optionalTrack = this.trackRepository.findById(spotifyId);
        return optionalTrack.orElse(null);
    }

    public Track getTrackOrCreateBySimplified(TrackSimplified trackSimplified) {
        Optional<Track> optionalTrack = this.trackRepository.findById(trackSimplified.getId());
        return optionalTrack.orElseGet(() -> {
            Track track = SimplifiedUtils.createTrack(trackSimplified);
            return this.trackRepository.save(track);
        });
    }

    public Map<String, Track> getTracksById(String[] spotifyIds) {
        Map<String, Track> retrievedTracks = Maps.newHashMap();
        try {
            for (String spotifyId : spotifyIds)
                retrievedTracks.put(spotifyId, this.getTrackById(spotifyId));
        } catch (ResponseStatusException ignored) {
        }
        return retrievedTracks;
    }

    //@CachePut("track")
    public Track save(Track track) {
        return this.trackRepository.save(track);
    }
}
