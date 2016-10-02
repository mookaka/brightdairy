package com.brightdairy.personal.model.Event;

import com.brightdairy.personal.model.HttpReqBody.UpdateAddress;

/**
 * Created by shuangmusuihua on 2016/10/2.
 */

public class SetAddressDefaultEvent
{
    public UpdateAddress mUpdateAddress;

    public SetAddressDefaultEvent(UpdateAddress updateAddress) {
        mUpdateAddress = updateAddress;
    }
}
