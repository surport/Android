package com.ruyicai.activity.buy;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.BaseActivity.BallSensor;
import com.ruyicai.activity.usercenter.TrackQueryActivity;
import com.ruyicai.constant.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public abstract class TouzhuBaseActivity extends Activity {
	public static final String ERROR_ISSUE = "1001";
	public Context context;
	public boolean isFromTrackQuery = false; //add by yejc 20130510

	public abstract void touzhuIssue(String issue);// 当前期过期再次投注

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this;
		//add by yejc 20130510
		isFromTrackQuery = getIntent().getBooleanExtra(TrackQueryActivity.FLAG_FROM_TRACK_QUERY, false);
	}

	public void isNoIssue(Handler handler, JSONObject obj) {
		try {
			String error = obj.getString("error_code");
			if (error.equals(ERROR_ISSUE)) {
				final String issue = obj.getString("batchcode");
				handler.post(new Runnable() {
					@Override
					public void run() {
						showIssue(issue);
					}
				});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void showIssue(final String issue) {
		LayoutInflater inflate = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		TextView textContent = (TextView) view
				.findViewById(R.id.touzhu_succe_text);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button cancel = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		ok.setBackgroundResource(R.drawable.loginselecter);
		cancel.setBackgroundResource(R.drawable.loginselecter);
		ok.setText(context.getString(R.string.dialog_issue_text_ok));
		cancel.setText(context.getString(R.string.dialog_issue_text_cancel));
		textContent.setText(context
				.getString(R.string.dialog_issue_text_content) + issue + "期。");
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				touzhuIssue(issue);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				finish();
			}
		});

		dialog.show();
		dialog.getWindow().setContentView(view);
	}
}
