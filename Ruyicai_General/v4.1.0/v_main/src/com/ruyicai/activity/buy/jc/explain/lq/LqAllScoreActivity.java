package com.ruyicai.activity.buy.jc.explain.lq;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.explain.zq.JcExplainActivity;

public class LqAllScoreActivity extends LqScoreActivity {
	public JSONArray getJsonArray() {
		/**add by yejc 20130418 start*/
		isLetScore = false;
		TextView guest = (TextView)findViewById(R.id.jc_data_analysis_score_guest);
		TextView letScore = (TextView)findViewById(R.id.jc_data_analysis_score_let);
		TextView home = (TextView)findViewById(R.id.jc_data_analysis_score_home);
		guest.setText("大球");
		letScore.setText("总分");
		home.setText("小球");
		/**add by yejc 20130418 end*/
		JSONArray json = null;
		try {
			json = JcExplainActivity.jsonObject.getJSONObject("result")
					.getJSONArray("totalScores");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
