package board;

import board.exceptions.BoardException;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if (rows < 1 || columns < 1) {
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column.");
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

    public Piece piece(int row, int column) {
        if (!positionExists(row, column)) {
            throw new BoardException("There is not a piece on the given position.");
        }
        return pieces[row][column];
    }

    public Piece piece(Position p) {
        if (!positionExists(p)) {
            throw new BoardException("There is not a piece on the given position.");
        }
        return pieces[p.getRow()][p.getColumn()];
    }

    public void placePiece(Piece piece, Position position) {
        if (thereIsAPiece(position)) {
            throw new BoardException("There is already a piece on position " + position + ".");
        }
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    public Piece removePiece(Position position) {
        if(!thereIsAPiece(position)) {
            return null;
        }

        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;

        return aux;
    }

    public boolean thereIsAPiece(Position p) {
        if (!positionExists(p)) {
            throw new BoardException("Position is not on the board.");
        }
        return piece(p) != null;
    }

    public boolean positionExists(Position p) {
        return positionExists(p.getRow(), p.getColumn());
    }

    private boolean positionExists(int row, int column) {
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

}
