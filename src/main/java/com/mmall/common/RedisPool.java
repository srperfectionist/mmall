package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author SR
 * @date 2017/12/26
 */
public class RedisPool {
    /**
     * jedis连接池
     */
    private static JedisPool jedisPool;

    /**
     * redisIp
     */
    private static String redisIp = PropertiesUtil.getPropertyToString("redis1.ip");

    /**
     * redisPort
     */
    private static Integer redisPort = PropertiesUtil.getPropertyToInt("redis1.port");

    /**
     * 最大连接池数量
     */
    private static Integer maxTotal = PropertiesUtil.getPropertyToInteger("redis.max.total", 20);

    /**
     * 在JedisPool中最大的idle状态(空闲的)的jdeis实例个数
     */
    private static Integer maxIdle = PropertiesUtil.getPropertyToInteger("redis.max.idle", 10);

    /**
     * 在JedisPool中最小的idle状态(空闲的)的jdeis实例个数
     */
    private static Integer minIdle = PropertiesUtil.getPropertyToInteger("redis.min.idle", 2);

    /**
     * 在borrow一个Jedis实例时候，是否要进行验证。如果为true，Jedis实例可用
     */
    private static boolean testOnBorrow = PropertiesUtil.getPropertyToBoolean("redis.test.borrow", true);

    /**
     * 在return一个Jedis实例时候，是否要进行验证。如果为true，返回JedisPool的Jedis实例可用
     */
    private static boolean testOnReturn = PropertiesUtil.getPropertyToBoolean("redis.test.return", true);

    /**
     * 连接耗尽时，是否阻塞。false会抛出异常，true会阻塞直到超时。默认为true
     */
    private static boolean blockWhenExhausted = PropertiesUtil.getPropertyToBoolean("redis.block.when.exhausted", true);

    static {
        initPool();
    }

    /**
     * 初始化Jedis
     */
    private static void initPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);

        jedisPool = new JedisPool(jedisPoolConfig, redisIp, redisPort, 1000 * 2);
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 将jdeis实例放回returnBrokenResource
     *
     * @param jedis
     */
    public static void returnBrokenResource(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }

    /**
     * 将jedis实例放回returnResource
     *
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }
}
