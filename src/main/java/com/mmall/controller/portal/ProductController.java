package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Order;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author SR
 * @date 2017/11/24
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 产品详情
     *
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId) {
        return iProductService.getProductDetail(productId);
    }

    /**
     * 产品详情 RESTful
     *
     * @param productId
     * @return
     */
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVo> detailRESTful(@PathVariable Integer productId) {
        return iProductService.getProductDetail(productId);
    }

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
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy,
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iProductService.getProductByKeywordCategory(keyword, categoryId, orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/{keyword}/{categoryId}/{orderBy}/{pageNum}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "keyword") String keyword,
                                                @PathVariable(value = "categoryId") Integer categoryId,
                                                @PathVariable(value = "orderBy") String orderBy,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize) {
        if (pageNum == null){
            pageNum = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_asc";
        }
        return iProductService.getProductByKeywordCategory(keyword, categoryId, orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/keyword/{keyword}/{orderBy}/{pageNum}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "keyword") String keyword,
                                                @PathVariable(value = "orderBy") String orderBy,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize) {
        if (pageNum == null){
            pageNum = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_asc";
        }
        return iProductService.getProductByKeywordCategory(keyword, null, orderBy, pageNum, pageSize);
    }

    @RequestMapping(value = "/category/{categoryId}/{orderBy}/{pageNum}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listRESTful(@PathVariable(value = "categoryId") Integer categoryId,
                                                @PathVariable(value = "orderBy") String orderBy,
                                                @PathVariable(value = "pageNum") Integer pageNum,
                                                @PathVariable(value = "pageSize") Integer pageSize) {
        if (pageNum == null){
            pageNum = 1;
        }
        if (pageSize == null){
            pageSize = 10;
        }
        if (StringUtils.isBlank(orderBy)){
            orderBy = "price_asc";
        }
        return iProductService.getProductByKeywordCategory("", categoryId, orderBy, pageNum, pageSize);
    }
}
