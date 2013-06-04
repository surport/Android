package com.ruyicai.activity.buy.jc.explain.lq;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.Toast;

import com.ruyicai.activity.buy.jc.explain.zq.AsiaActivity;
import com.ruyicai.activity.buy.jc.explain.zq.EuropeActivity;
import com.ruyicai.activity.buy.jc.explain.zq.ExplainListActivity;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.ExplainInterface;

public class JcLqExplainActivity extends JcExplainActivity {
	public static int Lq_TYPE;
	public static final int LQ_SF = 0;
	public static final int LQ_RFSF = 1;
	public static final int LQ_DX = 2;
	public static final int LQ_SFC = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setTitleText() {
		title.setText(titleStr);
		titles = new String[] { "分析", "欧指", "让分", "总分" };
		allId = new Class[] { LqExplainListActivity.class,
				LqEuropeActivity.class, LqScoreActivity.class,
				LqAllScoreActivity.class };
		type = "dataAnalysisJcl";
	}
}
