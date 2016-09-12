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
import com.brightdairy.personal.model.HttpReqBody.UserRegister;
import com.brightdairy.personal.model.entity.LoginResult;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/11.
 */
public class RegisterInputPwdActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_input_psw);

        initData();
        initView();
        initListener();

    }

    @Override
    protected void onDestroy()
    {
        mCompositeSubscription.clear();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private TextView getValidCodePhoneNum;
    private TextView resendSms;
    private EditText inputValidationCode;
    private EditText inputPwd;
    private EditText inputComfirmPwd;
    private Button comfirmRegister;

    private void initView() {
        getValidCodePhoneNum = (TextView) findViewById(R.id.txtview_register_input_pwd_phone_num);
        inputValidationCode = (EditText) findViewById(R.id.edit_input_register_validation_code);
        resendSms = (TextView) findViewById(R.id.txtview_register_input_pwd_resend_sms);
        inputPwd = (EditText) findViewById(R.id.edit_input_register_psw);
        inputComfirmPwd = (EditText) findViewById(R.id.edit_input_register_confirm_psw);
        comfirmRegister = (Button) findViewById(R.id.btn_register_confirm_register);

        resendSms.setEnabled(false);
        getValidCodePhoneNum.setText(phoneNum);
    }


    private CompositeSubscription mCompositeSubscription;
    private LoginRegisterHttp loginRegisterApi;
    private String phoneNum;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        loginRegisterApi = AppLocalUtils.getLoginRegisterApi();
        phoneNum = getIntent().getStringExtra("phonenum");
    }

    private String validCode;
    private String passWord;
    private String comfirmPwd;
    private void initListener()
    {
        mCompositeSubscription.add(RxTextView.textChanges(inputValidationCode)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        validCode = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxTextView.textChanges(inputPwd)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        passWord = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxTextView.textChanges(inputComfirmPwd)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        comfirmPwd = String.valueOf(sequence);

                    }
                }));


        mCompositeSubscription.add(RxView.clicks(comfirmRegister)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        comfirmRegister.setEnabled(false);
                        handleComfirmRegisterByPhome();
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(resendSms)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        resendSms.setEnabled(false);
                        resendSms();
                    }
                }));
    }

    private void handleComfirmRegisterByPhome()
    {
        if(TextUtils.isEmpty(validCode) || !validCode.matches("^\\d{4,6}$"))
        {
            SuperActivityToast.create(RegisterInputPwdActivity.this, "验证码好像有问题唉，检查一下吧:)", Style.DURATION_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(passWord))
        {
            SuperActivityToast.create(RegisterInputPwdActivity.this, "密码不能为空哦:)", Style.DURATION_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(comfirmPwd) || !comfirmPwd.endsWith(passWord))
        {
            SuperActivityToast.create(RegisterInputPwdActivity.this, "干嘛呀！两次输入密码不一致哎:)", Style.DURATION_LONG).show();
            return;
        }


        mCompositeSubscription.add(loginRegisterApi
                .userRegister(new UserRegister(phoneNum,
                        AppLocalUtils.encyptPwd(passWord),
                        AppLocalUtils.encyptPwd(comfirmPwd),
                        phoneNum, validCode))
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
                        handleRegisterByPhome(result);
                    }
                }));

    }

    private void handleRegisterByPhome(DataResult<LoginResult> result)
    {
        switch (result.msgCode)
        {
            case GlobalHttpConfig.LOGIN_MSGCODE.REQUST_OK:

                GlobalHttpConfig.UID = result.result.userLoginId;
                PrefUtil.setString(GlobalConstants.AppConfig.UID_LOCAL, GlobalHttpConfig.UID);

                GlobalHttpConfig.TID = result.result.tid;
                PrefUtil.setString(GlobalConstants.AppConfig.TID_LOCAL, GlobalHttpConfig.TID);

                Intent mainPageIntent = new Intent(RegisterInputPwdActivity.this, MainActivity.class);
                startActivity(mainPageIntent);
                finish();

                break;

            case GlobalHttpConfig.LOGIN_MSGCODE.VALID_CODE_EXPIRED:
            case GlobalHttpConfig.LOGIN_MSGCODE.REQUST_FAILED:

                SuperActivityToast.create(RegisterInputPwdActivity.this, "验证码好像有问题唉，点击重新获取验证码吧:)", Style.DURATION_LONG).show();
                resendSms.setText("重新获取验证码");
                resendSms.setEnabled(true);

                break;

            case GlobalHttpConfig.LOGIN_MSGCODE.PHONE_NUM_DOUBLE_REGISTER:

                SuperActivityToast.create(RegisterInputPwdActivity.this, "这个手机号已经注册过了:)", Style.DURATION_LONG).show();
                Intent RegisterIntent = new Intent(RegisterInputPwdActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
                finish();

                break;
        }
    }


    private void resendSms()
    {
        mCompositeSubscription.add(loginRegisterApi.sendSmsCodeForReg(new SendSms(phoneNum, "REGISTE"))
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

            case GlobalHttpConfig.LOGIN_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.LOGIN_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                SuperActivityToast.create(RegisterInputPwdActivity.this, "验证码发送失败，再试试:)", Style.DURATION_LONG).show();
                resendSms.setEnabled(true);

                break;
        }
    }

}
