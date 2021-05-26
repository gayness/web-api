package pink.zak.api.wavybot.models.music.album;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wrapper.spotify.enums.AlbumType;
import com.wrapper.spotify.enums.ReleaseDatePrecision;
import org.mongojack.Id;
import pink.zak.discord.wavybot.models.music.SpotifyImage;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class Album {
    @Id
    private String id;
    private Set<String> artistIds;
    private Set<SpotifyImage> albumImages;
    private String name;

    // retrieved from spotify so only present if it is enriched.
    private long lastSpotifyUpdate;
    private AlbumType albumType;
    private String label;
    private Date releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private String[] genres;
    private List<String> trackIds;

    @JsonIgnore
    public boolean isRich() {
        return this.albumType != null;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getArtistIds() {
        return this.artistIds;
    }

    public void setArtistIds(Set<String> artistIds) {
        this.artistIds = artistIds;
    }

    public Set<SpotifyImage> getAlbumImages() {
        return this.albumImages;
    }

    public void setAlbumImages(Set<SpotifyImage> albumImages) {
        this.albumImages = albumImages;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastSpotifyUpdate() {
        return this.lastSpotifyUpdate;
    }

    public void setLastSpotifyUpdate(long lastSpotifyUpdate) {
        this.lastSpotifyUpdate = lastSpotifyUpdate;
    }

    public AlbumType getAlbumType() {
        return this.albumType;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Date getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ReleaseDatePrecision getReleaseDatePrecision() {
        return this.releaseDatePrecision;
    }

    public void setReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public String[] getGenres() {
        return this.genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }

    public List<String> getTrackIds() {
        return this.trackIds;
    }

    public void setTrackIds(List<String> trackIds) {
        this.trackIds = trackIds;
    }
}
