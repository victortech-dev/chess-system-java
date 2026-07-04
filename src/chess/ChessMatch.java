package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.enums.Color;
import chess.exceptions.ChessException;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private final Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check = false;
    private boolean checkMate = false;

    private final List<Piece> piecesOnTheBoard = new ArrayList<>();
    private final List<Piece> capturedPieces = new ArrayList<>();

    public boolean isCheck() {
        return check;
    }

    public ChessMatch() {
        turn = 1;
        currentPlayer = Color.WHITE;
        board = new Board(8, 8);
        initialSetup();
    }

    public int getTurn() {
        return turn;
    }


    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public List<ChessPiece> getPiecesOnTheBoard() {
        return piecesOnTheBoard.stream().map(p -> (ChessPiece) p).collect(Collectors.toList());
    }

    public List<ChessPiece> getCapturedPieces() {
        return capturedPieces.stream().map(p -> (ChessPiece) p).collect(Collectors.toList());
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetposition) {
        Position source = sourcePosition.toPosition();
        Position target = targetposition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }
        nextTurn();
        check = testCheck(currentPlayer);
        if (check) {
            checkMate = testCheckMate(currentPlayer);
        }
        return (ChessPiece) capturedPiece;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position source = sourcePosition.toPosition();
        validateSourcePosition(source);

        return board.piece(source).possibleMoves();
    }


    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        ((ChessPiece) p).increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
            piecesOnTheBoard.remove(capturedPiece);
        }
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        Piece p = board.removePiece(target);
        ((ChessPiece) p).decreaseMoveCount();
        board.placePiece(p, source);
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);

        }
    }

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;

    }

    private King king(Color color) {
        return (King) piecesOnTheBoard.stream().filter(p -> p instanceof King && ((ChessPiece) p).getColor() == color).findFirst().orElseThrow(() -> new IllegalStateException("There is no " + color + " king on the board"));
    }

    private boolean testCheck(Color color) {
        Position positionKing = king(color).getPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(p -> ((ChessPiece) p).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            if (p.possibleMove(positionKing)) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> colorPiece = piecesOnTheBoard.stream().filter(p -> ((ChessPiece) p).getColor() == color).collect(Collectors.toList());
        for (Piece p : colorPiece) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = p.getPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }

                    }
                }
            }
        }
        return true;

    }


    private void validateSourcePosition(Position position) {
        if (!board.thereIsApiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (((ChessPiece) board.piece(position)).getColor() != currentPlayer) {
            throw new ChessException("The chosen piece is not yours");

        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece ");

        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("Ther chosen piece cannot move to target position");
        }

    }


    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);

    }

    private void initialSetup() {

        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('g', 1, new Kinght(board, Color.WHITE));
        placeNewPiece('b', 1, new Kinght(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));


        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('g', 8, new Kinght(board, Color.BLACK));
        placeNewPiece('b', 8, new Kinght(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));
    }


}
