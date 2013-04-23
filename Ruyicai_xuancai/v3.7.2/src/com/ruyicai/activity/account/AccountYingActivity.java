package com.ruyicai.activity.account;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.alipay.android.secure.AlixId;
import com.alipay.android.secure.BaseHelper;
import com.alipay.android.secure.MobileSecurePayHelper;
import com.alipay.android.secure.MobileSecurePayer;
import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.AccountRechargeInterface;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.AlipaySecurePayInterface;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.unionpay.upomp.lthj.util.PluginHelper;

/**
 * 银行充值
 * 
 * @author Administrator
 * 
 */
public class AccountYingActivity extends Activity implements OnClickListener {
	Button secureOk, secureCancel;
	EditText accountnum;
	public ProgressDialog progressdialog;
	private TextView textView;
	private ProgressDialog mProgress = null;
	private boolean isOnClick = true;
	private boolean isAlipay = false;// true跳转到支付宝安全支付
	private final String TYPE = "08";
	private List allcountries = null;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_dialog);
		textView = (TextView) findViewById(R.id.yinhangPay_text);
		textView.setTextColor(Color.RED);
		initView();
		getAccountNet();
		initTextViewContent();
	}

	private void initTextViewContent() {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = getJSONByLotno();
				try {
					final String conten = jsonObject.get("content").toString();
					handler.post(new Runnable() {
						public void run() {
							textView.setText(conten);
						}
					});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();
	}

	private static JSONObject getJSONByLotno() {
		JSONObject jsonObjectByLotno = RechargeDescribeInterface.getInstance()
				.rechargeDescribe("bankChargeDescription");
		return jsonObjectByLotno;
	}

	private void initView() {
		secureOk = (Button) findViewById(R.id.ok);
		secureOk.setOnClickListener(this);
		secureCancel = (Button) findViewById(R.id.canel);
		secureCancel.setOnClickListener(this);
		accountnum = (EditText) findViewById(R.id.zfb_recharge_value);
	}

	private void initNameSpinner(final List<AccountName> listName) {
		final Spinner money_brank = (Spinner) findViewById(R.id.account_spinner_bank);
		// TextView dnaRemind = (TextView)findViewById(R.id.TextView07);
		allcountries = new ArrayList<String>();
		if (listName != null && listName.size() > 0) {
			for (AccountName bankName : listName) {
				allcountries.add(bankName.getBankName());
			}
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		money_brank.setAdapter(adapter);
		money_brank.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				initTypeSpinner(listName.get(position).getBankType());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	private void initTypeSpinner(final List<AccountType> listType) {
		final Spinner type_brank = (Spinner) findViewById(R.id.account_spinner_bank_stye);

		// TextView dnaRemind = (TextView)findViewById(R.id.TextView07);
		allcountries = new ArrayList<String>();
		if (listType != null && listType.size() > 0) {
			for (AccountType bankType : listType) {
				allcountries.add(bankType.getName());
			}
		}
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allcountries);
		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);
		type_brank.setAdapter(adapter);
		type_brank.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				isAlipay = listType.get(position).getToAction();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

	}

	public List<AccountName> getList(JSONArray jsonArray) {
		List<AccountName> listName = new ArrayList<AccountName>();

		for (int i = 0; i < jsonArray.length(); i++) {
			AccountName bankName = new AccountName();
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				bankName.setBankName(json.getString("bankname"));
				// Log.e("json.isNull===",""+json.isNull("creditCard"));
				if (!json.isNull("creditCard")) {// 信用卡
					AccountType bankType = new AccountType();
					bankType.setName("信用卡");
					setAccountType(json.getString("creditCard"), bankType);
					bankName.getBankType().add(bankType);
				}
				if (!json.isNull("debitCard")) {// 借记卡
					AccountType bankType = new AccountType();
					bankType.setName("借记卡");
					setAccountType(json.getString("debitCard"), bankType);
					bankName.getBankType().add(bankType);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listName.add(bankName);
		}

		return listName;
	}

	private void setAccountType(String name, AccountType bankType)
			throws JSONException {
		if (name.equals("zfb")) {
			bankType.setToAction(true);
		} else if (name.equals("lthj")) {
			bankType.setToAction(false);
		}
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isOnClick = true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok:
			if (isOnClick) {
				isOnClick = false;
				isTurnView();
			}
			break;

		case R.id.canel:
			finish();
			break;
		}
	}

	/**
	 * 
	 * @param rechargepojo
	 */
	private void getAccountNet() {
		RWSharedPreferences pre = new RWSharedPreferences(
				AccountYingActivity.this, "addInfo");
		final String phonenum = pre.getStringValue(ShellRWConstants.PHONENUM);
		final String userno = pre.getStringValue(ShellRWConstants.USERNO);
		mProgress = UserCenterDialog.onCreateDialog(AccountYingActivity.this);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String re = AccountRechargeInterface.getInstance()
							.recharge(userno, phonenum, TYPE);
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals("0000")) {
						final JSONArray jsonArray = obj.getJSONArray("result");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								initNameSpinner(getList(jsonArray));
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(AccountYingActivity.this,
										message, Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void isTurnView() {
		RWSharedPreferences shellRW = new RWSharedPreferences(
				AccountYingActivity.this, ShellRWConstants.SHAREPREFERENCESNAME);
		String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
		String phonenum = shellRW.getStringValue(ShellRWConstants.PHONENUM);
		String accountnumstr = accountnum.getText().toString();
		if (accountnumstr.length() > 9) {
			Toast.makeText(AccountYingActivity.this, "你有那么多钱吗?",
					Toast.LENGTH_SHORT).show();
			accountnum.setText("100");
			isOnClick = true;
		} else if (accountnumstr.equals("")) {
			Toast.makeText(AccountYingActivity.this, "请输入充值金额!",
					Toast.LENGTH_SHORT).show();
			isOnClick = true;
		} else if (Integer.parseInt(accountnumstr) == 0) {
			Toast.makeText(AccountYingActivity.this, "充值金额不能为0！",
					Toast.LENGTH_SHORT).show();
			isOnClick = true;
		} else if (Integer.parseInt(accountnumstr) < 20) {
			Toast.makeText(AccountYingActivity.this, "充值金额至少为20元！",
					Toast.LENGTH_SHORT).show();
			isOnClick = true;
		} else {
			if (isAlipay) {
				isInstallSecurePay(userno, phonenum, accountnumstr);
			} else {
				actoinYin(accountnumstr);
			}
		}
	}

	public void isInstallSecurePay(String userno, String phonenum, String amt) {
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist) {
			return;
		} else {
			getOrderInfo(userno, phonenum, amt);
		}
	}

	private void getOrderInfo(final String userno, final String phonenum,
			final String amt) {
		// TODO Auto-generated method stub
		(new Handler()).post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String rechargenum = Integer.parseInt(amt) * 100 + "";
				String alipaysecure = AlipaySecurePayInterface.getInstance()
						.alipaySecurePay(rechargenum, userno, phonenum);
				try {
					JSONObject json = new JSONObject(alipaysecure);
					String error_code = json.getString("error_code");
					if (error_code.equals("0000")) {
						String info = json.getString("value");
						MobileSecurePayer msp = new MobileSecurePayer();
						boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY,
								AccountYingActivity.this);

						if (bRet) {
							// show the progress bar to indicate that we have
							// started
							// paying.
							closeProgress();
							mProgress = BaseHelper.showProgress(
									AccountYingActivity.this, null, "正在支付",
									false, true);
						}
					} else {
						AccountYingActivity.this.finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;
				Log.v("strRet==", strRet);
				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();
					try {
						String resultStatus = "resultStatus={";
						int iresultStart = strRet.indexOf("resultStatus={");
						iresultStart += resultStatus.length();
						int iresultEnd = strRet.indexOf("};memo=");
						resultStatus = strRet.substring(iresultStart,
								iresultEnd);
						String memo = "memo=";
						int imemoStart = strRet.indexOf("memo=");
						imemoStart += memo.length();
						int imemoEnd = strRet.indexOf(";result=");
						memo = strRet.substring(imemoStart, imemoEnd);
						if (resultStatus.equals("9000")) {
							memo = "{充值成功}";
						}
						BaseHelper.showDialog(AccountYingActivity.this, "提示",
								memo, R.drawable.info);

					} catch (Exception e) {
						e.printStackTrace();
						BaseHelper.showDialog(AccountYingActivity.this, "提示",
								strRet, R.drawable.info);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return false;
	}

	public void onDestroy() {
		super.onDestroy();
		closeProgress();
	}

	/***************** 银联支付 *********************/
	private final String YINTYPE = "0900";
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

	// 支付宝充值网络获取
	// 改为线程 2010/7/9陈晨
	private void actoinYin(String amt) {
		RechargePojo rechargepojo = new RechargePojo();
		;
		rechargepojo.setAmount(amt);
		rechargepojo.setRechargetype("06");
		rechargepojo.setCardtype(YINTYPE);
		recharge(rechargepojo);
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		RWSharedPreferences pre = new RWSharedPreferences(
				AccountYingActivity.this, "addInfo");
		final String sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		final String userno = pre.getStringValue(ShellRWConstants.USERNO);
		mProgress = UserCenterDialog.onCreateDialog(AccountYingActivity.this);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					rechargepojo.setSessionid(sessionId);
					rechargepojo.setUserno(userno);
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);

					JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals("0000")) {
						final String xml = obj.getString("value");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								turnYinView(xml);
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(AccountYingActivity.this,
										message, Toast.LENGTH_SHORT);
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 银行名内部类
	 * 
	 * @author Administrator
	 * 
	 */
	class AccountName {
		private String bankName = "";
		private List bankType;

		public List getBankType() {
			return bankType;
		}

		public void setBankType(List bankType) {
			this.bankType = bankType;
		}

		public String getBankName() {
			return bankName;
		}

		public void setBankName(String bankName) {
			this.bankName = bankName;
		}

		public AccountName() {
			bankType = new ArrayList<AccountType>();
		}
	}

	/**
	 * 银行卡类型内部类
	 * 
	 * @author Administrator
	 * 
	 */
	class AccountType {
		private String name = "";
		private boolean toAction = true;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean getToAction() {
			return toAction;
		}

		public void setToAction(boolean toAction) {
			this.toAction = toAction;
		}

		public AccountType() {

		}
	}
}
