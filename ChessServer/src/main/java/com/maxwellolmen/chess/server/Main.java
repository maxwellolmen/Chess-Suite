package com.maxwellolmen.chess.server;

import com.maxwellolmen.chess.server.auth.AccountManager;
import com.maxwellolmen.chess.server.sql.SQLManager;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static SQLManager sqlManager;
    public static AccountManager accountManager;

    public static void main(String[] args) throws SQLException {
        System.out.print("Please specify port number: ");

        Scanner in = new Scanner(System.in);

        int port = -1;

        while (port == -1) {
            try {
                port = Integer.parseInt(in.nextLine());

                if (port < 1 || port > 65535) {
                    port = -1;
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid port number.\nPlease specify port number: ");
            }
        }

        System.out.print("Please specify MySQL username: ");
        String username = in.nextLine();
        System.out.print("Please specify MySQL password: ");
        String password = in.nextLine();

        sqlManager = new SQLManager(username, password);
        sqlManager.start();

        accountManager = new AccountManager();

        Server server = new Server(port);
        server.start();
    }
}