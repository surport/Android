package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.BindIDInterface;
import com.ruyicai.net.newtransaction.BindPhoneInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 绑定手机号
 * 
 * @author Administrator
 * 
 */
public class BindPhonenumActivity extends Activity {

	private String phonenum, sessionid, userno;
	RWSharedPreferences shellRW;
	String bindphonenum = "";
	TextView label;
	Button submit, cancle;
	EditText context;
	ProgressDialog progressDialog;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:// 提交绑定手机号成功
				progressDialog.dismiss();
				Toast.makeText(BindPhonenumActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				initSubmitBindSecurtyCode();
				break;
			case 2:// 提交验证码
				progressDialog.dismiss();
				Toast.makeText(BindPhonenumActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				shellRW.putStringValue("mobileid", bindphonenum);
				finish();
				break;
			case 3:// 提交绑定手机号失败
				progressDialog.dismiss();
				Toast.makeText(BindPhonenumActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				finish();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_bindphone);
		label = (TextView) findViewById(R.id.usercenter_bindphonelabel);
		context = (EditText) findViewById(R.id.usercenter_edittext_bindphoneContext);
		submit = (Button) findViewById(R.id.usercenter_bindphone_ok);
		cancle = (Button) findViewById(R.id.usercenter_bindphone_back);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(10000);
				finish();
			}
		});
		shellRW = new RWSharedPreferences(this, "addInfo");
		initSubmitBindphone();
		initPojo();
	}

	private void initPojo() {
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	private void initSubmitBindphone() {
		label.setText(R.string.usercenter_phonenum);
		submit.setText(R.string.get_money_submit);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				bindphonenum = context.getText().toString();
				if (bindphonenum.length() < 11) {
					Toast.makeText(BindPhonenumActivity.this, "请输入正确的手机号格式",
							Toast.LENGTH_SHORT).show();
				} else {
					showDialog(0);
					new Thread(new Runnable() {
						public void run() {
							String bindIDback = BindPhoneInterface
									.getInstance().submitPhonenum(bindphonenum,
											phonenum, userno);
							try {
								JSONObject bindIDjson = new JSONObject(
										bindIDback);
								String errorCode = bindIDjson
										.getString("error_code");
								String message = bindIDjson
										.getString("message");
								Message msg = new Message();
								if (errorCode.equals("0000")) {
									msg.what = 1;
									msg.obj = message;
								} else {
									msg.what = 3;
									msg.obj = message;
								}

								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
							}
						}
					}).start();
				}
			}
		});
	}

	private void initSubmitBindSecurtyCode() {
		label.setText(R.string.usercenter_securitycode);
		context.setText("");
		submit.setText(R.string.usercenter_yanzheng);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String securityCode = context.getText().toString();
				if (securityCode.length() == 0) {
					Toast.makeText(BindPhonenumActivity.this, "验证码不能为空",
							Toast.LENGTH_SHORT).show();
				} else {
					showDialog(0);
					new Thread(new Runnable() {
						public void run() {
							String bindIDback = BindPhoneInterface
									.getInstance().submitSecurityCode(
											securityCode, bindphonenum,
											phonenum, userno);
							try {
								JSONObject bindIDjson = new JSONObject(
										bindIDback);
								String errorCode = bindIDjson
										.getString("error_code");
								String message = bindIDjson
										.getString("message");
								Message msg = new Message();
								if (errorCode.equals("0000")) {
									msg.what = 2;
									msg.obj = message;
								} else {
									msg.what = 3;
									msg.obj = message;
								}

								handler.sendMessage(msg);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
							}
						}
					}).start();
				}
			}
		});
	}

	protected Dialog onCreateDialog(int id) {
		progressDialog = new ProgressDialog(this);
		switch (id) {
		case 0: {
			progressDialog.setTitle(R.string.usercenter_netDialogTitle);
			progressDialog
					.setMessage(getString(R.string.usercenter_netDialogRemind));
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		}
		}
		return progressDialog;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
