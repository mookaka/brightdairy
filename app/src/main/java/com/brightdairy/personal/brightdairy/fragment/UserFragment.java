package com.brightdairy.personal.brightdairy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.UserApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.LoginSmsActivity;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.view.badgeview.BadgeRadioButton;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.UserInfoLite;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */

public class UserFragment extends LazyLoadFragment
{
    private BadgeRadioButton shoppingCart;
    private CircleImageView userAvatar;
    private TextView userName;
    private TextView needLogin;
    private TextView userPoints;
    private TextView userDefaultAddress;
    private TextView userCoupon;
    private LinearLayout flUserService;
    private LinearLayout flUserSetting;
    private TextView userBindingMobile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        if (fragmentView == null)
        {
            fragmentView = inflater.inflate(R.layout.fragment_home_page_user, null);

            shoppingCart = (BadgeRadioButton) fragmentView.findViewById(R.id.radio_fragment_user_shopping_cart);
            userAvatar = (CircleImageView) fragmentView.findViewById(R.id.circleimg_fragment_user_avatar);
            userName = (TextView) fragmentView.findViewById(R.id.txtview_fragment_user_name);
            needLogin = (TextView) fragmentView.findViewById(R.id.txtview_fragment_need_login);
            userPoints = (TextView) fragmentView.findViewById(R.id.txtview_fragment_user_points);
            userDefaultAddress = (TextView) fragmentView.findViewById(R.id.txtxview_fragment_user_default_address);
            userCoupon = (TextView) fragmentView.findViewById(R.id.txtview_fragment_user_my_coupon);
            flUserService = (LinearLayout) fragmentView.findViewById(R.id.fl_fragment_user_service);
            flUserSetting = (LinearLayout) fragmentView.findViewById(R.id.fl_fragment_user_setting);
            userBindingMobile = (TextView) fragmentView.findViewById(R.id.txtview_fragment_user_mobile);


            initData();
            initListener();
        }

        return fragmentView;
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }


    private CompositeSubscription mCompositeSubscription;
    private UserApi userApi;
    private void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        userApi = GlobalRetrofit.getRetrofitDev().create(UserApi.class);
    }

    private void initListener()
    {

    }

    private UserInfoLite userInfoLite;
    private void fetchUserData()
    {
        showLoading();

        mCompositeSubscription.add(userApi.getLiteUserInfo(GlobalConstants.ZONE_CODE,
                GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<UserInfoLite>>() {
                    @Override
                    public void call(DataResult<UserInfoLite> result) {
                        switch (result.msgCode) {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:

                                userInfoLite = result.result;
                                hideLoading();
                                fillViewWithData();

                                break;
                            case GlobalHttpConfig.API_MSGCODE.NEED_RELOGIN:

                                hideLoading();
                                showNeedLogin();

                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        throwable.printStackTrace();
                    }
                }));
    }


    private void fillViewWithData()
    {

        userName.setVisibility(View.VISIBLE);
        needLogin.setVisibility(View.GONE);

        Glide.with(getActivity()).load(GlobalConstants.IMG_URL_BASE + userInfoLite.avatar).asBitmap().into(userAvatar);
        userName.setText(userInfoLite.userName);
        userPoints.setText(userInfoLite.points);
        userDefaultAddress.setText(userInfoLite.postalAddr);
        userCoupon.setText("已有" + userInfoLite.userCoupons + "卡券");

        if (userInfoLite.bindPhone != null && !userInfoLite.bindPhone.equals(""))
        {
            userBindingMobile.setText(userInfoLite.bindPhone);
        } else
        {
            userBindingMobile.setText("您还没有绑定手机号！");
        }

        GlobalHttpConfig.UID = userInfoLite.userLoginId;
        PrefUtil.setString(GlobalHttpConfig.HTTP_HEADER.UID, GlobalHttpConfig.UID);

    }

    private void showNeedLogin()
    {

        userName.setVisibility(View.GONE);
        needLogin.setVisibility(View.VISIBLE);


        needLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent jumpToLogin = new Intent(getActivity(), LoginSmsActivity.class);
                startActivity(jumpToLogin);
            }
        });
    }


    @Override
    protected void onFragmentInVisible()
    {
        mCompositeSubscription.clear();
    }

    @Override
    protected void onFragmentVisible()
    {
        fetchUserData();
    }
}
