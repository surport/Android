/**
 * 
 */
package com.ruyicai.activity.buy.qlc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.constant.Constants;

/**
 * 七乐彩
 * 
 * @author Administrator
 * 
 */
public class Qlc extends BuyActivityGroup {

	private String[] titles = { "自选", "胆拖", "机选" };
	private String[] topTitles = { "七乐彩", "七乐彩", "七乐彩" };
	private Class[] allId = { QlcZiZhiXuan.class, QlcZiDanTuo.class,QlcJiXuan.class };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_QLC);
		init(titles, topTitles, allId);
		setIssue(Constants.LOTNO_QLC);
	}

	public boolean getIsLuck() {
		return true;
	}
}
