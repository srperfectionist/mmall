package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author SR
 * @date 2017/11/22
 */
@Service("iCategoryService")
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加分类
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("添加参数品类错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    /**
     * 更新分来名称
     *
     * @param categoryId
     * @param categoryName
     * @return
     */
    @Override
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新参数品类错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    /**
     * 查询子节点平级分类 不递归
     *
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectChildrenParallelCategory(categoryId);

        if (CollectionUtils.isEmpty(categoryList)) {
            log.error("未找到当前分类的子类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 查询子节点平级分类 递归
     *
     * @param categoryId
     * @return
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findCategory(categorySet, categoryId);

        List<Integer> integerList = Lists.newArrayList();

        for(Category categoryItem : categorySet){
            integerList.add(categoryItem.getId());
        }
        return ServerResponse.createBySuccess(integerList);
    }

    /**
     * 递归查询
     *
     * @param categorySet
     * @param categoryId
     * @return
     */
    private Set<Category> findCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectChildrenParallelCategory(categoryId);
        for (Category categoryItem : categoryList) {
            findCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
