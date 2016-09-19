package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.LocalStoreUtil;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shuangmusuihua on 2016/7/27.
 */
public class AdPageActivity extends Activity
{
    private TextView adTimeLeft;
    private ImageView adImgContainer;
    private Timer adPageJumpTimer;

    private Handler pageConfig = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x0003:
                    adTimeLeft.setText(String.valueOf(msg.arg1));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_page);
        adImgContainer = (ImageView)findViewById(R.id.imgview_ad_img_container);
        adTimeLeft = (TextView)findViewById(R.id.txtview_ad_left_time);
        ConfigAdPage();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        adPageJumpTimer.cancel();
        pageConfig.removeCallbacksAndMessages(null);
    }

    private void ConfigAdPage()
    {
        String usrl = PrefUtil.getString("adImgUrl", null);
        Glide.with(this).load(usrl).asBitmap().into(adImgContainer);
        adPageJumpTimer = new Timer();

        adPageJumpTimer.scheduleAtFixedRate(new TimerTask()
        {
            int curTime = 5;
            @Override
            public void run()
            {
                if(curTime-- > 0)
                {
                    Message msg = pageConfig.obtainMessage();
                    msg.arg1 =curTime;
                    msg.what = 0x0003;
                    pageConfig.sendMessage(msg);
                } else
                {
                    Intent goToHome = new Intent(AdPageActivity.this, MainActivity.class);
                    startActivity(goToHome);
                    AdPageActivity.this.finish();

                }
            }
        }, 0, 1000);

        switch (PrefUtil.getString("adAction", null))
        {
            case "h5":

                adImgContainer.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent jumpToWebPage = new Intent(AdPageActivity.this, WebBrowserContainerActivity.class);
                        startActivity(jumpToWebPage);
                        AdPageActivity.this.finish();
                    }
                });

                break;
            case "page":
                break;
            default:
                break;
        }

    }
}
