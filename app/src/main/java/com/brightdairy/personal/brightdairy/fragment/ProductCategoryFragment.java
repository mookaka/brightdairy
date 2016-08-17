package com.brightdairy.personal.brightdairy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductCategoryHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.ProductCategory;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by shuangmusuihua on 2016/8/1.
 */
public class ProductCategoryFragment extends Fragment
{

    private ListView listviewCategoryList;
    private RecyclerView rclviewProductList;
    private View categoryPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        categoryPage = inflater.inflate(R.layout.fragment_home_page_category, container, false);
        listviewCategoryList = (ListView) categoryPage.findViewById(R.id.rclview_category_page_category_list);
        rclviewProductList = (RecyclerView) categoryPage.findViewById(R.id.rclview_category_page_category_products);


        rclviewProductList.setLayoutManager(new GridLayoutManager(GlobalConstants.APPLICATION_CONTEXT, 2));

        initData();

        return categoryPage;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void initData()
    {
        ProductCategoryHttp productCategoryHttp = GlobalRetrofit.getRetrofitInstance().create(ProductCategoryHttp.class);

        productCategoryHttp.getSubCategoryList(GlobalConstants.ZONE_CODE).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DataResult<ProductCategory>>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onNext(DataResult<ProductCategory> productCategoryDataResult)
                    {

                    }
                });

    }
}
