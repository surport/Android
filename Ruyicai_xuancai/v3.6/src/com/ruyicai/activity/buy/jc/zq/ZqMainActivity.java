package com.ruyicai.activity.buy.jc.zq;

import android.os.Bundle;
import android.view.Window;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.jc.JcMainActivity;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class ZqMainActivity extends JcMainActivity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setType(Constants.JCFOOT);
        createView(SF,false);
        setScoreBtn(true);
        setTitle(false);
        isTeamBtn(true);
        MobclickAgent.onEvent(this,"jingcaizuqiu" ); //BY贺思明 点击首页的“竞彩足球”图标
	}
}
