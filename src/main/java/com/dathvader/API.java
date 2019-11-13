package com.dathvader;

import com.dathvader.data.Cache;
import com.dathvader.data.LoginDataBase;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.sql.SQLException;
import java.util.function.Consumer;

public class API {

    private final LoginDataBase loginDataBase;
    private final int poolInteger = 128;
    private final Cache cache;

    private final String redisHost = "134.209.22.191";
    private final int redisPort = 6379;

    public API() throws SQLException {
        cache = new Cache(redisHost, redisPort, poolInteger);
        loginDataBase = new LoginDataBase(); // ADD MULTITHREADING LATER ?

        Consumer<Vertx> runner = vertx -> {
            for (int i=0;i<poolInteger; i++)
                vertx.deployVerticle(new Start(loginDataBase, cache), new DeploymentOptions());
        };

        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(poolInteger));
        runner.accept(vertx);

    }

    public static void main(String[] args) throws SQLException {
        System.out.println(" * API Login (By Darthvader Groups)");
        new API();
    }


}
