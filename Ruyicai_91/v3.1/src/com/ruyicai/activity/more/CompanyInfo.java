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

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.interfaces.ReturnPage;

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
    String titleStr = "关于授权";
    String iFileName = "ruyihelper_authorizing.html";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getInfo();
		setContentView(showView());
	}
    public void getInfo(){
    	Intent intent = getIntent();
    	Bundle bundle = intent.getExtras();
    	if(bundle != null){
    		titleStr = bundle.getString(TITLE);
    		iFileName = bundle.getString(URL);
    	}
    }
	public View showView() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.layout_ruyizhushou,null);

		Button btnreturn = (Button) view.findViewById(R.id.ruyizhushou_btn_return);
		TextView title = (TextView) view.findViewById(R.id.ruyipackage_title);
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		WebView webView = (WebView) view.findViewById(R.id.ruyipackage_webview);
		title.setText(titleStr);
		String url = "file:///android_asset/" + iFileName;
		webView.loadUrl(url);
		return view;

	}
}
