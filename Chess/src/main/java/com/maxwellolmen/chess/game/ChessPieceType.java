package com.maxwellolmen.chess.game;

public enum ChessPieceType {
    //WHITE_PAWN(true, '♟'), BLACK_PAWN(false, '♙'), WHITE_KNIGHT(true, '♞'), BLACK_KNIGHT(false, '♘'),
    //    WHITE_BISHOP(true, '♝'), BLACK_BISHOP(false, '♗'), WHITE_ROOK(true, '♜'), BLACK_ROOK(false, '♖'),
    //    WHITE_QUEEN(true, '♛'), BLACK_QUEEN(false, '♕'), WHITE_KING(true, '♚'), BLACK_KING(false, '♔');

    WHITE_PAWN(true, 'P'), BLACK_PAWN(false, 'p'), WHITE_KNIGHT(true, 'N'), BLACK_KNIGHT(false, 'n'),
    WHITE_BISHOP(true, 'B'), BLACK_BISHOP(false, 'b'), WHITE_ROOK(true, 'R'), BLACK_ROOK(false, 'r'),
    WHITE_QUEEN(true, 'Q'), BLACK_QUEEN(false, 'q'), WHITE_KING(true, 'K'), BLACK_KING(false, 'k');

    private boolean white;
    private char display;

    ChessPieceType(boolean white, char display) {
        this.white = white;
        this.display = display;
    }

    public boolean isWhite() {
        return white;
    }

    public char getDisplay() {
        return display;
    }
}