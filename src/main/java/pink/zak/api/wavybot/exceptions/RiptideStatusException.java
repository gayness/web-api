package pink.zak.api.wavybot.exceptions;

import org.springframework.web.server.ResponseStatusException;

public class RiptideStatusException extends ResponseStatusException {
    private final RiptideStatusCode statusCode;

    public RiptideStatusException(RiptideStatusCode statusCode, String description, Throwable throwable) {
        super(statusCode.getStatusCode(), description, throwable);
        this.statusCode = statusCode;
    }

    public RiptideStatusCode getStatusCode() {
        return this.statusCode;
    }
}
