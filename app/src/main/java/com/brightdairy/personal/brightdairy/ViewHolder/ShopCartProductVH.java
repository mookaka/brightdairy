package com.brightdairy.personal.brightdairy.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

/**
 * Created by shuangmusuihua on 2016/9/21.
 */
public class ShopCartProductVH extends RecyclerView.ViewHolder
{
    public SwipeHorizontalMenuLayout swipeMenu;
    public CheckBox checkboxProductSelected;
    public ImageView imgviewProductImg;
    public TextView txtviewProductName;
    public TextView txtviewSendMode;
    public TextView txtviewSendTime;
    public TextView txtviewUnitQuantity;
    public TextView txtviewTotalAmout;

    public TextView txtviewSendModeEdit;
    public TextView txtviewTotalCost;

    public Button btnDeleteProduct;

    public ShopCartProductVH(View itemView)
    {
        super(itemView);

        swipeMenu = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.swl_item_shop_cart_swiper);
        checkboxProductSelected = (CheckBox) itemView.findViewById(R.id.checkbox_item_shop_cart_product);
        imgviewProductImg = (ImageView) itemView.findViewById(R.id.imgview_item_shop_product_img);
        txtviewProductName = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_product_name);
        txtviewSendMode = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_send_mode);
        txtviewSendTime = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_send_time);
        txtviewUnitQuantity = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_unit_quantity);
        txtviewTotalAmout = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_product_total_amount);

        txtviewSendModeEdit = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_edit);
        txtviewTotalCost = (TextView) itemView.findViewById(R.id.txtview_item_shop_cart_total_cost);
        btnDeleteProduct = (Button) itemView.findViewById(R.id.btn_item_shop_cart_delete_product);
    }
}
