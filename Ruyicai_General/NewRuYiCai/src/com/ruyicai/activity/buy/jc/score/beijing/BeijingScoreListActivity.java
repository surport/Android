package com.ruyicai.activity.buy.jc.score.beijing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.ruyicai.activity.buy.jc.score.zq.JcScoreListActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.ScoreListInterface;
import com.ruyicai.util.RWSharedPreferences;

public class BeijingScoreListActivity extends JcScoreListActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isBeiDan = true;
		super.onCreate(savedInstanceState);
		getPreferences();
	}

	@Override
	public void getPreferences() {
		shellRw = new RWSharedPreferences(this, ShellRWConstants.BEIJING_PREFER);
	}

//	@Override
//	public void initListInfo() {
//		if (allcountries == null) {
//			getScoreNet(TYPE, "", reguestType);
//		} else {
//			spinnerOnclik(allcountries.get(index));
//		}
//	}
	
	/**
	 * 获取列表联网
	 */
	public void getScoreNet(final String type, final String batchCode,
			final String reguestType) {
		mProgress = UserCenterDialog.onCreateDialog(context);
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String re = ScoreListInterface.getBeiDanScore(type,
								reguestType, BeijingScoreActivity.lotno, batchCode);
					mProgress.dismiss();
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					if(obj.has("batchCodeSelect")) {
						allcountries = setSpinnerDate(obj.getString("batchCodeSelect"));
					}
					if (obj.has("todayIndex")) {
						todayIndex = Integer.parseInt(obj.getString("todayIndex"));
					}
					
					if (error_code.equals("0000")) {
						final JSONArray jsonArray = obj.getJSONArray("result");
						handler.post(new Runnable() {
							@Override
							public void run() {
								listInfo = getScoreInfo(jsonArray);
								initNameSpinner();
								initList();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								initNameSpinner();
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
	
	public void spinnerOnclik(String batchCode) {
		if (listInfo != null) {
			listInfo.clear();
			initList();
		}
		getScoreNet(TYPE, batchCode, reguestType);
	}
	
	public void turnInfoActivity(int position) {
		Intent intent = new Intent(this, BeijingScoreInfoActivity.class);
		intent.putExtra("event", listInfo.get(position).getEvent());
		startActivity(intent);
	}

}
