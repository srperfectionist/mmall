package com.mmall.dao;

import com.mmall.pojo.Category;

import java.util.List;

/**
 * @author SR
 * @date 2017/11/22
 */
public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectChildrenParallelCategory(Integer categoryId);
}