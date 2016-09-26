package com.brightdairy.personal.brightdairy.view.RecyclerviewItemDecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brightdairy.personal.brightdairy.ViewHolder.ShopCartProductVH;
import com.brightdairy.personal.brightdairy.adapter.ShopCartAdapter;

/**
 * Created by shuangmusuihua on 2016/9/21.
 */
public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration
{

    private final int mVerticalSpaceHeight;
    private final int ITEM_TYPE_SUPPLIER = 0x12;
    private final int ITEM_TYPE_PRODUCT = 0x13;

    public VerticalSpaceItemDecoration(int mVerticalSpaceHeight)
    {
        this.mVerticalSpaceHeight = mVerticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state)
    {
        switch (parent.getAdapter().getItemViewType(parent.getChildAdapterPosition(view)))
        {
            case ITEM_TYPE_PRODUCT:
                outRect.bottom = mVerticalSpaceHeight;
                break;
            case ITEM_TYPE_SUPPLIER:
                outRect.bottom = mVerticalSpaceHeight;
                outRect.top = mVerticalSpaceHeight * 10;
                break;
            default:
                outRect.bottom = mVerticalSpaceHeight;
        }
    }

}
