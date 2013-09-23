package com.ruyicai.android.controller.compontent.bar;

/**
 * 竞彩投注栏接口：定义了使用投注栏必须实现的方法
 * 
 * @author Administrator
 * @since RYC1.0 2013-8-6
 */
public interface CompeteBetBarInterface {
	/**
	 * 设置重选按钮
	 */
	abstract void setReselectButton();

	/**
	 * 设置选择场次文本
	 */
	abstract void setSelectedNumTextView();

	/**
	 * 设置立即投注按钮
	 */
	abstract void setRecentlyBetButton();
}
