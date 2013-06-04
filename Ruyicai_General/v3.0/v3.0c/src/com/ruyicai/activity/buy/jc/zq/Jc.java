package com.ruyicai.activity.buy.jc.zq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ruyicai.activity.buy.BuyActivityGroup;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryJcInfoInterface;
import com.ruyicai.util.Constants;

public class Jc extends BuyActivityGroup implements HandlerMsg {

	ProgressDialog progressdialog;
	MyHandler handler = new MyHandler(this);// 自定义handler
	public static List<Info> infos = new ArrayList<Info>();
	JSONObject jsonObj;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isIssue(false);
		initView();
		jcInfoNet();

	}
	/**
	 * 投注联网
	 */
	public void jcInfoNet() {	
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = QueryJcInfoInterface.getInstance().queryJcInfo(QueryJcInfoInterface.JCZQ);
				try {
					jsonObj = new JSONObject(str);
					String msg = jsonObj.getString("message");
					String error = jsonObj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	public void initView() {
		removeTabs();
		String[] titles = {"过关投注"};
		String[] topTitles = {"竞彩足球-过关投注"};
		Class[] allId = {JcGuoGuan.class};
		init(titles, topTitles, allId);
	}

	public void setValue() {
		try {
			JSONArray jsonArray = jsonObj.getJSONArray("result");
			for (int i = 0; i < jsonArray.length(); i++) {
				Info itemInfo = new Info();
				JSONObject jsonItem = jsonArray.getJSONObject(i);
				itemInfo.setTime(jsonItem.getString("dayForamt")+"  "+jsonItem.getString("week"));
				itemInfo.setDay(jsonItem.getString("day"));
				itemInfo.setWeek(jsonItem.getString("weekId"));
				itemInfo.setTeam(jsonItem.getString("league"));
				itemInfo.setTimeEnd(jsonItem.getString("endTime"));
				itemInfo.setTeamId(jsonItem.getString("teamId"));
				itemInfo.setLetPoint(jsonItem.getString("letPoint"));
				itemInfo.setWin(jsonItem.getString("v3"));
				itemInfo.setLevel(jsonItem.getString("v1"));
				itemInfo.setFail(jsonItem.getString("v0"));
				String teams[] = jsonItem.getString("team").split(":");
				itemInfo.setHome(teams[0]);
				itemInfo.setAway(teams[1]);
				infos.add(itemInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		initView();
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
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

	/**
	 * 竞彩内部类
	 * 
	 * @author Administrator
	 */
	class Info {
		String time = "";
		String day = "";
		String team = "";
		String home = "";
		String away = "";
		String score = "";
		String timeEnd = "";
		String win = "";
		String level = "";
		String fail = "";
		String week = "";
		String teamId = "";
		String letPoint = "";
		public int onclikNum = 0;
		boolean isWin = false;
		boolean isLevel = false;
		boolean isFail = false;

		public Info() {

		}
		public String getTeamId() {
			return teamId;
		}
		public void setTeamId(String teamId) {
			this.teamId = teamId;
		}
		public String getLetPoint() {
			return letPoint;
		}
		public void setLetPoint(String letPoint) {
			this.letPoint = letPoint;
		}
		public String getWeek() {
			return week;
		}
		public void setWeek(String week) {
			this.week = week;
		}
		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public int getOnclikNum() {
			return onclikNum;
		}

		public void setOnclikNum(int onclikNum) {
			this.onclikNum = onclikNum;
		}
		public boolean isWin() {
			return isWin;
		}

		public void setWin(boolean isWin) {
			this.isWin = isWin;
		}

		public boolean isLevel() {
			return isLevel;
		}

		public void setLevel(boolean isLevel) {
			this.isLevel = isLevel;
		}

		public boolean isFail() {
			return isFail;
		}

		public void setFail(boolean isFail) {
			this.isFail = isFail;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTeam() {
			return team;
		}

		public void setTeam(String team) {
			this.team = team;
		}

		public String getHome() {
			return home;
		}

		public void setHome(String home) {
			this.home = home;
		}

		public String getAway() {
			return away;
		}

		public void setAway(String away) {
			this.away = away;
		}

		public String getScore() {
			return score;
		}

		public void setScore(String score) {
			this.score = score;
		}

		public String getTimeEnd() {
			return timeEnd;
		}

		public void setTimeEnd(String timeEnd) {
			this.timeEnd = timeEnd;
		}

		public String getWin() {
			return win;
		}

		public void setWin(String win) {
			this.win = win;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public String getFail() {
			return fail;
		}

		public void setFail(String fail) {
			this.fail = fail;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		infos.clear();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		setValue();
	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	
}
