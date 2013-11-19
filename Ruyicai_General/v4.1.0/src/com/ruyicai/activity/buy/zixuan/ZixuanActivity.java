/**
 * 
 */
package com.ruyicai.activity.buy.zixuan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
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
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.buy.BaseActivity;
import com.ruyicai.activity.buy.jixuan.DanshiJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 自选购彩父类
 * 
 * @author Administrator
 * 
 */
public abstract class ZixuanActivity extends BaseActivity implements
		OnClickListener, SeekBar.OnSeekBarChangeListener, HandlerMsg {

	private TextView mTextSumMoney;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected EditText mTextBeishu;
	private EditText mTextQishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	public int iScreenWidth;
	private CodeInterface code;// 注码接口类
	public View view;
	public Toast toast;
	private boolean toLogin = false;
	ProgressDialog progressdialog;
	public BetAndGiftPojo betAndGift = new BetAndGiftPojo();// 投注信息类
	MyHandler handler = new MyHandler(this);// 自定义handler
	String phonenum, sessionId, userno;
	public boolean isGift = false;// 是否赠送
	public boolean isJoin = false;// 是否合买
	public boolean isTouzhu = false;// 是否投注
	RadioButton check;
	RadioButton joinCheck;
	RadioButton touzhuCheck;
	String codeStr;
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	Dialog touZhuDialog;
	TextView textAlert;
	TextView textZhuma;
	TextView textTitle;
	public boolean isTen = false;
	public boolean isplw = false;// 是否排列五
	public int SCREENUM = 2;// 屏幕最大图标数
	public LinearLayout layoutView;
	public List<BuyViewItem> itemViewArray = new ArrayList<BuyViewItem>();
	public String[] mLabelArray = new String[SCREENUM];
	public AddView addView;
	protected int MAX_ZHU = 10000;
	protected int ALL_ZHU = 99;

	public void setAddView(AddView addView) {
		this.addView = addView;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflate.inflate(R.layout.buy_zixuan_activity_new,
				null);
		setContentView(view);
		layoutView = (LinearLayout) findViewById(R.id.buy_activity_layout_view);
		initBottom();

	}

	float startX;
	float startY;

	/**
	 * 初始化view
	 */
	public abstract void initViewItem();

	/**
	 * 点击小球提示金额
	 * 
	 * @param areaNum
	 * @param iProgressBeishu
	 * @return
	 */
	public abstract String textSumMoney(AreaNum areaNum[], int iProgressBeishu);

	/**
	 * 判断是否满足投注条件
	 */
	public abstract String isTouzhu();

	/**
	 * 返回注数
	 */
	public abstract int getZhuShu();

	/**
	 * 返回投注提示框提示信息
	 */
	public abstract String getZhuma();

	/**
	 * 投注联网信息
	 */
	public abstract void touzhuNet();

	/**
	 * 初始化底部
	 */
	public void initBottom() {
		mTextSumMoney = (TextView) findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) findViewById(R.id.buy_zixuan_edit_zhuma);
		mTextSumMoney.setText(getResources().getString(
				R.string.please_choose_number));
		final TextView textNum = (TextView) findViewById(R.id.buy_zixuan_add_text_num);
		Button add_dialog = (Button) findViewById(R.id.buy_zixuan_img_add_delet);
//		addView = new AddView(textNum, this);
		addView.setTextNum(textNum);
		addView.setContext(this.getContext());
		addView.setzXActivity(this);
		addView.updateTextNum();
		add_dialog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (addView.getSize() > 0) {
					showAddViewDialog();
				} else {
					Toast.makeText(
							ZixuanActivity.this,
							ZixuanActivity.this
									.getString(R.string.buy_add_dialog_alert),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Button add = (Button) findViewById(R.id.buy_zixuan_img_add);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String alertStr = isTouzhu();
				if (alertStr.equals("true")) {
					addToCodes();
				} else if (alertStr.equals("false")) {
					dialogExcessive();
				} else {
					alertInfo(alertStr);
				}
			}

		});
		Button delete = (Button) findViewById(R.id.buy_zixuan_img_delele);
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
					itemViewArray.get(0).areaNums[i].table.clearAllHighlights();
				}
			}
		});
		Button zixuanTouzhu = (Button) findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
	}

	/**
	 * 将选取信息添加到号码篮里
	 */
	private void addToCodes() {
		if (getZhuShu() > MAX_ZHU) {
			dialogExcessive();
		} else if (addView.getSize() >= ALL_ZHU) {
			Toast.makeText(
					ZixuanActivity.this,
					ZixuanActivity.this
							.getString(R.string.buy_add_view_zhu_alert),
					Toast.LENGTH_SHORT).show();
		} else {
			getCodeInfo(addView);
			addView.updateTextNum();
			again();
		}
	}

	public void getCodeInfo(AddView addView) {
		int zhuShu = getZhuShu();
		CodeInfo codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		setLotoNoAndType(codeInfo);
		String lotoNo = codeInfo.getLotoNo();
		String touzhuType = codeInfo.getTouZhuType();
		AreaNum[] areaNums = itemViewArray.get(0).areaNums;
		codeInfo.setTouZhuCode(code.zhuma(areaNums, iProgressBeishu, 0));
		boolean isFirst = true;
		for (AreaNum areaNum : areaNums) {
			int[] codes = areaNum.table.getHighlightBallNOs();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				if (isTen) {
					codeStr += PublicMethod.isTen(codes[i]);
				} else {
					codeStr += String.valueOf(codes[i]);
				}
				if (i != codes.length - 1) {
					if (!lotoNo.equals(Constants.LOTNO_QXC)
							&& !lotoNo.equals(Constants.LOTNO_PL5)
							&& !(lotoNo.equals(Constants.LOTNO_PL3) && touzhuType
									.equals("zhixuan"))) {
						codeStr += ",";
					}
				}
			}
			if (lotoNo.equals(Constants.LOTNO_PL3)
					&& touzhuType.equals("zu3_danshi") && isFirst) {
				codeStr += ("," + codeStr);
				isFirst = false;
			}
			codeInfo.addAreaCode(codeStr, areaNum.textColor);
		}
		addView.addCodeInfo(codeInfo);
	}

	void setLotoNoAndType(CodeInfo codeInfo) {

	}

	/**
	 * * isTen 显示的注码小于十的是否加零
	 */
	public void setIsTen(boolean isTen) {
		this.isTen = isTen;
	}

	/**
	 * * @param code 对应的注码类
	 */
	public void setCode(CodeInterface code) {
		this.code = code;
	}

	/**
	 * 初始化倍数和期数进度条
	 * 
	 * @param view
	 */
	public void initImageView(View view) {
		mSeekBarBeishu = (SeekBar) view
				.findViewById(R.id.buy_zixuan_seek_beishu);
		mTextBeishu = (EditText) view.findViewById(R.id.buy_zixuan_text_beishu);
		mTextQishu = (EditText) view.findViewById(R.id.buy_zixuan_text_qishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) view.findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

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
				mSeekBarBeishu, true, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,
				true, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,
				mSeekBarQishu, false, view);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,
				false, view);
	}

	/**
	 * 投注方法
	 */
	public void beginTouZhu() {

		toLogin = false;
		String alertStr = isTouzhu();
		if (alertStr.equals("true") && addView.getSize() == 0) {
			if (getZhuShu() > MAX_ZHU) {
				dialogExcessive();
			} else if (addView.getSize() >= ALL_ZHU) {
				Toast.makeText(
						ZixuanActivity.this,
						ZixuanActivity.this
								.getString(R.string.buy_add_view_zhu_alert),
						Toast.LENGTH_SHORT).show();
			} else {
				addToCodes();
				alert();
			}
		} else if (alertStr.equals("true") && addView.getSize() > 0) {
			addToCodes();
			showAddViewDialog();
		} else if (addView.getSize() > 0) {
			showAddViewDialog();
		} else if (alertStr.equals("false")) {
			dialogExcessive();
		} else if (alertStr.equals("no")) {
			dialogZhixuan();
		} else {
			alertInfo(alertStr);
		}

	}

	private void showAddViewDialog() {
		addView.createDialog(ZixuanActivity.this
				.getString(R.string.buy_add_dialog_title));
		addView.showDialog();
	}

	public int getAmt(int zhuShu) {
		if (betAndGift != null) {
			return zhuShu * betAndGift.getAmt();
		} else {
			return 0;
		}

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
			final SeekBar mSeekBar, final boolean isBeiShu, View view) {
		ImageButton subtractBeishuBtn = (ImageButton) view.findViewById(idFind);
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
	 * seekBar组件的监听器
	 */
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
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			stateCheck();
			break;
		default:
			break;
		}
		alertText.setText(getTouzhuAlert());

	}

	/**
	 * 计算注数和金额的方法
	 * 
	 */
	public void changeTextSumMoney() {
		String text = textSumMoney(itemViewArray.get(0).areaNums,
				iProgressBeishu);
		if (toast == null) {
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		} else {
			toast.setText(text);
			toast.show();
		}

	}

	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId) {
		int nBallId = 0;
		for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
			AreaNum areaNums1 = itemViewArray.get(0).areaNums[i];
			nBallId = iBallId;
			iBallId = iBallId - areaNums1.areaNum;
			if (iBallId < 0) {
				areaNums1.table.changeBallState(areaNums1.chosenBallSum,
						nBallId);
				break;
			}

		}

	}

	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < itemViewArray.get(0).areaNums.length; j++) {
			BallTable ballTable = itemViewArray.get(0).areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j != 0) {
				zhumas += " | ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (isTen) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				} else {
					zhumas += zhuMa[i] + "";
				}

				if (i != ballTable.getHighlightBallNOs().length - 1) {
					zhumas += ",";
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
		} else {
			builder.append(zhumas);
			String zhuma[] = zhumas.split("\\|");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK),
							length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(
						itemViewArray.get(0).areaNums[i].textColor), length
						- zhuma[i].length(), length, Spanned.SPAN_COMPOSING);

			}
			editZhuma.setText(builder, BufferType.EDITABLE);
		}
	}

	/**
	 * 单复式投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alert() {
		touzhuNet();// 投注类型和彩种
		toLogin = true;
		isTouzhu = true;
		ApplicationAddview app = (ApplicationAddview) getApplicationContext();
		app.setPojo(betAndGift);
		app.setAddview(addView);
		Intent intent = new Intent(ZixuanActivity.this, OrderDetails.class);
		startActivity(intent);
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
	 * 判断期数是否大于1
	 */
	public void stateCheck() {
		if (iProgressQishu > 1) {
			isGift = false;
			isJoin = false;
			isTouzhu = true;
			check.setVisibility(CheckBox.GONE);
			joinCheck.setVisibility(CheckBox.GONE);
			touzhuCheck.setVisibility(CheckBox.GONE);
			textAlert.setVisibility(TextView.VISIBLE);
		} else {
			check.setVisibility(CheckBox.VISIBLE);
			joinCheck.setVisibility(CheckBox.VISIBLE);
			touzhuCheck.setVisibility(CheckBox.VISIBLE);
			textAlert.setVisibility(TextView.GONE);
		}
	}

	public void toActivity(String zhuma) {
		clearAddView();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betAndGift);
		} catch (IOException e) {
			return;
		}

		Intent intent = new Intent(ZixuanActivity.this, GiftActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		intent.putExtra("zhuma", zhuma);
		startActivity(intent);

	}

	public void toJoinActivity() {
		clearAddView();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
			objStream.writeObject(betAndGift);
		} catch (IOException e) {
			return; // should not happen, so donot do error handling
		}

		Intent intent = new Intent(ZixuanActivity.this, JoinStartActivity.class);
		intent.putExtra("info", byteStream.toByteArray());
		startActivity(intent);

	}

	/**
	 * 投注联网
	 */
	public void touZhuNet() {
		Controller.getInstance(this.getContext()).doBettingAction(handler,
				betAndGift);
	}

	/**
	 * 初始化投注信息
	 */
	public void initBet() {
		betAndGift.setIsSellWays("1");
		betAndGift.setAmount("" + addView.getAllAmt() * iProgressBeishu * 100);
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift
		betAndGift.setLotmulti("" + iProgressBeishu);// lotmulti 倍数 投注的倍数
		betAndGift.setBatchnum("" + iProgressQishu);// batchnum 追号期数 默认为1（不追号）
		betAndGift.setBet_code(addView.getTouzhuCode(iProgressBeishu,
				betAndGift.getAmt() * 100));
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(Controller.getInstance(ZixuanActivity.this)
				.toNetIssue(betAndGift.getLotno()));// 期号
	}

	/**
	 * 投注提示框中的信息
	 */
	public String getTouzhuAlert() {

		return "注数：" + addView.getAllZhu() + "注     " + "金额：" + +iProgressQishu
				* addView.getAllAmt() * iProgressBeishu + "元";
	}

	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}

				});
		dialog.show();

	}

	/**
	 * 单笔投注大于1万注时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ZixuanActivity.this);
		builder.setTitle(ZixuanActivity.this.getString(
				R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于" + MAX_ZHU + "注！");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	// wangyl 7.23 福彩3D投注注数大于600注时的对话框
	/**
	 * 福彩3D直选投注注数大于600注时的对话框
	 */
	public void dialogZhixuan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ZixuanActivity.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title)
				.toString());
		builder.setMessage("请选择不大于600注投注");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	/**
	 * 重新方法
	 * 
	 */
	public void again() {
		if (itemViewArray.get(0).areaNums != null) {
			for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
				itemViewArray.get(0).areaNums[i].table.clearAllHighlights();
				// itemViewArray.get(1).areaNums[i].table.clearAllHighlights();
			}
			showEditText();
		}
	}

	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int iBallId = v.getId();
		isBallTable(iBallId);
		showEditText();
		changeTextSumMoney();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
		if (!toLogin) {
			again();
		}
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		String codeStr = addView.getsharezhuma();
		clearAddView();
		for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
			itemViewArray.get(0).areaNums[i].table.clearAllHighlights();
			// itemViewArray.get(1).areaNums[i].table.clearAllHighlights();
		}

		showEditText();
		PublicMethod.showDialog(ZixuanActivity.this, lotno + codeStr);

	}

	private void clearAddView() {
		addView.clearInfo();
		addView.updateTextNum();
	}

	public void errorCode_000000() {
		// TODO Auto-generated method stub

	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
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
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
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
			if (addView.getSize() != 0) {
				alertExit(getString(R.string.buy_alert_exit));
			} else {
				finish();
			}
			break;
		}
		return false;
	}

	public void showBetInfo(String text) {

	}
}
