/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.ruyicai.activity.home;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.CheckWireless;
import com.ruyicai.dialog.ShowConnectionDialog;
import com.ruyicai.dialog.ShowNoConnectionDialog;
import com.ruyicai.net.transaction.SoftwareUpdateInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

public class HomePage extends Activity {

	private static final String TAG = "HomePage";

	// 软件升级相关
	private String softwareErrorCode = "";
	private String softwareurl = "";
	private String softwaremessageStr = "";
	private String softwaretitle = "";

	private ShellRWSharesPreferences shellRW;
	public static String channel_id = "C10319";
	public static String channel = Constants.COOP_ID;
	private StartTask mStartTask;
	// 处理消息
	public Handler mHandler = new Handler();
	ImageView imageview;
	int iShowStatus = 0;
	public boolean isHint = false;
	boolean b = false;

	// 实现网络的检测，如果没有网络则显示一个对话框，提示进行网络设置。
	CheckWireless checkWireless;
	private ProgressDialog pBar;
	private Handler handler = new Handler();
	private JSONObject obj;
	private String softwareUpdateInfo;
	
	
	Bitmap resizedBitmap = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.homepage);
		getMachineInfo();// 获取手机信息
		clearLastLoginInfo();// 清空上次登录信息
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
		// 初始化imageview
		imageview = (ImageView) this.findViewById(R.id.cp1);
		// 设置图片
		Resources r = this.getResources();
		InputStream is = r.openRawResource(R.drawable.cp1);

		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		Bitmap iBackgrounBitmap = bmpDraw.getBitmap();

		// 绘图
		Matrix matrix = new Matrix();

		float iScreenWidth = PublicMethod.getDisplayWidth(this);
		float iScreenHeight = PublicMethod.getDisplayHeight(this);
		float w = iScreenWidth / iBackgrounBitmap.getWidth();
		float h = iScreenHeight / iBackgrounBitmap.getHeight();
		matrix.postScale(w, h);
		resizedBitmap = Bitmap.createBitmap(iBackgrounBitmap, 0, 0,
				iBackgrounBitmap.getWidth(), iBackgrounBitmap.getHeight(),
				matrix, true);
		imageview.setImageBitmap(resizedBitmap);
//		iBackgrounBitmap.recycle();

		checkWireless = new CheckWireless(this);

		// 客户端正常加载顺序是 1->3->5->6->4
		// 初始化handler

		// 客户端正常加载顺序是 1->3->5->6->4

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				switch (msg.what) {
				case 1: {
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
					Message mg = Message.obtain();
					mg.what = 6;
					mHandler.sendMessage(mg);
					break;
				}
				case 4: {
					Intent in = new Intent(HomePage.this, RuyicaiAndroid.class);
					startActivity(in);
					HomePage.this.finish();
					break;
				}
				case 5: {
					checkCurrentVersion();// 检查是否要升级
					break;
				}
				case 6:
					mStartTask = new StartTask();
					mStartTask.execute();
					break;
				case 7:
					Toast.makeText(HomePage.this, "参数异常！", Toast.LENGTH_SHORT).show();
					break;
				case 8:
					Toast.makeText(HomePage.this, "异常！", Toast.LENGTH_SHORT).show();
					break;
				case 9:
					update(softwareurl, softwaremessageStr, softwaretitle);
					break;
				}
				
			}
		};
		Message mg = Message.obtain();
		mg.what = 1;
		mHandler.sendMessage(mg);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void checkWirelessNetwork() {
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		if (!checkWireless.getConnectGPRS() && !checkWireless.getConnectWIFI()) {
			ShowNoConnectionDialog showNoConnectionDialog = new ShowNoConnectionDialog(
					this, this);
			showNoConnectionDialog.showNoConnectionDialog();
		} else {
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

			}
		}
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
									shellRW.setUserLoginInfo("information" + i,"");
								}
								Intent in = new Intent(HomePage.this,
										RuyicaiAndroid.class);
								startActivity(in);
								HomePage.this.finish();
							}

						}).setNegativeButton("退出",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								HomePage.this.finish();
							}
						});

		dialog.show();
	}

	/**
	 * 此函数功能已废除
	 */
	public void checkCurrentVersion() {
		Message msg = new Message();
		msg.what = 6;
		mHandler.sendMessage(msg);

	}

	/**
	 * 开机联网,保存必要信息,判断软件升级
	 * 
	 */
	public void initSoftwareStartInfomationFromServer() {

		// 判断是否要上传统计信息到服务器
		SharedPreferences sharedPreferences = getSharedPreferences(
				Constants.APPNAME, MODE_PRIVATE);

		int gameSumCount = sharedPreferences
				.getInt(Constants.GAME_CLICK_SUM, 0); // 游戏名称+n , 默认值为0
		boolean isUpdateStatInfo = false;// 是否上传了统计信息
		JSONObject statJsonObject = null;
		if (gameSumCount >= Constants.STAT_INFO_CACHE_NUM) {
			// 用户点的游戏够多了,值得上传一次统计信息
			statJsonObject = new JSONObject();
			try {
				for (int i = 0; i < 12; i++) {
					statJsonObject.put(String.valueOf(i), sharedPreferences.getInt(Constants.GAME_CLASS + i, 0));// 游戏名称+n ,
				}
				isUpdateStatInfo = true;
			} catch (JSONException e) {
				statJsonObject = null;
			}

		}

		softwareUpdateInfo = SoftwareUpdateInterface.getInstance().softwareupdate(statJsonObject);
		try {
			obj = new JSONObject(softwareUpdateInfo);
			PublicConst.MESSAGE = obj.getString("broadcastmessage");
			if (isUpdateStatInfo == true) {
				// 上传了统计信息,在这里将统计信息清空
				SharedPreferences.Editor editor = getSharedPreferences(
						Constants.APPNAME, MODE_PRIVATE).edit();

				for (int i = 0; i < 12; i++) {
					editor.putInt(Constants.GAME_CLASS + i, 0);
				}
				editor.putInt(Constants.GAME_CLICK_SUM, 0);
				editor.commit();
			}

			softwareErrorCode = obj.getString("errorCode");
			if (softwareErrorCode.equals("true")) {
				// 需要升级,设置升级相关字段
				softwareurl = obj.getString("updateurl");
				softwaretitle = obj.getString("title");
				softwaremessageStr = obj.getString("message");
			}
			Constants.NEWS = obj.getString("news");

			Constants.currentLotnoInfo = obj.getJSONObject("currentBatchCode");// 获取网络的期号信息
		} catch (JSONException e) {
			e.printStackTrace();
		}
		flag = false;
	}

	/**
	 * @作者：付磊
	 * @修改内容：在线自动更新新版本
	 * @版本：
	 */
	public void update(final String url, String message, String title) {
		Dialog dialog = new AlertDialog.Builder(HomePage.this).setTitle(
				"有新版本更新").setMessage(message)// 设置内容
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
									pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为长进度条
									pBar.setIndeterminate(false);
									pBar.incrementProgressBy(1); // 增加和减少进度
									downFile(url);
								} else {
									Toast.makeText(HomePage.this,
											"你未插入SD卡，请插入SD卡之后再更新",
											Toast.LENGTH_SHORT).show();
								}
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Message mg = new Message();
								mg.what = 4;
								mHandler.sendMessage(mg);
							}
						}).create();
		
		try {
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 * @param url
	 */
	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					double onePiece = length / 100;
					int percent = 1;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(),"RuyicaiAndroid_update.apk");
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							// baos.write(buf, 0, ch);
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
								if (count >= (onePiece * percent)) {
									percent = percent + 1;
									pBar.incrementProgressBy(1);
								}
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {

				} catch (IOException e) {

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
				"/sdcard/RuyicaiAndroid_update.apk")),
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

	/**
	 * 获取手机设备信息
	 */
	public void getMachineInfo() {

		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
		Constants.IMEI = telephonyManager.getDeviceId();
		Constants.IMSI = telephonyManager.getSubscriberId();

		Constants.MACHINE_ID = Build.MODEL;

	}

	/**
	 * 清空上次的登录信息
	 */
	public void clearLastLoginInfo() {
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		shellRW.setUserLoginInfo("sessionid", "");
		ScrollableTabActivity.isturn = false;
	}

	private boolean flag = true;
	
	private class StartTask extends AsyncTask<String, Void, Integer> {
		protected Integer doInBackground(String... params) {

			Thread t = new Thread(new Runnable() {
				public void run() {
					initSoftwareStartInfomationFromServer();
				}
			});
			try {
				// 6 - 500ms
				t.start();
				int counter = 0;
				while(flag){
					Thread.sleep(500);
					counter += 500;
					if(counter >= 5000){
						flag = false;
					}
				}
			} catch (Exception e) {

			}

			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (softwareErrorCode.equals("true")) {
				Message message = Message.obtain();
				message.what = 9;
				mHandler.sendMessage(message);
				// 需要更新

				return;
			} else {
				Bundle b = new Bundle();
				Intent in = new Intent(HomePage.this, RuyicaiAndroid.class);
				in.putExtras(b);
				startActivity(in);
				HomePage.this.finish();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		resizedBitmap.recycle();
	}
}