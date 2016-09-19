package com.brightdairy.personal.brightdairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.PopSelectVolVH;
import com.brightdairy.personal.brightdairy.fragment.HomeFragment;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.VolChangeEvent;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.bumptech.glide.Glide;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class PopupSelectVolAdapter extends RecyclerView.Adapter<PopSelectVolVH> implements View.OnClickListener
{
    private ArrayList<ProductDetail.ProductAssocBean> productVols;
    private LayoutInflater popVolInflater;
    private int currentChecked = -1;
    private int tmpPosition = -1;
    private boolean canChooseVol = true;
    private RxBus mRxBus;

    public PopupSelectVolAdapter(ArrayList<ProductDetail.ProductAssocBean> productVols)
    {
        this.productVols = productVols;
        this.productVols.addAll(productVols);
        this.popVolInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
        this.mRxBus = RxBus.EventBus();
    }

    @Override
    public PopSelectVolVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View volView = popVolInflater.inflate(R.layout.item_popup_select_vol, null, false);

        return new PopSelectVolVH(volView);
    }

    @Override
    public void onBindViewHolder(PopSelectVolVH holder, int position)
    {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);

        String volImgUrl = GlobalConstants.IMG_URL_BASR + productVols.get(position).AsscProdImage;
        Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(volImgUrl).asBitmap().into(holder.productVolImg);

        if(currentChecked == position)
        {
            holder.productVolImg.setBackgroundResource(R.mipmap.product_pop_select_l);
        } else {
            holder.productVolImg.setBackgroundResource(R.drawable.shape_line_lineout);
        }

    }

    @Override
    public int getItemCount()
    {
        return productVols.size();
    }


    @Override
    public void onClick(View v)
    {
        if(canChooseVol)
        {
            PopSelectVolVH holder = (PopSelectVolVH) v.getTag();

            int CurrentPosition = holder.getAdapterPosition();

            tmpPosition = currentChecked;
            currentChecked = CurrentPosition;
            notifyItemChanged(tmpPosition);
            notifyItemChanged(currentChecked);

            if(mRxBus.hasObservers())
            {
                canChooseVol = false;
                mRxBus.dispatchEvent(new VolChangeEvent(productVols.get(currentChecked).AsscProd));
            }
        } else {
            SuperToast.create(GlobalConstants.APPLICATION_CONTEXT, "正在奋力获取产品信息", Style.DURATION_LONG).show();
        }

    }

    public void setCanChooseVol(boolean canChooseVol)
    {
        this.canChooseVol = canChooseVol;
    }

    public String getCurrentCheckedVol()
    {
        return productVols.get(currentChecked).AsscProdVolume;
    }
}
