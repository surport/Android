package com.ruyicai.activity.buy.dlt;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;



public class Dlt extends BuyActivityGroup{
	private String[] titles ={"自选","胆拖","机选","12选2"};
	private String[] topTitles ={"大乐透自选","大乐透胆拖","大乐透机选","大乐透12选2"};
	private Class[] allId ={DltNormalSelect.class,DltDantuoSelect.class,DltRandomChose.class,DltTwoInDozenSelect.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_DLT);
	}
}
