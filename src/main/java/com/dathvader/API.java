package com.dathvader;

import com.dathvader.data.DataSet;
import com.dathvader.data.JedisConnection;
import com.dathvader.data.files.DatabaseFile;
import com.dathvader.data.sql.BinaryLogger;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;

public class API {

    private final JedisConnection cache;
    private final DataSet dataSet = new DataSet();

    public API(int poolInteger) {

        Configuration config = new Configuration();
        DatabaseFile dataFile = config.getDatabase();
        new BinaryLogger(dataSet, dataFile.getHost(), dataFile.getPort(), dataFile.getUser(), dataFile.getPassword(), dataFile.getLogBinary(), dataFile.getLogPosition()).connect();
        cache = new JedisConnection(config.getRedis().getHost(), poolInteger);

        Vertx vertx = Vertx.vertx(new VertxOptions().setEventLoopPoolSize(poolInteger));
        Consumer<Vertx> runner = v -> {
            for (int i=0;i<poolInteger; i++)
                v.deployVerticle(new Start(dataSet, cache), new DeploymentOptions());
        };

        runner.accept(vertx);

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);
                    dataSet.getClients().forEach((user, pass) -> {
                        System.out.println(user +"  -  "+pass);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        System.out.println(" * API Login (By Darthvader Team)");
        System.out.println("   Join our discord https://discord.gg/C6GUVa9\n");

        int poolInteger = 128;
        for (int i = 0; i < args.length; i++)
            if(args.equals("-T") && args.length > i)
                poolInteger = Integer.parseInt(args[i+1]);

        new API(poolInteger);
    }

}
