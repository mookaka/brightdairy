package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.view.Banner;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductDetail;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public class ProductDetailActivity extends Activity {
    private ProductDetail productDetail;

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


    private void fillViewWithData() {
        bannerProductImgs.setImages(productDetail.picScrollUrls);
        txtviewProductName.setText(productDetail.productName);
        txtviewProductPrice.setText(String.valueOf(productDetail.prices.basePrice));
        txtviewProductVol.setText("产品规格：" + productDetail.productVol + productDetail.productVolUom);

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

    }

}
