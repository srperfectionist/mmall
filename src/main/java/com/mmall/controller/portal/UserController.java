package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author SR
 * @date 2017/11/24
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param response
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpServletResponse response, HttpSession session) {
        ServerResponse<User> serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()) {
            // 将sessionId写入cookie
            CookieUtil.writeLoginToken(response, session.getId());
            // 将登录用户的json写入redis
            RedisShardedPoolUtil.setEx(session.getId(), Const.RedisCacheExTime.REDIS_CACHE_EXTIME, JsonUtil.objToString(serverResponse.getData()));
        }
        return serverResponse;
    }

    /**
     * 用户登出
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从cookie中删除登录用户的token
        CookieUtil.delLoginToken(request, response);
        // 从redis中删除登录用户的json
        RedisShardedPoolUtil.del(userLoginToken);
        return ServerResponse.createBySuccess();
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 用户注册校验
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户登录信息");
    }

    /**
     * 获取用户密码问题
     *
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 用户找回问题答案校验
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 用户密码重置
     *
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 用户登录修改密码
     *
     * @param request
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpServletRequest request, String passwordOld, String passwordNew) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(user, passwordOld, passwordNew);
    }

    /**
     * 更新个人信息
     *
     * @param request
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpServletRequest request, User user) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User currentUser = JsonUtil.stringToObject(userJsonStr, User.class);

        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());

        ServerResponse<User> userServerResponse = iUserService.updateInformation(user);
        if (userServerResponse.isSuccess()) {
            userServerResponse.getData().setUsername(currentUser.getUsername());
            RedisShardedPoolUtil.setEx(userLoginToken, Const.RedisCacheExTime.REDIS_CACHE_EXTIME, JsonUtil.objToString(userServerResponse.getData()));
        }
        return userServerResponse;
    }

    /**
     * 获取登录用户个人信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User currentUser = JsonUtil.stringToObject(userJsonStr, User.class);

        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
