package chess;

import board.Board;
import board.Piece;
import board.Position;import chess.enums.Color;

public abstract class ChessPiece extends Piece {

    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
        moveCount = 0;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    public Color getColor() {
        return color;
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece aux = (ChessPiece) getBoard().piece(position);

        return aux != null && color != aux.getColor();
    }
}
