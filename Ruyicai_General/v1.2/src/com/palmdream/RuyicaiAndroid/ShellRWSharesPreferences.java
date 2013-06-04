package com.palmdream.RuyicaiAndroid;

import android.content.Context;

//这个类的作用是把RWSharedPreferences类实例化，并把用户信息进行保存,有利于重复使用这个类。
class ShellRWSharesPreferences extends RWSharedPreferences {
	Context context;
	boolean isSame;
	RWSharedPreferences sp; // 定义一个RWSharedPreferences的对象

	/**
	 * 初始化信息
	 * 
	 * @param c
	 * @param name
	 *            名称
	 */
	public ShellRWSharesPreferences(Context c, String name) {
		super(c, name);
		// TODO Auto-generated constructor stub
		context = c;
		sp = new RWSharedPreferences(context, "LotteryUserInfo");
	}

	// 得到用户需要保存的信息valueName
	/**
	 * 把value的值与key关联起来
	 * 
	 * @param aKeyName
	 *            键
	 * @param aKeyValue
	 *            值
	 */
	public void setUserLoginInfo(String aKeyName, String aKeyValue) {
		// Log.d("tag","ok");
		// 判断valueName的值是否和“username”相同，若相同则返回
		if (aKeyName.equals(null) || aKeyValue.equals(null)) {
		} else {
			sp.putPreferencesValue(aKeyName, aKeyValue);// 把要保存的用户信息和相应的key对应起来

			// Log.d("tag","ok");
		}

	}

	// 通过得到key，就可以找到相应的value值，然后返回value的值
	/**
	 * 通过key可以得到对应的value值，返回value的值
	 * 
	 * @param aKeyName
	 *            键
	 * @return 返回key对应的值
	 */
	public String getUserLoginInfo(String aKeyName) {
		if (aKeyName.equals(null)) {
			return null;
		} else {
			return sp.getPreferencesValue(aKeyName);
		}
		// Log.d("tag","ok");
	}

	// 通过yn来判断是否联网
	/**
	 * 判断是否联网
	 * 
	 * @param yn
	 */
	public String connectNetYOrN(boolean yn) {
		String changBooleanToString = new String();
		if (yn) {
			return changBooleanToString.valueOf(yn);
		}
		return "";
	}
}
