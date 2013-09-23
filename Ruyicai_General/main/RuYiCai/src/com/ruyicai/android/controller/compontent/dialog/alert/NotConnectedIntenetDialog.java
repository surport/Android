package com.ruyicai.android.controller.compontent.dialog.alert;

import com.ruyicai.android.R;
import com.ruyicai.android.controller.activity.splash.SplashActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

/**
 * 没有连接互联网警告对话框
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public class NotConnectedIntenetDialog extends AlertDialog implements
		AlertDialogInterface {
	private Context		_fContext;
	private Resources	_fResources;

	public NotConnectedIntenetDialog(Context aContext) {
		super(aContext);
		_fContext = aContext;
		_fResources = aContext.getResources();
	}

	public void setAlertIcon() {
		setIcon(android.R.drawable.ic_dialog_alert);
	}

	@Override
	public void setAlertTitle() {
		setTitle(R.string.nointenetdialog_title_warm);
	}

	@Override
	public void setAlertMessage() {
		setMessage(_fResources
				.getString(R.string.nointenetdialog_message_intenetnotconnted));
	}

	@Override
	public void setAlertPositiveButton() {
		setButton(BUTTON_POSITIVE,
				_fResources.getString(R.string.nointenetdialog_button_set),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						goToNetSetScreenOfSystem();
					}
				});
	}

	/**
	 * 跳转系统的网络设置界面
	 */
	private void goToNetSetScreenOfSystem() {
		Intent intent = new Intent();
		ComponentName componentName = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(componentName);
		intent.setAction("android.intent.action.View");

		_fContext.startActivity(intent);
	}

	@Override
	public void setAlertNegativeButton() {
		setButton(BUTTON_NEGATIVE,
				_fResources.getString(R.string.nointenetdialog_button_cancle),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.exit(0);
					}
				});
	}

	@Override
	public void setAlertNeutralButton() {
		setButton(BUTTON_NEUTRAL,
				_fResources.getString(R.string.nointenetdialog_button_enter),
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((SplashActivity) _fContext).goToNextScreen();
					}
				});
	}

}
