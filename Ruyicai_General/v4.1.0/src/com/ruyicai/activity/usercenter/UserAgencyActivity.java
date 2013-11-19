package com.ruyicai.activity.usercenter;

import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.AboutInfoInterface;
import com.ruyicai.net.newtransaction.AgencyInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 代理充值
 * 
 * @author win
 * 
 */
public class UserAgencyActivity extends Activity implements OnClickListener {
	private Button secureOk, secureCancel;
	private EditText userAmt, userName, userPassword;
	private ProgressDialog progressdialog;
	private Context context;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.user_agency);
		initView();
	}

	private void initView() {
		secureOk = (Button) findViewById(R.id.ok);
		secureOk.setOnClickListener(this);
		secureCancel = (Button) findViewById(R.id.canel);
		secureCancel.setOnClickListener(this);
		userAmt = (EditText) findViewById(R.id.zfb_recharge_value);
		userName = (EditText) findViewById(R.id.zfb_recharge_name);
		userPassword = (EditText) findViewById(R.id.zfb_recharge_password);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ok:
			isAgencyNet();
			break;
		case R.id.canel:
			finish();
			break;
		}
	}

	private void isAgencyNet() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String userno = pre.getStringValue(ShellRWConstants.USERNO);
		String phonenum = pre.getStringValue(ShellRWConstants.PHONENUM);
		String toPhonenum = userName.getText().toString();
		String amout = userAmt.getText().toString();
		String password = userPassword.getText().toString();

		if (toPhonenum == null || toPhonenum.equals("")) {
			Toast.makeText(context, "您输入的用户名不能为空！", Toast.LENGTH_SHORT).show();
		} else if (amout == null || amout.equals("")) {
			Toast.makeText(context, "您输入的金额不能为空！", Toast.LENGTH_SHORT).show();
		} else if (password == null || password.equals("")) {
			Toast.makeText(context, "您输入的密码不能为空！", Toast.LENGTH_SHORT).show();
		} else if (Integer.parseInt(amout.substring(0, 1)) == 0) {
			Toast.makeText(context, "您输入的金额第一位数不能为零", Toast.LENGTH_SHORT)
					.show();
		} else if (amout.length() > 5) {
			Toast.makeText(context, "您输入的金额不能超过10万元！", Toast.LENGTH_SHORT)
					.show();
		} else if (Integer.parseInt(amout) == 0) {
			Toast.makeText(context, "您输入的金额不能为零！", Toast.LENGTH_SHORT).show();
		} else {
			amout = PublicMethod.toFen(amout);
			agencyNet(userno, phonenum, toPhonenum, amout, password);
		}
	}

	private void agencyNet(final String userno, final String name,
			final String toName, final String amout, final String password) {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonObjectByLotno = AgencyInterface.getInstance()
						.agency(userno, name, toName, amout, password);
				try {
					String errorCode = jsonObjectByLotno
							.getString("error_code");
					final String message = jsonObjectByLotno
							.getString("message");
					if (errorCode.equals("0000")) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
								finish();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (progressdialog != null) {
					progressdialog.dismiss();
				}
			}
		}).start();

	}
}
