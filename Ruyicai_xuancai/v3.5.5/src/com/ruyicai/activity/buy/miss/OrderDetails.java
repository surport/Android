package com.ruyicai.activity.buy.miss;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.BuyActivityGroup;

public class OrderDetails extends BuyActivityGroup {
	private String[] titles ={"投注","追号","合买","赠送"};
	private String[] topTitles ={"投注确认","追号设置","发起合买","赠送彩票"};
	private Class[] allId ={ZiXuanTouZhu.class,ZixuanZhuihao.class,JoinStartActivity.class,GiftActivity.class};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ZiXuanTouZhu.type="fc";
		JoinStartActivity.type="fc";
		GiftActivity.type="fc";
		isIssue(false);
	    init(titles, topTitles, allId);
	}
   
	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);

	}

}
