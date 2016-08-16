package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class CategoryItemVH extends RecyclerView.ViewHolder
{

    private TextView txtViewCategoryName;

    public CategoryItemVH(View itemView)
    {
        super(itemView);

        this.txtViewCategoryName = (TextView) itemView.findViewById(R.id.txtview_category_name);
    }
}
