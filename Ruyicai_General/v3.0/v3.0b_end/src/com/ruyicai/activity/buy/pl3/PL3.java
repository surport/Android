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
	private String[] titles ={"直选","组三","组六","机选"};
	private String[] topTitles ={"排列三直选","排列三组三","排列三组六","排列三机选"};
	public static int iCurrentButton;
	private Class[] allId ={PL3ZhiXuan.class,PL3ZuSan.class,PL3ZuLiu.class,PL3JiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_PL3);
	}
}
