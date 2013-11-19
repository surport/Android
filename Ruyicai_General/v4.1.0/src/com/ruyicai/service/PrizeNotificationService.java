/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ruyicai.service;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.home.HomeActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.NoticeWinInterface;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.FormatZhuma;
import com.ruyicai.util.RWSharedPreferences;

/**
 * This is an example of implementing an application service that will run in
 * response to an alarm, allowing us to move long duration work out of an intent
 * receiver.
 * 
 * @see AlarmService
 * @see AlarmService_Alarm
 */
public class PrizeNotificationService extends Service {
	RWSharedPreferences shellRW;
	int nextNetTime;
	String[] lotteryLable = { "ssq", "ddd", "qlc", "dlt", "qxc", "pl3", "pl5",
			"22-5" };// 从开奖json串中获取响应数据的标签
	int[] showNotificationTYPE = {};
	String[] lotteryName;
	Object[] prizeObject = new Object[2];
	List<Map<String, Object>> listPrize = new ArrayList<Map<String, Object>>();
	int[] batchCodeValue = new int[lotteryLable.length];
	List<Map<String, Object>> listPrizeNew = new ArrayList<Map<String, Object>>();
	int[] batchCodeValueNew = new int[lotteryLable.length];
	Notification notification;

	boolean[] lotteryIShow = new boolean[lotteryLable.length];

	int NETSUCCESS = 1, NETFAIL = 0;

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Object[] aaNew = null;
		try {
			aaNew = netting();
			if (listPrize.size() == 0) {
				batchCodeValue = (int[]) aaNew[0];
				listPrize = (List<Map<String, Object>>) aaNew[1];
			} else {
				batchCodeValueNew = (int[]) aaNew[0];
				listPrizeNew = (List<Map<String, Object>>) aaNew[1];
				comparison();
			}
		} catch (SocketException e) {

			// TODO Auto-generated catch block
		}
		NextStartService();
	}

	private void NextStartService() {
		Intent intent = new Intent();
		intent.setAction("com.ruyicai.PRIZE_ORDER_SERVICE");
		PendingIntent sender = PendingIntent.getService(this, 0, intent, 0);

		// We want the alarm to go off 30 seconds from now.
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.SECOND, nextNetTime);

		// Schedule the alarm!
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
	}

	private void comparison() {
		// TODO Auto-generated method stub
		for (int i = 0; i < batchCodeValue.length; i++) {
			lotteryIShow[i] = (batchCodeValueNew[i] > batchCodeValue[i]);

		}
		// listPrize.size()
		// 循环赋值
		listPrize = listPrizeNew;
		listPrizeNew = new ArrayList<Map<String, Object>>();
		// Collections.copy(listPrize, listPrizeNew);
		batchCodeValue = batchCodeValueNew;

		listPrizeNew.clear();
		batchCodeValueNew = new int[7];

		lotteryShow(lotteryIShow);
	}

	/**
	 * 根据彩种更新列表显示各彩种的通知
	 * 
	 * @param lotteryShowNotifacation2
	 */
	private void lotteryShow(boolean[] lotteryIShow) {
		// TODO Auto-generated method stub
		for (int i = 0; i < lotteryIShow.length; i++) {
			// 当彩种期号已经更新并且用户设置了此彩种更新时，弹出Natification
			if (lotteryIShow[i] == true
					&& shellRW.getBooleanValue(Constants.orderPrize[i]) == true) {
				showNotification(i, lotteryName[i], listPrize.get(i));
			}
		}
	}

	NotificationManager mNM;

	@Override
	public void onCreate() {
		super.onCreate();
		lotteryName = this.getResources().getStringArray(R.array.lotttery_list);
		shellRW = new RWSharedPreferences(this, "addInfo");
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}

	Object[] netting() throws SocketException {
		JSONObject prizeReturn = NoticeWinInterface.getInstance()
				.getLotteryAllNotice();
		List<Map<String, Object>> listPrizeNet = new ArrayList<Map<String, Object>>();
		int[] batchCodeValueNet = new int[lotteryLable.length];
		/*
		 * 联网失败，默认返回{error_code:0,massage:联网异常}这是对联网失败的处理
		 */
		String errorcode;
		try {
			errorcode = prizeReturn.getString("error_code");
			nextNetTime = 600;
		} catch (JSONException e) {
			errorcode = "0000";
		}
		if (errorcode.equals("0000")) {
			try {
				nextNetTime = Integer.valueOf(CheckUtil.isNull(prizeReturn
						.getString("noticeTime")));
			} catch (JSONException e) {
				// TODO: handle exception
				nextNetTime = 600;
			}
			try {
				for (int i = 0; i < lotteryLable.length; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					String singleLotteryJSON = prizeReturn.get(lotteryLable[i])
							+ "";
					JSONObject aa = new JSONObject(singleLotteryJSON);
					String batchCode = aa.getString("batchCode");
					String winCode = aa.getString("winCode");
					map.put("batchCode", batchCode);
					map.put("winCode", winCode);
					listPrizeNet.add(map);
					batchCodeValueNet[i] = Integer.valueOf(CheckUtil.isNull(batchCode));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			prizeObject[0] = batchCodeValueNet;
			prizeObject[1] = listPrizeNet;
		} else {
			prizeObject[0] = batchCodeValue;
			prizeObject[1] = listPrize;
		}
		return prizeObject;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Cancel the notification -- we use the same ID that we had used to
		// start it
		// Tell the user we stopped.
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * 根据彩种和相关数据弹出通知
	 * 
	 * @param lotteryName
	 *            要弹出的彩种名
	 * @param map
	 *            带有开奖号码和开奖期号的Map
	 */
	private void showNotification(int i, String lotteryName,
			Map<String, Object> map) {
		CharSequence text = lotteryName + "第" + map.get("batchCode")
				+ "期开奖号码为：";
		CharSequence text2 = lotteryName + "第" + map.get("batchCode") + "期";
		CharSequence text3 = "开奖号码为："
				+ FormatZhuma.formatZhuma(i, map.get("winCode").toString());
		notification = new Notification(R.drawable.notification, text,
				System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		RemoteViews customNoti = new RemoteViews(getPackageName(),
				R.layout.notification_orderprize);
		customNoti.setTextViewText(R.id.notification_title, text2);
		customNoti.setTextViewText(R.id.informationcontent1, text3);
		notification.contentView = customNoti;
		Intent intent = new Intent();
		// intent.putExtra(Constants.NOTIFICATION_MARKS, "notice");
		intent.setClass(PrizeNotificationService.this, HomeActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		notification.setLatestEventInfo(this, text2, text3, contentIntent);
		mNM.notify(i, notification);
	}

	public class LocalBinder extends Binder {
		PrizeNotificationService getService() {
			return PrizeNotificationService.this;
		}
	}

}
