package com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation;

import android.os.Bundle;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;

/**
 * 投注信息页面：用于显示在选中投注号码后，显示投注信息：彩种，期号，注码，倍数，注数和金额等信息。用于双色球，大乐透。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public class BetInformationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bettinginformation_activity);
	}
}
