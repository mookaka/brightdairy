package com.brightdairy.personal.brightdairy.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductHttp;
import com.brightdairy.personal.api.ShopCartApi;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.popup.OrderSendModePopup;
import com.brightdairy.personal.brightdairy.popup.QuikBuyInfoPopup;
import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.brightdairy.personal.brightdairy.utils.GeneralUtils;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.RxBus;
import com.brightdairy.personal.brightdairy.view.Banner;
import com.brightdairy.personal.brightdairy.view.TopView;
import com.brightdairy.personal.brightdairy.view.badgeview.BadgeRadioButton;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.Event.SendModeChangeEvent;
import com.brightdairy.personal.model.Event.SendTimeChangeEvent;
import com.brightdairy.personal.model.Event.UnitQuantityChangeEvent;
import com.brightdairy.personal.model.Event.VolChangeEvent;
import com.brightdairy.personal.model.entity.ProductDetail;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.brightdairy.personal.model.entity.ShopCart;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by shuangmusuihua on 2016/8/21.
 */
public class ProductDetailActivity extends FragmentActivity
{

    private Banner bannerProductImgs;
    private TextView txtviewProductName;
    private TextView txtviewProductPrice;
    private TextView txtviewProductVol;
    private TextView txtviewOrderNorms;
    private TextView txtviewCampannyName;
    private ImageButton popupSendModeSelector;
    private ImageView imgviewProductDetail;
    private LinearLayout llSendMode;

    private TextView txtviewSendTime;
    private TextView txtviewSendMode;
    private TextView txtviewUnitQuantity;
    private TextView txtviewTotalAmount;

    private BadgeRadioButton bdbtnShopCart;
    private Button btnAddToCart;
    private Button btnBuyNow;

    public ProductDetail productDetail;
    public ProductSendInfo mProductSendModeInfo;
    private CompositeSubscription mCompositeSubscription;

    private TopView addToCartAnimView;

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
        addToCartAnimView = TopView.attach2Window(this);
        bannerProductImgs = (Banner) findViewById(R.id.banner_product_imgs);
        txtviewProductName = (TextView) findViewById(R.id.txtview_product_detail_name);
        txtviewProductPrice = (TextView) findViewById(R.id.txtview_product_detail_price);
        txtviewProductVol = (TextView) findViewById(R.id.txtview_product_detail_vol);
        txtviewOrderNorms = (TextView) findViewById(R.id.txtview_product_detail_order_norms);
        txtviewCampannyName = (TextView) findViewById(R.id.txtview_companny_name);

        llSendMode = (LinearLayout) findViewById(R.id.ll_product_detail_send_time);

        popupSendModeSelector = (ImageButton)findViewById(R.id.imgbtn_select_send_mode);
        imgviewProductDetail = (ImageView) findViewById(R.id.imgview_product_detail);

        txtviewSendTime = (TextView) findViewById(R.id.txtview_product_detail_send_time);
        txtviewSendMode = (TextView) findViewById(R.id.txtview_product_detail_send_mode);
        txtviewUnitQuantity = (TextView) findViewById(R.id.txtview_product_detail_unit_quantity);
        txtviewTotalAmount = (TextView) findViewById(R.id.txtview_product_detail_total_amount);

        bdbtnShopCart = (BadgeRadioButton) findViewById(R.id.radio_product_shopping_cart);
        btnAddToCart = (Button) findViewById(R.id.btn_product_detail_add_to_cart);
        btnBuyNow = (Button) findViewById(R.id.btn_product_detail_buy_now);

        bannerProductImgs.setDelayTime(5000);
        bannerProductImgs.setIndicatorGravity(Banner.CENTER);
        bannerProductImgs.setBannerStyle(Banner.CIRCLE_INDICATOR);
    }

    private ProductHttp productHttp;
    private ShopCartApi mShopCartApi;
    private RxBus mRxBus;
    private void initData()
    {


        String productId = getIntent().getStringExtra("productId");

        initProductSendModeData(productId);

        mCompositeSubscription = new CompositeSubscription();
        mRxBus = RxBus.EventBus();

        productHttp = GlobalRetrofit.getRetrofitDev().create(ProductHttp.class);
        mShopCartApi = GlobalRetrofit.getRetrofitDev().create(ShopCartApi.class);

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
                handleRxEvent(Event);
            }
        }));



    }

    private void handleRxEvent(Object event)
    {
        if (event instanceof VolChangeEvent)
        {
            VolChangeEvent volChangeEvent = ((VolChangeEvent) event);
            initProductDetailById(volChangeEvent.productId);
        } else if (event instanceof SendModeChangeEvent)
        {
            SendModeChangeEvent sendModeChangeEvent = (SendModeChangeEvent) event;
            mProductSendModeInfo.shipModuleName = sendModeChangeEvent.shipModuleName;
            mProductSendModeInfo.shipModuleId = sendModeChangeEvent.shipModuleId;
            mProductSendModeInfo.shipModuleType = sendModeChangeEvent.shipModuleType;

            refreshSendModeViewWithNewData();

        } else if (event instanceof SendTimeChangeEvent)
        {
            SendTimeChangeEvent sendTimeChangeEvent = (SendTimeChangeEvent) event;
            mProductSendModeInfo.startDate = sendTimeChangeEvent.startTime;
            mProductSendModeInfo.endate = sendTimeChangeEvent.endTime;

            refreshSendModeViewWithNewData();

        } else if (event instanceof UnitQuantityChangeEvent)
        {
            UnitQuantityChangeEvent unitQuantityChangeEvent = (UnitQuantityChangeEvent) event;
            mProductSendModeInfo.unitQuantity = unitQuantityChangeEvent.unitQuantity;

            refreshSendModeViewWithNewData();
        }
    }

    private void refreshSendModeViewWithNewData()
    {
        txtviewSendTime.setText("配送时间：" + mProductSendModeInfo.startDate + " 到 " + mProductSendModeInfo.endate);
        txtviewSendMode.setText("配送模式：" + mProductSendModeInfo.shipModuleName);
        txtviewUnitQuantity.setText("每次配送：" + mProductSendModeInfo.unitQuantity + "份");
        txtviewTotalAmount.setText("总份数：" + AppLocalUtils.getTotalAmount(mProductSendModeInfo) + "份");
    }


    private void initProductDetailById(String productId)
    {
        mCompositeSubscription.add(productHttp.getProductDetailById(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,productId)
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
        mProductSendModeInfo.endate = AppLocalUtils.getDateWithOffset(AppLocalUtils.DATE_NEXT_MONTH_FROM_NOW);
        mProductSendModeInfo.unitQuantity = 1;

        refreshSendModeViewWithNewData();
    }


    private void fillViewWithData()
    {
        bannerProductImgs.setImages(AppLocalUtils.fussImgUrl(productDetail.picScrollUrls));
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

        Glide.with(this)
                .load(GlobalConstants.PRODUCT_DETAIL_IMG_URL_BASE + productDetail.productId + GlobalConstants.PRODUCT_DETAIL_SUFIXX)
                .asBitmap()
                .into(imgviewProductDetail);

        freshSendModePopup(productDetail);
    }


    private OrderSendModePopup orderSendModePopup;
    private void initListener()
    {
        mCompositeSubscription.add(RxView.clicks(bdbtnShopCart)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        Intent ToShopCartIntent = new Intent(ProductDetailActivity.this, ShopCartActivity.class);
                        startActivity(ToShopCartIntent);
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(btnAddToCart)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        AddProductToCart();
                    }
                }));


        mCompositeSubscription.add(RxView.clicks(btnBuyNow)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        QuikBuyInfoPopup quikBuyInfoPopup = QuikBuyInfoPopup.newInstance(mProductSendModeInfo);
                        quikBuyInfoPopup.show(getSupportFragmentManager(), "quickBuyInfo");
                    }
                }));

        mCompositeSubscription.add(RxView.clicks(llSendMode)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>()
                {
                    @Override
                    public void call(Void aVoid)
                    {
                        if(orderSendModePopup == null)
                        {
                            orderSendModePopup = new OrderSendModePopup();
                        }
                        orderSendModePopup.show(getSupportFragmentManager(), "orderSendModePopup");
                    }
                }));

    }

    private void AddProductToCart()
    {
        mCompositeSubscription.add(mShopCartApi.addCartItem(GlobalHttpConfig.PID,
                GlobalHttpConfig.UID,
                GlobalHttpConfig.TID,
                GlobalHttpConfig.PIN,
                mProductSendModeInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ShopCart>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(DataResult<ShopCart> result)
                    {
                        switch (result.msgCode)
                        {
                            case GlobalHttpConfig.API_MSGCODE.REQUST_OK:
                                showAddToCartAnim();
                                break;
                            default:
                                GeneralUtils.showToast(ProductDetailActivity.this, result.msgText);
                                break;
                        }
                    }
                }));
    }


    private void showAddToCartAnim()
    {
        int[] start = new int[]{0, 0};
        btnAddToCart.getLocationInWindow(start);
        int[] end = new int[]{0, 0};
        bdbtnShopCart.getLocationOnScreen(end);

        PointF animViewStartLocatin = new PointF(start[0], start[1]);
        PointF animViewMiddleLocation = new PointF(start[0], end[1]);
        PointF animViewEndLocation = new PointF(end[0], end[1]-10);

        TopView.AnimationInfo animInfo = new TopView.AnimationInfo.Builder().callback(
                new TopView.AnimationCallback()
                {
                    @Override
                    public void animationEnd()
                    {
                        bdbtnShopCart.setBadgeShown(true);
                    }
                })
                .resId(R.mipmap.vip_newproduct)
                .p0(animViewStartLocatin)
                .p1(animViewMiddleLocation)
                .p2(animViewEndLocation)
                .create();

        addToCartAnimView.add(animInfo);
    }

    private void freshSendModePopup(ProductDetail detail)
    {
        if (orderSendModePopup != null && orderSendModePopup.isVisible())
            orderSendModePopup.freshPopupData(detail, mProductSendModeInfo);
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
