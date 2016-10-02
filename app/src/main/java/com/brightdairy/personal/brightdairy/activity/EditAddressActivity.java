package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.baoyz.pg.PG;
import com.brightdairy.personal.api.AddressApi;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.popup.AddressSelectorPopup;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.NewAddress;
import com.brightdairy.personal.model.HttpReqBody.UpdateAddress;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/30.
 */

public class EditAddressActivity extends BaseActivity
{
    private EditText editReceiverName;
    private EditText editReceiverMobile;
    private EditText editAddressDetail;
    private EditText editGateNum;
    private CheckBox checkboxSetDefault;
    private TextView txtviewPopupAdressSelector;
    private Button btnConfirm;

    private final int CREATE_ADDRESS_INFO = 0;
    private final int UPDATE_ADDRESS_INFO = 1;
    private int OPERATOR_TYPE = -1;

    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_edit_address);

        editReceiverName = (EditText) findViewById(R.id.edit_modify_address_name);
        editReceiverMobile = (EditText) findViewById(R.id.edit_modify_address_mobile);
        editAddressDetail = (EditText) findViewById(R.id.edit_modify_address_detail);
        editGateNum = (EditText) findViewById(R.id.edit_modify_address_gate_num);
        checkboxSetDefault = (CheckBox) findViewById(R.id.checkbox_modify_address_set_default);
        txtviewPopupAdressSelector = (TextView) findViewById(R.id.txtview_modify_address_popup_address_selector);
        btnConfirm = (Button) findViewById(R.id.btn_modify_address_confirm);
    }


    private CompositeSubscription mCompositeSubscription;
    private AddressApi mAddressApi;
    private NewAddress mNewAddress;
    private UpdateAddress mUpdateAddress;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mAddressApi = GlobalRetrofit.getRetrofitDev().create(AddressApi.class);

        mUpdateAddress = getIntent().getParcelableExtra("updateAddressInfo");

        if (mUpdateAddress != null)
        {
            fillViewWithData();
            mNewAddress = mUpdateAddress.mNewAddress;
            OPERATOR_TYPE = UPDATE_ADDRESS_INFO;
        }
        else
        {
            mNewAddress = new NewAddress();
            OPERATOR_TYPE = CREATE_ADDRESS_INFO;
        }


    }


    private AddressSelectorPopup mAddressSelectorPopup;
    @Override
    protected void initListener()
    {
        super.initListener();

        mCompositeSubscription.add(RxView.clicks(btnConfirm)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        builUpdateAddressInfoReqBody();
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(txtviewPopupAdressSelector)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mAddressSelectorPopup == null)
                        {
                            mAddressSelectorPopup = new AddressSelectorPopup();
                        }

                        mAddressSelectorPopup.show(getFragmentManager(), "addressSelector");
                    }
                }));
    }

    private void builUpdateAddressInfoReqBody()
    {
        if (AppLocalUtils.isValidUserName(editReceiverName.getText().toString()))
        {
            mNewAddress.toName = editReceiverName.getText().toString();
        }
        else
        {
            SuperActivityToast.create(this, "用户名不合随心订的八字哦:)", Style.DURATION_LONG).show();
            return;
        }

        if (AppLocalUtils.isValidPhoneNum(editReceiverMobile.getText().toString()))
        {
            mNewAddress.mobile = editReceiverMobile.getText().toString();
        }
        else
        {
            SuperActivityToast.create(this, "手机号有点小问题耶:)", Style.DURATION_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(editAddressDetail.getText().toString()))
        {
            mNewAddress.address = editAddressDetail.getText().toString();
        }
        else
        {
            SuperActivityToast.create(this, "详细地址不可以为空哦:)", Style.DURATION_LONG).show();
            return;
        }

        mNewAddress.isDefault = checkboxSetDefault.isChecked()?"Y":"N";

        operateAddressInfo();
    }


    private void fillViewWithData()
    {
        if (mUpdateAddress != null)
        {
            editReceiverName.setHint(mUpdateAddress.mNewAddress.toName);
            editReceiverMobile.setHint(mUpdateAddress.mNewAddress.mobile);
            editAddressDetail.setHint(mUpdateAddress.mNewAddress.city
                    + mUpdateAddress.mNewAddress.county
                    + mUpdateAddress.mNewAddress.street
                    + mUpdateAddress.mNewAddress.address);

            checkboxSetDefault.setChecked(mUpdateAddress.mNewAddress.isDefault.equals("Y"));
        }
    }


    private void operateAddressInfo()
    {
        switch (OPERATOR_TYPE)
        {
            case CREATE_ADDRESS_INFO:
                createAddressInfo();
                break;
            case UPDATE_ADDRESS_INFO:
                updateAddressInfo();
                break;
        }
    }

    private void updateAddressInfo()
    {
        mCompositeSubscription.add(mAddressApi.updateShipAddress(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                mUpdateAddress)
                .subscribe(new Subscriber<DataResult<Object>>() {

                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onNext(DataResult<Object> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                SuperActivityToast.create(EditAddressActivity.this, "成功更新地址", Style.DURATION_LONG).show();
                                break;
                            default:
                                SuperActivityToast.create(EditAddressActivity.this, result.msgText, Style.DURATION_LONG).show();
                                break;
                        }
                    }
                }));
    }

    private void createAddressInfo()
    {
        mCompositeSubscription.add(mAddressApi.createShipAddress(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                mNewAddress)
                .subscribe(new Subscriber<DataResult<Object>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onNext(DataResult<Object> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                SuperActivityToast.create(EditAddressActivity.this, "成功创建地址", Style.DURATION_LONG).show();
                                break;
                            default:
                                SuperActivityToast.create(EditAddressActivity.this, result.msgText, Style.DURATION_LONG).show();
                                break;
                        }
                    }
                }));
    }
}
