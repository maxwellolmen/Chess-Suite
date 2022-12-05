package com.maxwellolmen.chess.game;

public class ChessPiece {

    private ChessPieceType type;
    private ChessPosition position;

    public ChessPiece(ChessPieceType type, ChessPosition position) {
        this.type = type;
        this.position = position;
    }

    public ChessPieceType getType() {
        return type;
    }

    public ChessPosition getPosition() {
        return position;
    }

    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    public void setType(ChessPieceType type) {
        this.type = type;
    }

    public char getDisplay() {
        return type.getDisplay();
    }

    public ChessPiece clone() {
        return new ChessPiece(type, position);
    }
}
