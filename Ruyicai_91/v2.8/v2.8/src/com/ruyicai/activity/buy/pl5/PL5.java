package com.ruyicai.activity.buy.pl5;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.pl3.PL3JiXuan;
import com.ruyicai.activity.buy.pl3.PL3ZiZhiXuan;
import com.ruyicai.util.Constants;

/**
 * 排列五
 * @author miao
 *
 */
public class PL5 extends BuyActivityGroup{
	private String[] titles ={"直选","机选"};
	private String[] topTitles ={"排列五直选","排列五机选"};
	private Class[] allId ={PL5ZiZhiXuan.class,PL5ZiJX.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_PL5);
	}
}
