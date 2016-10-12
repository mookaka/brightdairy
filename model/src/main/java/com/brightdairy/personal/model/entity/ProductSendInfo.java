package com.brightdairy.personal.model.entity;

import com.baoyz.pg.Parcelable;

/**
 * Created by shuangmusuihua on 2016/9/18.
 */
@Parcelable
public class ProductSendInfo
{

    public String productId;
    public String startDate;
    public String endate;
    public String shipModuleId;
    public String shipModuleName;
    public String shipModuleType;
    public String shipModuleStr;
    public int unitQuantity;

}
