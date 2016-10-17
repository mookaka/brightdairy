package com.brightdairy.personal.brightdairy.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.lang.reflect.Field;

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

//        try {
//            Field childFragmentManager = android.app.Fragment.class.getDeclaredField("mChildFragmentManager");
//            childFragmentManager.setAccessible(true);
//            childFragmentManager.set(this, null);
//
//
//        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
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


    @Override
    public void onDetach()
    {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //avoid the exception of java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
    //when displaying dialogFragment instance after actiivty has called its onSaveState method
    private static final Class clz = DialogFragment.class;

    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        try {
            Field dismissed = clz.getDeclaredField("mDismissed");
            dismissed.setAccessible(true);
            dismissed.set(this, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Field shown = clz.getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            shown.set(this, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

}
