package board;

public abstract class Piece {

    protected Position position;
    private Board board;

    public Piece() {

    }

    public Piece(Board board) {
        this.board = board;
        position = null;
    }

    protected Board getBoard() {
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()];
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] possibleMovesMatrix = possibleMoves();
        for (int i = 0; i < possibleMovesMatrix.length; i++) {
            for (int j = 0; j < possibleMovesMatrix.length; j++) {
                if (possibleMoves()[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
