package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.widget.FrameLayout;

import com.brightdairy.personal.brightdairy.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/13.
 */

public class AppSettingActivity extends BaseActivity
{

    private FrameLayout flUsrInfoDetail;
    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_app_setting);

        flUsrInfoDetail = (FrameLayout) findViewById(R.id.fl_setting_modify_usr_info);
    }

    private CompositeSubscription mCompositeSubscription;
    @Override
    protected void initData()
    {
        mCompositeSubscription  = new CompositeSubscription();
    }


    @Override
    protected void doThingWhenDestroy()
    {
        mCompositeSubscription.clear();
        super.doThingWhenDestroy();
    }

    @Override
    protected void initListener()
    {
        super.initListener();

        mCompositeSubscription.add(RxView.clicks(flUsrInfoDetail)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent gotoUsrInfoDetail = new Intent(AppSettingActivity.this, UserInfoDetailActivity.class);
                        startActivity(gotoUsrInfoDetail);
                    }
                }));
    }
}
