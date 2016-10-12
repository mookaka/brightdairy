package com.brightdairy.personal.model.entity;

import com.baoyz.pg.Parcelable;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/10/7.
 */
@Parcelable
public class SelectedCartItem
{
    public String supplierId;
    public ArrayList<String> productIds;
}
