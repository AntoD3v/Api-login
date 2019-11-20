package com.dathvader.data.sql;

import com.dathvader.data.DataSet;
import com.github.shyiko.mysql.binlog.BinaryLogClient;

import java.io.IOException;

public class BinaryLogger implements Runnable{

    private final BinaryLogClient client;
    private final DataSet dataSet;
    private final String logBinary;
    private final Integer logPosition;

    public BinaryLogger(DataSet dataSet, String host, Integer port, String user, String password, String logBinary, Integer logPosition) {
        this.dataSet = dataSet;
        this.logBinary = logBinary;
        this.logPosition = logPosition;
        client = new BinaryLogClient(host, port, user, password);
    }

    public void connect() {
        new Thread(this, "binary-logger").start();
    }

    @Override
    public void run() {
        try {
            client.registerEventListener(new QueryEvent(dataSet));
            client.setBinlogFilename(logBinary);
            client.setBinlogPosition(logPosition);
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
