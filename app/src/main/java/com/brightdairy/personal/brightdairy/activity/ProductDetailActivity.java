package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.popup.OrderSendModePopup;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.view.Banner;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductDetail;

import java.util.Arrays;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public class ProductDetailActivity extends Activity
{
    public ProductDetail productDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        bannerProductImgs = (Banner) findViewById(R.id.banner_product_imgs);
        txtviewProductName = (TextView) findViewById(R.id.txtview_product_detail_name);
        txtviewProductPrice = (TextView) findViewById(R.id.txtview_product_detail_price);
        txtviewProductVol = (TextView) findViewById(R.id.txtview_product_detail_vol);
        txtviewOrderNorms = (TextView) findViewById(R.id.txtview_product_detail_order_norms);
        txtviewCampannyName = (TextView) findViewById(R.id.txtview_companny_name);
        promoGive = (TextView) findViewById(R.id.txtview_product_detail_promo_give);
        promoDiscount = (TextView)findViewById(R.id.txtview_product_detail_promo_discount);
        popupSendModeSelector = (ImageButton)findViewById(R.id.imgbtn_select_send_mode);

        bannerProductImgs.setDelayTime(5000);
        bannerProductImgs.setIndicatorGravity(Banner.CENTER);
        bannerProductImgs.setBannerStyle(Banner.CIRCLE_INDICATOR);

        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        String productId = getIntent().getStringExtra("productId");

        ProductHttp productHttp = GlobalRetrofit.getRetrofitDev().create(ProductHttp.class);

        productHttp.getProductDetailById(productId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ProductDetail>>() {
                    @Override
                    public void onCompleted() {
                        fillViewWithData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DataResult<ProductDetail> productDetailDataResult) {
                        productDetail = productDetailDataResult.result;
                    }
                });
    }

    private Banner bannerProductImgs;
    private TextView txtviewProductName;
    private TextView txtviewProductPrice;
    private TextView txtviewProductVol;
    private TextView txtviewOrderNorms;
    private TextView txtviewCampannyName;
    private TextView promoGive;
    private TextView promoDiscount;
    private ImageButton popupSendModeSelector;

    private void fillViewWithData()
    {
        bannerProductImgs.setImages(fussImgUrl(productDetail.picScrollUrls));
        txtviewProductName.setText(productDetail.productName);
        txtviewProductPrice.setText(String.valueOf("随心订价：￥" + productDetail.prices.basePrice));
        txtviewProductVol.setText("产品规格：" + productDetail.productVol + productDetail.productVolUom);

        for(int promoIndex = 0; promoIndex < productDetail.promoInfos.size(); promoIndex++)
        {
            ProductDetail.PromoInfos promoInfos = productDetail.promoInfos.get(promoIndex);

            if("满减".equals(promoInfos.promoName))
                promoDiscount.setText(promoInfos.promoText);
            else promoGive.setText(promoInfos.promoText);

        }

        switch (productDetail.preDays)
        {
            case "1":
                txtviewOrderNorms.setText("今天订奶，明天就开始送了:)");
                break;
            case "2":
                txtviewOrderNorms.setText("今天订奶，后天才能送哦:)");
                break;
        }

        txtviewCampannyName.setText(productDetail.companyName);

        initListener();
    }

    private void initListener()
    {
        popupSendModeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderSendModePopup orderSendModePopup = new OrderSendModePopup();
                orderSendModePopup.show(ProductDetailActivity.this.getFragmentManager(), "orderSendModePopup");
            }
        });
    }

    private String[] fussImgUrl(String[] initImgUrls)
    {

        if(initImgUrls != null && initImgUrls.length > 0)
        {
            StringBuilder fussImgUrl = new StringBuilder().append(GlobalConstants.IMG_URL_BASR);
            final int IMG_URL_BASE_LEN = GlobalConstants.IMG_URL_BASR.length() -1;

            for (int index = 0; index < initImgUrls.length; index++)
            {
                fussImgUrl.delete(IMG_URL_BASE_LEN, fussImgUrl.length());
                initImgUrls[index] = fussImgUrl.append(initImgUrls[index]).toString();
            }
        }

        return initImgUrls;
    }

}
