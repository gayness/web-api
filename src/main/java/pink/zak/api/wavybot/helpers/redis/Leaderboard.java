package pink.zak.api.wavybot.helpers.redis;

import org.jetbrains.annotations.NotNull;

public enum Leaderboard {
        SERVER_ALBUMS("server_albums"),
        SERVER_ARTISTS("server_artists"),
        SERVER_TRACKS("server_tracks"),
        SERVER_CROWNS("server_crowns"),

        PERSONAL_ALBUMS("personal_albums"),
        PERSONAL_ARTISTS("personal_artists"),
        PERSONAL_TRACKS("personal_tracks");

        @NotNull
        private final String id;

        Leaderboard(@NotNull String id) {
            this.id = id;
        }

        @NotNull
        public String getLeaderboardId(long id) {
            return this.id + "-" + id;
        }

        @NotNull
        public String getId() {
            return this.id;
        }
    }