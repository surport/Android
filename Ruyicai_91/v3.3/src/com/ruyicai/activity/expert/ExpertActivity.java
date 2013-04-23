package com.ruyicai.activity.expert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyActivityGroup;

import android.app.Activity;
import android.app.ActivityGroup;
import android.os.Bundle;
/**
 * ×¨¼Ò¼öºÅ
 * @author Administrator
 *
 */
public class ExpertActivity extends BuyActivityGroup {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIssue(false);
		initView();
	}

	public void initView() {
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
		init(titles, topTitles, allId,16);
	}
}
