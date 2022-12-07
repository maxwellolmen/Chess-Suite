package com.maxwellolmen.chess.server.client;

import com.maxwellolmen.chess.server.comm.Comm;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;

public class ClientOutputHandler {

    private ClientHandler clientHandler;

    public ClientOutputHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void sendComm(Comm comm) throws IOException {
        byte[] ser = SerializationUtils.serialize(comm);
        clientHandler.getOutput().writeInt(ser.length);
        clientHandler.getOutput().write(ser);
    }
}