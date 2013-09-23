package com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation;

import android.os.Bundle;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;

/**
 * 赠送信息页面：用户选中投注号码后，显示赠送彩票信息：彩种、期号、注码、倍数、注数、朋友联系方式、你的赠言和金额。用户双色球和大乐透。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public class PresentInformationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.presentinformation_activity);
	}
}
