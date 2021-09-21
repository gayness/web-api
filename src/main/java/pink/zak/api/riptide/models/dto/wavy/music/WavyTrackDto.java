package pink.zak.api.riptide.models.dto.wavy.music;

import lombok.Data;
import pink.zak.api.riptide.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.riptide.models.music.Track;

import java.util.Set;

@Data
public class WavyTrackDto {
    private String id;
    private String name;
    private WavyAlbumDto album; // todo remove of figure out how we can use them
    private Set<WavyArtistDto> artists;

    public Track toTrack() {
        return new Track(this.id, this.name);
    }
}
