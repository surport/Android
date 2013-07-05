package com.ruyicai.activity.more;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.BindEmailActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.controller.Controller;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class ProgrammeArchiveSettingsActivity extends Activity {
	private ImageView ssqProgrammeSettingsIV;
	private ImageView dltProgrammeSettingsIV;
	private RWSharedPreferences shellRW;
	ViewClickListener clickListener = new ViewClickListener();
	private Context context = null;
	private String userNo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.more_settings_programme_settings);
		context = this;
		shellRW = new RWSharedPreferences(this, "addInfo");
		userNo = shellRW.getStringValue(ShellRWConstants.USERNO);
		initView();
	}
	
	private void initView() {
		ssqProgrammeSettingsIV = (ImageView)findViewById(R.id.more_settings_progamme_ssq_on_off);
		dltProgrammeSettingsIV = (ImageView)findViewById(R.id.more_settings_progamme_dlt_on_off);
		ssqProgrammeSettingsIV.setOnClickListener(clickListener);
		dltProgrammeSettingsIV.setOnClickListener(clickListener);
		if (shellRW.getBooleanValue(Constants.isSSQON)) {
			ssqProgrammeSettingsIV.setImageResource(R.drawable.on);
		} else {
			ssqProgrammeSettingsIV.setImageResource(R.drawable.off);
		}
		
		if (shellRW.getBooleanValue(Constants.isDLTON)) {
			dltProgrammeSettingsIV.setImageResource(R.drawable.on);
		} else {
			dltProgrammeSettingsIV.setImageResource(R.drawable.off);
		}
	}
	
	private class ViewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.more_settings_progamme_ssq_on_off:
				if (shellRW.getBooleanValue(Constants.isSSQON)) {
					ssqProgrammeSettingsIV.setImageResource(R.drawable.off);
					shellRW.putBooleanValue(Constants.isSSQON, false);
					Controller.getInstance(context).setOrderEmail(Constants.LOTNO_SSQ, "0", userNo);
				} else {
					String email = shellRW.getStringValue("email");
					if (email == null || "".equals(email)) {
						Intent intent = new Intent(context,
								BindEmailActivity.class);
						startActivity(intent);
					} else {
						ssqProgrammeSettingsIV.setImageResource(R.drawable.on);
						shellRW.putBooleanValue(Constants.isSSQON, true);
						Controller.getInstance(context).setOrderEmail(Constants.LOTNO_SSQ, "1", userNo);
					}
				}
//				Controller.getInstance(context).queryOrderEmail(Constants.LOTNO_SSQ, userNo);
				
				break;
				
			case R.id.more_settings_progamme_dlt_on_off:
				if (shellRW.getBooleanValue(Constants.isDLTON)) {
					dltProgrammeSettingsIV.setImageResource(R.drawable.off);
					shellRW.putBooleanValue(Constants.isDLTON, false);
					Controller.getInstance(context).setOrderEmail(Constants.LOTNO_DLT, "0", userNo);
				} else {
					String email = shellRW.getStringValue("email");
					if (email == null || "".equals(email)) {
						Intent intent = new Intent(context,
								BindEmailActivity.class);
						startActivity(intent);
					} else {
						dltProgrammeSettingsIV.setImageResource(R.drawable.on);
						shellRW.putBooleanValue(Constants.isDLTON, true);
						Controller.getInstance(context).setOrderEmail(Constants.LOTNO_DLT, "1", userNo);
					}
				}
//				Controller.getInstance(context).queryOrderEmail(Constants.LOTNO_DLT, userNo);
				break;

			default:
				break;
			}
			
		}
		
	}

}
