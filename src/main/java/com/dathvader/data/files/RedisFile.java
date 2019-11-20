package com.dathvader.data.files;

import java.io.IOException;

public class RedisFile extends APIFiles {

    private String user;
    private String password;
    private String host;

    public RedisFile() throws IOException {
        super("config/redis.darthvader");
    }

    @Override
    public void lineRead(String line) {
        String[] splitedLine = line.split("=");

        switch (splitedLine[0]) {
            case "host":
                this.host = splitedLine[1];
                break;
            case "user":
                this.user = splitedLine[1];
                break;
            case "password":
                this.password = splitedLine[1];
                break;
        }
    }

    @Override
    public String[] getDefaultConfig() {
        return new String[] {"host=127.0.0.1:6379","user=root", "password=foo", "", "Join our discord https://discord.gg/C6GUVa9"};
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
