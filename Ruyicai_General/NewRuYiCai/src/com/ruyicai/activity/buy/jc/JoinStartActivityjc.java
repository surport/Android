/**
 * 
 */
package com.ruyicai.activity.buy.jc;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ssq.BettingSuccessActivity;
import com.ruyicai.activity.join.JoinStarShare;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;

/**
 * 发起合买
 * 
 * @author Administrator
 * 
 */
public class JoinStartActivityjc extends Activity implements HandlerMsg,
		OnCheckedChangeListener {

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
	private int allAtm;
	private int beishu;
	private int zhushu;
	private String commisionRation = "1";
	private String visible = "0";
	ProgressDialog progressdialog;
	String message;
	JSONObject obj;
	MyHandler handler = new MyHandler(this);// 自定义handler
	private Controller controller = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_start);
		getInfo();
		init();
		initRadioGroup();

	}

	public void init() {
		titleText = (TextView) findViewById(R.id.layout_join_text_title);
		atmText = (TextView) findViewById(R.id.layout_join_text_all_atm);
		zhuText = (TextView) findViewById(R.id.layout_join_text_zhushu);
		beiText = (TextView) findViewById(R.id.layout_join_text_beishu);

		renText = (TextView) findViewById(R.id.layout_join_text_rengou);
		baoText = (TextView) findViewById(R.id.layout_join_text_baodi);
		buyEdit = (EditText) findViewById(R.id.layout_join_edit_rengou);
		minEdit = (EditText) findViewById(R.id.layout_join_edit_gendan);
		safeEdit = (EditText) findViewById(R.id.layout_join_edit_baodi);
		descriptionEdit = (EditText) findViewById(R.id.layout_join_edit_description);

		buyEdit.setText("1");
		
		safeEdit.setText("0");
		minEdit.setText("1");
		deductSpinner = (Spinner) findViewById(R.id.layout_join_start_spinner);
		Button imgRetrun = (Button) findViewById(R.id.layout_join_img_return);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Button ok = (Button) findViewById(R.id.join_img_ok);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isJoin();
			}
		});
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
		beishu = Integer.parseInt(betAndGift.getLotmulti());
		allAtm = Integer.parseInt(betAndGift.getAmount()) / 100;
		/**add by pengcx 20130523 start*/
		String amount = buyEdit.getText().toString();
		renText.setText("占总额" + progress(isNull(amount), "" + allAtm)
				+ "%");// 总金额
		/**add by pengcx 20130523 end*/
		zhushu = allAtm / beishu / betAndGift.getAmt();
		titleText
				.setText("发起合买-" + PublicMethod.toLotno(betAndGift.getLotno()));
		atmText.setText("￥" + allAtm);
		zhuText.setText(zhushu + "注");
		beiText.setText(beishu + "倍");
		onEditTextClik();
	}

	public void onEditTextClik() {
		buyEdit.addTextChangedListener(new EditTextWatcher(buyEdit));
		minEdit.addTextChangedListener(new EditTextWatcher(minEdit));
		safeEdit.addTextChangedListener(new EditTextWatcher(safeEdit));
	}

	public void setEditText() {
		int buyInt = Integer.parseInt(isNull(buyEdit.getText().toString()));
		int safeInt = Integer.parseInt(isNull(safeEdit.getText().toString()));
		int minInt = Integer.parseInt(isNull(minEdit.getText().toString()));
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
		String string;
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
		Intent intent = getIntent();
		byte[] bytes = intent.getByteArrayExtra("info");
		if (bytes != null) {
			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
			try {
				ObjectInputStream objStream = new ObjectInputStream(byteStream);
				betAndGift = (BetAndGiftPojo) objStream.readObject();
			} catch (Exception e) {
			}
		}

	}

	/**
	 * 对合买对象进行赋值
	 */
	public void setPojo() {
		betAndGift.setBettype("startcase");
		// betAndGift.setTotalAmt(betAndGift.getAmount());
		// betAndGift.setAmount(""+Integer.parseInt(betAndGift.getTotalAmt())/Integer.parseInt(betAndGift.getLotmulti()));
		if (betAndGift.getIssuper().equals("0")) {
			betAndGift.setOneAmount("300");
		} else {
			betAndGift.setOneAmount("200");
		}
		// Log.v("sellway", betAndGift.getIsSellWays());
		betAndGift.setSafeAmt(isNull(PublicMethod.toFen(isNull(safeEdit
				.getText().toString()))));
		betAndGift.setBuyAmt(isNull(PublicMethod.toFen(isNull(buyEdit.getText()
				.toString()))));
		betAndGift.setMinAmt(PublicMethod.toFen(isNull(minEdit.getText()
				.toString())));
		betAndGift.setCommisionRation(commisionRation);
		betAndGift.setVisibility(visible);
		betAndGift.setBatchcode(Controller.getInstance(JoinStartActivityjc.this).toNetIssue(betAndGift.getLotno()));
		betAndGift.setDescription(descriptionEdit.getText().toString());

	}

	/**
	 * 是否发起合买
	 */
	public void isJoin() {
		int buyInt = Integer.parseInt(isNull(buyEdit.getText().toString()));
		int safeInt = Integer.parseInt(isNull(safeEdit.getText().toString()));
		int minInt = Integer.parseInt(isNull(minEdit.getText().toString()));
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
		controller = Controller.getInstance(JoinStartActivityjc.this);
		controller.doBettingJoinAction(handler, betAndGift);
	}

	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_0000()
	 */
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		if (controller != null) {
			try {
				Toast.makeText(this, controller.getRtnJSONObject().getString("message"), Toast.LENGTH_SHORT).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/**modify by pengcx 20130605 start*/
		Intent intent = new Intent(this, BettingSuccessActivity.class);
		intent.putExtra("page", BettingSuccessActivity.COOPERATION);
		intent.putExtra("lotno", betAndGift.getLotno());
		intent.putExtra("amount", betAndGift.getAmount());
		startActivity(intent);
		/**modify by pengcx 20130605 end*/
	}

	public void showfenxdialog() {
		LayoutInflater inflate = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.touzhu_succe, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).create();
		ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		Button share = (Button) view
				.findViewById(R.id.touzhu_succe_button_share);
		TextView message = (TextView) view.findViewById(R.id.touzhu_succe_text);
		message.setText("合买成功");
		image.setImageResource(R.drawable.succee);
		ok.setBackgroundResource(R.drawable.loginselecter);
		share.setBackgroundResource(R.drawable.loginselecter);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				JoinStartActivityjc.this.finish();
			}
		});
		share.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
				Intent intent = new Intent(JoinStartActivityjc.this,
						JoinStarShare.class);
				JoinStartActivityjc.this.startActivity(intent);
				JoinStartActivityjc.this.finish();
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ruyicai.handler.HandlerMsg#getContext()
	 */
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	
	/**add by yejc 20130624 start*/
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
			} else if (R.id.layout_join_edit_baodi == mEdit.getId()){
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
	/**add by yejc 20130624 end*/

}
