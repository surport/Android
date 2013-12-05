package com.ruyicai.activity.buy.beijing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.jc.JoinStartActivityjc;
import com.ruyicai.activity.buy.jc.oddsprize.JCPrizePermutationandCombination;
import com.ruyicai.activity.buy.jc.touzhu.RadioGroupView;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;

import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BeiJingSingleGameIndentActivity extends Activity implements
		HandlerMsg {
	/** 减少倍数按钮 */
	private ImageButton reduceMutipleButton;
	/** 倍数滑动条 */
	private SeekBar mutipleSeekBar;
	/** 增加倍数按钮 */
	private ImageButton addMutipleButton;
	/** 倍数输入框 */
	private EditText mutipleEditText;
	/** 自由过关按钮 */
	private Button freedomButton;
	/** 多串过关按钮 */
	private Button bunchButton;
	/** 过关方式选择容器布局 */
	private LinearLayout bunchStyleLinearLayout;
	/** 发起合买按钮 */
	private Button cooperationBuyButton;
	/** 确定购买按钮 */
	private Button confirmBuyButton;

	/** add by pengcx 20130703 start */
	private TextView lotoTypeTextView;
	private TextView gameNumTextView;
	private TextView betNumTextView;
	private TextView moneyTextView;
	private TextView predictMoneyTextView;
	private TextView schemeTextView;
	private TextView schemeDetailTextView;
	private ImageView upDownImageView;
	private RelativeLayout schemeRelativeLayout;
	private LinearLayout schemeDetailLinearLayout;
	/** add by pengcx 20130703 end */

	/** 过关选择单选按钮组 */
	private RadioGroupView radioGroupView;

	/** 选择的场次 */
	private int selectedNum;
	private int selectedDanNum;
	/** 对阵字符串 */
	private String againstedString;
	/** 注數 */
	public long bettingNum = 0;
	/** 当前选择的场次集合 */
	private List<String> bettingInfoList;
	private List<String> bettingDanList;

	/** 投注信息类 */
	public BetAndGiftPojo betAndGift = new BetAndGiftPojo();

	private String sessionId;
	private String phonenum;
	private String userno;
	private String laterpartbettingcode;
	private String nowIssueString;
	private String lotnoString;
	private boolean isFreedom = true;

	/** add by pengcx 20130708 start */
	/** 最高预计奖金 */
	public static double freedomMaxprize;
	public static double freedomMinprize;
	private StringBuffer predictStringBuffer;
	/** 最低预计奖金 */
	/** add by pengcx 20130708 end */
	private MyHandler handler = new MyHandler(this);

	public int getSelectedDanNum() {
		return selectedDanNum;
	}

	public void setSelectedDanNum(int selectedDanNum) {
		this.selectedDanNum = selectedDanNum;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog_beijing_touzhu);
		mutipleEditText = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		reduceMutipleButton = (ImageButton) findViewById(R.id.buy_zixuan_img_subtract_beishu);
		reduceMutipleButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());
		addMutipleButton = (ImageButton) findViewById(R.id.buy_zixuan_img_add_beishu);
		addMutipleButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());
		mutipleSeekBar = (SeekBar) findViewById(R.id.buy_jc_zixuan_seek_beishu);
		mutipleSeekBar
				.setOnSeekBarChangeListener(new BeiJingSingleGameIndentOnSeekBarChangeListener());

		mutipleSeekBar.setProgress(1);

		mutipleEditText.setText(String.valueOf(mutipleSeekBar.getProgress()));
		mutipleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String mutipleString = mutipleEditText.getText().toString();

				if (!mutipleString.equals("")) {
					mutipleSeekBar.setProgress(Integer.valueOf(CheckUtil.isNull(mutipleString)));
				}

				if (mutipleString.length() > 5) {
					mutipleEditText.setText("10000");
					((EditText) mutipleEditText).setSelection(5);
				}

				setBettingInformationShow();
			}
		});

		freedomButton = (Button) findViewById(R.id.jc_alert_btn_ziyou);
		freedomButton.setBackgroundResource(R.drawable.jc_alert_right_radio_b);
		freedomButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());
		bunchButton = (Button) findViewById(R.id.jc_alert_btn_duochuan);
		bunchButton.setBackgroundResource(R.drawable.jc_alert_left_radio);
		bunchButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());
		selectedDanNum = getIntent().getIntExtra("selecteddannum", 0);
//		if (selectedDanNum > 0) {
//			bunchButton.setVisibility(View.GONE);
//		}

		bunchStyleLinearLayout = (LinearLayout) findViewById(R.id.alert_dialog_jc_layout_group);

		cooperationBuyButton = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		cooperationBuyButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());
		confirmBuyButton = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		confirmBuyButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());

		selectedNum = getIntent().getIntExtra("selectedgamenum", 0);
		againstedString = getIntent().getStringExtra("selectedagainst");
		bettingInfoList = getIntent().getStringArrayListExtra(
				"selectedeventclicknum");
		bettingDanList = getIntent().getStringArrayListExtra(
				"selecteddanclicknum");
		laterpartbettingcode = getIntent().getStringExtra(
				"laterpartbettingcode");
		nowIssueString = getIntent().getStringExtra("nowIssueString");
		lotnoString = getIntent().getStringExtra("lotno");
		getIntent().getClass();

		radioGroupView = new RadioGroupView(this);
		bunchStyleLinearLayout.removeAllViews();
		addSelectDuoButtons();

		/** add by pengcx 20130703 start */
		lotoTypeTextView = (TextView) findViewById(R.id.alert_dialog_jc_lotnotype);
		gameNumTextView = (TextView) findViewById(R.id.alert_dialog_jc_gamenum);
		betNumTextView = (TextView) findViewById(R.id.alert_dialog_jc_betnum);
		moneyTextView = (TextView) findViewById(R.id.alert_dialog_jc_money);
		predictMoneyTextView = (TextView) findViewById(R.id.alert_dialog_jc_predictmoney);
		schemeTextView = (TextView) findViewById(R.id.alert_dialog_touzhu_alert_scheme);
//		schemeTextView.setText(againstedString);
		schemeTextView.setText(Html.fromHtml(againstedString));
		schemeDetailTextView = (TextView) findViewById(R.id.alert_dialog_touzhu_alert_textview_schemedetail);
		upDownImageView = (ImageView) findViewById(R.id.alert_dialog_touzhu_updown);
		schemeRelativeLayout = (RelativeLayout) findViewById(R.id.alert_dialog_touzhu_linear_qihao_beishu);
		schemeDetailLinearLayout = (LinearLayout) findViewById(R.id.alert_dialog_touzhu_alert_schemedetail);
//		schemeDetailTextView.setText(againstedString);
		schemeDetailTextView.setText(Html.fromHtml(againstedString));
		schemeRelativeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**modify by pengcx 20130806 start*/
				int visibility = schemeDetailLinearLayout.getVisibility();
				if (visibility == View.VISIBLE) {
					schemeDetailLinearLayout.setVisibility(View.GONE);
					// schemeTextView.setText(againstedString);
					schemeTextView.setVisibility(View.VISIBLE);
					upDownImageView.setImageResource(R.drawable.down_icon);
				} else {
					schemeDetailLinearLayout.setVisibility(View.VISIBLE);
					upDownImageView.setImageResource(R.drawable.up_icon);
					// schemeTextView.setText("");
					schemeTextView.setVisibility(View.INVISIBLE);
				}
				/**modify by pengcx 20130806 end*/
			}
		});
		lotoTypeTextView.setText(PublicMethod.toLotno(lotnoString));
		/** add by pengcx 20130703 end */

		/** add by pengcx 20130709 start */
		freedomMaxprize = 0;
		freedomMinprize = 0;
		/** add by pengcx 20130709 end */
		setBettingInformationShow();

		handler.setBetAndGift(betAndGift);
	}

	private void addSelectDuoButtons() {
		int maxChuanGuan = selectedNum;
		switch (BeiJingSingleGameActivity.playMethodType) {
		case WINTIELOSS:
			break;
		case TOTALGOALS:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;
		case OVERALL:
			if (selectedNum > 3) {
				maxChuanGuan = 3;
			}
			break;
		case HALFTHEAUDIENCE:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;
		case UPDOWNSINGLEDOUBLE:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;

		}
		bunchStyleLinearLayout.addView(radioGroupView
				.createBeijingDuoView(maxChuanGuan,selectedNum));
	}

	class BeiJingSingleGameIndentOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buy_zixuan_img_subtract_beishu:
				int progress = mutipleSeekBar.getProgress();
				/* Add by pengcx 20130515 start */
				if (progress > 1) {
					progress--;
				}
				/* Add by pengcx 20130515 end */
				mutipleSeekBar.setProgress(progress);
				break;
			case R.id.buy_zixuan_img_add_beishu:
				int progress2 = mutipleSeekBar.getProgress();
				progress2++;
				mutipleSeekBar.setProgress(progress2);
				break;
			case R.id.jc_alert_btn_ziyou:
				freedomButton
						.setBackgroundResource(R.drawable.jc_alert_right_radio_b);
				bunchButton
						.setBackgroundResource(R.drawable.jc_alert_left_radio);
				bunchStyleLinearLayout.removeAllViews();
				addSelectDuoButtons();
				isFreedom = true;
				bettingNum = 0;
				/** add by pengcx 20130709 start */
				freedomMaxprize = 0;
				freedomMinprize = 0;
				/** add by pengcx 20130709 end */
				setBettingInformationShow();
				break;
			case R.id.jc_alert_btn_duochuan:
				freedomButton
						.setBackgroundResource(R.drawable.jc_alert_right_radio);
				bunchButton
						.setBackgroundResource(R.drawable.jc_alert_left_radio_b);
				bunchStyleLinearLayout.removeAllViews();
				addSelectDanButtons();
				isFreedom = false;
				bettingNum = 0;
				/** add by pengcx 20130709 start */
				freedomMaxprize = 0;
				freedomMinprize = 0;
				/** add by pengcx 20130709 end */
				setBettingInformationShow();
				break;
			case R.id.alert_dialog_touzhu_button_ok:
				bettingOrCooperation(true);
				break;
			case R.id.alert_dialog_touzhu_button_cancel:
				bettingOrCooperation(false);
				break;
			}
		}

		private void bettingOrCooperation(boolean isBetting) {
			RWSharedPreferences pre = new RWSharedPreferences(
					BeiJingSingleGameIndentActivity.this, "addInfo");
			sessionId = pre.getStringValue("sessionid");
			phonenum = pre.getStringValue("phonenum");
			userno = pre.getStringValue("userno");
			if (bettingNum == 0) {
				Toast.makeText(BeiJingSingleGameIndentActivity.this, "请选择过关方式",
						Toast.LENGTH_SHORT).show();
			} else {
				if (userno == null || userno.equals("")) {
					Intent intentSession = new Intent(
							BeiJingSingleGameIndentActivity.this,
							UserLogin.class);
					BeiJingSingleGameIndentActivity.this
							.startActivityForResult(intentSession, 0);
				} else {
					if (bettingNum > 100000/*isOutMaxAmt()*/) {
						alertInfo(
								BeiJingSingleGameIndentActivity.this
										.getString(R.string.jc_main_touzhu_alert_text_content_zhushu),
								BeiJingSingleGameIndentActivity.this
										.getString(R.string.jc_main_touzhu_alert_text_title));
					} else {
						initBettingInfo();
						if (isBetting) {
							Betting();
						} else {
							turnToCooperation();
						}
					}
				}
			}
		}
	}

	private void addSelectDanButtons() {
		int maxChuanGuan = selectedNum;
		switch (BeiJingSingleGameActivity.playMethodType) {
		case WINTIELOSS:
			break;
		case TOTALGOALS:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;
		case OVERALL:
			if (selectedNum > 3) {
				maxChuanGuan = 3;
			}
			break;
		case HALFTHEAUDIENCE:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;
		case UPDOWNSINGLEDOUBLE:
			if (selectedNum > 6) {
				maxChuanGuan = 6;
			}
			break;

		}
		bunchStyleLinearLayout.addView(radioGroupView.createBeijingDanView(
				maxChuanGuan, selectedNum));
	}

	public void turnToCooperation() {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betAndGift);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(BeiJingSingleGameIndentActivity.this,
				JoinStartActivityjc.class);
		intent.putExtra("info", byteStream.toByteArray());
		startActivity(intent);
	}

	public void Betting() {
		Controller.getInstance(BeiJingSingleGameIndentActivity.this).doBettingAction(handler, betAndGift);
	}

	public void alertInfo(String string, String title) {
		Builder dialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();

	}

	public void initBettingInfo() {
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");
		betAndGift.setAmount("" + getBettingAccount() * 100);
		betAndGift.setLotmulti("" + getBettingMutile());
		betAndGift.setBet_code(getBettingCode());
		betAndGift.setIsSellWays("1");
		betAndGift.setBatchnum("1");
		betAndGift.setBatchcode(nowIssueString);
		betAndGift.setLotno(lotnoString);
		/** add by pengcx 20130709 start */
		betAndGift.setPredictMoney(predictStringBuffer.toString());
		/** add by pengcx 20130709 end */
	}

	private String getBettingCode() {
		StringBuffer bettingCodeString = new StringBuffer();
		boolean isFirst = true;

		if (isFreedom) {
			for (CheckBox beijingCheckBox : radioGroupView.beijingChecks) {
				if (beijingCheckBox.isChecked()) {
					if (!isFirst) {
						bettingCodeString.append("!");
					} else {
						isFirst = false;
					}
					/** modify by pengcx 20130712 start */
					if (selectedDanNum > 0) {
						switch (beijingCheckBox.getId()) {
						case 0:
							bettingCodeString.append("600@");
							break;
						case 1:
							bettingCodeString.append("602@");
							break;
						case 2:
							bettingCodeString.append("603@");
							break;
						case 3:
							bettingCodeString.append("604@");
							break;
						case 4:
							bettingCodeString.append("605@");
							break;
						case 5:
							bettingCodeString.append("606@");
							break;
						case 6:
							bettingCodeString.append("607@");
							break;
						case 7:
							bettingCodeString.append("608@");
							break;
						case 8:
							bettingCodeString.append("209@");
							break;
						case 9:
							bettingCodeString.append("210@");
							break;
						case 10:
							bettingCodeString.append("211@");
							break;
						case 11:
							bettingCodeString.append("212@");
							break;
						case 12:
							bettingCodeString.append("213@");
							break;
						case 13:
							bettingCodeString.append("214@");
							break;
						case 14:
							bettingCodeString.append("215@");
							break;
						}
					} else {
						switch (beijingCheckBox.getId()) {
						case 0:
							bettingCodeString.append("500@");
							break;
						case 1:
							bettingCodeString.append("502@");
							break;
						case 2:
							bettingCodeString.append("503@");
							break;
						case 3:
							bettingCodeString.append("504@");
							break;
						case 4:
							bettingCodeString.append("505@");
							break;
						case 5:
							bettingCodeString.append("506@");
							break;
						case 6:
							bettingCodeString.append("507@");
							break;
						case 7:
							bettingCodeString.append("508@");
							break;
						case 8:
							bettingCodeString.append("109@");
							break;
						case 9:
							bettingCodeString.append("110@");
							break;
						case 10:
							bettingCodeString.append("111@");
							break;
						case 11:
							bettingCodeString.append("112@");
							break;
						case 12:
							bettingCodeString.append("113@");
							break;
						case 13:
							bettingCodeString.append("114@");
							break;
						case 14:
							bettingCodeString.append("115@");
							break;
						}
					}
					/** modify by pengcx 20130712 end */
					bettingCodeString.append(laterpartbettingcode);
					bettingCodeString
							.append("_")
							.append(mutipleSeekBar.getProgress())
							.append("_200_")
							.append(getBettingNum(beijingCheckBox.getId() + 1) * 200);
				}

			}
		} else {
			for (RadioButton radioButton : radioGroupView.radioBtns) {
				if (radioButton.isChecked()) {
					if (!isFirst) {
						bettingCodeString.append("!");
					} else {
						isFirst = false;
					}

					/** modify by pengcx 20130712 start */
					if (selectedDanNum > 0) {
						switch (radioButton.getId()) {
						case 0:
							bettingCodeString.append("609@");
							break;
						case 1:
							bettingCodeString.append("627@");
							break;
						case 2:
							bettingCodeString.append("611@");
							break;
						case 3:
							bettingCodeString.append("640@");
							break;
						case 4:
							bettingCodeString.append("629@");
							break;
						case 5:
							bettingCodeString.append("614@");
							break;
						case 6:
							bettingCodeString.append("645@");
							break;
						case 7:
							bettingCodeString.append("641@");
							break;
						case 8:
							bettingCodeString.append("632@");
							break;
						case 9:
							bettingCodeString.append("618@");
							break;
						case 10:
							bettingCodeString.append("650@");
							break;
						case 11:
							bettingCodeString.append("646@");
							break;
						case 12:
							bettingCodeString.append("643@");
							break;
						case 13:
							bettingCodeString.append("636@");
							break;
						case 14:
							bettingCodeString.append("623@");
							break;
						}
					} else {
						switch (radioButton.getId()) {
						case 0:
							bettingCodeString.append("509@");
							break;
						case 1:
							bettingCodeString.append("527@");
							break;
						case 2:
							bettingCodeString.append("511@");
							break;
						case 3:
							bettingCodeString.append("540@");
							break;
						case 4:
							bettingCodeString.append("529@");
							break;
						case 5:
							bettingCodeString.append("514@");
							break;
						case 6:
							bettingCodeString.append("545@");
							break;
						case 7:
							bettingCodeString.append("541@");
							break;
						case 8:
							bettingCodeString.append("532@");
							break;
						case 9:
							bettingCodeString.append("518@");
							break;
						case 10:
							bettingCodeString.append("550@");
							break;
						case 11:
							bettingCodeString.append("546@");
							break;
						case 12:
							bettingCodeString.append("543@");
							break;
						case 13:
							bettingCodeString.append("536@");
							break;
						case 14:
							bettingCodeString.append("523@");
							break;
						}
					}
					/** modify by pengcx 20130712 end */
					bettingCodeString.append(laterpartbettingcode);
					bettingCodeString
							.append("_")
							.append(mutipleSeekBar.getProgress())
							.append("_200_")
							.append(radioGroupView
									.getBeijingRadioZhu(radioButton.getText()
											.toString()) * 200);
				}
			}
		}

		return bettingCodeString.toString();
	}

	private int getBettingMutile() {
		return mutipleSeekBar.getProgress();
	}

	class BeiJingSingleGameIndentOnSeekBarChangeListener implements
			OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (progress < 1) {
				seekBar.setProgress(1);
			}

			if (progress >= 1 && progress <= 10000) {
				mutipleEditText.setText(String.valueOf(progress));
				mutipleEditText.setSelection(String.valueOf(progress).length());
			}
			setBettingInformationShow();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	}

	/**
	 * 自由过关
	 * 
	 * @param select
	 * @return
	 */
	public long getBettingNum(int select) {
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingDanList.size(); i++) {
			if (bettingDanList.get(i).equals("true")) {
				isDanList.add(true);
			} else {
				isDanList.add(false);
			}
		}
		long bettingNum = PublicMethod.getAllAmt(bettingInfoList, select,
				isDanList, selectedDanNum);

		return bettingNum;
	}

	/**
	 * 多串gu
	 * 
	 * @param selectnum
	 * @param select
	 * @return
	 */
	public long getChuanBettingNum(int selectnum, int select) {
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingDanList.size(); i++) {
			if (bettingDanList.get(i).equals("true")) {
				isDanList.add(true);
			} else {
				isDanList.add(false);
			}
		}
		long bettingNum = PublicMethod.getDouZhushu(selectnum, bettingInfoList,
				select, isDanList, selectedDanNum);
		return bettingNum;
	}

	public void setBettingInformationShow() {
		/** add by pengcx 20130703 start */
		betNumTextView.setText("共" + bettingNum + "注");
		moneyTextView.setText("共" + getBettingAccount() + "元");
		gameNumTextView.setText("共" + bettingInfoList.size() + "场");
		predictMoneyTextView.setText(getPredictMoneyTextString());
		/** add by pengcx 20130703 end */
	}

	/**
	 * 获取预计奖金字符串
	 * 
	 * @return 预计奖金字符串
	 */
	public String getPredictMoneyTextString() {
		double max = freedomMaxprize * getBettingMutile();
		double min = freedomMinprize * getBettingMutile();

		predictStringBuffer = new StringBuffer();
		predictStringBuffer.append(PublicMethod.formatStringToTwoPoint(min))
				.append("元~").append(PublicMethod.formatStringToTwoPoint(max))
				.append("元");
		return predictStringBuffer.toString();
	}

	public long getBettingAccount() {
		return bettingNum * mutipleSeekBar.getProgress() * 2;
	}

	@Override
	public void errorCode_0000() {
		/** Add by pengcx 20130516 start */
		BeiJingSingleGameActivity.isBettingReturn = true;
		/** Add by pengcx 20130516 end */

		/** Add by pengcx 20130605 start */
		Intent intent = new Intent(this, BettingSuccessActivity.class);
		intent.putExtra("page", BettingSuccessActivity.BETTING);
		intent.putExtra("lotno", betAndGift.getLotno());
		intent.putExtra("amount", betAndGift.getAmount());
		startActivity(intent);
		/** Add by pengcx 20130605 end */
	}

	@Override
	public void errorCode_000000() {

	}

	@Override
	public Context getContext() {
		return this;
	}

	/** add by pengcx 20130708 start */
	/**
	 * 获取单关投注的最大和最小奖金
	 * 
	 * @param muti
	 * @return
	 */
	public double computeDanGuanMaxPrize() {
		return JCPrizePermutationandCombination
				.getBeijingDanGuanMaxPrize(BeiJingSingleGameActivity.newSelectedSPList);
	}

	public double computeDanGuanMinPrize() {
		return JCPrizePermutationandCombination
				.getBeijingDanGuanMinPrize(BeiJingSingleGameActivity.newSelectedSPList);
	}

	/**
	 * 计算多场最大预计奖金
	 * 
	 * @param select
	 * @return
	 */
	public double computeDuoGuanMaxPrize(int team, int select) {
		double max = 0;
		int isDanNum = 0;
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingDanList.size(); i++) {
			if (bettingDanList.get(i).equals("true")) {
				isDanList.add(true);
				isDanNum ++;
			} else {
				isDanList.add(false);
			}
		}
		if (isFreedom) {
			max = JCPrizePermutationandCombination
					.getBeiJingFreedomGuoGuanMaxPrize(
							BeiJingSingleGameActivity.newSelectedSPList,
							select, isDanList, isDanNum);
		} else {
			max = JCPrizePermutationandCombination.getBeiJingDuoMaxPrize(team,
					BeiJingSingleGameActivity.newSelectedSPList, select,
					isDanList, isDanNum);
		}

		return max;
	}

	/**
	 * 计算多场最小预计奖金
	 * 
	 * @param select
	 * @return
	 */
	public double computeDuoGuanMinPrize(int team, int select) {
		double min = 0;
		int isDanNum = 0;
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingDanList.size(); i++) {
			if (bettingDanList.get(i).equals("true")) {
				isDanList.add(true);
				isDanNum ++;
			} else {
				isDanList.add(false);
			}
		}
		
		if (isFreedom) {
			min = JCPrizePermutationandCombination
					.getBeijingFreedomGuoGuanMixPrize(
							BeiJingSingleGameActivity.newSelectedSPList,
							select, isDanList, isDanNum);
		} else {
			min = JCPrizePermutationandCombination.getBeiJingDuoMinPrize(team,
					BeiJingSingleGameActivity.newSelectedSPList, select,
					isDanList, isDanNum);
		}

		return min;
	}
	/** add by pengcx 20130708 end */
}
