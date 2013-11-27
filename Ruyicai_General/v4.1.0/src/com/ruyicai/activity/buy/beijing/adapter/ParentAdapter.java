package com.ruyicai.activity.buy.beijing.adapter;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.bean.AgainstInformation;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.constant.Constants;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import android.widget.BaseAdapter;

public abstract class ParentAdapter extends BaseAdapter {
	/** 上下文对象 */
	public Context context;
	/**add by yejc 20130822 start */
	public Resources  resources;
	public int white = 0;
	public int black = 0;
	public int red = 0;
	public int green = 0;
	public int oddsColor = 0;
	/**add by yejc 20130822 start */
	
	public ParentAdapter(Context context) {
		super();
		this.context = context;
		/**add by yejc 20130822 start */
		resources = context.getResources();
		white = resources.getColor(R.color.white);
		black = resources.getColor(R.color.black);
		red = resources.getColor(R.color.red);
		green = resources.getColor(R.color.gree_black);
		oddsColor = resources.getColor(R.color.jc_odds_text_color);
		/**add by yejc 20130822 start */
	}

	/**
	 * 跳转到分析界面
	 */
	public void trunExplain(String event) {
		Intent intent = new Intent(context, JcExplainActivity.class);
		intent.putExtra("event", event);
		Constants.currentTickType = Constants.BEIJINGSINGLE;
		context.startActivity(intent);
	}
	
	/**
	 * 获得event
	 * @param info
	 * @return
	 */
	public String getEvent(AgainstInformation info) {
		return Constants.currentLoto + "_" + info.getTeamId();
	}

}
