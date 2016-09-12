package com.brightdairy.personal.brightdairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.PopSelectVolVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class PopupSelectVolAdapter extends RecyclerView.Adapter<PopSelectVolVH> implements View.OnClickListener
{
    private ArrayList<ProductDetail.ProductAssocBean> productVols;
    private LayoutInflater popVolInflater;
    private int currentChecked = -1;

    public PopupSelectVolAdapter(ArrayList<ProductDetail.ProductAssocBean> productVols)
    {
        this.productVols = productVols;
        this.popVolInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
    }

    @Override
    public PopSelectVolVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View volView = popVolInflater.inflate(R.layout.item_popup_select_vol, parent, true);

        return new PopSelectVolVH(volView);
    }

    @Override
    public void onBindViewHolder(PopSelectVolVH holder, int position)
    {
        holder.productVolImg.setOnClickListener(this);
        holder.productVolImg.setTag(position);

        String volImgUrl = GlobalConstants.IMG_URL_BASR + productVols.get(position).AsscProdImage;
        Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(volImgUrl).asBitmap().into(holder.productVolImg);
    }

    @Override
    public int getItemCount()
    {
        return productVols.size();
    }


    @Override
    public void onClick(View v)
    {
        int CurrentPosition = (int) v.getTag();
        
        if(currentChecked > 0)
        {
            notifyItemChanged(currentChecked);
            currentChecked = CurrentPosition;
            v.setBackgroundResource(R.mipmap.product_pop_select_l);
        }
    }

    public int getCurrentChecked()
    {
        return currentChecked;
    }
}
