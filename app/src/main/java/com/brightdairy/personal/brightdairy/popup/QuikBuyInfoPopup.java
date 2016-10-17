package com.brightdairy.personal.brightdairy.popup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.pg.PG;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.OperateOrderApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.ModifyAddressActivity;
import com.brightdairy.personal.brightdairy.adapter.ConfirmOrderAdapter;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.brightdairy.view.ExpandBarLinearLayoutManager;
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.AddrInfo;
import com.brightdairy.personal.model.entity.ConfirmOrderInfos;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/9.
 */

public class QuikBuyInfoPopup extends BasePopup
{
    private View mQuickBuyInfoView;
    private TextView txtviewRecipient;
    private ImageButton imgbtnModifyAddr;
    private TextView txtviewDefaultAddr;
    private TextView txtviewDefualtPhone;
    private TextView txtviewSupplier;
    private RecyclerView rclProductLists;
    private TextView txtviewTotalPoints;
    private ImageButton imgbtnPopupPointsRule;
    private CheckBox checkboxUsePoints;
    private AddSubtractionBtn addsubbtnSelectPoints;
    private CheckBox checkboxIfInstallBox;
    private TextView txtviewTotalCost;
    private TextView txtviewPointsSub;
    private TextView txtviewPromoSub;
    private TextView txtviewCanGetPoints;
    private TextView txtviewNeedCost;
    private Button btnConfirmOrder;
    private LinearLayout llUserPoints;



    public static QuikBuyInfoPopup newInstance(ProductSendInfo productSendInfo)
    {
        QuikBuyInfoPopup quikBuyInfoPopup = new QuikBuyInfoPopup();

        Bundle additionData = new Bundle();
        additionData.putParcelable("quickBuyInfo", PG.convertParcelable(productSendInfo));
        quikBuyInfoPopup.setArguments(additionData);

        return quikBuyInfoPopup;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        mQuickBuyInfoView = inflater.inflate(R.layout.popup_quick_buy_info, container, false);

        txtviewRecipient = (TextView) mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_recipient);
        imgbtnModifyAddr = (ImageButton)  mQuickBuyInfoView.findViewById(R.id.imgbtn_confirm_order_modify_address_info);
        txtviewDefaultAddr = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_default_address);
        txtviewDefualtPhone = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_default_phone);
        txtviewSupplier = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_supplier);
        rclProductLists = (RecyclerView)  mQuickBuyInfoView.findViewById(R.id.rclview_confirm_order_product_lists);
        txtviewTotalPoints = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_total_points);
        imgbtnPopupPointsRule = (ImageButton)  mQuickBuyInfoView.findViewById(R.id.imgbtn_confirm_order_popup_points_rule);
        checkboxUsePoints = (CheckBox)  mQuickBuyInfoView.findViewById(R.id.checkbox_confirm_order_if_use_points);
        addsubbtnSelectPoints = (AddSubtractionBtn)  mQuickBuyInfoView.findViewById(R.id.addsubbtn_confirm_order_select_points);
        checkboxIfInstallBox = (CheckBox)  mQuickBuyInfoView.findViewById(R.id.checkbox_confirm_order_if_install_box);
        txtviewTotalCost = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_total_cost);
        txtviewPointsSub = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_points_sub);
        txtviewPromoSub = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_promotion_sub);
        txtviewCanGetPoints = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_get_points);
        txtviewNeedCost = (TextView)  mQuickBuyInfoView.findViewById(R.id.txtview_confirm_order_need_cost);
        btnConfirmOrder = (Button)  mQuickBuyInfoView.findViewById(R.id.btn_confirm_order_confirm);
        llUserPoints = (LinearLayout)  mQuickBuyInfoView.findViewById(R.id.ll_confirm_order_use_point);

        rclProductLists.setLayoutManager(new ExpandBarLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rclProductLists.addItemDecoration(new VerticalSpaceItemDecoration(15));
        rclProductLists.setNestedScrollingEnabled(false);

        return mQuickBuyInfoView;
    }


    @Override
    protected void customizePopupView(Window thisWindow)
    {
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        thisWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        thisWindow.setGravity(Gravity.BOTTOM);
    }

    private CompositeSubscription mCompositeSubscription;
    private OperateOrderApi mOperateOrderApi;
    private ProductSendInfo mProductSendInfo;
    private ConfirmOrderInfos mConfirmOrderInfos;
    private String supplierId;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mOperateOrderApi = GlobalRetrofit.getRetrofitDev().create(OperateOrderApi.class);

        mProductSendInfo = getArguments().getParcelable("quickBuyInfo");


        mCompositeSubscription.add(mOperateOrderApi.quickBuyInfo(
                GlobalConstants.ZONE_CODE, GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                mProductSendInfo).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ConfirmOrderInfos>>()
                {
                    @Override
                    public void call(DataResult<ConfirmOrderInfos> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mConfirmOrderInfos = result.result;
                                supplierId = mConfirmOrderInfos.supplierPartyId;
                                fillViewWithData();
                                break;
                            case GlobalHttpConfig.API_MSGCODE.NEED_RELOGIN:
                                DialogPopupHelper.showLoginPopup(getActivity());
                                dismiss();
                                break;
                            default:
                                GeneralUtils.showToast(getActivity(), result.msgText);
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


    @Override
    protected void initListener()
    {
        mCompositeSubscription.add(RxCompoundButton.checkedChanges(checkboxUsePoints)
                .subscribe(new Action1<Boolean>()
                {
                    @Override
                    public void call(Boolean aBoolean)
                    {
                        if (aBoolean)
                        {
                            llUserPoints.setVisibility(View.VISIBLE);
                        } else
                        {
                            llUserPoints.setVisibility(View.GONE);
                        }
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(imgbtnModifyAddr)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent goToAddrModification = new Intent(getActivity(), ModifyAddressActivity.class);
                        goToAddrModification.putExtra("supplierId", supplierId);
                        startActivity(goToAddrModification);
                    }
                }));
    }

    private void fillViewWithData()
    {
        rclProductLists.setAdapter(new ConfirmOrderAdapter(getActivity(), mConfirmOrderInfos.cartItems));

        if (mConfirmOrderInfos.defaultAddr != null)
        {
            AddrInfo defaultAddr = mConfirmOrderInfos.defaultAddr;
            txtviewRecipient.setText(defaultAddr.toName);
            txtviewDefualtPhone.setText(defaultAddr.mobile);
            txtviewDefaultAddr.setText(defaultAddr.city + "市" + defaultAddr.county + defaultAddr.town + defaultAddr.street );
        }

        txtviewSupplier.setText(mConfirmOrderInfos.cartItems.get(0).cartItem.supplierName);
        txtviewTotalPoints.setText("共有" + String.valueOf(mConfirmOrderInfos.availablePoints) + "积分");
        txtviewTotalCost.setText(String.valueOf(mConfirmOrderInfos.orderTotalAmt + "元"));
    }

    @Override
    protected void clearResOnDestroyView()
    {
        mCompositeSubscription.clear();
        super.clearResOnDestroyView();
    }

//    private void showNeedLoginPopup()
//    {
//
//        DialogPopup dialogPopup = DialogPopup.newInstance(
//                getString(R.string.need_login),
//                getString(R.string.confirm_login),
//                getString(R.string.cancel_login));
//
//        dialogPopup.setDialogListener(new DialogPopup.DialogListener()
//        {
//            @Override
//            public void onConfirmClick()
//            {
//                Intent jumpToLogin = new Intent(getActivity(), LoginSmsActivity.class);
//                jumpToLogin.putExtra(GlobalConstants.INTENT_FLAG.NEED_RELOGIN, true);
//                startActivityForResult(jumpToLogin, GlobalConstants.INTENT_FLAG.RELOGIN_REQ_FLG);
//            }
//
//            @Override
//            public void onCancelClick()
//            {
//
//            }
//        });
//
//        dialogPopup.show(getActivity().getSupportFragmentManager(), "needLogin");
//
//    }
}
