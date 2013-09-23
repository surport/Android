package com.ruyicai.activity.buy.jc.score.beijing;

import android.content.Intent;
import android.os.Bundle;

import com.ruyicai.activity.buy.jc.score.zq.TrackActivity;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.RWSharedPreferences;

public class TrackBeijingActivity extends TrackActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		isBeiDanTrack = true;
		isBeiDan = true;
		super.onCreate(savedInstanceState);
		getPreferences();
	}

	public void turnInfoActivity(int position) {
		Intent intent = new Intent(this, BeijingScoreInfoActivity.class);
		intent.putExtra("event", listInfoCopy.get(position).getEvent());
		startActivity(intent);
	}
	@Override
	public void getPreferences() {
		shellRw = new RWSharedPreferences(this, ShellRWConstants.BEIJING_PREFER);
	}
}
