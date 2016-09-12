package com.brightdairy.personal.model.HttpReqBody;

/**
 * Created by shuangmusuihua on 2016/9/11.
 */
public class ModifyPwdBySms
{
    public String userLoginId;
    public String phone;
    public String password;
    public String newPassword;
    public String confirmPassword;
    public String inputCode;
    public String usernamereg = "N";
    public String operation = "GETPSW";

    public ModifyPwdBySms(String newPassword,
                          String inputCode,
                          String confirmPassword,
                          String password,
                          String phone,
                          String userLoginId) {
        this.newPassword = newPassword;
        this.inputCode = inputCode;
        this.confirmPassword = confirmPassword;
        this.password = password;
        this.phone = phone;
        this.userLoginId = userLoginId;
    }
}
