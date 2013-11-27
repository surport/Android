package com.ruyicai.activity.usercenter;

import com.palmdream.RuyicaiAndroid.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.webkit.WebView;
import android.widget.TextView;

public class MyLetterActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.myletter);
		init();
	}

	private void init() {
		Bundle bundle = getIntent().getExtras();
		String Content = bundle.getString("content");
		String title = bundle.getString("title");
		TextView titletext = (TextView) findViewById(R.id.informationtitle);
		WebView web = (WebView) findViewById(R.id.letterview);
		titletext.setText(Html.fromHtml(title));
		web.loadDataWithBaseURL(null, Content, "text/html", "utf-8", null);
	}
}
