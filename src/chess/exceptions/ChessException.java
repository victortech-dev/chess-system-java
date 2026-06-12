package chess.exceptions;

import boardgame.exceptions.BoardException;

public class ChessException extends BoardException {
    private static final long serialVerisonUID = 1L;

    public ChessException(String message) {
        super(message);
    }
}
