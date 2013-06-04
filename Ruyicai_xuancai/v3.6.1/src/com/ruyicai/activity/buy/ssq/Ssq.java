/**
 * 
 */
package com.ruyicai.activity.buy.ssq;
import android.os.Bundle;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.constant.Constants;
import com.umeng.analytics.MobclickAgent;

public class Ssq extends BuyActivityGroup {
	
	private String[] titles ={"自选","胆拖"};
	private String[] topTitles ={"双色球","双色球"};
	private Class[] allId ={SsqZiZhiXuan.class,SsqZiDanTuo.class};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLotno(Constants.LOTNO_SSQ);
        init(titles, topTitles, allId);
        setIssue(Constants.LOTNO_SSQ);
        MobclickAgent.onEvent(this,"shuangseqiu" ); //BY贺思明 点击首页的“双色球”图标
        MobclickAgent.onEvent(this,"fucaigoucaijiemian" );//BY贺思明 福彩购彩页面
	}
	public boolean getIsLuck(){
		return true;
	}
}
