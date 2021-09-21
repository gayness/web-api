package pink.zak.api.riptide.repositories.music;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pink.zak.api.riptide.models.music.Track;

@Repository
public interface TrackRepository extends JpaRepository<Track, String> {
}
