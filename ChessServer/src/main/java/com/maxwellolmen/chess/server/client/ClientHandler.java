package com.maxwellolmen.chess.server.client;

import com.maxwellolmen.chess.server.Server;
import com.maxwellolmen.chess.server.comm.EncryptionManager;

import java.io.*;
import java.net.Socket;

public class ClientHandler {

    private Server server;
    private Socket socket;

    private EncryptionManager cipher;

    private ClientInputHandler cih;
    private ClientOutputHandler coh;

    private DataOutputStream dos;
    private DataInputStream dis;

    private ClientInputHandler inputHandler;

    private boolean auth;

    public ClientHandler(Server server, Socket socket) throws Exception {
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());

        this.server = server;
        cipher = new EncryptionManager(dis, dos);
        this.socket = socket;
        auth = false;

        boolean success = cipher.handshake();

        if (!success) {
            exit();
        }

        inputHandler = new ClientInputHandler(this);
        inputHandler.start();
    }

    public void exit() {
        server.removeHandler(this);

        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataOutputStream getOutput() {
        return dos;
    }

    public DataInputStream getInput() {
        return dis;
    }

    public Server getServer() {
        return server;
    }

    public EncryptionManager getCipher() {
        return cipher;
    }
}
