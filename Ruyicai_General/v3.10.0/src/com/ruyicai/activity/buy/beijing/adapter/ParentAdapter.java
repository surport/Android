package com.ruyicai.activity.buy.beijing.adapter;

import com.ruyicai.activity.buy.beijing.bean.AgainstInformation;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.constant.Constants;

import android.content.Context;
import android.content.Intent;

import android.widget.BaseAdapter;

public abstract class ParentAdapter extends BaseAdapter {
	/** 上下文对象 */
	public Context context;
	
	
	public ParentAdapter(Context context) {
		super();
		this.context = context;
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
