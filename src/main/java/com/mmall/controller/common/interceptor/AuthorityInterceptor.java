package com.mmall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author SR
 * @date 2018/1/22
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        List<String> classNameList = Arrays.asList("UserManageController","ProductManageController");
        List<String> methodNameList = Arrays.asList("login","richTextImgUpload");

        StringBuffer requestParamBuffer = new StringBuffer();
        Map<String, String[]> paramMap = request.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> entry = iterator.next();
            String mapKey = entry.getKey();

            String mapValue = StringUtils.EMPTY;

            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                mapValue = Arrays.toString((String[]) obj);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        if (StringUtils.equals(className, classNameList.get(0)) && StringUtils.equals(methodName, methodNameList.get(0))) {
            log.info("拦截器拦截,className:{},methodName:{}", className, methodName);
            return true;
        }

        log.info("拦截器拦截,className:{},methodName:{},param:{}", className, methodName, requestParamBuffer.toString());

        User user = null;
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(userLoginToken)) {
            // 从redis获取登录用户json
            String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
            // 将userJsonStr转换成User对象
            user = JsonUtil.stringToObject(userJsonStr, User.class);
        }

        if (user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)) {
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter printWriter = response.getWriter();

            if (user == null) {
                if (StringUtils.equals(className, classNameList.get(1)) && StringUtils.equals(methodName, methodNameList.get(1))) {
                    log.info("拦截器拦截,className:{},methodName:{}", className, methodName);
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户未登录，请用管理员登录");
                    printWriter.write(JsonUtil.objToString(resultMap));
                }else{
                    printWriter.write(JsonUtil.objToString(ServerResponse.createByErrorMessage("用户未登录")));
                }

            } else {
                if (StringUtils.equals(className, classNameList.get(1)) && StringUtils.equals(methodName, methodNameList.get(1))) {
                    log.info("拦截器拦截,className:{},methodName:{}", className, methodName);
                    Map<String, Object> resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户不是管理员");
                    printWriter.write(JsonUtil.objToString(resultMap));
                }else{
                    printWriter.write(JsonUtil.objToString(ServerResponse.createByErrorMessage("用户不是管理员")));
                }
            }
            printWriter.flush();
            printWriter.close();

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
