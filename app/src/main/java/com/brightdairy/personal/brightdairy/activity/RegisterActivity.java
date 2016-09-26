package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
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
 * Created by shuangmusuihua on 2016/9/9.
 */
public class RegisterActivity extends Activity
{



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        initView();
        initData();
        initListener();
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

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        observerComposition.clear();
        super.onDestroy();
    }


    private EditText inputPhoneNum;
    private Button getValidCode;
    private CheckBox agreeRegProtocol;
    private Button showProtocol;
    private void initView()
    {
        inputPhoneNum = (EditText) findViewById(R.id.edit_input_register_phone_num);
        getValidCode = (Button) findViewById(R.id.btn_get_register_validation_code);
        agreeRegProtocol = (CheckBox) findViewById(R.id.checkbox_register_protocol_agreement);
        showProtocol = (Button) findViewById(R.id.btn_register_show_protocol);
    }

    private CompositeSubscription observerComposition;
    private LoginRegisterHttp loginRegisterApi;
    private void initData()
    {
        observerComposition = new CompositeSubscription();
        loginRegisterApi = AppLocalUtils.getLoginRegisterApi();
    }

    private String phoneNum;
    private void initListener()
    {
        observerComposition.add(RxTextView.textChanges(inputPhoneNum)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        phoneNum = String.valueOf(sequence);
                    }
                }));

        observerComposition.add(RxView.clicks(getValidCode)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        getValidCode.setEnabled(false);
                        getValidCode();
                    }
                }));
    }

    private void getValidCode()
    {
        if(!agreeRegProtocol.isChecked())
        {
            SuperActivityToast.create(RegisterActivity.this, "必须先同意注册条款才能继续下一步哦:)", Style.DURATION_LONG).show();
            return;
        }

        if(!AppLocalUtils.isValidPhoneNum(phoneNum))
        {
            SuperActivityToast.create(RegisterActivity.this, "能不能好好填写手机号码:)", Style.DURATION_LONG).show();
            return;
        }

        observerComposition.add(loginRegisterApi.sendSmsCodeForReg(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,new SendSms(phoneNum, "REGISTE"))
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
                        handleAfterSendSms(result.msgCode, result.msgText, phoneNum);
                    }
                }));

    }

    private void handleAfterSendSms(String code, String text, String phoneNum)
    {
        switch (code)
        {
            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                Intent InputPwdIntent = new Intent(RegisterActivity.this, RegisterInputPwdActivity.class);
                InputPwdIntent.putExtra("phonenum", phoneNum);
                startActivity(InputPwdIntent);
                finish();

                break;

            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                SuperActivityToast.create(RegisterActivity.this, "验证码发送失败，检查号码再试试:)", Style.DURATION_LONG).show();
                getValidCode.setEnabled(true);

                break;
        }
    }
}
