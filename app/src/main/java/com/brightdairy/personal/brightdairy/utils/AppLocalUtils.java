package com.brightdairy.personal.brightdairy.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;

import com.brightdairy.personal.api.GlobalHttpConfig;
import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.LoginRegisterHttp;
import com.brightdairy.personal.model.entity.ProductSendInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by shuangmusuihua on 2016/9/9.
 */
public class AppLocalUtils
{
    public static boolean isLogin()
    {
        String localUsrLoginId = PrefUtil.getString(GlobalConstants.AppConfig.UID_LOCAL, null);

        return localUsrLoginId != null && !localUsrLoginId.equals("");
    }

    public static boolean isValidPhoneNum(String phoneNum)
    {
        String mobileValidRegx = "^1[3|4|5|7|8][0-9]\\d{8}$";

        return !TextUtils.isEmpty(phoneNum) && phoneNum.matches(mobileValidRegx);
    }

    public static boolean isValidUserName(String userName)
    {
//        String nameValidRegx = "";
//        return !TextUtils.isEmpty(userName) && userName.matches(nameValidRegx);
        return true;
    }

    public static LoginRegisterHttp loginRegisterHttp;
    public static LoginRegisterHttp getLoginRegisterApi()
    {
        if(loginRegisterHttp == null)
        {
            loginRegisterHttp = GlobalRetrofit.getRetrofitDev().create(LoginRegisterHttp.class);
        }

        return loginRegisterHttp;
    }



    public static final int DATE_NOW = 0;
    public static final int DATE_NEXT_MONTH_FROM_NOW = 1;
    public static final int DATE_NEXT_MONTH = 2;
    public static final int DATE_NEXT_TWO_MONTH = 3;
    public static final int DATE_NEXT_THREE_MONTH = 4;
    public static String getDateWithOffset(int offset)
    {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        switch (offset)
        {
            default:
            case DATE_NOW:
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                return simpleDateFormat.format(calendar.getTime());
            case DATE_NEXT_MONTH_FROM_NOW:
                calendar.add(Calendar.MONTH, DATE_NEXT_MONTH_FROM_NOW);
                return simpleDateFormat.format(calendar.getTime());
            case DATE_NEXT_MONTH:
                calendar.add(Calendar.MONTH, DATE_NEXT_MONTH);
                return simpleDateFormat.format(calendar.getTime());
            case DATE_NEXT_TWO_MONTH:
                calendar.add(Calendar.MONTH, DATE_NEXT_TWO_MONTH);
                return simpleDateFormat.format(calendar.getTime());
            case DATE_NEXT_THREE_MONTH:
                calendar.add(Calendar.MONTH, DATE_NEXT_THREE_MONTH);
                return simpleDateFormat.format(calendar.getTime());



        }
    }



    private static final byte[] iv = "12345678".getBytes();
    private static final String encryptKey = "babayeye";
    public static String encyptPwd(String pwd)
    {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = null;

        try
        {
            cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(pwd.getBytes());
            return new String(Base64.encode(encryptedData, Base64.NO_WRAP));

        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }

        return "";
    }


    public static String getTotalAmount(ProductSendInfo info)
    {
        return String.valueOf(new Random().nextInt(10));
    }


    public static String[] fussImgUrl(String[] initImgUrls)
    {

        if(initImgUrls != null && initImgUrls.length > 0)
        {
            StringBuilder fussImgUrl = new StringBuilder().append(GlobalConstants.IMG_URL_BASE);
            final int IMG_URL_BASE_LEN = GlobalConstants.IMG_URL_BASE.length();

            for (int index = 0; index < initImgUrls.length; index++)
            {
                fussImgUrl.delete(IMG_URL_BASE_LEN, fussImgUrl.length());
                initImgUrls[index] = fussImgUrl.append(initImgUrls[index]).toString();
            }
        }

        return initImgUrls;
    }


    private static Gson GlobalGson;

    public static Gson getGlobalGson()
    {
        if (GlobalGson == null)
        {
            GlobalGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }

        return GlobalGson;
    }

    private static float SCREEN_FACTOR = -1;

    public static float getScreenFactor()
    {
        if (SCREEN_FACTOR == -1)
        {
            SCREEN_FACTOR = GlobalConstants.APPLICATION_CONTEXT.getResources().getDisplayMetrics().density;
        }

        return SCREEN_FACTOR;
    }


    public static String getPIN()
    {
        StringBuilder pinInitial = new StringBuilder();

        pinInitial.append(GlobalHttpConfig.PID).append(GlobalHttpConfig.UID)
                .append(GlobalConstants.AppConfig.FAA_KEY);

        return GeneralUtils.str2HashKey(pinInitial.toString());
    }

}
