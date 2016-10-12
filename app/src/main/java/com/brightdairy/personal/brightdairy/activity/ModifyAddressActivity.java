package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.baoyz.pg.PG;
import com.brightdairy.personal.api.AddressApi;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.AddressInfosAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration.VerticalSpaceItemDecoration;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.DeleteAddressEvent;
import com.brightdairy.personal.model.Event.SetAddressDefaultEvent;
import com.brightdairy.personal.model.Event.UpdateAddressEvent;
import com.brightdairy.personal.model.entity.AddressInfo;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/30.
 */

public class ModifyAddressActivity extends BaseActivity
{
    private RecyclerView rclviewAddressList;
    private Button btnAddAddress;
    private RxBus mRxBus;

    @Override
    protected void initView()
    {
        setContentView(R.layout.activity_local_send_order_address);
        rclviewAddressList = (RecyclerView) findViewById(R.id.rclview_modify_address_adress_list);
        btnAddAddress = (Button) findViewById(R.id.btn_modify_address_add_address);

        rclviewAddressList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rclviewAddressList.addItemDecoration(new VerticalSpaceItemDecoration(10));
        rclviewAddressList.hasFixedSize();
    }

    private ArrayList<AddressInfo> addressInfos;
    private CompositeSubscription mCompositeSubscription;
    private AddressApi mAddressApi;
    private String supplierId;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mAddressApi = GlobalRetrofit.getRetrofitDev().create(AddressApi.class);
        supplierId = getIntent().getStringExtra("supplierId");
        mRxBus = RxBus.EventBus();

        initHandleRxBusEvent();

        fetchData();
    }



    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    protected void initListener()
    {
        super.initListener();

        mCompositeSubscription.add(RxView.clicks(btnAddAddress)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent gotoEditAddress = new Intent(ModifyAddressActivity.this, EditAddressActivity.class);
                        gotoEditAddress.putExtra("supplierId", supplierId);
                        startActivity(gotoEditAddress);
                    }
                }));
    }

    private void fillviewWithData()
    {
        rclviewAddressList.setAdapter(new AddressInfosAdapter(addressInfos, supplierId, this));
    }

    private void showEmpty()
    {

    }

    private void initHandleRxBusEvent()
    {
        mCompositeSubscription.add(mRxBus.EventDispatcher()
                .subscribe(new Action1<Object>()
                {
                    @Override
                    public void call(Object event)
                    {
                        if (event instanceof SetAddressDefaultEvent)
                        {
                            handleSetAddressDefaultEvent((SetAddressDefaultEvent)event);
                        }
                        else if (event instanceof UpdateAddressEvent)
                        {
                            handleUpdateAddressEvent((UpdateAddressEvent)event);
                        }
                        else if (event instanceof DeleteAddressEvent)
                        {
                            handleDeleteAddressEvent((DeleteAddressEvent)event);
                        }
                    }
                }));
    }

    private void handleDeleteAddressEvent(DeleteAddressEvent event)
    {
        mCompositeSubscription.add(mAddressApi.deleteShipAddress(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                event.mDeleteAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<Object>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<Object> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                fetchData();
                                break;
                            case GlobalHttpConfig.API_MSGCODE.ADDRESS_DONT_EXIST:
                            case GlobalHttpConfig.API_MSGCODE.DELETE_ADDRESS_NOT_EXIST:
                            case GlobalHttpConfig.API_MSGCODE.DELETE_ADDRESS_ERR:
                                SuperActivityToast.create(ModifyAddressActivity.this, result.msgText, Style.DURATION_LONG).show();
                                break;
                        }
                    }
                }));
    }

    private void handleUpdateAddressEvent(UpdateAddressEvent event)
    {
        Intent gotoEditAddress = new Intent(ModifyAddressActivity.this, EditAddressActivity.class);
        gotoEditAddress.putExtra("supplierId", supplierId);
        gotoEditAddress.putExtra("updateAddressInfo", PG.convertParcelable(event.mUpdateAddress));
        startActivity(gotoEditAddress);
    }

    private void handleSetAddressDefaultEvent(SetAddressDefaultEvent event)
    {
        mCompositeSubscription.add(mAddressApi.updateShipAddress(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                event.mUpdateAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<Object>>() {

                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<Object> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                fetchData();
                                break;
                            case GlobalHttpConfig.API_MSGCODE.ADDRESS_DONT_EXIST:
                            case GlobalHttpConfig.API_MSGCODE.UPDATE_ADDRESS_ERR:
                                SuperActivityToast.create(ModifyAddressActivity.this, result.msgText, Style.DURATION_LONG).show();
                                break;
                        }
                    }
                }));
    }

    private void fetchData()
    {
        mCompositeSubscription.add(mAddressApi.getShipAddressList(
                GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                GlobalConstants.ZONE_CODE, supplierId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ArrayList<AddressInfo>>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        fillviewWithData();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<ArrayList<AddressInfo>> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                addressInfos = result.result;
                                break;
                            default:
                            case GlobalHttpConfig.API_MSGCODE.EMPTY_AVAILABLE_ADDRESS:
                                showEmpty();
                                break;

                        }
                    }
                }));
    }
}
