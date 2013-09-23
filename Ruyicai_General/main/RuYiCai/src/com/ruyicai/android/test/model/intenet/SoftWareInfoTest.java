package com.ruyicai.android.test.model.intenet;

import com.ruyicai.android.model.bean.SoftWareInfo;

import android.test.AndroidTestCase;
import android.util.Log;

public class SoftWareInfoTest extends AndroidTestCase {
	private static final String	TAG	= "SoftWareInfoTest";

	public void testgetSoftWareInfo() throws Exception {
		SoftWareInfo softWareInfo = SoftWareInfo.getInstance(getContext());
		Log.i(TAG, "VersionName:" + softWareInfo.getVersionName());
	}
}
