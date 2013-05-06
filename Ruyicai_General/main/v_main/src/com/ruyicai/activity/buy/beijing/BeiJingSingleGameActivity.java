package com.ruyicai.activity.buy.beijing;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BuyGameDialog;
import com.ruyicai.activity.buy.beijing.adapter.HalfTheAudienceAdapter;
import com.ruyicai.activity.buy.beijing.adapter.OverAllAdapter;
import com.ruyicai.activity.buy.beijing.adapter.TotalGoalsAdapter;
import com.ruyicai.activity.buy.beijing.adapter.UpDownSingleDoubleAdapter;
import com.ruyicai.activity.buy.beijing.adapter.WinTieLossAdapter;
import com.ruyicai.activity.buy.beijing.bean.HalfTheAudienceAgainstInformation;
import com.ruyicai.activity.buy.beijing.bean.OverAllAgainstInformation;
import com.ruyicai.activity.buy.beijing.bean.PlayMethodEnum;
import com.ruyicai.activity.buy.beijing.bean.TotalGoalsAgainstInformation;
import com.ruyicai.activity.buy.beijing.bean.UpDownSingleDoubleAgainstInformation;
import com.ruyicai.activity.buy.beijing.bean.WinTieLossAgainstInformation;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreActivity;
import com.ruyicai.activity.notice.NoticeBeijingSingleActivity;

import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.net.newtransaction.BeiJingSingleGameInterface;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 北京单场页面
 * 
 * @author Administrator
 * 
 */
public class BeiJingSingleGameActivity extends Activity {
	private static final String TAG = "BeiJingSingleGameActivity";
	/** 玩法彩种编号 */
	private String lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS;
	/** 标题 */
	private TextView titleTextView;
	/** 菜单按钮 */
	private Button menuButton;
	/** 玩法切换按钮 */
	private Button playMethodChangeButton;
	/** 赛事选择按钮 */
	private Button eventSelectButton;
	/** 即时比分按钮 */
	private Button realtimeScoreButton;
	/** 客队在前 */
	private TextView guestTeamForwardTextView;
	/** 对阵填充布局 */
	private LinearLayout againstLinearLayout;
	/** 重选按钮 */
	private ImageButton reelectImageButton;
	/** 选择场次 */
	private TextView selectNumTextView;
	/** 投注按钮 */
	private ImageButton bettingImageButton;
	/** 下拉菜单 */
	private PopupWindow menuPopupwindow;
	/**玩法介绍*/
	private BuyGameDialog gameDialog;
	/**context对象*/
	protected Context context;
	/**彩种类型*/
	private String lotNo = Constants.LOTNO_JCL;
	/**玩法回调对象*/
	private Handler gameHandler = new Handler();
	/** 对阵列表视图对象 */
	private View againstView;
	/** 对阵列表 */
	private ListView againstListView;
	/** 玩法切换对话框 */
	private Dialog playMethodChangeDialog;

	/** 赛事选择对话框 */
	private Dialog eventSelectDialog;
	/** 赛事选择按钮集合 */
	private MyButton[] eventSelectButtons;

	/** LayoutInflater对象 */
	private LayoutInflater layoutInflater;
	/** 玩法类型:默认为胜平负 */
	private PlayMethodEnum playMethodType = PlayMethodEnum.WINTIELOSS;

	/** 联赛名称集合 */
	private List<String> eventsList;
	/** 当前选中的赛事集合 */
	private List<String> selectedEventsList;

	/** 胜平负对阵列表适配器 */
	private WinTieLossAdapter winTieLossAdapter;
	/** 总进球数对阵列表适配器 */
	private TotalGoalsAdapter totalGoalsAdapter;
	/** 全场总比分列表适配器 */
	private OverAllAdapter overAllAdapter;
	/** 半全场对阵列表适配器 */
	private HalfTheAudienceAdapter halfTheAudienceAdapter;
	/** 上下单双对阵列表适配器 */
	private UpDownSingleDoubleAdapter upDownSingleDoubleAdapter;

	/** 让球胜平负对阵信息对象集合 */
	private List<List<WinTieLossAgainstInformation>> winTieLossAgainstInformationList;
	/** 当前让球胜平负对阵信息对象集合 */
	private List<List<WinTieLossAgainstInformation>> nowWinTieLossAgainstInformationList;
	/** 总进球对阵信息对象集合 */
	private List<List<TotalGoalsAgainstInformation>> totalGoalsAgainstInformationList;
	/** 当前总进球对阵信息对象集合 */
	private List<List<TotalGoalsAgainstInformation>> nowTotalGoalsAgainstInformationList;
	/** 全场比分对阵信息对象集合 */
	private List<List<OverAllAgainstInformation>> overAllagainstInformationList;
	/** 当前全场比分对阵信息对象集合 */
	private List<List<OverAllAgainstInformation>> nowOverAllagainstInformationList;
	/** 半全场对阵信息对象集合 */
	private List<List<HalfTheAudienceAgainstInformation>> halfTheAudienceagainstInformationList;
	/** 当前半全场对阵信息对象集合 */
	private List<List<HalfTheAudienceAgainstInformation>> nowHalfTheAudienceagainstInformationList;
	/** 上下单双对阵信息对象集合 */
	private List<List<UpDownSingleDoubleAgainstInformation>> upDownSigleDoubleagainstInformationList;
	/** 当前上下单双对阵信息对象集合 */
	private List<List<UpDownSingleDoubleAgainstInformation>> nowUpDownSigleDoubleagainstInformationList;

	/** 从网络获取数据成功标识 */
	private static final int SUCCESS = 0;
	/** 从网络获取数据失败标识 */
	private static final int FAILD = 1;
	/** 网络请求返回消息 */
	private String messageString;
	/** Handler对象 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS:
				refreshAgainstInformationShow(true, true);
				break;
			case FAILD:
				Toast.makeText(BeiJingSingleGameActivity.this, messageString,
						Toast.LENGTH_LONG).show();
				againstLinearLayout.removeAllViews();
				break;
			}
		}
	};

	/**
	 * 刷新对阵列表的显示
	 * 
	 * @param b
	 */
	public void refreshAgainstInformationShow(boolean isReset, boolean isCleared) {
		againstLinearLayout.removeAllViews();

		switch (playMethodType) {
		case WINTIELOSS:
			getNowShowWinTieLossAgainstInformation(isCleared);
			if (winTieLossAdapter == null) {
				winTieLossAdapter = new WinTieLossAdapter(
						BeiJingSingleGameActivity.this,
						nowWinTieLossAgainstInformationList);
			} else {
				winTieLossAdapter.notifyDataSetChanged();
			}

			if (isReset) {
				againstListView.setAdapter(winTieLossAdapter);
			}

			break;
		case TOTALGOALS:
			getNowShowTotalGoalsAgainstInformation(isCleared);
			if (totalGoalsAdapter == null) {
				totalGoalsAdapter = new TotalGoalsAdapter(
						BeiJingSingleGameActivity.this,
						nowTotalGoalsAgainstInformationList);
			} else {
				totalGoalsAdapter.notifyDataSetChanged();
			}

			if (isReset) {
				againstListView.setAdapter(totalGoalsAdapter);

			}
			break;
		case OVERALL:
			getNowShowOverAllAgainstInformation(isCleared);
			if (overAllAdapter == null) {
				overAllAdapter = new OverAllAdapter(
						BeiJingSingleGameActivity.this,
						nowOverAllagainstInformationList);
			} else {
				overAllAdapter.notifyDataSetChanged();
			}
			
			if (isReset) {
				againstListView.setAdapter(overAllAdapter);
			}
			

			break;
		case HALFTHEAUDIENCE:
			getNowShowHalfTheAudienceAgainstInformation(isCleared);
			if (halfTheAudienceAdapter == null) {
				halfTheAudienceAdapter = new HalfTheAudienceAdapter(
						BeiJingSingleGameActivity.this,
						nowHalfTheAudienceagainstInformationList);
			} else {
				halfTheAudienceAdapter.notifyDataSetChanged();
			}
			
			if (isReset) {
				againstListView.setAdapter(halfTheAudienceAdapter);
			}
			
			break;
		case UPDOWNSINGLEDOUBLE:
			getNowShowUpDownSingleDoubleAgainstInformation(isCleared);
			if (upDownSingleDoubleAdapter == null) {
				upDownSingleDoubleAdapter = new UpDownSingleDoubleAdapter(
						BeiJingSingleGameActivity.this,
						nowUpDownSigleDoubleagainstInformationList);
			} else {
				upDownSingleDoubleAdapter.notifyDataSetChanged();
			}

			if (isReset) {
				againstListView.setAdapter(upDownSingleDoubleAdapter);
			}
			
			break;
		}

		againstLinearLayout.addView(againstView);
	}

	private void getNowShowUpDownSingleDoubleAgainstInformation(
			boolean isCleared) {

		if (nowUpDownSigleDoubleagainstInformationList == null) {
			nowUpDownSigleDoubleagainstInformationList = new ArrayList<List<UpDownSingleDoubleAgainstInformation>>();
		}
		if (isCleared) {
			nowUpDownSigleDoubleagainstInformationList.clear();
			int listnum = upDownSigleDoubleagainstInformationList.size();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = upDownSigleDoubleagainstInformationList
						.get(list_i);
				List<UpDownSingleDoubleAgainstInformation> nowUpDownSingleDoubleAgainstInformations = new ArrayList<UpDownSingleDoubleAgainstInformation>();

				int infonum = upDownSingleDoubleAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation = upDownSingleDoubleAgainstInformations
							.get(info_j);
					String league = upDownSingleDoubleAgainstInformation
							.getLeague();
					if (selectedEventsList.contains(league)) {
						nowUpDownSingleDoubleAgainstInformations
								.add(upDownSingleDoubleAgainstInformation.clone());
					}
				}
				nowUpDownSigleDoubleagainstInformationList
						.add(nowUpDownSingleDoubleAgainstInformations);
			}
		}

	}

	private void getNowShowHalfTheAudienceAgainstInformation(boolean isCleared) {

		if (nowHalfTheAudienceagainstInformationList == null) {
			nowHalfTheAudienceagainstInformationList = new ArrayList<List<HalfTheAudienceAgainstInformation>>();
		}

		if (isCleared) {
			nowHalfTheAudienceagainstInformationList.clear();
			int listnum = halfTheAudienceagainstInformationList.size();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = halfTheAudienceagainstInformationList
						.get(list_i);
				List<HalfTheAudienceAgainstInformation> nowHalfTheAudienceAgainstInformations = new ArrayList<HalfTheAudienceAgainstInformation>();

				int infonum = halfTheAudienceAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation = halfTheAudienceAgainstInformations
							.get(info_j);
					String league = halfTheAudienceAgainstInformation
							.getLeague();
					if (selectedEventsList.contains(league)) {
						nowHalfTheAudienceAgainstInformations
								.add(halfTheAudienceAgainstInformation.clone());
					}
				}
				nowHalfTheAudienceagainstInformationList
						.add(nowHalfTheAudienceAgainstInformations);
			}
		}

	}

	private void getNowShowOverAllAgainstInformation(boolean isCleared) {
		
		if (nowOverAllagainstInformationList == null) {
			nowOverAllagainstInformationList = new ArrayList<List<OverAllAgainstInformation>>();
		}
		if (isCleared) {
			nowOverAllagainstInformationList.clear();
			int listnum = overAllagainstInformationList.size();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<OverAllAgainstInformation> overAllAgainstInformations = overAllagainstInformationList
						.get(list_i);
				List<OverAllAgainstInformation> nowOverAllAgainstInformations = new ArrayList<OverAllAgainstInformation>();

				int infonum = overAllAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					OverAllAgainstInformation overAllAgainstInformation = overAllAgainstInformations
							.get(info_j);
					String league = overAllAgainstInformation.getLeague();
					if (selectedEventsList.contains(league)) {
						nowOverAllAgainstInformations
								.add(overAllAgainstInformation.clone());
					}
				}
				nowOverAllagainstInformationList
						.add(nowOverAllAgainstInformations);
			}
		}

	}

	private void getNowShowTotalGoalsAgainstInformation(boolean isCleared) {
		if (nowTotalGoalsAgainstInformationList == null) {
			nowTotalGoalsAgainstInformationList = new ArrayList<List<TotalGoalsAgainstInformation>>();
		}

		if (isCleared) {
			nowTotalGoalsAgainstInformationList.clear();
			int listnum = totalGoalsAgainstInformationList.size();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations = totalGoalsAgainstInformationList
						.get(list_i);
				List<TotalGoalsAgainstInformation> nowTotalGoalsAgainstInformations = new ArrayList<TotalGoalsAgainstInformation>();

				int infonum = totalGoalsAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					TotalGoalsAgainstInformation totalGoalsAgainstInformation = totalGoalsAgainstInformations
							.get(info_j);
					String league = totalGoalsAgainstInformation.getLeague();
					if (selectedEventsList.contains(league)) {
						nowTotalGoalsAgainstInformations
								.add(totalGoalsAgainstInformation.clone());
					}
				}
				nowTotalGoalsAgainstInformationList
						.add(nowTotalGoalsAgainstInformations);
			}
		}

	}

	/**
	 * 获取当前显示的胜平负对阵信息
	 * 
	 */
	private void getNowShowWinTieLossAgainstInformation(boolean isCleared) {

		if (nowWinTieLossAgainstInformationList == null) {
			nowWinTieLossAgainstInformationList = new ArrayList<List<WinTieLossAgainstInformation>>();
		}

		if (isCleared) {
			int listnum = winTieLossAgainstInformationList.size();
			nowWinTieLossAgainstInformationList.clear();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<WinTieLossAgainstInformation> winTieLossAgainstInformations = winTieLossAgainstInformationList
						.get(list_i);
				List<WinTieLossAgainstInformation> nowWinTieLossAgainstInformations = new ArrayList<WinTieLossAgainstInformation>();

				int infonum = winTieLossAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					WinTieLossAgainstInformation winTieLossAgainstInformation = winTieLossAgainstInformations
							.get(info_j);
					String league = winTieLossAgainstInformation.getLeague();
					if (selectedEventsList.contains(league)) {
						nowWinTieLossAgainstInformations
								.add(winTieLossAgainstInformation.clone());
					}
				}
				nowWinTieLossAgainstInformationList
						.add(nowWinTieLossAgainstInformations);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		layoutInflater = LayoutInflater.from(this);
        context = this;
		setContentView(R.layout.buy_jc_main_new);

		initTitleBarShow();

		initEventInformationBarShow();

		initBettingBarShow();

		initAgainstInformationListShow();

		getAndnalysisAgainstInformations();
	}

	/**
	 * 初始化对阵列表的显示
	 */
	private void initAgainstInformationListShow() {
		againstLinearLayout = (LinearLayout) findViewById(R.id.buy_lq_mian_layout);
		// 获取对阵布局视图对象
		againstView = layoutInflater.inflate(R.layout.buy_jc_main_view_new,
				null);
		// 对阵列表
		againstListView = (ListView) againstView
				.findViewById(R.id.buy_jc_main_listview);
		// 将对阵列表布局添加到容器中
		againstLinearLayout.addView(againstView);
	}

	/**
	 * 初始化投注栏显示
	 */
	private void initBettingBarShow() {
		reelectImageButton = (ImageButton) findViewById(R.id.buy_zixuan_img_again);
		reelectImageButton.setOnClickListener(new ButtonOnClickListener());

		selectNumTextView = (TextView) findViewById(R.id.buy_jc_main_text_team_num);
		bettingImageButton = (ImageButton) findViewById(R.id.buy_zixuan_img_touzhu);
		bettingImageButton.setOnClickListener(new ButtonOnClickListener());
	}

	/**
	 * 初始化赛事信息栏显示
	 */
	private void initEventInformationBarShow() {
		playMethodChangeButton = (Button) findViewById(R.id.buy_lq_main_btn_type);
		playMethodChangeButton.setOnClickListener(new ButtonOnClickListener());

		eventSelectButton = (Button) findViewById(R.id.buy_lq_main_btn_team);
		eventSelectButton.setVisibility(View.VISIBLE);
		eventSelectButton.setOnClickListener(new ButtonOnClickListener());

		realtimeScoreButton = (Button) findViewById(R.id.buy_lq_main_btn_score);
		realtimeScoreButton.setOnClickListener(new ButtonOnClickListener());

		guestTeamForwardTextView = (TextView) findViewById(R.id.buy_jc_main_text_title);
		guestTeamForwardTextView.setVisibility(View.INVISIBLE);
	}

	/**
	 * 初始化标题栏显示
	 */
	private void initTitleBarShow() {
		titleTextView = (TextView) findViewById(R.id.layout_main_text_title);
		titleTextView.setText(R.string.beijingsinglegame_textview_wintieloss);

		menuButton = (Button) findViewById(R.id.layout_main_img_return);
		menuButton.setOnClickListener(new ButtonOnClickListener());
	}

	/**
	 * 获取并解析对阵信息
	 */
	private void getAndnalysisAgainstInformations() {
		final ProgressDialog connectDialog = UserCenterDialog
				.onCreateDialog(this);
		connectDialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取当前期号
				String nowIssueString = PublicMethod.toNetIssue(lotnoString);
				// 获取当前玩法的当前期对阵数据
				String returnString = BeiJingSingleGameInterface.getInstance()
						.getAgainstInformations(lotnoString, nowIssueString);

				try {
					// 解析网络反馈信息
					JSONObject returnJsonObject = new JSONObject(returnString);
					String errorCodeString = returnJsonObject
							.getString("error_code");
					messageString = returnJsonObject.getString("message");

					Message message = new Message();
					// 如果请求成功，解析对阵信息，然后发出获取并解析成功信息
					if (errorCodeString.equals("0000")) {
						AndnalysisAgainstInformations(returnJsonObject);
						message.what = SUCCESS;
					}
					// 如果请求失败，不解析数据，发出失败消息
					else {
						message.what = FAILD;
					}

					handler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				connectDialog.dismiss();
			}

		}).start();
	}

	/**
	 * 解析对阵数据
	 * 
	 * @param jsonObject
	 *            对阵数据Json对象
	 */
	private void AndnalysisAgainstInformations(JSONObject returnJsonObject) {
		try {
			JSONArray againstJsonArray = returnJsonObject
					.getJSONArray("result");
			analysisEventInformation(returnJsonObject);

			int againstLength = againstJsonArray.length();

			switch (playMethodType) {
			case WINTIELOSS:
				analysisWinTieLossAgainstInformation(againstJsonArray,
						againstLength);
				break;
			case TOTALGOALS:
				analysisTotalGoalsAgainstInformation(againstJsonArray,
						againstLength);
				break;
			case OVERALL:
				analysisOverAllAgainstInformation(againstJsonArray,
						againstLength);
				break;
			case HALFTHEAUDIENCE:
				analysisHalfTheAudienceAgainstInformation(againstJsonArray,
						againstLength);
				break;
			case UPDOWNSINGLEDOUBLE:
				analysisUpDownSingleDoubleAgainstInformation(againstJsonArray,
						againstLength);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析联赛信息
	 * 
	 * @param returnJsonObject
	 *            返回Json对象
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisEventInformation(JSONObject returnJsonObject)
			throws JSONException {
		String eventStrings = returnJsonObject.getString("leagues");
		String[] events = eventStrings.split(";");

		if (eventsList == null) {
			eventsList = new ArrayList<String>();
		} else {
			eventsList.clear();
		}

		if (selectedEventsList == null) {
			selectedEventsList = new ArrayList<String>();
		} else {
			selectedEventsList.clear();
		}

		for (int event_i = 0; event_i < events.length; event_i++) {
			eventsList.add(events[event_i]);
			selectedEventsList.add(events[event_i]);
		}
	}

	/**
	 * 解析上下单双对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @param againstLength
	 *            对阵Json对象的个数
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisUpDownSingleDoubleAgainstInformation(
			JSONArray againstJsonArray, int againstLength) throws JSONException {
		if (upDownSigleDoubleagainstInformationList == null) {
			upDownSigleDoubleagainstInformationList = new ArrayList<List<UpDownSingleDoubleAgainstInformation>>();
		} else {
			upDownSigleDoubleagainstInformationList.clear();
		}

		for (int jsonarray_i = 0; jsonarray_i < againstLength; jsonarray_i++) {
			List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations = new ArrayList<UpDownSingleDoubleAgainstInformation>();
			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				UpDownSingleDoubleAgainstInformation againstInformation = new UpDownSingleDoubleAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setSxds_v1(againstJsonObject
						.getString("sxds_v1"));
				againstInformation.setSxds_v2(againstJsonObject
						.getString("sxds_v2"));
				againstInformation.setSxds_v3(againstJsonObject
						.getString("sxds_v3"));
				againstInformation.setSxds_v4(againstJsonObject
						.getString("sxds_v4"));

				upDownSingleDoubleAgainstInformations.add(againstInformation);
			}
			upDownSigleDoubleagainstInformationList
					.add(upDownSingleDoubleAgainstInformations);
		}
	}

	/**
	 * 解析半全场对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @param againstLength
	 *            对阵Json对象的个数
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisHalfTheAudienceAgainstInformation(
			JSONArray againstJsonArray, int againstLength) throws JSONException {
		if (halfTheAudienceagainstInformationList == null) {
			halfTheAudienceagainstInformationList = new ArrayList<List<HalfTheAudienceAgainstInformation>>();
		} else {
			halfTheAudienceagainstInformationList.clear();
		}

		for (int jsonarray_i = 0; jsonarray_i < againstLength; jsonarray_i++) {
			List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations = new ArrayList<HalfTheAudienceAgainstInformation>();

			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				HalfTheAudienceAgainstInformation againstInformation = new HalfTheAudienceAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setHalf_v00(againstJsonObject
						.getString("half_v00"));
				againstInformation.setHalf_v01(againstJsonObject
						.getString("half_v01"));
				againstInformation.setHalf_v03(againstJsonObject
						.getString("half_v03"));
				againstInformation.setHalf_v11(againstJsonObject
						.getString("half_v11"));
				againstInformation.setHalf_v10(againstJsonObject
						.getString("half_v10"));
				againstInformation.setHalf_v13(againstJsonObject
						.getString("half_v13"));
				againstInformation.setHalf_v33(againstJsonObject
						.getString("half_v33"));
				againstInformation.setHalf_v31(againstJsonObject
						.getString("half_v31"));
				againstInformation.setHalf_v30(againstJsonObject
						.getString("half_v30"));

				halfTheAudienceAgainstInformations.add(againstInformation);
			}
			halfTheAudienceagainstInformationList
					.add(halfTheAudienceAgainstInformations);
		}
	}

	/**
	 * 解析全场总比分对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @param againstLength
	 *            对阵Json对象的个数
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisOverAllAgainstInformation(JSONArray againstJsonArray,
			int againstLength) throws JSONException {
		if (overAllagainstInformationList == null) {
			overAllagainstInformationList = new ArrayList<List<OverAllAgainstInformation>>();
		} else {
			overAllagainstInformationList.clear();
		}

		for (int jsonarray_i = 0; jsonarray_i < againstLength; jsonarray_i++) {
			List<OverAllAgainstInformation> overAllAgainstInformations = new ArrayList<OverAllAgainstInformation>();
			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				OverAllAgainstInformation againstInformation = new OverAllAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setScore_v10(againstJsonObject
						.getString("score_v10"));
				againstInformation.setScore_v20(againstJsonObject
						.getString("score_v20"));
				againstInformation.setScore_v21(againstJsonObject
						.getString("score_v21"));
				againstInformation.setScore_v30(againstJsonObject
						.getString("score_v30"));
				againstInformation.setScore_v31(againstJsonObject
						.getString("score_v31"));
				againstInformation.setScore_v32(againstJsonObject
						.getString("score_v32"));
				againstInformation.setScore_v40(againstJsonObject
						.getString("score_v40"));
				againstInformation.setScore_v41(againstJsonObject
						.getString("score_v41"));
				againstInformation.setScore_v42(againstJsonObject
						.getString("score_v42"));
				againstInformation.setScore_v00(againstJsonObject
						.getString("score_v00"));
				againstInformation.setScore_v11(againstJsonObject
						.getString("score_v11"));
				againstInformation.setScore_v22(againstJsonObject
						.getString("score_v22"));
				againstInformation.setScore_v33(againstJsonObject
						.getString("score_v33"));
				againstInformation.setScore_v01(againstJsonObject
						.getString("score_v01"));
				againstInformation.setScore_v02(againstJsonObject
						.getString("score_v02"));
				againstInformation.setScore_v12(againstJsonObject
						.getString("score_v12"));
				againstInformation.setScore_v03(againstJsonObject
						.getString("score_v03"));
				againstInformation.setScore_v13(againstJsonObject
						.getString("score_v13"));
				againstInformation.setScore_v23(againstJsonObject
						.getString("score_v23"));
				againstInformation.setScore_v04(againstJsonObject
						.getString("score_v04"));
				againstInformation.setScore_v14(againstJsonObject
						.getString("score_v14"));
				againstInformation.setScore_v24(againstJsonObject
						.getString("score_v24"));
				againstInformation.setScore_v90(againstJsonObject
						.getString("score_v90"));
				againstInformation.setScore_v99(againstJsonObject
						.getString("score_v99"));
				againstInformation.setScore_v09(againstJsonObject
						.getString("score_v09"));
				overAllAgainstInformations.add(againstInformation);
			}
			overAllagainstInformationList.add(overAllAgainstInformations);
		}
	}

	/**
	 * 解析总进球数对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @param againstLength
	 *            对阵Json对象的个数
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisTotalGoalsAgainstInformation(
			JSONArray againstJsonArray, int againstLength) throws JSONException {
		if (totalGoalsAgainstInformationList == null) {
			totalGoalsAgainstInformationList = new ArrayList<List<TotalGoalsAgainstInformation>>();
		} else {
			totalGoalsAgainstInformationList.clear();
		}

		for (int jsonarray_i = 0; jsonarray_i < againstLength; jsonarray_i++) {
			List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations = new ArrayList<TotalGoalsAgainstInformation>();

			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				TotalGoalsAgainstInformation againstInformation = new TotalGoalsAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setGoal_v0(againstJsonObject
						.getString("goal_v0"));
				againstInformation.setGoal_v1(againstJsonObject
						.getString("goal_v1"));
				againstInformation.setGoal_v2(againstJsonObject
						.getString("goal_v2"));
				againstInformation.setGoal_v3(againstJsonObject
						.getString("goal_v3"));
				againstInformation.setGoal_v4(againstJsonObject
						.getString("goal_v4"));
				againstInformation.setGoal_v5(againstJsonObject
						.getString("goal_v5"));
				againstInformation.setGoal_v6(againstJsonObject
						.getString("goal_v6"));
				againstInformation.setGoal_v7(againstJsonObject
						.getString("goal_v7"));
				totalGoalsAgainstInformations.add(againstInformation);
			}
			totalGoalsAgainstInformationList.add(totalGoalsAgainstInformations);
		}
	}

	/**
	 * 解析让球胜平负对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵信息Json数组
	 * @param againstLength
	 *            对阵Json对象的个数
	 * @throws JSONException
	 *             Json异常对象
	 */
	private void analysisWinTieLossAgainstInformation(
			JSONArray againstJsonArray, int againstLength) throws JSONException {
		if (winTieLossAgainstInformationList == null) {
			winTieLossAgainstInformationList = new ArrayList<List<WinTieLossAgainstInformation>>();
		} else {
			winTieLossAgainstInformationList.clear();
		}

		for (int jsonarray_i = 0; jsonarray_i < againstLength; jsonarray_i++) {
			List<WinTieLossAgainstInformation> winTieLossAgainstInformations = new ArrayList<WinTieLossAgainstInformation>();

			JSONArray againstWithDataJsonArray = againstJsonArray
					.getJSONArray(jsonarray_i);
			int againstWithDataLength = againstWithDataJsonArray.length();

			for (int json_i = 0; json_i < againstWithDataLength; json_i++) {
				JSONObject againstJsonObject = againstWithDataJsonArray
						.getJSONObject(json_i);

				WinTieLossAgainstInformation againstInformation = new WinTieLossAgainstInformation();
				againstInformation.setTeamId(againstJsonObject
						.getString("teamId"));
				againstInformation.setDayForamt(againstJsonObject
						.getString("dayForamt"));
				againstInformation.setHomeTeam(againstJsonObject
						.getString("homeTeam"));
				againstInformation.setGuestTeam(againstJsonObject
						.getString("guestTeam"));
				againstInformation.setLeague(againstJsonObject
						.getString("league"));
				againstInformation.setEndTime(againstJsonObject
						.getString("endTime"));
				againstInformation.setV0(againstJsonObject.getString("v0"));
				againstInformation.setV1(againstJsonObject.getString("v1"));
				againstInformation.setV3(againstJsonObject.getString("v3"));
				againstInformation.setLetPoint(againstJsonObject
						.getString("letPoint"));

				winTieLossAgainstInformations.add(againstInformation);
			}

			winTieLossAgainstInformationList.add(winTieLossAgainstInformations);
		}
	}

	/**
	 * 按钮事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_main_img_return:
				createMenuPopupwindow();
				break;
			case R.id.buy_lq_main_btn_type:
				createPlayMethodChangeDialog();
				break;
			case R.id.buy_lq_main_btn_team:
				createeventSelectDialog();
				break;
			case R.id.buy_lq_main_btn_score:
				Intent intent = new Intent(BeiJingSingleGameActivity.this,
						JcScoreActivity.class);
				startActivity(intent);
				break;
			case R.id.buy_zixuan_img_again:
				refreshAgainstInformationShow(false,true);
				break;
			case R.id.buy_zixuan_img_touzhu:
				Toast.makeText(BeiJingSingleGameActivity.this, "投注", 1).show();
				break;
			}
		}
	}

	/**
	 * 创建下拉菜单
	 */
	private void createMenuPopupwindow() {
		View popupView = (LinearLayout) layoutInflater.inflate(
				R.layout.buy_group_window, null);

		final PopupWindow popupwindow = new PopupWindow(popupView,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout2);
		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		popupwindow.setOutsideTouchable(true);
		popupwindow.showAsDropDown(menuButton);

		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
				return false;
			}
		});

		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if (gameDialog == null) {
					gameDialog = new BuyGameDialog(context, lotNo, gameHandler);
				}
				gameDialog.showDialog();
				popupwindow.dismiss();
			}
		});
		
		layoutHosity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutHosity
						.setBackgroundResource(R.drawable.buy_group_layout_b);
				turnHosity();
				popupwindow.dismiss();
			}

		});
		layoutParentLuck.setVisibility(LinearLayout.GONE);
	}
	public void turnHosity() {
		Intent intent = new Intent(context, NoticeBeijingSingleActivity.class);
		intent.putExtra(Constants.PLAY_METHOD_TYPE, lotnoString);
		startActivity(intent);
	}
	/**
	 * 创建玩法切换对话框
	 */
	public void createPlayMethodChangeDialog() {
		View dialogView = layoutInflater.inflate(
				R.layout.beijingsinglegame_playmethodchange_dialog, null);
		playMethodChangeDialog = new AlertDialog.Builder(this).create();
		playMethodChangeDialog.show();
		playMethodChangeDialog.getWindow().setContentView(dialogView);

		Button wintielossButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_wintieloss);
		wintielossButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());
		Button totalgoalsButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_totalgoals);
		totalgoalsButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());
		Button overallButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_overall);
		overallButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());
		Button halftheaudienceButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_halftheaudience);
		halftheaudienceButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());
		Button updownsigledoubleButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_updownsigledouble);
		updownsigledoubleButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());
	}

	/**
	 * 玩法切换对话框按钮事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class PlayMethodChangeDialogButtonOnClickListener implements
			OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.beijingsinglegame_playmethodchange_button_wintieloss:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_wintieloss);
				playMethodType = PlayMethodEnum.WINTIELOSS;
				lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_totalgoals:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_totalgoals);
				playMethodType = PlayMethodEnum.TOTALGOALS;
				lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_overall:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_overall);
				playMethodType = PlayMethodEnum.OVERALL;
				lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_halftheaudience:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_halftheaudience);
				playMethodType = PlayMethodEnum.HALFTHEAUDIENCE;
				lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_updownsigledouble:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_updownsigledouble);
				playMethodType = PlayMethodEnum.UPDOWNSINGLEDOUBLE;
				lotnoString = Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE;
				break;
			}
			playMethodChangeDialog.dismiss();

			getAndnalysisAgainstInformations();
		}
	}

	/**
	 * 创建赛事选择对话框
	 */
	public void createeventSelectDialog() {
		if (eventSelectDialog == null) {
			eventSelectDialog = new AlertDialog.Builder(this).create();
		}
		View DialogView = layoutInflater.inflate(R.layout.jc_main_team_dialog,
				null);

		TextView title = (TextView) DialogView
				.findViewById(R.id.zfb_text_title);
		title.setText(getString(R.string.jc_main_team_check));

		// 赛事按钮容器布局
		LinearLayout selectButtonLayout = (LinearLayout) DialogView
				.findViewById(R.id.jc_linear_check_all);
		addEventSelectButtonToLayout(selectButtonLayout);

		eventSelectDialog.show();
		eventSelectDialog.setCancelable(false);
		eventSelectDialog.getWindow().setContentView(DialogView);

		Button allSelectButton = (Button) DialogView
				.findViewById(R.id.all_check);
		allSelectButton
				.setOnClickListener(new EventSelectDialogButtonOnClickListener());
		Button allClearButton = (Button) DialogView
				.findViewById(R.id.clear_check);
		allClearButton
				.setOnClickListener(new EventSelectDialogButtonOnClickListener());
		Button okButton = (Button) DialogView.findViewById(R.id.ok);
		okButton.setOnClickListener(new EventSelectDialogButtonOnClickListener());
	}

	/**
	 * 向布局中添加赛事选择按钮
	 * 
	 * @param selectButtonLayout
	 */
	private void addEventSelectButtonToLayout(LinearLayout selectButtonLayout) {
		int buttonOfPreLine = 2;
		int buttonNum = 0;
		if (eventsList != null) {
			buttonNum = eventsList.size();
		}

		eventSelectButtons = new MyButton[buttonNum];

		if (buttonNum > 0) {
			int lineNum = eventsList.size() / buttonOfPreLine;
			int lastLineNum = eventsList.size() % buttonOfPreLine;
			// 如果满 一行
			if (buttonNum > 1) {
				for (int line_i = 0; line_i <= lineNum; line_i++) {
					LinearLayout linearLayout = new LinearLayout(this);

					// 如果不是最后一行
					if (line_i != lineNum) {
						for (int button_j = 0; button_j < buttonOfPreLine; button_j++) {
							addButtonToLine(buttonOfPreLine, line_i,
									linearLayout, button_j);
						}
					}
					// 如果是最后一行
					else {
						for (int button_j = 0; button_j < lastLineNum; button_j++) {
							addButtonToLine(buttonOfPreLine, line_i,
									linearLayout, button_j);
						}
					}

					selectButtonLayout.addView(linearLayout);
				}
			}
			// 如果不满一行
			else {
				LinearLayout linearLayout = new LinearLayout(this);
				addButtonToLine(buttonOfPreLine, 0, linearLayout, 0);
				selectButtonLayout.addView(linearLayout);
			}

		}
	}

	/**
	 * 向行里添加选择赛事按钮
	 * 
	 * @param buttonOfPreLine
	 *            每行按钮的个数
	 * @param line_i
	 *            行数
	 * @param linearLayout
	 *            行布局容器
	 * @param button_j
	 *            列数
	 */
	private void addButtonToLine(int buttonOfPreLine, int line_i,
			LinearLayout linearLayout, int button_j) {
		final MyButton button = new MyButton(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				PublicMethod.getPxInt(135, this), PublicMethod.getPxInt(35,
						this));
		params.setMargins(PublicMethod.getPxInt(10, this),
				PublicMethod.getPxInt(20, this), 0, 0);
		button.setLayoutParams(params);

		String buttonString = eventsList.get(line_i * buttonOfPreLine
				+ button_j);
		button.setBtnText(buttonString);

		if (selectedEventsList.contains(buttonString)) {
			button.setOnClick(true);
		} else {
			button.setOnClick(false);
		}
		button.switchBg();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				button.onAction();
			}
		});
		linearLayout.addView(button);
		eventSelectButtons[line_i * buttonOfPreLine + button_j] = button;
	}

	/**
	 * 赛事切换对话框按钮事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class EventSelectDialogButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_check:
				for (MyButton eventButton : eventSelectButtons) {
					eventButton.setOnClick(true);
					eventButton.switchBg();
				}
				break;
			case R.id.clear_check:
				for (MyButton eventButton : eventSelectButtons) {
					eventButton.setOnClick(false);
					eventButton.switchBg();
				}
				break;
			case R.id.ok:
				selectedEventsList.clear();
				for (MyButton eventButton : eventSelectButtons) {
					if (eventButton.isOnClick()) {
						selectedEventsList.add(eventButton.getBtnText());
					}
				}

				if (selectedEventsList.size() != 0) {
					refreshAgainstInformationShow(false,true);
					eventSelectDialog.dismiss();
				} else {
					Toast.makeText(BeiJingSingleGameActivity.this,
							"请至少选择一个赛事!", Toast.LENGTH_SHORT).show();
				}

				break;
			}

		}
	}
}
