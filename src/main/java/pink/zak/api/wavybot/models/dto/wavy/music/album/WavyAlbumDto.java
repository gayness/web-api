package pink.zak.api.wavybot.models.dto.wavy.music.album;

import lombok.Data;
import pink.zak.api.wavybot.models.dto.wavy.music.WavyArtistDto;
import pink.zak.api.wavybot.models.music.Album;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class WavyAlbumDto {
    private String id;
    private String name;
    private Set<WavyArtistDto> artists;
    private Set<WavyAlbumImageDto> images;

    public Album toAlbum() {
        return new Album(
                this.id,
                this.name,
                this.artists.stream().map(WavyArtistDto::getId).collect(Collectors.toSet()),
                this.images.stream().map(WavyAlbumImageDto::toSpotifyImage).collect(Collectors.toSet())
        );
    }
}
