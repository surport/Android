/**
 * 
 */
package com.ruyicai.activity.buy.fc3d;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

/**
 * 福彩3d
 * 
 * @author Administrator
 * 
 */
public class Fc3d extends BuyActivityGroup {
	public static int iCurrentButton;// 标签
	private String[] titles = { "直选", "组三", "组六" };
	private String[] topTitles = { "福彩3D", "福彩3D", "福彩3D" };
	private Class[] allId = { FC3DZhiXuan.class, FC3DZuSan.class,
			FC3DZuLiu.class };

	public AddViewMiss addView = new AddViewMiss(this);
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_FC3D);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_FC3D);
		setlastbatchcode(Constants.LOTNO_FC3D);
		MobclickAgent.onEvent(this, "fucai3d"); // BY贺思明 点击首页的“福彩3D”图标
		MobclickAgent.onEvent(this, "fucaigoucaijiemian");// BY贺思明 福彩购彩页面
	}
	
	private void updateAddMissViewNum() {
		addView.updateTextNum();
	}

	public boolean getIsLuck() {
		return true;
	}
}
