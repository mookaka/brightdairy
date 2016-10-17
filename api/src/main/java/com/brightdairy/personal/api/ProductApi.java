package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductCategory;
import com.brightdairy.personal.model.entity.ProductDetail;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public interface ProductApi
{
    @GET("productStore/getProductById/{productId}")
    Observable<DataResult<ProductDetail>> getProductDetailById(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                               @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                               @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                               @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                               @Path("productId") String productId);

    @GET("productStore/getSubCategoryList/bdroot/{zonecode}")
    Observable<Result<DataResult<ProductCategory>>> getSubCategoryList(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                                       @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                                       @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                                       @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                                       @Path("zonecode") String zonecode);
}
