package com.brightdairy.personal.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public class ProductDetail
{

    public String productId;
    public String productName;
    public Object productPrice;
    public String crmPdtId;
    public Object supplierPartyId;
    public String productVol;
    public String productVolUom;
    public PricesBean prices;
    public Object description;
    public Object images;
    public String guessImgUrl;
    public String milkType;
    public String packages;
    public String praise;
    public String belongPartyId;
    public String priceRaiseDate;
    public String knowPriceRaiseDate;
    public Object geoBlock;
    public String cityCode;
    public String companyId;
    public String companyName;
    public String isNewChannel;
    public String isExpired;
    public String increasedPrice;
    public String maxOrderDuration;
    public String preDays;
    public String minOrderDuration;
    public boolean beenCollected;
    public String[] picScrollUrls;
    public List<ProductAssocBean> productAssoc;
    public List<PromoInfos> promoInfos;

    public static class PricesBean {
        public double basePrice;
        public double price;
        public double defaultPrice;
        public Object competitivePrice;
        public Object averageCost;
        public Object promoPrice;
        public Object specialPromoPrice;
        public boolean validPriceFound;
        public boolean isSale;
        public String currencyUsed;
        public ArrayList<?> orderItemPriceInfos;
    }


    public static class ProductAssocBean {
        public String AsscProdVolume;
        public String AsscProd;
        public String AsscProdImage;
    }

    public static class PromoInfos {
        public String productPromoId;
        public String promoName;
        public String promoText;
        public String productId;
        public String useLimitPerCustomer;
        public String promoRules;
    }
}
