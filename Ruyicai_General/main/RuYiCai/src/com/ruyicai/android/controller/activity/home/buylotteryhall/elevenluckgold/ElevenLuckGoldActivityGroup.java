package com.ruyicai.android.controller.activity.home.buylotteryhall.elevenluckgold;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.buylotteryhall.activitygroups.SpinnersActivityGroup;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforOneGroupSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforOneSelfSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforThreeGroupSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforThreeSelfSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforTwoGroupSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveBeforTwoSelfSelectActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalEightActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalFiveActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalFourActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalSevenActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalSixActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalThreeActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.jiangxi11xuan5.JiangXiElevenSelectFiveOptionalTwoActivity;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 11运夺金选号页面组
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class ElevenLuckGoldActivityGroup extends SpinnersActivityGroup {

	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.ELEVEN_LUCKGOLD
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
				R.string.spinner_item_befortwogroupselect,
				R.string.spinner_item_beforthreegroupselect };
	}

	@Override
	protected void set_fSpinnerClasses() {
		_fSpinnersClasses = new Class<?>[] {
				ElvenLuckGoldOptionalTwoActivity.class,
				ElvenLuckGoldOptionalThreeActivity.class,
				ElvenLuckGoldOptionalFourActivity.class,
				ElvenLuckGoldOptionalFiveActivity.class,
				ElvenLuckGoldOptionalSixActivity.class,
				ElvenLuckGoldOptionalSevenActivity.class,
				ElvenLuckGoldOptionalEightActivity.class,
				ElvenLuckGoldBeforOneSelfSelectActivity.class,
				ElvenLuckGoldBeforTwoSelfSelectActivity.class,
				ElvenLuckGoldBeforThreeSelfSelectActivity.class,
				ElvenLuckGoldBeforTwoGroupSelectActivity.class,
				ElvenLuckGoldBeforThreeGroupSelectActivity.class };
	}

	@Override
	public void setTitleButton() {

	}

	@Override
	protected void initLotteryInformationBarShow() {

	}

}
