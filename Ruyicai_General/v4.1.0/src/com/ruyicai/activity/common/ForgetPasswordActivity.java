package com.ruyicai.activity.common;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.ForgetPasswordInterface;
import com.ruyicai.util.CallServicePhoneConfirm;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 找回密码
 * 
 * @author Administrator
 * 
 */
public class ForgetPasswordActivity extends Activity implements HandlerMsg {
	Context context = this;
	MyHandler handler = new MyHandler(this);// 自定义handler
	String message;
	ProgressDialog progressdialog;
	EditText nameEdit;
	EditText phoneEdit;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_password);
		initView();
	}

	/**
	 * 初始化界面
	 */
	public void initView() {
		nameEdit = (EditText) findViewById(R.id.forget_password_edit_name);
		phoneEdit = (EditText) findViewById(R.id.forget_password_edit_phone);
		Button login_return = (Button) findViewById(R.id.usercenter_btn_return);
		initAlertPhone();
		login_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		Button forgetPasswordBtn = (Button) findViewById(R.id.forget_password_btn);
		forgetPasswordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = nameEdit.getText().toString();
				String phone = phoneEdit.getText().toString();
				if (name.equals("")) {
					Toast.makeText(context, "用户名不能为空！", Toast.LENGTH_SHORT)
							.show();
				} else if (phone.equals("") || phone.length() < 11) {
					Toast.makeText(context, "请输入正确的手机号！", Toast.LENGTH_SHORT)
							.show();
				} else {
					getPasswordNet(name, phone);
				}
			}
		});
	}

	/**
	 * 设置客服电话
	 */
	private void initAlertPhone() {
		TextView alertText = (TextView) findViewById(R.id.register_free_info_view);
		// SpannableStringBuilder builder = new SpannableStringBuilder();
		// String alertStr =
		// getString(R.string.forget_password_hint).toString();
		// String alertPhone = getString(R.string.phone_kefu).toString();
		// builder.append(alertStr);
		// builder.setSpan(new URLSpan("tel:"+alertPhone ),28,
		// (28+alertPhone.length()),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// alertText.setText(builder);
		// alertText.setMovementMethod(LinkMovementMethod.getInstance());

		alertText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// phoneKefu();
				CallServicePhoneConfirm.phoneKefu(ForgetPasswordActivity.this);
			}
		});

	}

	public void getPasswordNet(final String name, final String phone) {
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = ForgetPasswordInterface.getInstance().forgetPasswordNet(
						name, phone);
				try {
					JSONObject obj = new JSONObject(str);
					message = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
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
