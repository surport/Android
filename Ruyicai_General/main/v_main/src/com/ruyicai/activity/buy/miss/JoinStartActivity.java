/**
 * 
 */
package com.ruyicai.activity.buy.miss;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.TouzhuBaseActivity;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.join.JoinStarShare;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.JoinStartInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 发起合买
 * 
 * @author Administrator
 * 
 */
public class JoinStartActivity extends TouzhuBaseActivity implements
		HandlerMsg, OnCheckedChangeListener, OnSeekBarChangeListener {

	private BetAndGiftPojo betAndGift;// 投注信息类
	private TextView titleText;
	private TextView atmText;
	private TextView zhuText;
	private TextView beiText;
	private TextView renText;
	private TextView baoText;
	private EditText buyEdit;
	private EditText minEdit;
	private EditText safeEdit;
	private EditText descriptionEdit;
	private Spinner deductSpinner;
	private String baoTitle[] = { "是", "否" };
	private String openTitle[] = { "对所有人公开", "对跟单者立即公开", "对所有人截止后公开",
			"对跟单者截止后公开", "保密" };
	private RadioGroup baoRadioGroup;
	private RadioGroup openRadioGroup;
	private long allAtm;
	private String commisionRation = "1";
	private String visible = "0";
	String message;
	JSONObject obj;
	String phonenum, sessionId, userno;
	protected ProgressDialog progressdialog;
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	JoinStartActivityHandler handler = new JoinStartActivityHandler(this);// 自定义handler
	TextView textAlert;
	TextView textZhuma;
	TextView textTitle;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected EditText mTextBeishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	TextView zhushu;
	TextView jine;
	TextView caizhong;
	private boolean toLogin = false;
	public boolean isTouzhu = false;// 是否投注
	public static String type = "";
	private AddViewMiss addviewmiss;
	private Controller controller = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.order_hemai);
		getInfo();
		allAtm = iProgressQishu * addviewmiss.getAllAmt() * iProgressBeishu;
		init();
		initRadioGroup();
	}

	private void getNetIssue() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				final String issue = Controller.getInstance(
						JoinStartActivity.this).toNetIssue(
						betAndGift.getLotno());
				handler.post(new Runnable() {

					@Override
					public void run() {
						issueText.setText("第" + issue + "期");
						betAndGift.setBatchcode(issue);
					}
				});
			}
		}).start();
	}

	public void init() {
		if (betAndGift.isZhui()) {
			initZhuiJia();
		}
		zhushu = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_zhushu);
		jine = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_jine);
		caizhong = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		issueText = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		textZhuma = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitle = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		renText = (TextView) findViewById(R.id.layout_join_text_rengou);
		baoText = (TextView) findViewById(R.id.layout_join_text_baodi);
		buyEdit = (EditText) findViewById(R.id.layout_join_edit_rengou);
		safeEdit = (EditText) findViewById(R.id.layout_join_edit_baodi);
		getNetIssue();
		getTouzhuAlert();
		if (type.equals("zc")) {
			textTitle.setText("注码：共有1笔投注");
			textZhuma.setText(betAndGift.getBet_code());
		} else {
			initImageView();
			CodeInfoMiss code = addviewmiss.getCodeList().get(
					addviewmiss.getSize() - 1);
			code.setTextCodeColor(textZhuma, code.getLotoNo(),
					code.getTouZhuType());
			textTitle.setText("注码：" + "共有" + addviewmiss.getSize() + "笔投注");
			codeInfo = (Button) findViewById(R.id.alert_dialog_touzhu_btn_look_code);
			isCodeText(codeInfo);
			codeInfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					addviewmiss.createCodeInfoDialog(JoinStartActivity.this);
					addviewmiss.showDialog();
				}
			});
		}
		Button cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (OrderDetails.isAlert) {
					if (OrderDetails.fromInt == BettingSuccessActivity.NOTICEBALL) {
						alertExit("退出该页面会清空已选择的投注号码，是否将已选择的投注号码保存？");
					} else {
						alertExit(getString(R.string.buy_alert_exit_detail));
					}

				} else {
					finish();
				}
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(
						JoinStartActivity.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(JoinStartActivity.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					isJoin();
				}
			}
		});
		titleText = (TextView) findViewById(R.id.layout_join_text_title);
		minEdit = (EditText) findViewById(R.id.layout_join_edit_gendan);
		descriptionEdit = (EditText) findViewById(R.id.layout_join_edit_description);

		buyEdit.setText("1");
		safeEdit.setText("1");
		minEdit.setText("1");
		deductSpinner = (Spinner) findViewById(R.id.layout_join_start_spinner);
		deductSpinner.setSelection(9);
		// 初始化spinner
		deductSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				commisionRation = (String) deductSpinner.getSelectedItem();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
		// 赋值
		onEditTextClik();
		renText.setText("占总额"
				+ progress(isNull(buyEdit.getText().toString()), "" + allAtm)
				+ "%");// 总金额
		baoText.setText("占总额"
				+ progress(isNull(safeEdit.getText().toString()), "" + allAtm)
				+ "%");// 总金额
	}

	private void isCodeText(Button codeInfo) {
		if (addviewmiss.getSize() > 1) {
			codeInfo.setVisibility(Button.VISIBLE);
		} else {
			codeInfo.setVisibility(Button.GONE);
		}
	}

	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert() {
		if (type.equals("zc")) {
			zhushu.setText(betAndGift.getZhushu() + "注     ");
			jine.setText(iProgressQishu
					* (Integer.valueOf(CheckUtil.isNull(betAndGift.getAmount())) / 100)
					* iProgressBeishu + "元");
		} else {
			zhushu.setText(addviewmiss.getAllZhu() + "注     ");
			jine.setText(iProgressQishu * addviewmiss.getAllAmt()
					* iProgressBeishu + "元");
		}
		// return "注数："
		// + addviewmiss.getAllZhu() + "注     "
		// + "金额：" +
		// + iProgressQishu * addviewmiss.getAllAmt() * iProgressBeishu
		// + "元";
	}

	public void onEditTextClik() {
		buyEdit.addTextChangedListener(new EditTextWatcher(buyEdit));
		minEdit.addTextChangedListener(new EditTextWatcher(minEdit));
		safeEdit.addTextChangedListener(new EditTextWatcher(safeEdit));
	}

	public void setEditText() {
		long buyInt = Integer.parseInt(isNull(buyEdit.getText().toString()));
		long safeInt = Integer.parseInt(isNull(safeEdit.getText().toString()));
		long minInt = Integer.parseInt(isNull(minEdit.getText().toString()));
		if (buyInt > allAtm) {
			buyInt = allAtm;
			buyEdit.setText("" + buyInt);
		}
		if (safeInt > allAtm - buyInt) {
			safeInt = allAtm - buyInt;
			safeEdit.setText("" + safeInt);
		}
		if (minInt > allAtm - buyInt) {
			minInt = allAtm - buyInt;
			minEdit.setText("" + minInt);
		}
	}

	/**
	 * 全额保底方法
	 */
	public void setAllSafeEdit(boolean isSafe) {
		if (isSafe) {
			int buyInt = Integer.parseInt(isNull(buyEdit.getText().toString()));
			safeEdit.setText("" + (allAtm - buyInt));
			safeEdit.setEnabled(false);
		} else {
			safeEdit.setText("0");
			safeEdit.setEnabled(true);
		}

	}

	/**
	 * 计算百分比
	 * 
	 * @param amt
	 * @param allAmt
	 * @return
	 */
	public String progress(String amt, String allAmt) {
		if (allAmt.equals("0")) {
			return "0";
		} else {
			float amount = Integer.parseInt(amt);
			float allAmount = Integer.parseInt(allAmt);
			float progress = (amount / allAmount) * 100;
			NumberFormat formatter = NumberFormat.getNumberInstance();
			formatter.setMaximumFractionDigits(1);
			formatter.setMinimumFractionDigits(1);
			return formatter.format(progress);
		}
	}

	/**
	 * 两数相减
	 * 
	 * @param allAmt
	 * @param amt
	 * @return
	 */
	public String leavMount(String allAmt, String amt) {
		String amtStr = "";
		amtStr = Integer.toString(Integer.parseInt(isNull(allAmt))
				- Integer.parseInt(isNull(amt)));
		return amtStr;
	}

	/**
	 * 判断字符串是否是空值
	 * 
	 */
	public String isNull(String str) {
		if (str == null || str.equals("")) {
			return "0";
		} else {
			return str;
		}

	}

	/**
	 * 初始化单选按钮组
	 */
	public void initRadioGroup() {
		baoRadioGroup = (RadioGroup) findViewById(R.id.buy_join_radiogroup_baodi);
		for (int i = 0; i < baoTitle.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(baoTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			baoRadioGroup.addView(radio);

		}
		baoRadioGroup.setOnCheckedChangeListener(this);
		baoRadioGroup.check(1);
		openRadioGroup = (RadioGroup) findViewById(R.id.buy_join_radiogroup_open);
		for (int i = 0; i < openTitle.length; i++) {
			RadioButton radio = new RadioButton(this);
			radio.setText(openTitle[i]);
			radio.setTextColor(Color.BLACK);
			radio.setTextSize(13);
			radio.setId(i);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setPadding(Constants.PADDING, 0, 10, 0);
			openRadioGroup.addView(radio);

		}
		openRadioGroup.setOnCheckedChangeListener(this);
		openRadioGroup.check(0);
	}

	/**
	 * 从上一个页面获取信息
	 */
	public void getInfo() {
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		addviewmiss = app.getAddviewmiss();
	}

	/**
	 * 对合买对象进行赋值
	 */
	public void setPojo() {
		RWSharedPreferences pre = new RWSharedPreferences(
				JoinStartActivity.this, "addInfo");
		sessionId = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		betAndGift.setBettype("startcase");
		betAndGift.setTotalAmt(betAndGift.getAmount());
		if (betAndGift.getIssuper().equals("0")) {
			betAndGift.setOneAmount("300");
		} else {
			betAndGift.setOneAmount("200");
		}
		betAndGift.setSafeAmt(isNull(PublicMethod.toFen(isNull(safeEdit
				.getText().toString()))));
		betAndGift.setBuyAmt(isNull(PublicMethod.toFen(isNull(buyEdit.getText()
				.toString()))));
		betAndGift.setMinAmt(PublicMethod.toFen(isNull(minEdit.getText()
				.toString())));
		betAndGift.setCommisionRation(commisionRation);
		betAndGift.setVisibility(visible);
		betAndGift.setDescription(descriptionEdit.getText().toString());
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）
		betAndGift.setAmount("" + addviewmiss.getAllAmt() * iProgressBeishu
				* 100);
		betAndGift.setIsSellWays("1");

		/** add by yejc 20130510 start */
		if (isFromTrackQuery) {
			betAndGift.setBet_code(betAndGift.getBet_code());
		} else {
			betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,
					betAndGift.getAmt() * 100));
		}
		/** add by yejc 20130510 start */
	}

	/**
	 * 是否发起合买
	 */
	public void isJoin() {
		int buyInt = Integer.parseInt(isNull(buyEdit.getText().toString()));
		int safeInt = Integer.parseInt(isNull(safeEdit.getText().toString()));
		long minInt = Integer.parseInt(isNull(minEdit.getText().toString()));
		if (buyInt == 0 && safeInt == 0) {
			Toast.makeText(this, "认购金额和保底金额不能都为0！", Toast.LENGTH_SHORT).show();
		} else if (allAtm - buyInt > 0 && minInt == 0) {
			Toast.makeText(this, "最低跟单至少为1元！", Toast.LENGTH_SHORT).show();
		} else {
			joinNet();
		}
		if (minInt > allAtm - buyInt) {
			minInt = allAtm - buyInt;
			minEdit.setText("" + minInt);
		}
	}

	/**
	 * 发起合买联网
	 * 
	 */
	public void joinNet() {
		setPojo();
		Controller.getInstance(JoinStartActivity.this).doBettingJoinAction(handler, betAndGift);
	}

	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.buy_join_radiogroup_baodi:
			switch (checkedId) {
			case 0:// 是
				setAllSafeEdit(true);
				break;
			case 1:// 否
				setAllSafeEdit(false);
				break;
			}
		case R.id.buy_join_radiogroup_open:
			switch (checkedId) {
			case 0:// 完全公开
				visible = "0";
				break;
			case 1:// 立即公开
				visible = "3";
				break;
			case 2:// 截止后公开
				visible = "2";
				break;
			case 3:// 跟单者公开
				visible = "4";
				break;
			case 4:// 保密
				visible = "1";
				break;
			}
		}
	}

	/**
	 * 从ShellRWSharesPreferences中获取phonenum 、sessionid 和userno
	 */
	/**
	 * 初始化倍数和期数进度条
	 * 
	 * @param view
	 */
	public void initImageView() {
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_beishu);
		mTextBeishu = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		/** add by pengcx 20130722 start */
		mTextBeishu.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 1 && s.charAt(0) == '0') {
					Integer integer = Integer.valueOf(s.toString());
					mTextBeishu.setText(integer.toString());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}
		});
		/** add by pengcx 20130722 end */
		mTextBeishu.setText("" + iProgressBeishu);

		PublicMethod.setEditOnclick(mTextBeishu, mSeekBarBeishu, new Handler());

		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,
				true);
	}

	/**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */

	protected void setSeekWhenAddOrSub(int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}

	/**
	 * 显示追加投注
	 * 
	 * @param view
	 */
	private void initZhuiJia() {
		LinearLayout toggleLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		ToggleButton zhuijiatouzhu = (ToggleButton) findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu
				.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						if (isChecked) {
							betAndGift.setAmt(3);
							betAndGift.setIssuper("0");
						} else {
							betAndGift.setIssuper("");
							betAndGift.setAmt(2);
						}
						addviewmiss.setCodeAmt(betAndGift.getAmt());
						getTouzhuAlert();
						progressAtm(); // add by yejc 20130520
					}
				});
	}

	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress() {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		// mSeekBarQishu.setProgress(iProgressQishu);
		if (isclearaddview) {
			if (addviewmiss != null) {
				addviewmiss.clearInfo();
				addviewmiss.updateTextNum();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_0000()
	 */
	@Override
	public void errorCode_0000() {
		Intent intent = new Intent(this, BettingSuccessActivity.class);
		if (isSsq()) {
			intent.putExtra("isssq", true);
		}
		if (OrderDetails.fromInt != 0) {
			intent.putExtra("from", OrderDetails.fromInt);
		}
		intent.putExtra("page", BettingSuccessActivity.COOPERATION);

		intent.putExtra("lotno", betAndGift.getLotno());
		intent.putExtra("amount", betAndGift.getAmount());
		startActivity(intent);
	}

	private boolean isSsq() {
		if (betAndGift.getLotno().equals(Constants.LOTNO_SSQ)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isclearaddview = true;

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void showfenxdialog(String str) {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		TextView message = (TextView) view.findViewById(R.id.touzhu_succe_text);
		message.setText(str);
		image.setImageResource(R.drawable.succee);
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				JoinStartActivity.this.finish();
			}
		});
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				Intent intent = new Intent(JoinStartActivity.this,
						JoinStarShare.class);
				JoinStartActivity.this.startActivity(intent);
				JoinStartActivity.this.finish();
			}
		});

		dialog.show();
		dialog.getWindow().setContentView(view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#getContext()
	 */
	@Override
	public Context getContext() {
		return this;
	}

	@Override
	protected void onPause() {
		super.onPause();
		betAndGift.setIssuper("");
		betAndGift.setAmt(2);
		addviewmiss.setCodeAmt(betAndGift.getAmt());
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			progressAtm(); // add by yejc 20130520
			/** close by yejc 20130520 start */
			// allAtm = iProgressQishu * addviewmiss.getAllAmt() *
			// iProgressBeishu;
			// renText.setText("占总额"
			// + progress(isNull(buyEdit.getText().toString()), ""
			// + allAtm) + "%");// 总金额
			// baoText.setText("占总额"
			// + progress(isNull(safeEdit.getText().toString()), ""
			// + allAtm) + "%");// 总金额
			/** close by yejc 20130520 end */
			break;
		default:
			break;
		}
		getTouzhuAlert();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void touzhuIssue(String issue) {
		betAndGift.setBatchcode(issue);
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = JoinStartInterface.getInstance().joinStart(betAndGift);
				try {
					obj = new JSONObject(str);
					message = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, message);
					isNoIssue(handler, obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				progressdialog.dismiss();
			}

		});
		t.start();
	}

	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("温馨提示")
				.setMessage(string)
				.setNeutralButton("是", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						isclearaddview = false;
						clearProgress();
						finish();
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isclearaddview = true;
						clearProgress();
						finish();
					}
				});
		dialog.show();

	}

	/**
	 * 重写放回建
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case 4:
			/** add by yejc 20130510 start */
			if (isFromTrackQuery) {
				break;
			}
			/**add by yejc 20130510 end*/
			if (OrderDetails.isAlert) {
				if (OrderDetails.fromInt == BettingSuccessActivity.NOTICEBALL) {
					alertExit("退出该页面会清空已选择的投注号码，是否将已选择的投注号码保存？");
				} else {
					alertExit(getString(R.string.buy_alert_exit_detail));
				}

			} else {
				finish();
			}
			break;
		}
		return false;
	}

	/** add by yejc 20130520 start */
	private void progressAtm() {
		allAtm = iProgressQishu * addviewmiss.getAllAmt() * iProgressBeishu;
		renText.setText("占总额"
				+ progress(isNull(buyEdit.getText().toString()), "" + allAtm)
				+ "%");// 总金额
		baoText.setText("占总额"
				+ progress(isNull(safeEdit.getText().toString()), "" + allAtm)
				+ "%");// 总金额
	}

	/** add by yejc 20130520 end */

	/** add by yejc 20130624 start */
	private class EditTextWatcher implements TextWatcher {
		public EditText mEdit = null;

		public EditTextWatcher(EditText et) {
			mEdit = et;
		}

		public void afterTextChanged(Editable s) {
			String amount = mEdit.getText().toString();
			if (R.id.layout_join_edit_rengou == mEdit.getId()) {
				renText.setText("占总额" + progress(isNull(amount), "" + allAtm)
						+ "%");// 总金额
			} else if (R.id.layout_join_edit_baodi == mEdit.getId()) {
				baoText.setText("占总额" + progress(isNull(amount), "" + allAtm)
						+ "%");// 总金额
			}
			setEditText();
			String str = s.toString();
			if (str.length() == 1 && str.startsWith("0")) {
				mEdit.setText("");
			} else if (str.length() > 1 && str.startsWith("0")) {
				mEdit.setText(str.subSequence(1, str.length()));
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}
	/** add by yejc 20130624 end */
	class JoinStartActivityHandler extends MyHandler {

		public JoinStartActivityHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			if (controller != null) {
				JSONObject obj = controller.getRtnJSONObject();
				isNoIssue(handler, obj);
			}

		}
	}

}
