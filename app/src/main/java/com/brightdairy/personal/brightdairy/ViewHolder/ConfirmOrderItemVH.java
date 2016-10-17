package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/22.
 */
public class ConfirmOrderItemVH extends RecyclerView.ViewHolder
{
    public ImageView imgviewProductImg;
    public TextView txtviewProductName;
    public TextView txtviewSendMode;
    public TextView txtviewSendTime;
    public TextView txtviewTotalAmout;
    public TextView txtviewProductPrice;
    public EditText editPromoCode;
    public TextView txtviewValidatePromoCode;
    public TextView txtviewAbandonPromoCode;
    public LinearLayout llPromotion;


    public ConfirmOrderItemVH(View itemView)
    {
        super(itemView);

        imgviewProductImg = (ImageView) itemView.findViewById(R.id.imgview_item_confirm_order_product_img);
        txtviewProductName = (TextView) itemView.findViewById(R.id.txtview_item_confirm_order_product_name);
        txtviewSendMode = (TextView) itemView.findViewById(R.id.txtview_item_confirm_order_send_mode);
        txtviewSendTime = (TextView) itemView.findViewById(R.id.txtview_item_confirm_order_send_time);
        txtviewTotalAmout = (TextView) itemView.findViewById(R.id.txtview_item_confirm_order_total_amount);
        txtviewProductPrice = (TextView) itemView.findViewById(R.id.txtview_item_confirm_order_product_price);
        llPromotion = (LinearLayout) itemView.findViewById(R.id.ll_item_confirm_order_promotion);
        editPromoCode = (EditText) itemView.findViewById(R.id.edit_item_confirm_order_input_promotion_code);
        txtviewValidatePromoCode = (TextView) itemView.findViewById(R.id.btn_item_confirm_order_validate);
        txtviewAbandonPromoCode = (TextView) itemView.findViewById(R.id.btn_item_confirm_order_abandon);
    }
}
