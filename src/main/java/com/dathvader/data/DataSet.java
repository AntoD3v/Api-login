package com.dathvader.data;

import java.util.concurrent.ConcurrentHashMap;

public class DataSet {

    private long logPosition = 1;
    private ConcurrentHashMap<String, String> clients = new ConcurrentHashMap<>();

    public boolean hasAccount(String user) {
        return clients.containsKey(user);
    }

    public boolean isLoginWith(String user, String pass) {
        return clients.get(user).equalsIgnoreCase(pass);
    }

    public void addClient(String user, String pass){
        clients.put(user, pass);
    }

    public void updateClient(String user, String pass){
        clients.put(user, pass);
    }

    public void removeClient(String user){
        clients.remove(user);
    }

    public ConcurrentHashMap<String, String> getClients() {
        return clients;
    }

    public long getLogPosition() {
        return logPosition;
    }

    public void updateLogPosition(long logPosition) {
        this.logPosition = logPosition;
    }
}
