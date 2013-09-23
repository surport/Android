package com.ruyicai.android.controller.activity.home.buylotteryhall.arrange5;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 排列五选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ArrangeFiveActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.ARRANGE_FIVE
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				ArrangeFiveSelfSelectActivity.class,
				ArrangeFiveRandomSelectActivity.class };
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
