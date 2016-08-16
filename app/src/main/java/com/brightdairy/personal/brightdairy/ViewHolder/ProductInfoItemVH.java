package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class ProductInfoItemVH extends RecyclerView.ViewHolder
{
    public ImageView imgviewProductImg;
    public TextView txtviewProductName;
    public TextView txtviewProductVol;
    public TextView txtviewProductPrice;

    public ProductInfoItemVH(View itemView)
    {
        super(itemView);

        imgviewProductImg = (ImageView) itemView.findViewById(R.id.imgview_item_product_img);
        txtviewProductName = (TextView) itemView.findViewById(R.id.txtview_item_product_namne);
        txtviewProductVol = (TextView) itemView.findViewById(R.id.txtview_item_product_vol);
        txtviewProductPrice = (TextView) itemView.findViewById(R.id.txtview_product_price);

    }
}
