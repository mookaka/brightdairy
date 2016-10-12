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
 * Created by shuangmusuihua on 2016/10/11.
 */

public class ShowInfoDialog extends BasePopup
{



    public static ShowInfoDialog newInstance(String info, String... btnTitle)
    {
        ShowInfoDialog showInfoPopup = new ShowInfoDialog();

        Bundle additionData = new Bundle();
        additionData.putString("info", info);

        if (btnTitle != null && btnTitle.length != 0)
        {
            additionData.putString("btnTitle", btnTitle[0]);
        }

        showInfoPopup.setArguments(additionData);

        return showInfoPopup;
    }



    private View showInfoPopupView;
    private TextView txtviewShowInfo;
    private TextView txtviewKnowInfo;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        showInfoPopupView = inflater.inflate(R.layout.popup_info_indication, container);
        txtviewShowInfo = (TextView) showInfoPopupView.findViewById(R.id.txtview_popup_info);
        txtviewKnowInfo = (TextView) showInfoPopupView.findViewById(R.id.txtview_popup_know_info_btn);
        return showInfoPopupView;
    }

    private CompositeSubscription mCompositeSubscription;
    @Override
    protected void initData()
    {
        Bundle additionData = getArguments();
        txtviewShowInfo.setText(additionData.getString("info"));
        txtviewKnowInfo.setText(additionData.getString("btnTitle", "知道了"));

        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void initListener()
    {
        mCompositeSubscription.add(RxView.clicks(txtviewKnowInfo)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mInfoDialogListener != null)
                        {
                            mInfoDialogListener.afterReadInfo();
                        }

                        dismiss();
                    }
                }));
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


    private InfoDialogListener mInfoDialogListener;
    public void setAfterReadInfoListener(InfoDialogListener infoListener)
    {
        this.mInfoDialogListener = infoListener;
    }

    public interface InfoDialogListener
    {
        void afterReadInfo();
    }
}
