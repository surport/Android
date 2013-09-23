package com.ruyicai.android.controller.activity.home.buylotteryhall.sevenhappy;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 七乐彩选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ServenHappyActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.SERVEN_HAPPY
				.get_fLotteryName());
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				SevenHappySelfSelectActivity.class,
				SevenHappyCourageSelectActivity.class };
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
