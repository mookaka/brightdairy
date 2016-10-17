package com.brightdairy.personal.brightdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.ConfirmOrderItemVH;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.AbandonPromoCodeEvent;
import com.brightdairy.personal.model.Event.ValidatePromoCodeEvent;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.OrderInfo;
import com.brightdairy.personal.model.entity.ValidatePromoCode;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.IdentityHashMap;

import static android.view.View.VISIBLE;

/**
 * Created by shuangmusuihua on 2016/9/23.
 */
public class ConfirmOrderAdapter extends RecyclerView.Adapter<ConfirmOrderItemVH> implements View.OnClickListener
{
    private ArrayList<OrderInfo> mOrderInfos;
    private LayoutInflater mInflater;
    private RxBus mRxBus;
    private Context mContext;

    public ConfirmOrderAdapter(Context context, ArrayList<OrderInfo> orderInfos)
    {
        this.mOrderInfos = orderInfos;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
        mRxBus = RxBus.EventBus();
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

        holder.txtviewAbandonPromoCode.setOnClickListener(this);
        holder.txtviewAbandonPromoCode.setTag(holder);

        holder.txtviewValidatePromoCode.setOnClickListener(this);
        holder.txtviewValidatePromoCode.setTag(holder);

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

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_item_confirm_order_validate:

                handleValidatePromoCode((ConfirmOrderItemVH)v.getTag());

                break;
            case R.id.btn_item_confirm_order_abandon:
                handleAbandonPromoCode((ConfirmOrderItemVH)v.getTag());
                break;
        }
    }

    private void handleAbandonPromoCode(ConfirmOrderItemVH confirmOrderItemVH)
    {
        String promoCode = confirmOrderItemVH.editPromoCode.getText().toString();

        if (TextUtils.isEmpty(promoCode))
        {
            GeneralUtils.showToast(mContext, "红包码不可为空");
            return;
        }


        if (mRxBus.hasObservers())
        {
            AbandonPromoCodeEvent abandonPromoCodeEvent = new AbandonPromoCodeEvent();
            int clickPos = confirmOrderItemVH.getAdapterPosition();
            abandonPromoCodeEvent.itemPos = clickPos;
            ValidatePromoCode validatePromoCode = new ValidatePromoCode();
            CartItem cartItem = mOrderInfos.get(clickPos).cartItem;

            abandonPromoCodeEvent.itemSeqId = cartItem.itemSeqId;
            validatePromoCode.cityCode = cartItem.cityCode;
            validatePromoCode.productId = cartItem.productId;
            validatePromoCode.promoCode = promoCode;
            validatePromoCode.promoId = cartItem.productPromotionId;
            abandonPromoCodeEvent.validatePromoCode = validatePromoCode;

            mRxBus.dispatchEvent(abandonPromoCodeEvent);

        }
    }

    private void handleValidatePromoCode(ConfirmOrderItemVH confirmOrderItemVH)
    {
        String promoCode = confirmOrderItemVH.editPromoCode.getText().toString();

        if (TextUtils.isEmpty(promoCode))
        {
            GeneralUtils.showToast(mContext, "红包码不可为空");
            return;
        }


        if (mRxBus.hasObservers())
        {
            ValidatePromoCodeEvent validatePromoCodeEvent = new ValidatePromoCodeEvent();
            int clickPos = confirmOrderItemVH.getAdapterPosition();
            validatePromoCodeEvent.itemPos = clickPos;
            ValidatePromoCode validatePromoCode = new ValidatePromoCode();
            CartItem cartItem = mOrderInfos.get(clickPos).cartItem;

            validatePromoCodeEvent.itemSeqId = cartItem.itemSeqId;
            validatePromoCode.cityCode = cartItem.cityCode;
            validatePromoCode.productId = cartItem.productId;
            validatePromoCode.promoCode = promoCode;
            validatePromoCode.promoId = cartItem.productPromotionId;
            validatePromoCodeEvent.validatePromoCode = validatePromoCode;

            mRxBus.dispatchEvent(validatePromoCodeEvent);

        }

    }


    public void refreshView(boolean usePromoCode, ConfirmOrderItemVH confirmOrderItemVH)
    {
        if (usePromoCode)
        {
            confirmOrderItemVH.txtviewAbandonPromoCode.setVisibility(VISIBLE);
            confirmOrderItemVH.txtviewValidatePromoCode.setVisibility(View.GONE);
        } else {
            confirmOrderItemVH.txtviewAbandonPromoCode.setVisibility(View.GONE);
            confirmOrderItemVH.txtviewValidatePromoCode.setVisibility(View.VISIBLE);
        }
    }
}
