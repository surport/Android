package com.ruyicai.activity.account;

import java.util.Hashtable;

import cn.immob.sdk.ImmobView;
import cn.immob.sdk.LMAdListener;
import cn.immob.sdk.listener.AdUtilityListener;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 积分墙
 * 
 * @author win
 * 
 */
public class AdWallActivity extends Activity {
	public static  String adUnitID = "9c697272e78036382b35056bdf53904b";//这里是广告墙的广告位id
	private LinearLayout layout = null;
	private ImmobView lmView = null;
	private ProgressDialog progressdialog;
	RWSharedPreferences shellRW = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adwall_layout);
		shellRW = new RWSharedPreferences(this, "addInfo");
		layout = (LinearLayout)findViewById(R.id.layout);
		lmView =new ImmobView(this, adUnitID);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
		lmView.setLayoutParams(params);
		lmView.setAdListener(new MyLMAdListener());
		layout.addView(lmView);
		
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		
		Hashtable<String, String> userProperties = new Hashtable<String, String>(); 
		userProperties.put("accountname", shellRW.getStringValue(ShellRWConstants.USERNO)); 
		lmView.setUserInfo(userProperties); 
	}
	
	private class MyLMAdListener implements LMAdListener {

		@Override
		public void onAdReceived(ImmobView arg0) {
			lmView.display();
			if (arg0.isAdReady()) {
				progressdialog.dismiss();
			}
		}

		@Override
		public void onDismissScreen(ImmobView arg0) {
			finish();
		}

		@Override
		public void onFailedToReceiveAd(ImmobView arg0, int arg1) {
//			Toast.makeText(AdWallActivity.this, R.string.get_free_gold_network_error, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onLeaveApplication(ImmobView arg0) {
		}

		@Override
		public void onPresentScreen(ImmobView arg0) {
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(lmView!=null){
			lmView.destroy();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		lmView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		lmView.onResume();
	}
}