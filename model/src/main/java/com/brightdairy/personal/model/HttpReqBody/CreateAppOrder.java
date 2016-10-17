package com.brightdairy.personal.model.HttpReqBody;

import java.util.HashMap;

/**
 * Created by shuangmusuihua on 2016/10/11.
 */

public class CreateAppOrder
{

    public String userLoginId;
    public String contactMechId;
    public String paymethod = "EXT_ONLINE_PAY";
    public String companyId;
    public String salesChannelEnumId = "APP_SALES_CHANNEL";
    public HashMap<String,String> promocodeinfo = new HashMap<>();
    public String pointsUsed;
}
