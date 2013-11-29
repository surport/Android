package com.ruyicai.activity.buy.zc;

import java.util.List;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.constant.Constants;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class FootBallBaseAdapter extends BaseAdapter {
	protected List<TeamInfo> mTeamList;
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected Resources  mResources;
	protected int white = 0;
	protected int black = 0;
	protected int oddsColor = 0;
	public static final String LOTNO_ZC = "LOTNO_ZC";
	public String mIssueState = "";
	
	public FootBallBaseAdapter(Context context) {
		super();
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mContext = context;
		initColor();
	}
	
	public void setList(List<TeamInfo> list) {
		this.mTeamList = list;
	}
	
	protected abstract int getZhuShu();
	protected abstract void clearSelected();
	protected abstract String getZhuMa();
	protected abstract boolean isTouZhu();
	protected abstract int getTeamNum(List<TeamInfo> list);
	
	
	protected void initColor() {
		mResources = mContext.getResources();
		white = mResources.getColor(R.color.white);
		black = mResources.getColor(R.color.black);
		oddsColor = mResources.getColor(R.color.jc_odds_text_color);
	}
	
	protected void setViewStyle(LinearLayout teamLayout, TextView team, TextView odds, boolean isChecked) {
		if (isChecked) {
			teamLayout.setBackgroundResource(R.drawable.team_name_bj_yellow);
			team.setBackgroundResource(R.drawable.team_name_bj_top_yellow);
			team.setTextColor(white);
			odds.setTextColor(white);
		} else {
			teamLayout.setBackgroundResource(android.R.color.transparent);
			team.setBackgroundResource(android.R.color.transparent);
			team.setTextColor(black);
			odds.setTextColor(oddsColor);
		}
	}
	
	protected String getCurrentIssue() {
		if (mContext instanceof FootBallMainActivity) {
			FootBallMainActivity activity = (FootBallMainActivity)mContext;
			return activity.currentIssue;
		}
		return "";
	}
	
	protected void setTeamNum(int count) {
		if (mContext instanceof FootBallMainActivity) {
			FootBallMainActivity activity = (FootBallMainActivity)mContext;
			activity.changeTextSumMoney(getZhuShu());
			activity.setTeamNum(count);
		}
	}
	
	protected void turnAnalysis(String lotno, String teamId) {
		Constants.currentTickType = "zuCai";
		Intent intent = new Intent(mContext, JcExplainActivity.class);
		String event = lotno + "_" + getCurrentIssue()
				+ "_" + teamId;
		intent.putExtra("event", event);
		Log.i("yejc", "======event="+event);
		intent.putExtra(LOTNO_ZC, LOTNO_ZC);
		mContext.startActivity(intent);
	}
	
	protected class ViewHolder {
		View divider;
		TextView gameName;
		TextView gameNum;
		TextView gameTime;
		TextView gameDate;
		TextView homeTeam;
		TextView homeOdds;
		TextView textVS;
		TextView textOdds;
		TextView guestTeam;
		TextView guestOdds;
		TextView analysis;
		Button btnDan;
		Button btnBet;
		LinearLayout homeLayout;
		LinearLayout vsLayout;
		LinearLayout guestLayout;
		LinearLayout layout;
		int index = -1;
	}
}
