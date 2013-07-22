package com.ruyicai.activity.buy.twentytwo;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class TwentyTwo extends BuyActivityGroup {

	private String[] titles = { "自选", "胆拖" };
	private String[] topTitles = { "22选5", "22选5" };
	private Class[] allId = { TwentyZiXuan.class, TwentyDanTuo.class };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_22_5);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_22_5);
		setlastbatchcode(Constants.LOTNO_22_5);
		MobclickAgent.onEvent(this, "22xuan5"); // BY贺思明 点击首页的“七22选5彩”图标
		MobclickAgent.onEvent(this, "ticaigoucaijiemian ");// BY贺思明 体彩购彩页
	}
}
