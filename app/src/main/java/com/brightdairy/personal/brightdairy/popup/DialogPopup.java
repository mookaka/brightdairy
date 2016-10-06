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
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/6.
 */

public class DialogPopup extends BasePopup
{

    private TextView txtviewTitle;
    private TextView txtviewConfirm;
    private TextView txtviewCancel;
    private CompositeSubscription mCompositeSubscription;
    private DialogListener mDialogListener;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        View popupDialogView = inflater.inflate(R.layout.popup_confirm_cancel_dialog, container);
        txtviewTitle = (TextView) popupDialogView.findViewById(R.id.txtview_popup_dialog_title);
        txtviewConfirm = (TextView) popupDialogView.findViewById(R.id.txtview_popup_dialog_confirm);
        txtviewCancel = (TextView) popupDialogView.findViewById(R.id.txtview_popup_dialog_cancel);
        return popupDialogView;
    }


    @Override
    protected void customizePopupView(Window thisWindow)
    {
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        thisWindow.setLayout((int) (displayMetrics.widthPixels * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        thisWindow.setGravity(Gravity.CENTER);
    }

    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        setTxtviewTitle(getArguments().getString("title"));
    }

    @Override
    protected void initListener()
    {
        mCompositeSubscription.add(RxView.clicks(txtviewConfirm)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mDialogListener != null)
                        {
                            mDialogListener.onConfirmClick();
                        }
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(txtviewCancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mDialogListener != null)
                        {
                            mDialogListener.onCancelClick();
                        }
                    }
                }));
    }

    @Override
    protected void clearResOnDestroyView()
    {
        mCompositeSubscription.clear();
        super.clearResOnDestroyView();
    }


    public void setDialogListener(DialogListener dialogListener)
    {
        this.mDialogListener = dialogListener;
    }

    public void setTxtviewTitle(String title)
    {
        if (title != null && !title.equals(""))
        {
            txtviewTitle.setText(title);
        }
    }

    public interface DialogListener
    {
        void onConfirmClick();
        void onCancelClick();
    }
}
