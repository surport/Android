package com.ruyicai.activity.webbrowser;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;

public class WebBrowser extends Activity {
	WebView webView = null;
	TextView tv = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//    	RuyicaiActivityManager.getInstance().addActivity(this);
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.webbrowser);
    	//tv = (TextView) findViewById(R.id.textview111);
    	webView = (WebView) findViewById(R.id.webView);
        
        webView.getSettings().setJavaScriptEnabled(true);
        
    	webView.loadUrl("file:///android_asset/webbrowser.html");
    	
    }
}
