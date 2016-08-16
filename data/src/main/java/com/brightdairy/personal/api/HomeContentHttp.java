package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.entity.HomeContent;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/3.
 */
public interface HomeContentHttp
{
    @GET("home/homecontent")
    Observable<DataBase<HomeContent>> getHomeContent(@Query("zonecode") String zCode, @Query("usertoken") String usrToken);
}
