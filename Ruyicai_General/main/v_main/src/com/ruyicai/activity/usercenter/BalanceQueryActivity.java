/**
 * 
 */
package com.ruyicai.activity.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.ReturnPage;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 余额查询
 * 
 * @author Administrator
 */
public class BalanceQueryActivity extends Activity {

	private TextView balanceInfo, remind;
	String jsonobject;
	private Button cancleLook;
	private String re;
	private JSONObject obj;
	MyHandler handler;// 自定义handler
	String deposit_amount = "";
	String Valid_amount = "";
	String freeze_amout = "";
	String totalBalance = "";
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		jsonobject = this.getIntent().getStringExtra("balancejson");
		encodejson(jsonobject);
	}

	private void encodejson(String json) {
		try {
			JSONObject balancejson = new JSONObject(json);
			initNetBackData(balancejson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initView() {
		setContentView(R.layout.usercenter_balancequery);
		balanceInfo = (TextView) findViewById(R.id.balanceinfo);
		initBalanceInfo();
		cancleLook = (Button) findViewById(R.id.usercenter_balancequery_back);
		cancleLook.setText("关闭");
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initBalanceInfo() {
		StringBuffer balanceInfoBuffer = new StringBuffer();
		balanceInfoBuffer.append(getString(R.string.total_balance))
				.append(totalBalance).append("\n\n")
				.append(getString(R.string.freeze_amout)).append(freeze_amout)
				.append("\n\n")
				.append(getString(R.string.usercenter_currentBalance))
				.append(deposit_amount).append("\n\n")
				.append(getString(R.string.usercenter_withdrawBalance))
				.append(Valid_amount).append("\n");
		balanceInfo.setText(balanceInfoBuffer.toString());
	}

	private void initNetBackData(JSONObject obj) {
		try {
			deposit_amount = obj.getString("bet_balance");
			Valid_amount = obj.getString("drawbalance");
			freeze_amout = obj.getString("freezebalance");
			totalBalance = obj.getString("balance");
			initView();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}
}
