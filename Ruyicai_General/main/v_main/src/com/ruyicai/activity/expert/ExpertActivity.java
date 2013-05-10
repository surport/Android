package com.ruyicai.activity.expert;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.umeng.analytics.MobclickAgent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 专家荐号
 * 
 * @author Administrator
 * 
 */
public class ExpertActivity extends BuyActivityGroup {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIssue(false);
		initExpertView();
		MobclickAgent.onEvent(this, "zhuanjiajianhao"); // BY贺思明 点击首页的“专家荐号”图标
	}

	public void initExpertView() {
		removeTabs();
		String[] titles = { getString(R.string.expert_one_title),
				getString(R.string.expert_two_title),
				getString(R.string.expert_third_title),
				getString(R.string.expert_four_title) };
		String[] topTitles = { getString(R.string.expert_top_title),
				getString(R.string.expert_top_title),
				getString(R.string.expert_top_title),
				getString(R.string.expert_top_title) };
		Class[] allId = { ExpertInfoActivity.class, ExpertInfoActivity.class,
				ExpertInfoActivity.class, ExpertInfoActivity.class };
		init(titles, topTitles, allId, 16);
	}

	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);

	}
}
