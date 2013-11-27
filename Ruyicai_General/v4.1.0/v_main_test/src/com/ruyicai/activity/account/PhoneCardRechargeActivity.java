package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
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
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class PhoneCardRechargeActivity extends Activity implements HandlerMsg {
	private String RECHARGTYPE = "0";
	private String phonenum = "";
	private String re;
	private MyHandler handler;
	private ProgressDialog progressdialog;
	private String sessionId = "";
	private String userno = "";
	private String message = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.account_phone_cards_recharge_dialog);
		handler = new MyHandler(this);
		progressdialog = UserCenterDialog.onCreateDialog(this);
		createPhoneRechargeCardDialog();
		
		/**add by yejc 20130419 start*/
		if(Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getName(), "onCreate");
		}
		/**add by yejc 20130419 end*/
		
		PublicMethod.setTextViewContent(this); //add by yejc 20130718

	}

	String[] values = { "10", "20", "30", "50", "100", "200", "300", "500" };
	private String phoneCardType = "0206";
	private String phoneCardValue = "100";

	// 电话卡充值弹出框
	protected void createPhoneRechargeCardDialog() {
		RECHARGTYPE = "02";
//		TextView contentText = (TextView) findViewById(R.id.alipay_content);
//		contentText.setText(Html.fromHtml(initTextViewContent()));
		WebView alipay_content = (WebView) findViewById(R.id.alipay_content);
		alipay_content.loadDataWithBaseURL("", initTextViewContent(), "text/html", "UTF-8", "");

		final Spinner phone_card_value_spinner = (Spinner) findViewById(R.id.phone_card_value_spinner);
		phone_card_value_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						int position = phone_card_value_spinner
								.getSelectedItemPosition();
						if (position > 0) {
							for (int i = 0; i < values.length; i++) {
								phoneCardValue = values[position - 1];
							}
						} else {
							phoneCardValue = null;
						}

					}

					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
					}
				});
		final Button ok = (Button) findViewById(R.id.alipay_secure_ok);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(
						PhoneCardRechargeActivity.this, "addInfo");
				String sessionIdStr = pre
						.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr != null && sessionIdStr.equals("")) {
					Intent intentSession = new Intent(
							PhoneCardRechargeActivity.this, UserLogin.class);
					startActivity(intentSession);
				} else {
					if (phoneCardType == null) {
						Toast.makeText(PhoneCardRechargeActivity.this,
								"请选择运营商", Toast.LENGTH_SHORT).show();
					} else if (phoneCardValue == null) {
						Toast.makeText(PhoneCardRechargeActivity.this,
								"请选择充值金额", Toast.LENGTH_SHORT).show();
					} else {
						beginPhoneCardRecharge();
					}
				}

			}
		});
	}

	private String initTextViewContent() {
		String conten = null;
		JSONObject jsonObject = getJSONByLotno();
		try {
			conten = jsonObject.get("content").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return conten;
	}

	private static JSONObject getJSONByLotno() {
		JSONObject jsonObjectByLotno = RechargeDescribeInterface.getInstance()
				.rechargeDescribe("phoneCardChargeDescriptionHtml");
		return jsonObjectByLotno;
	}

	/**
	 * 手机卡充值
	 * 
	 * @param view
	 */
	private void beginPhoneCardRecharge() {

		final EditText phone_card_rechargecard_info = (EditText) findViewById(R.id.phone_card_rechargecard_info);
		final String phone_card_rechargecard_info_string = phone_card_rechargecard_info
				.getText().toString();
		final EditText phone_card_rechargecard_password = (EditText) findViewById(R.id.phone_card_rechargecard_password);
		final String phone_card_rechargecard_password_string = phone_card_rechargecard_password
				.getText().toString();
		// 手机充值卡充值
		// 参数含义：0203 充值卡类型 10 充值钱数 5000充值总额 10623260104706723 充值卡号
		// 261324590869999653 充值密码 y00003银行标识默认
		// ui缺少充值的钱数
		if ((!(phone_card_rechargecard_info_string.equals("")) && phone_card_rechargecard_info_string != null)
				&& (!(phone_card_rechargecard_password_string.equals("")) && phone_card_rechargecard_password_string != null)) {
			if (isCardString(phone_card_rechargecard_info_string)) {
				// 改为线程 陈晨 200/7/9
				RechargePojo rechargepojo = new RechargePojo();
				;
				rechargepojo.setRechargetype(RECHARGTYPE);
				rechargepojo.setCardtype(phoneCardType);
				rechargepojo.setAmount(phoneCardValue);
				rechargepojo.setCardno(phone_card_rechargecard_info_string);
				rechargepojo
						.setCardpwd(phone_card_rechargecard_password_string);
				recharge(rechargepojo);
			} else {
				Toast.makeText(this, "充值卡序列号应为数字或字母！", Toast.LENGTH_LONG)
						.show();
			}
		} else
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(
				PhoneCardRechargeActivity.this, "addInfo");
		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		phonenum = pre.getStringValue(ShellRWConstants.MOBILEID);
		userno = pre.getStringValue(ShellRWConstants.USERNO);
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (RECHARGTYPE.equals("05")) {
			if (info.getExtraInfo() != null
					&& info.getExtraInfo().equalsIgnoreCase("3gwap")) {
				Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！",
						Toast.LENGTH_LONG).show();
			}
		}
		progressdialog.show();
		showDialog(0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String error_code = "00";
				try {
					rechargepojo.setSessionid(sessionId);
					rechargepojo.setUserno(userno);
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);
					JSONObject obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
					handler.handleMsg(error_code, message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}
		}).start();
	}

	@Override
	public void errorCode_0000() {
		Toast.makeText(PhoneCardRechargeActivity.this, message,
				Toast.LENGTH_SHORT).show();
		PhoneCardRechargeActivity.this.finish();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	private boolean isCardString(String cardNumber) {
		int length = cardNumber.length();
		boolean isRight = true;
		for (int i = 0; i < length - 1; i++) {
			if (cardNumber.charAt(i) < '0'
					|| (cardNumber.charAt(i) > '9' && cardNumber.charAt(i) < 'A')
					|| (cardNumber.charAt(i) > 'Z' && cardNumber.charAt(i) < 'a')
					|| (cardNumber.charAt(i) > 'z')) {
				isRight = false;
			}
		}
		return isRight;
	}
}