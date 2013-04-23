/**
 * 
 */
package com.ruyicai.activity.buy.ssq;
import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;

public class Ssq extends BuyActivityGroup {
	
	private String[] titles ={"直选","胆拖","机选"};
	private String[] topTitles ={"双色球直选","双色球胆拖","双色球机选"};
	private Class[] allId ={SsqZiZhiXuan.class,SsqZiDanTuo.class,SsqJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_SSQ);
	}
}
