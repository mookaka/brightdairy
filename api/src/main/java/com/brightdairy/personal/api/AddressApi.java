package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.DeleteAddress;
import com.brightdairy.personal.model.HttpReqBody.NewAddress;
import com.brightdairy.personal.model.HttpReqBody.UpdateAddress;
import com.brightdairy.personal.model.entity.AddressInfo;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/9/14.
 */
public interface AddressApi
{
    @GET("userStore/getShipAddressList/{cityCode}/{addInfo}")
    Observable<DataResult<ArrayList<AddressInfo>>> getShipAddressList(
            @Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
            @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
            @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
            @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
            @Path("cityCode") String cityCode, @Path("addInfo") String addInfo);

    @POST("userStore/createShipAddress")
    Observable<DataResult<Object>> createShipAddress(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                     @Body NewAddress newAddress);

    @POST("userStore/updateShipAddress")
    Observable<DataResult<Object>> updateShipAddress(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                     @Body UpdateAddress updateAddress);

    @POST("userStore/deleteShipAddress")
    Observable<DataResult<Object>> deleteShipAddress(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                     @Body DeleteAddress deleteAddress);
}
