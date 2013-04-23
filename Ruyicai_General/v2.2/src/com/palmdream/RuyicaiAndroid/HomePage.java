/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.palmdream.RuyicaiAndroid;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.palmdream.netintface.Jsoninformation;
import com.palmdream.netintface.ParseXML;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

public class HomePage extends Activity {
	private ShellRWSharesPreferences shellRW;
	// 渠道号
//	public static String channel_id = "C10144";// 掌上应用汇
//	public static String channel = "279";// 掌上应用汇
	
	public static String channel_id = "C10299";// 当铺a
	public static String channel = "495";

//	public static String channel_id = "C10203";// 魅族a
//	public static String channel = "371";
//	
//	public static String channel_id = "C10300";// 历趣a
//	public static String channel = "497";
//	
//	public static String channel_id = "C10119";// tompda-android
//	public static String channel = "232";
	public static String imei;
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
	private ProgressDialog pBar;
	private Handler handler = new Handler();
	private int iretrytimes = 2;
	private JSONObject obj;
	private String re;
	private String type = "000001";

	private byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.homepage);
		// 读取本地渠道号
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		String channel_id = shellRW.getUserLoginInfo("channel_id");
		String channel = shellRW.getUserLoginInfo("channel");
		if (channel_id == null || channel == null) {
			shellRW.setUserLoginInfo("channel_id", this.channel_id);
			shellRW.setUserLoginInfo("channel", this.channel);
		} else {
			this.channel_id = channel_id;
			this.channel = channel;
		}
		// 获取串码
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(this.TELEPHONY_SERVICE);
		imei = tm.getDeviceId();

		// 初始化imageview
		imageview = (ImageView) this.findViewById(R.id.cp1);
		// 设置图片
		Resources r = this.getResources();
		InputStream is = r.openRawResource(R.drawable.cp1);

		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		Bitmap iBackgrounBitmap = bmpDraw.getBitmap();

		// 绘图
		Matrix matrix = new Matrix();

		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		//
		// float ps1 = dm.widthPixels / iBackgrounBitmap.getWidth();
		// float ps2 = dm.heightPixels / iBackgrounBitmap.getHeight();
		float iScreenWidth = PublicMethod.getDisplayWidth(this);
		float iScreenHeight = PublicMethod.getDisplayHeight(this);
		float w = iScreenWidth / iBackgrounBitmap.getWidth();
		float h = iScreenHeight / iBackgrounBitmap.getHeight();
		matrix.postScale(w, h);
		Bitmap resizedBitmap = Bitmap.createBitmap(iBackgrounBitmap, 0, 0,
				iBackgrounBitmap.getWidth(), iBackgrounBitmap.getHeight(),
				matrix, true);

		imageview.setImageBitmap(resizedBitmap);
		iAnimateView = new MyAnimateView(this);
		// 默认为cmnet链接
		iHttp.conMethord = iHttp.CMNET;
		iHttp.iTimeOut = 5000;
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
					imageview.setAlpha(alpha);
					imageview.invalidate();
					break;
				}
				case 1: {
					PublicMethod.myOutput("-----new image");
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
					break;
				}
				case 3: {
					PublicMethod.myOutput("----comeback");
					iAnimateView.invalidate();
					Message mg = Message.obtain();
					mg.what = 5;
					mHandler.sendMessage(mg);
					break;
				}
				case 4: {
					uvNumber();// uv统计方法
					userNumber();// 串码统计
					iHttp.iTimeOut = 20000;
					Intent in = new Intent(HomePage.this, RuyicaiAndroid.class);
					startActivity(in);
					HomePage.this.finish();
					break;
				}
				case 5: {
					// 黄轲添加 查询当前软件的版本信息
					 checkCurrentVersion();
//					saveInformation();
					break;
				}
				case 6:
					isUpdate(re);
					break;
				case 7:
					Toast.makeText(HomePage.this, "参数异常！", Toast.LENGTH_SHORT)
							.show();
					break;
				case 8:
					Toast.makeText(HomePage.this, "异常！", Toast.LENGTH_SHORT)
							.show();
					break;

				}
			}
		};
	}

	/*
	 * UV统计
	 */
	public void uvNumber() {
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		shellRW.setUserLoginInfo("sessionid", "");
		BuyLotteryMainList.flag = true;// 数据重置
		BuyLotteryMainList.start = true;// 数据重置
		JoinHall.once = true;// 数据重置
		// uv统计
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean start = false;
				final Calendar c = Calendar.getInstance();
				int mYear = c.get(Calendar.YEAR); // 获取当前年份
				int mMonth = c.get(Calendar.MONTH);// 获取当前月份
				int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码
				if (shellRW.getUserLoginInfo("mYear") == null
						|| shellRW.getUserLoginInfo("mMonth") == null
						|| shellRW.getUserLoginInfo("mDay") == null) {
					start = true;
					shellRW.setUserLoginInfo("mYear", Integer.toString(mYear));
					shellRW
							.setUserLoginInfo("mMonth", Integer
									.toString(mMonth));
					shellRW.setUserLoginInfo("mDay", Integer.toString(mDay));

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
						jrtLot.setPara(100, channel_id);
						jrtLot.uvNumber(HomePage.channel_id,
										jrtLot.versionInfo);// 红星后台
						Log.v("========uvNumber", "uvNumber");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 串码统计
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void userNumber() {
		// 串码统计
		new Thread(new Runnable() {

			@Override
			public void run() {

				jrtLot.count("1", imei, HomePage.channel_id, null);
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

			mg.what = 1;
		}

		if (iShowStatus == 3) {
			// mg.what=2;
			return true;
		}
		mHandler.sendMessage(mg);
		return false;
	}

	private static class MyAnimateView extends View {
		private AnimateDrawable mDrawable;
		private Bitmap iBackgrounBitmap;
		private Bitmap iProgressBarBitmap;
		private Paint p = null;

		private float iScreenWidth;
		private float iScreenHeight;
		private float iBackgroundWidth;
		private float iBackgrounHeight;
		private float iOffsetX;
		private float iOffsetY;
		private float w, h;

		private int iShowStringX = 100;
		private int iShowStringY = 390;
		private ImageView bg;

		public MyAnimateView(Context context) {
			super(context);

			iScreenWidth = PublicMethod.getDisplayWidth(context);
			iScreenHeight = PublicMethod.getDisplayHeight(context);

			bg = new ImageView(context);
			setPaint();

			Drawable dr = context.getResources().getDrawable(R.drawable.light);
			dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
			// 背景图片
			Resources r = context.getResources();
			InputStream is = r.openRawResource(R.drawable.cp1);

			BitmapDrawable bmpDraw = new BitmapDrawable(is);
			iBackgrounBitmap = bmpDraw.getBitmap();
			// 进度条
			Resources r1 = context.getResources();
			InputStream is1 = r1.openRawResource(R.drawable.cp2);

			BitmapDrawable bmpDraw1 = new BitmapDrawable(is1);
			iProgressBarBitmap = bmpDraw1.getBitmap();

			iBackgroundWidth = iBackgrounBitmap.getWidth();
			iBackgrounHeight = iBackgrounBitmap.getHeight();
			// 缩放比例
			w = iScreenWidth / iBackgrounBitmap.getWidth();
			h = iScreenHeight / iBackgrounBitmap.getHeight();

			iOffsetX = (iScreenWidth - iBackgroundWidth) / 2;
			if (iOffsetX < 0)
				iOffsetX = 0;
			iOffsetY = (iScreenHeight - iBackgrounHeight) / 2;
			if (iOffsetY < 0)
				iOffsetY = 0;

			Animation an = new TranslateAnimation(58 * w, 240 * w, 366 * h,
					366 * h);
			an.setDuration(1000);
			an.setRepeatCount(-1);
			an.initialize(10, 10, 10, 10);

			mDrawable = new AnimateDrawable(dr, an);
			an.startNow();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// 绘图
			Matrix matrix = new Matrix();

			matrix.postScale(w, h);
			Bitmap resizedBitmap = Bitmap.createBitmap(iBackgrounBitmap, 0, 0,
					iBackgrounBitmap.getWidth(), iBackgrounBitmap.getHeight(),
					matrix, true);
			Bitmap iProgressBarBitmapMatrix = Bitmap.createBitmap(
					iProgressBarBitmap, 0, 0, iProgressBarBitmap.getWidth(),
					iProgressBarBitmap.getHeight(), matrix, true);
			// 绘图
			canvas.drawBitmap(resizedBitmap, 0, 0, null);
			canvas.drawBitmap(iProgressBarBitmapMatrix, 58 * w, 365 * h, null);

			mDrawable.draw(canvas);

			canvas.drawText("正在获取网络数据...", iShowStringX * w, iShowStringY * h,
					p);

			invalidate();
		}

		private void setPaint() {
			if (p == null) {
				p = new Paint();

				p.setColor(Color.YELLOW);
				p.setTypeface(Typeface.create(Typeface.SANS_SERIF,
						Typeface.NORMAL));
				p.setTextSize(13);
			}

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

	/**
	 * 
	 * @作者：黄轲
	 * @日期：2011/3/8
	 * @参数：无
	 * @返回值：无
	 * @作用：查询当前软件的版本，和服务器上保留的最新版本作比较，如果本地版本是旧版本就提示用户下载最新的，如果本地版本是最新的就什么都不提示
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：1.0
	 */
	public void checkCurrentVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";

				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.isVersion(type);

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("errorCode");

						iretrytimes = 3;
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						iretrytimes--;
					}

					if (iretrytimes == 0 && iHttp.whetherChange == false) {
						iHttp.whetherChange = true;
						if (iHttp.conMethord == iHttp.CMWAP) {
							iHttp.conMethord = iHttp.CMNET;
						} else {
							iHttp.conMethord = iHttp.CMWAP;
						}
						iretrytimes = 2;
						while (iretrytimes < 3 && iretrytimes > 0) {
							re = jrtLot.isVersion(type);

							try {
								obj = new JSONObject(re);
								error_code = obj.getString("errorCode");
								iretrytimes = 3;
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								iretrytimes--;
							}
						}
					}
				}
				iretrytimes = 2;
				if (error_code.equals("A030000")) {// 成功
					Message msg = new Message();
					msg.what = 6;
					mHandler.sendMessage(msg);

				} else if (error_code.equals("A030001")) {// 参数错误
					Message msg = new Message();
					msg.what = 7;
					mHandler.sendMessage(msg);
				} else if (error_code.equals("A030500")) {// 异常
					Message msg = new Message();
					msg.what = 8;
					mHandler.sendMessage(msg);
				}

			}

		}).start();

	}

	/**
	 * 是否更新新版本
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void isUpdate(String re) {
		String version = "";
		String url = "";
		try {
			JSONObject obj = new JSONObject(re);
			url = obj.getString("downurl");
			version = obj.getString("currentversion");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Double.parseDouble(jrtLot.versionInfo) < Double
				.parseDouble(version)) {
			update(url);
		} else {
			saveInformation();
		}
	}

	/**
	 * 
	 * @作者：付磊
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：在线自动更新新版本
	 * @修改日期：
	 * @版本：
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */

	public void update(final String url) {
		Dialog dialog = new AlertDialog.Builder(HomePage.this).setTitle("系统更新")
				.setMessage("发现新版本，请更新！")// 设置内容
				.setCancelable(false).setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (android.os.Environment.getExternalStorageState().equals(
										android.os.Environment.MEDIA_MOUNTED)) {
								pBar = new ProgressDialog(HomePage.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候…");
								pBar
										.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(url);
								}else{
									Toast.makeText(HomePage.this, "你未插入SD卡，请插入SD卡之后再更新", Toast.LENGTH_SHORT)
									.show();
								}

							}

						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击”取消”按钮之后退出程序
								saveInformation();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}

	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				// params[0]代表连接的url
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(Environment
								.getExternalStorageDirectory(),
								"RuyicaiAndroid_320480.apk");
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							// baos.write(buf, 0, ch);
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {

							}

						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}.start();

	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});

	}

	void update() {
		finish();

	
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(
					"/sdcard/RuyicaiAndroid_320480.apk")),
					"application/vnd.android.package-archive");
			startActivity(intent);
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return false;
		}
		return false;
	}

}