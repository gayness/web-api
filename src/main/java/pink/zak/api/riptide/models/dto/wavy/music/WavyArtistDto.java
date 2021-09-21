package pink.zak.api.riptide.models.dto.wavy.music;

import lombok.Data;
import pink.zak.api.riptide.models.music.Artist;

@Data
public class WavyArtistDto {
    private String id;
    private String name;

    public Artist toArtist() {
        return new Artist(this.id, this.name);
    }
}
