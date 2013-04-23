package com.ruyicai.dialog;

import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

public class ExitDialogFactory {
	
	public static void createExitDialog( final Activity activity){
		Dialog dialog = new AlertDialog.Builder(activity).setTitle("真的要离开？")
		.setMessage("您确定要离开")// 设置内容
		.setCancelable(false).setPositiveButton(activity.getString(R.string.ok),// 设置确定按钮
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,	int which) {
						RuyicaiActivityManager.getInstance().exit();
						PublicConst.islogin=false;
						Constants.RUYIHELPERSHOWLISTTYPE=0;
						
					}

				}).setNegativeButton(activity.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						dialog.cancel();
					}
				}).create();// 创建
		// 显示对话框
		dialog.show();
	}
}
