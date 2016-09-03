package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.brightdairy.personal.brightdairy.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class LoginRegisterActivity extends Activity
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

        initListener();
    }


    @Override
    protected void onDestroy()
    {
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

    }
}
