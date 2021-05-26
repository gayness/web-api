package pink.zak.api.wavybot.models.user.music;

public class SuspiciousDay {
    private final long startTimestamp;
    private final int plays;
    private final int playtime;

    public SuspiciousDay(long startTimestamp, int plays, int playtime) {
        this.startTimestamp = startTimestamp;
        this.plays = plays;
        this.playtime = playtime;
    }

    public long getStartTimestamp() {
        return this.startTimestamp;
    }

    public int getPlays() {
        return this.plays;
    }

    public int getPlaytime() {
        return this.playtime;
    }

    @Override
    public String toString() {
        return "SuspiciousDay{" +
                "startTimestamp=" + startTimestamp +
                ", plays=" + plays +
                ", playtime=" + playtime +
                '}';
    }
}
