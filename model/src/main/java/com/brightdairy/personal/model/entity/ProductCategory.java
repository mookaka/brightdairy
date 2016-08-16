package com.brightdairy.personal.model.entity;

import java.util.List;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class ProductCategory
{

    public String categoryId;
    public String categoryName;
    public String categoryType;
    public String categoryIcon;
    public List<ProductCategory> childCategoryList;
    public List<ProductInfo> productList;
    public List<Object> proList;
}
