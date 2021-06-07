package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pink.zak.api.wavybot.models.user.music.MusicData;
import pink.zak.api.wavybot.repositories.user.MusicDataRepository;

import java.util.UUID;

@Service
public class MusicDataService {
    private final MusicDataRepository musicDataRepository;

    @Autowired
    public MusicDataService(MusicDataRepository musicDataRepository) {
        this.musicDataRepository = musicDataRepository;
    }

    @Cacheable("musicData")
    public MusicData getByDiscordId(long discordId) {
        MusicData optionalData = this.musicDataRepository.findByUserId(discordId);
        if (optionalData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Music data not found");
        return optionalData;
    }

    @CachePut("musicData")
    public MusicData create(long discordId, UUID wavyUuid) {
        return this.musicDataRepository.insert(new MusicData(wavyUuid, discordId));
    }

    @CachePut("musicData")
    public MusicData save(MusicData musicData) {
        return this.musicDataRepository.save(musicData);
    }
}
