package com.maxwellolmen.chess.client.gui;

import com.maxwellolmen.chess.game.ChessPieceType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GamePiece {

    private ChessPieceType type;

    public GamePiece(ChessPieceType type) {
        this.type = type;
    }

    public Image getImage() {
        URL url = getClass().getClassLoader().getResource(type.name().toLowerCase() + ".png");

        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println("Couldn't load image for piece: " + type.name());
            return null;
        }
    }
}