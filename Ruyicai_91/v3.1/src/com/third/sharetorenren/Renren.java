/*
 * Copyright 2011-2012 Renren Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.third.sharetorenren;

import java.util.Set;
import java.util.TreeSet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.third.sharetorenren.common.AbstractRequestListener;
import com.third.sharetorenren.exception.RenrenError;
import com.third.sharetorenren.exception.RenrenException;
import com.third.sharetorenren.status.StatusHelper;
import com.third.sharetorenren.status.StatusSetRequestParam;
import com.third.sharetorenren.status.StatusSetResponseBean;
import com.third.sharetorenren.users.UsersGetInfoHelper;
import com.third.sharetorenren.users.UsersGetInfoRequestParam;
import com.third.sharetorenren.users.UsersGetInfoResponseBean;
import com.third.sharetorenren.view.RenrenAuthListener;
import com.third.sharetorenren.view.RenrenDialog;
import com.third.sharetorenren.view.RenrenDialogListener;
import com.third.sharetorenren.view.RenrenWidgetListener;



/**
 * 封装对人人的请求，如：显示登录界面�?�?��登录、请�?人人 APIs等�?
 * 
 * @author yong.li@opi-corp.com 
 */
public class Renren implements Parcelable {
	
	/**
	 * 版本MD5信息
	 */
	private static final String SIGNATURE = "b12b32ae8aec768f84cfdbb0714fbada";

	private static final String LOG_TAG = "Renren";

	/**
	 * 用来在Activity 等对象传递renren对象时用作读写Renren对象的key
	 */
	public static final String RENREN_LABEL = "Renren";

	/**
	 * 如果服务器redirect该地�?��SDK会认为用户做了取消操作�?
	 */
	public static final String CANCEL_URI = "rrconnect://cancel";

	/**
	 * 如果服务器redirect该地�?��SDK会认为用户做了确认操作�?
	 */
	public static final String SUCCESS_URI = "rrconnect://success";

	/**
	 * 人人登录和授权的地址
	 */
	public static final String AUTHORIZE_URL = "https://graph.renren.com/oauth/authorize";

	/**
	 * 接口支持的数据格�?	 */
	public static final String RESPONSE_FORMAT_JSON = "json";

	/**
	 * 接口支持的数据格�?	 */
	public static final String RESPONSE_FORMAT_XML = "xml";

	public static final String DEFAULT_REDIRECT_URI = "http://graph.renren.com/oauth/login_success.html";

	public static final String WIDGET_CALLBACK_URI = "http://widget.renren.com/callback.html";


	private static final String RESTSERVER_URL = "http://api.renren.com/restserver.do";

	private static final String KEY_API_KEY = "api_key";
	
	private static final String KEY_SECRET = "secret";
	
	private static final String KEY_APP_ID = "appid";

	private static final String[] DEFAULT_PERMISSIONS = { "publish_feed",
			"create_album","read_user_album", "status_update" };

	/**
	 * 记录请求和响应日志的TAG
	 */
	private static final String LOG_TAG_REQUEST = "Renren-SDK-Request";
	
	private static final String LOG_TAG_RESPONSE = "Renren-SDK-Response";

	private String apiKey = "172f2fdbdbe1422dbc013f4a5671a5e7";
	
	private String secret = "7ddfa8f84d9f4b50a6b7443349b7a5f7";
	
	private String appId = "192357";

	private AccessTokenManager accessTokenManager;

	private SSO mSso;
	public Renren(Context context) {
		this.mSso = new SSO(this);
		init(context);
	}


	public Renren(Parcel in) {
		Bundle bundle = Bundle.CREATOR.createFromParcel(in);
		apiKey = bundle.getString(KEY_API_KEY);
		secret = bundle.getString(KEY_SECRET);
		appId = bundle.getString(KEY_APP_ID);
		this.accessTokenManager = AccessTokenManager.CREATOR
				.createFromParcel(in);
		mSso = new SSO(this);
	}

	/**
	 * 初始�?	 * 
	 * @param context
	 */
	public void init(Context context) {
		if (context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
			Log.w(LOG_TAG,
					"App miss permission android.permission.ACCESS_NETWORK_STATE! "
							+ "Some mobile's WebView don't display page!");
		} else {
			WebView.enablePlatformNotifications();
		}
		this.accessTokenManager = new AccessTokenManager(context);
		this.accessTokenManager.restoreSessionKey();
	}

	/**
	 * 用户授权后更新accessToken和sessionKey�?	 * 
	 * @param accessToken
	 * @throws RenrenError
	 *             换取sessionKey出现错误�?	 * @throws RuntimeException
	 *             出现其他错误�?	 */
	void updateAccessToken(String accessToken) throws RenrenError,
			RuntimeException {
		if(accessTokenManager != null) {
			this.accessTokenManager.updateAccessToken(accessToken);			
		}
	}

	/**
	 * 尝试读取sessionKey；如果用户在�?��内登录过并且没有�?��返回true�?	 * 
	 * @param context
	 * @return
	 * @deprecated 用init方法代替
	 */
	public boolean restorSessionKey(Context context) {
		this.init(context);
		if (this.accessTokenManager.isSessionKeyValid()) {
			return true;
		}
		return false;
	}

	/**
	 * 用户登录和授�?Web Server Flow)�?用该方法完成的授权不能调用RenRes.request方法；对人人
	 * API的调用需要�?过开发�?自己的服务器来代理�?
	 * 
	 * @param activity
	 * @param listener
	 * @param redirectUrl
	 *            用来处理登录和授权的�?��者的服务器地�??
	 */
	public void authorize(Activity activity, final RenrenAuthListener listener,
			String redirectUrl) {
		this.authorize(activity, null, listener, redirectUrl);
	}

	/**
	 * 用户登录和授�?Web Server Flow)�?用该方法完成的授权不能调用RenRes.request方法；对人人
	 * API的调用需要�?过开发�?自己的服务器来代理�?
	 * 
	 * @param activity
	 * @param permissions
	 *            应用想拥有的权限列表�?	 * @param listener
	 * @param redirectUrl
	 *            用来处理登录和授权的�?��者的服务器地�??
	 */
	public void authorize(Activity activity, String[] permissions,
			final RenrenAuthListener listener, String redirectUrl) {
		if (this.isSessionKeyValid()) {
			listener.onComplete(new Bundle());
			return;
		}
		RenrenDialogListener rdl = RenrenListenerFactory
				.genWebServerFlowRenrenDialogListener(this, listener,
						redirectUrl);
		// 若开发�?提供的权限列表为空，则使用默认的权限列表
		if (permissions == null) {
			permissions = DEFAULT_PERMISSIONS;
		}

		this.authorize(activity, permissions, rdl, redirectUrl, "code");
	}

	/**
	 * 完成登录并获取sessionkey(User-Agent Flow)�?	 * 
	 * @param activity
	 * @param listener
	 */
	public void authorize(Activity activity, final RenrenAuthListener listener) {
		this.authorize(activity, (String[])null, listener);
	}

	/**
	 * 完成登录并获取sessionkey(User-Agent Flow)�?	 * 
	 * @param activity
	 * @param permissions           应用想拥有的权限列表�?	 * @param listener
	 */
	public void authorize(Activity activity, String[] permissions, final RenrenAuthListener listener) {
		if (this.isSessionKeyValid()) {
			listener.onComplete(new Bundle());
			return;
		}
		RenrenDialogListener rdl = RenrenListenerFactory .genUserAgentFlowRenrenDialogListener(this, listener, DEFAULT_REDIRECT_URI);
		this.authorize(activity, permissions, rdl, DEFAULT_REDIRECT_URI, "token");
	}

	private void authorize(Activity activity, String[] permissions, final RenrenDialogListener listener, String redirectUrl, String responseType) {
		// 调用CookieManager.getInstance之前
		// 必须先调用CookieSyncManager.createInstance
		CookieSyncManager.createInstance(activity);

		Bundle params = new Bundle();
		params.putString("client_id", apiKey);
		params.putString("redirect_uri", redirectUrl);
		params.putString("response_type", responseType);
		params.putString("display", "touch");
		
		//若开发�?提供的权限列表为空，则使用默认权限列�?		
		if(permissions == null) {
			permissions = DEFAULT_PERMISSIONS;
		}
		
		if (permissions != null && permissions.length > 0) {
			String scope = TextUtils.join(" ", permissions);
			params.putString("scope", scope);
		}

		String url = AUTHORIZE_URL + "?" + Util.encodeUrl(params);
		if (activity.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Util.showAlert(activity, "没有权限", "应用�?��访问互联网的权限");
		} else {
			new RenrenDialog(activity, url, listener).show();
		}
	}

	/**
	 * 使用Single Sign-on功能 完成登录和授�?User-Agent Flow)�?<br>
	 * <br>
	 * 警告�?br>
	 * 1.调用此方法前必须在开发�?中心为应用注册apk签名的Hash，否则会出错�?br>
	 * 2.使用此方法获取返回�?请在调用此方法的activity�?重写onActivityResult
	 * 并在onActivityResult中调用renren.authorizeCallback
	 * 
	 * @param activity
	 * @param permissions            应用想拥有的权限列表�?	 * @param listener
	 * @param requestCode     在callback时需要的activityCode.
	 */
	public void authorize(Activity activity, String[] permissions,
			final RenrenAuthListener listener, int requestCode) {
		//若开发�?提供的权限列表为空，则使用默认权限列�?		
		if(permissions == null) {
			permissions = DEFAULT_PERMISSIONS;
		}
		this.mSso.authorize(activity, permissions, listener, requestCode);
	}

	/**
	 * 弹出输入框，使用用户名和密码完成登陆和授�?<br>
	 * <br>
	 * 注意�?br>
	 * 
	 * 为了方便移动应用和桌面应用，特别是那些缺乏浏览器支持的应用，人人网开放平台OAuth2.0增加了Resource Owner Password
	 * Credentials Flow 这种Client端使用用户名和密码直接获�?Access Token 的方式�? <br>
	 * 但是由于将用户的 username �?password 直接暴露�?Client 端，�?���?��用户和Client端之间有很强的信任关系�?
	 * 只有和人人网�?��平台有深度合作的应用，才允许使用此授权方式�? <br>
	 * <br>
	 * 特别提醒�?br>
	 * Client端不得记录用户的 username �?password<br>
	 * <br>
	 * 详情请参考http://wiki.dev.renren.com/wiki/Resource_Owner_Password_Credentials
	 * 
	 * 
	 * @param secretKey
	 *            应用的secret key
	 * @param userName
	 *            用户�?	 * @param password
	 *            明文密码
	 * @param permissions
	 *            应用�?��的权限列表�?
	 * @param listener
	 *            授权状�?监听�?	 */
	public void authorize(Activity activity, PasswordFlowRequestParam param, final RenrenAuthListener listener) {
		if (this.isSessionKeyValid()) {
			listener.onComplete(new Bundle());
			return;
		}
		
		//若开发�?提供的权限列表为空，则使用默认的权限列表
		if(param.getPermissions() == null) {
			param.setPermissions(DEFAULT_PERMISSIONS);
		}
		
		PasswordFlowHelper passwordFlowHelper = new PasswordFlowHelper();
		param.setApiKey(apiKey);
		param.setSecretKey(secret);
		passwordFlowHelper.login(activity, param, listener, this);
	}

	/**
	 * 同步方法<br>
	 * 使用用户名和密码完成登陆和授�?<br>
	 * 
	 * @param param
	 *            请求对象
	 * @throws RenrenException
	 */
	public PasswordFlowResponseBean authorize(PasswordFlowRequestParam param) throws RenrenException, Throwable {
		//若开发�?提供的权限列表为空，则使用默认的权限列表
		if(param.getPermissions() == null) {
			param.setPermissions(DEFAULT_PERMISSIONS);
		}
		PasswordFlowHelper passwordFlowHelper = new PasswordFlowHelper();
		param.setApiKey(apiKey);
		param.setSecretKey(secret);
		PasswordFlowResponseBean result;
		result = passwordFlowHelper.login(param);
		if (result == null) {
			throw new RenrenException("unknown error: null response");
		}
		this.updateAccessToken(result.getAccessToken());
		return result;
	}

	/**
	 * 注意：使用SSO功能时请务必在调用activity�?重写onActivityResult(int requestCode, int
	 * resultCode, Intent data)方法 并在该方法中调用authorizeCallback才可以获得返回�?
	 * 
	 */
	public void authorizeCallback(int requestCode, int resultCode, Intent data) {
		this.mSso.authorizeCallback(requestCode, resultCode, data);
	}

	/**
	 * �?��登录
	 * 
	 * @param context
	 * @return
	 */
	public String logout(Context context) {
		Util.clearCookies(context);
		this.accessTokenManager.clearPersistSession();
		return "true";
	}


	/**
	 * 发�?app�?���?	 * 
	 * @param context
	 * @param params
	 * @param listener
	 * @deprecated 由widgetDialog替换
	 */
	public void appRequest(Context context, Bundle params,
			final RenrenWidgetListener listener) {
		String url = "http://widget.renren.com/dialog/request";
		this.widgetDialog(context, params, listener, url);
	}

	/**
	 * 和widget相关的Dialog
	 * 
	 * @param context
	 * @param params
	 * @param listener
	 * @param type
	 *            feed,request,status,like,friends�?	 */
	public void widgetDialog(Context context, Bundle params,
			final RenrenWidgetListener listener, String type) {
		String url = this.checkUrl(type);
		params.putString("display", "touch");
		params.putString("return", "redirect");
		params.putString("redirect_uri", WIDGET_CALLBACK_URI);
		if (this.isAccessTokenValid()) {
			params.putString("access_token",
					this.accessTokenManager.getAccessToken());
		}
		RenrenDialogListener rdl = RenrenListenerFactory
				.genRenrenWidgetDialogListener(this, listener);
		this.dialog(context, rdl, params, url, false);
	}

	/**
	 * 支持SSO的Dialog.
	 * 
	 * @param activity
	 * @param params
	 * @param listener
	 * @param type
	 *            feed,request,status,like,friends�?	 * @param requestCode
	 */
	public void widgetDialog(Activity activity, Bundle params,
			final RenrenWidgetListener listener, String type, int requestCode) {
		String url = this.checkUrl(type);
		if (this.isAccessTokenValid()) {
			params.putString("access_token",
					this.accessTokenManager.getAccessToken());
		}
		this.mSso.widgetDialog(activity, params, listener, url, requestCode);
	}

	private final static String DIALOG_ROOT = "http://widget.renren.com/dialog/";

	private String checkUrl(String url) {
		if (!url.startsWith(DIALOG_ROOT)) {
			url = DIALOG_ROOT + url;
		}
		return url;
	}

	/**
	 * sso widgetDialog返回时调�?	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void widgetDialogCallback(int requestCode, int resultCode,
			Intent data) {
		this.mSso.widgetDialogCallback(requestCode, resultCode, data);
	}

	/**
	 * 显示人人网的对话框�?
	 * 
	 * @param context
	 * @param listener
	 * @param params
	 * @param url
	 * @param showTitle
	 */
	private void dialog(Context context, final RenrenDialogListener listener,
			Bundle params, String url, boolean showTitle) {
		params.putString("api_key", apiKey);
		String sessionKey = accessTokenManager.getSessionKey();
		if (sessionKey != null) {
			params.putString("session_key", sessionKey);
		}
		if (!params.containsKey("display")) {
			params.putString("display", "touch");
		}

		url = url + "?ua=" + SIGNATURE + "&" + Util.encodeUrl(params);
		if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
			Util.showAlert(context, "没有权限", "应用�?��访问互联网的权限");
		} else {
			new RenrenDialog(context, url, listener, showTitle).show();
		}
	}

	/**
	 * 调用 人人 APIs
	 * 
	 * @param parameters
	 * @return 返回结果为xml格式
	 */
	public String requestXML(Bundle parameters) {
		return this.request(parameters, "xml");
	}

	/**
	 * 调用 人人 APIs
	 * 
	 * @param parameters
	 * @return 返回结果为Json格式
	 */
	public String requestJSON(Bundle parameters) {
		return this.request(parameters, "json");
	}


	/**
	 * 同步调用 users.getInfo接口，返回UsersGetInfoResponseBean对象<br>
	 * users.getInfo文档地址�?br>
	 * http://wiki.dev.renren.com/wiki/Users.getInfo
	 * 
	 * @param param
	 *            请求参数
	 * @return {@link UsersGetInfoResponseBean}对象
	 * @throws RenrenException
	 * @throws Throwable
	 */
	public UsersGetInfoResponseBean getUsersInfo(UsersGetInfoRequestParam param)
			throws RenrenException, Throwable {
		UsersGetInfoHelper helper = new UsersGetInfoHelper(this);
		return helper.getUsersInfo(param);
	}

	/**
	 * 发�?状�?<br>
	 * 同步调用<br>
	 * status.set接口，参见http://wiki.dev.renren.com/wiki/Status.set
	 * 
	 * @param status
	 *          要发送的状�?
	 * @return 
	 * 			若成功，返回发�?�?{@link StatusSetResponseBean}对象，否则返回null
	 * @thorws RenrenException
	 * @throws Throwable 
	 */
	public StatusSetResponseBean publishStatus(StatusSetRequestParam status) throws RenrenException, Throwable {
		StatusHelper helper = new StatusHelper(this);
		return helper.publish(status);
	}

	/**
	 * 使用发�?/编辑状�?的窗口发送状�?	 * 
	 * @param activity
	 * 			显示窗口的Activity，不能为null
	 * @param stat
	 * 			要发�?编辑的状态，若为null，则显示的窗口为新建状�?窗口
	 * 
	 * <p>注意：需要在AndroidManifest.xml中添加Activity描述�?	 * <p>&ltactivity android:name=
	 *    "com.renren.api.connect.android.AuthorizationHelper$BlockActivity"
	 *    android:theme="@android:style/Theme.Dialog" &gt&lt/activity&gt
	 * <p>同时�?��在AndroidManifest.xml中添加Activity�?	 * <p>&ltactivity android:name=
	 *    "com.renren.api.connect.android.status.StatusPubActivity"
	 *    &gt&lt/activity&gt
	 */
	public void publishStatus(Activity activity, StatusSetRequestParam stat) {
		StatusHelper helper = new StatusHelper(this);
		helper.startStatusPubActivity(activity, stat);
	}

	/**
	 * 发布状�?<br>
	 * 使用sdk提供的Widget窗口发状�?	 * 参见http://wiki.dev.renren.com/wiki/Status_dialog
	 * 
	 * @param activity
	 * 			当前activity
	 * @param listener
	 * 			监听结果的listener
	 * @throws RenrenException 
	 */
	public void publishStatus(Activity activity,
			final AbstractRequestListener<StatusSetResponseBean> listener) throws RenrenException {
		StatusHelper helper = new StatusHelper(this);
		helper.startStatusPubWidget(this.getAppId(), activity, listener);
	}

	
	/**
	 * 以startActivity方式根据用户传入�?	 * Intent 对象启动相应的AbstractRenrenRequestActivity对象
	 * 
	 * @param activity 
	 * 			当前Activity
	 * @param intent 
	 * 			启动AbstractRenrenRequestActivity对象用到的intent
	 */
	public void startRenrenRequestActivity(Activity activity, Intent intent) {
		intent.putExtra(Renren.RENREN_LABEL, this);
		activity.startActivity(intent);
	}
	/**
	 * 以startActivityForResult方式根据用户传入�?	 * Intent 对象启动相应的AbstractRenrenRequestActivity对象
	 * 
	 * @param activity 
	 * 			当前Activity
	 * @param intent 
	 * 			启动AbstractRenrenRequestActivity对象用到的intent
	 * @param requestCode
	 * 			启动Activity用到的RequestCode
	 */
	public void startRenrenRequestActivity(Activity activity, 
			Intent intent, int requestCode) {
		intent.putExtra(RENREN_LABEL, this);
		activity.startActivityForResult(intent, requestCode);
	}
	

	/**
	 * 调用 人人 APIs
	 * 
	 * @param parameters
	 * @param format
	 *            json or xml
	 * @return
	 */
	private String request(Bundle parameters, String format) {
		parameters.putString("format", format);
		if (isSessionKeyValid()) {
			parameters.putString("session_key", accessTokenManager.getSessionKey());
		}
		this.prepareParams(parameters);
		logRequest(parameters);
		String response = Util.openUrl(RESTSERVER_URL, "POST", parameters);
		logResponse(parameters.getString("method"), response);
		return response;
	}
	
	/**
	 * 记录请求log
	 */
	private void logRequest(Bundle params) {
		if(params != null) {
			String method = params.getString("method");
			if(method != null) {
				StringBuffer sb = new StringBuffer();
				sb.append("method=").append(method).append("&").append(params.toString());
				Log.i(LOG_TAG_REQUEST, sb.toString());
			} else {
				Log.i(LOG_TAG_REQUEST, params.toString());
			}
		}		
	}
	
	/**
	 * 记录响应log
	 * @param response
	 */
	private void logResponse(String method, String response) {
		if(method != null && response != null) {
			StringBuffer sb = new StringBuffer();
			sb.append("method=").append(method).append("&").append(response);
			Log.i(LOG_TAG_RESPONSE, sb.toString());
		}
	}


	private void prepareParams(Bundle params) {
		params.putString("api_key", apiKey);
		params.putString("v", "1.0");
		params.putString("call_id", String.valueOf(System.currentTimeMillis()));
		params.putString("xn_ss", "1");// sessionSecret作为加密密钥

		StringBuffer sb = new StringBuffer();
		Set<String> keys = new TreeSet<String>(params.keySet());
		for (String key : keys) {
			sb.append(key);
			sb.append("=");
			sb.append(params.getString(key));
		}
		sb.append(accessTokenManager.getSessionSecret());
		params.putString("sig", Util.md5(sb.toString()));
	}

	/**
	 * 判断sessionKey是否有效�?	 * 
	 * @return boolean
	 */
	public boolean isSessionKeyValid() {
		return this.accessTokenManager.isSessionKeyValid();
	}

	public boolean isAccessTokenValid() {
		String at = this.accessTokenManager.getAccessToken();
		if (at != null && at.trim().length() > 0) {
			return true;
		}
		return false;
	}

	public String getApiKey() {
		return this.apiKey;
	}
	
	public String getSecret () {
		return this.secret;
	}
	
	public String getAppId () {
		return this.appId;
	}
	
	public String getSessionKey() {
		if(accessTokenManager != null) {
			return accessTokenManager.getSessionKey();
		}
		return null;
	}
	
	public String getAccessToken() {
		if(accessTokenManager != null) {
			return accessTokenManager.getAccessToken();
		}
		return null;
	}

	/**
	 * 获取当前登录用户的uid
	 * 
	 * @return
	 */
	public long getCurrentUid() {
		return accessTokenManager.getUid();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		if (apiKey != null) {
			bundle.putString(KEY_API_KEY, apiKey);
		}
		if (secret != null) {
			bundle.putString(KEY_SECRET, secret);
		}
		if (appId != null) {
			bundle.putString(KEY_APP_ID, appId);
		}
		bundle.writeToParcel(dest, flags);
		this.accessTokenManager.writeToParcel(dest, flags);
	}

	public static final Parcelable.Creator<Renren> CREATOR = new Parcelable.Creator<Renren>() {
		public Renren createFromParcel(Parcel in) {
			return new Renren(in);
		}

		public Renren[] newArray(int size) {
			return new Renren[size];
		}
	};
}
