/**
 * 
 */
package com.ruyicai.activity.buy.pl3;

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
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.constant.Constants;

/**
 * 排列三
 * @author Administrator
 *
 */
public class PL3 extends BuyActivityGroup{
	private String[] titles ={"直选","组三","组六","机选"};
	private String[] topTitles ={"排列三","排列三","排列三","排列三"};
	public static int iCurrentButton;
	private Class[] allId ={PL3ZhiXuan.class,PL3ZuSan.class,PL3ZuLiu.class,PL3JiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_PL3);
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
				Intent intent = new Intent(PL3.this,NoticeHistroy.class);
				intent.putExtra("lotno", Constants.LOTNO_PL3);
				startActivity(intent);
			}
		});		
	}
}
