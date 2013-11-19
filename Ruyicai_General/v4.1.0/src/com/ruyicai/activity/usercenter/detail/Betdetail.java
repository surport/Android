package com.ruyicai.activity.usercenter.detail;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.beijing.BeiJingSingleGameActivity;
import com.ruyicai.activity.usercenter.BetQueryActivity;
import com.ruyicai.activity.usercenter.ContentListView;
import com.ruyicai.activity.usercenter.info.BetQueryInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class Betdetail extends Activity {

	// AlertDialog dialog = new AlertDialog.Builder(this).create();
	BetQueryInfo info;
	private ContentListView contentListView = new ContentListView(this);
	public Context context;
	private String phonenum, sessionid, userno;
	private BetAndGiftPojo betPojo = new BetAndGiftPojo();
	private final int DIALOG1_KEY = 0;
	ProgressDialog dialog;
	Handler handler = new Handler();
	StringBuffer buyAgainMessage;
	TextView buyagainbatchcode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bet_detail);
		context = this;
		getInfo();
		init();
	}

	/**
	 * 从上一个页面获取信息
	 */
	public void getInfo() {
		Intent intent = getIntent();
		byte[] bytes = intent.getByteArrayExtra("info");
		if (bytes != null) {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				info = (BetQueryInfo) objStream.readObject();
			} catch (Exception e) {
			}
		}

	}

	private void init() {
		final String lotno = (String) info.getLotNo();
		final String prizeqihao = (String) info.getBatchCode();
		final String amount = (String) info.getAmount();
		final String fPayMoney = PublicMethod.formatMoney(amount);
		final String lotName = (String) info.getLotName();
		final String betNum = (String) info.getBetNum();
		final String lotMulti = (String) info.getLotMulti();
		final String prizemoney = (String) info.getPrizeAmt();
		final String ordertime = (String) info.getOrdertime();
		final String betcode = (String) info.getBetCode();
		final String prize_State = (String) info.getPrizeState();
		final String win_code = (String) info.getWin_code();
		final String orderId = (String) info.getOrderId();
		final String stateMo = (String) info.getStateMemo();
		final String betcodehtml = (String) info.getBetCodeHtml();
		/**add by pengcx 20130609 start*/
		final String expectprize = (String)info.getExpectPrizeAmt();
		/**add by pengcx 20130609 end*/
		TextView lotkind = (TextView) findViewById(R.id.bet_detail_text_lotno);
		TextView batchcode = (TextView) findViewById(R.id.bet_detail_text_batchcode);
		TextView dingdanno = (TextView) findViewById(R.id.bet_detail_text_dingdan);
		TextView beishu = (TextView) findViewById(R.id.join_detail_text_beishu);
		TextView zhushu = (TextView) findViewById(R.id.bet_detail_text_zhushu);
		TextView atm = (TextView) findViewById(R.id.bet_detail_text_atm);
		TextView state = (TextView) findViewById(R.id.bet_detail_text_state);
		TextView bettime = (TextView) findViewById(R.id.bet_detail_tex_bettime);
		TextView content = (TextView) findViewById(R.id.bet_detail_text_content);
		TextView kaijianghao = (TextView) findViewById(R.id.bet_detail_text_kaijianghao);
		LinearLayout layoutMain = (LinearLayout) findViewById(R.id.bet_detail_layout_content);
		/**add by pengcx 20130609 start*/
		TextView expertTextView = (TextView) findViewById(R.id.bet_detail_text_expert);
		LinearLayout layoutExpert = (LinearLayout) findViewById(R.id.bet_detail_linearlayout_expert);
		/**add by pengcx 20130609 end*/
		/**add by yejc 20130709 start*/
		TextView playTextView = (TextView) findViewById(R.id.bet_detail_text_play);
		if (lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotno.equals(Constants.LOTNO_JCZQ)
				|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)
				|| lotno.equals(Constants.LOTNO_JCZQ_BF)
				|| lotno.equals(Constants.LOTNO_JCZQ_BQC)
				|| lotno.equals(Constants.LOTNO_JCZQ_ZQJ)
				|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
				|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)
				|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)
				|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)
				|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)
				|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)
				|| lotno.equals(Constants.LOTNO_RX9)
				|| lotno.equals(Constants.LOTNO_SFC)
				|| lotno.equals(Constants.LOTNO_JQC)
				|| lotno.equals(Constants.LOTNO_LCB)) {
			try {
				JSONObject jsonObj = new JSONObject(info.getJson());
				String disPlay = jsonObj.getString("display");
				if (disPlay.equals("true")) {
					JSONArray jsonArray = jsonObj.getJSONArray("result");
					JSONObject obj = jsonArray.getJSONObject(0);
//					playTextView.setText(obj.getString("play"));
					if (obj.has("play")) {
						playTextView.append(obj.getString("play"));
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			playTextView.setVisibility(View.GONE);
		} 
		/**add by yejc 20130709 end*/
		lotkind.append(lotName);
		dingdanno.append(orderId);
		beishu.append(lotMulti+"倍");
		zhushu.append(betNum+"注");
		atm.append(fPayMoney);
		state.append(stateMo);
		bettime.append(ordertime);
		// content.setText(Html.fromHtml("方案内容：<br>"+betcodehtml));
		JSONObject josn = null;
		try {
			josn = new JSONObject(info.getJson());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		contentListView.createListContent(layoutMain, content, info.getLotNo(),
				info.getBetCodeHtml(), josn);
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
			kaijianghao.setVisibility(View.GONE);
		} else {
			if (prize_State.equals("0")) {
				kaijianghao.append("未开奖");
			} else {
				kaijianghao.append(win_code);
			}
		}
		Button cancleLook = (Button) findViewById(R.id.bet_detail_img_cannle);
		cancleLook.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
			batchcode.setVisibility(View.GONE);
		} else {
			Button touzhu = (Button) findViewById(R.id.bet_detail_img_ok);
			if (lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_WINTIELOSS)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_OVERALL)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_TOTALGOALS)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_UPDOWNSINGLEDOUBLE)
					|| lotno.equals(Constants.LOTNO_BEIJINGSINGLEGAME_HALFTHEAUDIENCE)
					|| lotno.equals(Constants.LOTNO_JQC)
					|| lotno.equals(Constants.LOTNO_LCB)
					|| lotno.equals(Constants.LOTNO_SFC)
					|| lotno.equals(Constants.LOTNO_RX9)){
				touzhu.setVisibility(View.GONE);
				kaijianghao.setVisibility(View.GONE);
			} else {
				touzhu.setVisibility(View.VISIBLE);
				touzhu.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						showagaindialog(info);
					}
				});
			}
			batchcode.append(prizeqihao);
		}
		
		/*add by pengcx 20130609 start*/
		if (Constants.LOTNO_JCZQ.equals(lotno)
				|| Constants.LOTNO_JCZQ_RQSPF.equals(lotno)
				|| Constants.LOTNO_JCZQ_ZQJ.equals(lotno)
				|| Constants.LOTNO_JCZQ_BF.equals(lotno)
				|| Constants.LOTNO_JCZQ_BF.equals(lotno)
				|| Constants.LOTNO_JCZQ_BQC.equals(lotno)
				|| Constants.LOTNO_JCZQ_HUN.equals(lotno)
				|| Constants.LOTNO_JCLQ.equals(lotno)
				|| Constants.LOTNO_JCLQ_RF.equals(lotno)
				|| Constants.LOTNO_JCLQ_SFC.equals(lotno)
				|| Constants.LOTNO_JCLQ_DXF.equals(lotno)
				|| Constants.LOTNO_JCLQ_HUN.equals(lotno)) {
			if (prize_State.equals("0")) {
				expertTextView.setText(expectprize);
				layoutExpert.setVisibility(View.VISIBLE);
			} else {
				layoutExpert.setVisibility(View.GONE);
			}
		}else{
			layoutExpert.setVisibility(View.GONE);
		}
		/*add by pengcx 20130609 end*/
	}

	/**
	 * 再买一次
	 */
	private void showagaindialog(final BetQueryInfo info) {
		LayoutInflater factory = LayoutInflater.from(this);
		/* 中奖查询的查看详情使用余额查询的布局 */
		View view = factory.inflate(R.layout.usercenter_bugagain, null);
		final Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(view);
		TextView title = (TextView) view
				.findViewById(R.id.usercenter_bindphonetitle);
		TextView label = (TextView) view
				.findViewById(R.id.usercenter_bindphonelabel);
		final EditText context = (EditText) view
				.findViewById(R.id.usercenter_edittext_bindphoneContext);
		Button submit = (Button) view
				.findViewById(R.id.usercenter_bindphone_ok);
		Button cancle = (Button) view
				.findViewById(R.id.usercenter_bindphone_back);
		context.setKeyListener(new DigitsKeyListener());
		/**add by yejc 20130705 start*/
		context.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					if (mTextNum > 10000) {
						context.setText("" + 10000);
						context.setSelection(context.length());
					}
					if (text.length() == 1 && text.startsWith("0")) {
						context.setText("");
					} else if (text.length() > 1 && text.startsWith("0")) {
						context.setText(text.subSequence(1, text.length()));
					}
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		/**add by yejc 20130705 end*/
		title.setText("再买一次");
		label.setText("请输入购买倍数(最高10000倍)");
		submit.setText("确定");
		final String lotno = (String) info.getLotNo();
		final String prizeqihao = (String) info.getBatchCode();
		final String amount = (String) info.getAmount();
		final String lotName = (String) info.getLotName();
		final String betcode = (String) info.getBetCodeHtml();
		final String bet_code = (String) info.getBet_code();
		final String lotMulti = (String) info.getLotMulti();
		final String omeAmount = (String) info.getOneAmount();
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String beishu = context.getText().toString();

				if (beishu.equals("")) {
					Toast.makeText(Betdetail.this, "请输入倍数", Toast.LENGTH_SHORT)
							.show();
				} else if (!PublicMethod.isNumeric(beishu)) {
					Toast.makeText(Betdetail.this, "请输入数字", Toast.LENGTH_SHORT)
							.show();
				} /*else if (beishu.length() > 5
						|| Integer.valueOf(beishu) > 2000) {
					Toast.makeText(Betdetail.this, "不能超过10000倍",
							Toast.LENGTH_SHORT).show();
				}*/ else {
					int allAmount = Integer.valueOf(amount)
							/ Integer.valueOf(lotMulti)
							* Integer.valueOf(beishu) / 100;
					if (allAmount > 0) {
						initBetPojo(bet_code, lotno, amount, beishu, lotMulti,
								omeAmount);
						buyAgainMessage = new StringBuffer();
						if (lotno.equals("J00001") || lotno.equals("J00002")
								|| lotno.equals("J00003")
								|| lotno.equals("J00004")
								|| lotno.equals(Constants.LOTNO_JCLQ)
								|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
								|| lotno.equals(Constants.LOTNO_JCLQ_RF)
								|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
								|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
								|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
								|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
							alert(lotno, lotName, allAmount, beishu, betcode);
						} else {
							getNetIssue(lotno, lotName, allAmount, beishu,
									betcode);
						}
						dialog.dismiss();
					} else {
						Toast.makeText(Betdetail.this,
								getString(R.string.usercenter_max_amount),
								Toast.LENGTH_SHORT).show();
					}

				}

			}
		});
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void initPojo() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}

	private void initBetPojo(String zhuma, String lotno, String amount,
			String beishu, String lomit, String oneAmount) {
		initPojo();
		betPojo.setPhonenum(phonenum);
		betPojo.setSessionid(sessionid);
		betPojo.setUserno(userno);
		betPojo.setBet_code(zhuma);
		betPojo.setLotmulti(beishu);
		betPojo.setLotno(lotno);
		betPojo.setBatchnum("1");
		betPojo.setBettype("bet");
		betPojo.setAmount(Integer.valueOf(amount) / Integer.valueOf(lomit)
				* Integer.valueOf(beishu) + "");
		betPojo.setAmt(0);
		betPojo.setOneAmount(oneAmount);
		betPojo.setIsSellWays("1");
	}

	private void getNetIssue(final String lotno, final String lotname,
			final int allAmount, final String lommt, final String betcode) {
		showDialog(0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String issue = Controller.getInstance(Betdetail.this).toNetIssue(lotno);
				if (issue.equals("")) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							dismissDialog(0);
							Toast.makeText(Betdetail.this, "获取期号失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					handler.post(new Runnable() {

						@Override
						public void run() {
							dismissDialog(0);
							// TODO Auto-generated method stub
							alert(lotno, lotname, allAmount, lommt, betcode);
							buyagainbatchcode.setText("彩种期号：" + issue);
							betPojo.setBatchcode(issue);
						}
					});
				}
			}
		}).start();
	}

	/**
	 * 单复式投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert(String lotno, String lotname, int allAmount,
			String lommt, String betcode) {
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.usercenter_buyagain_layout, null);
		final AlertDialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.whatisyourselect)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {

							}

						}).create();
		dialog.show();

		TextView name = (TextView) v.findViewById(R.id.buyagain_touzhu_lotname);
		buyagainbatchcode = (TextView) v
				.findViewById(R.id.buyagain_touzhu_qihao);
		if (lotno.equals("J00001") || lotno.equals("J00002")
				|| lotno.equals("J00003") || lotno.equals("J00004")
				|| lotno.equals(Constants.LOTNO_JCLQ)
				|| lotno.equals(Constants.LOTNO_JCLQ_DXF)
				|| lotno.equals(Constants.LOTNO_JCLQ_RF)
				|| lotno.equals(Constants.LOTNO_JCLQ_SFC)
				|| lotno.equals(Constants.LOTNO_JCZQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCLQ_HUN)
				|| lotno.equals(Constants.LOTNO_JCZQ_RQSPF)) {
			buyagainbatchcode.setVisibility(View.GONE);
		}
		TextView jine = (TextView) v.findViewById(R.id.buyagain_touzhu_jine);
		TextView beishu = (TextView) v
				.findViewById(R.id.buyagain_touzhu_beishu);
		TextView zhuma = (TextView) v.findViewById(R.id.buyagain_touzhu_zhuma);
		Button cancel = (Button) v
				.findViewById(R.id.buyagain_touzhu_button_cancel);
		name.append(lotname);
		jine.append(allAmount + "元");
		beishu.append(lommt);
		zhuma.append("\n" + Html.fromHtml(betcode));

		cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
		cancel.setText(R.string.cancel);
		Button ok = (Button) v.findViewById(R.id.buyagain_touzhu_button_ok);
		ok.setBackgroundResource(R.drawable.join_info_btn_selecter);
		ok.setText(R.string.ok);
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				touZhuNet();
			}
		});
		dialog.getWindow().setContentView(v);
	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		showDialog(DIALOG1_KEY);
		Thread t = new Thread(new Runnable() {
			String str = "00";

			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betPojo);
				try {
					JSONObject obj = new JSONObject(str);
					final String message = obj.getString("message");
					String error = obj.getString("error_code");
					handler.post(new Runnable() {
						@Override
						public void run() {
							dismissDialog(0);
							Toast.makeText(Betdetail.this, message,
									Toast.LENGTH_SHORT).show();
						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
			}

		});
		t.start();
	}

	protected Dialog onCreateDialog(int id) {
		dialog = new ProgressDialog(this);
		switch (id) {
		case DIALOG1_KEY: {
			dialog.setTitle(R.string.usercenter_netDialogTitle);
			dialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}
		return dialog;
	}
}
