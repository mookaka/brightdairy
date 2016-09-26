package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.GetPid;
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
    @Headers("Content Type: application/json")
    Observable<DataResult<String>> getAppPid(
            @Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
            @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
            @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
            @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
            @Body GetPid oldPid);

    @GET("commonStore/getImageUrl")
    Observable<DataResult<String>> getImgBaseUrl(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin);

    @GET("geoStore/getAllCity")
    Observable<DataResult<ArrayList<CityZoneCode>>> getCurrCityCode(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                                    @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                                    @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                                    @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin);

    @GET("productStore/getDeliverMethodList")
    @Headers("Content Type: application/json")
    Observable<DataResult<ArrayList<DeliverMethod>>> getDeliverMethodList(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                                          @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                                          @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                                          @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin);

    @GET("geoStore/getGeoBySupplierPartyId/{supplierPartyId}")
    @Headers("Content Type: application/json")
    Observable<DataResult<DeliverZone>> getGeoInfoChn(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                      @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                      @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                      @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,@Path("supplierPartyId") String supplierPartyId);
}
