package com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation;

import android.os.Bundle;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;

/**
 * 追号信息页面：用于选中投注号码后，显示追号信息：彩种、期号、注码、倍数、期数、注数、中奖停止追号和金额信息。用于双色球、大乐透。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public class AppendInformationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appendedinformation_activity);
	}
}
