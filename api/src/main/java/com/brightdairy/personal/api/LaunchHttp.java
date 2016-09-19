package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.LaunchAd;
import com.brightdairy.personal.model.entity.LaunchPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public interface LaunchHttp
{
    @GET("launch/img/{cityCode}")
    Observable<DataResult<LaunchPage>> getLaunchPageConfig(@Path("cityCode") String citycode);

    @GET("launch/ad/{cityCode}")
    Observable<DataResult<LaunchAd>> getLaunchAdConfig(@Path("cityCode") String citycode);
}
