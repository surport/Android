package com.ruyicai.activity.buy.dlt;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;

public class Dlt extends BuyActivityGroup {
	//private String[] titles = { "自选", "胆拖", "12选2" };
	private String[] titles = { "自选", "胆拖"};
	//private String[] topTitles = { "大乐透", "大乐透", "大乐透" };
	private String[] topTitles = { "大乐透", "大乐透"};
	//private Class[] allId = { DltNormalSelect.class, DltDantuoSelect.class,
	//		DltTwoInDozenSelect.class };
	private Class[] allId = { DltNormalSelect.class, DltDantuoSelect.class};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_DLT);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_DLT);
		setlastbatchcode(Constants.LOTNO_DLT);
	}

	public boolean getIsLuck() {
		return true;
	}
}
