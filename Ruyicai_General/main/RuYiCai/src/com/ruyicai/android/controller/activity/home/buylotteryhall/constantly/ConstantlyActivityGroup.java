package com.ruyicai.android.controller.activity.home.buylotteryhall.constantly;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 时时彩选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ConstantlyActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.CONSTANTLY
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] { ConstantlyOneStarActivity.class,
				ConstantlyTwoStarActivity.class,
				ConstantlyThreeStarActivity.class,
				ConstantlyFiveStarActivity.class,
				ConstantlyBigSmallActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_onestar,
				R.string.tabhost_textview_twostar,
				R.string.tabhost_textview_threestar,
				R.string.tabhost_textview_fivestar,
				R.string.tabhost_textview_bigsmall };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {

	}

}
