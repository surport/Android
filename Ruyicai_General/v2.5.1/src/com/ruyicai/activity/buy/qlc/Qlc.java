/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.util.Constants;

/**
 * ∆ﬂ¿÷≤ 
 * @author Administrator
 *
 */
public class Qlc extends BuyActivityGroup{
	
	private String[] titles ={"÷±—°","µ®Õœ","ª˙—°"};
	private String[] topTitles ={"∆ﬂ¿÷≤ ÷±—°","∆ﬂ¿÷≤ µ®Õœ","∆ﬂ¿÷≤ ª˙—°"};
	private Class[] allId ={QlcZiZhiXuan.class,QlcZiDanTuo.class,QlcJiXuan.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_QLC);
	}
}
