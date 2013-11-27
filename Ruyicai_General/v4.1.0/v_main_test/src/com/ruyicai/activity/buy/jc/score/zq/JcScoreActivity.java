package com.ruyicai.activity.buy.jc.score.zq;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class JcScoreActivity extends BuyActivityGroup {
	private String titleStr = "即时比分";
	private String[] titles = { "全部", "未开赛", "进行中", "已完场", "关注" };
	private String[] topTitles = { titleStr, titleStr, titleStr, titleStr,
			titleStr };
	protected Class[] allId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initTop();
		isIssue(false);
		init(titles, topTitles, allId);
	}

	public void initTop() {
		allId = new Class[] { JcScoreListActivity.class,
				JcScoreListActivity.class, JcScoreListActivity.class,
				JcScoreListActivity.class, TrackActivity.class };
	}

	/**
	 * 初始化组件
	 */
	public void initView() {
		relativeLayout1 = (RelativeLayout) findViewById(R.id.last_batchcode);
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		imgIcon = (Button) findViewById(R.id.layout_main_img_return);
		imgIcon.setBackgroundResource(R.drawable.refresh_last_code);
//		imgIcon.setBackgroundResource(R.drawable.returnselecter);
//		imgIcon.setText("返回");
//		imgIcon.setWidth(PublicMethod.getPxInt(70, context));
		imgIcon.setVisibility(View.VISIBLE);
		// ImageView的返回事件
		imgIcon.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(JcScoreListActivity.BROADCAST_ACTION);
				sendBroadcast(intent);
//				finish();
			}
		});
	}

	public boolean getIsLuck() {
		return true;
	}
}
