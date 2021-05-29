package pink.zak.api.wavybot.repositories.music;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.music.Album;

@Repository
public interface AlbumRepository extends MongoRepository<Album, String> {
}
