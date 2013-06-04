/**
 * 
 */
package com.ruyicai.activity.buy.ssq;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.constant.Constants;

public class Ssq extends BuyActivityGroup {
	
	private String[] titles ={"自选","胆拖","机选"};
	private String[] topTitles ={"双色球","双色球","双色球"};
	private Class[] allId ={SsqZiZhiXuan.class,SsqZiDanTuo.class,SsqJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_SSQ);
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
				Intent intent = new Intent(Ssq.this,NoticeHistroy.class);
				intent.putExtra("lotno", Constants.LOTNO_SSQ);
				startActivity(intent);
			}
		});		
	}
}
