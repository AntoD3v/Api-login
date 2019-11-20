package com.dathvader.data;

import com.dathvader.average.AverageCalc;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;

public class JedisConnection {

    private final JedisPool jedisPool;
    private final AverageCalc averageCalc = new AverageCalc("redis");

    public JedisConnection(String host, int poolInteger) {

        String ip = host;
        int port = 6379;
        String[] h = host.split(":");
        if(h.length == 2){
            ip = h[0];
            port = Integer.parseInt(h[1]);
        }

        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(poolInteger + 1);
        conf.setTestOnBorrow(false);
        conf.setTestOnReturn(false);
        conf.setTestOnCreate(false);
        conf.setTestWhileIdle(false);
        conf.setMinEvictableIdleTimeMillis(60000);
        conf.setTimeBetweenEvictionRunsMillis(30000);
        conf.setNumTestsPerEvictionRun(-1);
        conf.setFairness(true);


        this.jedisPool = new JedisPool(conf, ip, port);
    }

    public String generateToken(String playerName){
        try (Jedis jedis = jedisPool.getResource()) {
            String generatedToken = generateToken();

            long begin = System.currentTimeMillis();

            jedis.set(playerName, generatedToken, SetParams.setParams().ex(3600));

            averageCalc.addValue(System.currentTimeMillis() - begin);

            return generatedToken;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean match(String user, String token){
        try (Jedis jedis = jedisPool.getResource()){
            return jedis.exists(user) ? jedis.get(user).equals(token) : false;
        }
    }

}
