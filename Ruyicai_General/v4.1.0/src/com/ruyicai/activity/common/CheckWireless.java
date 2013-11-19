/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.ruyicai.activity.common;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class CheckWireless extends Activity {
	Context context;
	private boolean connectGPRS = false;
	private boolean connectWIFI = false;
	private boolean connectMobNetInfo = false;

	/* 判断连接网络状态的接口函数 */
	public boolean getConnectGPRS() {
		return connectGPRS;
	}

	public boolean getConnectWIFI() {
		return connectWIFI;
	}

	public boolean connectMobNetInfo() {
		return connectMobNetInfo;
	}

	/* 调用到网络设置列表的接口函数 */
	public void showNoConnectionDialog() {
		context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
	}

	/* 构造函数 */
	public CheckWireless(Context aContext) {
		context = aContext;
		checkWirelessStatus();
	}

	/* 获取无线网络的链接状态 */
	private void checkWirelessStatus() {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo activeNetInfo = connectivityManager
					.getActiveNetworkInfo();
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			try {
				if (activeNetInfo != null) {
					if (connectivityManager.getNetworkInfo(
							ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
						connectWIFI = true;
					}
					if (connectivityManager.getNetworkInfo(
							ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
						connectGPRS = true;
					}
				}
				if (mobNetInfo != null) {
					connectMobNetInfo = true;

				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
