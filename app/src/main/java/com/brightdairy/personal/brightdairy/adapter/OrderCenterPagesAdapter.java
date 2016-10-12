package com.brightdairy.personal.brightdairy.adapter;

import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;

/**
 * Created by shuangmusuihua on 2016/10/8.
 */

public class OrderCenterPagesAdapter extends FragmentPagerAdapter implements AdvancedPagerSlidingTabStrip.IconTabProvider
{

    public OrderCenterPagesAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        return null;
    }

    @Override
    public int getCount()
    {
        return 0;
    }

    @Override
    public <T> T getPageIcon(int position) {
        return null;
    }

    @Override
    public <T> T getPageSelectIcon(int position) {
        return null;
    }

    @Override
    public Rect getPageIconBounds(int position) {
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return super.getPageTitle(position);
    }
}
