package com.ruyicai.activity.buy.jc.score.lq;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreInfoActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.ScoreInfo;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.ScoreListInterface;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JcLqScoreListActivity extends JcScoreListActivity {

	public void setReguestType() {
		reguestType = "immediateScoreJcl";
	}

	/**
	 * 读取数据
	 */
	public void getPreferences() {
		shellRw = new RWSharedPreferences(this, ShellRWConstants.JCLQ_PREFER);
	}

	public void turnInfoActivity(int position) {
		Intent intent = new Intent(context, JcLqScoreInfoActivity.class);
		intent.putExtra("event", listInfo.get(position).getEvent());
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

	public void setIsLq() {
		isLq = true;
	}
}
