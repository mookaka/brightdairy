package com.brightdairy.personal.brightdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.ShopCartProductVH;
import com.brightdairy.personal.brightdairy.ViewHolder.ShopCartSupplierVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.SupplierItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/9/21.
 */
public class ShopCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener
{

    private ArrayList<Object> mShopCarts;
    private LayoutInflater mInflater;
    private Context mContext;

    private final int ITEM_TYPE_SUPPLIER = 0x12;
    private final int ITEM_TYPE_PRODUCT = 0x13;

    public ShopCartAdapter(ArrayList<Object> shopCarts, Context context)
    {
        this.mShopCarts = shopCarts;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case ITEM_TYPE_PRODUCT:

                View productView = mInflater.inflate(R.layout.item_shop_cart_products, parent, false);
                productView.setTag(ITEM_TYPE_PRODUCT);
                return new ShopCartProductVH(productView);

            default:
            case ITEM_TYPE_SUPPLIER:
                View supplierView = mInflater.inflate(R.layout.item_shop_cart_supplier, parent, false);
                supplierView.setTag(ITEM_TYPE_SUPPLIER);
                return new ShopCartSupplierVH(supplierView);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Object itemData = mShopCarts.get(position);

        if (holder instanceof ShopCartProductVH && itemData instanceof CartItem)
        {
            ShopCartProductVH shopCartProductVH = (ShopCartProductVH) holder;
            CartItem cartItem = (CartItem) itemData;

            shopCartProductVH.btnDeleteProduct.setOnClickListener(this);
            shopCartProductVH.btnDeleteProduct.setTag(shopCartProductVH);

            shopCartProductVH.checkboxProductSelected.setChecked(cartItem.isSelect.equals("Y"));

            Glide.with(mContext).load(GlobalConstants.IMG_URL_BASE + cartItem.itemImg)
                    .asBitmap().fitCenter()
                    .into(shopCartProductVH.imgviewProductImg);

            shopCartProductVH.txtviewProductName.setText(cartItem.productName);
            shopCartProductVH.txtviewSendMode.setText("送奶模式：" + cartItem.shipModuleName);
            shopCartProductVH.txtviewSendTime.setText("送奶时间：" + cartItem.startDate + "到" + cartItem.endate);
            shopCartProductVH.txtviewUnitQuantity.setText("每次配送：" + String.valueOf(cartItem.unitQuantity));
            shopCartProductVH.txtviewTotalAmout.setText("总分数：" + String.valueOf(cartItem.totalQuantity));
            shopCartProductVH.txtviewTotalCost.setText(cartItem.totalAmount + "元");
        }
        else if (holder instanceof ShopCartSupplierVH && itemData instanceof  SupplierItem)
        {
            ShopCartSupplierVH supplierVH = (ShopCartSupplierVH) holder;
            SupplierItem supplierItem = (SupplierItem) itemData;

            supplierVH.txtviewSupplierName.setText(supplierItem.supplierName);
            supplierVH.checkboxSupplier.setChecked(supplierItem.supplierSelected);
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mShopCarts.get(position) instanceof SupplierItem)
        {
            return ITEM_TYPE_SUPPLIER;
        } else
        {
            return ITEM_TYPE_PRODUCT;
        }
    }

    @Override
    public int getItemCount()
    {
        return mShopCarts.size();
    }


    @Override
    public void onClick(View v)
    {
        ShopCartProductVH shopCartProductVH = (ShopCartProductVH) v.getTag();
        shopCartProductVH.swipeMenu.smoothCloseMenu();
        mShopCarts.remove(shopCartProductVH.getAdapterPosition());
        notifyItemRemoved(shopCartProductVH.getAdapterPosition());

    }
}
