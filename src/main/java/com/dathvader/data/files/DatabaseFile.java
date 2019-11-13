package com.dathvader.data.files;

import java.io.IOException;
import java.net.InetSocketAddress;

public class DatabaseFile extends APIFiles {

    private String database, user,password;
    private InetSocketAddress master;
    private InetSocketAddress slaves[];

    public DatabaseFile() throws IOException {
        super("config/database.darthvader");
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public InetSocketAddress getMaster() {
        return master;
    }

    public InetSocketAddress[] getSlaves() {
        return slaves;
    }

    @Override
    public void lineRead(String line) {
        String[] splitedLine = line.split("=");

        switch (splitedLine[0]) {
            case "database":
                this.database = splitedLine[1];
                break;
            case "user":
                this.user = splitedLine[1];
                break;
            case "password":
                this.password = splitedLine[1];
                break;
            case "master":
                this.master = getAdresses(splitedLine[1])[0];
                break;
            case "slaves":
                this.slaves = getAdresses(splitedLine[1]);
                break;
        }
    }

    private InetSocketAddress[] getAdresses(String s) {
        String[] splitedLines = s.split(",");
        InetSocketAddress[] inetSocketAddress = new InetSocketAddress[splitedLines.length]; // ?

        for (int i = 0; i < splitedLines.length; i++) {
            String[] splitedLine = splitedLines[i].split(":");

            inetSocketAddress[i] = new InetSocketAddress(splitedLine[0], Integer.parseInt(splitedLine[1]));
        }

        return inetSocketAddress;
    }

    @Override
    public String[] getDefaultConfig() {
        return new String[] {"database=players","user=root", "password=foo","master=127.0.0.1:007", "slaves=127.0.0.1:008,127.0.0.1:009,127.0.0.1:0010","", "Join our discord https://discord.gg/C6GUVa9"};
    }
}
