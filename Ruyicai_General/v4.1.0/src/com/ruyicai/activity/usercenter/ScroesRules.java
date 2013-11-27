package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.usercenter.AutoGetScoresRules;
import com.ruyicai.util.PublicMethod;
import com.umeng.analytics.MobclickAgent;

public class ScroesRules {
	Context scroesContext;
	ProgressDialog progressDialog;

	ScroesRules(Context context) {
		scroesContext = context;
		progressDialog = UserCenterDialog.onCreateDialog(scroesContext,
				"loading........");
		progressDialog.show();
		initScoreDialog();
		MobclickAgent.onEvent(scroesContext, "shuoming"); // BY贺思明
															// 在用户中心主页点击上面的积分后面的“说明”。
	}

	private void initScoreDialog() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) scroesContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.userscroe_show, null);
		Handler handler = new Handler();
		final TextView scoresrules = (TextView) view
				.findViewById(R.id.userscore_webview);
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String net = AutoGetScoresRules.getInstance().getScoresRules();
				try {
					JSONObject json = new JSONObject(net);
					String scoresRules = json.getString("content");
					PublicMethod.myOutLog("scoresRules", scoresRules);
					scoresrules.setText(scoresRules);
					progressDialog.dismiss();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		Button close = (Button) view.findViewById(R.id.usercenter_scrore_back);
		final Dialog dialog = new Dialog(scroesContext, R.style.dialog);
		dialog.setContentView(view);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
