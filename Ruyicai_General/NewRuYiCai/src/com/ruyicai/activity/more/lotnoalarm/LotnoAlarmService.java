package com.ruyicai.activity.more.lotnoalarm;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * 发送购彩提醒消费服务类：1.接受购彩提醒定时意图，发送购彩提醒消息
 * 
 * @author PengCX
 * 
 */
public class LotnoAlarmService extends Service {
	private static final String TAG = "LotnoAlarmService";

	private LotnoAlarmManager lotnoAlarmManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lotnoAlarmManager = LotnoAlarmManager.getInstance(this);

		// 当前是否打开购彩提醒，并且有彩种需要提醒
		if (isOpenAlarmAndHasAlarm(this)) {
			lotnoAlarmManager.sendBuyLotnoNotification();
		}
		// 执行完发送购彩提醒消息任务，停止该服务
		stopSelf();
	}

	/**
	 * 是否打开闹钟，并且有提醒彩种
	 */
	private boolean isOpenAlarmAndHasAlarm(Context context) {
		// 是否打开并有提醒
		boolean isOpenAndAlarm = false;

		Iterator iterator = LotnoAlarmManager.lotnosNameMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry lotno = (Entry) iterator.next();
			String key = lotno.getKey().toString();

			// 如果当前是提醒彩种，则在购彩提醒内容中追加彩种名称
			if (lotnoAlarmManager.isAlarmNow(key)) {
				isOpenAndAlarm = true;
			}
		}

		return isOpenAndAlarm;
	}
}
