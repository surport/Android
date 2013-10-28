package com.ruyicai.activity.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;

public class NoticeZCActivity extends BuyActivityGroup {
	int lotnoZC[] = { NoticeActivityGroup.ID_SUB_SFC_LISTVIEW,
			NoticeActivityGroup.ID_SUB_RXJ_LISTVIEW,
			NoticeActivityGroup.ID_SUB_LCB_LISTVIEW,
			NoticeActivityGroup.ID_SUB_JQC_LISTVIEW};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIssue(false);
		String[] topTitles8 = { "足彩开奖公告", "足彩开奖公告", "足彩开奖公告", "足彩开奖公告" };
		String[] titles8 = { "胜负彩", "任选九", "六场半", "进球彩" };
		Class[] allId8 = { NoticeInfoActivity.class, NoticeInfoActivity.class,
				NoticeInfoActivity.class, NoticeInfoActivity.class };
		init(titles8, topTitles8, allId8);
		tabOnclick();
		mTabHost.setTag(0);
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
	}

	/**
	 * 添加标签
	 * 
	 * @param index
	 * @param id
	 */
	public void addTab(int index) {
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab
				.findViewById(R.id.layout_nav_item);
		topTitle = (TextView) indicatorTab
				.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		topTitle.setText(titles[index]);
		Intent intent = new Intent(this, allId[index]);
		intent.putExtra("lotnoZC", lotnoZC[index]);
		firstSpec = mTabHost.newTabSpec(titles[index])
				.setIndicator(indicatorTab).setContent(intent);
		mTabHost.addTab(firstSpec);
	}

	public void tabOnclick() {
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for (int i = 0; i < titles.length; i++) {
					if (tabId.equals(titles[i])) {
						title.setText(topTitles[i]);
						return;
					}
				}
			}
		});
	}
}
