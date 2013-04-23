/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.third.sharetorenren.status;

import java.util.concurrent.Executor;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.third.sharetorenren.Renren;
import com.third.sharetorenren.Util;
import com.third.sharetorenren.common.AbstractRequestListener;
import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.exception.RenrenException;
import com.third.sharetorenren.view.RenrenWidgetListener;



/**
 * 提供对发布人人网状�?等方�? * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 * 
 */
public class StatusHelper {
	/**
	 * 发�?状�?�?��的权�?	 */
	public static final String[] PUBLISH_STATUS_PERMISSIONS = { "status_update" };

	/**
	 * 用来发�?请求的{@link Renren}对象
	 */
	private Renren renren;

	/**
	 * StatusHelper构�?函数
	 * 
	 * @param renren
	 *          用来发�?请求的{@link Renren}对象
	 */
	public StatusHelper(Renren renren) {
		this.renren = renren;
	}

	/**
	 * 发布�?��状�?到人人网
	 * 
	 * @param status
	 *          要发布的状�?对象
	 * @return 
	 * 			若状态为空或者发送失败，会抛出异常，否则返回成功
	 * 			{@link FeedPublishResponseBean}对象
	 * @throws RenrenException
	 * @throws Throwable 
	 */
	public StatusSetResponseBean publish(StatusSetRequestParam status) throws RenrenException, Throwable {
		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}
		
		//参数不能为空
		if(status == null) {
			String errorMsg = "The parameter is null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg, errorMsg);
		}
		
		// 发布状�?
		String response;
		try {
			Bundle params = status.getParams();
			response = renren.requestJSON(params);
		} catch (RenrenException rre) {
			Util.logger(rre.getMessage());
			throw rre;
		} catch (RuntimeException re) {
			Util.logger(re.getMessage());
			throw new Throwable(re);
		}

		RenrenError rrError = Util.parseRenrenError(response, Renren.RESPONSE_FORMAT_JSON);
		if (rrError != null) {
			throw new RenrenException(rrError);
		} else {
			try {
				JSONObject json = new JSONObject(response);
				if (json.optInt("result") == 1) {
					return new StatusSetResponseBean(response);
				} else {
					String errorMsg = "Cannot parse the response.";
					throw new RenrenException(
							RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE,
							errorMsg, errorMsg);
				}
			} catch (JSONException je) {
				Util.logger(je.getMessage());
				throw new RenrenException(
						RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE, je.getMessage(),
						je.getMessage());
			}
		}
	} // end of public Status publish(Activity activity, Status status,

	/**
	 * 异步发�?状�?的方�?	 * 
	 * @param pool
	 *            执行发�?状�?操作的线程池
	 * @param status
	 *            要发布的状�?对象
	 * @param listener
	 *            用以监听发布状�?结果的监听器对象
	 * @param truncOption
	 *            若超出了长度，是否自动截短至140个字
	 */
	public void asyncPublish(Executor pool, 
			final StatusSetRequestParam status,
			final AbstractRequestListener<StatusSetResponseBean> listener,
			final boolean truncOption) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					StatusSetResponseBean stat = publish(status);
					if(listener != null) {
						listener.onComplete(stat);
					}			
				} catch (RenrenException rre) { // 参数、服务器等错误或异常
					Util.logger(rre.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(rre
								.getErrorCode(), rre.getMessage(), rre
								.getOrgResponse()));
					}
				} catch (Throwable t) { // 运行时异�?					Util.logger(t.getMessage());
					if (listener != null) {
						listener.onFault(t);
					}
				}
			}
		});
	}

	/**
	 * 显示发�?/编辑状�?的窗�?	 * 
	 * @param activity
	 *            显示窗口的Activity，不能为null
	 * @param stat
	 *            要发�?编辑的状态，若为null，则显示的窗口为新建状�?窗口
	 *            
	 * <p>注意：需要在AndroidManifest.xml中添加Activity描述�?	 * <p>&ltactivity android:name=
	 *    "com.renren.api.connect.android.AuthorizationHelper$BlockActivity"
	 *    android:theme="@android:style/Theme.Dialog" &gt&lt/activity&gt
	 * <p>同时�?��在AndroidManifest.xml中添加Activity�?	 * <p>&ltactivity android:name=
	 *    "com.renren.api.connect.android.activity.StatusPubActivity"
	 *    &gt&lt/activity&gt
	 * 
	 */
	public void startStatusPubActivity(final Activity activity, 
			final StatusSetRequestParam stat) {
		Intent intent = new Intent(activity, StatusPubActivity.class);
		Bundle bundle = new Bundle();
		if (stat != null) {
			bundle.putParcelable(StatusSetRequestParam.STATUS_LABEL, stat);
		}
		intent.putExtras(bundle);
		renren.startRenrenRequestActivity(activity, intent);
	}

	/**
	 * 使用Widget窗口发状�?	 * 
	 * @param apiID
	 *            应用的api ID
	 * @param activity
	 *            显示此widget的activity
	 * @param listener
	 *            监听结果的listener
	 * @throws RenrenException
	 */
	public void startStatusPubWidget(String appID, Activity activity,
			final AbstractRequestListener<StatusSetResponseBean> listener)
			throws RenrenException {

		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}

		final Bundle params = new Bundle();
		params.putString("app_id", appID);
		renren.widgetDialog(activity, params, new RenrenWidgetListener() {

			@Override
			public void onError(Bundle retParams) {
				if (listener != null) {
					listener.onRenrenError(new RenrenError(retParams
							.getString("error")
							+ retParams.getString("error_description")));
				}
			}

			@Override
			public void onComplete(Bundle retParams) {
				if (listener != null) {
					if(retParams.getString("flag").equals("success")) {
						listener.onComplete(new StatusSetResponseBean(1));
					} else {
						listener.onComplete(null);
					}
				}
			}

			@Override
			public void onCancel(Bundle retParams) {
				String errorMsg = "Operation cancelled.";
				if (listener != null) {
					listener.onRenrenError(new RenrenError(
							RenrenError.ERROR_CODE_OPERATION_CANCELLED,
							errorMsg, errorMsg));
				}
			}
		}, "status");
	}
}
