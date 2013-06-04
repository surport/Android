package com.ruyicai.activity.home;

import com.ruyicai.activity.introduce.PhotoActivity;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ShellRWSharesPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TransitActivity extends Activity{
	private ShellRWSharesPreferences shellRW;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		turnActivity();
	}
	public void turnActivity(){
		// 读取本地渠道号
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		boolean isFirst = shellRW.getPreferencesBoolean("isFirst");
		if(isFirst){
			shellRW.setPreferencesBoolean("isFirst", false);
			Intent in = new Intent(TransitActivity.this,PhotoActivity.class);
			startActivity(in);
			TransitActivity.this.finish();
		}else{
			Intent in = new Intent(TransitActivity.this,MainGroup.class);
			startActivity(in);
			TransitActivity.this.finish();
		}

	}
}
