package com.ruyicai.android.controller.activity.home.buylotteryhall.sevenstar;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 七星彩选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ServenStarActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.SEVEN_STAR
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				ServenStarSelfSelectActivity.class,
				ServenStarRandomSelectActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_selfselect,
				R.string.tabhost_textview_randomselect };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {

	}

}
