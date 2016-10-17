package com.brightdairy.personal.api;

import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.ModifyPwdBySms;
import com.brightdairy.personal.model.HttpReqBody.SendSms;
import com.brightdairy.personal.model.HttpReqBody.UserLogin;
import com.brightdairy.personal.model.HttpReqBody.UserLoginBySms;
import com.brightdairy.personal.model.HttpReqBody.UserRegister;
import com.brightdairy.personal.model.entity.LoginResult;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public interface LoginRegisterApi
{
    @POST("sms/sendSms")
    @Headers("Content Type: application/json")
    Observable<DataResult<Object>> sendSms(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                           @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                           @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                           @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                           @Body SendSms sendSmsBody);

    @POST("sms/getSmsCodeForReg")
    @Headers("Content Type: application/json")
    Observable<DataResult<Object>> sendSmsCodeForReg(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                     @Body SendSms sendSmsBody);

    @POST("user/userLogin")
    @Headers("Content Type: application/json")
    Observable<DataResult<LoginResult>> userLogin(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                  @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                  @Body UserLogin userLogin);

    @POST("user/userLoginBySms")
    @Headers("Content Type: application/json")
    Observable<DataResult<LoginResult>> userLoginBySms(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                       @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                       @Body UserLoginBySms userLoginBySms);

    @GET("user/logout")
    @Headers("Content Type: application/json")
    Observable<DataResult<Object>> userLogout();

    @POST("user/createCustomer")
    @Headers("Content Type: application/json")
    Observable<DataResult<LoginResult>> userRegister(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                     @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                     @Body UserRegister userRegister);

    @GET("user/modifyPasswordBySmsCode")
    @Headers("Content Type: application/json")
    Observable<DataResult<Object>> modifyPasswordBySmsCode(@Header(GlobalHttpConfig.HTTP_HEADER.PID) String pid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.UID) String uid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.TID) String tid,
                                                           @Header(GlobalHttpConfig.HTTP_HEADER.PIN) String pin,
                                                           @Body ModifyPwdBySms modifyPwdBySms);

}
