package com.ruyicai.android.controller.compontent.bar;

/**
 * 标题栏接口：定义了使用标题栏必须实现的方法
 * 
 * @author xiang_000
 * @since RYC1.0 2013-4-8
 */
public interface TitleBarInterface {
	/**
	 * 设置标题栏左边文本
	 */
	abstract void setTitleTextView();

	/**
	 * 设置标题栏右边按钮
	 */
	abstract void setTitleButton();
}
