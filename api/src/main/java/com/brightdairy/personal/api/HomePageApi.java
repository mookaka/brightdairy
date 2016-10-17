package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.HomeConfig;
import com.brightdairy.personal.model.entity.HomeContent;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/3.
 */
public interface HomePageApi
{
    @GET("homepage/content/{cityCode}")
    Observable<DataResult<HomeContent>> getHomeContent(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                       @Path("cityCode") String cityCode);

    @GET("homepage/config/{cityCode}")
    Observable<DataResult<HomeConfig>> getHomeConfigByZone(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                           @Path("cityCode") String cityCode);
}
