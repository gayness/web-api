package pink.zak.api.wavybot.models.music;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyImage {
    private int height;
    private int width;
    private String url;
}
