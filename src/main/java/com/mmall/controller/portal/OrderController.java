package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.RedisShardedPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author SR
 * @date 2017/12/9
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     * 创建订单
     *
     * @param request
     * @param shippingId
     * @return
     */
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, Integer shippingId) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.crateOrder(user.getId(), shippingId);
    }

    /**
     * 取消订单
     *
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request, Long orderNo) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    /**
     * 获取购物车商品信息
     *
     * @param request
     * @return
     */
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    /**
     * 获取订单详情
     *
     * @param request
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(), orderNo);
    }

    /**
     * list
     *
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }

    /**
     * pay
     *
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpServletRequest request, Long orderNo) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, path);
    }

    /**
     * alipay_callback
     *
     * @param request
     * @return
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipay_callback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Iterator iterator = parameterMap.keySet().iterator(); iterator.hasNext(); ) {
            String name = (String) iterator.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}", request.getParameter("sign"), request.getParameter("trade_status"), params);

        //验证回调的正确性
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());

            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求，验证不通过");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        //todo 验证数据

        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if (serverResponse.isSuccess()) {
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }

    /**
     * queryOrderPayStatus
     *
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse queryOrderPayStatus(HttpServletRequest request, Long orderNo) {
        // 从cookie获取登录用户的token
        String userLoginToken = CookieUtil.readLoginToken(request);
        // 从redis获取登录用户json
        String userJsonStr = RedisShardedPoolUtil.get(userLoginToken);
        // 将userJsonStr转换成User对象
        User user = JsonUtil.stringToObject(userJsonStr, User.class);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }
}
