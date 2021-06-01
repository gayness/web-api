package pink.zak.api.wavybot.exceptions;

import org.springframework.web.server.ResponseStatusException;

public enum RiptideStatusCode {
    DISCORD_ALREADY_LINKED(5101, "Discord account is already linked to a wavy account"),
    WAVY_ALREADY_LINKED(5102, "Wavy account is already linked to a Discord account"),
    WAVY_PROFILE_HAS_NO_DISCORD(5103, "Wavy account does not have a Discord account displayed"),
    WAVY_DISCORD_DOES_NOT_MATCH(5104, "Wavy Discord account does not match");

    private final int statusCode;
    private final String description;

    RiptideStatusCode(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public ResponseStatusException getException(Object... values) {
        return new RiptideStatusException(this, String.format(this.description, values), null);
    }

    public ResponseStatusException getException(Throwable throwable, Object... values) {
        return new RiptideStatusException(this, String.format(this.description, values), throwable);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getDescription(Object... values) {
        return String.format(this.description, values);
    }
}
