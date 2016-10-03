package com.brightdairy.personal.model.entity;

/**
 * Created by shuangmusuihua on 2016/10/3.
 */

public class AddressSelectorInfo
{
    public static final int TYPE_CIYT = 1;
    public static final int TYPE_COUNTY = 2;
    public static final int TYPE_STREET = 3;
    public static final int TYPE_PROVINCE = 4;
    public String geoName;
    public String geoId;
    public boolean isSelected;
    public int addressType;
}
