package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.LaunchAd;
import com.brightdairy.personal.model.entity.LaunchPage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public interface LaunchHttp
{
    @GET("launch/img/{cityCode}")
    Observable<DataResult<LaunchPage>> getLaunchPageConfig(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                           @Path("cityCode") String citycode);

    @GET("launch/ad/{cityCode}")
    Observable<DataResult<LaunchAd>> getLaunchAdConfig(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                       @Path("cityCode") String citycode);
}
