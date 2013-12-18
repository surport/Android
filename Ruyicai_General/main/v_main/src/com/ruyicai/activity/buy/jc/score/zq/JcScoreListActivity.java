package com.ruyicai.activity.buy.jc.score.zq;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.ScoreListInterface;
import com.ruyicai.util.RWSharedPreferences;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JcScoreListActivity extends Activity {
	protected ListView listMain;
	protected JcInfoAdapter adapter;
	protected Context context;
	protected String TYPE = "0";
	protected ProgressDialog mProgress = null;
	protected ArrayAdapter<String> adapterArray;
	protected Spinner money_brank;
	protected int index;
	protected boolean isOne = true;
	protected List<String> allcountries;
	protected int todayIndex;
	protected List<ScoreInfo> listInfo;
	protected String reguestType = "immediateScore";
	protected RWSharedPreferences shellRw;
	protected boolean isLq = false;
	protected boolean isTrack = false;
	LinearLayout layoutSpinner;
	/**add by yejc 20130712 start*/
	protected boolean isBeiDan = false;
	RefreshBroadcastReceiver refreshReceiver = new RefreshBroadcastReceiver();
	public final static String BROADCAST_ACTION  = "BROADCAST_ACTION";
	int tabIndex = 0;
	protected String playType = "jczq";
	/**add by yejc 20130712 end*/

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jc_score_list);
		layoutSpinner = (LinearLayout) findViewById(R.id.jc_score_layout);
		layoutSpinner.setVisibility(View.VISIBLE);
		context = this;
		setIsLq();
		setReguestType();
		getPreferences();
		TYPE = getType();
		/**add by yejc 20130719 start*/
		if (isBeiDan) {
			TextView tv = (TextView)findViewById(R.id.jc_score_list_text);
			tv.setText("选择期号：");
		}
		tabIndex = getIntent().getIntExtra("index", 0);
		/**add by yejc 20130719 end*/
	}

	public void setIsLq() {
		isLq = false;
	}

	/**
	 * 读取数据
	 */
	public void getPreferences() {
		if (isLq) {
			shellRw = new RWSharedPreferences(this,
					ShellRWConstants.JCLQ_PREFER);
		} else {
			shellRw = new RWSharedPreferences(this,
					ShellRWConstants.JCZQ_PREFER);
		}
	}

	/**
	 * 遍历mapTrack数据集
	 */
	public boolean isTrack(String date, String event) {
		Map<String, ?> mapTrack = shellRw.getAllKey();
		if (mapTrack.containsKey(date)) {
			String events = shellRw.getStringValue(date);
			String eventStrs[] = events.split(";");
			for (int i = 0; i < eventStrs.length; i++) {
				if (eventStrs[i].equals(event)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setReguestType() {
		reguestType = "immediateScore";
	}

	public void initNameSpinner() {
		if (money_brank == null && allcountries != null) {
			money_brank = (Spinner) findViewById(R.id.jc_score_list_spinner);
			money_brank.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					index = position;
					if (!isOne) {
						spinnerOnclik(allcountries.get(position));
					} else {
						isOne = false;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
			adapterArray = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, allcountries);
			adapterArray.setDropDownViewResource(R.layout.myspinner_dropdown);
			money_brank.setAdapter(adapterArray);
			if (todayIndex - 1 > 0) {
				money_brank.setSelection(todayIndex - 1, false);
			} else {
				money_brank.setSelection(0, false);
			}
			/**add by yejc 20130725 start*/
			index = money_brank.getSelectedItemPosition();
			/**add by yejc 20130725 end*/

		}
	}

	public void spinnerOnclik(String date) {
		if (listInfo != null) {
			listInfo.clear();
			initList();
		}
		getScoreNet(TYPE, dateString(date), reguestType);
	}

	public String dateString(String date) {
		String dateStr = "";
		String dates[] = date.split("-");
		for (int i = 0; i < dates.length; i++) {
			dateStr += dates[i];
		}
		return dateStr;
	}

	protected void onResume() {
		super.onResume();
		/**add by yejc 20130716 start*/
		IntentFilter filter = new IntentFilter(BROADCAST_ACTION);    
        registerReceiver(refreshReceiver, filter);
        /**add by yejc 20130716 end*/
		initListInfo();
	}

	public void initListInfo() {
		if (tabIndex == 2) {
			layoutSpinner.setVisibility(View.GONE);
			if (isLq) {
				getCurrentScoreNet("jingCaiLq");
			} else if (isBeiDan) {
				getCurrentScoreNet("beiDan");
			} else{
				getCurrentScoreNet("jingCaiZq");
			}
		} else {
			if (allcountries == null) {
				getScoreNet(TYPE, "", reguestType);
			} else {
				spinnerOnclik(allcountries.get(index));
			}
		}
	}

	/**
	 * 获取列表联网
	 */
	public void getScoreNet(final String type, final String date,
			final String reguestType) {
		mProgress = UserCenterDialog.onCreateDialog(context);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String re = ScoreListInterface.getScore(type, date,
								reguestType);
					
					mProgress.dismiss();
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					allcountries = setSpinnerDate(obj.getString("date"));
					todayIndex = Integer.parseInt(obj.getString("todayIndex"));
					if (error_code.equals("0000")) {
						final JSONArray jsonArray = obj.getJSONArray("result");

						handler.post(new Runnable() {
							@Override
							public void run() {
								listInfo = getScoreInfo(jsonArray);
								initNameSpinner();
								initList();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								initNameSpinner();
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public List<String> setSpinnerDate(String date) {
		List<String> allcountries = new ArrayList<String>();
		String[] dates = date.split(";");
		for (int i = 0; i < dates.length; i++) {
			allcountries.add(dates[i]);
		}
		return allcountries;
	}

	protected List<ScoreInfo> getScoreInfo(JSONArray jsonArray) {
		List<ScoreInfo> listInfo = new ArrayList<ScoreInfo>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				ScoreInfo info = new ScoreInfo();
				info.setJson(json);
				info.setTeamName(json.getString("sclassName"));
				info.sethTeam(json.getString("homeTeam"));
				info.setState(json.getString("stateMemo"));
				info.setTime(json.getString("matchTime"));
				info.setvTeam(json.getString("guestTeam"));
				if (isBeiDan) {
					info.setEvent(json.getString("bdEvent"));
				} else {
					info.setEvent(json.getString("event"));
				}
				info.setHomeScore(json.getString("homeScore"));
				info.setGuestScore(json.getString("guestScore"));
				info.setHomeHalfScore(json.getString("homeHalfScore"));
				info.setGuestHalfScore(json.getString("guestHalfScore"));
				info.setRed(json.getString("red"));
				info.setYellow(json.getString("yellow"));
				info.setMatchStateMemo(json.getString("matchStateMemo"));
				info.setMatchState(json.getString("matchState"));
				/**add by yejc 20130826 start*/
				if(json.has("progressedTime")) {
					info.setProgressedTime(json.getString("progressedTime"));
				}
				/**add by yejc 20130826 end*/
				listInfo.add(info);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listInfo;

	}

	public void initList() {

		listMain = (ListView) findViewById(R.id.buy_jc_main_listview);
		adapter = new JcInfoAdapter(context, listInfo);
		listMain.setAdapter(adapter);
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				turnInfoActivity(position);
			}

		};
		listMain.setOnItemClickListener(clickListener);
	}

	public void turnInfoActivity(int position) {
		Intent intent = new Intent(context, JcScoreInfoActivity.class);
		intent.putExtra("event", listInfo.get(position).getEvent());
		startActivity(intent);
	}

	/**
	 * 得到当前页面的下标
	 */
	public int getIntentInfo() {
		int index = 0;
		Intent intent = getIntent();
		index = intent.getIntExtra("index", 0);
		return index;
	}

	/**
	 * 类型(0：全部 1：未开 2：比赛中 3：完场)
	 */
	public String getType() {
		int index = getIntentInfo();
		String type = "0";
		switch (index) {
		case 0:
			type = "0";
			break;
		case 1:
			type = "1";
			break;
		case 2:
			type = "2";
			break;
		case 3:
			type = "3";
			break;
		default:
			break;
		}
		return type;
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		public LayoutInflater mInflater; // 扩充主列表布局
		public List<ScoreInfo> mList;

		public JcInfoAdapter(Context context, List<ScoreInfo> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;

		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ScoreInfo info = mList.get(position);
			ViewHolder holder1 = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.jc_score_list_iteam,
						null);
				holder1 = new ViewHolder();
				holder1.index = (TextView) convertView
						.findViewById(R.id.jc_score_text_index);
				holder1.teamName = (TextView) convertView
						.findViewById(R.id.jc_score_text_team_name);
				holder1.hTeam = (TextView) convertView
						.findViewById(R.id.jc_score_text_hteam);
				holder1.state = (TextView) convertView
						.findViewById(R.id.jc_score_text_state);
				holder1.time = (TextView) convertView
						.findViewById(R.id.jc_score_text_time);
				holder1.vTeam = (TextView) convertView
						.findViewById(R.id.jc_score_text_vteam);
				holder1.startImg = (ImageView) convertView
						.findViewById(R.id.jc_score_btn_start);
				holder1.halfScore = (TextView) convertView
						.findViewById(R.id.jc_score_text_half_score);
				holder1.layoutLeft = (LinearLayout) convertView
						.findViewById(R.id.jc_score_list_item_layout_left);
				holder1.homeScore = (TextView) convertView
						.findViewById(R.id.jc_score_text_home_score);
				holder1.guestScore = (TextView) convertView
						.findViewById(R.id.jc_score_text_guest_score);
				holder1.attentionLayout = (LinearLayout)convertView
						.findViewById(R.id.jc_score_list_item_layout_left);
				convertView.setTag(holder1);
			} else {
				holder1 = (ViewHolder) convertView.getTag();
			}
			final ViewHolder holder = holder1;
			holder.teamName.setText(info.getTeamName());
			holder.hTeam.setText(info.gethTeam());
			holder.index.setText(String.format("%03d", (position + 1)));
			/**add by yejc 20130807 start*/
			if (tabIndex == 2) {
				String state = info.getMatchState();
				if ("1".equals(state) || 
						"3".equals(state) || "4".equals(state)) {
					if ("jclq".equals(playType)) {
						holder.state.setText(info.getMatchStateMemo()+" "+
					    info.getRemainTime());
						holder.state.setTextColor(Color.RED);
					} else if ("jczq".equals(playType)){
						holder.state.setText(info.getProgressedTime()+"'");
						holder.state.setTextColor(Color.RED);
					} else {
						holder.state.setText(info.getState());
						holder.state.setTextColor(setStateColor(info.getState()));
					}
				} else {
					holder.state.setText(info.getMatchStateMemo());
					holder.state.setTextColor(Color.RED);
				}
				
				holder1.attentionLayout.setVisibility(View.GONE);
				/**add by yejc 20130807 end*/
			} else if(tabIndex == 3) {
				holder.state.setText(info.getState());
				holder.state.setTextColor(setStateColor(info.getState()));
				holder1.attentionLayout.setVisibility(View.GONE);
			} else {
				holder.state.setText(info.getState());
				holder.state.setTextColor(setStateColor(info.getState()));
				holder1.attentionLayout.setVisibility(View.VISIBLE);
				
				if (isTrack(allcountries.get(index), info.getEvent())) {
					info.setStart(true);
					holder.startImg
							.setBackgroundResource(R.drawable.jc_list_start_b);
				} else {
					info.setStart(false);
					holder.startImg.setBackgroundResource(R.drawable.jc_list_start);
				}
				holder.layoutLeft.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (info.isStart()) {
							info.setStart(false);
							holder.startImg
									.setBackgroundResource(R.drawable.jc_list_start);
							deletShellRw(allcountries.get(index), info.getEvent());
							Toast.makeText(context, "已取消关注", Toast.LENGTH_SHORT)
									.show();
							if (isTrack) {
								initList();
							}
						} else {
							info.setStart(true);
							holder.startImg
									.setBackgroundResource(R.drawable.jc_list_start_b);
							addShellRw(allcountries.get(index), info.getEvent());
							Toast.makeText(context, "已添加至我的关注", Toast.LENGTH_SHORT)
									.show();
						}

					}
				});
			}
			
			holder.time.setText("开赛：" + info.getTime());
			holder.vTeam.setText(info.getvTeam());
			
			/**add by yejc 20130802 start*/
			if ("".equals(info.getHomeScore())) {
				holder1.homeScore.setText("0");
			} else {
				holder1.homeScore.setText(info.getHomeScore());
			}
			
			if ("".equals(info.getGuestScore())) {
				holder1.guestScore.setText("0");
			} else {
				holder1.guestScore.setText(info.getGuestScore());
			}
			
			if (!isLq) {
				String halfScoreStr = "";
				halfScoreStr+= "(";
				if ("".equals(info.getHomeHalfScore())) {
					halfScoreStr+= "0";
				} else {
					halfScoreStr+=info.getHomeHalfScore();
				}
				halfScoreStr+= ":";
				if ("".equals(info.getGuestHalfScore())) {
					halfScoreStr+= "0";
				} else {
					halfScoreStr+=info.getGuestHalfScore();
				}
				halfScoreStr+= ")";
				holder.halfScore.setText(halfScoreStr);
			}
			/**add by yejc 20130802 end*/
			
			return convertView;
		}

		public int setStateColor(String state) {
			int colorInt = 0;
			if (state.equals("未开赛")) {
				colorInt = Color.GRAY;
			} else if (state.equals("已完场")) {
				colorInt = Color.RED;
			} else if (state.equals("进行中")) {
				colorInt = getResources().getColor(R.color.gree_black);
			}
			return colorInt;
		}

		public class ViewHolder {
			TextView index;
			TextView teamName;// 联赛名称
			TextView hTeam;// 主队
			TextView vTeam;// 客队
			TextView time;// 开赛时间
			TextView state;// 开赛进度
			TextView halfScore;
			ImageView startImg;
			LinearLayout layoutLeft;
			TextView homeScore;
			TextView guestScore;
			LinearLayout attentionLayout;
		}
	}

	public void addShellRw(String date, String event) {
		Map<String, ?> mapTrack = shellRw.getAllKey();
		if (mapTrack.containsKey(date)) {
			String events = shellRw.getStringValue(date);
			event = events + ";" + event;
		}
		shellRw.putStringValue(date, event);
	}

	public void deletShellRw(String date, String event) {
		Map<String, ?> mapTrack = shellRw.getAllKey();
		if (mapTrack.containsKey(date)) {
			String events = shellRw.getStringValue(date);
			String eventStrs[] = events.split(";");
			String eventStr = "";
			for (int i = 0; i < eventStrs.length; i++) {
				if (!eventStrs[i].equals(event)) {
					eventStr += eventStrs[i] + ";";
				}
			}
			if (eventStr.equals("")) {
				shellRw.removeKey(date);
			} else {
				eventStr = eventStr.substring(0, eventStr.length() - 1);
				shellRw.putStringValue(date, eventStr);
			}
		}
	}

	protected List<ScoreInfo> getScoreInfo(Map<String, ?> map) {
		List<ScoreInfo> listInfo = new ArrayList<ScoreInfo>();
		Set<String> keys = map.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			try {
				String key = (String) it.next();
				JSONObject json = new JSONObject(shellRw.getStringValue(key));
				ScoreInfo info = new ScoreInfo();
				info.setTeamName(json.getString("sclassName"));
				info.sethTeam(json.getString("homeTeam"));
				info.setState(json.getString("stateMemo"));
				info.setTime(json.getString("matchTime"));
				info.setvTeam(json.getString("guestTeam"));
				info.setEvent(json.getString("event"));
//				if(json.has("progressedTime")) {
//					info.setProgressedTime(json.getString("progressedTime"));
//				}
				listInfo.add(info);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listInfo;
	}

	public class ScoreInfo {
		private String teamName;// 联赛名称
		private String hTeam;// 主队
		private String vTeam;// 客队
		private String time;// 开赛时间
		private String state;// 开赛进度
		private String event;
		private boolean isStart = false;
		private JSONObject json;
		private String homeScore = "";
		private String guestScore = "";
		private String homeHalfScore = "";
		private String guestHalfScore = "";
		private String result = "";
		private String red = "";
		private String yellow = "";
		private String matchStateMemo = "";
		private String remainTime = "";
		private String progressedTime = "";
		private String matchState = "";

		public String getMatchState() {
			return matchState;
		}

		public void setMatchState(String matchState) {
			this.matchState = matchState;
		}

		public String getMatchStateMemo() {
			return matchStateMemo;
		}

		public void setMatchStateMemo(String matchStateMemo) {
			this.matchStateMemo = matchStateMemo;
		}

		public String getProgressedTime() {
			return progressedTime;
		}

		public void setProgressedTime(String progressedTime) {
			this.progressedTime = progressedTime;
		}

		public String getRemainTime() {
			return remainTime;
		}

		public void setRemainTime(String remainTime) {
			this.remainTime = remainTime;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getHomeScore() {
			return homeScore;
		}

		public void setHomeScore(String homeScore) {
			this.homeScore = homeScore;
		}

		public String getGuestScore() {
			return guestScore;
		}

		public void setGuestScore(String guestScore) {
			this.guestScore = guestScore;
		}

		public String getHomeHalfScore() {
			return homeHalfScore;
		}

		public void setHomeHalfScore(String homeHalfScore) {
			this.homeHalfScore = homeHalfScore;
		}

		public String getGuestHalfScore() {
			return guestHalfScore;
		}

		public void setGuestHalfScore(String guestHalfScore) {
			this.guestHalfScore = guestHalfScore;
		}

		public String getRed() {
			return red;
		}

		public void setRed(String red) {
			this.red = red;
		}

		public String getYellow() {
			return yellow;
		}

		public void setYellow(String yellow) {
			this.yellow = yellow;
		}

		public JSONObject getJson() {
			return json;
		}

		public void setJson(JSONObject json) {
			this.json = json;
		}

		public boolean isStart() {
			return isStart;
		}

		public void setStart(boolean isStart) {
			this.isStart = isStart;
		}

		public ScoreInfo() {

		}

		public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public String gethTeam() {
			return hTeam;
		}

		public void sethTeam(String hTeam) {
			this.hTeam = hTeam;
		}

		public String getvTeam() {
			return vTeam;
		}

		public void setvTeam(String vTeam) {
			this.vTeam = vTeam;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}
	
	/**add by yejc 20130716 start*/
	private class RefreshBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			initListInfo();
		}
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(refreshReceiver);
	}
	
	/**
	 * 获取列表联网
	 */
	public void getCurrentScoreNet(final String command) {
		mProgress = UserCenterDialog.onCreateDialog(context);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String re = ScoreListInterface.getCurrentScore(command,"processingMatches");
					mProgress.dismiss();
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					if (error_code.equals("0000")) {
						final JSONArray jsonArray = obj.getJSONArray("result");

						handler.post(new Runnable() {
							@Override
							public void run() {
								listInfo = getScoreInfo(jsonArray);
								initList();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
	/**add by yejc 20130716 end*/
}
