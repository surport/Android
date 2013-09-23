package com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5;

import android.app.ActivityGroup;
import android.os.Bundle;
import android.widget.TextView;

public class JiangXiElevenSelectFiveBeforTwoGroupSelectActivity extends
		ActivityGroup {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("13");
		setContentView(textView);
	}
}
