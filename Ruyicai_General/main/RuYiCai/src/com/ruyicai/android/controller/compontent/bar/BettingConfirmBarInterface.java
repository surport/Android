package com.ruyicai.android.controller.compontent.bar;

/**
 * 投注确认栏接口：规定了投注确认栏需要实现的方法
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-19
 */
public interface BettingConfirmBarInterface {
	/**
	 * 设置投注确认按钮事件
	 */
	abstract void setBettingConfirmButton();

	/**
	 * 设置投注取消按钮事件
	 */
	abstract void setBettingCancelButton();
}
