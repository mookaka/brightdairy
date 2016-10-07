package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ShopCartApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.ShopCartAdapter;
import com.brightdairy.personal.brightdairy.popup.DialogPopup;
import com.brightdairy.personal.brightdairy.popup.GeneralLoadingPopup;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.CheckCartItemEvent;
import com.brightdairy.personal.model.Event.DeleteCartItemEvent;
import com.brightdairy.personal.model.HttpReqBody.OperateCartItem;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.SelectedCartItem;
import com.brightdairy.personal.model.entity.ShopCart;
import com.brightdairy.personal.model.entity.SupplierItem;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jakewharton.rxbinding.view.RxView;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
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
    private ShopCartApi mShopCartApi;
    private ShopCart mShopCart;
    private RxBus mRxBus;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        handleRxBusEvent();

        mShopCartApi = GlobalRetrofit.getRetrofitDev().create(ShopCartApi.class);

        mCompositeSubscription.add(mShopCartApi.getCartInfo(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ShopCart>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        fillViewWithData();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<ShopCart> shopCart)
                    {
                        if (GlobalHttpConfig.API_MSGCODE.SHOP_CART_EMPTY.equals(shopCart.msgCode))
                        {
                            showEmptyShop();
                        } else {
                            mShopCart = shopCart.result;
                        }
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
                            showLoadingPopup();
                            handleDeleteCartItemEvent((DeleteCartItemEvent)event);
                        } else if (event instanceof CheckCartItemEvent)
                        {
                            checkCartItemEvent((CheckCartItemEvent)event);
                        }
                    }
                }));
    }

    private void checkCartItemEvent(final CheckCartItemEvent event)
    {
        if (!event.supplierId.equals(mSelectedCartItem.supplierId))
        {
            showDialogPopup();

            if (mDialogPopup != null)
            {
                mDialogPopup.setDialogListener(new DialogPopup.DialogListener()
                {
                    @Override
                    public void onConfirmClick()
                    {
                        handleCheckCartItemEvent(event);
                    }

                    @Override
                    public void onCancelClick()
                    {
                        dismissDialogPopup();
                    }
                });
            }

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

        if (event.selectItem)
        {
            mCompositeSubscription.add(mShopCartApi.selectCartItem(GlobalHttpConfig.PID,
                    GlobalHttpConfig.UID,
                    GlobalHttpConfig.TID,
                    GlobalHttpConfig.PIN,checkCartItem)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DataResult<Object>>()
                    {
                        @Override
                        public void call(DataResult<Object> result)
                        {
                            switch (result.msgCode)
                            {
                                case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                    dismissLoadingPopup();
                                    mShopCartAdapter.handleCheckCartItem(swipeRclShopCartProducts
                                            .findViewHolderForAdapterPosition(event.itemAdapterPosition), event.selectItem);
                                    break;
                                default:
                                    SuperActivityToast.create(ShopCartActivity.this, result.msgText, Style.DURATION_LONG).show();
                                    break;
                            }
                        }
                    }, new Action1<Throwable>()
                    {
                        @Override
                        public void call(Throwable throwable)
                        {
                            dismissLoadingPopup();
                            throwable.printStackTrace();
                        }
                    }));
        } else
        {
            mCompositeSubscription.add(mShopCartApi.unselectCartItem(GlobalHttpConfig.PID,
                    GlobalHttpConfig.UID,
                    GlobalHttpConfig.TID,
                    GlobalHttpConfig.PIN,checkCartItem)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<DataResult<Object>>()
                    {
                        @Override
                        public void call(DataResult<Object> result)
                        {
                            switch (result.msgCode)
                            {
                                case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                    dismissLoadingPopup();
                                    mShopCartAdapter.handleCheckCartItem(swipeRclShopCartProducts
                                            .findViewHolderForAdapterPosition(event.itemAdapterPosition), event.selectItem);
                                    break;
                                default:
                                    SuperActivityToast.create(ShopCartActivity.this, result.msgText, Style.DURATION_LONG).show();
                                    break;
                            }
                        }
                    }, new Action1<Throwable>()
                    {
                        @Override
                        public void call(Throwable throwable)
                        {
                            dismissLoadingPopup();
                            throwable.printStackTrace();
                        }
                    }));
        }
    }

    private void handleDeleteCartItemEvent(final DeleteCartItemEvent event)
    {
        OperateCartItem deleteCartItem = new OperateCartItem();
        deleteCartItem.itemSeqId = event.itemSeqId;

        mCompositeSubscription.add(mShopCartApi.deleteCartItem(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,deleteCartItem)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DataResult<Object>>() {
                    @Override
                    public void call(DataResult<Object> result) {
                        switch (result.msgCode) {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                dismissLoadingPopup();
                                mShopCartAdapter.handleDeleteCartItem(swipeRclShopCartProducts
                                        .findViewHolderForAdapterPosition(event.itemAdapterPosition));
                                break;
                            default:
                                SuperActivityToast.create(ShopCartActivity.this, result.msgText, Style.DURATION_LONG).show();
                                break;
                        }
                    }
                }, new Action1<Throwable>()
                {
                    @Override
                    public void call(Throwable throwable)
                    {
                        dismissLoadingPopup();
                        throwable.printStackTrace();
                    }
                }));
    }


    private GeneralLoadingPopup loadingPopup;
    private void showLoadingPopup()
    {
        if (loadingPopup == null)
        {
            loadingPopup = new GeneralLoadingPopup();
        }
        loadingPopup.show(getFragmentManager(), "generalLoadingPopup");
    }

    private void dismissLoadingPopup()
    {
        if (loadingPopup != null)
        {
            loadingPopup.dismiss();
        }
    }

    private DialogPopup mDialogPopup;
    private void showDialogPopup()
    {
        if (mDialogPopup == null)
        {
            mDialogPopup = new DialogPopup();

            Bundle addtionData = new Bundle();
            addtionData.putString("title", "一次只能选择一个供销商\n您确定更换供销商？");
            mDialogPopup.setArguments(addtionData);
        }

        mDialogPopup.show(getFragmentManager(), "dialogPopup");
    }

    private void dismissDialogPopup()
    {
        if (mDialogPopup != null)
        {
            mDialogPopup.dismiss();
        }
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

    private SelectedCartItem mSelectedCartItem;
    private ArrayList<Object> flattenShopCartData(ArrayList<ShopCart.ItemsBean> items)
    {

        if (mSelectedCartItem == null)
        {
            mSelectedCartItem = new SelectedCartItem();
            mSelectedCartItem.productIds = new ArrayList<>();
        }

        ArrayList<Object> shopCartItems = new ArrayList<>();

        for (int supplierIndex =  0; supplierIndex < items.size(); supplierIndex++)
        {
            ShopCart.ItemsBean shopCartItem = items.get(supplierIndex);

            SupplierItem supplierItem = new SupplierItem();
            supplierItem.supplierId = shopCartItem.supplierId;
            supplierItem.supplierName = shopCartItem.supplierName;
            supplierItem.supplierSelected = shopCartItem.supplierSelected.equals("Y");
            shopCartItems.add(supplierItem);
            shopCartItems.addAll(shopCartItem.cartItemList);

            for (int cartItemIndex = 0; cartItemIndex < shopCartItem.cartItemList.size(); cartItemIndex++)
            {
                if (mSelectedCartItem.supplierId != null)
                    break;

                CartItem cartItem = shopCartItem.cartItemList.get(cartItemIndex);

                if (cartItem.isSelect.equals("Y"))
                {
                    mSelectedCartItem.productIds.add(cartItem.productId);
                    mSelectedCartItem.supplierId = cartItem.supplierId;
                }
            }

        }

        return shopCartItems;
    }

}
