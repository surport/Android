package com.ruyicai.activity.notice;

import android.os.Bundle;

public class BeforThreeNoticeBallActivity extends NoticeRedBallActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isBeforeThree = true;
		super.onCreate(savedInstanceState);
	}
}
