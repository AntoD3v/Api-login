package com.dathvader;

import java.security.NoSuchAlgorithmException;

public interface Callback<T> {

    void completeFuture(T callback) throws NoSuchAlgorithmException;

}
