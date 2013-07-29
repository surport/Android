package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import com.alipay.android.secure.MobileSecurePayHelper;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 联动优势话费充值
 * 
 * @author win
 * 
 */
public class UmPayPhoneActivity extends Activity {
	public ProgressDialog progressdialog;
	Button secureOk, secureCancel;
	EditText accountnum;
	private TextView alipayContent = null;
	private TextView accountTitleTextView = null;
	private String money = "";
	public static final String UMPAY_RECHARGE_AMOUNT = "UMPAY_RECHARGE_AMOUNT";
	RadioGroup radioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_alipay_secure_recharge_dialog);
		accountTitleTextView = (TextView) findViewById(R.id.accountTitle_text);
		accountTitleTextView.setText("话费充值");
		TextView title = (TextView)findViewById(R.id.recharge_title);
		title.setText(R.string.umpay_phone_text_moneny);
		LinearLayout layout = (LinearLayout)findViewById(R.id.umpay_phone_linear);
		layout.setVisibility(View.VISIBLE);
		alipayContent = (TextView) findViewById(R.id.alipay_content);
		initTextViewContent();
//		alipayContent.setText(R.string.umpay_phone_content);
		radioGroup = (RadioGroup)findViewById(R.id.umpay_recharge_radiogroup);

		secureOk = (Button) findViewById(R.id.alipay_secure_ok);
		secureCancel = (Button) findViewById(R.id.alipay_secure_cancel);
		accountnum = (EditText) findViewById(R.id.alipay_secure_recharge_value);
		accountnum.setVisibility(View.GONE);
		secureOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginUmpayRecharge();
			}
		});
		secureCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				UmPayPhoneActivity.this.finish();
			}
		});
	}

	// 联动优势充值
	private void beginUmpayRecharge() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivity(intentSession);
		} else {
			// 检测安全支付服务是否被安装，
			MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
			boolean isInstall = mspHelper.detectMobile_sp(Constants.PAY_PLUGIN_NAME, Constants.UMPAY_PHONE_PACK_NAME);
			
			if (isInstall) {
				int id = radioGroup.getCheckedRadioButtonId();
				if (id == R.id.umpay_phone_thirty) {
					money = "30";
				} else {
					money = "3";
				}
				Intent intent = new Intent(this, UmPayPhonePopActivity.class);
				intent.putExtra(UMPAY_RECHARGE_AMOUNT, money);
				startActivity(intent);
			}
		}
	}
	
	private void initTextViewContent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject jsonObject = RechargeDescribeInterface.getInstance()
						.rechargeDescribe("umpayHfChargeDescription");
				try {
					String conten = jsonObject.get("content").toString();
					alipayContent.setText(conten);
					alipayContent.setTextColor(getResources().getColor(R.color.red));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}