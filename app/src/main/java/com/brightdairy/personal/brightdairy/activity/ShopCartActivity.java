package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.pg.PG;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ShopCartApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.ShopCartAdapter;
import com.brightdairy.personal.brightdairy.popup.DialogPopup;
import com.brightdairy.personal.brightdairy.popup.GeneralLoadingPopup;
import com.brightdairy.personal.brightdairy.popup.ShopCartSendModePopup;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.CheckCartItemEvent;
import com.brightdairy.personal.model.Event.CheckSupplierEvent;
import com.brightdairy.personal.model.Event.DeleteCartItemEvent;
import com.brightdairy.personal.model.Event.ShopCartEditSendModeEvent;
import com.brightdairy.personal.model.Event.UpdateSendInfoEvent;
import com.brightdairy.personal.model.HttpReqBody.OperateCartItem;
import com.brightdairy.personal.model.HttpReqBody.SelectSupplier;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.SelectedCartItem;
import com.brightdairy.personal.model.entity.ShopCart;
import com.brightdairy.personal.model.entity.SupplierItem;
import com.jakewharton.rxbinding.view.RxView;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/20.
 */
public class ShopCartActivity extends BaseActivity
{

    private SwipeMenuRecyclerView swipeRclShopCartProducts;
    private TextView txtviewToatalCost;
    private Button btnGotoComfirmOrder;

    private TextView txtviewShopCartEmpty;
    private LinearLayout shopCartContentView;

    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_shop_cart);

        swipeRclShopCartProducts = (SwipeMenuRecyclerView) findViewById(R.id.rclview_shop_cart_orders);
        txtviewToatalCost = (TextView) findViewById(R.id.txtview_shop_cart_total_cost);
        btnGotoComfirmOrder = (Button) findViewById(R.id.btn_shop_cart_goto_comfirm_order);
        txtviewShopCartEmpty = (TextView) findViewById(R.id.txtview_shop_cart_empty);
        shopCartContentView = (LinearLayout) findViewById(R.id.shop_cart_content);

        swipeRclShopCartProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        swipeRclShopCartProducts.addItemDecoration(new VerticalSpaceItemDecoration(2));
        swipeRclShopCartProducts.hasFixedSize();
    }

    private CompositeSubscription mCompositeSubscription;
    private SelectedCartItem mSelectedCartItem;
    private ShopCartApi mShopCartApi;
    private ShopCart mShopCart;
    private RxBus mRxBus;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        mSelectedCartItem = new SelectedCartItem();
        mSelectedCartItem.productIds = new ArrayList<>();

        handleRxBusEvent();

        mShopCartApi = GlobalRetrofit.getRetrofitDev().create(ShopCartApi.class);

        mCompositeSubscription.add(mShopCartApi.getCartInfo(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ShopCart>>() {
                    @Override
                    public void call(DataResult<ShopCart> result) {
                        switch (result.msgCode) {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mShopCart = result.result;
                                fillViewWithData();
                                break;
                            case GlobalHttpConfig.API_MSGCODE.SHOP_CART_EMPTY:
                                showEmptyShop();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable)
                    {
                        showEmptyShop();
                        throwable.printStackTrace();
                    }
                }));

    }



    @Override
    protected void initListener()
    {
        super.initListener();

        mCompositeSubscription.add(RxView.clicks(btnGotoComfirmOrder)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent toConfirmOrder = new Intent(ShopCartActivity.this, ConfirmOrderActivity.class);
                        toConfirmOrder.putExtra("SelectedCartItemInfo", PG.convertParcelable(mSelectedCartItem));
                        startActivity(toConfirmOrder);

                    }
                }));
    }


    private void handleRxBusEvent()
    {
        mCompositeSubscription.add(mRxBus.EventDispatcher()
                .subscribe(new Action1<Object>()
                {
                    @Override
                    public void call(Object event)
                    {
                        if (event instanceof DeleteCartItemEvent)
                        {
                            handleDeleteCartItemEvent((DeleteCartItemEvent)event);
                        } else if (event instanceof CheckCartItemEvent)
                        {
                            checkCartItemEvent((CheckCartItemEvent)event);
                        } else if (event instanceof CheckSupplierEvent)
                        {
                            checkSupplierEvent((CheckSupplierEvent)event);
                        } else if (event instanceof ShopCartEditSendModeEvent)
                        {
                            handleShopCartEditSendModeEvent((ShopCartEditSendModeEvent)event);
                        } else if (event instanceof UpdateSendInfoEvent)
                        {
                            handleUpdateSendInfoEvent((UpdateSendInfoEvent)event);
                        }
                    }
                }));
    }

    private void handleUpdateSendInfoEvent(UpdateSendInfoEvent event)
    {
        showLoadingPopup();

        mCompositeSubscription.add(mShopCartApi.modifyCartItem(
                GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                event.mUpdateSendInfo)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ShopCart>>()
                {
                    @Override
                    public void call(DataResult<ShopCart> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mShopCartAdapter.handleModifyCartItem(flattenShopCartData(result.result.items));
                                dismissLoadingPopup();
                                break;
                            default:
                                dismissLoadingPopup();
                                GeneralUtils.showToast(ShopCartActivity.this,  result.msgText);
                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        dismissLoadingPopup();
                        GeneralUtils.showToast(ShopCartActivity.this, "修改信息遇到问题哎");
                        throwable.printStackTrace();
                    }
                }));
    }

    private void handleShopCartEditSendModeEvent(ShopCartEditSendModeEvent event)
    {
        ShopCartSendModePopup shopCartSendModePopup = ShopCartSendModePopup.newInstance(event);
        shopCartSendModePopup.show(getSupportFragmentManager(), "cartSendModePopup");
    }

    private void checkSupplierEvent(final CheckSupplierEvent event)
    {
        if (!event.supplierId.equals(mSelectedCartItem.supplierId))
        {
            DialogPopup dialogPopup = DialogPopup.newInstance(getString(R.string.change_supplier_popup_title));

            dialogPopup.setDialogListener(new DialogPopup.DialogListener()
            {
                @Override
                public void onConfirmClick()
                {
                    handleCheckSupplierEvent(event);
                }

                @Override
                public void onCancelClick()
                {
                }
            });
        } else
        {
            handleCheckSupplierEvent(event);
        }
    }

    private void handleCheckSupplierEvent(CheckSupplierEvent event)
    {
        showLoadingPopup();

        SelectSupplier selectSupplier = new SelectSupplier();
        selectSupplier.selected = event.selected;
        selectSupplier.supplierId = event.supplierId;

        mCompositeSubscription.add(mShopCartApi.selectItemsBySupplierId(
                GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                selectSupplier)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<DataResult<ShopCart>>() {
                    @Override
                    public void call(DataResult<ShopCart> result) {
                        switch (result.msgCode) {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mShopCartAdapter.handleCheckItem(flattenShopCartData(result.result.items));
                                dismissLoadingPopup();
                                break;
                            default:
                                dismissLoadingPopup();
                                GeneralUtils.showToast(ShopCartActivity.this, result.msgText);
                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        dismissLoadingPopup();
                        GeneralUtils.showToast(ShopCartActivity.this, "选择供应商发生问题哎");
                    }
                }));
    }


    private void checkCartItemEvent(final CheckCartItemEvent event)
    {
        if (!event.supplierId.equals(mSelectedCartItem.supplierId))
        {
            DialogPopup dialogPopup = DialogPopup.newInstance(getString(R.string.change_supplier_popup_title));

            dialogPopup.setDialogListener(new DialogPopup.DialogListener()
            {
                @Override
                public void onConfirmClick()
                {
                    handleCheckCartItemEvent(event);
                }

                @Override
                public void onCancelClick()
                {
                }
            });

        } else
        {
            handleCheckCartItemEvent(event);
        }
    }


    private void handleCheckCartItemEvent(final CheckCartItemEvent event)
    {
        showLoadingPopup();

        OperateCartItem checkCartItem = new OperateCartItem();
        checkCartItem.itemSeqId = event.itemSeqId;
        String operator = event.selectItem?ShopCartApi.selectCartItem:ShopCartApi.unSelectCartItem;

        mCompositeSubscription.add(mShopCartApi.checkCartItem(operator,
                GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,checkCartItem)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ShopCart>>()
                {
                    @Override
                    public void call(DataResult<ShopCart> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                mShopCartAdapter.handleCheckItem(flattenShopCartData(result.result.items));
                                dismissLoadingPopup();
                                break;
                            default:
                                dismissLoadingPopup();
                                GeneralUtils.showToast(ShopCartActivity.this, result.msgText);
                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        dismissLoadingPopup();
                        GeneralUtils.showToast(ShopCartActivity.this, "选择产品发生问题哎");
                        throwable.printStackTrace();
                    }
                }));
    }

    private void handleDeleteCartItemEvent(final DeleteCartItemEvent event)
    {
        showLoadingPopup();

        OperateCartItem deleteCartItem = new OperateCartItem();
        deleteCartItem.itemSeqId = event.itemSeqId;

        mCompositeSubscription.add(mShopCartApi.deleteCartItem(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,deleteCartItem)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<ShopCart>>() {
                    @Override
                    public void call(DataResult<ShopCart> result) {
                        switch (result.msgCode) {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                dismissLoadingPopup();
                                mShopCartAdapter.handleDeleteCartItem(swipeRclShopCartProducts
                                        .findViewHolderForAdapterPosition(event.itemAdapterPosition));
                                break;
                            default:
                                dismissLoadingPopup();
                                GeneralUtils.showToast(ShopCartActivity.this, result.msgText);
                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        dismissLoadingPopup();
                        GeneralUtils.showToast(ShopCartActivity.this, "删除商品发生错误哎");
                        throwable.printStackTrace();
                    }
                }));
    }


    private void showEmptyShop()
    {
        txtviewShopCartEmpty.setVisibility(View.VISIBLE);
        shopCartContentView.setVisibility(View.GONE);
    }


    private ShopCartAdapter mShopCartAdapter;
    private void fillViewWithData()
    {
        mShopCartAdapter = new ShopCartAdapter(flattenShopCartData(mShopCart.items), this);
        swipeRclShopCartProducts.setAdapter(mShopCartAdapter);
        txtviewToatalCost.setText(mShopCart.cartAmount);
    }

    private ArrayList<Object> flattenShopCartData(ArrayList<ShopCart.ItemsBean> items)
    {

        mSelectedCartItem.supplierId = "";
        mSelectedCartItem.productIds.clear();

        ArrayList<Object> shopCartItems = new ArrayList<>();

        for (int supplierIndex =  0; supplierIndex < items.size(); supplierIndex++) {
            ShopCart.ItemsBean shopCartItem = items.get(supplierIndex);

            SupplierItem supplierItem = new SupplierItem();
            supplierItem.supplierId = shopCartItem.supplierId;
            supplierItem.supplierName = shopCartItem.supplierName;
            supplierItem.supplierSelected = shopCartItem.supplierSelected.equals("Y");
            shopCartItems.add(supplierItem);
            shopCartItems.addAll(shopCartItem.cartItemList);

            if (!(mSelectedCartItem.productIds.size() > 0))
            {
                for (int cartItemIndex = 0; cartItemIndex < shopCartItem.cartItemList.size(); cartItemIndex++)
                {

                    CartItem cartItem = shopCartItem.cartItemList.get(cartItemIndex);

                    if (cartItem.isSelect.equals("Y"))
                    {
                        mSelectedCartItem.productIds.add(cartItem.productId);
                        mSelectedCartItem.supplierId = cartItem.supplierId;
                    }
                }
            }


        }

        return shopCartItems;
    }

}
