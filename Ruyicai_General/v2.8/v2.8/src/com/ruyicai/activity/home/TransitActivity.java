package com.ruyicai.activity.home;

import com.ruyicai.util.Constants;
import com.ruyicai.util.ShellRWSharesPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TransitActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		turnActivity();
	}
	public void turnActivity(){
			Intent in = new Intent(TransitActivity.this,MainGroup.class);
			startActivity(in);
			TransitActivity.this.finish();
	}
}
