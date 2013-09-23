package com.ruyicai.android.tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * 资源工具类： 1.从资源目录中获取各种资源，如字符串、图片等
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-24
 */
public class ResourceTools {
	/** 上下文对象 */
	private Resources	_fResources;

	public ResourceTools(Context aContext) {
		super();
		this._fResources = aContext.getResources();
	}

	/**
	 * 从资源库中使用资源的id获取位图对象
	 * 
	 * @return 位图资源id
	 */
	public static Drawable getDrawableFromResourceWithResourceId(int aResourceId) {
		// TODO 实现从Res目录下使用图片资源id获取位图对象
		return null;
	}
}
