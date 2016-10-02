package com.brightdairy.personal.brightdairy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.CitySelectorItemVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.model.Event.CityChangedEvent;
import com.brightdairy.personal.model.entity.CityZoneCode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/9/29.
 */

public class CitySelectorAdapter extends RecyclerView.Adapter<CitySelectorItemVH> implements View.OnClickListener
{
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<CityZoneCode> cityZoneCodess;
    private int currentCheckedPos;
    private RxBus mRxBus;
    private CompositeSubscription mCompositeSubscription;

    public CitySelectorAdapter(Context context)
    {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRxBus = RxBus.EventBus();
        mCompositeSubscription = new CompositeSubscription();

        String cityZonesStr = PrefUtil.getString(GlobalConstants.AppConfig.CITY_CODE_CACHE, null);

        if (cityZonesStr != null)
        {
            Gson gson = new Gson();
            cityZoneCodess = gson.fromJson(cityZonesStr, new TypeToken< ArrayList<CityZoneCode>>() {}.getType());
        }
    }

    @Override
    public CitySelectorItemVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mLayoutInflater.inflate(R.layout.item_city_selector, null);
        CitySelectorItemVH citySelectorItemVH = new CitySelectorItemVH(itemView);
        return citySelectorItemVH;
    }

    @Override
    public void onBindViewHolder(CitySelectorItemVH holder, int position)
    {
        holder.checkboxCityItem.setText(cityZoneCodess.get(position).cityName);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(holder);


        if (GlobalConstants.ZONE_CODE != null && GlobalConstants.ZONE_CODE.equals(cityZoneCodess.get(position).cityCode))
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
        return cityZoneCodess.size();
    }

    @Override
    public void onViewDetachedFromWindow(CitySelectorItemVH holder)
    {
        mCompositeSubscription.clear();
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onClick(View view)
    {
        CitySelectorItemVH holder = (CitySelectorItemVH) view.getTag();
        int clickViewPos = holder.getAdapterPosition();

        if (clickViewPos != currentCheckedPos)
        {

            CityZoneCode curCityZoneCode = cityZoneCodess.get(clickViewPos);

            GlobalConstants.ZONE_CODE = curCityZoneCode.cityCode;
            GlobalConstants.CURR_ZONE_CN_NAME = curCityZoneCode.cityName;

            notifyItemChanged(currentCheckedPos);
            notifyItemChanged(clickViewPos);

           mRxBus.dispatchEvent(new CityChangedEvent());
        }
    }
}
