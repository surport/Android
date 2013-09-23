package com.ruyicai.android.controller.activity.home.buylotteryhall.activitygroups;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.bar.LotteryInformationBar;
import com.ruyicai.android.controller.compontent.bar.TitleBar;
import com.ruyicai.android.controller.compontent.bar.TitleBarInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import roboguice.activity.RoboActivityGroup;
import roboguice.inject.InjectView;

/**
 * 下拉列表切换页面基类：包含标题栏、彩种开奖信息栏和下拉列表，使用下拉列表切换页面；适用于江西11选5投注页面
 * 
 * @author xiang_000
 * @since RYC1.0 2013-5-1
 */
public abstract class SpinnersActivityGroup extends RoboActivityGroup implements
		TitleBarInterface {
	/** 下拉列表跳转页面类数组 */
	protected Class<?>[]			_fSpinnersClasses;
	/** 下拉列表选项字符串数组 */
	protected int[]					_fSpinnerStringIds;
	/** 下拉列表字符串数组 */
	private String[]				_fSpinnerStrings;

	/** 引用视图：标题栏 */
	@InjectView(R.id.spinners_activitygroup_titlebar)
	protected TitleBar				_fTitleBar;
	/** 引用视图：彩种信息栏 */
	@InjectView(R.id.spinners_activitygroup_lotteryinfomationbar)
	private LotteryInformationBar	_fLotteryInformationBar;
	/** 引用视图：下拉列表 */
	@InjectView(R.id.spinners_activitygroup_spinner)
	protected Spinner				_fSpinner;
	/** 引用视图：Activity容器 */
	@InjectView(R.id.spinners_activitygroup_container)
	private LinearLayout			_fContainerLinearLayout;

	/**
	 * 设置软件信息栏显示
	 */
	protected abstract void initLotteryInformationBarShow();

	/**
	 * 设置下拉菜单选项
	 */
	protected abstract void set_fSpinnerItems();

	/**
	 * 设置下拉菜单切换类数组
	 */
	protected abstract void set_fSpinnerClasses();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spinners_activitygroup);

		// 设置标题栏接口
		_fTitleBar.set_fTitleBarInterface(this);
		_fTitleBar.initTitleBarShow();

		// 初始化彩种信息栏
		initLotteryInformationBarShow();

		// 初始化下拉按钮切换选项卡的显示
		initSpinnersShow();
	}

	/**
	 * 初始化下拉按钮显示
	 */
	private void initSpinnersShow() {
		set_fSpinnerItems();

		// 获取下拉选项字符串资源
		int length = _fSpinnerStringIds.length;
		_fSpinnerStrings = new String[length];
		for (int string_i = 0; string_i < length; string_i++) {
			_fSpinnerStrings[string_i] = getResources().getString(
					_fSpinnerStringIds[string_i]);
		}

		// 设置下拉列表适配器
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, _fSpinnerStrings);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		_fSpinner.setAdapter(spinnerAdapter);

		// 设置下拉列表选项切换页面事件监听
		set_fSpinnerClasses();
		_fSpinner
				.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());
	}

	/**
	 * 下拉列表选项选中事件监听器实现类
	 * 
	 * @author xiang_000
	 * @since RYC1.0 2013-5-1
	 */
	class SpinnerOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			_fContainerLinearLayout.removeAllViews();
			_fContainerLinearLayout.addView(getLocalActivityManager()
					.startActivity(
							_fSpinnerStrings[position],
							new Intent(SpinnersActivityGroup.this,
									_fSpinnersClasses[position])
									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	}
}
