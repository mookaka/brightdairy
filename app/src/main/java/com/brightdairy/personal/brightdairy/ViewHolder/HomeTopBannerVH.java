package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.view.Banner;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomeTopBannerVH extends RecyclerView.ViewHolder
{
    public Banner topCarousel;
    public RadioButton radioFreshmilk;
    public RadioButton radioYourt;
    public RadioButton radioDingnaibao;
    public RadioButton radioQuickorder;
    public ArrayList<RadioButton> topBars;

    public HomeTopBannerVH(View itemView)
    {
        super(itemView);
        topCarousel = (Banner) itemView.findViewById(R.id.banner_home_top_carousel);
        radioFreshmilk = (RadioButton) itemView.findViewById(R.id.radio_top_freshmilk);
        radioYourt = (RadioButton) itemView.findViewById(R.id.radio_top_yogurt);
        radioDingnaibao = (RadioButton) itemView.findViewById(R.id.radio_top_dingnaibao);
        radioQuickorder = (RadioButton) itemView.findViewById(R.id.radio_top_quick_order);

        topCarousel.setBannerStyle(Banner.CIRCLE_INDICATOR);
        topCarousel.setIndicatorGravity(Banner.CENTER);
        topCarousel.setDelayTime(5000);

        topBars = new ArrayList<RadioButton>();
        topBars.add(radioFreshmilk);
        topBars.add(radioYourt);
        topBars.add(radioDingnaibao);
        topBars.add(radioQuickorder);
    }
}
