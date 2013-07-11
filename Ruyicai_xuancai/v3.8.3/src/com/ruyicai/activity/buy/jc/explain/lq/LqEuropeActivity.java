package com.ruyicai.activity.buy.jc.explain.lq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.activity.buy.jc.explain.zq.EuropeActivity;

public class LqEuropeActivity extends EuropeActivity {

	protected List getScoreInfo(JSONArray jsonArray) {
		listInfo = new ArrayList<ExplainInfo>();
		for (int i = 0; i < jsonArray.length(); i++) {
			ExplainInfo info = new ExplainInfo();
			try {
				JSONObject json = jsonArray.getJSONObject(i);
				info.setCompanyName(json.getString("companyName"));
				info.setHomeWin(json.getString("homeWin"));
				info.setGuestWin(json.getString("guestWin"));
				info.setHomeWinLu(json.getString("homeWinLv"));
				info.setGuestWinLu(json.getString("guestWinLv"));
				info.setK_h(json.getString("k_h"));
				info.setK_g(json.getString("k_g"));
				info.setFanHuanLu(json.getString("fanHuanLv"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			listInfo.add(info);
		}
		return listInfo;
	}

	public void setLQ() {
		this.isLq = true;
	}
}
