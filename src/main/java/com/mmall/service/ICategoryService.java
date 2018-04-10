package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author SR
 * @date 2017/11/22
 */
public interface ICategoryService {
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
