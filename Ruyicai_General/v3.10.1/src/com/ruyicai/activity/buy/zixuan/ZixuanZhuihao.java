package com.ruyicai.activity.buy.zixuan;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
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
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.getRecoveryBatchCode;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class ZixuanZhuihao extends TouzhuBaseActivity implements HandlerMsg,
		OnSeekBarChangeListener {
	String phonenum, sessionId, userno;
	protected ProgressDialog progressdialog;
	private BetAndGiftPojo betAndGift;// 投注信息类
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	ZiXunTouZhuihaoHandler handler = new ZiXunTouZhuihaoHandler(this);// 自定义handler
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
	private AddView addview;
	private Context context;
	public ArrayList<Checktouinfo> subscribeInfocheck = new ArrayList<Checktouinfo>();
	Checktouinfo checkinfo[];
	private final int HIGHT_MAX = 10000;
	private Controller controller = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_zhuihao);
		context = this;
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		betAndGift = app.getPojo();
		addview = app.getAddview();
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
				// TODO Auto-generated method stub
				if (state == 0) {
					getbatchcodes();
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
		CodeInfo code = addview.getCodeList().get(addview.getSize() - 1);
		code.setTextCodeColor(textZhuma, code.getLotoNo(), code.getTouZhuType());
		// getNetIssue();
		
		if (isFromTrackQuery) {
			getNetIssue();
		} else {
			if (Constants.type.equals("hight") || Constants.type.equals("zc")) {
				issueText.setText("第" + betAndGift.getBatchcode() + "期");
			} else {
				getNetIssue();
			}
		}
		
		
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
			textTitle.setText("注码：" + "共有" + addview.getSize() + "笔投注");
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				addview.createCodeInfoDialog(context);
				addview.showDialog();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertExit(getString(R.string.buy_alert_exit_detail));
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
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

	private void getNetIssue() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String issue = Controller.getInstance(ZixuanZhuihao.this).toNetIssue(betAndGift
						.getLotno());
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						betAndGift.setBatchcode(issue);// 期号
						issueText.setText("第" + issue + "期");
					}
				});
			}
		}).start();
	}

	private void isCodeText(Button codeInfo) {
		if (addview.getSize() > 1) {
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
				addview.setCodeAmt(betAndGift.getAmt());
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
				// TODO Auto-generated method stub
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
								// TODO Auto-generated method stub
								getviewofzhuiqi();
							}
						});
					} else {
						hand.post(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Toast.makeText(ZixuanZhuihao.this, message,
										Toast.LENGTH_SHORT).show();
							}
						});
						state = 0;

					}

				} catch (JSONException e) {
					// TODO: handle exception
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
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

	void getviewofzhuiqi() {
		checkinfo = new Checktouinfo[iProgressQishu];
		subscribeInfocheck.clear(); //add by yejc 20130703
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
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {

					// TODO Auto-generated method stub
					if (edit.getText().toString().equals("")) {
						Toast.makeText(ZixuanZhuihao.this, "倍数不能为空",
								Toast.LENGTH_SHORT).show();
						edit.setText("1");
					} else if (!PublicMethod.isNumeric(edit.getText()
							.toString())) {
						Toast.makeText(ZixuanZhuihao.this, "请输入数字",
								Toast.LENGTH_SHORT).show();
						edit.setText("1");
					} else if (Integer.valueOf(edit.getText().toString()) > 10000) {
//						Toast.makeText(ZixuanZhuihao.this, "超过倍数上限10000",
//								Toast.LENGTH_SHORT).show();
						edit.setText("10000");
						edit.setSelection(edit.length());
					} else {
						/** add by yejc 20130703 start */
						if (isFromTrackQuery) {
							int zhuShu = Integer.valueOf(betAndGift.getZhushu());
							text3.setText(2*zhuShu*Integer.valueOf(edit.getText().toString())+ "元");
						/** add by yejc 20130703 end */
						} else {
							text3.setText(addview.getAllAmt()
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
			/** add by yejc 20130703 start */
			if (isFromTrackQuery) {
				int zhuShu = Integer.valueOf(betAndGift.getZhushu());
				text3.setText(2*zhuShu*iProgressBeishu+ "元");
				/** add by yejc 20130703 end */
			} else {
				text3.setText(addview.getAllAmt()
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
					// TODO Auto-generated method stub
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

	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert() {
		if (Constants.type.equals("zc")) {
			zhushu.setText(betAndGift.getZhushu() + "注     ");
			if (state == 0 || state == 1) {
				jine.setText("金额：" + iProgressQishu * addview.getAllAmt()
						* iProgressBeishu + "元");
			} else if (state == 2) {
				jine.setText("金额：" + getSubstringforamt() + "元");
			}
		} else {
			if (isFromTrackQuery) {
				/**add by yejc 20130703 start*/
				if (state == 2) {
					jine.setText("金额：" + getSubstringforamt() + "元");
				} else {
					zhushu.setText(betAndGift.getZhushu() + "注     ");
					int zhuShu = Integer.valueOf(betAndGift.getZhushu());
					jine.setText("金额：" + 2*zhuShu*iProgressQishu * iProgressBeishu+ "元");
				}
				/**add by yejc 20130703 end*/
			} else {
				zhushu.setText(addview.getAllZhu() + "注     ");
				if (state == 0 || state == 1) {
					jine.setText("金额：" + iProgressQishu * addview.getAllAmt()
							* iProgressBeishu + "元");
				} else if (state == 2) {
					jine.setText("金额：" + getSubstringforamt() + "元");
				}
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
		// TODO Auto-generated method stub
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
	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
	    controller = Controller.getInstance(context);
	    if (controller != null) {
	    	controller.doBettingAction(handler, betAndGift);
	    }
	}

	/**
	 * 初始化投注信息
	 */
	public void initBet() {
		betAndGift.setIsSellWays("1");
		betAndGift.setAmount("" + addview.getAllAmt() * iProgressBeishu * 100);
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift
		betAndGift.setDescription("");
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）
		/**add by yejc 20130703 start*/
		if (isFromTrackQuery) {
			String betCode = betAndGift.getBet_code();
			int zhuShu = Integer.valueOf(betAndGift.getZhushu());
//			int amt = betAndGift.getAmt() * 100;
			int amt = 2 * 100;
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
			
			/**add by yejc 20130703 end*/
		} else {
			betAndGift.setBet_code(addview.getTouzhuCode(iProgressBeishu,
					betAndGift.getAmt() * 100));
		}

		lotno = PublicMethod.toLotno(betAndGift.getLotno());
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
		if (Constants.type.equals("hight")) {
			mSeekBarBeishu.setMax(HIGHT_MAX);
		}
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
		/** modify pengcx 20130604 start */
		Intent intent = new Intent(this, BettingSuccessActivity.class);
		intent.putExtra("page", BettingSuccessActivity.ADDTO);
		intent.putExtra("lotno", betAndGift.getLotno());
//		int totalAmount = Integer.valueOf(betAndGift.getAmount())
//				* Integer.valueOf(betAndGift.getBatchnum());
//		intent.putExtra("amount", totalAmount);
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
		/** modify pengcx 20130604 end */
	}

	private boolean isclearaddview = true;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isclearaddview) {
			if (addview != null) {
				addview.clearInfo();
				addview.updateTextNum();
			}
		}

	}

	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
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
			// changeTextSumMoney();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void touzhuIssue(String issue) {
		// TODO Auto-generated method stub
		betAndGift.setBatchcode(issue);// 设置新的投注期号
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					handler.handleMsg(error, msg);
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
						finish();
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						isclearaddview = true;
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
		// TODO Auto-generated method stub
		switch (keyCode) {
		case 4:
			/** add by yejc 20130703 start */
			if (isFromTrackQuery) {
				break;
			}
			/** add by yejc 20130703 end */

			if (addview.getSize() != 0) {
				alertExit(getString(R.string.buy_alert_exit_detail));
			} else {
				finish();
			}
			break;
		}
		return false;
	}
	
	class ZiXunTouZhuihaoHandler extends MyHandler {

		public ZiXunTouZhuihaoHandler(HandlerMsg msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (controller != null) {
				isNoIssue(handler, controller.getRtnJSONObject());
			}
		}
	}

}
