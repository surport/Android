/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;

/**
 * 七乐彩
 * @author Administrator
 *
 */
public class Qlc extends BuyActivityGroup{
	
	private String[] titles ={"自选","胆拖","机选"};
	private String[] topTitles ={"七乐彩自选","七乐彩胆拖","七乐彩机选"};
	private Class[] allId ={QlcZiZhiXuan.class,QlcZiDanTuo.class,QlcJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_QLC);
	}
}
