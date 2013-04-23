package com.ruyicai.activity.buy.zc;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

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
		setLotno(lotno);
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
        MobclickAgent.onEvent(this,"zucai" ); //BY贺思明 点击首页的“足彩”图标
	}
	
}
