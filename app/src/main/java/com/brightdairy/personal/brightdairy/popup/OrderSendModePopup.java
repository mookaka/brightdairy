package com.brightdairy.personal.brightdairy.popup;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.ProductDetailActivity;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.bumptech.glide.Glide;

/**
 * Created by shuangmusuihua on 2016/8/23.
 */
public class OrderSendModePopup extends DialogFragment
{

    private ImageView productImg;
    private TextView productName;
    private TextView productPrice;
    private ProductDetail productDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View sendModeSelectorView = inflater.inflate(R.layout.popup_product_detail_selector, container, true);

        productImg = (ImageView)sendModeSelectorView.findViewById(R.id.imgview_popup_product_img);
        productName = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_name);
        productPrice = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_price);

        return sendModeSelectorView;
    }


    @Override
    public void onResume()
    {
        Window thisWindow = getDialog().getWindow();
        thisWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        thisWindow.setLayout((int) (displayMetrics.widthPixels * 0.9), (int) (displayMetrics.heightPixels * 0.8));
        thisWindow.setGravity(Gravity.BOTTOM);
        initData();
        super.onResume();
    }


    private void initData()
    {
        productDetail = ((ProductDetailActivity)getActivity()).productDetail;

        if(productDetail != null)
        {
            productName.setText(productDetail.productName);
            productPrice.setText("随心订价：" + productDetail.prices.basePrice);
            Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(GlobalConstants.IMG_URL_BASR + productDetail.guessImgUrl).asBitmap().into(productImg);
        }

    }


}
