package com.brightdairy.personal.model.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangmusuihua on 2016/10/3.
 */

public class AddressBySupplierPartyId
{

    public String geoName;
    @Expose(deserialize = true,serialize = false)
    public String geoCode;
    public String geoId;
    @Expose(deserialize = true,serialize = false)
    public Object addedInfo;
    @Expose(deserialize = true,serialize = false)
    public String activeFlag;
    @Expose(deserialize = true,serialize = false)
    public Object geoTypeId;
    @Expose(deserialize = true,serialize = false)
    public Object parentGeo;
    public ArrayList<AddressBySupplierPartyId> childList;
}
