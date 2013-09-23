package com.ruyicai.activity.more;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.AboutInfoInterface;
import com.ruyicai.net.newtransaction.HelpCenterItemInterface;

/**
 * 关于软件
 * 
 * @author win
 * 
 */
public class AboutSoftwareActivity extends Activity {
	private String contentString = null;
	private TextView titleTextView;
	private TextView contenTextView;
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_center_item);
		context = this;
		setTitle();
		getJSONByLotno();

	}

	private void setTitle() {
		titleTextView = (TextView) findViewById(R.id.helpCenterItemTitle);
		titleTextView.setText(getString(R.string.ruyihelper_about));
	}

	public void initView(String content) {
		contenTextView = (TextView) findViewById(R.id.helpCenterItemContent);
		contenTextView.setText(content);
	}

	public ProgressDialog progressdialog;

	private void getJSONByLotno() {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonObjectByLotno = AboutInfoInterface.getInstance()
						.aboutInfoQuery();
				try {
					String errorCode = jsonObjectByLotno
							.getString("error_code");
					final String message = jsonObjectByLotno
							.getString("message");
					if (errorCode.equals("0000")) {
						final String content = jsonObjectByLotno
								.getString("introduce");
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								initView(content);
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (progressdialog != null) {
					progressdialog.dismiss();
				}
			}
		}).start();

	}
}
