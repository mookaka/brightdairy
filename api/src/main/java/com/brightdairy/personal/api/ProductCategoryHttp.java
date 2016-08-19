package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductCategory;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public interface ProductCategoryHttp
{
    @GET("productStore/getSubCategoryList/bdroot/{zonecode}")
    Observable<Result<DataResult<ProductCategory>>> getSubCategoryList(@Path("zonecode") String zonecode
                                                               , @Header("pid") String headerPid
                                                                , @Header("uid") String headerUid
                                                                , @Header("rid") String headerRid
                                                                , @Header("pin") String headerPin);
}
