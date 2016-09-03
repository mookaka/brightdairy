package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.http.GET;
import retrofit2.http.Header;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/19.
 */
public interface AppConfigHttp
{
    @GET("commonStore/getPid")
    Observable<DataResult<String>> getAppPid();

    @GET("commonStore/getImageUrl")
    Observable<DataResult<String>> getImgBaseUrl(@Header("rid") String headerRid);

    @GET("geoStore/getAllCities")
    Observable<DataResult<ArrayList<JSONObject>>> getCurrCityCode();
}
