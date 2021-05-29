package pink.zak.api.wavybot.models.dto.wavy.music.listens;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WavyListenPage {
    @JsonProperty("total_tracks")
    private int totalTracks;
    @JsonProperty("total_artists")
    private int totalArtists;
    @JsonProperty("tracks")
    private List<WavyListenDto> tracks;
    @JsonProperty("pages")
    private int pages;
    @JsonProperty("tracked_days")
    private int trackedDays;
}
