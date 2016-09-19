package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.CityZoneCode;
import com.brightdairy.personal.model.entity.DeliverMethod;
import com.brightdairy.personal.model.entity.DeliverZone;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
    Observable<DataResult<ArrayList<CityZoneCode>>> getCurrCityCode();

    @GET("productStore/getDeliverMethodList")
    @Headers("Content Type: application/json")
    Observable<DataResult<ArrayList<DeliverMethod>>> getDeliverMethodList();

    @GET("geoStore/getGeoBySupplierPartyId/{supplierPartyId}")
    @Headers("Content Type: application/json")
    Observable<DataResult<DeliverZone>> getGeoInfoChn(@Path("supplierPartyId") String supplierPartyId);
}
