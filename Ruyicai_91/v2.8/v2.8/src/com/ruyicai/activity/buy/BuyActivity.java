/**
 * 
 */
package com.ruyicai.activity.buy;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.jc.Jc;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.pl5.PL5;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.Ssq;
import com.ruyicai.activity.buy.zc.FootballChooseNineLottery;
import com.ruyicai.activity.buy.zc.FootballGoalsLottery;
import com.ruyicai.activity.buy.zc.FootballSFLottery;
import com.ruyicai.activity.buy.zc.FootballSixSemiFinal;
import com.ruyicai.activity.join.JoinHallActivity;
import com.ruyicai.activity.more.HelpCenter;
import com.ruyicai.activity.more.LuckChoose;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.pojo.Lights;
import com.ruyicai.util.Constants;
import com.ruyicai.util.FlingGallery;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 购彩大厅界面
 * 
 * @author Administrator
 * 
 */
public class BuyActivity extends Activity implements OnClickListener {
	private String messageflage = null;
	String messagetitle;
	String messagedetail;
	String messageerrorcode;
	private String messageidflag = null;
	private JSONObject obj;
	private Dialog dialog;
	private int SCREENMAX = 9;// 屏幕最大图标数
	private int SCREENUM = 2;// 屏幕最大图标数
	private int SCREEALL = 0;// 屏幕总图标数
	private int WIDTH = 0;
	private int top = 20;
	private List<String> mLabelArray = new ArrayList<String>();
	private int[] imageViews = { R.drawable.ico_buy, R.drawable.ico_double,
			R.drawable.ico_3d, R.drawable.ico_115, R.drawable.ico_super,
			R.drawable.ico_timec, R.drawable.ico_seven, R.drawable.ico_three,
			R.drawable.icon_jc,  R.drawable.icon_pl5,
			R.drawable.icon_qxc,R.drawable.zc_sfc,R.drawable.zc_rx9,R.drawable.zc_jqc,R.drawable.zc_6cb };
	private String[] imageTitle = { "合买大厅", "双色球", "福彩3D", "11选5", "大乐透",
			"时时彩", "七乐彩", "排列三", "竞彩足球", "排列五", "七星彩","胜负彩","任选九","进球彩","六场半"};
	private final Class[] cla = { JoinHallActivity.class, Ssq.class,
			Fc3d.class, Dlc.class, Dlt.class, Ssc.class, Qlc.class, PL3.class,
			Jc.class,PL5.class, QXC.class,FootballSFLottery.class,FootballChooseNineLottery.class,FootballGoalsLottery.class,FootballSixSemiFinal.class };
	private int[] imgViewsId = { R.id.mainpage_hemai_sign,
			R.id.mainpage_ssq_sign, R.id.mainpage_fc3d_sign,
			R.id.mainpage_11x5_sign, R.id.mainpage_dlt_sign,
			R.id.mainpage_ssc_sign, R.id.mainpage_qlc_sign,
			R.id.mainpage_pl3_sign, R.id.mainpage_zucai_sign , R.id.mainpage_10_sign, R.id.mainpage_11_sign,R.id.mainpage_12_sign,R.id.mainpage_13_sign,R.id.mainpage_14_sign,R.id.mainpage_15_sign};
	private int[] textViewId = { R.id.mainpage_hemai_text,
			R.id.mainpage_ssq_text, R.id.mainpage_fc3d_text,
			R.id.mainpage_11x5_text, R.id.mainpage_dlt_text,
			R.id.mainpage_ssc_text, R.id.mainpage_qlc_text,
			R.id.mainpage_pl3_text, R.id.mainpage_zucai_text,
			R.id.mainpage_10_text, R.id.mainpage_11_text,
			R.id.mainpage_12_text, R.id.mainpage_13_text,
			R.id.mainpage_14_text, R.id.mainpage_15_text
			};
	private FlingGallery mGallery;
	private Lights lights;
	float startX = 0;
	float endX = 0;
	float newX = 0;
	float oldX = 0;
	float move = 0;
	boolean isMove = false;
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 4:
				showmessageDialog();
				break;
			}
		}

	};
	private LayoutInflater layoutinflater;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.buy_activity);
		mGallery = (FlingGallery) findViewById(R.id.buy_activity_fling_gallery);
		initNumber();
		initLights();
		initGallery();
		initImgView();
		initRollingText();
		dialogMsg();
	}

	/**
	 * 初始化数据
	 */
	public void initNumber() {
		SCREEALL = imageViews.length;
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		if (WIDTH == 240) {
			SCREENMAX = 6;
		} else if (WIDTH == 320) {
			SCREENMAX = 9;
			top = 5;
		} else if (WIDTH == 480) {
			SCREENMAX = 9;
			top = 25;
		} 
		else if (WIDTH > 480) {
			SCREENMAX = 12;
			top = 20;
		}

		int num = SCREEALL / SCREENMAX;
		if (num * SCREENMAX < SCREEALL) {
			num += 1;
		}
		for (int i = 0; i < num; i++) {
			mLabelArray.add("" + i);
		}
		SCREENUM = mLabelArray.size();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return mGallery.onGalleryTouchEvent(event);
	}

	// 活动提示框
	private void showmessageDialog() {
		ShellRWSharesPreferences shellcheck = new ShellRWSharesPreferences(
				BuyActivity.this, "UserMessage");
		View view = layoutinflater.from(BuyActivity.this).inflate(
				R.layout.tanchuxinxi, null);
		TextView msg = (TextView) view.findViewById(R.id.tanchuxinxi_msg);
		msg.setText(messagedetail);
		dialog = new AlertDialog.Builder(BuyActivity.this).setView(view)
				.setTitle(messagetitle).setNeutralButton("确定", null).show();
		dialog.getWindow().setLayout(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);

	}

	/**
	 * 初始化滑动
	 */
	public void initGallery() {
		mGallery.setIsGalleryCircular(false);
		mGallery.setPaddingWidth(10);
		mGallery.setLights(lights);
		mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
				android.R.layout.simple_list_item_1, mLabelArray) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				return new GalleryViewItem(getApplicationContext(), position);
			}
		});
	}

	public void initLights() {
		lights = new Lights(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.buy_activity_light_layout);
		int[] images = { R.drawable.buy_radio, R.drawable.buy_radio_b };
		lights.createViews(SCREENUM, images, layout);
		lights.isDefault(0);
	}

	/**
	 * 初始化imageView按钮
	 */
	public void initImgView() {
		int[] buttons = { R.id.mainpage_usercenter, R.id.mainpage_luck_sign,
				R.id.mainpage_help };
		for (int j = 0; j < 3; j++) {
			Button button = (Button) findViewById(buttons[j]);
			button.setBackgroundResource(R.drawable.join_info_btn_selecter);
			button.setOnClickListener(this);
		}
	}

	/**
	 * 初始化组件,滚动数据
	 */
	public void initRollingText() {
		ViewFlipper mFlipper = ((ViewFlipper) this
				.findViewById(R.id.notice_other_flipper));
		String str[] = splitStr(Constants.NEWS, 23);
		for (int i = 0; i < str.length; i++) {
			mFlipper.addView(addTextByText(str[i]));
		}
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		mFlipper.startFlipping();
	}

	/**
	 * 拆分滚动信息字符串 格式为逗号隔开
	 */
	public String[] splitStr(String str, int with) {
		String strss[] = str.split(",");
		if (strss.length == 0) {
			int indexs = str.length() / with + 1;
			String strs[] = new String[indexs];
			for (int i = 0; i < indexs; i++) {
				if (i == indexs - 1) {
					strs[i] = str.substring(i * with, str.length());
				} else {
					strs[i] = str.substring(i * with, with * (i + 1));
				}
			}
			return strs;
		}
		return strss;
	}

	public View addTextByText(String text) {
		TextView tv = new TextView(this);
		tv.setText(text);
		tv.setGravity(1);
		tv.setTextSize(15);
		tv.setTextColor(Color.BLACK);
		return tv;
	}

	protected void onStart() {
		super.onStart();
	}

	protected void onResume() {
		super.onResume();
		Constants.MEMUTYPE = 0;
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mainpage_usercenter:
			tendToUserCenter();
			break;
		case R.id.mainpage_luck_sign:
			tendToLuckCenter();
			break;
		case R.id.mainpage_help:
			tendToHelpCenter();
			break;
		}
	}

	public void tendToUserCenter() {
		Intent intent = new Intent(BuyActivity.this, NewUserCenter.class);
		startActivity(intent);
	}

	public void tendToHelpCenter() {
		Intent intent = new Intent(BuyActivity.this, HelpCenter.class);
		startActivity(intent);
	}

	public void tendToLuckCenter() {
		Intent intent = new Intent(BuyActivity.this, LuckChoose.class);
		startActivity(intent);
	}

	private void dialogMsg() {

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				BuyActivity.this, "UserMessage");
		messageflage = shellRW.getPreferencesValue("tanchumessage");
		messageidflag = shellRW.getPreferencesValue("id");
		if (!PublicConst.MESSAGE.equals("")) {
			try {
				obj = new JSONObject(PublicConst.MESSAGE);
				String id = obj.getString("id");
				messagetitle = obj.getString("title");
				messagedetail = obj.getString("message");
				if (messageidflag == null) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (!messageidflag.equals(id)) {
					shellRW.putPreferencesValue("id", id);
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				}

			} catch (JSONException e) {

			}
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
			ExitDialogFactory.createExitDialog(this);
			break;
		}
		return false;
	}

	private class GalleryViewItem extends TableLayout {

		public GalleryViewItem(Context context, int position) {
			super(context);
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = (LinearLayout) inflate.inflate(
					R.layout.buy_activity_btn1, null);
			initBtn(view, position);
			this.addView(view);
		}

		public void initBtn(View view, int position) {
			int length = SCREENMAX;
			if (position == mLabelArray.size() - 1) {
				length = SCREEALL - SCREENMAX * position;
			}
			LinearLayout top1 = (LinearLayout) view
					.findViewById(R.id.layout1_top);
			LinearLayout top2 = (LinearLayout) view
					.findViewById(R.id.layout2_top);
			LinearLayout top3 = (LinearLayout) view
					.findViewById(R.id.layout3_top);
			if (WIDTH == 480) {
				top1.setPadding(0, top, 0, 0);
			}
			top2.setPadding(0, top, 0, 0);
			top3.setPadding(0, top, 0, 0);
			for (int i = 0; i < length; i++) {
				final int index = i + SCREENMAX * position;
				ImageView imgView = (ImageView) view
						.findViewById(imgViewsId[i]);
				imgView.setVisibility(ImageView.VISIBLE);
				imgView.setImageResource(imageViews[index]);
				imgView.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent ev) {
						// TODO Auto-generated method stub

						final int action = ev.getAction();
						newX = ev.getX();
						switch (action) {
						case MotionEvent.ACTION_DOWN:
							endX = 0;
							isMove = false;
							startX = ev.getX();
							move = ev.getX();
							break;
						case MotionEvent.ACTION_UP:
							endX = ev.getX() + move;
							break;
						case MotionEvent.ACTION_MOVE:
							float deltaX = newX - startX;
							if (deltaX > 0) {// 向右移动
								// move = Math.max(newX, oldX);
								move += deltaX;
								ev.setLocation(move, ev.getY());
							} else if (deltaX < 0) {// 向左移动
								// move = Math.min(newX, oldX);
								move += deltaX;
								ev.setLocation(move, ev.getY());
							}
							if (Math.abs(deltaX) > 40) {
								isMove = true;
							}
							break;

						}
						if (action == MotionEvent.ACTION_UP) {
							float x = Math.abs(move - startX);
							if (x < 15 && !isMove) {
								Intent intent = new Intent(BuyActivity.this,
										cla[index]);
								startActivity(intent);
								return false;
							} else {
								return mGallery.onGalleryTouchEvent(ev);
							}
						} else {
							return mGallery.onGalleryTouchEvent(ev);
						}
					}
				});
				TextView textTitle = (TextView) view
						.findViewById(textViewId[i]);
				textTitle.setVisibility(TextView.VISIBLE);
				textTitle.setText(imageTitle[index]);
			}
		}
	}

}
