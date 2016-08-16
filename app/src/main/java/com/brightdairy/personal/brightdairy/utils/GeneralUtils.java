package com.brightdairy.personal.brightdairy.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shuangmusuihua on 2016/7/28.
 */
public class GeneralUtils
{
    private static Toast toast;

    /**
     * 单例吐司
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg)
    {
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        toast.setText(msg);
        toast.show();
    }

    public static Boolean isDateExpired(String localExpiredDare)
    {
        long givenExpireDate = PrefUtil.getLong(localExpiredDare, 0);
        return System.currentTimeMillis() > givenExpireDate;
    }


    public static File getDiskCacheDir(String uniqueFileName)
    {
        String cachePath;
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = GlobalConstants.APPLICATION_CONTEXT.getExternalCacheDir().getPath();
        }else {
            cachePath = GlobalConstants.APPLICATION_CONTEXT.getCacheDir().getPath();
        }

        return new File(cachePath + File.separator + uniqueFileName);
    }

    public static int getAPPVersion()
    {
        try {
            PackageInfo info = GlobalConstants.APPLICATION_CONTEXT.getPackageManager()
                    .getPackageInfo(GlobalConstants.APPLICATION_CONTEXT.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        return 1;
    }

    public static String str2HashKey(String str)
    {
        String hashKey;

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            hashKey = bytes2HexStr(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            hashKey = String.valueOf(str.hashCode());
            e.printStackTrace();
        }

        return hashKey;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap)
    {
        Drawable drawable = new BitmapDrawable(GlobalConstants.APPLICATION_CONTEXT.getResources(), bitmap);
        return drawable;
    }

    private static String bytes2HexStr(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1)
            {
                sb.append('0');
            }

            sb.append(hex);
        }

        return sb.toString();
    }
}
