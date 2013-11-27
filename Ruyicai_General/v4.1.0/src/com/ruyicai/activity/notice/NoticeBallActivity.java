package com.ruyicai.activity.notice;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.high.HghtOrderdeail;
import com.ruyicai.activity.buy.miss.AddViewMiss;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.miss.OrderDetails;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.buy.zixuan.AddView;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.code.Gdeleven.GdelevenCode;
import com.ruyicai.code.dlc.DlcCode;
import com.ruyicai.code.dlt.DltNormalSelectCode;
import com.ruyicai.code.fc3d.Fc3dZiZhiXuanCode;
import com.ruyicai.code.pl3.PL3ZiZhiXuanCode;
import com.ruyicai.code.qlc.QlcZiZhiXuanCode;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.net.newtransaction.MissInterface;
import com.ruyicai.net.newtransaction.PrizeInfoInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 开奖走势基类
 * 
 */
public class NoticeBallActivity extends Activity {
	/** 走势面板填充布局 */
	LinearLayout layout;

	/** add by pengcx 20130808 start */
	/** 选号小球面板填充布局 */
	LinearLayout selectlayout;
	/** add by pengcx 20130808 end */

	// 红球走势图控件
	NoticeBallView ballRedView;
	// 篮球走势图控件
	NoticeBallView ballBlueView;
	/** add by pengcx 20130809 start */
	NoticeBallView ballSelectedRedView;
	NoticeBallView ballSelectedBlueView;
	/** add by pengcx 20130809 end */

	protected boolean isRed;
	List<JSONObject> listall;
	protected TextView textRedCodeOne;
	protected TextView textBlueCodeOne;
	/** add by pengcx 20130809 start */
	protected TextView textRedCodeTwo;
	protected TextView textBlueCodeTwo;
	protected TextView textThreeCodeOne;
	protected TextView textThreeCodeTwo;
	private AddViewMiss addViewMiss;
	private AddView addView;
	private BetAndGiftPojo betAndGiftPojo;
	/** add by pengcx 20130809 end */
	RelativeLayout bottomlayout;
	Button touzhuBtn;
	private RWSharedPreferences shellRW;
	protected HorizontalScrollView hScrollView;
	protected Spinner oneSelectButtonSpinner;
	protected Spinner twoSelectButtonSpinner;
	static List<String> missRed;
	static List<String> missBlue;

	private static final String[] methods1 = { "任选二", "任选三", "任选四 ", "任选五 ",
			"任选六", "任选七 ", "任选八" };
	private static final String[] methods2 = { "前一直选", "前二直选", "前三直选 ",
			"前二组选 ", "前三组选" };

	boolean isBeforeThree;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notice_ball_main);
		// 选号栏相对布局
		bottomlayout = (RelativeLayout) findViewById(R.id.notice_ball_relative_bottom);
		// 开奖走势滑动布局
		hScrollView = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView01);

		// 开奖走势滑动布局
		layout = (LinearLayout) findViewById(R.id.notice_ball_main_linear);
		/** add by pengcx 20130808 start */
		// 选号红球和篮球栏
		selectlayout = (LinearLayout) findViewById(R.id.notice_ball_select_linear);
		/** add by pengcx 20130808 end */
		/** modify by pengcx 20130809 start */
		// 选号一红球
		textRedCodeOne = (TextView) findViewById(R.id.notice_ball_red_text_code_one);
		// 选号一篮球
		textBlueCodeOne = (TextView) findViewById(R.id.notice_ball_blue_text_code_one);
		/** modify by pengcx 20130809 end */
		/** add by pengcx 20130809 start */
		// 选号二红球
		textRedCodeTwo = (TextView) findViewById(R.id.notice_ball_red_text_code_two);
		// 选号二篮球
		textBlueCodeTwo = (TextView) findViewById(R.id.notice_ball_blue_text_code_two);
		textThreeCodeOne = (TextView) findViewById(R.id.notice_ball_three_text_code_one);
		textThreeCodeTwo = (TextView) findViewById(R.id.notice_ball_three_text_code_two);
		/** add by pengcx 20130809 end */
		// 投注按钮
		touzhuBtn = (Button) findViewById(R.id.notice_ball_btn_touzhu);
		touzhuBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				begainTouZhu();
			}
		});

		oneSelectButtonSpinner = (Spinner) findViewById(R.id.onedown_button_spinner);
		twoSelectButtonSpinner = (Spinner) findViewById(R.id.twodown_button_spinner);
		ArrayAdapter<String> adapter = null;

		if (isBeforeThree) {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, methods2);
			oneSelectButtonSpinner.setAdapter(adapter);
			twoSelectButtonSpinner.setAdapter(adapter);
			oneSelectButtonSpinner.setSelection(2);
			twoSelectButtonSpinner.setSelection(2);
		} else {
			adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, methods1);
			oneSelectButtonSpinner.setAdapter(adapter);
			twoSelectButtonSpinner.setAdapter(adapter);
			oneSelectButtonSpinner.setSelection(3);
			twoSelectButtonSpinner.setSelection(3);
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		oneSelectButtonSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (isBeforeThree) {
							ballSelectedRedView.setFirstDraw(true);
						}

						if (ballSelectedRedView != null) {
							ballSelectedRedView.ballsChcekOne.clear();
							ballSelectedRedView.invalidate();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		twoSelectButtonSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (isBeforeThree) {
							ballSelectedRedView.setFirstDraw(true);
						}

						if (ballSelectedRedView != null) {
							ballSelectedRedView.ballsChcekTwo.clear();
							ballSelectedRedView.invalidate();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		missRed = new ArrayList<String>();
		missBlue = new ArrayList<String>();
	}

	public List<JSONObject> getballlist() {
		return listall;
	}

	/**
	 * 联网获取所有彩种开奖信息
	 */
	public void noticeAllNet(final boolean isRed) {
		// 如果是第一次加载，则使用独立线程
		if (NoticeMainActivity.isFirstNotice) {
			final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
			dialog.show();
			final Handler handler = new Handler();
			new Thread(new Runnable() {
				@Override
				public void run() {
					dialog.cancel();
					handler.post(new Runnable() {
						@Override
						public void run() {
							addBallView(isRed);
						}
					});
				}

			}).start();
		} else {
			addBallView(isRed);
		}
	}

	/**
	 * 开奖公告里主列表与子列表之间的跳转
	 * 
	 * @param listViewID
	 *            列表ID
	 */
	public void addBallView(boolean isRed) {
		this.isRed = isRed;
		List<JSONObject> list = null;
		switch (NoticeActivityGroup.LOTNO) {
		// 广东11-5
		case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
			// 创建红球走势图控件对象
			ballRedView = new NoticeBallView(this);
			// 创建篮球走势图控件对象
			ballBlueView = new NoticeBallView(this);
			// 联网获取开奖信息
			list = getSubInfoForListView(Constants.LOTNO_GD115);
			ballSelectedRedView = new NoticeBallView(this);
			// 初始化控球走势图控件对象
			if (isBeforeThree) {
				ballRedView.setIsBeforThree(true);
				ballSelectedRedView.setIsBeforThree(true);
				ballSelectedRedView.initNoticeBall(3, 11, 1, null, true,
						"gd11-5", 1 * NoticeMainActivity.SCALE);
				ballSelectedRedView.setHundredPart(textRedCodeOne);
				ballSelectedRedView.setDecadePart(textBlueCodeOne);
				ballSelectedRedView.setUnitPart(textThreeCodeOne);
				ballSelectedRedView.setHundredTwoPart(textRedCodeTwo);
				ballSelectedRedView.setDecadeTwoPart(textBlueCodeTwo);
				ballSelectedRedView.setUnitTwoPart(textThreeCodeTwo);
			} else {
				ballSelectedRedView.initNoticeBall(2, 11, 1, null, true,
						"gd11-5", 1 * NoticeMainActivity.SCALE);
				ballSelectedRedView.setTextCode(textRedCodeOne);
				ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
			}
			ballRedView.initNoticeBall(list.size(), 11, 1, list, true,
					"gd11-5", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			bottomlayout.setVisibility(View.VISIBLE);

			selectlayout.addView(ballSelectedRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		// 广东快乐十分
		case NoticeActivityGroup.ID_SUB_GD10_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_ten);
			ballRedView.initNoticeBall(list.size(), 18, 1, list, false,
					"gd-10", 1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 2, 19, list, true,
					"gd-10", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);
			hScrollView.setPadding(0, 0, 0, 0);

			break;
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_SSQ);
			ballRedView.initNoticeBall(list.size(), 33, 1, list, true, "ssq",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 16, 1, list, false, "ssq",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);

			/** add by pengcx 20130808 start */
			/** 创建红球和篮球选号面板 */
			ballSelectedRedView = new NoticeBallView(this);
			ballSelectedBlueView = new NoticeBallView(this);
			ballSelectedRedView.initNoticeBall(3, 33, 1, null, true, "ssq",
					1 * NoticeMainActivity.SCALE);
			ballSelectedBlueView.initNoticeBall(3, 16, 1, null, false, "ssq",
					1 * NoticeMainActivity.SCALE);
			ballSelectedRedView.setTextCode(textRedCodeOne);
			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
			ballSelectedBlueView.setTextCode(textBlueCodeOne);
			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
			selectlayout.addView(ballSelectedRedView);
			selectlayout.addView(ballSelectedBlueView);
			/** add by pengcx 20130808 end */

			break;
		case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_FC3D);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "fc3d",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);

			/** add by pengcx 20130812 start */
			ballSelectedRedView = new NoticeBallView(this);
			ballSelectedRedView.initNoticeBall(3, 10, 0, null, isRed, "fc3d",
					1 * NoticeMainActivity.SCALE);
			ballSelectedRedView.setHundredPart(textRedCodeOne);
			ballSelectedRedView.setDecadePart(textBlueCodeOne);
			ballSelectedRedView.setUnitPart(textThreeCodeOne);
			ballSelectedRedView.setHundredTwoPart(textRedCodeTwo);
			ballSelectedRedView.setDecadeTwoPart(textBlueCodeTwo);
			ballSelectedRedView.setUnitTwoPart(textThreeCodeTwo);
			selectlayout.addView(ballSelectedRedView);
			/** add by pengcx 20130812 end */

			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QLC);
			ballRedView.initNoticeBall(list.size(), 30, 1, list, true, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 30, 1, list, false, "qlc",
					1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);

			/** add by pengcx 20130808 start */
			/** 创建红球和篮球选号面板 */
			ballSelectedRedView = new NoticeBallView(this);
			ballSelectedBlueView = new NoticeBallView(this);
			ballSelectedRedView.initNoticeBall(3, 30, 1, null, true, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballSelectedBlueView.initNoticeBall(3, 30, 1, null, false, "qlc",
					1 * NoticeMainActivity.SCALE);
			ballSelectedRedView.setTextCode(textRedCodeOne);
			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
			ballSelectedBlueView.setTextCode(textBlueCodeOne);
			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
			selectlayout.addView(ballSelectedRedView);
			selectlayout.addView(ballSelectedBlueView);
			/** add by pengcx 20130808 end */
			break;
		case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
			// zlm 排列三
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL3);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			bottomlayout.setVisibility(View.VISIBLE);

			/** add by pengcx 20130812 start */
			ballSelectedRedView = new NoticeBallView(this);
			ballSelectedRedView.initNoticeBall(3, 10, 0, null, isRed, "pl3",
					1 * NoticeMainActivity.SCALE);
			ballSelectedRedView.setHundredPart(textRedCodeOne);
			ballSelectedRedView.setDecadePart(textBlueCodeOne);
			ballSelectedRedView.setUnitPart(textThreeCodeOne);
			ballSelectedRedView.setHundredTwoPart(textRedCodeTwo);
			ballSelectedRedView.setDecadeTwoPart(textBlueCodeTwo);
			ballSelectedRedView.setUnitTwoPart(textThreeCodeTwo);
			selectlayout.addView(ballSelectedRedView);
			/** add by pengcx 20130812 end */
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_PL5_LISTVIEW:
			// zlm 排列五
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_PL5);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "pl5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_QXC_LISTVIEW:
			// zlm 七星彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_QXC);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "qxc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			// zlm 超级大乐透
			ballRedView = new NoticeBallView(this);
			ballBlueView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_DLT);
			ballRedView.initNoticeBall(list.size(), 35, 1, list, true, "cjdlt",
					1 * NoticeMainActivity.SCALE);
			ballBlueView.initNoticeBall(list.size(), 12, 1, list, false,
					"cjdlt", 1 * NoticeMainActivity.SCALE);
			bottomlayout.setVisibility(View.VISIBLE);
			layout.addView(ballRedView);
			layout.addView(ballBlueView);

			/** add by pengcx 20130812 start */
			/** 创建红球和篮球选号面板 */
			ballSelectedRedView = new NoticeBallView(this);
			ballSelectedBlueView = new NoticeBallView(this);
			ballSelectedRedView.initNoticeBall(3, 35, 1, null, true, "cjdlt",
					1 * NoticeMainActivity.SCALE);
			ballSelectedBlueView.initNoticeBall(3, 12, 1, null, false, "cjdlt",
					1 * NoticeMainActivity.SCALE);
			ballSelectedRedView.setTextCode(textRedCodeOne);
			ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
			ballSelectedBlueView.setTextCode(textBlueCodeOne);
			ballSelectedBlueView.setTextCodeTow(textBlueCodeTwo);
			selectlayout.addView(ballSelectedRedView);
			selectlayout.addView(ballSelectedBlueView);
			/** add by pengcx 20130812 end */
			break;
		case NoticeActivityGroup.ID_SUB_SHISHICAI_LISTVIEW:
			// zlm 时时彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_SSC);
			ballRedView.initNoticeBall(list.size(), 10, 0, list, isRed, "ssc",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			ballSelectedRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_11_5);
			if (isBeforeThree) {
				ballRedView.setIsBeforThree(true);
				ballSelectedRedView.setIsBeforThree(true);
				ballSelectedRedView.initNoticeBall(3, 11, 1, null, true,
						"11-5", 1 * NoticeMainActivity.SCALE);
				ballSelectedRedView.setHundredPart(textRedCodeOne);
				ballSelectedRedView.setDecadePart(textBlueCodeOne);
				ballSelectedRedView.setUnitPart(textThreeCodeOne);
				ballSelectedRedView.setHundredTwoPart(textRedCodeTwo);
				ballSelectedRedView.setDecadeTwoPart(textBlueCodeTwo);
				ballSelectedRedView.setUnitTwoPart(textThreeCodeTwo);
			} else {
				ballSelectedRedView.initNoticeBall(2, 11, 1, null, true,
						"11-5", 1 * NoticeMainActivity.SCALE);
				ballSelectedRedView.setTextCode(textRedCodeOne);
				ballSelectedRedView.setTextCodeTow(textRedCodeTwo);
			}
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed, "11-5",
					1 * NoticeMainActivity.SCALE);

			layout.addView(ballRedView);
			bottomlayout.setVisibility(View.VISIBLE);
			selectlayout.addView(ballSelectedRedView);
			hScrollView.setPadding(0, 0, 0, 0);

			break;
		case NoticeActivityGroup.ID_SUB_YDJ_LISTVIEW:
			// zlm 11-5彩
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_eleven);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed,
					"11-ydj", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_CQ11X5_LISTVIEW:
			// 重庆11选5
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_CQ_ELVEN_FIVE);
			ballRedView.initNoticeBall(list.size(), 11, 1, list, isRed,
					"cq-11-5", 1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		case NoticeActivityGroup.ID_SUB_TWENTY_LISTVIEW:
			ballRedView = new NoticeBallView(this);
			list = getSubInfoForListView(Constants.LOTNO_22_5);
			ballRedView.initNoticeBall(list.size(), 22, 1, list, isRed, "22-5",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;

		// 内蒙快三
		case NoticeActivityGroup.ID_SUB_NMK3_LISTVIEW:
			// 创建红球走势图控件对象
			ballRedView = new NoticeBallView(this);
			// 联网获取开奖信息
			list = getSubInfoForListView(Constants.LOTNO_NMK3);
			// 初始化控球走势图控件对象
			ballRedView.initNoticeBall(list.size(), 6, 1, list, true, "nmk3",
					1 * NoticeMainActivity.SCALE);
			layout.addView(ballRedView);
			hScrollView.setPadding(0, 0, 0, 0);
			break;
		}
		listall = list;
	}

	private List<Integer> getList(String codeStr) {
		List<Integer> list = new ArrayList<Integer>();
		String codeStrs[] = codeStr.split(",");
		for (int i = 0; i < codeStrs.length; i++) {
			if (!codeStrs[i].equals("")) {
				try {
					Integer codeInt = new Integer(codeStrs[i]);
					list.add(codeInt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	private void begainTouZhu() {
		if (!isLogin()) {
			Intent intent = new Intent(this, UserLogin.class);
			startActivity(intent);
		} else {
			if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW
					|| NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW
					|| ((NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW || NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_DLC_LISTVIEW) && isBeforeThree)) {
				String oneHundredStr = textRedCodeOne.getText().toString();
				String oneDecadeStr = textBlueCodeOne.getText().toString();
				String oneUnitStr = textThreeCodeOne.getText().toString();

				if (oneHundredStr.contains("|")) {
					oneHundredStr = oneHundredStr.substring(0,
							oneHundredStr.length() - 1);
				}
				if (oneDecadeStr.contains("|")) {
					oneDecadeStr = oneDecadeStr.substring(0,
							oneDecadeStr.length() - 1);
				}
				List<Integer> oneHundreds = getListOther(oneHundredStr);
				List<Integer> oneDecades = getListOther(oneDecadeStr);
				List<Integer> oneUnits = getListOther(oneUnitStr);

				String twoHundredStr = textRedCodeTwo.getText().toString();
				String twoDecadeStr = textBlueCodeTwo.getText().toString();
				String twoUnitStr = textThreeCodeTwo.getText().toString();
				if (twoHundredStr.contains("|")) {
					twoHundredStr = twoHundredStr.substring(0,
							twoHundredStr.length() - 1);
				}
				if (twoDecadeStr.contains("|")) {
					twoDecadeStr = twoDecadeStr.substring(0,
							twoDecadeStr.length() - 1);
				}
				List<Integer> twoHundreds = getListOther(twoHundredStr);
				List<Integer> twoDecades = getListOther(twoDecadeStr);
				List<Integer> twoUnits = getListOther(twoUnitStr);
				if ((NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW || NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_DLC_LISTVIEW)
						&& isBeforeThree) {
					if (oneSelectButtonSpinner.getSelectedItemPosition() == 3) {
						oneHundreds.addAll(oneDecades);
						oneDecades.clear();
						PublicMethod.rankIntList(oneHundreds);
					} else if (oneSelectButtonSpinner.getSelectedItemPosition() == 4) {
						oneHundreds.addAll(oneDecades);
						oneHundreds.addAll(oneUnits);
						oneDecades.clear();
						oneUnits.clear();
						PublicMethod.rankIntList(oneHundreds);
					}

					if (twoSelectButtonSpinner.getSelectedItemPosition() == 3) {
						twoHundreds.addAll(twoDecades);
						twoDecades.clear();
						PublicMethod.rankIntList(twoHundreds);
					} else if (twoSelectButtonSpinner.getSelectedItemPosition() == 4) {
						twoHundreds.addAll(twoDecades);
						twoHundreds.addAll(twoUnits);
						twoDecades.clear();
						twoUnits.clear();
						PublicMethod.rankIntList(twoHundreds);
					}

					setTouZhuInfoFour(oneHundreds, oneDecades, oneUnits,
							twoHundreds, twoDecades, twoUnits,
							oneSelectButtonSpinner.getSelectedItemPosition(),
							twoSelectButtonSpinner.getSelectedItemPosition());
				} else {
					setTouZhuInfoOther(oneHundreds, oneDecades, oneUnits,
							twoHundreds, twoDecades, twoUnits);
				}

			} else if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW
					|| NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_DLC_LISTVIEW) {
				String redStrOne = textRedCodeOne.getText().toString();
				List<Integer> redListOne = getList(redStrOne);
				String redStrTwo = textRedCodeTwo.getText().toString();
				List<Integer> redListTwo = getList(redStrTwo);
				setTouZhuInfoThree(redListOne, redListTwo,
						oneSelectButtonSpinner.getSelectedItemPosition() + 2,
						twoSelectButtonSpinner.getSelectedItemPosition() + 2);
			} else {
				/** modify by pengcx 20130809 start */
				String redStrOne = textRedCodeOne.getText().toString();
				String blueStrOne = textBlueCodeOne.getText().toString();
				if (blueStrOne.contains("|")) {
					blueStrOne = blueStrOne.substring(1, blueStrOne.length());
				}
				List<Integer> redListOne = getList(redStrOne);
				List<Integer> blueListOne = getList(blueStrOne);

				String redStrTwo = textRedCodeTwo.getText().toString();
				String blueStrTwo = textBlueCodeTwo.getText().toString();
				if (blueStrTwo.contains("|")) {
					blueStrTwo = blueStrTwo.substring(1, blueStrTwo.length());
				}

				List<Integer> redListTwo = getList(redStrTwo);
				List<Integer> blueListTwo = getList(blueStrTwo);

				setTouZhuInfo(redListOne, blueListOne, redListTwo, blueListTwo);
				/** modify by pengcx 20130809 end */
			}
		}
	}

	private void setTouZhuInfoFour(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits,
			List<Integer> twoHundreds, List<Integer> twoDecades,
			List<Integer> twoUnits, int i, int j) {
		String lotno = "";
		String code = "";
		long betNums1 = 0;
		long betNums2 = 0;

		int isOneRight = isBetLegitimacyFour(oneHundreds, oneDecades, oneUnits,
				i);
		int isTowRight = 0;
		if (isOneRight == 4 || isOneRight == 0) {
			isTowRight = isBetLegitimacyFour(twoHundreds, twoDecades, twoUnits,
					j);
		}

		if (((isOneRight == 0) && (isTowRight == 0))
				|| ((isOneRight == 0) && (isTowRight == 4))
				|| ((isOneRight == 4) && (isTowRight == 0))) {
			switch (NoticeActivityGroup.LOTNO) {
			case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
				lotno = Constants.LOTNO_11_5;

				if (isOneRight == 0) {
					code = DlcCode.simulateZhumaOther(oneHundreds, oneDecades,
							oneUnits, i);
					betNums1 = caculateBetNumFour(oneHundreds, oneDecades,
							oneUnits, i);
					addViewAndTouZhuFour(betNums1, oneHundreds, oneDecades,
							oneUnits, lotno, code);
				}

				if (isTowRight == 0) {
					code = DlcCode.simulateZhumaOther(twoHundreds, twoDecades,
							twoUnits, j);
					betNums2 = caculateBetNumFour(twoHundreds, twoDecades,
							twoUnits, j);
					addViewAndTouZhuFour(betNums2, twoHundreds, twoDecades,
							twoUnits, lotno, code);
				}
				break;
			case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
				lotno = Constants.LOTNO_GD115;
				if (isOneRight == 0) {
					code = GdelevenCode.simulateZhumaOther(oneHundreds,
							oneDecades, oneUnits, i);
					betNums1 = caculateBetNumFour(oneHundreds, oneDecades,
							oneUnits, i);
					addViewAndTouZhuFour(betNums1, oneHundreds, oneDecades,
							oneUnits, lotno, code);
				}

				if (isTowRight == 0) {
					code = GdelevenCode.simulateZhumaOther(twoHundreds,
							twoDecades, twoUnits, j);
					betNums2 = caculateBetNumFour(twoHundreds, twoDecades,
							twoUnits, j);
					addViewAndTouZhuFour(betNums2, twoHundreds, twoDecades,
							twoUnits, lotno, code);
				}
				break;
			}

			if (betNums1 <= 10000 && betNums2 <= 10000) {
				Intent intent = new Intent(this, HghtOrderdeail.class);
				intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
				intent.putExtra("isAlert", true);
				startActivity(intent);
			}

		} else {
			showPromtFour(isOneRight, isTowRight, i, j);
		}
	}

	private void addViewAndTouZhuFour(long betNums, List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits, String lotno,
			String code) {
		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			if (betAndGiftPojo == null) {
				betAndGiftPojo = new BetAndGiftPojo();
			}
			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");

			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");

			if (lotno.equals(Constants.LOTNO_DLT)) {
				betAndGiftPojo.setZhui(true);
			}

			if (addView == null) {
				addView = new AddView(this);
			}

			CodeInfo codeInfo = addView.initCodeInfo(2, 1);
			codeInfo.setTouZhuCode(code);
			codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
			codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
			codeInfo = setCodeInfoColorFour(codeInfo, oneHundreds, oneDecades,
					oneUnits);
			addView.addCodeInfo(codeInfo);
			betAndGiftPojo.setBet_code(addView.getTouzhuCode(1,
					betAndGiftPojo.getAmt() * 100));
			ApplicationAddview app = (ApplicationAddview) getApplicationContext();
			app.setPojo(betAndGiftPojo);
			app.setAddview(addView);
			app.setHtextzhuma(addView.getsharezhuma());
			app.setHzhushu(addView.getAllZhu());
		}
	}

	private CodeInfo setCodeInfoColorFour(CodeInfo codeInfo,
			List<Integer> oneHundreds, List<Integer> oneDecades,
			List<Integer> oneUnits) {
		StringBuffer hunderdsString = new StringBuffer();
		StringBuffer decadesString = new StringBuffer();
		StringBuffer unitsString = new StringBuffer();

		int hundredSize = oneHundreds.size();
		int decadeSize = oneDecades.size();
		int unitSize = oneUnits.size();

		for (int hundred = 0; hundred < hundredSize; hundred++) {
			hunderdsString.append(PublicMethod.isTen(oneHundreds.get(hundred))
					+ ",");
		}
		if (hunderdsString.length() > 0) {
			String hunderds = hunderdsString.substring(0,
					hunderdsString.length() - 1);
			codeInfo.addAreaCode(hunderds, Color.RED);
		}

		for (int decade = 0; decade < decadeSize; decade++) {
			decadesString.append(PublicMethod.isTen(oneDecades.get(decade))
					+ ",");
		}
		if (decadesString.length() > 0) {
			String decades = decadesString.substring(0,
					decadesString.length() - 1);
			codeInfo.addAreaCode(decades, Color.RED);
		}

		for (int unit = 0; unit < unitSize; unit++) {
			unitsString.append(PublicMethod.isTen(oneUnits.get(unit)) + ",");
		}
		if (unitsString.length() > 0) {
			String units = unitsString.substring(0, unitsString.length() - 1);
			codeInfo.addAreaCode(units, Color.RED);
		}

		return codeInfo;
	}

	private long caculateBetNumFour(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits, int i) {
		long betNums = 0;
		if (i == 0) {
			betNums = oneHundreds.size();
		} else if (i == 1) {
			betNums = caculateBetNumFourQ2(oneHundreds, oneDecades);
		} else if (i == 2) {
			betNums = caculateBetNumFourQ3(oneHundreds, oneDecades, oneUnits);
		} else if (i == 3) {
			betNums = PublicMethod.zuhe(i - 1, oneHundreds.size());
		} else if (i == 4) {
			betNums = PublicMethod.zuhe(i - 1, oneHundreds.size());
		}
		return betNums;
	}

	private long caculateBetNumFourQ3(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits) {
		int zhushu = 0;
		for (int i = 0; i < oneHundreds.size(); i++) {
			for (int j = 0; j < oneDecades.size(); j++) {
				if (!oneHundreds.get(i).equals(oneDecades.get(j))) {
					for (int k = 0; k < oneUnits.size(); k++) {
						if (!oneUnits.get(k).equals(oneDecades.get(j))
								&& !oneUnits.get(k).equals(oneHundreds.get(i))) {
							zhushu++;
						}
					}
				}
			}
		}
		return zhushu;
	}

	private long caculateBetNumFourQ2(List<Integer> oneHundreds,
			List<Integer> oneDecades) {
		int zhushu = 0;
		for (int i = 0; i < oneHundreds.size(); i++) {
			for (int j = 0; j < oneDecades.size(); j++) {
				if (!oneHundreds.get(i).equals(oneDecades.get(j))) {
					zhushu++;
				}
			}
		}
		return zhushu;
	}

	private void showPromtFour(int isOneRight, int isTowRight, int i, int j) {
		if (isOneRight == 1 || isTowRight == 1 || isOneRight == 4
				|| isTowRight == 4) {
			if (i == 3 || j == 3) {
				Toast.makeText(this, "至少需要两个小球", Toast.LENGTH_SHORT).show();
			} else if (i == 4 || j == 4) {
				Toast.makeText(this, "至少需要三个小球", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "至少需要一个一位小球", Toast.LENGTH_SHORT).show();
			}

		} else if (isOneRight == 2 || isTowRight == 2) {
			Toast.makeText(this, "至少需要一个二位小球", Toast.LENGTH_SHORT).show();
		} else if (isOneRight == 3 || isTowRight == 3) {
			Toast.makeText(this, "至少需要一个三位小球", Toast.LENGTH_SHORT).show();
		}

		if (isOneRight == 5 || isTowRight == 5) {
			Toast.makeText(this, "请选择正确的投注号码", Toast.LENGTH_SHORT).show();
		}
	}

	private int isBetLegitimacyFour(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits, int i) {
		int legalState = -1;
		if (i == 0) {
			if (oneHundreds.size() == 0) {
				legalState = 4;
			} else if (oneHundreds.size() > 0) {
				legalState = 0;
			} else {
				legalState = 1;
			}
		} else if (i == 1) {
			if (oneHundreds.size() == 0 && oneDecades.size() == 0) {
				legalState = 4;
			} else if (oneHundreds.size() > 0 && oneDecades.size() > 0) {
				legalState = 0;
			} else if (oneHundreds.size() <= 0) {
				legalState = 1;
			} else if (oneDecades.size() <= 0) {
				legalState = 2;
			}

			if ((caculateBetNumFour(oneHundreds, oneDecades, oneUnits, i) == 0)
					&& ((oneHundreds.size() + oneDecades.size()) != 0)) {
				legalState = 5;
			}
		} else if (i == 2) {
			if (oneHundreds.size() == 0 && oneDecades.size() == 0
					&& oneUnits.size() == 0) {
				legalState = 4;
			} else if (oneHundreds.size() > 0 && oneDecades.size() > 0
					&& oneUnits.size() > 0) {
				legalState = 0;
			} else if (oneHundreds.size() <= 0) {
				legalState = 1;
			} else if (oneDecades.size() <= 0) {
				legalState = 2;
			} else if (oneUnits.size() <= 0) {
				legalState = 3;
			}

			if ((caculateBetNumFour(oneHundreds, oneDecades, oneUnits, i) == 0)
					&& ((oneHundreds.size() + oneDecades.size() + oneUnits
							.size()) != 0)) {
				legalState = 5;
			}
		} else if (i == 3) {
			if (oneHundreds.size() == 0) {
				legalState = 4;
			} else if (oneHundreds.size() >= 2) {
				legalState = 0;
			} else {
				legalState = 1;
			}
		} else if (i == 4) {
			if (oneHundreds.size() == 0) {
				legalState = 4;
			} else if (oneHundreds.size() >= 3) {
				legalState = 0;
			} else {
				legalState = 1;
			}
		}

		return legalState;
	}

	private void setTouZhuInfoThree(List<Integer> redListOne,
			List<Integer> redListTwo, int oneSelectItem, int towSelectItem) {
		String lotno = "";
		String code = "";
		long betNums1 = 0;
		long betNums2 = 0;
		int oneRight = isBetLegitimacyThree(redListOne, oneSelectItem);
		int twoRight = -1;
		if (oneRight == -1 || oneRight == 0) {
			twoRight = isBetLegitimacyThree(redListTwo, towSelectItem);
		}

		if (((oneRight == 0) && (twoRight == 0))
				|| ((oneRight == 0) && (twoRight == -1))
				|| ((oneRight == -1) && (twoRight == 0))) {
			if (oneRight == 0) {
				if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_DLC_LISTVIEW) {
					code = DlcCode.simulateZhuma(redListOne, oneSelectItem);
					lotno = Constants.LOTNO_11_5;
				} else if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW) {
					code = GdelevenCode
							.simulateZhuma(redListOne, oneSelectItem);
					lotno = Constants.LOTNO_GD_11_5;
				}
				betNums1 = caculateBetThree(redListOne.size(), oneSelectItem);
				addViewAndTouZhuThree(betNums1, redListOne, lotno, code);

			}

			if (twoRight == 0) {
				if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_DLC_LISTVIEW) {
					code = DlcCode.simulateZhuma(redListTwo, towSelectItem);
					lotno = Constants.LOTNO_11_5;
				} else if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW) {
					code = GdelevenCode
							.simulateZhuma(redListTwo, towSelectItem);
					lotno = Constants.LOTNO_GD_11_5;
				}
				betNums2 = caculateBetThree(redListTwo.size(), towSelectItem);
				addViewAndTouZhuThree(betNums2, redListTwo, lotno, code);
			}

			if (betNums1 <= 10000 && betNums2 <= 10000) {
				Intent intent = new Intent(this, HghtOrderdeail.class);
				intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
				intent.putExtra("isAlert", true);
				startActivity(intent);
			}

		} else {
			showPromtThree(oneRight, twoRight, oneSelectItem, towSelectItem);
		}
	}

	private void addViewAndTouZhuThree(long betNums, List<Integer> redListTwo,
			String lotno, String code) {
		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			if (betAndGiftPojo == null) {
				betAndGiftPojo = new BetAndGiftPojo();
			}
			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");
			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");


			if (addView == null) {
				addView = new AddView(this);
			}

			CodeInfo codeInfo = addView.initCodeInfo(2, 1);
			codeInfo.setTouZhuCode(code);
			codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
			codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
			codeInfo = setCodeInfoColorThree(codeInfo, redListTwo);
			addView.addCodeInfo(codeInfo);
			betAndGiftPojo.setBet_code(addView.getTouzhuCode(1,
					betAndGiftPojo.getAmt() * 100));

			ApplicationAddview app = (ApplicationAddview) getApplicationContext();
			app.setPojo(betAndGiftPojo);
			app.setAddview(addView);
			app.setHtextzhuma(addView.getsharezhuma());
			app.setHzhushu(addView.getAllZhu());
		}
	}

	private CodeInfo setCodeInfoColorThree(CodeInfo codeInfo,
			List<Integer> redListTwo) {
		StringBuffer hunderdsString = new StringBuffer();

		int hundredSize = redListTwo.size();

		for (int hundred = 0; hundred < hundredSize; hundred++) {
			hunderdsString.append(integerToString(redListTwo.get(hundred))
					+ ",");
		}
		String hunderds = hunderdsString.substring(0,
				hunderdsString.length() - 1);
		codeInfo.addAreaCode(hunderds, Color.RED);

		return codeInfo;
	}

	private void showPromtThree(int oneRight, int twoRight, int oneSelectItem,
			int twoSelectItem) {
		if (oneRight == -1 && twoRight == -1) {
			Toast.makeText(this, "还需要" + oneSelectItem + "个小球",
					Toast.LENGTH_SHORT).show();
		} else if (oneRight > 0 && (twoRight == 0 || twoRight == -1)) {
			Toast.makeText(this, "还需要" + oneRight + "个小球", Toast.LENGTH_SHORT)
					.show();
		} else if ((oneRight == 0 || oneRight == -1) && twoRight > 0) {
			Toast.makeText(this, "还需要" + twoRight + "个小球", Toast.LENGTH_SHORT)
					.show();
		} else if (oneRight > 0 && twoRight > 0) {
			Toast.makeText(this, "还需要" + oneRight + "个小球", Toast.LENGTH_SHORT)
					.show();
		} else if (oneRight == -2
				|| twoRight == -2
				&& NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW) {
			Toast.makeText(this, "只能选择8个小球进行投注", Toast.LENGTH_SHORT).show();
		}
	}

	private int isBetLegitimacyThree(List<Integer> redListOne, int selectNum) {
		int legalState = -1;
		if (redListOne.size() == 0) {
			legalState = -1;
		} else if (redListOne.size() > 0 && redListOne.size() <= selectNum) {
			legalState = selectNum - redListOne.size();
		} else if (redListOne.size() > selectNum) {
			if (selectNum == 8
					&& NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_GD115_LISTVIEW) {
				legalState = -2;
			} else {
				legalState = 0;
			}

		}

		return legalState;
	}

	private void setTouZhuInfoOther(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits,
			List<Integer> twoHundreds, List<Integer> twoDecades,
			List<Integer> twoUnits) {
		String lotno = "";
		String code = "";
		long betNums1 = 0;
		long betNums2 = 0;
		int oneRight = isBetLegitimacyOther(oneHundreds, oneDecades, oneUnits);
		int twoRight = 0;
		if (oneRight == 0 || oneRight == 4) {
			twoRight = isBetLegitimacyOther(twoHundreds, twoDecades, twoUnits);
		}

		if (((oneRight == 0) && (twoRight == 0))
				|| ((oneRight == 0) && (twoRight == 4))
				|| ((oneRight == 4) && (twoRight == 0))) {

			if (oneRight == 0) {
				if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW) {
					code = Fc3dZiZhiXuanCode.simulateZhuma(oneHundreds,
							oneDecades, oneUnits);
					lotno = Constants.LOTNO_FC3D;
				} else {
					code = PL3ZiZhiXuanCode.simulateZhuma(oneHundreds,
							oneDecades, oneUnits);
					lotno = Constants.LOTNO_PL3;
				}

				betNums1 = caculateBetNumsOther(oneHundreds.size(),
						oneDecades.size(), oneUnits.size());
				addViewAndTouZhuOther(betNums1, oneHundreds, oneDecades,
						oneUnits, lotno, code);
			}

			if (twoRight == 0) {
				if (NoticeActivityGroup.LOTNO == NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW) {
					code = Fc3dZiZhiXuanCode.simulateZhuma(twoHundreds,
							twoDecades, twoUnits);
					lotno = Constants.LOTNO_FC3D;
				} else {
					code = PL3ZiZhiXuanCode.simulateZhuma(twoHundreds,
							twoDecades, twoUnits);
					lotno = Constants.LOTNO_PL3;
				}
				betNums2 = caculateBetNumsOther(twoHundreds.size(),
						twoDecades.size(), twoUnits.size());
				addViewAndTouZhuOther(betNums2, twoHundreds, twoDecades,
						twoUnits, lotno, code);
			}

			Intent intent = null;
			if (lotno == Constants.LOTNO_PL3) {
				intent = new Intent(this,
						com.ruyicai.activity.buy.zixuan.OrderDetails.class);
			} else {
				intent = new Intent(this, OrderDetails.class);
			}

			if (betNums1 <= 10000 && betNums2 <= 10000) {
				intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
				intent.putExtra("isAlert", true);
				startActivity(intent);
			}

		} else {
			showPromtOther(oneRight, twoRight);
		}
	}

	private void showPromtOther(int oneRight, int twoRight) {
		if (oneRight == 1 || twoRight == 1 || oneRight == 4 || twoRight == 4) {
			Toast.makeText(this, "至少需要一个百位小球", Toast.LENGTH_SHORT).show();
		} else if (oneRight == 2 || twoRight == 2) {
			Toast.makeText(this, "至少需要一个十位小球", Toast.LENGTH_SHORT).show();
		} else if (oneRight == 3 || twoRight == 3) {
			Toast.makeText(this, "至少需要一个个位小球", Toast.LENGTH_SHORT).show();
		}
	}

	private void addViewAndTouZhuOther(long betNums, List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits, String lotno,
			String code) {
		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			if (betAndGiftPojo == null) {
				betAndGiftPojo = new BetAndGiftPojo();
			}
			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");
			betAndGiftPojo.setBet_code("");
			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");

			if (lotno.equals(Constants.LOTNO_DLT)) {
				betAndGiftPojo.setZhui(true);
			}

			ApplicationAddview app = (ApplicationAddview) getApplicationContext();

			if (lotno == Constants.LOTNO_PL3) {
				if (addView == null) {
					addView = new AddView(this);
				}
				com.ruyicai.activity.buy.zixuan.AddView.CodeInfo codeInfo = addView
						.initCodeInfo(2, 1);
				codeInfo.setTouZhuCode(code);
				codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
				codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
				codeInfo = setCodeInfoColorOther(codeInfo, oneHundreds,
						oneDecades, oneUnits);
				addView.addCodeInfo(codeInfo);
				app.setAddview(addView);
			} else {
				if (addViewMiss == null) {
					addViewMiss = new AddViewMiss(this);

				}
				CodeInfoMiss codeInfo = addViewMiss.initCodeInfo(2, 1);
				codeInfo.setTouZhuCode(code);
				codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
				codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
				codeInfo = setCodeInfoColorOther(codeInfo, oneHundreds,
						oneDecades, oneUnits);
				addViewMiss.addCodeInfo(codeInfo);
				app.setAddviewmiss(addViewMiss);
			}
			app.setPojo(betAndGiftPojo);
		}
	}

	private com.ruyicai.activity.buy.zixuan.AddView.CodeInfo setCodeInfoColorOther(
			com.ruyicai.activity.buy.zixuan.AddView.CodeInfo codeInfo,
			List<Integer> oneHundreds, List<Integer> oneDecades,
			List<Integer> oneUnits) {
		StringBuffer hunderdsString = new StringBuffer();
		StringBuffer decadesString = new StringBuffer();
		StringBuffer unitsString = new StringBuffer();

		int hundredSize = oneHundreds.size();
		int decadeSize = oneDecades.size();
		int unitSize = oneUnits.size();

		for (int hundred = 0; hundred < hundredSize; hundred++) {
			hunderdsString.append(oneHundreds.get(hundred) + ",");
		}
		if (hunderdsString.length() > 0) {
			String hunderds = hunderdsString.substring(0,
					hunderdsString.length() - 1);
			codeInfo.addAreaCode(hunderds, Color.BLACK);
		}

		for (int decade = 0; decade < decadeSize; decade++) {
			decadesString.append(oneDecades.get(decade) + ",");
		}
		if (decadesString.length() > 0) {
			String decades = decadesString.substring(0,
					decadesString.length() - 1);
			codeInfo.addAreaCode(decades, Color.BLACK);
		}

		for (int unit = 0; unit < unitSize; unit++) {
			unitsString.append(oneUnits.get(unit) + ",");
		}
		if (unitsString.length() > 0) {
			String units = unitsString.substring(0, unitsString.length() - 1);
			codeInfo.addAreaCode(units, Color.BLACK);
		}

		return codeInfo;
	}

	private CodeInfoMiss setCodeInfoColorOther(CodeInfoMiss codeInfo,
			List<Integer> oneHundreds, List<Integer> oneDecades,
			List<Integer> oneUnits) {
		StringBuffer hunderdsString = new StringBuffer();
		StringBuffer decadesString = new StringBuffer();
		StringBuffer unitsString = new StringBuffer();

		int hundredSize = oneHundreds.size();
		int decadeSize = oneDecades.size();
		int unitSize = oneUnits.size();

		for (int hundred = 0; hundred < hundredSize; hundred++) {
			hunderdsString.append(oneHundreds.get(hundred) + ",");
		}
		if (hunderdsString.length() > 0) {
			String hunderds = hunderdsString.substring(0,
					hunderdsString.length() - 1);
			codeInfo.addAreaCode(hunderds, Color.BLACK);
		}

		for (int decade = 0; decade < decadeSize; decade++) {
			decadesString.append(oneDecades.get(decade) + ",");
		}
		if (decadesString.length() > 0) {
			String decades = decadesString.substring(0,
					decadesString.length() - 1);
			codeInfo.addAreaCode(decades, Color.BLACK);
		}

		for (int unit = 0; unit < unitSize; unit++) {
			unitsString.append(oneUnits.get(unit) + ",");
		}
		if (unitsString.length() > 0) {
			String units = unitsString.substring(0, unitsString.length() - 1);
			codeInfo.addAreaCode(units, Color.BLACK);
		}

		return codeInfo;
	}

	private long caculateBetNumsOther(int size, int size2, int size3) {
		return size * size2 * size3;
	}

	private int isBetLegitimacyOther(List<Integer> oneHundreds,
			List<Integer> oneDecades, List<Integer> oneUnits) {
		int legalState = 0;
		if (oneHundreds.size() == 0 && oneDecades.size() == 0
				&& oneUnits.size() == 0) {
			legalState = 4;
		} else if (oneHundreds.size() == 0) {
			legalState = 1;
		} else if (oneDecades.size() == 0) {
			legalState = 2;
		} else if (oneUnits.size() == 0) {
			legalState = 3;
		}

		return legalState;
	}

	private List<Integer> getListOther(String strOne) {
		List<Integer> unitList = new ArrayList<Integer>();
		if (!strOne.equals("请点击选号二小球选取号码") && !strOne.equals("请点击选号一小球选取号码")
				&& !strOne.equals("")) {
			String[] units = strOne.split(",");
			for (int i = 0; i < units.length; i++) {
				unitList.add(Integer.valueOf(units[i]));
			}
		}

		return unitList;
	}

	private void setTouZhuInfo(List<Integer> redList, List<Integer> blueList,
			List<Integer> redListTwo, List<Integer> blueListTwo) {
		String code1 = "";
		String code2 = "";
		long betNums1 = 0;
		long betNums2 = 0;
		String lotno = "";
		int redNum = 0, blueNum = 0;
		int isOneRight;
		switch (NoticeActivityGroup.LOTNO) {
		case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
			redNum = 6;
			blueNum = 1;
			isOneRight = isBetLegitimacy(redList.size(), blueList.size(),
					redNum, blueNum);

			int isTowRight = 0;
			if (isOneRight == 0 || isOneRight == 3) {
				isTowRight = isBetLegitimacy(redListTwo.size(),
						blueListTwo.size(), redNum, blueNum);
			}

			if (((isOneRight == 0) && (isTowRight == 0))
					|| ((isOneRight == 0) && (isTowRight == 3))
					|| ((isOneRight == 3) && (isTowRight == 0))) {
				lotno = Constants.LOTNO_SSQ;

				if (isOneRight == 0) {
					code1 = SsqZiZhiXuanCode.simulateZhuma(redList, blueList);
					betNums1 = caculateBetNums(redList.size(), blueList.size(),
							redNum, blueNum);
				}

				if (isTowRight == 0) {
					code2 = SsqZiZhiXuanCode.simulateZhuma(redListTwo,
							blueListTwo);
					betNums2 = caculateBetNums(redListTwo.size(),
							blueListTwo.size(), redNum, blueNum);
				}

				if (betNums1 <= 10000 && betNums2 <= 10000) {
					if (isOneRight == 0) {
						addViewAndTouZhu(betNums1, redList, blueList, lotno,
								code1);
					}

					if (isTowRight == 0) {
						addViewAndTouZhu(betNums2, redListTwo, blueListTwo,
								lotno, code2);
					}

					Intent intent = new Intent(this, OrderDetails.class);
					intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
					intent.putExtra("isAlert", true);
					startActivity(intent);
				} else {
					dialogExcessive(10000);
				}

			} else {
				showPromt(isOneRight, isTowRight, redNum, blueNum);
			}

			break;
		case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
			redNum = 7;

			isOneRight = isBetLegitimacy(redList.size(), blueList.size(),
					redNum, blueNum);

			isTowRight = 0;
			if (isOneRight == 0 || isOneRight == 3) {
				isTowRight = isBetLegitimacy(redListTwo.size(),
						blueListTwo.size(), redNum, blueNum);
			}

			if (((isOneRight == 0) && (isTowRight == 0))
					|| ((isOneRight == 0) && (isTowRight == 3))
					|| ((isOneRight == 3) && (isTowRight == 0))) {
				lotno = Constants.LOTNO_QLC;

				if (isOneRight == 0) {
					code1 = QlcZiZhiXuanCode.simulateZhuma(redList, blueList);
					betNums1 = caculateBetNums(redList.size(), blueList.size(),
							redNum, blueNum);
				}

				if (isTowRight == 0) {
					code2 = QlcZiZhiXuanCode.simulateZhuma(redListTwo,
							blueListTwo);
					betNums2 = caculateBetNums(redListTwo.size(),
							blueListTwo.size(), redNum, blueNum);
				}

				if (betNums1 <= 10000 && betNums2 <= 10000) {
					if (isOneRight == 0) {
						addViewAndTouZhu(betNums1, redList, blueList, lotno,
								code1);
					}
					if (isTowRight == 0) {
						addViewAndTouZhu(betNums2, redListTwo, blueListTwo,
								lotno, code2);
					}
					Intent intent = new Intent(this,
							com.ruyicai.activity.buy.zixuan.OrderDetails.class);
					intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
					intent.putExtra("isAlert", true);
					startActivity(intent);
				} else {
					dialogExcessive(10000);
				}

			} else {
				showPromt(isOneRight, isTowRight, redNum, blueNum);
			}

			break;
		case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
			redNum = 5;
			blueNum = 2;

			isOneRight = isBetLegitimacy(redList.size(), blueList.size(),
					redNum, blueNum);

			isTowRight = 0;
			if (isOneRight == 0 || isOneRight == 3) {
				isTowRight = isBetLegitimacy(redListTwo.size(),
						blueListTwo.size(), redNum, blueNum);
			}

			if (((isOneRight == 0) && (isTowRight == 0))
					|| ((isOneRight == 0) && (isTowRight == 3))
					|| ((isOneRight == 3) && (isTowRight == 0))) {
				lotno = Constants.LOTNO_DLT;

				if (isOneRight == 0) {
					code1 = DltNormalSelectCode
							.simulateZhuma(redList, blueList);
					betNums1 = caculateBetNums(redList.size(), blueList.size(),
							redNum, blueNum);
				}

				if (isTowRight == 0) {
					code2 = DltNormalSelectCode.simulateZhuma(redListTwo,
							blueListTwo);
					betNums2 = caculateBetNums(redListTwo.size(),
							blueListTwo.size(), redNum, blueNum);
				}

				if (betNums1 <= 10000 && betNums2 <= 10000) {
					if (isOneRight == 0) {
						addViewAndTouZhu(betNums1, redList, blueList, lotno,
								code1);
					}

					if (isTowRight == 0) {
						addViewAndTouZhu(betNums2, redListTwo, blueListTwo,
								lotno, code2);
					}
					Intent intent = new Intent(this, OrderDetails.class);
					intent.putExtra("from", BettingSuccessActivity.NOTICEBALL);
					intent.putExtra("isAlert", true);
					startActivity(intent);
				} else {
					dialogExcessive(10000);
				}

			} else {
				showPromt(isOneRight, isTowRight, redNum, blueNum);
			}

			break;

		}
	}

	/**
	 * add by pengcx 20130809 start
	 * 
	 * @param blueNum2
	 */
	private void showPromt(int isOneRight, int isTowRight, int redNum,
			int blueNum) {
		if (isOneRight != 0) {
			if (isOneRight == 1 || isOneRight == 3) {
				Toast.makeText(this, "请至少选择" + redNum + "个红球",
						Toast.LENGTH_SHORT).show();
			} else if (isOneRight == 2) {
				Toast.makeText(this, "请选择" + blueNum + "个蓝球",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			if (isTowRight == 1 || isTowRight == 3) {
				Toast.makeText(this, "请至少选择" + redNum + "个红球",
						Toast.LENGTH_SHORT).show();
			} else if (isTowRight == 2) {
				Toast.makeText(this, "请选择" + blueNum + "个蓝球",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/** add by pengcx 20130809 end */
	private boolean isLogin() {
		boolean isLogin = true;

		shellRW = new RWSharedPreferences(this, "addInfo");
		String sessionidStr = shellRW.getStringValue("sessionid");
		if (sessionidStr == null || sessionidStr.equals("")) {
			isLogin = false;
		}

		return isLogin;
	}

	private int isBetLegitimacy(int redSize, int blueSize, int redNum,
			int blueNum) {

		int legalState = 0;

		if (redSize == 0 && blueSize == 0) {
			legalState = 3;
		} else if (redSize < redNum) {
			legalState = 1;
		} else if (blueSize < blueNum) {
			legalState = 2;
		}

		return legalState;
	}

	private void addViewAndTouZhu(long betNums, List<Integer> reds,
			List<Integer> blues, String lotno, String code) {

		if (betNums > 10000) {
			dialogExcessive(10000);
		} else {
			if (betAndGiftPojo == null) {
				betAndGiftPojo = new BetAndGiftPojo();
			}
			String sessionid = shellRW.getStringValue("sessionid");
			String phonenum = shellRW.getStringValue("phonenum");
			String userno = shellRW.getStringValue("userno");

			betAndGiftPojo.setSessionid(sessionid);
			betAndGiftPojo.setPhonenum(phonenum);
			betAndGiftPojo.setUserno(userno);
			betAndGiftPojo.setBettype("bet");
			betAndGiftPojo.setBet_code("");
			betAndGiftPojo.setLotmulti("1");
			betAndGiftPojo.setBatchnum("1");
			betAndGiftPojo.setSellway("0");
			betAndGiftPojo.setLotno(lotno);
			betAndGiftPojo.setZhushu(String.valueOf(betNums));
			betAndGiftPojo.setAmount("" + betNums * 200);
			betAndGiftPojo.setIsSellWays("1");

			if (lotno.equals(Constants.LOTNO_DLT)) {
				betAndGiftPojo.setZhui(true);
			}

			ApplicationAddview app = (ApplicationAddview) getApplicationContext();

			if (lotno == Constants.LOTNO_QLC) {
				if (addView == null) {
					addView = new AddView(this);
				}
				com.ruyicai.activity.buy.zixuan.AddView.CodeInfo codeInfo = addView
						.initCodeInfo(2, 1);
				codeInfo.setTouZhuCode(code);
				codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
				codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
				codeInfo = setCodeInfoColor(codeInfo, reds, blues);
				addView.addCodeInfo(codeInfo);
				app.setAddview(addView);
			} else {

				if (addViewMiss == null) {
					addViewMiss = new AddViewMiss(this);
				}
				CodeInfoMiss codeInfo = addViewMiss.initCodeInfo(2, 1);
				codeInfo.setTouZhuCode(code);
				codeInfo.setZhuShu(Integer.valueOf(String.valueOf(betNums)));
				codeInfo.setAmt(Integer.valueOf(String.valueOf(betNums * 2)));
				codeInfo = setCodeInfoColor(codeInfo, reds, blues);
				addViewMiss.addCodeInfo(codeInfo);
				app.setAddviewmiss(addViewMiss);
			}
			app.setPojo(betAndGiftPojo);

		}
	}

	public void dialogExcessive(int maxNums) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(this.getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于" + maxNums + "注！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	private CodeInfoMiss setCodeInfoColor(CodeInfoMiss codeInfo, List<Integer> redList,
			List<Integer> blueList) {
		StringBuffer redString = new StringBuffer();
		StringBuffer blueString = new StringBuffer();

		int redSize = redList.size();
		int blueSize = blueList.size();

		for (int red = 0; red < redSize - 1; red++) {
			redString.append(integerToString(redList.get(red)) + ",");
		}

		redString.append(integerToString(redList.get(redSize - 1)));
		codeInfo.addAreaCode(redString.toString(), Color.RED);

		if (blueSize != 0) {
			for (int blue = 0; blue < blueSize - 1; blue++) {
				blueString.append(integerToString(blueList.get(blue)) + ",");
			}

			blueString.append(integerToString(blueList.get(blueSize - 1)));
			codeInfo.addAreaCode(blueString.toString(), Color.BLUE);
		}

		return codeInfo;
	}

	private com.ruyicai.activity.buy.zixuan.AddView.CodeInfo setCodeInfoColor(
			com.ruyicai.activity.buy.zixuan.AddView.CodeInfo codeInfo,
			List<Integer> redList, List<Integer> blueList) {
		StringBuffer redString = new StringBuffer();
		StringBuffer blueString = new StringBuffer();

		int redSize = redList.size();
		int blueSize = blueList.size();

		for (int red = 0; red < redSize - 1; red++) {
			redString.append(integerToString(redList.get(red)) + ",");
		}

		redString.append(integerToString(redList.get(redSize - 1)));
		codeInfo.addAreaCode(redString.toString(), Color.RED);

		if (blueSize != 0) {
			for (int blue = 0; blue < blueSize - 1; blue++) {
				blueString.append(integerToString(blueList.get(blue)) + ",");
			}

			blueString.append(integerToString(blueList.get(blueSize - 1)));
			codeInfo.addAreaCode(blueString.toString(), Color.BLUE);
		}

		return codeInfo;
	}

	private String integerToString(Integer integer) {
		String result = null;
		if (integer < 10) {
			result = "0" + integer.toString();
		} else {
			result = integer.toString();
		}

		return result;
	}

	private long caculateBetNums(int redSize, int blueSize, int redNum,
			int blueNum) {
		long betNums = PublicMethod.zuhe(redNum, redSize)
				* PublicMethod.zuhe(blueNum, blueSize);
		return betNums;
	}

	private long caculateBetThree(int redSize, int redNum) {
		long betNums = PublicMethod.zuhe(redNum, redSize);
		return betNums;
	}

	/**
	 * 子列表中相应的数据
	 */
	protected static List<JSONObject> getSubInfoForListView(String lotno) {

		if (lotno == null || lotno.equals("")) {
			return null;
		}
		List<JSONObject> _list = new ArrayList<JSONObject>();
		if (lotno.equals(Constants.LOTNO_GD115)) {
			// 广东11-5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_GD115, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.gd115List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd115List.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.gd115List.add(tempObj);
				}
			}

		} else if (lotno.equals(Constants.LOTNO_ten)) {
			// 广东快乐十分
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_ten, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.gd10List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.gd10List.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "0000000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.gd10List.add(tempObj);
				}
			}

		} else if (lotno.equals(Constants.LOTNO_SSQ)) {
			// 双色球信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSQ, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");

				String missString = MissInterface.getInstance().betMiss(
						Constants.LOTNO_SSQ, MissConstant.SSQ_Miss);
				JSONObject missJson = new JSONObject(missString);
				String errorcode = missJson.getString("error_code");
				if (errorcode.equals("0000")) {
					JSONObject resultJson = missJson.getJSONObject("result");
					JSONArray redJsonArray = resultJson.getJSONArray("red");
					for (int i = 0; i < redJsonArray.length(); i++) {
						missRed.add(String.valueOf(redJsonArray.get(i)));
					}
					JSONArray blueJsonArray = resultJson.getJSONArray("blue");
					for (int i = 0; i < blueJsonArray.length(); i++) {
						missBlue.add(String.valueOf(blueJsonArray.get(i)));
					}
				}
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.ssqNoticeList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _ssq = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_ssq);
						Constants.ssqNoticeList.add(_ssq);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.ssqNoticeList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_DLT)) {
			// 大乐透信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_DLT, "50");
				String missString = MissInterface.getInstance().betMiss(
						Constants.LOTNO_DLT, MissConstant.DLT_Miss);
				JSONObject missJson = new JSONObject(missString);
				String errorcode = missJson.getString("error_code");
				if (errorcode.equals("0000")) {
					JSONObject resultJson = missJson.getJSONObject("result");
					JSONArray redJsonArray = resultJson.getJSONArray("qian");
					for (int i = 0; i < redJsonArray.length(); i++) {
						missRed.add(String.valueOf(redJsonArray.get(i)));
					}
					JSONArray blueJsonArray = resultJson.getJSONArray("hou");
					for (int i = 0; i < blueJsonArray.length(); i++) {
						missBlue.add(String.valueOf(blueJsonArray.get(i)));
					}
				}

				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.dltList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.dltList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取大乐透数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.dltList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_FC3D)) {
			// 福彩3D信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_FC3D, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.fc3dList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.fc3dList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取福彩3D数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
							tempObj.put("tryCode", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.fc3dList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL3)) {
			// 排列3信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL3, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.pl3List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.pl3List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列3数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.pl3List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_PL5)) {
			// 排列5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_PL5, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.pl5List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.pl5List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取排列5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.pl5List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QXC)) {
			// 七星彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QXC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.qxcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.qxcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七星彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.qxcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_QLC)) {
			// 七乐彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_QLC, "50");
				String missString = MissInterface.getInstance().betMiss(
						Constants.LOTNO_QLC, MissConstant.QLC_Miss);
				JSONObject missJson = new JSONObject(missString);
				String errorcode = missJson.getString("error_code");
				if (errorcode.equals("0000")) {
					JSONObject resultJson = missJson.getJSONObject("result");
					JSONArray redJsonArray = resultJson.getJSONArray("miss");
					for (int i = 0; i < redJsonArray.length(); i++) {
						missRed.add(String.valueOf(redJsonArray.get(i)));
					}
				}
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.qlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.qlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取七乐彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.qlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SSC)) {
			// 时时彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SSC, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.sscList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.sscList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取时时彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.sscList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_SFC)) {
			// 足彩胜负彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_SFC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.sfcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.sfcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩胜负彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.sfcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_RX9)) {
			// 足彩任选9信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_RX9, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.rx9List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.rx9List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩任选9数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.rx9List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_LCB)) {
			// 足彩六场半信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_LCB, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.half6List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.half6List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取足彩六场半数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.half6List.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_JQC)) {
			// 进球彩信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_JQC, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.jqcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.jqcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取进球彩数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.jqcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_11_5)) {
			// 11_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_11_5, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.dlcList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.dlcList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.dlcList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_eleven)) {
			// 11运夺金信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_eleven, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.ydjList.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.ydjList.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取11运夺金数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.ydjList.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_22_5)) {
			// 22_5信息获取
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_22_5, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.twentylist.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.twentylist.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取22_5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.twentylist.add(tempObj);
				}
			}
		} else if (lotno.equals(Constants.LOTNO_NMK3)) {
			// 内蒙快三获取信息
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_NMK3, "50");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.nmk3List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject _nmk3 = (JSONObject) jsonArrayByLotno.get(i);
						_list.add(_nmk3);
						Constants.nmk3List.add(_nmk3);
					}
				}
			} catch (Exception e) {
				// 获取双色球数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.nmk3List.add(tempObj);
				}
			}
		}else if (lotno.equals(Constants.LOTNO_CQ_ELVEN_FIVE)) {
			// 重庆11选5
			try {
				JSONObject jsonObjectByLotno = getJSONByLotno(
						Constants.LOTNO_CQ_ELVEN_FIVE, "100");
				JSONArray jsonArrayByLotno = jsonObjectByLotno
						.getJSONArray("result");
				if (jsonArrayByLotno != null && jsonArrayByLotno.length() > 0) {
					NoticeMainActivity.isFirstNotice = false;
					_list.clear();
					Constants.cq11x5List.clear();
					for (int i = 0; i < jsonArrayByLotno.length(); i++) {
						JSONObject jsonObject = (JSONObject) jsonArrayByLotno
								.get(i);
						_list.add(jsonObject);
						Constants.cq11x5List.add(jsonObject);
					}
				}
			} catch (Exception e) {
				// 获取重庆11选5数据出现异常
				e.printStackTrace();
			} finally {
				// 判断是否已经从网络上获取到了数据
				if (_list == null || _list.size() == 0) {
					// 没数据,初始化点数居
					JSONObject tempObj = new JSONObject();
					for (int i = 0; i < 5; i++) {
						try {
							tempObj.put("lotno", "");
							tempObj.put("winno", "00000000000000");
							tempObj.put("date", "");
						} catch (JSONException e) {

						}
					}
					_list.add(tempObj);
					Constants.cq11x5List.add(tempObj);
				}
			}
		}

		return _list;
	}

	private static JSONObject getJSONByLotno(String lotnoString,
			String maxresultString) {
		JSONObject jsonObjectByLotno = PrizeInfoInterface.getInstance()
				.getNoticePrizeInfo(lotnoString, "1", maxresultString);
		return jsonObjectByLotno;
	}

	/**
	 * 将获取到的开奖信息放到常量类中
	 */
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	protected void onStop() {
		super.onStop();
		clearSelectedBall();
		
		textRedCodeOne.setText("请点击选号一小球选取号码");
		textBlueCodeOne.setText("");
		textRedCodeTwo.setText("请点击选号二小球选取号码");
		textBlueCodeTwo.setText("");
		textThreeCodeOne.setText("");
		textThreeCodeTwo.setText("");
	}

	private void clearSelectedBall() {
		if (isLogin()) {
			switch (NoticeActivityGroup.LOTNO) {
			// 广东11-5
			case NoticeActivityGroup.ID_SUB_GD115_LISTVIEW:
				if (isBeforeThree) {
					ballSelectedRedView.resetSelect();
					
				} else {
					ballSelectedRedView.resetSelect();
				}
				ballSelectedRedView.invalidate();
				break;
			case NoticeActivityGroup.ID_SUB_SHUANGSEQIU_LISTVIEW:
				ballSelectedRedView.resetSelect();
				ballSelectedBlueView.resetSelect();
				ballSelectedRedView.invalidate();
				ballSelectedBlueView.invalidate();
				break;
			case NoticeActivityGroup.ID_SUB_FUCAI3D_LISTVIEW:
				ballSelectedRedView.resetSelect();
				ballSelectedRedView.invalidate();
				break;
			case NoticeActivityGroup.ID_SUB_QILECAI_LISTVIEW:
				ballSelectedRedView.resetSelect();
				ballSelectedBlueView.resetSelect();
				ballSelectedRedView.invalidate();
				ballSelectedBlueView.invalidate();
				break;
			case NoticeActivityGroup.ID_SUB_PAILIESAN_LISTVIEW:
				ballSelectedRedView.resetSelect();
				break;
			case NoticeActivityGroup.ID_SUB_DLT_LISTVIEW:
				ballSelectedRedView.resetSelect();
				ballSelectedBlueView.resetSelect();
				ballSelectedRedView.invalidate();
				ballSelectedBlueView.invalidate();
				break;
			case NoticeActivityGroup.ID_SUB_DLC_LISTVIEW:
				if (isBeforeThree) {
					ballSelectedRedView.resetSelect();
				} else {
					ballSelectedRedView.resetSelect();
				}
				ballSelectedRedView.invalidate();
				break;
			}
		}
	}
}