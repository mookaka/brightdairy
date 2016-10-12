package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.OrderCenterPagesAdapter;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class OrderCenterFragment extends Fragment
{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private View orderCenterView;
    private AdvancedPagerSlidingTabStrip pagesTab;
    private ViewPager orderCenterPageContainer;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        orderCenterView = inflater.inflate(R.layout.fragment_home_page_order_center, null);
        pagesTab = (AdvancedPagerSlidingTabStrip) orderCenterView.findViewById(R.id.tabs_fragment_order_center_tab);
        orderCenterPageContainer = (ViewPager) orderCenterView.findViewById(R.id.viewpager_fragment_order_center_pages);

        initData();

        return orderCenterView;
    }


    private CompositeSubscription mCompositeSubscription;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        orderCenterPageContainer.setAdapter(new OrderCenterPagesAdapter(getChildFragmentManager()));
        pagesTab.setViewPager(orderCenterPageContainer);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
