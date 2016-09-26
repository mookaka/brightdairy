package com.brightdairy.personal.model.Event;

/**
 * Created by shuangmusuihua on 2016/9/20.
 */
public class SendModeChangeEvent
{
    public String shipModuleId;
    public String shipModuleName;
    public String shipModuleType;

    public SendModeChangeEvent(String shipModuleId, String shipModuleName, String shipModuleType) {
        this.shipModuleId = shipModuleId;
        this.shipModuleName = shipModuleName;
        this.shipModuleType = shipModuleType;
    }
}
