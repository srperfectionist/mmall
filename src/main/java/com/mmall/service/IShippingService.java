package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

/**
 * @author SR
 * @date 2017/12/1
 */
public interface IShippingService {
    /**
     * 新建地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse add(Integer userId, Shipping shipping);

    /**
     * 删除地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<String> del(Integer userId, Integer shippingId);

    /**
     * 更新地址
     *
     * @param userId
     * @param shipping
     * @return
     */
    ServerResponse<String> update(Integer userId, Shipping shipping);

    /**
     * 查询地址
     *
     * @param userId
     * @param shippingId
     * @return
     */
    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    /**
     * 查询地址列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
