package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.OperateOrderApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.ConfirmOrderAdapter;
import com.brightdairy.personal.brightdairy.popup.DialogPopup;
import com.brightdairy.personal.brightdairy.popup.DialogPopupHelper;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.brightdairy.view.ExpandBarLinearLayoutManager;
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.HttpReqBody.ConfirmOrder;
import com.brightdairy.personal.model.HttpReqBody.CreateAppOrder;
import com.brightdairy.personal.model.entity.AddrInfo;
import com.brightdairy.personal.model.entity.ConfirmOrderInfos;
import com.brightdairy.personal.model.entity.SelectedCartItem;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/22.
 */
public class ConfirmOrderActivity extends BaseActivity
{

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


    private CreateAppOrder mCreateAppOrder;

    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_confirm_orders);

        txtviewRecipient = (TextView) findViewById(R.id.txtview_confirm_order_recipient);
        imgbtnModifyAddr = (ImageButton) findViewById(R.id.imgbtn_confirm_order_modify_address_info);
        txtviewDefaultAddr = (TextView) findViewById(R.id.txtview_confirm_order_default_address);
        txtviewDefualtPhone = (TextView) findViewById(R.id.txtview_confirm_order_default_phone);
        txtviewSupplier = (TextView) findViewById(R.id.txtview_confirm_order_supplier);
        rclProductLists = (RecyclerView) findViewById(R.id.rclview_confirm_order_product_lists);
        txtviewTotalPoints = (TextView) findViewById(R.id.txtview_confirm_order_total_points);
        imgbtnPopupPointsRule = (ImageButton) findViewById(R.id.imgbtn_confirm_order_popup_points_rule);
        checkboxUsePoints = (CheckBox) findViewById(R.id.checkbox_confirm_order_if_use_points);
        addsubbtnSelectPoints = (AddSubtractionBtn) findViewById(R.id.addsubbtn_confirm_order_select_points);
        checkboxIfInstallBox = (CheckBox) findViewById(R.id.checkbox_confirm_order_if_install_box);
        txtviewTotalCost = (TextView) findViewById(R.id.txtview_confirm_order_total_cost);
        txtviewPointsSub = (TextView) findViewById(R.id.txtview_confirm_order_points_sub);
        txtviewPromoSub = (TextView) findViewById(R.id.txtview_confirm_order_promotion_sub);
        txtviewCanGetPoints = (TextView) findViewById(R.id.txtview_confirm_order_get_points);
        txtviewNeedCost = (TextView) findViewById(R.id.txtview_confirm_order_need_cost);
        btnConfirmOrder = (Button) findViewById(R.id.btn_confirm_order_confirm);
        llUserPoints = (LinearLayout) findViewById(R.id.ll_confirm_order_use_point);

        rclProductLists.setLayoutManager(new ExpandBarLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rclProductLists.addItemDecoration(new VerticalSpaceItemDecoration(15));
        rclProductLists.setNestedScrollingEnabled(false);
    }


    private CompositeSubscription mCompositeSubscription;
    private OperateOrderApi mOperateOrderApi;
    private ConfirmOrderInfos mConfirmOrderInfos;
    private ConfirmOrder mConfirmOrder;
    private String supplierId;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mOperateOrderApi = GlobalRetrofit.getRetrofitDev().create(OperateOrderApi.class);
        SelectedCartItem selectedCartItem = getIntent().getParcelableExtra("SelectedCartItemInfo");

        mConfirmOrder = new ConfirmOrder();
        mConfirmOrder.cityCode = GlobalConstants.ZONE_CODE;
        mConfirmOrder.productIds = selectedCartItem.productIds;

        fetchUsrConfirmOrderInfo();


    }

    private void fetchUsrConfirmOrderInfo()
    {
        mCompositeSubscription.add(mOperateOrderApi.getConfirmOrderInfo(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                mConfirmOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ConfirmOrderInfos>>() {
                    @Override
                    public void call(DataResult<ConfirmOrderInfos> result)
                    {

                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mConfirmOrderInfos = result.result;
                                supplierId = mConfirmOrderInfos.supplierPartyId;


                                fillviewwithdata();
                                break;
                            case GlobalHttpConfig.API_MSGCODE.NEED_RELOGIN:
                                DialogPopupHelper.showLoginPopup(ConfirmOrderActivity.this);
                                break;
                            default:
                                SuperActivityToast.create(ConfirmOrderActivity.this, result.msgText, Style.DURATION_LONG).show();
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


    private void fillviewwithdata()
    {
        rclProductLists.setAdapter(new ConfirmOrderAdapter(mConfirmOrderInfos.cartItems));

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
    protected void doThingWhenDestroy()
    {
        super.doThingWhenDestroy();
        mCompositeSubscription.clear();
    }

    @Override
    protected void initListener()
    {
        super.initListener();
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
                        Intent goToAddrModification = new Intent(ConfirmOrderActivity.this, ModifyAddressActivity.class);
                        goToAddrModification.putExtra("supplierId", supplierId);
                        startActivity(goToAddrModification);
                    }
                }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == GlobalConstants.INTENT_FLAG.RELOGIN_REQ_FLG
                && resultCode == GlobalConstants.INTENT_FLAG.RELOGIN_OK_FLG)
        {
            fetchUsrConfirmOrderInfo();
        }
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
//                Intent jumpToLogin = new Intent(ConfirmOrderActivity.this, LoginSmsActivity.class);
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
//        dialogPopup.show(getSupportFragmentManager(), "needLogin");
//
//    }
}
