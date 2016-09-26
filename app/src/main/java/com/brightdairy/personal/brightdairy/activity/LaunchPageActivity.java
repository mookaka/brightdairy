package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.brightdairy.personal.api.AppConfigHttp;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.LaunchHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.GetPid;
import com.brightdairy.personal.model.entity.CityZoneCode;
import com.brightdairy.personal.model.entity.LaunchAd;
import com.brightdairy.personal.model.entity.LaunchPage;
import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private LaunchHttp launchPageHttpService;
    private AppConfigHttp appConfigHttp;
    private void initData()
    {

        appConfigHttp = GlobalRetrofit.getRetrofitDev().create(AppConfigHttp.class);

        String oldPid = PrefUtil.getString(GlobalConstants.AppConfig.PID_LOCAL, "");

        appConfigHttp.getAppPid(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,new GetPid(oldPid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<String>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        StringBuilder pinInitial = new StringBuilder();

                        pinInitial.append(GlobalHttpConfig.PID).append(GlobalHttpConfig.UID)
                                .append(GlobalConstants.AppConfig.FAA_KEY);

                        GlobalHttpConfig.PIN = GeneralUtils.str2HashKey(pinInitial.toString());

                        getCityCode();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<String> stringDataResult)
                    {
                        GlobalHttpConfig.PID = stringDataResult.result;
                        PrefUtil.setString(GlobalConstants.AppConfig.PID_LOCAL, GlobalHttpConfig.PID);
                    }
                });

    }

    private void getCityCode()
    {
        appConfigHttp.getCurrCityCode(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<DataResult<ArrayList<CityZoneCode>>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<ArrayList<CityZoneCode>> cityZoneCodeResult)
                    {
                        ArrayList<CityZoneCode> cityZoneCodess = cityZoneCodeResult.result;

                        for (int index = 0; index < cityZoneCodess.size(); index++)
                        {
                            CityZoneCode cityCode = cityZoneCodess.get(index);

                            if(cityCode.cityName.equals(GlobalConstants.CURR_ZONE_CN_NAME))
                            {
                                GlobalConstants.ZONE_CODE = cityCode.cityCode;
                            }

                        }

                        getImgBaseUrl();
                    }
                });
    }

    private void getImgBaseUrl()
    {
        appConfigHttp.getImgBaseUrl(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DataResult<String> stringDataResult) {
                        GlobalConstants.IMG_URL_BASR = stringDataResult.result;

                        freshLaunPage();
                    }
                });
    }

    private void freshLaunPage()
    {
        launchPageHttpService = GlobalRetrofit.getRetrofitDev().create(LaunchHttp.class);

        launchPageHttpService.getLaunchPageConfig(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,GlobalConstants.ZONE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<LaunchPage>>()
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
                    public void onNext(DataResult<LaunchPage> launchPageDataBase)
                    {
                        changeLaunchPage(launchPageDataBase);
                    }
                });
    }


    private ImageView launchImgContainer;
    private void changeLaunchPage(DataResult<LaunchPage> LaunchPageConfig)
    {
        launchImgContainer  = (ImageView) findViewById(R.id.imgview_launch_img_container);
        if(GlobalHttpConfig.API_MSGCODE.REQUST_OK.equals(LaunchPageConfig.msgCode))
        {
            String launchImg = GlobalConstants.IMG_URL_BASR + LaunchPageConfig.result.lancherImg;
            Glide.with(this).load(launchImg).asBitmap().into(launchImgContainer);
        } else
        {
            launchImgContainer.setImageResource(R.mipmap.lanch_ad_img);
        }
    }

    private void changeAdPage()
    {

        launchPageHttpService.getLaunchAdConfig(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,GlobalConstants.ZONE_CODE)
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<DataResult<LaunchAd>>() {
                        @Override public void onCompleted()
                        {
                            if(PrefUtil.getString("adAction", null) != null) {
                                pageSwitcher.sendEmptyMessageDelayed(0x002, 2000);
                            } else {
                                pageSwitcher.sendEmptyMessageDelayed(0x001, 2000);
                            }
                        }

                        @Override public void onError(Throwable e)
                        {
                            pageSwitcher.sendEmptyMessageDelayed(0x001, 1000);
                        }

                        @Override public void onNext(DataResult<LaunchAd> launchAdDataBase)
                        {
                            if(GlobalHttpConfig.API_MSGCODE.REQUST_OK_NO_CONTENT.equals(launchAdDataBase.msgCode))
                            {
                                PrefUtil.setString("adImgUrl", null);
                                PrefUtil.setString("adActionUrl", null);
                                PrefUtil.setString("adAction", null);
                            } else if (GlobalHttpConfig.API_MSGCODE.REQUST_OK.equals(launchAdDataBase.msgCode))
                            {
                                PrefUtil.setString("adImgUrl", GlobalConstants.IMG_URL_BASR + launchAdDataBase.result.adImg);
                                PrefUtil.setString("adActionUrl", launchAdDataBase.result.adActionUrl);
                                PrefUtil.setString("adAction", launchAdDataBase.result.adAction);
                            }
                        }
                    });

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
