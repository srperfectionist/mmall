package com.mmall.util;

import com.mmall.common.RedisShardedPool;
import com.mmall.common.RedisShardedPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author SR
 * @date 2018/1/10
 */
@Slf4j
public class RedisShardedPoolUtil {

    public static String set(String key, String value) {
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.set(key, value);
        } catch (Exception e) {
            log.error("set key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    public static String getSet(String key, String value) {
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getSet key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * @param key
     * @param exTime 单位是秒
     * @param value
     * @return
     */
    public static String setEx(String key, int exTime, String value) {
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx key:{} exTime:{} value:{} error", key, exTime, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public static Long setNx(String key, String value) {
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setEx key:{} value:{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    /**
     * @param key
     * @param exTime 秒
     * @return
     */
    public static Long expire(String key, int exTime) {
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire key:{} exTime:{} error", key, exTime, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis shardedJedis = null;
        String result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.get(key);
        } catch (Exception e) {
            log.error("get key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }

    public static Long del(String key) {
        ShardedJedis shardedJedis = null;
        Long result = null;

        try {
            shardedJedis = RedisShardedPool.getShartedJedis();
            result = shardedJedis.del(key);
        } catch (Exception e) {
            log.error("del key:{} error", key, e);
            RedisShardedPool.returnBrokenResource(shardedJedis);
            return result;
        }

        RedisShardedPool.returnResource(shardedJedis);
        return result;
    }
}
