package com.ruyicai.android.controller.compontent.dialog;

/**
 * 对话框枚举对象：1.显示对话框的类型
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public enum DialogType {
	/** 软件更新对话框 */
	SOFTWARE_UPDATE_DIALOG(1),
	/** 应用程序退出对话框 */
	APPLICATION_EXIT_DIALOG(2),
	/** 没有互联网连接对话框 */
	NOTCONNECTED_INTENET_DIALOG(3),
	/** 竞彩足球玩法改变对话框 */
	COMPETEFOOTBALL_PLAYMETHODCHANGE_DIALOG(4);

	/** 对话框编号 */
	public int	_fDialogNumber;

	/**
	 * 对话框类型构造方法
	 * 
	 * @param _fDialogNumber
	 *            对话框编号
	 */
	private DialogType(int _fDialogNumber) {
		this._fDialogNumber = _fDialogNumber;
	}
}
