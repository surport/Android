package com.ruyicai.android.controller.activity.home.buylotteryhall.betinformation;

import android.os.Bundle;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.BaseActivity;

/**
 * 合买信息页面：用户选中投注号码后，显示合买信息：彩种、期号、注码、倍数、注数、方案总额、认购金额、最低跟单、我要保底、全额保底、我的提成、
 * 是否公开和方案描述等信息。用户双色球、大乐透。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public class UnionInformationActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unioninformation_activity);
	}
}
