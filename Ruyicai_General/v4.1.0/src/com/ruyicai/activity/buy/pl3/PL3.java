/**
 * 
 */
package com.ruyicai.activity.buy.pl3;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

/**
 * 排列三
 * 
 * @author Administrator
 * 
 */
public class PL3 extends BuyActivityGroup {
	private String[] titles = { "直选", "组三", "组六" };
	private String[] topTitles = { "排列三", "排列三", "排列三" };
	public static int iCurrentButton;
	private Class[] allId = { PL3ZhiXuan.class, PL3ZuSan.class, PL3ZuLiu.class };
	
	public AddView addView = new AddView(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_PL3);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_PL3);
		setlastbatchcode(Constants.LOTNO_PL3);
		MobclickAgent.onEvent(this, "pailie3"); // BY贺思明 点击首页的“排列三”图标
		MobclickAgent.onEvent(this, "ticaigoucaijiemian ");// BY贺思明 体彩购彩页
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

}
