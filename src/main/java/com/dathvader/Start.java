package com.dathvader;

import com.dathvader.data.Cache;
import com.dathvader.data.LoginDataBase;
import com.dathvader.handler.handlePostLogin;
import com.dathvader.handler.handlePostToken;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Start extends AbstractVerticle {

    private final LoginDataBase loginDataBase;
    private final Cache cache;

    public Start(LoginDataBase loginDataBase, Cache cache) {
        this.cache = cache;
        this.loginDataBase = loginDataBase;
    }

    public void start() {

        final Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());
        router.post("/auth/login").handler(new handlePostLogin(loginDataBase, cache));
        router.post("/auth/token").handler(new handlePostToken(cache));

        vertx.createHttpServer().requestHandler(router).listen(8080);

    }

}
