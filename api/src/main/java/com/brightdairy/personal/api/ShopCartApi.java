package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ShopCart;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
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
}
