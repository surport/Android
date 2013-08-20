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
import com.ruyicai.activity.buy.jc.score.beijing.BeijingScoreActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.net.newtransaction.BeiJingSingleGameInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
 * 北京单场选号页面
 *
 * @author Administrator
 *
 */
public class BeiJingSingleGameActivity extends Activity {
	private static final String TAG = "BeiJingSingleGameActivity";

	/** 上下文对象 */
	private Context context;
	/** 玩法回调对象 */
	private Handler gameHandler = new Handler();
	/** 从网络获取数据成功标识 */
	private static final int SUCCESS = 0;
	/** 从网络获取数据失败标识 */
	private static final int FAILD = 1;
	/** Add by pengcx 20130516 start */
	/** 是否是投注成功返回 */
	static boolean isBettingReturn = false;
	/** Add by pengcx 20130516 end */

	/** 玩法类型:默认为胜平负 */
	static PlayMethodEnum playMethodType = PlayMethodEnum.WINTIELOSS;

	/** 标题文本框 */
	private TextView titleTextView;
	/** 下拉菜单按钮 */
	private Button popupWindowButton;
	/** 玩法切换按钮 */
//	private Button playMethodChangeButton;
	/** 赛事选择按钮 */
//	private LinearLayout eventSelectButton;
	private Button eventSelectButton;
	/** 即时比分按钮 */
	private Button realtimeScoreButton;
	/** 对阵列表视图 */
	private View againstView;
	/** 对阵列表 */
	private ListView againstListView;
	/** 对阵填充布局 */
	private LinearLayout againstLinearLayout;
	/** 重选按钮 */
	private ImageButton reelectImageButton;
	/** 选择场次文本框 */
	private TextView selectNumTextView;
	/** 投注按钮 */
	private ImageButton bettingImageButton;

	/** 赛事选择对话框 */
	private Dialog eventSelectDialog;
	/** 玩法切换对话框 */
	private Dialog playMethodChangeDialog;
	/** 玩法介绍对话框 */
	private BuyGameDialog gameDialog;
	/** 下拉菜单 */
	private PopupWindow menuPopupwindow;

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

	/** 赛事选择按钮集合 */
	private MyButton[] eventSelectButtons;
	/** 赛事名称集合 */
	private List<String> eventsList;
	/** 当前选中的赛事集合 */
	private List<String> nowSelectedEventsList;

	/** add by pengcx 20130708 start */
	/** 当前选中赛事的赔率集合 */
	static List<double[]> newSelectedSPList;
	/** add by pengcx 20130708 end */

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

	private List<String> bettinginfoList;
	private List<String> bettingDanList;

	/** 当前选中的场次数 */
	private int selectedGameNum = 0;
	private int selectedDanNum = 0;

	/** 当前期号 */
	private String nowIssueString;
	/** 网络请求返回消息 */
	private String messageString;

	/** LayoutInflater对象 */
	private LayoutInflater layoutInflater;

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
	 * @param isReset
	 *            是否重置适配器
	 * @param isCleared
	 *            是否清除原有数据
	 */
	public void refreshAgainstInformationShow(boolean isReset, boolean isCleared) {
		// againstLinearLayout.removeAllViews();

		switch (playMethodType) {
		case WINTIELOSS:
			getNowShowWinTieLossAgainstInformationWithSelectedEvent(isCleared);

			if (winTieLossAdapter == null) {
				winTieLossAdapter = new WinTieLossAdapter(
						BeiJingSingleGameActivity.this,
						nowWinTieLossAgainstInformationList);
			}

			if (isReset) {
				againstListView.setAdapter(winTieLossAdapter);
			} else {
				winTieLossAdapter.notifyDataSetChanged();
			}

			break;
		case TOTALGOALS:
			getNowShowTotalGoalsAgainstInformationWithSelectedEvent(isCleared);

			if (totalGoalsAdapter == null) {
				totalGoalsAdapter = new TotalGoalsAdapter(
						BeiJingSingleGameActivity.this,
						nowTotalGoalsAgainstInformationList);
			}

			if (isReset) {
				againstListView.setAdapter(totalGoalsAdapter);
			} else {
				totalGoalsAdapter.notifyDataSetChanged();
			}
			break;
		case OVERALL:
			getNowShowOverAllAgainstInformationWithSelectedEvent(isCleared);

			if (overAllAdapter == null) {
				overAllAdapter = new OverAllAdapter(
						BeiJingSingleGameActivity.this,
						nowOverAllagainstInformationList);
			}

			if (isReset) {
				againstListView.setAdapter(overAllAdapter);
			} else {
				overAllAdapter.notifyDataSetChanged();
			}

			break;
		case HALFTHEAUDIENCE:
			getNowShowHalfTheAudienceAgainstInformationWithSelectedEvent(isCleared);

			if (halfTheAudienceAdapter == null) {
				halfTheAudienceAdapter = new HalfTheAudienceAdapter(
						BeiJingSingleGameActivity.this,
						nowHalfTheAudienceagainstInformationList);
			}

			if (isReset) {
				againstListView.setAdapter(halfTheAudienceAdapter);
			} else {
				halfTheAudienceAdapter.notifyDataSetChanged();
			}

			break;
		case UPDOWNSINGLEDOUBLE:
			getNowShowUpDownSingleDoubleAgainstInformationWithSelectedEvent(isCleared);

			if (upDownSingleDoubleAdapter == null) {
				upDownSingleDoubleAdapter = new UpDownSingleDoubleAdapter(
						BeiJingSingleGameActivity.this,
						nowUpDownSigleDoubleagainstInformationList);
			}

			if (isReset) {
				againstListView.setAdapter(upDownSingleDoubleAdapter);
			} else {
				upDownSingleDoubleAdapter.notifyDataSetChanged();
			}

			break;
		}
		// againstLinearLayout.addView(againstView);

		selectNumTextView.setText("已经选择了" + selectedGameNum + "场比赛");

	}

	/**
	 * 更具当前选择的赛事获取当前上下单双显示的对阵信息
	 * 
	 * @param isCleared
	 *            是否清除原有的显示信息
	 */
	private void getNowShowUpDownSingleDoubleAgainstInformationWithSelectedEvent(
			boolean isCleared) {

		if (nowUpDownSigleDoubleagainstInformationList == null) {
			nowUpDownSigleDoubleagainstInformationList = new ArrayList<List<UpDownSingleDoubleAgainstInformation>>();
		}

		if (isCleared && upDownSigleDoubleagainstInformationList != null) {
			// 清空原有的上下单双当前显示信息
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

					// 获取赛事信息，如果当前选中该赛事，则将该赛事信息加入当前显示的半全场对阵信息
					String league = upDownSingleDoubleAgainstInformation
							.getLeague();
					if (nowSelectedEventsList.contains(league)) {
						nowUpDownSingleDoubleAgainstInformations
								.add(upDownSingleDoubleAgainstInformation
										.clone());
					}
				}
				nowUpDownSigleDoubleagainstInformationList
						.add(nowUpDownSingleDoubleAgainstInformations);
			}
		}
	}

	/**
	 * 根据当前选择的赛事获取当前显示的半全场对阵信息
	 * 
	 * @param isCleared
	 *            是否清空原来显示的半全场信息
	 */
	private void getNowShowHalfTheAudienceAgainstInformationWithSelectedEvent(
			boolean isCleared) {

		if (nowHalfTheAudienceagainstInformationList == null) {
			nowHalfTheAudienceagainstInformationList = new ArrayList<List<HalfTheAudienceAgainstInformation>>();
		}

		if (isCleared && halfTheAudienceagainstInformationList != null) {
			// 清空原有的半全场当前显示信息
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

					// 获取赛事信息，如果当前赛事被选中，则将该信息加入当前显示的半全场对阵信息
					String league = halfTheAudienceAgainstInformation
							.getLeague();
					if (nowSelectedEventsList.contains(league)) {
						nowHalfTheAudienceAgainstInformations
								.add(halfTheAudienceAgainstInformation.clone());
					}
				}
				nowHalfTheAudienceagainstInformationList
						.add(nowHalfTheAudienceAgainstInformations);
			}
		}

	}

	/**
	 * 根据选中的赛事获取当前显示的全场总比分对阵信息
	 * 
	 * @param isCleared
	 *            是否清除原有的对阵信息
	 */
	private void getNowShowOverAllAgainstInformationWithSelectedEvent(
			boolean isCleared) {

		if (nowOverAllagainstInformationList == null) {
			nowOverAllagainstInformationList = new ArrayList<List<OverAllAgainstInformation>>();
		}
		if (isCleared && overAllagainstInformationList != null) {
			// 清空原有的全场总比分当前显示信息
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

					// 获取全场总比分赛事，如果当前赛事被选中，则放入当前显示的全场总比分数信息集合中
					String league = overAllAgainstInformation.getLeague();
					if (nowSelectedEventsList.contains(league)) {
						nowOverAllAgainstInformations
								.add(overAllAgainstInformation.clone());
					}
				}
				nowOverAllagainstInformationList
						.add(nowOverAllAgainstInformations);
			}
		}

	}

	/**
	 * 获取当前显示的总进球数对阵信息
	 * 
	 * @param isCleared
	 *            是否清除原有信息
	 */
	private void getNowShowTotalGoalsAgainstInformationWithSelectedEvent(
			boolean isCleared) {
		if (nowTotalGoalsAgainstInformationList == null) {
			nowTotalGoalsAgainstInformationList = new ArrayList<List<TotalGoalsAgainstInformation>>();
		}

		if (isCleared && totalGoalsAgainstInformationList != null) {
			// 清空原有的总进球数当前显示信息
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

					// 获取总进球数信息的赛事，如果当前赛事被选中，则放入当前显示的总进球数信息集合中
					String league = totalGoalsAgainstInformation.getLeague();
					if (nowSelectedEventsList.contains(league)) {
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
	 * 获取当前显示的胜平负的对阵信息
	 * 
	 * @param isCleared
	 *            是否情况原有数据
	 */
	private void getNowShowWinTieLossAgainstInformationWithSelectedEvent(
			boolean isCleared) {

		if (nowWinTieLossAgainstInformationList == null) {
			nowWinTieLossAgainstInformationList = new ArrayList<List<WinTieLossAgainstInformation>>();
		}

		// 如果清空，则从完整的数据中再次获取胜平负信息
		if (isCleared && winTieLossAgainstInformationList != null) {
			// 清空原有的胜平负当前显示信息
			nowWinTieLossAgainstInformationList.clear();

			int listnum = winTieLossAgainstInformationList.size();
			for (int list_i = 0; list_i < listnum; list_i++) {
				List<WinTieLossAgainstInformation> winTieLossAgainstInformations = winTieLossAgainstInformationList
						.get(list_i);
				List<WinTieLossAgainstInformation> nowWinTieLossAgainstInformations = new ArrayList<WinTieLossAgainstInformation>();

				int infonum = winTieLossAgainstInformations.size();
				for (int info_j = 0; info_j < infonum; info_j++) {
					WinTieLossAgainstInformation winTieLossAgainstInformation = winTieLossAgainstInformations
							.get(info_j);

					// 获取胜平负信息的赛事，如果当前赛事被选中，则放入当前显示的胜平负信息集合中
					String league = winTieLossAgainstInformation.getLeague();
					if (nowSelectedEventsList.contains(league)) {
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
		context = this;

		layoutInflater = LayoutInflater.from(this);
		playMethodType = PlayMethodEnum.WINTIELOSS;

		setContentView(R.layout.buy_jc_main_new);

		initTitleBarShow();

		initEventInformationBarShow();

		initBettingBarShow();

		initAgainstInformationListShow();

		getAndnalysisAgainstInformations();
	}

	/** Add by pengcx 20130516 start */
	@Override
	protected void onRestart() {
		super.onRestart();
		if (isBettingReturn) {
			selectedGameNum = 0;
			isBettingReturn = false;
			refreshAgainstInformationShow(false, true);
		}
	}

	/** Add by pengcx 20130516 end */

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
		reelectImageButton
				.setOnClickListener(new BeijingSingleGameButtonOnClickListener());

		selectNumTextView = (TextView) findViewById(R.id.buy_jc_main_text_team_num);
		selectNumTextView.setText("已选择了0场比赛");
		bettingImageButton = (ImageButton) findViewById(R.id.buy_zixuan_img_touzhu);
		bettingImageButton
				.setOnClickListener(new BeijingSingleGameButtonOnClickListener());
	}

	public void refreshSelectNumAndDanNum() {
		selectedGameNum = 0;
		selectedDanNum = 0;
		switch (playMethodType) {
		case WINTIELOSS:
			refreshWinTieLossSelectNumAndDanNum();
			break;
		case TOTALGOALS:
			refreshTotalGoalsSelectNumAndDanNum();
			break;
		case OVERALL:
			refreshOverAllSelectNumAndDanNum();
			break;
		case HALFTHEAUDIENCE:
			refreshHalfTheAudienceSelectNumAndDanNum();
			break;
		case UPDOWNSINGLEDOUBLE:
			refreshUpDownSingleDoubleSelectNumAndDanNum();
			break;
		}
		selectNumTextView.setText("已经选择了" + selectedGameNum + "场比赛");
	}

	private void refreshUpDownSingleDoubleSelectNumAndDanNum() {
		for (List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations : nowUpDownSigleDoubleagainstInformationList) {
			for (UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation : upDownSingleDoubleAgainstInformations) {
				if (upDownSingleDoubleAgainstInformation.isSelected()) {
					selectedGameNum++;
				}

				if (upDownSingleDoubleAgainstInformation.isDan()) {
					selectedDanNum++;
				}
			}
		}
	}

	private void refreshHalfTheAudienceSelectNumAndDanNum() {
		for (List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations : nowHalfTheAudienceagainstInformationList) {
			for (HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation : halfTheAudienceAgainstInformations) {
				if (halfTheAudienceAgainstInformation.isSelected()) {
					selectedGameNum++;
				}

				if (halfTheAudienceAgainstInformation.isDan()) {
					selectedDanNum++;
				}
			}
		}
	}

	private void refreshOverAllSelectNumAndDanNum() {
		for (List<OverAllAgainstInformation> overAllAgainstInformations : nowOverAllagainstInformationList) {
			for (OverAllAgainstInformation overAllAgainstInformation : overAllAgainstInformations) {
				if (overAllAgainstInformation.isSelected()) {
					selectedGameNum++;
				}

				if (overAllAgainstInformation.isDan()) {
					selectedDanNum++;
				}
			}
		}
	}

	private void refreshTotalGoalsSelectNumAndDanNum() {
		for (List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations : nowTotalGoalsAgainstInformationList) {
			for (TotalGoalsAgainstInformation totalGoalsAgainstInformation : totalGoalsAgainstInformations) {
				if (totalGoalsAgainstInformation.isSelected()) {
					selectedGameNum++;
				}

				if (totalGoalsAgainstInformation.isDan()) {
					selectedDanNum++;
				}
			}
		}
	}

	private void refreshWinTieLossSelectNumAndDanNum() {
		for (List<WinTieLossAgainstInformation> winTieLossAgainstInformations : nowWinTieLossAgainstInformationList) {
			for (WinTieLossAgainstInformation winTieLossAgainstInformation : winTieLossAgainstInformations) {
				if (winTieLossAgainstInformation.isSelected()) {
					selectedGameNum++;
				}

				if (winTieLossAgainstInformation.isDan()) {
					selectedDanNum++;
				}
			}
		}
	}

	/**
	 * 初始化赛事信息栏显示
	 */
	private void initEventInformationBarShow() {
//		playMethodChangeButton = (Button) findViewById(R.id.buy_lq_main_btn_type);
//		playMethodChangeButton
//				.setOnClickListener(new BeijingSingleGameButtonOnClickListener());

		eventSelectButton = (Button) findViewById(R.id.buy_lq_main_btn_team);
		eventSelectButton.setVisibility(View.VISIBLE);
		eventSelectButton
				.setOnClickListener(new BeijingSingleGameButtonOnClickListener());

		realtimeScoreButton = (Button) findViewById(R.id.buy_lq_main_btn_score);
		realtimeScoreButton.setVisibility(View.VISIBLE);
		realtimeScoreButton.setOnClickListener(new BeijingSingleGameButtonOnClickListener());
	}

	/**
	 * 初始化标题栏显示
	 */
	private void initTitleBarShow() {
		titleTextView = (TextView) findViewById(R.id.layout_main_text_title);
		titleTextView.setText(R.string.beijingsinglegame_textview_wintieloss);

		popupWindowButton = (Button) findViewById(R.id.layout_main_img_return);
		popupWindowButton
				.setOnClickListener(new BeijingSingleGameButtonOnClickListener());
	}

	/**
	 * 获取并解析对阵列表信息
	 */
	private void getAndnalysisAgainstInformations() {
		// 创建联网等待对话框
		final ProgressDialog connectDialog = UserCenterDialog
				.onCreateDialog(this);
		connectDialog.show();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// 获取当前期号
				nowIssueString = PublicMethod.toNetIssue(playMethodType
						.getLotnoString());

				// 如果获取期号成功，则继续获取对阵信息
				if (nowIssueString != null && !nowIssueString.equals("网络异常")) {
					// 获取当前玩法的当前期对阵数据
					Constants.currentLoto = nowIssueString;
					String returnString = BeiJingSingleGameInterface
							.getInstance().getAgainstInformations(
									playMethodType.getLotnoString(),
									nowIssueString);

					analysisReturnJsonString(returnString);
				} else {
					messageString = "获取期号失败";
					Message message = new Message();
					message.what = FAILD;
					handler.sendMessage(message);
				}
				// 取消对话框
				connectDialog.dismiss();
			}
		}).start();
	}

	/**
	 * 解析返回的Json字符串
	 * 
	 * @param returnString
	 *            返回的Json字符串
	 */
	private void analysisReturnJsonString(String returnString) {
		try {
			// 解析网络反馈信息
			JSONObject returnJsonObject = new JSONObject(returnString);
			String errorCodeString = returnJsonObject.getString("error_code");
			messageString = returnJsonObject.getString("message");

			Message message = new Message();
			// 如果请求成功，解析对阵信息，然后发出获取并解析成功信息
			if (errorCodeString.equals("0000")) {
				analysisSuccessReturnInformations(returnJsonObject);
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
	}

	/**
	 * 解析成功返回的Json数据
	 * 
	 * @param returnJsonObject
	 *            成功返回数据Json对象
	 */
	private void analysisSuccessReturnInformations(JSONObject returnJsonObject) {
		try {
			JSONArray againstJsonArray = returnJsonObject
					.getJSONArray("result");

			analysisEventInformation(returnJsonObject);

			analysisAgainstInformations(againstJsonArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析对阵信息
	 * 
	 * @param againstJsonArray
	 *            对阵Json数组
	 * @throws JSONException
	 *             json解析异常
	 */
	private void analysisAgainstInformations(JSONArray againstJsonArray)
			throws JSONException {

		switch (playMethodType) {
		case WINTIELOSS:
			if (winTieLossAgainstInformationList == null) {
				winTieLossAgainstInformationList = new ArrayList<List<WinTieLossAgainstInformation>>();
			} else {
				winTieLossAgainstInformationList.clear();
			}

			winTieLossAgainstInformationList = WinTieLossAgainstInformation
					.analysisWinTieLossAgainstInformation(againstJsonArray);
			break;
		case TOTALGOALS:
			if (totalGoalsAgainstInformationList == null) {
				totalGoalsAgainstInformationList = new ArrayList<List<TotalGoalsAgainstInformation>>();
			} else {
				totalGoalsAgainstInformationList.clear();
			}
			totalGoalsAgainstInformationList = TotalGoalsAgainstInformation
					.analysisTotalGoalsAgainstInformation(againstJsonArray);
			break;
		case OVERALL:
			if (overAllagainstInformationList == null) {
				overAllagainstInformationList = new ArrayList<List<OverAllAgainstInformation>>();
			} else {
				overAllagainstInformationList.clear();
			}

			overAllagainstInformationList = OverAllAgainstInformation
					.analysisOverAllAgainstInformation(againstJsonArray);
			break;
		case HALFTHEAUDIENCE:
			if (halfTheAudienceagainstInformationList == null) {
				halfTheAudienceagainstInformationList = new ArrayList<List<HalfTheAudienceAgainstInformation>>();
			} else {
				halfTheAudienceagainstInformationList.clear();
			}

			halfTheAudienceagainstInformationList = HalfTheAudienceAgainstInformation
					.analysisHalfTheAudienceAgainstInformation(againstJsonArray);
			break;
		case UPDOWNSINGLEDOUBLE:
			if (upDownSigleDoubleagainstInformationList == null) {
				upDownSigleDoubleagainstInformationList = new ArrayList<List<UpDownSingleDoubleAgainstInformation>>();
			} else {
				upDownSigleDoubleagainstInformationList.clear();
			}

			upDownSigleDoubleagainstInformationList = UpDownSingleDoubleAgainstInformation
					.analysisUpDownSingleDoubleAgainstInformation(againstJsonArray);
			break;
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

		// 创建赛事集合
		if (eventsList == null) {
			eventsList = new ArrayList<String>();
		} else {
			eventsList.clear();
		}

		// 当前选中赛事集合
		if (nowSelectedEventsList == null) {
			nowSelectedEventsList = new ArrayList<String>();
		} else {
			nowSelectedEventsList.clear();
		}
		
		/** add by pengcx 20130708 start */
		// 当前选中的赛事集合
		if (newSelectedSPList == null) {
			newSelectedSPList = new ArrayList<double[]>();
		} else {
			newSelectedSPList.clear();
		}
		/** add by pengcx 20130708 end */

		// 将赛事字符串信息分别存入赛事集合和当前选中赛事集合（默认情况下所有赛事被选中）
		String[] events = eventStrings.split(";");
		for (int event_i = 0; event_i < events.length; event_i++) {
			eventsList.add(events[event_i]);
			nowSelectedEventsList.add(events[event_i]);
		}

	}

	/**
	 * 北京单场按钮事件监听实现类
	 * 
	 * @author Administrator
	 * 
	 */
	class BeijingSingleGameButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 下拉菜单按钮
			case R.id.layout_main_img_return:
				createMenuPopupwindow();
				break;
			// 玩法选择按钮
			case R.id.buy_lq_main_btn_type:
				createPlayMethodChangeDialog();
				break;
			// 赛事选择按钮
			case R.id.buy_lq_main_btn_team:
				createeventSelectDialog();
				break;
			// 重选按钮
			case R.id.buy_zixuan_img_again:
				selectedGameNum = 0;
				refreshAgainstInformationShow(false, true);
				break;
			// 投注按钮
			case R.id.buy_zixuan_img_touzhu:
				if (isLegalSelect()) {
					
					Intent intent = new Intent(BeiJingSingleGameActivity.this,
							BeiJingSingleGameIndentActivity.class);

					intent.putExtra("selectedagainst",
							getSelectedAgainstString());
					intent.putExtra("selectedgamenum", selectedGameNum);
					getSelectedEventClickNumAndDanClickNum();
					intent.putStringArrayListExtra("selectedeventclicknum",
							(ArrayList<String>) bettinginfoList);
					intent.putStringArrayListExtra("selecteddanclicknum",
							(ArrayList<String>) bettingDanList);
					intent.putExtra("laterpartbettingcode",
							getLaterPartBettingCode());
					intent.putExtra("nowIssueString", nowIssueString);
					intent.putExtra("lotno", playMethodType.getLotnoString());
					intent.putExtra("selecteddannum", selectedDanNum);

					startActivity(intent);
				}

				break;
				
			case R.id.buy_lq_main_btn_score:
				Intent intent = new Intent(BeiJingSingleGameActivity.this,
						BeijingScoreActivity.class);
				intent.putExtra("lotno",playMethodType.getLotnoString());
//				intent.putExtra("bebatchCode", "");
				startActivity(intent);
				break;
			}
		}
	}

	private void createMenuPopupwindow() {
		View popupView = (LinearLayout) layoutInflater.inflate(
				R.layout.buy_group_window, null);

		final PopupWindow popupwindow = new PopupWindow(popupView,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.beijing_single_mani_history);
		layoutHosity.setVisibility(View.GONE);

		final LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		popupwindow.setOutsideTouchable(true);
		popupwindow.showAsDropDown(popupWindowButton);

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
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if (gameDialog == null) {
					gameDialog = new BuyGameDialog(context, playMethodType
							.getLotnoString(), gameHandler);
				}
				gameDialog.showDialog();
				popupwindow.dismiss();
			}
		});

		LinearLayout layoutQuery = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout4);
		layoutQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RWSharedPreferences shellRW = new RWSharedPreferences(context,
						"addInfo");
				String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
				if (userno == null || userno.equals("")) {
					Intent intentSession = new Intent(context, UserLogin.class);
					startActivity(intentSession);
				} else {
					Intent intent = new Intent(context, BetQueryActivity.class);
					intent.putExtra("lotno", Constants.LOTNO_BJ_SINGLE);
					startActivity(intent);
				}
				popupwindow.dismiss();
			}
		});
		layoutParentLuck.setVisibility(LinearLayout.GONE);
	}

	/**
	 * 初始化玩法切换对话框的显示
	 * 
	 * @param dialogView
	 *            对话框对象
	 */
	private void initPlayMethodChangeDialogShow(View dialogView) {
		// 胜平负按钮
		Button wintielossButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_wintieloss);
		wintielossButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());

		// 总进球数按钮
		Button totalgoalsButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_totalgoals);
		totalgoalsButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());

		// 全场比分按钮
		Button overallButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_overall);
		overallButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());

		// 半全场按钮
		Button halftheaudienceButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_halftheaudience);
		halftheaudienceButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());

		// 上下单双按钮
		Button updownsigledoubleButton = (Button) dialogView
				.findViewById(R.id.beijingsinglegame_playmethodchange_button_updownsigledouble);
		updownsigledoubleButton
				.setOnClickListener(new PlayMethodChangeDialogButtonOnClickListener());

		// 根据当前的玩法，设置该玩法的按钮为选中状态
		switch (playMethodType) {
		case WINTIELOSS:
			wintielossButton
					.setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
			wintielossButton.setTextColor(getResources().getColorStateList(
					R.color.white));
			break;
		case TOTALGOALS:
			totalgoalsButton
					.setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
			totalgoalsButton.setTextColor(getResources().getColorStateList(
					R.color.white));
			break;
		case OVERALL:
			overallButton
					.setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
			overallButton.setTextColor(getResources().getColorStateList(
					R.color.white));
			break;
		case HALFTHEAUDIENCE:
			halftheaudienceButton
					.setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
			halftheaudienceButton.setTextColor(getResources()
					.getColorStateList(R.color.white));
			break;
		case UPDOWNSINGLEDOUBLE:
			updownsigledoubleButton
					.setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
			updownsigledoubleButton.setTextColor(getResources()
					.getColorStateList(R.color.white));
			break;
		}
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
				break;
			case R.id.beijingsinglegame_playmethodchange_button_totalgoals:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_totalgoals);
				playMethodType = PlayMethodEnum.TOTALGOALS;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_overall:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_overall);
				playMethodType = PlayMethodEnum.OVERALL;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_halftheaudience:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_halftheaudience);
				playMethodType = PlayMethodEnum.HALFTHEAUDIENCE;
				break;
			case R.id.beijingsinglegame_playmethodchange_button_updownsigledouble:
				titleTextView
						.setText(R.string.beijingsinglegame_textview_updownsigledouble);
				playMethodType = PlayMethodEnum.UPDOWNSINGLEDOUBLE;
				break;
			}
			selectedGameNum = 0;
			selectedDanNum = 0;
			playMethodChangeDialog.dismiss();

			getAndnalysisAgainstInformations();
		}
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

		initPlayMethodChangeDialogShow(dialogView);
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

		// 赛事选择对话框标题
		TextView title = (TextView) DialogView
				.findViewById(R.id.zfb_text_title);
		title.setText(getString(R.string.jc_main_team_check));

		// 赛事按钮容器布局
		LinearLayout selectButtonLayout = (LinearLayout) DialogView
				.findViewById(R.id.jc_linear_check_all);
		addEventSelectButtonToLayout(selectButtonLayout);

		eventSelectDialog.show();
		eventSelectDialog.getWindow().setContentView(DialogView);

		// 全选按钮
		Button allSelectButton = (Button) DialogView
				.findViewById(R.id.all_check);
		allSelectButton
				.setOnClickListener(new EventSelectDialogButtonOnClickListener());
		// 全清按钮
		Button allClearButton = (Button) DialogView
				.findViewById(R.id.clear_check);
		allClearButton
				.setOnClickListener(new EventSelectDialogButtonOnClickListener());
		// 确定按钮
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
	 * 获取后半部分的注码格式字符串,该部分注码标识投注内容，在投注确认页面和代表串关方法的注码等合并为完整的注码
	 * 
	 * @return 注码格式字符串
	 */
	public String getLaterPartBettingCode() {
		StringBuffer laterPartStringBuffer = new StringBuffer();
		switch (playMethodType) {
		case WINTIELOSS:
			getWinTieLossLaterPartBettingCode(laterPartStringBuffer);
			break;
		case TOTALGOALS:
			getTotalGoalsLaterPartBettingCode(laterPartStringBuffer);
			break;
		case OVERALL:
			getOverAllLaterPartBettingCode(laterPartStringBuffer);
			break;
		case HALFTHEAUDIENCE:
			getHalfTheAudienceLaterPartBetttingCode(laterPartStringBuffer);
			break;
		case UPDOWNSINGLEDOUBLE:
			getUpDownSingleDoubleLaterPartBettingCode(laterPartStringBuffer);
			break;
		}

		return laterPartStringBuffer.toString();
	}

	/**
	 * 获取上下单双后半部分注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码格式StringBuffer对象
	 */
	private void getUpDownSingleDoubleLaterPartBettingCode(
			StringBuffer batchCodeStringBuffer) {
		StringBuffer danBatchCodeStringBuffer = new StringBuffer();
		StringBuffer noDanBatchCodeStringBuffer = new StringBuffer();
		for (List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations : nowUpDownSigleDoubleagainstInformationList) {
			for (UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation : upDownSingleDoubleAgainstInformations) {
				if (upDownSingleDoubleAgainstInformation.isSelected()) {
					if (upDownSingleDoubleAgainstInformation.isDan()) {
						appendUpDownSingleDoubleBatchCodeString(
								danBatchCodeStringBuffer,
								upDownSingleDoubleAgainstInformation);
					} else {
						appendUpDownSingleDoubleBatchCodeString(
								noDanBatchCodeStringBuffer,
								upDownSingleDoubleAgainstInformation);
					}
				}
			}
		}

		appendDanAndNoDanBatchCode(batchCodeStringBuffer,
				danBatchCodeStringBuffer, noDanBatchCodeStringBuffer);
	}

	/**
	 * 拼接胆和非胆的注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码字符串
	 * @param danBatchCodeStringBuffer
	 *            胆注码字符串
	 * @param noDanBatchCodeStringBuffer
	 *            非胆注码字符串
	 */
	private void appendDanAndNoDanBatchCode(StringBuffer batchCodeStringBuffer,
			StringBuffer danBatchCodeStringBuffer,
			StringBuffer noDanBatchCodeStringBuffer) {
		if (danBatchCodeStringBuffer.length() > 0) {
			batchCodeStringBuffer.append(danBatchCodeStringBuffer).append("$")
					.append(noDanBatchCodeStringBuffer);
		} else {
			batchCodeStringBuffer.append(noDanBatchCodeStringBuffer);
		}
	}

	private void appendUpDownSingleDoubleBatchCodeString(
			StringBuffer batchCodeStringBuffer,
			UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation) {
		batchCodeStringBuffer.append(upDownSingleDoubleAgainstInformation
				.getTeamId() + "|");
		if (upDownSingleDoubleAgainstInformation.isV1IsClick()) {
			batchCodeStringBuffer.append("1");
		}

		if (upDownSingleDoubleAgainstInformation.isV2IsClick()) {
			batchCodeStringBuffer.append("2");
		}

		if (upDownSingleDoubleAgainstInformation.isV3IsClick()) {
			batchCodeStringBuffer.append("3");
		}

		if (upDownSingleDoubleAgainstInformation.isV4IsClick()) {
			batchCodeStringBuffer.append("4");
		}

		batchCodeStringBuffer.append("^");
	}

	/**
	 * 获取半全场后半部分注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码格式StringBuffer对象
	 */
	private void getHalfTheAudienceLaterPartBetttingCode(
			StringBuffer batchCodeStringBuffer) {
		StringBuffer danBatchCodeStringBuffer = new StringBuffer();
		StringBuffer noDanBatchCodeStringBuffer = new StringBuffer();

		for (List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations : nowHalfTheAudienceagainstInformationList) {
			for (HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation : halfTheAudienceAgainstInformations) {
				if (halfTheAudienceAgainstInformation.isSelected()) {
					if (halfTheAudienceAgainstInformation.isDan()) {
						appendHalfTheAudienceBatchCodeString(
								danBatchCodeStringBuffer,
								halfTheAudienceAgainstInformation);
					} else {
						appendHalfTheAudienceBatchCodeString(
								noDanBatchCodeStringBuffer,
								halfTheAudienceAgainstInformation);
					}

				}
			}
		}

		appendDanAndNoDanBatchCode(batchCodeStringBuffer,
				danBatchCodeStringBuffer, noDanBatchCodeStringBuffer);

	}

	private void appendHalfTheAudienceBatchCodeString(
			StringBuffer batchCodeStringBuffer,
			HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation) {
		batchCodeStringBuffer.append(halfTheAudienceAgainstInformation
				.getTeamId() + "|");

		boolean[] isClicks = halfTheAudienceAgainstInformation.getIsClicks();
		int length = isClicks.length;
		for (int t = 0; t < length; t++) {
			if (isClicks[t]) {
				switch (t) {
				case 0:
					batchCodeStringBuffer.append("33");
					break;
				case 1:
					batchCodeStringBuffer.append("31");
					break;
				case 2:
					batchCodeStringBuffer.append("30");
					break;
				case 3:
					batchCodeStringBuffer.append("13");
					break;
				case 4:
					batchCodeStringBuffer.append("11");
					break;
				case 5:
					batchCodeStringBuffer.append("10");
					break;
				case 6:
					batchCodeStringBuffer.append("03");
					break;
				case 7:
					batchCodeStringBuffer.append("01");
					break;
				case 8:
					batchCodeStringBuffer.append("00");
					break;
				}
			}
		}
		batchCodeStringBuffer.append("^");
	}

	/**
	 * 获取全场总比分后半部分注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码格式StringBuffer对象
	 */
	private void getOverAllLaterPartBettingCode(
			StringBuffer batchCodeStringBuffer) {
		StringBuffer danBatchCodeStringBuffer = new StringBuffer();
		StringBuffer noDanBatchCodeStringBuffer = new StringBuffer();
		for (List<OverAllAgainstInformation> overAllAgainstInformations : nowOverAllagainstInformationList) {
			for (OverAllAgainstInformation overAllAgainstInformation : overAllAgainstInformations) {
				if (overAllAgainstInformation.isSelected()) {
					if (overAllAgainstInformation.isDan()) {
						appendOverAllBatchCodeString(danBatchCodeStringBuffer,
								overAllAgainstInformation);
					} else {
						appendOverAllBatchCodeString(noDanBatchCodeStringBuffer,
								overAllAgainstInformation);
					}

				}
			}
		}

		appendDanAndNoDanBatchCode(batchCodeStringBuffer,
				danBatchCodeStringBuffer, noDanBatchCodeStringBuffer);

	}

	/**
	 * 拼接总比分赛事注码
	 * 
	 * @param batchCodeStringBuffer
	 *            注码字符串
	 * @param overAllAgainstInformation
	 *            总比赛事信息
	 */
	private void appendOverAllBatchCodeString(
			StringBuffer batchCodeStringBuffer,
			OverAllAgainstInformation overAllAgainstInformation) {
		batchCodeStringBuffer.append(overAllAgainstInformation.getTeamId()
				+ "|");

		boolean[] isClicks = overAllAgainstInformation.getIsClicks();
		int length = isClicks.length;
		for (int t = 0; t < length; t++) {
			if (isClicks[t]) {
				/** modify by pengcx 20130617 start */
				switch (t) {
				case 0:
					batchCodeStringBuffer.append("90");
					break;
				case 1:
					batchCodeStringBuffer.append("10");
					break;
				case 2:
					batchCodeStringBuffer.append("20");
					break;
				case 3:
					batchCodeStringBuffer.append("21");
					break;
				case 4:
					batchCodeStringBuffer.append("30");
					break;
				case 5:
					batchCodeStringBuffer.append("31");
					break;
				case 6:
					batchCodeStringBuffer.append("32");
					break;
				case 7:
					batchCodeStringBuffer.append("40");
					break;
				case 8:
					batchCodeStringBuffer.append("41");
					break;
				case 9:
					batchCodeStringBuffer.append("42");
					break;
				case 10:
					batchCodeStringBuffer.append("99");
					break;
				case 11:
					batchCodeStringBuffer.append("00");
					break;
				case 12:
					batchCodeStringBuffer.append("11");
					break;
				case 13:
					batchCodeStringBuffer.append("22");
					break;
				case 14:
					batchCodeStringBuffer.append("33");
					break;
				case 15:
					batchCodeStringBuffer.append("09");
					break;
				case 16:
					batchCodeStringBuffer.append("01");
					break;
				case 17:
					batchCodeStringBuffer.append("02");
					break;
				case 18:
					batchCodeStringBuffer.append("12");
					break;
				case 19:
					batchCodeStringBuffer.append("03");
					break;
				case 20:
					batchCodeStringBuffer.append("13");
					break;
				case 21:
					batchCodeStringBuffer.append("23");
					break;
				case 22:
					batchCodeStringBuffer.append("04");
					break;
				case 23:
					batchCodeStringBuffer.append("14");
					break;
				case 24:
					batchCodeStringBuffer.append("24");
					break;
				}
			}
			/** modify by pengcx 20130617 end */
		}
		batchCodeStringBuffer.append("^");
	}

	/**
	 * 获取总进球数后半部分注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码格式StringBuffer对象
	 */
	private void getTotalGoalsLaterPartBettingCode(
			StringBuffer batchCodeStringBuffer) {
		StringBuffer danBatchCodeStringBuffer = new StringBuffer();
		StringBuffer noDanBatchCodeStringBuffer = new StringBuffer();
		for (List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations : nowTotalGoalsAgainstInformationList) {
			for (TotalGoalsAgainstInformation totalGoalsAgainstInformation : totalGoalsAgainstInformations) {
				if (totalGoalsAgainstInformation.isSelected()) {
					if (totalGoalsAgainstInformation.isDan()) {
						appendTotalGoalsBatchCodeString(
								danBatchCodeStringBuffer,
								totalGoalsAgainstInformation);
					} else {
						appendTotalGoalsBatchCodeString(
								noDanBatchCodeStringBuffer,
								totalGoalsAgainstInformation);
					}

				}
			}
		}

		appendDanAndNoDanBatchCode(batchCodeStringBuffer,
				danBatchCodeStringBuffer, noDanBatchCodeStringBuffer);

	}

	/**
	 * 拼接总进球赛事注码
	 * 
	 * @param batchCodeStringBuffer
	 *            注码字符串
	 * @param totalGoalsAgainstInformation
	 *            总比分赛事信息
	 */
	private void appendTotalGoalsBatchCodeString(
			StringBuffer batchCodeStringBuffer,
			TotalGoalsAgainstInformation totalGoalsAgainstInformation) {
		batchCodeStringBuffer.append(totalGoalsAgainstInformation.getTeamId()
				+ "|");

		boolean[] isClicks = totalGoalsAgainstInformation.getIsClicks();
		int length = isClicks.length;
		for (int t = 0; t < length; t++) {
			if (isClicks[t]) {
				switch (t) {
				case 0:
					batchCodeStringBuffer.append("0");
					break;
				case 1:
					batchCodeStringBuffer.append("1");
					break;
				case 2:
					batchCodeStringBuffer.append("2");
					break;
				case 3:
					batchCodeStringBuffer.append("3");
					break;
				case 4:
					batchCodeStringBuffer.append("4");
					break;
				case 5:
					batchCodeStringBuffer.append("5");
					break;
				case 6:
					batchCodeStringBuffer.append("6");
					break;
				case 7:
					batchCodeStringBuffer.append("7");
					break;
				}
			}
		}
		batchCodeStringBuffer.append("^");
	}

	/**
	 * 获取让球胜平负后半部分注码字符串
	 * 
	 * @param batchCodeStringBuffer
	 *            注码格式StringBuffer对象
	 */
	private void getWinTieLossLaterPartBettingCode(
			StringBuffer batchCodeStringBuffer) {
		StringBuffer danBatchCodeStringBuffer = new StringBuffer();
		StringBuffer noDanBatchCodeStringBuffer = new StringBuffer();
		for (List<WinTieLossAgainstInformation> winTieLossAgainstInformations : nowWinTieLossAgainstInformationList) {
			for (WinTieLossAgainstInformation winTieLossAgainstInformation : winTieLossAgainstInformations) {

				if (winTieLossAgainstInformation.isSelected()) {
					if (winTieLossAgainstInformation.isDan()) {
						appendWinTieLossBatchCodeString(
								danBatchCodeStringBuffer,
								winTieLossAgainstInformation);
					} else {
						appendWinTieLossBatchCodeString(
								noDanBatchCodeStringBuffer,
								winTieLossAgainstInformation);
					}
				}
			}
		}

		appendDanAndNoDanBatchCode(batchCodeStringBuffer,
				danBatchCodeStringBuffer, noDanBatchCodeStringBuffer);

	}

	/**
	 * 拼接胜平负赛事的注码
	 * 
	 * @param batchCodeStringBuffer
	 *            注码字符串类
	 * @param winTieLossAgainstInformation
	 *            胜平负赛事信息类
	 */
	private void appendWinTieLossBatchCodeString(
			StringBuffer batchCodeStringBuffer,
			WinTieLossAgainstInformation winTieLossAgainstInformation) {
		batchCodeStringBuffer.append(winTieLossAgainstInformation.getTeamId()
				+ "|");
		if (winTieLossAgainstInformation.isV0IsClick()) {
			batchCodeStringBuffer.append("3");
		}

		if (winTieLossAgainstInformation.isV1IsClick()) {
			batchCodeStringBuffer.append("1");
		}

		if (winTieLossAgainstInformation.isV3IsClick()) {
			batchCodeStringBuffer.append("0");
		}

		batchCodeStringBuffer.append("^");
	}
	/**modify by pengcx 20130712 end*/
	
	/**
	 * 获取当前选择的比赛的字符串，用户投注确认页面的赛事详情的显示
	 * 
	 * @return 选择比赛字符串
	 */
	public String getSelectedAgainstString() {
		StringBuffer againstStringBufffer = new StringBuffer();
		
		/** add by pengcx 20130708 start */
		// 当前选中的赛事集合
		if (newSelectedSPList == null) {
			newSelectedSPList = new ArrayList<double[]>();
		} else {
			newSelectedSPList.clear();
		}
		/** add by pengcx 20130708 end */

		switch (playMethodType) {
		case WINTIELOSS:
			getWinTieLossSelectedAgainstString(againstStringBufffer);
			break;
		case TOTALGOALS:
			getTotalGoalsSelectedAgainstString(againstStringBufffer);
			break;
		case OVERALL:
			getOverAllSelectedAgainstString(againstStringBufffer);
			break;
		case HALFTHEAUDIENCE:
			getHalfTheAudienceSelectedAgainstString(againstStringBufffer);
			break;
		case UPDOWNSINGLEDOUBLE:
			getUpDownSingleDoubleSelectedAgainstString(againstStringBufffer);
			break;
		}
		return againstStringBufffer.toString();
	}

	/**
	 * 获取上下单选择的比赛字符串
	 * 
	 * @param againstStringBufffer
	 *            选择比赛字符串
	 */
	private void getUpDownSingleDoubleSelectedAgainstString(
			StringBuffer againstStringBufffer) {
		for (List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations : nowUpDownSigleDoubleagainstInformationList) {
			for (UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation : upDownSingleDoubleAgainstInformations) {
				if (upDownSingleDoubleAgainstInformation.isSelected()) {
					againstStringBufffer.append(
							PublicMethod.stringToHtml(upDownSingleDoubleAgainstInformation.getTeamId(),
									Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + "  ");
					/* Modify by pengcx 20130703 start */
					againstStringBufffer
							.append(upDownSingleDoubleAgainstInformation
									.getHomeTeam())
							.append(" vs ")
							.append(upDownSingleDoubleAgainstInformation
									.getGuestTeam()).append("<br>上下单双：");
					/* Modify by pengcx 20130703 end */

					
					/* Modify by pengcx 20130708 start */
					List<Double> selectedSP = new ArrayList<Double>();
					if (upDownSingleDoubleAgainstInformation.isV1IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("上单",
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double.valueOf(upDownSingleDoubleAgainstInformation.getSxds_v1()));
					}
					if (upDownSingleDoubleAgainstInformation.isV2IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("上双",
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double.valueOf(upDownSingleDoubleAgainstInformation.getSxds_v2()));
					}
					if (upDownSingleDoubleAgainstInformation.isV3IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("下单",
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double.valueOf(upDownSingleDoubleAgainstInformation.getSxds_v3()));
					}
					if (upDownSingleDoubleAgainstInformation.isV4IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("下双",
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double.valueOf(upDownSingleDoubleAgainstInformation.getSxds_v4()));
					}
					if (upDownSingleDoubleAgainstInformation.isDan()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("(胆)",
								Constants.JC_TOUZHU_TEXT_COLOR));
					}
					double[] SPs = PublicMethod.ListToArray(selectedSP);
					newSelectedSPList.add(SPs);
					againstStringBufffer.append("<br>");
					/* Modify by pengcx 20130708 end */
				}
			}
		}
	}

	/**
	 * 获取半全场选择的比赛字符串
	 * 
	 * @param againstStringBufffer
	 *            选择比赛字符串
	 */
	private void getHalfTheAudienceSelectedAgainstString(
			StringBuffer againstStringBufffer) {
		for (List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations : nowHalfTheAudienceagainstInformationList) {
			for (HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation : halfTheAudienceAgainstInformations) {
				if (halfTheAudienceAgainstInformation.isSelected()) {
					againstStringBufffer.append(
							PublicMethod.stringToHtml(halfTheAudienceAgainstInformation.getTeamId(),
									Constants.JC_TOUZHU_TITLE_TEXT_COLOR)+ "  ");
					/* Modify by pengcx 20130703 start */
					againstStringBufffer
							.append(halfTheAudienceAgainstInformation
									.getHomeTeam())
							.append(" vs ")
							.append(halfTheAudienceAgainstInformation
									.getGuestTeam()).append("<br>半全场：");
					/* Modify by pengcx 20130703 end */
					
					/* Modify by pengcx 20130708 start */
					List<Double> selectedSP = new ArrayList<Double>();
					boolean[] IsClicks = halfTheAudienceAgainstInformation
							.getIsClicks();
					for (int click_i = 0; click_i < IsClicks.length; click_i++) {
						if (IsClicks[click_i] == true) {
							againstStringBufffer
									.append(PublicMethod.stringToHtml(HalfTheAudienceAdapter.selectButtonTitles[click_i],
											Constants.JC_TOUZHU_TEXT_COLOR) + "  ")	
									.append(" ");
							selectedSP.add(halfTheAudienceAgainstInformation
									.getSelectedSp(click_i));
						}
					}
					
					double[] SPs = PublicMethod.ListToArray(selectedSP);
					newSelectedSPList.add(SPs);
					
					if (halfTheAudienceAgainstInformation.isDan()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("(胆)", 
								Constants.JC_TOUZHU_TEXT_COLOR));
					}
					
					againstStringBufffer.append("<br>");
					/* Modify by pengcx 20130708 end */
				}
			}
		}
	}

	/**
	 * 获取全场总比分选择的比赛字符串
	 * 
	 * @param againstStringBufffer
	 *            选择比赛字符串
	 */
	private void getOverAllSelectedAgainstString(
			StringBuffer againstStringBufffer) {
		for (List<OverAllAgainstInformation> overAllAgainstInformations : nowOverAllagainstInformationList) {
			for (OverAllAgainstInformation overAllAgainstInformation : overAllAgainstInformations) {
				if (overAllAgainstInformation.isSelected()) {
					againstStringBufffer.append(PublicMethod.stringToHtml(overAllAgainstInformation.getTeamId(),
							Constants.JC_TOUZHU_TITLE_TEXT_COLOR)+ "  ");
					/* Modify by pengcx 20130703 start */
					againstStringBufffer
							.append(overAllAgainstInformation.getHomeTeam())
							.append(" vs ")
							.append(overAllAgainstInformation.getGuestTeam())
							.append("<br>全场总比分：");
					/* Modify by pengcx 20130703 end */
					
					/* Modify by pengcx 20130708 start */
					List<Double> selectedSP = new ArrayList<Double>();
					boolean[] IsClicks = overAllAgainstInformation
							.getIsClicks();
					for (int click_i = 0; click_i < IsClicks.length; click_i++) {
						if (IsClicks[click_i] == true) {
							againstStringBufffer.append(
									PublicMethod.stringToHtml(OverAllAdapter.selectButtonTitles[click_i],
									Constants.JC_TOUZHU_TEXT_COLOR) + "  ")
									.append(" ");
							selectedSP.add(overAllAgainstInformation
									.getSelectedSp(click_i));
						}
					}
					double[] SPs = PublicMethod.ListToArray(selectedSP);
					newSelectedSPList.add(SPs);
					if (overAllAgainstInformation.isDan()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("(胆)", 
								Constants.JC_TOUZHU_TEXT_COLOR));
					}
					againstStringBufffer.append("<br>");
					/* Modify by pengcx 20130708 end */
				}

			}
		}
	}

	/**
	 * 获取总进球选择的比赛字符串
	 * 
	 * @param againstStringBufffer
	 *            选择比赛字符串
	 */
	private void getTotalGoalsSelectedAgainstString(
			StringBuffer againstStringBufffer) {
		for (List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations : nowTotalGoalsAgainstInformationList) {
			for (TotalGoalsAgainstInformation totalGoalsAgainstInformation : totalGoalsAgainstInformations) {
				if (totalGoalsAgainstInformation.isSelected()) {
					againstStringBufffer.append(PublicMethod.stringToHtml(totalGoalsAgainstInformation.getTeamId(), 
							Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + "  ");
					/* Modify by pengcx 20130703 start */
					againstStringBufffer
							.append(totalGoalsAgainstInformation.getHomeTeam())
							.append(" vs ")
							.append(totalGoalsAgainstInformation.getGuestTeam())
							.append("<br>总进球数：");
					/* Modify by pengcx 20130703 end */
					boolean[] IsClicks = totalGoalsAgainstInformation
							.getIsClicks();

					/* Modify by pengcx 20130708 start */
					List<Double> selectedSP = new ArrayList<Double>();
					for (int click_i = 0; click_i < IsClicks.length; click_i++) {
						if (IsClicks[click_i] == true) {
							againstStringBufffer
									.append(PublicMethod.stringToHtml(TotalGoalsAdapter.selectButtonTitles[click_i],
											Constants.JC_TOUZHU_TEXT_COLOR) + "  ")
									.append(" ");
							selectedSP.add(totalGoalsAgainstInformation
									.getSelectedSp(click_i));
						}
					}
					double[] SPs = PublicMethod.ListToArray(selectedSP);
					newSelectedSPList.add(SPs);
					
					if (totalGoalsAgainstInformation.isDan()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("(胆)", 
								Constants.JC_TOUZHU_TEXT_COLOR));
					}
					againstStringBufffer.append("<br>");
					/* Modify by pengcx 20130708 end */
				}
			}
		}
	}

	/**
	 * 获取胜平负选择的比赛字符串
	 * 
	 * @param againstStringBufffer
	 *            选择比赛字符串
	 */
	private void getWinTieLossSelectedAgainstString(
			StringBuffer againstStringBufffer) {
		for (List<WinTieLossAgainstInformation> winTieLossAgainstInformations : nowWinTieLossAgainstInformationList) {
			for (WinTieLossAgainstInformation winTieLossAgainstInformation : winTieLossAgainstInformations) {
				if (winTieLossAgainstInformation.isSelected()) {
					againstStringBufffer.append(PublicMethod.stringToHtml(winTieLossAgainstInformation.getTeamId(), 
							Constants.JC_TOUZHU_TITLE_TEXT_COLOR) + "  ");
					
					/* Modify by pengcx 20130703 start */
					againstStringBufffer
							.append(winTieLossAgainstInformation.getHomeTeam())
							.append(getLetPoint(winTieLossAgainstInformation.getLetPoint()))
							.append(winTieLossAgainstInformation.getGuestTeam())
							.append("<br>让球胜平负：");
					/* Modify by pengcx 20130703 end */

					/* Modify by pengcx 20130708 start */
					List<Double> selectedSP = new ArrayList<Double>();
					if (winTieLossAgainstInformation.isV0IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("胜", 
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double
								.valueOf(winTieLossAgainstInformation.getV3()));
					}
					if (winTieLossAgainstInformation.isV1IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("平", 
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double
								.valueOf(winTieLossAgainstInformation.getV1()));
					}
					if (winTieLossAgainstInformation.isV3IsClick()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("负", 
								Constants.JC_TOUZHU_TEXT_COLOR) + "  ");
						selectedSP.add(Double
								.valueOf(winTieLossAgainstInformation.getV0()));
					}
					double[] SPs = PublicMethod.ListToArray(selectedSP);
					newSelectedSPList.add(SPs);
					
					if (winTieLossAgainstInformation.isDan()) {
						againstStringBufffer.append(PublicMethod.stringToHtml("(胆)", 
								Constants.JC_TOUZHU_TEXT_COLOR));
					}
					/* Modify by pengcx 20130708 end */
					againstStringBufffer.append("<br>");
				}
			}
		}
	}

	/**
	 * 是否是合法的选择
	 * 
	 * @return 是否合法标识
	 */
	public boolean isLegalSelect() {
		if (selectedGameNum >= 1) {
			return true;
		} else {
			Toast.makeText(BeiJingSingleGameActivity.this, "请至少选择一场比赛", 1)
					.show();
			return false;
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

		if (nowSelectedEventsList.contains(buttonString)) {
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
				/** modify by pengcx 20130617 start */
				if (nowSelectedEventsList != null) {
					nowSelectedEventsList.clear();

					for (MyButton eventButton : eventSelectButtons) {
						if (eventButton.isOnClick()) {
							nowSelectedEventsList.add(eventButton.getBtnText());
						}
					}
				}

				if (nowSelectedEventsList != null
						&& nowSelectedEventsList.size() != 0) {
					refreshAgainstInformationShow(false, true);
					eventSelectDialog.dismiss();
				} else {
					Toast.makeText(BeiJingSingleGameActivity.this,
							"请至少选择一个赛事!", Toast.LENGTH_SHORT).show();
				}
				/** modify by pengcx 20130617 end */

				break;
			}

		}
	}

	/**
	 * 获取选择的比赛的投注次数集合
	 * 
	 * @return 投注次数集合
	 */
	public void getSelectedEventClickNumAndDanClickNum() {
		if (bettingDanList != null) {
			bettingDanList.clear();
		} else {
			bettingDanList = new ArrayList<String>();
		}

		if (bettinginfoList != null) {
			bettinginfoList.clear();
		} else {
			bettinginfoList = new ArrayList<String>();
		}

		switch (playMethodType) {
		case WINTIELOSS:
			getWinTieLossSelectdEventClickNumAndDanClickNum(bettinginfoList,
					bettingDanList);
			break;
		case TOTALGOALS:
			getTotalGoalsSelectedEventClickNumAndDanClickNum(bettinginfoList,
					bettingDanList);
			break;
		case OVERALL:
			getOverAllSelectedEventClickNumAndDanClickNum(bettinginfoList,
					bettingDanList);
			break;
		case HALFTHEAUDIENCE:
			getHalfTheAudienceSelectedEventClickNumAndDanClickNum(
					bettinginfoList, bettingDanList);
			break;
		case UPDOWNSINGLEDOUBLE:
			getUpDownSingleDoubleSelectedEventClickNumAndDanClickNum(
					bettinginfoList, bettingDanList);
			break;
		}
	}

	private void getUpDownSingleDoubleSelectedEventClickNumAndDanClickNum(
			List<String> bettinginfoList, List<String> bettingDanList) {
		for (List<UpDownSingleDoubleAgainstInformation> upDownSingleDoubleAgainstInformations : nowUpDownSigleDoubleagainstInformationList) {
			for (UpDownSingleDoubleAgainstInformation upDownSingleDoubleAgainstInformation : upDownSingleDoubleAgainstInformations) {
				if (upDownSingleDoubleAgainstInformation.isSelected()) {
					bettinginfoList.add(String
							.valueOf(upDownSingleDoubleAgainstInformation
									.getClickNum()));

					if (upDownSingleDoubleAgainstInformation.isDan()) {
						bettingDanList.add("true");
					} else {
						bettingDanList.add("false");
					}
				}
			}
		}
	}

	private void getHalfTheAudienceSelectedEventClickNumAndDanClickNum(
			List<String> bettinginfoList, List<String> bettingDanList) {
		for (List<HalfTheAudienceAgainstInformation> halfTheAudienceAgainstInformations : nowHalfTheAudienceagainstInformationList) {
			for (HalfTheAudienceAgainstInformation halfTheAudienceAgainstInformation : halfTheAudienceAgainstInformations) {
				if (halfTheAudienceAgainstInformation.isSelected()) {
					bettinginfoList.add(String
							.valueOf(halfTheAudienceAgainstInformation
									.getClickNum()));

					if (halfTheAudienceAgainstInformation.isDan()) {
						bettingDanList.add("true");
					} else {
						bettingDanList.add("false");
					}
				}
			}
		}
	}

	private void getOverAllSelectedEventClickNumAndDanClickNum(
			List<String> bettinginfoList, List<String> bettingDanList) {
		for (List<OverAllAgainstInformation> overAllAgainstInformations : nowOverAllagainstInformationList) {
			for (OverAllAgainstInformation overAllAgainstInformation : overAllAgainstInformations) {
				if (overAllAgainstInformation.isSelected()) {
					bettinginfoList.add(String
							.valueOf(overAllAgainstInformation.getClickNum()));

					if (overAllAgainstInformation.isDan()) {
						bettingDanList.add("true");
					} else {
						bettingDanList.add("false");
					}
				}
			}
		}
	}

	private void getTotalGoalsSelectedEventClickNumAndDanClickNum(
			List<String> bettinginfoList, List<String> bettingDanList) {
		for (List<TotalGoalsAgainstInformation> totalGoalsAgainstInformations : nowTotalGoalsAgainstInformationList) {
			for (TotalGoalsAgainstInformation totalGoalsAgainstInformation : totalGoalsAgainstInformations) {
				if (totalGoalsAgainstInformation.isSelected()) {
					bettinginfoList
							.add(String.valueOf(totalGoalsAgainstInformation
									.getClickNum()));

					if (totalGoalsAgainstInformation.isDan()) {
						bettingDanList.add("true");
					} else {
						bettingDanList.add("false");
					}
				}

			}
		}
	}

	private void getWinTieLossSelectdEventClickNumAndDanClickNum(
			List<String> bettinginfoList, List<String> bettingDanList) {
		for (List<WinTieLossAgainstInformation> winTieLossAgainstInformations : nowWinTieLossAgainstInformationList) {
			for (WinTieLossAgainstInformation winTieLossAgainstInformation : winTieLossAgainstInformations) {
				if (winTieLossAgainstInformation.isSelected()) {
					bettinginfoList
							.add(String.valueOf(winTieLossAgainstInformation
									.getClickNum()));

					if (winTieLossAgainstInformation.isDan()) {
						bettingDanList.add("true");
					} else {
						bettingDanList.add("false");
					}
				}
			}
		}
	}

	/**
	 * 选择的场次是否合法
	 * 
	 * @return
	 */
	public boolean isSelectedEventNumLegal() {
		boolean isLegal = true;

		switch (playMethodType) {
		case WINTIELOSS:
			if (selectedGameNum >= 15) {
				isLegal = false;
			}
			break;
		case TOTALGOALS:
			if (selectedGameNum >= 10) {
				isLegal = false;
			}
			break;
		case OVERALL:
			if (selectedGameNum >= 10) {
				isLegal = false;
			}
			break;
		case HALFTHEAUDIENCE:
			if (selectedGameNum >= 10) {
				isLegal = false;
			}
			break;
		case UPDOWNSINGLEDOUBLE:
			if (selectedGameNum >= 10) {
				isLegal = false;
			}
			break;
		}
		return isLegal;
	}
	
	private String getLetPoint(String letpoint) {
		if (!"".equals(letpoint) && !"0".equals(letpoint)) {
			return " ("+letpoint+") ";
		} else {
			return " vs ";
		}
	}
}
