package com.brightdairy.personal.brightdairy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.ProductInfoItemVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.entity.CategoryForTitle;
import com.brightdairy.personal.model.entity.ProductInfo;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class CategoryPageRightInfoAdapter extends RecyclerView.Adapter<ProductInfoItemVH>
{

    private ArrayList<ProductInfo> productInfos;
    private ArrayList<CategoryForTitle> categoryForTitles;
    private LayoutInflater layoutInflater;
    private Gson gson;
    private StringBuilder fussStrBuilder;

    public CategoryPageRightInfoAdapter()
    {
        productInfos = new ArrayList<>();
        layoutInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
        gson = new Gson();
        fussStrBuilder = new StringBuilder();

        String categoryTitles = PrefUtil.getString(GlobalConstants.ALL_CATEGORY, "");

        if(!categoryTitles.equals(""))
        {
            categoryForTitles = gson.fromJson(categoryTitles, new TypeToken<ArrayList<CategoryForTitle>>() {}.getType());
        }
    }

    @Override
    public ProductInfoItemVH onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View productInfoView = layoutInflater.inflate(R.layout.item_category__product_info, null);

        return new ProductInfoItemVH(productInfoView);
    }

    private ProductInfo productInfo;
    @Override
    public void onBindViewHolder(ProductInfoItemVH holder, int position)
    {
        productInfo = productInfos.get(position);

        Glide.with(GlobalConstants.APPLICATION_CONTEXT).load(productInfo.imageUrl)
                .asBitmap()
                .into(holder.imgviewProductImg);

        holder.txtviewProductName.setText(productInfo.productName);

        fussStrBuilder.append("(").append(productInfo.productVol).append(productInfo.productVolUom).append(")");

        holder.txtviewProductVol.setText(fussStrBuilder.toString());

        fussStrBuilder.delete(0, fussStrBuilder.length());

        fussStrBuilder.append("随心订价：￥").append(productInfo.productPrice);

        holder.txtviewProductPrice.setText(fussStrBuilder.toString());

        fussStrBuilder.delete(0, fussStrBuilder.length());
    }

    @Override
    public int getItemCount()
    {
        return productInfos.size();
    }

    public void freshProductList(int position)
    {
        CategoryForTitle categoryForTitle = categoryForTitles.get(position);
        String categoryId = categoryForTitle.categoryId;

        String productInfosCache = PrefUtil.getString(categoryId, "");

        if(!productInfosCache.equals(""))
        {
            productInfos.clear();
            productInfos = gson.fromJson(productInfosCache, new TypeToken<ArrayList<ProductInfo>>() {}.getType());
        }

        notifyDataSetChanged();
    }
}
