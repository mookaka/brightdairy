package com.brightdairy.personal.model.Event;

import com.brightdairy.personal.model.HttpReqBody.DeleteAddress;

/**
 * Created by shuangmusuihua on 2016/10/2.
 */

public class DeleteAddressEvent
{
    public DeleteAddress mDeleteAddress;

    public DeleteAddressEvent(DeleteAddress deleteAddress) {
        mDeleteAddress = deleteAddress;
    }
}
