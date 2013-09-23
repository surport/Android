package com.ruyicai.android.tools;

import android.util.Log;

/**
 * 日志工具类：可以通过开关控制项目中各种日志的输出
 * 
 * @author xiang_000
 * @since RYC1.0 2013-3-28
 */
public class LogTools {
	/** 错误输出标识 */
	public static String	TAG				= "ruyicai";
	/** 错误日志 */
	public static final int	ERROR			= 0;
	/** 警告调试日志 */
	public static final int	WARN			= 1;
	/** 信息日志 */
	public static final int	INFO			= 2;
	/** 调试日志 */
	public static final int	DEBUG			= 3;
	/** 详细日志 */
	public static final int	VERBOSE			= 4;

	/** 是否关闭日志信息 */
	public static boolean	isLogOff		= true;
	/** 是否关闭错误日志 */
	public static boolean	isErrorLogOff	= true;
	/** 是否关闭警告调制日志 */
	public static boolean	isWarnLogOff	= true;
	/** 是否关闭信息日志 */
	public static boolean	isInfoLogOff	= true;
	/** 是否关闭调试日志 */
	public static boolean	isDebugLogOff	= true;
	/** 是否关闭详细日志 */
	public static boolean	isVerboseLogOff	= true;

	/**
	 * 输出日志信息
	 * 
	 * @param aTag
	 *            日志标识
	 * @param aLogMessage
	 *            日志消息
	 * @param aLogStyle
	 *            日志类型
	 */
	public static void showLog(String aTag, String aLogMessage, int aLogStyle) {
		if (isLogOff) {
			switch (aLogStyle) {
			case ERROR:
				if (isErrorLogOff) {
					Log.e(aTag, aLogMessage);
				}
				break;
			case WARN:
				if (isWarnLogOff) {
					Log.w(aTag, aLogMessage);
				}
				break;
			case INFO:
				if (isInfoLogOff) {
					Log.i(aTag, aLogMessage);
				}
				break;
			case DEBUG:
				if (isDebugLogOff) {
					Log.d(aTag, aLogMessage);
				}
				break;
			case VERBOSE:
				if (isVerboseLogOff) {
					Log.v(aTag, aLogMessage);
				}
				break;
			}
		}
	}
}
