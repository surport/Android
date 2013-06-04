/**
 * 
 */
package com.ruyicai.util;

import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 倒计时线程类
 * 
 * @author Administrator
 * 
 */
public class ClockThread {
	public static String[] times = new String[9];
	public static String[] lotnos = { Constants.LOTNO_SSQ,
			Constants.LOTNO_FC3D, Constants.LOTNO_QLC, Constants.LOTNO_PL3,
			Constants.LOTNO_DLT, Constants.LOTNO_JQC, Constants.LOTNO_RX9,
			Constants.LOTNO_SFC, Constants.LOTNO_LCB };

	public ClockThread() {
		initVaule();
	}

	public void initVaule() {
		for (int i = 0; i < times.length; i++) {
			times[i] = getTime(lotnos[i]);
		}
	}

	public void startThread() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);// 休眠1秒
						for (int i = 0; i < times.length; i++) {
							times[i] = Integer.toString(Integer
									.parseInt(times[i]) - 1);

						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static String getVaule(String lotno) {
		String time = "";
		for (int i = 0; i < times.length; i++) {
			if (lotno.equals(lotnos[i])) {
				time = times[i];
			}
		}
		return time;

	}

	/**
	 * 格式化时间
	 */
	public static String formatTime(String times) {
		if (times != null) {
			if (Integer.parseInt(times) < 0) {
				return "0天00时00分00秒";
			} else {
				int time = Integer.parseInt(times);
				int day, hour, minute, second;
				second = time % 60;
				DecimalFormat df=new DecimalFormat("00");
				minute = time / 60;
				if (minute > 60) {
					minute = minute % 60;
				}
				hour = time / 3600;
				if (hour > 24) {
					hour = hour % 24;
				}
				day = time / (3600 * 24);
				return day + "天" + df.format(hour) + "时" + df.format(minute) + "分" + df.format(second) + "秒";
			}
		} else {
			return "0天00时00分00秒";
		}

	}

	/**
	 * 获得当前时间
	 * 
	 * @param type彩种编号
	 */
	public String getTime(String type) {
		String time = "0";
		// 获取期号和截止时间
		JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(type);
		if (ssqLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				time = ssqLotnoInfo.getString("endSecond");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// 没有获取到期号信息,重新联网获取期
		}
		return time;
	}
}
