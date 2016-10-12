package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/12.
 */

public class OrderCenterPageFragment extends LazyLoadFragment
{

    public static OrderCenterPageFragment newInstance(String pageType)
    {
        OrderCenterPageFragment orderCenterPageFragment = new OrderCenterPageFragment();
        Bundle additionData = new Bundle();

        additionData.putString("pageType", pageType);

        orderCenterPageFragment.setArguments(additionData);

        return orderCenterPageFragment;

    }


    private RecyclerView orderLists;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (fragmentView == null)
        {
            fragmentView = inflater.inflate(R.layout.fragment_order_center_order_list, container);
            orderLists = (RecyclerView) fragmentView.findViewById(R.id.rclview_order_center_order_list);

            orderLists.setLayoutManager(new LinearLayoutManager(getActivity()));

            initData();

        }

        return fragmentView;
    }

    private CompositeSubscription mCompositeSubscription;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();

    }


    @Override
    protected void onFragmentInVisible()
    {

    }

    @Override
    protected void onFragmentVisible()
    {

    }
}
