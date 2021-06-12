package pink.zak.api.wavybot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.wavybot.models.music.Album;
import pink.zak.api.wavybot.models.music.Artist;
import pink.zak.api.wavybot.models.music.Track;
import pink.zak.api.wavybot.services.AlbumService;
import pink.zak.api.wavybot.services.ArtistService;
import pink.zak.api.wavybot.services.TrackService;

@RestController
@RequestMapping("/spotify")
public class SpotifyController {
    private final AlbumService albumService;
    private final ArtistService artistService;
    private final TrackService trackService;

    @Autowired
    public SpotifyController(AlbumService albumService, ArtistService artistService, TrackService trackService) {
        this.albumService = albumService;
        this.artistService = artistService;
        this.trackService = trackService;
    }

    @GetMapping("/album/get")
    public Album getAlbum(@RequestParam String id) {
        return this.albumService.getAlbumById(id);
    }

    @GetMapping("/artist/get")
    public Artist getArtist(@RequestParam String id) {
        return this.artistService.getArtistById(id);
    }

    @GetMapping("/track/get")
    public Track getTrack(@RequestParam String id) {
        return this.trackService.getTrackById(id);
    }
}
