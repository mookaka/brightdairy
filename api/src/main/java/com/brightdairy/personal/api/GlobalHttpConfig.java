package com.brightdairy.personal.api;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class GlobalHttpConfig
{
    public static String PID = "";
    public static String UID = "";
    public static String RID = "FAA";
    public static String PIN = "";
    public static String TID = "";


    public interface LOGIN_MSGCODE
    {
        String REQUST_OK = "000";
        String REQUST_FAILED = "104";
        String SEND_SMS_FAILED = "142";
        String SEND_SMS_INVALID_PHONE_NUM = "143";
        String LOGIN_FAILED = "293";
        String PHONE_NUM_DOUBLE_REGISTER = "130";
        String PWD_NOT_SAME = "134";
        String VALID_CODE_EXPIRED = "131";
        String BAD_VALID_CODE = "141";
    }

}
