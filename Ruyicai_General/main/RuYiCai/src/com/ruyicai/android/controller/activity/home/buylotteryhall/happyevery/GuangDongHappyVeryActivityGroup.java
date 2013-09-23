package com.ruyicai.android.controller.activity.home.buylotteryhall.happyevery;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.activitygroups.SpinnersActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 广东快乐十分选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class GuangDongHappyVeryActivityGroup extends SpinnersActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.GUANGDONG_HAPPYVERY
				.get_fLotteryName());
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	protected void initLotteryInformationBarShow() {

	}

	@Override
	protected void set_fSpinnerItems() {
		_fSpinnerStringIds = new int[] {
				R.string.spinner_item_selectonenumbetting,
				R.string.spinner_item_selectoneredbetting,
				R.string.spinner_item_optionaltwo,
				R.string.spinner_item_optionalthree,
				R.string.spinner_item_optionalfour,
				R.string.spinner_item_optionalfive,
				R.string.spinner_item_selecttwolinkdirectly,
				R.string.spinner_item_selectthreebeforgroup,
				R.string.spinner_item_selecttwolinkgroup,
				R.string.spinner_item_selectthreebeforgroup };
	}

	@Override
	protected void set_fSpinnerClasses() {
		_fSpinnersClasses = new Class<?>[] {
				GuangDongHappyVerySelectOneNumBettingActivity.class,
				GuangDongHappyVerySelectOneRedBettingActivity.class,
				GuangDongHappyVeryOptionalTwoActivity.class,
				GuangDongHappyVeryOptionalThreeActivity.class,
				GuangDongHappyVeryOptionalFourActivity.class,
				GuangDongHappyVeryOptionalFiveActivity.class,
				GuangDongHappyVerySelectTwoLinkDirectlyActivity.class,
				GuangDongHappyVerySelectThreeBeforDirectly.class,
				GuangDongHappyVerySelectTwoLinkGroup.class,
				GuangDongHappyVerySelectThreeBeforGroupActivity.class };
	}

}
