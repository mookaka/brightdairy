package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/13.
 */

public class UserInfoDetailActivity extends BaseActivity
{

    private TextView txtviewEditUsrInfo;
    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_user_info_detail);

        txtviewEditUsrInfo = (TextView) findViewById(R.id.txtview_usr_info_detail_edit);
    }

    private CompositeSubscription mCompositeSubscription;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
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

        mCompositeSubscription.add(RxView.clicks(txtviewEditUsrInfo)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent goToEditUsrInfo = new Intent(UserInfoDetailActivity.this, EditUsrInfoActivity.class);
                        startActivity(goToEditUsrInfo);
                    }
                }));
    }
}
