package com.brightdairy.personal.brightdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.AddressInfoVH;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.DeleteAddressEvent;
import com.brightdairy.personal.model.Event.SetAddressDefaultEvent;
import com.brightdairy.personal.model.Event.UpdateAddressEvent;
import com.brightdairy.personal.model.HttpReqBody.DeleteAddress;
import com.brightdairy.personal.model.HttpReqBody.NewAddress;
import com.brightdairy.personal.model.HttpReqBody.UpdateAddress;
import com.brightdairy.personal.model.entity.AddressInfo;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCheckedTextView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/30.
 */

public class AddressInfosAdapter extends RecyclerView.Adapter<AddressInfoVH>
{
    private ArrayList<AddressInfo> mAddressInfos = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private StringBuilder addressBuilder;
    private RxBus mRxBus;
    private String supplierId;
    private CompositeSubscription mCompositeSubscription;

    public AddressInfosAdapter(ArrayList<AddressInfo> addressInfos, String supplierId, Context context)
    {
        if (addressInfos != null)
        {
            mAddressInfos = addressInfos;
        }
        mContext = context;
        mRxBus = RxBus.EventBus();
        mCompositeSubscription = new CompositeSubscription();
        this.supplierId = supplierId;
        addressBuilder = new StringBuilder();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AddressInfoVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mLayoutInflater.inflate(R.layout.item_address_info, parent, false);
        return new AddressInfoVH(itemView);
    }

    @Override
    public void onBindViewHolder(AddressInfoVH holder, int position)
    {
        AddressInfo addressInfo = mAddressInfos.get(position);

        holder.txtviewReceiverName.setText(addressInfo.toName);
        holder.txtviewReceiverMobile.setText(addressInfo.mobile);


        addressBuilder.append(addressInfo.city)
                .append(addressInfo.county)
                .append(addressInfo.town)
                .append(addressInfo.street)
                .append(addressInfo.address);

        holder.txtviewReceiverAddress.setText(addressBuilder.toString());

        holder.checkboxSetDefaultAddress.setChecked(addressInfo.isDefault.equals("Y"));

        bindingItemListener(holder, position);
    }

    private void bindingItemListener(final AddressInfoVH holder, final int position)
    {
        mCompositeSubscription.add(RxView
                .clicks(holder.checkboxSetDefaultAddress)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (mRxBus.hasObservers())
                        {
                            AddressInfo addressinfo = mAddressInfos.get(position);
                            UpdateAddress setDefaultAddress = new UpdateAddress();
                            NewAddress newAddress = new NewAddress();

                            setDefaultAddress.contactMechId = addressinfo.contactMechId;

                            newAddress.address = addressinfo.address;
                            newAddress.toName = addressinfo.toName;
                            newAddress.province = addressinfo.province;
                            newAddress.provinceId = addressinfo.provinceId;
                            newAddress.city = addressinfo.city;
                            newAddress.cityId = addressinfo.cityId;
                            newAddress.county = addressinfo.county;
                            newAddress.countyId = addressinfo.countyId;
                            newAddress.street = addressinfo.street;
                            newAddress.streetId = addressinfo.streetId;
                            newAddress.mobile = addressinfo.mobile;
                            newAddress.telPhone = addressinfo.telPhone;
                            newAddress.isDefault = holder.checkboxSetDefaultAddress.isChecked()?"Y":"N";
                            newAddress.postalCode = addressinfo.postalCode;
                            newAddress.partyId = addressinfo.partyId;
                            newAddress.needCase = addressinfo.needCase;
                            newAddress.supplierPartyId = supplierId;

                            setDefaultAddress.mNewAddress = newAddress;

                            mRxBus.dispatchEvent(new SetAddressDefaultEvent(setDefaultAddress));
                        }
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(holder.txtviewModifyAddress)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mRxBus.hasObservers())
                        {
                            AddressInfo addressinfo = mAddressInfos.get(position);
                            UpdateAddress setDefaultAddress = new UpdateAddress();
                            NewAddress newAddress = new NewAddress();

                            setDefaultAddress.contactMechId = addressinfo.contactMechId;

                            newAddress.address = addressinfo.address;
                            newAddress.toName = addressinfo.toName;
                            newAddress.province = addressinfo.province;
                            newAddress.provinceId = addressinfo.provinceId;
                            newAddress.city = addressinfo.city;
                            newAddress.cityId = addressinfo.cityId;
                            newAddress.county = addressinfo.county;
                            newAddress.countyId = addressinfo.countyId;
                            newAddress.street = addressinfo.street;
                            newAddress.streetId = addressinfo.streetId;
                            newAddress.mobile = addressinfo.mobile;
                            newAddress.telPhone = addressinfo.telPhone;
                            newAddress.isDefault = holder.checkboxSetDefaultAddress.isChecked()?"Y":"N";
                            newAddress.postalCode = addressinfo.postalCode;
                            newAddress.partyId = addressinfo.partyId;
                            newAddress.needCase = addressinfo.needCase;
                            newAddress.supplierPartyId = supplierId;

                            setDefaultAddress.mNewAddress = newAddress;

                            mRxBus.dispatchEvent(new UpdateAddressEvent(setDefaultAddress));
                        }
                    }
                    }));

        mCompositeSubscription.add(RxView.clicks(holder.txtviewDeleteAddress)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if (mRxBus.hasObservers())
                        {
                            AddressInfo addressinfo = mAddressInfos.get(position);
                            DeleteAddress deleteAddress = new DeleteAddress();
                            deleteAddress.contactMechId = addressinfo.contactMechId;
                            deleteAddress.partyId = addressinfo.partyId;

                            mRxBus.dispatchEvent(new DeleteAddressEvent(deleteAddress));
                        }
                    }
                }));


    }

    @Override
    public int getItemCount()
    {
        return mAddressInfos.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        mCompositeSubscription.clear();
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
