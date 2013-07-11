package com.ruyicai.activity.buy.zc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class FootballLottery extends BuyActivityGroup {

	RelativeLayout fatherIssueLinear;
	RelativeLayout relativeLayout1;
	String lotno = Constants.LOTNO_ZC;
	private String[] titles = { "胜负彩", "任选九", "进球彩", "六场半" };
	private String[] topTitles = { "足彩-胜负彩", "足彩-任选九", "足彩-进球彩", "足彩-六场半" };
	private Class[] allId = { FootballSFLottery.class,
			FootballChooseNineLottery.class, FootballGoalsLottery.class,
			FootballSixSemiFinal.class };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fatherIssueLinear = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		fatherIssueLinear.setVisibility(View.GONE);
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		relativeLayout1.setVisibility(View.GONE);
		setLotno(lotno);
		init(titles, topTitles, allId);
		getInfo();
		MobclickAgent.onEvent(this, "zucai"); // BY贺思明 点击首页的“足彩”图标
	}

	public void getInfo() {
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		setTab(position);
	}

	public boolean isPicture() {
		return false;
	}

}
