package com.ruyicai.activity.buy.beijing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.jc.JoinStartActivityjc;
import com.ruyicai.activity.buy.jc.touzhu.RadioGroupView;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.dialog.MessageDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
	/** 投注详情按钮 */
	private Button bettingDetailButton;
	/** 自由过关按钮 */
	private Button freedomButton;
	/** 多串过关按钮 */
	private Button bunchButton;
	/** 过关方式选择容器布局 */
	private LinearLayout bunchStyleLinearLayout;
	/** 投注信息文本框 */
	private TextView bettingInformationTextView;
	/** 发起合买按钮 */
	private Button cooperationBuyButton;
	/** 确定购买按钮 */
	private Button confirmBuyButton;

	/** 过关选择单选按钮组 */
	private RadioGroupView radioGroupView;

	/** 选择的场次 */
	private int selectedNum;
	/** 对阵字符串 */
	private String againstedString;
	/** 注數 */
	public long bettingNum = 0;
	/** 当前选择的场次集合 */
	private List<String> bettingInfoList;
	/** 最大投注金额 */
	private final int MAXAMT = 20000;
	/** 投注信息类 */
	public BetAndGiftPojo betAndGift = new BetAndGiftPojo();
	private String sessionId;
	private String phonenum;
	private String userno;
	private String laterpartbettingcode;
	private String nowIssueString;
	private String lotnoString;
	private boolean isFreedom = true;

	private MyHandler handler = new MyHandler(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alert_dialog_beijing_touzhu);

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

		mutipleEditText = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		mutipleEditText.setText(String.valueOf(mutipleSeekBar.getProgress()));
		mutipleEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String mutipleString = mutipleEditText.getText().toString();

				if (!mutipleString.equals("")) {
					mutipleSeekBar.setProgress(Integer.valueOf(mutipleString));
				}

				if (mutipleString.length() > 5) {
					mutipleEditText.setText("10000");
					((EditText) mutipleEditText).setSelection(5);
				}

				setBettingInformationShow();
			}
		});

		bettingDetailButton = (Button) findViewById(R.id.alert_dialog_jc_touzhu_btn_info);
		bettingDetailButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());

		freedomButton = (Button) findViewById(R.id.jc_alert_btn_ziyou);
		freedomButton.setBackgroundResource(R.drawable.jc_alert_right_radio_b);
		freedomButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());

		bunchButton = (Button) findViewById(R.id.jc_alert_btn_duochuan);
		bunchButton.setBackgroundResource(R.drawable.jc_alert_left_radio);
		bunchButton
				.setOnClickListener(new BeiJingSingleGameIndentOnClickListener());

		bunchStyleLinearLayout = (LinearLayout) findViewById(R.id.alert_dialog_jc_layout_group);

		bettingInformationTextView = (TextView) findViewById(R.id.alert_dialog_touzhu_text_one);

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
		laterpartbettingcode = getIntent().getStringExtra("laterpartbettingcode");
		nowIssueString = getIntent().getStringExtra("nowIssueString");
		lotnoString = getIntent().getStringExtra("lotno");
		getIntent().getClass();

		radioGroupView = new RadioGroupView(this);
		bunchStyleLinearLayout.removeAllViews();
		addSelectDuoButtons();

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
				.createBeijingDuoView(maxChuanGuan));
	}

	class BeiJingSingleGameIndentOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.buy_zixuan_img_subtract_beishu:
				int progress = mutipleSeekBar.getProgress();
				/*Add by pengcx 20130515 start*/
				if(progress > 1){
					progress--;
				}
				/*Add by pengcx 20130515 end*/
				mutipleSeekBar.setProgress(progress);
				break;
			case R.id.buy_zixuan_img_add_beishu:
				int progress2 = mutipleSeekBar.getProgress();
				progress2++;
				mutipleSeekBar.setProgress(progress2);
				break;
			case R.id.alert_dialog_jc_touzhu_btn_info:
				MessageDialog msgDialog = new MessageDialog(
						BeiJingSingleGameIndentActivity.this, "投注详情",
						againstedString);
				msgDialog.showDialog();
				msgDialog.createFillDialog();
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
					if (isOutMaxAmt()) {
						alertInfo(
								BeiJingSingleGameIndentActivity.this
										.getString(R.string.jc_main_touzhu_alert_text_content),
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
		bunchStyleLinearLayout.addView(radioGroupView
				.createBeijingDanView(maxChuanGuan));
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
		final ProgressDialog progressDialog = UserCenterDialog
				.onCreateDialog(this);
		progressDialog.show();

		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				progressDialog.dismiss();
				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
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

		Log.i("111", bettingCodeString.toString());
		return bettingCodeString.toString();
	}

	private int getBettingMutile() {
		return mutipleSeekBar.getProgress();
	}

	private boolean isOutMaxAmt() {
		long bettingAccount = getBettingAccount();
		if (bettingAccount > MAXAMT || bettingAccount < 0) {
			return true;
		} else {
			return false;
		}

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

	public long getBettingNum(int select) {
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingInfoList.size(); i++) {
			isDanList.add(false);
		}

		long bettingNum = PublicMethod.getAllAmt(bettingInfoList, select,
				isDanList, 0);

		return bettingNum;
	}

	public long getChuanBettingNum(int selectnum, int select) {
		List<Boolean> isDanList = new ArrayList<Boolean>();
		for (int i = 0; i < bettingInfoList.size(); i++) {
			isDanList.add(false);
		}

		long bettingNum = PublicMethod.getDouZhushu(selectnum, bettingInfoList,
				select, isDanList, 0);
		return bettingNum;
	}

	public void setBettingInformationShow() {
		String bettingInfoString = "注数：" + bettingNum + "注   " + "倍数："
				+ getBettingMutile() + "倍   " + "金额：" + getBettingAccount()
				+ "元";
		bettingInformationTextView.setText(bettingInfoString);
	}

	public long getBettingAccount() {
		return bettingNum * mutipleSeekBar.getProgress() * 2;
	}

	@Override
	public void errorCode_0000() {
		/**Add by pengcx 20130516 start*/
		BeiJingSingleGameActivity.isBettingReturn = true;
		/**Add by pengcx 20130516 end*/
		PublicMethod.showDialog(BeiJingSingleGameIndentActivity.this);
	}

	@Override
	public void errorCode_000000() {

	}

	@Override
	public Context getContext() {
		return this;
	}
}
