package com.maxwellolmen.chess.server.client;

import com.maxwellolmen.chess.server.comm.Comm;
import org.apache.commons.lang.SerializationUtils;

public class ClientInputHandler extends Thread {

    private ClientHandler clientHandler;

    public ClientInputHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void run() {
        while (true) {
            int len;

            try {
                len = clientHandler.getInput().readInt();
                byte[] data = new byte[len];

                if (len > 0) {
                    clientHandler.getInput().readFully(data, 0, len);
                }

                len = clientHandler.getInput().readInt();
                byte[] params = new byte[len];

                if (len > 0) {
                    clientHandler.getInput().readFully(params, 0, len);
                }

                byte[] raw = clientHandler.getCipher().interpretEncryptedData(data, params);

                Comm comm;
                try {
                    comm = (Comm) SerializationUtils.deserialize(raw);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    continue;
                }

                // TODO: Process Comm object
            } catch (Exception e) {
                clientHandler.getServer().removeHandler(clientHandler);
                break;
            }
        }
    }
}
