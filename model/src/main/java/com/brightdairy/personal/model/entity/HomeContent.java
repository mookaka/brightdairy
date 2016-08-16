package com.brightdairy.personal.model.entity;

/**
 * Created by shuangmusuihua on 2016/7/27.
 */
public class HomeContent
{
    public String activity;
    public String shoppingCart;
    public OrderCenter orderCenter;
    public CarouselItem[] carousel;
    public HomePageItem[] homepageItems;

    public class OrderCenter
    {
        public int orderNum;
        public String orderInfo;
    }
}
