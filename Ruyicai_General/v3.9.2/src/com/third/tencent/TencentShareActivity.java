package com.third.tencent;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.Addscorewithshare;
import com.ruyicai.util.RWSharedPreferences;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv1.OAuthV1;

public class TencentShareActivity extends Activity {
	EditText sharecontent;
	Button commit;
	Button cannel;
	OAuthV1 oAuthV1;
	String tencentsharecontent = "";
	String userno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tencentshare);
		getInfo();
		init();
	}

	protected void init() {
		sharecontent = (EditText) findViewById(R.id.sharecontent);
		sharecontent.setText(tencentsharecontent);
		commit = (Button) findViewById(R.id.share);
		cannel = (Button) findViewById(R.id.btn_return);
		commit.setOnClickListener(click);
		cannel.setOnClickListener(click);

	}

	OnClickListener click = new OnClickListener() {
		String response;
		TAPI tAPI;

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.share:
				tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_1);
				try {
					response = tAPI.add(oAuthV1, "json", tencentsharecontent,
							"127.0.0.1");
					JSONObject json = new JSONObject(response);
					String errorcode = json.getString("errcode");
					final String msg = json.getString("msg");
					if (errorcode.equals("0")) {
						runOnUiThread(new Runnable() {
							@SuppressWarnings("static-access")
							@Override
							public void run() {
								RWSharedPreferences pre = new RWSharedPreferences(
										TencentShareActivity.this, "addInfo");
								userno = pre.getStringValue("userno");
								Toast.makeText(TencentShareActivity.this,
										R.string.send_sucess, Toast.LENGTH_LONG)
										.show();
								Addscorewithshare.getInstance().addscore(
										userno, "资讯分享", Constants.source);// 添加积分
							}
						});
						TencentShareActivity.this.finish();
					} else {
						runOnUiThread(new Runnable() {
							@SuppressWarnings("static-access")
							@Override
							public void run() {
								Toast.makeText(TencentShareActivity.this, msg,
										Toast.LENGTH_LONG).show();
							}
						});
						TencentShareActivity.this.finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				tAPI.shutdownConnection();
				break;
			case R.id.btn_return:
				finish();
			}

		}
	};

	public void onBackPressed() {
		finish();
	}

	public void getInfo() {
		Intent intent = getIntent();
		if (intent != null) {
			tencentsharecontent = intent.getStringExtra("tencent");
			oAuthV1 = (OAuthV1) intent.getExtras().getSerializable("oauth");
		}
	}
}
