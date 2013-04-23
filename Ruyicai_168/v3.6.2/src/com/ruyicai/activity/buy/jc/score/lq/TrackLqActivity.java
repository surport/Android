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

import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.ScoreInfo;
import com.ruyicai.activity.buy.jc.score.zq.TrackActivity;
import com.ruyicai.constant.ShellRWConstants;

public class TrackLqActivity extends TrackActivity{
	
	public void setShareName(){
		SHARE_NAME = ShellRWConstants.JCLQ_PREFER;
	}
	public void turnInfoActivity(int position){
		Intent intent = new Intent(context, JcLqScoreInfoActivity.class);
		intent.putExtra("event", listInfo.get(position).getEvent());
		startActivity(intent);
	}
	protected List<ScoreInfo> getScoreInfo(Map<String, ?> map){
		List<ScoreInfo> listInfo = new ArrayList<ScoreInfo>();
        Set<String> keys = map.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();) {
            try {
            	String key = (String) it.next();
			    JSONObject json = new JSONObject(shellRw.getStringValue(key));
			    ScoreInfo info = new ScoreInfo();
				info.setTeamName(json.getString("sclassName"));
				info.sethTeam(json.getString("guestTeam"));
				info.setState(json.getString("stateMemo"));
				info.setTime(json.getString("matchTime"));
				info.setvTeam(json.getString("homeTeam"));
				info.setEvent(json.getString("event"));
				listInfo.add(info);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
		return listInfo;
	}
}
