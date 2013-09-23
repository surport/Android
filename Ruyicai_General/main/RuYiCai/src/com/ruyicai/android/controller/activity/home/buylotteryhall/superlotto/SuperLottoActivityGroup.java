package com.ruyicai.android.controller.activity.home.buylotteryhall.superlotto;

import android.view.View;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs.LotterySwitchTabsActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 大乐透选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class SuperLottoActivityGroup extends LotterySwitchTabsActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.SUPER_LOTTO
				.get_fLotteryName());
	}

	@Override
	protected void initLotteryInformationBarShow() {
		_fLotteryInformationBar.setVisibility(View.VISIBLE);
	}

	@Override
	protected void set_fSwithTabClasses() {
		_fSwithTabClasses = new Class<?>[] {
				SuperLottoSelfSelectActivity.class,
				SuperLottoCourageSelectActivity.class,
				SuperLottoTwelveSelectTwoActivity.class };
	}

	@Override
	protected void set_fSwitchTabTags() {
		_fSwitchTabTagIds = new int[] { R.string.tabhost_textview_selfselect,
				R.string.tabhost_textview_courageselect,
				R.string.tabhost_textview_12xuan2 };
	}

	@Override
	protected void setSwitchTabHostOnTabChangeListener() {

	}

}
