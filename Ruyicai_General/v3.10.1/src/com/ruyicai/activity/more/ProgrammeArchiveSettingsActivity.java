package com.ruyicai.activity.more;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.BindEmailActivity;
import com.ruyicai.activity.usercenter.FeedbackListActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

public class ProgrammeArchiveSettingsActivity extends Activity {
	private ImageView ssqProgrammeSettingsIV;
//	private ImageView dltProgrammeSettingsIV;
	private RWSharedPreferences shellRW;
	ViewClickListener clickListener = new ViewClickListener();
	private Context context = null;
	private String userNo = "";
	Controller controller;

	Handler handler = new MyHandler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_settings_programme_settings);
		context = this;
		controller = Controller.getInstance(context);
		shellRW = new RWSharedPreferences(this, "addInfo");
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		userNo = shellRW.getStringValue(ShellRWConstants.USERNO);
		if (userNo != null && "!".equals(userNo)) {
			controller.queryOrderEmail(handler, Constants.LOTNO_SSQ, userNo);
		}
	}

	private void initView() {
		ssqProgrammeSettingsIV = (ImageView)findViewById(R.id.more_settings_progamme_ssq_on_off);
//		dltProgrammeSettingsIV = (ImageView)findViewById(R.id.more_settings_progamme_dlt_on_off);
		ssqProgrammeSettingsIV.setOnClickListener(clickListener);
//		dltProgrammeSettingsIV.setOnClickListener(clickListener);
		if (shellRW.getBooleanValue(Constants.isSSQON)) {
			ssqProgrammeSettingsIV.setImageResource(R.drawable.on);
		} else {
			ssqProgrammeSettingsIV.setImageResource(R.drawable.off);
		}
		
//		if (shellRW.getBooleanValue(Constants.isDLTON)) {
//			dltProgrammeSettingsIV.setImageResource(R.drawable.on);
//		} else {
//			dltProgrammeSettingsIV.setImageResource(R.drawable.off);
//		}
	}
	
	private class ViewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.more_settings_progamme_ssq_on_off:
				String sessionIdStr = shellRW.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr == null || sessionIdStr.equals("")) {
					Intent intentSession = new Intent(context, UserLogin.class);
					startActivity(intentSession);
				} else {
					if (shellRW.getBooleanValue(Constants.isSSQON)) {
						controller.setOrderEmail(handler, Constants.LOTNO_SSQ, "0", userNo);
					} else {
						String email = shellRW.getStringValue("email");
						if (email == null || "".equals(email)) {
							Intent intent = new Intent(context,
									BindEmailActivity.class);
							startActivity(intent);
						} else {
							controller.setOrderEmail(handler, Constants.LOTNO_SSQ, "1", userNo);
						}
					}
				}
				
				break;
				
//			case R.id.more_settings_progamme_dlt_on_off:
//				if (shellRW.getBooleanValue(Constants.isDLTON)) {
//					controller.setOrderEmail(handler, Constants.LOTNO_DLT, "0", userNo);
//				} else {
//					String email = shellRW.getStringValue("email");
//					if (email == null || "".equals(email)) {
//						Intent intent = new Intent(context,
//								BindEmailActivity.class);
//						startActivity(intent);
//					} else {
//						controller.setOrderEmail(handler, Constants.LOTNO_DLT, "1", userNo);
//					}
//				}
//				break;

			default:
				break;
			}
			
		}
		
	}
	
	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				setOnOff((String)msg.obj);
				break;
				
			case 2:
				Bundle date = msg.getData();
				String lotno = date.getString("lotno");
				String state = date.getString("state");
				setShowState(lotno, state);
				break;
				
			case 3:
				Toast.makeText(context, "设置失败请查看网络连接状态！", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}
	
	private void setOnOff(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(Constants.LOTNO_SSQ)) {
				JSONObject ssqObj = new JSONObject(obj.getString(Constants.LOTNO_SSQ));
				if ("1".equals(ssqObj.getString("state"))) {
					ssqProgrammeSettingsIV.setImageResource(R.drawable.on);
					shellRW.putBooleanValue(Constants.isSSQON, true);
				} else {
					ssqProgrammeSettingsIV.setImageResource(R.drawable.off);
					shellRW.putBooleanValue(Constants.isSSQON, false);
				}
			}
//			if (obj.has(Constants.LOTNO_DLT)) {
//				JSONObject dltqObj = new JSONObject(obj.getString(Constants.LOTNO_DLT));
//				if ("1".equals(dltqObj.getString("state"))) {
//					dltProgrammeSettingsIV.setImageResource(R.drawable.on);
//					shellRW.putBooleanValue(Constants.isDLTON, true);
//				} else {
//					dltProgrammeSettingsIV.setImageResource(R.drawable.off);
//					shellRW.putBooleanValue(Constants.isDLTON, false);
//				}
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void setShowState(String lotno, String state) {
		if (Constants.LOTNO_SSQ.equals(lotno)) {
			if ("1".equals(state)) {
				ssqProgrammeSettingsIV.setImageResource(R.drawable.on);
				shellRW.putBooleanValue(Constants.isSSQON, true);
			} else {
				ssqProgrammeSettingsIV.setImageResource(R.drawable.off);
				shellRW.putBooleanValue(Constants.isSSQON, false);
			}
			
		} /*else if (Constants.LOTNO_DLT.equals(lotno)) {
			if ("1".equals(state)) {
				dltProgrammeSettingsIV.setImageResource(R.drawable.on);
				shellRW.putBooleanValue(Constants.isDLTON, true);
			} else {
				dltProgrammeSettingsIV.setImageResource(R.drawable.off);
				shellRW.putBooleanValue(Constants.isDLTON, false);
			}
		}*/
		
	}

}
