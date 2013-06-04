package com.ruyicai.activity.buy.jc.explain.lq;

import org.json.JSONArray;
import org.json.JSONException;

import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;

public class LqAllScoreActivity extends LqScoreActivity{
	public JSONArray getJsonArray(){
		JSONArray json = null;
		try {
		  json = JcExplainActivity.jsonObject.getJSONObject("result").getJSONArray("totalScores");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return	json;
	}
}
