package com.brightdairy.personal.brightdairy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.ViewHolder.CategoryItemVH;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.brightdairy.personal.model.entity.CategoryForTitle;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by shuangmusuihua on 2016/8/12.
 */
public class CategoryPageLeftListAdapter extends BaseAdapter
{
    private ArrayList<CategoryForTitle> categoryForTitles;
    private LayoutInflater layoutInflater;

    public CategoryPageLeftListAdapter()
    {
        categoryForTitles = new ArrayList<>();
        String categoryTitles = PrefUtil.getString(GlobalConstants.ALL_CATEGORY, "");

        if(!categoryTitles.equals(""))
        {
            categoryForTitles = new Gson().fromJson(categoryTitles, new TypeToken<ArrayList<CategoryForTitle>>() {}.getType());
        }

        this.layoutInflater = LayoutInflater.from(GlobalConstants.APPLICATION_CONTEXT);
    }

    @Override
    public int getCount()
    {
        return categoryForTitles.size();
    }

    @Override
    public Object getItem(int position)
    {
        return categoryForTitles.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    private CategoryItemVH categoryItemVH = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if(convertView == null)
        {
            categoryItemVH = new CategoryItemVH();
            convertView = layoutInflater.inflate(R.layout.item_category_category_name, null);
            categoryItemVH.txtviewCategoryName = (TextView)convertView.findViewById(R.id.txtview_category_name);
            categoryItemVH.imgviewCategoryImg = (ImageView)convertView.findViewById(R.id.imgview_category_img);

            convertView.setTag(categoryItemVH);
        } else {
            categoryItemVH = (CategoryItemVH)convertView.getTag();
        }

        CategoryForTitle categoryForTitle = categoryForTitles.get(position);

        categoryItemVH.txtviewCategoryName.setText(categoryForTitle.categoryName);

        if(categoryForTitle.categoryIcon != null && categoryForTitle.categoryId.equals("freshMilk") || categoryForTitle.categoryId.equals("freshYogurt"))
        {
            Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                    .load(GlobalConstants.IMG_URL_BASE + categoryForTitle.categoryIcon)
                    .asBitmap()
                    .into(categoryItemVH.imgviewCategoryImg);

            categoryItemVH.imgviewCategoryImg.setVisibility(View.VISIBLE);
        }

        return convertView;
    }
}
