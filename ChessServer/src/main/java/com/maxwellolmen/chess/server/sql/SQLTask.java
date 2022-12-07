package com.maxwellolmen.chess.server.sql;

import com.maxwellolmen.chess.server.Main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;

public abstract class SQLTask implements Callable<ResultSet> {

    public abstract ResultSet execute(Connection connection);

    @Override
    public ResultSet call() throws SQLException {
        Main.sqlManager.verifyConnection();
        return execute(Main.sqlManager.getConnection());
    }
}