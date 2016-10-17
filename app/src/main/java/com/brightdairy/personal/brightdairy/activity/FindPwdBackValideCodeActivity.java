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
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.SendSms;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/11.
 */
public class FindPwdBackValideCodeActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_back_psw);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy()
    {
        mCompositeSubscription.clear();
        super.onDestroy();
    }

    private EditText inputPhoneNum;
    private Button getFindPwdBackValidCode;
    private void initView()
    {
        inputPhoneNum = (EditText) findViewById(R.id.edit_find_psw_back_input_phone_num);
        getFindPwdBackValidCode = (Button) findViewById(R.id.btn_get_find_psw_back_validation_code);
    }

    private CompositeSubscription mCompositeSubscription;
    private LoginRegisterHttp mLoginRegisterApi;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mLoginRegisterApi = AppLocalUtils.getLoginRegisterApi();
    }

    private String phoneNum;
    private void initListener()
    {
        mCompositeSubscription.add(RxTextView.textChanges(inputPhoneNum)
                .subscribe(new Action1<CharSequence>()
                {
                    @Override
                    public void call(CharSequence sequence)
                    {
                        phoneNum = String .valueOf(sequence);
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(getFindPwdBackValidCode)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        getFindPwdBackValidCode.setEnabled(false);
                        handleGetFindPwdValidCode(phoneNum);
                    }
                }));
    }

    private void handleGetFindPwdValidCode(final String num)
    {
        if(!AppLocalUtils.isValidPhoneNum(num))
        {
            GeneralUtils.showToast(FindPwdBackValideCodeActivity.this, "咱能不能好好填写号码呀:)");
            return;
        }

        mCompositeSubscription.add(mLoginRegisterApi.sendSms(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,new SendSms("GETPSW", num))
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
                        handleAfterGetValidCode(result, num);
                    }
                }));
    }

    private void handleAfterGetValidCode(DataResult<Object> result, String phoneNum)
    {
        switch (result.msgCode)
        {
            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                Intent findPwdBackIntent = new Intent(FindPwdBackValideCodeActivity.this, FindBackPwdActivity.class);
                findPwdBackIntent.putExtra("phonenum", phoneNum);
                startActivity(findPwdBackIntent);
                finish();

                break;

            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_FAILED:
            case GlobalHttpConfig.API_MSGCODE.SEND_SMS_INVALID_PHONE_NUM:

                GeneralUtils.showToast(FindPwdBackValideCodeActivity.this, "验证码发送失败，检查号码再试试:)");
                getFindPwdBackValidCode.setEnabled(true);

                break;
        }
    }
}
