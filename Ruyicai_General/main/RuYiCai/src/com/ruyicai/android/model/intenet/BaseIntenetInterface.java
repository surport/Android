package com.ruyicai.android.model.intenet;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.ruyicai.android.model.bean.PhoneInfo;
import com.ruyicai.android.model.bean.SoftWareInfo;
import com.ruyicai.android.tools.HttpTools;
import com.ruyicai.android.tools.LogTools;

/**
 * 如意彩网络接口基类
 * 
 * @author PengCX
 * @since RYC1.0 2013-2-26
 */
public abstract class BaseIntenetInterface {
	private static final String	TAG				= "BaseInterface";

	/** 是否连接正式线标识 */
	private boolean				isFormalLine	= false;

	/** 手否添加SIM卡的手机号 */
	private boolean				_fAddPhoneSIM;
	/** 是否添加网卡地址 */
	private boolean				_fAddMac;

	public BaseIntenetInterface() {
		super();
	}

	public BaseIntenetInterface(boolean aAddPhoneSIM, boolean aAddMac) {
		super();
		_fAddPhoneSIM = aAddPhoneSIM;
		_fAddMac = aAddMac;
	}

	/**
	 * 设置接口特有参数
	 * 
	 * @param aParamtersJsonObject
	 *            将要保存特有参数的Json对象
	 * @return 保存了特有参数的Json对象
	 */
	public abstract JSONObject setParticularParamerters(Context aContext,
			JSONObject aParamtersJsonObject);

	/**
	 * 获取接口中的JSON字符串数据
	 */
	public String getDataFromIntenet(final Context aContext) {
		final String resultJsonString;
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				JSONObject paramtersJsonObject = new JSONObject();

				// 设置接口公共参数
				paramtersJsonObject = setCommonParameters(aContext,
						paramtersJsonObject);
				// 设置接口特有参数
				paramtersJsonObject = setParticularParamerters(aContext,
						paramtersJsonObject);
				LogTools.showLog(TAG,
						"发送的联网请求字符串：" + paramtersJsonObject.toString(),
						LogTools.INFO);

				// 连接网络获取结果数据
//				if (isFormalLine) {
//					resultJsonString = HttpTools.connectingIntenetForResult(
//							"http://www.ruyicai.com/lotserver/RuyicaiServlet",
//							HttpTools.POST_METHOD_ID,
//							paramtersJsonObject.toString());
//				} else {
//					resultJsonString = HttpTools
//							.connectingIntenetForResult(
//									"http://202.43.152.170:8080/lotserver/RuyicaiServlet",
//									HttpTools.POST_METHOD_ID,
//									paramtersJsonObject.toString());
//				}
//				
				
			}
		}).start();
		return "";
	}

	/**
	 * 设置公共的参数
	 * 
	 * @param aContext
	 *            上下文对象
	 * @param aJsonObject
	 *            将要保存公共参数的Json对象
	 * @return 保存了公共参数的Json对象
	 */
	private JSONObject setCommonParameters(Context aContext,
			JSONObject aJsonObject) {
		PhoneInfo phoneInfo = PhoneInfo.getInstance(aContext);
		SoftWareInfo softWareInfo = SoftWareInfo.getInstance(aContext);

		try {
			// 设置用户识别码
			aJsonObject.put("imsi", phoneInfo.getImsi());
			// 设置身份码
			aJsonObject.put("imei", phoneInfo.getImei());
			// 设置版本号
			aJsonObject.put("softwareversion", softWareInfo.getVersionName());
			// 设置手机型号
			aJsonObject.put("machineid", phoneInfo.getMachineid());
			// 设置渠道号
			aJsonObject.put("coopid", SoftWareInfo.CHANNEL_ID);

			if (_fAddPhoneSIM) {
				// 设置SIM卡的手机号
				aJsonObject.put("phoneSIM", phoneInfo.getPhoneSIM());
			}

			// 设置平台
			aJsonObject.put("platform", phoneInfo.getPlatform());

			if (_fAddMac) {
				// 设置网卡地址
				aJsonObject.put("mac", phoneInfo.getMac());
			}

			// 设置是否压缩
			aJsonObject.put("isCompress", SoftWareInfo.ISCOMPRESS);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return aJsonObject;
	}
	
	
	class InnerHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	}
}
