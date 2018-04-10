package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedissonManager;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.concurrent.TimeUnit;

/**
 * @author SR
 * @date 2018/2/22
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private RedissonManager redissonManager;

    /**
     * 没有分布式锁，运行起来来看日志。
     */
    public void closeOrderTaskV1() {
        Integer hour = PropertiesUtil.getPropertyToInteger("close.order.task.time.hour", 2);
        iOrderService.closeOrder(hour);
    }

    /**
     * 可能出现死锁，虽然在执行close的时候有防死锁，但是还是会出现，继续演进V3
     */
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务开始");
        Long lockTimeout = PropertiesUtil.getPropertyToLong("lock.timeout", 5000L);

        Long setNxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(lockTimeout + Clock.systemDefaultZone().millis()));

        //返回1表示成功
        if (setNxResult != null && setNxResult.intValue() == 1) {
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获得分布式锁{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("关闭订单定时任务结束");
    }

    /**
     * 防死锁分布式锁
     */
    public void closeOrderTaskV3() {
        log.info("关闭订单定时任务开始");
        Long lockTimeout = PropertiesUtil.getPropertyToLong("lock.timeout", 5000L);

        Long setNxResult = RedisShardedPoolUtil.setNx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(lockTimeout + Clock.systemDefaultZone().millis()));

        //返回1表示成功
        if (setNxResult != null && setNxResult.intValue() == 1) {
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            String lockValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

            //如果lockValue不是空,并且当前时间大于锁的有效期,说明之前的lock的时间已超时,执行getSet命令.
            if (lockValueStr != null && Clock.systemDefaultZone().millis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisShardedPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(lockTimeout + Clock.systemDefaultZone().millis()));
                //再次用当前时间戳getSet，
                //返回给定 key 的旧值。旧值判断，是否可以获取锁
                // 当 key 没有旧值时，即 key 不存在时，返回 nil 。 ->获取锁
                //这里我们set了一个新的value值，获取旧的值。
                boolean lockValueAndGetSetResultEquals = getSetResult != null && StringUtils.equals(lockValueStr, getSetResult);
                if (getSetResult == null || lockValueAndGetSetResultEquals) {
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("关闭订单定时任务结束");
    }

    private void closeOrder(String lockName) {
        //expire命令用于给该锁设定一个过期时间，用于防止线程crash，导致锁一直有效，从而导致死锁。
        //有效期50秒,防死锁
        Long expireResult = RedisShardedPoolUtil.expire(lockName, 5);
        if (expireResult != null && expireResult.intValue() == 1) {
            log.info("获取{},ThreadName{}", lockName, Thread.currentThread().getName());
            Integer hour = PropertiesUtil.getPropertyToInteger("close.order.task.time.hour", 2);
            //iOrderService.closeOrder(hour);
            RedisShardedPoolUtil.del(lockName);
            log.info("释放{},ThreadName{}", lockName, Thread.currentThread().getName());
        } else {
            log.info("CLOSE_ORDER_TASK_LOCK设置时间失败");
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrderTaskV4() {
        RLock rLock = redissonManager.getRedisson().getLock(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        boolean getLock = false;
        int waitTime = 0;
        int leaseTime = 50;
        try {
            if (getLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                log.info("Redisson获取分布式锁{},ThreadName{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
                Integer hour = PropertiesUtil.getPropertyToInteger("close.order.task.time.hour", 2);
                //iOrderService.closeOrder(hour);
            } else {
                log.info("Redisson没有获取分布式锁{},ThreadName{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
            }
        } catch (InterruptedException e) {
            log.error("Redisson获取分布式锁获取异常{}", e);
        } finally {
            if (!getLock) {
                return;
            }
            rLock.unlock();
            log.info("Redisson分布式锁释放");
        }
    }
}
