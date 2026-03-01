package chess;

import board.Board;
import board.Piece;
import board.Position;
import chess.enums.Color;
import chess.exceptions.ChessException;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Pawn;
import chess.pieces.Rook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private List<Piece> piecesOnTheBoard;
    private List<Piece> capturedPieces;
    private boolean check;
    private boolean checkMate;

    public ChessMatch() {
        board = new Board(8, 8);
        piecesOnTheBoard = new ArrayList<>();
        capturedPieces = new ArrayList<>();
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        initSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matrix[i][j] = (ChessPiece) board.piece(i,j);
            }
        }

        return matrix;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on the given position.");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece.");
        }
        ChessPiece chessPiece = (ChessPiece) board.piece(position);
        if (chessPiece.getColor() != currentPlayer) {
            throw new ChessException("The chosen piece is not yours.");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        Piece sourcePositionPiece = board.piece(source);
        if (!sourcePositionPiece.possibleMove(target)) {
            throw new ChessException("The chosen piece can not be moved to target position.");
        }
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece aux = (ChessPiece)board.removePiece(source);
        aux.increaseMoveCount();
        Piece captured = board.removePiece(target);
        board.placePiece(aux, target);

        if (captured != null) {
            piecesOnTheBoard.remove(captured);
            capturedPieces.add(captured);
        }

        return captured;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece aux = (ChessPiece)board.removePiece(target);
        aux.decreaseMoveCount();
        board.placePiece(aux, source);
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);
        if(testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can not put yourself in check.");
        }
        check = testCheck(opponent(currentPlayer));

        if(testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position p = sourcePosition.toPosition();
        validateSourcePosition(p);
        return board.piece(p).possibleMoves();
    }

    private void initSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
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
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
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

    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> piecesFilteredByColor = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();

        for (Piece p : piecesFilteredByColor) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color.toString().toLowerCase() + "king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> oppPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).toList();

        for (Piece p : oppPieces) {
            boolean[][] possibleMoves = p.possibleMoves();
            if(possibleMoves[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate(Color color) {
        if(!testCheck(color)) {
            return false;
        }

        List<Piece> piecesFilteredByColor = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).toList();
        for(Piece p : piecesFilteredByColor) {
            boolean[][] possibleMoves = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if(possibleMoves[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i,j);
                        Piece captured = makeMove(source, target);
                        undoMove(source, target, captured);

                        if(!testCheck(color)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
