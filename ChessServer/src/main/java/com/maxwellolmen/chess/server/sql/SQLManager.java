package com.maxwellolmen.chess.server.sql;

import java.sql.*;
import java.util.Queue;
import java.util.concurrent.*;

public class SQLManager {

    private Connection connection;

    private String username, password;

    private ExecutorService thread;

    public SQLManager(String username, String password) {
        this.username = username;
        this.password = password;

        thread = Executors.newSingleThreadExecutor();
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wb5?useSSL=false&autoReconnect=true", username, password);
    }

    public void verifyConnection() throws SQLException {
        if (connection.isClosed()) {
            openConnection();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void start() throws SQLException {
        openConnection();

        Statement st = connection.createStatement();

        st.execute("CREATE TABLE IF NOT EXISTS hashes (username varchar(32), hash varchar(10000), salt varchar(64));");

        st.close();
    }

    public void close() throws SQLException {
        connection.close();
    }

    // THIS IS A BLOCKING CALL!!!
    public ResultSet call(SQLTask task) throws ExecutionException, InterruptedException {
        Future<ResultSet> f = thread.submit(task);
        return f.get();
    }

    public String loadValue(String username, String value) throws SQLException {
        if (connection.isClosed()) {
            openConnection();
        }

        PreparedStatement pst = connection.prepareStatement("SELECT * FROM hashes WHERE username=?;");
        pst.setString(1, username);

        ResultSet rs = pst.executeQuery();

        String hash = null;
        if (rs.next()) {
            hash = rs.getString(value);
        }

        rs.close();

        return hash;
    }

    public String loadHash(String username) throws SQLException {
        return loadValue(username, "hash");
    }

    public String loadSalt(String username) throws SQLException {
        return loadValue(username, "salt");
    }

    public void saveHash(String username, String hash, String salt) throws SQLException {
        if (connection.isClosed()) {
            openConnection();
        }

        PreparedStatement pst = connection.prepareStatement("DELETE FROM hashes WHERE username=?;");
        pst.setString(1, username);
        pst.execute();
        pst.close();

        pst = connection.prepareStatement("INSERT INTO hashes (username, hash, salt) VALUES (?, ?, ?);");
        pst.setString(1, username);
        pst.setString(2, hash);
        pst.setString(3, salt);
        pst.execute();
        pst.close();
    }
}