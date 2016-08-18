package com.brightdairy.personal.model.entity;

/**
 * Created by shuangmusuihua on 2016/8/18.
 */
public class CategoryForTitle
{
    public String categoryId;
    public String categoryName;
    public String categoryIcon;

    public CategoryForTitle(String categoryIcon, String categoryId, String categoryName)
    {
        this.categoryIcon = categoryIcon;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
