package com.dathvader.data;

import com.dathvader.Callback;
import com.dathvader.average.AverageCalc;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Replication {

    //private final ReplicationDriver driver;
    //private final Properties props;
    private final Connection[] connections;
    private final ExecutorService executorService;
    private int position;
    private final AverageCalc averageCalc = new AverageCalc("sql");

    public Replication(int connectionSize) {

        connections = new Connection[connectionSize];
        executorService = Executors.newFixedThreadPool(connectionSize);

        for(int i = 0; i < connections.length; i++) {
            try {
                connections[i] = DriverManager.getConnection(
                        "jdbc:mysql://104.248.161.138/obscurfight", "api", "a?]ng{MS3}W*h[k^");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Connection #"+i+" failed");
            }
        }

    }

    public void getClient(String clientName, Callback<String> callback){

        executorService.execute(() -> {
            try {

                final PreparedStatement prepare = getConnection().prepareStatement("SELECT motdepasse FROM auth_clients WHERE pseudonyme = ?");
                prepare.setString(1, clientName);

                long begin = System.currentTimeMillis();
                final ResultSet rs = prepare.executeQuery();
                if(rs.next())
                    callback.completeFuture(rs.getString("motdepasse"));
                else
                    callback.completeFuture(null);

                rs.close();
                prepare.close();
                averageCalc.addValue(System.currentTimeMillis() - begin);
            } catch (SQLException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        });
    }

    public Connection getConnection() {
        if(position >= connections.length - 1) position = 0;
        this.position++;
        return connections[position - 1];
    }
}
