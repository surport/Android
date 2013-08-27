package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.BindEmailInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 显示绑定邮箱信息页面
 * 
 * @author Administrator
 * 
 */
public class UnBindEmailActivity extends Activity {
	/** 联网对话框id */
	private static final int CONNECT_INETNET_DIALOG = 0;
	/** 解绑成功标识 */
	private static final int UNBIND_SUCCESS = 10;
	/** 解绑失败标识 */
	private static final int UNBIND_FAILD = 11;

	/** 绑定邮箱文本 */
	private TextView bindEmailTextView;
	/** 返回按钮 */
	private Button returnButton;
	/** 解绑按钮 */
	private Button unbindButton;
	/** 联网进度对话框 */
	private ProgressDialog connectIntentDialog;

	/** 全局共享参数 */
	private RWSharedPreferences shellRW;

	/** 进程通信Handler对象 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 取消联网对话框
			connectIntentDialog.dismiss();

			switch (msg.what) {
			case UNBIND_SUCCESS:
				// 解绑邮箱成功
				shellRW.putStringValue("email", "");
				
				/**add by yejc 20130617 start*/
				shellRW.putBooleanValue(Constants.isSSQON, false);
				shellRW.putBooleanValue(Constants.isDLTON, false);
				/**add by yejc 20130617 end*/
				break;
			}

			Toast.makeText(UnBindEmailActivity.this, (String) msg.obj,
					Toast.LENGTH_LONG).show();
			finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hademailinfo);

		// 初始化绑定邮箱文本框
		bindEmailTextView = (TextView) findViewById(R.id.usercenter_bindedemail);
		bindEmailTextView.setText(getIntent().getStringExtra("email"));

		// 初始化返回按钮
		returnButton = (Button) findViewById(R.id.usercenter_bindedemail_back);
		returnButton.setOnClickListener(new ButtonOnClickListener());

		// 初始化解绑按钮
		unbindButton = (Button) findViewById(R.id.usercenter_bindedemail_unbind);
		unbindButton.setOnClickListener(new ButtonOnClickListener());

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

	/**
	 * 按钮点击事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.usercenter_bindedemail_unbind:
				connectIntenetUnBindEmail();
				break;
			case R.id.usercenter_bindedemail_back:
				finish();
				break;
			}
		}

		/**
		 * 联网解除邮箱绑定
		 */
		private void connectIntenetUnBindEmail() {
			showDialog(CONNECT_INETNET_DIALOG);

			new Thread(new Runnable() {

				@Override
				public void run() {
					String userno = shellRW.getStringValue("userno");
					String returnString = BindEmailInterface.getInstance()
							.unBindEmail(userno);

					try {
						JSONObject json = new JSONObject(returnString);

						String errorCode = json.getString("error_code");
						String message = json.getString("message");

						Message msg = new Message();
						if (errorCode.equals("0000")) {
							msg.what = UNBIND_SUCCESS;
						} else {
							msg.what = UNBIND_FAILD;
						}

						msg.obj = message;
						handler.sendMessage(msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}

	}
}
