package com.brightdairy.personal.model.entity;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/9/22.
 */
public class ConfirmOrderInfos
{
    public ArrayList<OrderInfo> cartItems;
    public AddrInfo defaultAddr;
    public ArrayList<AddrInfo> addrInfo;
    public ProdGeoInfo prodGeoInfo;
    public int availablePoints;
    public float reduceAmt;
    public float orderTotalAmt;
    public String supplierPartyId;
}
