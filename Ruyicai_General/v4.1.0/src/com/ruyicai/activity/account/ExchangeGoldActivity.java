package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.BindPhonenumActivity;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.QueryintegrationInterface;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 彩金码兑换彩金
 * 
 * @author win
 * 
 */
public class ExchangeGoldActivity extends Activity {
	private ProgressDialog mProgressdialog;
	private Dialog mDialog;
	private Button mExChangeGoldBtn;
	private EditText mInputGoldCodeEdit;
	private String sessionId = "";
	private String userno = "";
	private String phonenum;
	private final int REQUEST_CODE = 1000;
	private MyHandler mHandler = new MyHandler();
	private TextView accountTitleTextView;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		initUserMessage();
		getMobileIdState();
		initView();
		initWebView();
	}

	private void initView() {
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText(R.string.account_exchange_gold);
		mExChangeGoldBtn = (Button) findViewById(R.id.alipay_secure_ok);
		mExChangeGoldBtn.setText(R.string.account_exchange_gold_start);
		mInputGoldCodeEdit = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		mInputGoldCodeEdit.setInputType(InputType.TYPE_CLASS_TEXT);
		mInputGoldCodeEdit.setHint(R.string.account_exchange_gold_hint);
		mInputGoldCodeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
		mInputGoldCodeEdit.setTextColor(getResources().getColor(R.color.red));
		mExChangeGoldBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String code = mInputGoldCodeEdit.getText().toString().trim();
				code = code.replaceAll(" ", "");
				if ("".equals(code)) {
					createDialog(1, "");
				} else {
					exChangeGold(code);
				}
				MobclickAgent.onEvent(ExchangeGoldActivity.this, "chongzhi");
			}
		});
		PublicMethod.setTextViewContent(this);
	}
	
	private void initWebView() {
		webView = (WebView) findViewById(R.id.alipay_content);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = RechargeDescribeInterface.getInstance()
						.rechargeDescribe("handselCodeChargeDescriptionHtml");
				try {
					final String content = jsonObject.get("content").toString();
					mHandler.post(new Runnable() {
						public void run() {
							webView.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void initUserMessage() {
		RWSharedPreferences pre = new RWSharedPreferences(ExchangeGoldActivity.this,
				"addInfo");
		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		userno = pre.getStringValue(ShellRWConstants.USERNO);
		phonenum = pre.getStringValue("phonenum");
	}

	private void getMobileIdState() {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String jsonString = QueryintegrationInterface.getInstance()
						.queryintegration(phonenum, sessionId, userno);
				Message msg = new Message();
				try {
					JSONObject json = new JSONObject(jsonString);
					msg.what = 0;
					msg.obj = json.getString("mobileId");
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void createDialog(int type, String amount) {
		mDialog = new AlertDialog.Builder(this).create();
		mDialog.show();
		mDialog.getWindow().setContentView(getView(type, amount));
	}
	
	private View getView(final int type, String amount) {
		View view = LayoutInflater.from(this)
				.inflate(R.layout.account_exchange_dialog, null);
		Button ok = (Button)view.findViewById(R.id.button_ok);
		TextView content = (TextView)view.findViewById(R.id.zfb_text_title);
		LinearLayout layout = (LinearLayout)view.findViewById(R.id.LinearLayout01);
		
		if (type == -1) {
			Button cancel = (Button)view.findViewById(R.id.canel);
			cancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.cancel();
					finish();
				}
			});
			
			Button bindOk = (Button)view.findViewById(R.id.ok);
			bindOk.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.cancel();
					Intent intent = new Intent(ExchangeGoldActivity.this,
							BindPhonenumActivity.class);
					startActivityForResult(intent, REQUEST_CODE);
				}
			});
		} else {
			layout.setVisibility(View.GONE);
			ok.setVisibility(View.VISIBLE);
			ok.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (type == 0) {
						mInputGoldCodeEdit.setText("");
					}
					mDialog.cancel();
				}
			});
			if (type == 0) {
				long money = Long.valueOf(amount.trim())/100;
				String message = String.format(getResources().
						getString(R.string.account_exchange_gold_success), money);
				content.setText(message);
			} else if (type == 1) {
				content.setText(R.string.account_exchange_gold_fail);
			}
		}
		return view;
	}
	
	private void exChangeGold(final String code) {
		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = RechargeInterface.exChangeGold(userno, sessionId, "14", code);
				JSONObject obj;
				try {
					obj = new JSONObject(result);
					String error_code = obj.getString("error_code");
					Message msg = mHandler.obtainMessage();
					if ("0000".equals(error_code)) {
						if (obj.has("amount")) {
							msg.obj = obj.getString("amount");
						}
						msg.what = 1;
					} else {
						msg.what = 2;
					}
					mHandler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 10000) {
			createDialog(-1, "");
		}
	}

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				String mobileId = (String)msg.obj;
				if (mobileId == null || "".equals(mobileId) || "null".equals(mobileId)) {
					createDialog(-1, "");
				}
				break;
				
			case 1:
				createDialog(0, (String)msg.obj);
				break;
				
			case 2:
				createDialog(1,"");
				break;
				
			}
			if (mProgressdialog != null) {
				mProgressdialog.dismiss();
			}
		}
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			mProgressdialog = new ProgressDialog(this);
			mProgressdialog.setMessage("网络连接中...");
			mProgressdialog.setIndeterminate(true);
			return mProgressdialog;
		}
		}
		return null;
	}
	
}