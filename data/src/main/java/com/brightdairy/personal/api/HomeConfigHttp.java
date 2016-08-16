package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataListBase;
import com.brightdairy.personal.model.entity.HomeConfig;
import com.brightdairy.personal.model.entity.HomeContent;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/7/29.
 */
public interface HomeConfigHttp
{
    @GET("home/homeconfig")
    Observable<DataBase<HomeConfig>> getHomeConfigByZone(@Query("zonecode") String zCode);
}
