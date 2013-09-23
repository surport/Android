package com.ruyicai.android.controller.activity.home.buylotteryhall.guangdong11xuan5;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.activitygroups.SpinnersActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 广东11选5选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class GuangDongElevenSelectFiveActivityGroup extends
		SpinnersActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView
				.setText(LotteryType.GUANGDONG_ELEVENE_SELECT_FIVE
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
		_fSpinnerStringIds = new int[] { R.string.spinner_item_optionaltwo,
				R.string.spinner_item_optionalthree,
				R.string.spinner_item_optionalfour,
				R.string.spinner_item_optionalfive,
				R.string.spinner_item_optionalsix,
				R.string.spinner_item_optionalseven,
				R.string.spinner_item_optionaleight,
				R.string.spinner_item_beforoneselfselect,
				R.string.spinner_item_befortwoselfselect,
				R.string.spinner_item_beforthreeselfselect,
				R.string.spinner_item_befortwogroupselect,
				R.string.spinner_item_beforthreegroupselect };
	}

	@Override
	protected void set_fSpinnerClasses() {
		_fSpinnersClasses = new Class<?>[] {
				GuangDongElevenSelectFiveOptionalTwoActivity.class,
				GuangDongElevenSelectFiveOptionalThreeActivity.class,
				GuangDongElevenSelectFiveOptionalFourActivity.class,
				GuangDongElevenSelectFiveOptionalFiveActivity.class,
				GuangDongElevenSelectFiveOptionalSixActivity.class,
				GuangDongElevenSelectFiveOptionalSevenActivity.class,
				GuangDongElevenSelectFiveOptionalEightActivity.class,
				GuangDongElevenSelectFiveBeforOneSelfSelectActivity.class,
				GuangDongElevenSelectFiveBeforTwoSelfSelectActivity.class,
				GuangDongElevenSelectFiveBeforThreeSelfSelectActivity.class,
				GuangDongElevenSelectFiveBeforTwoGroupSelectActivity.class,
				GuangDongElevenSelectFiveBeforThreeGroupSelectActivity.class };
	}

}
