package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RechargeMoneyTextWatcher;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryDNAInterface;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 银联语音充值
 * 
 * @author win
 * 
 */
public class YinDNAPayActivity extends Activity implements HandlerMsg {
	private String RECHARGTYPE = "0";
	private String phonenum = "";
	private String re;
	private MyHandler handler = new MyHandler(this);
	private ProgressDialog progressdialog;
	private String sessionId = "";
	private String userno = "";
	private String message = "";
	private String[] bankNames = { "中国工商银行", "中国农业银行", "中国建设银行", "招商银行",
			"中国邮政储蓄银行", "华夏银行", "兴业银行", "中信银行", "中国光大银行", "上海浦东发展银行", "深圳发展银行" };
	private String strExpand;
	EditText bankCardRechargeValue;
	EditText bankCardRechargeValueNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.account_bank_card_phone_online_dialog);
		checkDNA();
	}

	@Override
	public void errorCode_0000() {
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * DNA充值账户绑定查询
	 */
	public void checkDNA() {
		progressdialog = onCreateDialog(0);
		RECHARGTYPE = "01";// 为chenckDNA
		RWSharedPreferences pre = new RWSharedPreferences(
				YinDNAPayActivity.this, "addInfo");
		final String sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		final String mobile = pre.getStringValue(ShellRWConstants.MOBILEID);
		phonenum = mobile;
		final String userno = pre.getStringValue("userno");
		if (sessionId == null || sessionId.equals("")) {
			Intent intentSession = new Intent(YinDNAPayActivity.this,
					UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			showDialog(0);
			new Thread(new Runnable() {
				@Override
				public void run() {
					String error_code = "00";
					String message = "";
					try {
						re = QueryDNAInterface.getInstance().queryDNA(mobile,
								sessionId, userno);
						JSONObject obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						message = obj.getString("message");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (error_code.equals("0047")) {
						handler.post(new Runnable() {
							public void run() {
								createBankCardPhoneDialogNo();
							}
						});
					} else if (error_code.equals("0000")) {
						handler.post(new Runnable() {
							public void run() {
								dialogDNA(re);
							}
						});
					} else {
						handler.handleMsg(error_code, message);
					}
					progressdialog.dismiss();
				}
			}).start();
		}
	}

	/**
	 * 创建dna对话框
	 * 
	 * @版本：
	 */
	private String name = "";
	private String certid = "";
	private String bankAddress = "";
	private String certAddress = "";
	private String bankCardNo = "";
	private String bindPhone = "";
	private String bankName = "";

	public void dialogDNA(String str) {
		String bindState = "";
		View view = null;
		try {
			JSONObject obj = new JSONObject(str);
			bindState = obj.getString("bindstate");
			bankCardNo = obj.getString("bankcardno");
			name = obj.getString("name");
			certid = obj.getString("certid");
			bankAddress = obj.getString("bankaddress");
			certAddress = obj.getString("addressname");
			bindPhone = obj.getString("phonenum");
			bankName = obj.getString("bankname");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bindState.equals("1")) {// 已经绑定
			createBankCardPhoneDialog();
		} else if (bindState.equals("0")) {
			createBankCardPhoneDialogNo();
		}
	}

	LayoutInflater factory = null;
	// private Dialog RechargeType=null;
	EditText bank_card_phone_bankid;
	EditText bank_card_phone_phone_num;
	EditText bank_card_phone_name;
	RWSharedPreferences shellRW;

	// 银行卡电话充值弹出框(DNA)
	protected void createBankCardPhoneDialog() {
		RECHARGTYPE = "01";
		this.setContentView(R.layout.account_bank_card_phone_online_dialog);
		/**add by yejc 20130802 start*/
		bankCardRechargeValue = (EditText) findViewById(R.id.bank_card_phone_recharge_value);
		bankCardRechargeValue.addTextChangedListener(new RechargeMoneyTextWatcher(bankCardRechargeValue));
		/**add by yejc 20130802 end*/
		bank_card_phone_bankid = (EditText) findViewById(R.id.bank_card_phone_bankid);
		bank_card_phone_bankid.setText(bankCardNo);
		bank_card_phone_phone_num = (EditText) findViewById(R.id.bank_card_phone_phone_num);// 手机号
		bank_card_phone_phone_num.setText(bindPhone);
		bank_card_phone_bankid.setEnabled(false);
		WebView textContent = (WebView) findViewById(R.id.alipay_content);
		initTextViewContent(textContent);
		final Button ok = (Button) findViewById(R.id.alipay_secure_ok);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(
						YinDNAPayActivity.this, "addInfo");
				String sessionIdStr = pre
						.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr == null || sessionIdStr.equals("")) {
					Intent intentSession = new Intent(YinDNAPayActivity.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					// 银行卡语音充值网络连接
					beiginBankCardPhoneOnline();
				}
			}
		});
	}

	// 银行卡语音充值(DNA)
	private void beiginBankCardPhoneOnline() {
		final String rechargevalue = bankCardRechargeValue.getText()
				.toString();
		EditText bank_card_phone_phone_num = (EditText) findViewById(R.id.bank_card_phone_phone_num);
		final String cardphonenum = bank_card_phone_phone_num.getText()
				.toString();
		final String cardid = bank_card_phone_bankid.getText().toString();
		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
		// ；
		if ((!(cardphonenum.equals("")) && cardphonenum != null)
				&& (!(rechargevalue.equals("")) && rechargevalue != null)
				&& (!(cardid.equals("")) && cardid != null)) {
			if (cardphonenum.length() == 11) {
				if (Integer.parseInt(rechargevalue) >= 20) {
					String bank_card_id = cardid;
					RechargePojo rechargepojo = new RechargePojo();
					rechargepojo.setAmount(rechargevalue);
					rechargepojo.setCardno(bank_card_id);
					rechargepojo.setCardtype("0101");
					rechargepojo.setRechargetype(RECHARGTYPE);
					rechargepojo.setIswhite("true");
					rechargepojo.setPhonenum(cardphonenum);
					rechargepojo.setBankName(bankName);
					recharge(rechargepojo);
				} else {
					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 网络连接提示框
	 */
	protected ProgressDialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
		}
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		return null;
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(
				YinDNAPayActivity.this, "addInfo");
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

		showDialog(0);
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
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(YinDNAPayActivity.this, message,
										Toast.LENGTH_SHORT).show();
							}
						});
						YinDNAPayActivity.this.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (error_code.equals("001400")) {
					handler.post(new Runnable() {
						public void run() {
							createBankPhoneCardRegisterDialog().show();
						}
					});
				} else {
					handler.handleMsg(error_code, message);
				}
				progressdialog.dismiss();
			}
		}).start();
	}

	private void initTextViewContent(final WebView alipay_content) {
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

	// 银行卡电话充值弹出框(DNA)（未绑定 ）
	protected void createBankCardPhoneDialogNo() {
		RECHARGTYPE = "01";
		factory = LayoutInflater.from(this);
		RWSharedPreferences pre = new RWSharedPreferences(
				YinDNAPayActivity.this, "addInfo");
		this.setContentView(R.layout.account_bank_card_phone_dialog);
		PublicMethod.setTextViewContent(this); //add by yejc 20130718
		/**add by yejc 20130802 start*/
		bankCardRechargeValueNo = (EditText) findViewById(R.id.bank_card_phone_recharge_value);// 金额
		bankCardRechargeValueNo.addTextChangedListener(new RechargeMoneyTextWatcher(bankCardRechargeValueNo));
		/**add by yejc 20130802 end*/
		String phonenum = pre.getStringValue(ShellRWConstants.MOBILEID);
		bank_card_phone_phone_num = (EditText) findViewById(R.id.bank_card_phone_phone_num);// 手机号
		bank_card_phone_name = (EditText) findViewById(R.id.bank_card_phone_phone_name);// 姓名
		if ("".equals(name.trim())) {
			bank_card_phone_name.setText(name.trim());
		}
		if ("".equals(phonenum.trim())) {
			bank_card_phone_phone_num.setText(phonenum.trim());
		}
		
		EditText bank_card_phone_bankid = (EditText) findViewById(R.id.bank_card_phone_bankid);// 银行卡号
		EditText bank_card_phone_idcard = (EditText) findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
		EditText bank_card_phone_home = (EditText) findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
		EditText bank_card_phone_province = (EditText) findViewById(R.id.bank_card_phone_phone_province);// 所在省
		WebView YinDNAtext_content = (WebView) findViewById(R.id.alipay_content);
		initTextViewContent(YinDNAtext_content);
		final Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, bankNames);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = spinner.getSelectedItemPosition();
				bankName = bankNames[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		spinner.setSelection(0);
		if ("".equals(bankCardNo.trim())) {
			bank_card_phone_bankid.setText(bankCardNo.trim());
		}
		if ("".equals(certid.trim())) {
			bank_card_phone_idcard.setText(certid.trim());
		}
		if ("".equals(certAddress.trim())) {
			bank_card_phone_home.setText(certAddress.trim());
		}
		if ("".equals(bankAddress.trim())) {
			bank_card_phone_province.setText(bankAddress.trim());
		}
		
		final Button ok = (Button) findViewById(R.id.alipay_secure_ok);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(
						YinDNAPayActivity.this, "addInfo");
				String sessionIdStr = pre
						.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr != null && sessionIdStr.equals("")) {
					Intent intentSession = new Intent(YinDNAPayActivity.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					beiginBankCardPhoneNo(bankName);
				}
			}
		});
	}

	// 银行卡语音充值(未绑定)DNA
	private void beiginBankCardPhoneNo(String bankName) {
		final String value = bankCardRechargeValueNo.getText()
				.toString();
		EditText bank_card_phone_bankid = (EditText) findViewById(R.id.bank_card_phone_bankid);// 银行卡号
		final String bankid = bank_card_phone_bankid.getText().toString();
		final String name = bank_card_phone_name.getText().toString();
		EditText bank_card_phone_idcard = (EditText) findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
		final String idcard = bank_card_phone_idcard.getText().toString();
		EditText bank_card_phone_home = (EditText) findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
		final String home = bank_card_phone_home.getText().toString();
		EditText bank_card_phone_province = (EditText) findViewById(R.id.bank_card_phone_phone_province);// 所在省
		final String province = bank_card_phone_province.getText().toString();
		final String num = bank_card_phone_phone_num.getText().toString();

		// 银行卡语音充值网络连接
		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
		// ui还缺少银行卡号
		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
		if ((!(num.equals("")) && num != null)
				&& (!(value.equals("")) && value != null)
				&& (!(bankid.equals("")) && bankid != null)
				&& (!(name.equals("")) && name != null)
				&& (!(idcard.equals("")) && idcard != null)
				&& (!(home.equals("")) && home != null)
				&& (!(province.equals("")) && province != null)) {
			if (num.length() == 11) {
				if (Integer.parseInt(value) >= 20) {
					String acceptphonenum = num;
					strExpand = name + "," + idcard + "," + province + ","
							+ home + " ," + acceptphonenum + ",false";

					String bank_card_id = bankid;
					RechargePojo rechargepojo = new RechargePojo();
					;
					rechargepojo.setAmount(value);
					rechargepojo.setCardno(bank_card_id);
					rechargepojo.setCardtype("0101");
					rechargepojo.setRechargetype(RECHARGTYPE);
					rechargepojo.setName(name);
					rechargepojo.setCertid(idcard);
					rechargepojo.setAddressname(home);
					rechargepojo.setPhonenum(acceptphonenum);
					rechargepojo.setBankaddress(province);
					rechargepojo.setIswhite("false");
					rechargepojo.setBankName(bankName);
					recharge(rechargepojo);
					bankCardRechargeValueNo.setText("");
					bank_card_phone_phone_num.setText("");
					bank_card_phone_bankid.setText("");
				} else {
					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
		}
	}

	private Dialog RechargeType = null;

	// 第一次DNA充值绑定弹出框
	protected Dialog createBankPhoneCardRegisterDialog() {
		RECHARGTYPE = "01";//
		final View phone_bank_card_view = factory.inflate(
				R.layout.account_bank_card_phone_register_dialog, null);
		RechargeType = new AlertDialog.Builder(this)
				.setTitle(R.string.bank_cards_recharge)
				.setView(phone_bank_card_view)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								beginRegisterBankPhoneCard(phone_bank_card_view);
								/* User clicked OK so do some stuff */
							}
						}).setNegativeButton(R.string.cancel, null).create();
		return RechargeType;
	}

	/**
	 * 开始注册银行电话卡
	 * 
	 * @param vi
	 */
	private void beginRegisterBankPhoneCard(View vi) {

		RWSharedPreferences shellRW = new RWSharedPreferences(
				YinDNAPayActivity.this, "addInfo");
		final String phonenum = shellRW.getStringValue("mobileid");
		final String sessionid = shellRW.getStringValue("sessionid");
		// 得到真实姓名、身份证号、开户银行所在地、开户银行所在地、银行卡号、金额、手机号 20100711
		EditText bank_card_phone_username = (EditText) vi
				.findViewById(R.id.bank_card_phone_user_name);
		final String bank_card_phone_username_string = bank_card_phone_username
				.getText().toString();

		EditText bank_card_phone_user_idnum = (EditText) vi
				.findViewById(R.id.bank_card_phone_user_idnum);
		final String bank_card_phone_user_idnum_string = bank_card_phone_user_idnum
				.getText().toString();

		EditText bank_card_phone_open_bank = (EditText) vi
				.findViewById(R.id.bank_card_phone_open_bank);
		final String bank_card_phone_open_bank_string = bank_card_phone_open_bank
				.getText().toString();

		EditText bank_card_phone_open_bankuser_address = (EditText) vi
				.findViewById(R.id.bank_card_phone_open_bankuser_address);
		final String bank_card_phone_open_bankuser_address_string = bank_card_phone_open_bankuser_address
				.getText().toString();

		EditText bank_card_phone_bankid = (EditText) vi
				.findViewById(R.id.bank_card_phone_bankid);
		final String bank_card_phone_bankid_string = bank_card_phone_bankid
				.getText().toString();

		EditText bank_card_phone_recharge_value = (EditText) vi
				.findViewById(R.id.bank_card_phone_recharge_value);
		final String bank_card_phone_recharge_value_string = bank_card_phone_recharge_value
				.getText().toString();

		EditText bank_card_phone_phone_num = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_num);
		final String bank_card_phone_phone_num_string = bank_card_phone_phone_num
				.getText().toString();

		strExpand = bank_card_phone_username_string + ","
				+ bank_card_phone_user_idnum_string + ","
				+ bank_card_phone_open_bank_string + ","
				+ bank_card_phone_open_bankuser_address_string + ","
				+ bank_card_phone_phone_num_string + ",false";

		String bank_card_id = bank_card_phone_bankid_string;
		RechargePojo rechargepojo = new RechargePojo();
		;
		rechargepojo.setName(bank_card_phone_username_string);
		rechargepojo.setCertid(bank_card_phone_user_idnum_string);
		rechargepojo.setCardtype("0101");
		rechargepojo
				.setBankaddress(bank_card_phone_open_bankuser_address_string);
		rechargepojo.setPhonenum(bank_card_phone_phone_num_string);
		rechargepojo.setAmount(bank_card_phone_recharge_value_string);
		rechargepojo.setCertid(bank_card_id);
		rechargepojo.setRechargetype(RECHARGTYPE);
		recharge(rechargepojo);
	}

	private static JSONObject getJSONByLotno() {
		JSONObject jsonObjectByLotno = RechargeDescribeInterface.getInstance()
				.rechargeDescribe("dnaChargeDescriptionHtml");
		return jsonObjectByLotno;
	}
}