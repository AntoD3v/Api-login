package com.dathvader.handler;

import com.dathvader.data.Cache;
import com.dathvader.data.Replication;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class handlePostLogin implements Handler<RoutingContext> {

    private final Replication replication;
    private final Cache cache;

    public handlePostLogin(Replication replication, Cache cache) {
        this.cache = cache;
        this.replication = replication;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        try{

            String username = routingContext.request().getParam("username");
            String password = routingContext.request().getParam("password");

            replication.getClient(username, response -> {
                if(response != null) {

                    if (response.equals(sha1(password))) {

                        final String re = "{\"STATUS\": \"Connected\", \"TOKEN\": \""+cache.generateToken(username)+"\"}";
                        routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                        routingContext.response().setStatusCode(200);
                        routingContext.response().end(re);

                        return;

                    }

                    final String re = "{\"STATUS\": \"Bad password\"}";
                    routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                    routingContext.response().setStatusCode(401);
                    routingContext.response().end(re);
                    return;
                }

                final String re = "{\"STATUS\": \"Not player found\"}";
                routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                routingContext.response().setStatusCode(404);
                routingContext.response().end(re);
                return;

            });
        }catch (Exception e){
            e.printStackTrace();

            final String re = "{\"STATUS\": \"Internal error\"}";
            routingContext.response().putHeader("content-length", String.valueOf(re.length()));
            routingContext.response().setStatusCode(500);
            routingContext.response().end(re);
            return;
        }
    }

    public static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < result.length; i++)
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));

        return sb.toString();
    }
}
