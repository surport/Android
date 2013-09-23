package com.ruyicai.android.model.bean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * 该类提供了获取如屏幕高度和宽度等相关手机信息的方法，如： 1.获取手机显示屏幕的高度； 2.获取手机显示屏幕的宽度； 3.检测手机是否有有效网络连接等等
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-22
 */
public class PhoneInfo {
	/** 连接管理对象 */
	private ConnectivityManager	_fConnectivityManager;
	/** 手机管理对象 */
	private TelephonyManager	_fTelephonyManager;
	/** WIFI管理对象 */
	private WifiManager			_fWifiManager;

	/** 单例对象 */
	private static PhoneInfo	fInstance;

	private PhoneInfo(Context aContext) {
		super();
		_fConnectivityManager = (ConnectivityManager) aContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		_fTelephonyManager = (TelephonyManager) aContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		_fWifiManager = (WifiManager) aContext
				.getSystemService(Context.WIFI_SERVICE);
	}

	public static PhoneInfo getInstance(Context aContext) {
		if (fInstance == null) {
			fInstance = new PhoneInfo(aContext);
		}

		return fInstance;
	}

	/**
	 * 获取用户识别码
	 * 
	 * @return 用户识别码
	 */
	public String getImsi() {
		// XXX 当手机没有插入SIM卡的时候返回空，应处理该异常
		return _fTelephonyManager.getSubscriberId();
	}

	/**
	 * 获取手机身份码
	 * 
	 * @return 手机身份码
	 */
	public String getImei() {
		return _fTelephonyManager.getDeviceId();
	}

	/**
	 * 获取手机的型号
	 * 
	 * @return 手机的型号
	 */
	public String getMachineid() {
		// 如果是模拟器，返回sdk
		return Build.MODEL;
	}

	/**
	 * 获取手机SIM卡的手机号
	 * 
	 * @return 手机SIM卡的手机号
	 */
	public String getPhoneSIM() {
		// FIXME 无法获取SIM卡中的手机号码
		return _fTelephonyManager.getLine1Number();
	}

	/**
	 * 获取手机平台信息
	 * 
	 * @return 手机平台信息
	 */
	public String getPlatform() {
		return "android";
	}

	/**
	 * 获取手机联网的mac地址
	 * 
	 * @return mac地址
	 */
	public String getMac() {
		// 如果是模拟器，Mac为null
		WifiInfo wifiInfo = _fWifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	/**
	 * 检测手机手否用有效的网络连接
	 * 
	 * @param context
	 *            上下文对象
	 * @return 网络是否连接标识
	 */
	public boolean isConnectedIntenet() {

		NetworkInfo networkInfo = _fConnectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测当前手机是否是模拟器
	 * 
	 * @return 是否是模拟器标识
	 */
	public boolean isSimulater() {
		String imeiString = getImei();

		// 如果是模拟器IMEI号码为“000000000000000”
		if (imeiString.equalsIgnoreCase("000000000000000")) {
			return true;
		} else {
			return false;
		}
	}
}
