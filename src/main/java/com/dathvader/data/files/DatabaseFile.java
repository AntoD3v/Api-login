package com.dathvader.data.files;

import java.io.IOException;

public class DatabaseFile extends APIFiles {

    private String host;
    private Integer port;
    private String password;
    private String user;
    private String logBinary;
    private Integer logPosition;

    public DatabaseFile() throws IOException {
        super("config/database.darthvader");
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }

    public String getLogBinary() {
        return logBinary;
    }

    public Integer getLogPosition() {
        return logPosition;
    }

    @Override
    public void lineRead(String line) {
        String[] splitedLine = line.split("=");

        switch (splitedLine[0]) {
            case "host":
                this.host = splitedLine[1];
                break;
            case "port":
                this.port = Integer.valueOf(splitedLine[1]);
                break;
            case "user":
                this.user = splitedLine[1];
                break;
            case "password":
                this.password = splitedLine[1];
                break;
            case "log_binary":
                this.logBinary = splitedLine[1];
                break;
            case "log_position":
                this.logPosition = Integer.valueOf(splitedLine[1]);
                break;
        }
    }

    @Override
    public String[] getDefaultConfig() {
        return new String[] {"host=68.183.46.163", "port=3306", "user=api", "password=PUe3m9", "log_binary=mysql-bin.000001", "log_position=4", "Join our discord https://discord.gg/C6GUVa9"};
    }
}
