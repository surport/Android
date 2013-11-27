package com.ruyicai.activity.account;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RechargeMoneyTextWatcher;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 银联充值(免手续费)
 * 
 * @author win
 * 
 */
public class YinPayActivity extends Activity implements HandlerMsg {
	private String tn = null;
	public ProgressDialog progressdialog;
	private final String YINTYPE = "1001";
	Button secureOk;
	EditText accountnum;
	// private TextView alipay_content = null;
	private WebView alipay_content = null;
	private String sessionId = "";
	private String phonenum = "";
	private String userno = "";
	private String message = "";
	private MyHandler handler = new MyHandler(this);
	private TextView accountTitleTextView = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		initTextViewContent();

		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText("银联卡充值");

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		/** add by yejc 20130802 start */
		accountnum.addTextChangedListener(new RechargeMoneyTextWatcher(
				accountnum));
		/** add by yejc 20130802 end */
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MobclickAgent.onEvent(YinPayActivity.this, "chongzhi ");// BY贺思明
																		// 2012-6-29
				beginYinpayRecharge(v);
			}
		});
		PublicMethod.setTextViewContent(this); // add by yejc 20130718
	}

	private void initTextViewContent() {
		alipay_content = (WebView) findViewById(R.id.alipay_content);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject jsonObject = getJSONByLotno();
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
							// alipay_content.setText(Html.fromHtml(conten));
							// alipay_content.loadData(conten,
							// "text/html; charset=UTF-8", null);
							alipay_content.loadDataWithBaseURL("", conten,
									"text/html", "UTF-8", "");
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	private static JSONObject getJSONByLotno() {
		JSONObject jsonObjectByLotno = RechargeDescribeInterface.getInstance()
				.rechargeDescribe("lthjChargeDescriptionHtml");
		return jsonObjectByLotno;
	}

	// 银联充值
	private void beginYinpayRecharge(View Vi) {
		String zfb_recharge_value_string = accountnum.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(YinPayActivity.this,
				"addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(YinPayActivity.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			if (!PublicMethod.isRecharge(zfb_recharge_value_string, this)) {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo = new RechargePojo();
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype("15");
				rechargepojo.setCardtype(YINTYPE);
				recharge(rechargepojo);
			}
		}
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(YinPayActivity.this,
				"addInfo");
		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		phonenum = pre.getStringValue(ShellRWConstants.MOBILEID);
		userno = pre.getStringValue(ShellRWConstants.USERNO);
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (info.getExtraInfo() != null
				&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
			Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
					Toast.LENGTH_LONG).show();
		}

		progressdialog = UserCenterDialog.onCreateDialog(YinPayActivity.this);
		progressdialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String error_code = "00";
				message = "";
				try {
					rechargepojo.setSessionid(sessionId);
					rechargepojo.setUserno(userno);
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);

					JSONObject obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
					if (error_code.equals("0000")) {
						tn = obj.getString("value");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				handler.handleMsg(error_code, message);
				progressdialog.dismiss();
			}
		}).start();

	}

	@Override
	public void errorCode_0000() {
		turnYinView();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	// 启动插件(进入支付页面)
	public final static String CMD_PAY_PLUGIN = "cmd_pay_plugin";

	/**
	 * 银联充值跳转到插件
	 */
	public void turnYinView() {
		// 向插件提交3要素报文
		// *********************************************************************************//
		if (tn == null || tn.length() == 0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("错误提示");
			builder.setMessage("网络连接失败,请重试!");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		} else {
			/*************************************************
			 * 
			 * 步骤2：通过银联工具类启动支付插件
			 * 
			 ************************************************/
			// mMode参数解释：
			// 0 - 启动银联正式环境info
			// 1 - 连接银联测试环境
			UPPayAssistEx.startPayByJAR(YinPayActivity.this, PayActivity.class,
					null, null, tn, Constants.BANCK_Mode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 * 
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}