package com.ruyicai.dialog;

import android.app.Activity;
import android.util.Log;
import android.view.View;

public class MessageDialog extends BaseDialog{
	/**
	 * 首页活动提示框
	 * @param activity
	 * @param title
	 * @param message
	 */
	public MessageDialog(Activity activity, String title, String message) {
		super(activity, title, message);
	}

	@Override
	public void onOkButton() {
		// TODO Auto-generated method stub
		dialog.cancel();
	}

	@Override
	public void onCancelButton() {
		// TODO Auto-generated method stub
		dialog.cancel();
	}

}
