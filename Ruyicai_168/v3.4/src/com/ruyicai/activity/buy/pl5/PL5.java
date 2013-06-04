package com.ruyicai.activity.buy.pl5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.twentytwo.TwentyTwo;
import com.ruyicai.constant.Constants;

/**
 * 排列五
 * @author miao
 *
 */
public class PL5 extends BuyActivityGroup{
	private String[] titles ={"自选","机选"};
	private String[] topTitles ={"排列五","排列五"};
	private Class[] allId ={PL5ZiZhiXuan.class,PL5ZiJX.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_PL5);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_PL5);
	}
}
