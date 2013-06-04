/**
 * 
 */
package com.ruyicai.activity.buy.ssq;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.constant.Constants;

public class Ssq extends BuyActivityGroup {
	
	private String[] titles ={"自选","胆拖"};
	private String[] topTitles ={"双色球","双色球"};
	private Class[] allId ={SsqZiZhiXuan.class,SsqZiDanTuo.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_SSQ);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_SSQ);
	}
	public boolean getIsLuck(){
		return true;
	}
}
