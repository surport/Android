package com.ruyicai.activity.buy.miss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;

public class OrderDetails extends BuyActivityGroup {
	public static boolean isAlert = true;// 温馨提示
	private String[] titles = { "投注", "追号", "合买", "赠送" };
	private String[] topTitles = { "投注确认", "追号设置", "发起合买", "赠送彩票" };
	private Class[] allId = { ZiXuanTouZhu.class, ZixuanZhuihao.class,
			JoinStartActivity.class, GiftActivity.class };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constants.type = "fc";
		isIssue(false);
		init(titles, topTitles, allId);
		Intent intent = getIntent();
		isAlert = intent.getBooleanExtra("isAlert", true);
	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isAlert = true;
		isRun = false;
	}
}
