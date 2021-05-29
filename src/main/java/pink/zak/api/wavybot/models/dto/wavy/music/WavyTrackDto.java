package pink.zak.api.wavybot.models.dto.wavy.music;

import lombok.Data;
import pink.zak.api.wavybot.models.dto.wavy.music.album.WavyAlbumDto;
import pink.zak.api.wavybot.models.music.Track;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class WavyTrackDto {
    private String id;
    private String name;
    private WavyAlbumDto album;
    private Set<WavyArtistDto> artists;

    public Track toTrack() {
        return new Track(this.id, this.name, this.album.getId(), this.artists.stream().map(WavyArtistDto::getId).collect(Collectors.toSet()));
    }
}
