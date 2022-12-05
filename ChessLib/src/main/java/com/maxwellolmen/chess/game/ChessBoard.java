package com.maxwellolmen.chess.game;

import com.maxwellolmen.chess.Chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChessBoard implements Cloneable {

    private ArrayList<ChessPiece> pieces;

    private boolean white, wQC, wKC, bQC, bKC;
    private ChessPosition passantable;

    private ChessLogic logic;

    public ChessBoard() {
        pieces = new ArrayList<>();

        for (int i = 1; i < 9; i++) {
            pieces.add(new ChessPiece(ChessPieceType.WHITE_PAWN, ChessPosition.get(i, 2)));
        }

        for (int i = 1; i < 9; i++) {
            pieces.add(new ChessPiece(ChessPieceType.BLACK_PAWN, ChessPosition.get(i, 7)));
        }

        pieces.add(new ChessPiece(ChessPieceType.WHITE_ROOK, ChessPosition.A1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_ROOK, ChessPosition.H1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_KNIGHT, ChessPosition.B1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_KNIGHT, ChessPosition.G1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_BISHOP, ChessPosition.C1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_BISHOP, ChessPosition.F1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_QUEEN, ChessPosition.D1));
        pieces.add(new ChessPiece(ChessPieceType.WHITE_KING, ChessPosition.E1));

        pieces.add(new ChessPiece(ChessPieceType.BLACK_ROOK, ChessPosition.A8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_ROOK, ChessPosition.H8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_KNIGHT, ChessPosition.B8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_KNIGHT, ChessPosition.G8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_BISHOP, ChessPosition.C8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_BISHOP, ChessPosition.F8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_QUEEN, ChessPosition.D8));
        pieces.add(new ChessPiece(ChessPieceType.BLACK_KING, ChessPosition.E8));

        white = wQC = wKC = bQC = bKC = true;
        passantable = null;

        logic = new ChessLogic(this);
    }

    public ChessBoard(ChessBoard board) {
        pieces = new ArrayList<>();

        for (ChessPiece piece : board.getPieces()) {
            pieces.add(piece.clone());
        }

        white = board.getWhite();
        wQC = board.isCastleable(1);
        wKC = board.isCastleable(2);
        bQC = board.isCastleable(3);
        bKC = board.isCastleable(4);
        passantable = board.getPassantable();
        logic = board.getLogic();
    }

    private ChessLogic getLogic() {
        return logic;
    }

    public ChessBoard ghostMove(ChessPosition from, int dFile, int dRank) {
        return ghostMove(from, dFile, dRank, null);
    }

    public ChessBoard ghostMove(ChessPosition from, int dFile, int dRank, ChessPieceType promotion) {
        return ghostMove(from, ChessPosition.get(from, dFile, dRank));
    }

    public ChessBoard ghostMove(ChessPosition from, ChessPosition to) {
        ChessBoard board = new ChessBoard(this);

        if (board.getPiece(to) != null) {
            board.getPieces().remove(board.getPiece(to));
        }

        board.forceMove(from, to);

        return board;
    }

    public ChessPiece getPiece(ChessPosition position) {
        if (position == null) {
            return null;
        }

        for (ChessPiece piece : pieces) {
            if (piece == null) {
                continue;
            }

            try {
                if (piece.getPosition() == position) {
                    return piece;
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public ArrayList<ChessPiece> getPieces() {
        return pieces;
    }

    public boolean getWhite() {
        return white;
    }

    public void flipWhite() {
        white = !white;
    }

    // c == 1, White Queenside
    // c == 2, White Kingside
    // c == 3, Black Queenside
    // c == 4, Black Kingside
    public boolean isCastleable(int c) {
        switch (c) {
            case 1:
                return wQC;
            case 2:
                return wKC;
            case 3:
                return bQC;
            case 4:
                return bKC;
        }

        return false;
    }

    public ChessPosition getPassantable() {
        return passantable;
    }



    public void forceMove(ChessPosition from, ChessPosition to) {
        if (getPiece(from) == null) {
            System.out.println("t1");
            return;
        }

        if (getPiece(to) != null) {
            pieces.remove(getPiece(to));
        }

        getPiece(from).setPosition(to);
    }

    // 0: Move executed
    // 1: Error, no piece
    // 2: Error, self-capture
    // 3: Error, illegal move
    // 4: Error, move into check
    public int move(ChessPosition from, ChessPosition to, ChessPieceType promotion) {
        ChessPiece p1 = getPiece(from), p2 = getPiece(to);

        if (p1 == null) {
            return 1;
        }

        if (p2 != null && p1.getType().isWhite() == p2.getType().isWhite()) {
            return 2;
        }

        int l = logic.isLegal(from, to);

        if (l == 7) {
            return 3;
        }

        ChessBoard preMove = ghostMove(from, to);

        if (preMove.getStatus(preMove.getPieces()) > 0) {
            return 4;
        }

        if (getPiece(from).getType() == (white ? ChessPieceType.WHITE_KING : ChessPieceType.BLACK_KING)) {
            if (white) {
                wQC = wKC = false;
            } else {
                bQC = bKC = false;
            }
        }

        if (white) {
            if (wQC && from == ChessPosition.A1) {
                wQC = false;
            } else if (wKC && from == ChessPosition.H1) {
                wKC = false;
            }
        } else {
            if (bQC && from == ChessPosition.A8) {
                bQC = false;
            } else if (bKC && from == ChessPosition.H8) {
                bKC = false;
            }
        }

        if (l == 1) {
            forceMove(ChessPosition.A1, ChessPosition.D1);
            wQC = wKC = false;
        } else if (l == 2) {
            forceMove(ChessPosition.H1, ChessPosition.F1);
            wQC = wKC = false;
        } else if (l == 3) {
            forceMove(ChessPosition.A8, ChessPosition.D8);
            bQC = bKC = false;
        } else if (l == 4) {
            forceMove(ChessPosition.H8, ChessPosition.F8);
            bQC = bKC = false;
        } else if (l == 5) {
            pieces.remove(getPiece(passantable));
        } else if (l == 6) {
            p1.setType(promotion);
        } else if (l == 8) {
            passantable = to;
        }

        if (l != 8) {
            passantable = null;
        }

        forceMove(from, to);
        white = !white;

        return 0;
    }

    public int getStatus() {
        return getStatus(pieces);
    }

    public int getStatus(List<ChessPiece> position) {
        return getStatus(position, false);
    }

    // 0: Legal, passive position
    // 1: Check
    // 2: Checkmate
    public int getStatus(List<ChessPiece> position, boolean ignoreMate) {
        for (ChessPiece piece : position) {
            int ic = logic.isChecking(piece, ignoreMate);
            if (ic != 0) {
                return ic;
            }
        }

        return 0;
    }
    
    public void displayBoard() {
        if (white) {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nWhite to play!");

            System.out.println("_________________________________");
            for (int r = 8; r >= 1; r--) {
                System.out.print("|");
                for (int f = 1; f <= 8; f++) {
                    if (getPiece(ChessPosition.get(f, r)) == null) {
                        System.out.print("   |");
                    } else {
                        System.out.print(" " + getPiece(ChessPosition.get(f, r)).getDisplay() + " |");
                    }
                }
                System.out.print("\n");

                System.out.println("_________________________________");
            }
        } else {
            System.out.println("\n\n\n\n\n\n\n\n\n\n\nBlack to play!");

            System.out.println("_________________________________");
            for (int r = 1; r <= 8; r++) {
                System.out.print("|");
                for (int f = 8; f >= 1; f--) {
                    if (getPiece(ChessPosition.get(f, r)) == null) {
                        System.out.print("   |");
                    } else {
                        System.out.print(" " + getPiece(ChessPosition.get(f, r)).getDisplay() + " |");
                    }
                }
                System.out.print("\n");

                System.out.println("_________________________________");
            }
        }
    }
}