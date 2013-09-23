package com.ruyicai.android.controller.activity.home.buylotteryhall.twentytwoselectfive;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.SwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 22选5选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class TwentyTwoSelectFiveActivityGroup extends
		LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.TWENTYTWO_SELECT_FIVE
				.get_fLotteryName());
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				TwentyTwoSelectFiveSelfSelectActivity.class,
				TwentyTwoSelectFiveCourageSelectActivity.class };
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
