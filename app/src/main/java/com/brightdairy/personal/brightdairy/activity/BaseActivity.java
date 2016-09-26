package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by shuangmusuihua on 2016/9/20.
 */
public abstract class BaseActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy()
    {
        doThingWhenDestroy();
        super.onDestroy();
    }

    protected void doThingWhenDestroy() {}
    protected abstract void initView();
    protected abstract void initData();
    protected void initListener() {};
}
