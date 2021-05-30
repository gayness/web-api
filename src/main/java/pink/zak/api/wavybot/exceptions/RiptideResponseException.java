package pink.zak.api.wavybot.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;

public class RiptideResponseException extends ResponseStatusException {

    public RiptideResponseException(@NonNull RiptideStatusCode status, Object... values) {
        super(status.getStatusCode(), status.getDescription(values), null);
    }

    public RiptideResponseException(@NonNull HttpStatus status, @NonNull String description) {
        super(status, description);
    }
}
