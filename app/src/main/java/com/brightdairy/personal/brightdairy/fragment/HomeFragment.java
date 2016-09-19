package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.HomePageHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.MainActivity;
import com.brightdairy.personal.brightdairy.adapter.HomePageAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.view.badgeview.BadgeRadioButton;
import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.HomeContent;


import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomeFragment extends Fragment
{
    private View homePage;
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
        homeContentContainer.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        initData();
        return homePage;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    private void initData()
    {

        HomePageHttp homeHttpService = GlobalRetrofit.getRetrofitDev().create(HomePageHttp.class);

        homeHttpService.getHomeContent(GlobalConstants.ZONE_CODE)
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
                });
    }

    private void inflatePage()
    {
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
