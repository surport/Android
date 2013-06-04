package com.ruyicai.activity.home;

import java.io.File;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.CheckWireless;
import com.ruyicai.dialog.ShowConnectionDialog;
import com.ruyicai.dialog.ShowNoConnectionDialog;
import com.ruyicai.dialog.UpdateDialog;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.service.DownLoadImg;
import com.ruyicai.util.ClockThread;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {

	// 软件升级相关
	public static String softwareErrorCode = "not";
	public static String softwareurl = "";
	public static String softwaremessageStr = "";
	public static String softwaretitle = "";
	private ShellRWSharesPreferences shellRW;
	private StartTask mStartTask;
	private ImageView imageview;
	public boolean isHint = false;
	private JSONObject obj;
	private String softwareUpdateInfo;
	// 客户端正常加载顺序是 1->3->5->6->4
	// 处理消息
	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: {
				try {
					showalert();
				} catch (Exception e) {
					e.printStackTrace();// 显示提示框（没有网络，用户是否继续 ） 2010/7/2 陈晨
				}
				break;
			}
			case 2: {
				mStartTask = new StartTask();
				mStartTask.execute();
				break;
			}
			case 3: {
			
				turnActivity();
				break;
			}
			case 4:
				Toast.makeText(HomeActivity.this, "参数异常！", Toast.LENGTH_SHORT)
						.show();
				break;
			case 5:
				Toast.makeText(HomeActivity.this, "异常！", Toast.LENGTH_SHORT)
						.show();
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.home_activity);
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		initImgView();// 开机图片
		getMachineInfo();// 获取手机信息
		clearLastLoginInfo();// 清空上次登录信息
		getChannel(shellRW);// 读取本地渠道号
		DisplayMetrics metric = new DisplayMetrics();// 设置时时彩，11选5单选按钮间距。
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		Constants.SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		if (Constants.SCREEN_WIDTH == 240) {
			Constants.PADDING = 20;
		} else if (Constants.SCREEN_WIDTH == 320) {
			Constants.PADDING = 27;
		} else if (Constants.SCREEN_WIDTH == 480) {
			Constants.PADDING = 40;
		}
		initBitmap();// 初始化小球
		checkWirelessNetwork();// 实现网络的检测
	}

	/**
	 * 设置开机图片
	 * 
	 */
	public void initImgView() {
		imageview = (ImageView) this.findViewById(R.id.cp1);
		File sdcardDir = Environment.getExternalStorageDirectory();
		String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
		// 得到开机图片
		Bitmap bitmap = BitmapFactory.decodeFile(path + DownLoadImg.LOCAL_DIR + DownLoadImg.IMG_NAME);
		String errorCode = shellRW.getUserLoginInfo("errorCode");
		if (bitmap == null||errorCode == null||errorCode.equals("false")) {
			Resources r = this.getResources();
			InputStream is = r.openRawResource(R.drawable.cp1);
			BitmapDrawable bmpDraw = new BitmapDrawable(is);
			bitmap = bmpDraw.getBitmap();
		}
		// 缩放开机图片
		Matrix matrix = new Matrix();
		float iScreenWidth = PublicMethod.getDisplayWidth(this);
		float iScreenHeight = PublicMethod.getDisplayHeight(this);
		float w = iScreenWidth / bitmap.getWidth();
		float h = iScreenHeight / bitmap.getHeight();
		if (w != 1 || h != 1) {
			matrix.postScale(w, h);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
		}
		imageview.setImageBitmap(bitmap);
	}

	/**
	 * 读取本地渠道号
	 */
	public void getChannel(ShellRWSharesPreferences shellRW) {
		String channel = shellRW.getUserLoginInfo("channel");
		if (channel == null) {
			shellRW.setUserLoginInfo("channel", Constants.COOP_ID);
		} else {
			Constants.COOP_ID = channel;
		}
	}

	/**
	 * 跳转下一页
	 */
	public void turnActivity() {
		Intent in = new Intent(HomeActivity.this, TransitActivity.class);
		startActivity(in);
		HomeActivity.this.finish();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * 检测网络是否可用
	 */
	private void checkWirelessNetwork() {
		CheckWireless checkWireless = new CheckWireless(this);// 初始化检测网络对象
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
						mg.what = 2;
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
		Builder dialog = new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("网络出现异常")
				.setPositiveButton("继续", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						shellRW = new ShellRWSharesPreferences(
								HomeActivity.this, "addInfo");
						for (int i = 0; i < 7; i++) {
							shellRW.setUserLoginInfo("information" + i, "");
						}
						turnActivity();
					}

				})
				.setNegativeButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						HomeActivity.this.finish();
					}
				});

		dialog.show();
	}

	private boolean initProxySetting() {
		ConnectivityManager ConnMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if (info != null) {
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				Constants.mProxyPort = 0;
				Constants.mProxyHost = null;
			} else {
				Constants.mProxyHost = android.net.Proxy.getDefaultHost();
				Constants.mProxyPort = android.net.Proxy.getDefaultPort();
			}
			return (!TextUtils.isEmpty(Constants.mProxyHost) && Constants.mProxyPort != 0);
		}
		return false;
	}

	/**
	 * 开机联网,保存必要信息,判断软件升级
	 * 
	 */
	public void initSoftwareStartInfomationFromServer() {

		Constants.isProxyConnect = initProxySetting();// 初始化代理设置
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
					statJsonObject.put(String.valueOf(i), sharedPreferences
							.getInt(Constants.GAME_CLASS + i, 0));// 游戏名称+n ,
				}
				isUpdateStatInfo = true;
			} catch (JSONException e) {
				statJsonObject = null;
			}

		}
		softwareUpdateInfo = SoftwareUpdateInterface.getInstance()
				.softwareupdate(statJsonObject);
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
			imageJson(obj.getJSONObject("image"));//是否下载开奖图片
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
			ClockThread clock = new ClockThread();// 创建合买大厅倒计时线程
			clock.startThread();
		}

		flag = false;
	}
	/**
	 * 是否下载开奖图片
	 * @param image
	 * @throws JSONException
	 */
    public void imageJson(JSONObject image) throws JSONException{
    	String errorCode = image.getString("errorCode");
    	shellRW.setUserLoginInfo("errorCode", errorCode);
        if(errorCode.equals("true")){
        	String jsonId = image.getString("id");
        	String imageId = shellRW.getUserLoginInfo("imageId");
        	if(imageId == null||!imageId.equals(jsonId)){
        		shellRW.setUserLoginInfo("imageId", jsonId);
        	  	DownLoadImg down = new DownLoadImg();
        		down.downThread(image.getString("imageUrl"));
        	}
        }
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

		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
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
		shellRW.setUserLoginInfo("userno", "");
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
				t.start();
				// 6 - 500ms
				int counter = 0;
				while (flag) {
					Thread.sleep(500);
					counter += 500;
					if (counter >= 5000) {
						flag = false;
					}
				}
			} catch (Exception e) {
			}
			return 0;
		}

		protected void onPostExecute(Integer result) {
			if (softwareErrorCode.equals("true")) {
				try{
					HomeUpdate update = new HomeUpdate(HomeActivity.this,
							new Handler(), softwareurl, softwaremessageStr,
							softwaretitle);
					update.showDialog();
					update.createMyDialog();
				}catch (Exception e){
					e.printStackTrace();
				}
				return;
			} else {
				turnActivity();	
			}
		}
	}

	class HomeUpdate extends UpdateDialog {

		public HomeUpdate(Activity activity, Handler handler, String url,
				String message, String title) {
			super(activity, handler, url, message, title);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCancelButton() {
			// TODO Auto-generated method stub
			Message mg = Message.obtain();
			mg.what = 3;
			mHandler.sendMessage(mg);
		}

	}

	/**
	 * 初始化小球图片
	 * 
	 */
	public void initBitmap() {
		Resources res = this.getResources();
		Constants.grey = new BitmapDrawable(
				res.openRawResource(R.drawable.grey)).getBitmap();
		Constants.red = new BitmapDrawable(res.openRawResource(R.drawable.red))
				.getBitmap();
		Constants.blue = new BitmapDrawable(
				res.openRawResource(R.drawable.blue)).getBitmap();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

}
