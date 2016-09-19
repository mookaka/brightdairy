package com.brightdairy.personal.brightdairy.utils;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import com.brightdairy.personal.brightdairy.activity.MainActivity;

import retrofit2.http.PUT;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public class GlobalConstants
{

    public interface PageRelatedType
    {
        String PAGE = "page";
        String HTML5 = "H5";
        String CAROUSEL = "carousel";
        String SINGLE_PAGE = "single";

        int HOME_PAGE = 0;
        int CATEGORY_PAGE = 1;
        int ACTIVITY_PAGE = 2;
        int ORDER_CENTER_PAGE = 3;
        int USER_PAGE = 4;
    }

    public static Context APPLICATION_CONTEXT;
    public static MainActivity HOME_MANAGER;

    public static String ALL_CATEGORY = "all category";


    public static String CURR_ZONE_CN_NAME = "上海市";
    public static String ZONE_CODE = "CN-320600";


    public static String IMG_URL_BASR;
    public static String PRODUCT_DETAIL_IMG_URL_BASE = "http://img.4008117117.com/imgs/";
    public static String PRODUCT_DETAIL_SUFIXX = ".jpg";

    public interface AppConfig
    {
        String FAA_KEY = "a5b1827ba615422b97f73e07dc9c602d";
        String UID_LOCAL = "userLoginIdLocal";
        String TID_LOCAL = "tidLocal";
        String PID_LOCAL = "pidLocal";
    }


}
