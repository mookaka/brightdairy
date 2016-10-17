package com.brightdairy.personal.brightdairy.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/10/6.
 */

public class GeneralLoadingPopup extends BasePopup
{

    public static GeneralLoadingPopup newInstance()
    {
        GeneralLoadingPopup generalLoadingPopup = new GeneralLoadingPopup();

        return generalLoadingPopup;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        View LoadingPopupView = inflater.inflate(R.layout.popup_general_loading, container);
        return LoadingPopupView;
    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void customizePopupView(Window thisWindow)
    {
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getDialog().setCancelable(true);
        thisWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        thisWindow.setGravity(Gravity.CENTER);
    }

    @Override
    protected void initListener()
    {

    }
}
