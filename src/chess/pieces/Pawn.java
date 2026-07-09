package chess.pieces;

import boardgame.Board;


import boardgame.Position;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.enums.Color;

public class Pawn extends ChessPiece {

    private ChessMatch chessMatch;

    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        if (getColor() == Color.WHITE) {
            p.setValues(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsApiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 2, position.getColumn());
            Position p2 = new Position(position.getRow() - 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsApiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsApiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        } else {
            p.setValues(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsApiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 2, position.getColumn());
            Position p2 = new Position(position.getRow() + 1, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsApiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsApiece(p2) && getMoveCount() == 0) {
                mat[p.getRow()][p.getColumn()] = true;
            }

            p.setValues(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
            p.setValues(position.getRow() + 1, position.getColumn() - 1);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
            }
        }
        if (getColor() == Color.WHITE) {
            Position enPassantleft = new Position(position.getRow() - 1, position.getColumn() - 1);
            Position enPassantRitgth = new Position(position.getRow() - 1, position.getColumn() + 1);
            if (getBoard().positionExists(enPassantleft)) {
                if (getBoard().piece(position.getRow(), position.getColumn() - 1) == chessMatch.getEnPassantVullnerable()) {
                    mat[position.getRow() - 1][position.getColumn() - 1] = true;
                }
            }
            if (getBoard().positionExists(enPassantRitgth)) {
                if (getBoard().piece(position.getRow(), position.getColumn() + 1) == chessMatch.getEnPassantVullnerable()) {
                    mat[position.getRow() - 1][position.getColumn() + 1] = true;
                }
            }
        } else {
            Position enPassantleft = new Position(position.getRow() + 1, position.getColumn() - 1);

            Position enPassantRitgth = new Position(position.getRow() + 1, position.getColumn() + 1);
            if (getBoard().positionExists(enPassantleft)) {
                if (getBoard().piece(position.getRow(), position.getColumn() - 1) == chessMatch.getEnPassantVullnerable()) {
                    mat[position.getRow() + 1][position.getColumn() - 1] = true;
                }
            }
            if (getBoard().positionExists(enPassantRitgth)) {
                if (getBoard().piece(position.getRow(), position.getColumn() + 1) == chessMatch.getEnPassantVullnerable()) { 
                    mat[position.getRow() + 1][position.getColumn() + 1] = true;
                }
            }
        }
        return mat;
    }

    @Override
    public String toString() {
        return "P";
    }
}
