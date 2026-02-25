package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.exceptions.ChessException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        ChessMatch chessMatch = new ChessMatch();
        Scanner scanner = new Scanner(System.in);

        while(true) {
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
                System.out.print("Source: ");
                ChessPosition source = UI.readChessPosition(scanner);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);

                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("Target: ");
                ChessPosition target = UI.readChessPosition(scanner);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            }
            catch(ChessException e) {
                System.out.println("\n" + e.getMessage());
                scanner.nextLine();
                System.out.println("\nPress ENTER to continue");
                scanner.nextLine();
            }
            catch(InputMismatchException e) {
                System.out.println("\n" + e.getMessage());
                scanner.nextLine();
                System.out.println("\nPress ENTER to continue");
                scanner.nextLine();
            }
        }
    }
}
