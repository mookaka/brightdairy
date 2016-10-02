package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;

/**
 * Created by shuangmusuihua on 2016/9/30.
 */

public class AddressInfoVH extends RecyclerView.ViewHolder
{
    public TextView txtviewReceiverName;
    public TextView txtviewReceiverMobile;
    public TextView txtviewReceiverAddress;
    public CheckBox checkboxSetDefaultAddress;
    public TextView txtviewModifyAddress;
    public TextView txtviewDeleteAddress;

    public AddressInfoVH(View itemView)
    {
        super(itemView);

        txtviewReceiverName = (TextView) itemView.findViewById(R.id.txtview_item_address_name);
        txtviewReceiverAddress = (TextView) itemView.findViewById(R.id.txtview_item_address_address);
        txtviewReceiverMobile = (TextView) itemView.findViewById(R.id.txtview_item_address_phone);
        checkboxSetDefaultAddress = (CheckBox) itemView.findViewById(R.id.checkbox_item_address_default);
        txtviewModifyAddress = (TextView) itemView.findViewById(R.id.btn_item_address_modify);
        txtviewDeleteAddress = (TextView) itemView.findViewById(R.id.btn_item_address_delete);
    }
}
