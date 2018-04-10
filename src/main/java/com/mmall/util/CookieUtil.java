package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author SR
 * @date 2017/12/29
 */
@Slf4j
public class CookieUtil {

    /**
     * cookie的domain
     */
    private final static String COOKIE_DOMAIN = ".happymmall.com";

    /**
     * cookie的名字
     */
    private final static String COOKIE_NAME = "mmall_login_token";

    /**
     * 登录用户信息写入cookie
     *
     * @param response
     * @param value
     */
    public static void writeLoginToken(HttpServletResponse response, String value) {
        Cookie cookie = new Cookie(COOKIE_NAME, value);
        cookie.setDomain(COOKIE_DOMAIN);
        // 设置在根目录
        cookie.setPath("/");
        // 不允许通过脚本访问cookie
        cookie.setHttpOnly(true);
        /*
            1:单位时秒
            2:设置-1,为永久
            3:如果不设置,cookie不会写入硬盘,而是写在内存,仅在当前页面有效
         */
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    /**
     * 读取登录用户cookie
     *
     * @param request
     * @return
     */
    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                log.info("return cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 删除登录用户cookie
     *
     * @param request
     * @param response
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                cookie.setDomain(COOKIE_DOMAIN);
                // 根目录
                cookie.setPath("/");
                // 不允许通过脚本访问cookie
                cookie.setHttpOnly(true);
                // 0代表删除
                cookie.setMaxAge(0);
                log.info("del cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                response.addCookie(cookie);
                return;
            }
        }
    }
}
