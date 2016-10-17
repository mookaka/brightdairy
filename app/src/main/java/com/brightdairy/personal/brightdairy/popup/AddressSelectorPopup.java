package com.brightdairy.personal.brightdairy.popup;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.brightdairy.personal.api.AddressApi;
import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.PopupAddressSelectorAdapter;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.StateLayout;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.AddressChangeEvent;
import com.brightdairy.personal.model.HttpReqBody.NewAddress;
import com.brightdairy.personal.model.entity.AddressBySupplierPartyId;
import com.brightdairy.personal.model.entity.AddressSelectorInfo;
import com.brightdairy.personal.model.entity.ProdGeoInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/12.
 */
public class AddressSelectorPopup extends BasePopup
{

    private ImageButton imgbtnClosePopup;
    private RadioGroup rgSelectorGroup;
    private RadioButton radioCity;
    private RadioButton radioCounty;
    private RadioButton radioStreet;
    private RecyclerView rclAddressLists;
    private StateLayout stateLayout;
    private String supplierId;



    public static AddressSelectorPopup newInstance(String supplierId, NewAddress newAddress)
    {
        AddressSelectorPopup mAddressSelectorPopup = new AddressSelectorPopup();

        Bundle addition = new Bundle();
        addition.putString("supplierId", supplierId);
        addition.putString("currCity", newAddress.city);
        addition.putString("currCounty", newAddress.county);
        addition.putString("currStreet", newAddress.street);
        mAddressSelectorPopup.setArguments(addition);


        return mAddressSelectorPopup;
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle state)
    {
        View addressSelectorView = inflater.inflate(R.layout.popup_address_selector, container);

        imgbtnClosePopup = (ImageButton) addressSelectorView.findViewById(R.id.imgbtn_address_selector_popup_close);
        rgSelectorGroup = (RadioGroup) addressSelectorView.findViewById(R.id.rg_address_selector_group);
        rclAddressLists = (RecyclerView) addressSelectorView.findViewById(R.id.rclview_address_selector_list);
        radioCity = (RadioButton) addressSelectorView.findViewById(R.id.radio_address_selector_city);
        radioCounty = (RadioButton) addressSelectorView.findViewById(R.id.radio_address_selector_county);
        radioStreet = (RadioButton) addressSelectorView.findViewById(R.id.radio_address_selector_street);
        stateLayout = (StateLayout) addressSelectorView.findViewById(R.id.state_address_selector_lists_wrapper);

        stateLayout.setContentViewResId(R.id.rclview_address_selector_list)
                .setLoadingViewResId(R.id.progress_bar_data_loading)
                .initWithState(StateLayout.VIEW_LOADING);

        rclAddressLists.setLayoutManager(new LinearLayoutManager(getActivity()));

        return addressSelectorView;
    }

    @Override
    protected void initListener()
    {
        mCompositeSubscription.add(RxRadioGroup.checkedChanges(rgSelectorGroup)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Integer>()
                {
                    @Override
                    public void call(Integer checkedId)
                    {
                        switch (checkedId)
                        {
                            case R.id.radio_address_selector_city:

                                if (mPopupAddressSelectorAdapter != null && provinceInfo != null)
                                {
                                    mPopupAddressSelectorAdapter.freshAddressType(provinceInfo.geoName);
                                }

                                break;
                            case R.id.radio_address_selector_county:

                                if (mPopupAddressSelectorAdapter != null)
                                {
                                    mPopupAddressSelectorAdapter.freshAddressType(currCity);
                                }

                                break;
                            case R.id.radio_address_selector_street:

                                if (mPopupAddressSelectorAdapter != null)
                                {
                                    mPopupAddressSelectorAdapter.freshAddressType(currCounty);
                                }

                                break;
                        }
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(imgbtnClosePopup)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        dismiss();
                    }
                }));
    }

    private CompositeSubscription mCompositeSubscription;
    private AddressApi mAddressApi;
    private AddressBySupplierPartyId addressInfos;
    private AddressSelectorInfo provinceInfo;
    private Gson mGson;

    private String currCity;
    private String currCounty;
    private String currStreet;

    private RxBus mRxBus;
    @Override
    protected void initData()
    {
        mCompositeSubscription = new CompositeSubscription();
        mAddressApi = GlobalRetrofit.getRetrofitDev().create(AddressApi.class);
        mGson = new Gson();
        mRxBus = RxBus.EventBus();

        handleRxBusEvent();

        supplierId = getArguments().getString("supplierId");
        currCity = getArguments().getString("currCity");
        currCounty = getArguments().getString("currCounty");
        currStreet = getArguments().getString("currStreet");

        freshViewWithData();

        mCompositeSubscription.add(mAddressApi.getGeoBySupplierPartyId(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                supplierId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<DataResult<AddressBySupplierPartyId>, Object>()
                {
                    @Override
                    public Object call(DataResult<AddressBySupplierPartyId> result)
                    {
                        addressInfos = result.result;
                        cacheAddressInfo2Local();
                        return Observable.empty();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>()
                {
                    @Override
                    public void call(Object o)
                    {
                        fillViewWithData();
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
                        if (event instanceof AddressChangeEvent)
                        {
                            AddressSelectorInfo addressSelectorInfo = ((AddressChangeEvent) event).mAddressSelectorInfo;

                            switch (addressSelectorInfo.addressType)
                            {
                                case AddressSelectorInfo.TYPE_CIYT:
                                    currCity = addressSelectorInfo.geoName;
                                    currCounty = "未选择";
                                    currStreet = "未选择";
                                    freshViewWithData();
                                    break;
                                case AddressSelectorInfo.TYPE_COUNTY:
                                    currCounty = addressSelectorInfo.geoName;
                                    currStreet = "未选择";
                                    freshViewWithData();
                                    break;
                                case AddressSelectorInfo.TYPE_STREET:
                                    currStreet = addressSelectorInfo.geoName;
                                    freshViewWithData();
                                    break;
                            }
                        }
                    }
                }));
    }

    private void freshViewWithData()
    {
        radioCity.setText(currCity);
        radioCounty.setText(currCounty);
        radioStreet.setText(currStreet);
    }

    private PopupAddressSelectorAdapter mPopupAddressSelectorAdapter;
    private void fillViewWithData()
    {
        mPopupAddressSelectorAdapter = new PopupAddressSelectorAdapter(getActivity(),provinceInfo);
        rclAddressLists.setAdapter(mPopupAddressSelectorAdapter);
        stateLayout.setState(StateLayout.VIEW_CONTENT);
    }

    private void cacheAddressInfo2Local()
    {
        provinceInfo = new AddressSelectorInfo();
        provinceInfo.geoName = addressInfos.geoName;
        provinceInfo.geoId = addressInfos.geoId;
        provinceInfo.addressType = AddressSelectorInfo.TYPE_PROVINCE;

        ArrayList<AddressBySupplierPartyId> cityInfos = addressInfos.childList;
        ArrayList<AddressSelectorInfo> citySelectorInfos = new ArrayList<>();

        for (int cityIndex = 0; cityIndex < cityInfos.size(); cityIndex++)
        {
            AddressBySupplierPartyId cityInfo = cityInfos.get(cityIndex);
            AddressSelectorInfo citySelectorInfo = new AddressSelectorInfo();
            citySelectorInfo.geoId = cityInfo.geoId;
            citySelectorInfo.geoName = cityInfo.geoName;
            citySelectorInfo.isSelected = cityInfo.geoCode.equals(currCity);
            citySelectorInfo.addressType = AddressSelectorInfo.TYPE_CIYT;

            citySelectorInfos.add(citySelectorInfo);

            ArrayList<AddressBySupplierPartyId> countyInfos = cityInfo.childList;
            ArrayList<AddressSelectorInfo> countySelectorInfos = new ArrayList<>();
            for (int countyIndex = 0; countyIndex < countyInfos.size(); countyIndex++)
            {
                AddressBySupplierPartyId countyInfo = countyInfos.get(countyIndex);
                AddressSelectorInfo countySelectorInfo = new AddressSelectorInfo();

                countySelectorInfo.geoName = countyInfo.geoName;
                countySelectorInfo.geoId = countyInfo.geoId;
                countySelectorInfo.isSelected = countyInfo.equals(currCounty);
                countySelectorInfo.addressType = AddressSelectorInfo.TYPE_COUNTY;

                countySelectorInfos.add(countySelectorInfo);

                ArrayList<AddressBySupplierPartyId> streetInfos = countyInfo.childList;
                ArrayList<AddressSelectorInfo> streetSelectorInfos = new ArrayList<>();


                for (int streetIndex = 0; streetIndex < streetInfos.size(); streetIndex++)
                {
                    AddressBySupplierPartyId streetInfo = streetInfos.get(streetIndex);
                    AddressSelectorInfo streetSelectorInfo = new AddressSelectorInfo();

                    streetSelectorInfo.geoName = streetInfo.geoName;
                    streetSelectorInfo.geoId = streetInfo.geoId;
                    streetSelectorInfo.isSelected = streetInfo.equals(currStreet);
                    streetSelectorInfo.addressType = AddressSelectorInfo.TYPE_STREET;

                    streetSelectorInfos.add(streetSelectorInfo);

                }

                String steertInfosStr = mGson.toJson(streetSelectorInfos, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType());
                PrefUtil.setString(countySelectorInfo.geoName, steertInfosStr);

            }

            String countyInfosStr = mGson.toJson(countySelectorInfos, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType());
            PrefUtil.setString(citySelectorInfo.geoName, countyInfosStr);
        }

        String cityInfosStr = mGson.toJson(citySelectorInfos, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType());
        PrefUtil.setString(provinceInfo.geoName + "省", cityInfosStr);
    }

}
