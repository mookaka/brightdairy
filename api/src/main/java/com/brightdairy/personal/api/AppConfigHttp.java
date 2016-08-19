package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/8/19.
 */
public interface AppConfigHttp
{
    @GET("commonStore/getPid")
    Observable<DataResult<String>> getAppPid();
}
