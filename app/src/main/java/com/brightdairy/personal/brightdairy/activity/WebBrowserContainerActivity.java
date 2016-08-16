package com.brightdairy.personal.brightdairy.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brightdairy.personal.brightdairy.R;
import com.brightdairy.personal.brightdairy.utils.PrefUtil;

/**
 * Created by shuangmusuihua on 2016/7/27.
 */
public class WebBrowserContainerActivity extends Activity
{
    private ImageButton backToHomeBtn;
    private TextView pageTitle;
    private WebView webBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.general_web_broswer_container);
        backToHomeBtn = (ImageButton) findViewById(R.id.btn_back_to_main_activity);
        pageTitle = (TextView) findViewById(R.id.txtview_page_title);
        webBrowser = (WebView) findViewById(R.id.webview_web_broswer);

        pageTitle.setText("活动");

        backToHomeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent jumpToHome = new Intent(WebBrowserContainerActivity.this, MainActivity.class);
                startActivity(jumpToHome);
            }
        });

        webBrowser.getSettings().setJavaScriptEnabled(true);

        webBrowser.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                WebBrowserContainerActivity.this.setProgress(newProgress * 1000);
                super.onProgressChanged(view, newProgress);
            }
        });

        webBrowser.loadUrl(PrefUtil.getString("adActionUrl", null));
    }
}
