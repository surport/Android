package com.ruyicai.activity.buy.jc.score.lq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.ScoreInfo;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreInfoActivity;
import com.ruyicai.activity.buy.jc.score.zq.TrackActivity;
import com.ruyicai.constant.ShellRWConstants;

public class TrackLqActivity extends TrackActivity {

	public void setReguestType() {
		reguestType = "immediateScoreJcl";
	}

	public void setIsLq() {
		isLq = true;
	}

	public void turnInfoActivity(int position) {
		Intent intent = new Intent(context, JcLqScoreInfoActivity.class);
		intent.putExtra("event", listInfoCopy.get(position).getEvent());
		startActivity(intent);
	}

	protected List<ScoreInfo> getScoreInfo(JSONArray jsonArray) {
		List<ScoreInfo> listInfo = new ArrayList<ScoreInfo>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject json = jsonArray.getJSONObject(i);
				ScoreInfo info = new ScoreInfo();
				info.setJson(json);
				info.setTeamName(json.getString("sclassName"));
				info.sethTeam(json.getString("guestTeam"));
				info.setState(json.getString("stateMemo"));
				info.setTime(json.getString("matchTime"));
				info.setvTeam(json.getString("homeTeam"));
				info.setEvent(json.getString("event"));
				info.setHomeScore(json.getString("guestScore"));
				info.setGuestScore(json.getString("homeScore"));
				try {
					int homeInt = Integer.parseInt(info.getHomeScore());
					int guestInt = Integer.parseInt(info.getGuestScore());
					if (homeInt == guestInt) {
						info.setResult("平手");
					} else if (homeInt > guestInt) {
						info.setResult("主负");
					} else {
						info.setResult("主胜");
					}
				} catch (Exception e) {

				}
				listInfo.add(info);
			}

		} catch (Exception e) {

		}
		return listInfo;
	}
}
