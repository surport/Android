package com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.activitygroups.SpinnersActivityGroup;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 江西11选5选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class JiangXiElevenSelectFiveActivityGroup extends SpinnersActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView
				.setText(LotteryType.JIANGXI_ELEVEN_SELECT_FIVE
						.get_fLotteryName());
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
				R.string.spinner_item_beforonesgroupselect,
				R.string.spinner_item_befortwogroupselect,
				R.string.spinner_item_beforthreegroupselect };
	}

	@Override
	protected void set_fSpinnerClasses() {
		_fSpinnersClasses = new Class<?>[] {
				JiangXiElevenSelectFiveOptionalTwoActivity.class,
				JiangXiElevenSelectFiveOptionalThreeActivity.class,
				JiangXiElevenSelectFiveOptionalFourActivity.class,
				JiangXiElevenSelectFiveOptionalFiveActivity.class,
				JiangXiElevenSelectFiveOptionalSixActivity.class,
				JiangXiElevenSelectFiveOptionalSevenActivity.class,
				JiangXiElevenSelectFiveOptionalEightActivity.class,
				JiangXiElevenSelectFiveBeforOneSelfSelectActivity.class,
				JiangXiElevenSelectFiveBeforTwoSelfSelectActivity.class,
				JiangXiElevenSelectFiveBeforThreeSelfSelectActivity.class,
				JiangXiElevenSelectFiveBeforOneGroupSelectActivity.class,
				JiangXiElevenSelectFiveBeforTwoGroupSelectActivity.class,
				JiangXiElevenSelectFiveBeforThreeGroupSelectActivity.class };
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	protected void initLotteryInformationBarShow() {

	}
}
