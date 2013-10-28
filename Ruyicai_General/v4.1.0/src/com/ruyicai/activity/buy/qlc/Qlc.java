/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

/**
 * 七乐彩
 * 
 * @author Administrator
 * 
 */
public class Qlc extends BuyActivityGroup {

	private String[] titles = { "自选", "胆拖" };
	private String[] topTitles = { "七乐彩", "七乐彩" };
	private Class[] allId = { QlcZiZhiXuan.class, QlcZiDanTuo.class };
	
	public AddView addView = new AddView(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_QLC);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_QLC);
		setlastbatchcode(Constants.LOTNO_QLC);
		MobclickAgent.onEvent(this, "qilecai"); // BY贺思明 点击首页的“七乐彩”图标
		MobclickAgent.onEvent(this, "fucaigoucaijiemian");// BY贺思明 福彩购彩页面
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

}
