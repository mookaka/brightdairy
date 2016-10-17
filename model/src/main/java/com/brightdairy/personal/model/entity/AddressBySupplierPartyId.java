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
    public String geoCode;
    public String geoId;
    public Object addedInfo;
    public String activeFlag;
    public Object geoTypeId;
    public Object parentGeo;
    public ArrayList<AddressBySupplierPartyId> childList;
}
