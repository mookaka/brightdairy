package com.brightdairy.personal.brightdairy.popup;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/12.
 */
public class AddressSelectorPopup extends BasePopup
{

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        View addressSelectorView = inflater.inflate(R.layout.popup_address_selector, container);
        return addressSelectorView;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData()
    {

    }

}
