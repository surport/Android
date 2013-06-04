/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.palmdream.RuyicaiAndroid;

import java.io.InputStream;
import java.util.Calendar;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.palmdream.netintface.Jsoninformation;
import com.palmdream.netintface.jrtLot;

public class HomePage extends Activity {
	private ShellRWSharesPreferences shellRW;

	// 处理消息
	public Handler mHandler = new Handler();

	// cyhx logo image
	ImageView imageview;

	MyAnimateView iAnimateView;

	// alpha通道变量
	int alpha = 255;
	// 显示状态
	// 0 - 不变
	// 1 - 渐变
	// 2 - 跳转 Activity
	int iShowStatus = 0;
	public boolean isHint = false;
	boolean b = false;

	// 实现网络的检测，如果没有网络则显示一个对话框，提示进行网络设置。
	CheckWireless checkWireless;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.homepage);
		// else {
		// ShowConnectionDialog showConnectionDialog = new
		// ShowConnectionDialog(this);
		// showConnectionDialog.showConnectionDialog(this);
		// }
		// 初始化imageview
		imageview = (ImageView) this.findViewById(R.id.cp1);

		iAnimateView = new MyAnimateView(this);

		// 设置imageview的alpha通道
		imageview.setAlpha(alpha);

		// imageview淡出线程
		new Thread(new Runnable() {
			public void run() {
				// initApp();
				while (iShowStatus < 2) {
					try {
						if (iShowStatus == 0) {
							Thread.sleep(2700);
							iShowStatus = 1;
						} else {
							Thread.sleep(20);
						}
						if (updateApp()) {
							return;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		checkWireless = new CheckWireless(this);

		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0: {
					// 实现渐变
					imageview.setAlpha(alpha);
					imageview.invalidate();
					break;
				}
				case 1: {
					PublicMethod.myOutput("-----new image");
					/*
					 * imageview.setImageResource(R.drawable.cp1); alpha = 255;
					 * imageview.setAlpha(alpha); imageview.invalidate();
					 */
					// setContentView(new MyAnimateView(HomePage.this));
					setContentView(iAnimateView);
					iAnimateView.invalidate();
					iShowStatus = 3;

					checkWirelessNetwork();
					break;
				}
				case 2: {
					try {
						showalert();
					} catch (Exception e) {
						e.printStackTrace();// 显示提示框（没有网络，用户是否继续 ） 2010/7/2 陈晨
					}
					// Intent in = new Intent(HomePage.this,
					// RuyicaiAndroid.class);
					// startActivity(in);
					// HomePage.this.finish();
					break;
				}
				case 3: {
					PublicMethod.myOutput("----comeback");
					// setContentView(iAnimateView);
					iAnimateView.invalidate();
					Message mg = Message.obtain();
					mg.what = 5;
					mHandler.sendMessage(mg);
					// saveInformation();
					break;
				}
				case 4: {
					uvNumber();// uv统计方法
					Intent in = new Intent(HomePage.this, RuyicaiAndroid.class);
					startActivity(in);
					HomePage.this.finish();
					break;
				}
				case 5: {
					saveInformation();
					break;
				}
				}
			}
		};
	}

	/*
	 * UV统计
	 */
	public void uvNumber() {
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		// uv统计
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean start = false;
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR); // 获取当前年份
				int mMonth = c.get(Calendar.MONTH);// 获取当前月份
				int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
				// Log.e("-------====------", "" + mYear + "," + mMonth + ","
				// + mDay);
				if (shellRW.getUserLoginInfo("mYear") == null
						|| shellRW.getUserLoginInfo("mMonth") == null
						|| shellRW.getUserLoginInfo("mDay") == null) {
					start = true;
					shellRW.setUserLoginInfo("mYear", Integer.toString(mYear));
					shellRW
							.setUserLoginInfo("mMonth", Integer
									.toString(mMonth));
					shellRW.setUserLoginInfo("mDay", Integer.toString(mDay));
					Log.e("===:", shellRW.getUserLoginInfo("mYear") + ","
							+ shellRW.getUserLoginInfo("mMonth") + ","
							+ shellRW.getUserLoginInfo("mDay"));

				} else if (mYear > Integer.parseInt(shellRW
						.getUserLoginInfo("mYear"))) {
					start = true;
					shellRW.setUserLoginInfo("mYear", Integer.toString(mYear));
				} else if (mMonth > Integer.parseInt(shellRW
						.getUserLoginInfo("mMonth"))) {
					start = true;
					shellRW
							.setUserLoginInfo("mMonth", Integer
									.toString(mMonth));
				} else if (mDay > Integer.parseInt(shellRW
						.getUserLoginInfo("mDay"))) {
					start = true;
					shellRW.setUserLoginInfo("mDay", Integer.toString(mDay));
				}
				if (start) {
					try {
						Log.e("-------====------", "start=" + start);
						jrtLot.setPara(100, jrtLot.channel_id);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	private void checkWirelessNetwork() {
		shellRW = new ShellRWSharesPreferences(this, "addInfo");

		if (!checkWireless.getConnectGPRS() && !checkWireless.getConnectWIFI()) {
			PublicMethod.myOutput("--------------ShowNoConnectionDialog");
			ShowNoConnectionDialog showNoConnectionDialog = new ShowNoConnectionDialog(
					this, this);
			showNoConnectionDialog.showNoConnectionDialog();
			// this.finish();
		} else {

			PublicMethod.myOutput("------------noHint"
					+ shellRW.getUserLoginInfo("noHint"));
			try {
				if (shellRW.getUserLoginInfo("noHint") != null) {
					if (shellRW.getUserLoginInfo("noHint").equals("false")) {
						ShowConnectionDialog showConnectionDialog = new ShowConnectionDialog(
								this, this, shellRW);
						showConnectionDialog.showConnectionDialog(this);
					} else {
						Message mg = Message.obtain();
						mg.what = 3;
						// mHandler.sendMessage(mHandler.obtainMessage());
						mHandler.sendMessage(mg);
					}
				} else {
					ShowConnectionDialog showConnectionDialog = new ShowConnectionDialog(
							this, this, shellRW);

					showConnectionDialog.showConnectionDialog(this);
				}
			} catch (Exception e) {

				PublicMethod.myOutput("------exception e-----???????-");
			}

		}

	}

	// 渐变处理函数
	public boolean updateApp() {
		Message mg = Message.obtain();
		mg.what = 0;
		// handler.sendMessage(mg);

		alpha -= 5;
		if (alpha <= 0) {
			iShowStatus = 2;
			/*
			 * // 启动封面页 Intent in = new Intent(this, RuyicaiAndroid.class);
			 * startActivity(in); // 终止当前Activity this.finish();
			 */
			// --- imageview.setImageResource(R.drawable.cp1);
			mg.what = 1;
		}

		if (iShowStatus == 3) {
			// mg.what=2;
			return true;
		}

		// mHandler.sendMessage(mHandler.obtainMessage());
		mHandler.sendMessage(mg);
		return false;
	}

	private static class MyAnimateView extends View {
		private AnimateDrawable mDrawable;
		private Bitmap iBackgrounBitmap;
		private Paint p = null;

		private int iScreenWidth;
		private int iScreenHeight;
		private int iBackgroundWidth;
		private int iBackgrounHeight;
		private int iOffsetX;
		private int iOffsetY;

		private int iShowStringX = 100;
		private int iShowStringY = 390;

		public MyAnimateView(Context context) {
			super(context);

			iScreenWidth = PublicMethod.getDisplayWidth(context);
			iScreenHeight = PublicMethod.getDisplayHeight(context);

			setPaint();
			// setFocusable(true);
			// setFocusableInTouchMode(true);

			Drawable dr = context.getResources().getDrawable(R.drawable.light);
			// PublicMethod.myOutput("------==="+dr.getIntrinsicWidth()+" "+dr.getIntrinsicHeight());
			dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

			Resources r = context.getResources();
			InputStream is = r.openRawResource(R.drawable.cp2);
			BitmapDrawable bmpDraw = new BitmapDrawable(is);
			iBackgrounBitmap = bmpDraw.getBitmap();

			iBackgroundWidth = iBackgrounBitmap.getWidth();
			iBackgrounHeight = iBackgrounBitmap.getHeight();

			iOffsetX = (iScreenWidth - iBackgroundWidth) / 2;
			if (iOffsetX < 0)
				iOffsetX = 0;
			iOffsetY = (iScreenHeight - iBackgrounHeight) / 2;
			if (iOffsetY < 0)
				iOffsetY = 0;
			iShowStringX += iOffsetX;
			iShowStringY += iOffsetY;

			Animation an = new TranslateAnimation(iOffsetX + 58,
					iOffsetX + 242, iOffsetY + 366, iOffsetY + 366);
			an.setDuration(1000);
			an.setRepeatCount(-1);
			an.initialize(10, 10, 10, 10);

			mDrawable = new AnimateDrawable(dr, an);
			an.startNow();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// canvas.drawColor(Color.WHITE);
			// canvas.setBitmap(iBackgrounBitmap);
			canvas.drawBitmap(iBackgrounBitmap, iOffsetX, iOffsetY, null);
			mDrawable.draw(canvas);

			canvas.drawText("正在获取网络数据...", iShowStringX, iShowStringY, p);

			invalidate();
		}

		private void setPaint() {
			if (p == null) {
				p = new Paint();
				// String familyName = "宋体";
				// Typeface font =
				// Typeface.create(familyName,Typeface.NORMAL);//Typeface.BOLD);
				// font.
				p.setColor(Color.YELLOW);
				// p.setTypeface(font);
				p.setTypeface(Typeface.create(Typeface.SANS_SERIF,
						Typeface.NORMAL));
				p.setTextSize(13);
				// p.setUnderlineText(true);
			}
			/*
			 * float[] stringLength=new float[iShowString.length()];
			 * p.getTextWidths(iShowString, stringLength); float fTemp=0;
			 * for(int i1=0;i1<stringLength.length;i1++){
			 * fTemp+=stringLength[i1]; }
			 */
		}
	}

	/**
	 * 网络获取即时信息、开奖查询、当前期数的后台返回值
	 */
	public void saveInformation() {
		new Thread(new Runnable() {
			public void run() {
				Message mg = new Message();
				// mg.what=0;
				String[] info = Jsoninformation.getjsoninformation();
				PublicMethod.myOutput("---------------=====-----:" + info[1]);
				if (info[0].equalsIgnoreCase("notice")
						&& info[1].equalsIgnoreCase("ssqinfo")
						&& info[2].equalsIgnoreCase("dddinfo")
						&& info[3].equalsIgnoreCase("qlcinfo")
						&& info[4].equalsIgnoreCase("getlotno_ssq")
						&& info[5].equalsIgnoreCase("getlotno_ddd")
						&& info[6].equalsIgnoreCase("getlotno_qlc")
						&& info[7].equalsIgnoreCase("dltinfo")
						&& info[8].equalsIgnoreCase("pl3info")
						&& info[9].equalsIgnoreCase("getlotno_dlt")
						&& info[10].equalsIgnoreCase("getlotno_pl3")) {
					mg.what = 2;
				} else {
					shellRW = new ShellRWSharesPreferences(HomePage.this,
							"addInfo");
					for (int i = 0; i < info.length; i++) {
						shellRW.setUserLoginInfo("information" + i, info[i]);
					}
					PublicMethod.myOutput("-------------------shellRW"
							+ shellRW.getUserLoginInfo("information4"));
					mg.what = 4;
				}
				mHandler.sendMessage(mg);
			};
		}).start();
	}

	/**
	 * 网络异常提示框
	 */
	public void showalert() {
		Builder dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("网络出现异常").setPositiveButton("继续",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								shellRW = new ShellRWSharesPreferences(
										HomePage.this, "addInfo");
								for (int i = 0; i < 7; i++) {
									shellRW.setUserLoginInfo("information" + i,
											"");
								}
								Intent in = new Intent(HomePage.this,
										RuyicaiAndroid.class);
								startActivity(in);
								HomePage.this.finish();
							}

						}).setNegativeButton("退出",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// 添加退出响应事件 20100715 陈晨
								HomePage.this.finish();
							}

						});
		dialog.show();
	}
	// public boolean jsonanalysis(){
	// shellRW =new ShellRWSharesPreferences(this,"addInfo");
	// int iretrytimes=2;
	// String[] json =new String[7] ;
	// // String json;
	// for(int i=0;i<7;i++){
	// json[i]=shellRW.getUserLoginInfo("information"+i);
	// PublicMethod.myOutput("----------"+json[i]);
	// }
	// PublicMethod.myOutput("----------"+json[0]);
	// // while(iretrytimes<3&&iretrytimes>0){
	// try{
	// JSONObject obj = new JSONObject(json[0]);
	// for(int i=1;i<4;i++){
	// JSONArray jsonObject3=new JSONArray(json[i]);
	// }
	// for(int i=4;i<json.length;i++){
	// JSONObject obj1 = new JSONObject(json[i]);
	// }
	// b=true;
	// PublicMethod.myOutput("------------"+b);
	// iretrytimes=3;
	// }catch(JSONException e){
	// iretrytimes--;
	// b=false;
	// }
	// // }
	// return b;
	// }
	//
	// // json解析即时信息、开奖查询、当前期数的后台返回值
	// public String[] getlotteryinfo(String string,int index){
	// // ShellRWSharesPreferences shellRW;
	// String error_code="00";
	// String win_base_code = "123";
	// String term_code = "2010";
	// String win_special_code = "7";
	// int iretrytimes=2;
	// shellRW =new ShellRWSharesPreferences(this,"addInfo");
	// String lottery_ssq=shellRW.getUserLoginInfo(string);
	// while(iretrytimes<3&&iretrytimes>0){
	// try{
	// try
	// {
	// JSONObject obj = new JSONObject(lottery_ssq);
	// error_code=obj.getString("error_code");
	// if(error_code.equals("0000")){
	// win_base_code=obj.getString("win_base_code");//获取中奖号码
	// term_code=obj.getString("term_code");//获取期号
	// win_special_code = obj.getString("win_special_code");//获取蓝球号码
	// }
	// }
	// catch(Exception e){
	// JSONArray jsonObject3=new JSONArray(lottery_ssq);
	// JSONObject obj = jsonObject3.getJSONObject(index);
	// error_code=obj.getString("error_code");
	// if(error_code.equals("0000")){
	// win_base_code=obj.getString("win_base_code");
	// term_code=obj.getString("term_code");
	// }
	// }
	// iretrytimes=3;
	// }catch(JSONException e){
	// e.printStackTrace();
	// iretrytimes--;
	// }
	// }
	// String[] ssq_wininfo={win_base_code,win_special_code,term_code};
	// return ssq_wininfo;
	// }
	// public String getnews(String string){
	// int iretrytimes=2;
	// String news="";
	// shellRW =new ShellRWSharesPreferences(this,"addInfo");
	// String notice=shellRW.getUserLoginInfo(string);
	// while(iretrytimes<3&&iretrytimes>0){
	// try{
	// JSONObject obj = new JSONObject(notice);
	// news=obj.getString("news");
	//				    
	// iretrytimes=3;
	// }catch(JSONException e){
	// e.printStackTrace();
	// iretrytimes--;
	// }
	// }
	// return news;
	// }
	// public String getLotno(String string){
	// int iretrytimes=2;
	// String error_code;
	// String batchcode="";
	// shellRW =new ShellRWSharesPreferences(this,"addInfo");
	// String notice=shellRW.getUserLoginInfo(string);
	// while(iretrytimes<3&&iretrytimes>0){
	// try{
	// JSONObject obj = new JSONObject(notice);
	// error_code=obj.getString("error_code");
	// if(error_code.equals("0000")){
	// batchcode=obj.getString("batchCode");
	// }
	// iretrytimes=3;
	// }catch(JSONException e){
	// e.printStackTrace();
	// iretrytimes--;
	// }
	// }
	// return batchcode;
	// }
	//	 
}