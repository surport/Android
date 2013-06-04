/**
 * 
 */
package com.ruyicai.activity.account;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroidxuancai.R;

/**
 * 公司简介
 * 
 * @author Administrator
 * 
 */
public class Accoutmovecash extends Activity {
	// ReturnPage returnPage;
	public static final String TITLE = "title";
	public static final String URL = "url";
	Handler handler = new Handler();
    String titleStr = "银行转账";
    String iFileName = "accoutchangecash.html";
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showView());
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
