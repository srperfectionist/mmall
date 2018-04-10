package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author SR
 * @date 2017/11/22
 */
@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;


    /**
     * 新增或更新产品
     *
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse<String> productSave(Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * 更新产品状态
     *
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sales_status.do")
    @ResponseBody
    public ServerResponse<String> setSalesStatus(Integer productId, Integer status) {
        return iProductService.setSalesStatus(productId, status);
    }

    /**
     * 获取产品详情
     *
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(Integer productId) {
        return iProductService.manageProductDetail(productId);
    }

    /**
     * 获取产品列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    /**
     * 根据条件查询产品列表
     *
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse productSearch(String productName, Integer productId, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    /**
     * SpringMVC
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(HttpServletRequest request, @RequestParam(value = "upload_file", required = false) MultipartFile file) {
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getPropertyToString("ftp.server.http.prefix") + targetFileName;

        Map<String, String> fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);

        return ServerResponse.createBySuccess(fileMap);
    }

    /**
     * 富文本
     *
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("rich_text_img_upload.do")
    @ResponseBody
    public Map richTextImgUpload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = Maps.newHashMap();

        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(file, path);
        String url = PropertiesUtil.getPropertyToString("ftp.server.http.prefix") + targetFileName;

        if (StringUtils.isBlank(targetFileName)) {
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }

        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("file_path", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}