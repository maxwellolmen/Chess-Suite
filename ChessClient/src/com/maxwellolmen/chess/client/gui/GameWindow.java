package com.maxwellolmen.chess.client.gui;

import com.maxwellolmen.chess.game.ChessBoard;
import com.maxwellolmen.chess.game.ChessPiece;
import com.maxwellolmen.chess.game.ChessPosition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameWindow extends Window {

    private ChessBoard board;
    private Map<ChessPosition, GamePiece> pieces;

    public GameWindow() {
        board = new ChessBoard();
        pieces = new HashMap<>();

        for (ChessPosition pos : ChessPosition.values()) {
            ChessPiece piece = board.getPiece(pos);

            if (piece != null) {
                pieces.put(pos, new GamePiece(piece.getType()));
            }
        }

        render();
    }

    public void render() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(752, 752));
        setUndecorated(true);
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                g.setColor(Color.WHITE);

                if (x % 2 == 1) {
                    g.setColor(Color.BLACK);
                }

                if (y % 2 == 1) {
                    g.setColor(g.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE);
                }

                g.fillRect(x * 94, y * 94, 94, 94);
            }
        }

        for (ChessPosition pos : ChessPosition.values()) {
            GamePiece piece = pieces.get(pos);

            if (piece == null) {
                continue;
            }

            int x = pos.getFile() * 94 - 80;
            int y = pos.getRank() * 94 - 80;

            g.drawImage(piece.getImage(), x, y, this);
        }
    }
}