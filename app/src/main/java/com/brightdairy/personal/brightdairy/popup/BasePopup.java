package com.brightdairy.personal.brightdairy.popup;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by shuangmusuihua on 2016/9/12.
 */
public abstract class BasePopup extends DialogFragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View popupView = initView(inflater, container, savedInstanceState);
        initData();
        initListener();
        return popupView;
    }


    @Override
    public void onDestroyView()
    {
        clearResOnDestroyView();
        super.onDestroyView();
    }



    @Override
    public void onResume()
    {
        super.onResume();
        Window thisWindow = getDialog().getWindow();
        customizePopupView(thisWindow);
    }


    protected void customizePopupView(Window thisWindow)
    {
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        thisWindow.setLayout((int) (displayMetrics.widthPixels * 1.0), (int) (displayMetrics.heightPixels * 0.8));
        thisWindow.setGravity(Gravity.BOTTOM);
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle state);
    protected abstract void initData();
    protected abstract void initListener();
    protected void clearResOnDestroyView() {}
}
