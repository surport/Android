package com.ruyicai.activity.buy.qxc;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class QXC extends BuyActivityGroup {
	private String[] titles = { "自选", "机选" };
	private String[] topTitles = { "七星彩", "七星彩" };
	private Class[] allId = { QXCZhiXuan.class, QXCJX.class };
	
	public AddView addView = new AddView(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_QXC);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_QXC);
		setlastbatchcode(Constants.LOTNO_QXC);
		MobclickAgent.onEvent(this, "qixingcai"); // BY贺思明 点击首页的“七星彩”图标
		MobclickAgent.onEvent(this, "ticaigoucaijiemian ");// BY贺思明 体彩购彩页
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

}
