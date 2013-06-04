package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NoticePrizesOfLottery extends Activity implements MyDialogListener {

	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String ICON = "ICON";/* 图标 */
	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";

	public final static String[] mainLotteryTitle = { "双色球", "福彩3D", "七乐彩" };
	public final static String[] mainAnnoucementDateAndTime = {
			"开奖:2010-06-01 20:00", "开奖:2010-06-01 20:00", "开奖:2010-06-01 20:00" };
	public final static String[] mainWinningNumName = { "中奖号码:", "中奖号码:",
			"中奖号码:" };
	public final static String[] mainWinningNum = { "01,02,03,04,05,06,07",
			"01,02,03,04,05,06,07", "01,02,03,04,05,06,07" };
	public final static String[] mainLotteryIssue = { "第2010026期", "第2010026期",
			"第2010026期" };

	private static final String[] shuangseqiu_LotteryIssue = { "第2010026期",
			"第2010025期", "第2010024期", "第201023期" };
	private static final String[] shuangseqiu_NoticeDateAndTime = {
			"2010-06-01", "2010-06-01", "2010-06-01", "2010-06-01" };
	private static final String[] shuangseqiu_WinningNum = {
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07",
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07" };
	private static final String[] shuangseqiu_FinalPrizesDate = {
			"结奖日期：2010-08-01", "结奖日期：2010-08-01", "结奖日期：2010-08-01",
			"结奖日期：2010-08-01" };
	private static final String[] shuangseqiu_TotalSum = { "总金额：100万元",
			"总金额：100万元", "总金额：100万元", "总金额：100万元" };

	private static final String[] fucai_LotteryIssue = { "第2010026期",
			"第2010025期", "第2010024期", "第2010023期" };
	private static final String[] fucai_NoticeDateAndTime = { "2010-06-01",
			"2010-06-01", "2010-06-01", "2010-06-01" };
	private static final String[] fucai_WinningNum = {
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07",
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07" };
	private static final String[] fucai_FinalPrizesDate = { "结奖日期：2010-08-01",
			"结奖日期：2010-08-01", "结奖日期：2010-08-01", "结奖日期：2010-08-01" };
	private static final String[] fucai_TotalSum = { "总金额：100万元", "总金额：100万元",
			"总金额：100万元", "总金额：100万元" };

	private static final String[] qilecai_LotteryIssue = { "第2010026期",
			"第2010025期", "第2010024期", "第201023期" };
	private static final String[] qilecai_NoticeDateAndTime = { "2010-06-01",
			"2010-06-01", "2010-06-01", "2010-06-01" };
	private static final String[] qilecai_WinningNum = {
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07",
			"中奖号码：01,02,03,04,05,06,07", "中奖号码：01,02,03,04,05,06,07" };
	private static final String[] qilecai_FinalPrizesDate = {
			"结奖日期：2010-08-01", "结奖日期：2010-08-01", "结奖日期：2010-08-01",
			"结奖日期：2010-08-01" };
	private static final String[] qilecai_TotalSum = { "总金额：100万元",
			"总金额：100万元", "总金额：100万元", "总金额：100万元" };

	public final static int ID_MAINLISTVIEW = 1;
	public final static int ID_SUB_SHUANGSEQIU_LISTVIEW = 2;
	public final static int ID_SUB_FUCAI3D_LISTVIEW = 3;
	public final static int ID_SUB_QILECAI_LISTVIEW = 4;

	int redBallViewNum = 7;
	int redBallViewWidth = 22;

	public static final int BALL_WIDTH = 28;

	TextView text_lotteryName; // 彩票种类的TextView
	List<Map<String, Object>> list, list_ssq, list_fc3d, list_qlc;

	static BallTable kaiJiangGongGaoBallTable = null;
	private static int iScreenWidth;
	static LinearLayout iV;
	static String strChuanZhi;

	private static int[] aRedColorResId = { R.drawable.red28 };
	private static int[] aBlueColorResId = { R.drawable.blue28 };
	// 周黎鸣 7.5 代码修改：添加代码
	private boolean iQuitFlag = true;

	// private boolean iCallOnKeyDownFlag=false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.notice_prizes_main);

		showListView(ID_MAINLISTVIEW);

	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	private void showListView(int listViewID) {
		iQuitFlag = false; // 周黎鸣 7.5 代码修改：添加退出检测
		switch (listViewID) {
		case ID_MAINLISTVIEW:
			// iCallOnKeyDownFlag=false; //周黎鸣 7.5 代码修改：添加退出检测
			iQuitFlag = true;
			showMainListView();
			break;
		case ID_SUB_SHUANGSEQIU_LISTVIEW:
			// iCallOnKeyDownFlag=false; //周黎鸣 7.5 代码修改：添加退出检测
			iQuitFlag = false;
			showSubShuangSeQiuListView();
			break;
		case ID_SUB_FUCAI3D_LISTVIEW:
			// iCallOnKeyDownFlag=false;
			iQuitFlag = false; // 周黎鸣 7.5 代码修改：添加退出检测
			showSubFuCai3DListView();
			break;
		case ID_SUB_QILECAI_LISTVIEW:
			// iCallOnKeyDownFlag=false;
			iQuitFlag = false; // 周黎鸣 7.5 代码修改：添加退出检测
			showSubQiLeCaiListView();
			break;
		}
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
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
			if (iQuitFlag == false) {
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			} else {
				WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(this,
						this);
				iQuitDialog.show();
			}
			break;
		}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 取消
	 */
	public void onCancelClick() {
		// TODO Auto-generated method stub
		// //iCallOnKeyDownFlag=false;
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 确定
	 */
	public void onOkClick(int[] nums) {
		// TODO Auto-generated method stub
		// 退出
		this.finish();
	}

	/**
	 * 主列表
	 */
	private void showMainListView() {
		setContentView(R.layout.notice_prizes_main);
		// iQuitFlag=true;
		ListView listview = (ListView) findViewById(R.id.notice_prizes_listview);

		list = getListForMainListViewSimpleAdapter();

		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE };
		// int[] id = new int[]{R.id.icon ,R.id.notice_prezes_lotteryNameId
		// ,R.id.ballview01 ,R.id.notice_prizes_dateAndTimeId
		// ,R.id.notice_prizes_issueId};

		// SimpleAdapter adapter = new SimpleAdapter(this, list,
		// R.layout.notice_prizes_main_layout, str , id);
		MainEfficientAdapter adapter = new MainEfficientAdapter(this, str, list);
		listview.setAdapter(adapter);

		// 设置点击监听
		OnItemClickListener clickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				text_lotteryName = (TextView) view
						.findViewById(R.id.notice_prezes_lotteryNameId);
				String str = text_lotteryName.getText().toString();
				// 点击福彩3D跳转到福彩3D子列表中
				if (getString(R.string.fucai3d).equals(str)) {
					showListView(ID_SUB_FUCAI3D_LISTVIEW);
				}
				// 点击七乐彩跳转到七乐彩子列表中
				if (getString(R.string.qilecai).equals(str)) {
					showListView(ID_SUB_QILECAI_LISTVIEW);
				}
				// 点击双色球跳转到双色球子列表中
				if (getString(R.string.shuangseqiu).equals(str)) {
					showListView(ID_SUB_SHUANGSEQIU_LISTVIEW);
				}
			}

		};

		listview.setOnItemClickListener(clickListener);
	}

	/**
	 * 双色球子列表
	 */
	private void showSubShuangSeQiuListView() {
		String[] ssqInfo_issue1, ssqInfo_issue2, ssqInfo_issue3, ssqInfo_issue4;
		ssqInfo_issue1 = getLotteryInfo("information1", 0);
		ssqInfo_issue2 = getLotteryInfo("information1", 1);
		ssqInfo_issue3 = getLotteryInfo("information1", 2);
		PublicMethod.myOutput("--------------ssqInfo_issue3:" + ""
				+ ssqInfo_issue3[2]);
		ssqInfo_issue4 = getLotteryInfo("information1", 3);
		PublicMethod.myOutput("--------------ssqInfo_issue4:" + ""
				+ ssqInfo_issue4[2]);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		// iQuitFlag=false;
		list_ssq = getSubInfoForListView("ssq", ssqInfo_issue1, ssqInfo_issue2,
				ssqInfo_issue3, ssqInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };
		// 设置双色球标题：双色球开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.shuangseqiu_kaijianggonggao);

		// 添加提示信息
		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.shuangseqiu_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回主列表
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_ssq);
		listview.setAdapter(adapter);

	}

	/**
	 * 福彩3D子列表
	 */
	private void showSubFuCai3DListView() {
		String[] fc3dInfo_issue1, fc3dInfo_issue2, fc3dInfo_issue3, fc3dInfo_issue4;
		fc3dInfo_issue1 = getLotteryInfo("information2", 0);
		fc3dInfo_issue2 = getLotteryInfo("information2", 1);
		fc3dInfo_issue3 = getLotteryInfo("information2", 2);
		fc3dInfo_issue4 = getLotteryInfo("information2", 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		// iQuitFlag=false;
		list_fc3d = getSubInfoForListView("fc3d", fc3dInfo_issue1,
				fc3dInfo_issue2, fc3dInfo_issue3, fc3dInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		// 设置福彩3D标题：福彩3D开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.fucai3d_kaijianggonggao);

		// 添加提示信息
		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.fucai3d_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回主列表
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_fc3d);
		listview.setAdapter(adapter);
	}

	/**
	 * 七乐彩子列表
	 */
	private void showSubQiLeCaiListView() {
		String[] qlcInfo_issue1, qlcInfo_issue2, qlcInfo_issue3, qlcInfo_issue4;
		qlcInfo_issue1 = getLotteryInfo("information3", 0);
		qlcInfo_issue2 = getLotteryInfo("information3", 1);
		qlcInfo_issue3 = getLotteryInfo("information3", 2);
		qlcInfo_issue4 = getLotteryInfo("information3", 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		iQuitFlag = false;
		list_qlc = getSubInfoForListView("qlc", qlcInfo_issue1, qlcInfo_issue2,
				qlcInfo_issue3, qlcInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.qilecai_kaijianggonggao);

		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.qilecai_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_qlc);
		listview.setAdapter(adapter);
	}

	/**
	 * 主列表适配器
	 */
	public static class MainEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;

		public MainEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			String iIssueNo = (String) mList.get(position).get(mIndex[3]);

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.notice_prizes_main_layout, null);

				holder = new ViewHolder();
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView
						.findViewById(R.id.notice_prezes_lotteryNameId);
				holder.numbers = (LinearLayout) convertView
						.findViewById(R.id.ball_linearlayout);

				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_dateAndTimeId);
				holder.date.setText(iDate);
				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_issueId);
				holder.issue.setText(iIssueNo);
				if (iGameType.equalsIgnoreCase("ssq")) {
					holder.name.setText("双色球");
					holder.icon.setImageResource(R.drawable.shuangseqiu);
//zlm 7.28 代码修改：添加号码排序	
					int i1,i2,i3;
					String iShowNumber;
					OneBallView tempBallView;
					int[] ssqInt01 = new int[6];
					int[] ssqInt02 = new int[6];
					String[] ssqStr = new String[6];
					
					for(i2 = 0 ; i2<6 ; i2++){
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"+ssqInt01[i2]);
					}
					
					ssqInt02 = sort(ssqInt01);
					
					for(i3 = 0 ; i3 <6 ; i3++){
						if(ssqInt02[i3]<10){
							ssqStr[i3] = "0"+ssqInt02[i3];
						} else {
							ssqStr[i3] = ""+ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 6; i1++) {
						//iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}

					iShowNumber = iNumbers.substring(12, 14);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				} else if (iGameType.equalsIgnoreCase("fc3d")) {
					holder.name.setText("福彩3D");
					holder.icon.setImageResource(R.drawable.fucai);
					int i1;
//zlm 7.30 代码修改：修改福彩3D号码
					int iShowNumber;
					OneBallView tempBallView;
					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2, i1 * 2 + 2));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								""+iShowNumber, aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}
				} else {
					holder.name.setText("七乐彩");
					holder.icon.setImageResource(R.drawable.qilecai);
//zlm 7.28 代码修改：添加号码排序		
					int i1,i2,i3;
					String iShowNumber;
					OneBallView tempBallView;
					
					int[] ssqInt01 = new int[7];
					int[] ssqInt02 = new int[7];
					String[] ssqStr = new String[7];
					
					for(i2 = 0 ; i2<7 ; i2++){
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"+ssqInt01[i2]);
					}
					
					ssqInt02 = sort(ssqInt01);
					
					for(i3 = 0 ; i3 <7 ; i3++){
						if(ssqInt02[i3]<10){
							ssqStr[i3] = "0"+ssqInt02[i3];
						} else {
							ssqStr[i3] = ""+ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 7; i1++) {
						//iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}
//zlm 8.3 代码修改 ：添加七乐彩蓝球
					iShowNumber = iNumbers.substring(14, 16);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView name;
			LinearLayout numbers;
			TextView date;
			TextView issue;
		}
	}

	/**
	 * 子列表适配器
	 */
	public static class SubEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;

		public SubEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			PublicMethod.myOutput("------------mList.size()!" + mList.size());
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String iGameType = (String) mList.get(position).get(mIndex[0]);
			String iNumbers = (String) mList.get(position).get(mIndex[1]);
			String iDate = (String) mList.get(position).get(mIndex[2]);
			PublicMethod.myOutput("--------------iDate!" + iDate
					+ "-------------");
			String iIssueNo = (String) mList.get(position).get(mIndex[3]);
			PublicMethod.myOutput("--------------iIssueNo!" + iIssueNo
					+ "-------------");
//			String iFinalDate = (String) mList.get(position).get(mIndex[4]);
//			String iMoneySum = (String) mList.get(position).get(mIndex[5]);

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.notice_prizes_single_specific_layout, null);

				holder = new ViewHolder();
				holder.numbers = (LinearLayout) convertView
						.findViewById(R.id.notice_pirzes_single_specific_ball_linearlayout);

				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.date.setText(iDate);
				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.issue.setText(iIssueNo);
//				holder.finalDate = (TextView) convertView
//						.findViewById(R.id.notice_prizes_single_specific_finalPrizesDate_id);
//				holder.finalDate.setText(iFinalDate);
//				holder.moneySum = (TextView) convertView
//						.findViewById(R.id.notice_prizes_single_specific_totalSum_id);
//				holder.moneySum.setText(iMoneySum);
				if (iGameType.equalsIgnoreCase("ssq")) {
//zlm 7.28 代码修改：添加号码排序
					int i1,i2,i3;
					String iShowNumber;
					OneBallView tempBallView;
					
					int[] ssqInt01 = new int[6];
					int[] ssqInt02 = new int[6];
					String[] ssqStr = new String[6];
					
					for(i2 = 0 ; i2<6 ; i2++){
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"+ssqInt01[i2]);
					}
					
					ssqInt02 = sort(ssqInt01);
					
					for(i3 = 0 ; i3 <6 ; i3++){
						if(ssqInt02[i3]<10){
							ssqStr[i3] = "0"+ssqInt02[i3];
						} else {
							ssqStr[i3] = ""+ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 6; i1++) {
						//iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}

					iShowNumber = iNumbers.substring(12, 14);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				} else if (iGameType.equalsIgnoreCase("fc3d")) {

					int i1;
//zlm 7.30 代码修改：修改福彩3D号码
					int iShowNumber;
					OneBallView tempBallView;
					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2, i1 * 2 + 2));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								""+iShowNumber, aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}
				} else if (iGameType.equalsIgnoreCase("qlc")) {
//zlm 7.28 代码修改：添加号码排序	
					int i1,i2,i3;
					String iShowNumber;
					OneBallView tempBallView;
				
					int[] ssqInt01 = new int[7];
					int[] ssqInt02 = new int[7];
					String[] ssqStr = new String[7];
					
					for(i2 = 0 ; i2<7 ; i2++){
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"+ssqInt01[i2]);
					}
					
					ssqInt02 = sort(ssqInt01);
					
					for(i3 = 0 ; i3 <7 ; i3++){
						if(ssqInt02[i3]<10){
							ssqStr[i3] = "0"+ssqInt02[i3];
						} else {
							ssqStr[i3] = ""+ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 7; i1++) {
						//iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						holder.numbers.addView(tempBallView, lp);
					}
//zlm 8.3 代码修改 ：添加七乐彩蓝球
					iShowNumber = iNumbers.substring(14, 16);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					holder.numbers.addView(tempBallView, lp);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		static class ViewHolder {
			LinearLayout numbers;
			TextView date;
			TextView issue;
//			TextView finalDate;
//			TextView moneySum;

		}
	}

	/**
	 * 主列表中相应的数据
	 */
	private List<Map<String, Object>> getListForMainListViewSimpleAdapter() {
		String[] jieshou_ssq, jieshou_fc3d, jieshou_qlc;
		jieshou_ssq = getLotteryInfo("information1", 0);
		jieshou_fc3d = getLotteryInfo("information2", 0);
		jieshou_qlc = getLotteryInfo("information3", 0);

		String iGameName[] = { "ssq", "fc3d", "qlc" };

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(4);
		Map<String, Object> map = new HashMap<String, Object>();
		// String iLayoutType ;

		// zlm 7.16 代码修改：添加开奖日期
		map.put(LOTTERYTYPE, iGameName[0]);
		map.put(WINNINGNUM, jieshou_ssq[0] + jieshou_ssq[1]);
		map.put(DATE, "开奖日期： " + jieshou_ssq[3]);
		map.put(ISSUE, "第" + jieshou_ssq[2] + "期");
		list.add(map);

		// zlm 7.16 代码修改：添加开奖日期
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[1]);
		map.put(WINNINGNUM, jieshou_fc3d[0] + jieshou_fc3d[1]);
		map.put(DATE, "开奖日期： " + jieshou_fc3d[3]);
		map.put(ISSUE, "第" + jieshou_fc3d[2] + "期");
		list.add(map);

		// zlm 7.16 代码修改：添加开奖日期
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[2]);
		map.put(WINNINGNUM, jieshou_qlc[0] + jieshou_qlc[1]);
		map.put(DATE, "开奖日期： " + jieshou_qlc[3]);
		map.put(ISSUE, "第" + jieshou_qlc[2] + "期");
		list.add(map);

		return list;
	}

	/**
	 * 子列表中相应的数据
	 */
	private List<Map<String, Object>> getSubInfoForListView(String iGameName,
			String[] getInfo1, String[] getInfo2, String[] getInfo3,
			String[] getInfo4) {
		PublicMethod.myOutput("--------------------getInfo3!" + getInfo3[2]);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(6);
		Map<String, Object> map = new HashMap<String, Object>();
		// String iLayoutType ;

		// zlm 7.16 代码修改：添加开奖时间、结奖时间、金额
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo1[0] + getInfo1[1]);
		map.put(DATE, "开奖日期： " + getInfo1[3]);
		map.put(ISSUE, "第" + getInfo1[2] + "期");
//		map.put(FINALDATE, "结奖日期： " + getInfo1[4]);
//		map.put(MONEYSUM, "总金额： " + getInfo1[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo2[0] + getInfo2[1]);
		map.put(DATE, "开奖日期： " + getInfo2[3]);
		map.put(ISSUE, "第" + getInfo2[2] + "期");
//		map.put(FINALDATE, "结奖日期： " + getInfo2[4]);
//		map.put(MONEYSUM, "总金额： " + getInfo2[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo3[0] + getInfo3[1]);
		map.put(DATE, "开奖日期： " + getInfo3[3]);
		map.put(ISSUE, "第" + getInfo3[2] + "期");
//		map.put(FINALDATE, "结奖日期： " + getInfo3[4]);
//		map.put(MONEYSUM, "总金额： " + getInfo3[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo4[0] + getInfo4[1]);
		map.put(DATE, "开奖日期： " + getInfo4[3]);
		map.put(ISSUE, "第" + getInfo4[2] + "期");
//		map.put(FINALDATE, "结奖日期： " + getInfo4[4]);
//		map.put(MONEYSUM, "总金额： " + getInfo4[5] + " 元");
		list.add(map);

		return list;
	}

	/**
	 * 从网络上获取数据
	 * 
	 * @param string
	 *            键值对Key
	 * @param index
	 *            编号
	 * @return
	 */
	public String[] getLotteryInfo(String string, int index) {
		// ShellRWSharesPreferences shellRW;
		String error_code = "00";
		String win_base_code = "";
		String term_code = "";
		String win_special_code = "";
		String beginTime = "";
		String endTime = "";
		String totalMoney = "";
		int iretrytimes = 2;
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String lottery_ssq = shellRW.getUserLoginInfo(string);
		if (lottery_ssq.equalsIgnoreCase("") || lottery_ssq == null||lottery_ssq.equalsIgnoreCase("ssqinfo")
				||lottery_ssq.equalsIgnoreCase("dddinfo")||lottery_ssq.equalsIgnoreCase("qlcinfo")) {
			if (string.equalsIgnoreCase("information1")) {
				win_base_code = "000000000000";
				term_code = "0000";
				win_special_code = "00";
				beginTime = "";
				endTime = "";
				totalMoney = "";
			}
			if (string.equalsIgnoreCase("information2")) {
				win_base_code = "000000";
				term_code = "0000";
				win_special_code = "00";
				beginTime = "";
				endTime = "";
				totalMoney = "";
			}
			if (string.equalsIgnoreCase("information3")) {
				win_base_code = "00000000000000";
				term_code = "0000";
				win_special_code = "00";
				beginTime = "";
				endTime = "";
				totalMoney = "";
			}
		} else {
			while (iretrytimes < 3 && iretrytimes > 0) {
				try {
					try {
						JSONObject obj = new JSONObject(lottery_ssq);
						error_code = obj.getString("error_code");
						if (error_code.equals("000000")) {
							win_base_code = obj.getString("win_base_code");// 获取中奖号码
							term_code = obj.getString("term_code");// 获取期号
							win_special_code = obj
									.getString("win_special_code");// 获取蓝球号码
							// 新加的信息 陈晨20100716
							beginTime = obj.getString("Begin_time");
							endTime = obj.getString("End_time");
							totalMoney = PublicMethod.changeMoney(obj
									.getString("act_sell_amount"));
						}
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(lottery_ssq);
						JSONObject obj = jsonObject3.getJSONObject(index);
						error_code = obj.getString("error_code");
						if (error_code.equals("000000")) {
							win_base_code = obj.getString("win_base_code");
							win_special_code = obj
									.getString("win_special_code");//
							term_code = obj.getString("term_code");

							// 新加的信息 陈晨20100716
							beginTime = obj.getString("Begin_time");
							endTime = obj.getString("End_time");
							totalMoney = PublicMethod.changeMoney(obj
									.getString("act_sell_amount"));
						} else {
							win_base_code = "000000";
							term_code = "0000";
							win_special_code = "00";
							beginTime = "";
							endTime = "";
							totalMoney = "";
						}
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					e.printStackTrace();
					iretrytimes--;
				}
			}
		}
		String[] ssq_wininfo = { win_base_code, win_special_code, term_code,
				beginTime, endTime, totalMoney };
		return ssq_wininfo;
	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 * @return
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;
		int temp;
		for (int i = 0; i < t_s.length; i++) {
			PublicMethod.myOutput("----------------------t_s0000" + i + "----"
					+ t_s[i]);
		}
		// int t_a = 0;
		for (int i = 0; i < t_s.length; i++) {
			for (int j = i + 1; j < t_s.length; j++) {
				if (t_s[i] > t_s[j]) {
					temp = t_s[i];
					t_s[i] = t_s[j];
					t_s[j] = temp;
				}
			}
		}
		for (int i = 0; i < t_s.length; i++) {
			PublicMethod.myOutput("----------------------t_s" + i + t_s[i]);
		}
		return t_s;
	}
}
