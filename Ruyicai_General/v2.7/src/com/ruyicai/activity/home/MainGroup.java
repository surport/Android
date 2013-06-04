package com.ruyicai.activity.home;

import org.json.JSONObject;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.account.AccountActivity;
import com.ruyicai.activity.buy.BuyActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.info.LotInfoActivity;
import com.ruyicai.activity.join.JoinHallActivity;
import com.ruyicai.activity.more.CompanyInfo;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.activity.more.HelpCenter;
import com.ruyicai.activity.more.MoreActivity;
import com.ruyicai.activity.notice.NoticePrizesOfLottery;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ShellRWSharesPreferences;

public class MainGroup extends ActivityGroup {

	public static TabHost mTabHost = null;
	private LayoutInflater mInflater = null;
	private String[] mSubTabTagArray = new String[] { "first", "second","third", "fourth", "fifth" };
	private TabHost.TabSpec firstSpec = null;
	private TabHost.TabSpec secondSpec = null;
	private TabHost.TabSpec thirdSpec = null;
	private TabHost.TabSpec fourthSpec = null;
	private TabHost.TabSpec fifthSpec = null;
	private TextView moneyText;
	private ImageButton login;
	ShellRWSharesPreferences shellRW ;
	Handler handler = new Handler();
    int index = 0;	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initNum();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_group);
		login = (ImageButton) findViewById(R.id.mainpage_chongzhi);
		moneyText = (TextView) findViewById(R.id.mainpagetext2);
		mTabHost = (TabHost) findViewById(R.id.tab_host);
		mTabHost.setup(getLocalActivityManager());
		// 监听tab切换事件
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				for(int i=0;i<mSubTabTagArray.length-1;i++){
					if (tabId.equals(mSubTabTagArray[i])) {
					    index = i;
					}
				}
				if (tabId.equals(mSubTabTagArray[4])) {
					mTabHost.setCurrentTab(index);
					MainGroup.super.openOptionsMenu(); 
				}
		
			}
		});
		mInflater = LayoutInflater.from(this);
		initTabWidge();
	}

	/**
	 * 初始化顶部组件
	 */
	private void initTop() {
		 shellRW = new ShellRWSharesPreferences(MainGroup.this, "addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");
		String userno = shellRW.getUserLoginInfo("userno");
		if (sessionid == null || sessionid.equals("")) {
			login.setBackgroundResource(R.drawable.chongzhi);
		} else {
			login.setBackgroundResource(R.drawable.chongzhi_b);
			mountThread(sessionid, userno, phonenum);
		}
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainGroup.this, UserLogin.class);
				startActivity(intent);
			}
		});
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
						shellRW.setUserLoginInfo(Constants.DRAWBALANCE, drawMoney);
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
		secondSpec = mTabHost.newTabSpec(mSubTabTagArray[1]).setIndicator(indicatorTab2).setContent(new Intent(MainGroup.this, NoticePrizesOfLottery.class));
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
		fourthSpec = mTabHost.newTabSpec(mSubTabTagArray[3]).setIndicator(indicatorTab4).setContent(new Intent(MainGroup.this, LotInfoActivity.class));
		mTabHost.addTab(fourthSpec);

		View indicatorTab5 = mInflater.inflate(R.layout.layout_nav_item, null);
		((ImageView) indicatorTab5.findViewById(R.id.buttontype)).setImageResource(R.drawable.ico_more);
		((ImageView) indicatorTab5.findViewById(R.id.layout_nav_item)).setBackgroundResource(R.drawable.buy_selector);
		fifthSpec = mTabHost.newTabSpec(mSubTabTagArray[4]).setIndicator(indicatorTab5);
		fifthSpec.setContent(new Intent(MainGroup.this, MoreActivity.class));
		mTabHost.addTab(fifthSpec);
		
		Intent intent = getIntent();
		String fromHomeMark = intent.getStringExtra(Constants.NOTIFICATION_MARKS);
//		Log.v("", "fromHomeMark="+fromHomeMark);
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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.usercenter:
			// mTabHost.setCurrentTab(0);
			// mTabHost.clearAllTabs();
			Intent intent = new Intent(MainGroup.this, NewUserCenter.class);
			startActivity(intent);
			break;
		case R.id.feedback:
			// mTabHost.setCurrentTab(0);
			// mTabHost.clearAllTabs();
			Intent intent1 = new Intent(MainGroup.this, FeedBack.class);
			startActivity(intent1);
			break;
		case R.id.help:
			Intent intent2 = new Intent(MainGroup.this, HelpCenter.class);
			startActivity(intent2);
			break;
		case R.id.about:
			Intent intent3 = new Intent(MainGroup.this, CompanyInfo.class);
			startActivity(intent3);
			break;
		case R.id.eixt:
			ExitDialogFactory.createExitDialog(this);
			break;
		default:

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initTop();
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