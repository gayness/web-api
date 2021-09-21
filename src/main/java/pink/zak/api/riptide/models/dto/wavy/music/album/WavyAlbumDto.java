package pink.zak.api.riptide.models.dto.wavy.music.album;

import lombok.Data;
import pink.zak.api.riptide.models.dto.wavy.music.WavyArtistDto;
import pink.zak.api.riptide.models.music.Album;

import java.util.Set;

@Data
public class WavyAlbumDto {
    private String id;
    private String name;
    private Set<WavyArtistDto> artists; // todo remove of figure out how we can use them properly
    private Set<WavyAlbumImageDto> images;

    public Album toAlbum() {
        return new Album(
                this.id,
                this.name
        );
    }
}
