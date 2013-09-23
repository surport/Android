package com.ruyicai.android.model.bean;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 应用提供了应用的版本号等基本信息
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-26
 */
public class SoftWareInfo {
	private Context				_fContext;

	/** 单例对象 */
	private static SoftWareInfo	fInstance;

	/** 软件渠道号 */
	public static final String	CHANNEL_ID			= "944";
	/** 传输是否压缩标识 */
	public static final String	ISCOMPRESS			= "1";

	/** 软件是否有更新标识 */
	private boolean				hasSoftWareUpdate	= false;
	/** 软件升级提示信息 */
	private String				softWareUpdateMessage;
	/** 软件升级url */
	private String				softWareUpdateUrl;

	public boolean isHasSoftWareUpdate() {
		return hasSoftWareUpdate;
	}

	public void setHasSoftWareUpdate(boolean hasSoftWareUpdate) {
		this.hasSoftWareUpdate = hasSoftWareUpdate;
	}

	public String getSoftWareUpdateMessage() {
		return softWareUpdateMessage;
	}

	public void setSoftWareUpdateMessage(String softWareUpdateMessage) {
		this.softWareUpdateMessage = softWareUpdateMessage;
	}

	public String getSoftWareUpdateUrl() {
		return softWareUpdateUrl;
	}

	public void setSoftWareUpdateUrl(String softWareUpdateUrl) {
		this.softWareUpdateUrl = softWareUpdateUrl;
	}

	private SoftWareInfo(Context aContext) {
		super();
		_fContext = aContext;
	}

	public static SoftWareInfo getInstance(Context aContext) {
		if (fInstance == null) {
			fInstance = new SoftWareInfo(aContext);
		}

		return fInstance;
	}

	/**
	 * 获取应用程序当前的版本号
	 * 
	 * @return 应用程序版本号字符串
	 */
	public String getVersionName() {
		String versionName = null;

		try {
			String packageNameString = _fContext.getPackageName();
			versionName = _fContext.getPackageManager().getPackageInfo(
					packageNameString, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return versionName;
	}
}
