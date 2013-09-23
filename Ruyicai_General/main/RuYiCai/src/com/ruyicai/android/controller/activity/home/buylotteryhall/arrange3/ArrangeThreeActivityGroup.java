package com.ruyicai.android.controller.activity.home.buylotteryhall.arrange3;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 排列三选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ArrangeThreeActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.ARRANGE_THREE
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				ArrangeThreeSelfSelectActivity.class,
				ArrangeThreeGroupThreeActivity.class,
				ArrangeThreeGroupSixActivity.class };
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
