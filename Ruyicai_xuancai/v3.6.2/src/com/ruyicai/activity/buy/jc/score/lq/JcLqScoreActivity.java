package com.ruyicai.activity.buy.jc.score.lq;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity;
import com.ruyicai.activity.buy.jc.score.zq.TrackActivity;
import com.ruyicai.activity.buy.ssq.SsqJiXuan;
import com.ruyicai.activity.buy.ssq.SsqZiDanTuo;
import com.ruyicai.activity.buy.ssq.SsqZiZhiXuan;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JcLqScoreActivity extends JcScoreActivity{
	
	public void initTop(){
		allId = new Class[]{JcLqScoreListActivity.class,JcLqScoreListActivity.class,
				           JcLqScoreListActivity.class,JcLqScoreListActivity.class};
	}
}
