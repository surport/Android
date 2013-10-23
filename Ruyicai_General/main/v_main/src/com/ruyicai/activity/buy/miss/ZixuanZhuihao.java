package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;
import org.json.JSONArray;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.TouzhuBaseActivity;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.getRecoveryBatchCode;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.CheckUtil;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 追号设置界面
 * 
 * @author win
 * 
 */
public class ZixuanZhuihao extends TouzhuBaseActivity implements HandlerMsg,
		OnSeekBarChangeListener {
	String phonenum, sessionId, userno;
	protected ProgressDialog progressdialog;
	private BetAndGiftPojo betAndGift;// 投注信息类
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	ZiXuanZhuihaoActivityHandler handler = new ZiXuanZhuihaoActivityHandler(this);// 自定义handler
	TextView textAlert;
	TextView textZhuma;
	TextView textTitle;
	LinearLayout zhuiqishezhi;
	Button zhuiqi;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected EditText mTextBeishu, mTextQishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	TextView zhushu;
	TextView jine;
	TextView caizhong;
	private boolean toLogin = false;
	public boolean isTouzhu = false;// 是否投注
	ArrayList<String> batchcodes = new ArrayList<String>();
	int state = 0;
	public ArrayList<Checktouinfo> subscribeInfocheck = new ArrayList<Checktouinfo>();
	Checktouinfo checkinfo[];
	private AddViewMiss addviewmiss;
	private Controller controller = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_zhuihao);
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		addviewmiss = app.getAddviewmiss();
		init();
	}

	private void init() {
		zhuiqishezhi = (LinearLayout) findViewById(R.id.zhuiqishezhi);
		initImageView();
		if (betAndGift.isZhui()) {
			initZhuiJia();
		}

		zhuiqi = (Button) findViewById(R.id.zhuiqi);
		zhuiqi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (state == 0) {
					getbatchcodes();
					/*add by pengcx 20130517 start*/
					zhuiqishezhi.setVisibility(View.VISIBLE);
					/*add by pengcx 20130517 end*/
					state = 2;
				} else if (state == 1) {
					zhuiqishezhi.setVisibility(View.VISIBLE);
					state = 2;
				} else if (state == 2) {
					zhuiqishezhi.setVisibility(View.GONE);
					state = 1;
				}
			}
		});
		zhushu = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_zhushu);
		jine = (TextView) findViewById(R.id.alert_dialog_touzhu_text_one);
		caizhong = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		issueText = (TextView) findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		textZhuma = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitle = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		CodeInfoMiss code = addviewmiss.getCodeList()
				.get(addviewmiss.getSize() - 1);
		code.setTextCodeColor(textZhuma, code.getLotoNo(), code.getTouZhuType());
		getNetIssue();

		/**add by yejc 20130705 start*/
		if (isFromTrackQuery) {
			String betCode = betAndGift.getBet_code();
			if (betCode != null && betCode.contains("!")) {
				textTitle.setText("注码：" + "共有" + betCode.split("!").length + "笔投注");
			} else {
				textTitle.setText("注码：" + "共有" + 1 + "笔投注");
			}
			/**add by yejc 20130705 end*/
		} else {
			textTitle.setText("注码：" + "共有" + addviewmiss.getSize() + "笔投注");
		}
		getTouzhuAlert();
		Button cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		codeInfo = (Button) findViewById(R.id.alert_dialog_touzhu_btn_look_code);
		isCodeText(codeInfo);
		CheckBox checkPrize = (CheckBox) findViewById(R.id.alert_dialog_touzhu_check_prize);
		checkPrize.setChecked(true);

		// 设置betAndGift.prizeend与checkPrize保持一致
		betAndGift.setPrizeend("1");

		checkPrize.setButtonDrawable(R.drawable.check_on_off);
		checkPrize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					betAndGift.setPrizeend("1");
				} else {
					betAndGift.setPrizeend("0");
				}
			}
		});
		codeInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addviewmiss.createCodeInfoDialog(ZixuanZhuihao.this);
				addviewmiss.showDialog();
			}
		});
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
				if(!AllowCommit())return;
				RWSharedPreferences pre = new RWSharedPreferences(
						ZixuanZhuihao.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(ZixuanZhuihao.this,
							UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					touZhu();
				}
			}
		});

	}
	
	protected boolean AllowCommit(){
		if(checkinfo!=null){
			for (int i = 0; i < checkinfo.length; i++) {
				if("".equals(checkinfo[i].getBeishu()) && "1".equals(checkinfo[i].getState())){
					Toast.makeText(ZixuanZhuihao.this, "倍数不能为空",Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		return true;
	}

	private void getNetIssue() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String issue = Controller.getInstance(ZixuanZhuihao.this).toNetIssue(betAndGift
						.getLotno());
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

	private void isCodeText(Button codeInfo) {
		if (addviewmiss.getSize() > 1) {
			codeInfo.setVisibility(Button.VISIBLE);
		} else {
			codeInfo.setVisibility(Button.GONE);
		}
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
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					betAndGift.setAmt(3);
					betAndGift.setIssuper("0");
				} else {
					betAndGift.setIssuper("");
					betAndGift.setAmt(2);
				}
				/**add by pengcx 20130515 start*/
				state = 0;
				zhuiqishezhi.setVisibility(View.GONE);
				/**add by pengcx 20130515 end*/
				addviewmiss.setCodeAmt(betAndGift.getAmt());
				getTouzhuAlert();
			}
		});
	}

	public void getbatchcodes() {
		final Handler hand = new Handler();
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
		dialog.show();
		batchcodes.clear();
		new Thread(new Runnable() {

			@Override
			public void run() {
				String bathcode = getRecoveryBatchCode.getInstance().getCode(
						betAndGift.getLotno(), String.valueOf(iProgressQishu));

				try {
					JSONObject json = new JSONObject(bathcode);
					JSONArray array = json.getJSONArray("result");
					String errorcode = json.getString("error_code");
					final String message = json.getString("message");
					dialog.dismiss();
					if (errorcode.equals("0000")) {
						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.getJSONObject(i);
							String betcode = obj.getString("batchCode");
							batchcodes.add(betcode);
						}
						hand.post(new Runnable() {
							@Override
							public void run() {
								getviewofzhuiqi();
							}
						});
					} else {
						hand.post(new Runnable() {
							@Override
							public void run() {
								dialog.dismiss();
								Toast.makeText(ZixuanZhuihao.this, message,
										Toast.LENGTH_SHORT).show();
							}
						});
						state = 0;

					}

				} catch (JSONException e) {
					hand.post(new Runnable() {

						@Override
						public void run() {
							dialog.dismiss();
							Toast.makeText(ZixuanZhuihao.this, "期号获取失败",
									Toast.LENGTH_SHORT).show();
						}
					});
					state = 0;
				}
			}
		}).start();

	}

	private void getviewofzhuiqi() {
		checkinfo = new Checktouinfo[iProgressQishu];
		/**add by pengcx 20130517 start*/
		zhuiqishezhi.removeAllViews();
		/**add by pengcx 20130517 end*/
		subscribeInfocheck.clear(); //add by yejc 20130621
		for (int i = 0; i < iProgressQishu; i++) {
			checkinfo[i] = new Checktouinfo();
			final int index = i;
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflate.inflate(R.layout.zhuihaoshezhi, null);
			zhuiqishezhi.addView(view);
			TextView text1 = (TextView) view.findViewById(R.id.textView1);
			final TextView text3 = (TextView) view.findViewById(R.id.textView3);
			final EditText edit = (EditText) view.findViewById(R.id.edittext1);
			edit.setText(String.valueOf(iProgressBeishu));
			edit.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (edit.getText().toString().equals("")) {
						Toast.makeText(ZixuanZhuihao.this, "倍数不能为空",
								Toast.LENGTH_SHORT).show();
						//edit.setText("1");
						checkinfo[index].setBeishu("");
					} else if (!PublicMethod.isNumeric(edit.getText()
							.toString())) {
						Toast.makeText(ZixuanZhuihao.this, "请输入数字",
								Toast.LENGTH_SHORT).show();
						edit.setText("1");
					} else if (Integer.valueOf(edit.getText().toString()) > 10000) {
//						Toast.makeText(ZixuanZhuihao.this, "超过倍数上限9999",
//								Toast.LENGTH_SHORT).show();
						edit.setText("10000");
						edit.setSelection(edit.length());
					} else {
						if (isFromTrackQuery) {
							int zhuShu = Integer.valueOf(CheckUtil.isNull(betAndGift.getZhushu()));
							text3.setText(betAndGift.getAmt()*zhuShu*Integer.valueOf(edit.getText().toString())+ "元");
						} else {
							text3.setText(addviewmiss.getAllAmt()
									* Integer.valueOf(edit.getText().toString())
									+ "元");
						}
						checkinfo[index].setBeishu(edit.getText().toString());
						checkinfo[index].setAmt(Integer.valueOf(text3.getText()
								.toString().replace("元", ""))
								* 100 + "");
						getTouzhuAlert();
					}
					String str = s.toString();
					if (str.length() == 1 && str.startsWith("0")) {
						edit.setText("");
					} else if (str.length() > 1 && str.startsWith("0")) {
						edit.setText(str.subSequence(1, str.length()));
					}
				}
			});
			
			if (isFromTrackQuery) {
				int zhuShu = Integer.valueOf(betAndGift.getZhushu());
				text3.setText(betAndGift.getAmt()*zhuShu*iProgressBeishu+ "元");
			} else {
				text3.setText(addviewmiss.getAllAmt()
						* Integer.valueOf(edit.getText().toString()) + "元");
			}
			
			checkinfo[i].setAmt(Integer.valueOf(text3.getText().toString()
					.replace("元", ""))
					* 100 + "");
			text1.setText(batchcodes.get(i) + "期");
			checkinfo[i].setBatchcode(batchcodes.get(i));
			checkinfo[i].setBeishu(edit.getText().toString());
			CheckBox check = (CheckBox) view.findViewById(R.id.checkBox1);
			check.setChecked(true);
			if (check.isChecked()) {
				checkinfo[i].setState("1");
			}
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						checkinfo[index].setState("1");
						getTouzhuAlert();
					} else {
						checkinfo[index].setState("0");
						getTouzhuAlert();
					}

				}
			});
			subscribeInfocheck.add(checkinfo[i]);
		}
	}

	class Checktouinfo {
		String state = "";
		String batchcode = "";
		String beishu = "";
		String amt = "";

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getBatchcode() {
			return batchcode;
		}

		public void setBatchcode(String batchcode) {
			this.batchcode = batchcode;
		}

		public String getBeishu() {
			return beishu;
		}

		public void setBeishu(String beishu) {
			this.beishu = beishu;
		}

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		betAndGift.setIssuper("");
		betAndGift.setAmt(2);
		addviewmiss.setCodeAmt(betAndGift.getAmt());
	}

	private String getSubstringforset() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < subscribeInfocheck.size(); i++) {
			Checktouinfo check = subscribeInfocheck.get(i);
			if (check.state.equals("1")) {
				str.append(check.getBatchcode()).append(",")
						.append(check.getAmt()).append(",")
						.append(check.getBeishu()).append("!");
			}
		}
		if (str.toString().equals("")) {
			isTouzhu = false;
			Toast.makeText(this, "请至少选择一期", Toast.LENGTH_SHORT).show();
			return "";
		} else {
			isTouzhu = true;
			String strset = str.toString().substring(0,
					str.toString().length() - 1);
			return strset;
		}

	}

	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert() {
		if (isFromTrackQuery) {
			/**add by yejc 20130621 start*/
			if (state == 2) {
				jine.setText("金额：" + getSubstringforamt() + "元");
			} else {
				zhushu.setText(betAndGift.getZhushu() + "注     ");
				int zhuShu = Integer.valueOf(betAndGift.getZhushu());
				jine.setText("金额：" + betAndGift.getAmt()*zhuShu*iProgressQishu * iProgressBeishu+ "元");
			}
			/**add by yejc 20130621 end*/
		} else {
			zhushu.setText(addviewmiss.getAllZhu() + "注     ");
			if (state == 0 || state == 1) {
				jine.setText("金额：" + iProgressQishu * addviewmiss.getAllAmt()
						* iProgressBeishu + "元");
			} else if (state == 2) {
				jine.setText("金额：" + getSubstringforamt() + "元");
			}
		}
	}

	private int getSubstringforamt() {
		int amt = 0;
		for (int i = 0; i < subscribeInfocheck.size(); i++) {
			Checktouinfo check = subscribeInfocheck.get(i);
			if (check.state.equals("1")) {
				amt += Integer.valueOf(check.getAmt());
			}
		}
		return amt / 100;

	}

	/**
	 * 投注方法
	 */
	private void touZhu() {
		toLogin = false;
		isTouzhu = true;
		initBet();
		if (isTouzhu) {
			touZhuNet();
		}
		// clearProgress();
	}

	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress() {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu.setProgress(iProgressQishu);
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
		Controller.getInstance(this.getContext()).doBettingAction(handler, betAndGift);
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
			/**add by yejc 20130621 start*/
			String betCode = betAndGift.getBet_code();
			int zhuShu = Integer.valueOf(betAndGift.getZhushu());
			int amt = betAndGift.getAmt() * 100;
			
			if (betCode.contains("!")) {
				String betCodesArray[] = betCode.split("!");
				String betCodes = "";
				int allAmount = 0;
				for (int i = 0; i < betCodesArray.length; i++) {
					String code = betCodesArray[i];
					String betCodeStr = code.substring(0, code.indexOf("_")+1);
					String amount = code.substring(code.lastIndexOf("_")+1, code.length());
					int zhushu_i = Integer.valueOf(amount)/200;
					
					String zhuMa_i = PublicMethod.isTen(iProgressBeishu) + "_" + amt
					+ "_" + zhushu_i * amt;
					betCodes = betCodes + betCodeStr + zhuMa_i + "!";
					allAmount = allAmount + (zhushu_i * amt * iProgressBeishu);
				}
				if (betCodes.endsWith("!")) {
					betCodes = betCodes.substring(0, betCodes.length()-1);
				}
				betAndGift.setAmount(String.valueOf(allAmount));
				betAndGift.setBet_code(betCodes);
			} else {
				String subString = betCode.substring(0, betCode.indexOf("_")+1);
				String zhuMa = PublicMethod.isTen(iProgressBeishu) + "_" + amt
						+ "_" + zhuShu * amt;
				betAndGift.setAmount(""+zhuShu * amt * iProgressBeishu);
				betAndGift.setBet_code(subString+zhuMa);
			}
			
			/**add by yejc 20130621 end*/
		} else {
			betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,
			betAndGift.getAmt() * 100));
		}
		/** add by yejc 20130510 end */

		if (state == 2) {
			betAndGift.setSubscribeInfo(getSubstringforset());
		} else {
			betAndGift.setSubscribeInfo("");
		}
	}

	/**
	 * 初始化倍数和期数进度条
	 * 
	 * @param view
	 */
	public void initImageView() {
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_beishu);
		mTextBeishu = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		mTextQishu = (EditText) findViewById(R.id.buy_zixuan_text_qishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		/**add by pengcx 20130722 start*/
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
		/**add by pengcx 20130722 end*/
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu.setText("" + iProgressQishu);

		PublicMethod.setEditOnclick(mTextBeishu, mSeekBarBeishu, new Handler());
		PublicMethod.setEditOnclick(mTextQishu, mSeekBarQishu, new Handler());
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
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,
				mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,
				false);
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
		if(isSsq())
		{
			intent.putExtra("isssq", true);
		}
		
		if(OrderDetails.fromInt != 0){
			intent.putExtra("from", OrderDetails.fromInt);
		}
		
		intent.putExtra("page", BettingSuccessActivity.ADDTO);

		intent.putExtra("lotno", betAndGift.getLotno());
//		int totalAmount = Integer.valueOf(betAndGift.getAmount())
//				* Integer.valueOf(betAndGift.getBatchnum());
		/**add by yejc 20130708 start*/
		if (isFromTrackQuery) {
			if (state == 2) {
				intent.putExtra("amount", String.valueOf(getSubstringforamt()*100));
			} else {
				int zhuShu = Integer.valueOf(betAndGift.getZhushu());
				int amount = betAndGift.getAmt()*zhuShu*iProgressQishu * iProgressBeishu*100;
				intent.putExtra("amount", String.valueOf(amount));
			}
		} else {
			if (state == 0 || state == 1) {
				int totalAmount = Integer.valueOf(betAndGift.getAmount())
						* Integer.valueOf(betAndGift.getBatchnum());
				intent.putExtra("amount", String.valueOf(totalAmount));
			} else if (state == 2) {
				intent.putExtra("amount", String.valueOf(getSubstringforamt()*100));
			}
		}
		/**add by yejc 20130708 end*/
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
			state = 0;
			zhuiqishezhi.removeAllViews();
			subscribeInfocheck.clear();
			checkinfo = null;
			zhuiqishezhi.setVisibility(View.VISIBLE);
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			state = 0;
			zhuiqishezhi.removeAllViews();
			subscribeInfocheck.clear();
			checkinfo = null;
			zhuiqishezhi.setVisibility(View.VISIBLE);
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
        controller = Controller.getInstance(ZixuanZhuihao.this);
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
			/** add by yejc 20130510 end */

			if (OrderDetails.isAlert && OrderDetails.fromInt != Constants.SEND_FROM_SIMULATE) {
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
	class ZiXuanZhuihaoActivityHandler extends MyHandler {

		public ZiXuanZhuihaoActivityHandler(HandlerMsg msg) {
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
