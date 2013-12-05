package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.secure.AlipaySecurePayDialog;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RechargeMoneyTextWatcher;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.dialog.UpdateDialog;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
 * 拉卡拉支付(免手续费)
 * 
 * @author win
 * 
 */
public class LakalaActivity extends Activity {
	private String xml = "";
	public ProgressDialog progressdialog;
	private final String YINTYPE = "0910";
	private final String TYPE = "10";
	Button secureOk;
	EditText accountnum;
	private ProgressDialog mProgress = null;
	private boolean isOnClick = true;
//	private TextView alipay_content = null;
	private WebView alipay_content = null;
	private boolean isWebView = false;// 浏览器打开支付宝
	private Handler handler = new Handler();
	private TextView accountTitleTextView = null;
	private static final String PAYBY_LAKALA_PACKAGENAME = "com.lakala.android";
	private static final String PAYBY_LAKALA_CLASSNAME = "com.lakala.android.payplatform.PayByLakalaActivity";
	public static final int KEY_REQUEST = 9;
	public static final String KEY_ISTRANSACTION = "transactionOk";
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		context = this;
		initTextViewContent();
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText(getString(R.string.la_ka_la_recharge));

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		/**add by yejc 20130802 start*/
		accountnum.addTextChangedListener(new RechargeMoneyTextWatcher(accountnum));
		/**add by yejc 20130802 end*/
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MobclickAgent.onEvent(LakalaActivity.this, "chongzhi");
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
				.rechargeDescribe("lakalaChargeDescriptionHtml");
		return jsonObjectByLotno;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == KEY_REQUEST && resultCode == RESULT_OK) {
			isTransaction(data);
		}
	}

	private void isTransaction(Intent intent) {
		String isTransactionOk = intent.getStringExtra(KEY_ISTRANSACTION);
		if (null != isTransactionOk && isTransactionOk.equals("true")) {
			Toast.makeText(this, R.string.transactionOK, Toast.LENGTH_LONG)
					.show();
			MobclickAgent.onEvent(this, "chongzhichenggong");// BY贺思明 拉卡拉充值成功
		} else if (null != isTransactionOk && isTransactionOk.equals("false")) {
			Toast.makeText(this, R.string.transactionErr, Toast.LENGTH_LONG)
					.show();
		}
	}

	// 银联充值
	private void beginYinpayRecharge(View Vi) {
		String zfb_recharge_value_string = accountnum.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(LakalaActivity.this,
				"addInfo");
		String sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		String userno = pre.getStringValue(ShellRWConstants.USERNO);
		if (sessionId.equals("")) {
			Intent intentSession = new Intent(LakalaActivity.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			if (!PublicMethod.isRecharge(zfb_recharge_value_string, this)) {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo = new RechargePojo();
				rechargepojo.setSessionid(sessionId);
				rechargepojo.setUserno(userno);
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype(TYPE);
				rechargepojo.setCardtype(YINTYPE);
				recharge(rechargepojo);
			}
		}
	}

	// 充值
	private void recharge(final RechargePojo rechargepojo) {
		progressdialog = UserCenterDialog.onCreateDialog(context);
		progressdialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				try {
					String re = RechargeInterface.getInstance().recharge(
							rechargepojo);
					final JSONObject obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					if (error_code.equals("0000")) {
						handler.post(new Runnable() {
							public void run() {
								jsonToStr(obj);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}
		}).start();

	}

	public void jsonToStr(JSONObject obj) {
		try {
			String ver = obj.getString("ver");
			String merId = obj.getString("merId");
			String minCode = obj.getString("minCode");
			String order = obj.getString("orderId");
			String amount = obj.getString("amount");
			String time = obj.getString("chargeTime");
			String macType = obj.getString("macType");
			String desc = obj.getString("desc");
			String randnum = obj.getString("randNum");
			String productName = obj.getString("productName");
			String url = obj.getString("androidDownloadUrl");
			String mac = obj.getString("mac");
			String randNum = obj.getString("randNum");
			turnYinView(ver, merId, minCode, order, amount, time, macType,
					desc, randnum, productName, randNum, mac, url);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拉卡拉充值跳转到插件
	 */
	public void turnYinView(String ver, String merId, String minCode,
			String order, String amount, String time, String macType,
			String desc, String randnum, String productName, String randNum,
			String mac, String url) {
		try {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(PAYBY_LAKALA_PACKAGENAME,
					PAYBY_LAKALA_CLASSNAME));
			/**
			 * 测试
			 */
			addShortcutDateForIntent(intent, ver, merId, minCode, order,
					amount, time, macType, desc, randnum, productName, randNum,
					mac);
			startActivityForResult(intent, KEY_REQUEST);
		} catch (Exception e) {
			createDialogDownloadApp(url);
		}

	}

	private void addShortcutDateForIntent(Intent intent, String ver,
			String merId, String minCode, String order, String amount,
			String time, String macType, String desc, String randnum,
			String productName, String randNum, String mac) {
		Bundle bundle = new Bundle();
		bundle.putString("ver", ver);
		bundle.putString("merId", merId);
		bundle.putString("orderId", order);
		bundle.putString("amount", amount);
		bundle.putString("time", time);
		bundle.putString("minCode", minCode);// 快捷商户传递
		bundle.putString("randnum", randNum);
		// bundle.putString("merPkg", getPackageName());// 交易完成，返回商户应用程序包名
		// bundle.putString("merClassName",
		// getComponentName().getClassName());//交易成功，返回商户应用程序类名
		bundle.putString("macType", macType);
		bundle.putString("mac", mac);
		intent.putExtra("payInformation", bundle);
	}

	private void createDialogDownloadApp(String softwareurl) {
		HomeUpdate update = new HomeUpdate(this, new Handler(), softwareurl,
				getString(R.string.isInstall),
				getString(R.string.toast_install_lakala));
		update.setDialogNoBack();
		update.showDialog();
		update.createMyDialog();
	}

	class HomeUpdate extends UpdateDialog {

		public HomeUpdate(Activity activity, Handler handler, String url,
				String message, String title) {
			super(activity, handler, url, message, title);
		}

		@Override
		public void onCancelButton() {
			dialogCancel();
		}

	}
}