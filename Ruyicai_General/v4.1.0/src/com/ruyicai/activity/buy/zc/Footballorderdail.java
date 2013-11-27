package com.ruyicai.activity.buy.zc;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zixuan.ZiXuanTouZhu;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;

public class Footballorderdail extends BuyActivityGroup {
	private String[] titles = { "投注", "合买", "赠送" };
	private String[] topTitles = { "投注确认", "发起合买", "赠送彩票" };
	private Class[] allId = { ZiXuanTouZhu.class, JoinStartActivity.class,
			GiftActivity.class };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.type = "zc";
		isIssue(false);
		init(titles, topTitles, allId);
	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);

	}

}
