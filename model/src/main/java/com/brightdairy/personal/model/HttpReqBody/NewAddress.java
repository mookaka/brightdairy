package com.brightdairy.personal.model.HttpReqBody;

import com.baoyz.pg.Parcelable;

/**
 * Created by shuangmusuihua on 2016/10/2.
 */

@Parcelable
public class NewAddress
{
    public String address;
    public String toName;
    public String province;
    public String provinceId;
    public String city;
    public String cityId;
    public String county;
    public String countyId;
    public String street;
    public String streetId;
    public String mobile;
    public String telPhone;
    public String isDefault;
    public String postalCode;
    public String partyId;
    public String needCase;
    public String supplierPartyId;

    public NewAddress() {

    }
}
