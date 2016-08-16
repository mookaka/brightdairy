package com.brightdairy.personal.brightdairy.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import libcore.io.DiskLruCache;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public class LocalStoreUtil
{
    public static final DiskLruCache mDiskLruCache = getDiskLruCache("bitmap");

    public static void storeBitmapByKey(final String imgUrl)
    {
        String hashKey = GeneralUtils.str2HashKey(imgUrl);
        try {
            final DiskLruCache.Editor editor = mDiskLruCache.edit(hashKey);
            if(editor != null)
            {
                final OutputStream bitmapOutStream = editor.newOutputStream(0);

                new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Glide.with(GlobalConstants.APPLICATION_CONTEXT)
                                .load(imgUrl)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                                    {
                                        resource.compress(Bitmap.CompressFormat.PNG, 100, bitmapOutStream);
                                        try {
                                            editor.commit();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        resource.recycle();
                                    }

                                    @Override
                                    public void onLoadFailed(Exception e, Drawable errorDrawable)
                                    {
                                        super.onLoadFailed(e, errorDrawable);
                                        try
                                        {
                                            editor.abort();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                });

                    }
                }).start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getLocalBitmap(String ImgUrl)
    {
        String hashKey = GeneralUtils.str2HashKey(ImgUrl);
        Bitmap bitmap = null;

        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(hashKey);
            if(snapshot != null)
            {
                InputStream bitmapInputStream = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(bitmapInputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static DiskLruCache getDiskLruCache(String uniqueName)
    {
        DiskLruCache diskLruCache = null;
        File cacheDir = GeneralUtils.getDiskCacheDir(uniqueName);

        try {
            if(!cacheDir.exists())
            {
                cacheDir.mkdirs();
            }
            diskLruCache = DiskLruCache.open(cacheDir, GeneralUtils.getAPPVersion(), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diskLruCache;
    }


}
