package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.impl.ShippingServiceImpl;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author SR
 * @date 2017/12/1
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private ShippingServiceImpl shippingService;

    /**
     * 新建地址
     *
     * @param request
     * @param shipping
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpServletRequest request, Shipping shipping) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.add(user.getId(), shipping);
    }

    /**
     * 删除地址
     *
     * @param request
     * @param shippingId
     * @return
     */
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse<String> del(HttpServletRequest request, Integer shippingId) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.del(user.getId(), shippingId);
    }

    /**
     * 更新地址
     *
     * @param request
     * @param shipping
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<String> update(HttpServletRequest request, Shipping shipping) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.update(user.getId(), shipping);
    }

    /**
     * 查询地址
     *
     * @param request
     * @param shippingId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpServletRequest request, Integer shippingId) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.select(user.getId(), shippingId);
    }

    /**
     * 查询地址列表
     *
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                         HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
