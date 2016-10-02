package com.brightdairy.personal.model.HttpReqBody;

import com.baoyz.pg.Parcelable;

/**
 * Created by shuangmusuihua on 2016/9/30.
 */

@Parcelable
public class UpdateAddress
{
    public String contactMechId;
    public NewAddress mNewAddress;

    public UpdateAddress() {
    }
}
