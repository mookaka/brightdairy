package com.brightdairy.personal.model.Event;

import com.baoyz.pg.Parcelable;
import com.brightdairy.personal.model.entity.CartItem;
import com.brightdairy.personal.model.entity.ProductSendInfo;

/**
 * Created by shuangmusuihua on 2016/10/9.
 */
@Parcelable
public class ShopCartEditSendModeEvent
{
    public String itemSeqId;
    public CartItem mCartItemInfo;
}
