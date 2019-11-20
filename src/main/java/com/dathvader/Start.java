package com.dathvader;

import com.dathvader.data.DataSet;
import com.dathvader.data.JedisConnection;
import com.dathvader.handler.handlePostLogin;
import com.dathvader.handler.handlePostToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Start extends AbstractVerticle {

    private final DataSet dataSet;
    private final JedisConnection cache;

    public Start(DataSet dataSet, JedisConnection cache) {
        this.cache = cache;
        this.dataSet = dataSet;
    }

    public void start() {
        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/auth/login").handler(new handlePostLogin(dataSet, cache));
        router.post("/auth/token").handler(new handlePostToken(cache));

        vertx.createHttpServer().requestHandler(router).listen(8080);

    }

}
