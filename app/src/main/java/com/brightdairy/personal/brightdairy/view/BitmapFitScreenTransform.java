package com.brightdairy.personal.brightdairy.view;

import android.content.Context;
import android.graphics.Bitmap;

import com.brightdairy.personal.brightdairy.utils.AppLocalUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by shuangmusuihua on 2016/10/11.
 */

public class BitmapFitScreenTransform extends BitmapTransformation
{
    private float screenFactor = 1;
    public BitmapFitScreenTransform(Context context)
    {
        super(context);
        screenFactor = AppLocalUtils.getScreenFactor();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight)
    {
        int bitmapWidth = Math.round(toTransform.getWidth() * screenFactor);
        int bitmapHeight = Math.round(toTransform.getHeight() * screenFactor);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(toTransform, bitmapWidth, bitmapHeight, false);
        toTransform.recycle();
        return scaledBitmap;
    }

    @Override
    public String getId()
    {
        return "bitmapfitscreen";
    }
}
