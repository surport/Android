package com.ruyicai.activity.buy.ssq;

import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.buy.cq11x5.Cq11Xuan5;
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
import com.ruyicai.activity.join.JoinCheckActivity;
import com.ruyicai.activity.buy.zc.FootBallMainActivity;
import com.ruyicai.activity.join.JoinInfoActivity;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.activity.usercenter.AccountDetailsActivity;
import com.ruyicai.activity.usercenter.BalanceQueryActivity;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.activity.usercenter.FeedbackListActivity;
import com.ruyicai.activity.usercenter.GiftQueryActivity;
import com.ruyicai.activity.usercenter.NewUserCenter;
import com.ruyicai.activity.usercenter.TrackQueryActivity;
import com.ruyicai.activity.usercenter.UserScoreActivity;
import com.ruyicai.activity.usercenter.WinPrizeActivity;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	/** 收益追号 */
	public static final int HIGHTADDTO = 5;
	/** add by pengcx 20130723 start */
	/** 参与合买成功标识 */
	public static final int JOINCOOPERATION = 10;
	/** add by pengcx 20130723 end */
	/** add by pengcx 20130809 start */
	/** 开奖走势成功标识 */
	public static final int NOTICEBALL = 11;
	/** add by pengcx 20130809 end */

	/** 彩种编号 */
	private String lotnoString;
	/** 跳转页面类型 */
	private int pageInt;
	/** 投注提示语文本框 */
	private TextView promptTextView;
	/** 彩种类型文本框 */
	private TextView lottypeTextView;
	/** 购彩金额文本框 */
	private TextView amtTextView;
	/** 返回投注界面按钮 */
	private Button returnBettingButton;
	/** 投注详情按钮 */
	private Button betDetailButton;
	/** 将方案发送到邮箱 */
	private RelativeLayout sendToEmailLayout;

	/** 全局共享参数 */
	private RWSharedPreferences shellRW;
	private int fromInt;
	ProgressDialog dialog;
	
	private boolean isGift = false;

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			dialog.dismiss();
			switch (msg.what) {
			case 1:

				Toast.makeText(BettingSuccessActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				Intent intent = new Intent(BettingSuccessActivity.this,
						WinPrizeActivity.class);
				intent.putExtra("winjson", (String) msg.obj);
				startActivity(intent);
				break;
			case 4:
				Intent intentbet = new Intent(BettingSuccessActivity.this,
						BetQueryActivity.class);
				intentbet.putExtra("betjson", (String) msg.obj);
				startActivity(intentbet);
				break;

			case 5:
				Intent intentgift = new Intent(BettingSuccessActivity.this,
						GiftQueryActivity.class);
				intentgift.putExtra("giftjson", (String) msg.obj);
				startActivity(intentgift);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.betting_success);

		// 获取意图数据的数据
		Intent intent = getIntent();
		// 获取页面标识
		pageInt = intent.getIntExtra("page", -1);
		fromInt = intent.getIntExtra("from", -1);
		// 获取彩种编号
		lotnoString = intent.getStringExtra("lotno");
		// 获取投注金额
		String amountString = intent.getStringExtra("amount");
		// 是否是双色球
		boolean isSsq = intent.getBooleanExtra("isssq", false);
		isGift = intent.getBooleanExtra("isgift", false);

		// 初始化返回按钮
		returnBettingButton = (Button) findViewById(R.id.ssq_bettingsuccess_returnbetting);
		returnBettingButton.setOnClickListener(new ButtonOnClickListener());

		/** modify by pengcx 20130723 start */
		// 初始化投注提示语显示
		promptTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_prompt);
		switch (pageInt) {
		case BETTING:
			promptTextView.setText("恭喜您，方案发起成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnbet);
			break;
		case ADDTO:
			promptTextView.setText("恭喜您，方案发起成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnbet);
			break;
		case COOPERATION:
			promptTextView.setText("恭喜您，发起合买成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnbet);
			break;
		case PRESENT:
			promptTextView.setText("恭喜您，方案赠送成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnbet);
			break;
		case HIGHTADDTO:
			promptTextView.setText("恭喜您，方案发起成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnbet);
			break;
		}

		if (fromInt == JOINCOOPERATION) {
			promptTextView.setText("恭喜您，参与合买成功!");
			returnBettingButton.setText(R.string.ssq_bettingsuccess_returnjoin);
		}

		betDetailButton = (Button) findViewById(R.id.ssq_bettingsuccess_betdetail);
		betDetailButton.setOnClickListener(new ButtonOnClickListener());
		if (Constants.LOTNO_JCZQ_HUN.equals(lotnoString)
				|| Constants.LOTNO_JCZQ.equals(lotnoString)
				|| Constants.LOTNO_JCZQ_RQSPF.equals(lotnoString)
				|| Constants.LOTNO_JCZQ_ZQJ.equals(lotnoString)
				|| Constants.LOTNO_JCZQ_BF.equals(lotnoString)
				|| Constants.LOTNO_JCZQ_BQC.equals(lotnoString)
				|| Constants.LOTNO_ZC.equals(lotnoString)
				|| Constants.LOTNO_JQC.equals(lotnoString)
				|| Constants.LOTNO_LCB.equals(lotnoString)
				|| Constants.LOTNO_SFC.equals(lotnoString)
				|| Constants.LOTNO_RX9.equals(lotnoString)
				|| Constants.LOTNO_JCLQ.equals(lotnoString)
				|| Constants.LOTNO_JCLQ_RF.equals(lotnoString)
				|| Constants.LOTNO_JCLQ_SFC.equals(lotnoString)
				|| Constants.LOTNO_JCLQ_DXF.equals(lotnoString)
				|| Constants.LOTNO_JCLQ_HUN.equals(lotnoString)
				|| Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS.equals(lotnoString)
				|| Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS.equals(lotnoString)
				|| Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL.equals(lotnoString)
				|| Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE.equals(lotnoString)
				|| Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE.equals(lotnoString)) {
			betDetailButton.setVisibility(View.VISIBLE);
		}

		/** modify by pengcx 20130723 end */
		// 初始化彩种的显示
		lottypeTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_lottype);
		String lottypeStrig = PublicMethod.toLotno(lotnoString);
		lottypeTextView.setText(lottypeStrig);

		// 初始化金额的显示
		amtTextView = (TextView) findViewById(R.id.ssq_bettingsuccess_amt);
		long menoy = Long.valueOf(amountString) / 100;
		amtTextView.setText(menoy + "元");

		// 如果没有绑定，则显示；否则默认不显示
		if (!isBindedEmail() && isSsq && !isGift) {
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

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("网络连接中...");
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
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
			case R.id.ssq_bettingsuccess_betdetail:
				if (pageInt == COOPERATION) {
					Intent intent = new Intent(BettingSuccessActivity.this,
							JoinCheckActivity.class);
					startActivity(intent);
				} else if (pageInt == PRESENT) {
					showDialog(0);
					new Thread(new Runnable() {
						public void run() {
							String userno = shellRW
									.getStringValue(ShellRWConstants.USERNO);
							String sessionid = shellRW
									.getStringValue(ShellRWConstants.SESSIONID);
							String phonenum = shellRW
									.getStringValue(ShellRWConstants.PHONENUM);
							BetAndWinAndTrackAndGiftQueryPojo giftQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
							giftQueryPojo.setUserno(userno);
							giftQueryPojo.setSessionid(sessionid);
							giftQueryPojo.setPhonenum(phonenum);
							giftQueryPojo.setPageindex("1");
							giftQueryPojo.setMaxresult("10");
							giftQueryPojo.setType("gift");

							Message msg = new Message();
							String jsonString = GiftQueryInterface
									.getInstance().giftQuery(giftQueryPojo);
							try {
								JSONObject aa = new JSONObject(jsonString);
								String errcode = aa.getString("error_code");
								String message = aa.getString("message");
								if (errcode.equals("0047")) {
									msg.what = 1;
									msg.obj = message;
									handler.sendMessage(msg);
								} else if (errcode.equals("0000")) {
									msg.what = 5;
									msg.obj = jsonString;
									handler.sendMessage(msg);
								}
							} catch (Exception e) {
							}
						}
					}).start();
				} else {
					showDialog(0);
					new Thread(new Runnable() {
						public void run() {
							String userno = shellRW
									.getStringValue(ShellRWConstants.USERNO);
							BetAndWinAndTrackAndGiftQueryPojo betQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
							betQueryPojo.setUserno(userno);
							betQueryPojo.setPageindex("0");
							betQueryPojo.setMaxresult("10");
							betQueryPojo.setType("betList");

							Message msg = new Message();
							String jsonString = BetQueryInterface.getInstance()
									.betQuery(betQueryPojo);
							try {
								JSONObject aa = new JSONObject(jsonString);
								String errcode = aa.getString("error_code");
								String message = aa.getString("message");
								if (errcode.equals("0047")) {
									msg.what = 1;
									msg.obj = message;
									handler.sendMessage(msg);
								} else if (errcode.equals("0000")) {
									msg.what = 4;
									msg.obj = jsonString;
									handler.sendMessage(msg);
								} else {
									msg.what = 1;
									msg.obj = message;
									handler.sendMessage(msg);
								}
							} catch (Exception e) {
								msg.what = 2;
								msg.obj = jsonString;
								handler.sendMessage(msg);
							}
						}
					}).start();
				}

				break;
			}
		}

		/**
		 * 返回投注
		 */
		private void returnToBet() {
			/** modify by pengcx 20130723 start */
			Intent intent = null;
			if (fromInt == JOINCOOPERATION) {
				intent = new Intent(BettingSuccessActivity.this,
						JoinInfoActivity.class);
			} else if (fromInt == NOTICEBALL) {
				intent = new Intent(BettingSuccessActivity.this,
						NoticeActivityGroup.class);
				intent.putExtra("isPosition", true);
				intent.putExtra("position", 1);
			} else {
				if (Constants.LOTNO_SSQ.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Ssq.class);
				} else if (Constants.LOTNO_DLT.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Dlt.class);
				} else if (Constants.LOTNO_FC3D.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Fc3d.class);
				} else if (Constants.LOTNO_11_5.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Dlc.class);
				} else if (Constants.LOTNO_SSC.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Ssc.class);
				} else if (Constants.LOTNO_JCZQ_HUN.equals(lotnoString)
						|| Constants.LOTNO_JCZQ.equals(lotnoString)
						|| Constants.LOTNO_JCZQ_RQSPF.equals(lotnoString)
						|| Constants.LOTNO_JCZQ_ZQJ.equals(lotnoString)
						|| Constants.LOTNO_JCZQ_BF.equals(lotnoString)
						|| Constants.LOTNO_JCZQ_BQC.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							ZqMainActivity.class);
				} else if (Constants.LOTNO_NMK3.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							Nmk3Activity.class);
				} else if (Constants.LOTNO_eleven.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							Eleven.class);
				} else if (Constants.LOTNO_GD_11_5.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							GdEleven.class);
				} else if (Constants.LOTNO_PL3.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, PL3.class);
				} else if (Constants.LOTNO_QLC.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, Qlc.class);
				} else if (Constants.LOTNO_22_5.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							TwentyTwo.class);
				} else if (Constants.LOTNO_PL5.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, PL5.class);
				} else if (Constants.LOTNO_QXC.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this, QXC.class);
				} else if (Constants.LOTNO_ZC.equals(lotnoString)
						|| Constants.LOTNO_JQC.equals(lotnoString)
						|| Constants.LOTNO_LCB.equals(lotnoString)
						|| Constants.LOTNO_SFC.equals(lotnoString)
						|| Constants.LOTNO_RX9.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							FootBallMainActivity.class);
				} else if (Constants.LOTNO_JCLQ.equals(lotnoString)
						|| Constants.LOTNO_JCLQ_RF.equals(lotnoString)
						|| Constants.LOTNO_JCLQ_SFC.equals(lotnoString)
						|| Constants.LOTNO_JCLQ_DXF.equals(lotnoString)
						|| Constants.LOTNO_JCLQ_HUN.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							LqMainActivity.class);
				} else if (Constants.LOTNO_ten.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							TenActivity.class);
				} else if (Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS
						.equals(lotnoString)
						|| Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS
								.equals(lotnoString)
						|| Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL
								.equals(lotnoString)
						|| Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE
								.equals(lotnoString)
						|| Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE
								.equals(lotnoString)) {
					intent = new Intent(BettingSuccessActivity.this,
							BeiJingSingleGameActivity.class);
				} else if (lotnoString.equals(Constants.LOTNO_CQ_ELVEN_FIVE)) {
					intent = new Intent(BettingSuccessActivity.this,
							Cq11Xuan5.class);
				}
			}
			/** modify by pengcx 20130723 end */
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}
}
