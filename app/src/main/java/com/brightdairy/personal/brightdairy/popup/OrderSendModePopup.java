package com.brightdairy.personal.brightdairy.popup;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.ProductDetailActivity;
import com.brightdairy.personal.brightdairy.adapter.PopupSelectVolAdapter;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.brightdairy.view.BitmapFitScreenTransform;
import com.brightdairy.personal.model.Event.SendModeChangeEvent;
import com.brightdairy.personal.model.Event.SendTimeChangeEvent;
import com.brightdairy.personal.model.Event.UnitQuantityChangeEvent;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/23.
 */
public class OrderSendModePopup extends BasePopup {

    private ImageView productImg;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView volSelectors;
    private RadioGroup radiogpSendMode;
    private RadioGroup radiogpSendModeOther;
    private RadioGroup radiogpSendTime;
    private AddSubtractionBtn addSubtractionBtn;

    private ProductDetail productDetail;
    private ProductSendInfo productSendModeInfo;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View sendModeSelectorView = inflater.inflate(R.layout.popup_product_detail_selector, container, true);

        productImg = (ImageView) sendModeSelectorView.findViewById(R.id.imgview_popup_product_img);
        productName = (TextView) sendModeSelectorView.findViewById(R.id.txtview_popup_product_name);
        productPrice = (TextView) sendModeSelectorView.findViewById(R.id.txtview_popup_product_price);
        volSelectors = (RecyclerView) sendModeSelectorView.findViewById(R.id.rclview_popup_vol_selector);
        addSubtractionBtn = (AddSubtractionBtn) sendModeSelectorView.findViewById(R.id.addsubbtn_popup_per_nums);
        radiogpSendMode = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_mode);
        radiogpSendModeOther = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_mode_other);
        radiogpSendTime = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_time);

        volSelectors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        volSelectors.addItemDecoration(new SpaceItemDecoration(20));
        volSelectors.hasFixedSize();


        return sendModeSelectorView;
    }


    private PopupSelectVolAdapter mPopupSelectVolAdapter;
    private CompositeSubscription mCompositeSubscription;
    private RxBus mRxBus;

    @Override
    protected void initData() {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        productDetail = ((ProductDetailActivity) getActivity()).productDetail;
        productSendModeInfo = ((ProductDetailActivity) getActivity()).mProductSendModeInfo;

        mPopupSelectVolAdapter = new PopupSelectVolAdapter(productDetail.productAssoc, productDetail.productId);
        volSelectors.setAdapter(mPopupSelectVolAdapter);
        fillViewWithData(productDetail,productSendModeInfo);
    }

    @Override
    protected void initListener() {
        addSubtractionBtn.setAddSubBtnListener(new AddSubtractionBtn.AddSubBtnListener() {
            @Override
            public void addSubBtnClick(int curValue) {
                if (mRxBus.hasObservers()) {
                    mRxBus.dispatchEvent(new UnitQuantityChangeEvent(curValue));
                }
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

                                if (radiogpSendModeOther.getVisibility() == View.VISIBLE)
                                    radiogpSendModeOther.setVisibility(View.GONE);
                                afterSendModeChangeHandler(integer);

                                break;
                            case R.id.radio_popup_other:
                                if (radiogpSendModeOther.getVisibility() == View.GONE)
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
    }

    private void afterSendModeChangeHandler(int checkedId) {

        if (mRxBus.hasObservers()) {
            switch (checkedId) {
                case R.id.radio_popup_everyday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("66", "每日送", "M"));
                    break;
                case R.id.radio_popup_singleday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("63", "单日送", "M"));
                    break;
                case R.id.radio_popup_triday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("49", "三日送", "M"));
                    break;
                case R.id.radio_popup_twiceday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("64", "双日送", "M"));
                    break;
                case R.id.radio_popup_workday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("73", "工作日送", "W"));
                    break;
                case R.id.radio_popup_weekenday:
                    mRxBus.dispatchEvent(new SendModeChangeEvent("65", "周末送", "W"));
                    break;

            }
        }
    }

    private void afterSendTimeChangeHandler(int checkedId)
    {
        if (mRxBus.hasObservers())
        {
            switch (checkedId)
            {
                case R.id.radio_popup_arbitrary:
                    mRxBus.dispatchEvent(new SendTimeChangeEvent(AppLocalUtils
                            .getDateWithOffset(AppLocalUtils.DATE_NOW),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH_FROM_NOW)));
                    break;
                case R.id.radio_popup_nextmonth:
                    mRxBus.dispatchEvent(new SendTimeChangeEvent(AppLocalUtils
                            .getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_TWO_MONTH)));
                    break;
                case R.id.radio_popup_nexttwomonth:
                    mRxBus.dispatchEvent(new SendTimeChangeEvent(AppLocalUtils
                            .getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_THREE_MONTH)));
                    break;
                case R.id.radio_popup_nextthreemonth:
                    mRxBus.dispatchEvent(new SendTimeChangeEvent(AppLocalUtils
                            .getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH),
                            AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_THREE_MONTH)));
                    break;

            }
        }
    }

    @Override
    protected void clearResOnDestroyView()
    {
        super.clearResOnDestroyView();
        addSubtractionBtn.unSubscribe();
        mCompositeSubscription.clear();
    }

    private void fillViewWithData(ProductDetail productDetail, ProductSendInfo productSendModeInfo)
    {
        if(this.productDetail != null)
        {
            productName.setText(this.productDetail.productName);
            productPrice.setText("随心订价：" + this.productDetail.prices.basePrice + "元");
            Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                    .load(GlobalConstants.IMG_URL_BASE + this.productDetail.guessImgUrl)
                    .asBitmap()
                    .transform(new BitmapFitScreenTransform(GlobalConstants.APPLICATION_CONTEXT))
                    .into(productImg);

        }

        if (this.productSendModeInfo != null)
        {
            addSubtractionBtn.setCurrentValue(this.productSendModeInfo.unitQuantity);

            switch (this.productSendModeInfo.shipModuleName)
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


    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration
    {
        private int space;

        public SpaceItemDecoration(float space)
        {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, space, GlobalConstants.APPLICATION_CONTEXT.getResources().getDisplayMetrics());
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            outRect.right = space;
        }
    }


    public void freshPopupData(ProductDetail productDetail, ProductSendInfo productSendModeInfo)
    {
        fillViewWithData(productDetail, productSendModeInfo);

        if (mPopupSelectVolAdapter != null && productDetail != null)
            mPopupSelectVolAdapter.setCanChooseVol(true);
    }


}
