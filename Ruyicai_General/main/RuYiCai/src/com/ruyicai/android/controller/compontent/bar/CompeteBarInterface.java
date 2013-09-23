package com.ruyicai.android.controller.compontent.bar;

/**
 * 竞彩栏接口：规定了使用竞彩栏必须实现的方法，设置玩法选择按钮、设置赛事选择按钮和设置比赛按钮。
 * 
 * @author xiang_000
 * @since RYC1.0 2013-5-4
 */
public interface CompeteBarInterface {
	/**
	 * 设置玩法切换按钮
	 */
	abstract void setPlayMethodChangeButton();

	/**
	 * 设置赛事选择按钮
	 */
	abstract void setEventSelectButton();

	/**
	 * 设置即时比分按钮
	 */
	abstract void setRealTimeScoreButton();
}
