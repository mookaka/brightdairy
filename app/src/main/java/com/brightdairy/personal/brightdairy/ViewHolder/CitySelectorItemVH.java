package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/29.
 */

public class CitySelectorItemVH extends RecyclerView.ViewHolder
{

    public TextView checkboxCityItem;
    public ImageView checkIndicator;

    public CitySelectorItemVH(View itemView)
    {
        super(itemView);

        checkboxCityItem = (TextView) itemView.findViewById(R.id.checkbox_item_city_selector_city_name);
        checkIndicator = (ImageView) itemView.findViewById(R.id.imgview_item_city_selector_indicator);

    }
}
