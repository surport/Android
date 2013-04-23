package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

public class BuyLotteryMainList extends Activity implements MyDialogListener,
		RadioGroup.OnCheckedChangeListener {
	private String messageidflag = null;
	private Dialog dialog;
	private String messageflage = null;
	private LayoutInflater layoutinflater;
	private Gallery gallery;
	private RadioGroup joinGroup;
	private String topButtonStringId[] = { "满员   ", "人气", "金额", "更多>>" };
	int topButtonIdOn[] = { R.drawable.manyuan2, R.drawable.renqi2,
			R.drawable.jiner2, R.drawable.genduo2 };
	int topButtonIdOff[] = { R.drawable.manyuan, R.drawable.renqi,
			R.drawable.jiner, R.drawable.genduo };
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private TextView joinTitle1;
	private TextView joinTitle2;
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String CONTENT = "CONTENT"; /* 标题 */
	// private String[] titles = new String[12];// 需要加载数据
	private static ArrayList<String[]> titles = new ArrayList<String[]>();// 需要加载数据
	private String title[];// 滚动字幕的值
	ProgressDialog progressdialog;
	public static boolean start = true;
	private int iretrytimes = 2;
	private JSONObject obj;
	String re;
	String title_0 = "";// 即将满员
	String title_1 = "";// 人气排行
	String title_2 = "";// 金额
	String messagetitle;
	String messagedetail;
	String messageerrorcode;
	String name;// 发起人
	String allNum;// 总份数
	int screen_width;
	int width;
	int index = 1;
	private PopupWindow popList;
	String sessionid;// 登录id
	ImageButton login;
	private IntentFilter loginSuccessFilter;
	private SuccessReceiver loginSuccessReceiver;
	private IntentFilter loginFailFilter;
	private FailReceiver loginFailReceiver;
	public static boolean flag = true;
	private int position = 3;
	private SeekBar g_seekbar;

	/* 联网 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				Toast.makeText(BuyLotteryMainList.this, "网络异常！",
						Toast.LENGTH_SHORT).show();
				break;
			case 1:
				progressdialog.dismiss();
				initList();
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(BuyLotteryMainList.this, "没有合买方案！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (index > 6) {
					index = 1;
				}
				gallery.setSelection(index);
				index++;
				break;
			case 4:
				showmessageDialog();
				break;
			}
		}

	};

	public void onCreate(Bundle savedInstanceState) {
	//pv统计 （红星后台）
//		jrtLot.pvNumber("主页", HomePage.channel_id, null, 
//				null, jrtLot.versionInfo);
		super.onCreate(savedInstanceState);
		// 是否显示活动提示框
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						BuyLotteryMainList.this, "UserMessage");
				messageflage = shellRW.getPreferencesValue("tanchumessage");
				messageidflag = shellRW.getPreferencesValue("id");
				String re = jrtLot.getMessage();
			try {

					obj = new JSONObject(re);
					messageerrorcode = obj.getString("errorCode");
					if (messageerrorcode.equals("A020500")) {

					} else if (messageerrorcode.equals("A020000")) {
						String id = obj.getString("id");
						messagetitle = obj.getString("title");
						messagedetail = obj.getString("detail");
						if (messageidflag == null) {
							shellRW.putPreferencesValue("id", id);
							Message msg = new Message();
							msg.what = 4;
							handler.sendMessage(msg);
						} else

						if (!messageidflag.equals(id)) {
							shellRW.putPreferencesValue("id", id);
							Message msg = new Message();
							msg.what = 4;
							handler.sendMessage(msg);

						} else if (messageflage == null) {
							Message msg = new Message();
							msg.what = 4;
							handler.sendMessage(msg);
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// TODO Auto-generated method stub

			}
		}).start();

	}

	// 活动提示框
	private void showmessageDialog() {
		ShellRWSharesPreferences shellcheck = new ShellRWSharesPreferences(
				BuyLotteryMainList.this, "UserMessage");
		View view = layoutinflater.from(BuyLotteryMainList.this).inflate(
				R.layout.tanchuxinxi, null);
		dialog = new AlertDialog.Builder(BuyLotteryMainList.this).setView(view)
				.setNeutralButton("确定", null).show();
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		TextView title = (TextView) view.findViewById(R.id.tanchuxinxi_txt1);
		TextView chankan = (TextView) view.findViewById(R.id.tanchuxinxi_txt2);
		title.setText(messagetitle);
		CheckBox chek = (CheckBox) view.findViewById(R.id.tanchuxinxi_checkbox);
		if (shellcheck.getPreferencesValue("ischeck") != null
				&& shellcheck.getPreferencesValue("ischeck").equals("true")) {
			chek.setChecked(true);
		}
		LinearLayout dialoglayout = (LinearLayout) view
				.findViewById(R.id.tanchuxinxi_layout);
		dialoglayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		chankan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				// Uri uri=Uri.parse("http://wap.baidu.com/");
				// Intent returnIt = new Intent(Intent.ACTION_VIEW, uri);
				// BuyLotteryMainList.this.startActivity(returnIt);
				dialog.dismiss();
				new AlertDialog.Builder(BuyLotteryMainList.this).setTitle(
						"详细信息").setMessage(messagedetail).setNegativeButton(
						"确定", null).show();

			}
		});
		chek.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						BuyLotteryMainList.this, "UserMessage");
				// TODO Auto-generated method stub
				if (isChecked) {

					shellRW.putPreferencesValue("tanchumessage", "1");
					shellRW.putPreferencesValue("ischeck", "true");
					messageflage = "1";
					// dialog.dismiss();
				} else {
					shellRW.putPreferencesValue("ischeck", "flase");
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
//				BuyLotteryMainList.this, "addInfo");
//		sessionid = shellRW.getUserLoginInfo("sessionid");
//		
//		
//		String phonenum = shellRW.getUserLoginInfo("phonenum");
//		if(sessionid.equals("")||phonenum.equals("")){
//			jrtLot.pvNumber( "5", HomePage.channel_id, null, 
//					null, jrtLot.versionInfo);
//		}else{
//			jrtLot.pvNumber( "5", HomePage.channel_id, sessionid, 
//					phonenum, jrtLot.versionInfo);
//		}

		setContentView(R.layout.notice_prizes_main_other);
		// 初始化组件
		init();
	}

	/**
	 * 初始化组件
	 */

	public void init() {
		// 初始化gallery
		g_seekbar = (SeekBar) findViewById(R.id.mainview_seekbar);
		gallery = (Gallery) findViewById(R.id.gallery_top);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setPressed(true);

		gallery.setSelection(position);
		g_seekbar.setMax(gallery.getCount() - 1);
		g_seekbar.setProgress(position);
		g_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				position = seekBar.getProgress();
				gallery.setSelection(position);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
		});

		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 返回postion的求余值
				int a = position % gallery.getCount();
				if (a == 0) {

					Intent intent1 = new Intent(BuyLotteryMainList.this,
							ssqtest.class);

					startActivity(intent1);
					// finish();

				} else if (a == 1) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							FC3DTest.class);

					startActivity(intent1);

				} else if (a == 2) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							QLC.class);

					startActivity(intent1);

				} else if (a == 3) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							PL3.class);

					startActivity(intent1);

				} else if (a == 4) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							DLT.class);

					startActivity(intent1);

				} else if (a == 5) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							Ssc.class);

					startActivity(intent1);

				} else if (a == 6) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							ShengFC.class);

					startActivity(intent1);
				} else if (a == 7) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							JinQC.class);

					startActivity(intent1);

				} else if (a == 8) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							LiuCB.class);

					startActivity(intent1);

				} else if (a == 9) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							RenX9.class);

					startActivity(intent1);

				}
			}

		});

		// goGallery();// gallery滚动
		// 初始化textView
		joinTitle1 = (TextView) findViewById(R.id.buyLotteryMainList_join_title_one);
		joinTitle1.setText(Html.fromHtml("<B><u>" + "合买大厅" + "</u></B>"));
		joinTitle1.setTextSize(20);
		joinTitle1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1 = new Intent(BuyLotteryMainList.this,
						JoinHall.class);
				startActivity(intent1);
			}
		});
		joinTitle2 = (TextView) findViewById(R.id.buyLotteryMainList_join_title_two);
		joinTitle2.setText("搜索:");
		// 初始化radioGroup
		joinGroup = (RadioGroup) findViewById(R.id.notice_radioGroup_join);
		joinGroup.setOnCheckedChangeListener(this);
		// screen_width = getWindowManager().getDefaultDisplay().getWidth();
		screen_width = 240;
		// screen_width = 320;
		width = screen_width / 4;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < topButtonStringId.length; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			// tabButton.setStateLabel(topButtonStringId[i], 15,
			// RadioStateDrawable.SHADE_BLACK,
			// RadioStateDrawable.SHADE_YELLOW);
			tabButton.setState(topButtonIdOn[i], topButtonIdOff[i]);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.LEFT);
			tabButton.setTextSize(10);
			joinGroup.addView(tabButton, i, topButtonLayoutParams);
		}
		// 初始化list
		initList();
		// 滚动字幕
		ViewFlipper mFlipper = ((ViewFlipper) this
				.findViewById(R.id.notice_other_flipper));

		String str[] = splitStr(getInfo(), 23);
		for (int i = 0; i < str.length; i++) {

			mFlipper.addView(addTextByText(str[i]));
		}
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		mFlipper.startFlipping();
		// imageButton按钮

		ImageButton menu;
		login = (ImageButton) findViewById(R.id.imageButton_login);
		menu = (ImageButton) findViewById(R.id.imageButton_menu);
		if (flag) {
			login.setBackgroundResource(R.drawable.shouye_login);
			login.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							UserLogin.class);
					startActivityForResult(intent1, 0);
				}
			});
		} else {
			login.setBackgroundResource(R.drawable.shouye_login_b);
		}

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!popList.isShowing()) {
					popList.showAtLocation(findViewById(R.id.imageButton_menu),
							Gravity.LEFT | Gravity.BOTTOM, 0, 33);
				} else {
					popList.dismiss();
				}

			}
		});
		// 下拉菜单
		LayoutInflater mLayoutInflater = (LayoutInflater) BuyLotteryMainList.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View iv = mLayoutInflater.inflate(R.layout.menu_list, null);
		ImageButton logn = (ImageButton) iv.findViewById(R.id.logon);

		logn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popList.dismiss();
				Intent intent1 = new Intent(BuyLotteryMainList.this,
						UserLogin.class);
				Bundle mBundle = new Bundle();
				mBundle.putBoolean("switch", true);// 压入数据
				intent1.putExtras(mBundle);
				startActivity(intent1);
			}
		});

		ImageButton exit = (ImageButton) iv.findViewById(R.id.exit);
		exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popList.dismiss();
				WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(
						BuyLotteryMainList.this, BuyLotteryMainList.this);
				iQuitDialog.show();
			}
		});
		popList = new PopupWindow(iv, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 合买查询
		if (start) {
			start = false;
			checkJoin(0);
		}
		// 添加广播接收器
		loginSuccessFilter = new IntentFilter(UserLogin.SUCCESS);
		loginSuccessReceiver = new SuccessReceiver();
		registerReceiver(loginSuccessReceiver, loginSuccessFilter);

		loginFailFilter = new IntentFilter(UserLogin.UNSUCCESS);
		loginFailReceiver = new FailReceiver();
		registerReceiver(loginFailReceiver, loginFailFilter);

	}

	/**
	 * 拆分字符串
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
		// tv.setTextColor(com.palmdream.RuyicaiAndroid.R.color.black);
		return tv;
	}

	public void initList() {
		// 初始化list
		// 数据源
		list = getListForJoinAdapter();

		ListView listview = (ListView) findViewById(R.id.buy_join_list);
		listview.setDividerHeight(0);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.buy_lotter_main_list, new String[] { TITLE, CONTENT },
				new int[] { R.id.buyLotterMianList_text,
						R.id.buyLotterMianList_text_two });

		listview.setAdapter(adapter);

		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < titles.size(); i++) {
					if (position == i) {
						Intent intent = new Intent(BuyLotteryMainList.this,
								JoinIn.class);
						Bundle mBundle = new Bundle();
						mBundle.putString("id", titles.get(i)[0]);// 压入数据
						mBundle.putString("user", titles.get(i)[2]);// 发起人
						mBundle.putString("allNum", titles.get(i)[3]);// 总份数
						mBundle.putString("type", "F47104");// 总份数
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				}

			}

		};
		listview.setOnItemClickListener(clickListener);
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkClick(int[] aNums) {
		// TODO Auto-generated method stub
		finish();

	}

	public class ImageAdapter extends BaseAdapter {

		private Context mContext;

		private Integer[] mImageIds = { R.drawable.shuangseqiu,
				R.drawable.fucai, R.drawable.qilecai, R.drawable.pailiesan,
				R.drawable.daletou, R.drawable.ssc, R.drawable.shengfucai,
				R.drawable.jinqiucai, R.drawable.liuchangban,
				R.drawable.renxuanjiu };

		public ImageAdapter(Context c) {
			TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);

			this.mContext = c;
		}

		@Override
		public int getCount() {
			return mImageIds.length;// 获取图片的个数
		}

		@Override
		public Object getItem(int position) {

			return position;// 获取图片在库中的位置
		}

		@Override
		public long getItemId(int position) {
			g_seekbar.setProgress(position);
			return position;// 获取图片在库中的位置
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView = new ImageView(mContext);
			if (position < 0) {
				position = position + mImageIds.length;
			}
			imageView.setImageResource(this.mImageIds[position]);
			imageView.setLayoutParams(new Gallery.LayoutParams(60, 60));

			// 设置图片大小
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置显示比例类型
			imageView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					Animation animation = AnimationUtils.loadAnimation(
							BuyLotteryMainList.this,
							R.anim.layout_animation_image);
					v.setAnimation(animation);
					return false;
				}
			});
			// imageView.setBackgroundResource(mGalleryItemBackground);
			return imageView;

		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int index = 0;
		switch (checkedId) {
		case 0:
			index = 0;// 即将满员
			break;
		case 1:
			index = 1;// 人气排行
			break;
		case 2:
			index = 2;// 方案金额
			break;
		case 3:// 更多
			index = 3;
			break;
		}
		Intent intent = new Intent(BuyLotteryMainList.this, JoinHall.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("index", index);
		intent.putExtras(mBundle);
		startActivity(intent);
	}

	public List<Map<String, Object>> getListForJoinAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		if (titles.size() != 0) {
			for (int i = 0; i < titles.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, titles.get(i)[1]);
				map.put(CONTENT, titles.get(i)[4]);
				list.add(map);
			}
		}
		return list;
	}

	public String getInfo() {

		String news = "欢迎使用如意彩";
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String re = shellRW.getUserLoginInfo("information0");
		if (re.equalsIgnoreCase("") || re == null) {
			// 获取失败 不改变news
		} else {
			try {
				JSONObject obj = new JSONObject(re);
				news = obj.getString("news");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return news;

	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}

	private void checkJoin(int type) {
		final String lotno_ssq = getLotno("information4");
		final String lotno_ddd = getLotno("information5");
		final String lotno_qlc = getLotno("information6");
		titles = new ArrayList<String[]>();
		title_0 = "";// 即将满员
		title_1 = "";// 人气排行
		title_2 = "";// 金额
		switch (type) {
		case 0:
			title_0 = "true";
			break;
		case 1:
			title_1 = "true";
			break;
		case 2:
			title_2 = "6";
			break;
		}

		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (iretrytimes < 3 && iretrytimes > 0) {

					re = jrtLot.queryJoin("F47104", title_2, "", title_0,
							title_1, "", "", lotno_ssq, "100");

					try {
						obj = new JSONObject(re);
						error_code = obj.getString("error_code");
						iretrytimes = 3;
					} catch (JSONException e) {

						try {
							JSONArray objArray = new JSONArray(re);
							for (int i = 0; i < objArray.length(); i++) {
								obj = objArray.getJSONObject(i);
								String[] strs = new String[5];
								strs[0] = (String) obj.get("id");
								try {
									Integer mm = (Integer) obj
											.get("caseAllAmt") / 100;

									// int amt = (int) Math.round((Double) obj
									// .get("caseBuyAfterAmt") * 100);
									float caseBuyAfterAmt = Float
											.parseFloat(obj
													.getString("caseBuyAfterAmt"));
									if (caseBuyAfterAmt * 100 < 100
											&& caseBuyAfterAmt * 100 + 1 < 100) {// 0.001
										caseBuyAfterAmt = caseBuyAfterAmt * 1000 % 10 > 0 ? caseBuyAfterAmt * 100 + 1
												: caseBuyAfterAmt * 100;
									} else {
										caseBuyAfterAmt = caseBuyAfterAmt * 100;
									}

									name = (String) obj.get("userNo");
									allNum = Integer.toString((Integer) obj
											.get("caseAllNum"));
									String str = name;
									String str1 = "￥"
											+ Integer.toString(mm)
											+ "+"
											+ Integer
													.toString((int) caseBuyAfterAmt)
											+ "%";
									strs[1] = str;
									strs[2] = name;
									strs[3] = allNum;
									strs[4] = str1;
									titles.add(strs);
								} catch (Exception ex) {
									Integer mm = (Integer) obj
											.get("caseAllAmt") / 100;

									Integer amt = (Integer) obj
											.get("caseBuyAfterAmt") * 100;
									name = (String) obj.get("userNo");
									allNum = Integer.toString((Integer) obj
											.get("caseAllNum"));
									String str = name;
									String str1 = "￥" + Integer.toString(mm)
											+ "+" + Integer.toString(amt) + "%";
									strs[1] = str;
									strs[2] = name;
									strs[3] = allNum;
									strs[4] = str1;
									titles.add(strs);
								}
							}

							iretrytimes = 3;
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							iretrytimes--;
						}
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
							re = jrtLot.queryJoin("F47104", title_2, "",
									title_0, title_1, "", "", lotno_ssq, "100");
							try {
								obj = new JSONObject(re);
								error_code = obj.getString("error_code");
								iretrytimes = 3;
							} catch (JSONException e) {

								try {
									JSONArray objArray = new JSONArray(re);
									for (int i = 0; i < objArray.length(); i++) {
										obj = objArray.getJSONObject(i);
										String[] strs = new String[5];
										strs[0] = (String) obj.get("id");
										try {
											Integer mm = (Integer) obj
													.get("caseAllAmt") / 100;

											// int amt = (int) Math
											// .round((Double) obj
											// .get("caseBuyAfterAmt") * 100);
											float caseBuyAfterAmt = Float
													.parseFloat(obj
															.getString("caseBuyAfterAmt"));
											if (caseBuyAfterAmt * 100 < 100
													&& caseBuyAfterAmt * 100 + 1 < 100) {// 0.001
												caseBuyAfterAmt = caseBuyAfterAmt * 1000 % 10 > 0 ? caseBuyAfterAmt * 100 + 1
														: caseBuyAfterAmt * 100;
											} else {
												caseBuyAfterAmt = caseBuyAfterAmt * 100;
											}
											name = (String) obj.get("userNo");
											allNum = Integer
													.toString((Integer) obj
															.get("caseAllNum"));
											String str = name;
											String str1 = "￥"
													+ Integer.toString(mm)
													+ "+"
													+ Integer
															.toString((int) caseBuyAfterAmt)
													+ "%";
											strs[1] = str;
											strs[2] = name;
											strs[3] = allNum;
											strs[4] = str1;
											titles.add(strs);

										} catch (Exception ex) {
											Integer mm = (Integer) obj
													.get("caseAllAmt") / 100;

											Integer amt = (Integer) obj
													.get("caseBuyAfterAmt") * 100;
											String str = (String) obj
													.get("userNo");
											String str1 = "￥"
													+ Integer.toString(mm)
													+ "+"
													+ Integer.toString(amt)
													+ "%";
											strs[1] = str;
											strs[2] = name;
											strs[3] = allNum;
											strs[4] = str1;
											titles.add(strs);
										}
									}
									iretrytimes = 3;
								} catch (JSONException e1) {
									// TODO Auto-generated catch block
									iretrytimes--;
									error_code = "000";
								}
							}
						}
					}
				}
				iretrytimes = 2;
				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);

				} else if (error_code.equals("400001")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("000")) {
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}

			}

		}).start();
	}

	/**
	 * 获得当前期数
	 * 
	 * @param string
	 * @return
	 */
	public String getLotno(String string) {
		String error_code;
		String batchcode = "";
		// ShellRWSharesPreferences
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String notice = shellRW.getUserLoginInfo(string);
		PublicMethod.myOutput("------------------lotnossq" + notice);
		// 判断取值是否为空 cc 2010/7/9
		if (!notice.equals("") || notice != null) {
			try {
				JSONObject obj = new JSONObject(notice);
				error_code = obj.getString("error_code");
				if (error_code.equals("0000")) {
					batchcode = obj.getString("batchCode");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return batchcode;
	}

	// @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		popList.dismiss();
		start = true;
	}

	/**
	 * Gallery的滚动
	 */
	private void goGallery() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				while (true) {
					try {
						Thread.sleep(6000);
						Message msg = new Message();
						msg.what = 3;
						handler.sendMessage(msg);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	/**
	 * 退出检测
	 * 
	 * @param keyCode
	 *            返回按键的号码
	 * @param event
	 *            事件
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>NoticePrizesOfLottery key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			break;
		}
			// 周黎鸣 7.8 代码修改：添加新的判断
		case 0x12345678: {
			popList.dismiss();
			WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(this, this);
			iQuitDialog.show();
			break;
		}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	/**
	 * 已登录的广播
	 * 
	 * @author Administrator
	 * 
	 */
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
			// login.setBackgroundResource(R.drawable.shouye_login_b);
			flag = false;

		}
	}

	/**
	 * 未登录的广播
	 * 
	 * @author Administrator
	 * 
	 */
	public class FailReceiver extends BroadcastReceiver {
		Context context;

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			this.context = context;
			showSuccessReceiver();
		}

		public void showSuccessReceiver() {
			// PublicMethod.myOutput("--------success----------");

			// login.setBackgroundResource(R.drawable.shouye_login_b);
			flag = true;

		}
	}

}