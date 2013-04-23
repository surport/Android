package com.ruyicai.activity.buy.zc;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.constant.Constants;

public class FootballLottery extends BuyActivityGroup{

	RelativeLayout  fatherIssueLinear ;
	String lotno=Constants.LOTNO_SFC;
	private String[] titles ={"胜负彩","任选九","进球彩","六场半"};
	private String[] topTitles ={"足彩-胜负彩","足彩-任选九","足彩-进球彩","足彩-六场半"};
	private String[] batch = {Constants.LOTNO_SFC,Constants.LOTNO_RX9,Constants.LOTNO_JQC,Constants.LOTNO_LCB};
	private Class[] allId ={FootballSFLottery.class,FootballChooseNineLottery.class,FootballGoalsLottery.class,FootballSixSemiFinal.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fatherIssueLinear = (RelativeLayout)findViewById(R.id.main_buy_relat_issue);
		fatherIssueLinear.setVisibility(View.GONE);
		initView();
        init(titles, topTitles, allId);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for(int i=0;i<titles.length;i++){
					if(tabId.equals(titles[i])){
						title.setText(topTitles[i]);
						lotno =batch[i];
						return;
					}
				}
			}
		});
	}
	
	private void initView(){
		relativeLayout =(RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
		imgRetrun.setText("历史开奖");
		imgRetrun.setVisibility(View.VISIBLE);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
	    //ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(FootballLottery.this,NoticeHistroy.class);
				intent.putExtra("lotno", lotno);
				startActivity(intent);
			}
		});		
	}
}
