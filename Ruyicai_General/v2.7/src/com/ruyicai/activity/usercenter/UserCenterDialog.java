package com.ruyicai.activity.usercenter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class UserCenterDialog {
	
//	protected static ProgressDialog progressDialog;
	/**
	 *  网络连接框
	 */
	protected static ProgressDialog onCreateDialog(Context context) {
		ProgressDialog progressDialog;
	
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("网络连接中...");
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}


}
