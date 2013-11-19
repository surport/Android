package com.ruyicai.activity.buy.jc.zq;

import android.content.Intent;
import android.os.Bundle;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.activity.notice.NoticeJcActivity;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class ZqMainActivity extends JcMainActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setType(Constants.JCFOOT);
		createView(SF, false);
		setLotNo(Constants.LOTNO_JCZ);
		setScoreBtn();
		isTeamBtn();
		MobclickAgent.onEvent(this, "jingcaizuqiu"); // BY贺思明 点击首页的“竞彩足球”图标
	}

	public void turnHosity() {
		Intent intent = new Intent(context, NoticeJcActivity.class);
		startActivity(intent);
	}
}
