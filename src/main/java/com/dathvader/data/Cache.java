package com.dathvader.data;

import com.dathvader.average.AverageCalc;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private final JedisPool jedisPool;
    private final AverageCalc averageCalc = new AverageCalc("redis");

    public Cache(String host, int port, int poolInteger) {

        JedisPoolConfig conf = new JedisPoolConfig();
        conf.setMaxTotal(poolInteger + 1    );
        conf.setTestOnBorrow(false);

            conf.setTestOnReturn(false);
        conf.setTestOnCreate(false);
        conf.setTestWhileIdle(false);
        conf.setMinEvictableIdleTimeMillis(60000);
        conf.setTimeBetweenEvictionRunsMillis(30000);
        conf.setNumTestsPerEvictionRun(-1);
        conf.setFairness(true);


        this.jedisPool = new JedisPool(conf, host, port);
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
