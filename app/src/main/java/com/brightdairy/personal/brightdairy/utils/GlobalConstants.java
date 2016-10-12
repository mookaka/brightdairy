package com.brightdairy.personal.brightdairy.utils;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.brightdairy.personal.brightdairy.activity.MainActivity;
import com.brightdairy.personal.model.entity.ShopCart;

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
    public static String ZONE_CODE = "CN-3101";


    public static String IMG_URL_BASE;
    public static String PRODUCT_DETAIL_IMG_URL_BASE = "http://img.4008117117.com/imgs/";
    public static String PRODUCT_DETAIL_SUFIXX = ".jpg";

    public interface AppConfig
    {
        String FAA_KEY = "a5b1827ba615422b97f73e07dc9c602d";
        String UID_LOCAL = "userLoginIdLocal";
        String TID_LOCAL = "tidLocal";
        String PID_LOCAL = "pidLocal";
        String CITY_CODE_CACHE = "cityCodeCache";
    }


    private static AMapLocationClient mAMapLocationClient;

    public static AMapLocationClient getGlobalLocationClient()
    {
        if (mAMapLocationClient == null)
        {
            mAMapLocationClient = new AMapLocationClient(APPLICATION_CONTEXT);
            AMapLocationClientOption mapLocationClientOption = new AMapLocationClientOption();
            mapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
            mapLocationClientOption.setOnceLocation(true);
            mapLocationClientOption.setOnceLocationLatest(true);
            mapLocationClientOption.setNeedAddress(true);
            mapLocationClientOption.setWifiActiveScan(true);
            mAMapLocationClient.setLocationOption(mapLocationClientOption);
        }
        return mAMapLocationClient;
    }

    //flag for intent which to startActivityForResult or intent which has extra data
    public interface INTENT_FLAG
    {
        int RELOGIN_REQ_FLG = 0x001;
        int RELOGIN_OK_FLG = 0x002;

        String NEED_RELOGIN = "relogin";
    }

}
