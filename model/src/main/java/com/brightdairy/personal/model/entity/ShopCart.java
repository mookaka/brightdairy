package com.brightdairy.personal.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangmusuihua on 2016/9/21.
 */
public class ShopCart
{

    public String cartAmount;
    public int cartNum;
    public ArrayList<ItemsBean> items;
    public ArrayList<Object> adjustments;

    public static class ItemsBean
    {
        public String supplierName;
        public String supplierSelected;
        public String supplierId;
        public ArrayList<CartItem> cartItemList;
    }

}
