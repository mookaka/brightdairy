package com.brightdairy.personal.brightdairy.popup;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baoyz.pg.PG;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.model.Event.SendModeChangeEvent;
import com.brightdairy.personal.model.Event.SendTimeChangeEvent;
import com.brightdairy.personal.model.Event.ShopCartEditSendModeEvent;
import com.brightdairy.personal.model.Event.UnitQuantityChangeEvent;
import com.brightdairy.personal.model.Event.UpdateSendInfoEvent;
import com.brightdairy.personal.model.HttpReqBody.UpdateSendInfo;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/10/9.
 */

public class ShopCartSendModePopup extends BasePopup
{
    private View editSenInfoPopupView;
    private ImageView productImg;
    private TextView productName;
    private TextView productPrice;
    private RadioGroup radiogpSendMode;
    private RadioGroup radiogpSendModeOther;
    private RadioGroup radiogpSendTime;
    private Button btnShowCanlendar; //现在发出改变配送模式消息，日历做好后显示日历，将由日历发出事件。
    private AddSubtractionBtn addSubtractionBtn;


    public static ShopCartSendModePopup newInstance(ShopCartEditSendModeEvent shopCartEditSendModeEvent)
    {
        ShopCartSendModePopup shopCartSendModePopup = new ShopCartSendModePopup();

        Bundle oldSendMode = new Bundle();
        oldSendMode.putParcelable("editSendInfoEvent", PG.convertParcelable(shopCartEditSendModeEvent));
        shopCartSendModePopup.setArguments(oldSendMode);

        return  shopCartSendModePopup;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        editSenInfoPopupView = inflater.inflate(R.layout.popup_shop_cart_edit_send_info, container, false);

        productImg = (ImageView) editSenInfoPopupView.findViewById(R.id.imgview_popup_product_img);
        productName = (TextView) editSenInfoPopupView.findViewById(R.id.txtview_popup_product_name);
        productPrice = (TextView) editSenInfoPopupView.findViewById(R.id.txtview_popup_product_price);
        addSubtractionBtn = (AddSubtractionBtn) editSenInfoPopupView.findViewById(R.id.addsubbtn_popup_per_nums);
        radiogpSendMode = (RadioGroup) editSenInfoPopupView.findViewById(R.id.rdgroup_popup_send_mode);
        radiogpSendModeOther = (RadioGroup) editSenInfoPopupView.findViewById(R.id.rdgroup_popup_send_mode_other);
        radiogpSendTime = (RadioGroup) editSenInfoPopupView.findViewById(R.id.rdgroup_popup_send_time);
        btnShowCanlendar = (Button) editSenInfoPopupView.findViewById(R.id.btn_popup_show_canlendar);

        return editSenInfoPopupView;
    }

    private CompositeSubscription mCompositeSubscription;
    private RxBus mRxBus;
    private UpdateSendInfo mUpdateSendInfo;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();
        mUpdateSendInfo = new UpdateSendInfo();

        Bundle additionData = getArguments();

        ShopCartEditSendModeEvent shopCartEditSendModeEvent = additionData.getParcelable("editSendInfoEvent");
        CartItem cartItem = shopCartEditSendModeEvent.mCartItemInfo;
        mUpdateSendInfo.itemSeqId = shopCartEditSendModeEvent.itemSeqId;


        mUpdateSendInfo.startDate = cartItem.startDate;
        mUpdateSendInfo.endate = cartItem.endate;
        mUpdateSendInfo.shipModuleType = cartItem.shipModuleType;
        mUpdateSendInfo.shipModuleName = cartItem.shipModuleName;
        mUpdateSendInfo.shipModuleId = cartItem.shipModuleId;
        mUpdateSendInfo.unitQuantity = cartItem.unitQuantity;
        mUpdateSendInfo.productId = cartItem.productId;
        mUpdateSendInfo.shipModuleStr = cartItem.shipModuleStr;


        fillViewWithData(shopCartEditSendModeEvent.mCartItemInfo);
    }


    @Override
    protected void initListener()
    {
        addSubtractionBtn.setAddSubBtnListener(new AddSubtractionBtn.AddSubBtnListener() {
            @Override
            public void addSubBtnClick(int curValue) {
                mUpdateSendInfo.unitQuantity = curValue;
            }
        });

        mCompositeSubscription.add(RxRadioGroup.checkedChanges(radiogpSendMode)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        switch (integer) {
                            case R.id.radio_popup_everyday:
                            case R.id.radio_popup_singleday:
                            case R.id.radio_popup_triday:

                                radiogpSendModeOther.setVisibility(View.GONE);
                                afterSendModeChangeHandler(integer);

                                break;
                            case R.id.radio_popup_other:
                                    radiogpSendModeOther.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }));

        mCompositeSubscription.add(RxRadioGroup.checkedChanges(radiogpSendModeOther)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        afterSendModeChangeHandler(integer);
                    }
                }));

        mCompositeSubscription.add(RxRadioGroup.checkedChanges(radiogpSendTime)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer integer)
                    {
                        afterSendTimeChangeHandler(integer);
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(btnShowCanlendar)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mRxBus.hasObservers())
                        {
                            UpdateSendInfoEvent updateSendInfoEvent = new UpdateSendInfoEvent();
                            updateSendInfoEvent.mUpdateSendInfo = mUpdateSendInfo;
                            mRxBus.dispatchEvent(updateSendInfoEvent);
                            dismiss();
                        }
                    }
                }));
    }

    private void afterSendModeChangeHandler(int checkedId) {

        if (mRxBus.hasObservers()) {
            switch (checkedId) {
                case R.id.radio_popup_everyday:
                    refreshSendModeInfo("66", "每日送", "M");
                    break;
                case R.id.radio_popup_singleday:
                    refreshSendModeInfo("63", "单日送", "M");
                    break;
                case R.id.radio_popup_triday:
                    refreshSendModeInfo("49", "三日送", "M");
                    break;
                case R.id.radio_popup_twiceday:
                    refreshSendModeInfo("64", "双日送", "M");
                    break;
                case R.id.radio_popup_workday:
                    refreshSendModeInfo("73", "工作日送", "W");
                    break;
                case R.id.radio_popup_weekenday:
                    refreshSendModeInfo("65", "周末送", "W");
                    break;

            }
        }
    }

    private void refreshSendModeInfo(String id, String name, String type)
    {
        mUpdateSendInfo.shipModuleType =type;
        mUpdateSendInfo.shipModuleStr = "";
        mUpdateSendInfo.shipModuleName = name;
        mUpdateSendInfo.shipModuleId = id;
    }

    private void afterSendTimeChangeHandler(int checkedId)
    {
        if (mRxBus.hasObservers())
        {
            switch (checkedId)
            {
                case R.id.radio_popup_arbitrary:
                    refreshSendTime(AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NOW),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH_FROM_NOW));
                    break;
                case R.id.radio_popup_nextmonth:
                    refreshSendTime(AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_TWO_MONTH));
                    break;
                case R.id.radio_popup_nexttwomonth:
                    refreshSendTime(AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_THREE_MONTH));
                    break;
                case R.id.radio_popup_nextthreemonth:
                    refreshSendTime(AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_THREE_MONTH));
                    break;

            }
        }
    }


    private void refreshSendTime(String startDate, String endDate)
    {
        mUpdateSendInfo.startDate = startDate;
        mUpdateSendInfo.endate = endDate;
    }

    @Override
    protected void clearResOnDestroyView()
    {
        super.clearResOnDestroyView();
        addSubtractionBtn.unSubscribe();
        mCompositeSubscription.clear();
    }

    private void fillViewWithData(CartItem info)
    {
        if (info != null)
        {
            Glide.with(getActivity()).load(GlobalConstants.IMG_URL_BASE + info.itemImg).asBitmap().into(productImg);
            productName.setText(info.productName);
            productPrice.setText("随心订价：" + info.price + "元");
            addSubtractionBtn.setCurrentValue(info.unitQuantity);

            switch (info.shipModuleName)
            {
                case "每日送":

                    radiogpSendMode.check(R.id.radio_popup_everyday);

                    break;
                case "单日送":

                    radiogpSendMode.check(R.id.radio_popup_singleday);

                    break;
                case "三日送":

                    radiogpSendMode.check(R.id.radio_popup_triday);

                    break;
                case "双日送":
                    radiogpSendMode.setVisibility(View.VISIBLE);
                    radiogpSendMode.check(R.id.radio_popup_other);
                    radiogpSendModeOther.check(R.id.radio_popup_twiceday);
                    break;
                case "工作日送":
                    radiogpSendMode.setVisibility(View.VISIBLE);
                    radiogpSendMode.check(R.id.radio_popup_workday);
                    radiogpSendModeOther.check(R.id.radio_popup_twiceday);
                    break;
                case "周末送":
                    radiogpSendMode.setVisibility(View.VISIBLE);
                    radiogpSendMode.check(R.id.radio_popup_weekenday);
                    radiogpSendModeOther.check(R.id.radio_popup_twiceday);
                    break;
            }
        }
    }
}
