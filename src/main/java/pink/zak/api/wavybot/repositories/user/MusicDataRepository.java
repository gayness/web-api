package pink.zak.api.wavybot.repositories.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import pink.zak.api.wavybot.models.user.music.MusicData;

import java.util.UUID;

public interface MusicDataRepository extends MongoRepository<MusicData, UUID> {

    MusicData findByUserId(long userId);
}
