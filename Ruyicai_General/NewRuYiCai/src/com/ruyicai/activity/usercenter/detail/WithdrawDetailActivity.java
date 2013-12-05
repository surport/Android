package com.ruyicai.activity.usercenter.detail;

import org.json.JSONException;
import org.json.JSONObject;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.usercenter.AccountWithdrawActivity;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class WithdrawDetailActivity extends Activity implements HandlerMsg {
	private Intent intent = null;
	private Controller controller = Controller.getInstance(this);
	private MyHandler handler = new MyHandler(this);
//	private JSONObject jsonObject = null;
	
	private ImageView checkTimeImageView;
	private ImageView remitTimeImageView;
	private ImageView finishTimeImageView;
	
	private TextView createTimeTextView;
	private TextView checkTimeTextView;
	private TextView remitTimeTextView;
	private TextView finishTimeTextView;
	
	private TextView withdrawAmountTextView;
	private TextView withdrawModeTextView;
	private TextView withdrawStateTextView;
	private TextView remarkTextView;
	
	private TextView withdrawNumber;
	
	private String queryShow = "查询中...";
	private String forecastShow = "预计";
	private String cashState = "";
	private String cashId = "";
	private String cashStateText = "";
	private String cashMoney = "";
	private String createTiem = "";
	private String checkTiem = "";
	private String remitTiem = "";
	private String finishTiem = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_withdraw_detail);
		intent = getIntent();
		cashState = intent.getStringExtra(AccountWithdrawActivity.CASH_STATE);
		cashMoney = intent.getStringExtra(AccountWithdrawActivity.CASH_MONEY);
		cashStateText = intent.getStringExtra(AccountWithdrawActivity.CASH_STATE_TEXT); 
		cashId = intent.getStringExtra(AccountWithdrawActivity.CASHID);
		initView();
		setViewShowState();
		controller.queryCashDetail(handler, cashId);
	}
	
	private void initView() {
		checkTimeImageView = (ImageView)findViewById(R.id.account_withdraw_detail_imageview_checktime);
		remitTimeImageView = (ImageView)findViewById(R.id.account_withdraw_detail_imageview_remittime);
		finishTimeImageView = (ImageView)findViewById(R.id.account_withdraw_detail_imageview_finishtime);
		
		createTimeTextView = (TextView)findViewById(R.id.account_withdraw_detail_textview_createtime);
		checkTimeTextView = (TextView)findViewById(R.id.account_withdraw_detail_textview_checktime);
		remitTimeTextView = (TextView)findViewById(R.id.account_withdraw_detail_textview_remittime);
		finishTimeTextView = (TextView)findViewById(R.id.account_withdraw_detail_textview_finishtime);
		
		withdrawAmountTextView = (TextView)findViewById(R.id.account_withdraw_detail_amount);
		withdrawModeTextView = (TextView)findViewById(R.id.account_withdraw_detail_withdraw_mode);
		withdrawStateTextView = (TextView)findViewById(R.id.account_withdraw_detail_order_state);
		remarkTextView = (TextView)findViewById(R.id.account_withdraw_detail_remark);
		withdrawNumber = (TextView)findViewById(R.id.account_withdraw_number);
		withdrawNumber.setText(cashId);
	}
	
	private void setViewShowState() {
		createTimeTextView.setText(formatString(R.string.account_withdraw_detail_create_time,queryShow));
		withdrawAmountTextView.setText(cashMoney+"元");
		withdrawStateTextView.setText(cashStateText);
		if ("1".equals(cashState)) {
//			withdrawStateTextView.setText(cashStateText);
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,forecastShow,queryShow));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,forecastShow,queryShow));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,forecastShow,queryShow));
		} else if ("103".equals(cashState)) {
			withdrawStateTextView.setText(cashStateText);
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,"",queryShow));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,forecastShow,queryShow));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,forecastShow,queryShow));
			checkTimeImageView.setImageResource(R.drawable.account_withdraw_finish);
			checkTimeTextView.setTextColor(getColor(R.color.account_withdraw_text_color));
			remitTimeImageView.setImageResource(R.drawable.account_withdraw_doing);
		} else if ("105".equals(cashState)) {
//			withdrawStateTextView.setText("已汇款");
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,"",queryShow));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,"",queryShow));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,"",queryShow));
			checkTimeImageView.setImageResource(R.drawable.account_withdraw_finish);
			remitTimeImageView.setImageResource(R.drawable.account_withdraw_finish);
			finishTimeImageView.setImageResource(R.drawable.account_withdraw_finish);
			checkTimeTextView.setTextColor(getColor(R.color.account_withdraw_text_color));
			remitTimeTextView.setTextColor(getColor(R.color.account_withdraw_text_color));
			finishTimeTextView.setTextColor(getColor(R.color.account_withdraw_text_color));
		}
	}

	private String formatString(int resId, String args) {
		return String.format(getResources().getString(resId), args);
	}
	
	private String formatString(int resId, String arg0, String arg1) {
		return String.format(getResources().getString(resId), arg0, arg1);
	}
	
	private String formatString(int resId, String arg0, String arg1, String arg2) {
		return String.format(getResources().getString(resId), arg0, arg1, arg2);
	}
	
	private int getColor(int resId) {
		return getResources().getColor(resId);
	}
	
	private void setShowTiem(JSONObject obj) {
		try {
			JSONObject json = new JSONObject(obj.getString("result"));
			createTiem = json.getString("createTime");
			checkTiem = json.getString("checkTime");
			remitTiem = json.getString("remitTime");
			finishTiem = json.getString("expectFinishTime");
			String type = json.getString("type");
			if ("2".equals(type)) {
				withdrawModeTextView.setText(R.string.account_withdraw_detail_type);
			} else {
				withdrawModeTextView.setText(json.getString("bankName"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		createTimeTextView.setText(formatString(R.string.account_withdraw_detail_create_time,createTiem));
		if ("1".equals(cashState)) {
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,forecastShow,checkTiem));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,forecastShow,remitTiem));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,forecastShow,finishTiem));
			remarkTextView.setText(formatString(R.string.account_withdraw_detail_remark_content,cashStateText,checkTiem, "审核"));
		} else if ("105".equals(cashState)) {
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,"",checkTiem));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,"",remitTiem));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,"",finishTiem));
			remarkTextView.setText("已到账"/*formatString(R.string.account_withdraw_detail_remark_content,"已汇款",finishTiem, "到账")*/);
		} else if ("103".equals(cashState)) {
			checkTimeTextView.setText(formatString(R.string.account_withdraw_detail_check_time,"",checkTiem));
			remitTimeTextView.setText(formatString(R.string.account_withdraw_detail_remit_time,forecastShow,remitTiem));
			finishTimeTextView.setText(formatString(R.string.account_withdraw_detail_finish_time,forecastShow,finishTiem));
			remarkTextView.setText(formatString(R.string.account_withdraw_detail_remark_content,cashStateText,remitTiem, "汇款"));
		}
	}

	@Override
	public void errorCode_0000() {
		setShowTiem(controller.getRtnJSONObject());
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
	}

}
