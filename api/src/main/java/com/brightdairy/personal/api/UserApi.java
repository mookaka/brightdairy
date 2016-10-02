package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.UserInfoLite;


import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/10/1.
 */

public interface UserApi
{
    @GET("user/getMySXD/{cityCode}")
    Observable<DataResult<UserInfoLite>> getLiteUserInfo(@Path("cityCode") String cityCode,
                                                         @Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                         @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                         @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                         @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin);
}
