package com.ruyicai.activity.join;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.palmdream.RuyicaiAndroid.R.id;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.CustomizeInfoInterface;
import com.ruyicai.net.newtransaction.CustomizeInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class JoinDingActivity extends Activity {
	private String[] childtype = { "双色球", "福彩3D", "七乐彩", "大乐透", "排列三", "排列五",
			"七星彩", "22选5", "竞彩足球", "竞彩篮球", "足彩" };
	protected EditText amtEdit;
	protected EditText numEdit;
	private EditText percentEdit;
	private EditText percentMaxEdit;
	private EditText percentNumEdit;
	private TextView amtText;
	private final int MAX_BUY = 1000000;
	private final int MAX_NUM = 99;
	protected Context context;
	protected ProgressDialog mProgress;
	protected CustomizeInfo customizeInfo = new CustomizeInfo();
	protected String starterUserNo;
	protected String lotno;
	private final int LineNum = 3;
	private TextView tv_jianzhitixing;
	private TextView tv_jianzhitixing02;
	private List<RadioButton> radioBtns = new ArrayList<RadioButton>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_ding);
		context = this;
		getInfo();
		initView();
	}

	private void getInfo() {
		Intent intent = getIntent();
		starterUserNo = intent.getStringExtra(JoinInfoActivity.USER_NO);
		lotno = intent.getStringExtra(Constants.LOTNO);
		infoNet(starterUserNo, lotno);
	}

	/**
	 * 初始化组建
	 */
	private void initView() {
		initButtonLayout();
		initBuyView();
		initbottomView();
	}

	/**
	 * 初始化定制按钮
	 */
	protected void initbottomView() {
		Button buy = (Button) findViewById(R.id.join_ding_btn_buy);
		buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isLogin();
			}
		});
	}

	/**
	 * 初始化发起人信息
	 */
	private void initInfoView(String name, String crown, String grayCrown,
			String cup, String grayCup, String diamond, String grayDiamond,
			String star, String grayStar, String lotno, String person) {
		TextView nameText = (TextView) findViewById(R.id.join_detail_text_name);
		LinearLayout starLayout = (LinearLayout) findViewById(R.id.join_detail_linear_record);
		TextView personText = (TextView) findViewById(R.id.ding_text_person_id);
		TextView lotnoText = (TextView) findViewById(R.id.ding_text_lotno_id);
		nameText.append(name);
		PublicMethod.createStar(starLayout, crown, grayCrown, cup, grayCup,
				diamond, grayDiamond, star, grayStar, this, 0);
		lotnoText.append(PublicMethod.toLotno(lotno));
		personText.append(person + "人");
	}

	/**
	 * 初始化跟单彩种设置
	 */
	private void initPrizeView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.ding_group_layout_id);

		createGroupRadio(layout);
	}

	private void createGroupRadio(LinearLayout layoutMain) {
		int danNum = childtype.length;
		if (danNum > LineNum) {
			int num = danNum % LineNum;
			int line = danNum / LineNum;
			for (int i = 0; i < line; i++) {
				addLine(layoutMain, i, LineNum);
			}
			if (num != 0) {
				addLine(layoutMain, line, num);
			}
		} else {
			LinearLayout layoutOne = new LinearLayout(context);
			addLine(layoutMain, 0, danNum);
		}
	}

	/**
	 * 加载每一行的单选按钮
	 * 
	 * @param layoutMain
	 * @param line
	 * @param lineNum
	 */
	private void addLine(LinearLayout layoutMain, int line, int lineNum) {
		LinearLayout layoutOne = new LinearLayout(context);
		int id = 0;
		for (int j = 0; j < lineNum; j++) {
			id = line * this.LineNum + j;
			RadioButton radio = new RadioButton(context);
			radio.setTextColor(Color.BLACK);
			radio.setButtonDrawable(R.drawable.radio_select);
			radio.setText(childtype[id]);

			int width = PublicMethod.getDisplayWidth(context);
			if (width == 720) {
				radio.setTextSize(PublicMethod.getPxInt(7, context));
			} else if (width == 640) {
				radio.setTextSize(PublicMethod.getPxInt(7, context));
			} else if (width == 240) {
				radio.setTextSize(PublicMethod.getPxInt(20, context));
			} else if (width == 320) {
				radio.setTextSize(PublicMethod.getPxInt(15, context));
			} else if (width == 800) {
				radio.setTextSize(PublicMethod.getPxInt(8, context));
			} else {
				radio.setTextSize(PublicMethod.getPxInt(10, context));
			}
			if (Constants.SCREEN_HEIGHT == 854) {
				radio.setTextSize(PublicMethod.getPxInt(8, context));
			}
			radio.setId(id);
			radio.setPadding(PublicMethod.getPxInt(25, context), 0, 0, 0);
			int withPx = PublicMethod.getPxInt(75, context);// 将dip换算成px
			radio.setLayoutParams(new LayoutParams(withPx,
					LayoutParams.WRAP_CONTENT));
			radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						clearRadio(buttonView);
					}

				}
			});
			radioBtns.add(radio);
			layoutOne.addView(radio);
		}
		layoutMain.addView(layoutOne);
	}

	/**
	 * 清空单选按钮
	 * 
	 * @param buttonView
	 */
	private void clearRadio(CompoundButton buttonView) {
		for (RadioButton radio : radioBtns) {
			if (radio.isChecked() && radio.getId() != buttonView.getId()) {
				radio.setChecked(false);
			}
		}
	}

	/**
	 * 初始化金额跟单
	 */
	private void initGroupOneView() {
		amtEdit = (EditText) findViewById(R.id.join_ding_edit_buy);
		numEdit = (EditText) findViewById(R.id.join_ding_edit_num);
		amtText = (TextView) findViewById(R.id.join_ding_text_amt);
		CheckBox checkBox = (CheckBox) findViewById(R.id.ding_group1_check);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setForceJoin("1");
				} else {
					customizeInfo.setForceJoin("0");
				}
			}
		});
		checkBox.setChecked(true);
		setAmtText();
		amtEdit.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					if (mTextNum < 1) {
						setValueThread(amtEdit, new Handler(), 1);
					} else if (mTextNum > MAX_BUY) {
						amtEdit.setText("" + MAX_BUY);
					} else {
						setAmtText();
					}
				} else {
					setValueThread(amtEdit, new Handler(), 1);
				}
				amtEdit.setSelection(amtEdit.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		numEdit.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable edit) {
				String text = edit.toString();
				int mTextNum = 1;
				if (text != null && !text.equals("")) {
					mTextNum = Integer.parseInt(text);
					if (mTextNum < 1) {
						setValueThread(numEdit, new Handler(), 1);
					} else if (mTextNum > MAX_NUM) {
						numEdit.setText("" + MAX_NUM);
					} else {
						setAmtText();
					}
				} else {
					setValueThread(numEdit, new Handler(), 1);
				}
				numEdit.setSelection(numEdit.length());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

	}

	public void setValueThread(final EditText mTextBeishu,
			final Handler handler, final int minInt) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTextBeishu.post(new Runnable() {
					public void run() {
						String text = mTextBeishu.getText().toString();
						if (text.equals("")) {
							mTextBeishu.setText("" + minInt);
						} else if (Integer.parseInt(text) < minInt) {
							mTextBeishu.setText("" + minInt);
							setAmtText();
						}
					}
				});
			}
		}).start();
	}

	protected void setAmtText() {
		try {
			amtText.setText("" + Integer.parseInt(amtEdit.getText().toString())
					* Integer.parseInt(numEdit.getText().toString()));
		} catch (Exception e) {

		}
	}

	/**
	 * 初始化百分比跟单
	 */
	private void initGroupTwoView() {
		percentEdit = (EditText) findViewById(R.id.join_ding_radio2_edit_buy);
		percentMaxEdit = (EditText) findViewById(R.id.join_ding_radio2_edit_max_buy);
		percentNumEdit = (EditText) findViewById(R.id.join_ding_radio2_edit_num);
		CheckBox checkBox = (CheckBox) findViewById(R.id.ding_group2_check);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setForceJoin("1");
				} else {
					customizeInfo.setForceJoin("0");
				}
			}
		});
		checkBox.setChecked(true);
		PublicMethod.setEditOnclick(percentEdit, 1, 80, new Handler());
		PublicMethod.setEditOnclick(percentMaxEdit, 10, MAX_BUY, new Handler());
		PublicMethod.setEditOnclick(percentNumEdit, 1, MAX_NUM, new Handler());

		final LinearLayout layout = (LinearLayout) findViewById(R.id.ding_group2_max_layout);
		RadioGroup group = (RadioGroup) findViewById(R.id.ding_buy_group2);
		RadioButton radio0 = (RadioButton) findViewById(R.id.ding_group2_radio0);
		RadioButton radio1 = (RadioButton) findViewById(R.id.ding_group2_radio1);
		radio0.setPadding(Constants.PADDING, 0, 15, 0);
		radio1.setPadding(Constants.PADDING, 0, 15, 0);
		radio0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setMaxAmt(true);
					layout.setVisibility(View.GONE);
				}

			}
		});
		radio1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setMaxAmt(false);
					layout.setVisibility(View.VISIBLE);
				}

			}
		});
	}

	/**
	 * 跟单金额设置
	 */
	private void initBuyView() {
		final LinearLayout layoutOne = (LinearLayout) findViewById(R.id.radio_one_layout);
		final LinearLayout layoutTwo = (LinearLayout) findViewById(R.id.radio_two_layout);
		RadioGroup group = (RadioGroup) findViewById(R.id.ding_buy_group1);
		RadioButton radio0 = (RadioButton) findViewById(R.id.ding_group1_radio0);
		RadioButton radio1 = (RadioButton) findViewById(R.id.ding_group1_radio1);
		radio0.setPadding(Constants.PADDING, 0, 15, 0);
		radio1.setPadding(Constants.PADDING, 0, 15, 0);
		tv_jianzhitixing = (TextView) findViewById(R.id.tv_jianzhitixing);
		tv_jianzhitixing02 = (TextView) findViewById(R.id.tv_jianzhitixing02);
		setTextdifColor(tv_jianzhitixing02);// 设置颜色值
		setTextdifColor(tv_jianzhitixing);// 设置颜色值

		radio0.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setAmt(true);
					layoutOne.setVisibility(View.VISIBLE);
					layoutTwo.setVisibility(View.GONE);
				}

			}
		});
		radio1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					customizeInfo.setAmt(false);
					layoutOne.setVisibility(View.GONE);
					layoutTwo.setVisibility(View.VISIBLE);
				}

			}
		});
		initGroupOneView();
		initGroupTwoView();
	}

	private void setTextdifColor(TextView tv) {
		String str = getResources().getString(R.string.ding_group1_check_text);
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		// //SpannableStringBuilder实现CharSequence接口
		style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 4,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.RED), 4, str.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(style);// 将其添加到tv中

	}

	/**
	 * 初始化按钮显示隐藏
	 */
	public void initButtonLayout() {
		// final Button infoBtn = (Button) findViewById(R.id.ding_info);
		final LinearLayout infoLyoaut = (LinearLayout) findViewById(R.id.faqixinxi);
		final Button prizeBtn = (Button) findViewById(R.id.fangan);
		final LinearLayout prizeLayout = (LinearLayout) findViewById(R.id.fanganxiangqing);
		final Button buyBtn = (Button) findViewById(R.id.rengou);
		final LinearLayout buyLyoaut = (LinearLayout) findViewById(R.id.rengoushezhi);
		/*
		 * infoBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub setLayoutVisbe(infoLyoaut, infoBtn); } });
		 */
		prizeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setLayoutVisbe(prizeLayout, prizeBtn);
			}
		});
		// buyBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// setLayoutVisbe(buyLyoaut, buyBtn);
		// }
		// });

	}

	private void setLayoutVisbe(LinearLayout layout, Button button) {
		if (layout.getVisibility() == View.GONE) {
			layout.setVisibility(View.VISIBLE);
			button.setBackgroundResource(R.drawable.joininfobuttonup);
		} else if (layout.getVisibility() == View.VISIBLE) {
			layout.setVisibility(View.GONE);
			button.setBackgroundResource(R.drawable.joninfobuttonoff);
		}
	}

	/**
	 * 查询合买发起人信息
	 * 
	 * @param rechargepojo
	 */
	protected void infoNet(final String starterUserNo, final String lotno) {
		if (mProgress == null) {
			mProgress = UserCenterDialog.onCreateDialog(context);
		}
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String re = CustomizeInfoInterface.getInstance()
							.customizeNet(starterUserNo, lotno);
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals(Constants.SUCCESS_CODE)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								jsonToStr(lotno, obj);
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void jsonToStr(final String lotno, final JSONObject obj) {
		String crown = "";
		String grayCrown = "";
		String cup = "";
		String grayCup = "";
		String diamond = "";
		String grayDiamon = "";
		String star = "";
		String grayStar = "";
		try {
			String name = obj.getString("starter");
			String person = obj.getString("persons");
			JSONObject displayIcon = obj.getJSONObject("displayIcon");
			if (displayIcon.has("cup")) {
				try {
					cup = displayIcon.getString("cup");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("graycup")) {
				try {
					grayCup = displayIcon.getString("graycup");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("diamond")) {
				try {
					diamond = displayIcon.getString("diamond");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("graydiamond")) {
				try {
					grayDiamon = displayIcon.getString("graydiamond");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("goldStar")) {
				try {
					star = displayIcon.getString("goldStar");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("graygoldStar")) {
				try {
					grayStar = displayIcon.getString("graygoldStar");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("crown")) {
				try {
					crown = displayIcon.getString("crown");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (displayIcon.has("graycrown")) {
				try {
					grayCrown = displayIcon.getString("graycrown");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			initInfoView(name, crown, grayCrown, cup, grayCrown, diamond,
					grayDiamon, star, grayStar, lotno, person);
		} catch (Exception e) {

		}
	}

	/**
	 * 发起定制跟单
	 * 
	 * @param rechargepojo
	 */
	private void dingNet() {
		if (mProgress == null) {
			mProgress = UserCenterDialog.onCreateDialog(context);
		}
		mProgress.show();
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					String re = CustomizeInterface.customizeNet(customizeInfo);
					final JSONObject obj = new JSONObject(re);
					String error_code = obj.getString("error_code");
					final String message = obj.getString("message");
					closeProgress();
					if (error_code.equals(Constants.SUCCESS_CODE)) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
								//finish();
							}
						});
					} else {
						handler.post(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(context, message,
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否登录
	 */
	public void isLogin() {
		RWSharedPreferences pre = new RWSharedPreferences(context, "addInfo");
		final String userno = pre.getStringValue(ShellRWConstants.USERNO);
		if (userno == null || userno.equals("")) {
			Intent intentSession = new Intent(context, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			if (!initPojo()) {
				dingNet();
			}
		}
	}

	/**
	 * 提取跟单数据
	 */
	protected boolean initPojo() {
		boolean isNull = false;
		RWSharedPreferences pre = new RWSharedPreferences(context, "addInfo");
		String userno = pre.getStringValue(ShellRWConstants.USERNO);
		customizeInfo.setLotno(lotno);
		customizeInfo.setStarterUserNo(starterUserNo);
		customizeInfo.setUserno(userno);
		if (customizeInfo.isAmt()) {
			if (amtEdit.getText().toString().equals("")
					|| numEdit.getText().toString().equals("")) {
				Toast.makeText(context, "输入框不能为空", Toast.LENGTH_SHORT).show();
				isNull = true;
			} else {
				customizeInfo.setJoinAmt(amtEdit.getText().toString());
				customizeInfo.setTimes(numEdit.getText().toString());
				customizeInfo.setJoinType("0");
			}
		} else {
			if (percentEdit.getText().toString().equals("")
					|| ((percentNumEdit.getText().toString().equals("")) && !(this instanceof JoinModifyActivity))) {
				Toast.makeText(context, "输入框不能为空", Toast.LENGTH_SHORT).show();
				isNull = true;
			} else {
				customizeInfo.setJoinType("1");
				customizeInfo.setPercent(percentEdit.getText().toString());
				customizeInfo.setTimes(percentNumEdit.getText().toString());
			}
			if (customizeInfo.isMaxAmt()) {
				customizeInfo.setMaxAmt("0");
			} else {
				customizeInfo.setMaxAmt(percentMaxEdit.getText().toString());
			}
		}
		return isNull;
	}

	public class CustomizeInfo {
		String userno = ""; // 用户编号
		String starterUserNo = ""; // 发起人用户编号
		String times = ""; // 跟单次数
		String joinAmt = "0"; // 跟单金额
		String percent = ""; // 跟单百分比
		String maxAmt = "0"; // 百分比跟单最大金额
		String safeAmt = "0"; // 止损金额
		String joinType = "0";// 跟单类型 0:金额跟单;1:百分比跟单
		String forceJoin = "1";// 是否强制跟单 0:不强制跟单;1:强制跟单
		String lotno = "";
		String id = "";
		boolean isAmt = true;// 金额或百分比跟单
		boolean isMaxAmt = false;// 百分比跟单是否有限制 false代表有限制

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLotno() {
			return lotno;
		}

		public void setLotno(String lotno) {
			this.lotno = lotno;
		}

		public boolean isAmt() {
			return isAmt;
		}

		public void setAmt(boolean isAmt) {
			this.isAmt = isAmt;
		}

		public boolean isMaxAmt() {
			return isMaxAmt;
		}

		public void setMaxAmt(boolean isMaxAmt) {
			this.isMaxAmt = isMaxAmt;
		}

		public String getUserno() {
			return userno;
		}

		public void setUserno(String userno) {
			this.userno = userno;
		}

		public String getStarterUserNo() {
			return starterUserNo;
		}

		public void setStarterUserNo(String starter) {
			this.starterUserNo = starter;
		}

		public String getTimes() {
			return times;
		}

		public void setTimes(String times) {
			this.times = times;
		}

		public String getJoinAmt() {
			return joinAmt;
		}

		public void setJoinAmt(String joinAmt) {
			this.joinAmt = joinAmt;
		}

		public String getPercent() {
			return percent;
		}

		public void setPercent(String percent) {
			this.percent = percent;
		}

		public String getMaxAmt() {
			return maxAmt;
		}

		public void setMaxAmt(String maxAmt) {
			this.maxAmt = maxAmt;
		}

		public String getSafeAmt() {
			return safeAmt;
		}

		public void setSafeAmt(String safeAmt) {
			this.safeAmt = safeAmt;
		}

		public String getJoinType() {
			return joinType;
		}

		public void setJoinType(String joinType) {
			this.joinType = joinType;
		}

		public String getForceJoin() {
			return forceJoin;
		}

		public void setForceJoin(String forceJoin) {
			this.forceJoin = forceJoin;
		}

	}

	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case RESULT_OK:
			isLogin();
			break;
		}
	}

}
