package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * @author SR
 * @date 2017/12/9
 */
public interface IOrderService {
    /**
     * pay
     *
     * @param userId
     * @param orderNo
     * @param path
     * @return
     */
    ServerResponse pay(Integer userId, Long orderNo, String path);

    /**
     * aliCallback
     *
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String, String> params);

    /**
     * queryOrderPayStatus
     *
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId, Long orderNo);

    /**
     * 创建订单
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Object> crateOrder(Integer userId, Integer shippingId);

    /**
     * 取消订单
     *
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<String> cancel(Integer userId, Long orderNo);

    /**
     * 获取购物车商品信息
     *
     * @param userId
     * @return
     */
    ServerResponse getOrderCartProduct(Integer userId);

    /**
     * 获取订单详情
     *
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    /**
     * list
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    /**
     * manageList
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageList(int pageNum, int pageSize);

    /**
     * manageDetail
     *
     * @param orderNo
     * @return
     */
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    /**
     * search
     *
     * @param orderNo
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

    /**
     * sendGoods
     *
     * @param orderNo
     * @return
     */
    ServerResponse<String> manageSendGoods(Long orderNo);

    /**
     * 关闭订单
     *
     * @param hour
     */
    void closeOrder(int hour);
}
