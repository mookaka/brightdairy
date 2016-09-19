package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.popup.OrderSendModePopup;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.AddSubtractionBtn;
import com.brightdairy.personal.brightdairy.view.Banner;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.ProductInfoChangeEvet;
import com.brightdairy.personal.model.Event.VolChangeEvent;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.bumptech.glide.Glide;

import java.util.Arrays;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public class ProductDetailActivity extends Activity
{

    private Banner bannerProductImgs;
    private TextView txtviewProductName;
    private TextView txtviewProductPrice;
    private TextView txtviewProductVol;
    private TextView txtviewOrderNorms;
    private TextView txtviewCampannyName;
    private ImageButton popupSendModeSelector;
    private ImageView imgviewProductDetail;

    public ProductDetail productDetail;
    private ProductSendInfo mProductSendModeInfo;
    private CompositeSubscription mCompositeSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();
        initData();
        initListener();

    }

    private void initView()
    {
        bannerProductImgs = (Banner) findViewById(R.id.banner_product_imgs);
        txtviewProductName = (TextView) findViewById(R.id.txtview_product_detail_name);
        txtviewProductPrice = (TextView) findViewById(R.id.txtview_product_detail_price);
        txtviewProductVol = (TextView) findViewById(R.id.txtview_product_detail_vol);
        txtviewOrderNorms = (TextView) findViewById(R.id.txtview_product_detail_order_norms);
        txtviewCampannyName = (TextView) findViewById(R.id.txtview_companny_name);
        popupSendModeSelector = (ImageButton)findViewById(R.id.imgbtn_select_send_mode);
        imgviewProductDetail = (ImageView) findViewById(R.id.imgview_product_detail);

        bannerProductImgs.setDelayTime(5000);
        bannerProductImgs.setIndicatorGravity(Banner.CENTER);
        bannerProductImgs.setBannerStyle(Banner.CIRCLE_INDICATOR);
    }

    private ProductHttp productHttp;
    private RxBus mRxBus;
    private void initData()
    {

        String productId = getIntent().getStringExtra("productId");

        initProductSendModeData(productId);

        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        productHttp = GlobalRetrofit.getRetrofitDev().create(ProductHttp.class);

        initProductDetailById(productId);

        initRxEventBusOperator();

    }

    private void initRxEventBusOperator()
    {
        mCompositeSubscription.add(mRxBus.EventDispatcher().subscribe(new Action1<Object>()
        {
            @Override
            public void call(Object Event)
            {
                if (Event instanceof VolChangeEvent)
                {
                    VolChangeEvent volChangeEvent = ((VolChangeEvent) Event);
                    initProductDetailById(volChangeEvent.productId);
                }
            }
        }));
    }


    private void initProductDetailById(String productId)
    {
        mCompositeSubscription.add(productHttp.getProductDetailById(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ProductDetail>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        fillViewWithData();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DataResult<ProductDetail> productDetailDataResult)
                    {
                        productDetail = productDetailDataResult.result;
                        mProductSendModeInfo.productId = productDetail.productId;
                    }
                }));
    }

    private void initProductSendModeData(String productId)
    {
        mProductSendModeInfo = new ProductSendInfo();
        mProductSendModeInfo.productId = productId;
        mProductSendModeInfo.shipModuleId = "66";
        mProductSendModeInfo.shipModuleStr = "";
        mProductSendModeInfo.shipModuleType = "M";
        mProductSendModeInfo.shipModuleName = "每日送";
        mProductSendModeInfo.startDate = AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NOW);
        mProductSendModeInfo.endate = AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH);
        mProductSendModeInfo.unitQuantity = 1;
    }


    private void fillViewWithData()
    {
        bannerProductImgs.setImages(fussImgUrl(productDetail.picScrollUrls));
        txtviewProductName.setText(productDetail.productName);
        txtviewProductPrice.setText(String.valueOf("随心订价：￥" + productDetail.prices.basePrice));
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

        Glide.with(this).load(GlobalConstants.PRODUCT_DETAIL_IMG_URL_BASE + productDetail.productId + GlobalConstants.PRODUCT_DETAIL_SUFIXX).asBitmap().into(imgviewProductDetail);

        freshSendModePopup(productDetail);
    }


    private OrderSendModePopup orderSendModePopup;
    private void initListener()
    {
        popupSendModeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(orderSendModePopup == null)
                {
                    orderSendModePopup = new OrderSendModePopup();
                }
                orderSendModePopup.show(ProductDetailActivity.this.getFragmentManager(), "orderSendModePopup");
            }
        });
    }

    private void freshSendModePopup(ProductDetail detail)
    {
        if (orderSendModePopup != null && orderSendModePopup.isVisible())
            orderSendModePopup.freshPopupData(detail);
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

    @Override
    protected void onDestroy()
    {
        mCompositeSubscription.clear();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
