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

    public interface HTTP_HEADER
    {
        String PID = "pid";
        String UID = "uid";
        String PIN = "pin";
        String TID = "tid";
    }

    public interface API_MSGCODE
    {
        String REQUST_OK = "000";
        String REQUST_OK_NO_CONTENT = "010";
        String SHOP_CART_EMPTY = "401";
        String REQUST_FAILED = "104";
        String SEND_SMS_FAILED = "142";
        String SEND_SMS_INVALID_PHONE_NUM = "143";
        String LOGIN_FAILED = "293";
        String PHONE_NUM_DOUBLE_REGISTER = "130";
        String PWD_NOT_SAME = "134";
        String VALID_CODE_EXPIRED = "131";
        String BAD_VALID_CODE = "141";
        String NEED_RELOGIN = "800";

        String ADDRESS_EXISTED = "201";
        String CANNOT_CREATE_CONTACT = "202";
        String CREATE_ADDRESS_ERR = "203";
        String ADDRESS_NUM_NOT_EXIST = "204";
        String ADDRESS_DONT_EXIST = "205";
        String UPDATE_ADDRESS_ERR = "206";
        String DELETE_ADDRESS_NOT_EXIST = "207";
        String DELETE_ADDRESS_ERR = "208";
        String EMPTY_AVAILABLE_ADDRESS = "211";
    }

}
