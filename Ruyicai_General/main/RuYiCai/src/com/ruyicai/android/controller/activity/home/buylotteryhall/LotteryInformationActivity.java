package com.ruyicai.android.controller.activity.home.buylotteryhall;

import roboguice.activity.RoboActivityGroup;
import android.os.Bundle;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.panel.SelectNumberPanel;

/**
 * 彩种资讯页面：
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-4
 */
public class LotteryInformationActivity extends RoboActivityGroup {
	private SelectNumberPanel	selectNumberPanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compete_againstlist_wintielossitem);
	}
}
