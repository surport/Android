package com.ruyicai.activity.usercenter;

import android.app.ProgressDialog;
import android.content.Context;

public class UserCenterDialog {
	
//	protected static ProgressDialog progressDialog;
	/**
	 *  网络连接框
	 */
	protected static ProgressDialog onCreateDialog(Context context) {
	//	Log.v("11111111111","22222222222222");
		ProgressDialog progressDialog;
	
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage("网络连接中...");
		progressDialog.setIndeterminate(true);
		return progressDialog;
	}


}
