/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;

/**
 * 七乐彩
 * 
 * @author Administrator
 * 
 */
public class Qlc extends BuyActivityGroup {

	private String[] titles = { "自选", "胆拖" };
	private String[] topTitles = { "七乐彩", "七乐彩" };
	private Class[] allId = { QlcZiZhiXuan.class, QlcZiDanTuo.class };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_QLC);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_QLC);
		setlastbatchcode(Constants.LOTNO_QLC);
	}

}
