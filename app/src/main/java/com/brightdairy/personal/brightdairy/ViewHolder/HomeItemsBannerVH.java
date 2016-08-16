package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.view.Banner;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomeItemsBannerVH extends RecyclerView.ViewHolder
{

    public TextView itemCarouselTitle;
    public Banner itemCarouselBanner;

    public HomeItemsBannerVH(View itemView)
    {
        super(itemView);

        itemCarouselTitle = (TextView) itemView.findViewById(R.id.item_home_carousel_title);
        itemCarouselBanner = (Banner) itemView.findViewById(R.id.item_home_carousel_banner);

        itemCarouselBanner.setDelayTime(5000);
        itemCarouselBanner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        itemCarouselBanner.setIndicatorGravity(Banner.CENTER);

    }
}
