package com.ruyicai.android.controller.compontent.bar;

/**
 * 投注栏接口：定义了使用投注栏必须实现的方法
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-18
 */
public interface BetBarInterface {
	/**
	 * 设置号码篮按钮
	 */
	abstract void setNumberBasketButton();

	/**
	 * 设置清除选中号码按钮
	 */
	abstract void setClearSelectedNumberButton();

	/**
	 * 设置加入号码蓝按钮
	 */
	abstract void setAddToNumberBasketButton();

	/**
	 * 设置投注按钮
	 */
	abstract void setBettingButton();
}
