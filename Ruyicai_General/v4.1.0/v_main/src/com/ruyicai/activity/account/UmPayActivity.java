package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.secure.AlipaySecurePayDialog;
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
import com.umpay.creditcard.android.UmpayActivity;

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
 * 联动优势充值
 * 
 * @author win
 * 
 */
public class UmPayActivity extends Activity implements HandlerMsg {
	public ProgressDialog progressdialog;
	private final String YINTYPE = "0900";
	Button secureOk;
	EditText accountnum;
//	private TextView alipay_content = null;
	private WebView alipay_content = null;
	private String sessionId = "";
	private String userno = "";
	private String message = "";
	private MyHandler handler = new MyHandler(this);
	private TextView accountTitleTextView = null;
	private final int REQUESTCODE = 1;
	private final int CREDIT_CARD_RECHARGE = 1; //信用卡充值
	private final int DEBIT_CARD_RECHARGE = 8; //借记卡充值
	private String orderId = "";
	private boolean isUmPay = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		initTextViewContent();

		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText(R.string.umpay_recharge);

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		/**add by yejc 20130802 start*/
		accountnum.addTextChangedListener(new RechargeMoneyTextWatcher(accountnum));
		/**add by yejc 20130802 end*/
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MobclickAgent.onEvent(UmPayActivity.this, "chongzhi ");
				beginUmpayRecharge(v);
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
				.rechargeDescribe("umpayChargeDescriptionHtml");
		return jsonObjectByLotno;
	}

	// 联动优势充值
	private void beginUmpayRecharge(View Vi) {
		String umPayRechargeValue = accountnum.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(UmPayActivity.this,
				"addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(UmPayActivity.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			if (!PublicMethod.isRecharge(umPayRechargeValue, this)) {
				RechargePojo rechargepojo = new RechargePojo();
				rechargepojo.setAmount(umPayRechargeValue);
				rechargepojo.setRechargetype("11");
				rechargepojo.setCardtype(YINTYPE);
				/**add by yejc 20130527 start*/
				if (isUmPay) {
					rechargepojo.setBankId("ump003");
				} else {
					rechargepojo.setBankId("ump001");
				}
				/**add by yejc 20130527 end*/
				
				recharge(rechargepojo);
			}
		}
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(UmPayActivity.this,
				"addInfo");
		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		userno = pre.getStringValue(ShellRWConstants.USERNO);
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (info.getExtraInfo() != null
				&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
			Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
					Toast.LENGTH_LONG).show();
		}
		progressdialog = UserCenterDialog.onCreateDialog(UmPayActivity.this);
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
						orderId = obj.getString("orderId");
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
		turnUMPayView();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * 银联充值跳转到插件
	 */
	public void turnUMPayView() {
		Intent intent = new Intent(this, UmpayActivity.class);
		intent.putExtra("tradNo", orderId);
        intent.putExtra("payType", CREDIT_CARD_RECHARGE + DEBIT_CARD_RECHARGE);
        /**add by umpay start*/
        intent.putExtra("channelId", Constants.UMPAY_CHANNEL_ID);
        /**add by umpay end*/
        startActivityForResult(intent, REQUESTCODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			String message = data.getStringExtra("resultMessage");//支付结果描述
//			String result = data.getStringExtra("resultCode");//支付结果
			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		}
	}
}