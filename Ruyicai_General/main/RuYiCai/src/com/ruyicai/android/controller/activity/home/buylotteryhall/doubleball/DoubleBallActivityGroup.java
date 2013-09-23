package com.ruyicai.android.controller.activity.home.buylotteryhall.doubleball;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 双色球选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class DoubleBallActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.DOUBLE_BALL
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				DoubleBallSelfSelectActivity.class,
				DoubleBallCourageSelectActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_selfselect,
				R.string.tabhost_textview_courageselect };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {
		
	}
}
