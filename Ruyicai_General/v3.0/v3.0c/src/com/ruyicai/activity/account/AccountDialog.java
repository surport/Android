package com.ruyicai.activity.account;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.Window;
import android.webkit.CacheManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.palmdream.RuyicaiAndroid.R;

public class AccountDialog extends Activity{

	WebView mWebView;
	String account_url;
	private ProgressDialog mSpinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_openurlxml);
		mSpinner = new ProgressDialog(this);
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
		account_url =  getIntent().getStringExtra("accounturl");
		mWebView = (WebView)findViewById(R.id.account_webView);
		//
		mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		//
		mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WeiboWebViewClient());
        mWebView.loadUrl(account_url);
	}
	private class WeiboWebViewClient extends WebViewClient {
		  @Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			mSpinner.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
			   view.loadUrl(url); 
	           return true; 
	        }

	}


}
