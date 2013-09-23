package com.ruyicai.activity.buy.zc;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class FootballContantDialog extends Activity {
	private static String alertIssueNOFQueue = "彩票未正式开售，对阵球队未产生,敬请期待!";

	public static void alertIssueNOFQueue(Context mContext) {
		Builder dialog = new AlertDialog.Builder(mContext).setTitle("提示")
				.setMessage(alertIssueNOFQueue)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		dialog.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

}
