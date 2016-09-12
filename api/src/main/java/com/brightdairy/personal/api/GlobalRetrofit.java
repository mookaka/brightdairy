package com.brightdairy.personal.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public class GlobalRetrofit
{
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://172.16.11.25:3000/api/";


    public static Retrofit getRetrofitInstance()
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }


    private static Retrofit retrofitDev;
    private static final String BASE_URL_DEV = "http://172.16.4.58:8180/api/";

    public static Retrofit getRetrofitDev()
    {
        if (retrofitDev == null)
        {
            OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor()
            {
                @Override
                public Response intercept(Chain chain) throws IOException
                {
                    Request originalReq = chain.request();

                    Request.Builder newReqBuilder = originalReq.newBuilder()
                            .addHeader("pid", GlobalHttpConfig.PID)
                            .addHeader("uid", GlobalHttpConfig.UID)
                            .addHeader("rid", GlobalHttpConfig.RID)
                            .addHeader("pin", GlobalHttpConfig.PIN)
                            .addHeader("tid", GlobalHttpConfig.TID);

                    return chain.proceed(newReqBuilder.build());
                }
            }).build();

            retrofitDev = new Retrofit.Builder().client(httpClient)
                    .baseUrl(BASE_URL_DEV)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        return retrofitDev;
    }
}
