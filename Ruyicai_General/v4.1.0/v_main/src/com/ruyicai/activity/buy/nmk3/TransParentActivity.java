package com.ruyicai.activity.buy.nmk3;

import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.util.RuyicaiActivityManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class TransParentActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transparent_activity);
	}
}
