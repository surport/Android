package com.ruyicai.activity.buy.eleven;

import android.os.Bundle;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.code.eleven.ElevenCode;
import com.ruyicai.jixuan.ElevenBalls;
import com.ruyicai.util.Constants;

public class Eleven extends Dlc{
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitleOne(getString(R.string.eleven));
		
	}
	/**
	 * 设置彩种编号
	 * @param lotno
	 */
    public void setLotno(){
    	this.lotno = Constants.LOTNO_eleven;
    	lotnoStr = lotno;
    }
	/**
	 * 初始化自选选区
	 */
	public void createViewZx(){
		iProgressBeishu = 1;iProgressQishu = 1;
		sscCode = new ElevenCode();
		initArea();
		createView(areaInfos, sscCode,this,ZixuanAndJiXuan.NULL,false);
	}
	/**
	 * 初始化机选选区
	 */	
	public void createViewJx(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		ElevenBalls dlcb = new ElevenBalls(num);
		createviewmechine(dlcb,this);
	}
	   /**
	    * 投注注码
	    * @return
	    */
	   public String getZhuma(){
		   String zhuma="";
		   if(isJiXuan){
			zhuma = ElevenBalls.getZhuma(balls, state);
		   }else {
		    zhuma = ElevenCode.zhuma(areaNums, state);
		   }
		   return zhuma;
	   }

}
