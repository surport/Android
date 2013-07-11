package com.ruyicai.activity.more.lotnoalarm;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 购彩提醒广播接受者类：1.接受开机，日期更改，设置提醒广播；2.定时每天启动购彩服务
 * 
 * @author PengCX
 * 
 */
public class LotnoAlarmReciver extends BroadcastReceiver {
	private static final String TAG = "LotnoAlarmReciver";

	private static final String REBOOT_ACTION_FLAG = "android.intent.action.BOOT_COMPLETED";

	private static final String RECIVER_ACTION_FLAG = "com.android.alarmdemo.rebootreciver";

	private static final int MILLISCEONDS_OF_ONEDAY = 24 * 60 * 60 * 1000;

	private AlarmManager alarmManager;
	private LotnoAlarmManager lotnoAlarmManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果获取开机、设置购彩提醒广播，这设置购彩提醒闹钟
		if (intent.getAction().equals(REBOOT_ACTION_FLAG)
				|| intent.getAction().equals(RECIVER_ACTION_FLAG)) {
			startServiceInFiexdTime(context);
		}
	}

	/**
	 * 在设定每天制定时间启动购彩提醒服务
	 */
	public void startServiceInFiexdTime(Context context) {
		// 设置提醒消息点击预意图，启动如意彩客户端
		Intent intentForAlarmService = new Intent(context,
				LotnoAlarmService.class);
		PendingIntent pendingIntentForAlarmService = PendingIntent.getService(
				context, 0, intentForAlarmService, 0);

		// 获取设置购彩提醒日期对象
		Calendar calendar = caculateAlarmTime(context);

		// 设置定时启动购彩提醒服务
		alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), MILLISCEONDS_OF_ONEDAY,
				pendingIntentForAlarmService);
	}

	/**
	 * 计算启动购彩提醒的日期： 如果当前时间大于设置时间，则设置为第二天启动；如果当前时间小于设置时间，则设置为当天启动
	 */
	private Calendar caculateAlarmTime(Context context) {
		// 获取系统当前时间
		long nowTime = System.currentTimeMillis();

		// 获取购彩设置时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		lotnoAlarmManager = LotnoAlarmManager.getInstance(context);
		int setHour = lotnoAlarmManager
				.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_HOUR_KEY);
		int setMinute = lotnoAlarmManager
				.getAlarmTimeSetting(LotnoAlarmManager.PREFERENCE_MINUTE_KEY);

		Date setDate = new Date(year - 1900, month, day, setHour, setMinute);
		calendar.setTime(setDate);
		long setTime = calendar.getTimeInMillis();

		// 判断当前时间和购彩提醒设置时间，如果设置时间小于当前时间，则将启动时间设置为第二天
		if (setTime < nowTime) {
			setTime += 24 * 60 * 60 * 1000;
			calendar.setTimeInMillis(setTime);
		}

		return calendar;
	}
}
