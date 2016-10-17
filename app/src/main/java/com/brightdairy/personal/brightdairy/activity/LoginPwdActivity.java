package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.LoginRegisterApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.UserLogin;
import com.brightdairy.personal.model.entity.LoginResult;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/9.
 */
public class LoginPwdActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_psw);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private EditText inputUsrName;
    private EditText inputUsrPwd;
    private Button loginByUsrName;
    private Button findBackPwd;
    private void initView()
    {
        inputUsrName = (EditText) findViewById(R.id.edit_input_user_name);
        inputUsrPwd = (EditText) findViewById(R.id.edit_input_user_psw);
        loginByUsrName = (Button) findViewById(R.id.btn_user_login_by_name);
        findBackPwd = (Button) findViewById(R.id.btn_find_psw_back);

    }

    private LoginRegisterApi loginRegisterApi;
    private CompositeSubscription mCompositeSubscription;
    private void initData()
    {
        loginRegisterApi = AppLocalUtils.getLoginRegisterApi();
        mCompositeSubscription = new CompositeSubscription();
    }

    private String userName;
    private String userPwd;
    private void initListener()
    {
        mCompositeSubscription.add(RxTextView.textChanges(inputUsrName)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        userName = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxTextView.textChanges(inputUsrPwd)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        userPwd = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(loginByUsrName)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        loginByUsrName.setEnabled(false);
                        handleLoginByUserName();
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(findBackPwd)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent findPwdBackIntent = new Intent(LoginPwdActivity.this, FindPwdBackValideCodeActivity.class);
                        startActivity(findPwdBackIntent);
                        finish();
                    }
                }));
    }

    private void handleLoginByUserName()
    {
        if(TextUtils.isEmpty(userName))
        {
            GeneralUtils.showToast(LoginPwdActivity.this, "用户名不能为空哦:(");
            return;
        }

        if(TextUtils.isEmpty(userPwd))
        {
            GeneralUtils.showToast(LoginPwdActivity.this, "密码不能为空哦:(");
            return;
        }

        mCompositeSubscription.add(loginRegisterApi.userLogin(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,new UserLogin(AppLocalUtils.encyptPwd(userPwd), userName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<LoginResult>>()
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
                    public void onNext(DataResult<LoginResult> result)
                    {
                        handleAfterLoginByUsrName(result);
                    }
                }));
    }

    private void handleAfterLoginByUsrName(DataResult<LoginResult> result)
    {
        switch (result.msgCode)
        {
            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                GlobalHttpConfig.UID = result.result.userLoginId;
                PrefUtil.setString(GlobalConstants.AppConfig.UID_LOCAL, GlobalHttpConfig.UID);
                GlobalHttpConfig.TID = result.result.tid;
                PrefUtil.setString(GlobalConstants.AppConfig.TID_LOCAL, GlobalHttpConfig.TID);

                GlobalHttpConfig.PIN = AppLocalUtils.getPIN();

                Intent mainPageIntent = new Intent(LoginPwdActivity.this, MainActivity.class);
                startActivity(mainPageIntent);
                finish();

                break;
            default:

                GeneralUtils.showToast(LoginPwdActivity.this, result.msgText);
                loginByUsrName.setEnabled(true);

                break;
        }
    }
}

