package com.ruyicai.activity.buy.jc.explain.lq;

import android.os.Bundle;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.buy.jc.explain.zq.RecommendActivity;
import com.ruyicai.constant.Constants;

public class JcLqExplainActivity extends JcExplainActivity {
	public static int Lq_TYPE;
	public static final int LQ_SF = 0;
	public static final int LQ_RFSF = 1;
	public static final int LQ_DX = 2;
	public static final int LQ_SFC = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		event = getIntent().getStringExtra("event");//add by yejc 20130423
	}

	public void setTitleText() {
		title.setText(titleStr);
		titles = new String[] { "分析", "欧指", "让分", "总分", "推荐"};
		allId = new Class[] { LqExplainListActivity.class,
				LqEuropeActivity.class, LqScoreActivity.class,
				LqAllScoreActivity.class, RecommendActivity.class};
		type = "dataAnalysisJcl";
		Constants.currentTickType = "jingCai";
	}
}
