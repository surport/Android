package com.ruyicai.activity.home;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.CheckWireless;
import com.ruyicai.activity.introduce.PhotoActivity;
import com.ruyicai.activity.notice.NoticeMainActivity;
import com.ruyicai.constant.ChannelConstants;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.dialog.ShowNoConnectionDialog;
import com.ruyicai.dialog.UpdateDialog;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.net.newtransaction.WinAndPulsaward;
import com.ruyicai.service.DownLoadImg;
import com.ruyicai.util.ClockThread;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Administrator
 * 
 */
public class HomeActivity extends Activity {
    
	boolean isWifi = false;
	boolean isGPRS = false;
	// 软件升级相关
	public static String softwareErrorCode = "not";
	public static String softwareurl = "";
	public static String softwaremessageStr = "";
	public static String softwaretitle = "";
	private RWSharedPreferences shellRW;

 	private StartTask mStartTask;
	private ImageView imageview;
	public boolean isHint = false;
	private JSONObject obj;
	private String softwareUpdateInfo;
	private String packageName;
	private String todaykaijianginfo;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏显示
		setContentView(R.layout.home_activity);
		MobclickAgent.onError(this);//在总入口添加错误报告提交BY贺思明 2012-6-28
		MobclickAgent.openActivityDurationTrack(true);//统计在线时长BY贺思明 2012-6-28
		shellRW = new RWSharedPreferences(this, ShellRWConstants.SHAREPREFERENCESNAME);
		setPackageName();
		initImgView();// 开机图片
		getMachineInfo();// 获取手机信息
		clearLastLoginInfo();// 清空上次登录信息
		getChannel();// 读取本地渠道号
		setPadding();//设置高频彩单选按钮间距
		initBitmap();// 初始化小球
		checkWirelessNetwork();// 实现网络的检测
		checkCaizhongSetting();//获取彩种设置
	}
    /**
     * 设置高频彩单选按钮间距
     */
	private void setPadding() {
		DisplayMetrics metric = new DisplayMetrics();// 设置时时彩，11选5单选按钮间距。
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		Constants.SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		Constants.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		Log.e("width",""+Constants.SCREEN_WIDTH+"     SCREEN_HEIGHT    " +Constants.SCREEN_HEIGHT);
		if (Constants.SCREEN_WIDTH == 240) {
			Constants.PADDING = 18;
		} else if (Constants.SCREEN_WIDTH == 320) {
			Constants.PADDING = 25;
		} else if (Constants.SCREEN_WIDTH == 480) {
			Constants.PADDING = 35;
		} else{
			Constants.PADDING = PublicMethod.getPxInt(30, this);
		}
		
		if (Constants.SCREEN_HEIGHT == 854) {
			Constants.PADDING = PublicMethod.getPxInt(30, this);
		}
	}

	/**
	 * 设置开机图片
	 * 
	 */
	public void initImgView() {
		imageview = (ImageView) this.findViewById(R.id.cp1);
		File sdcardDir = Environment.getExternalStorageDirectory();
		String path = sdcardDir.getParent() + "/" + sdcardDir.getName();
		Log.e("packgeName===",""+getPackageName());
		// 得到开机图片
		Bitmap bitmap = BitmapFactory.decodeFile(path + DownLoadImg.LOCAL_DIR + packageName +"/"+ DownLoadImg.IMG_NAME);
		String errorCode = shellRW.getStringValue("errorCode");
		String imageId = shellRW.getStringValue("imageId");
		if (bitmap == null||errorCode.equals("")||errorCode.equals("false")) {
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
	 * 设置下载图片文件夹名称
	 */
	private void setPackageName(){
		this.packageName = getPackageName();
	}

	/**
	 * 读取本地渠道号
	 */
	public void getChannel() {
		String channel = shellRW.getStringValue("channel");
		if (channel.equals("")||channel == null) {
			shellRW.putStringValue("channel", ChannelConstants.COOP_ID);
		} else {
			ChannelConstants.COOP_ID = channel;
		}
	}

	/**
	 * 跳转下一页
	 */
	public void turnActivity() {
//		Intent in = new Intent(HomeActivity.this, TransitActivity.class);
//		startActivity(in);
//		HomeActivity.this.finish();
		// 读取本地渠道号
				shellRW = new RWSharedPreferences(this, "addInfo");
				boolean isFirst = shellRW.getBooleanValue("isFirst");
				if(isFirst){
					Intent in = new Intent(HomeActivity.this,MainGroup.class);
					startActivity(in);
					HomeActivity.this.finish();
				}else{
					shellRW.putBooleanValue("isFirst", true);
					Intent in = new Intent(HomeActivity.this,PhotoActivity.class);
					startActivity(in);
					HomeActivity.this.finish();
				}
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
		isWifi = checkWireless.getConnectWIFI();
		isGPRS = checkWireless.getConnectGPRS();
		if (isWifi == false && isGPRS == false) {
			ShowNoConnectionDialog showNoConnectionDialog = new ShowNoConnectionDialog(this, this);
			showNoConnectionDialog.showNoConnectionDialog();
		} else {
			try {
				Message mg = Message.obtain();
				mg.what = 2;
				mHandler.sendMessage(mg);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 网络异常提示框
	 */
	public void showalert() {
		Builder dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("网络出现异常")
				.setPositiveButton("继续", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						shellRW = new RWSharedPreferences(
								HomeActivity.this, "addInfo");
						for (int i = 0; i < 7; i++) {
							shellRW.putStringValue("information" + i, "");
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
		int gameSumCount = sharedPreferences.getInt(Constants.GAME_CLICK_SUM, 0); // 游戏名称+n , 默认值为0
		boolean isUpdateStatInfo = false;// 是否上传了统计信息
		JSONObject statJsonObject = null;
		if (gameSumCount >= Constants.STAT_INFO_CACHE_NUM) {
			// 用户点的游戏够多了,值得上传一次统计信息
			statJsonObject = new JSONObject();
			try {
				for (int i = 0; i < 12; i++) {
					statJsonObject.put(String.valueOf(i), sharedPreferences .getInt(Constants.GAME_CLASS + i, 0));// 游戏名称+n ,
				}
				isUpdateStatInfo = true;
			} catch (JSONException e) {
				statJsonObject = null;
			}

		}
		
		softwareUpdateInfo = SoftwareUpdateInterface.getInstance().softwareupdate(statJsonObject,shellRW.getStringValue("randomNumber"));
		todaykaijianginfo=WinAndPulsaward.getInstance().winandpulsquarey();//今日开奖信息。
		try {
			obj = new JSONObject(softwareUpdateInfo);
			JSONObject autoLogin = obj.getJSONObject("autoLogin");
			if(autoLogin.getString("isAutoLogin").equals("true")){
				Constants.isInitTop = true;
				shellRW.putBooleanValue(ShellRWConstants.AUTO_LOGIN, true);
				shellRW.putStringValue(ShellRWConstants.USERNO, autoLogin.getString("userno"));
				shellRW.putStringValue(ShellRWConstants.CERTID, autoLogin.getString("certid"));
				shellRW.putStringValue(ShellRWConstants.MOBILEID, autoLogin.getString("mobileid"));
				shellRW.putStringValue(ShellRWConstants.NAME, autoLogin.getString("name"));
				shellRW.putStringValue(ShellRWConstants.USERNAME, autoLogin.getString("userName"));
				shellRW.putStringValue(ShellRWConstants.SESSIONID, autoLogin.getString("sessionid"));
			}else{
				shellRW.putBooleanValue(ShellRWConstants.AUTO_LOGIN, false);
				Constants.isInitTop = false;
			}
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
			Constants.todayjosn=new JSONObject(todaykaijianginfo);//今日开奖信息
			imageJson(obj.getJSONObject("image"));//是否下载开奖图片
		} catch (JSONException e) {
			e.printStackTrace();
		}finally{
//			ClockThread clock = new ClockThread();// 创建合买大厅倒计时线程
//			clock.startThread();
		}
	}
	/**
	 * 是否下载开奖图片
	 * @param image
	 * @throws JSONException
	 */
    public void imageJson(JSONObject image) throws JSONException{
    	String errorCode = image.getString("errorCode");
    	shellRW.putStringValue("errorCode", errorCode);
        if(errorCode.equals("true")){
        	String jsonId = image.getString("id");
        	String imageId = shellRW.getStringValue("imageId");
        	if(imageId.equals("")||imageId == null||!imageId.equals(jsonId)){
        		if(isWifi == true){
        			shellRW.putStringValue("imageId", jsonId);
            	  	DownLoadImg down = new DownLoadImg(packageName);
            		down.downThread(image.getString("imageUrl"));
        		}
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
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		Constants.MAC = info.getMacAddress();
		Constants.IMEI = telephonyManager.getDeviceId();
		Constants.IMSI = telephonyManager.getSubscriberId();
		Constants.MACHINE_ID = Build.MODEL;
		String moblie = telephonyManager.getLine1Number();
		if(moblie!=null&&moblie.equals("null")){
			Constants.PHONE_SIM = moblie;
		}
		Log.e("moblie",""+moblie);

	}

	/**
	 * 清空上次的登录信息
	 */
	public void clearLastLoginInfo() {
		shellRW.putStringValue("sessionid", "");
		shellRW.putStringValue("userno", "");
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
					HomeUpdate update = new HomeUpdate(HomeActivity.this,new Handler(), softwareurl, softwaremessageStr,softwaretitle);
					update.setDialogNoBack();
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
		MobclickAgent.onResume(this);//BY贺思明 2012-6-28
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this); //BY贺思明 2012-6-28
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	/**
	 * 检验彩种设置信息
	 */
	public static List<Map<String, String>> shellRWList;
	private String[] titles = { "合买大厅","双色球","福彩3D","七乐彩","大乐透","排列三","排列五","七星彩","22选5","时时彩","江西11选5","11运夺金","专家荐号","广东11选5","足彩","竞足彩","竞篮彩","广东快乐十分"};
	private String iGameName[] = {"hmdt","ssq", "fc3d",    "qlc",      "cjdlt",   "pl3",     "pl5",     "qxc",     "22-5",  "ssc",     "11-5",           "11-ydj",     "zjjh",       "gd-11-5",    "zc",    "jcz",         "jcl",          "gd-10"}; // 8.9
	private void checkCaizhongSetting() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, ShellRWConstants.CAIZHONGSETTING);
		shellRWList = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		for (int i = 0; i < iGameName.length; i++) {
			map =new  HashMap<String, String>();
//			if (iGameName[i].equals("sfc")) {
//				map.put("shellKey","zc");
//				map.put("shellName", "足彩");
//				shellRWList.add(map);
//			}else if(iGameName[i].equals("rxj")){
//			}else if(iGameName[i].equals("lcb")){
//			}else if(iGameName[i].equals("jqc")){
//			}else {
//				 if(titles[i].equals("合买大厅")) {
//						map.put("shellKey", "hmdt");
//						map.put("shellName", titles[i].toString());
//						shellRWList.add(map);
//					}else if(titles[i].equals("专家荐号")) {
//						map.put("shellKey", "zjjh");
//						map.put("shellName", titles[i].toString());
//						shellRWList.add(map);
//					}else {
						map.put("shellKey", iGameName[i].toString());
						map.put("shellName", titles[i].toString());
						shellRWList.add(map);
//					}
//			}
		}
		//获得彩种
		for (int i = 0; i < shellRWList.size(); i++) {
			String channel = shellRW.getStringValue(shellRWList.get(i).get("shellKey"));
			if (channel.equals("")||channel == null) {
					shellRW.putStringValue(shellRWList.get(i).get("shellKey"), Constants.CAIZHONG_OPEN);
			} 
		}
	}
}
