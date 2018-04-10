package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author SR
 * @date 2018/3/8
 */
@Component
@Slf4j
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * redis1Ip
     */
    private static String redis1Ip = PropertiesUtil.getPropertyToString("redis1.ip");

    /**
     * redis1Port
     */
    private static Integer redis1Port = PropertiesUtil.getPropertyToInt("redis1.port");

    @PostConstruct
    public void init(){
        try {
            config.useSingleServer().setAddress(new StringBuilder().append(redis1Ip).append(":").append(redis1Port).toString());

            redisson = (Redisson) Redisson.create(config);

            log.info("初始化Redisson结束");
        } catch (Exception e) {
            log.error("初始化Redisson失败",e);
            e.printStackTrace();
        }
    }
}
