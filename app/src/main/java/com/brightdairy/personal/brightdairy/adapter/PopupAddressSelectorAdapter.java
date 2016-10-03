package com.brightdairy.personal.brightdairy.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.CitySelectorItemVH;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.AddressChangeEvent;
import com.brightdairy.personal.model.entity.AddressSelectorInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/10/3.
 */

public class PopupAddressSelectorAdapter extends RecyclerView.Adapter<CitySelectorItemVH> implements View.OnClickListener
{

    private ArrayList<AddressSelectorInfo> addressSelectorInfos = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RxBus mRxBus;
    private int currentCheckedPos = -1;
    private AddressSelectorInfo province;
    private Gson mGson;
    private String currentAddressType;

    public PopupAddressSelectorAdapter(Activity activity, AddressSelectorInfo province)
    {
        mContext = activity;
        mLayoutInflater = LayoutInflater.from(mContext);
        mRxBus = RxBus.EventBus();
        this.province = province;
        currentAddressType = province.geoName;

        String cityInfosStr = PrefUtil.getString(province.geoName, null);

        if (cityInfosStr != null)
        {
            mGson = new Gson();
            addressSelectorInfos.addAll((ArrayList<AddressSelectorInfo>)mGson.fromJson(cityInfosStr, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType()));
        }
    }

    @Override
    public CitySelectorItemVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mLayoutInflater.inflate(R.layout.item_city_selector, parent, false);
        CitySelectorItemVH citySelectorItemVH = new CitySelectorItemVH(itemView);
        return citySelectorItemVH;
    }

    @Override
    public void onBindViewHolder(CitySelectorItemVH holder, int position)
    {
        AddressSelectorInfo itemData = addressSelectorInfos.get(position);


        holder.checkboxCityItem.setText(itemData.geoName);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);

        if (itemData.isSelected)
        {
            holder.checkboxCityItem.setTextColor(mContext.getResources().getColor(R.color.colorGeneralBg));
            holder.checkIndicator.setVisibility(View.VISIBLE);
            currentCheckedPos = position;
        } else
        {
            holder.checkIndicator.setVisibility(View.GONE);
            holder.checkboxCityItem.setTextColor(mContext.getResources().getColor(R.color.colorBlackLight));
        }
    }

    @Override
    public int getItemCount()
    {
        return addressSelectorInfos.size();
    }

    @Override
    public void onClick(View view)
    {
        CitySelectorItemVH holder = (CitySelectorItemVH) view.getTag();
        int clickViewPos = holder.getAdapterPosition();

        if (currentCheckedPos != clickViewPos)
        {
            AddressSelectorInfo addressSelectorInfo = addressSelectorInfos.get(clickViewPos);
            addressSelectorInfo.isSelected = true;

            if (currentCheckedPos != -1)
            {
                addressSelectorInfos.get(currentCheckedPos).isSelected = false;
            }


            currentAddressType = addressSelectorInfo.geoName;

            String addressSelectorInfosStr = PrefUtil.getString(currentAddressType, null);

            if (addressSelectorInfo.addressType != AddressSelectorInfo.TYPE_STREET && addressSelectorInfosStr != null)
            {
                addressSelectorInfos.clear();
                addressSelectorInfos.addAll((ArrayList<AddressSelectorInfo>)mGson.fromJson(addressSelectorInfosStr, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType()));
                notifyDataSetChanged();
            }

            if (mRxBus.hasObservers())
            {
                AddressChangeEvent addressChangeEvent = new AddressChangeEvent();

                if (addressSelectorInfo.addressType == AddressSelectorInfo.TYPE_CIYT)
                {
                    addressChangeEvent.mAddressSelectorInfo = province;
                    mRxBus.dispatchEvent(addressChangeEvent);
                }

                addressChangeEvent.mAddressSelectorInfo = addressSelectorInfo;
                mRxBus.dispatchEvent(addressChangeEvent);
            }

        }
    }


    public void freshAddressType(String addressType)
    {
        String addressSelectorInfosStr = PrefUtil.getString(addressType, null);

        if (addressSelectorInfosStr != null)
        {
            addressSelectorInfos.clear();
            addressSelectorInfos.addAll((ArrayList<AddressSelectorInfo>)mGson.fromJson(addressSelectorInfosStr, new TypeToken<ArrayList<AddressSelectorInfo>>(){}.getType()));
            notifyDataSetChanged();
        }
    }
}
