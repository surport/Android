package com.ruyicai.activity.more.lotnoalarm;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 购彩提醒广播接受者类：1.接受开机，日期更改，设置提醒广播；2.定时每天启动购彩服务
 * 
 * @author PengCX
 * 
 */
public class AlarmReciver extends BroadcastReceiver {
	private static final String TAG = "AlarmReciver";

	// 重启广播action
	private static final String REBOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

	// 日期改变广播action
	private static final String DATACHANGET_ACION = "android.intent.action.DATE_CHANGED";

	// 设置提醒广播action
	private static final String RECIVER_ACTION = "com.android.alarmdemo.rebootreciver";

	// 发送消息服务意图action
	private static final String SERVICE_ACTION = "com.android.alarmdemo.alarmservice";
	// 整天的毫秒数
	private static final int ONE_DAY_MILLISECOND = 24 * 60 * 60 * 1000;

	// 启动发送消息服务意图
	private Intent intentForService;
	// 定时预意图
	private PendingIntent pendingIntent;
	private Calendar calendar;
	private AlarmManager alarmManager;
	private AlarmManage alarmManage;

	@Override
	public void onReceive(Context context, Intent intent) {
		/**
		 * 如果获取开机、日期改变、设置购彩提醒广播，这设置购彩提醒闹钟
		 */
		if (intent.getAction().equals(REBOOT_ACTION) || intent.getAction().equals(RECIVER_ACTION)) {
			// Log.i(TAG, "reciver the broadcast of reboot or alarmset...");

			startServiceInFiexdTime(context);
		}
	}

	/**
	 * 在设定每天制定时间启动购彩提醒服务
	 * 
	 * @param context
	 *            上下文对象
	 */
	public void startServiceInFiexdTime(Context context) {
		/**
		 * 设置提醒消息点击预意图，启动如意彩客户端
		 */
		intentForService = new Intent();
		intentForService.setAction(SERVICE_ACTION);
		pendingIntent = PendingIntent.getService(context, 0, intentForService, 0);

		// 获取设置购彩提醒日期对象
		calendar = caculateAlarmTime(context);

		/**
		 * 设置定时启动购彩提醒服务
		 */
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), ONE_DAY_MILLISECOND,
				pendingIntent);
		// Log.i(TAG, "set pending intent to start service at:" +
		// calendar.getTime().toString());
	}

	/**
	 * 计算启动购彩提醒的日期： 如果当前时间大于设置时间，则设置为第二天启动；如果当前时间小于设置时间，则设置为当天启动
	 * 
	 * @param context
	 *            上下文对象
	 * @return 相关日期对象
	 */
	private Calendar caculateAlarmTime(Context context) {
		Log.i(TAG, "caculateAlarmTime");
		// 获取系统当前时间
		long nowTime = System.currentTimeMillis();
		// Log.i(TAG, "nowTime:" + nowTime);

		// 获取购彩设置时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		alarmManage = AlarmManage.getInstance(context);
		int setHour = alarmManage.getAlarmTimeSetting(AlarmManage.HOUR_KEY);
		int setMinute = alarmManage.getAlarmTimeSetting(AlarmManage.MINUTE_KEY);

		Date setDate = new Date(year - 1900, month, day, setHour, setMinute);
		calendar.setTime(setDate);
		long setTime = calendar.getTimeInMillis();
		// Log.i(TAG, "setTime first:" + setTime);

		/**
		 * 判断当前时间和购彩提醒设置时间，如果设置时间小于当前时间，则将启动时间设置为第二天
		 */
		if (setTime < nowTime) {
			setTime += 24 * 60 * 60 * 1000;
			calendar.setTimeInMillis(setTime);
			// Log.i(TAG, "setTime reset:" + setTime);
		}

		return calendar;
	}
}
