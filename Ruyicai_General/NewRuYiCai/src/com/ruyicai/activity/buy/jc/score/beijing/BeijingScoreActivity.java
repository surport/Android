package com.ruyicai.activity.buy.jc.score.beijing;

import android.os.Bundle;
import com.ruyicai.activity.buy.jc.score.lq.TrackLqActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreActivity;

public class BeijingScoreActivity extends JcScoreActivity {
	
	public static String lotno = "";
	public static String bebatchCode = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lotno = getIntent().getStringExtra("lotno");
//		bebatchCode = getIntent().getStringExtra("bebatchCode");
	}

	public void initTop() {
		allId = new Class[] { BeijingScoreListActivity.class,
				BeijingScoreListActivity.class, BeijingScoreListActivity.class,
				BeijingScoreListActivity.class, TrackBeijingActivity.class };
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		lotno = "";
		bebatchCode = "";
	}
	
	
}
