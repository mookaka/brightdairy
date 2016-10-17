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
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.ModifyPwdBySms;
import com.brightdairy.personal.model.HttpReqBody.SendSms;
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
public class FindBackPwdActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_psw_back_new_psw);
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

    private TextView findPwdBackPhoneNum;
    private EditText findPwdBackValidCode;
    private TextView resendSms;
    private EditText inputNewPwd;
    private EditText inputComfirmNewPwd;
    private Button comfirmModification;
    private void initView()
    {
        findPwdBackPhoneNum = (TextView) findViewById(R.id.txtview_find_psw_back_get_validation_phone_num);
        findPwdBackValidCode = (EditText) findViewById(R.id.edit_input_find_psw_back_validation_code);
        resendSms = (TextView) findViewById(R.id.txtview_resend_find_pwd_back_valid_code);
        inputNewPwd = (EditText) findViewById(R.id.edit_input_new_psw);
        inputComfirmNewPwd = (EditText) findViewById(R.id.edit_input_confirm_psw);
        comfirmModification = (Button) findViewById(R.id.btn_confirm_psw_modification);

        findPwdBackPhoneNum.setText(phoneNum);
        resendSms.setEnabled(false);
    }

    private String phoneNum;
    private CompositeSubscription mCompositeSubscription;
    private LoginRegisterHttp mLoginRegisterApi;
    private void initData()
    {
        phoneNum = getIntent().getStringExtra("phonenum");
        mCompositeSubscription = new CompositeSubscription();
        mLoginRegisterApi = AppLocalUtils.getLoginRegisterApi();
    }

    private String validCode;
    private String newPwd;
    private String newPwdConfirm;
    private void initListener()
    {
        mCompositeSubscription.add(RxTextView.textChanges(findPwdBackValidCode)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        validCode = String .valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxTextView.textChanges(inputNewPwd)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        newPwd = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxTextView.textChanges(inputComfirmNewPwd)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        newPwdConfirm = String.valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(comfirmModification)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        comfirmModification.setEnabled(false);
                        handlePwdModification();
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


    private void handlePwdModification()
    {
        if(TextUtils.isEmpty(validCode) || !validCode.matches("^\\d{4,6}$"))
        {
            GeneralUtils.showToast(FindBackPwdActivity.this, "验证码好像有问题唉，检查一下吧:)");
            return;
        }

        if(TextUtils.isEmpty(newPwd))
        {
            GeneralUtils.showToast(FindBackPwdActivity.this, "密码不能为空哦:)");
            return;
        }

        if(TextUtils.isEmpty(newPwdConfirm) || !newPwdConfirm.endsWith(newPwd))
        {
            GeneralUtils.showToast(FindBackPwdActivity.this, "干嘛呀！两次输入密码不一致哎:)");
            return;
        }


        mCompositeSubscription.add(mLoginRegisterApi.modifyPasswordBySmsCode(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                new ModifyPwdBySms(AppLocalUtils.encyptPwd(newPwd)
                , validCode
                , AppLocalUtils.encyptPwd(newPwdConfirm)
                , AppLocalUtils.encyptPwd(newPwd)
                , phoneNum
                , GlobalHttpConfig.UID))
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
                        handleAfterFindPwdBackBySms(result);
                    }
                }));
    }

    private void handleAfterFindPwdBackBySms(DataResult<Object> result)
    {
        switch (result.msgCode)
        {

            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                GeneralUtils.showToast(FindBackPwdActivity.this, "已经重置密码了，这次要记牢哦:)");
                Intent loginPwdIntent = new Intent(FindBackPwdActivity.this, LoginPwdActivity.class);
                startActivity(loginPwdIntent);
                finish();

                break;

            case GlobalHttpConfig.API_MSGCODE.BAD_VALID_CODE:
            case GlobalHttpConfig.API_MSGCODE.REQUST_FAILED:
            default:

                GeneralUtils.showToast(FindBackPwdActivity.this, "验证码好像有问题唉，点击重新获取验证码吧:)");
                resendSms.setText("重新获取验证码");
                resendSms.setEnabled(true);

                break;
        }
    }

    private void resendSms()
    {
        mCompositeSubscription.add(mLoginRegisterApi.sendSmsCodeForReg(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,new SendSms(phoneNum, "GETPSW"))
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

            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                GeneralUtils.showToast(FindBackPwdActivity.this, "验证码发送失败，再试试:)");
                resendSms.setEnabled(true);

                break;
        }
    }
}
