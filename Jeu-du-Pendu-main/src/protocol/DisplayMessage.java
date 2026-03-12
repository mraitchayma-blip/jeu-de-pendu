package protocol;

import model.GameStatus;

public class DisplayMessage {
    public final String masked;
    public final String proposed;
    public final int errors;
    public final GameStatus status;

    public DisplayMessage(String masked, String proposed, int errors, GameStatus status) {
        if (masked == null || masked.isEmpty()) throw new IllegalArgumentException("masked empty");
        if (errors < 0 || errors > 8) throw new IllegalArgumentException("errors range");
        this.masked = masked;
        this.proposed = proposed == null ? "" : proposed;
        this.errors = errors;
        this.status = status;
    }
}
