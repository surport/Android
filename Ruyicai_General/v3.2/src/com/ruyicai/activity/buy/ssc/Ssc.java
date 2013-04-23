package com.ruyicai.activity.buy.ssc;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.NoticeHistroy;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.GetLotNohighFrequency;
import com.ruyicai.util.PublicMethod;

public class Ssc extends BuyActivityGroup implements HandlerMsg {
	
	private String[] titles ={"一星","二星","三星","五星","大小"};
	private String[] topTitles ={"时时彩","时时彩","时时彩","时时彩","时时彩"};
	public  static  String  batchCode;
	private int   lesstime = 0;
	private Class[] allId ={SscOneStar.class,SscTwoStar.class,SscThreeStar.class,SscFiveStar.class,SscBigSmall.class};
	private MyHandler handler = new MyHandler(this);
	private boolean isRun = true;//线程是否运行变量
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		time=(TextView)findViewById(R.id.layout_main_text_timessc);
		time.setVisibility(View.VISIBLE);
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {	
	        if(tabId.equals(titles[0])){
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
	private void initView(){
		relativeLayout =(RelativeLayout) findViewById(R.id.main_buy_relat_issue);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		issue = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		imgRetrun = (Button) findViewById(R.id.layout_main_img_return);
		imgRetrun.setText("历史开奖");
		imgRetrun.setVisibility(View.VISIBLE);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
	    //ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(Ssc.this,NoticeHistroy.class);
				intent.putExtra("lotno", Constants.LOTNO_SSC);
				startActivity(intent);
			}
		});		
	}
	/**
	 * 赋值给当前期
	 * @param type彩种编号
	 */
	public void setIssue(){
		final Handler sscHandler = new Handler();
		issue.setText("期号获取中....");
		time.setText("获取中...");
		Thread thread = new Thread(new Runnable() {
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
							while(isRun){
								if(isEnd(lesstime)){
									sscHandler.post(new Runnable(){
										public void run() {
										issue.setText("第" + batchCode + "期");
										time.setText("剩余时间:" + PublicMethod.isTen(lesstime/60)+":"+PublicMethod.isTen(lesstime%60));
										}});
									Thread.sleep(1000);
									lesstime--;
								}else{
									sscHandler.post(new Runnable() {
										public void run() {
											issue.setText("第" + batchCode + "期");
											time.setText("剩余时间:00:00");	
											nextIssue();
								      }});
									break;
								}
							}
						}catch (Exception e) {
							sscHandler.post(new Runnable() {
								public void run() {
									issue.setText("获取期号失败");
									time.setText("获取失败");
						}});
						}
					} else {
						
					}
			}
		});
		thread.start();
	}
	private boolean isEnd(int time) {
		if(time>0){
			return true;
		}else{
			return false;
		}
	}
	private void nextIssue() {
		new AlertDialog.Builder(Ssc.this).setTitle("提示").setMessage("时时彩第"+batchCode+"期已经结束,是否转入下一期")
		        .setNegativeButton("转入下一期", new Dialog.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setIssue();
			}
		
		}).setNeutralButton("返回主页面", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			 Ssc.this.finish();
			}
		 }).create().show();
	}
	public void updatePage() {
		Intent intent = new Intent(Ssc.this, Ssc.class);
		Ssc.this.startActivity(intent);
		finish();
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
//	/**
//	 * 重写放回建
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		switch (keyCode) {
//		case 4:
//			isRun = false;
//			Log.e("isRun","====");
//			finish();
//			break;
//		}
//		return false;
//	}
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
		isRun = false;
	}
    
}