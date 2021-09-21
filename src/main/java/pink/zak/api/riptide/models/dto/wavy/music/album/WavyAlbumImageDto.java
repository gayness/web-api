package pink.zak.api.riptide.models.dto.wavy.music.album;

import lombok.Data;
import pink.zak.api.riptide.models.music.SpotifyImage;

@Data
public class WavyAlbumImageDto {
    private int height;
    private int width;
    private String url;

    public SpotifyImage toSpotifyImage() {
        return new SpotifyImage(this.url, this.height, this.width);
    }
}
