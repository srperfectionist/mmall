package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author SR
 * @date 2017/11/28
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    /**
     * 添加
     *
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(Integer productId, Integer count, HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    /**
     * 更新
     *
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(Integer productId, Integer count, HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    /**
     * 删除
     *
     * @param productIds
     * @param request
     * @return
     */
    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse<CartVo> deleteProduct(String productIds, HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }

    /**
     * 列表
     *
     * @param request
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.list(user.getId());
    }

    /**
     * 全选
     *
     * @param request
     * @return
     */
    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    /**
     * 不全选
     *
     * @param request
     * @return
     */
    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    /**
     * 单选
     *
     * @param request
     * @param productId
     * @return
     */
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    /**
     * 不单选
     *
     * @param request
     * @param productId
     * @return
     */
    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iCartService.selectOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    /**
     * 获取购物车商品数量
     *
     * @param request
     * @return
     */
    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}
