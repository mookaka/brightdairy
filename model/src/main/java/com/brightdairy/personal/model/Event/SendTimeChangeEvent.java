package com.brightdairy.personal.model.Event;

/**
 * Created by shuangmusuihua on 2016/9/20.
 */
public class SendTimeChangeEvent
{
    public String startTime;
    public String endTime;

    public SendTimeChangeEvent(String startTime, String endTime) {
        this.endTime = endTime;
        this.startTime = startTime;
    }
}
