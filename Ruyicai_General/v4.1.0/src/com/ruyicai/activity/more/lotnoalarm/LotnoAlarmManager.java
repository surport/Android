package com.ruyicai.activity.more.lotnoalarm;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.HomeActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * 购彩提醒管理类：1.处理购彩相关设置的保存和获取；2.购彩消息的发送
 * 
 * @author PengCX
 * 
 */
public class LotnoAlarmManager {
	private static final String TAG = "LotnoAlarmManager";

	private static final int LNTNOALARM_NOTIFICATION_ID = 1;

	private static final String LONTOALARM_PREFERENCES_NAME = "AlarmSet";

	static final String PREFERENCE_HOUR_KEY = "hour";
	static final String PREFERENCE_MINUTE_KEY = "minute";
	static final String PREFERENCE_OPENVOICE_KEY = "openvoice";
	static final String PREFERENCE_SSQ_KEY = "ssq";
	static final String PREFERENCE_DLT_KEY = "dlt";
	static final String PREFERENCE_FC3D_KEY = "fc3d";
	static final String PREFERENCE_QLC_KEY = "qlc";
	static final String PREFERENCE_QXC_KEY = "qxc";
	static final String PREFERENCE_PL3_KEY = "pl3";
	static final String PREFERENCE_PL5_KEY = "pl5";
	static final String PREFERENCE_TWENTYFIVE_KEY = "22x5";

	static Map<String, String> lotnosNameMap;

	private static LotnoAlarmManager instance = null;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Context context;

	public boolean getLotnoSetting(String key) {
		boolean setting = sharedPreferences.getBoolean(key, false);
		return setting;
	}

	public int getAlarmTimeSetting(String key) {
		int time;

		// 默认的设置时间是19:00
		if (key.equals(PREFERENCE_HOUR_KEY)) {
			time = sharedPreferences.getInt(key, 19);
		} else {
			time = sharedPreferences.getInt(key, 0);
		}
		return time;
	}

	public void setLotnoSetting(String key, boolean setting) {
		editor.putBoolean(key, setting);
		editor.commit();
	}

	public void setAlarmTimeSetting(String key, int time) {
		editor.putInt(key, time);
		editor.commit();
	}

	private LotnoAlarmManager(Context context) {
		this.context = context;

		sharedPreferences = context.getSharedPreferences(
				LONTOALARM_PREFERENCES_NAME, 0);
		editor = sharedPreferences.edit();

		lotnosNameMap = new HashMap<String, String>();
		lotnosNameMap.put(PREFERENCE_SSQ_KEY, "双色球");
		lotnosNameMap.put(PREFERENCE_DLT_KEY, "大乐透");
		lotnosNameMap.put(PREFERENCE_FC3D_KEY, "福彩3D");
		lotnosNameMap.put(PREFERENCE_QLC_KEY, "七乐彩");
		lotnosNameMap.put(PREFERENCE_QXC_KEY, "七星彩");
		lotnosNameMap.put(PREFERENCE_PL3_KEY, "排列3");
		lotnosNameMap.put(PREFERENCE_PL5_KEY, "排列5");
		lotnosNameMap.put(PREFERENCE_TWENTYFIVE_KEY, "22选5");
	}

	public static final LotnoAlarmManager getInstance(Context context) {
		if (instance == null) {
			instance = new LotnoAlarmManager(context);
		}
		return instance;
	}

	public void sendBuyLotnoNotification() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// 初始化消息内容
		int notificationIcon = R.drawable.icon;
		String notificationTitle = "购彩提醒";
		long notificationTime = System.currentTimeMillis();
		Notification notification = new Notification(notificationIcon,
				notificationTitle, notificationTime);

		// 初始化消息视图
		RemoteViews notificationView = new RemoteViews(context
				.getApplicationContext().getPackageName(),
				R.layout.custom_notification);
		notification.contentView = notificationView;
		notificationView.setImageViewResource(R.id.icon, notificationIcon);
		notificationView.setTextViewText(R.id.title, notificationTitle);
		String notificationContent = appendNotifationContent();
		notificationView.setTextViewText(R.id.content, notificationContent);

		// 初始化点击意图
		Intent launchIntent = new Intent(context, HomeActivity.class);
		PendingIntent pendingIntentForHomeActivity = PendingIntent.getActivity(
				context, 0, launchIntent, 0);
		notification.contentIntent = pendingIntentForHomeActivity;

		// 取消消息设置和声音设置
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		if (getLotnoSetting(PREFERENCE_OPENVOICE_KEY)) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}

		// 发送消息
		notificationManager.notify(LNTNOALARM_NOTIFICATION_ID, notification);
	}

	/**
	 * 拼接购彩提示消息内容
	 * 
	 * @return 购彩提示消息内容
	 */
	private String appendNotifationContent() {
		StringBuffer lotnoContent = new StringBuffer();

		// 遍历各个彩种，如果该彩种的提醒打开，并且当前时间可提醒，则加入到提示消息
		Iterator iterator = lotnosNameMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry lotno = (Entry) iterator.next();

			if (isAlarmNow(lotno.getKey().toString())) {
				lotnoContent.append(lotno.getValue() + " ");
			}
		}

		String notificationContent = "今天是" + lotnoContent.toString()
				+ "的开奖日，不要错过购彩！";

		return notificationContent;

	}

	/**
	 * 当前是否是可提醒彩种
	 * 
	 * @param key
	 *            彩种关键字
	 * @return 是否是可提醒彩种标识符
	 */
	public boolean isAlarmNow(String key) {
		boolean isSetting = getLotnoSetting(key);

		if (isSetting) {
			// 福彩3D、排列3、排列5、22选5每天提醒
			if (key.equals(PREFERENCE_FC3D_KEY)
					|| key.equals(PREFERENCE_PL3_KEY)
					|| key.equals(PREFERENCE_PL5_KEY)
					|| key.equals(PREFERENCE_TWENTYFIVE_KEY)) {
				return true;
			} else {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_WEEK);

				switch (day) {
				// 周日提醒双色球和七星彩
				case Calendar.SUNDAY:
					if (key.equals(PREFERENCE_SSQ_KEY)
							|| key.equals(PREFERENCE_QXC_KEY)) {
						return true;
					}
					break;
				// 周一提醒大乐透和七乐彩
				case Calendar.MONDAY:
					if (key.equals(PREFERENCE_DLT_KEY)
							|| key.equals(PREFERENCE_QLC_KEY)) {
						return true;
					}
					break;
				// 周二提醒双色球和七星彩
				case Calendar.TUESDAY:
					if (key.equals(PREFERENCE_SSQ_KEY)
							|| key.equals(PREFERENCE_QXC_KEY)) {
						return true;
					}
					break;
				// 周三提醒大乐透和七乐彩
				case Calendar.WEDNESDAY:
					if (key.equals(PREFERENCE_DLT_KEY)
							|| key.equals(PREFERENCE_QLC_KEY)) {
						return true;
					}
					break;
				// 周四提醒双色球
				case Calendar.THURSDAY:
					if (key.equals(PREFERENCE_SSQ_KEY)) {
						return true;
					}
					break;
				// 周五提醒七星彩和七乐彩
				case Calendar.FRIDAY:
					if (key.equals(PREFERENCE_QXC_KEY)
							|| key.equals(PREFERENCE_QLC_KEY)) {
						return true;
					}
					break;
				// 周六提醒大乐透
				case Calendar.SATURDAY:
					if (key.equals(PREFERENCE_DLT_KEY)) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
}
