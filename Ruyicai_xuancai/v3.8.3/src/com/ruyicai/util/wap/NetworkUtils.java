package com.ruyicai.util.wap;

import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

public class NetworkUtils {

	// cmwap转换cmnet
	public static boolean cmwapToCmnet(Context context) {

		return new UpdateAPN(context).updateApn();
	}

	// 是否是cmwap网络连接

	public static boolean isCmwap(Context context) {

		if (context == null) {
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		String extraInfo = info.getExtraInfo();
		// Constant.debug("extraInfo ---> " + extraInfo);
		// 工具类，判断是否为空及null
		if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3)) {
			return false;
		}
		if (extraInfo.toLowerCase().indexOf("wap") > 0) {
			return true;
		}
		// return extraInfo.regionMatches(true, extraInfo.length() - 3, "wap",
		// 0, 3);
		return false;
	}

	/**
	 * 是否是cmnet链接网络
	 * 
	 * @return ture是，false否
	 */
	public static boolean isCmnet(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		String extraInfo = info.getExtraInfo();
		if (TextUtils.isEmpty(extraInfo) || (extraInfo.length() < 3)) {
			return false;
		}
		if (extraInfo.toLowerCase().indexOf("net") > 0) {
			return true;
		}
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {
		boolean isOk = false;
		long startTime = System.currentTimeMillis();
		int count = 0;
		while (!NetworkUtils.isCmnet(context)) {
			// cmwap切换到cmnet需要大概4秒的时间，只有切换过去，才结束
			if (count >= 10) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
			count++;
		}
		long endTime = System.currentTimeMillis();
		PublicMethod.myOutLog("wap====",
				("切换结束，切换花费时间为：" + ((endTime - startTime) / 1000.0)
						+ "；切换循环次数（如果大于10可能没有切换成功）：" + count));
		if (NetworkUtils.isCmnet(context)) {
			PublicMethod.myOutLog("wap====", ("切换结束，网络连接方式为cmnet"));
			isOk = true;
		}
		return isOk;
	}
}
