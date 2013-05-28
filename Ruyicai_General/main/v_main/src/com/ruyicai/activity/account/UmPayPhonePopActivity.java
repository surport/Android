package com.ruyicai.activity.account;

import com.palmdream.RuyicaiAndroid.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 联动优势话费充值弹出信息
 * 
 * @author win
 * 
 */
public class UmPayPhonePopActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.umpay_recharge_bindphone);
		ViewClickListener clickListener = new ViewClickListener();
		Button cancel = (Button) findViewById(R.id.usercenter_bindphone_back);
		cancel.setOnClickListener(clickListener);
		Button summit = (Button) findViewById(R.id.usercenter_bindphone_ok);
		summit.setOnClickListener(clickListener);
		Button ok = (Button) findViewById(R.id.usercenter_bindphone_complete);
		ok.setOnClickListener(clickListener);
	}

	private class ViewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.usercenter_bindphone_ok:
				summit();
				break;

			case R.id.usercenter_bindphone_back:
			case R.id.usercenter_bindphone_complete:
				finish();
				break;

			default:
				break;
			}
		}
	}
	
	private void summit() {
		EditText edit = (EditText) findViewById(R.id.input_phone_number);
		String number = edit.getText().toString();
		if (number.length() != 11) {
			Toast.makeText(this, "请输入正确的手机号格式",
					Toast.LENGTH_SHORT).show();
		} else {
			//summit 
			setupView();
		}
	}

	private void setupView() {
		LinearLayout contentLayout = (LinearLayout) findViewById(R.id.usercenter_changepwd_content);
		LinearLayout summitLayout = (LinearLayout) findViewById(R.id.umpay_recharge_summit);
		LinearLayout completeLayout = (LinearLayout) findViewById(R.id.umpay_recharge_complete);
		TextView completeContent = (TextView) findViewById(R.id.umpay_recharge_complete_content);
		contentLayout.setVisibility(View.GONE);
		summitLayout.setVisibility(View.GONE);
		completeLayout.setVisibility(View.VISIBLE);
		completeContent.setVisibility(View.VISIBLE);
	}
}