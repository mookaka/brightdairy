package com.brightdairy.personal.model.entity;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class ProductInfo
{
    public String productId;
    public String productName;
    public String productPrice;
    public String crmPdtId;
    public String supplierPartyId;
    public String productVol;
    public String productVolUom;
    public String imageUrl;
    public String memberComments;

    public ProductInfo(String crmPdtId, String imageUrl, String memberComments, String productId, String productName, String productPrice, String productVol, String productVolUom, String supplierPartyId) {
        this.crmPdtId = crmPdtId;
        this.imageUrl = imageUrl;
        this.memberComments = memberComments;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productVol = productVol;
        this.productVolUom = productVolUom;
        this.supplierPartyId = supplierPartyId;
    }
}
