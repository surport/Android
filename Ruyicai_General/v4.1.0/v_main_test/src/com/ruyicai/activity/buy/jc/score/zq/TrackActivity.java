package com.ruyicai.activity.buy.jc.score.zq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.score.beijing.BeijingScoreActivity;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.JcInfoAdapter;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.ScoreInfo;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity.JcInfoAdapter.ViewHolder;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.ScoreListInterface;
import com.ruyicai.util.RWSharedPreferences;

import android.R.integer;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 关注类
 * 
 * @author win
 * 
 */
public class TrackActivity extends JcScoreListActivity {
	TextView textInfo;
	protected List<ScoreInfo> listInfoCopy;
	public boolean isBeiDanTrack = false; //add by yejc 20130716

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isTrack = true;
		textInfo = (TextView) findViewById(R.id.jc_score_text_no_info);
	}

	protected void onResume() {
		super.onResume();

	}

	public void initList() {
		String events = shellRw.getStringValue(allcountries.get(index));
		
		String eventStrs[] = events.split(";");
		listInfoCopy = new ArrayList<JcScoreListActivity.ScoreInfo>();
		for (int j = 0; j < listInfo.size(); j++) {
			for (int i = 0; i < eventStrs.length; i++) {
				if (listInfo.get(j).getEvent().equals(eventStrs[i])) {
					listInfoCopy.add(listInfo.get(j));
				}
			}
		}
		listMain = (ListView) findViewById(R.id.buy_jc_main_listview);
		adapter = new JcInfoAdapter(context, listInfoCopy);
		listMain.setAdapter(adapter);
		/* 列表的点击后的背景 */
		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				turnInfoActivity(position);
			}

		};
		listMain.setOnItemClickListener(clickListener);
	}

	public void initNameSpinner() {
		if (allcountries.size() > 0) {
			money_brank = (Spinner) findViewById(R.id.jc_score_list_spinner);
			money_brank.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					index = position;
					spinnerOnclik(allcountries.get(position));
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});

			adapterArray = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, allcountries);
			adapterArray.setDropDownViewResource(R.layout.myspinner_dropdown);
			money_brank.setAdapter(adapterArray);
			money_brank.setSelection(0);
		}
	}

	public void initListInfo() {
		index = 0;
		allcountries = new ArrayList<String>();
		Map<String, ?> mapTrack = shellRw.getAllKey();
		Set<String> keys = mapTrack.keySet();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			allcountries.add(key);
		}
		for (int i = 0; i < allcountries.size(); i++) {
			for (int j = i + 1; j < allcountries.size(); j++) {
				int oneDate = Integer.parseInt(dateString(allcountries.get(i)));
				int twoDate = Integer.parseInt(dateString(allcountries.get(j)));
				if (oneDate < twoDate) {
					String date = allcountries.get(i);
					allcountries.set(i, allcountries.get(j));
					allcountries.set(j, date);
				}
			}
		}
		if (allcountries.size() > 0) {
			setContentView(R.layout.jc_score_list);
			layoutSpinner = (LinearLayout) findViewById(R.id.jc_score_layout);
			layoutSpinner.setVisibility(View.VISIBLE);
			// layoutSpinner.setVisibility(View.VISIBLE);
			// textInfo.setVisibility(View.GONE);
			initNameSpinner();
		} else {
			// layoutSpinner.setVisibility(View.GONE);
			// textInfo.setVisibility(View.VISIBLE);
			setContentView(R.layout.jc_score_track_no_info);
		}
	}

	/**
	 * 获取列表联网
	 */
	public void getScoreNet(final String type, final String date,
			final String reguestType) {
		mProgress = UserCenterDialog.onCreateDialog(context);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String re = "";
					if (isBeiDanTrack) { //add by yejc 20130716
						re = ScoreListInterface.getBeiDanScore(type, reguestType, 
								BeijingScoreActivity.lotno, allcountries.get(index));
					} else {
						re = ScoreListInterface.getScore(type, date,
								reguestType);
					}
					
					mProgress.dismiss();
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					if (error_code.equals("0000")) {
						final JSONArray jsonArray = obj.getJSONArray("result");
						handler.post(new Runnable() {
							@Override
							public void run() {
								listInfo = getScoreInfo(jsonArray);
								initList();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void turnInfoActivity(int position) {
		Intent intent = new Intent(context, JcScoreInfoActivity.class);
		intent.putExtra("event", listInfoCopy.get(position).getEvent());
		startActivity(intent);
	}
}
