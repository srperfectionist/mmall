package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * @author SR
 * @date 2017/11/22
 */
public interface IProductService {
    /**
     * 新增或更新产品
     *
     * @param product
     * @return
     */
    ServerResponse<String> saveOrUpdateProduct(Product product);

    /**
     * 修改产品销售状态
     *
     * @param productId
     * @param status
     * @return
     */
    ServerResponse<String> setSalesStatus(Integer productId, Integer status);

    /**
     * 获取产品详情
     *
     * @param productId
     * @return
     */
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    /**
     * 获取产品列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse getProductList(Integer pageNum, Integer pageSize);

    /**
     * 根据条件查询产品列表
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, Integer pageNum, Integer pageSize);

    /**
     * 产品详情
     * @param productId
     * @return
     */ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    /**
     * 根据关键字和分来ID查询
     *
     * @param keyword
     * @param categoryId
     * @param orderBy
     * @param pageNum
     * @param pageSize
     * @return
     */
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword, Integer categoryId, String orderBy, Integer pageNum, Integer pageSize);
}
