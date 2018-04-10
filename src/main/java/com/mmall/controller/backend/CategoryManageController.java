package com.mmall.controller.backend;

import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author SR
 * @date 2017/11/22
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do")
    @ResponseBody
    public ServerResponse<String> addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 更新品类名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse<String> setCategoryName(Integer categoryId, String categoryName) {
        return iCategoryService.updateCategoryName(categoryId, categoryName);
    }

    /**
     * 查询子节点平级分类 不递归
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //不递归
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }

    /**
     * 查询子节点平级分类 递归
     *
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenParallelCategory(@RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        //递归
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
    }
}