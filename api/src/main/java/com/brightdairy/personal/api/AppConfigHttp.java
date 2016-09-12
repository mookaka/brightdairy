package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/19.
 */
public interface AppConfigHttp
{
    @POST("commonStore/getPid")
    Observable<DataResult<String>> getAppPid(@Body String oldPid);

    @GET("commonStore/getImageUrl")
    Observable<DataResult<String>> getImgBaseUrl(@Header("rid") String headerRid);

    @GET("geoStore/getAllCities")
    Observable<DataResult<ArrayList<JSONObject>>> getCurrCityCode();
}
