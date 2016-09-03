package com.brightdairy.personal.model.HttpReqBody;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class UserLogin
{
    public String userLoginId;
    public String password;

    public UserLogin(String password, String userLoginId)
    {
        this.password = password;
        this.userLoginId = userLoginId;
    }
}
