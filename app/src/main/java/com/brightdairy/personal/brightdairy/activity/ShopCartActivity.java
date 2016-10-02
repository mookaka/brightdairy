package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
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
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ShopCart;
import com.brightdairy.personal.model.entity.SupplierItem;
import com.jakewharton.rxbinding.view.RxView;
import com.tubb.smrv.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.internal.operators.OnSubscribeJoin;
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
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();

        mShopCartApi = GlobalRetrofit.getRetrofitTest().create(ShopCartApi.class);

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

    private void showEmptyShop()
    {
        txtviewShopCartEmpty.setVisibility(View.VISIBLE);
        shopCartContentView.setVisibility(View.GONE);
    }

    private void fillViewWithData()
    {
        swipeRclShopCartProducts.setAdapter(new ShopCartAdapter(flattenShopCartData(mShopCart.items), this));
        txtviewToatalCost.setText(mShopCart.cartAmount);
    }

    private ArrayList<Object> flattenShopCartData(ArrayList<ShopCart.ItemsBean> items)
    {
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
        }

        return shopCartItems;
    }

}
