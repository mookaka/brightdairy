package com.brightdairy.personal.brightdairy.popup;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.ProductDetailActivity;
import com.brightdairy.personal.brightdairy.adapter.PopupSelectVolAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.widget.RxCheckedTextView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.ArrayList;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/23.
 */
public class OrderSendModePopup extends BasePopup
{

    private ImageView productImg;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView volSelectors;
    private RadioGroup radiogpSendMode;
    private RadioGroup radiogpSendModeOther;
    private RadioGroup radiogpSendTime;
    private AddSubtractionBtn addSubtractionBtn;

    private ProductDetail productDetail;


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        View sendModeSelectorView = inflater.inflate(R.layout.popup_product_detail_selector, container, true);

        productImg = (ImageView)sendModeSelectorView.findViewById(R.id.imgview_popup_product_img);
        productName = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_name);
        productPrice = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_price);
        volSelectors = (RecyclerView)sendModeSelectorView.findViewById(R.id.rclview_popup_vol_selector);
        addSubtractionBtn = (AddSubtractionBtn)sendModeSelectorView.findViewById(R.id.addsubbtn_popup_per_nums);
        radiogpSendMode = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_mode);
        radiogpSendModeOther = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_mode_other);
        radiogpSendTime = (RadioGroup) sendModeSelectorView.findViewById(R.id.rdgroup_popup_send_time);

        volSelectors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        volSelectors.addItemDecoration(new SpaceItemDecoration(20));
        volSelectors.hasFixedSize();


        return sendModeSelectorView;
    }



    private PopupSelectVolAdapter mPopupSelectVolAdapter;
    @Override
    protected void initData()
    {
        productDetail = ((ProductDetailActivity)getActivity()).productDetail;
        mPopupSelectVolAdapter = new PopupSelectVolAdapter(productDetail.productAssoc);
        fillViewWithData(productDetail);
    }

    @Override
    protected void clearResOnDestroyView()
    {
        super.clearResOnDestroyView();
        addSubtractionBtn.unSubscribe();
    }

    private void fillViewWithData(ProductDetail productDetail)
    {
        if(productDetail != null)
        {
            productName.setText(productDetail.productName);
            productPrice.setText("随心订价：" + productDetail.prices.basePrice + "元");
            Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(GlobalConstants.IMG_URL_BASR + productDetail.guessImgUrl).asBitmap().into(productImg);

            volSelectors.setAdapter(mPopupSelectVolAdapter);

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


    public void freshPopupData(ProductDetail productDetail)
    {
        fillViewWithData(productDetail);

        if (mPopupSelectVolAdapter != null && productDetail != null)
            mPopupSelectVolAdapter.setCanChooseVol(true);
    }


}
