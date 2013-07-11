package com.ruyicai.activity.usercenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.umeng.analytics.MobclickAgent;

public class HadBindedID extends Activity {

	private String theBindCertid, theBindName;
	TextView remindTitle, bindIdnum, servicephone, bindName;
	Button cancleLook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		theBindCertid = this.getIntent().getStringExtra("certid");
		theBindName = this.getIntent().getStringExtra("name");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.binded_certid_remind);
		remindTitle = (TextView) findViewById(R.id.usercenter_bindphoneorid_dialogtitle);
		bindName = (TextView) findViewById(R.id.usercenter_bindedname);
		bindIdnum = (TextView) findViewById(R.id.usercenter_bindedcertidnum);
		bindName.setText(theBindName);
		bindIdnum.setText(formatIDNum(theBindCertid));
		servicephone = (TextView) findViewById(R.id.bindedcertid_servicephone);
		cancleLook = (Button) findViewById(R.id.usercenter_bindedidremind_back);
		servicephone.setLinkTextColor(0xff0066ff);
		cancleLook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private String formatIDNum(String idstr) {
		String formattedIDstr = "";
		int idlength = idstr.length();
		String part1 = idstr.substring(0, idlength - 4);
		formattedIDstr = part1 + "****";
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
