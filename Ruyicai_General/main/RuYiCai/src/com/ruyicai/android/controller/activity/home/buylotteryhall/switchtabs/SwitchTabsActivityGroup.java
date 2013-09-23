package com.ruyicai.android.controller.activity.home.buylotteryhall.switchtabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.bar.LotteryInformationBar;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

import roboguice.activity.RoboActivityGroup;
import roboguice.inject.InjectView;

/**
 * 选项卡切换页面基类：包含标题栏、彩种开奖信息栏和选项卡切换导航栏，使用选项卡导航栏切换页面；适用于彩种投注页面，彩种投注信息页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-8
 */
public abstract class SwitchTabsActivityGroup extends RoboActivityGroup
		implements TitleBarInterface {
	/** 选项卡导航栏标签的跳转页面类数组 */
	protected Class<?>[]			_fSwithTabClasses;
	/** 选项卡导航栏标签的视图数组 */
	protected View[]				_fSwtichTabViews;
	/** 选项卡导航栏标签标记字符串数组 */
	protected int[]					_fSwitchTabTagIds;

	/** 引用视图：标题栏 */
	@InjectView(R.id.switchtabs_activitygroup_titlebar)
	protected TitleBar				_fTitleBar;
	/** 引用视图：彩种信息栏 */
	@InjectView(R.id.switchtabs_activitygroup_lotteryinfomationbar)
	protected LotteryInformationBar	_fLotteryInformationBar;
	/** 引用视图tabHost导航栏 */
	@InjectView(R.id.switchtabs_activitygroup_tabhost)
	protected TabHost				_fSwitchTabHost;

	/**
	 * 设置软件信息栏显示
	 */
	protected abstract void initLotteryInformationBarShow();

	/**
	 * 设置切换选项卡类集合
	 */
	protected abstract void set_fSwithTabClasses();

	/**
	 * 设置切换选项卡的标识集合
	 */
	protected abstract void set_fSwitchTabTags();

	/**
	 * 设置选项卡切换事件监听器
	 */
	protected abstract void setSwitchTabHostOnTabChangeListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchtabs_activitygroup);

		// 设置标题栏接口
		_fTitleBar.set_fTitleBarInterface(this);
		_fTitleBar.initTitleBarShow();

		// 设置彩种信息栏
		initLotteryInformationBarShow();

		// 初始化切换选项卡的显示
		initSwitchTabsShow();
	}

	/**
	 * 初始化切换选项卡的显示
	 */
	private void initSwitchTabsShow() {
		_fSwitchTabHost.setup(getLocalActivityManager());
		set_fSwitchTabTags();
		set_fSwtichTabViews();
		set_fSwithTabClasses();

		// 添加选项卡
		for (int tab_i = 0; tab_i < _fSwitchTabTagIds.length; tab_i++) {
			TabHost.TabSpec tabSpec = _fSwitchTabHost
					.newTabSpec(getString(_fSwitchTabTagIds[tab_i]))
					.setIndicator(_fSwtichTabViews[tab_i])
					.setContent(new Intent(this, _fSwithTabClasses[tab_i]));
			_fSwitchTabHost.addTab(tabSpec);

			setSwitchTabHostOnTabChangeListener();
		}
	}

	/**
	 * 设置选项卡内容视图
	 */
	private void set_fSwtichTabViews() {
		_fSwtichTabViews = new View[_fSwitchTabTagIds.length];

		for (int view_i = 0; view_i < _fSwitchTabTagIds.length; view_i++) {
			TextView textView = new TextView(this);

			// 设置文本、位置、字体颜色，字体大小和背景图片等属性
			textView.setText(_fSwitchTabTagIds[view_i]);
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(getResources().getColor(R.color.black));
			textView.setTextSize(16.0f);
			textView.setBackgroundResource(R.drawable.tabhost_tab_selector);

			_fSwtichTabViews[view_i] = textView;
		}
	}

}
