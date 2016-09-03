package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class PopSelectVolVH extends RecyclerView.ViewHolder
{
    public ImageView productVolImg;
    public View itemView;

    public PopSelectVolVH(View itemView)
    {
        super(itemView);
        productVolImg = (ImageView) itemView.findViewById(R.id.imgview_popup_vol_img);
        this.itemView = itemView;
    }
}
