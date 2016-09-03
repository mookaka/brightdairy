package com.brightdairy.personal.model.HttpReqBody;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class UserRegister
{
    public String userName;
    public String passWord;
    public String confirmPassword;
    public String phone;
    public String inputCode;
    public String sendSmsId;

    public String mailType = "";
    public boolean requireEmail = false;
    public String usernamereg = "usernamereg";
    public String url = "";
    public String forgetpwurl = "";
    public String basePath = "";
    public String operation = "REGISTE";

    public UserRegister(String userName, String passWord
            , String confirmPassword
            , String phone
            , String inputCode, String sendSmsId)
    {
        this.userName = userName;
        this.passWord = passWord;
        this.confirmPassword = confirmPassword;
        this.phone = phone;
        this.inputCode = inputCode;
        this.sendSmsId = sendSmsId;
    }
}
