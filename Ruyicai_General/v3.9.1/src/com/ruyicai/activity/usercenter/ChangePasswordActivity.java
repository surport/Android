package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.ChangePasswordInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 密码修改
 * 
 * @author miao
 * 
 */
public class ChangePasswordActivity extends Activity {
	private String phonenum, sessionid, userno;
	private String re;
	private JSONObject obj;
	ProgressDialog progressDialog;
	EditText oldPassWD, newPassWD, newPassWDAgain;
	Button submit, cancle;
	final int DIALOG1_KEY = 0;

	/**
	 * 处理登录的消息及注册的消息
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				progressDialog.dismiss();
				Toast.makeText(ChangePasswordActivity.this, (String) msg.obj,
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
		setContentView(R.layout.usercenter_changepawd);
		oldPassWD = (EditText) findViewById(R.id.usercenter_edittext_originalpwd);
		newPassWD = (EditText) findViewById(R.id.usercenter_edittext_newpwd);
		newPassWDAgain = (EditText) findViewById(R.id.usercenter_edittext_confirmnewpwd);
		submit = (Button) findViewById(R.id.usercenter_changepwd_submit);
		submit.setOnClickListener(changepawdlistener);
		cancle = (Button) findViewById(R.id.usercenter_changepwd_back);
		cancle.setOnClickListener(changepawdlistener);
		initPojo();
	}

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	public void editPassword() {

		final String originalpwd = oldPassWD.getText().toString();
		final String newpwd = newPassWD.getText().toString();
		final String confirmpwd = newPassWDAgain.getText().toString();

		// wangyl 7.21 验证密码长度
		if (oldPassWD.length() >= 6 && oldPassWD.length() <= 16
				&& newPassWD.length() >= 6 && newPassWD.length() <= 16
				&& newPassWDAgain.length() >= 6
				&& newPassWDAgain.length() <= 16) {
			if (!confirmpwd.equalsIgnoreCase(newpwd)) {
				newPassWDAgain.setText("");
				Toast.makeText(this, R.string.usercenter_changPSWRemind2,
						Toast.LENGTH_SHORT).show();
			} else {
				showDialog(0);
				new Thread(new Runnable() {
					public void run() {
						String changepassback = ChangePasswordInterface
								.getInstance().changePass(phonenum,
										originalpwd, newpwd, sessionid, userno);
						try {
							JSONObject changepassjson = new JSONObject(
									changepassback);
							String errorCode = changepassjson
									.getString("error_code");
							String message = changepassjson
									.getString("message");
							Message msg = new Message();
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
						}
					}
				}).start();
			}
		} else {
			Toast.makeText(this, R.string.usercenter_changPSWRemind,
					Toast.LENGTH_SHORT).show();
		}

	}

	OnClickListener changepawdlistener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.usercenter_changepwd_submit:
				editPassword();
				break;
			case R.id.usercenter_changepwd_back:
				finish();
				break;
			default:
				break;
			}
		}
	};

	protected Dialog onCreateDialog(int id) {
		progressDialog = new ProgressDialog(this);
		switch (id) {
		case DIALOG1_KEY: {
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
