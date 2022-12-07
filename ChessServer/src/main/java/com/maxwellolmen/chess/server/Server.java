package com.maxwellolmen.chess.server;

import com.maxwellolmen.chess.server.auth.AccountManager;
import com.maxwellolmen.chess.server.client.ClientHandler;
import com.maxwellolmen.chess.server.sql.SQLManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private int port;
    private ArrayList<ClientHandler> handlers;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
        this.handlers = new ArrayList<>();
    }

    public void init() throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void maintain() {
        if (serverSocket.isClosed()) {
            try {
                init();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        interrupt();
        serverSocket.close();
    }

    @Override
    public void run() {
        while (true) try {
            maintain();

            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler;
            try {
                clientHandler = new ClientHandler(this, clientSocket);
                clientHandler.start();
            } catch (Exception e) {
                continue;
            }

            handlers.add(clientHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }
}