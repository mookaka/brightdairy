package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.entity.LaunchPage;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public interface LaunchPageHttp
{
    @GET("lanch/lanchpage")
    Observable<DataBase<LaunchPage>> getLaunchPageConfig();
}
