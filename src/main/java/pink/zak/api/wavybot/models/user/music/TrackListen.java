package pink.zak.api.wavybot.models.user.music;


import org.springframework.lang.NonNull;

public class TrackListen implements Comparable<TrackListen> {
    private String playId;
    private String spotifyId;
    private long listenTime;

    public String getPlayId() {
        return this.playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getSpotifyId() {
        return this.spotifyId;
    }

    public void setSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
    }

    public long getListenTime() {
        return this.listenTime;
    }

    public void setListenTime(long listenTime) {
        this.listenTime = listenTime;
    }

    @Override
    public int compareTo(@NonNull TrackListen o) {
        return Long.compare(o.listenTime, this.listenTime);
    }
}
