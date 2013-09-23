package com.ruyicai.android.controller.activity.home;

import roboguice.activity.RoboActivityGroup;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.home.accountrecharge.AccountRechargeActivity;
import com.ruyicai.android.controller.activity.home.buylotteryhall.BuyLotteryHallActivity;
import com.ruyicai.android.controller.activity.home.lotterynotice.LotteryNoticeActivity;
import com.ruyicai.android.controller.activity.home.more.MoreActivity;
import com.ruyicai.android.controller.activity.home.usercenter.UserCenterActivity;

/**
 * 应用程序主页面
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-22
 */
public class HomeActivity extends RoboActivityGroup {

	/** 导航栏标签标记数组 */
	private String[]	tabsTagStrings			= { "buylotteryhall",
			"lotterynotice", "accountrecharge", "usercenter", "more" };
	/** 导航栏标签图片资源数组 */
	private int[]		tabsIconImages			= {
			R.drawable.home_tabbar_buylotteryhall,
			R.drawable.home_tabbar_lotterynotice,
			R.drawable.home_tabbar_accountrecharge,
			R.drawable.home_tabbar_usercenter, R.drawable.home_tabbar_more };
	/** 导航栏标签的跳转页面类数组 */
	private Class<?>[]	tabsDestinationClasses	= {
			BuyLotteryHallActivity.class, LotteryNoticeActivity.class,
			AccountRechargeActivity.class, UserCenterActivity.class,
			MoreActivity.class					};

	/** 引用视图：tabHost导航栏 */
	@InjectView(R.id.home_tabhost_navigationbar)
	TabHost				_fNavigationBarTabHost;

	String				_fBuyLotteryHallString;
	String				_fMoreString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_activity);

		initScreenShow();
	}

	/**
	 * 初始化页面的显示
	 */
	private void initScreenShow() {
		initNavigtionBarShow();
	}

	/**
	 * 初始化导航栏的显示
	 */
	private void initNavigtionBarShow() {
		// XXX 能否把工具栏也封装成一个组合控件
		_fNavigationBarTabHost.setup(getLocalActivityManager());

		// 创建导航栏标签
		for (int tab_i = 0; tab_i < tabsTagStrings.length; tab_i++) {
			initNavigationBarTabShow(tab_i);
		}
	}

	/**
	 * 初始化导航栏标签
	 * 
	 * @param aTab_i
	 *            标签索引
	 */
	private void initNavigationBarTabShow(int aTab_i) {
		// 获取导航标签中的视图对象
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View tabView = layoutInflater.inflate(R.layout.home_tabhost_item, null);

		// 设置背景图片选择器
		ImageView iconImageView = (ImageView) tabView
				.findViewById(R.id.home_tabbar_item_icon);
		iconImageView.setBackgroundResource(tabsIconImages[aTab_i]);

		// 设置标识、内容和跳转意图
		TabHost.TabSpec tabSpec = _fNavigationBarTabHost
				.newTabSpec(tabsTagStrings[aTab_i])
				.setIndicator(tabView)
				.setContent(
						new Intent(HomeActivity.this,
								tabsDestinationClasses[aTab_i]));

		// 向tabHost添加tabSpec
		_fNavigationBarTabHost.addTab(tabSpec);
	}
}
