package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.LoginRegisterHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.SendSms;
import com.brightdairy.personal.model.HttpReqBody.UserLoginBySms;
import com.brightdairy.personal.model.entity.LoginResult;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
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
public class LoginValidationActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_validation_code);

        initView();
        initData();
        initListener();

    }


    @Override
    protected void onDestroy()
    {
        compositeSubscription.clear();
        super.onDestroy();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private TextView getValidationPhoneNum;
    private EditText inputValidation;
    private TextView resendSms;
    private Button login;
    private void initView()
    {
        getValidationPhoneNum = (TextView) findViewById(R.id.txtview_login_validation_phone_num);
        inputValidation = (EditText) findViewById(R.id.edit_input_login_validation_code);
        resendSms = (TextView) findViewById(R.id.txtview_login_validaton_resend_sms);
        login = (Button) findViewById(R.id.btn_user_login);

        resendSms.setEnabled(false);
    }


    private String phoneNum;
    private CompositeSubscription compositeSubscription;
    private LoginRegisterHttp loginRegisterApi;
    private void initData()
    {
        compositeSubscription = new CompositeSubscription();
        loginRegisterApi = AppLocalUtils.getLoginRegisterApi();

        phoneNum = getIntent().getStringExtra("phonenum");

        getValidationPhoneNum.setText(phoneNum);

    }

    private String validatioCode;
    private void initListener()
    {
        compositeSubscription.add(RxTextView.textChanges(inputValidation)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence sequence) {

                        validatioCode = String.valueOf(sequence);

                    }
                }));

        compositeSubscription.add(RxView.clicks(login)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        login.setEnabled(false);
                        loginByInvalidationCode();

                    }
                }));

        compositeSubscription.add(RxView.clicks(resendSms)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        resendSms.setEnabled(false);
                        resendValidCode();

                    }
                }));


    }

    private void loginByInvalidationCode()
    {

        if(TextUtils.isEmpty(validatioCode) || !validatioCode.matches("^\\d{4,6}$"))
        {
            SuperActivityToast.create(LoginValidationActivity.this, "验证码好像有问题唉，检查一下吧:)", Style.DURATION_LONG).show();
            login.setEnabled(true);
            return;
        }

        compositeSubscription.add(loginRegisterApi.userLoginBySms(new UserLoginBySms(validatioCode, phoneNum))
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
                        handleLoginByValidCode(result);
                    }
                }));

    }

    private void handleLoginByValidCode(DataResult<LoginResult> result)
    {
        switch (result.msgCode)
        {
            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                GlobalHttpConfig.UID = result.result.userLoginId;
                GlobalHttpConfig.TID = result.result.tid;

                PrefUtil.setString(GlobalConstants.AppConfig.UID_LOCAL, GlobalHttpConfig.UID);
                PrefUtil.setString(GlobalConstants.AppConfig.TID_LOCAL, GlobalHttpConfig.TID);

                Intent loginOkIntent = new Intent(LoginValidationActivity.this, MainActivity.class);
                startActivity(loginOkIntent);
                finish();

                break;
            case GlobalHttpConfig.API_MSGCODE.LOGIN_FAILED:

                SuperActivityToast.create(LoginValidationActivity.this, "验证码错误:(，单击重新获取吧:)", Style.DURATION_LONG).show();
                resendSms.setText("重新获取验证码:)");
                resendSms.setEnabled(true);

                break;
        }
    }


    private void resendValidCode()
    {
        compositeSubscription.add(loginRegisterApi.sendSms(new SendSms("LOGIN", phoneNum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<Object>>()
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
                    public void onNext(DataResult<Object> result)
                    {
                        handleAfterSendSms(result.msgCode, result.msgText);
                    }

                }));
    }

    private void handleAfterSendSms(String code, String text)
    {
        switch (code)
        {
            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                login.setEnabled(true);
                break;

            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                SuperActivityToast.create(LoginValidationActivity.this, "验证码错误:(，单击重新获取吧:)", Style.DURATION_LONG).show();
                resendSms.setEnabled(true);

                break;
        }
    }

}
