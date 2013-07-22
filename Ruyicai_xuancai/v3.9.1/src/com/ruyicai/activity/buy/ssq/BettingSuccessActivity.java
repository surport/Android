package com.ruyicai.activity.buy.ssq;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.dlt.Dlt;
import com.ruyicai.activity.buy.eleven.Eleven;
import com.ruyicai.activity.buy.fc3d.Fc3d;
import com.ruyicai.activity.buy.gdeleven.GdEleven;
import com.ruyicai.activity.buy.jc.lq.LqMainActivity;
import com.ruyicai.activity.buy.jc.zq.ZqMainActivity;
import com.ruyicai.activity.buy.nmk3.Nmk3Activity;
import com.ruyicai.activity.buy.pl3.PL3;
import com.ruyicai.activity.buy.pl5.PL5;
import com.ruyicai.activity.buy.qlc.Qlc;
import com.ruyicai.activity.buy.qxc.QXC;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.buy.ten.TenActivity;
import com.ruyicai.activity.buy.twentytwo.TwentyTwo;
import com.ruyicai.activity.buy.zc.FootballLottery;
import com.ruyicai.constant.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 投注成功页面
 * 
 * @author Administrator
 * 
 */
public class BettingSuccessActivity extends Activity {
	/** 投注页面标识 */
	public static final int BETTING = 1;
	/** 追号页面标识 */
	public static final int ADDTO = 2;
	/** 合买页面标识 */
	public static final int COOPERATION = 3;
	/** 赠送页面标识 */
	public static final int PRESENT = 4;
	/**收益追号*/
	public static final int HIGHTADDTO = 5;

	/** 彩种编号 */
	private String lotnoString;
	/** 投注提示语文本框 */
	private TextView promptTextView;
	/** 彩种类型文本框 */
	private TextView lottypeTextView;
	/** 购彩金额文本框 */
	private TextView amtTextView;
	/** 返回投注界面按钮 */
	private Button returnBettingButton;
	/** 将方案发送到邮箱 */
	private RelativeLayout sendToEmailLayout;

	/** 全局共享参数 */
	private RWSharedPreferences shellRW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_success);

		// 获取意图数据的数据
		Intent intent = getIntent();
		// 获取页面标识
		int pageInt = intent.getIntExtra("page", -1);
		// 获取彩种编号
		lotnoString = intent.getStringExtra("lotno");
		// 获取投注金额
		String amountString = intent.getStringExtra("amount");
		// 是否是双色球
		boolean isSsq = intent.getBooleanExtra("isssq", false);

		// 初始化投注提示语显示
		promptTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_prompt);
		switch (pageInt) {
		case BETTING:
			promptTextView.setText("恭喜您，方案发起成功!");
			break;
		case ADDTO:
			promptTextView.setText("恭喜您，方案发起成功!");
			break;
		case COOPERATION:
			promptTextView.setText("恭喜您，发起合买成功!");
			break;
		case PRESENT:
			promptTextView.setText("恭喜您，方案赠送成功!");
			break;
		case HIGHTADDTO:
			promptTextView.setText("恭喜您，方案发起成功!");
			break;
		}

		// 初始化彩种的显示
		lottypeTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_lottype);
		String lottypeStrig = PublicMethod.toLotno(lotnoString);
		lottypeTextView.setText(lottypeStrig);

		// 初始化金额的显示
		amtTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_amt);
		int menoy = Integer.valueOf(amountString) / 100;
		amtTextView.setText(menoy + "元");

		// 初始化返回按钮
		returnBettingButton = (Button) findViewById(R.id.ssq_bettingsuccess_returnbetting);
		returnBettingButton.setOnClickListener(new ButtonOnClickListener());

		// 如果没有绑定，则显示；否则默认不显示
		if (!isBindedEmail() && isSsq) {
			// 初始化发送到邮箱
			sendToEmailLayout = (RelativeLayout) findViewById(R.id.ssq_bettingsuccess_sendtoemail);
			sendToEmailLayout.setOnClickListener(new ButtonOnClickListener());
			sendToEmailLayout.setVisibility(View.VISIBLE);
		}

	}

	// 是否綁定绑定邮箱
	private boolean isBindedEmail() {
		// 初始化全局共享参数
		shellRW = new RWSharedPreferences(this, "addInfo");
		String email = shellRW.getStringValue("email");

		if (email == null || email.equals("") || email.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	class ButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.ssq_bettingsuccess_returnbetting:
				returnToBet();
				break;

			case R.id.ssq_bettingsuccess_sendtoemail:
				Intent intent2 = new Intent(BettingSuccessActivity.this,
						ReceiveAndBindEmailActivity.class);
				startActivity(intent2);
				break;
			}
		}

		/**
		 * 返回投注
		 */
		private void returnToBet() {
			Intent intent = null;
			if (lotnoString.equals(Constants.LOTNO_SSQ)) {
				intent = new Intent(BettingSuccessActivity.this, Ssq.class);
			} else if (lotnoString.equals(Constants.LOTNO_DLT)) {
				intent = new Intent(BettingSuccessActivity.this, Dlt.class);
			} else if (lotnoString.equals(Constants.LOTNO_FC3D)) {
				intent = new Intent(BettingSuccessActivity.this, Fc3d.class);
			} else if (lotnoString.equals(Constants.LOTNO_11_5)) {
				intent = new Intent(BettingSuccessActivity.this, Dlc.class);
			} else if (lotnoString.equals(Constants.LOTNO_SSC)) {
				intent = new Intent(BettingSuccessActivity.this, Ssc.class);
			} else if (lotnoString.equals(Constants.LOTNO_JCZQ_HUN)
					|| lotnoString.equals(Constants.LOTNO_JCZQ)
					|| lotnoString.equals(Constants.LOTNO_JCZQ_RQSPF)
					|| lotnoString.equals(Constants.LOTNO_JCZQ_ZQJ)
					|| lotnoString.equals(Constants.LOTNO_JCZQ_BF)
					|| lotnoString.equals(Constants.LOTNO_JCZQ_BQC)) {
				intent = new Intent(BettingSuccessActivity.this,
						ZqMainActivity.class);
			} else if (lotnoString.equals(Constants.LOTNO_NMK3)) {
				intent = new Intent(BettingSuccessActivity.this,
						Nmk3Activity.class);
			} else if (lotnoString.equals(Constants.LOTNO_eleven)) {
				intent = new Intent(BettingSuccessActivity.this, Eleven.class);
			} else if (lotnoString.equals(Constants.LOTNO_GD_11_5)) {
				intent = new Intent(BettingSuccessActivity.this, GdEleven.class);
			} else if (lotnoString.equals(Constants.LOTNO_PL3)) {
				intent = new Intent(BettingSuccessActivity.this, PL3.class);
			} else if (lotnoString.equals(Constants.LOTNO_QLC)) {
				intent = new Intent(BettingSuccessActivity.this, Qlc.class);
			} else if (lotnoString.equals(Constants.LOTNO_22_5)) {
				intent = new Intent(BettingSuccessActivity.this,
						TwentyTwo.class);
			} else if (lotnoString.equals(Constants.LOTNO_PL5)) {
				intent = new Intent(BettingSuccessActivity.this, PL5.class);
			} else if (lotnoString.equals(Constants.LOTNO_QXC)) {
				intent = new Intent(BettingSuccessActivity.this, QXC.class);
			} else if (lotnoString.equals(Constants.LOTNO_ZC)
					|| lotnoString.equals(Constants.LOTNO_JQC)
					|| lotnoString.equals(Constants.LOTNO_LCB)
					|| lotnoString.equals(Constants.LOTNO_SFC)
					|| lotnoString.equals(Constants.LOTNO_RX9)) {
				intent = new Intent(BettingSuccessActivity.this,
						FootballLottery.class);
			} else if (lotnoString.equals(Constants.LOTNO_JCLQ)
					|| lotnoString.equals(Constants.LOTNO_JCLQ_RF)
					|| lotnoString.equals(Constants.LOTNO_JCLQ_SFC)
					|| lotnoString.equals(Constants.LOTNO_JCLQ_DXF)
					|| lotnoString.equals(Constants.LOTNO_JCLQ_HUN)) {
				intent = new Intent(BettingSuccessActivity.this,
						LqMainActivity.class);
			} else if (lotnoString.equals(Constants.LOTNO_ten)) {
				intent = new Intent(BettingSuccessActivity.this,
						TenActivity.class);
			} else if (lotnoString
					.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)
					|| lotnoString
							.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)
					|| lotnoString
							.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)
					|| lotnoString
							.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)
					|| lotnoString
							.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)) {
				intent = new Intent(BettingSuccessActivity.this,
						BeiJingSingleGameActivity.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
