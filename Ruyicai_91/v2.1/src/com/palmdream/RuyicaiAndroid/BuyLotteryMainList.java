package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.ScrollableTabActivity.SuccessReceiver;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class BuyLotteryMainList extends Activity implements MyDialogListener,
		RadioGroup.OnCheckedChangeListener {

	private Gallery gallery;
	private RadioGroup joinGroup;
	private String topButtonStringId[] = { "满员", "人气", "金额", "更多>>" };
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
	String name;// 发起人
	String allNum;// 总份数
	int screen_width;
	int width;
	int index = 2;
	private PopupWindow popList;
	String sessionid;// 登录id
	ImageButton login;
	private IntentFilter loginSuccessFilter;
	private SuccessReceiver loginSuccessReceiver;
	private IntentFilter loginFailFilter;
	private FailReceiver loginFailReceiver;
	public static boolean flag = true;
	// showDialog(0);//显示进度条
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
			}
		}

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.notice_prizes_main_other);
		// // 初始化组件
		// init();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
		// BuyLotteryMainList.this, "addInfo");
		// sessionid = shellRW.getUserLoginInfo("sessionid");
		//
		// setContentView(R.layout.notice_prizes_main_other);
		// // 初始化组件
		// init();
	}

	// @Override
	// public void onConfigurationChanged(Configuration newConfig) {
	// // TODO Auto-generated method stub
	// super.onConfigurationChanged(newConfig);
	// Log.v("=================", "onConfigurationChanged");
	// if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_LANDSCAPE) {
	//
	// //加入横屏要处理的代码
	//
	// }else if (this.getResources().getConfiguration().orientation ==
	// Configuration.ORIENTATION_PORTRAIT) {
	//
	// //加入竖屏要处理的代码
	//
	// }
	// }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				BuyLotteryMainList.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");

		setContentView(R.layout.notice_prizes_main_other);
		// 初始化组件
		init();
	}

	/**
	 * 初始化组件
	 */
	public void init() {
		// 初始化gallery
		gallery = (Gallery) findViewById(R.id.gallery_top);
		gallery.setAdapter(new ImageAdapter(this));
		gallery.setSelection(index);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				int a = position % 5;
				if (a == 0) {
					Intent intent1 = new Intent(BuyLotteryMainList.this,
							ssqtest.class);
					startActivity(intent1);
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
				}
			}

		});
		// goGallery();// gallery滚动
		// 初始化textView
		joinTitle1 = (TextView) findViewById(R.id.buyLotteryMainList_join_title_one);
		joinTitle1.setText("热门合买:");
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
			tabButton.setStateLabel(topButtonStringId[i], 15,
					RadioStateDrawable.SHADE_BLACK,
					RadioStateDrawable.SHADE_YELLOW);
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
		// TextView mtext = (TextView) findViewById(R.id.notice_other_title);
		// mtext.setText(getInfo());
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
				// WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(
				// BuyLotteryMainList.this, BuyLotteryMainList.this);
				// iQuitDialog.show();
				if (!popList.isShowing()) {
					popList.showAtLocation(findViewById(R.id.imageButton_menu),
							Gravity.RIGHT | Gravity.BOTTOM, 0, 33);
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
				R.drawable.daletou };

		public ImageAdapter(Context c) {
			this.mContext = c;
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;// 获取图片的数
		}

		@Override
		public Object getItem(int position) {
			return position;// 获取图片在库中的位置
		}

		@Override
		public long getItemId(int position) {
			return position;// 获取图片在库中的位置
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(mContext);
			if (position < 0) {
				position = position + mImageIds.length;
			}

			imageView.setImageResource(this.mImageIds[position
					% mImageIds.length]);

			imageView.setLayoutParams(new Gallery.LayoutParams(60, 60));//
			// 设置图片大小
			imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);// 设置显示比例类型
			return imageView;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int index = 0;
		switch (checkedId) {
		case 0:
			// checkJoin(0);
			index = 0;// 即将满员
			break;
		case 1:
			// checkJoin(1);
			index = 1;// 人气排行
			break;
		case 2:
			// checkJoin(2);
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
				map.put(CONTENT, titles.get(i)[4] + "%");
				list.add(map);
			}
		}
		return list;
	}

	public String getInfo() {
		// 20100712 delete fyj
		// TnoticeInterface tnotice=new TnoticeInterface();
		// String news=tnotice.Tnotice();
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
		// String[] iPmdText={news,"test2"};
		return news;
		// String[] iPmdText={"test1","test2"};
		// timer.schedule(task, iTimeInterval,iTimeInterval);
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			// progressdialog.setCancelable(true);
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
		iHttp.conMethord = iHttp.CMNET;// 默认为cmnet链接
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
													.toString((int) caseBuyAfterAmt);

									strs[1] = str;
									strs[2] = name;
									strs[3] = allNum;
									strs[4] = str1;
									// Log.e("====", strs[0] + "===" + allNum);
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
											+ "+" + Integer.toString(amt);
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
															.toString((int) caseBuyAfterAmt);
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
													+ Integer.toString(amt);
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
	 * 已经登录的广播
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
			Log.e("======", "+++++++fail");

		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// switch (resultCode) {
	// case RESULT_OK:
	// Log.e("-----++=", "success");
	// // tv_right.setText(R.string.welcome);
	// login.setImageResource(R.drawable.shouye_login_b);
	// // iLoginFlag = true;
	// }
	// }
}