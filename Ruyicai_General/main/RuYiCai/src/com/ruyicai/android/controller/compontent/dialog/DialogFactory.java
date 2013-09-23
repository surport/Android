package com.ruyicai.android.controller.compontent.dialog;

import com.ruyicai.android.controller.compontent.dialog.alert.AlertDialogInterface;
import com.ruyicai.android.controller.compontent.dialog.alert.NotConnectedIntenetDialog;
import com.ruyicai.android.controller.compontent.dialog.prompt.ApplicationExitDialog;
import com.ruyicai.android.controller.compontent.dialog.prompt.CompeteFootballPlayMethodChangeDialog;
import com.ruyicai.android.controller.compontent.dialog.prompt.PromptDialog;
import com.ruyicai.android.controller.compontent.dialog.prompt.SoftUpdateDialog;

import android.content.Context;

/**
 * 对话框工厂类：1.根据对话框的类型创建对话框的对象
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public class DialogFactory {
	/** 上下文对象 */
	private Context	_fContext;

	/**
	 * 构造方法
	 * 
	 * @param _fContext
	 *            上下文对象
	 */
	public DialogFactory(Context _fContext) {
		super();
		this._fContext = _fContext;
	}

	/**
	 * 根据对话框的类型装配提示对话框对象
	 * 
	 * @return 提示对话框对象
	 */
	public PromptDialog assemblePromptDialogWithType(DialogType aDialogType) {
		PromptDialog promptDialog = newPromptDialogWithType(aDialogType);

		promptDialog.set_fView();
		promptDialog.set_fTitleString();
		promptDialog.set_fContentView();
		promptDialog.set_fPositiveButton();
		promptDialog.set_fOtherButton();
		promptDialog.set_fNegativeButton();
		promptDialog.create(promptDialog);

		return promptDialog;
	}

	/**
	 * 根据对话框的类型创建提示对话框对象
	 * 
	 * @param aDialogType
	 *            对话框类型
	 * @return 提示对话框对象
	 */
	public PromptDialog newPromptDialogWithType(DialogType aDialogType) {
		PromptDialog promptDialog = null;

		switch (aDialogType) {
		case SOFTWARE_UPDATE_DIALOG:
			promptDialog = new SoftUpdateDialog(_fContext);
			break;

		case APPLICATION_EXIT_DIALOG:
			promptDialog = new ApplicationExitDialog(_fContext);
			break;

		case COMPETEFOOTBALL_PLAYMETHODCHANGE_DIALOG:
			promptDialog = new CompeteFootballPlayMethodChangeDialog(_fContext);
			break;
		}
		return promptDialog;
	}

	/**
	 * 使用对话框类型装配警告对话框
	 * 
	 * @param aDialogType
	 *            对话框类型
	 * @return 警告对话框对象
	 */
	public AlertDialogInterface assembleAlertDialogWithType(
			DialogType aDialogType) {
		AlertDialogInterface alertDialog = newAlertDialogWithType(aDialogType);

		alertDialog.setAlertIcon();
		alertDialog.setAlertTitle();
		alertDialog.setAlertMessage();
		alertDialog.setAlertPositiveButton();
		alertDialog.setAlertNegativeButton();
		alertDialog.setAlertNeutralButton();

		return alertDialog;
	}

	/**
	 * 根据警告对话框类型创建警告对话框对象
	 * 
	 * @param aDialogType
	 *            对话框类型
	 * @return 警告对话框对象
	 */
	private AlertDialogInterface newAlertDialogWithType(DialogType aDialogType) {
		AlertDialogInterface alertDialog = null;

		switch (aDialogType) {
		case NOTCONNECTED_INTENET_DIALOG:
			alertDialog = new NotConnectedIntenetDialog(_fContext);
			break;
		}
		return alertDialog;
	}
}
