package com.brightdairy.personal.brightdairy.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.HomeItemSingleVH;
import com.brightdairy.personal.brightdairy.ViewHolder.HomeItemsBannerVH;
import com.brightdairy.personal.brightdairy.ViewHolder.HomeTopBannerVH;
import com.brightdairy.personal.brightdairy.activity.WebBrowserContainerActivity;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.view.Banner;
import com.brightdairy.personal.model.entity.CarouselItem;
import com.brightdairy.personal.model.entity.HomeConfig;
import com.brightdairy.personal.model.entity.HomeContent;
import com.brightdairy.personal.model.entity.HomePageItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private HomeContent homeContent;
    private final LayoutInflater homePageInflater;

    private final int ITEM_TOP_CAROUSEL = 0;
    private final int ITEM_CAROUSEL = 1;
    private final int ITEM_SINGLE = 2;

    private HomePageItem[] homePageItems;


    private ArrayList<String> imgUrls;
    private ArrayList<String> actions;
    private ArrayList<String> actionUrls;

    private Gson homeConfigParser;
    private String homeConfig;

    public HomePageAdapter(HomeContent homeContent)
    {
        this.homeContent = homeContent;
        this.homePageItems = homeContent.homepageItems;
        this.homePageInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
        imgUrls = new ArrayList<>();
        actions = new ArrayList<>();
        actionUrls = new ArrayList<>();
        homeConfig = PrefUtil.getString("homeConfig", null);

        if(homeConfig != null && !homeConfig.equals(""))
        {
            homeConfigParser = new Gson();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case ITEM_TOP_CAROUSEL:
                View topBannerView = homePageInflater.inflate(R.layout.item_home_top_banner, null);
                return new HomeTopBannerVH(topBannerView);
            case ITEM_CAROUSEL:
                View itemCarousel = homePageInflater.inflate(R.layout.item_home_type_carousel, parent, false);
                return new HomeItemsBannerVH(itemCarousel);
            case ITEM_SINGLE:
                View itemSingle = homePageInflater.inflate(R.layout.item_home_type_single, parent, false);
                return new HomeItemSingleVH(itemSingle);
            default:
                View itemDefault = homePageInflater.inflate(R.layout.item_home_type_carousel, parent, false);
                return new HomeItemsBannerVH(itemDefault);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        imgUrls.clear();
        actionUrls.clear();
        actions.clear();

        if(holder instanceof HomeTopBannerVH)
        {

            final HomeTopBannerVH homeTopBannerVH = (HomeTopBannerVH)holder;
            HomeConfig config = null;

            for(int i = 0; i < homeContent.carousel.length; i++)
            {
                CarouselItem carouselItem = homeContent.carousel[i];
                imgUrls.add(carouselItem.imgUrl);
                actions.add(carouselItem.action);
                actionUrls.add(carouselItem.actionUrl);
            }


            if(homeConfigParser != null)
            {
               config = homeConfigParser.fromJson(homeConfig, HomeConfig.class);
            }

            if(config != null)
            {
                for(int i = 0; i < config.topBar.length; i++)
                {
                    final int topbarIndex = i;

                    homeTopBannerVH.topBars.get(topbarIndex).setText(config.topBar[topbarIndex].titleText);

                    Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                            .load(GlobalConstants.IMG_URL_BASR + config.topBar[topbarIndex].iconUrl)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    Drawable icon = GeneralUtils.bitmap2Drawable(resource);
                                    homeTopBannerVH.topBars.get(topbarIndex).setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                                }
                            });

                }
            }

            String[] imgUrlss = AppLocalUtils.fussImgUrl(imgUrls.toArray(new String[0]));
            homeTopBannerVH.topCarousel.setImages(imgUrlss);


            homeTopBannerVH.topCarousel.setOnBannerClickListener(new Banner.OnBannerClickListener()
            {
                @Override
                public void OnBannerClick(View view, int position)
                {
                    position = position - 1;
                    switch (actions.get(position))
                    {
                        case "h5":

                            Intent webBrowserIntent = new Intent(GlobalConstants.APPLICATION_CONTEXT, WebBrowserContainerActivity.class);
                            webBrowserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            webBrowserIntent.putExtra("actionUrl", actionUrls.get(position));
                            GlobalConstants.APPLICATION_CONTEXT.startActivity(webBrowserIntent);
                            break;
                    }
                }
            });

        } else {
            final HomePageItem homePageItem = homePageItems[position-1];
            CarouselItem[] itemPages;

            if(holder instanceof HomeItemsBannerVH)
            {
                final HomeItemsBannerVH homeItemsBannerVH = (HomeItemsBannerVH)holder;

                homeItemsBannerVH.itemCarouselTitle.setText(homePageItem.titleText);

                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(GlobalConstants.IMG_URL_BASR + homePageItem.leftIcon)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>()
                        {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                            {
                                final Drawable leftIcon = GeneralUtils.bitmap2Drawable(resource);

                                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                                        .load(homePageItem.rightIcon)
                                        .asBitmap()
                                        .into(new SimpleTarget<Bitmap>()
                                        {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                                            {
                                                Drawable rightIcon = GeneralUtils.bitmap2Drawable(resource);
                                                homeItemsBannerVH.itemCarouselTitle.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);
                                            }
                                        });
                            }
                        });



                itemPages = homePageItem.itemPages;

                for(int i = 0; i < itemPages.length; i++)
                {
                    CarouselItem carouselItem = itemPages[i];
                    imgUrls.add(carouselItem.imgUrl);
                    actions.add(carouselItem.action);
                    actionUrls.add(carouselItem.actionUrl);
                }

                homeItemsBannerVH.itemCarouselBanner.setImages(imgUrls.toArray(new String[] {}));

                homeItemsBannerVH.itemCarouselBanner.setOnBannerClickListener(new Banner.OnBannerClickListener()
                {
                    @Override
                    public void OnBannerClick(View view, int position)
                    {
                        switch (actions.get(position))
                        {
                            case "h5":

                                Intent webBrowserIntent = new Intent(GlobalConstants.APPLICATION_CONTEXT, WebBrowserContainerActivity.class);
                                webBrowserIntent.putExtra("actionUrl", actionUrls.get(position));
                                GlobalConstants.APPLICATION_CONTEXT.startActivity(webBrowserIntent);
                                break;
                        }
                    }
                });
            } else if(holder instanceof HomeItemSingleVH) {
                final HomeItemSingleVH homeItemSingleVH = (HomeItemSingleVH) holder;

                homeItemSingleVH.itemSingleTitle.setText(homePageItem.titleText);

                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(GlobalConstants.IMG_URL_BASR + homePageItem.leftIcon)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>()
                        {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                            {
                                final Drawable leftIcon = GeneralUtils.bitmap2Drawable(resource);
                                homeItemSingleVH.itemSingleTitle.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, leftIcon, null);

                                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                                        .load(homePageItem.rightIcon)
                                        .asBitmap()
                                        .into(new SimpleTarget<Bitmap>()
                                        {
                                            @Override
                                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                                            {
                                                Drawable rightIcon = GeneralUtils.bitmap2Drawable(resource);
                                                homeItemSingleVH.itemSingleTitle.setCompoundDrawablesWithIntrinsicBounds(leftIcon, null, rightIcon, null);
                                            }
                                        });
                            }
                        });


                itemPages = homePageItem.itemPages;

                for(int i = 0; i < itemPages.length; i++)
                {
                    CarouselItem carouselItem = itemPages[i];
                    imgUrls.add(carouselItem.imgUrl);
                    actions.add(carouselItem.action);
                    actionUrls.add(carouselItem.actionUrl);
                }

                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(GlobalConstants.IMG_URL_BASR + imgUrls.get(0))
                        .asBitmap()
                        .centerCrop()
                        .into(homeItemSingleVH.itemSingleImgLeft);

                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(GlobalConstants.IMG_URL_BASR + imgUrls.get(1))
                        .asBitmap()
                        .centerCrop()
                        .into(homeItemSingleVH.itemSingleImgTop);

                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(GlobalConstants.IMG_URL_BASR + imgUrls.get(2))
                        .asBitmap()
                        .centerCrop()
                        .into(homeItemSingleVH.itemSingleImgBottom);
            }
        }

    }

    @Override
    public int getItemCount()
    {
        return (homeContent.homepageItems.length + 1);
    }


    @Override
    public int getItemViewType(int position)
    {
        if(position == ITEM_TOP_CAROUSEL)
            return ITEM_TOP_CAROUSEL;

        HomePageItem homePageItem = homeContent.homepageItems[position-1];

        switch (homePageItem.displayType)
        {
            case "carousel":
                return ITEM_CAROUSEL;
            case "single":
                return ITEM_SINGLE;
            default:
                return ITEM_SINGLE;
        }
    }
}
