package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.*;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SR
 * @date 2018/1/9
 */
public class RedisShardedPool {
    /**
     * ShardedJedis连接池
     */
    private static ShardedJedisPool shardedJedisPool;

    /**
     * redis1Ip
     */
    private static String redis1Ip = PropertiesUtil.getPropertyToString("redis1.ip");

    /**
     * redis1Port
     */
    private static Integer redis1Port = PropertiesUtil.getPropertyToInt("redis1.port");

    /**
     * redis2Ip
     */
    private static String redis2Ip = PropertiesUtil.getPropertyToString("redis2.ip");

    /**
     * redis2Port
     */
    private static Integer redis2Port = PropertiesUtil.getPropertyToInt("redis2.port");

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

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redis1Ip, redis1Port, 1000 * 2);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redis2Ip, redis2Port, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<>(2);
        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);

        shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public static ShardedJedis getShartedJedis() {
        return shardedJedisPool.getResource();
    }

    /**
     * 将jdeis实例放回returnBrokenResource
     *
     * @param shardedJedis
     */
    public static void returnBrokenResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnBrokenResource(shardedJedis);
    }

    /**
     * 将jedis实例放回returnResource
     *
     * @param shardedJedis
     */
    public static void returnResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
    }
}
