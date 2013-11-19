package com.ruyicai.activity.buy.high;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.activity.buy.cq11x5.Cq11Xuan5;
import com.ruyicai.activity.buy.cq11x5.HistoryNumberView;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.activity.buy.miss.MainViewPagerAdapter;
import com.ruyicai.activity.buy.miss.NumViewItem;
import com.ruyicai.activity.buy.miss.ZHmissViewItem;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.buy.zixuan.JiXuanBtn;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.code.ssc.OneStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.net.newtransaction.MissInterface;
import com.ruyicai.net.newtransaction.NMK3MissInterface;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;

public abstract class ZixuanAndJiXuan extends BaseActivity implements
		OnCheckedChangeListener, OnClickListener,
		SeekBar.OnSeekBarChangeListener, HandlerMsg {
	protected int BallResId[] = { R.drawable.grey, R.drawable.red };
	protected LayoutInflater inflater;
	protected LinearLayout buyview;
	protected BallTable BallTable;
	protected RadioGroup group;
	protected CodeInterface sscCode = new OneStarCode();
	/** 单选按钮文本 */
	protected String[] childtype = null;
	protected boolean isbigsmall = false;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	public BetAndGiftPojo betAndGift = new BetAndGiftPojo();// 投注信息类
	MyHandler handler = new MyHandler(this);// 自定义handler
	public String phonenum, sessionId, userno;
	ProgressDialog progressdialog;
	public String codeStr;
	public String lotno, sellWay = MissConstant.SSC_5X_ZX;
	public String highttype;
	public int type;
	public final static int NULL = 0;
	public final static int ONE = 1;
	public final static int TWO = 2;
	public final static int THREE = 3;
	public final static int FIVE = 5;
	public final static int TWO_ZUXUAN = 6;
	public final static int TWO_HEZHI = 7;
	public final static int FIVE_TONGXUAN = 8;
	public final static int BIG_SMALL = 9;
	public final static String PAGE = "1";
	public final static String MAX = "5";
	public final static int TIME = 5 * 60000;// 获取期号线程刷新时间单位是分
	private final static String ERROR_WIN = "0000";

	public final static int NMK3_HEZHI = 10;//和值
	public final static int NMK3_THREESAME = 18;//三同通选
	public final static int NMK3_THREESAME_TONG = 11;//三同通选
	public final static int NMK3_THREESAME_DAN = 12;//三同单选
	public final static int NMK3_TWOSAME_FU = 13;//二同复选
	public final static int NMK3_TWOSAME_DAN = 14;//二同单选
	public final static int NMK3_DIFF_THREE = 15;//三不同
	public final static int NMK3_DIFF_TWO = 16;//二不同
	public final static int NMK3_THREE_LINK = 17;//三连号

	int iZhuShu;
	int zhushuforshouyi;
	Dialog touZhuDialog;
	TextView issueText;
	TextView alertText;
	TextView textZhuma;
	public static String lotnoStr;
	public static boolean isTime = true;
	public static boolean isStart = true;
	public static JSONArray prizeInfos = null;
	public boolean isViewEnd = true;
	public boolean isViewStart = true;
	public static long startTime;
	long endTime;
	/** 号码篮对象 */
	public AddView addView;
	private boolean isJiXuan = false;
	protected boolean isTen = true;
	protected int MAX_ZHU = 10000;// 每笔最多为1万注
	private final int All_ZHU = 99;
	private Context context;
	public int radioId = 0;
	public boolean isMiss = true;// 是否进行遗漏值查询
	public boolean isshouyi = false;
	public int hightballs;
	/** 是否滑动页面显示遗漏值 */
	protected boolean isMove = false;
	public Map<Integer, HighItemView> missView = new HashMap<Integer, HighItemView>();
	float startX;
	float startY;
	protected LinearLayout childtypes;
	protected View layoutMain;
	public ScrollView scrollView;
	
	protected LinearLayout zixuanLayout;
	
	protected void setAddView(AddView addView) {
		this.addView = addView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		setContentView(R.layout.sscbuyview);
		Constants.type = "hight";
		childtype = new String[] { "直选", "机选" };

	}

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public abstract String textSumMoney(AreaNum areaNum[], int iProgressBeishu);

	/**
	 * 判断是否满足投注条件
	 */
	public abstract String isTouzhu();

	/**
	 * 返回注数
	 */
	public abstract int getZhuShu();

	/**
	 * 返回投注提示框提示信息
	 */
	public abstract String getZhuma();

	/**
	 * 返回机选投注注码
	 */
	public abstract String getZhuma(Balls ball);

	/**
	 * 投注联网信息
	 */
	public abstract void touzhuNet();

	/**
	 * 单选按钮响应事件
	 * 
	 * @param checkedId
	 */
	public abstract void onCheckAction(int checkedId);

	/**
	 * 获取相关的布局对象，并初始化单选按钮
	 */
	public void init() {
		childtypes = (LinearLayout) findViewById(R.id.sscchildtype);
		childtypes.setVisibility(View.VISIBLE);
		childtypes.removeAllViews();
		buyview = (LinearLayout) findViewById(R.id.buyview);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		group = new RadioGroup(this);
		group.setOrientation(RadioGroup.HORIZONTAL);
		// 设置屏幕宽度
		for (int i = 0; i < childtype.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(childtype[i]);
			if(lotno ==Constants.LOTNO_NMK3){
				radio.setTextColor(Color.WHITE);
			}else {
				radio.setTextColor(Color.BLACK);
			}
			radio.setId(i);
			radio.setTextSize(14);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 15, 0);
			group.addView(radio);
			group.setOnCheckedChangeListener(this);
		}
		group.check(0);
		// 将单选按钮组添加到布局中
		childtypes.addView(group);

	}

	/**
	 * 初始化选区
	 */
	public AreaNum[] initArea() {
		areaNums = new AreaNum[1];
		String title = "请选择投注号码";
		areaNums[0] = new AreaNum(10, 10, 1, 11, BallResId, 0, 0, Color.RED,
				title, false, true);
		return areaNums;
	}

	// 单选框切换直选，机选
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		radioId = checkedId;
		switch (checkedId) {
		case 0:
			iProgressBeishu = 1;
			iProgressQishu = 1;
			initArea();
			createView(areaNums, sscCode, type, false, checkedId, true);
			BallTable = areaNums[0].table;
			break;
		case 1:
			iProgressBeishu = 1;
			iProgressQishu = 1;
			SscBalls onestar = new SscBalls(1);
			createviewmechine(onestar, checkedId);
			break;
		}

	}

	private TextView mTextSumMoney;
	protected TextView textTitle;
	protected TextView textPrize;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	private EditText mTextBeishu, mTextQishu;
	public int iScreenWidth;
	protected CodeInterface code;// 注码接口类
	protected View view;
	public Toast toast;
	private boolean toLogin = false;
	protected int lineNum = 3;// 组合遗漏每行按钮数
	protected int textSize = 1;

	protected ViewPager mGallery;
	// 缓存需要左右滑动的视图群的列表容器
	public List<BuyViewItemMiss> itemViewArray;
	protected ListView latestLotteryList;

	protected Button historyBtn;
	protected boolean historyFlag=false;
	protected LinearLayout listView;
	protected HistoryNumberView simulateSelectNumberView;


	/**
	 * 创建可滑动直选页面
	 * 
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 *            id 当前view的id
	 */
	public void createViewNew(AreaNum areaNum[], CodeInterface code, int type,
			boolean isTen, int id) {
		sensor.stopAction();
		isJiXuan = false;
		isMove = true;
		setNewPosition(0);
		this.code = code;
		buyview.removeAllViews();

		if (missView.get(id) == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View zhixuanview = inflater.inflate(R.layout.ssczhixuan_new, null);
			initZixuanView(zhixuanview);
			initViewItemNew(areaNum, isTen, zhixuanview);
			initBotm(zhixuanview);
			missView.put(id, new HighItemView(zhixuanview, areaNum, addView,
					itemViewArray, editZhuma));
			missView.get(id).setmGallery(mGallery);
			refreshView(type, id);
		} else {
			mGallery.setCurrentItem(0);
			refreshView(type, id);
		}

	}

	/**
	 * 创建不可滑动直选页面
	 * 
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 */
	public void createView(final AreaNum areaNum[], CodeInterface code, int type,
			boolean isTen, int id, boolean isMiss) {
		sensor.stopAction();
		isJiXuan = false;
		isMove = false;
		this.code = code;
		buyview.removeAllViews();
		if (missView.get(id) == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
			latestLotteryList = (ListView) zhixuanview
					.findViewById(R.id.buy_zixuan_latest_lottery);
			initZixuanView(zhixuanview);
			initViewItem(areaNum, isTen, zhixuanview, isMiss, type);
			initBotm(zhixuanview);
			missView.put(id, new HighItemView(zhixuanview, areaNum, addView,
					null, editZhuma));
			refreshView(type, id);
			/**add by yejc 20131008 start*/
			if (Constants.LOTNO_NMK3.equals(lotno)) {
				scrollView = (ScrollView) zhixuanview.findViewById(R.id.activitygroup_scrollview);
				LinearLayout nmk3TitleLayout  = (LinearLayout)zhixuanview.findViewById(R.id.nmk3_play_title_layout);
				nmk3TitleLayout.setVisibility(View.VISIBLE);
				LinearLayout zhumaLayout  = (LinearLayout)zhixuanview.findViewById(R.id.zhuma_introduction);
				zhumaLayout.setVisibility(View.GONE);
				LinearLayout benNumLayout  = (LinearLayout)zhixuanview.findViewById(R.id.buy_activity_bottom_layout);
//				benNumLayout.setVisibility(View.GONE);
				zixuanLayout = (LinearLayout)zhixuanview.findViewById(R.id.sszhixuan_layout);
//				zixuanLayout.setBackgroundResource(R.drawable.nmk3_bg);//来自2013-10-16徐培松
				RelativeLayout bottomLayout = (RelativeLayout)zhixuanview.findViewById(R.id.buy_zixuan_relativelayout);
				bottomLayout.setBackgroundResource(R.drawable.nmk3_bottom_bg);
				Button addBtn = (Button)zhixuanview.findViewById(R.id.buy_zixuan_img_add);
				Button betBtn = (Button)zhixuanview.findViewById(R.id.buy_zixuan_img_touzhu);
				addBtn.setBackgroundResource(R.drawable.nmk3_buy_add_selector);
				betBtn.setBackgroundResource(R.drawable.nmk3_bet_selector);
				betBtn.setText("");
				textTitle.setVisibility(View.GONE);
				CheckBox lossCheckBox = (CheckBox) zhixuanview
						.findViewById(R.id.louzhi_check);
				lossCheckBox
						.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (!isChecked) {
									for (int area_i = 0; area_i < areaNum.length; area_i++) {
										int rowNum = areaNum[area_i].tableLayout
												.getChildCount();
										for (int row_j = 0; row_j < rowNum; row_j++) {
											if(row_j % 2 != 0){
												areaNum[area_i].tableLayout
												.getChildAt(row_j)
												.setVisibility(View.GONE);
											}
										}
									}

								} else {
									for (int area_i = 0; area_i < areaNum.length; area_i++) {
										int rowNum = areaNum[area_i].tableLayout
												.getChildCount();
										for (int row_j = 0; row_j < rowNum; row_j++) {
											if(row_j % 2 != 0){
												areaNum[area_i].tableLayout
												.getChildAt(row_j)
												.setVisibility(View.VISIBLE);
											}
										}
									}
								}
							}
						});
				
				ImageView zouShiTu = (ImageView) findViewById(R.id.nmk3_zoushitu);
				zouShiTu.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						NoticeActivityGroup.LOTNO = NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW;
						Intent intent = new Intent(ZixuanAndJiXuan.this,
								NoticeActivityGroup.class);
						intent.putExtra("position", 0);
						startActivity(intent);
					}
				});
				
				ImageView jiXuanBtn = (ImageView) findViewById(R.id.nmk3_jixuan);
				jiXuanBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						baseSensor.action();
					}
				});
				
			}
			/**add by yejc 20131008 end*/		
		} else {
			refreshView(type, id);
		}
	}

	/**
	 * 创建胆拖页面
	 * 
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 */
	public void createViewDanTuo(AreaNum areaNum[], CodeInterface code,
			int type, boolean isTen, int id, boolean isMiss) {
		sensor.stopAction();
		isJiXuan = false;
		isMove = false;
		this.code = code;
		buyview.removeAllViews();
		if (missView.get(id) == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
			latestLotteryList = (ListView) zhixuanview
					.findViewById(R.id.buy_zixuan_latest_lottery);
			initZixuanView(zhixuanview);
			initViewItemDan(areaNum, isTen, zhixuanview, isMiss, type);
			initBotm(zhixuanview);
			missView.put(id, new HighItemView(zhixuanview, areaNum, addView,
					null, editZhuma));
			missView.get(id).setMissList(missView.get(0).getMissList());
			refreshView(type, id);
		} else {
			refreshView(type, id);
			missView.get(id).setMissList(missView.get(0).getMissList());
		}
		initMissText(missView.get(id).getAreaNum(), true, id);
	}

	private void refreshView(int type, int id) {
		areaNums = missView.get(id).getAreaNum();
		addView = missView.get(id).getAddView();
		editZhuma = missView.get(id).editZhuma;
		itemViewArray = missView.get(id).getItemViewArray();
		if (missView.get(id).getmGallery() != null) {
			mGallery = missView.get(id).getmGallery();
		}
		this.type = type;
		showEditTitle(type);
		setTextPrize(type);
		buyview.addView(missView.get(id).getView());
		initLatestLotteryList();
	}

	public void initLatestLotteryList() {
		final Handler handler = new Handler();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					JSONObject prizemore = PrizeInfoInterface.getInstance()
							.getNoticePrizeInfo(lotno, "1", "10");
					final String msg = prizemore.getString("message");
					final String code = prizemore.getString("error_code");
					if (code.equals("0000")) {
						JSONArray prizeArray = prizemore.getJSONArray("result");

						final List<LatestLotteryInfo> latestLotteryInfos = new ArrayList<LatestLotteryInfo>(
								10);
						for (int i = 0; i < 10; i++) {
							JSONObject prizeJson1 = (JSONObject) prizeArray
									.get(i);
							LatestLotteryInfo latestLotteryInfo = new LatestLotteryInfo();
							latestLotteryInfo.setBatchCode(prizeJson1
									.getString("batchCode"));
							latestLotteryInfo.setWinCode(prizeJson1
									.getString("winCode"));
							latestLotteryInfos.add(latestLotteryInfo);
						}
						handler.post(new Runnable() {

							@Override
							public void run() {
								latestLotteryListAdapter listAdapter = new latestLotteryListAdapter(
										context, latestLotteryInfos);
								if (latestLotteryList != null) {
									latestLotteryList.setAdapter(listAdapter);
									setListViewHeightBasedOnChildren(latestLotteryList);
								}
							}
						});

					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, msg, Toast.LENGTH_SHORT)
										.show();
							}
						});
					}
				} catch (JSONException e) {
				}
			}
		}).start();
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	private void initMissText(AreaNum areaNums[], boolean isDanTuo, int id) {
		List<List> missList = missView.get(id).getMissList();
		for (int i = 0; i < areaNums.length; i++) {
			int index = 0;
			if (highttype.equals("SSC") || highttype.equals("CQ11X5_PT_QZ2")
					|| highttype.equals("CQ11X5_PT_QZ3")) {
				index = areaNums.length - 1 - i;
			} else if (highttype.equals("DLC")
					|| highttype.equals("NMK3_TWO_SAME_DAN")) {
				index = i;
			}
			if (missList.size() > 0 && missList.size() > index && !isDanTuo) {
				PublicMethod.setMissText(areaNums[i].table.textList,
						missList.get(index));
			} else if (missList.size() > 0) {
				PublicMethod.setMissText(areaNums[i].table.textList,
						missList.get(0));
			}
		}
	}

	private void initViewItemDan(AreaNum areaNums[], boolean isTen,
			View zhixuanview, boolean isMiss, int type) {
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int tableLayoutIds[] = { R.id.buy_zixuan_table_one,
				R.id.buy_zixuan_table_two, R.id.buy_zixuan_table_third,
				R.id.buy_zixuan_table_four, R.id.buy_zixuan_table_five };
		int textViewIds[] = { R.id.buy_zixuan_text_one,
				R.id.buy_zixuan_text_two, R.id.buy_zixuan_text_third,
				R.id.buy_zixuan_text_four, R.id.buy_zixuan_text_five };
		int linearViewIds[] = { R.id.buy_zixuan_linear_one,
				R.id.buy_zixuan_linear_two, R.id.buy_zixuan_linear_third,
				R.id.buy_zixuan_linear_four, R.id.buy_zixuan_linear_five };
		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			areaNums[i].initView(tableLayoutIds[i], textViewIds[i],
					linearViewIds[i], zhixuanview);
			AreaNum areaNum = areaNums[i];
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum
						+ areaNums[i - 1].aIdStart;
			}
			areaNums[i].table = makeBallTable(areaNums[i].tableLayout,
					iScreenWidth, areaNum.areaNum, areaNum.ballResId,
					areaNum.aIdStart, areaNum.aBallViewText, this, this, isTen,
					null, isMiss, type, i);
			areaNums[i].init(type);

		}
	}

	/**
	 * 初始化自选底部
	 * 
	 * @param type
	 */
	private void initBotm(View zhixuanview) {
		// 获取号码篮按钮对象
		Button add_dialog = (Button) zhixuanview
				.findViewById(R.id.buy_zixuan_img_add_delet);
		final TextView textNum = (TextView) zhixuanview
				.findViewById(R.id.buy_zixuan_add_text_num);
		addView.setTextNum(textNum);
		addView.setzJActivity(this);
		addView.setContext(this.getContext());
		addView.setZiXuan(false);
		addView.updateTextNum();
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addView.getSize() > 0) {
					showAddViewDialog();
				} else {
					Toast.makeText(
							ZixuanAndJiXuan.this,
							ZixuanAndJiXuan.this
									.getString(R.string.buy_add_dialog_alert),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 获取加入号码蓝按钮对象
		Button add = (Button) zhixuanview.findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertStr = isTouzhu();
				if (alertStr.equals("true")) {
					addToCodes();
				} else if (alertStr.equals("false")) {
					dialogExcessive();
				} else {
					alertInfo(alertStr);
				}
			}

		});

		Button delete = (Button) zhixuanview
				.findViewById(R.id.buy_zixuan_img_delele);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				for (int i = 0; i < areaNums.length; i++) {
					areaNums[i].table.clearAllHighlights();
				}
			}
		});
		Button zixuanTouzhu = (Button) zhixuanview
				.findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
	}

	private void initViewItem(AreaNum[] areaNums, boolean isTen,
			View zhixuanview, boolean isMiss, int type) {
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int tableLayoutIds[] = { R.id.buy_zixuan_table_one,
				R.id.buy_zixuan_table_two, R.id.buy_zixuan_table_third,
				R.id.buy_zixuan_table_four, R.id.buy_zixuan_table_five };
		int textViewIds[] = { R.id.buy_zixuan_text_one,
				R.id.buy_zixuan_text_two, R.id.buy_zixuan_text_third,
				R.id.buy_zixuan_text_four, R.id.buy_zixuan_text_five };
		int linearViewIds[] = { R.id.buy_zixuan_linear_one,
				R.id.buy_zixuan_linear_two, R.id.buy_zixuan_linear_third,
				R.id.buy_zixuan_linear_four, R.id.buy_zixuan_linear_five };

		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			areaNums[i].initView(tableLayoutIds[i], textViewIds[i],
					linearViewIds[i], zhixuanview);
			AreaNum areaNum = areaNums[i];
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum
						+ areaNums[i - 1].aIdStart;
			}
			areaNums[i].table = makeBallTable(areaNums[i].tableLayout,
					iScreenWidth, areaNum.areaNum, areaNum.ballResId,
					areaNum.aIdStart, areaNum.aBallViewText, this, this, isTen,
					null, isMiss, type, i);
			areaNums[i].init(type);
	
			Button btn = new Button(this);
			Button btnDw = new Button(this);
			if (areaNum.isJxBtn) {
				btn.setVisibility(Button.VISIBLE);
				btnDw.setVisibility(Button.VISIBLE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,
						areaNum.chosenBallSum, this, view, areaNum.table,
						areaNum.areaMin, i);
			} else {
				btn.setVisibility(Button.GONE);
				btnDw.setVisibility(Button.GONE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,
						areaNum.chosenBallSum, this, view, areaNum.table,
						areaNum.areaMin, i);
			}
			if (areaNum.isSensor) {
				this.areaNums = areaNums;
			}

		}
	}

	/**
	 * 创建重庆11选5不可滑动直选页面
	 * 
	 * @param areaNum
	 * @param code
	 * @param type
	 * @param isTen
	 */
	public void createViewCQ(AreaNum areaNum[], CodeInterface code, int type,
			int id, boolean isMiss) {
		sensor.stopAction();
		isJiXuan = false;
		isMove = false;
		this.code = code;
		buyview.removeAllViews();
		if (missView.get(id) == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View zhixuanview = inflater.inflate(R.layout.ssczhixuan_new_green,null);
			latestLotteryList = (ListView) zhixuanview.findViewById(R.id.buy_zixuan_latest_lottery);
			simulateSelectNumberView = (HistoryNumberView) zhixuanview.findViewById(R.id.simulate_selectnumber_view);
			initZixuanView(zhixuanview);
			initViewItem(areaNum, zhixuanview, isMiss, type);
			initBotm(zhixuanview);
			missView.put(id, new HighItemView(zhixuanview, areaNum, addView,null, editZhuma));
			refreshView(type, id);

			zixuanLayout = (LinearLayout) zhixuanview
					.findViewById(R.id.sszhixuan_layout);
			//...miqingqiang start
			simulateSelectNumberView = (HistoryNumberView)zhixuanview. findViewById(R.id.simulate_selectnumber_view);
			historyBtn=(Button)zhixuanview.findViewById(R.id.buy_choose_history_list);
			listView=(LinearLayout)zhixuanview.findViewById(R.id.buy_choose_history_listview);
			historyBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!historyFlag){
						listView.setVisibility(View.GONE);
						historyBtn.setBackgroundResource(R.drawable.choose_button_up);
						historyFlag=true;
					}else{
						listView.setVisibility(View.VISIBLE);
						historyBtn.setBackgroundResource(R.drawable.choose_button_down);
						historyFlag=false;
					}
				}});
			//...end

		} else {
			refreshView(type, id);
		}
		historyBtn=(Button)findViewById(R.id.buy_choose_history_list);
		
	}

	/**
	 * 初始化重庆11选五选区
	 * 
	 * @param areaNums
	 * @param isTen
	 * @param zhixuanview
	 * @param isMiss
	 * @param type
	 */
	private void initViewItem(AreaNum[] areaNums, View zhixuanview,boolean isMiss, int type) {
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		int tableLayoutIds[] = { R.id.buy_zixuan_table_one,
				R.id.buy_zixuan_table_two, R.id.buy_zixuan_table_third,
				R.id.buy_zixuan_table_four, R.id.buy_zixuan_table_five };
		int textViewIds[] = { R.id.buy_zixuan_text_one,
				R.id.buy_zixuan_text_two, R.id.buy_zixuan_text_third,
				R.id.buy_zixuan_text_four, R.id.buy_zixuan_text_five };
		int textViewTishiIds[] = { R.id.buy_zixuan_tishi_text_one,
				R.id.buy_zixuan_tishi_text_two,
				R.id.buy_zixuan_tishi_text_third,
				R.id.buy_zixuan_tishi_text_four,
				R.id.buy_zixuan_tishi_text_five };
		int linearViewIds[] = { R.id.buy_zixuan_linear_one,
				R.id.buy_zixuan_linear_two, R.id.buy_zixuan_linear_third,
				R.id.buy_zixuan_linear_four, R.id.buy_zixuan_linear_five };

		// 初始化选区
		for (int i = 0; i < areaNums.length; i++) {
			areaNums[i].initView(tableLayoutIds[i], textViewIds[i],linearViewIds[i], textViewTishiIds[i], zhixuanview);
			AreaNum areaNum = areaNums[i];
			if (i != 0) {
				areaNum.aIdStart = areaNums[i - 1].areaNum+ areaNums[i - 1].aIdStart;
			}
			areaNums[i].table = makeBallTableCQ(areaNums[i].tableLayout,
					iScreenWidth, areaNum.areaNum, areaNum.ballResId,
					areaNum.aIdStart, areaNum.aBallViewText, this, this, isTen,
					null, isMiss, type, i, areaNum.area);
			areaNums[i].init();
			areaNums[i].initTishi();
			if (!TextUtils.isEmpty(areaNums[i].textTtitle)) {
				if(Constants.LOTNO_CQ_ELVEN_FIVE.equals(lotno)){
					areaNums[i].initTextColor(Color.WHITE,getResources().getColor(R.color.cq_11_5_text_color));
					areaNums[i].initTextBg(R.drawable.tips_bg, 0);
				}
			}
			Button btn = new Button(this);
			Button btnDw = new Button(this);
			if (areaNum.isJxBtn) {
				btn.setVisibility(Button.VISIBLE);
				btnDw.setVisibility(Button.VISIBLE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,areaNum.chosenBallSum, this, view, areaNum.table,areaNum.areaMin, i);
			} else {
				btn.setVisibility(Button.GONE);
				btnDw.setVisibility(Button.GONE);
				areaNum.jixuanBtn = new JiXuanBtn(areaNum.isRed, btn, btnDw,areaNum.chosenBallSum, this, view, areaNum.table,areaNum.areaMin, i);
			}
			if (areaNum.isSensor) {
				this.areaNums = areaNums;
			}

		}
	}

	/**
	 * 创建重庆11选5BallTable
	 * 
	 * @param LinearLayoutaParentView
	 *            上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public BallTable makeBallTableCQ(TableLayout tableLayout, int aFieldWidth,
			int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick, boolean isTen,
			List<String> missValues, boolean isMiss, int type, int area,
			int[] areaNum) {

		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart, context);
		int iFieldWidth = aFieldWidth;// 屏幕宽度
		int scrollBarWidth = 4;
		int maxNum = areaNum[zuidazhi(areaNum)];
		int nmk3HezhiMargin = PublicMethod.getPxInt(8, context);
		int iBallViewWidth = (iFieldWidth - scrollBarWidth - (maxNum - 1)* nmk3HezhiMargin) / maxNum+3;// 设置球的宽度
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* maxNum) / 2;

		int iBallViewNo = 0;
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}
		int iBallViewHeight = iBallViewWidth;// 设置球的高度
		String[][] nmk3ThreeSameStrs = { { "1", "2", "3", "4", "5" },{ "6", "7", "8", "9", "10", "11" } };// 设置球上面显示的文字

		for (int i = 0; i < areaNum.length; i++) {
			TableRow tableRowText = new TableRow(context);
			TableRow tableRow = new TableRow(context);
			tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
			tableRowText.setGravity(Gravity.CENTER_HORIZONTAL);
			for (int col = 0; col < areaNum[i]; col++) {
				String iStrTemp = nmk3ThreeSameStrs[i][col];
				/**
				 * 开始画小球
				 */
				OneBallView tempBallView = PaindBall(aIdStart + iBallViewNo,iBallViewWidth, iBallViewHeight, iStrTemp, aResId,onclick);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				TableRow.LayoutParams lpMiss = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(0, 10, 2, 1);
				} else if (col == areaNum[i]) {
					lp.setMargins(2, 10, 0, 1);
				} else {
					lp.setMargins(2, 10, 2, 1);
				}
				lpMiss.setMargins(0, 1,0, 1);
				tableRow.addView(tempBallView, lp);
				if (isMiss) {
					/**
					 * 开始画遗漏值
					 */
					TextView textView = PaindMiss(missValues, iBallViewNo,rankInt,R.drawable.cq_11_5_miss_bg);
					tableRowText.addView(textView, lpMiss);
					iBallTable.textList.add(textView);
				}
				iBallViewNo++;
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(PublicConst.WC, PublicConst.WC));
		}
		return iBallTable;
	}

	/**
	 * 计算数组最大值的下标
	 * 
	 * @param a
	 * @return
	 */
	private int zuidazhi(int[] a) {
		int max = a[0];
		int dex = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
				dex = i;
			}
			if (i == a.length - 1) {
				return dex;
			}
		}
		return 0;
	}

	/**
	 * 画球
	 * 
	 * @param ballId小球ID
	 * @param iBallViewWidth小球宽度
	 * @param iBallViewHeight小球高度
	 * @param iStrTemp小球上显示的文字
	 * @param aResId小球颜色ID
	 * @param onclick小球点击事件
	 */
	private OneBallView PaindBall(int ballId, int iBallViewWidth,int iBallViewHeight, String iStrTemp, int[] aResId,OnClickListener onclick) {
		OneBallView tempBallView = new OneBallView(context);
		tempBallView.setId(ballId);// 设置小球ID
		tempBallView.initBall(iBallViewWidth, iBallViewHeight, iStrTemp, aResId);// 设置小球属性
		tempBallView.setOnClickListener(onclick);// 小球点击监听
		return tempBallView;
	}

	/**
	 * 画遗漏值
	 * 
	 * @param missValues遗漏值数据
	 * @param iBallViewNo
	 * @param rankInt
	 * @return
	 */
	private TextView PaindMiss(List<String> missValues, int iBallViewNo,int[] rankInt,int textbg) {
		TextView textView = new TextView(context);
		textView.setBackgroundResource(textbg);
		if (missValues != null) {
			String missValue = missValues.get(iBallViewNo);
			textView.setText(missValue);
			if (rankInt[0] == Integer.parseInt(missValue) || rankInt[1] == Integer.parseInt(missValue)) {
				textView.setTextColor(Color.RED);
			}
		} else {
			textView.setText("0");
		}
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	/**
	 * 初始化选区界面
	 */
	public void initViewItemNew(AreaNum[] areaNum, boolean isTen,
			View zhixuanview) {
		mGallery = (ViewPager) zhixuanview
				.findViewById(R.id.buy_zixuan_viewpager);
		mGallery.removeAllViews();
		NumViewItem numView = new NumViewItem(this, areaNum, null, true);
		ZHmissViewItem zhView = new ZHmissViewItem(this, null, lineNum,
				textSize);
		itemViewArray = new ArrayList<BuyViewItemMiss>();
		itemViewArray.add(numView);
		itemViewArray.add(zhView);
		// 设置 ViewPager 的 Adapter
		MainViewPagerAdapter MianAdapter = new MainViewPagerAdapter(null);
		View view = numView.createView();
		latestLotteryList = (ListView) view
				.findViewById(R.id.buy_zixuan_latest_lottery);
		numView.rightBtn(view);
		numView.rightBtnBG(R.drawable.buy_zh_miss_btn);

		MianAdapter.addView(view);
		MianAdapter.addView(zhView.createView());

		mGallery.setAdapter(MianAdapter);
		// 设置第一显示页面
		mGallery.setCurrentItem(0);
		mGallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setNewPosition(arg0);
				// activity从1到2滑动，2被加载后掉用此方法
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// 从1到2滑动，在1滑动前调用
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});// 设置监听器
	}

	/**
	 * 初始化滑动
	 */
	public void initGallery() {
	}

	/**
	 * 获取直选视图中的相关对象
	 * 
	 * @param zhixuanview
	 *            直选视图对象
	 */
	private void initZixuanView(View zhixuanview) {
		mTextSumMoney = (TextView) zhixuanview
				.findViewById(R.id.buy_zixuan_text_sum_money);
		// 显示注码
		editZhuma = (EditText) zhixuanview
				.findViewById(R.id.buy_zixuan_edit_zhuma);
		textTitle = (TextView) zhixuanview
				.findViewById(R.id.buy_zixuan_text_title);
		textPrize = (TextView) zhixuanview
				.findViewById(R.id.buy_zixuan_text_prize);
		mTextSumMoney.setText(getResources().getString(
				R.string.please_choose_number));
	}

	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addToCodes() {
		// 判断当前投注的注数是否超过最大注数限制
		if (getZhuShu() > MAX_ZHU) {
			dialogExcessive();
		}
		// 判断号码篮中的注数是否大于最大注数限制
		else if (addView.getSize() >= All_ZHU) {
			Toast.makeText(
					ZixuanAndJiXuan.this,
					ZixuanAndJiXuan.this
							.getString(R.string.buy_add_view_zhu_alert),
					Toast.LENGTH_SHORT).show();
		} else {
			if (type == BIG_SMALL) {
				getCodeInfoDX(addView);
			} else {
				// 如果是滑动页面显示遗漏值
				if (isMove && itemViewArray.get(newPosition).isZHmiss) {
					getZCodeInfo(addView);
				}
				// 如有在当前页面显示遗漏值
				else {
					getCodeInfo(addView);
				}
			}
			// 更新号码篮的注数显示
			addView.updateTextNum();
			// 清除号码区域的号码
			again();
		}
	}

	private void showAddViewDialog() {
		addView.createDialog(ZixuanAndJiXuan.this
				.getString(R.string.buy_add_dialog_title));
		addView.showDialog();
	}

	public void getCodeInfo(AddView addView) {
		//快三三不同号的购彩信息获取
		if ("NMK3-DIFFER-THREE".equals(highttype)
				|| "NMK3-SAME-THREE".equals(highttype)) {
			//三不同号
			int threeDiffZhuShu = getThreeDiffZhuShu();
			if (threeDiffZhuShu > 0) {
				CodeInfo codeInfo = addView.initCodeInfo(
						getAmt(threeDiffZhuShu), threeDiffZhuShu);
				setLotoNoAndType(codeInfo);

				String lotoNo = codeInfo.getLotoNo();
				String touzhuType = codeInfo.getTouZhuType();

				code.setZHmiss(false);
				// 设置投注信息的注码，用于与后台投注
				codeInfo.setTouZhuCode(getZhuma());
				// 设置投注信息类的注码，用户客户端显示
				boolean isFirst = true;
				int[] codes = areaNums[0].table.getHighlightBallNOs();
				hightballs = codes.length;
				String codeStr = "";
				if (isFirst) {
					if (lotoNo.equals(Constants.LOTNO_SSC)) {
						if (touzhuType.equals("1start")) {
							codeStr = "-,-,-,-,";
						} else if (touzhuType.equals("2start_zhixuan")) {
							codeStr = "-,-,-,";
						} else if (touzhuType.equals("3start")) {
							codeStr = "-,-,";
						}
					}
					isFirst = false;
				}

				for (int i = 0; i < codes.length; i++) {
					String code = String.valueOf(codes[i]);

					if (lotoNo.equals(Constants.LOTNO_NMK3)) {
						if (touzhuType.equals("hezhi")
								|| touzhuType.equals("different_three")
								|| touzhuType.equals("different_two")) {
							code = PublicMethod.getZhuMa(codes[i]);
						} else if (touzhuType.equals("threesame_tong")) {
							code = "111,222,333,444,555,666";
						} else if (touzhuType.equals("twosame_fu")) {
							code = code + "*";
						} else if (touzhuType.equals("threelink")) {
							code = "123,234,345,456";
						}
					}

					codeStr += code;
					if (i != codes.length - 1
							&& (!lotoNo.equals(Constants.LOTNO_SSC)
									|| touzhuType.equals("2start_zuxuan") || touzhuType
										.equals("2start_hezhi"))) {
						codeStr += ",";
					}
				}
				codeInfo.addAreaCode(codeStr, areaNums[0].textColor);
				// 将选择的注码加入号码篮中
				addView.addCodeInfo(codeInfo);
			}

			//三连号
			int threeLinkZhuShu = getThreeLinkZhuShu();
			if (threeLinkZhuShu > 0) {
				CodeInfo codeInfo = addView.initCodeInfo(
						getAmt(threeLinkZhuShu), threeLinkZhuShu);
				setLotoNoAndType2(codeInfo);

				String lotoNo = codeInfo.getLotoNo();
				String touzhuType = codeInfo.getTouZhuType();

				code.setZHmiss(false);
				// 设置投注信息的注码，用于与后台投注
				codeInfo.setTouZhuCode(getZhuma2());

				// 设置投注信息类的注码，用户客户端显示
				boolean isFirst = true;
				int[] codes = areaNums[1].table.getHighlightBallNOs();
				hightballs = codes.length;
				String codeStr = "";
				if (isFirst) {
					if (lotoNo.equals(Constants.LOTNO_SSC)) {
						if (touzhuType.equals("1start")) {
							codeStr = "-,-,-,-,";
						} else if (touzhuType.equals("2start_zhixuan")) {
							codeStr = "-,-,-,";
						} else if (touzhuType.equals("3start")) {
							codeStr = "-,-,";
						}
					}
					isFirst = false;
				}

				for (int i = 0; i < codes.length; i++) {
					String code = String.valueOf(codes[i]);

					if (lotoNo.equals(Constants.LOTNO_NMK3)) {
						if (touzhuType.equals("hezhi")
								|| touzhuType.equals("different_three")
								|| touzhuType.equals("different_two")) {
							code = PublicMethod.getZhuMa(codes[i]);
						} else if (touzhuType.equals("threesame_tong")) {
							code = "111,222,333,444,555,666";
						} else if (touzhuType.equals("twosame_fu")) {
							code = code + "*";
						} else if (touzhuType.equals("threelink")) {
							code = "123,234,345,456";
						}
					}

					codeStr += code;
					if (i != codes.length - 1
							&& (!lotoNo.equals(Constants.LOTNO_SSC)
									|| touzhuType.equals("2start_zuxuan") || touzhuType
										.equals("2start_hezhi"))) {
						codeStr += ",";
					}
				}
				codeInfo.addAreaCode(codeStr, areaNums[1].textColor);
				// 将选择的注码加入号码篮中
				addView.addCodeInfo(codeInfo);
			}
		} else {
			// 创建投注信息对象，包括金额和注数属性
			int zhuShu = getZhuShu();
			CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
			setLotoNoAndType(codeInfo);

			String lotoNo = codeInfo.getLotoNo();
			String touzhuType = codeInfo.getTouZhuType();

			code.setZHmiss(false);
			// 设置投注信息的注码，用于与后台投注
			codeInfo.setTouZhuCode(getZhuma());

			// 设置投注信息类的注码，用户客户端显示
			boolean isFirst = true;
			for (AreaNum areaNum : areaNums) {
				int[] codes = areaNum.table.getHighlightBallNOs();
				hightballs = codes.length;
				String codeStr = "";
				if (isFirst) {
					if (lotoNo.equals(Constants.LOTNO_SSC)) {
						if (touzhuType.equals("1start")) {
							codeStr = "-,-,-,-,";
						} else if (touzhuType.equals("2start_zhixuan")) {
							codeStr = "-,-,-,";
						} else if (touzhuType.equals("3start")) {
							codeStr = "-,-,";
						}
					}
					isFirst = false;
				}

				for (int i = 0; i < codes.length; i++) {
					String code = String.valueOf(codes[i]);

					if (lotoNo.equals(Constants.LOTNO_NMK3)) {
						if (touzhuType.equals("hezhi")
								|| touzhuType.equals("different_three")
								|| touzhuType.equals("different_two")) {
							code = PublicMethod.getZhuMa(codes[i]);
						} else if (touzhuType.equals("threesame_tong")) {
							code = "111,222,333,444,555,666";
						} else if (touzhuType.equals("twosame_fu")) {
							code = code + "*";
						} else if (touzhuType.equals("threelink")) {
							code = "123,234,345,456";
						}
					}

					codeStr += code;
					if (i != codes.length - 1
							&& (!lotoNo.equals(Constants.LOTNO_SSC)
									|| touzhuType.equals("2start_zuxuan") || touzhuType
										.equals("2start_hezhi"))) {
						codeStr += ",";
					}
				}
				codeInfo.addAreaCode(codeStr, areaNum.textColor);
			}
			// 将选择的注码加入号码篮中
			addView.addCodeInfo(codeInfo);
		}
	}

	void setLotoNoAndType2(CodeInfo codeInfo) {
		
	}

	String getZhuma2() {
		return "";
	}

	int getThreeLinkZhuShu() {
		return -1;
	}

	int getThreeDiffZhuShu() {
		return -1;
	}

	/**
	 * 组合遗漏添加到选号篮
	 * 
	 * @param addView
	 */
	public void getZCodeInfo(AddView addView) {
		List<MyButton> missBtnList = itemViewArray.get(newPosition).missBtnList;
		for (int i = 0; i < missBtnList.size(); i++) {
			MyButton myBtn = missBtnList.get(i);
			if (myBtn.isOnClick()) {
				int zhuShu = 1;
				CodeInfo codeInfo = addView
						.initCodeInfo(getAmt(zhuShu), zhuShu);
				String codeStr = myBtn.getBtnText();
				code.setZHmiss(true);
				code.setIsZHcode(codeStr);
				codeInfo.setTouZhuCode(getZhuma());
				String[] alertStr = codeStr.split("\\,");
				for (String str : alertStr) {
					codeInfo.addAreaCode(str, Color.RED);
				}
				addView.addCodeInfo(codeInfo);
			}
		}
	}

	/**
	 * 计算组合遗漏选中按钮数
	 * 
	 * @return
	 */
	public int getClickNum() {
		int onClickNum = 0;
		List<MyButton> missBtnList = itemViewArray.get(newPosition).missBtnList;
		for (int i = 0; i < missBtnList.size(); i++) {
			MyButton myBtn = missBtnList.get(i);
			if (myBtn.isOnClick()) {
				onClickNum++;
			}
		}
		return onClickNum;
	}

	/**
	 * 添加大小
	 * 
	 * @param addView
	 */
	public void getCodeInfoDX(AddView addView) {
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		setLotoNoAndType(codeInfo);
		codeInfo.setTouZhuCode(getZhuma());
		for (AreaNum areaNum : areaNums) {
			String[] codes = areaNum.table.getHighlightStr();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				codeStr += codes[i];
				if (i != codes.length - 1) {
					codeStr += ",";
				}
			}
			codeInfo.addAreaCode(codeStr, areaNum.textColor);
		}
		addView.addCodeInfo(codeInfo);
	}

	public int getAmt(int zhuShu) {
		if (betAndGift != null) {
			return zhuShu * betAndGift.getAmt();
		} else {
			return 0;
		}

	}

	public void showEditTitle(int type) {
		textTitle.setTextSize(11);
		textTitle.setText("已选:");
		textTitle.setTextSize(15);
	}

	public void setTextPrize(int type) {
		textPrize.setTextSize(11);
		switch (type) {
		case ONE:
			textPrize.setText(getString(R.string.ssc_prize_one));
			break;
		case TWO:
			textPrize.setText(getString(R.string.ssc_prize_two_zx));
			break;
		case THREE:
			textPrize.setText(getString(R.string.ssc_prize_third));
			break;
		case Constants.SSC_THREE_GROUP_THREE:
			textPrize.setText(getString(R.string.ssc_prize_third_three));
			break;
		case Constants.SSC_THREE_GROUP_SIX:
			textPrize.setText(getString(R.string.ssc_prize_third_six));
			break;
		case FIVE:
			textPrize.setText(getString(R.string.ssc_prize_five_zx));
			break;
		case TWO_ZUXUAN:
			textPrize.setText(getString(R.string.ssc_prize_two_zu));
			break;
		case TWO_HEZHI:
			textPrize.setText(getString(R.string.ssc_prize_two_hz));
			break;
		case FIVE_TONGXUAN:
			textPrize.setText(getString(R.string.ssc_prize_five_tx));
			break;
		case BIG_SMALL:
			textPrize.setText(getString(R.string.ssc_prize__dx));
			break;
		case NMK3_HEZHI:
			textPrize.setText(getString(R.string.nmk3_hezhi));
			break;
		case NMK3_THREESAME_TONG:
			textPrize.setText(getString(R.string.nmk3_threesame_tong));
			break;
		case NMK3_THREESAME_DAN:
			textPrize.setText(getString(R.string.nmk3_threesame_dan));
			break;
		case NMK3_TWOSAME_FU:
			textPrize.setText(getString(R.string.nmk3_twosame_fu));
			break;
		case NMK3_TWOSAME_DAN:
			textPrize.setText(getString(R.string.nmk3_twosame_dan));
			break;
		case NMK3_DIFF_THREE:
			textPrize.setText(getString(R.string.nmk3_diff_three));
			break;
		case NMK3_DIFF_TWO:
			textPrize.setText(getString(R.string.nmk3_diff_two));
			break;
		case NMK3_THREE_LINK:
			textPrize.setText(getString(R.string.nmk3_three_link));
			break;
		}

	}

	// 创建机选页面

	private Spinner jixuanZhu;
	private LinearLayout zhumaView;
	protected SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	public Vector<Balls> balls = new Vector();
	protected Balls ballOne;
	public Toast toastjixuan;

	public void createviewmechine(Balls balles, int id) {
		isJiXuan = true;
		isMove = false;
		buyview.removeAllViews();
		if (missView.get(id) == null) {
			View jixuanview = inflater.inflate(R.layout.sscmechine, null);
			initJiXuanView(balles, jixuanview);
			missView.put(id, new HighItemView(jixuanview, null, addView, null,
					editZhuma));
		} else {
			initJiXuanView(balles, missView.get(id).getView());
		}
	}

	private void initJiXuanView(Balls balles, View jixuanview) {
		buyview.addView(jixuanview);
		sensor.startAction();
		this.ballOne = balles;
		zhumaView = (LinearLayout) jixuanview
				.findViewById(R.id.buy_danshi_jixuan_linear_zhuma);
		zhumaView.removeAllViews();
		toastjixuan = Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT);
		toastjixuan.show();
		balls = new Vector<Balls>();
		// 初始化spinner
		jixuanZhu = (Spinner) jixuanview
				.findViewById(R.id.buy_danshi_jixuan_spinner);
		if (isbigsmall) {
			TextView textview = (TextView) jixuanview
					.findViewById(R.id.TextView01);
			textview.setText("注数:一注");
			jixuanZhu.setVisibility(View.GONE);
			jixuanZhu.setSelection(0);
		} else {
			jixuanZhu.setSelection(4);
			jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = jixuanZhu.getSelectedItemPosition();
					if (isOnclik) {
						zhumaView.removeAllViews();
						balls = new Vector();
						for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
							Balls ball = ballOne.createBalls();
							balls.add(ball);
						}
						createTable(zhumaView);
					} else {
						isOnclik = true;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
		}

		int index = jixuanZhu.getSelectedItemPosition() + 1;
		for (int i = 0; i < index; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
		sensor.onVibrator();// 震动
		Button zixuanTouzhu = (Button) jixuanview
				.findViewById(R.id.buy_danshi_jixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		final TextView textNum = (TextView) findViewById(R.id.buy_zixuan_add_text_num);
		Button add_dialog = (Button) findViewById(R.id.buy_zixuan_img_add_delet);
		addView = new AddView(textNum, this, false);
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addView.getSize() > 0) {
					showAddViewDialog();
				} else {
					Toast.makeText(
							ZixuanAndJiXuan.this,
							ZixuanAndJiXuan.this
									.getString(R.string.buy_add_dialog_alert),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Button add = (Button) findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addJxToCodes();
			}

		});
	}

	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addJxToCodes() {
		if (addView.getSize() + balls.size() - 1 >= All_ZHU) {
			Toast.makeText(
					ZixuanAndJiXuan.this,
					ZixuanAndJiXuan.this
							.getString(R.string.buy_add_view_zhu_alert),
					Toast.LENGTH_SHORT).show();
		} else {
			if (type == BIG_SMALL) {
				getJxCodeInfoDX(addView);
			} else {
				getJxCodeInfo(addView);
			}
			addView.updateTextNum();
			againJx();
		}
	}

	public void getJxCodeInfo(AddView addView) {
		for (int i = 0; i < balls.size(); i++) {
			Balls ball = balls.get(i);
			String codeStr = getAddZhuma(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			codeInfo.addAreaCode(codeStr, Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}

	public void getJxCodeInfoDX(AddView addView) {
		for (int i = 0; i < balls.size(); i++) {
			Balls ball = balls.get(i);
			String codeStr = getAddZhumaDX(i);
			CodeInfo codeInfo = addView.initCodeInfo(betAndGift.getAmt(), 1);
			setLotoNoAndType(codeInfo);
			codeInfo.addAreaCode(codeStr, Color.BLACK);
			codeInfo.setTouZhuCode(getZhuma(ball));
			addView.addCodeInfo(codeInfo);
		}
		addView.setIsJXcode(ballOne.getZhuma(balls, iProgressBeishu));
	}

	void setLotoNoAndType(CodeInfo codeInfo) {

	}

	/**
	 * 重新选择方法
	 */
	private void againJx() {
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}

	/**
	 * 购彩篮显示注码
	 * 
	 * @return
	 */
	private String getAddZhuma(int index) {
		String zhumaString = "";
		for (int j = 0; j < balls.get(index).getVZhuma().size(); j++) {
			if (isTen) {
				zhumaString += balls.get(index).getTenShowZhuma(j);
			} else {
				zhumaString += balls.get(index).getShowZhuma(j);
			}

			if (j != balls.get(index).getVZhuma().size() - 1) {
				zhumaString += "|";
			}
		}
		return zhumaString;
	}

	/**
	 * 购彩篮显示注码
	 * 
	 * @return
	 */
	private String getAddZhumaDX(int index) {
		String zhumaString = "";
		for (int j = 0; j < balls.get(index).getVZhuma().size(); j++) {
			String str = balls.get(index).getTenShowZhuma(j);
			if (str.equals("00")) {
				zhumaString += "大";
			} else if (str.equals("01")) {
				zhumaString += "小";
			} else if (str.equals("02")) {
				zhumaString += "单";
			} else if (str.equals("03")) {
				zhumaString += "双";
			}
			if (j != balls.get(index).getVZhuma().size() - 1) {
				zhumaString += "|";
			}
		}
		return zhumaString;
	}

	// 机选创建小球
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			for (int j = 0; j < balls.get(i).getVZhuma().size(); j++) {
				String color = (String) balls.get(i).getVColor().get(j);
				TableLayout table;
				if (isbigsmall) {
					table = PublicMethod.makeBallTableJiXuanbigsmall(null,
							iScreenWidth, BallResId, balls.get(i).getBalls(j),
							this);
				} else {
					table = PublicMethod.makeBallTableJiXuan(null,
							iScreenWidth, BallResId, balls.get(i).getBalls(j),
							this);
				}
				lines.addView(table);
			}
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhumaView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhumaView);
					} else {
						Toast.makeText(
								ZixuanAndJiXuan.this,
								getResources().getText(
										R.string.zhixuan_jixuan_toast),
								Toast.LENGTH_SHORT).show();

					}

				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(10, 5, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
			if (i % 2 == 0) {
				lines.setBackgroundResource(R.drawable.jixuan_list_bg);
			}
			lines.setPadding(0, 3, 0, 0);
			layout.addView(lines);

		}

	}

	/**
	 * 投注方法
	 */
	public void beginTouZhu() {
		if (isJiXuan) {
			touZhuJX();
		} else {
			touZhuZX();
		}
	}

	public void touZhuJX() {

		if (balls.size() == 0) {
			alertInfo("请至少选择1注彩票");
		} else {
			if (addView.getSize() == 0) {
				addJxToCodes();
				alertJX();
			} else {
				showAddViewDialog();
			}

		}
	}

	public void touZhuZX() {
		String alertStr = isTouzhu();
		if (alertStr.equals("true") && addView.getSize() == 0) {
			addToCodes();
			alertZX();
		} else if (alertStr.equals("true") && addView.getSize() > 0) {
			addToCodes();
			showAddViewDialog();
		} else if (addView.getSize() > 0) {
			showAddViewDialog();
		} else if (alertStr.equals("false")) {
			dialogExcessive();
		} else {
			alertInfo(alertStr);
		}
	}

	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
	public void setSeekWhenAddOrSub(int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu, View view) {
		ImageButton subtractBeishuBtn = (ImageButton) view.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}

	/**
	 * seekBar组件的监听器
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
		alertText.setText(getTouzhuAlert());

	}

	public void changeTextSumMoney() {
		String text = textSumMoney(itemViewArray.get(0).areaNums,
				iProgressBeishu);
		if (toast == null) {
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		} else {
			toast.setText(text);
			toast.show();
		}

	}

	/**
	 * 创建BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,
			int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick, boolean isTen,
			List<String> missValues, boolean isMiss, int type, int area) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart, context);
		int iBallNum = aBallNum;
		int viewNumPerLine = 8; // 时时彩设置每列小球的个数为10
		if (isTen) {
			viewNumPerLine = 10;
		}

		if ((type == NMK3_THREESAME && area == 0) || type == NMK3_TWOSAME_FU
				|| type == NMK3_TWOSAME_DAN || (type == NMK3_DIFF_THREE && area == 0)
				|| type == NMK3_DIFF_TWO) {
			viewNumPerLine = 6;//来自2013-10-17徐培松    每行显示的最大个数
		} else if (type == NMK3_HEZHI) {
			viewNumPerLine = 7;
		}

		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		
		int nmk3HezhiMargin = PublicMethod.getPxInt(8, context);
		int nmk3HezhiMarginTop = PublicMethod.getPxInt(3, context);
		int iBallViewWidth = 0;
		if (type == NMK3_HEZHI||(type == NMK3_THREESAME && area == 0)){ //add by yejc 20130929
			iBallViewWidth = (iFieldWidth - scrollBarWidth - 7*nmk3HezhiMargin) / viewNumPerLine- 2;
		} else if(type == NMK3_TWOSAME_FU||type == NMK3_TWOSAME_DAN||(type == NMK3_DIFF_THREE && area == 0)||type == NMK3_DIFF_TWO||(type == NMK3_THREESAME && area == 0)){
			iBallViewWidth = (iFieldWidth - scrollBarWidth - 6*nmk3HezhiMargin) / viewNumPerLine-2;
		}else {
			iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine- 2;
		}
		
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		int[] rankInt = null;
		if (missValues != null) {
			rankInt = rankList(missValues);
		}

		int iBallViewHeight = iBallViewWidth;
		if ((type == NMK3_THREESAME && area == 1) || type == NMK3_THREE_LINK || (type == NMK3_DIFF_THREE && area == 1)) {
			viewNumPerLine = 1;
			lineNum = 1;
			iBallViewWidth = (iFieldWidth - scrollBarWidth) / 3;
			/** add by pengcx 20130718 start */
			int screenHeight = PublicMethod.getDisplayHeight(context);
			iBallViewHeight = (int) ((screenHeight / 800.0f) * 35);
			/** add by pengcx 20130718 end */
			lastLineViewNum = 0;
			margin = (iFieldWidth - scrollBarWidth - iBallViewWidth
					* viewNumPerLine) / 2;
		}

		String[] nmk3ThreeSameStrs = { "111", "222", "333", "444", "555", "666" };
		String[] nmk3TwoSameStrs = { "11*", "22*", "33*", "44*", "55*", "66*" };
		String[] nmk3TwoSameDanStrs = { "11", "22", "33", "44", "55", "66" };
		int[][] nmk3DifBg={
				{R.drawable.one_normal,R.drawable.one_click},
				{R.drawable.two_normal,R.drawable.two_click},
				{R.drawable.three_normal,R.drawable.three_click},
				{R.drawable.four_normal,R.drawable.four_click},
				{R.drawable.five_normal,R.drawable.five_click},
				{R.drawable.six_normal,R.drawable.six_click}
			};
//		if (type == NMK3_THREESAME_DAN || type == NMK3_TWOSAME_FU
//				|| type == NMK3_TWOSAME_DAN || type == NMK3_DIFF_THREE
//				|| type == NMK3_DIFF_TWO) {
//			/** add by pengcx 20130718 start */
//			int screenHeight = PublicMethod.getDisplayHeight(context);
//			iBallViewHeight = (int) ((screenHeight / 800.0f) * 35.0f);
//			/** add by pengcx 20130718 end */
//		}
		//2013-10-17徐培松
//		if (type == NMK3_THREESAME_DAN || type == NMK3_TWOSAME_FU
//				|| type == NMK3_TWOSAME_DAN ) {
//			/** add by pengcx 20130718 start */
//			int screenHeight = PublicMethod.getDisplayHeight(context);
//			iBallViewHeight = (int) ((screenHeight / 800.0f) * 35.0f);
//			/** add by pengcx 20130718 end */
//		}

		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);

			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = setTemp(aBallViewText, iBallViewNo, col);
				if (type == NMK3_THREESAME && area == 1) {
					iStrTemp = "三同号通选";
				} else if (type == NMK3_THREE_LINK || (type == NMK3_DIFF_THREE && area == 1)) {
					iStrTemp = "三连号通选";
				} else if (type == NMK3_THREESAME && area == 0) {
					iStrTemp = nmk3ThreeSameStrs[col];
				} else if (type == NMK3_TWOSAME_FU) {
					iStrTemp = nmk3TwoSameStrs[col];
				} else if (type == NMK3_TWOSAME_DAN && area == 0) {
					iStrTemp = nmk3TwoSameDanStrs[col];
				}
				
				
				OneBallView tempBallView = null;
				//徐培松start
				if((type==NMK3_DIFF_THREE && area == 0)||type==NMK3_DIFF_TWO){
					tempBallView =new  OneBallView(context,2);
					tempBallView.setId(aIdStart + iBallViewNo);
					tempBallView.initBall(iBallViewWidth,iBallViewHeight,iStrTemp, nmk3DifBg[col],R.color.transparent);
				}else {
					tempBallView =new  OneBallView(context);
					tempBallView.setId(aIdStart + iBallViewNo);
					tempBallView.initBall(iBallViewWidth, iBallViewHeight,iStrTemp, aResId);
				}
				//end
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				//来自2013-10-17徐培松start
//				TableRow.LayoutParams lp2 = new TableRow.LayoutParams();
//				lp2.setMargins(0, nmk3HezhiMargin, 0, nmk3HezhiMargin);
				//...end
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else {
					lp.setMargins(1, 1, 1, 1);
				}
				
				if (type == NMK3_HEZHI||type == NMK3_TWOSAME_DAN || type == NMK3_TWOSAME_FU|| (type == NMK3_THREESAME && area == 0)||(type == NMK3_DIFF_THREE && area == 0)||type == NMK3_DIFF_TWO) {
					if (col != (viewNumPerLine - 1)) {
						lp.setMargins(nmk3HezhiMargin, nmk3HezhiMarginTop, 0, nmk3HezhiMargin);
					} else {
						lp.setMargins(nmk3HezhiMargin, nmk3HezhiMarginTop, nmk3HezhiMargin, nmk3HezhiMargin);
					}
				}
				tableRow.addView(tempBallView, lp);
				if (isMiss) {
					TextView textView = new TextView(context);
					//来自2013-10-17徐培松
					if (lotno == Constants.LOTNO_NMK3) {
						textView.setTextColor(Color.WHITE);
						textView.setBackgroundResource(R.drawable.miss_bg);
					}else {
						textView.setBackgroundColor(Color.WHITE);
					}
					if (missValues != null) {
						String missValue = missValues.get(iBallViewNo);
						textView.setText(missValue);
						if (rankInt[0] == Integer.parseInt(missValue)
								|| rankInt[1] == Integer.parseInt(missValue)) {
							textView.setTextColor(getResources().getColor(
									R.color.nmk3_loss_value_max));
						}
					} else {
						textView.setText("0");
					}
					textView.setGravity(Gravity.CENTER);
					//来自2013-10-17徐培松start
					if((type==NMK3_THREESAME && area == 1)||type==NMK3_THREE_LINK || (type == NMK3_DIFF_THREE && area == 1)){
						tableRowText.addView(textView,lp);
					}else {
//						if(type==NMK3_DIFF_THREE||type==NMK3_DIFF_TWO){
//							tableRowText.addView(textView,lp2);
//						}else {
//							tableRowText.addView(textView);
//						}
						tableRowText.addView(textView);
					}
					//...end
					iBallTable.textList.add(textView);
				}
				iBallViewNo++;
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			TableRow tableRowText = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = setTemp(aBallViewText, iBallViewNo, col);
				if (type == NMK3_THREESAME && area == 0) {
					iStrTemp = nmk3ThreeSameStrs[4 + col];
				} else if (type == NMK3_TWOSAME_FU) {
					iStrTemp = nmk3TwoSameStrs[4 + col];
				} else if (type == NMK3_TWOSAME_DAN && area == 0) {
					iStrTemp = nmk3TwoSameDanStrs[4 + col];
				}

				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				//徐培松start
				if(type==NMK3_DIFF_THREE||type==NMK3_DIFF_TWO){
					tempBallView.initBall(iBallViewWidth, iBallViewHeight,iStrTemp, nmk3DifBg[nmk3DifBg.length-lastLineViewNum+col-1]);
				}else {
					tempBallView.initBall(iBallViewWidth, iBallViewHeight,iStrTemp, aResId);
				}
				//end
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				if (isMiss) {
					TextView textView = new TextView(context);
					//来自2013-10-17徐培松
					textView.setBackgroundColor(Color.WHITE);
					if (missValues != null) {
						String missValue = missValues.get(iBallViewNo);
						textView.setText(missValue);
						if (rankInt[0] == Integer.parseInt(missValue)
								|| rankInt[1] == Integer.parseInt(missValue)) {
							textView.setTextColor(Color.RED);
						}
					} else {
						textView.setText("0");
					}
					textView.setGravity(Gravity.CENTER);
					//来自2013-10-17徐培松start
					if((type==NMK3_THREESAME && area == 1)||type==NMK3_THREE_LINK || (type == NMK3_DIFF_THREE && area == 1)){
						tableRowText.addView(textView,lp);
					}else {
						tableRowText.addView(textView);
					}
					//...end
					iBallTable.textList.add(textView);
				}
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
			tabble.addView(tableRowText, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	public String setTemp(int aBallViewText, int iBallViewNo, int col) {
		String iStrTemp = "";
		if (aBallViewText == 0) {
			iStrTemp = "" + (iBallViewNo);// 小球从0开始
		} else if (aBallViewText == 1) {
			iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
		} else if (aBallViewText == 3) {
			iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
		} else {
			iStrTemp = "" + (aBallViewText + iBallViewNo);
		}
		return iStrTemp;
	}

	public int[] rankList(List<String> myArray) {
		int[] rankInt = new int[myArray.size()];
		for (int n = 0; n < myArray.size(); n++) {
			rankInt[n] = Integer.parseInt(myArray.get(n));
		}
		// 取长度最长的词组 -- 冒泡法
		for (int j = 1; j < rankInt.length; j++) {
			for (int i = 0; i < rankInt.length - 1; i++) {
				// 如果 myArray[i] > myArray[i+1] ，则 myArray[i] 上浮一位
				if (rankInt[i] < rankInt[i + 1]) {
					int temp = rankInt[i];
					rankInt[i] = rankInt[i + 1];
					rankInt[i + 1] = temp;
				}
			}
		}
		return rankInt;
	}

	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		int iBallId = v.getId();
		isBallTable(iBallId);
		showEditText();
		String text = textSumMoney(areaNums, iProgressBeishu);
		showBetMoney(v);
		if (getParent() == null) {
			if (Constants.LOTNO_CQ_ELVEN_FIVE.equals(lotno)) {
			} else {
				((Dlc) this).showBetInfo(text);
			}
		} else {	
			((BuyActivityGroup) getParent()).showBetInfo(text);
		}
	}

	public void showBetMoney(View v) {

	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		int nBallId = 0;
		if (!isMove) {
			for (int i = 0; i < areaNums.length; i++) {
				nBallId = iBallId;
				iBallId = iBallId - areaNums[i].areaNum;
				if (iBallId < 0) {
					if (highttype.equals("NMK3-TWOSAME-DAN")) {
						if (i == 0) {
							if (areaNums[i + 1].table.ballViewVector.get(
									nBallId).getShowId() == 1) {
								areaNums[i + 1].table.changeBallState(
										areaNums[i + 1].chosenBallSum, nBallId);
							}
						} else if (i == 1) {
							if (areaNums[i - 1].table.ballViewVector.get(
									nBallId).getShowId() == 1) {
								areaNums[i - 1].table.changeBallState(
										areaNums[i - 1].chosenBallSum, nBallId);
							}
						}
					}
					areaNums[i].table.changeBallState(
							areaNums[i].chosenBallSum, nBallId);
					break;
				}

			}
		} else {
			AreaNum areaNums[] = itemViewArray.get(0).areaNums;
			for (int i = 0; i < areaNums.length; i++) {
				nBallId = iBallId;
				AreaNum areaNum = areaNums[i];
				iBallId = iBallId - areaNum.areaNum;
				if (iBallId < 0) {
					areaNum.table.changeBallState(areaNum.chosenBallSum,
							nBallId);
					break;
				}
			}
		}
	}

	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < areaNums.length; j++) {
			BallTable ballTable = areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j != 0) {
				if ("NMK3-TWOSAME-DAN".equals(highttype)) {
					zhumas += "#";
				} else {
					zhumas += " | ";
				}
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (highttype.equals("SSC")
						|| highttype.equals("NMK3-THREESAME-DAN")
						|| highttype.equals("NMK3-TWOSAME-DAN")) {
					zhumas += (zhuMa[i]) + "";
				} else if (highttype.equals("DLC")
						|| highttype.equals("NMK3-HE")
						|| highttype.equals("NMK3-DIFFER-THREE")
						|| highttype.equals("NMK3-DIFFER-TWO")) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				} else if (highttype.equals("NMK3-THREESAME-TONG")) {
					zhumas = "111,222,333,444,555,666";
				} else if (highttype.equals("NMK3-THREE-LINK")) {
					zhumas = "123,234,345,456";
				} else if (highttype.equals("NMK3-TWOSAME-FU")) {
					zhumas += (zhuMa[i]) + "*";
				}
				if (i != ballTable.getHighlightBallNOs().length - 1) {
					zhumas += ",";
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
			showEditTitle(this.type);
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\|");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor),
						length - zhuma[i].length(), length,
						Spanned.SPAN_COMPOSING);
			}
			editZhuma.setText(builder, BufferType.EDITABLE);
			showEditTitle(NULL);
		}
	}

	public void alertJX() {
		sensor.stopAction();
		touzhuNet();
		initBet();
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betAndGift);
		app.setAddview(addView);
		Intent intent = new Intent(ZixuanAndJiXuan.this, HghtOrderdeail.class);
		app.setHtextzhuma(addView.getsharezhuma());
		app.setHightball(hightballs);
		app.setHzhushu(addView.getAllZhu());
		startActivity(intent);

	}

	public void alertZX() {
		touzhuNet();
		initBet();
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betAndGift);
		app.setAddview(addView);
		app.setHtextzhuma(addView.getsharezhuma());
		app.setHightball(hightballs);
		app.setHzhushu(addView.getAllZhu());
		Intent intent = new Intent(ZixuanAndJiXuan.this, HghtOrderdeail.class);
		startActivity(intent);
	}

	/**
	 * 初始化倍数和期数进度条
	 * 
	 * @param view
	 */
	public void initImageView(View view) {
		mTextBeishu = (EditText) view.findViewById(R.id.buy_zixuan_text_beishu);
		mSeekBarBeishu = (SeekBar) view
				.findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (EditText) view.findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		setProgressMax(2000);// 设置倍数和期数最大值
		PublicMethod.setEditOnclick(mTextBeishu, mSeekBarBeishu, new Handler());
		PublicMethod.setEditOnclick(mTextQishu, mSeekBarQishu, new Handler());
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,
				mSeekBarBeishu, true, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,
				true, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,
				mSeekBarQishu, false, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,
				false, view);
	}

	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress() {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu.setProgress(iProgressQishu);
	}

	/**
	 * 当前注数
	 */
	public void setZhuShu(int zhushu) {
		iZhuShu = zhushu;
	}

	/**
	 * 自选投注提示框中的信息
	 */
	public String getTouzhuAlert() {
		return "注数：" + addView.getAllZhu() + "注    " + "金额：" + iProgressQishu
				* addView.getAllAmt() * iProgressBeishu + "元";
	}

	/**
	 * 返回当前期数
	 * 
	 * @return
	 */
	public String getBatchCode() {
		String batchCode = "";
		if (highttype.equals("")) {
			batchCode = Ssc.batchCode;
		} else if (highttype.equals("DLC")) {
			batchCode = Dlc.batchCode;
		}
		return batchCode += "期";
	}

	public static void clearBatchCode() {
		Ssc.batchCode = "";
		Dlc.batchCode = "";
	}

	/**
	 * 机选提醒框注码
	 * 
	 * @return
	 */
	public String getJxAlertZhuma() {
		String zhumaString = "";
		for (int i = 0; i < balls.size(); i++) {
			for (int j = 0; j < balls.get(i).getVZhuma().size(); j++) {
				if (highttype.equals("SSC")) {
					zhumaString += balls.get(i).getSpecialShowZhuma(j);
				} else if (highttype.equals("DLC")) {
					zhumaString += balls.get(i).getTenSpecialShowZhuma(j);
				}

				if (j != balls.get(i).getVZhuma().size() - 1) {
					zhumaString += ",";
				}
			}
			if (i != balls.size() - 1) {
				zhumaString += "\n";
			}
		}
		codeStr = "注码：\n" + zhumaString;
		return codeStr;
	}

	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

	}

	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.show();

	}

	/**
	 * 单笔投注大于1万注时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ZixuanAndJiXuan.this);
		builder.setTitle(ZixuanAndJiXuan.this.getString(
				R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于1万注！");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	/**
	 * 重新方法
	 * 
	 */
	public void again() {
		if (areaNums != null) {
			if (!isMove) {
				for (int i = 0; i < areaNums.length; i++) {
					areaNums[i].table.clearAllHighlights();
				}
			} else if (itemViewArray.get(newPosition).isZHmiss) {
				againZH(newPosition);
			} else {
				for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
					itemViewArray.get(0).areaNums[i].table.clearAllHighlights();
				}
			}
			showEditText();
		}
	}

	/**
	 * 清空指定选区小球
	 */
	public void again(int position) {
		if (itemViewArray != null) {
			if (itemViewArray.get(0).areaNums != null) {
				itemViewArray.get(0).areaNums[position].table
						.clearAllHighlights();
			}
		} else {
			areaNums[position].table.clearAllHighlights();
		}
	}

	private void againZH(int position) {
		List<MyButton> missBtnList = itemViewArray.get(position).missBtnList;
		if (missBtnList != null) {
			for (int i = 0; i < missBtnList.size(); i++) {
				MyButton myBtn = missBtnList.get(i);
				if (myBtn.isOnClick()) {
					myBtn.onAction();
				}
			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!toLogin) {
			again();
			sensor.stopAction();
		}
	}

	protected void onStop() {
		super.onStop();
		isTime = true;
		isStart = false;
		isViewEnd = false;
		prizeInfos = null;
		lotnoStr = "";
	}

	/**
	 * 重新初始化机选界面
	 */
	public void againView() {
		sensor.startAction();
		sensor.onVibrator();// 震动
		toastjixuan.show();
		jixuanZhu.setSelection(4);
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}

	/**
	 * 实现震动的类
	 * 
	 * @author Administrator
	 * 
	 */
	public class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			zhumaView.removeAllViews();
			balls = new Vector<Balls>();
			for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
				Balls ball = ballOne.createBalls();
				balls.add(ball);
			}

			createTable(zhumaView);
		}
	}

	/**
	 * 初始化投注信息
	 */
	public void initBet() {
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）
		betAndGift.setAmount("" + addView.getAllAmt() * iProgressBeishu * 100);
		betAndGift.setIsSellWays("1");
		betAndGift.setBet_code(addView.getTouzhuCode(iProgressBeishu,
				betAndGift.getAmt() * 100));

	}

	public void isMissNet(MissJson missJson, String sellWay, boolean isZHMiss) {
		if (isZHMiss && missView.get(radioId).isZMissNet) {
			missView.get(radioId).isZMissNet = false;
			List<String> zMissList = missView.get(radioId).getZHMissList();
			if (!isJiXuan && (zMissList == null || zMissList.size() == 0)) {
				getMissNet(missJson, sellWay, isZHMiss);
			}
		} else if (missView.get(radioId).isMissNet) {
			missView.get(radioId).isMissNet = false;
			List<List> missList = missView.get(radioId).getMissList();
			if (!isJiXuan && (missList == null || missList.size() == 0)) {
				getMissNet(missJson, sellWay, isZHMiss);
			}
		}
		Log.e("missView.get(radioId).isMissNet", ""
				+ missView.get(radioId).isMissNet);
	}

	/**
	 * 查询遗漏值联网
	 */
	public void getMissNet(final MissJson missJson, final String sellWay,
			final boolean isZHMiss) {
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			int id = radioId;

			@Override
			public void run() {
				if (sellWay.contains(";")) {
					str = NMK3MissInterface.getInstance().betMiss(lotnoStr,
							sellWay);
				} else {
					str = MissInterface.getInstance()
							.betMiss(lotnoStr, sellWay);
				}

				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					missJson.jsonToList(sellWay, obj.getJSONObject("result"));
					missError(error, msg, missJson, isZHMiss, id);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	private void missError(String error, final String msg,
			final MissJson missJson, final boolean isZHMiss, final int id)
			throws JSONException {
		if (error.equals(ERROR_WIN)) {

			handler.post(new Runnable() {
				@Override
				public void run() {
					if (isZHMiss) {
						missView.get(id).setZHMissList(missJson.zMissList);
						updateMissView(missJson);
					} else {
						missView.get(id).setMissList(missJson.missList);
						initMissText(missView.get(id).getAreaNum(), false, id);
					}

				}
			});
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * 刷新遗漏值界面
	 * 
	 * @param missList
	 */
	private void updateMissView(MissJson missJson) {
		if (itemViewArray != null) {
			itemViewArray.get(1).updateView(missJson);
		}
	}

	/**
	 * 格式化开奖号码
	 */
	public String formtPrizeInfo(String info) {
		if (info.length() > 5) {
			return info;
		} else {
			String returnStr = "";
			int start = 0;
			while (start < info.length()) {
				returnStr += info.substring(start, start += 1);
				if (start != info.length()) {
					returnStr += ",";
				}
			}
			return returnStr;
		}

	}

	/**
	 * 设置倍数和期数最大值
	 * 
	 * @param max
	 */
	public void setProgressMax(int max) {
		mSeekBarBeishu.setMax(max);
		mSeekBarQishu.setMax(max);
	}

	@Override
	public void errorCode_0000() {
		String code = addView.getsharezhuma();
		clearArea();
		for (int i = 0; i < areaNums.length; i++) {
			areaNums[i].table.clearAllHighlights();
		}
		editZhuma.setText("");

		Intent intent = new Intent(this, BettingSuccessActivity.class);
		intent.putExtra("page", BettingSuccessActivity.BETTING);
		intent.putExtra("lotno", betAndGift.getLotno());
		intent.putExtra("amount", betAndGift.getAmount());
		startActivity(intent);
	}

	public void clearArea() {
		if (addView != null) {
			addView.clearInfo();
			addView.updateTextNum();
		}
	}

	@Override
	public void errorCode_000000() {

	}

	@Override
	public Context getContext() {
		return this;
	}

	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(string)
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.show();

	}

	public void showBetInfo(String text) {
		if (getParent() != null) {
			((BuyActivityGroup) getParent()).showBetInfo(textSumMoney(areaNums,iProgressBeishu));
		}
	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 4:
			if (addView != null && addView.getSize() != 0) {
				alertExit(getString(R.string.buy_alert_exit));
			} else {
				finish();
			}
			break;
		}
		return false;
	}

	public void setLotnoX(String lotno) {
		this.lotno = lotno;
	}

	class latestLotteryListAdapter extends BaseAdapter {
		private Context context;
		private List<LatestLotteryInfo> latestLotteryList;
		private LayoutInflater inflater;

		public latestLotteryListAdapter(Context context,
				List<LatestLotteryInfo> latestLotteryList) {
			super();
			this.context = context;
			this.latestLotteryList = latestLotteryList;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return latestLotteryList.size();
		}

		@Override
		public Object getItem(int position) {
			return latestLotteryList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.latestlottery_listitem,
						null);
				holder.issue = (TextView) convertView
						.findViewById(R.id.latestlottery_textview_issue);
				holder.winningNumber = (TextView) convertView
						.findViewById(R.id.latestlottery_textview_winningnumbers);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String batchCode = latestLotteryList.get(position).getBatchCode();
			StringBuffer batchCodeString = new StringBuffer();
			if (lotno == Constants.LOTNO_eleven) {
				batchCodeString.append("第").append(batchCode.substring(0, 8))
						.append("期");
			} else {
				batchCodeString.append("第").append(batchCode.substring(0, 8))
						.append("-").append(batchCode.substring(8)).append("期");
			}

			holder.issue.setText(batchCodeString);

			String winCodeString = null;
			if (lotno == Constants.LOTNO_NMK3) {
				winCodeString = PublicMethod.formatNMK3Num(latestLotteryList
						.get(position).getWinCode(), 2);
			} else if (lotno == Constants.LOTNO_SSC) {
				winCodeString = PublicMethod.formatSSCNum(latestLotteryList
						.get(position).getWinCode(), 1);
			} else {
				winCodeString = PublicMethod.formatNum(
						latestLotteryList.get(position).getWinCode(), 2);
			}

			holder.winningNumber.setText(winCodeString);
			
			
			//来自2013-10-17徐培松  －－－>>>latestlottery_listitem布局
			if (lotno == Constants.LOTNO_NMK3) {
				holder.issue.setTextColor(getResources().getColor(R.color.white));
				holder.winningNumber.setTextColor(getResources().getColor(R.color.white));
				if (position % 2 == 0) {
					convertView
							.setBackgroundResource(R.color.nmk3_latest_lottery_list_one);//0x157800
				} else {
					convertView
							.setBackgroundResource(R.color.nmk3_latest_lottery_list_two);//0x126800
				}
			} else {
				if (Constants.LOTNO_CQ_ELVEN_FIVE.equals(lotno)) {
					holder.issue.setTextColor(getResources().getColor(
							R.color.cq_11_5_text_color));
					holder.winningNumber.setTextColor(getResources().getColor(
							R.color.cq_11_5_text_color));
				}
				if (position % 2 == 0) {
					convertView
							.setBackgroundResource(R.color.latest_lottery_list_one);
				} else {
					convertView
							.setBackgroundResource(R.color.latest_lottery_list_two);
				}
			}

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView issue;
		public TextView winningNumber;
	}
}

class LatestLotteryInfo {
	private String batchCode;
	private String winCode;

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getWinCode() {
		return winCode;
	}

	public void setWinCode(String winCode) {
		this.winCode = winCode;
	}
}
