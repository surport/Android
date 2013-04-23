package com.ruyicai.activity.buy.ssc;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

public class Ssc extends BuyActivityGroup implements HandlerMsg {
	
	private String[] titles ={"一星","二星","三星","五星","大小"};
	private String[] topTitles ={"时时彩一星","时时彩二星","时时彩三星","时时彩五星","时时彩大小双单"};
	public  static  String  batchCode;
	private int   lesstime = 0;
	private Thread thread;
	private Timer timer;
	private Class[] allId ={SscOneStar.class,SscTwoStar.class,SscThreeStar.class,SscFiveStar.class,SscBigSmall.class};
	private MyHandler handler = new MyHandler(this);
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		time=(TextView)findViewById(R.id.layout_main_text_timessc);
		time.setVisibility(View.VISIBLE);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {	
	        if(tabId.equals(titles[0])){
	          title.setText(topTitles[0]);
	          SscOneStar.self.theMethodYouWantToCall();
	        }else if(tabId.equals(titles[0])){
		          title.setText(topTitles[0]);
		          SscOneStar.self.theMethodYouWantToCall();
	        }else if(tabId.equals(titles[1])){
		          title.setText(topTitles[1]);
		          SscTwoStar.self.theMethodYouWantToCall();
	        }else if(tabId.equals(titles[2])){
		          title.setText(topTitles[2]);
		          SscThreeStar.self.theMethodYouWantToCall();
	        }else if(tabId.equals(titles[3])){
		          title.setText(topTitles[3]);
		          SscFiveStar.self.theMethodYouWantToCall();
	        }else if(tabId.equals(titles[4])){
		          title.setText(topTitles[4]);
		          SscBigSmall.self.theMethodYouWantToCall();
	        }	           
			}
		});
        init(titles, topTitles, allId);
        setIssue();
       
    }   	

	/**
	 * 赋值给当前期
	 * @param type彩种编号
	 */
	public void setIssue(){
		issue.setText("期号获取中....");
		time.setText("获取中...");
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				String re = "";
				String message="";
					re = GetLotNohighFrequency.getInstance().getInfo(Constants.LOTNO_SSC);
					if (!re.equalsIgnoreCase("")) {
						try {
							JSONObject obj = new JSONObject(re);
							message = obj.getString("message");
					        error_code = obj.getString("error_code");
					        lesstime = Integer.valueOf(obj.getString("time_remaining"));
							batchCode = obj.getString("batchcode");	
							handler.post(new Runnable(){
								public void run() {
								issue.setText("第" + batchCode + "期");
								}});
							TimerTask task = new TimerTask(){
							public void run() {
							handler.post(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									//issue.setText("第" + "00000000" + "期");
									time.setText("剩余时间:" + PublicMethod.isTen(lesstime/60)+":"+PublicMethod.isTen(lesstime%60));
									lesstime--;
									if(lesstime==0){
										timer.cancel();timer=null;
									new AlertDialog.Builder(Ssc.this).setTitle("提示").setMessage("时时彩第"+batchCode+"期已经结束,是否转入下一期")
									.setNegativeButton("转入下一期", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
											setIssue();
										}
									}).setNeutralButton("返回主页面", new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
											// TODO Auto-generated method stub
										 Ssc.this.finish();
										}
									}).create().show();
									}
								}
							});							
			             	}};
							timer = new Timer(true);
						    timer.schedule(task, 0, 1000);
						}catch (Exception e) {
							handler.post(new Runnable() {
								public void run() {
									issue.setText("获取期号失败");
									time.setText("获取失败");
						} });
						}
						handler.handleMsg(error_code, message);
					} else {
						
					}
			}
		});
		thread.start();
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
       
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	/* (non-Javadoc)
	 * @see android.app.ActivityGroup#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(timer!=null){
			timer.cancel();
		}
	}
    
}