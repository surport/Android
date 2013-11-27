package com.ruyicai.activity.buy.pl5;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

/**
 * 排列五
 * 
 * @author miao
 * 
 */
public class PL5 extends BuyActivityGroup {
	private String[] titles = { "自选", "机选" };
	private String[] topTitles = { "排列五", "排列五" };
	private Class[] allId = { PL5ZiZhiXuan.class, PL5ZiJX.class };
	
	public AddView addView = new AddView(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_PL5);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_PL5);
		setlastbatchcode(Constants.LOTNO_PL5);
		MobclickAgent.onEvent(this, "pailie5"); // BY贺思明 点击首页的“排列五”图标
		MobclickAgent.onEvent(this, "ticaigoucaijiemian ");// BY贺思明 体彩购彩页
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}
}
