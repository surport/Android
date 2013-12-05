package com.ruyicai.activity.buy.zixuan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;

public class OrderDetails extends BuyActivityGroup {
	public static boolean isAlert = true;// 温馨提示
	private String[] titles = { "投注", "追号", "合买", "赠送" };
	private String[] topTitles = { "投注确认", "追号设置", "发起合买", "赠送彩票" };
	private Class[] allId = { ZiXuanTouZhu.class, ZixuanZhuihao.class,
			JoinStartActivity.class, GiftActivity.class };
	public static int fromInt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fromInt = getIntent().getIntExtra("from", 0);
		Intent intent = getIntent();
		isAlert = intent.getBooleanExtra("isAlert", true);
		Constants.type = "fc";
		isIssue(false);
		init(titles, topTitles, allId);
	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);

	}

}
