package com.brightdairy.personal.brightdairy.adapter;

import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.brightdairy.personal.brightdairy.fragment.FragmentHelper;
import com.brightdairy.personal.brightdairy.fragment.OrderCenterPageFragment;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/10/8.
 */

public class OrderCenterPagesAdapter extends FragmentPagerAdapter
{

    private ArrayList<OrderCenterPageFragment> mOrderCenterPageFragments;
    private ArrayList<String> mOrderCenterPageTitles;
    private ArrayList<Integer> mOrderCenterUnIncon;
    private ArrayList<Integer> mOrderCenterIcon;

    public OrderCenterPagesAdapter(FragmentManager fm, ArrayList<OrderCenterPageFragment> orderCenterPages)
    {
        super(fm);
        this.mOrderCenterPageFragments = orderCenterPages;
        this.mOrderCenterPageTitles = FragmentHelper.getOrderCenterPageTitles();
        this.mOrderCenterUnIncon = FragmentHelper.getOrderCenterUnIcon();
        this.mOrderCenterIcon = FragmentHelper.getOrderCenterIcon();
    }



    @Override
    public Fragment getItem(int position)
    {
        return mOrderCenterPageFragments.get(position);
    }

    @Override
    public int getCount()
    {
        return mOrderCenterPageFragments.size();
    }

//    @Override
//    public Integer getPageIcon(int position)
//    {
//        return mOrderCenterUnIncon.get(position);
//    }
//
//    @Override
//    public Integer getPageSelectIcon(int position)
//    {
//        return mOrderCenterIcon.get(position);
//    }
//
//    @Override
//    public Rect getPageIconBounds(int position)
//    {
//        return null;
//    }

//    @Override
//    public CharSequence getPageTitle(int position)
//    {
//        return mOrderCenterPageTitles.get(position);
//    }
}
