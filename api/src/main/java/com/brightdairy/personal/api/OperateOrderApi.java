package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.ConfirmOrder;
import com.brightdairy.personal.model.HttpReqBody.QuikBuyInfo;
import com.brightdairy.personal.model.entity.ConfirmOrderInfos;
import com.brightdairy.personal.model.entity.ProductSendInfo;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/9/22.
 */
public interface OperateOrderApi
{
    @POST("productStore/confirmInfo")
    @Headers("Content Type: application/json")
    Observable<DataResult<ConfirmOrderInfos>> getConfirmOrderInfo(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                                         @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                                         @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                                         @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                                         @Body ConfirmOrder confirmOrder);

    @POST("productStore/quickBuyInfo/{cityCode}")
    @Headers("Content Type: application/json")
    Observable<DataResult<ConfirmOrderInfos>> quickBuyInfo(@Path("cityCode") String cityCode,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                           @Body ProductSendInfo quikBuyInfo);



}
