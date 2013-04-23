package com.ruyicai.activity.home;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.net.transaction.GetUserInfoInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;
import com.ruyicai.view.RadioStateDrawable;
import com.ruyicai.view.TabBarButton;

public class ScrollableTabActivity extends ActivityGroup implements
/* MyDialogListener, */OnClickListener, RadioGroup.OnCheckedChangeListener {

	private LocalActivityManager activityManager;
	private LinearLayout contentViewLayout;
	private LinearLayout.LayoutParams contentViewLayoutParams;
	private RadioGroup.LayoutParams buttonLayoutParams;
	public RadioGroup bottomRadioGroup;
	private Context context;
	private List intentList;
	private List myIntentList;
	private List titleList;
	private List states;
	private SliderBarActivityDelegate delegate;
	private int defaultOffShade;
	private int defaultOnShade;

	private IntentFilter changeTabIntentFilter;
	private ChangeTabBroadcastReceiver changeTabBroadcastReceiver;
	private IntentFilter loginSuccessFilter;
	private SuccessReceiver loginSuccessReceiver;
	public static String ACTION_LOGIN_SUCCESS = "loginsuccess";
	public static String CURRENT_TAB_INDEX;
	public static String ACTION_CHANGE_TAB = "com.mobyfactory.changeTab";

	TextView tv_left;
	// TextView tv_right;
	ImageView iv_right;
	private ShellRWSharesPreferences shellRW;
	private ImageView chongzhi;
	public static boolean isturn = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		context = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		activityManager = getLocalActivityManager();

		setContentView(R.layout.customslidingtabhost);
		// 充值按钮
		chongzhi = (ImageView) findViewById(R.id.mainpage_chongzhi);

		chongzhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScrollableTabActivity.this,UserLogin.class);
				ScrollableTabActivity.this.startActivity(intent);
			}
		});

		layout = (LinearLayout) findViewById(R.id.contentViewLayout1);
		contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
		// bottomBar = (HorizontalScrollView) findViewById(R.id.bottomBar);

		bottomRadioGroup = (RadioGroup) findViewById(R.id.bottomMenu);// 底部tab按钮
		contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		defaultOffShade = RadioStateDrawable.SHADE_GRAY;
		defaultOnShade = RadioStateDrawable.SHADE_YELLOW;

		// startMarquee();

		loginSuccessFilter = new IntentFilter(ACTION_LOGIN_SUCCESS);
		// loginSuccessReceiver = new SuccessReceiver();
		registerReceiver(loginSuccessReceiver, loginSuccessFilter);

		bottomRadioGroup.setOnCheckedChangeListener(this);
		intentList = new ArrayList();
		titleList = new ArrayList();
		states = new ArrayList();

		myIntentList = new ArrayList();

		buttonLayoutParams = new RadioGroup.LayoutParams(
				RadioGroup.LayoutParams.FILL_PARENT,
				RadioGroup.LayoutParams.FILL_PARENT);
	}

	@Override
	protected void onStart() {
		
		super.onStart();
		Log.v("ScrollableonStart", "onStart");
		shellRW = new ShellRWSharesPreferences(this, "addInfo");
		String ssoinid = shellRW.getUserLoginInfo("sessionid");
		loginSuccessFilter = new IntentFilter(UserLogin.SUCCESS);
		loginSuccessReceiver = new SuccessReceiver();
		registerReceiver(loginSuccessReceiver, loginSuccessFilter);
		Log.v("sessionid", ssoinid);
		Log.v("PublicConst.islogin", PublicConst.islogin+"");
		if (PublicConst.islogin && !ssoinid.equals("")) {
			TextView text = (TextView) findViewById(R.id.mainpagetext2);
			text.setText("查询中..");
			showSuccessReceiver();
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(loginSuccessReceiver);
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4: {
			activityManager.getCurrentActivity().onKeyDown(0x12345678, event); // 周黎鸣
			// 7.8yManager.saveInstanceState()
			break;
		}
		}
		// return super.onKeyDown(keyCode, event);
		return false;
	}

	public void onResume() {
		if (isturn||PublicConst.islogin) {
			chongzhi.setBackgroundResource(R.drawable.chongzhi_b);
		} else {
			chongzhi.setBackgroundResource(R.drawable.chongzhi);
		}
		changeTabIntentFilter = new IntentFilter(ACTION_CHANGE_TAB);
		changeTabBroadcastReceiver = new ChangeTabBroadcastReceiver();
		registerReceiver(changeTabBroadcastReceiver, changeTabIntentFilter);
		super.onResume();

	}

	public void onPause() {
		unregisterReceiver(changeTabBroadcastReceiver);
		super.onPause();
	}
    
	//创建菜单
    @Override
 public boolean onCreateOptionsMenu(Menu menu) {
 	// TODO Auto-generated method stub
 	   MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainpage_menu,menu);
        Log.v("onCreateOptionsMenu", "onCreateOptionsMenu");
 	   return super.onCreateOptionsMenu(menu);
 	
 }
	//菜单响应
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.chaxun:
			Intent intent = new Intent(ScrollableTabActivity.this,RuyicaiAndroid.class);
			Bundle bundle = new Bundle();
			bundle.putInt("index", 3);
			intent.putExtras(bundle);
			startActivity(intent);
			this.finish();
			Constants.RUYIHELPERSHOWLISTTYPE=2;
		    break;
		case R.id.zhanghu:
			Intent intent2 = new Intent(ScrollableTabActivity.this,RuyicaiAndroid.class);
			Bundle bundle2 = new Bundle();
			bundle2.putInt("index", 3);
			intent2.putExtras(bundle2);
			startActivity(intent2);
			this.finish();
			Constants.RUYIHELPERSHOWLISTTYPE=3;
			break;
		case R.id.help:
			Intent intent3 = new Intent(ScrollableTabActivity.this,RuyicaiAndroid.class);
			Bundle bundle3 = new Bundle();
			bundle3.putInt("index", 3);
			intent3.putExtras(bundle3);
			startActivity(intent3);
			this.finish();
			Constants.RUYIHELPERSHOWLISTTYPE=4;
			break;
		case R.id.about:
			Intent intent4 = new Intent(ScrollableTabActivity.this,RuyicaiAndroid.class);
			Bundle bundle4 = new Bundle();
			bundle4.putInt("index", 3);
			intent4.putExtras(bundle4);
			startActivity(intent4);
			this.finish();
			Constants.RUYIHELPERSHOWLISTTYPE=5;
			break;
		case R.id.eixt:

			ExitDialogFactory.createExitDialog(this);
			break;
		default:
			
			break;

		}
		return true;
	}
	
	public void commit(int index) {
		bottomRadioGroup.removeAllViews();

		int optimum_visible_items_in_portrait_mode = 4;
//		try {
//			WindowManager window = getWindowManager();
//			Display display = window.getDefaultDisplay();
//			int window_width = display.getWidth();
//
//			optimum_visible_items_in_portrait_mode = (int) (window_width / 64.0);
//		} catch (Exception e) {
//			optimum_visible_items_in_portrait_mode = 4;
//		}

		int width;
		if (intentList.size() <= optimum_visible_items_in_portrait_mode) {
			width = Constants.SCREEN_WIDTH / intentList.size();
		} else {
			// width = screen_width/5;
			width = 64;// 320/5;
		}
		RadioStateDrawable.width = width;
		RadioStateDrawable.screen_width = Constants.SCREEN_WIDTH;
		buttonLayoutParams = new RadioGroup.LayoutParams(width, RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < intentList.size(); i++) {
			TabBarButton tabButton = new TabBarButton(this);
			int[] iconStates = (int[]) states.get(i);

			tabButton.setState(iconStates[0], iconStates[1], 4);
			
			
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);

			bottomRadioGroup.addView(tabButton, i, buttonLayoutParams);
		}

		setCurrentTab(index);
//		commit(index);
	}

	/**
	 * 增加一个底部tab页按钮
	 * 
	 * @param title
	 * @param offIconStateId
	 * @param onIconStateId
	 * @param intent
	 */
	protected void addTab(String title, int offIconStateId, int onIconStateId,
			Intent intent) {
		int[] iconStates = { onIconStateId, offIconStateId };
		states.add(iconStates);
		intentList.add(intent);
		titleList.add(title);
		// commit();
	}

	/**
	 * <b><i>protected void addTab(String title, int iconStateId, Intent
	 * intent)</i></b>
	 * <p>
	 * <p>
	 * Add a tab to the navigation bar by specifying the title, and 1 image for
	 * the button. Default yellow/gray shade is used for button on/off state
	 * <p>
	 * 
	 * @param title
	 *            a String that specifies that title of the tab button
	 * @param iconStateId
	 *            id of the image used for both on/off state
	 * @param intent
	 *            intent to start when button is tapped
	 */
	protected void addTab(String title, int iconStateId, Intent intent) {
		// int[] iconStates = {iconStateId};
		int[] iconStates = { iconStateId, defaultOffShade, defaultOnShade };
		states.add(iconStates);
		intentList.add(intent);
		titleList.add(title);
		//commit();
	}

	protected void addMyIntent(Intent intent) {
		myIntentList.add(intent);
	}

	protected void addTab(String title, int iconStateId, int offShade,
			int onShade, Intent intent) {
		int[] iconStates = { iconStateId, offShade, onShade };
		states.add(iconStates);
		intentList.add(intent);
		titleList.add(title);
		// commit();
	}

	protected void setDefaultShade(int offShade, int onShade) {
		defaultOffShade = offShade;
		defaultOnShade = onShade;
	}

	/**
	 * 底部切换view事件
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		try {
			if (delegate != null) {
				delegate.onTabChanged(checkedId);
			}
		} catch (Exception e) {

		}
		startGroupActivity(titleList.get(checkedId).toString(),(Intent) intentList.get(checkedId));
	}

	public void startGroupActivity(String id, Intent intent) {
		contentViewLayout.removeAllViews();

		View view = activityManager.startActivity(id, intent).getDecorView();
		// View view = activityManager.startActivity("123456",
		// (Intent)myIntentList.get(0)).getDecorView();
		contentViewLayout.addView(view, contentViewLayoutParams);
	}

	public void setCurrentTab(int index) {
		bottomRadioGroup.check(index);
		startGroupActivity(titleList.get(index).toString(), (Intent) intentList
				.get(index));
	}

	public int getCurrentTab() {
		return bottomRadioGroup.getCheckedRadioButtonId();
	}

	public static abstract class SliderBarActivityDelegate {

		/*
		 * Called when tab changed
		 */
		protected void onTabChanged(int tabIndex) {
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			// tv_right.setText(R.string.welcome);
			// 2010.1.17
			// iv_right.setImageResource(R.drawable.login);
			// iLoginFlag = true;
		}
	}

	public class ChangeTabBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int index = intent.getExtras().getInt(CURRENT_TAB_INDEX);
			setCurrentTab(index);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	public static LinearLayout layout;

	public static void gone() {
		layout.setVisibility(LinearLayout.GONE);
	}

	public static void visible() {
		layout.setVisibility(LinearLayout.VISIBLE);
	}

	public void showSuccessReceiver() {
		Thread t = new Thread() {
			public void run() {
                
				ShellRWSharesPreferences shell = new ShellRWSharesPreferences(
						ScrollableTabActivity.this, "addInfo");
				String mobile = shell.getUserLoginInfo("phonenum");
				String sessionid = shell.getUserLoginInfo("sessionid");
				JSONObject obj = null;
				String re = GetUserInfoInterface
						.findUserInfo(mobile, sessionid);
				try {
				obj = new JSONObject(re);
				
				String totalBalance = "";
	
				int amt=0;
				amt=Integer.parseInt(obj.getString("deposit_amount"));
				    // 20100710 cc 修改余额格式
					totalBalance = PublicMethod.changeMoney(Integer.toString(amt));
					Constants.LOGIN_USER_BALANCE = totalBalance;
				}catch (Exception e) {
					e.printStackTrace();
				}
				updateUserBalanceHandler.post(updateUserBalance);
			}
		};
		t.start();

	}

	final Handler updateUserBalanceHandler = new Handler();

	/**
	 * 更新用户余额
	 */
    final Runnable updateUserBalance = new Runnable() {
        public void run() {
            TextView text=(TextView)findViewById(R.id.mainpagetext2);
            text.setText(Constants.LOGIN_USER_BALANCE+"元");
//            if(Constants.FACE_TYPE==1&&Constants.BALANCE_SHOW&&Float.valueOf(Constants.LOGIN_USER_BALANCE)<2f){
//            Accoutdialog.getInstance().createAccoutdialog(ScrollableTabActivity.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
//              Constants.BALANCE_SHOW=false;
//            }
        }
    };
	public class SuccessReceiver extends BroadcastReceiver {
		Context context;

		@Override
		public void onReceive(Context context, Intent intent) {
			this.context = context;
			chongzhi.setBackgroundResource(R.drawable.chongzhi_b);
			isturn = true;
			showSuccessReceiver();
		}
	}

}
