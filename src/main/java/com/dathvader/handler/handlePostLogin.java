package com.dathvader.handler;

import com.dathvader.average.AverageCalc;
import com.dathvader.data.DataSet;
import com.dathvader.data.JedisConnection;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import net.sf.jsqlparser.expression.StringValue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class handlePostLogin implements Handler<RoutingContext> {

    private final AverageCalc averageCalc = new AverageCalc("request");
    private final DataSet dataSet;
    private final JedisConnection cache;

    public handlePostLogin(DataSet dataSet, JedisConnection cache) {
        this.cache = cache;
        this.dataSet = dataSet;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        try{
            long begin = System.currentTimeMillis();
            String username = routingContext.request().getParam("username");
            String password = routingContext.request().getParam("password");

            if(dataSet.hasAccount(username)){

                if(dataSet.isLoginWith(username, sha1(password))){

                    final String re = "{\"STATUS\": \"Connected\", \"TOKEN\": \""+cache.generateToken(username)+"\"}";
                    routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                    routingContext.response().setStatusCode(200);
                    routingContext.response().end(re);
                    averageCalc.addValue(System.currentTimeMillis() - begin);
                    return;

                }else {

                    final String re = "{\"STATUS\": \"Bad password\"}";
                    routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                    routingContext.response().setStatusCode(401);
                    routingContext.response().end(re);
                    return;
                }

            }else{

                final String re = "{\"STATUS\": \"Client not found\"}";
                routingContext.response().putHeader("content-length", String.valueOf(re.length()));
                routingContext.response().setStatusCode(404);
                routingContext.response().end(re);
                return;

            }


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
