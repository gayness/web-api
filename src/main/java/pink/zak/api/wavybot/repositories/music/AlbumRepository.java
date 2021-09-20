package pink.zak.api.wavybot.repositories.music;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.wavybot.models.music.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, String> {
}
