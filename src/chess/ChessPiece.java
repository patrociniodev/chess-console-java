package chess;

import board.Board;
import board.Piece;
import chess.enums.Color;

public class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
        moveCount = 0;
    }

    public Color getColor() {
        return color;
    }
}
