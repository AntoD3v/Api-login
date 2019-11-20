package com.dathvader;

import com.dathvader.data.files.DatabaseFile;
import com.dathvader.data.files.RedisFile;

import java.io.IOException;

public class Configuration {

    private DatabaseFile database;
    private RedisFile redis;

    public Configuration() {
        try {
            this.database = new DatabaseFile();
            this.redis = new RedisFile();
            if(redis.isDoExit() || database.isDoExit()) {
                System.out.println("Config file were successfully created, configure it then start again");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public DatabaseFile getDatabase() {
        return database;
    }

    public RedisFile getRedis() {
        return redis;
    }
}
