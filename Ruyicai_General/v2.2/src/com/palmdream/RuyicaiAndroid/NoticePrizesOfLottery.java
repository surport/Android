package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.netintface.Jsoninformation;

public class NoticePrizesOfLottery extends Activity implements MyDialogListener {

	public final static String LOTTERYTYPE = "LOTTERYTYPE";
	public final static String ICON = "ICON";/* 图标 */
	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String WINNINGNUM = "WINNINGNUM";
	public final static String DATE = "DATA";
	public final static String ISSUE = "ISSUE";
	public final static String FINALDATE = "FINALDATE";
	public final static String MONEYSUM = "MONEYSUM";
	public final static int c1 = 0x64FFC78E;// fcebc7//FFC78E
	public final static int c2 = 0x64fcebc7;//FFFF6F;
	public final static int a = R.drawable.kaijiang_lotterytype;
	public static final int[] drawables = new int[] { a, 0, 0, 0, a, 0, 0, 0,
			a, 0, 0, 0, 0 };
	public final static String[] mainLotteryTitle = { "双色球", "福彩3D", "七乐彩" };
	public final static String[] mainAnnoucementDateAndTime = {
			"开奖:2010-06-01 20:00", "开奖:2010-06-01 20:00", "开奖:2010-06-01 20:00" };
	public final static String[] mainWinningNumName = { "中奖号码:", "中奖号码:",
			"中奖号码:" };
	public final static String[] mainWinningNum = { "01,02,03,04,05,06,07",
			"01,02,03,04,05,06,07", "01,02,03,04,05,06,07" };
	public final static String[] mainLotteryIssue = { "第2010026期", "第2010026期",
			"第2010026期" };

	public final static int ID_MAINLISTVIEW = 1;
	public final static int ID_SUB_SHUANGSEQIU_LISTVIEW = 2;
	public final static int ID_SUB_FUCAI3D_LISTVIEW = 3;
	public final static int ID_SUB_QILECAI_LISTVIEW = 4;
	public final static int ID_SUB_PAILIESAN_LISTVIEW = 5; // zlm 8.9 代码添加：排列三
	public final static int ID_SUB_CHAOJIDALETOU_LISTVIEW = 6; // zlm 8.9
	public final static int ID_SUB_SHISHICAI_LISTVIEW = 7; // zlm 8.9
	public final static int ID_SUB_SFC_LISTVIEW = 8; // zlm 8.9
	public final static int ID_SUB_RXJ_LISTVIEW = 9; // zlm 8.9
	public final static int ID_SUB_LCB_LISTVIEW = 10; // zlm 8.9
	public final static int ID_SUB_JQC_LISTVIEW = 11; // zlm 8.9

	// 代码添加：超级大乐透

	int redBallViewNum = 7;
	int redBallViewWidth = 22;

	public static final int BALL_WIDTH = 28;

	TextView text_lotteryName; // 彩票种类的TextView
	List<Map<String, Object>> list, list_ssq, list_fc3d, list_qlc, list_pl3,
			list_cjdlt; // zlm 8.9 添加排列三、超级大乐透

	static BallTable kaiJiangGongGaoBallTable = null;

	static LinearLayout iV;
	static String strChuanZhi;

	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };
	// 周黎鸣 7.5 代码修改：添加代码
	private boolean iQuitFlag = true;
	// 进度条
	private ProgressDialog progressdialog;
	private static final int DIALOG1_KEY = 0;
	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				progressdialog.dismiss();
				showListView(ID_MAINLISTVIEW);
				break;

			}

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_prizes_main);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						NoticePrizesOfLottery.this, "addInfo");
				String sscinfo = Jsoninformation.sscinfo();
				String sfcinfo = Jsoninformation.zcinfo("T01003");
				String jqcinfo = Jsoninformation.zcinfo("T01005");
				String lcbinfo = Jsoninformation.zcinfo("T01006");
				String rxjinfo = Jsoninformation.zcinfo("T01004");
				shellRW.setUserLoginInfo("informationssc", sscinfo);
				shellRW.setUserLoginInfo("informationsfc", sfcinfo);
				shellRW.setUserLoginInfo("informationjqc", jqcinfo);
				shellRW.setUserLoginInfo("informationlcb", lcbinfo);
				shellRW.setUserLoginInfo("informationrxj", rxjinfo);
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}).start();
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
		case ID_SUB_PAILIESAN_LISTVIEW:
			iQuitFlag = false;
			showSubPaiLieSanListView(); // zlm 排列三
			break;
		case ID_SUB_CHAOJIDALETOU_LISTVIEW:
			iQuitFlag = false;
			showSubChaoJiDaLeTouListView(); // zlm 超级大乐透
			break;
		case ID_SUB_SHISHICAI_LISTVIEW:
			iQuitFlag = false;
			showSubShiShiCaiListView(); // zlm 时时彩
			break;
		case ID_SUB_SFC_LISTVIEW:
			iQuitFlag = false;
			showSubZuCaiListView("sfc"); // zlm 胜负彩
			break;
		case ID_SUB_RXJ_LISTVIEW:
			iQuitFlag = false;
			showSubZuCaiListView("rxj");// zlm 任选九
			break;
		case ID_SUB_LCB_LISTVIEW:
			iQuitFlag = false;
			showSubZuCaiListView("lcb"); // zlm 六场半
			break;
		case ID_SUB_JQC_LISTVIEW:
			iQuitFlag = false;
			showSubZuCaiListView("jqc"); // zlm 进球彩
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
				ScrollableTabActivity.visible();// 显示顶部标签
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

		ListView listview = (ListView) findViewById(R.id.notice_prizes_listview);

		list = getListForMainListViewSimpleAdapter();

		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE };

		MainEfficientAdapter adapter = new MainEfficientAdapter(this, str, list);
		//PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
		Drawable drawable = getResources().getDrawable(
				R.drawable.list_selector_red);
		listview.setSelector(drawable);

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
				// 点击排列三跳转到排列三子列表中
				if (getString(R.string.pailiesan).equals(str)) {
					showListView(ID_SUB_PAILIESAN_LISTVIEW);
				}
				// 点击超级大乐透跳转到超级大乐透子列表中
				if (getString(R.string.chaojidaletou).equals(str)) {
					showListView(ID_SUB_CHAOJIDALETOU_LISTVIEW);
				}
				// 点击时时彩跳转到时时彩子列表中
				if (getString(R.string.shishicai).equals(str)) {
					showListView(ID_SUB_SHISHICAI_LISTVIEW);
				}
				// 点击时时彩跳转到胜负彩子列表中
				if (getString(R.string.shengfucai).equals(str)) {
					showListView(ID_SUB_SFC_LISTVIEW);
				}
				// 点击时时彩跳转到任选九彩子列表中
				if (getString(R.string.renxuanjiu).equals(str)) {
					showListView(ID_SUB_RXJ_LISTVIEW);
				}
				// 点击时时彩跳转到六场半子列表中
				if (getString(R.string.liuchangban).equals(str)) {
					showListView(ID_SUB_LCB_LISTVIEW);
				}
				// 点击时时彩跳转到进球彩子列表中
				if (getString(R.string.jinqiucai).equals(str)) {
					showListView(ID_SUB_JQC_LISTVIEW);
				}
			}

		};

		listview.setOnItemClickListener(clickListener);

	}

	/**
	 * 双色球子列表eiw
	 */
	private void showSubShuangSeQiuListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签
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
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_ssq);
		//PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
        PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 福彩3D子列表
	 */
	private void showSubFuCai3DListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签
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
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_fc3d);
		//PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}   

	/**
	 * 七乐彩子列表
	 */
	private void showSubQiLeCaiListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签
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
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_qlc);
		//PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
		
	}

	/**
	 * 排列三子列表
	 */
	private void showSubPaiLieSanListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签
		// String[] fc3dInfo_issue1, fc3dInfo_issue2, fc3dInfo_issue3,
		// fc3dInfo_issue4;
		String[] pl3Info_issue1 = getAddLotteryInfo("information8", 0);
		String[] pl3Info_issue2 = getAddLotteryInfo("information8", 1);// getLotteryInfo("information2",
		// 1);
		String[] pl3Info_issue3 = getAddLotteryInfo("information8", 2);// getLotteryInfo("information2",
		// 2);
		String[] pl3Info_issue4 = getAddLotteryInfo("information8", 3);// getLotteryInfo("information2",
		// 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		// iQuitFlag=false;
		list_pl3 = getSubInfoForListView("pl3", pl3Info_issue1, pl3Info_issue2,
				pl3Info_issue3, pl3Info_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		// 设置福彩3D标题：福彩3D开奖公告
		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.pailiesan_kaijianggonggao);

		// 添加提示信息
		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.pailiesan_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回主列表
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_pl3);
	//	PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 超级大乐透子列表
	 */
	private void showSubChaoJiDaLeTouListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签

		String[] cjdltInfo_issue1 = getAddLotteryInfo("information7", 0);// getLotteryInfo("information3",
		// 0);
		String[] cjdltInfo_issue2 = getAddLotteryInfo("information7", 1);// getLotteryInfo("information3",
		// 1);
		String[] cjdltInfo_issue3 = getAddLotteryInfo("information7", 2);// getLotteryInfo("information3",
		// 2);
		String[] cjdltInfo_issue4 = getAddLotteryInfo("information7", 3);// getLotteryInfo("information3",
		// 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		iQuitFlag = false;
		list_cjdlt = getSubInfoForListView("cjdlt", cjdltInfo_issue1,
				cjdltInfo_issue2, cjdltInfo_issue3, cjdltInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.chaojidaletou_kaijianggonggao);

		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.chaojidaletou_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_cjdlt);
	//	PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 时时彩子列表
	 */
	private void showSubShiShiCaiListView() {
		ScrollableTabActivity.gone();// 隐藏顶部标签
		// String[] qlcInfo_issue1, qlcInfo_issue2, qlcInfo_issue3,
		// qlcInfo_issue4;
		String[] cjdltInfo_issue1 = getAddLotteryInfo("informationssc", 0);// getLotteryInfo("information3",
		// 0);
		String[] cjdltInfo_issue2 = getAddLotteryInfo("informationssc", 1);// getLotteryInfo("information3",
		// 1);
		String[] cjdltInfo_issue3 = getAddLotteryInfo("informationssc", 2);// getLotteryInfo("information3",
		// 2);
		String[] cjdltInfo_issue4 = getAddLotteryInfo("informationssc", 3);// getLotteryInfo("information3",
		// 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		iQuitFlag = false;
		list_cjdlt = getSubInfoForListView("ssc", cjdltInfo_issue1,
				cjdltInfo_issue2, cjdltInfo_issue3, cjdltInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		noticePrizesTitle.setText(R.string.shishicai_kaijianggonggao);

		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		attention.setText(R.string.shishicai_attention);

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_cjdlt);
		//PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	/**
	 * 足彩子列表
	 */
	private void showSubZuCaiListView(String type) {
		ScrollableTabActivity.gone();// 隐藏顶部标签

		String[] cjdltInfo_issue1 = getAddLotteryInfo("information" + type, 0);// getLotteryInfo("information3",
		// 0);
		String[] cjdltInfo_issue2 = getAddLotteryInfo("information" + type, 1);// getLotteryInfo("information3",
		// 1);
		String[] cjdltInfo_issue3 = getAddLotteryInfo("information" + type, 2);// getLotteryInfo("information3",
		// 2);
		String[] cjdltInfo_issue4 = getAddLotteryInfo("information" + type, 3);// getLotteryInfo("information3",
		// 3);

		TextView noticePrizesTitle;
		TextView attention;

		setContentView(R.layout.notice_prizes_single_specific_main);
		iQuitFlag = false;
		list_cjdlt = getSubInfoForListView(type, cjdltInfo_issue1,
				cjdltInfo_issue2, cjdltInfo_issue3, cjdltInfo_issue4);
		String[] str = new String[] { LOTTERYTYPE, WINNINGNUM, DATE, ISSUE,
				FINALDATE, MONEYSUM };

		noticePrizesTitle = (TextView) findViewById(R.id.notice_prizes_single_specific_title_id);
		// noticePrizesTitle.setText(R.string.shishicai_kaijianggonggao);

		attention = (TextView) findViewById(R.id.notice_prizes_single_specific_attention);
		if (type.equals("sfc")) {
			attention.setText(R.string.shengfucai);
			noticePrizesTitle.setText(R.string.shengfucai_kaijiang);
		} else if (type.equals("rxj")) {
			attention.setText(R.string.renxuanjiu);
			noticePrizesTitle.setText(R.string.renxuanjiu_kaijiang);
		} else if (type.equals("lcb")) {
			attention.setText(R.string.liuchangban);
			noticePrizesTitle.setText(R.string.liuchangban_kaijiang);
		} else if (type.equals("jqc")) {
			attention.setText(R.string.jinqiucai);
			noticePrizesTitle.setText(R.string.jinqiucai_kaijiang);
		}

		ListView listview = (ListView) findViewById(R.id.notice_prizes_single_specific_listview);

		// 返回
		ImageButton reBtn;
		reBtn = (ImageButton) findViewById(R.id.notice_prizes_single_specific_main_returnID);
		reBtn.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ScrollableTabActivity.visible();// 显示顶部标签
				setContentView(R.layout.notice_prizes_main);
				showListView(ID_MAINLISTVIEW);
			}

		});

		SubEfficientAdapter adapter = new SubEfficientAdapter(this, str,
				list_cjdlt);
		ViewGroup.LayoutParams params;
		params = listview.getLayoutParams();
		params.height = 450;
		listview.setLayoutParams(params);
	//	PublicMethod.addHeight(this, listview);
		listview.setAdapter(adapter);
		PublicMethod.setmydividerHeight(listview);
	}

	// 主列表适配器
	public static class MainEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		private String[] mIndex;
		private Context context;

		public MainEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
			mIndex = index;
			this.context = context;
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
				holder.numbers = (TableLayout) convertView
						.findViewById(R.id.ball_linearlayout);
				holder.numbers.setStretchAllColumns(true);
				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_dateAndTimeId);

				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_issueId);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.numbers.removeAllViews();

			}
			int[] a = { 0, 0, 0, 0 };
			if (iGameType.equalsIgnoreCase("fucai")) {
				holder.name.setText(Html.fromHtml("<B>" + "福彩" + "</B>"));
				holder.name.setGravity(0);
				holder.name.setPadding(a[0], a[1], a[2], a[3]);
				// holder.name.layout(0, 0, 0, 0);
				holder.icon.setImageBitmap(null);
				holder.icon.setImageMatrix(null);
				holder.date.setVisibility(ImageView.GONE);
				holder.date.setText("");
				holder.date.setVisibility(TextView.GONE);
				holder.issue.setText("");
				holder.issue.setVisibility(TextView.GONE);
			} else if (iGameType.equalsIgnoreCase("ticai")) {
				holder.name.setText(Html.fromHtml("<B>" + "体彩" + "</B>"));
				holder.name.setGravity(0);
				holder.name.setPadding(a[0], a[1], a[2], a[3]);
				// holder.name.layout(0, 0, 0, 0);
				holder.icon.setImageBitmap(null);
				holder.icon.setImageMatrix(null);
				holder.date.setVisibility(ImageView.GONE);
				holder.date.setText("");
				holder.date.setVisibility(TextView.GONE);
				holder.issue.setText("");
				holder.issue.setVisibility(TextView.GONE);
			} else if (iGameType.equalsIgnoreCase("zucai")) {
				holder.name.setText(Html.fromHtml("<B>" + "足彩" + "</B>"));
				holder.name.setGravity(0);
				holder.name.setPadding(a[0], a[1], a[2], a[3]);
				// holder.name.layout(0, 0, 0, 0);
				holder.icon.setImageBitmap(null);
				holder.icon.setImageMatrix(null);
				holder.date.setVisibility(ImageView.GONE);
				holder.date.setText("");
				holder.date.setVisibility(TextView.GONE);
				holder.issue.setText("");
				holder.issue.setVisibility(TextView.GONE);
			} else if (iGameType.equalsIgnoreCase("ssq")) {
				holder.name.setText("双色球");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.shuangseqiu_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;
				int[] ssqInt01 = new int[6];
				int[] ssqInt02 = new int[6];
				String[] ssqStr = new String[6];

				for (i2 = 0; i2 < 6; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
					PublicMethod.myOutput("-------ssqInt01[i2]---------"
							+ ssqInt01[i2]);
				}

				ssqInt02 = sort(ssqInt01);

				for (i3 = 0; i3 < 6; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 6; i1++) {
					// iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
							aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}

				iShowNumber = iNumbers.substring(12, 14);
				tempBallView = new OneBallView(convertView.getContext());
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
						aBlueColorResId);

				tableRow.addView(tempBallView);
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("fc3d")) {
				holder.name.setText("福彩3D");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.fucai_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				int i1;
				// zlm 7.30 代码修改：修改福彩3D号码
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers.substring(i1 * 2,
							i1 * 2 + 2));
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("qlc")) {
				holder.name.setText("七乐彩");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.qilecai_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;

				int[] ssqInt01 = new int[7];
				int[] ssqInt02 = new int[7];
				String[] ssqStr = new String[7];

				for (i2 = 0; i2 < 7; i2++) {
					iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
					ssqInt01[i2] = Integer.valueOf(iShowNumber);
					PublicMethod.myOutput("-------ssqInt01[i2]---------"
							+ ssqInt01[i2]);
				}

				ssqInt02 = sort(ssqInt01);

				for (i3 = 0; i3 < 7; i3++) {
					if (ssqInt02[i3] < 10) {
						ssqStr[i3] = "0" + ssqInt02[i3];
					} else {
						ssqStr[i3] = "" + ssqInt02[i3];
					}
				}
				for (i1 = 0; i1 < 7; i1++) {

					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ssqStr[i1],
							aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}
				// zlm 8.3 代码修改 ：添加七乐彩蓝球
				iShowNumber = iNumbers.substring(14, 16);
				tempBallView = new OneBallView(convertView.getContext());
				tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
						aBlueColorResId);

				tableRow.addView(tempBallView);
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("pl3")) {
				holder.name.setText("排列三");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.pailiesan_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 3; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("cjdlt")) {
				holder.name.setText("超级大乐透");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.daletou_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				// zlm 7.28 代码修改：添加号码排序
				int i1, i2, i3;
				String iShowNumber;
				OneBallView tempBallView;
				int[] cjdltInt01 = new int[5];
				int[] cjdltInt02 = new int[5];
				int[] cjdltInt03 = new int[2];
				int[] cjdltInt04 = new int[2];
				String[] cjdltStr = new String[5];
				String[] cjdltStr1 = new String[2];

				System.out.println("------iNumbers+cjdlt------" + iNumbers);
				for (i2 = 0; i2 < 5; i2++) {
					iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
					cjdltInt01[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt02 = sort(cjdltInt01);

				for (i3 = 0; i3 < 5; i3++) {
					if (cjdltInt02[i3] < 10) {
						cjdltStr[i3] = "0" + cjdltInt02[i3];
					} else {
						cjdltStr[i3] = "" + cjdltInt02[i3];
					}
				}
				for (i1 = 0; i1 < 5; i1++) {

					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, cjdltStr[i1],
							aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}

				for (i2 = 0; i2 < 2; i2++) {
					iShowNumber = iNumbers.substring(i2 * 3 + 15, i2 * 3 + 17);
					cjdltInt03[i2] = Integer.valueOf(iShowNumber);
				}

				cjdltInt04 = sort(cjdltInt03);

				for (i3 = 0; i3 < 2; i3++) {
					if (cjdltInt04[i3] < 10) {
						cjdltStr1[i3] = "0" + cjdltInt04[i3];
					} else {
						cjdltStr1[i3] = "" + cjdltInt04[i3];
					}
				}

				for (i1 = 0; i1 < 2; i1++) {
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
							cjdltStr1[i1], aBlueColorResId);

					tableRow.addView(tempBallView);
				}
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("ssc")) {
				holder.name.setText("时时彩");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.ssc_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				TableRow tableRow = new TableRow(context);

				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				for (i1 = 0; i1 < 5; i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);

					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);

				}
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			} else if (iGameType.equalsIgnoreCase("sfc")) {
				holder.name.setText("胜负彩");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.shengfucai_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				int oneRowNum = 7;
				OneBallView tempBallView;
				for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
					TableRow tableRow = new TableRow(context);
					for (i1 = 0; i1 < oneRowNum; i1++) {

						iShowNumber = Integer
								.valueOf(iNumbers.substring((j * oneRowNum)
										+ i1, (j * oneRowNum) + i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				}

			} else if (iGameType.equalsIgnoreCase("rxj")) {
				holder.name.setText("任选九");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.renxuanjiu_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				int oneRowNum = 7;
				OneBallView tempBallView;
				for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
					TableRow tableRow = new TableRow(context);
					for (i1 = 0; i1 < oneRowNum; i1++) {

						iShowNumber = Integer
								.valueOf(iNumbers.substring((j * oneRowNum)
										+ i1, (j * oneRowNum) + i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				}
			} else if (iGameType.equalsIgnoreCase("lcb")) {
				holder.name.setText("六场半全场");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.liuchangban_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				int oneRowNum = 6;
				OneBallView tempBallView;
				for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
					TableRow tableRow = new TableRow(context);
					for (i1 = 0; i1 < oneRowNum; i1++) {

						iShowNumber = Integer
								.valueOf(iNumbers.substring((j * oneRowNum)
										+ i1, (j * oneRowNum) + i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				}
			} else if (iGameType.equalsIgnoreCase("jqc")) {
				holder.name.setText("进球彩");
				holder.name.setGravity(0);
				holder.icon.setImageResource(R.drawable.jinqiucai_1);
				holder.date.setText(iDate);
				holder.date.setVisibility(TextView.VISIBLE);
				holder.issue.setText(iIssueNo);
				holder.issue.setVisibility(TextView.VISIBLE);
				int i1;
				int iShowNumber;
				OneBallView tempBallView;
				TableRow tableRow = new TableRow(context);

				for (i1 = 0; i1 < iNumbers.length(); i1++) {
					iShowNumber = Integer.valueOf(iNumbers
							.substring(i1, i1 + 1));
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
							+ iShowNumber, aRedColorResId);
					TableRow.LayoutParams lp = new TableRow.LayoutParams();
					tableRow.addView(tempBallView, lp);
				}
				holder.numbers.addView(tableRow, new TableLayout.LayoutParams(
						PublicConst.FP, PublicConst.WC));
			}

			// convertView.setBackgroundColor(colors[position]);
			convertView.setBackgroundResource(drawables[position]);
			if (convertView.isSelected()) {

			}
			return convertView;
		}

		static class ViewHolder {
			ImageView icon;
			TextView name;
			TableLayout numbers;
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
		private Context context;

		public SubEfficientAdapter(Context context, String[] index,
				List<Map<String, Object>> list) {
			this.context = context;
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

		public static final int[] colors = new int[] { 0x3000000, 0x300010ff };

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
			// String iFinalDate = (String) mList.get(position).get(mIndex[4]);
			// String iMoneySum = (String) mList.get(position).get(mIndex[5]);

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.notice_prizes_single_specific_layout, null);

				holder = new ViewHolder();
				holder.numbers = (TableLayout) convertView
						.findViewById(R.id.notice_pirzes_single_specific_ball_linearlayout);
				holder.numbers.setStretchAllColumns(true);
				holder.date = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_noticedDate_id);
				holder.date.setText(iDate);
				holder.issue = (TextView) convertView
						.findViewById(R.id.notice_prizes_single_specific_issue_id);
				holder.issue.setText(iIssueNo);

				if (iGameType.equalsIgnoreCase("ssq")) {
					// zlm 7.28 代码修改：添加号码排序
					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);
					int[] ssqInt01 = new int[6];
					int[] ssqInt02 = new int[6];
					String[] ssqStr = new String[6];

					for (i2 = 0; i2 < 6; i2++) {
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"
								+ ssqInt01[i2]);
					}

					ssqInt02 = sort(ssqInt01);

					for (i3 = 0; i3 < 6; i3++) {
						if (ssqInt02[i3] < 10) {
							ssqStr[i3] = "0" + ssqInt02[i3];
						} else {
							ssqStr[i3] = "" + ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 6; i1++) {
						// iShowNumber = iNumbers.substring(i1 * 2, i1 * 2 + 2);
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}

					iShowNumber = iNumbers.substring(12, 14);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					tableRow.addView(tempBallView);
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("fc3d")) {

					int i1;
					// zlm 7.30 代码修改：修改福彩3D号码
					int iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);
					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(
								i1 * 2, i1 * 2 + 2));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("qlc")) {
					// zlm 7.28 代码修改：添加号码排序
					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);

					int[] ssqInt01 = new int[7];
					int[] ssqInt02 = new int[7];
					String[] ssqStr = new String[7];

					for (i2 = 0; i2 < 7; i2++) {
						iShowNumber = iNumbers.substring(i2 * 2, i2 * 2 + 2);
						ssqInt01[i2] = Integer.valueOf(iShowNumber);
						PublicMethod.myOutput("-------ssqInt01[i2]---------"
								+ ssqInt01[i2]);
					}

					ssqInt02 = sort(ssqInt01);

					for (i3 = 0; i3 < 7; i3++) {
						if (ssqInt02[i3] < 10) {
							ssqStr[i3] = "0" + ssqInt02[i3];
						} else {
							ssqStr[i3] = "" + ssqInt02[i3];
						}
					}
					for (i1 = 0; i1 < 7; i1++) {

						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								ssqStr[i1], aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					// zlm 8.3 代码修改 ：添加七乐彩蓝球
					iShowNumber = iNumbers.substring(14, 16);
					tempBallView = new OneBallView(convertView.getContext());
					tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, iShowNumber,
							aBlueColorResId);

					tableRow.addView(tempBallView);
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("pl3")) {

					int i1;
					int iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);

					for (i1 = 0; i1 < 3; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,
								i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("cjdlt")) {

					int i1, i2, i3;
					String iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);

					int[] cjdltInt01 = new int[5];
					int[] cjdltInt02 = new int[5];
					int[] cjdltInt03 = new int[2];
					int[] cjdltInt04 = new int[2];
					String[] cjdltStr = new String[5];
					String[] cjdltStr1 = new String[2];

					for (i2 = 0; i2 < 5; i2++) {
						iShowNumber = iNumbers.substring(i2 * 3, i2 * 3 + 2);
						cjdltInt01[i2] = Integer.valueOf(iShowNumber);
					}

					cjdltInt02 = sort(cjdltInt01);

					for (i3 = 0; i3 < 5; i3++) {
						if (cjdltInt02[i3] < 10) {
							cjdltStr[i3] = "0" + cjdltInt02[i3];
						} else {
							cjdltStr[i3] = "" + cjdltInt02[i3];
						}
					}
					for (i1 = 0; i1 < 5; i1++) {

						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								cjdltStr[i1], aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}

					for (i2 = 0; i2 < 2; i2++) {
						iShowNumber = iNumbers.substring(i2 * 3 + 15,
								i2 * 3 + 17);
						cjdltInt03[i2] = Integer.valueOf(iShowNumber);
					}

					cjdltInt04 = sort(cjdltInt03);

					for (i3 = 0; i3 < 2; i3++) {
						if (cjdltInt04[i3] < 10) {
							cjdltStr1[i3] = "0" + cjdltInt04[i3];
						} else {
							cjdltStr1[i3] = "" + cjdltInt04[i3];
						}
					}

					for (i1 = 0; i1 < 2; i1++) {
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH,
								cjdltStr1[i1], aBlueColorResId);

						tableRow.addView(tempBallView);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("ssc")) {
					int i1;
					int iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);

					for (i1 = 0; i1 < 5; i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,
								i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);

						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				} else if (iGameType.equalsIgnoreCase("sfc")) {
					int i1;
					int iShowNumber;
					int oneRowNum = 7;
					OneBallView tempBallView;
					for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
						TableRow tableRow = new TableRow(context);
						for (i1 = 0; i1 < oneRowNum; i1++) {

							iShowNumber = Integer.valueOf(iNumbers.substring(
									(j * oneRowNum) + i1, (j * oneRowNum) + i1
											+ 1));
							tempBallView = new OneBallView(convertView
									.getContext());
							tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
									+ iShowNumber, aRedColorResId);
							TableRow.LayoutParams lp = new TableRow.LayoutParams();
							tableRow.addView(tempBallView, lp);
						}
						holder.numbers.addView(tableRow,
								new TableLayout.LayoutParams(PublicConst.FP,
										PublicConst.WC));
					}
				} else if (iGameType.equalsIgnoreCase("rxj")) {

					int i1;
					int iShowNumber;
					int oneRowNum = 7;
					OneBallView tempBallView;
					for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
						TableRow tableRow = new TableRow(context);
						for (i1 = 0; i1 < oneRowNum; i1++) {

							iShowNumber = Integer.valueOf(iNumbers.substring(
									(j * oneRowNum) + i1, (j * oneRowNum) + i1
											+ 1));
							tempBallView = new OneBallView(convertView
									.getContext());
							tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
									+ iShowNumber, aRedColorResId);
							TableRow.LayoutParams lp = new TableRow.LayoutParams();
							tableRow.addView(tempBallView, lp);
						}
						holder.numbers.addView(tableRow,
								new TableLayout.LayoutParams(PublicConst.FP,
										PublicConst.WC));
					}
				} else if (iGameType.equalsIgnoreCase("lcb")) {

					int i1;
					int iShowNumber;
					int oneRowNum = 6;
					OneBallView tempBallView;
					for (int j = 0; j < iNumbers.length() / oneRowNum; j++) {
						TableRow tableRow = new TableRow(context);
						for (i1 = 0; i1 < oneRowNum; i1++) {

							iShowNumber = Integer.valueOf(iNumbers.substring(
									(j * oneRowNum) + i1, (j * oneRowNum) + i1
											+ 1));
							tempBallView = new OneBallView(convertView
									.getContext());
							tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
									+ iShowNumber, aRedColorResId);
							TableRow.LayoutParams lp = new TableRow.LayoutParams();
							tableRow.addView(tempBallView, lp);
						}
						holder.numbers.addView(tableRow,
								new TableLayout.LayoutParams(PublicConst.FP,
										PublicConst.WC));
					}
				} else if (iGameType.equalsIgnoreCase("jqc")) {

					int i1;
					int iShowNumber;
					OneBallView tempBallView;
					TableRow tableRow = new TableRow(context);
					for (i1 = 0; i1 < iNumbers.length(); i1++) {
						iShowNumber = Integer.valueOf(iNumbers.substring(i1,
								i1 + 1));
						tempBallView = new OneBallView(convertView.getContext());
						tempBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
								+ iShowNumber, aRedColorResId);
						TableRow.LayoutParams lp = new TableRow.LayoutParams();
						tableRow.addView(tempBallView, lp);
					}
					holder.numbers.addView(tableRow,
							new TableLayout.LayoutParams(PublicConst.FP,
									PublicConst.WC));
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		static class ViewHolder {
			TableLayout numbers;
			TextView date;
			TextView issue;

		}
	}

	/**
	 * 主列表中相应的数据
	 */
	private List<Map<String, Object>> getListForMainListViewSimpleAdapter() {
		// 新加获取时时彩信息

		String[] jieshou_ssq, jieshou_fc3d, jieshou_qlc, jieshou_pl3, jieshou_cjdlt, jieshou_ssc, jieshou_sfc, jieshou_rxj, jieshou_lcb, jieshou_jqc;
		jieshou_ssq = getLotteryInfo("information1", 0);
		jieshou_fc3d = getLotteryInfo("information2", 0);
		jieshou_qlc = getLotteryInfo("information3", 0);
		jieshou_pl3 = getAddLotteryInfo("information8", 0);
		jieshou_cjdlt = getAddLotteryInfo("information7", 0);
		jieshou_ssc = getAddLotteryInfo("informationssc", 0);
		jieshou_sfc = getAddLotteryInfo("informationsfc", 0);
		jieshou_rxj = getAddLotteryInfo("informationrxj", 0);
		jieshou_lcb = getAddLotteryInfo("informationlcb", 0);
		jieshou_jqc = getAddLotteryInfo("informationjqc", 0);
		String iGameName[] = { "fucai", "ssq", "fc3d", "qlc", "ticai", "pl3",
				"cjdlt", "ssc", "zucai", "sfc", "rxj", "lcb", "jqc" }; // 8.9
		// 添加：排列三、超级大乐透

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		// 福彩标题
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[0]);
		map.put(WINNINGNUM, "");
		map.put(DATE, "");
		map.put(ISSUE, "");
		list.add(map);

		// zlm 7.16 代码修改：添加开奖日期
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[1]);
		map.put(WINNINGNUM, jieshou_ssq[0] + jieshou_ssq[1]);
		map.put(DATE, "开奖日期： " + jieshou_ssq[3]);
		map.put(ISSUE, "第" + jieshou_ssq[2] + "期");
		list.add(map);

		// zlm 7.16 代码修改：添加开奖日期
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[2]);
		map.put(WINNINGNUM, jieshou_fc3d[0] + jieshou_fc3d[1]);
		map.put(DATE, "开奖日期： " + jieshou_fc3d[3]);
		map.put(ISSUE, "第" + jieshou_fc3d[2] + "期");
		list.add(map);

		// zlm 7.16 代码修改：添加开奖日期
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[3]);
		map.put(WINNINGNUM, jieshou_qlc[0] + jieshou_qlc[1]);
		map.put(DATE, "开奖日期： " + jieshou_qlc[3]);
		map.put(ISSUE, "第" + jieshou_qlc[2] + "期");
		list.add(map);

		// 体彩标题
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[4]);
		map.put(WINNINGNUM, "");
		map.put(DATE, "");
		map.put(ISSUE, "");
		list.add(map);
		// 8.9 添加：排列三
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[5]);
		map.put(WINNINGNUM, jieshou_pl3[0]);
		map.put(DATE, "开奖日期： " + jieshou_pl3[3]);
		map.put(ISSUE, "第" + jieshou_pl3[2] + "期");
		list.add(map);

		// 8.9 添加：超级大乐透
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[6]);
		map.put(WINNINGNUM, jieshou_cjdlt[0]);
		map.put(DATE, "开奖日期： " + jieshou_cjdlt[3]);
		map.put(ISSUE, "第" + jieshou_cjdlt[2] + "期");
		list.add(map);
		// 添加：时时彩
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[7]);
		map.put(WINNINGNUM, jieshou_ssc[0]);
		map.put(DATE, "开奖日期： " + jieshou_ssc[3]);
		map.put(ISSUE, "第" + jieshou_ssc[2] + "期");
		list.add(map);
		// 足彩标题
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[8]);
		map.put(WINNINGNUM, "");
		map.put(DATE, "");
		map.put(ISSUE, "");
		list.add(map);
		// 添加：胜负彩
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[9]);
		map.put(WINNINGNUM, jieshou_sfc[0]);
		map.put(DATE, "开奖日期： " + jieshou_sfc[3]);
		map.put(ISSUE, "第" + jieshou_sfc[2] + "期");
		list.add(map);
		// 添加：任选九彩
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[10]);
		map.put(WINNINGNUM, jieshou_rxj[0]);
		map.put(DATE, "开奖日期： " + jieshou_rxj[3]);
		map.put(ISSUE, "第" + jieshou_rxj[2] + "期");
		list.add(map);
		// 添加：六场半
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[11]);
		map.put(WINNINGNUM, jieshou_lcb[0]);
		map.put(DATE, "开奖日期： " + jieshou_lcb[3]);
		map.put(ISSUE, "第" + jieshou_lcb[2] + "期");
		list.add(map);
		// 添加：进球彩
		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName[12]);
		map.put(WINNINGNUM, jieshou_jqc[0]);
		map.put(DATE, "开奖日期： " + jieshou_jqc[3]);
		map.put(ISSUE, "第" + jieshou_jqc[2] + "期");
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
		// map.put(FINALDATE, "结奖日期： " + getInfo1[4]);
		// map.put(MONEYSUM, "总金额： " + getInfo1[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo2[0] + getInfo2[1]);
		map.put(DATE, "开奖日期： " + getInfo2[3]);
		map.put(ISSUE, "第" + getInfo2[2] + "期");
		// map.put(FINALDATE, "结奖日期： " + getInfo2[4]);
		// map.put(MONEYSUM, "总金额： " + getInfo2[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo3[0] + getInfo3[1]);
		map.put(DATE, "开奖日期： " + getInfo3[3]);
		map.put(ISSUE, "第" + getInfo3[2] + "期");
		// map.put(FINALDATE, "结奖日期： " + getInfo3[4]);
		// map.put(MONEYSUM, "总金额： " + getInfo3[5] + " 元");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put(LOTTERYTYPE, iGameName);
		map.put(WINNINGNUM, getInfo4[0] + getInfo4[1]);
		map.put(DATE, "开奖日期： " + getInfo4[3]);
		map.put(ISSUE, "第" + getInfo4[2] + "期");

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
		if (lottery_ssq.equalsIgnoreCase("") || lottery_ssq == null
				|| lottery_ssq.equalsIgnoreCase("ssqinfo")
				|| lottery_ssq.equalsIgnoreCase("dddinfo")
				|| lottery_ssq.equalsIgnoreCase("qlcinfo")) {
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
						} else {
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
	 * 从网络上获取大乐透和排列三数据
	 * 
	 * @param string
	 * @param index
	 * @return
	 */
	public String[] getAddLotteryInfo(String string, int index) {
		// ShellRWSharesPreferences shellRW;
		String error_code = "00";
		String win_base_code = "";
		String win_special_code = "";
		String term_code = "";
		String beginTime = "";
		String endTime = "";
		String totalMoney = "";
		int iretrytimes = 2;
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String lottery_ssq = shellRW.getUserLoginInfo(string);
		if (lottery_ssq.equalsIgnoreCase("") || lottery_ssq == null
				|| lottery_ssq.equalsIgnoreCase("dltinfo")
				|| lottery_ssq.equalsIgnoreCase("pl3info")) {
			if (string.equalsIgnoreCase("information7")) {
				win_base_code = "00 00 00 00 00+00 00";
				term_code = "0000";
				beginTime = "";
				// endTime = "";
				totalMoney = "";
			} else if (string.equalsIgnoreCase("information8")) {
				win_base_code = "000";
				term_code = "0000";
				beginTime = "";
				// endTime = "";
				totalMoney = "";
			} else if (string.equalsIgnoreCase("informationssc")) {
				win_base_code = "00000";
				term_code = "0000";
				beginTime = "";
				// endTime = "";
				totalMoney = "";
			} else {
				win_base_code = "00000";
				term_code = "0000";
				beginTime = "";
				// endTime = "";
				totalMoney = "";
			}
		} else {
			while (iretrytimes < 3 && iretrytimes > 0) {
				try {
					try {
						JSONObject obj = new JSONObject(lottery_ssq);
						error_code = obj.getString("error_code");
						if (error_code.equals("0000")) {
							win_base_code = obj.getString("win_base_code");// 获取中奖号码
							term_code = obj.getString("term_code");// 获取期号

							// 新加的信息 陈晨20100716
							beginTime = obj.getString("Begin_time");

						} else {
							if (string.equalsIgnoreCase("information7")) {
								win_base_code = "00 00 00 00 00+00 00";
								term_code = "0000";

								beginTime = "";

							}
							if (string.equalsIgnoreCase("information8")) {
								win_base_code = "000";
								term_code = "0000";

								beginTime = "";

							}
							if (string.equalsIgnoreCase("informationssc")) {
								win_base_code = "00000000000000";
								term_code = "0000";
								beginTime = "";

								totalMoney = "";
							}

						}
					} catch (Exception e) {
						JSONArray jsonObject3 = new JSONArray(lottery_ssq);
						JSONObject obj = jsonObject3.getJSONObject(index);
						error_code = obj.getString("error_code");
						if (error_code.equals("0000")) {
							win_base_code = obj.getString("win_base_code");

							term_code = obj.getString("term_code");

							// 新加的信息 陈晨20100716
							beginTime = obj.getString("Begin_time");

						} else {
							if (string.equalsIgnoreCase("information7")) {
								win_base_code = "00 00 00 00 00+00 00";
								term_code = "0000";

								beginTime = "";

							}
							if (string.equalsIgnoreCase("information8")) {
								win_base_code = "000";
								term_code = "0000";

								beginTime = "";

							}
							if (string.equalsIgnoreCase("informationssc")) {
								win_base_code = "00000";
								term_code = "0000";
								beginTime = "";
								// endTime = "";
								totalMoney = "";
							}

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

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
}
