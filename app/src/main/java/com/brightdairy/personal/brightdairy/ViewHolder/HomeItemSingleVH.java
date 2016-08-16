package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.view.Banner;

/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class HomeItemSingleVH extends RecyclerView.ViewHolder
{
    public TextView itemSingleTitle;
    public ImageView itemSingleImgLeft;
    public ImageView itemSingleImgTop;
    public ImageView itemSingleImgBottom;

    public HomeItemSingleVH(View itemView)
    {
        super(itemView);

        itemSingleTitle = (TextView) itemView.findViewById(R.id.item_home_single_title);
        itemSingleImgLeft = (ImageView) itemView.findViewById(R.id.item_home_single_img_left);
        itemSingleImgTop = (ImageView)itemView.findViewById(R.id.item_home_single_img_top);
        itemSingleImgBottom = (ImageView)itemView.findViewById(R.id.item_home_single_img_bottom);
    }
}
