/**
 * 
 */
package com.ruyicai.activity.buy.miss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import com.ruyicai.activity.buy.miss.AddViewMiss.CodeInfoMiss;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.constant.Constants;
import com.ruyicai.controller.Controller;
import com.ruyicai.custom.gallery.GalleryViewItem;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.json.miss.MissJson;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.MissInterface;
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
	protected ProgressDialog progressdialog;
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
	protected boolean isTen = false;
	public boolean isplw = false;// 是否排列五
	public int SCREENUM = 2;// 屏幕最大图标数
	public LinearLayout layoutView;
	public String[] mLabelArray = new String[SCREENUM];
	public AddViewMiss addView;
	protected int MAX_ZHU = 10000;
	protected int ALL_ZHU = 99;
	private final static String ERROR_WIN = "0000";
	private final int UP = 30;
	private TextView textNum;

	protected ViewPager viewPagerContainer;
	// 缓存需要左右滑动的视图群的列表容器
	public List<GalleryViewItem> itemViewArray = new ArrayList<GalleryViewItem>();

	protected void setAddViewMiss(AddViewMiss addView) {
		this.addView = addView;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflate.inflate(R.layout.buy_zixuan_activity_new,
				null);
		setContentView(view);
		layoutView = (LinearLayout) findViewById(R.id.buy_activity_layout_view);
		viewPagerContainer = (ViewPager) findViewById(R.id.buy_zixuan_viewpager);
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
	 * 初始化选区
	 */
	public abstract AreaNum[] initArea();

	/**
	 * 初始化底部
	 */
	public void initBottom() {
		mTextSumMoney = (TextView) findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) findViewById(R.id.buy_zixuan_edit_zhuma);
		mTextSumMoney.setText(getResources().getString(
				R.string.please_choose_number));
		textNum = (TextView) findViewById(R.id.buy_zixuan_add_text_num);
		Button add_dialog = (Button) findViewById(R.id.buy_zixuan_img_add_delet);
		addView.setTextNum(textNum);
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
					itemViewArray.get(1).areaNums[i].table.clearAllHighlights();
				}
				// recognizer();
			}
		});

		Button zixuanTouzhu = (Button) findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
				MobclickAgent.onEvent(ZixuanActivity.this, "touzhu");// BY贺思明
																		// 点击投注按钮
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

	public void getCodeInfo(AddViewMiss addView) {
		int zhuShu = getZhuShu();
		CodeInfoMiss codeInfo = addView.initCodeInfo(getAmt(zhuShu), zhuShu);
		setLotoNoAndType(codeInfo);
		String lotoNo = codeInfo.getLotoNo();
		String touzhuType = codeInfo.getTouZhuType();
		// Log.v("lotno", lotoNo+"3d");
		AreaNum[] areaNums = itemViewArray.get(0).areaNums;
		codeInfo.setTouZhuCode(code.zhuma(areaNums, iProgressBeishu, 0));
		boolean isFirst = true;
		for (AreaNum areaNum : areaNums) {
			int[] codes = areaNum.table.getHighlightBallNOs();
			String codeStr = "";
			for (int i = 0; i < codes.length; i++) {
				if (isTen == false) {
					codeStr += String.valueOf(codes[i]);
				} else {
					codeStr += PublicMethod.isTen(codes[i]);
				}
				if (i != codes.length - 1) {
					if (!(lotoNo.equals(Constants.LOTNO_FC3D) && touzhuType
							.equals("zhixuan"))) {
						codeStr += ",";
					}
				}
			}
			if (lotoNo.equals(Constants.LOTNO_FC3D)
					&& touzhuType.equals("zu3_danshi") && isFirst) {
				codeStr += ("," + codeStr);
				isFirst = false;
			}
			codeInfo.addAreaCode(codeStr, areaNum.textColor);
		}

		addView.addCodeInfo(codeInfo);
	}

	void setLotoNoAndType(CodeInfoMiss codeInfo) {

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
		// TODO Auto-generated method stub
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
			AreaNum areaNums2 = itemViewArray.get(1).areaNums[i];
			nBallId = iBallId;
			iBallId = iBallId - areaNums1.areaNum;
			if (iBallId < 0) {
				areaNums1.table.changeBallState(areaNums1.chosenBallSum,
						nBallId);
				areaNums2.table.changeBallState(areaNums2.chosenBallSum,
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
		app.setAddviewmiss(addView);
		Intent intent = new Intent(ZixuanActivity.this, OrderDetails.class);
		startActivity(intent);
	}


	/**
	 * 投注方法
	 */
	private void touZhu() {
		toLogin = false;
		touZhuDialog.cancel();
		initBet();
		// TODO Auto-generated method stub
		if (isGift) {
			toActivity(addView.getsharezhuma());
		} else if (isJoin) {
			toJoinActivity();
		} else if (isTouzhu) {
			touZhuNet();
		}
		clearProgress();
	}

	/**
	 * 显示追加投注
	 * 
	 * @param view
	 */
	private void initZhuiJia(View view) {
		LinearLayout toggleLinear = (LinearLayout) view
				.findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		ToggleButton zhuijiatouzhu = (ToggleButton) view
				.findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					betAndGift.setAmt(3);
					betAndGift.setIssuper("0");
				} else {
					betAndGift.setIssuper("");
					betAndGift.setAmt(2);
				}
				addView.setCodeAmt(betAndGift.getAmt());
				alertText.setText(getTouzhuAlert());
			}
		});
	}

	private void isCodeText(Button codeInfo) {
		if (addView.getSize() > 1) {
			codeInfo.setVisibility(Button.VISIBLE);
		} else {
			codeInfo.setVisibility(Button.GONE);
		}
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
		Controller.getInstance(this.getContext()).doBettingAction(handler, betAndGift);
	}

	/**
	 * 查询遗漏值联网
	 */
	public void getMissNet(final MissJson missJson, final String sellWay,
			final String type) {
		touzhuNet();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";

			@Override
			public void run() {
				str = MissInterface.getInstance().betMiss(
						betAndGift.getLotno(), sellWay);
				try {
					JSONObject obj = new JSONObject(str);
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
					missError(
							error,
							msg,
							missJson.jsonToList(type,
									obj.getJSONObject("result")));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	private void missError(String error, final String msg,
			final List<List> missList) throws JSONException {
		if (error.equals(ERROR_WIN)) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateMissView(missList);
				}

			});
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(ZixuanActivity.this, msg, Toast.LENGTH_SHORT)
							.show();
				}
			});
		}
	}

	/**
	 * 刷新遗漏值界面
	 * 
	 * @param missList
	 */
	private void updateMissView(final List<List> missList) {
		if (itemViewArray.size() > 1) {
			initMissText(itemViewArray.get(1).areaNums, missList);
		}
	}

	private void initMissText(AreaNum areaNums[], List<List> missList) {
		try {
			for (int i = 0; i < areaNums.length; i++) {
				int index = i;
				if (missList.size() > 0 && missList.size() > index) {
					PublicMethod.setMissText(areaNums[i].table.textList,
							missList.get(index));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		betAndGift.setBatchcode(Controller.getInstance(ZixuanActivity.this).toNetIssue(betAndGift.getLotno()));// 期号
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
				itemViewArray.get(1).areaNums[i].table.clearAllHighlights();
			}
			showEditText();
		}
	}

	/**
	 * 清空指定选区小球
	 */
	public void again(int position) {
		if (itemViewArray.get(0).areaNums != null) {
			itemViewArray.get(0).areaNums[position].table.clearAllHighlights();
			itemViewArray.get(1).areaNums[position].table.clearAllHighlights();
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
		Log.e("iBallId", "" + iBallId);
		isBallTable(iBallId);
		showEditText();
		changeTextSumMoney();
	}

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	/**
	 * 语音识别方法
	 */
	public void recognizer() {
		try {
			// 通过Intent传递语音识别的模式，开启语音
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			// 语言模式和自由模式的语音识别
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			// 提示语音开始
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "开始语音");
			// 开始语音识别
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "找不到语音设备", 1).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			// touZhu();
			break;

		}
		// 回调获取从谷歌得到的数据
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			// 取得语音的字符
			ArrayList<String> results = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

			String resultString = "";
			for (int i = 0; i < results.size(); i++) {
				resultString = results.get(i);
				Log.e("resultString", "" + resultString);
				String str[] = resultString.split(" ");
				for (int j = 0; j < str.length; j++) {
					try {
						isBallTable(Integer.parseInt(str[j]) - 1);
						showEditText();
						changeTextSumMoney();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
			Toast.makeText(this, resultString, 1).show();
		}
		super.onActivityResult(requestCode, resultCode, data);
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
		// Log.e("onResume===","onResume");
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Log.e("onPause===","onPause");
		if (!toLogin) {
			again();
		}
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.e("onStop===","onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// Log.e("onDestroy===","onDestroy");
	}

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		String codeStr = addView.getsharezhuma();
		clearAddView();
		for (int i = 0; i < itemViewArray.get(0).areaNums.length; i++) {
			itemViewArray.get(0).areaNums[i].table.clearAllHighlights();
			itemViewArray.get(1).areaNums[i].table.clearAllHighlights();
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
		case KeyEvent.KEYCODE_BACK:
			if (addView.getSize() != 0) {
				alertExit(getString(R.string.buy_alert_exit));
			} else {
				finish();
			}
			break;
		}
		
		return false;
	}
	
	public void showBetInfo(String text){
		
	}
}
