package com.ruyicai.android.test.model.intenet;

import com.ruyicai.android.model.bean.PhoneInfo;

import android.test.AndroidTestCase;
import android.util.Log;

public class PhoneInfoTest extends AndroidTestCase {
	private static final String	TAG	= "PhoneInfoTest";

	public void testgetPhoneInfo() throws Exception {
		PhoneInfo phoneInfo = PhoneInfo.getInstance(getContext());
		Log.i(TAG, "Imsi:" + phoneInfo.getImsi());
		Log.i(TAG, "Imei:" + phoneInfo.getImei());
		Log.i(TAG, "Machineid:" + phoneInfo.getMachineid());
		Log.i(TAG, "PhoneSIM:" + phoneInfo.getPhoneSIM());
		Log.i(TAG, "Platform:" + phoneInfo.getPlatform());
		Log.i(TAG, "Mac:" + phoneInfo.getMac());
		Log.i(TAG, "isConnectedIntenet:" + phoneInfo.isConnectedIntenet());
		Log.i(TAG, "isSimulator:" + phoneInfo.isSimulater());
	}

}
