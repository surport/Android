package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.BindPhoneInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
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

public class HadBindedPhoneOrUnBindPhone extends Activity {
	private String phonenum, userno;
	TextView label;
	Button submit, cancle;
	EditText context;
	ProgressDialog progressDialog;
	RWSharedPreferences shellRW;
	String bindPhone;// 已经绑定的手机号

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:// 解除绑定手机号成功
				progressDialog.dismiss();
				Toast.makeText(HadBindedPhoneOrUnBindPhone.this,
						(String) msg.obj, Toast.LENGTH_LONG).show();
				shellRW.putStringValue("mobileid", "");
				finish();
				break;
			case 2:// 失败
				progressDialog.dismiss();
				Toast.makeText(HadBindedPhoneOrUnBindPhone.this,
						(String) msg.obj, Toast.LENGTH_LONG).show();
				finish();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		bindPhone = this.getIntent().getStringExtra("mobileid");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_bindphone);
		label = (TextView) findViewById(R.id.usercenter_bindphonelabel);
		label.setPadding(0, 20, 0, 20);
		label.setText("您已成功绑定手机" + formatPhoneNum(bindPhone));
		context = (EditText) findViewById(R.id.usercenter_edittext_bindphoneContext);
		context.setVisibility(EditText.GONE);
		submit = (Button) findViewById(R.id.usercenter_bindphone_ok);
		submit.setText(R.string.usercenter_unbind);
		submit.setOnClickListener(unbindListener);
		cancle = (Button) findViewById(R.id.usercenter_bindphone_back);
		cancle.setOnClickListener(unbindListener);
		shellRW = new RWSharedPreferences(this, "addInfo");
	}

	OnClickListener unbindListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.usercenter_bindphone_ok:
				Unbind();
				break;
			case R.id.usercenter_bindphone_back:
				finish();
				break;
			default:
				break;
			}
		}
	};

	private void Unbind() {
		phonenum = shellRW.getStringValue("phonenum");
		userno = shellRW.getStringValue("userno");
		showDialog(0);
		new Thread(new Runnable() {
			public void run() {
				String bindIDback = BindPhoneInterface.getInstance().unBind(
						phonenum, userno);
				try {
					JSONObject bindIDjson = new JSONObject(bindIDback);
					String errorCode = bindIDjson.getString("error_code");
					String message = bindIDjson.getString("message");
					Message msg = new Message();
					if (errorCode.equals("0000")) {
						msg.what = 1;
						msg.obj = message;
					} else {
						msg.what = 2;
						msg.obj = message;
					}

					handler.sendMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
				}
			}
		}).start();
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

	private String formatPhoneNum(String idstr) {
		String formattedIDstr = "";
		int idlength = idstr.length();
		if (idlength == 11) {
			String part1 = idstr.substring(0, idlength - 4);
			formattedIDstr = part1 + "****";
		} else {
			formattedIDstr = idstr + "(号码有误)";
		}
		return formattedIDstr;
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
