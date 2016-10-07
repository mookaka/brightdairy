package com.brightdairy.personal.brightdairy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.ShopCartProductVH;
import com.brightdairy.personal.brightdairy.ViewHolder.ShopCartSupplierVH;
import com.brightdairy.personal.brightdairy.popup.GeneralLoadingPopup;
import com.brightdairy.personal.brightdairy.popup.OrderSendModePopup;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.CheckCartItemEvent;
import com.brightdairy.personal.model.Event.DeleteCartItemEvent;
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
    private Activity mContext;
    private RxBus mRxBus;

    private final int ITEM_TYPE_SUPPLIER = 0x12;
    private final int ITEM_TYPE_PRODUCT = 0x13;

    public ShopCartAdapter(ArrayList<Object> shopCarts, Activity context)
    {
        this.mShopCarts = shopCarts;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        mRxBus = RxBus.EventBus();
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
            shopCartProductVH.checkboxProductSelected.setOnClickListener(this);
            shopCartProductVH.txtviewSendModeEdit.setOnClickListener(this);
            shopCartProductVH.checkboxProductSelected.setTag(shopCartProductVH);
            shopCartProductVH.btnDeleteProduct.setTag(shopCartProductVH);
            shopCartProductVH.txtviewSendModeEdit.setTag(shopCartProductVH);

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
        switch (v.getId())
        {
            case R.id.btn_item_shop_cart_delete_product:
                deleteCartItem(v);
                break;
            case R.id.checkbox_item_shop_cart_product:
                checkCartItem((CheckBox)v);
                break;
            case R.id.txtview_item_shop_cart_edit:
                break;
        }
    }

    private void modifyCartItem(View v)
    {

    }

    private void checkCartItem(CheckBox v)
    {
        if (mRxBus.hasObservers())
        {
            v.setChecked(!v.isChecked());
            ShopCartProductVH shopCartProductVH = (ShopCartProductVH) v.getTag();
            int clickViewPos = shopCartProductVH.getAdapterPosition();
            CheckCartItemEvent checkCartItemEvent = new CheckCartItemEvent();
            CartItem cartItem = (CartItem) mShopCarts.get(clickViewPos);
            checkCartItemEvent.itemAdapterPosition = clickViewPos;
            checkCartItemEvent.itemSeqId = cartItem.itemSeqId;
            checkCartItemEvent.supplierId = cartItem.supplierId;
            checkCartItemEvent.selectItem = v.isChecked();

            mRxBus.dispatchEvent(checkCartItemEvent);
        }
    }


    public void handleCheckCartItem(RecyclerView.ViewHolder viewHolder, boolean selectItem)
    {
        ShopCartProductVH shopCartProductVH = (ShopCartProductVH) viewHolder;
        shopCartProductVH.checkboxProductSelected.setChecked(selectItem);
    }

    private void deleteCartItem(View v)
    {
        if (mRxBus.hasObservers())
        {
            ShopCartProductVH shopCartProductVH = (ShopCartProductVH) v.getTag();
            int clickViewPos = shopCartProductVH.getAdapterPosition();
            DeleteCartItemEvent deleteCartItemEvent = new DeleteCartItemEvent();
            deleteCartItemEvent.itemAdapterPosition = clickViewPos;
            deleteCartItemEvent.itemSeqId = ((CartItem)mShopCarts.get(clickViewPos)).itemSeqId;
            mRxBus.dispatchEvent(deleteCartItemEvent);

        }
    }

    public void handleDeleteCartItem(RecyclerView.ViewHolder viewHolder)
    {
        ShopCartProductVH shopCartProductVH = (ShopCartProductVH) viewHolder;
        int clickViewPos = shopCartProductVH.getAdapterPosition();
        shopCartProductVH.swipeMenu.smoothCloseMenu();
        mShopCarts.remove(clickViewPos);
        notifyItemRemoved(clickViewPos);

        int preItemPos = clickViewPos - 1;
        if (mShopCarts.get(preItemPos) instanceof SupplierItem)
        {
            mShopCarts.remove(preItemPos);
            notifyItemRemoved(preItemPos);
        }
    }

}
