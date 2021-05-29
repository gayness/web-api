package pink.zak.api.wavybot.models.dto.wavy.music.listens;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pink.zak.api.wavybot.models.dto.wavy.music.WavyTrackDto;
import pink.zak.api.wavybot.models.user.music.TrackListen;

import java.util.Date;
import java.util.Set;

@Data
public class WavyListenDto {
    @JsonProperty("artists")
    private Set<String> artistIds;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("play_id")
    private String playId;
    @JsonProperty("track")
    private WavyTrackDto track;

    public TrackListen toTrackListen() {
        return new TrackListen(this.playId, this.track.getId(), this.date.getTime());
    }
}
