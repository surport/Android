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

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.HelpCenterItemInterface;

public class HelpCenterItemActivity extends Activity {
	private String id = null;
	private String contentString = null;
	private String tittleString = null;
	private TextView titleTextView;
	private TextView contenTextView;
	private Context context;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_center_item);
		context = this;
		getIntentInfo();
		setTitle();
		getJSONByLotno(id);

	}

	private void setTitle() {
		titleTextView = (TextView) findViewById(R.id.helpCenterItemTitle);
		titleTextView.setText(tittleString);
	}

	/**
	 * 得到当前页面的下标
	 */
	public void getIntentInfo() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		id = bundle.getString("id");
		tittleString = bundle.getString("title");
	}

	public void initView(String content) {
		contenTextView = (TextView) findViewById(R.id.helpCenterItemContent);
		contenTextView.setText(content);
	}

	public ProgressDialog progressdialog;

	private void getJSONByLotno(final String id) {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				JSONObject jsonObjectByLotno = HelpCenterItemInterface
						.getInstance().accountDetailQuery(id);
				try {
					final String content = getList(jsonObjectByLotno);
					handler.post(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							initView(content);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (progressdialog != null) {
					progressdialog.dismiss();
				}
			}
		}).start();

	}

	private String getList(JSONObject jsonObjectByLotno) throws JSONException {
		String content = "";
		JSONArray result = null;
		try {
			if (jsonObjectByLotno.has("title")) {
				content = jsonObjectByLotno.get("content").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}
