package com.ruyicai.activity.buy.expert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.jc.zq.JcGuoGuan;

import android.app.Activity;
import android.app.ActivityGroup;
import android.os.Bundle;

public class ExpertActivity extends BuyActivityGroup {
	public static Map<String, Object> expertMap = new HashMap<String, Object>();
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
