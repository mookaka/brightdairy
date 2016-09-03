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
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.ProductDetailActivity;
import com.brightdairy.personal.brightdairy.adapter.PopupSelectVolAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/8/23.
 */
public class OrderSendModePopup extends DialogFragment
{

    private ImageView productImg;
    private TextView productName;
    private TextView productPrice;
    private RecyclerView volSelectors;

    private ProductDetail productDetail;
    private AddSubtractionBtn addSubtractionBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View sendModeSelectorView = inflater.inflate(R.layout.popup_product_detail_selector, container, true);

        productImg = (ImageView)sendModeSelectorView.findViewById(R.id.imgview_popup_product_img);
        productName = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_name);
        productPrice = (TextView)sendModeSelectorView.findViewById(R.id.txtview_popup_product_price);
        volSelectors = (RecyclerView)sendModeSelectorView.findViewById(R.id.rclview_popup_vol_selector);
        addSubtractionBtn = (AddSubtractionBtn)sendModeSelectorView.findViewById(R.id.addsubbtn_popup_per_nums);

        volSelectors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        volSelectors.addItemDecoration(new SpaceItemDecoration(20));
        volSelectors.hasFixedSize();

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

    @Override
    public void onDestroyView()
    {
        addSubtractionBtn.unSubscribe();
        super.onDestroyView();
    }

    private void initData()
    {
        productDetail = ((ProductDetailActivity)getActivity()).productDetail;


        if(productDetail != null)
        {
            productName.setText(productDetail.productName);
            productPrice.setText("随心订价：" + productDetail.prices.basePrice);
            Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(GlobalConstants.IMG_URL_BASR + productDetail.guessImgUrl).asBitmap().into(productImg);

            ArrayList<ProductDetail.ProductAssocBean> productVols = productDetail.productAssoc;
            volSelectors.setAdapter(new PopupSelectVolAdapter(productVols));

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


}
