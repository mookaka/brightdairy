package com.brightdairy.personal.brightdairy.popup;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.PopupCitySelectorAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.CityChangedEvent;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/29.
 */

public class CitySelectorPopup extends BasePopup
{

    private ImageButton imgbtnClosePopup;
    private TextView txtviewCurrentSelectedCity;
    private RecyclerView rclviewAllAvailableCities;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        View citySelectorView = inflater.inflate(R.layout.popup_city_selector, null);

        imgbtnClosePopup = (ImageButton) citySelectorView.findViewById(R.id.imgbtn_city_selector_popup_close);
        txtviewCurrentSelectedCity = (TextView) citySelectorView.findViewById(R.id.txtview_city_selector_current_selected_city);
        rclviewAllAvailableCities = (RecyclerView) citySelectorView.findViewById(R.id.rclview_city_selector_all_available_cities);

        rclviewAllAvailableCities.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rclviewAllAvailableCities.hasFixedSize();

        return citySelectorView;

    }

    private CompositeSubscription mCompositeSubscription;
    private RxBus mRxBus;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();
        handleRxBusEvent();
        txtviewCurrentSelectedCity.setText(GlobalConstants.CURR_ZONE_CN_NAME);
        rclviewAllAvailableCities.setAdapter(new PopupCitySelectorAdapter(getActivity()));
    }

    private void handleRxBusEvent()
    {
        mCompositeSubscription.add(mRxBus.EventDispatcher()
                .subscribe(new Action1<Object>()
                {
                    @Override
                    public void call(Object Event)
                    {
                        if (Event instanceof CityChangedEvent)
                        {
                            txtviewCurrentSelectedCity.setText(GlobalConstants.CURR_ZONE_CN_NAME);
                        }
                    }
                }));
    }


    @Override
    protected void initListener()
    {
        mCompositeSubscription.add(RxView.clicks(imgbtnClosePopup)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        dismiss();
                    }
                }));
    }
}
