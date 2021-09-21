package pink.zak.api.riptide.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pink.zak.api.riptide.models.music.Album;
import pink.zak.api.riptide.models.music.Artist;
import pink.zak.api.riptide.models.music.Track;
import pink.zak.api.riptide.services.AlbumService;
import pink.zak.api.riptide.services.ArtistService;
import pink.zak.api.riptide.services.TrackService;

import java.util.Map;

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

    @GetMapping("/album/getBulk")
    public Map<String, Album> getMultipleAlbums(@RequestParam String ids) {
        return this.albumService.getAlbumsById(ids.split(","));
    }

    @GetMapping("/artist/get")
    public Artist getArtist(@RequestParam String id) {
        return this.artistService.getArtistById(id);
    }

    @GetMapping("/artist/getBulk")
    public Map<String, Artist> getMultipleArtists(@RequestParam String ids) {
        return this.artistService.getArtistsById(ids.split(","));
    }

    @GetMapping("/track/get")
    public Track getTrack(@RequestParam String id) {
        return this.trackService.getTrackById(id);
    }

    @GetMapping("/track/getBulk")
    public Map<String, Track> getMultipleTracks(@RequestParam String ids) {
        return this.trackService.getTracksById(ids.split(","));
    }
}
