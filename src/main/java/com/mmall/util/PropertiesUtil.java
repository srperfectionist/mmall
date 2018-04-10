package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

/**
 * @author SR
 * @date 2017/11/22
 */
@Slf4j
public class PropertiesUtil {
    private static Configuration configuration;

    static {
        String fileName = "mmall.properties";
        try {
            configuration = new PropertiesConfiguration(fileName);
        } catch (ConfigurationException e) {
            log.error("配置文件读取异常", e);
        }
    }

    public static String getPropertyToString(String key) {
        String value = configuration.getString(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static int getPropertyToInt(String key) {
        return configuration.getInt(key);
    }

    public static String getPropertyToString(String key, String defaultValue) {
        String value = configuration.getString(key.trim());
        if (StringUtils.isBlank(value)) {
            value = defaultValue;
        }
        return value.trim();
    }

    public static Integer getPropertyToInteger(String key, Integer defaultValue) {
        Integer value = configuration.getInteger(key, defaultValue);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static Long getPropertyToLong(String key, Long defaultValue) {
        Long value = configuration.getLong(key, defaultValue);
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public static boolean getPropertyToBoolean(String key, boolean defaultValue) {
        boolean value = configuration.getBoolean(key, defaultValue);
        return value;
    }
}
