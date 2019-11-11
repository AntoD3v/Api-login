package com.dathvader.handler;

import com.dathvader.data.Cache;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class handlePostToken implements Handler<RoutingContext> {

    private final Cache cache;

    public handlePostToken(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void handle(RoutingContext routingContext) {

        try{

            String token = routingContext.request().getParam("token");
            String username = routingContext.request().getParam("username");

            if(token != null){

                if(cache.match(username, token)) {

                    final String re = "{\"STATUS\": \"Token valid\"}";
                    routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                    routingContext.response().setStatusCode(200);
                    routingContext.response().end(re);

                    return;

                }

                final String re = "{\"STATUS\": \"Token expired/invalid\"}";
                routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                routingContext.response().setStatusCode(498);
                routingContext.response().end(re);

                return;

            }

            final String re = "{\"STATUS\": \"Invalid parameters\"}";
            routingContext.response().putHeader("content-length", String.valueOf(re.length()));
            routingContext.response().setStatusCode(498);
            routingContext.response().end(re);

            return;

        }catch (Exception e){
            e.printStackTrace();

            final String re = "{\"STATUS\": \"Internal error\"}";
            routingContext.response().putHeader("content-length", String.valueOf(re.length()));
            routingContext.response().setStatusCode(500);
            routingContext.response().end(re);

            return;

        }

    }
}
