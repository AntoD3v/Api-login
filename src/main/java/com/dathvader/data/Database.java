package com.dathvader.data;

import com.dathvader.Callback;
import com.dathvader.average.AverageCalc;
import com.mysql.jdbc.ReplicationDriver;

import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

public class Database {

    private String connectionString;
    private Connection replicationConnection;
    private Properties connectionProperties;
    private ReplicationDriver replicationDriver = new ReplicationDriver();

    private AverageCalc averageCalc = new AverageCalc("mysql");

    public Database(String databaseName,String user, String pass, InetSocketAddress master, InetSocketAddress... slave) throws SQLException {
        this.connectionString = "jdbc:mysql:replication://" + master.getHostName() + ":" + master.getPort();

        for(InetSocketAddress slaveAddress : slave)
            this.connectionString += "," + slaveAddress.getHostName() + ":" + slaveAddress.getPort();

        this.connectionString += "/" + databaseName + "?allowMasterDownConnections=true";
        this.replicationConnection = getConnection();

        this.connectionProperties = new Properties();

        connectionProperties.put("autoReconnect", "true");

        connectionProperties.put("roundRobinLoadBalance", "true");

        connectionProperties.put("user", user);
        connectionProperties.put("password", pass);
    }

    private Connection getConnection() throws SQLException {
        if(this.replicationConnection == null || this.replicationConnection.isClosed()) {
            this.replicationConnection = replicationDriver.connect(this.connectionString, this.connectionProperties);
            this.replicationConnection.setReadOnly(true);
        }

        return this.replicationConnection;
    }

    public void getPlayerInformations(String id, Callback<String> callback) {
        long begin = System.currentTimeMillis();
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT motdepasse FROM auth_clients WHERE pseudonyme=?");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.beforeFirst();

            if(resultSet.next()){
                callback.completeFuture(resultSet.getString(1));
            } else {
                callback.completeFuture(null);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        this.averageCalc.addValue(System.currentTimeMillis() - begin);
    }

}
