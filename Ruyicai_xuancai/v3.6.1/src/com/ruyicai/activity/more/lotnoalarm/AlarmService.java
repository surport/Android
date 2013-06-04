package com.ruyicai.activity.more.lotnoalarm;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 发送购彩提醒消费服务类：1.发送购彩提醒消息
 * 
 * @author PengCX
 * 
 */
public class AlarmService extends Service {
	private static final String TAG = "AlarmService";

	private AlarmManage alarmManage;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// Log.i(TAG, "AlarmService start at:" +
		// Calendar.getInstance().getTime().toGMTString());
		super.onCreate();
		AlarmManage alarmManage = AlarmManage.getInstance(this);
		// 当前是否打开购彩提醒，并且有彩种需要提醒
		if (isOpenAlarmAndHasAlarm(this)) {
			alarmManage.sendBuyLotnoNotification();
		}
		// 执行完发送购彩提醒消息任务，停止该服务
		stopSelf();
	}

	/**
	 * 是否打开闹钟，并且有提醒彩种
	 * 
	 * @param context
	 *            上下文对象
	 * @return 布尔标识
	 */
	private boolean isOpenAlarmAndHasAlarm(Context context) {
		// 是否打开并有提醒
		boolean isOpenAndAlarm = false;

		alarmManage = AlarmManage.getInstance(context);

		Iterator iterator = AlarmManage.lotnos.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry lotno = (Entry) iterator.next();
			String key = lotno.getKey().toString();

			// 如果当前是提醒彩种，则在购彩提醒内容中追加彩种名称
			if (alarmManage.isAlarmNow(key)) {
				isOpenAndAlarm = true;
			}
		}

		// Log.i(TAG, "alarm setting is open:" + isOpenAndAlarm);
		return isOpenAndAlarm;
	}
}
