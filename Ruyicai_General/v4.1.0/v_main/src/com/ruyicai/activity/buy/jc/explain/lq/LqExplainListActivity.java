package com.ruyicai.activity.buy.jc.explain.lq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.ExplainListActivity;
import com.ruyicai.activity.buy.jc.explain.zq.ExplainListActivity.ExplainInfo;
import com.ruyicai.activity.buy.jc.explain.zq.ExplainListActivity.ViewHolder;

public class LqExplainListActivity extends ExplainListActivity {

	public void setLQ() {
		this.isLq = true;
	}

	/**
	 * 主队近期战绩
	 * 
	 * @param jsonArray
	 * @return
	 */
	protected List getHTerm10Info(JSONArray jsonArray, String title) {
		List listInfo = new ArrayList<ExplainInfo>();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setMatchTime(json.getString("matchTime"));
				info.setHomeTeam(json.getString("homeTeam"));
				info.setGuestTeam(json.getString("guestTeam"));
				info.setScore(json.getString("score"));
				info.setHomeTeamId(json.getString("homeTeamId"));
				info.setGuestTeamId(json.getString("guestTeamId"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setRanking(json.getString("ranking"));
				info.setTeamName(json.getString("teamName"));
				info.setWin(json.getString("winCount"));
				info.setLose(json.getString("loseCount"));
				info.setGoalDifference(json.getString("scoreDifference"));
				info.setMatchCount(json.getString("matchCount"));
				info.setGainScore(json.getString("gainScore"));
				info.setLoseScore(json.getString("loseScore"));
				info.setTeamId(json.getString("teamId"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setMatchTime(json.getString("matchTime"));
				info.setHomeTeam(json.getString("homeTeam"));
				info.setGuestTeam(json.getString("guestTeam"));
				info.setScore(json.getString("score"));
				info.setSclassName(json.getString("sclassName"));
				info.setHomeTeamId(json.getString("homeTeamId"));
				info.setGuestTeamId(json.getString("guestTeamId"));
				switch (JcLqExplainActivity.Lq_TYPE) {
				case JcLqExplainActivity.LQ_SF:
					info.setResult(json.getString("resultSf"));
					break;
				case JcLqExplainActivity.LQ_RFSF:
					info.setResult(json.getString("resultRfsf"));
					break;
				case JcLqExplainActivity.LQ_DX:
					info.setResult(json.getString("resultDx"));
				case JcLqExplainActivity.LQ_SFC:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			listInfo.add(info);
		}
		return listInfo;
	}
}
