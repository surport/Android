package com.ruyicai.activity.more.lotnoalarm;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.palmdream.RuyicaiAndroid168.R;
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
public class AlarmManage {
	private static final String TAG = "AlarmManage";
	// 购彩提醒消息ID
	private static final int NOTIFICATION_ID = 1;

	// 保存购彩设置信息的共享参数名称
	private static final String PREFERENCES_NAME = "AlarmSet";

	static final String HOUR_KEY = "hour";
	static final String MINUTE_KEY = "minute";
	static final String OPENVOICE_KEY = "openvoice";
	static final String SSQ_KEY = "ssq";
	static final String DLT_KEY = "dlt";
	static final String FC3D_KEY = "fc3d";
	static final String QLC_KEY = "qlc";
	static final String QXC_KEY = "qxc";
	static final String PL3_KEY = "pl3";
	static final String PL5_KEY = "pl5";
	static final String TWENTY_FIVE_KEY = "22x5";

	// 购彩提示语中，彩种名称
	static Map<String, String> lotnos;

	private static AlarmManage instance = null;

	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private Context context;

	/**
	 * 获取购彩提醒彩种设置信息
	 * 
	 * @param key
	 *            彩种关键字
	 * @return 彩种设置信息
	 */
	public boolean getLotnoSetting(String key) {
		boolean setting = sharedPreferences.getBoolean(key, false);
		// Log.i(TAG, "reciver" + key + ":" + setting);
		return setting;
	}

	/**
	 * 获取购彩提醒时间设置信息
	 * 
	 * @param key
	 *            时间关键字
	 * @return 提醒时间设置信息
	 */
	public int getAlarmTimeSetting(String key) {
		int time;

		/**
		 * 默认的设置时间是19:00
		 */
		if (key.equals(HOUR_KEY)) {
			time = sharedPreferences.getInt(key, 19);
		} else {
			time = sharedPreferences.getInt(key, 0);
		}
		// Log.i(TAG, "reciver " + key + ":" + time);
		return time;
	}

	/**
	 * 保存购彩提醒彩种信息
	 * 
	 * @param key
	 *            彩种关键字
	 * @param setting
	 *            彩种设置信息
	 */
	public void setLotnoSetting(String key, boolean setting) {
		editor.putBoolean(key, setting);
		editor.commit();
		// Log.i(TAG, "set" + key + ":" + setting);
	}

	/**
	 * 保存购彩提醒时间信息
	 * 
	 * @param key
	 *            时间关键字
	 * @param time
	 *            时间设置信息
	 */
	public void setAlarmTimeSetting(String key, int time) {
		editor.putInt(key, time);
		editor.commit();
		// Log.i(TAG, "set" + key + ":" + time);
	}

	/**
	 * AlarmManage构造方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	private AlarmManage(Context context) {
		this.context = context;

		sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
		editor = sharedPreferences.edit();

		/**
		 * 初始化购彩提醒内容的彩种名称数组
		 */
		lotnos = new HashMap<String, String>();
		lotnos.put(SSQ_KEY, "双色球");
		lotnos.put(DLT_KEY, "大乐透");
		lotnos.put(FC3D_KEY, "福彩3D");
		lotnos.put(QLC_KEY, "七乐彩");
		lotnos.put(QXC_KEY, "七星彩");
		lotnos.put(PL3_KEY, "排列3");
		lotnos.put(PL5_KEY, "排列5");
		lotnos.put(TWENTY_FIVE_KEY, "22选5");
	}

	public static final AlarmManage getInstance(Context context) {
		if (instance == null) {
			instance = new AlarmManage(context);
		}
		return instance;
	}

	/**
	 * 发送购彩提醒信息
	 */
	public void sendBuyLotnoNotification() {
		// Log.i(TAG, "init Alarm");
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
		/**
		 * 初始化消息
		 */
		int icon = R.drawable.icon;
		String alarmTitle = "购彩提醒";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, alarmTitle, when);
		
		// 消息布局
		RemoteViews contentView = new RemoteViews(context.getApplicationContext().getPackageName(),
				R.layout.custom_notification);
		notification.contentView = contentView;
		// 消息图标
		contentView.setImageViewResource(R.id.icon, icon);
		// 消息标题
		contentView.setTextViewText(R.id.title, alarmTitle);
		// 消息内容
		String alarmContent = appendNotifationContent();
		contentView.setTextViewText(R.id.content, alarmContent);

		// 消息预意图
		Intent intent = new Intent(context, HomeActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		notification.contentIntent = pendingIntent;

		// 取消消息标识
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// 消息声音标识
		if (getLotnoSetting(OPENVOICE_KEY)) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}

		// Log.i(TAG, "start notification:" + NOTIFICATION_ID);
		// 发送消息
		notificationManager.notify(NOTIFICATION_ID, notification);
	}

	/**
	 * 拼接购彩提示消息内容
	 * 
	 * @return 购彩提示消息内容
	 */
	private String appendNotifationContent() {
		StringBuffer content = new StringBuffer();

		/**
		 * 遍历各个彩种，如果该彩种的提醒打开，并且当前时间可提醒，则加入到提示消息
		 */
		Iterator iterator = lotnos.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry lotno = (Entry) iterator.next();

			if (isAlarmNow(lotno.getKey().toString())) {
				content.append(lotno.getValue() + " ");
			}
		}

		String result = "今天是" + content.toString() + "的开奖日，不要错过购彩！";

		return result;

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
			if (key.equals(FC3D_KEY) || key.equals(PL3_KEY) || key.equals(PL5_KEY) || key.equals(TWENTY_FIVE_KEY)) {
				return true;
			} else {
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_WEEK);

				switch (day) {
				// 周日提醒双色球和七星彩
				case Calendar.SUNDAY:
					if (key.equals(SSQ_KEY) || key.equals(QXC_KEY)) {
						return true;
					}
					break;
				// 周一提醒大乐透和七乐彩
				case Calendar.MONDAY:
					if (key.equals(DLT_KEY) || key.equals(QLC_KEY)) {
						return true;
					}
					break;
				// 周二提醒双色球和七星彩
				case Calendar.TUESDAY:
					if (key.equals(SSQ_KEY) || key.equals(QXC_KEY)) {
						return true;
					}
					break;
				// 周三提醒大乐透和七乐彩
				case Calendar.WEDNESDAY:
					if (key.equals(DLT_KEY) || key.equals(QLC_KEY)) {
						return true;
					}
					break;
				// 周四提醒双色球
				case Calendar.THURSDAY:
					if (key.equals(SSQ_KEY)) {
						return true;
					}
					break;
				// 周五提醒七星彩和七乐彩
				case Calendar.FRIDAY:
					if (key.equals(QXC_KEY) || key.equals(QLC_KEY)) {
						return true;
					}
					break;
				// 周六提醒大乐透
				case Calendar.SATURDAY:
					if (key.equals(DLT_KEY)) {
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
}
