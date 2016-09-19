package com.brightdairy.personal.brightdairy;

import android.app.Application;

import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.facebook.stetho.Stetho;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public class MyApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        GlobalConstants.APPLICATION_CONTEXT = this;
        Stetho.initializeWithDefaults(this);
    }
}
