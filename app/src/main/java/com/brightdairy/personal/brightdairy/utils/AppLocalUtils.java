package com.brightdairy.personal.brightdairy.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.brightdairy.personal.api.GlobalRetrofit;
import com.brightdairy.personal.api.LoginRegisterHttp;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

    public static LoginRegisterHttp loginRegisterHttp;
    public static LoginRegisterHttp getLoginRegisterApi()
    {
        if(loginRegisterHttp == null)
        {
            loginRegisterHttp = GlobalRetrofit.getRetrofitDev().create(LoginRegisterHttp.class);
        }

        return loginRegisterHttp;
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


}
