package com.ruyicai.android.model.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 应用程序SharedPreferences类：集中保存应用程序全局的共享参数
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-30
 */
public class AppSharedPreferences {
	/** 如意彩应用共享参数关键字 */
	private static final String	APPLICATION_SHAREDPREFERENCE_KEY	= "com.ruyicai";

	/** 首次启动保存共享参数键 */
	public static final String	FIRST_LAUNCHER_KEY					= "ryc_first_launcer";

	/** SharedPreferences对象 */
	private SharedPreferences	_fSharedPreferences;
	/** Editor对象 */
	private Editor				_fEditor;

	/**
	 * 构造方法
	 * 
	 * @param aContext
	 *            上下文对象
	 */
	public AppSharedPreferences(Context aContext) {
		super();
		_fSharedPreferences = aContext.getSharedPreferences(
				APPLICATION_SHAREDPREFERENCE_KEY, Context.MODE_PRIVATE);
		_fEditor = _fSharedPreferences.edit();
	}

	/**
	 * 根据关键字获取布尔值
	 * 
	 * @param aKey
	 *            关键字
	 * @param aDefalult
	 *            如果无此值的默认值
	 * @return 获取的布尔值
	 */
	public Boolean getBoolean(String aKey, Boolean aDefalult) {
		return _fSharedPreferences.getBoolean(aKey, aDefalult);
	}

	/**
	 * 设置指定关键字布尔值
	 * 
	 * @param aKey
	 *            关键字
	 * @param 设置的布尔值
	 */
	public void putBoolean(String aKey, boolean aBoolean) {
		_fEditor.putBoolean(aKey, aBoolean);
		_fEditor.commit();
	}
}
