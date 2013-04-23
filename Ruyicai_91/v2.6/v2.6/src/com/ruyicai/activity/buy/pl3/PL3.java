/**
 * 
 */
package com.ruyicai.activity.buy.pl3;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;

/**
 * 排列三
 * @author Administrator
 *
 */
public class PL3 extends BuyActivityGroup{
	private String[] titles ={"直选","组选","和值","机选"};
	private String[] topTitles ={"排列三直选","排列三组选","排列三和值","排列三机选"};
	private Class[] allId ={PL3ZiZhiXuan.class,PL3ZiZuXuan.class,PL3ZiHeZhi.class,PL3JiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_PL3);
	}
}
