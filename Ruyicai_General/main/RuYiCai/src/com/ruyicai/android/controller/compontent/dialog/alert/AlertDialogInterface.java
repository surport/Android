package com.ruyicai.android.controller.compontent.dialog.alert;

/**
 * 警告对话框接口：定义了警告对话框需要实现的方法
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public interface AlertDialogInterface {
	/**
	 * 设置警告对话框图标
	 */
	void setAlertIcon();

	/**
	 * 设置警告对话框标题
	 */
	void setAlertTitle();

	/**
	 * 设置警告对话框内容
	 */
	void setAlertMessage();

	/**
	 * 设置警告对话框”确定“按钮
	 */
	void setAlertPositiveButton();

	/**
	 * 设置警告对话框“取消”按钮
	 */
	void setAlertNegativeButton();

	/**
	 * 设置警告对话框“其它”按钮
	 */
	void setAlertNeutralButton();
}
