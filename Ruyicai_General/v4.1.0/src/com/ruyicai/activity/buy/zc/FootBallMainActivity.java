package com.ruyicai.activity.buy.zc;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.BuyGameDialog;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.notice.NoticeZCActivity;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.net.transaction.FootballInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class FootBallMainActivity extends Activity {
	public String currentIssue;
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private Button layout_football_issue;
	private TextView layout_football_time;
	private Toast toast;
	private TextView textTeamNum;
	private PopupWindow popupwindow;
	/**玩法介绍对话框*/
	private BuyGameDialog gameDialog;
	private Handler gameHandler = new Handler(); 
	/** 标题 */
	private TextView titleView;
	private ImageButton againButton;
	private Button playIntroduce;
	private ListView footBallList;
	private LinearLayout downLayersLayout;
	private LinearLayout upLayersLayout;
	private LinearLayout middleLayersLayout;
	/** 下拉弹出的玩法选择布局 */
	private LinearLayout mainPalySelectLayout;
	/** 玩法切换布局 */
	private LinearLayout playSelectBtnLayout;
	/** 下拉弹出的赛事选择布局 */
	private LinearLayout issueSelectLayout;
	private int mScreenWidth;
	/**玩法切换按钮*/
	private Button[] playBtn = null;
	/**彩种编号数组*/
	private String[] mLotnoArray = {Constants.LOTNO_SFC, Constants.LOTNO_RX9, 
			Constants.LOTNO_LCB, Constants.LOTNO_JQC};
	private ProgressDialog progressdialog;
	private Context mContext;
	private MyButton[] myBtns;
	private int[] mBgId= {R.drawable.jc_main_team_select_normal, R.drawable.jc_main_team_select_click};
	private int[] mPaintColor= {Color.BLACK, Color.WHITE};
	private int mIssueIndex = 0;
	private int[] mIssueIndexArray = {0,0,0,0};
	private int mPlayIndex = 0;
	private ImageButton startTouZhu;
	/**玩法适配器数组*/
	private FootBallBaseAdapter[] mFootBallAdapters = new FootBallBaseAdapter[4];
	/**存放不同玩法的对阵数据*/
	private ArrayList[] mTeamInfoLists = new ArrayList[4];
	/**存放不同玩法的期号*/
	private ArrayList[] mIssueArray = new ArrayList[4];
	private LinearLayout noGamePrompt;
	private boolean[] isShowState = {false, false, false, false};
	private int[] mStringId = {R.string.zc_14sf_play, R.string.zc_rx9_play, 
			R.string.zc_6cb_play, R.string.zc_4jq_play};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.buy_footballlottery_layout);
		mPlayIndex = getIntent().getIntExtra("index", 0);
		mScreenWidth = PublicMethod.getDisplayWidth(this);
		mContext = this;
		progressdialog = new ProgressDialog(this);
		initView();
		getZCAdvanceBatchCodeData(mLotnoArray[mPlayIndex]);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mPlayIndex = intent.getIntExtra("index", 0);
		if (playBtn == null) {
			initPlayBtn();
		}
		for (int i = 0; i < playBtn.length; i++) {
			if (mPlayIndex == i) {
				playBtn[i].setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
				playBtn[i].setTextColor(getResources().getColor(R.color.white));
				titleView.setText(mStringId[i]);
				if (!isShowState[mPlayIndex]) {
					if (mIssueArray[mPlayIndex] == null) {
						getZCAdvanceBatchCodeData(mLotnoArray[mPlayIndex]);
					} else {
						getTeamInfo(0);
					}
				} else {
					setIssue(mIssueIndexArray[mPlayIndex]);
					initList();
				}
				setViewState();
			} else {
				playBtn[i].setBackgroundResource(R.drawable.beijing_playmethodbutton_normal);
				playBtn[i].setTextColor(getResources().getColor(R.color.black));
			}
		}
	}

	private void initView() {
		ZcViewClickListener listener = new ZcViewClickListener();
		playSelectBtnLayout = (LinearLayout) findViewById(R.id.jc_play_select);
		downLayersLayout = (LinearLayout) findViewById(R.id.jc_main_team_layout_layers_down);
		middleLayersLayout = (LinearLayout) findViewById(R.id.jc_main_team_layout_layers_middle);
		upLayersLayout = (LinearLayout) findViewById(R.id.jc_main_team_layout_layers_up);
		mainPalySelectLayout = (LinearLayout) findViewById(R.id.zc_play_change_layout);
		issueSelectLayout = (LinearLayout) findViewById(R.id.jc_main_team_select);
		titleView = (TextView) findViewById(R.id.layout_main_text_title);
		titleView.setText(mStringId[mPlayIndex]);
		layout_football_issue = (Button) findViewById(R.id.layout_football_issue);
		layout_football_time = (TextView) findViewById(R.id.layout_football_time);
		footBallList = (ListView) findViewById(R.id.buy_footballlottery_list);
		textTeamNum = (TextView) findViewById(R.id.buy_jc_main_text_team_num);
		setTeamNum(0);
		againButton = (ImageButton) findViewById(R.id.buy_zixuan_img_again);
		startTouZhu = (ImageButton) findViewById(R.id.buy_footballlottery_img_touzhu);
		playIntroduce = (Button) findViewById(R.id.layout_main_img_return);
		noGamePrompt = (LinearLayout) findViewById(R.id.no_game_prompt);
		playIntroduce.setOnClickListener(listener);
		startTouZhu.setOnClickListener(listener);
		playSelectBtnLayout.setOnClickListener(listener);
		downLayersLayout.setOnClickListener(listener);
		middleLayersLayout.setOnClickListener(listener);
		upLayersLayout.setOnClickListener(listener);
		issueSelectLayout.setOnClickListener(listener);
		layout_football_issue.setOnClickListener(listener);
		againButton.setOnClickListener(listener);
		initPlayBtn();
	}
	
	private void initPlayBtn() {
		playBtn = new Button[4];
		playBtn[0] = (Button) findViewById(R.id.zc_play_change_button_14sfc);
		playBtn[1] = (Button) findViewById(R.id.zc_play_change_button_rx9);
		playBtn[2] = (Button) findViewById(R.id.zc_play_change_button_6cb);
		playBtn[3] = (Button) findViewById(R.id.zc_play_change_button_4jq);
		playBtn[mPlayIndex].setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
		playBtn[mPlayIndex].setTextColor(getResources().getColor(R.color.white));
		PlayChangeBtnClickListener clickListener = new PlayChangeBtnClickListener();
		for (int i = 0; i < playBtn.length; i++) {
			playBtn[i].setOnClickListener(clickListener);
		}
	}
	
	/**
	 * 玩法切换监听
	 */
	private class PlayChangeBtnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < playBtn.length; i++) {
				if (v.getId() == playBtn[i].getId()) {
					playBtn[i].setBackgroundResource(R.drawable.beijing_playmethodbutton_click);
					playBtn[i].setTextColor(getResources().getColor(R.color.white));
					mPlayIndex = i;
					if (!isShowState[mPlayIndex]) {
						if (mIssueArray[mPlayIndex] == null) {
							getZCAdvanceBatchCodeData(mLotnoArray[mPlayIndex]);
						} else {
							getTeamInfo(0);
						}
					} else {
						setIssue(mIssueIndexArray[mPlayIndex]);
						initList();
					}
					setViewState();
				} else {
					playBtn[i].setBackgroundResource(R.drawable.beijing_playmethodbutton_normal);
					playBtn[i].setTextColor(getResources().getColor(R.color.black));
				}
			}
		}
	}
	
	/**
	 * 获取期号
	 * @param Lotno
	 */
	private void getZCAdvanceBatchCodeData(final String Lotno) {
		showDialog();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String advanceBatchCodeData = FootballInterface.getInstance().getAdvanceBatchCodeList(Lotno);
				String errorCode = "";
				String message = "";
				try {
					JSONObject advanceBatchCode = new JSONObject(
							advanceBatchCodeData);
					errorCode = advanceBatchCode.getString("error_code");
					message = advanceBatchCode.getString("message");
					JSONArray batchCodeArray = null;
					if (advanceBatchCode.has("result")) {
						batchCodeArray = advanceBatchCode
								.getJSONArray("result");
					}
					if (errorCode.equals("0047") || errorCode.equals("0000")) {
						if (batchCodeArray == null || batchCodeArray.length() == 0) {
							handler.sendEmptyMessage(5);
							return;
						}
					}
					Message msg = handler.obtainMessage();
					if (errorCode.equals("0000")) {
						ArrayList<AdvanceBatchCode> bactchArray = new ArrayList<AdvanceBatchCode>();
						bactchArray.clear();
						for (int i = 0; i < batchCodeArray.length(); i++) {
							JSONObject item = batchCodeArray.getJSONObject(i);
							AdvanceBatchCode aa = new AdvanceBatchCode();
							aa.setBatchCode(item.getString("batchCode"));
							aa.setEndTime(formatEndtime(item
									.getString("endTime")));
							if (item.has("state")) {
								aa.setState(item.getString("state"));
							}
							bactchArray.add(aa);
							mIssueArray[mPlayIndex] = bactchArray;
						}
						msg.what = 2;
					} else {
						msg.what = 3;
						msg.obj = message;
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					handler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 下拉玩法切换对话框
	 */
	private void showPlayChangeDialog() {
		mainPalySelectLayout.setVisibility(View.VISIBLE);
		upLayersLayout.setVisibility(View.VISIBLE);
		middleLayersLayout.setVisibility(View.VISIBLE);
//		mainPalySelectLayout.startAnimation(AnimationUtils.loadAnimation(this,
//				R.anim.jc_top_menu_window_enter));
		PublicMethod.setLayoutHeight(45, upLayersLayout, this);
	}
	
	/**
	 * 下拉期号选择对话框
	 */
	private void createIssueDialog() {
		LinearLayout layoutMain = (LinearLayout)findViewById(R.id.jc_linear_check_all);
		if (mIssueArray[mPlayIndex] != null && mIssueArray[mPlayIndex].size() > 0) {
			myBtns = new MyButton[mIssueArray[mPlayIndex].size()];
			addLayout(layoutMain, myBtns);
		} else {
			myBtns = new MyButton[0];
		}
	}
	
	private void addLayout(LinearLayout layoutMain, MyButton[] myBtns) {
		layoutMain.removeAllViews();
		int length = mIssueArray[mPlayIndex].size();
		int lineNum = 3;// 每行个数
		int lastNum = length % lineNum;// 最后一行个数
		int line = 1;// 行数
		mIssueIndex = 0;
		if (length >= lineNum) {
			line = length / lineNum;
			for (int i = 0; i < line; i++) {
				LinearLayout layoutOne = addLine(lineNum, i, myBtns, lineNum);
				layoutMain.addView(layoutOne);
			}
			if (lastNum > 0) {
				LinearLayout layoutOne = addLine(lastNum, line, myBtns, lineNum);
				layoutMain.addView(layoutOne);
			}
		} else {
			LinearLayout layoutOne = addLine(length, 0, myBtns, lineNum);
			layoutMain.addView(layoutOne);
		}
	}

	private LinearLayout addLine(int lastNum, int line, final MyButton[] myBtns,
			int lineNum) {
		LinearLayout layoutOne = new LinearLayout(mContext);
		for (int j = 0; j < lastNum; j++) {
			
			final MyButton btn = new MyButton(mContext);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					(mScreenWidth-PublicMethod.getPxInt(40, mContext))/3, 
					PublicMethod.getPxInt(42, mContext));
			if (j == 0) {
				params.setMargins(0,
						PublicMethod.getPxInt(10, mContext), 0, 0);
			} else {
				params.setMargins(PublicMethod.getPxInt(10, mContext),
						PublicMethod.getPxInt(10, mContext), 0, 0);
			}
			if (mIssueIndexArray[mPlayIndex] == mIssueIndex) {
				btn.setOnClick(true);
			} else {
				btn.setOnClick(false);
			}
			btn.setTag(mIssueIndex);
			btn.setLayoutParams(params);
			myBtns[line * lineNum + j] = btn;
			AdvanceBatchCode aBatchCode = (AdvanceBatchCode)mIssueArray[mPlayIndex].get(line * lineNum + j);
			String issue = aBatchCode.getBatchCode();
			String state = aBatchCode.getState();
			
			if ("5".equals(state)) {
				String waitIssue = getResources().getString(R.string.football_wait_issue);
				btn.setBtnText(issue+waitIssue);
			} else {
				btn.setBtnText(issue);
			}
			btn.initBg(mBgId);
			btn.setPaintColorArray(mPaintColor);
			btn.switchBg();
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 0; i < myBtns.length; i++) {
						myBtns[i].setOnClick(false);
						myBtns[i].switchBg();
					}
					btn.setOnClick(true);
					btn.switchBg();
					int which = (Integer)((MyButton)v).getTag();
					mIssueIndexArray[mPlayIndex] = which;
					if (mIssueArray[mPlayIndex] == null) {
						getZCAdvanceBatchCodeData(mLotnoArray[mPlayIndex]);
						return;
					}
					AdvanceBatchCode batchCode = (AdvanceBatchCode)mIssueArray[mPlayIndex].get(which);
					setIssue(which);
					String selectIssue = batchCode.getBatchCode();
					showDialog();
					getData(mLotnoArray[mPlayIndex], selectIssue);
					setViewState();
				}
			});
			
			layoutOne.addView(btn);
			mIssueIndex++;
		}
		return layoutOne;
	}
	
	private String formatBatchCode(String batchCode) {
		return "第" + batchCode + "期";
	}

	private String formatEndtime(String endTime) {
		return "截止时间：" + endTime;
	}

	private class ZcViewClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_main_img_return:
				createDialog();
				break;
				
			case R.id.buy_zixuan_img_again:
				if (mFootBallAdapters[mPlayIndex] != null) {
					mFootBallAdapters[mPlayIndex].clearSelected();
				}
				setTeamNum(0);
				break;
				
			case R.id.buy_footballlottery_img_touzhu:
				beginTouZhu();
				break;

			case R.id.jc_play_select:
				showPlayChangeDialog();
				break;
				
			case R.id.layout_football_issue:
				issueShowState();
				break;
			case R.id.jc_main_team_layout_layers_down:
			case R.id.jc_main_team_layout_layers_up:
			case R.id.jc_main_team_layout_layers_middle:
				setViewState();
				break;
				
			}
		}
	}
	
	private void issueShowState() {
		if (issueSelectLayout.getVisibility() == View.GONE
				|| issueSelectLayout.getVisibility() == View.INVISIBLE) {
			createIssueDialog();
			PublicMethod.setLayoutHeight(85, upLayersLayout, mContext);
			issueSelectLayout.setVisibility(View.VISIBLE);
			downLayersLayout.setVisibility(View.VISIBLE);
			upLayersLayout.setVisibility(View.VISIBLE);
//			issueSelectLayout.startAnimation(AnimationUtils.loadAnimation(
//					mContext, R.anim.jc_top_menu_window_enter));
		} else {
			setViewState();
		}
	}

	/**
	 * 消息处理函数
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				initList();
				dismissDialog();
				setIssue(mIssueIndexArray[mPlayIndex]);
				Toast.makeText(getBaseContext(), "数据解析异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 1:
				initList();
				isShowState[mPlayIndex] = true;
				dismissDialog();
				break;
			case 2:
				getTeamInfo(0);
				break;
			case 3:
				initList();
				dismissDialog();
				setIssue(mIssueIndexArray[mPlayIndex]);
				Toast.makeText(getBaseContext(), msg.obj + "",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				dismissDialog();
				FootballContantDialog.alertIssueNOFQueue(mContext);
				break;
			case 5:
				setMainLayoutState();
				dismissDialog();
				break;
			}
		}
	};
	
	private void showDialog() {
		if (progressdialog == null) {
			progressdialog = new ProgressDialog(this);
		}
		progressdialog.show();
		progressdialog.setCancelable(false);
		View dialogView = PublicMethod.getView(this);
		progressdialog.getWindow().setContentView(dialogView);
	}
	
	private void dismissDialog() {
		if (progressdialog != null && progressdialog.isShowing()) {
			progressdialog.dismiss();
		}
	}
	
	/**
	 * 初始化列表
	 */
	private void initList() {
		switch (mPlayIndex) {
		case 0:
			if (mFootBallAdapters[0] == null) {
				mFootBallAdapters[0] = new FootBallSFAdapter(this);
			}
			break;
			
		case 1:
			if (mFootBallAdapters[1] == null) {
				mFootBallAdapters[1] = new FootBallRX9Adapter(this);
			}
			break;
			
		case 2:
			if (mFootBallAdapters[2] == null) {
				mFootBallAdapters[2] = new FootBall6CBAdapter(this);
			}
			break;
			
		case 3:
			if (mFootBallAdapters[3] == null) {
				mFootBallAdapters[3] = new FootBall4CJQAdapter(this);
			}
			break;	
		}
		mFootBallAdapters[mPlayIndex].setList(mTeamInfoLists[mPlayIndex]);
		if (mIssueArray[mPlayIndex] != null) {
			AdvanceBatchCode adBatchCode = (AdvanceBatchCode) mIssueArray[mPlayIndex]
					.get(mIssueIndexArray[mPlayIndex]);
			mFootBallAdapters[mPlayIndex].mIssueState = adBatchCode.getState().trim();
		}
		footBallList.setAdapter(mFootBallAdapters[mPlayIndex]);
		titleView.setText(mStringId[mPlayIndex]);
	}
	
	private void getTeamInfo(int index) {
		if (mIssueArray[mPlayIndex] == null) {
			getZCAdvanceBatchCodeData(mLotnoArray[mPlayIndex]);
			return;
		}
		int tempIndex = 0;
		for (int i = 0; i < mIssueArray[mPlayIndex].size(); i++) {
			AdvanceBatchCode batchMsg = (AdvanceBatchCode) mIssueArray[mPlayIndex].get(i);
			if (!"5".equals(batchMsg.getState())) {
				tempIndex = i;
				break;
			}
		}
		
		AdvanceBatchCode batchObj = (AdvanceBatchCode) mIssueArray[mPlayIndex].get(tempIndex);
		currentIssue = batchObj.getBatchCode().trim();
		layout_football_issue.setText(formatBatchCode(currentIssue));
		layout_football_time.setText(batchObj.getEndTime());
		mIssueIndexArray[mPlayIndex] = tempIndex;
		getData(mLotnoArray[mPlayIndex], currentIssue);
	}
	
	private void setIssue(int index) {
		if (mIssueArray[mPlayIndex] != null) {
			AdvanceBatchCode batchMsg = (AdvanceBatchCode) mIssueArray[mPlayIndex].get(index);
			currentIssue = batchMsg.getBatchCode().trim();
			layout_football_issue.setText(formatBatchCode(currentIssue));
			layout_football_time.setText(batchMsg.getEndTime());
		} else {
			layout_football_issue.setText("");
			layout_football_time.setText("");
		}
	}
	
	/**
	 * 获取对阵数据
	 */
	private void getData(final String lotno, final String batchCode) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				String message = "";
				String re = FootballInterface.getInstance().getZCData(lotno, batchCode);
				try {
					JSONObject obj = new JSONObject(re);
					error_code = obj.getString("error_code");
					message = obj.getString("message");
					Message msg = handler.obtainMessage();
					if (error_code.equals("0000")) {
						JSONArray result = obj.getJSONArray("result");
						if(result == null || result.length() == 0) {
							handler.sendEmptyMessage(5);
							return;
						}
						if (mTeamInfoLists[mPlayIndex] == null) {
							mTeamInfoLists[mPlayIndex] = new ArrayList();
						}
						mTeamInfoLists[mPlayIndex].clear();
						for (int i = 0; i < result.length(); i++) {
							JSONObject json = result.getJSONObject(i);
							TeamInfo team = new TeamInfo();
							team.setTeamId(json.getString("teamId"));
							team.setDate(json.getString("matchTime"));
							team.setLeagueName(json.getString("leagueName"));
							team.setHomeTeam(json.getString("homeTeam"));
							team.setGuestTeam(json.getString("guestTeam"));
							team.setHomeOdds(json.getString("homeWinAverageOuPei"));
							team.setVsOdds(json.getString("standoffAverageOuPei"));
							team.setGuestOdds(json.getString("guestWinAverageOuPei"));
							mTeamInfoLists[mPlayIndex].add(team);
						}
						msg.what = 1;
					} else if (error_code.equals("200005")){
						msg.what = 4;
					} else {
						msg.what = 3;
						msg.obj = message;
					}
					handler.sendMessage(msg);
				} catch (Exception e) {
					handler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
	public class AdvanceBatchCode {
		private String BatchCode;
		private String EndTime;
		private String state;

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
		
		public String getBatchCode() {
			return BatchCode;
		}

		public void setBatchCode(String batchCode) {
			BatchCode = batchCode;
		}

		public String getEndTime() {
			return EndTime;
		}

		public void setEndTime(String endTime) {
			EndTime = endTime;
		}
	}
	
	private void setViewState() {
		mainPalySelectLayout.setVisibility(View.GONE);
		issueSelectLayout.setVisibility(View.GONE);
		downLayersLayout.setVisibility(View.GONE);
		middleLayersLayout.setVisibility(View.GONE);
		upLayersLayout.setVisibility(View.GONE);
	}
	
	private void initBetPojo() {
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionid = pre.getStringValue("sessionid");
		String phonenum = pre.getStringValue("phonenum");
		String userno = pre.getStringValue("userno");
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(mFootBallAdapters[mPlayIndex].getZhuMa());
		betPojo.setLotno(mLotnoArray[mPlayIndex]);
		betPojo.setBatchnum("1");
		betPojo.setBatchcode(currentIssue);
		betPojo.setLotmulti("1");
		betPojo.setBettype("bet");
		betPojo.setAmount(mFootBallAdapters[mPlayIndex].getZhuShu() * 200 + "");
		betPojo.setZhushu(mFootBallAdapters[mPlayIndex].getZhuShu() + "");
	}
	
	public void beginTouZhu() {
		if (mFootBallAdapters[mPlayIndex] == null) {
			return;
		}
		int iZhuShu = mFootBallAdapters[mPlayIndex].getZhuShu();// 注数是注数*倍数的结果
		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
		String sessionid = pre.getStringValue("sessionid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivityForResult(intentSession, 0);
			startTouZhu.setClickable(true);
		} else {
			if (mFootBallAdapters[mPlayIndex].isTouZhu()
					&& !"5".equals(mFootBallAdapters[mPlayIndex].mIssueState)) {
				Toast.makeText(this, "请至少选择一注！",
						Toast.LENGTH_SHORT).show();
			} else if (iZhuShu > 10000) {
				DialogExcessive();
			} else {
				initBetPojo();
				toorderdetail();
			}
		}
	}

	private void toorderdetail() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betPojo);
		Intent intent = new Intent(this, Footballorderdail.class);
		intent.putExtra("tpye", "zc");
		intent.putExtra("zhuma", mFootBallAdapters[mPlayIndex].getZhuMa());
		startActivity(intent);
		
		/**投注完是否需要清空 yejc*/
//		mFootBallAdapters[mPlayIndex].clearSelected();
	}
	
	/**
	 * 单笔投注大于2万元时的对话框
	 */
	protected void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("投注失败");
		builder.setMessage("单笔投注不能大于10000注");
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		builder.show();
	}
	
	/**
	 * 提示信息
	 */
	public void changeTextSumMoney(int iZhuShu) {
		if (iZhuShu != 0) {
			StringBuffer touzhuAlert = new StringBuffer();
			if (toast == null) {
				toast = Toast.makeText(this, touzhuAlert.toString(),
						Toast.LENGTH_SHORT);
			}
			touzhuAlert.append("共").append(iZhuShu).append("注，共")
					.append((iZhuShu * 2)).append("元");
			toast.setText(touzhuAlert.toString());
			toast.show();
		}
	}
	
	public void setTeamNum(int num) {
		textTeamNum.setText("已选择了" + num + "场比赛");
	}
	
	/**
	 * 创建下拉列表
	 */
	private void createDialog() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View popupView = (LinearLayout) inflate.inflate(
				R.layout.buy_group_window, null);
		// 如果是双色球，在popupwindow中添加模拟选号选项
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupwindow.setTouchable(true); // 设置PopupWindow可触摸
		popupwindow.setOutsideTouchable(true);
		popupView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
					popupwindow = null;
				}
				return false;
			}
		});
		popupwindow.showAsDropDown(playIntroduce);
		// 玩法介绍
		final LinearLayout layoutGame = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout1);
		layoutGame.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutGame.setBackgroundResource(R.drawable.buy_group_layout_b);
				if (gameDialog == null) {
					gameDialog = new BuyGameDialog(mContext, mLotnoArray[mPlayIndex], gameHandler);
				}
				gameDialog.showDialog();
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
				
			}
		});

		// 历史开奖
		final LinearLayout layoutHosity = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout2);
		layoutHosity.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutHosity
						.setBackgroundResource(R.drawable.buy_group_layout_b);
				Intent intent = new Intent(FootBallMainActivity.this, NoticeZCActivity.class);
				intent.putExtra("position", mPlayIndex);
				startActivity(intent);
				
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
			}

		});

		// 投注查询
		final LinearLayout layoutQuery = (LinearLayout) popupView
				.findViewById(R.id.buy_group_layout4);
		layoutQuery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				RWSharedPreferences shellRW = new RWSharedPreferences(mContext,
						"addInfo");
				String userno = shellRW.getStringValue(ShellRWConstants.USERNO);
				if (userno == null || userno.equals("")) {
					Intent intentSession = new Intent(mContext, UserLogin.class);
					startActivity(intentSession);
				} else {
					Intent intent = new Intent(FootBallMainActivity.this,
							BetQueryActivity.class);
					intent.putExtra("lotno", Constants.LOTNO_ZC);
					startActivity(intent);
				}
				
				if (popupwindow != null && popupwindow.isShowing()) {
					popupwindow.dismiss();
				}
				
			}

		});
		LinearLayout layoutParentLuck = (LinearLayout) popupView
				.findViewById(R.id.buy_group_one_layout3);
		layoutParentLuck.setVisibility(View.GONE);
	}
	
	private void setMainLayoutState() {
		footBallList.setVisibility(View.GONE);
		noGamePrompt.setVisibility(View.VISIBLE);
	}
}
