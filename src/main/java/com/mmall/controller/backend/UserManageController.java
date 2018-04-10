package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SR
 * @date 2017/11/22
 */
@Controller
@RequestMapping("/manage/user/")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员用户登录
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response) {
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        if (userServerResponse.isSuccess()) {
            User user = userServerResponse.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                // 将sessionId写入cookie
                CookieUtil.writeLoginToken(response, session.getId());
                // 将登录用户的json写入redis
                RedisShardedPoolUtil.setEx(session.getId(), Const.RedisCacheExTime.REDIS_CACHE_EXTIME, JsonUtil.objToString(userServerResponse.getData()));
                return userServerResponse;
            } else {
                return ServerResponse.createByErrorMessage("不是管理员，不能登录");
            }
        }
        return userServerResponse;
    }
}