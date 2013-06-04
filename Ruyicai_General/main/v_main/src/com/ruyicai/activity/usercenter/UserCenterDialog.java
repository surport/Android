package com.ruyicai.activity.usercenter;

import android.app.ProgressDialog;
import android.content.Context;

public class UserCenterDialog {
	/* Add by fansm 20130520 start */
	private static ProgressDialog progressDialog = null;
	/* Add by fansm 20130520 end */
	/**
	 * 网络连接框
	 */
	public static ProgressDialog onCreateDialog(Context context) {
		if (progressDialog == null) {
		    progressDialog = new ProgressDialog(context);
			progressDialog.setMessage("网络连接中...");
			progressDialog.setIndeterminate(true);
		}
		return progressDialog;
	}

	/**
	 * 网络连接框
	 */
	public static ProgressDialog onCreateDialog(Context context, String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(msg);
			progressDialog.setIndeterminate(true);
		}
		return progressDialog;
	}
}
