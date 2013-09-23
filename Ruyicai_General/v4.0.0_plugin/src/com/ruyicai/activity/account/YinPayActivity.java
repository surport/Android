package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.umeng.analytics.MobclickAgent;
import com.unionpay.upomp.lthj.util.PluginHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
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
	private String xml = "";
	public ProgressDialog progressdialog;
	private final String YINTYPE = "0900";
	Button secureOk;
	EditText accountnum;
//	private TextView alipay_content = null;
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
		/**add by yejc 20130802 start*/
		accountnum.addTextChangedListener(new RechargeMoneyTextWatcher(accountnum));
		/**add by yejc 20130802 end*/
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MobclickAgent.onEvent(YinPayActivity.this, "chongzhi ");// BY贺思明
																		// 2012-6-29
				beginYinpayRecharge(v);
			}
		});
		PublicMethod.setTextViewContent(this); //add by yejc 20130718
	}

	private void initTextViewContent() {
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
//							alipay_content.loadData(conten, "text/html; charset=UTF-8", null);
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
				rechargepojo.setRechargetype("06");
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
						xml = obj.getString("value");
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
		turnYinView(xml);
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
	public void turnYinView(String info) {
		// 向插件提交3要素报文
		// *********************************************************************************//

		byte[] to_upomp = info.getBytes();

		Bundle mbundle = new Bundle();
		// to_upomp为商户提交的XML
		mbundle.putByteArray("xml", to_upomp);
		// 注：此处的action是：商户的action
		mbundle.putString("action_cmd", CMD_PAY_PLUGIN);

		PluginHelper.LaunchPlugin(this, mbundle);
	}
}