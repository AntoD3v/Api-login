package com.dathvader.data;

import com.dathvader.data.files.DatabaseFile;

import java.io.IOException;
import java.sql.SQLException;

public class LoginDataBase extends Database {

    private static DatabaseFile databaseFile;

    static { // MOUAIS ...
        try {
            databaseFile = new DatabaseFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LoginDataBase() throws SQLException {
        super(databaseFile.getDatabase(), databaseFile.getUser(), databaseFile.getPassword(), databaseFile.getMaster(), databaseFile.getSlaves());
    }

}
