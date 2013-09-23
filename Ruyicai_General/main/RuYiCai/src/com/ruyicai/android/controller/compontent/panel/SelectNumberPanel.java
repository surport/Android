package com.ruyicai.android.controller.compontent.panel;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.compontent.button.RandomSelectNumberButton;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallType;
import com.ruyicai.android.controller.compontent.selectnumberpanel.SelectNumberBallsTableLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 选号面板类：实现了彩票选号号功能，默认标题：文本-红球区；随机按钮：最小随机个数-6个，选择按钮-10个，产生随机数个数-6个；选号小球表：起始编号-1，
 * 小球个数-32个，小球种类-红球，是否显示遗漏值-显示，遗漏值-都为0。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-9
 */
public class SelectNumberPanel extends LinearLayout {
	/** 上下文对象 */
	private Context							_fContext;

	/** 选号面板标题栏相对布局 */
	private RelativeLayout					_fTitleRelativeLayout;
	/** 选号面板标题文本框 */
	private TextView						_fTitleTextView;
	/** 选号面板随机选号按钮 */
	private RandomSelectNumberButton		_fRandomSelectNumberButton;
	/** 选号面板选号小球表格布局 */
	private SelectNumberBallsTableLayout	_fSelectNumberBallsTableLayout;

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 */
	public SelectNumberPanel(Context aContext) {
		super(aContext);
		_fContext = aContext;

		// 设置整体线性布局的方向
		setOrientation(VERTICAL);

		addSelectNumberPanelTitleRelateLayout();
	}

	/**
	 * 构造函数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aAttrs
	 *            属性对象
	 */
	public SelectNumberPanel(Context aContext, AttributeSet aAttrs) {
		super(aContext, aAttrs);
		_fContext = aContext;

		// 设置整体线性布局的方向
		setOrientation(VERTICAL);
		addSelectNumberPanelTitleRelateLayout();
	}

	/**
	 * 添加选号面板标题行的相对布局，用于容纳标题和随机选号按钮
	 */
	private void addSelectNumberPanelTitleRelateLayout() {
		// 添加选号面板标题行的相对布局
		_fTitleRelativeLayout = new RelativeLayout(_fContext);
		addView(_fTitleRelativeLayout);
	}

	/**
	 * 初始化选号面板的标题显示
	 * 
	 * @param aTitleString
	 *            标题字符串
	 */
	public void initSelectNumberPanelTitleShow(String aTitleString) {
		_fTitleTextView = new TextView(_fContext);
		_fTitleTextView.setText(aTitleString);
		_fTitleTextView.setTextColor(getResources().getColor(R.color.black));
		_fTitleTextView.setTextSize(16.0f);
		android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL,
				RelativeLayout.TRUE);
		_fTitleRelativeLayout.addView(_fTitleTextView, layoutParams);
	}

	/**
	 * 初始化选号面板的随机按钮显示
	 * 
	 * @param aRandomButtonMinRandomNum
	 *            最小的随机号码个数
	 * @param aRandomButtonDropDownMenuButtonNum
	 *            选择按钮的个数
	 */
	public void initSelectNumberPanelRandomButtonShow(
			int aRandomButtonMinRandomNum,
			int aRandomButtonDropDownMenuButtonNum) {
		_fRandomSelectNumberButton = new RandomSelectNumberButton(_fContext,
				aRandomButtonMinRandomNum);

		_fRandomSelectNumberButton
				.setDropDownMenuMinRandomNum(aRandomButtonMinRandomNum);
		_fRandomSelectNumberButton
				.setDropDownMenuSelectButtonNum(aRandomButtonDropDownMenuButtonNum);
		android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
				android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		_fTitleRelativeLayout.addView(_fRandomSelectNumberButton, layoutParams);
	}

	/**
	 * 初始化选号面板选号小球的显示
	 * 
	 * @param aSelectNumberBallStartNum
	 *            选号小球起始号码
	 * @param aSelectBallNum
	 *            选号小球的数目
	 * @param aSelectNumberBallType
	 *            选号小球的类型
	 * @param aLossValues
	 *            遗漏值数组
	 * @param aIsShowLossValue
	 *            是否显示遗漏值
	 */
	public void initSelectNumberPanelSelectNumberBallsShow(
			int aSelectNumberBallStartNum, int aSelectBallNum,
			SelectNumberBallType aSelectNumberBallType, int[] aLossValues,
			boolean aIsShowLossValue) {

		// FIXME 在没有输入任何参数的情况下的处理
		_fSelectNumberBallsTableLayout = new SelectNumberBallsTableLayout(
				_fContext, aSelectNumberBallStartNum, aSelectBallNum,
				aSelectNumberBallType, aLossValues, aIsShowLossValue);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = 5;
		addView(_fSelectNumberBallsTableLayout, layoutParams);

		// 设置随机按钮控制的选号小球表格
		_fRandomSelectNumberButton
				.set_fSelectNumberBallsTableLayout(_fSelectNumberBallsTableLayout);
	}

	/**
	 * 初始化选号面板的显示
	 * 
	 * @param aTitleString
	 *            选号面板标题字符串
	 * @param aRandomButtonMinRandomNum
	 *            随机按钮最小的随机号码个数
	 * @param aRandomButtonDropDownMenuButtonNum
	 *            随机按钮下拉菜单按钮的个数
	 * @param aSelectNumberBallStartNum
	 *            选号小球的起始号码
	 * @param aSelectBallNum
	 *            选号小球的个数
	 * @param aSelectNumberBallType
	 *            选号小球的类型
	 * @param aLossValues
	 *            遗漏值数组
	 * @param aIsShowLossValue
	 *            是否显示遗漏值标识
	 */
	public void initSelectNumberPanelShow(String aTitleString,
			int aRandomButtonMinRandomNum,
			int aRandomButtonDropDownMenuButtonNum,
			int aSelectNumberBallStartNum, int aSelectBallNum,
			SelectNumberBallType aSelectNumberBallType, int[] aLossValues,
			boolean aIsShowLossValue) {
		initSelectNumberPanelTitleShow(aTitleString);
		initSelectNumberPanelRandomButtonShow(aRandomButtonMinRandomNum,
				aRandomButtonDropDownMenuButtonNum);
		initSelectNumberPanelSelectNumberBallsShow(aSelectNumberBallStartNum,
				aSelectBallNum, aSelectNumberBallType, aLossValues,
				aIsShowLossValue);
	}

	/**
	 * 设置选号面板的标题文本
	 * 
	 * @param aTitlString
	 *            标题文本
	 */
	public void setPanelTitle(String aTitleString) {
		_fTitleTextView.setText(aTitleString);
	}

	/**
	 * 设置随机选号按钮最小的随机号码个数
	 * 
	 * @param aMinRandomNum
	 *            最小的随机号码个数
	 */
	public void setMinRandomNum(int aMinRandomNum) {
		_fRandomSelectNumberButton.setDropDownMenuMinRandomNum(aMinRandomNum);
	}

	/**
	 * 设置随机下拉菜单中选择随机数个数按钮的个数
	 * 
	 * @param aSelectButtonNum
	 *            下拉菜单选择随机数个数按钮的个数
	 */
	public void setSelectButtonNum(int aSelectButtonNum) {
		_fRandomSelectNumberButton
				.setDropDownMenuSelectButtonNum(aSelectButtonNum);
	}

	/**
	 * 设置选号小球的起始号码
	 */
	public void setSelectBallsStartNum(int aSelectNumberBallStartNum) {
		_fSelectNumberBallsTableLayout
				.set_fSelectNumberBallStartNum(aSelectNumberBallStartNum);
	}

	/**
	 * 设置随机按钮的可见性（在初始化面板之后调用，否则按钮对象为空）
	 * 
	 * @param visibility
	 *            可见性标识
	 */
	public void setRandomButtonVisibiity(int visibility) {
		// FIXME 如何控制在为空的调用的时候的异常处理
		if (_fRandomSelectNumberButton != null) {
			_fRandomSelectNumberButton.setVisibility(visibility);
		}
	}
}
