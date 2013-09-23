package com.ruyicai.android.controller.activity.home.buylotteryhall.welfare3d;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 福彩3D选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class Welfare3DActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.WELFARE_3D
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] { Welfare3DSelfSelectActivity.class,
				Welfare3DGroupThreeActivity.class,
				Welfare3DGroupSixActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_selfselect,
				R.string.tabhost_textview_group3,
				R.string.tabhost_textview_group6 };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {

	}

}
