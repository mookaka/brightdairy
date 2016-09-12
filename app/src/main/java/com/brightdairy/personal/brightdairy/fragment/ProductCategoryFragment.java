package com.brightdairy.personal.brightdairy.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.ProductCategoryHttp;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.adapter.CategoryPageLeftListAdapter;
import com.brightdairy.personal.brightdairy.adapter.CategoryPageRightInfoAdapter;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.DataResult;
import com.brightdairy.personal.model.entity.CategoryForTitle;
import com.brightdairy.personal.model.entity.ProductCategory;
import com.brightdairy.personal.model.entity.ProductInfo;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.adapter.rxjava.Result;
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
        rclviewProductList.setItemAnimator(new DefaultItemAnimator());

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
        ProductCategoryHttp productCategoryHttp = GlobalRetrofit.getRetrofitDev().create(ProductCategoryHttp.class);

        productCategoryHttp.getSubCategoryList(GlobalConstants.ZONE_CODE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<DataResult<ProductCategory>>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        fillViewData();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Result<DataResult<ProductCategory>> productCategoryDataResult)
                    {
                        ArrayList<ProductCategory> childCategoryList = productCategoryDataResult.response().body().result.childCategoryList;
                        cacheCategoryToLocal(childCategoryList);
                    }
                });

    }



    private void cacheCategoryToLocal(ArrayList<ProductCategory> productCategories)
    {
        Gson localCacheParser = new Gson();

        ArrayList<CategoryForTitle> categoryForTitles = new ArrayList<>();
        ArrayList<ProductInfo>  productInfos = new ArrayList<>();
        ArrayList<ProductInfo> childProductInfos = new ArrayList<>();

        for (int indexCategory = 0; indexCategory < productCategories.size(); indexCategory++)
        {
            productInfos.clear();
            ProductCategory parentCategory = productCategories.get(indexCategory);

            categoryForTitles.add(new CategoryForTitle(parentCategory.categoryIcon, parentCategory.categoryId, parentCategory.categoryName));

            ArrayList<ProductCategory> childProductCategory = parentCategory.childCategoryList;

            for(int index = 0; index < childProductCategory.size(); index++)
            {
                childProductInfos.clear();

                ProductCategory childCategory = childProductCategory.get(index);
                categoryForTitles.add(new CategoryForTitle(childCategory.categoryIcon, childCategory.categoryId, childCategory.categoryName));

                ArrayList<ProductInfo> childProductInfo = childCategory.productList;

                for(int indexProduct = 0; indexProduct < childProductInfo.size(); indexProduct++)
                {
                    ProductInfo productInfo = childProductInfo.get(indexProduct);
                    productInfos.add(productInfo);
                    childProductInfos.add(productInfo);
                }

                String childProductInfoCache = localCacheParser.toJson(childProductInfos);
                PrefUtil.setString(childCategory.categoryId, childProductInfoCache);

            }

            String productInfosCache = localCacheParser.toJson(productInfos);
            PrefUtil.setString(parentCategory.categoryId, productInfosCache);

        }

        String categoryTitlesCache = localCacheParser.toJson(categoryForTitles);
        PrefUtil.setString(GlobalConstants.ALL_CATEGORY, categoryTitlesCache);

    }


    private CategoryPageRightInfoAdapter categoryPageRightInfoAdapter;
    private void fillViewData()
    {
       categoryPageRightInfoAdapter = new CategoryPageRightInfoAdapter();
        listviewCategoryList.setAdapter(new CategoryPageLeftListAdapter());
        rclviewProductList.addItemDecoration(new SpaceItemDecoration(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8.0f, GlobalConstants.APPLICATION_CONTEXT.getResources().getDisplayMetrics())));
        rclviewProductList.setAdapter(categoryPageRightInfoAdapter);

        listviewCategoryList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                categoryPageRightInfoAdapter.freshProductList(position);
            }
        });

        refreshPageFocusState(0);
    }

    public void refreshPageFocusState(final int position)
    {
        listviewCategoryList.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                listviewCategoryList.requestFocusFromTouch();
                listviewCategoryList.setSelection(position);
                categoryPageRightInfoAdapter.freshProductList(position);
            }
        }, 500);
    }

    private static class SpaceItemDecoration extends RecyclerView.ItemDecoration
    {
        private int space;

        public SpaceItemDecoration(float space)
        {
            this.space = (int) space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
        {
            outRect.bottom = space;

            if(parent.getChildAdapterPosition(view)%2 ==0)
            {
                outRect.right = space;
            }
        }
    }

}
