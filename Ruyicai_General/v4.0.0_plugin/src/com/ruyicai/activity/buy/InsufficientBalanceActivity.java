package com.ruyicai.activity.buy;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.account.AccountListActivity;
import com.ruyicai.activity.account.DirectPayActivity;
import com.ruyicai.util.PublicMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 余额不足页面
 * 
 * @author Administrator
 * 
 */
public class InsufficientBalanceActivity extends Activity {
	/** 彩种编号 */
	private String lotnoString;
	/** 彩种类型文本框 */
	private TextView lottypeTextView;
	/** 购彩金额文本框 */
	private TextView amtTextView;
	/** 直接支付按钮 */
	private Button directlyPayButton;
	/** 去充值按钮 */
	private Button toRechargeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.insufficientbalance_activity);

		// 获取意图数据的数据
		Intent intent = getIntent();
		// 获取彩种编号
		lotnoString = intent.getStringExtra("lotno");
		// 获取投注金额
		String amountString = intent.getStringExtra("amount");

		// 初始化彩种的显示
		lottypeTextView = (TextView) findViewById(R.id.insufficient_balance_lottype);
		String lottypeStrig = PublicMethod.toLotno(lotnoString);
		lottypeTextView.setText(lottypeStrig);

		// 初始化金额的显示
		amtTextView = (TextView) findViewById(R.id.insufficient_balance_amt);
		long menoy = Long.valueOf(amountString) / 100;
		amtTextView.setText(menoy + "元");

		// 初始化直接支付按钮
		directlyPayButton = (Button) findViewById(R.id.insufficient_balance_button_directpay);
		directlyPayButton.setOnClickListener(new ButtonOnClickLisntener());

		// 初始化去充值按钮
		toRechargeButton = (Button) findViewById(R.id.insufficient_balance_button_recharge);
		toRechargeButton.setOnClickListener(new ButtonOnClickLisntener());
	}

	class ButtonOnClickLisntener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = null;
			switch (v.getId()) {
			case R.id.insufficient_balance_button_directpay:
				intent = new Intent(InsufficientBalanceActivity.this,
						DirectPayActivity.class);
				break;
			case R.id.insufficient_balance_button_recharge:
				intent = new Intent(InsufficientBalanceActivity.this,
						AccountListActivity.class);
				intent.putExtra("isonKey", "fasle");
				break;
			}

			startActivity(intent);
		}

	}
}
