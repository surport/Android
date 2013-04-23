package com.ruyicai.activity.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.account.AccountActivity;
import com.ruyicai.activity.buy.BuyActivity;
import com.ruyicai.activity.common.OrderPrizeDiaog;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.more.CompanyInfo;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.activity.more.HelpTitles;
import com.ruyicai.activity.more.MoreActivity;
import com.ruyicai.activity.notice.NoticeMainActivity;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.dialog.LogOutDialog;
import com.ruyicai.dialog.MyDialogListener;
import com.ruyicai.dialog.UpdateDialog;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.net.newtransaction.SoftwareUpdateInterface;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

public class MainGroup extends ActivityGroup implements MyDialogListener{

	public static TabHost mTabHost = null;
	private LayoutInflater mInflater = null;
	private String[] mSubTabTagArray = new String[] { "first", "second","third", "fourth", "fifth" };
	private TabHost.TabSpec firstSpec = null;
	private TabHost.TabSpec secondSpec = null;
	private TabHost.TabSpec thirdSpec = null;
	private TabHost.TabSpec fourthSpec = null;
	private TabHost.TabSpec fifthSpec = null;
	private final int USERCENTER = 3;
	private  TextView moneyText;
	private  TextView ruyicaiLogo;
	private  TextView usernameText;
	private  TextView yuetext;
	public   TextView shouyeTitle;
	private  LinearLayout usermessage;//用户名和账户余额信息Linear
	private  String   username;
	private ImageButton login;
	private boolean isAutoLogin = false;
	
	private Button dingyue;
	private TopinitReceiver receiver;
	RWSharedPreferences shellRW ;
	OrderPrizeDiaog orderPrizeDialog ;//开奖订阅类
	Handler handler = new Handler();
    private int index = 0;
    ProgressDialog pBar;
    LogOutDialog logOutDialog;//注销框
    boolean   isnotice = false;//判断是不是开奖信息页面
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initNum();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_group);
		shellRW = new RWSharedPreferences(MainGroup.this, "addInfo");
		orderPrizeDialog = new OrderPrizeDiaog(shellRW, MainGroup.this);//开奖订阅
		isAutoLogin = shellRW.getBooleanValue(ShellRWConstants.AUTO_LOGIN); 
		shouyeTitle = (TextView)findViewById(R.id.ruyicai_icon_text);
		ruyicaiLogo = (TextView)findViewById(R.id.ruyicai_beauty_logo);
		ruyicaiLogo.setVisibility(View.GONE);
		login = (ImageButton) findViewById(R.id.mainpage_chongzhi);
		usermessage = (LinearLayout)findViewById(R.id.shouye_usermessage_linear);
		dingyue = (Button) findViewById(R.id.mainpage_dingyue);
		moneyText = (TextView) findViewById(R.id.mainpagetext2);
		usernameText = (TextView)findViewById(R.id.mainpagetext3);
		yuetext = (TextView)findViewById(R.id.mainpagetext1);
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		mTabHost.setup(getLocalActivityManager());
		// 监听tab切换事件
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for(int i=0;i<mSubTabTagArray.length;i++){
					if (tabId.equals(mSubTabTagArray[i])) {
					    index = i;
					}
				}
				switch (index) {
					case 0:
						isnotice = false;
						usermessage.setVisibility(View.VISIBLE);
						shouyeTitle.setVisibility(View.VISIBLE);
						shouyeTitle.setText(R.string.goucaidating);
						dingyue.setVisibility(View.GONE);
						isShowLoginButton();
						break;
					case 1:
						isnotice = true;
						shouyeTitle.setVisibility(View.VISIBLE);
						shouyeTitle.setText(R.string.kaijiangxinxi);
						NoticeMainActivity.isFirstNotice = true;
						ruyicaiLogo.setVisibility(View.GONE);
						login.setVisibility(View.INVISIBLE);
						usermessage.setVisibility(View.INVISIBLE);
						dingyue.setVisibility(View.VISIBLE);
						dingyue.setText("订阅");
	                    dingyue.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								orderPrizeDialog.orderPrizeDialog().show();
							}
						});
						break;
					case 2:
						isnotice = false;
						isShowLoginButton();
						dingyue.setVisibility(View.GONE);
						usermessage.setVisibility(View.VISIBLE);
						ruyicaiLogo.setVisibility(View.GONE);
						shouyeTitle.setVisibility(View.VISIBLE);
						shouyeTitle.setText(R.string.zhanghuchongzhi1);
						break;	
					case 3:
						isnotice = false;
						ruyicaiLogo.setVisibility(View.GONE);
						shouyeTitle.setVisibility(View.VISIBLE);
						shouyeTitle.setText(R.string.yonghuzhongxin);
						login.setVisibility(View.INVISIBLE);
						usermessage.setVisibility(View.INVISIBLE);
						dingyue.setVisibility(View.VISIBLE);
						dingyue.setText("注销");
	                    dingyue.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								// TODO Auto-generated method stub
								logOutDialog = LogOutDialog.createDialog(MainGroup.this);
								logOutDialog.setOnClik(MainGroup.this);
							}
						});
					    isLogin();
						break;
					case 4:
						isnotice = false;
						isShowLoginButton();
						dingyue.setVisibility(View.GONE);
						usermessage.setVisibility(View.VISIBLE);
						ruyicaiLogo.setVisibility(View.GONE);
						shouyeTitle.setVisibility(View.VISIBLE);
						shouyeTitle.setText(R.string.more);
						break;
				}
			}

		
		});
		
		mInflater = LayoutInflater.from(this);
		initTabWidge();
	}

	private void isLogin() {
		RWSharedPreferences shellRW = new RWSharedPreferences(MainGroup.this, "addInfo");
		String sessionid = shellRW.getStringValue("sessionid");
		if(sessionid.equals("")||sessionid==null){
			Intent intent =new Intent(MainGroup.this,UserLogin.class);		
			startActivityForResult(intent, 0);
		}
	}
	private  void isShowLoginButton(){
		if(Constants.hasLogin == false){
			login.setVisibility(View.VISIBLE);
		}else{
			login.setVisibility(View.GONE);
		}
	}
	/**
	 * 初始化顶部组件
	 */
	public void initTop() {
		if(!isnotice){
		if(index!=USERCENTER){
			dingyue.setVisibility(View.INVISIBLE);
			login.setVisibility(View.VISIBLE);
		}else{
			dingyue.setVisibility(View.VISIBLE);
		}
		String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
	    username =shellRW.getStringValue(ShellRWConstants.USERNAME);
		if (userno == null || userno.equals("")) {
			login.setBackgroundResource(R.drawable.chongzhi);
			moneyText.setText("");
			usernameText.setText("");
			yuetext.setText("");
		} else {
			updateMoneny();
		}
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainGroup.this, UserLogin.class);
				startActivity(intent);
			}
		});
		}
	}

	private void updateMoneny() {
		String sessionid = shellRW.getStringValue(ShellRWConstants.SESSIONID);
		String phonenum = shellRW.getStringValue(ShellRWConstants.PHONENUM);
		String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
		yuetext.setText("余额:");
		usernameText.setText(getusername(username));
		login.setVisibility(View.GONE);
		Constants.hasLogin = true;
		mountThread(sessionid, userno, phonenum);
	}
    private String  getusername(String username){
    	if(username.equals("")){
    		return username;
    	}else if(username.length()<=8){
    		return username;
    	}else{
    		String name = username.substring(0,8)+"**";
    		return name;
    	}
    	
    }
	/**
	 * 余额查询线程
	 */
	public void mountThread(final String sessionid, final String userno,
			final String phonenum) {
		moneyText.setText("查询中....");
		new Thread(new Runnable() {
			public void run() {
				try {
					String str = BalanceQueryInterface.balanceQuery(userno,
							sessionid, phonenum);
					JSONObject json = new JSONObject(str);
					String error_code = json.getString("error_code");
					if (error_code.equals("0000")) {
						final String money = json.getString("bet_balance");
						final String drawMoney = json.getString(Constants.DRAWBALANCE);
						shellRW.putStringValue(Constants.DRAWBALANCE, drawMoney);
						handler.post(new Runnable() {
							public void run() {
								moneyText.setText(money);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.post(new Runnable() {
						public void run() {
							moneyText.setText("查询失败");
						}
					});
				}
			}
		}).start();
	}

	private void initTabWidge() {
		View indicatorTab1 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab1.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		((ImageView) indicatorTab1.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_home);
		firstSpec = mTabHost.newTabSpec(mSubTabTagArray[0]).setIndicator(indicatorTab1).setContent(new Intent(MainGroup.this, BuyActivity.class));
		mTabHost.addTab(firstSpec);

		View indicatorTab2 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab2.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		((ImageView) indicatorTab2.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_announcement);
		secondSpec = mTabHost.newTabSpec(mSubTabTagArray[1]).setIndicator(indicatorTab2).setContent(new Intent(MainGroup.this, NoticeMainActivity.class));
		mTabHost.addTab(secondSpec);

		View indicatorTab3 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab3.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_money);
		((ImageView) indicatorTab3.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		thirdSpec = mTabHost.newTabSpec(mSubTabTagArray[2]).setIndicator(indicatorTab3).setContent(new Intent(MainGroup.this, AccountActivity.class));
		mTabHost.addTab(thirdSpec);

		// 彩票资讯
		View indicatorTab4 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab4.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_info);
		((ImageView) indicatorTab4.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		fourthSpec = mTabHost.newTabSpec(mSubTabTagArray[3]).setIndicator(indicatorTab4).setContent(new Intent(MainGroup.this, NewUserCenter.class));
		mTabHost.addTab(fourthSpec);

		View indicatorTab5 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab5.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_more);
		((ImageView) indicatorTab5.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		fifthSpec = mTabHost.newTabSpec(mSubTabTagArray[4]).setIndicator(indicatorTab5);
		fifthSpec.setContent(new Intent(MainGroup.this, MoreActivity.class));
		mTabHost.addTab(fifthSpec);
		
		Intent intent = getIntent();
		String fromHomeMark = intent.getStringExtra(Constants.NOTIFICATION_MARKS);
		if(fromHomeMark!=null&&fromHomeMark.equals("notice")){
			mTabHost.setCurrentTab(1);
		}else{
			mTabHost.setCurrentTab(0);
		}

	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			break;
		}
		return false;
	}

	/**
	 * 根据屏幕初始化一些数据
	 */
	protected void initNum() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		Constants.SCREEN_DENSITYDPI = metric.densityDpi;
		Constants.SCREEN_DENSITY = metric.density;
		Constants.SCREEN_WIDTH = getWindowManager().getDefaultDisplay()
				.getWidth();// 设置屏幕宽度
		Constants.SCREEN_HEIGHT = getWindowManager().getDefaultDisplay()
				.getHeight();
	}

	// 创建菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainpage_menu, menu);
		return super.onCreateOptionsMenu(menu);

	}

	// 菜单响应
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.userExit:
			logOutDialog = LogOutDialog.createDialog(this);
			logOutDialog.setOnClik(this);
			break;
		//case R.id.prizeLook:
		case R.id.prizeLook:
//			Intent intent = new Intent(MainGroup.this, OrderPrizeMessage.class);
//			startActivity(intent);
//			ExitDialogFactory.createExitDialog(this);
		
			orderPrizeDialog.orderPrizeDialog().show();
			break;
		case R.id.feedback:
			Intent intent1 = new Intent(MainGroup.this, FeedBack.class);
			startActivity(intent1);
			break;
		case R.id.help:
			Intent intent2 = new Intent(MainGroup.this, HelpTitles.class);
			startActivity(intent2);
			break;
		case R.id.about:
			Intent intent3 = new Intent(MainGroup.this, CompanyInfo.class);
			startActivity(intent3);
			break;
		case R.id.update:
			if (HomeActivity.softwareErrorCode.equals("true")) {
				MainUpdate update = new MainUpdate(MainGroup.this,new Handler(),HomeActivity.softwareurl, HomeActivity.softwaremessageStr, HomeActivity.softwaretitle);
				update.showDialog();
				update.createMyDialog();
			}else if(HomeActivity.softwareErrorCode.equals("not")){
				isUpdateNet();
			}else{
				Toast.makeText(this, "当前已经是最新版本！", Toast.LENGTH_SHORT).show();
			}
			break;
		default:

			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MainUpdate extends UpdateDialog{

		public MainUpdate(Activity activity, Handler handler, String url,
				String message, String title) {
			super(activity, handler, url, message, title);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCancelButton() {
			// TODO Auto-generated method stub
			
		}


		
	}
	/**
	 * 联网检测新版本
	 * 
	 */
	public void isUpdateNet(){
		pBar = UserCenterDialog.onCreateDialog(this);
		pBar.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject obj;
				try {
				obj = new JSONObject(SoftwareUpdateInterface.getInstance().softwareupdate(null,shellRW.getStringValue("randomNumber")));
				pBar.dismiss();
				String softwareErrorCode = obj.getString("errorCode");
				if (softwareErrorCode.equals("true")) {
					// 需要升级,设置升级相关字段
					final String softwareurl = obj.getString("updateurl");
					final String softwaretitle = obj.getString("title");
					final String softwaremessageStr = obj.getString("message");
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							MainUpdate update = new MainUpdate(MainGroup.this,new Handler(),softwareurl, softwaremessageStr, softwaretitle);
							update.showDialog();
							update.createMyDialog();
						}
					});
				}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(Constants.isInitTop == true){
			initTop();
		}
        receiver = new TopinitReceiver();
        registerReceiver(receiver, new IntentFilter("com.ruyicai.activity.home.MainGroup.inittop"));
	}
	

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
		initTop();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}

	@Override
	public void onOkClick() {
		// TODO Auto-generated method stub
		logOutDialog.clearLastLoginInfo();
		Constants.hasLogin = false;
		mTabHost.setCurrentTab(0);
		initTop();
	
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub
		
	}
	
	public class TopinitReceiver extends BroadcastReceiver{
	    
	    public TopinitReceiver() {
	    }
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		    initTop();
		}
	}
}