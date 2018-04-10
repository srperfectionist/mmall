package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;

/**
 * @author SR
 * @date 2017/11/28
 */
public interface ICartService {
    /**
     * 添加
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    /**
     * 更新
     *
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    /**
     * 删除
     *
     * @param userId
     * @param productIds
     * @return
     */
    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);

    /**
     * 列表
     *
     * @param userId
     * @return
     */
    ServerResponse<CartVo> list(Integer userId);

    /**
     * 全选或不全选
     *
     * @param userId
     * @param productId
     * @param checked
     * @return
     */
    ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer productId, Integer checked);

    /**
     * 获取购物车商品数量
     *
     * @param userId
     * @return
     */
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
