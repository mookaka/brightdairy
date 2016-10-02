package com.brightdairy.personal.brightdairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.ConfirmOrderItemVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.OrderInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

/**
 * Created by shuangmusuihua on 2016/9/23.
 */
public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderItemVH>
{
    private ArrayList<OrderInfo> mOrderInfos;
    private LayoutInflater mInflater;

    public ConfirmOrderAdapter(ArrayList<OrderInfo> orderInfos)
    {
        this.mOrderInfos = orderInfos;
        this.mInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
    }

    @Override
    public ConfirmOrderItemVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View productInfoView= mInflater.inflate(R.layout.item_confirm_order_product, null);
        return new ConfirmOrderItemVH(productInfoView);
    }

    @Override
    public void onBindViewHolder(ConfirmOrderItemVH holder, int position)
    {
        CartItem cartItem = mOrderInfos.get(position).cartItem;

        Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(GlobalConstants.IMG_URL_BASE + cartItem.itemImg)
                .asBitmap()
                .centerCrop()
                .into(holder.imgviewProductImg);

        holder.txtviewProductName.setText(cartItem.productName);
        holder.txtviewSendMode.setText("配送模式：" + cartItem.shipModuleName);
        holder.txtviewSendTime.setText("配送时间：" + cartItem.startDate + "到" + cartItem.endate);
        holder.txtviewTotalAmout.setText("配送总量：" + String.valueOf(cartItem.totalQuantity) + "份");
        holder.txtviewProductPrice.setText("产品单价" + cartItem.price + "元");

        if (!cartItem.productPromotionId.equals(""))
        {
            holder.llPromotion.setVisibility(VISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return mOrderInfos.size();
    }
}
