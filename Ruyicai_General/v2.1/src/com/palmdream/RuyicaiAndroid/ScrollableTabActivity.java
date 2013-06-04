/**
 * Copyright (c) 2009 Muh Hon Cheng
 * 
 * Permission is hereby granted, free of charge, to any person obtaining 
 * a copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, merge, publish, 
 * distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject 
 * to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be 
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT 
 * WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT 
 * SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR 
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * @author 		Muh Hon Cheng <honcheng@gmail.com>
 * @copyright	2009	Muh Hon Cheng
 * @version
 * 
 */

package com.palmdream.RuyicaiAndroid;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.netintface.jrtLot;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ScrollableTabActivity extends ActivityGroup implements
/* MyDialogListener, */OnClickListener, RadioGroup.OnCheckedChangeListener {

	private LocalActivityManager activityManager;
	private LinearLayout contentViewLayout;
	private LinearLayout.LayoutParams contentViewLayoutParams;
	private HorizontalScrollView bottomBar;
	private RadioGroup.LayoutParams buttonLayoutParams;
	private RadioGroup bottomRadioGroup;
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

	private MyMarqueeView iMarquee;
	Timer timer = null;
	private int iTimeInterval = 50;

	TextView tv_left;
	// TextView tv_right;
	ImageView iv_right;

	private boolean iLoginFlag = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		activityManager = getLocalActivityManager();

		setContentView(R.layout.customslidingtabhost);

		// 登录修改 2010.1.17
		// tv_left = (TextView) findViewById(R.id.tv_hellol);
		// tv_left.setText(getLeftTip());
		//
		// iv_right = (ImageView) findViewById(R.id.iv_hellor);
		// iv_right.setImageResource(R.drawable.login_register);
		// tv_right.setText(R.string.login_register);
		// iv_right.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN:
		// // tv_right.setBackgroundResource(R.drawable.frame_rectangle_user_d);
		// break;
		// case MotionEvent.ACTION_UP:
		// // tv_right.setBackgroundResource(R.drawable.frame_rectangle_user);
		// break;
		// }
		// return false;
		// }
		// });
		// iv_right.setOnClickListener(this);
		/*
		 * tv_right = (TextView)findViewById(R.id.tv_hellor);
		 * tv_right.setText(R.string.login_register);
		 * tv_right.setOnTouchListener(new OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
		 * Auto-generated method stub switch(event.getAction()){ case
		 * MotionEvent.ACTION_DOWN:
		 * tv_right.setBackgroundResource(R.drawable.frame_rectangle_user_d);
		 * break; case MotionEvent.ACTION_UP:
		 * tv_right.setBackgroundResource(R.drawable.frame_rectangle_user);
		 * break; } return false; } }); tv_right.setOnClickListener(this);
		 */
		layout = (LinearLayout) findViewById(R.id.contentViewLayout1);
		contentViewLayout = (LinearLayout) findViewById(R.id.contentViewLayout);
		// bottomBar = (HorizontalScrollView) findViewById(R.id.bottomBar);
		bottomRadioGroup = (RadioGroup) findViewById(R.id.bottomMenu);
		contentViewLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		defaultOffShade = RadioStateDrawable.SHADE_GRAY;
		defaultOnShade = RadioStateDrawable.SHADE_YELLOW;

		int iCurrentWidth = PublicMethod.getDisplayWidth(this);
		// 2010.1.17
		// iMarquee = (MyMarqueeView) findViewById(R.id.marquee);
		// // iMarquee.initMarqueeWidth(iCurrentWidth,20,2);
		// iMarquee.initMarqueeWidth(iCurrentWidth, 30, 2);
		// iMarquee.setVisibility(View.INVISIBLE);
		startMarquee();

		loginSuccessFilter = new IntentFilter(ACTION_LOGIN_SUCCESS);
		loginSuccessReceiver = new SuccessReceiver();
		registerReceiver(loginSuccessReceiver, loginSuccessFilter);
		/*
		 * Object o = findViewById(R.id.picker); Class c = o.getClass(); try {
		 * Method m = c.getMethod("setRange", int.class, int.class); m.invoke(o,
		 * 0, 9); } catch (Exception e) { //Log.e("", e.getMessage()); }
		 */
		// iMarquee.setOnClickListener(this);
		/*
		 * alternative method to using XML for layout, not used
		 */
		/*
		 * if (inflateXMLForContentView()) { contentViewLayoutParams = new
		 * LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT); } else { RelativeLayout mainLayout = new
		 * RelativeLayout(this); RelativeLayout.LayoutParams mainLayoutParams =
		 * new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT);
		 * mainLayout.setLayoutParams(mainLayoutParams); contentViewLayout = new
		 * LinearLayout(this);
		 * contentViewLayout.setOrientation(LinearLayout.VERTICAL);
		 * contentViewLayout.setBackgroundColor(Color.WHITE);
		 * contentViewLayoutParams = new
		 * LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT); contentViewLayoutParams.bottomMargin = 55;
		 * mainLayout.addView(contentViewLayout, contentViewLayoutParams);
		 * bottomBar = new HorizontalScrollView(this);
		 * //bottomBar.setHorizontalFadingEdgeEnabled(false);
		 * RelativeLayout.LayoutParams bottomBarLayout = new
		 * RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 55);
		 * bottomBarLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * mainLayout.addView(bottomBar, bottomBarLayout); bottomRadioGroup =
		 * new RadioGroup(this);
		 * bottomRadioGroup.setOrientation(RadioGroup.HORIZONTAL); LayoutParams
		 * bottomRadioGroupLayoutParam = new
		 * LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		 * bottomRadioGroup.setLayoutParams(bottomRadioGroupLayoutParam);
		 * bottomBar.addView(bottomRadioGroup); if (bottomBar()!=-1)
		 * bottomBar.setBackgroundResource(bottomBar());
		 * //bottomBar.setBackgroundResource(R.drawable.bottom_bar);
		 * setContentView(mainLayout); }
		 */

		bottomRadioGroup.setOnCheckedChangeListener(this);
		intentList = new ArrayList();
		titleList = new ArrayList();
		states = new ArrayList();

		myIntentList = new ArrayList();

		buttonLayoutParams = new RadioGroup.LayoutParams(/* 320/5 */64,
				RadioGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>ScrollableTabActivity key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			activityManager.getCurrentActivity().onKeyDown(0x12345678, event); // 周黎鸣
			// 7.8
			// 代码修改：添加特殊值，来区分onKeyDown
			// activityManager.saveInstanceState()
			break;
		}
		}
		// return super.onKeyDown(keyCode, event);
		return true;
	}

	public void startMarquee() {
		// 20100712 delete fyj
		// TnoticeInterface tnotice=new TnoticeInterface();
		// String news=tnotice.Tnotice();
		// 2010.1.17
		// String news = "欢迎使用如意彩";
		// ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
		// ScrollableTabActivity.this, "addInfo");
		// String re = shellRW.getUserLoginInfo("information0");
		// if (re.equalsIgnoreCase("") || re == null) {
		// // 获取失败 不改变news
		// } else {
		// try {
		// JSONObject obj = new JSONObject(re);
		// news = obj.getString("news");
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// // String[] iPmdText={news,"test2"};
		// String[] iPmdText = { news };
		// // String[] iPmdText={"test1","test2"};
		// ScrollableTabActivity.this.iMarquee.initMarqueeString(iPmdText);
		// iMarquee.initMarqueeString(iPmdText);
		//
		// iMarquee.setVisibility(View.VISIBLE);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public final void run() {
				updateMarquee();
			}
		}, 0, iTimeInterval);
		// timer.schedule(task, iTimeInterval,iTimeInterval);
	}

	public void updateMarquee() {
		Message message = new Message();
		message.what = 0x80000001;
		handler.sendMessage(message);
	}

	private Handler handler = new Handler() {
		// @Override
		public void handleMessage(Message msg) {
			InputStream a = (InputStream) msg.obj;
			switch (msg.what) {
			case 0x80000001: {
				// PublicMethod.myOutput("--->>>80000001");
				// 2010.1.17
				// iMarquee.invalidate();
				break;
			}
			}
		}
	};

	public void onResume() {
		changeTabIntentFilter = new IntentFilter(ACTION_CHANGE_TAB);
		changeTabBroadcastReceiver = new ChangeTabBroadcastReceiver();
		registerReceiver(changeTabBroadcastReceiver, changeTabIntentFilter);
		super.onResume();
	}

	public void onPause() {
		unregisterReceiver(changeTabBroadcastReceiver);
		super.onPause();
	}

	public void commit(int index) {
		bottomRadioGroup.removeAllViews();

		int optimum_visible_items_in_portrait_mode = 5;
		try {
			WindowManager window = getWindowManager();
			Display display = window.getDefaultDisplay();
			int window_width = display.getWidth();

			optimum_visible_items_in_portrait_mode = (int) (window_width / 64.0);
		} catch (Exception e) {
			optimum_visible_items_in_portrait_mode = 5;
		}

		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width;
		if (intentList.size() <= optimum_visible_items_in_portrait_mode) {
			width = screen_width / intentList.size();
		} else {
			// width = screen_width/5;
			width = 64;// 320/5;
		}
		RadioStateDrawable.width = width;
		RadioStateDrawable.screen_width = screen_width;
		buttonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < intentList.size(); i++) {
			TabBarButton tabButton = new TabBarButton(this);
			int[] iconStates = (int[]) states.get(i);
			Log.e("iconStates===", "=" + iconStates.length);
			if (iconStates.length == 1)
				tabButton.setState(titleList.get(i).toString(), iconStates[0]);
			else if (iconStates.length == 2)
				// tabButton.setState(titleList.get(i).toString(),
				// iconStates[0],
				// iconStates[1]);
				tabButton.setState(iconStates[0], iconStates[1]);
			else if (iconStates.length == 3)
				tabButton.setState(titleList.get(i).toString(), iconStates[0],
						iconStates[1], iconStates[2]);
			// tabButton.setButtonDrawable(iconStates[0]);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			bottomRadioGroup.addView(tabButton, i, buttonLayoutParams);
		}

		setCurrentTab(index);
	}

	/**
	 * <b><i>protected void addTab(String title, int offIconStateId, int
	 * onIconStateId, Intent intent)</i></b>
	 * <p>
	 * <p>
	 * Add a tab to the navigation bar by specifying the title, 1 image for
	 * button on-state, and 1 image for button off-state
	 * <p>
	 * 
	 * @param title
	 *            a String that specifies that title of the tab button
	 * @param onIconStateId
	 *            id of the on-state image
	 * @param offIconStateId
	 *            id of the off-state image
	 * @param intent
	 *            intent to start when button is tapped
	 */
	// protected void addTab(String title, int offIconStateId, int
	// onIconStateId,
	// Intent intent) {
	// int[] iconStates = { onIconStateId, offIconStateId };
	// states.add(iconStates);
	// intentList.add(intent);
	// titleList.add(title);
	// // commit();
	// }
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
		// commit();
	}

	// protected void addTab(String title, int iconStateId, int iconStateId_on,
	// Intent intent) {
	// int[] iconStates = { iconStateId, iconStateId_on };
	// states.add(iconStates);
	// intentList.add(intent);
	// titleList.add(title);
	// // commit();
	// }

	protected void addMyIntent(Intent intent) {
		myIntentList.add(intent);
	}

	/**
	 * <b><i>protected void addTab(String title, int iconStateId, int offShade,
	 * int onShade, Intent intent)</i></b>
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
	 * @param offShade
	 *            id for off-state color shades (e.g.
	 *            RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN
	 *            etc)
	 * @param onShade
	 *            id for on-state color shades (e.g.
	 *            RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN
	 *            etc)
	 * @param intent
	 *            intent to start when button is tapped
	 */
	protected void addTab(String title, int iconStateId, int offShade,
			int onShade, Intent intent) {
		int[] iconStates = { iconStateId, offShade, onShade };
		states.add(iconStates);
		intentList.add(intent);
		titleList.add(title);
		// commit();
	}

	/**
	 * <b><i>protected void setDefaultShde(int offShade, int onShade)</i></b>
	 * <p>
	 * <p>
	 * Sets the default on and off color shades of the bottom bar buttons
	 * <p>
	 * If not specified, the default off shade is gray, and the default on shade
	 * is yellow
	 * 
	 * @param offShade
	 *            id for off-state color shades (e.g.
	 *            RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN
	 *            etc)
	 * @param onShade
	 *            id for on-state color shades (e.g.
	 *            RadioStateDrawable.SHADE_GRAY, RadioStateDrawable.SHADE_GREEN
	 *            etc)
	 */
	protected void setDefaultShade(int offShade, int onShade) {
		defaultOffShade = offShade;
		defaultOnShade = onShade;
	}

	public void onCheckedChanged(RadioGroup group, int checkedId) {
		try {
			if (delegate != null)
				delegate.onTabChanged(checkedId);
		} catch (Exception e) {
		}
		// pv统计
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					jrtLot.setPara(5, jrtLot.channel_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		PublicMethod.myOutput("-----onCheckedChanged");

		startGroupActivity(titleList.get(checkedId).toString(),
				(Intent) intentList.get(checkedId));
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

	/*
	 * gets required R, not used
	 */
	public boolean inflateXMLForContentView() {
		/*
		 * setContentView(R.layout.customslidingtabhost); contentViewLayout =
		 * (LinearLayout)findViewById(R.id.contentViewLayout); bottomBar =
		 * (HorizontalScrollView)findViewById(R.id.bottomBar); bottomRadioGroup
		 * = (RadioGroup)findViewById(R.id.bottomMenu);
		 */
		return false;
	}

	public int bottomBar() {
		return -1;
	}

	/*
	 * delegates
	 */

	public void setDelegate(SliderBarActivityDelegate delegate_) {
		delegate = delegate_;
	}

	public static abstract class SliderBarActivityDelegate {

		/*
		 * Called when tab changed
		 */
		protected void onTabChanged(int tabIndex) {
		}
	}

	/*
	 * Broadcast receiver to set current tab
	 */
	// 2010.1.17
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.iv_hellor: {
	// if (!iLoginFlag) {
	// Intent in = new Intent(this, UserLogin.class);
	// // 0 login
	// startActivityForResult(in, 0);
	// }
	// break;
	// }
	// }
	// }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		switch (resultCode) {
		case RESULT_OK:
			// tv_right.setText(R.string.welcome);
			// 2010.1.17
			// iv_right.setImageResource(R.drawable.login);
			iLoginFlag = true;
		}
	}

	public class ChangeTabBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			PublicMethod.myOutput("-----+ChangeTabBroadcastReceiver");
			int index = intent.getExtras().getInt(CURRENT_TAB_INDEX);
			setCurrentTab(index);
		}
	}

	public class SuccessReceiver extends BroadcastReceiver {
		Context context;

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			this.context = context;
			
			showSuccessReceiver();
		}

		public void showSuccessReceiver() {
			// PublicMethod.myOutput("--------success----------");
			// 2010.1.17
			// iv_right.setImageResource(R.drawable.login);
			iLoginFlag = true;
		}
	}

	private String getLeftTip() {
		Date date = new Date();
		int iCurrentHour = date.getHours();
		if (iCurrentHour >= 23 || iCurrentHour <= 2) {
			return "午夜好！";
		} else if (iCurrentHour <= 5 && iCurrentHour > 2) {
			return "凌晨好！";
		} else if (iCurrentHour <= 10 && iCurrentHour > 5) {
			return "早上好！";
		} else if (iCurrentHour <= 14 && iCurrentHour > 10) {
			return "中午好！";
		} else if (iCurrentHour <= 17 && iCurrentHour > 14) {
			return "下午好！";
		} else {
			return "晚上好！";
		}
	}

	/*
	 * @Override public void onCancelClick() { // TODO Auto-generated method
	 * stub
	 * 
	 * }
	 * 
	 * @Override public void onOkClick(String name) { // TODO Auto-generated
	 * method stub this.finish(); }
	 * 
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { // TODO
	 * Auto-generated method stub //return super.onKeyDown(keyCode, event);
	 * //PublicVarAndFun.OutPutString("--->> you pressed key:" +
	 * String.valueOf(keyCode)); switch(keyCode){ case 4:{ String
	 * iStringInDialog="123"; ShowSmsDialog iSmsDialog=new
	 * ShowSmsDialog(ScrollableTabActivity
	 * .this,iStringInDialog,ScrollableTabActivity.this); iSmsDialog.show();
	 * return true; } default:{ super.onKeyDown(keyCode, event); return false;
	 * //break; } } }
	 */

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
}
