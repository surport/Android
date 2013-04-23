package com.ruyicai.activity.buy.eleven;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.code.dlc.DlcCode;
import com.ruyicai.code.eleven.ElevenCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.MissConstant;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.DlcRxBalls;
import com.ruyicai.jixuan.ElevenBalls;

public class Eleven extends Dlc{
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buy_dlc_main);
		highttype = "DLC";
		setLotno();
		initTop();
		initSpinner();
		initGroup();
		setTitleOne(getString(R.string.eleven));
		
		
	}
	public void updatePage() {
		Intent intent = new Intent(Eleven.this, Eleven.class);
		startActivity(intent);
		finish();
	}
	/**
	 * 设置遗漏值类别
	 */
	public void setSellWay(){
		if(state.equals("Q2")||state.equals("Q3")||state.equals("R1")){
			if(!sellWay.equals(MissConstant.ELV_MV_Q3)){
				sellWay = MissConstant.ELV_MV_Q3;
				missList.clear();
			}
		}else if(state.equals("Z2")){
			if(!sellWay.equals(MissConstant.ELV_MV_Q2Z)){
				sellWay = MissConstant.ELV_MV_Q2Z;
				missList.clear();
			}
		}else if(state.equals("Z3")){
			if(!sellWay.equals(MissConstant.ELV_MV_Q3Z)){
				sellWay = MissConstant.ELV_MV_Q3Z;
				missList.clear();
			}
		}else{
			if(!sellWay.equals(MissConstant.ELV_MV_RX)){
				sellWay = MissConstant.ELV_MV_RX;
				missList.clear();
			}
		}
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
		createView(areaNums, sscCode,ZixuanAndJiXuan.NULL,true);
	}
	/**
	 * 初始化机选选区
	 */	
	public void createViewJx(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		ElevenBalls dlcb = new ElevenBalls(num);
		createviewmechine(dlcb);
	}
	   /**
	    * 投注注码
	    * @return
	    */
	   public String getZhuma(){
		   String zhuma="";
		    zhuma = ElevenCode.zhuma(areaNums, state);
		   return zhuma;
	   }
		/**
		 * 机选投注注码
		 */
		public String getZhuma(Balls ball) {
			   String zhuma="";
				zhuma = ElevenBalls.getZhuma(ball, state);
			   return zhuma;
		}
	   
		private void initTop(){
			titleOne = (TextView) findViewById(R.id.layout_main_text_title_one);
			issue = (TextView) findViewById(R.id.layout_main_text_issue);
			time = (TextView) findViewById(R.id.layout_main_text_time);
			imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
			imgRetrun.setBackgroundResource(R.drawable.returnselecter);
			imgRetrun.setText("历史开奖");
			imgRetrun.setVisibility(View.VISIBLE);
	        titleOne.setText(getString(R.string.dlc));
		    //ImageView的返回事件
			imgRetrun.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Eleven.this,NoticeHistroy.class);
					intent.putExtra("lotno", Constants.LOTNO_eleven);
					startActivity(intent);			}
			});		
		}

}
