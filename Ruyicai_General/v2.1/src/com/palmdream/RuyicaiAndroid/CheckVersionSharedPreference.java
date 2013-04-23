package com.palmdream.RuyicaiAndroid;

import android.content.Context;

/**
 * @作者：黄轲
 * @作用：从sharedpreference中读取版本信息的类
 * @日期：2011-03-07
 */
public class CheckVersionSharedPreference extends RWSharedPreferences {
	
	public Context context;
	RWSharedPreferences rwsp;

	/**
	 * @作者：黄轲
	 * @参数: c
	 * @参数: name
	 */
	public CheckVersionSharedPreference(Context c, String name) {
		super(c, name);
		context = c;
		rwsp = new RWSharedPreferences(context, "Version");
	}
	
}
