package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shuangmusuihua on 2016/9/20.
 */
public abstract class BaseActivity extends FragmentActivity
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
