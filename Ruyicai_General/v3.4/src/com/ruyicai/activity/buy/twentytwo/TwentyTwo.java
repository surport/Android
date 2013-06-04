package com.ruyicai.activity.buy.twentytwo;

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
import com.ruyicai.activity.buy.ssq.SsqJiXuan;
import com.ruyicai.activity.buy.ssq.SsqZiDanTuo;
import com.ruyicai.activity.buy.ssq.SsqZiZhiXuan;
import com.ruyicai.constant.Constants;

public class TwentyTwo  extends BuyActivityGroup {
		
		private String[] titles ={"自选","胆拖","机选"};
		private String[] topTitles ={"22选5","22选5","22选5"};
		private Class[] allId ={TwentyZiXuan.class,TwentyDanTuo.class,TwentyJiXuan.class};
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setLotno(Constants.LOTNO_22_5);
	        init(titles, topTitles, allId);
	        setIssue(Constants.LOTNO_22_5);
		}
	}



