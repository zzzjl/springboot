package com.zzz.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolWriper {
    /*Radisl连接池对象*/
    private JedisPool jedisPool;
    public JedisPoolWriper(final JedisPoolConfig poolConfig , final String host, final int port){
        try{
            jedisPool = new JedisPool(poolConfig,host,port);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /*获取Redis的连接池对象*/
    public JedisPool getJedisPool(){
        return jedisPool;
    }
    /* 注入连接池对象*/
    public void setJedisPool(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }

}
