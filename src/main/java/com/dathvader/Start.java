package com.dathvader;

import com.dathvader.data.Cache;
import com.dathvader.data.Replication;
import com.dathvader.handler.handlePostLogin;
import com.dathvader.handler.handlePostToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Start extends AbstractVerticle {

    private final Replication replication;
    private final Cache cache;

    public Start(Replication replication, Cache cache) {
        this.cache = cache;
        this.replication = replication;
    }

    public void start() {

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/auth/login").handler(new handlePostLogin(replication, cache));
        router.post("/auth/token").handler(new handlePostToken(cache));

        vertx.createHttpServer().requestHandler(router).listen(8080);

    }

}
