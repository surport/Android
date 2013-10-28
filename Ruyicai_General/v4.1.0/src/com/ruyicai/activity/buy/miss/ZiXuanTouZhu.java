package com.ruyicai.activity.buy.miss;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.TouzhuBaseActivity;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.notice.NoticeActivityGroup;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class ZiXuanTouZhu extends TouzhuBaseActivity implements HandlerMsg,
		OnSeekBarChangeListener {

	String phonenum, sessionId, userno;
	protected ProgressDialog progressdialog;
	private BetAndGiftPojo betAndGift;// 投注信息类
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	ZiXuanTouZhuActivityHandler handler = new ZiXuanTouZhuActivityHandler(this);// 自定义handler
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
	private ToggleButton zhuijiatouzhu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_touzhu);
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		addviewmiss = app.getAddviewmiss();
		init();

		handler.setBetAndGift(betAndGift);
	}

	private void init() {
		zhushu = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_zhushu);
		jine = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_jine);
		caizhong = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		issueText = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		textZhuma = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitle = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		getNetIssue();

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
					addviewmiss.createCodeInfoDialog(ZiXuanTouZhu.this);
					addviewmiss.showDialog();
				}
			});
		}
		if (betAndGift.isZhui()) {
			initZhuiJia();
		}
		getTouzhuAlert();
		Button cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (OrderDetails.isAlert && OrderDetails.fromInt != Constants.SEND_FROM_SIMULATE) {
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
						ZiXuanTouZhu.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(ZiXuanTouZhu.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					touZhu();
				}
			}
		});

	}

	private void isCodeText(Button codeInfo) {
		if (addviewmiss.getSize() > 1) {
			codeInfo.setVisibility(Button.VISIBLE);
		} else {
			codeInfo.setVisibility(Button.GONE);
		}
	}

	private void getNetIssue() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				final String issue = Controller.getInstance(ZiXuanTouZhu.this)
						.toNetIssue(betAndGift.getLotno());
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

	/**
	 * 显示追加投注
	 * 
	 * @param view
	 */
	private void initZhuiJia() {
		LinearLayout toggleLinear = (LinearLayout) findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		zhuijiatouzhu = (ToggleButton) findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					betAndGift.setAmt(3);
					betAndGift.setIssuper("0");
				} else {
					betAndGift.setIssuper("");
					betAndGift.setAmt(2);
				}
				addviewmiss.setCodeAmt(betAndGift.getAmt());
				getTouzhuAlert();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(zhuijiatouzhu != null && zhuijiatouzhu.isChecked()){
			betAndGift.setAmt(3);
			betAndGift.setIssuper("0");
		}else{
			betAndGift.setIssuper("");
			betAndGift.setAmt(2);
		}
		addviewmiss.setCodeAmt(betAndGift.getAmt());
	}

	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert() {
		if (type.equals("zc")) {
			zhushu.setText(betAndGift.getZhushu() + "注     ");
			jine.setText(+iProgressQishu
					* (Integer.valueOf(CheckUtil.isNull(betAndGift.getAmount())) / 100)
					* iProgressBeishu + "元");
		} else {
			zhushu.setText(addviewmiss.getAllZhu() + "注     ");
			jine.setText(iProgressQishu * addviewmiss.getAllAmt()
					* iProgressBeishu + "元");
		}
	}

	/**
	 * 投注方法
	 */
	private void touZhu() {
		toLogin = false;
		initBet();
		touZhuNet();
		// clearProgress();
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

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		Controller.getInstance(ZiXuanTouZhu.this).doBettingAction(handler,
				betAndGift);
	}

	/**
	 * 初始化投注信息
	 */
	public void initBet() {
		betAndGift.setIsSellWays("1");
		betAndGift.setAmount("" + addviewmiss.getAllAmt() * iProgressBeishu
				* 100);
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setDescription("");
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）

		/** add by yejc 20130510 start */
		if (isFromTrackQuery) {
			betAndGift.setBet_code(betAndGift.getBet_code());
		} else {
			betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,
					betAndGift.getAmt() * 100));
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

	@Override
	public void errorCode_0000() {
		Intent intent = new Intent(this, BettingSuccessActivity.class);
		if (isSsq()) {
			intent.putExtra("isssq", true);
		}
		if (OrderDetails.fromInt != 0) {
			intent.putExtra("from", OrderDetails.fromInt);
		}

		intent.putExtra("page", BettingSuccessActivity.BETTING);
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
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void errorCode_000000() {
	}

	@Override
	public Context getContext() {
		return this;
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
        controller = Controller.getInstance(ZiXuanTouZhu.this);
		if (controller != null) {
			controller.doBettingAction(handler, betAndGift);
		}
	}

	/**
	 * 退出提示
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertExit(String string) {
		Builder dialog = new AlertDialog.Builder(this)
				.setTitle("温馨提示")
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
			if (OrderDetails.isAlert && OrderDetails.fromInt != Constants.SEND_FROM_SIMULATE ) {
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
	class ZiXuanTouZhuActivityHandler extends MyHandler {

		public ZiXuanTouZhuActivityHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (controller != null) {
				JSONObject obj = controller.getRtnJSONObject();
				isNoIssue(handler, obj);
			}

		}
	}
}
