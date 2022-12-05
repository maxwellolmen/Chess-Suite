package com.maxwellolmen.chess;

import com.maxwellolmen.chess.game.ChessBoard;
import com.maxwellolmen.chess.game.ChessPieceType;
import com.maxwellolmen.chess.game.ChessPosition;

import java.util.Scanner;

public class Chess {

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        board.displayBoard();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print((board.getWhite() ? "White" : "Black") + " - Enter move: ");

            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] split = input.split("-");

            ChessPosition from, to;
            try {
                from = ChessPosition.valueOf(split[0].toUpperCase());
                to = ChessPosition.valueOf(split[1].toUpperCase());
            } catch (IllegalArgumentException ex) {
                board.displayBoard();
                System.out.println("Input could not be interpreted.");
                continue;
            }

            int result = board.move(from, to, board.getWhite() ? ChessPieceType.WHITE_QUEEN : ChessPieceType.BLACK_QUEEN);

            board.displayBoard();

            if (result == 1) {
                System.out.println("There is no piece at that position.");
            } else if (result == 2) {
                System.out.println("You can't capture your own piece.");
            } else if (result == 3) {
                System.out.println("Illegal move.");
            } else if (result == 4) {
                System.out.println("You can't move into check.");
            }

            if (board.getStatus() == 1) {
                System.out.println("Check!");
            } else if (board.getStatus() == 2) {
                System.out.println((board.getWhite() ? "Black" : "White") + " won! Good game.");
                break;
            }
        }
    }
}
