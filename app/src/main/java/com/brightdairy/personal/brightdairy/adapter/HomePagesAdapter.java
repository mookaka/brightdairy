package com.brightdairy.personal.brightdairy.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomePagesAdapter extends FragmentPagerAdapter
{
    private List<Fragment> homePages;

    public HomePagesAdapter(FragmentManager fm, ArrayList<Fragment> homePages)
    {
        super(fm);
        this.homePages = homePages;
    }

    @Override
    public Fragment getItem(int position)
    {
        return homePages.get(position);
    }

    @Override
    public int getCount()
    {
        return homePages.size();
    }
}
