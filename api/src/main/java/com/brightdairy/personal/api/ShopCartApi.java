package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.OperateCartItem;
import com.brightdairy.personal.model.HttpReqBody.UpdateSendInfo;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.brightdairy.personal.model.entity.ShopCart;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/9/21.
 */
public interface ShopCartApi
{
    @GET("cart/getCartInfo")
    @Headers("ContentType: application/json")
    Observable<DataResult<ShopCart>> getCartInfo(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                 @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin);

    @POST("cart/addItem")
    @Headers("ContentType: application/json")
    Observable<DataResult<Object>> addCartItem(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                               @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                               @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                               @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                               @Body ProductSendInfo productSendInfo);

    @POST("cart/deleteItem")
    @Headers("ContentType: application/json")
    Observable<DataResult<Object>> deleteCartItem(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                  @Body OperateCartItem itemSeqId);

    @POST("cart/selectItem")
    @Headers("ContentType: application/json")
    Observable<DataResult<Object>> selectCartItem(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                  @Body OperateCartItem itemSeqId);

    @POST("cart/unselectItem")
    @Headers("ContentType: application/json")
    Observable<DataResult<Object>> unselectCartItem(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                  @Body OperateCartItem itemSeqId);


    @POST("cart/modifyItem")
    @Headers("ContentType: application/json")
    Observable<DataResult<Object>> modifyCartItem(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                    @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                    @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                    @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                    @Body UpdateSendInfo updateSendInfo);


}
