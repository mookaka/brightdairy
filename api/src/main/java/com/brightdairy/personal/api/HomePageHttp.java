package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.HomeConfig;
import com.brightdairy.personal.model.entity.HomeContent;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/3.
 */
public interface HomePageHttp
{
    @GET("homepage/content/{cityCode}")
    Observable<DataResult<HomeContent>> getHomeContent(@Path("cityCode") String cityCode);

    @GET("homepage/conconfig/{cityCode}")
    Observable<DataResult<HomeConfig>> getHomeConfigByZone(@Path("cityCode") String cityCode);
}
