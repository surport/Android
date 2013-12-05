package com.ruyicai.receiver;

import com.ruyicai.activity.usercenter.FeedbackListActivity;
import com.ruyicai.databases.OperatingDataBases;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class SystemPushInfoReceiver extends BroadcastReceiver {
	private static final String TAG = "SystemPushInfoReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "接收到推送下来的通知");
			String pushInfo = bundle.getString(JPushInterface.EXTRA_ALERT);
			OperatingDataBases operatingDB = new OperatingDataBases(context);
			operatingDB.insert(pushInfo);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "用户点击打开了通知");
			// 打开自定义的Activity
			Intent i = new Intent(context, FeedbackListActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("isSystemInfo", true);
			context.startActivity(i);
		}
	}

}
