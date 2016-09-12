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

        return initView(inflater, container, savedInstanceState);
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
        Window thisWindow = getDialog().getWindow();
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        thisWindow.setLayout((int) (displayMetrics.widthPixels * 0.9), (int) (displayMetrics.heightPixels * 0.8));
        thisWindow.setGravity(Gravity.BOTTOM);
        initData();
        super.onResume();
    }

    protected abstract void initData();
    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle state);
    protected void clearResOnDestroyView() {
    }
}
