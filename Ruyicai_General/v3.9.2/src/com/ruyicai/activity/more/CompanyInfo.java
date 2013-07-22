/**
 * 
 */
package com.ruyicai.activity.more;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.interfaces.ReturnPage;
import com.umeng.analytics.MobclickAgent;

/**
 * 公司简介
 * 
 * @author Administrator
 * 
 */
public class CompanyInfo extends Activity {
	// ReturnPage returnPage;
	public static final String TITLE = "title";
	public static final String URL = "url";
	private ProgressDialog progressdialog;
	Handler handler = new Handler();
	String titleStr;
	String iFileName = "ruyihelper_about.html";
	private ImageView imageView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.company_info);
		imageView = (ImageView) findViewById(R.id.company_info_image);
		getInfo();
		showView();
	}

	public void getInfo() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			titleStr = bundle.getString(TITLE);
			iFileName = bundle.getString(URL);
			imageView.setVisibility(View.VISIBLE);
		} else {
			titleStr = getString(R.string.ruyihelper_about);
			imageView.setVisibility(View.GONE);
		}
	}

	public void showView() {
		TextView title = (TextView) findViewById(R.id.company_infoTitle_text);
		WebView webView = (WebView) findViewById(R.id.company_info_webview);
		title.setText(titleStr);
		String url = "file:///android_asset/" + iFileName;
		webView.loadUrl(url);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
