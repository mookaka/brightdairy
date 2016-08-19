package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.entity.LaunchAd;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public interface AdPageHttp
{
    @GET("lanch/lanchad")
    Observable<DataBase<LaunchAd>> getLaunchPageConfig();
}
