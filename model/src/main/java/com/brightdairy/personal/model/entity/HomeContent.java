package com.brightdairy.personal.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by shuangmusuihua on 2016/7/27.
 */
public class HomeContent
{
    @Expose(deserialize = true,serialize = true)
    public String activity;
    @Expose(deserialize = false,serialize = false)
    public String shoppingCart;
    @Expose(deserialize = true,serialize = false)
    public String orderCenter;
    @Expose(deserialize = false,serialize = false)
    public CarouselItem[] carousel;
    @Expose(deserialize = true,serialize = false)
    public HomePageItem[] homepageItems;

}
