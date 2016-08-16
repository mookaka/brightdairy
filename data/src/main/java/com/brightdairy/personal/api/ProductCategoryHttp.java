package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductCategory;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public interface ProductCategoryHttp
{
    @GET("productStore/getSubCategoryList/bdroot/{zonecode}")
    Observable<DataResult<ProductCategory>> getSubCategoryList(@Query("zonecode") String zonecode);
}
