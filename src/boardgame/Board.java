package boardgame;

import boardgame.exceptions.BoardException;

public class Board {

    private int rows;
    private int columns;
    private Piece [][] pieces;

    public Board(int rows, int columns) {

        if (rows < 1 || columns < 1){
        throw new BoardException("Error creating board: there must be at  least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public Piece piece(int row, int column){
        return pieces[row][column];
        
    }
    public Piece piece(Position position){
        return pieces[position.getRow()][position.getColumn()];
    }
    public void placePiece(Piece piece, Position position){
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private boolean positionExsists(int row, int column){
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public boolean postionExists(Position position){
        return positionExsists(position.getRow(), position.getColumn());
    }

    public boolean thereIsApiece(Position position){
        return piece(position) != null;
    }


}
