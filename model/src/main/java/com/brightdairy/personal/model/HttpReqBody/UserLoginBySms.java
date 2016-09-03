package com.brightdairy.personal.model.HttpReqBody;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class UserLoginBySms
{
    public String userLoginId;
    public String isPhone = "Y";
    public String inputCode;

    public UserLoginBySms(String inputCode, String userLoginId)
    {
        this.inputCode = inputCode;
        this.userLoginId = userLoginId;
    }
}
