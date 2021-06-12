package pink.zak.api.wavybot.services;

import org.springframework.beans.factory.annotation.Autowired;
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

    public MusicData getByDiscordId(long discordId) {
        MusicData optionalData = this.musicDataRepository.findByDiscordId(discordId);
        if (optionalData == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Music data not found");
        return optionalData;
    }

    public MusicData create(UUID wavyUuid, long discordId) {
        return this.musicDataRepository.insert(new MusicData(wavyUuid, discordId));
    }

    public MusicData save(MusicData musicData) {
        return this.musicDataRepository.save(musicData);
    }
}
