package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/22.
 */
public class ShopCartSupplierVH extends RecyclerView.ViewHolder
{

    public CheckBox checkboxSupplier;
    public TextView txtviewSupplierName;

    public ShopCartSupplierVH(View itemView)
    {
        super(itemView);

        checkboxSupplier = (CheckBox) itemView.findViewById(R.id.checkbox_item_shop_cart_company);
        txtviewSupplierName = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_company_name);
    }
}
