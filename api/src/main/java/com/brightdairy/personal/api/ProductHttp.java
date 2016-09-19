package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductCategory;
import com.brightdairy.personal.model.entity.ProductDetail;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public interface ProductHttp
{
    @GET("productStore/getProductById/{productId}")
    Observable<DataResult<ProductDetail>> getProductDetailById(@Path("productId") String productId);

    @GET("productStore/getSubCategoryList/bdroot/{zonecode}")
    Observable<Result<DataResult<ProductCategory>>> getSubCategoryList(@Path("zonecode") String zonecode);
}
