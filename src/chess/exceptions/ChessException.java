package chess.exceptions;

public class ChessException extends RuntimeException {
    private static final long serialVerisonUID = 1L;

    public ChessException(String message) {
        super(message);
    }
}
