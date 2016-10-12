package com.brightdairy.personal.brightdairy.popup;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.activity.LoginSmsActivity;
import com.brightdairy.personal.brightdairy.utils.GlobalConstants;

/**
 * Created by shuangmusuihua on 2016/10/10.
 */

public class DialogPopupHelper
{
    public static void showLoginPopup(final FragmentActivity activity)
    {

        DialogPopup dialogPopup = DialogPopup.newInstance(
                activity.getResources().getString(R.string.need_login),
                activity.getResources().getString(R.string.confirm_login),
                activity.getResources().getString(R.string.cancel_login));

        dialogPopup.setDialogListener(new DialogPopup.DialogListener()
        {
            @Override
            public void onConfirmClick()
            {
                Intent jumpToLogin = new Intent(activity, LoginSmsActivity.class);
                jumpToLogin.putExtra(GlobalConstants.INTENT_FLAG.NEED_RELOGIN, true);
                activity.startActivityForResult(jumpToLogin, GlobalConstants.INTENT_FLAG.RELOGIN_REQ_FLG);
            }

            @Override
            public void onCancelClick()
            {

            }
        });

        dialogPopup.show(activity.getSupportFragmentManager(), "needLogin");
    }

    public static void showInfoNoMoreAction(FragmentActivity activity)
    {
        ShowInfoDialog showInfoDialog = ShowInfoDialog.newInstance(activity.getResources().getString(R.string.point_usage_rule));

        showInfoDialog.show(activity.getSupportFragmentManager(), "showInfoNoMoreAction");
    }
}
