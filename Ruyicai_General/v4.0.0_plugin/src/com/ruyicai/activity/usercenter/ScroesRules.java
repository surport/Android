package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.net.newtransaction.usercenter.AutoGetScoresRules;

public class ScroesRules {
	Context scroesContext;
	ProgressDialog progressDialog;
	TextView scoresrules;

	ScroesRules(Context context) {
		scroesContext = context;
		progressDialog = UserCenterDialog.onCreateDialog(scroesContext,
				"loading........");
		progressDialog.show();
		initScoreDialog();
	}

	private void initScoreDialog() {
		LayoutInflater inflater = (LayoutInflater) scroesContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.userscroe_show, null);
//		Handler handler = new Handler();
		scoresrules = (TextView) view
				.findViewById(R.id.userscore_webview);
		new Thread(new Runnable() {
					@Override
					public void run() {
						String net = AutoGetScoresRules.getInstance().getScoresRules();
						JSONObject json;
						try {
							json = new JSONObject(net);
							String scoresRules = json.getString("content");
							Message msg=handler.obtainMessage();
							msg.what=0;
							msg.obj=scoresRules;
							handler.sendMessage(msg);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
					}
				}).start();
//		handler.post(new Runnable() {
//
//			@Override
//			public void run() {
//				String net = AutoGetScoresRules.getInstance().getScoresRules();
//				try {
//					JSONObject json = new JSONObject(net);
//					String scoresRules = json.getString("content");
//					PublicMethod.myOutLog("scoresRules", scoresRules);
//					scoresrules.setText(scoresRules);
//					progressDialog.dismiss();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		Button close = (Button) view.findViewById(R.id.usercenter_scrore_back);
		final Dialog dialog = new Dialog(scroesContext, R.style.dialog);
		dialog.setContentView(view);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int i=msg.what;
			if(i==0){
				scoresrules.setText((String)msg.obj);
				progressDialog.dismiss();
			}
		}
	};
}
