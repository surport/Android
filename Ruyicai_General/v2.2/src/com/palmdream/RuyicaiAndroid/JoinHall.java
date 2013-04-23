package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.BallBetPublicClass.BallHolder;
import com.palmdream.RuyicaiAndroid.BuyLotteryMainList.ImageAdapter;
import com.palmdream.RuyicaiAndroid.BuyLotteryMainList.SuccessReceiver;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 合买大厅
 */
public class JoinHall extends Activity implements
		RadioGroup.OnCheckedChangeListener, MyDialogListener {
	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String CONTENT = "CONTENT"; /* 标题 */
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private ArrayList<String[]> titles = new ArrayList();// 需要加载数据
	private RadioGroup joinGroup;
	private String topButtonStringId[] = { "双色球", "福彩3D", "七乐彩" };
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private int screen_width;
	private int width;
	private String[] type = { "双色球", "福彩3D", "七乐彩" };// 需要加载数据
	int topButtonIdOn[] = { R.drawable.shuangseqiu01, R.drawable.fucai_3,
			R.drawable.qilecai01 };
	int topButtonIdOff[] = { R.drawable.shuangseqiu02, R.drawable.fucai_3_b,
			R.drawable.qilecai02 };
	private String typeLabel = "F47104";// 彩种标识默认双色球
	ProgressDialog progressdialog;
	private boolean start;
	public static boolean once = true;
	private int index;
	private static int indexGroup;
	private PopupWindow popList;
	ImageButton login;
	private IntentFilter loginSuccessFilter;
	private SuccessReceiver loginSuccessReceiver;
	String sessionid;// 登录id
	int typeQuery;
	// showDialog(0);//显示进度条
	/* 联网 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				break;
			case 1:
				progressdialog.dismiss();
				initList();
				break;
			case 2:
				progressdialog.dismiss();
				initList();
				Toast.makeText(JoinHall.this, "没有合买方案！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}

	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.join_hall);

	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		popList.dismiss();
		once = true;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		lotno_ssq = getLotno("information4");
		lotno_ddd = getLotno("information5");
		lotno_qlc = getLotno("information6");
		// 读取数据
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			index = bundle.getInt("index");
		}
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.unregisterReceiver(loginSuccessReceiver);
	}

	// 初始化组件
	public void init() {
		// 初始化顶部组件
		ImageView back = (ImageView) findViewById(R.id.join_hall_back);
		TextView title = (TextView) findViewById(R.id.join_hall_title_top);
		ImageView startJoin = (ImageView) findViewById(R.id.join_hall_start);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		startJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkTypeDialog();
			}
		});
		title.setText("合买大厅");
		// 初始化radioGroup
		joinGroup = (RadioGroup) findViewById(R.id.join_hall_radio_group);
		joinGroup.setOnCheckedChangeListener(this);
		screen_width = getWindowManager().getDefaultDisplay().getWidth();
		// screen_width = 300;
		width = screen_width / 3 + 15;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < topButtonStringId.length; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			// tabButton.setState(topButtonStringId[i]);
			tabButton.setState(topButtonIdOff[i], topButtonIdOn[i]);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			tabButton.setTextSize(10);
			joinGroup.addView(tabButton, i, topButtonLayoutParams);
		}
		// if (once) {
		// once = false;
		joinGroup.check(indexGroup);
		// }
		// 初始化spinner
		final Spinner joinType = (Spinner) findViewById(R.id.join_hall_spinner);
		switch (index) {
		case 0:
			joinType.setSelection(2);
			break;
		case 1:
			joinType.setSelection(3);
			break;
		case 2:
			joinType.setSelection(4);
			break;
		case 3:
			joinType.setSelection(0);
			break;
		}

		joinType.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = joinType.getSelectedItemPosition();
				if (position == 0) {// 合买高手
					flag = 3;
				} else if (position == 1) {// 最新合买
					flag = 4;
				} else if (position == 2) {// 即将满员
					flag = 0;
				} else if (position == 3) {// 人气排行
					flag = 1;
				} else if (position == 4) {// 方案金额
					flag = 2;
				}
				checkJoin(flag);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		// 初始化list
		initList();
		// 滚动字幕
		ViewFlipper mFlipper = ((ViewFlipper) this
				.findViewById(R.id.join_hall_flipper));
		TextView mtext = (TextView) findViewById(R.id.join_hall_title);
		mtext.setText(getInfo());
		mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_in));
		mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_up_out));
		mFlipper.startFlipping();

		ImageButton join;
		ImageButton buy;
		join = (ImageButton) findViewById(R.id.query_join);
		buy = (ImageButton) findViewById(R.id.query_buy);
		join.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				typeQuery = QueryInfo.JOIN;
				turnQuery(QueryInfo.JOIN);
			}
		});
		buy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				typeQuery = QueryInfo.BUY;
				turnQuery(QueryInfo.BUY);
			}
		});
		// 下拉菜单
		LayoutInflater mLayoutInflater = (LayoutInflater) JoinHall.this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View iv = mLayoutInflater.inflate(R.layout.menu_list, null);
		ImageButton logn = (ImageButton) iv.findViewById(R.id.logon);
		logn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popList.dismiss();
				Intent intent1 = new Intent(JoinHall.this, UserLogin.class);
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
						JoinHall.this, JoinHall.this);
				iQuitDialog.show();
			}
		});
		popList = new PopupWindow(iv, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		// 添加广播接收器
		loginSuccessFilter = new IntentFilter(
				ScrollableTabActivity.ACTION_LOGIN_SUCCESS);
		loginSuccessReceiver = new SuccessReceiver();
		registerReceiver(loginSuccessReceiver, loginSuccessFilter);
	}

	// 网络连接框
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

	public void turnQuery(int type) {
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				JoinHall.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		if (!sessionid.equals("")) {
			Intent intent = new Intent(JoinHall.this, QueryInfo.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt("type", type);// 压入数据
			intent.putExtras(mBundle);
			startActivity(intent);
		} else {
			Intent intent = new Intent(JoinHall.this, UserLogin.class);
			startActivityForResult(intent, 0);
		}
	}

	// 初始化list
	public void initList() {

		// 数据源
		list = getListForJoinAdapter();

		ListView listview = (ListView) findViewById(R.id.join_hall_list);
		listview.setDividerHeight(0);

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.join_hall_list_item, new String[] { TITLE, CONTENT },
				new int[] { R.id.buyLotterMianList_text,
						R.id.buyLotterMianList_text_two });

		listview.setAdapter(adapter);

		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				for (int i = 0; i < titles.size(); i++) {
					if (position == i) {
						Intent intent = new Intent(JoinHall.this, JoinIn.class);
						Bundle mBundle = new Bundle();
						mBundle.putString("id", titles.get(i)[0]);// 压入数据
						mBundle.putString("user", titles.get(i)[2]);// 发起人
						mBundle.putString("allNum", titles.get(i)[3]);// 总份数
						mBundle.putString("type", typeLabel);// 彩种
						intent.putExtras(mBundle);
						startActivity(intent);
					}
				}

			}

		};
		listview.setOnItemClickListener(clickListener);
	}

	// 初始化对话框里的list
	public void initDialogList(ListView listview) {

		// 数据源
		listview.setDividerHeight(0);

		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, type);
		listview.setAdapter(adapter);

		listview.setItemsCanFocus(false);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

	}

	public List<Map<String, Object>> getListForJoinAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);

		for (int i = 0; i < titles.size(); i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles.get(i)[1]);
			map.put(CONTENT, titles.get(i)[4]);
			list.add(map);
		}

		return list;
	}

	// 获取滚动信息
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

	int type1 = 3;

	// 选择彩种对话框
	public void checkTypeDialog() {
		start = true;

		LayoutInflater inflater = LayoutInflater.from(this);
		View failView = inflater.inflate(R.layout.join_hall_check_dialog, null);
		ListView listView = (ListView) failView
				.findViewById(R.id.join_hall_check_dialog_list);
		// initDialogList(listView);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, type);
		listView.setAdapter(adapter);

		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					type1 = 0;
				} else if (position == 1) {
					type1 = 1;
				} else if (position == 2) {
					type1 = 2;
				}

			}

		};
		listView.setOnItemClickListener(clickListener);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle(R.string.join_hall_check_dialog_title);
		builder.setView(failView);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// WinningVector.removeAllElements();
						Intent intent;
						if (start) {
							start = false;
							switch (type1) {
							case 0:
								intent = new Intent(JoinHall.this,
										ssqtestJoin.class);
								startActivity(intent);
								break;
							case 1:
								intent = new Intent(JoinHall.this,
										FC3DTestJoin.class);
								startActivity(intent);
								break;
							case 2:
								intent = new Intent(JoinHall.this,
										QLCJoin.class);
								startActivity(intent);
								break;
							}
						}
					}

				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// WinningVector.removeAllElements();
					}

				});
		builder.show();
	}

	String title_0 = "";// 即将满员
	String title_1 = "";// 人气排行
	String title_2 = "";// 金额
	String title_3 = "";// 合买高手
	String title_4 = "";// 最新合买
	private int iretrytimes = 2;
	private JSONObject obj;
	String name;// 发起人
	String allNum;// 总份数
	String re;
	private int flag = 0;
	private String lotno;
	String lotno_ssq;
	String lotno_ddd;
	String lotno_qlc;

	// 合买查询
	private void checkJoin(int type) {

		title_0 = "";// 即将满员
		title_1 = "";// 人气排行
		title_2 = "";// 金额
		title_3 = "";// 合买高手
		title_4 = "";// 最新合买
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
		case 3:
			title_3 = "true";
		case 4:
			title_4 = "";
			break;
		}

		showDialog(0);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String error_code = "00";
				if (once) {
					once = false;
					while (iretrytimes < 3 && iretrytimes > 0) {
						titles = new ArrayList<String[]>();
						re = jrtLot.queryJoin(typeLabel, title_2, "", title_0,
								title_1, title_3, title_4, lotno, "100");

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
										String str1 = "￥"
												+ Integer.toString(mm) + "+"
												+ Integer.toString(amt) + "%";
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
								re = jrtLot.queryJoin(typeLabel, title_2, "",
										title_0, title_1, title_3, title_4,
										lotno, "100");
								try {
									obj = new JSONObject(re);
									error_code = obj.getString("error_code");
									iretrytimes = 3;
								} catch (JSONException e) {

									try {
										JSONArray objArray = new JSONArray(re);
										for (int i = 0; i < objArray.length(); i++) {
											String[] strs = new String[5];
											strs[0] = (String) obj.get("id");

											try {
												Integer mm = (Integer) obj
														.get("caseAllAmt") / 100;

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
												name = (String) obj
														.get("userNo");
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
												Log.e("titles[i][1].add(str)",
														"" + titles.get(i));
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
												Log.e("titles[i][1].add(str)",
														"" + titles.get(i));
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
					once = true;
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

			}

		}).start();
	}

	// 获得当前期数
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

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case 0:
			typeLabel = "F47104";// F47104、F02904代表双色球
			lotno = lotno_ssq;
			indexGroup = 0;
			checkJoin(flag);
			break;
		case 1:
			typeLabel = "F47103";// F47103、F02903代表3D
			lotno = lotno_ddd;
			indexGroup = 1;
			checkJoin(flag);
			break;
		case 2:
			typeLabel = "F47102";// F47102、F02902代表七乐彩
			lotno = lotno_qlc;
			indexGroup = 2;
			checkJoin(flag);
			break;
		}
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkClick(int[] aNums) {
		// TODO Auto-generated method stub
		// finish();
		// System.exit(0);
		// android.os.Process.killProcess(android.os.Process.myPid());
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		am.restartPackage(getPackageName());
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

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			// PublicMethod.myOutput("-------iType----" + iType);
			// if (iType == 0) {
			turnQuery(typeQuery);
			// }
			break;
		default:
			Toast.makeText(JoinHall.this, "未登录成功！", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
