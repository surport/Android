package com.ruyicai.activity.buy.ssq;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.BindEmailInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 接受并绑定邮箱
 * 
 * @author Administrator
 * 
 */
public class ReceiveAndBindEmailActivity extends Activity {
	/** 联网对话框id */
	private static final int CONNECT_INETNET_DIALOG = 0;
	/** 绑定成功标识 */
	private static final int BIND_SUCCESS = 10;
	/** 绑定失败标识 */
	private static final int BIND_FAILD = 11;
	/** 邮箱字符串 */
	private String email;

	/** 邮箱输入文本框 */
	private EditText emailEditText;
	/** 接受并绑定按钮 */
	private Button receiverAndBindButton;
	/** 联网进度对话框 */
	private ProgressDialog connectIntentDialog;

	/** 全局共享参数 */
	private RWSharedPreferences shellRW;

	/** 进程通信Handler对象 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 提交邮箱成功，取消联网对话框
			connectIntentDialog.dismiss();

			switch (msg.what) {
			case BIND_SUCCESS:
				// 如果绑定邮箱成功，保存保定邮箱
				shellRW.putStringValue("email", email);
				Intent intent = new Intent(ReceiveAndBindEmailActivity.this,
						Ssq.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				break;
			}

			// 显示联网返回信息，并关系当前页面
			Toast.makeText(ReceiveAndBindEmailActivity.this, (String) msg.obj,
					Toast.LENGTH_LONG).show();
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.receiver_bind_email);

		emailEditText = (EditText) findViewById(R.id.receiverandsend_email_edittext);
		receiverAndBindButton = (Button) findViewById(R.id.receiverandsend_email_button);
		receiverAndBindButton.setOnClickListener(new ButtonOnClickListener());

		// 初始化全局共享参数
		shellRW = new RWSharedPreferences(this, "addInfo");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		connectIntentDialog = new ProgressDialog(this);
		switch (id) {
		case CONNECT_INETNET_DIALOG: {
			connectIntentDialog.setTitle(R.string.usercenter_netDialogTitle);
			connectIntentDialog
					.setMessage(getString(R.string.usercenter_netDialogRemind));
			connectIntentDialog.setIndeterminate(true);
			connectIntentDialog.setCancelable(true);
			break;
		}
		}
		return connectIntentDialog;
	}

	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.receiverandsend_email_button:
				// 如果用户没有输入邮箱
				if (isInputEmail()) {
					Toast.makeText(ReceiveAndBindEmailActivity.this,
							"邮箱地址不能为空", Toast.LENGTH_SHORT).show();
				}
				// 如果用户输入，则验证，并联网绑定邮箱
				else {
					if (PublicMethod.isRightEmail(email)) {
						connectIntenetBindEmail();
					} else {
						Toast.makeText(ReceiveAndBindEmailActivity.this,
								"邮箱地址不正确", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
		}
	}

	/**
	 * 连接互联网绑定邮箱
	 */
	private void connectIntenetBindEmail() {
		showDialog(CONNECT_INETNET_DIALOG);

		new Thread(new Runnable() {

			@Override
			public void run() {
				final String userno = shellRW.getStringValue("userno");

				String returnString = BindEmailInterface.getInstance()
						.bindEmail(userno, email);
				analyzingReturnJsonString(returnString);
			}
		}).start();
	}

	/**
	 * 解析返回的json字符串
	 * 
	 * @param returnString
	 *            json字符串
	 */
	private void analyzingReturnJsonString(String returnString) {
		try {
			JSONObject bindIDjson = new JSONObject(returnString);

			String errorCode = bindIDjson.getString("error_code");
			String message = bindIDjson.getString("message");

			Message msg = new Message();
			if (errorCode.equals("0000")) {
				msg.what = BIND_SUCCESS;
			} else {
				msg.what = BIND_FAILD;
			}
			msg.obj = message;

			handler.sendMessage(msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户是否输入邮箱
	 * 
	 * @return 用户是否输入标识
	 */
	private boolean isInputEmail() {
		// 获取用户绑定的邮件和用户编号字符串
		email = emailEditText.getText().toString();

		return email.length() == 0;
	}
}
