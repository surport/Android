/**
 * 
 */
package com.ruyicai.activity.more;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;

/**
 * 公司简介
 * 
 * @author Administrator
 * 
 */
public class CompanyInfo extends Activity {
	// ReturnPage returnPage;
	private ProgressDialog progressdialog;
	Handler handler = new Handler();

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
		String iFileName = "ruyihelper_authorizing.html";
		title.setText("关于授权");
		String url = "file:///android_asset/" + iFileName;
		webView.loadUrl(url);
		return view;

	}
}
