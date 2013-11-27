package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RechargeMoneyTextWatcher;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 支付宝充值
 * 
 * @author win
 * 
 */
public class AlipaySecureActivity extends Activity implements HandlerMsg {
	public ProgressDialog progressdialog;
	Button secureOk, secureCancel;
	EditText accountnum;
	private WebView alipay_content = null;
	private boolean isWebView = false;// 浏览器打开支付宝
	private String sessionId = "";
	private String phonenum = "";
	private String userno = "";
	private String message = "";
	private MyHandler handler = new MyHandler(this);
	private String url = "";
	private TextView accountTitleTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		initTextViewContent();
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText("支付宝充值");
		PublicMethod.setTextViewContent(this);
		/**add by yejc 20130718 start*/
		Button button = (Button) findViewById(R.id.alipay_secure_ok);
		button.setVisibility(View.GONE);
		LinearLayout layout = (LinearLayout) findViewById(R.id.alipay_secure_ok_alipay_layout);
		layout.setVisibility(View.VISIBLE);
		/**add by yejc 20130718 end*/
		secureOk = (Button) findViewById(R.id.alipay_secure_ok_alipay);
		secureCancel = (Button) findViewById(R.id.alipay_secure_cancel);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		/**add by yejc 20130802 start*/
		accountnum.addTextChangedListener(new RechargeMoneyTextWatcher(accountnum));
		/**add by yejc 20130802 end*/
		secureOk.setText("wap支付");
		secureCancel.setText("浏览器支付");
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isWebView = true;
				beginAlipayRecharge(accountnum);
			}
		});
		secureCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// RechargeType.dismiss();
				isWebView = false;
				beginAlipayRecharge(accountnum);
			}
		});
	}

	private void initTextViewContent() {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		alipay_content = (WebView) findViewById(R.id.alipay_content);
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = getJSONByLotno();
				try {
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
//							alipay_content.setText(Html.fromHtml(conten));
//							alipay_content.loadData(conten, "text/html", "UTF-8");
							alipay_content.loadDataWithBaseURL("", conten, "text/html", "UTF-8", "");
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
				.rechargeDescribe("zfbChargeDescriptionHtml");
		return jsonObjectByLotno;
	}


	// 支付宝充值
	private void beginAlipayRecharge(View Vi) {// isWebView = false代表浏览器联网
		String zfb_recharge_value_string = accountnum.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(
				AlipaySecureActivity.this, "addInfo");
		String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
		phonenum = pre.getStringValue(ShellRWConstants.PHONENUM);

		if (sessionIdStr.equals("") || sessionIdStr == null) {
			Intent intentSession = new Intent(AlipaySecureActivity.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			if (!PublicMethod.isRecharge(zfb_recharge_value_string, this)) {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo = new RechargePojo();
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype("05");
				rechargepojo.setCardtype("0300");

				if (isWebView) {
					rechargepojo.setBankAccount("4");// 4支付宝wap支付
				} else {
					rechargepojo.setBankAccount("3");// 3支付宝浏览器支付
				}
				recharge(rechargepojo);
			}
		}
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(
				AlipaySecureActivity.this, "addInfo");
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
		showDialog(0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String error_code = "00";
				message = "";
				// TODO Auto-generated method stub
				try {
					rechargepojo.setSessionid(sessionId);
					rechargepojo.setUserno(userno);
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);

					JSONObject obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
					if (error_code.equals("0000")) {
						url = obj.getString("return_url");
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
		// TODO Auto-generated method stub
		if (isWebView) {
			Intent intent = new Intent(AlipaySecureActivity.this,
					AccountDialog.class);
			intent.putExtra("accounturl", url);
			intent.putExtra("isDirectPay", false);
			startActivity(intent);
		} else {
			PublicMethod.openUrlByString(AlipaySecureActivity.this, url);
		}
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
}