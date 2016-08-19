package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.brightdairy.personal.api.AdPageHttp;
import com.brightdairy.personal.api.AppConfigHttp;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.LaunchPageHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.LaunchAd;
import com.brightdairy.personal.model.entity.LaunchPage;
import com.bumptech.glide.Glide;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuangmusuihua on 2016/7/27.
 */
public class LaunchPageActivity extends Activity implements LaunchPageI
{

    private Handler pageSwitcher = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x001:
                    Intent goToHomeIntent = new Intent(LaunchPageActivity.this, MainActivity.class);
                    startActivity(goToHomeIntent);
                    LaunchPageActivity.this.finish();
                    break;
                case 0x002:
                    Intent goToAdIntent = new Intent(LaunchPageActivity.this, AdPageActivity.class);
                    startActivity(goToAdIntent);
                    LaunchPageActivity.this.finish();
                    break;
                default:
                    Intent goToHomeDefaultIntent = new Intent(LaunchPageActivity.this, MainActivity.class);
                    startActivity(goToHomeDefaultIntent);
                    LaunchPageActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_page);
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pageSwitcher.removeCallbacksAndMessages(null);
    }

    private void initData()
    {

        AppConfigHttp appConfigHttp = GlobalRetrofit.getRetrofitDev().create(AppConfigHttp.class);

        appConfigHttp.getAppPid().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<String>>() {
                    @Override
                    public void onCompleted()
                    {
                        StringBuilder pinInitial = new StringBuilder();

                        pinInitial.append(GlobalConstants.PID).append(GlobalConstants.UID)
                                .append(GlobalConstants.AppConfig.FAA_KEY);

                        GlobalConstants.PIN = GeneralUtils.str2HashKey(pinInitial.toString());
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<String> stringDataResult)
                    {
                        GlobalConstants.PID = stringDataResult.result;
                    }
                });

        if(GeneralUtils.isDateExpired("localLaunchExpiredDare"))
        {
            LaunchPageHttp launchPageHttpService = GlobalRetrofit.getRetrofitInstance()
                    .create(LaunchPageHttp.class);

            launchPageHttpService.getLaunchPageConfig()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DataBase<LaunchPage>>()
                    {
                        @Override
                        public void onCompleted()
                        {
                            changeAdPage();
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            changeAdPage();
                        }

                        @Override
                        public void onNext(DataBase<LaunchPage> launchPageDataBase)
                        {
                            if(launchPageDataBase.msgCode == 11)
                            {
                                PrefUtil.setLong("localLaunchExpiredDare", 0);
                                PrefUtil.setString("launchImgUrl", null);
                            } else if (launchPageDataBase.msgCode == 10)
                            {
                                PrefUtil.setLong("localLaunchExpiredDare", launchPageDataBase.data.expire);
                                PrefUtil.setString("launchImgUrl", launchPageDataBase.data.lancherImg);
//                                LocalStoreUtil.storeBitmapByKey(launchPageDataBase.data.lancherImg);
                            }
                        }
                    });

        } else {
            ImageView launchImgContainer = (ImageView) findViewById(R.id.imgview_launch_img_container);
//            launchImgContainer.setImageBitmap(LocalStoreUtil.getLocalBitmap(PrefUtil.getString("launchImgUrl", null)));
            Glide.with(this).load(PrefUtil.getString("launchImgUrl", null)).asBitmap().into(launchImgContainer);
            changeAdPage();
        }
    }


    private void changeAdPage()
    {
        if(GeneralUtils.isDateExpired("localAdExpireDate"))
        {
            AdPageHttp adPageHttpService = GlobalRetrofit.getRetrofitInstance()
                    .create(AdPageHttp.class);

            adPageHttpService.getLaunchPageConfig()
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<DataBase<LaunchAd>>() {
                        @Override
                        public void onCompleted()
                        {
                            if(PrefUtil.getString("adAction", null) != null) {
                                pageSwitcher.sendEmptyMessageDelayed(0x002, 1000);
                            } else {
                                pageSwitcher.sendEmptyMessageDelayed(0x001, 1000);
                            }
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            pageSwitcher.sendEmptyMessageDelayed(0x001, 1000);
                        }

                        @Override
                        public void onNext(DataBase<LaunchAd> launchAdDataBase)
                        {
                            if(launchAdDataBase.msgCode == 11)
                            {
                                PrefUtil.setLong("localAdExpireDate", 0);
                                PrefUtil.setString("adImgUrl", null);
                                PrefUtil.setString("adActionUrl", null);
                                PrefUtil.setString("adAction", null);
                            } else if (launchAdDataBase.msgCode == 10)
                            {
                                PrefUtil.setLong("localAdExpireDate", launchAdDataBase.data.expire);
                                PrefUtil.setString("adImgUrl", launchAdDataBase.data.adImg);
                                PrefUtil.setString("adActionUrl", launchAdDataBase.data.adActionUrl);
                                PrefUtil.setString("adAction", launchAdDataBase.data.adAction);
//                                LocalStoreUtil.storeBitmapByKey(launchAdDataBase.data.adImg);
                            }
                        }
                    });

        } else{
            pageSwitcher.sendEmptyMessageDelayed(0x002, 2000);
        }
    }

    @Override
    public void jumpToHome()
    {

    }

    @Override
    public void jumpToAd()
    {

    }

    @Override
    public void setLaunchImg(Bitmap bitmap)
    {

    }
}
