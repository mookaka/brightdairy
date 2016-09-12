package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.RadioGroup;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.HomeConfigHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.HomePagesAdapter;
import com.brightdairy.personal.brightdairy.fragment.ActivityFragment;
import com.brightdairy.personal.brightdairy.fragment.HomeFragment;
import com.brightdairy.personal.brightdairy.fragment.OrderCenterFragment;
import com.brightdairy.personal.brightdairy.fragment.ProductCategoryFragment;
import com.brightdairy.personal.brightdairy.fragment.UserFragment;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.view.NoScrollViewPager;
import com.brightdairy.personal.brightdairy.view.badgeview.BadgeRadioButton;
import com.brightdairy.personal.model.DataBase;
import com.brightdairy.personal.model.entity.HomeConfig;
import com.brightdairy.personal.model.entity.HomeContent;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends FragmentActivity
{
    private NoScrollViewPager homePagesContainer;
    private RadioGroup bottomBarGroup;
    private ArrayList<Fragment> homePages;


    private Gson homeConfigParser;
    private ArrayList<BadgeRadioButton> bottomBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homePagesContainer = (NoScrollViewPager) findViewById(R.id.viewpager_home_pages_container);
        bottomBarGroup = (RadioGroup) findViewById(R.id.radiogroup_home_bottom_bar);

        bottomBar = new ArrayList<>();

        bottomBar.add((BadgeRadioButton) findViewById(R.id.radio_home_bottom_bar_home));
        bottomBar.add((BadgeRadioButton) findViewById(R.id.radio_home_bottom_bar_product));
        bottomBar.add((BadgeRadioButton) findViewById(R.id.radio_home_bottom_bar_activity));
        bottomBar.add((BadgeRadioButton) findViewById(R.id.radio_home_bottom_bar_order));
        bottomBar.add((BadgeRadioButton) findViewById(R.id.radio_home_bottom_bar_user));

        initData();
        initListener();

        GlobalConstants.HOME_MANAGER = this;
    }

    private void initData()
    {
        homePages = new ArrayList();
        homePages.add(new HomeFragment());
        homePages.add(new ProductCategoryFragment());
        homePages.add(new ActivityFragment());
        homePages.add(new OrderCenterFragment());
        homePages.add(new UserFragment());

        FragmentPagerAdapter homePagesAdapter = new HomePagesAdapter(this.getSupportFragmentManager(), homePages);

        homePagesContainer.setAdapter(homePagesAdapter);


        if (GeneralUtils.isDateExpired("homeConfigExpireDate"))
        {
            HomeConfigHttp homeHttpService = GlobalRetrofit.getRetrofitInstance().create(HomeConfigHttp.class);

            homeHttpService.getHomeConfigByZone(null).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<DataBase<HomeConfig>>() {
                        @Override
                        public void onCompleted() {
                            initView();
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(DataBase<HomeConfig> homeConfigDataBase) {
                            if (homeConfigDataBase.msgCode == 11) {
                                PrefUtil.setLong("homeConfigExpireDate", 0);
                                PrefUtil.setString("homeConfig", null);
                            } else if (homeConfigDataBase.msgCode == 10) {
                                PrefUtil.setLong("homeConfigExpireDate", homeConfigDataBase.data.expire);
                                homeConfigParser = new Gson();
                                String homeConfig = homeConfigParser.toJson(homeConfigDataBase.data);
                                PrefUtil.setString("homeConfig", homeConfig);
                            }
                        }
                    });
        } else {
            initView();
        }


    }

    private void initView() {
        if (homeConfigParser == null)
            homeConfigParser = new Gson();

        HomeConfig homeConfig = homeConfigParser.fromJson(PrefUtil.getString("homeConfig", null), HomeConfig.class);


        if(homeConfig != null)
        {
            for (int i = 0; i < homeConfig.bottomBar.length; i++) {
                final int finalI = i;
                bottomBar.get(finalI).setText(homeConfig.bottomBar[i].titleText);
                Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                        .load(homeConfig.bottomBar[i].iconUrl)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Drawable icon = GeneralUtils.bitmap2Drawable(resource);
                                bottomBar.get(finalI).setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
                            }
                        });
            }
        }

    }

    private void initListener() {
        bottomBarGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_home_bottom_bar_home:

                        homePagesContainer.setCurrentItem(GlobalConstants.PageRelatedType.HOME_PAGE, true);

                        break;
                    case R.id.radio_home_bottom_bar_product:

                        homePagesContainer.setCurrentItem(GlobalConstants.PageRelatedType.CATEGORY_PAGE);

                        break;
                    case R.id.radio_home_bottom_bar_activity:

                        homePagesContainer.setCurrentItem(GlobalConstants.PageRelatedType.ACTIVITY_PAGE, true);

                        break;
                    case R.id.radio_home_bottom_bar_order:

                        homePagesContainer.setCurrentItem(GlobalConstants.PageRelatedType.ORDER_CENTER_PAGE, true);

                        break;
                    case R.id.radio_home_bottom_bar_user:

                        if (AppLocalUtils.isLogin())
                        {
                            homePagesContainer.setCurrentItem(GlobalConstants.PageRelatedType.USER_PAGE, true);

                        } else {
                            Intent loginIntent = new Intent(MainActivity.this, LoginSmsActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }

                        break;
                }
            }
        });
    }


    public void updateHomeIcon(String activity, HomeContent.OrderCenter orderCenter)
    {
        if(activity != null && !activity.equals(""))
        {
            bottomBar.get(GlobalConstants.PageRelatedType.ACTIVITY_PAGE).setBadgeCount(0);
            bottomBar.get(GlobalConstants.PageRelatedType.ACTIVITY_PAGE).setBadgeShown(true);
        }

        if(orderCenter != null)
        {
            bottomBar.get(GlobalConstants.PageRelatedType.ORDER_CENTER_PAGE).setBadgeCount(orderCenter.orderNum);
        }

    }



}
