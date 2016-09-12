package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.LoginRegisterHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.SendSms;
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
 * Created by shuangmusuihua on 2016/9/3.
 */
public class LoginSmsActivity extends Activity
{


    private EditText inputPhoneNum;
    private Button getValidNum;
    private Button jumpToRegister;
    private Button jumpToPswLogin;
    private CompositeSubscription compositeObsever;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_input_phone_num);
        inputPhoneNum = (EditText) findViewById(R.id.edit_login_input_phone);
        getValidNum = (Button) findViewById(R.id.btn_login_get_validation_num);
        jumpToRegister = (Button) findViewById(R.id.btn_login_jumpto_register);
        jumpToPswLogin = (Button) findViewById(R.id.btn_login_jumpto_psw_login);

        initData();
        initListener();
    }



    @Override
    protected void onDestroy()
    {
        compositeObsever.clear();
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

    private String phoneNum;
    private void initListener()
    {
       compositeObsever.add(RxTextView.textChanges(inputPhoneNum)
               .subscribe(new Action1<CharSequence>() {
                   @Override
                   public void call(CharSequence sequence) {
                       phoneNum = String.valueOf(sequence);
                   }
               }));

        compositeObsever.add(RxView.clicks(getValidNum)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        if (AppLocalUtils.isValidPhoneNum(phoneNum))
                        {
                            getValidNum.setEnabled(false);
                            login(phoneNum);

                        } else
                        {
                            SuperActivityToast.create(LoginSmsActivity.this, "能不能好好填号码:(", Style.DURATION_LONG).show();
                        }

                    }
                }));

        compositeObsever.add(RxView.clicks(jumpToRegister)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Intent registerIntent = new Intent(LoginSmsActivity.this, RegisterActivity.class);
                        startActivity(registerIntent);
                        finish();

                    }
                }));

        compositeObsever.add(RxView.clicks(jumpToPswLogin)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent loginByPwdIntent = new Intent(LoginSmsActivity.this, LoginPwdActivity.class);
                        startActivity(loginByPwdIntent);
                        finish();
                    }
                }));
    }

    private void login(final String num)
    {

        compositeObsever.add(loginRegisterHttp.sendSms(new SendSms("LOGIN", num))
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
                        handleAfterSendSms(result.msgCode, result.msgText, num);
                    }

                }));

    }

    private void handleAfterSendSms(String code, String text, String phoneNum)
    {
        switch (code)
        {
            case GlobalHttpConfig.LOGIN_MSGCODE.REQUST_OK:

                Intent loginIntent = new Intent(LoginSmsActivity.this, LoginValidationActivity.class);
                loginIntent.putExtra("phonenum", phoneNum);
                startActivity(loginIntent);
                finish();

                break;

            case GlobalHttpConfig.LOGIN_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.LOGIN_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                SuperActivityToast.create(LoginSmsActivity.this, "验证码发送失败，检查号码再试试:)", Style.DURATION_LONG).show();
                getValidNum.setEnabled(true);

                break;
        }
    }

    private LoginRegisterHttp loginRegisterHttp;
    private void initData()
    {
        compositeObsever = new CompositeSubscription();
        loginRegisterHttp = AppLocalUtils.getLoginRegisterApi();

    }
}
