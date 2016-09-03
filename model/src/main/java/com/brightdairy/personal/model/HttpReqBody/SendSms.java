package com.brightdairy.personal.model.HttpReqBody;

/**
 * Created by shuangmusuihua on 2016/9/3.
 */
public class SendSms
{
    public String sendTo;
    public String operation;
    public String captcha_input = "";
    public String inputCode = "";
    public int appType = -1;
    public int sendMobileTime = -1;
    public int usernamereg = -1;

    public SendSms(String operation, String sendTo)
    {
        this.operation = operation;
        this.sendTo = sendTo;
    }
}
