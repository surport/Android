package com.ruyicai.activity.buy.dlt;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class Dlt extends BuyActivityGroup {
	//private String[] titles = { "自选", "胆拖", "12选2" };
	private String[] titles = { "自选", "胆拖"};
	//private String[] topTitles = { "大乐透", "大乐透", "大乐透" };
	private String[] topTitles = { "大乐透", "大乐透"};
	//private Class[] allId = { DltNormalSelect.class, DltDantuoSelect.class,
	//		DltTwoInDozenSelect.class };
	private Class[] allId = { DltNormalSelect.class, DltDantuoSelect.class};
	
	public AddViewMiss addView = new AddViewMiss(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_DLT);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_DLT);
		setlastbatchcode(Constants.LOTNO_DLT);
		MobclickAgent.onEvent(this, "daletou"); // BY贺思明 点击首页的“大乐透”图标
		MobclickAgent.onEvent(this, "ticaigoucaijiemian ");// BY贺思明 体彩购彩页
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

	public boolean getIsLuck() {
		return true;
	}
}
