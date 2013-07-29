/**
 * 
 */
package com.ruyicai.activity.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.net.newtransaction.recharge.RechargeDescribeInterface;
import com.umeng.analytics.MobclickAgent;

/**
 * 公司简介
 * 
 * @author Administrator
 * 
 */
public class Accoutmovecash extends Activity {
	// ReturnPage returnPage;
	public ProgressDialog progressdialog;
	public static final String TITLE = "title";
	public static final String URL = "url";
	private TextView textView;
	Handler handler = new Handler();
	String titleStr = "银行转账";
	String iFileName = "accoutchangecash.html";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(showView());
	}

	public View showView() {
		LayoutInflater inflate = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.layout_ruyizhushou,
				null);
		view.findViewById(R.id.ruyihelper_listview_relative).setVisibility(
				View.GONE);
		Button btnreturn = (Button) view
				.findViewById(R.id.ruyizhushou_btn_return);
		TextView title = (TextView) view.findViewById(R.id.accountTitle_text);
		title.setText("银行转账");
		btnreturn.setBackgroundResource(R.drawable.returnselecter);
		btnreturn.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		textView = (TextView) view.findViewById(R.id.ruyipackage_text);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(16);
		initTextViewContent();
		return view;

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

	private void initTextViewContent() {
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		JSONObject jsonObject = getJSONByLotno();
		try {
			String conten = jsonObject.get("content").toString();
			textView.setText(conten);
			if (progressdialog != null) {
				progressdialog.dismiss();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static JSONObject getJSONByLotno() {
		JSONObject jsonObjectByLotno = RechargeDescribeInterface.getInstance()
				.rechargeDescribe("bankTransferChargeDescription");
		return jsonObjectByLotno;
	}
}
