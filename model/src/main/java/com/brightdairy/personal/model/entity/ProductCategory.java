package com.brightdairy.personal.model.entity;

import java.util.ArrayList;
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
    public ArrayList<ProductCategory> childCategoryList;
    public ArrayList<ProductInfo> productList;
    public ArrayList<Object> proList;
}
