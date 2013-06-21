/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.ruyicai.activity.common;

import java.util.Calendar;
import java.util.LinkedHashSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.alipay.android.app.IAliPay;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.alipay.android.secure.BaseHelper;
import com.alipay.android.secure.MobileSecurePayHelper;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ChannelConstants;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
import com.ruyicai.net.newtransaction.LoginAcrossWeibo;
import com.ruyicai.net.newtransaction.LoginInterface;
import com.ruyicai.net.newtransaction.RegisterInterface;
import com.ruyicai.util.CallServicePhoneConfirm;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.tauth.TAuthView;
import com.tencent.tauth.TencentOpenAPI;
import com.tencent.tauth.bean.OpenId;
import com.tencent.tauth.bean.UserInfo;
import com.tencent.tauth.http.Callback;
import com.tencent.tauth.http.TDebug;
import com.third.share.Token;
import com.third.share.Utility;
import com.third.share.Weibo;
import com.third.share.WeiboDialogListener;
import com.third.share.WeiboParameters;
import com.umeng.analytics.MobclickAgent;

public class UserLogin extends Activity implements TextWatcher {
	public static final String SUCCESS = "loginsuccess";
	public static final String UNSUCCESS = "unloginsuccess";
	private Context context;
	boolean isWifi = false;
	boolean isGPRS = false;
	Dialog dialog;
	private static final int DIALOG_FORGET_PASSWORD = 1;
	private static final int DIALOG_PROTOCOL = 2;
	// cc 20100711 网络提示框
	private static final int PROGRESS_VALUE = 0;
	ProgressDialog progressDialog;
	private RWSharedPreferences shellRW;
	private CheckBox remPwd_checkBox, auto_login_checkBox;// 自动登录和记住密码的checkbox
	private EditText phoneNum_edit;
	private EditText password_edit;
	boolean b = false;// 用户登录成功 b = true；用户登录失败 b = false；当用户登录成功的时候发出登录成功的广播
						// mainGroup接受这个广播初始化头信息（调用initTop方法）
	boolean isConfigChange = false;
	public int configFlag;// 0表示登录，1表示注册
	int age;
	String phonenum;
	String password;
	// 应用程序包名
	String packageName;
	boolean on = false;
	boolean turn = true;
	String realName;
	Animation shake = null;
	private String message, mobileid, name;
	private String randomNumber;// 自动登录后返回的随机数
	Boolean isProtocol = true;
	// String isBindPhone = "1";
	private String isAutoLogin = "0";
	private boolean autologin = false;
	boolean ischeckId = false;
	boolean ischeckReferrer = false;
	int i = 0;
//	ProgressDialog pDialog = null;

	/***
	 * 第三方登陆
	 * */
	String nickname;
	String Uid;// 第三方登陆
	private static final String TAG = "TAuthDemoActivity";
	public static final int REQUEST_PICK_PICTURE = 1001;
	/**
	 * <p>
	 * 不能包含特殊字符“#”</br> 不能是浏览器能识别的协议，比如：http://auth.qq.com</br> 不设置时默认使用：
	 * auth://tauth.qq.com/</br></br> 不区分大小写。在Manifest设置的scheme必须是小写</br>
	 * 在Manifest中设置intent-filter：data android:scheme="auth"
	 * </p>
	 * 例如：</br> &ltintent-filter> </br> &ltaction
	 * android:name="android.intent.action.VIEW" /> </br> &ltcategory
	 * android:name="android.intent.category.DEFAULT" /> </br> &ltcategory
	 * android:name="android.intent.category.BROWSABLE" /> </br> &ltdata
	 * android:scheme="auth"/> </br> &lt/intent-filter> </br>
	 * 
	 */
	private static final String CALLBACK = "auth://tauth.qq.com/";

	public String mAppid = "100305073";// 申请时分配的appid
	private String scope = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";// 授权范围
	private AuthReceiver receiver;
	public String mAccessToken, mOpenId;
	public static final int PROGRESS = 100000;

	/**
	 * 处理登录的消息及注册的消息
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				regToLogin(phonenum, password, "0", packageName);
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 3:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 4:
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 5:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 6:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 7:
				progressDialog.dismiss();
				password_edit.startAnimation(shake);
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 8:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 9:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				break;
			case 10:
				progressDialog.dismiss();
				MobclickAgent.onEvent(UserLogin.this, "dengluchenggong");// BY贺思明
																			// 登陆成功
				Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG)
						.show();
				if (b == true) {
					Intent intent = new Intent(SUCCESS);
					sendBroadcast(intent);
					PublicConst.islogin = true;
					UserLogin.this.setResult(RESULT_OK);
					UserLogin.this.finish();
				}
				break;
			case 20:// 微博获取昵称后登陆
				progressDialog.dismiss();
				sinaweibologin();
				break;
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_FORGET_PASSWORD:
			LayoutInflater factory = LayoutInflater.from(this);
			final View forgetPwd = factory.inflate(
					R.layout.alert_dialog_forget_password, null);
			return new AlertDialog.Builder(UserLogin.this)
					.setIcon(R.drawable.star_big_on)
					.setTitle(R.string.forget_password)
					.setView(forgetPwd)
					.setPositiveButton(R.string.giveCall,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent myIntentDial = new Intent(
											"android.intent.action.CALL", Uri
													.parse("tel:4006651000"));
									startActivity(myIntentDial);
								}
							})
					.setNegativeButton(R.string.return_button,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
		case DIALOG_PROTOCOL:
			return new AlertDialog.Builder(UserLogin.this)
					.setTitle(R.string.login_string_protocol)
					.setView(getView())
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

								}
							})
					.setNegativeButton(R.string.xitongshezhi_check_off,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
			// 网络连接提示框 2010/7/11 陈晨
		case PROGRESS_VALUE:
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("提示");
			progressDialog.setMessage("网络连接中...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		case PROGRESS:
			Dialog dialog = null;
			dialog = new ProgressDialog(this);
			((ProgressDialog) dialog).setMessage("请求中,请稍等...");
			return dialog;
		}

		// }

		return null;
	}

	public View getView() {
		LayoutInflater factoryProtocol = LayoutInflater.from(this);
		final View view = factoryProtocol.inflate(
				R.layout.user_login_protocol_dialog, null);
		WebView webView = (WebView) view.findViewById(R.id.ruyipackage_webview);
		String iFileName = "login_protocol.html";
		String url = "file:///android_asset/" + iFileName;
		webView.loadUrl(url);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		isConfigChange = true;
		if (configFlag == 0) {
			turnToLogin();
		} else if (configFlag == 1) {
			turnToReg();
		}
		isConfigChange = false;
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView(int)} to describe what is
	 * to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle bundle = getIntent().getExtras();
		context = this;
		if (bundle != null) {
			on = bundle.getBoolean("switch");// 读出数据
		}
		if (getinfo() != null && getinfo().equals("regist")) {
			turnToReg();
		} else {
			if (on) {
				turnToReg();
			} else {
				turnToLogin();
			}
		}
		// 获取应用的包名
		packageName = getPackageName();
		MobclickAgent.onEvent(UserLogin.this, "dengluzhuce");// BY贺思明
																// 2012-6-29,修改于2012-11-19在首页点击“登录注册”
	}

	private String getinfo() {
		String s = null;
		Intent intent = getIntent();
		if (intent != null) {
			s = intent.getStringExtra("regist");
		}
		return s;
	}

	/**
	 * 检测网络是否可用
	 */
	private void checkWirelessNetwork() {
		CheckWireless checkWireless = new CheckWireless(this);// 初始化检测网络对象
		isWifi = checkWireless.getConnectWIFI();
		isGPRS = checkWireless.getConnectGPRS();
		if (isWifi == false && isGPRS == false) {
			dialog = new Dialog(this, R.style.dialog);
			LayoutInflater factory = LayoutInflater.from(this);
			View view = factory.inflate(R.layout.unnetdialog, null);
			dialog.setContentView(view);
			Button ok = (Button) view.findViewById(R.id.ok);
			ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
			dialog.show();

		}
	}

	/**
	 * 登录对话框
	 */
	private void turnToLogin() {
		turn = false;
		configFlag = 0;
		setContentView(R.layout.alert_dialog_login_entry);
		checkWirelessNetwork();// 检测网络状态
		TextView title = (TextView) findViewById(R.id.alert_login_title);
		Button loginButton = (Button) findViewById(R.id.login_button);
		shellRW = new RWSharedPreferences(this, "addInfo");

		autologin = shellRW.getBooleanValue(ShellRWConstants.AUTO_LOGIN);
		// 记住电话号码 和手机号 和是否记住密码的状态
		remPwd_checkBox = (CheckBox) findViewById(R.id.remember_password_checkBox);
		auto_login_checkBox = (CheckBox) findViewById(R.id.auto_login_checkBox);
		auto_login_checkBox.setChecked(autologin);
		phoneNum_edit = (EditText) findViewById(R.id.phoneNum_edit);
		phoneNum_edit.setFocusable(true);
		phoneNum_edit.requestFocus();
		password_edit = (EditText) findViewById(R.id.password_edit);
		// fyj edit 逻辑错误 20100709 判断 checkbox记录的信息是否可用
		// fqc eidt 记住密码是在点击了记住密码的复选框 和 点击登录 时进入到preference password键值中
		// fqc edit 在屏幕切换的时候密码计入到preference中的passwordConfig中随时 记住密码
		String iTempCheck = shellRW.getStringValue("remPwd_checkBox");
		String iTempPass = shellRW.getStringValue("password");
		title.setText("用户登录");
		loginButton.setText(getResources().getString(R.string.login));
		if (isConfigChange == true) {
			if (shellRW.getStringValue("passwordConfig") != null) {
				password_edit.setText(shellRW.getStringValue("passwordConfig"));
			} else {
				password_edit.setText("");
			}
			if (shellRW.getStringValue("remPwd_checkBox") != null
					&& shellRW.getStringValue("remPwd_checkBox") == "true") {
				remPwd_checkBox.setChecked(true);
			} else {
				remPwd_checkBox.setText("");
			}
		} else if (iTempCheck != null && iTempPass != null) {
			if (iTempCheck.equalsIgnoreCase("true")) {
				password_edit.setText(iTempPass);
				remPwd_checkBox.setChecked(true);
			} else {
				password_edit.setText("");
			}
		} else {
			password_edit.setText("");
		}
		if (shellRW.getStringValue("phoneNumber") != null) {
			phoneNum_edit.setText(shellRW.getStringValue("phoneNumber"));
		}

		phoneNum_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String phoneNumber = phoneNum_edit.getText().toString();
				shellRW.putStringValue("phoneNumber", phoneNumber);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});
		password_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String password = password_edit.getText().toString();
				shellRW.putStringValue("passwordConfig", password);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});
		initLoginCheckBox();// 初始化 记住密码 和 自动登录框
		/* 当点击手机号文本框的时候，清除里面的内容 */
		final EditText phone_name_Text = (EditText) findViewById(R.id.phoneNum_edit);
		loginButton.setBackgroundResource(R.drawable.loginselecter);
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(UserLogin.this, "denglu");// BY贺思明 点击登录按钮
				beginLogin();
			}
		});
		// 新浪微博登陆
		// 点击注册按钮时，跳转到注册页面
		RelativeLayout sinalogin = (RelativeLayout) findViewById(R.id.logintosina);
		sinalogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				oauth();
			}
		});
		// 腾讯微博登陆
		RelativeLayout qqlogin = (RelativeLayout) findViewById(R.id.logintoqq);
		qqlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				auth(mAppid, "_self");
				registerIntentReceivers();
			}
		});
		
		//支付宝登录
		RelativeLayout zhifubaologin = (RelativeLayout) findViewById(R.id.loginto_zhifubao);
		zhifubaologin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(context);
				boolean isMobile_spExist = mspHelper.detectMobile_sp(Constants.ALIPAY_PLUGIN_NAME,
						Constants.ALIPAY_PACK_NAME);
				if (isMobile_spExist) {
					pay();
				}
			}
		});
		// 点击注册按钮时，跳转到注册页面
		Button login_return = (Button) findViewById(R.id.usercenter_btn_return);
		login_return.setBackgroundResource(R.drawable.returnselecter);
		login_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserLogin.this.setResult(0); // 未登录
				UserLogin.this.finish();
			}

		});
		// 点击注册按钮时，跳转到注册页面
		RelativeLayout register = (RelativeLayout) findViewById(R.id.doregister);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				turnToReg();
			}
		});
		RelativeLayout phoneofkefu = (RelativeLayout) findViewById(R.id.phoneofkefu);
		phoneofkefu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CallServicePhoneConfirm.phoneKefu(UserLogin.this);
			}
		});
		RelativeLayout forget_password = (RelativeLayout) findViewById(R.id.findoutpassword);
		forget_password.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(UserLogin.this,
						ForgetPasswordActivity.class);
				startActivity(intent);
			}
		});

	}

	/**
	 * 初始化用户登陆的CheckBox
	 */
	private void initLoginCheckBox() {
		remPwd_checkBox.setButtonDrawable(R.drawable.check_select);
		auto_login_checkBox.setButtonDrawable(R.drawable.check_select);

		auto_login_checkBox.setChecked(shellRW
				.getBooleanValue(ShellRWConstants.AUTO_LOGIN));
		auto_login_checkBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						auto_login(isChecked);
					}
				});
		// 实现记住密码 和 复选框的状态
		remPwd_checkBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						rem_password_num(isChecked);
					}
				});

	}

	/**
	 * 登录联网操作
	 */

	private void beginLogin() {
		// showDialog(DIALOG_PROGRESS);
		EditText password_edit = (EditText) findViewById(R.id.password_edit);
		Editable password = password_edit.getText();
		final String password_string = String.valueOf(password);
		if (remPwd_checkBox.isChecked())
			shellRW.putStringValue("password", password_string);
		// 判断手机号长度是否正确
		EditText phone_name_Text = (EditText) findViewById(R.id.phoneNum_edit);
		Editable editText = phone_name_Text.getText();
		// fqc edit 7/3/2010 判断密码长度 必须在6~16之间
		EditText password_text = (EditText) findViewById(R.id.password_edit);
		int passwordLength = password_text.getText().toString().length();
		if (editText.toString().equals("")) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_LONG).show();
		} else if (passwordLength < 6 || passwordLength > 16) {
			password_edit.startAnimation(shake);
			Toast.makeText(this, "密码必须为6~16位！", Toast.LENGTH_LONG).show();
		} else {//
			regToLogin(String.valueOf(phoneNum_edit.getText()),
					password_string, isAutoLogin, packageName);
		}
	}

	private void sinaweibologin() {
		weiboToLogin("sina", Uid, nickname, packageName);
	}

	/**
	 * 注册成功后直接登录
	 * 
	 */
	private void regToLogin(final String phonenum, final String password,
			final String isAutoLogin, final String alias) {
		showDialog(PROGRESS_VALUE);

		Thread t = new Thread(new Runnable() {
			public void run() {
				String error_code = "0";
				try {
					String str = LoginInterface.login(phonenum, password,
							isAutoLogin, alias.substring(14));
					JSONObject json = new JSONObject(str);
					error_code = json.getString("error_code");
					message = json.getString("message");
					if (error_code.equals("0000")) {
						// 登陆成功

						shellRW = new RWSharedPreferences(UserLogin.this,
								"addInfo");
						mobileid = json.getString("mobileid");
						name = json.getString("name");
						if (isAutoLogin.equals("1")) {
							randomNumber = json.getString("randomNumber");
							shellRW.putStringValue(
									ShellRWConstants.RANDOMNUMBER, randomNumber);
						} else {
							shellRW.putBooleanValue(
									ShellRWConstants.AUTO_LOGIN, false);
							shellRW.putStringValue(
									ShellRWConstants.RANDOMNUMBER, "");
						}
						String sessionid = json.getString("sessionid");
						String userno = json.getString("userno");
						String cerdid = json.getString("certid");
						String username = json.getString("userName");
						
						/**add by pengcx 20130604 start*/
						String email = json.getString("email");
						shellRW.putStringValue("email", email);
						/**add by pengcx 20130604 end*/
						
						shellRW.putStringValue("sessionid", sessionid);
						shellRW.putStringValue("name", name);
						shellRW.putStringValue("userno", userno);
						shellRW.putStringValue("phonenum", phonenum);
						shellRW.putStringValue("username", username);// 用户名
						shellRW.putStringValue("mobileid", mobileid);// 向ShellRWSharesPreferences中写入绑定手机号
						shellRW.putStringValue("certid", cerdid);// 向ShellRWSharesPreferences中写入身份证号

						// 添加極光推送用戶標簽,以用戶的用户id和渠道号作为参数
						setJpushAlias(userno);

						b = true;
						Message msg = new Message();
						msg.what = 10;
						handler.sendMessage(msg);
					} else if (error_code.equals("070002")) {
						Message msg = new Message();
						msg.what = 7;
						handler.sendMessage(msg);
					} else if (error_code.equals("4444")) {
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					} else if (error_code.equals("0")) {
						Message msg = new Message();
						msg.what = 8;
						handler.sendMessage(msg);

					} else {
						Message msg = new Message();
						msg.what = 9;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progressDialog.dismiss();
				}
			}
		});
		t.start();
	}

	/**
	 * 设置Jpush的别名
	 * 
	 * @param userno
	 *            用户编号
	 */
	private void setJpushAlias(String userno) {
		LinkedHashSet<String> tags = new LinkedHashSet<String>();
		tags.add(ChannelConstants.COOP_ID);
		Log.i("Jpush", "我设置了用户别名：" + userno + "|" + tags.toString());
		JPushInterface.setAliasAndTags(UserLogin.this, userno, tags);
	}

	private void weiboToLogin(final String source, final String openId,
			final String nickName, final String alias) {
		showDialog(PROGRESS_VALUE);

		Thread t = new Thread(new Runnable() {
			public void run() {
				String error_code = "0";
				try {
					String str = LoginAcrossWeibo.login(source, openId,
							nickName, alias.substring(14));
					JSONObject json = new JSONObject(str);
					error_code = json.getString("error_code");
					message = json.getString("message");
					if (error_code.equals("0000")) {
						shellRW = new RWSharedPreferences(UserLogin.this,
								"addInfo");
						mobileid = json.getString("mobileid");
						name = json.getString("name");
						if (isAutoLogin.equals("1")) {
							randomNumber = json.getString("randomNumber");
							shellRW.putStringValue(
									ShellRWConstants.RANDOMNUMBER, randomNumber);
						} else {
							shellRW.putBooleanValue(
									ShellRWConstants.AUTO_LOGIN, false);
							shellRW.putStringValue(
									ShellRWConstants.RANDOMNUMBER, "");
						}
						String sessionid = json.getString("sessionid");
						String userno = json.getString("userno");
						String cerdid = json.getString("certid");
						String username = json.getString("userName");

						shellRW.putStringValue("sessionid", sessionid);
						shellRW.putStringValue("name", name);
						shellRW.putStringValue("userno", userno);
						shellRW.putStringValue("phonenum", phonenum);
						shellRW.putStringValue("username", username);// 用户名
						shellRW.putStringValue("mobileid", mobileid);// 向ShellRWSharesPreferences中写入绑定手机号
						shellRW.putStringValue("certid", cerdid);// 向ShellRWSharesPreferences中写入身份证号
						
						// 添加極光推送用戶標簽,以用戶的用户id和渠道号作为参数
						setJpushAlias(userno);
						
						b = true;
						PublicConst.isthirdlogin = true;
						Message msg = new Message();
						msg.what = 10;

						handler.sendMessage(msg);
					} else if (error_code.equals("070002")) {
						Message msg = new Message();
						msg.what = 7;
						handler.sendMessage(msg);
					} else if (error_code.equals("4444")) {
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					} else if (error_code.equals("0")) {
						Message msg = new Message();
						msg.what = 8;
						handler.sendMessage(msg);

					} else {
						Message msg = new Message();
						msg.what = 9;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progressDialog.dismiss();
				}
			}

		});
		t.start();

	}

	/**
	 * 注册框
	 */
	private void turnToReg() {
		shellRW = new RWSharedPreferences(this, "addInfo");
		configFlag = 1;
		setContentView(R.layout.user_register);
		Button register_button = (Button) findViewById(R.id.register_button_);
		final EditText phoneNumEdit = (EditText) findViewById(R.id.register_username_edit);
		final EditText passwordEdit = (EditText) findViewById(R.id.register_password_edit);
		final EditText confirmPasswordEdit = (EditText) findViewById(R.id.register_confirm_password_edit);
		final EditText cardIdEdit = (EditText) findViewById(R.id.register_id_num_edit);
		final EditText nameEdit = (EditText) findViewById(R.id.register_name_edit);
		final EditText referrerEdit = (EditText) findViewById(R.id.register_referrer_edit);
		final LinearLayout layoutId = (LinearLayout) findViewById(R.id.user_register_linear_card_id);
		final LinearLayout layoutReferrer = (LinearLayout) findViewById(R.id.user_register_linear_referrer);

		/**add by yejc 20130424 start*/
		cardIdEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String cardId = cardIdEdit.getText().toString();
					if (!CheckUtil.isValidCard(cardId)) {
						Toast.makeText(UserLogin.this, R.string.input_card_id_error, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		
		nameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String name = nameEdit.getText().toString();
					if (!CheckUtil.isValidName(name)) {
						Toast.makeText(UserLogin.this, R.string.input_name_error, Toast.LENGTH_LONG).show();
					}
				}
			}
		});
		/**add by yejc 20130424 end*/
		// 绑定手机号复选框
		// CheckBox check = (CheckBox) findViewById(R.id.user_register_check);
		// check.setButtonDrawable(R.drawable.check_select);
		// check.setChecked(false);
		// check.setOnCheckedChangeListener(new
		// CompoundButton.OnCheckedChangeListener() {
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,boolean
		// isChecked) {
		// if(isChecked){
		// isBindPhone = "1";//1是绑定，0是不绑定
		// }else{
		// isBindPhone = "0";
		// }
		// }
		// });
		// 服务协议复选框
		CheckBox checkProtocol = (CheckBox) findViewById(R.id.user_register_check_protocol);
		checkProtocol.setButtonDrawable(R.drawable.check_select);
		checkProtocol.setChecked(true);
		checkProtocol
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						isProtocol = isChecked;
					}
				});
		// 绑定身份证
		CheckBox checkId = (CheckBox) findViewById(R.id.user_register_check_card_id);
		checkId.setButtonDrawable(R.drawable.check_select);
		checkId.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ischeckId = isChecked;
				if (isChecked) {
					layoutId.setVisibility(LinearLayout.VISIBLE);
				} else {
					cardIdEdit.setText("");
					nameEdit.setText("");
					layoutId.setVisibility(LinearLayout.GONE);
				}
			}
		});
		// 输入推荐人
		CheckBox referrer = (CheckBox) findViewById(R.id.user_register_check_referrer);
		referrer.setButtonDrawable(R.drawable.check_select);
		referrer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				ischeckReferrer = isChecked;
				if (ischeckReferrer) {
					layoutReferrer.setVisibility(LinearLayout.VISIBLE);
				} else {
					referrerEdit.setText("");
					layoutReferrer.setVisibility(LinearLayout.GONE);
				}
			}
		});
		// 合作协议
		TextView textProtocol = (TextView) findViewById(R.id.user_register_text_protocol);
		textProtocol.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DIALOG_PROTOCOL);
			}
		});
		phoneNumEdit.setFocusable(true);
		// InputMethodManager imm =
		// (InputMethodManager)UserLogin.this.getSystemService(INPUT_METHOD_SERVICE);
		// imm.showSoftInput(phoneNumEdit, 0);
		// phoneNumEdit.requestFocus();
		register_button.setBackgroundResource(R.drawable.loginselecter);
		register_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 首先判断注册时两次输入的密码是否一致 ，然后再进行注册
				String phoneNum = phoneNumEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				String confirmPassword = confirmPasswordEdit.getText()
						.toString();
				int passwordLength = password.length();
				String cardId = cardIdEdit.getText().toString();
				String referrer = referrerEdit.getText().toString();
				realName = nameEdit.getText().toString();
				if (phoneNum.length() != 11) {
					Toast.makeText(getBaseContext(), "手机号必须为11位！",
							Toast.LENGTH_LONG).show();
				} else if (passwordLength < 6 || passwordLength > 16) {
					Toast.makeText(getBaseContext(), "密码必须为6~16位！",
							Toast.LENGTH_LONG).show();
				} else if (!password.equals(confirmPassword)) {
					Toast.makeText(getBaseContext(), "两次密码输入不同！",
							Toast.LENGTH_LONG).show();
				} else if (!isProtocol) {
					Toast.makeText(getBaseContext(), "请勾选服务协议再注册！",
							Toast.LENGTH_LONG).show();
				} else if (ischeckId) {
					if (cardId.equals("") || cardId == null) {
						Toast.makeText(getBaseContext(), "身份证不能为空！",
								Toast.LENGTH_LONG).show();
//					} else if (!isCardId(cardId)) { //close by yejc 20130424
					} else if (!CheckUtil.isValidCard(cardId)) {  //add by yejc 20130424	 
						Toast.makeText(getBaseContext(), "您输入的身份证不正确，请重新输入！",
								Toast.LENGTH_LONG).show();
					} else if (realName.equals("") || realName == null) {
						Toast.makeText(getBaseContext(), "真实姓名不能为空！",
								Toast.LENGTH_LONG).show();
					} else if(!CheckUtil.isValidName(realName)) { //add by yejc 20130424
						Toast.makeText(UserLogin.this,
								R.string.input_name_error, Toast.LENGTH_LONG).show();
					} else {
						beginRegister(phoneNum, password, cardId, realName,
								null);
					}
				} else if (ischeckReferrer) {
					if (referrer.equals("") || referrer == null) {
						Toast.makeText(getBaseContext(), "推荐人不能为空！",
								Toast.LENGTH_LONG).show();
					} else {
						beginRegister(phoneNum, password, cardId, realName,
								referrer);
					}
				} else {
					beginRegister(phoneNum, password, cardId, realName, null);
				}
			}
		});

		Button login_return = (Button) findViewById(R.id.usercenter_btn_return);
		login_return.setBackgroundResource(R.drawable.returnselecter);
		login_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (turn) {
					finish();
				} else {
					turnToLogin();
				}

			}

		});

	}

	/**
	 * 判断是否是身份证
	 * 
	 * @return
	 */
	public boolean isCardId(String cardId) {
		boolean isRight = true;
		int cardIdLength = cardId.length();
		if (cardIdLength != 18 && cardIdLength != 15) {
			isRight = false;
		} else {
			String cardIdSubstring = cardId.substring(0, cardIdLength - 2);
			for (int i = 0; i < cardIdLength - 2; i++) {
				if (cardId.charAt(i) < '0' || cardId.charAt(i) > '9')
					isRight = false;
			}
			if (cardId.charAt(cardIdLength - 1) != 'x'
					&& cardId.charAt(cardIdLength - 1) != 'X'
					&& (cardId.charAt(cardIdLength - 1) > '9' || cardId
							.charAt(cardIdLength - 1) < '0')) {
				isRight = false;
			}

		}

		return isRight;
	}

	/**
	 * 根据身份证判断是否满十八岁
	 */
	public boolean isYear(String cardId) {
		boolean isRight = true;
		int cardIdLength = cardId.length();
		// 判断是否满18
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		if (cardIdLength == 15) {
			age = Integer.parseInt("19" + cardId.substring(6, 8));
		} else if (cardIdLength == 18) {
			age = Integer.parseInt(cardId.substring(6, 10));
		}
		if (year - age < 18) {
			isRight = false;
		}
		return isRight;
	}

	/**
	 * 当选中记住用户密码的按钮时
	 * 
	 * @param isChecked
	 */
	private void rem_password_num(boolean isChecked) {
		EditText password_edit = (EditText) findViewById(R.id.password_edit);
		String password_string = password_edit.getText().toString();
		if (isChecked) {
			shellRW.putStringValue("remPwd_checkBox", "true");
			shellRW.putStringValue("password", password_string);
		} else {
			shellRW.putStringValue("remPwd_checkBox", "false");
			shellRW.putStringValue("password", "");
		}
	}

	private void auto_login(boolean isChecked) {
		EditText password_edit = (EditText) findViewById(R.id.password_edit);
		String password_string = password_edit.getText().toString();
		if (isChecked) {
			shellRW.putStringValue("remPwd_checkBox", "true");
			shellRW.putBooleanValue(ShellRWConstants.AUTO_LOGIN, true);
			isAutoLogin = "1";
			shellRW.putStringValue("password", password_string);
		} else {
			isAutoLogin = "0";
			shellRW.putBooleanValue(ShellRWConstants.AUTO_LOGIN, false);
		}
	}

	// 点击注册，完成注册
	// 以下是注册时调用的代码
	// TODO Auto-generated method stub
	/**
	 * 注册联网操作
	 */
	private void beginRegister(final String phoneNum,
			final String partPassword, final String cardId, final String name,
			final String referrer) {
		showDialog(PROGRESS_VALUE);
		Thread regthread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// try{

				String re = RegisterInterface.getInstance().userregister(
						phoneNum, partPassword, cardId, name, referrer);
				JSONObject obj;
				String error_code = "0";
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if (error_code.equals("0000")) {
					shellRW.putStringValue("name", realName);
					phonenum = phoneNum;
					password = partPassword;
					shellRW.putStringValue("phonenum", phonenum);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else if (error_code.equals("0013")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("4444")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("000012")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("0")) {
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				}
			}

		});
		regthread.start();

	}

	/**
	 * 实现电话号码的记忆功能
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-23
		if (getinfo() == null) {
			SharedPreferences prefs = getPreferences(0);
			String restoredText = prefs.getString("phoneNumText", null);
			if (restoredText != null && !on) {
				phoneNum_edit.setText(restoredText,
						TextView.BufferType.EDITABLE);

				int selectionStart = prefs.getInt("selection-start", -1);
				int selectionEnd = prefs.getInt("selection-end", -1);
				if (selectionStart != -1 && selectionEnd != -1) {
					phoneNum_edit.setSelection(selectionStart, selectionEnd);
				}
			}
		}
		
		bindService(new Intent(IAlixPay.class.getName()), mAlixPayConnection,
				Context.BIND_AUTO_CREATE);
	}

	// sina 微博
	private void oauth() {

		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl(Constants.CONSUMER_URL);// 此处回调页内容应该替换为与appkey对应的应用回调页
		// 对应的应用回调页可在开发者登陆新浪微博开发平台之后，
		// 进入我的应用--应用详情--应用信息--高级信息--授权设置--应用回调页进行设置和查看，
		// 应用回调页不可为空
		weibo.authorize(UserLogin.this, new AuthDialogListener());
	}

	// sina weibo
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Uid = values.getString("uid");
			shellRW.putStringValue("token", token);
			shellRW.putStringValue("expires_in", expires_in);
			WeiboParameters parameters = new WeiboParameters();
			parameters.add("uid", Uid);
			parameters.add("access_token", token);
			getnickname(parameters);
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}
	}

	private void getnickname(final WeiboParameters p) {
		showDialog(PROGRESS_VALUE);
		Thread regthread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// try{

				String re = Utility.openUrl(context, Constants.sinaweibo,
						"GET", p, new Token());
				JSONObject obj;
				try {
					obj = new JSONObject(re);
					nickname = obj.getString("screen_name");
					Message msg = new Message();
					msg.what = 20;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					Message msg = new Message();
					msg.what = 20;
					nickname = "";
					handler.sendMessage(msg);
					e.printStackTrace();
				}

			}

		});
		regthread.start();

	}

	/**
	 * Any time we are paused we need to save away the current state, so it will
	 * be restored correctly when we are resumed.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-23
		if (getinfo() == null) {
			if (!on) {
				SharedPreferences.Editor editor = getPreferences(0).edit();
				editor.putString("phoneNumText", phoneNum_edit.getText()
						.toString());
				editor.putInt("selection-start",
						phoneNum_edit.getSelectionStart());
				editor.putInt("selection-end", phoneNum_edit.getSelectionEnd());
				editor.commit();
			}
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		String phoneNumber = phoneNum_edit.getText().toString();
		shellRW.putStringValue("phoneNumber", phoneNumber);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	// 记住手机号
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public static void main(String[] args) {
		String str = "abcd";
		System.out.println(str.indexOf("bc"));
	}

	private void registerIntentReceivers() {
		receiver = new AuthReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(TAuthView.AUTH_BROADCAST);
		registerReceiver(receiver, filter);
	}

	private void unregisterIntentReceivers() {
		unregisterReceiver(receiver);
	}

	/**
	 * 打开登录认证与授权页面
	 * 
	 * @param String
	 *            clientId 申请时分配的appid
	 * @param String
	 *            target 打开登录页面的方式：“_slef”以webview方式打开; "_blank"以内置安装的浏览器方式打开
	 * @author John.Meng<arzen1013@gmail> QQ:3440895
	 * @date 2011-9-5
	 */
	private void auth(String clientId, String target) {
		Intent intent = new Intent(UserLogin.this,
				com.tencent.tauth.TAuthView.class);
		intent.putExtra(TAuthView.CLIENT_ID, clientId);
		intent.putExtra(TAuthView.SCOPE, scope);
		intent.putExtra(TAuthView.TARGET, target);
		intent.putExtra(TAuthView.CALLBACK, CALLBACK);
		startActivity(intent);

	}

	/**
	 * 广播的侦听，授权完成后的回调是以广播的形式将结果返回
	 * 
	 * @author John.Meng<arzen1013@gmail> QQ:3440895
	 * @date 2011-9-5
	 */
	public class AuthReceiver extends BroadcastReceiver {

		private static final String TAG = "AuthReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle exts = intent.getExtras();
			String raw = exts.getString("raw");
			String access_token = exts.getString(TAuthView.ACCESS_TOKEN);
			String expires_in = exts.getString(TAuthView.EXPIRES_IN);
			String error_ret = exts.getString(TAuthView.ERROR_RET);
			String error_des = exts.getString(TAuthView.ERROR_DES);
			Log.i(TAG, String.format("raw: %s, access_token:%s, expires_in:%s",
					raw, access_token, expires_in));

			if (access_token != null) {
				mAccessToken = access_token;
				// ((TextView)findViewById(R.id.access_token)).setText(access_token);
				// TDebug.msg("正在获取OpenID...", getApplicationContext());
				Log.e("======", "===========");
				showDialog(PROGRESS);
				if (!isFinishing()) {

				}
				// 用access token 来获取open id
				TencentOpenAPI.openid(access_token, new Callback() {
					@Override
					public void onSuccess(final Object obj) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								dismissDialog(PROGRESS);
								// setOpenIdText(((OpenId)obj).getOpenId());
								// Uid=((OpenId)obj).getOpenId();
								mOpenId = ((OpenId) obj).getOpenId();
								getQQnickname();

							}
						});
					}

					@Override
					public void onFail(int ret, final String msg) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								UserLogin.this.dismissDialog(PROGRESS);
								mOpenId = "";
								TDebug.msg(msg, getApplicationContext());
							}
						});
					}
				});

			}

			if (error_ret != null) {
				// ((TextView)findViewById(R.id.access_token)).setText("获取access token失败"
				// + "\n错误码: " + error_ret + "\n错误信息: " + error_des);
			}
		}

	}

	public void getQQnickname() {
		if (!satisfyConditions()) {
			TDebug.msg("请先获取access token和open id", this);
			return;
		}
		showDialog(PROGRESS);
		TencentOpenAPI.userInfo(mAccessToken, mAppid, mOpenId, new Callback() {

			@Override
			public void onSuccess(final Object obj) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						dismissDialog(PROGRESS);
						i++;
						nickname = ((UserInfo) obj).getNickName();
						Log.e("用户信息", ((UserInfo) obj).getNickName() + "====="
								+ i);
						weiboToLogin("qq", mOpenId, nickname, packageName);
					}
				});
			}

			@Override
			public void onFail(final int ret, final String msg) {
				handler.post(new Runnable() {

					@Override
					public void run() {
						dismissDialog(PROGRESS);
						Log.e("用户信息", msg);
					}
				});
			}
		});
	}

	public boolean satisfyConditions() {
		return mAccessToken != null && mAppid != null && mOpenId != null
				&& !mAccessToken.equals("") && !mAppid.equals("")
				&& !mOpenId.equals("");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterIntentReceivers();
		}
	}
	
	IAlixPay mAlixPay = null;
	private ServiceConnection mAlixPayConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mAlixPay = IAlixPay.Stub.asInterface(service);
		}

		public void onServiceDisconnected(ComponentName className) {
			mAlixPay = null;
		}
	};
	
	private void pay() {
		final ProgressDialog pDialog = UserCenterDialog.onCreateDialog(context);
		pDialog.show();
		new Thread(new Runnable() {
			public void run() {
				String alipaySign = Controller.getInstance(context).getAlipaySign();
				try {
					mAlixPay.Pay(alipaySign);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pDialog.dismiss();
			}
		}).start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(mAlixPayConnection);
	}
	
//	private boolean pay(final String alipaySign) {
//
//		if (mAlixPay == null) {
//			bindService(new Intent(IAlixPay.class.getName()),
//					mAlixPayConnection, Context.BIND_AUTO_CREATE);
//		}
//
//		new Thread(new Runnable() {
//			public void run() {
//				try {
//					mAlixPay.Pay(alipaySign);
//					unbindService(mAlixPayConnection);
//					pDialog.dismiss();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//
//		return true;
//	}
}
