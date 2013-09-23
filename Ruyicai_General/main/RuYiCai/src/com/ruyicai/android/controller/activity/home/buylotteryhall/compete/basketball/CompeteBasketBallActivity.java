package com.ruyicai.android.controller.activity.home.buylotteryhall.compete.basketball;

import com.ruyicai.android.controller.activity.home.buylotteryhall.compete.CompeteActivity;
import com.ruyicai.android.model.bean.lottery.LotteryType;

/**
 * 竞彩篮球选号页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-5
 */
public class CompeteBasketBallActivity extends CompeteActivity {
	@Override
	public void setTitleTextView() {
		_fTitleBar._fTitleTextView.setText(LotteryType.COMPETE_BASKETBALL
				.get_fLotteryName());
	}
}
