package com.ruyicai.activity.buy.jc.explain.zq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 分析的界面
 * 
 * 
 */
public class ExplainListActivity extends BaseListActivity {
	private String[] titleStrs = { "联赛排名", "主队近期战绩", "客队近期战绩",
			"主队未来5场赛事", "客队未来5场赛事", "历史交锋" };
	private String[] keys = { "rankings", "homePreSchedules",
			"guestPreSchedules", "homeAfterSchedules", "guestAfterSchedules",
			"preClashSchedules" };
	protected boolean isLq = false;
	
	private String homeTeamId = "";
	private String guestTeamId = "";
	
	private String homeTeamName = "";
	private String guestTeamName = "";
	
	private String homeRanking = "";
	private String guestRanking = "";
	
	private String guestPreWinCount = "";
	private String guestPreStandoffCount = "";
	private String guestPreLoseCount = "";
	
	private String homePreWinCount= "";
	private String homePreStandoffCount = "";
	private String homePreLoseCount = "";
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setLQ();
		initList();
	}

	public void setLQ() {
		this.isLq = false;
	}

	public JSONArray getJsonArray(String value) {
		JSONArray json = null;
		try {
			json = JcExplainActivity.jsonObject.getJSONObject("result")
					.getJSONArray(value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public void initList() {
		JSONObject json = JcExplainActivity.jsonObject;
		if (json != null) {
			listMain = (ListView) findViewById(R.id.buy_jc_main_listview);
			List list = initExplainInfo();
			adapter = new JcInfoAdapter(context, list);
			listMain.setAdapter(adapter);
			initFenXiListViewHeader(getRankInfos(list));
		}
	}

	private List getRankInfos(List list) {
		return (List) list.get(0);
	}

	/**
	 * 设置表头显示
	 * 
	 * @param list
	 *            排名信息列表
	 */
	private void initFenXiListViewHeader(List list) {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fenxi_listview_header);
		linearLayout.setVisibility(View.VISIBLE);

		TextView homeTeamTextView = (TextView) findViewById(R.id.fenxi_listview_header_hometeam);
//		homeTeamTextView.setText(homeTeamName + "(主队)");

		TextView guestTeamTextView = (TextView) findViewById(R.id.fenxi_listview_header_guestteam);
//		guestTeamTextView.setText(guestTeamName + "(客队)");
		/**modify by yejc 20130418 start*/
		if (Constants.isDebug) {
			PublicMethod.outLog(this.getClass().getSimpleName(), "initFenXiListViewHeader");
		}
		if (isLq) {
			homeTeamTextView.setText(guestTeamName + "(客队)");
			guestTeamTextView.setText(homeTeamName + "(主队)");
		} else {
			homeTeamTextView.setText(homeTeamName + "(主队)");
			guestTeamTextView.setText(guestTeamName + "(客队)");
		}
		/**modify by yejc 20130418 start*/
	}

	protected List initExplainInfo() {
		try {
			JSONObject jsonObjectResult = JcExplainActivity.jsonObject.getJSONObject(
					"result");
			homeRanking = jsonObjectResult.getString("homeRanking");
			guestRanking = jsonObjectResult.getString("guestRanking");
			
			if (!isLq) {
				guestPreStandoffCount = jsonObjectResult.getString("guestPreStandoffCount");
				homePreStandoffCount = jsonObjectResult.getString("homePreStandoffCount");
			}
			guestPreWinCount = jsonObjectResult.getString("guestPreWinCount");
			guestPreLoseCount = jsonObjectResult.getString("guestPreLoseCount");
			homePreWinCount = jsonObjectResult.getString("homePreWinCount");
			homePreLoseCount = jsonObjectResult.getString("homePreLoseCount");
			
			JSONObject jsonObjectSchedule = jsonObjectResult.getJSONObject("schedule");
			homeTeamId = jsonObjectSchedule.getString("homeTeamId");
			guestTeamId = jsonObjectSchedule.getString("guestTeamId");
			homeTeamName = jsonObjectSchedule.getString("homeTeam");
			guestTeamName = jsonObjectSchedule.getString("guestTeam");
		
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List listInfos = new ArrayList<List>();
		for (int i = 0; i < 6; i++) {
			List listInfo = getHTerm10Info(getJsonArray(keys[i]), titleStrs[i]);
			listInfos.add(listInfo);
		}

		return listInfos;
	}

	/**
	 * 主队近期战绩
	 * 
	 * @param jsonArray
	 * @return
	 */
	protected List getHTerm10Info(JSONArray jsonArray, String title) {
		List listInfo = new ArrayList<ExplainInfo>();
		if (jsonArray == null) return listInfo;
		for (int i = 0; i < jsonArray.length(); i++) {
			ExplainInfo info = new ExplainInfo();
			info.setTitleName(title);
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setMatchTime(json.getString("matchTime"));
				info.setHomeTeam(json.getString("homeTeam"));
				info.setGuestTeam(json.getString("guestTeam"));
				info.setHomeTeamId(json.getString("homeTeamId"));
				info.setGuestTeamId(json.getString("guestTeamId"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setMatchTime(json.getString("matchTime"));
				info.setHomeTeam(json.getString("homeTeam"));
				info.setGuestTeam(json.getString("guestTeam"));
				info.setHomeTeamId(json.getString("homeTeamId"));
				info.setGuestTeamId(json.getString("guestTeamId"));
				info.setScore(json.getString("score"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				// 获取联赛排名数据
				JSONObject json = jsonArray.getJSONObject(i);
				info.setRanking(json.getString("ranking"));
				info.setTeamName(json.getString("teamName"));
				info.setTeamId(json.getString("teamId"));
				info.setWin(json.getString("win"));
				info.setLose(json.getString("lose"));
				info.setGoalDifference(json.getString("goalDifference"));
				info.setIntegral(json.getString("integral"));
				info.setMatchCount(json.getString("matchCount"));
				if (!isLq) {
					info.setStandoff(json.getString("standoff"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setMatchTime(json.getString("matchTime"));
				info.setHomeTeam(json.getString("homeTeam"));
				info.setGuestTeam(json.getString("guestTeam"));
				info.setHomeTeamId(json.getString("homeTeamId"));
				info.setGuestTeamId(json.getString("guestTeamId"));
				info.setScore(json.getString("score"));
				info.setSclassName(json.getString("sclassName"));
				info.setResult(json.getString("result"));
				if (!isLq) {
					info.setGoals(json.getString("goals"));
					info.setHalfResult(json.getString("halfResult"));
					info.setHalfScore(json.getString("halfScore"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listInfo.add(info);
		}
		return listInfo;
	}

	/**
	 * 竞彩的适配器
	 */
	public class JcInfoAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		private List<List> mList;

		public JcInfoAdapter(Context context, List<List> list) {
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

		int index;

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			index = position;
			final ArrayList<ExplainInfo> list = (ArrayList<ExplainInfo>) mList
					.get(position);
			convertView = mInflater.inflate(
					R.layout.buy_jc_main_view_list_item, null);
			final ViewHolder holder = new ViewHolder();
			holder.btn = (Button) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_btn);
			holder.layout = (LinearLayout) convertView
					.findViewById(R.id.buy_jc_main_view_list_item_linearLayout);
			holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			if (list.size() == 0) {
				holder.btn.setText(titleStrs[position]);
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Toast.makeText(context, titleStrs[position] + "没有数据",
								Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				isOpen(list, holder);
				StringBuilder buttonTextString = new StringBuilder(titleStrs[position]);
				if (position == 0) {
					buttonTextString.append("(主队" + homeRanking + " 客队" + guestRanking +")");
				}else if (position == 1) {
					/**Modify by yejc 20130415 start*/
					if ("".equals(homePreStandoffCount)) {
						buttonTextString.append("(近10场 胜"+homePreWinCount + "负" + homePreLoseCount +")");
					} else {
						buttonTextString.append("(近10场 胜"+homePreWinCount +"平" +homePreStandoffCount + "负" + homePreLoseCount +")");
					}
//					buttonTextString.append("(近10场 胜"+homePreWinCount +"平" +homePreStandoffCount + "负" + homePreLoseCount +")");
					/**Modify by yejc 20130415 end*/
				}else if (position == 2) {
					/**Modify by yejc 20130415 start*/
					if ("".equals(guestPreStandoffCount)) {
						buttonTextString.append("(近10场 胜"+guestPreWinCount + "负" + guestPreLoseCount +")");
					} else {
						buttonTextString.append("(近10场 胜"+guestPreWinCount +"平" +guestPreStandoffCount + "负" + guestPreLoseCount +")");
					}
//					buttonTextString.append("(近10场 胜"+guestPreWinCount +"平" +guestPreStandoffCount + "负" + guestPreLoseCount +")");
					/**Modify by yejc 20130415 end*/
				}
				holder.btn.setText(buttonTextString);
				holder.btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						list.get(0).isOpen = !list.get(0).isOpen;
						isOpen(list, holder);
					}
				});
				holder.layout.addView(addLayout(null, true, position));
				for (ExplainInfo info : list) {
					holder.layout.addView(addLayout(info, false, position));
				}
			}
			return convertView;
		}

		private void isOpen(final ArrayList<ExplainInfo> list,
				final ViewHolder holder) {
			if (list.get(0).isOpen) {
				holder.layout.setVisibility(LinearLayout.VISIBLE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_open);
			} else {
				holder.layout.setVisibility(LinearLayout.GONE);
				holder.btn.setBackgroundResource(R.drawable.buy_jc_btn_close);
			}
		}

		private View addLayout(final ExplainInfo info, boolean isTop, int index) {
			View view = null;
			if (index == 0) {
				view = createRank(info, isTop);
			} else if (index == 1 || index == 2) {
				view = createTerm10(info, isTop);
			} else if (index == 3 || index == 4) {
				view = createTerm5(info, isTop);
			} else if (index == 5) {
				view = createHistory(info, isTop);
			}
			return view;
		}

	}

	private View createRank(ExplainInfo info, boolean isTop) {
		View convertView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.explain_list_team_rank,
					null);
			holder = new ViewHolder();
			holder.oneText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_one);
			holder.twoText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_two);
			holder.teamText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_team);
			holder.threeText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_three);
			holder.fourText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_four);
			holder.fiveText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_five);
			holder.sixText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_six);
			holder.sevenText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_seven);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (isTop) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.jc_explain_list_bg));
			if (isLq) {
				holder.fourText.setText("负");
				holder.fiveText.setText("得");
				holder.sixText.setText("失");
				holder.sevenText.setText("净");
			}
		} else {
			SpannableStringBuilder team = new SpannableStringBuilder(
					info.getTeamName());
			if (info.getTeamId().equals(homeTeamId)) {
				team.setSpan(new ForegroundColorSpan(Color.RED), 0,
						team.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				homeTeamName = info.getTeamName();
			} else if (info.getTeamId().equals(guestTeamId)) {
				team.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						team.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				guestTeamName = info.getTeamName();
			}

			if (isLq) {
				holder.fourText.setText(info.getLose());
				holder.fiveText.setText(info.getGainScore());
				holder.sixText.setText(info.getLoseScore());
				holder.sevenText.setText(info.getGoalDifference());
			} else {
				holder.fourText.setText(info.getStandoff());
				holder.fiveText.setText(info.getLose());
				holder.sixText.setText(info.getGoalDifference());
				holder.sevenText.setText(info.getIntegral());
			}
			holder.oneText.setText(info.getRanking());
			holder.twoText.setText(team);
			holder.teamText.setText(info.getMatchCount());
			holder.threeText.setText(info.getWin());

		}
		return convertView;

	}

	private View createTerm10(ExplainInfo info, boolean isTop) {
		View convertView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.explain_term10_list_item,
					null);
			holder = new ViewHolder();
			holder.oneText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_one);
			holder.twoText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_two);
			holder.threeText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_three);
			holder.fourText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_four);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isTop) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.jc_explain_list_bg));
		} else {
			SpannableStringBuilder home = new SpannableStringBuilder(
					info.getHomeTeam());
			if (info.getHomeTeamId().equals(homeTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.RED), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getHomeTeamId().equals(guestTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			SpannableStringBuilder away = new SpannableStringBuilder(
					info.getGuestTeam());
			if (info.getGuestTeamId().equals(homeTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.RED), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getGuestTeamId().equals(guestTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			holder.oneText.setText(info.getMatchTime());
			holder.twoText.setText(home);
			holder.threeText.setText(info.getScore());
			holder.fourText.setText(away);
		}
		return convertView;

	}

	private View createTerm5(ExplainInfo info, boolean isTop) {
		View convertView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.explain_term5_list_item,
					null);
			holder = new ViewHolder();
			holder.oneText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_one);
			holder.twoText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_two);
			holder.threeText = (TextView) convertView
					.findViewById(R.id.explain_list_item_text_three);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (isTop) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.jc_explain_list_bg));
		} else {
			SpannableStringBuilder home = new SpannableStringBuilder(
					info.getHomeTeam());
			if (info.getHomeTeamId().equals(homeTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.RED), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getHomeTeamId().equals(guestTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			SpannableStringBuilder away = new SpannableStringBuilder(
					info.getGuestTeam());
			if (info.getGuestTeamId().equals(homeTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.RED), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getGuestTeamId().equals(guestTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			holder.oneText.setText(info.getMatchTime());
			holder.twoText.setText(home);
			holder.threeText.setText(away);
		}
		return convertView;

	}

	private View createHistory(ExplainInfo info, boolean isTop) {
		View convertView = null;
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = LayoutInflater.from(context);
			convertView = mInflater.inflate(R.layout.explain_history_list_item,
					null);
			holder = new ViewHolder();
			holder.oneText_1 = (TextView) convertView
					.findViewById(R.id.explain_history_one_1);
			holder.oneText_2 = (TextView) convertView
					.findViewById(R.id.explain_history_one_2);
			holder.twoText_1 = (TextView) convertView
					.findViewById(R.id.explain_history_two_1);
			holder.twoText_2 = (TextView) convertView
					.findViewById(R.id.explain_history_two_2);
			holder.twoText_3 = (TextView) convertView
					.findViewById(R.id.explain_history_two_3);
			holder.threeText_1 = (TextView) convertView
					.findViewById(R.id.explain_history_three_1);
			holder.threeText_2 = (TextView) convertView
					.findViewById(R.id.explain_history_three_2);
			holder.threeText_3 = (TextView) convertView
					.findViewById(R.id.explain_history_three_3);
			holder.fourText_1 = (TextView) convertView
					.findViewById(R.id.explain_history_four_1);
			holder.fourText_2 = (TextView) convertView
					.findViewById(R.id.explain_history_four_2);
			holder.fourText_3 = (TextView) convertView
					.findViewById(R.id.explain_history_four_3);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (isTop) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.jc_explain_list_bg));
			if (isLq) {
				holder.twoText_3.setVisibility(View.GONE);
				holder.threeText_3.setVisibility(View.GONE);
				holder.fourText_2.setVisibility(View.GONE);
				holder.threeText_2.setText("比分");
				holder.fourText_3.setText("赛果");
			}
		} else {
			SpannableStringBuilder home = new SpannableStringBuilder(
					info.getHomeTeam());
			if (info.getHomeTeamId().equals(homeTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.RED), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getHomeTeamId().equals(guestTeamId)) {
				home.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						home.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			SpannableStringBuilder away = new SpannableStringBuilder(
					info.getGuestTeam());
			if (info.getGuestTeamId().equals(homeTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.RED), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (info.getGuestTeamId().equals(guestTeamId)) {
				away.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
						away.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}

			holder.oneText_1.setText(info.getSclassName());
			holder.oneText_2.setText(info.getMatchTime());
			holder.twoText_1.setText(home);
			holder.twoText_2.setText(away);
			if (isLq) {
				holder.twoText_3.setVisibility(View.GONE);
				holder.threeText_3.setVisibility(View.GONE);
				holder.fourText_2.setVisibility(View.GONE);
				holder.threeText_2.setText(info.getScore());
				holder.fourText_3.setText(info.getResult());
			} else {
				holder.twoText_3.setText(info.getScore());
				holder.threeText_1.setText("");
				holder.threeText_2.setText(info.getHalfScore());
				holder.threeText_3.setText(info.getScore());
				holder.fourText_1.setText("");
				holder.fourText_2.setText(info.getResult());
				holder.fourText_3.setText("");
			}
		}
		return convertView;

	}

	public class ViewHolder {
		Button btn;
		LinearLayout layout;

		TextView teamText;
		TextView oneText;
		TextView twoText;
		TextView threeText;
		TextView fourText;
		TextView fiveText;
		TextView sixText;
		TextView sevenText;

		public TextView oneText_1;
		public TextView oneText_2;
		public TextView twoText_1;
		public TextView twoText_2;
		public TextView twoText_3;
		public TextView threeText_1;
		public TextView threeText_2;
		public TextView threeText_3;
		public TextView fourText_1;
		public TextView fourText_2;
		public TextView fourText_3;

	}

	public class ExplainInfo {
		private String titleName;
		private String matchTime;// "2011-10-12",
		private String homeTeamId;
		private String homeTeam;// "大阪樱花"
		private String guestTeamId;
		private String teamId = "";
		private String guestTeam;// "北海道大学",
		private String score;// "6:0"
		private String ranking;// "1",
		private String teamName;// "清水鼓动",
		private String win;// "0",
		private String standoff;// "0",
		private String lose;// "0",
		private String goalDifference;// "0",
		private String integral;// "0"

		private String sclassName;// "日本职业联赛J1",
		private String halfResult;// "负",
		private String result;// "胜",
		private String goals;// "5"
		private String halfScore;
		private String matchCount;
		private String gainScore;
		private String loseScore;

		public String getTeamId() {
			return teamId;
		}

		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}

		public String getHomeTeamId() {
			return homeTeamId;
		}

		public void setHomeTeamId(String homeTeamId) {
			this.homeTeamId = homeTeamId;
		}

		public String getGuestTeamId() {
			return guestTeamId;
		}

		public void setGuestTeamId(String guestTeamId) {
			this.guestTeamId = guestTeamId;
		}

		public String getGainScore() {
			return gainScore;
		}

		public void setGainScore(String gainScore) {
			this.gainScore = gainScore;
		}

		public String getLoseScore() {
			return loseScore;
		}

		public void setLoseScore(String loseScore) {
			this.loseScore = loseScore;
		}

		public String getMatchCount() {
			return matchCount;
		}

		public void setMatchCount(String matchCount) {
			this.matchCount = matchCount;
		}

		public ExplainInfo() {

		}

		public String getHalfScore() {
			return halfScore;
		}

		public void setHalfScore(String halfScore) {
			this.halfScore = halfScore;
		}

		public String getSclassName() {
			return sclassName;
		}

		public void setSclassName(String sclassName) {
			this.sclassName = sclassName;
		}

		public String getHalfResult() {
			return halfResult;
		}

		public void setHalfResult(String halfResult) {
			this.halfResult = halfResult;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public String getGoals() {
			return goals;
		}

		public void setGoals(String goals) {
			this.goals = goals;
		}

		public String getRanking() {
			return ranking;
		}

		public void setRanking(String ranking) {
			this.ranking = ranking;
		}

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public String getWin() {
			return win;
		}

		public void setWin(String win) {
			this.win = win;
		}

		public String getStandoff() {
			return standoff;
		}

		public void setStandoff(String standoff) {
			this.standoff = standoff;
		}

		public String getLose() {
			return lose;
		}

		public void setLose(String lose) {
			this.lose = lose;
		}

		public String getGoalDifference() {
			return goalDifference;
		}

		public void setGoalDifference(String goalDifference) {
			this.goalDifference = goalDifference;
		}

		public String getIntegral() {
			return integral;
		}

		public void setIntegral(String integral) {
			this.integral = integral;
		}

		public String getMatchTime() {
			return matchTime;
		}

		public void setMatchTime(String matchTime) {
			this.matchTime = matchTime;
		}

		public String getHomeTeam() {
			return homeTeam;
		}

		public void setHomeTeam(String homeTeam) {
			this.homeTeam = homeTeam;
		}

		public String getGuestTeam() {
			return guestTeam;
		}

		public void setGuestTeam(String guestTeam) {
			this.guestTeam = guestTeam;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public boolean isOpen = false;

		public String getTitleName() {
			return titleName;
		}

		public void setTitleName(String titleName) {
			this.titleName = titleName;
		}

		public boolean isOpen() {
			return isOpen;
		}

		public void setOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

	}
}
