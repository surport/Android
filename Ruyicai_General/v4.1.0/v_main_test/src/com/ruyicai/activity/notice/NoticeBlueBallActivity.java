package com.ruyicai.activity.notice;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;

/**
 * 蓝球走势图
 * 
 * @author Administrator
 * 
 */
public class NoticeBlueBallActivity extends NoticeBallActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noticeAllNet(false);
	}
}
