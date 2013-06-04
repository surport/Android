package com.ruyicai.activity.buy.zixuan;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.constant.Constants;

public class OrderDetails extends BuyActivityGroup {
	private String[] titles ={"投注","追号","合买","赠送"};
	private String[] topTitles ={"投注确认","追号设置","发起合买","赠送彩票"};
	private Class[] allId ={ZiXuanTouZhu.class,ZixuanZhuihao.class,JoinStartActivity.class,GiftActivity.class};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Constants.type="fc";
		isIssue(false);
	    init(titles, topTitles, allId);
	}
   
	public void initView() {
		relativeLayout = (RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		relativeLayout1=(RelativeLayout) findViewById(R.id.last_batchcode);

	}

}
