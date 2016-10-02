package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.HomePageHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.MainActivity;
import com.brightdairy.personal.brightdairy.adapter.HomePageAdapter;
import com.brightdairy.personal.brightdairy.popup.CitySelectorPopup;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.badgeview.BadgeRadioButton;
import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.CityChangedEvent;
import com.brightdairy.personal.model.entity.HomeContent;
import com.jakewharton.rxbinding.view.RxView;


import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomeFragment extends Fragment
{
    private View homePage;
    private TextView txtviewShowSelectCity;
    private RecyclerView homeContentContainer;
    private HomeContent homeContent;
    private BadgeRadioButton homeTopSearchCart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        homePage = inflater.inflate(R.layout.fragment_home_page_home, null);
        homeContentContainer = (RecyclerView) homePage.findViewById(R.id.rclview_home_page_container);
        homeTopSearchCart = (BadgeRadioButton)homePage.findViewById(R.id.search_shopping_cart);
        txtviewShowSelectCity = (TextView) homePage.findViewById(R.id.txtview_home_show_or_select_city);

        homeContentContainer.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        initData();
        initListener();
        return homePage;
    }


    @Override
    public void onDestroy()
    {
        mCompositeSubscription.clear();
        super.onDestroy();
    }


    private CompositeSubscription mCompositeSubscription;
    private HomePageHttp homeHttpService;
    private RxBus mRxBus;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        homeHttpService = GlobalRetrofit.getRetrofitDev().create(HomePageHttp.class);

        handleRxBudEvent();

        freshPageData();
    }

    private void handleRxBudEvent()
    {
        mCompositeSubscription.add(mRxBus.EventDispatcher()
                .subscribe(new Action1<Object>()
                {
                    @Override
                    public void call(Object Event)
                    {
                        if (Event instanceof CityChangedEvent)
                        {
                            freshPageData();
                        }
                    }
                }));
    }


    private void freshPageData()
    {
        mCompositeSubscription.add(homeHttpService.getHomeContent(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,GlobalConstants.ZONE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<HomeContent>>() {
                    @Override
                    public void onCompleted()
                    {
                        inflatePage();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<HomeContent> homeContentDataBase)
                    {
                        homeContent = homeContentDataBase.result;
                    }
                }));
    }

    private CitySelectorPopup citySelectorPopup;
    private void initListener()
    {
        mCompositeSubscription.add(RxView.clicks(txtviewShowSelectCity)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (citySelectorPopup == null)
                        {
                            citySelectorPopup = new CitySelectorPopup();
                        }

                        citySelectorPopup.show(getActivity().getFragmentManager(), "citySelectorPopup");
                    }
                }));
    }

    private void inflatePage()
    {
        txtviewShowSelectCity.setText(GlobalConstants.CURR_ZONE_CN_NAME);

        ((MainActivity)this.getActivity()).updateHomeIcon(homeContent.activity, homeContent.orderCenter);

        if(homeContent.shoppingCart != null && !homeContent.shoppingCart.equals(""))
        {
            homeTopSearchCart.setBadgeShown(true);
        }
        else {
            homeTopSearchCart.setBadgeShown(false);
        }

        homeContentContainer.setAdapter(new HomePageAdapter(homeContent));

    }

}
