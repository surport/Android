package com.ruyicai.activity.game.ssq;

/**
 * @Title 双色球玩法 Activity类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

import java.util.Iterator;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.dialog.Accoutdialog;
import com.ruyicai.dialog.ChooseNumberDialogSSQ;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.net.transaction.BettingInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;
import com.ruyicai.view.OneBallView;
import com.ruyicai.view.RadioStateDrawable;
import com.ruyicai.view.TabBarButton;

public class SsqActivity extends Activity implements OnClickListener,// 小球被点击
		// onClick
		SeekBar.OnSeekBarChangeListener, // SeekBar改变 onProgressChanged
		RadioGroup.OnCheckedChangeListener, // 机选复选框变化 onCheckedChangeListener
		MyDialogListener { // 对话框输入数值 onOKClick

	// 小球图的宽高
	public static final int BALL_WIDTH = 55;
	// 顶部标签个数
	private int iButtonNum = 3;

	// 首行button当前的位置
	private int iCurrentButton; // 0x55660001 --- SSQ danshi
	private int tempCurrentButton;
	private int iCurrentButton_menu;
	/*
	 * Button danshiButton; Button fushiButton; Button dantuoButton;
	 */
	public static final int WANFA_START_ID = 0x55550001;
	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;

	private int defaultOffShade;
	private int defaultOnShade;

	int topButtonStringId[] = { R.string.dantuo };
	int topButtonIdOn[] = { R.drawable.goucai_b, R.drawable.jixuan_b,
			R.drawable.dantuo_b };
	int topButtonIdOff[] = { R.drawable.goucai, R.drawable.jixuan,
			R.drawable.dantuo };

	// 小球起始ID
	public static final int RED_BALL_START = 0x80000001;
	public static final int RED_TUO_BALL_START = 0x82000001;
	public static final int BLUE_BALL_START = 0x81000001;
	private static final int DIALOG1_KEY = 0;// 进度条的值2010/7/4
	ProgressDialog progressdialog;

	SeekBar mSeekBarBeishu;
	SeekBar mSeekBarQishu;

	TextView mTextBeishu;
	TextView mTextQishu;

	TextView mTextSumMoney;

	ImageButton ssq_b_touzhu_danshi;
	ImageButton ssq_b_touzhu_fushi;
	ImageButton ssq_b_touzhu_dantuo;
	Button ssq_btn_newSelect;
	ImageButton imgJixuan;
	int iProgressBeishu = 1;
	int iProgressQishu = 1;

	private int iScreenWidth;


	private int redBallResId[] = { R.drawable.grey, R.drawable.red };

	BallTable redBallTable = null;// 红球ball table
	BallTable redTuoBallTable = null;

	BallTable blueBallTable = null;
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };

	LinearLayout buyView;

	// 复式 小球个数（机选）
	private int iFushiRedBallNumber;
	private int iFushiBlueBallNumber;

	// 胆拖 小球个数（机选）
	private int iDantuoRedDanNumber;
	private int iDantuoRedTuoNumber;
	private int iDantuoBlueNumber;

	// 注码
	public static int zhuma[] = null;
	// 不计算倍数的当前注数 用于构造请求
	private long iSendZhushu = 0;
	public int publicTopButton;
	public int type;
	public static final int ZHIXUAN = 0;
	public static final int DANTUO = 1;
	private EditText redEdit;
	private EditText redDanEdit;
	private EditText redTuoEdit;
	private EditText blueEdit;
	private boolean isJiXuanDanTuo = true;// 是否是胆拖机选
	private boolean isJiXuanZhiXuan = false;// 是否是直选机选
	private Vector<Balls> balls = new Vector();
	private LinearLayout zhuView;
	private Spinner jixuanZhu;
	private String endTime;
	private String issue[] = new String[2];
	private TextView term;
	private TextView title;
	private TextView time;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	int width;
	/** Called when the activity is first created. */

	/**
	 * 消息处理函数
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				break;
			case 1:
				progressdialog.dismiss();
				if(isFinishing() == false){
				    Accoutdialog.getInstance().createAccoutdialog(SsqActivity.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
				}
				// //需要添加AlertDialog提示注册成功
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "系统结算，请稍后！",Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "无空闲逻辑机！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示该号被暂停请联系客服
				break;

			// //需要添加AlertDialog注册失败

			case 6:
				// //需要添加AlertDialog提示用户登录成功

				progressdialog.dismiss();
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				// 投注成功后清除小球 陈晨 20100728
				if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
					if (isJiXuanZhiXuan) {
						zhuView.removeAllViews();
					} else {
						redEdit.setText("");
						blueEdit.setText("");
						redBallTable.clearAllHighlights();
						blueBallTable.clearAllHighlights();
						changeTextSumMoney();
					}
				} else if (iCurrentButton == PublicConst.BUY_SSQ_DANTUO ) {
					redDanEdit.setText("");
					redTuoEdit.setText("");
					blueEdit.setText("");
					redBallTable.removeView();
					blueBallTable.removeView();
					redTuoBallTable.removeView();
					changeTextSumMoney();
				}
				if(isFinishing() == false){
				    PublicMethod.showDialog(SsqActivity.this);
				}
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(SsqActivity.this,UserLogin.class);
				startActivity(intentSession);
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG).show();
				// // tv.setText(result);
				break;
			case 10:
				// 将图片的背景设置回原来的图案表示可点击
				if (iCurrentButton == PublicConst.BUY_SSQ_DANSHI) {
					ssq_b_touzhu_danshi
							.setImageResource(R.drawable.imageselecter);
				} else if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
					ssq_b_touzhu_fushi
							.setImageResource(R.drawable.imageselecter);
				} else if (iCurrentButton == PublicConst.BUY_SSQ_DANTUO) {
					ssq_b_touzhu_dantuo
							.setImageResource(R.drawable.imageselecter);
				}
				break;
			}
			//	
		}
	};


	/*
	 * @return void
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		// 获取期号和截止时间
		JSONObject ssqLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(Constants.LOTNO_SSQ);
		if (ssqLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				issue[0] = ssqLotnoInfo.getString("batchCode");
				issue[1] = ssqLotnoInfo.getString("endTime");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			term.setText("第" + this.issue[0] + "期");
			time.setText("截止时间：" + this.issue[1]);
		} else {
			// 没有获取到期号信息,重新联网获取期号
			issue[0] = "";
			issue[1] = "";
		    PublicMethod.getIssue(Constants.LOTNO_SSQ,term,time,new Handler());
		}
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initButtons();
		iCurrentButton = PublicConst.BUY_SSQ_ZHIXUAN;
		createBuyView(iCurrentButton);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensor.stopAction();
	}

	@Override
	public Object getLastNonConfigurationInstance() {
		return super.getLastNonConfigurationInstance();
	}

	/**
	 * 创建购彩View，按照顶部按钮的状态创建
	 * @param int aWhichBuy 对应View的Id
	 * @return void
	 */
	private void createBuyView(int aWhichBuy) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		publicTopButton = aWhichBuy;
		switch (aWhichBuy) {

		case PublicConst.BUY_SSQ_ZHIXUAN: // 直选
			if (isJiXuanZhiXuan) {
				sensor.startAction();
				create_SSQ_ZHIXUAN_JIXUAN();
			} else {
				sensor.stopAction();
				create_SSQ_ZHIXUAN();
			}
			topButtonGroup.setSelected(false);
			commit();
			break;
		case PublicConst.BUY_SSQ_DANTUO: // 胆拖

			if (isJiXuanDanTuo) {

				create_SSQ_DANTUO_JIXUAN();

			} else {

				create_SSQ_DANTUO();

			}
			topButtonGroup.setSelected(false);
			commit();
			break;
		default:
			break;
		}

	}

	/*
	 * 创建胆拖购彩View
	 * 
	 * @return void
	 */
	private void create_SSQ_DANTUO() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_ssq_dantuo, null);
			title.setText("双色球胆拖");

			final LinearLayout redDanArea = (LinearLayout) iV
					.findViewById(R.id.ssq_dantuo_linear_red_danma);
			final LinearLayout redTuoArea = (LinearLayout) iV
					.findViewById(R.id.ssq_dantuo_linear_red_tuoma);
			final LinearLayout blueArea = (LinearLayout) iV
					.findViewById(R.id.ssq_dantuo_linear_blue);
			blueArea.setVisibility(LinearLayout.GONE);
			redTuoArea.setVisibility(LinearLayout.GONE);
			final TextView redDanText = (TextView) iV.findViewById(R.id.ssq_dantuo_text_red_danma);
			final TextView redTuoText = (TextView) iV
					.findViewById(R.id.ssq_dantuo_text_red_tuoma);
			final TextView blueText = (TextView) iV
					.findViewById(R.id.ssq_dantuo_text_blue);
			redDanEdit = (EditText) iV
					.findViewById(R.id.ssq_dantuo_edit_red_danma);
			redTuoEdit = (EditText) iV
					.findViewById(R.id.ssq_dantuo_edit_red_tuoma);
			blueEdit = (EditText) iV.findViewById(R.id.ssq_dantuo_edit_blue);

			redDanEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if (hasFocus) {
						redDanArea.setVisibility(LinearLayout.VISIBLE);
						redTuoArea.setVisibility(LinearLayout.GONE);
						blueArea.setVisibility(LinearLayout.GONE);
						redDanEdit.setBackgroundResource(R.drawable.hongkuang);
						redDanText.setTextColor(Color.RED);
					} else {
						redDanEdit.setBackgroundResource(R.drawable.huikuang);
						redDanText.setTextColor(Color.BLACK);
					}

				}
			});
			redTuoEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {

					if (hasFocus) {
						redDanArea.setVisibility(LinearLayout.GONE);
						redTuoArea.setVisibility(LinearLayout.VISIBLE);
						blueArea.setVisibility(LinearLayout.GONE);
						redTuoEdit.setBackgroundResource(R.drawable.hongkuang);
						redTuoText.setTextColor(Color.RED);
					} else {
						redTuoEdit.setBackgroundResource(R.drawable.huikuang);
						redTuoText.setTextColor(Color.BLACK);
					}
				}
			});
			blueEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {

					if (hasFocus) {
						redDanArea.setVisibility(LinearLayout.GONE);
						redTuoArea.setVisibility(LinearLayout.GONE);
						blueArea.setVisibility(LinearLayout.VISIBLE);
						blueEdit.setBackgroundResource(R.drawable.lankuang);
						blueText.setTextColor(Color.BLUE);
					} else {
						blueEdit.setBackgroundResource(R.drawable.huikuang);
						blueText.setTextColor(Color.BLACK);
					}
				}
			});

			int redBallViewNum = 33;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			PublicMethod.recycleBallTable(redBallTable);//创建新球之前先做回收工作
			redBallTable = PublicMethod.makeBallTable(iV, R.id.table_red_danma,iScreenWidth, redBallViewNum, redBallResId, RED_BALL_START,1, this, this);
			
			PublicMethod.recycleBallTable(redTuoBallTable);
			redTuoBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_red_tuoma, iScreenWidth, redBallViewNum,
					redBallResId, RED_TUO_BALL_START, 1, this, this);

			int blueBallViewNum = 16;
			
			PublicMethod.recycleBallTable(blueBallTable);
			blueBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_blue_dantuo, iScreenWidth, blueBallViewNum,
					blueBallResId, BLUE_BALL_START, 1, this, this);
			mTextSumMoney = (TextView) iV
					.findViewById(R.id.ssq_dantuo_text_sum_money);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV
					.findViewById(R.id.ssq_dantuo_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV
					.findViewById(R.id.ssq_dantuo_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV
					.findViewById(R.id.ssq_dantuo_text_beishu_change);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV
					.findViewById(R.id.ssq_dantuo_text_qishu_change);
			mTextQishu.setText("" + iProgressQishu);
			// true - 倍数 false-期数
			setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_subtract_beishu, iV,
					-1, mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_add_qihao, iV, 1,
					mSeekBarQishu, false);

			ssq_b_touzhu_dantuo = (ImageButton) iV
					.findViewById(R.id.ssq_dantuo_b_touzhu);
			ssq_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}
			});
		ImageButton newSelect = (ImageButton) iV
				.findViewById(R.id.ssq_dantuo_new_select);
		newSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 投注成功后清除小球 陈晨 20100728
				redTuoEdit.setText("");
				redDanEdit.setText("");
				blueEdit.setText("");

				redBallTable.clearAllHighlights();
				blueBallTable.clearAllHighlights();
				redTuoBallTable.clearAllHighlights();
				changeTextSumMoney();

			}
		});
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/*
	 * 创建直选购彩View
	 * 
	 * @return void
	 */
	private void create_SSQ_ZHIXUAN() {
		
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_ssq_zhixuan, null);
		title.setText("双色球直选");
		int redBallViewNum = 33;
		iScreenWidth = PublicMethod.getDisplayWidth(this);

		mTextSumMoney = (TextView) iV.findViewById(R.id.ssq_zhixuan_text_sum_money);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
		PublicMethod.recycleBallTable(redBallTable);
		redBallTable = PublicMethod.makeBallTable(iV,R.id.ssq_zhixuan_table_red, iScreenWidth, redBallViewNum,redBallResId, RED_BALL_START, 1, this, this);
		int blueBallViewNum = 16;
		
		PublicMethod.recycleBallTable(blueBallTable);
		blueBallTable = PublicMethod.makeBallTable(iV,R.id.ssq_zhixuan_table_blue, iScreenWidth, blueBallViewNum,blueBallResId, BLUE_BALL_START, 1, this, this);
		
		final LinearLayout redArea = (LinearLayout) iV
				.findViewById(R.id.ssq_zhixuan_linear_red);
		final LinearLayout blueArea = (LinearLayout) iV
				.findViewById(R.id.ssq_zhixuan_linear_blue);
		blueArea.setVisibility(LinearLayout.GONE);
		final TextView redText = (TextView) iV
				.findViewById(R.id.ssq_zhixuan_text_red);
		final TextView blueText = (TextView) iV
				.findViewById(R.id.ssq_zhixuan_text_blue);

		View.OnFocusChangeListener redChooseListener = new OnFocusChangeListener() {
			// 设置 红球选择框
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					redEdit.setBackgroundResource(R.drawable.hongkuang);
					redArea.setVisibility(LinearLayout.VISIBLE);
					blueArea.setVisibility(LinearLayout.GONE);
					redText.setTextColor(Color.RED);
				} else {
					redEdit.setBackgroundResource(R.drawable.huikuang);
					redText.setTextColor(Color.BLACK);
				}
			}
		};
			
		View.OnFocusChangeListener blueChooseListener = new OnFocusChangeListener() {
			// 设置篮球的选择框
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (hasFocus) {
					blueEdit.setBackgroundResource(R.drawable.lankuang);
					redArea.setVisibility(LinearLayout.GONE);
					blueArea.setVisibility(LinearLayout.VISIBLE);
					blueText.setTextColor(Color.BLUE);
				} else {
					blueEdit.setBackgroundResource(R.drawable.huikuang);
					blueText.setTextColor(Color.BLACK);
				}
			}
		};

		redEdit = (EditText) iV.findViewById(R.id.ssq_zhixuan_edit_red);
		blueEdit = (EditText) iV.findViewById(R.id.ssq_zhixuan_edit_blue);
		redEdit.setOnFocusChangeListener(redChooseListener);
		blueEdit.setOnFocusChangeListener(blueChooseListener);

		mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_beishu, iV, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_beishu, iV, 1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_qihao, iV, -1,
				mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_qishu, iV, 1,
				mSeekBarQishu, false);
		ssq_b_touzhu_fushi = (ImageButton) iV
				.findViewById(R.id.ssq_fushi_b_touzhu);
		ssq_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton newSelect = (ImageButton) iV
				.findViewById(R.id.ssq_zhixuan_new_select);
		newSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 投注成功后清除小球 陈晨 20100728
				redEdit.setText("");
				blueEdit.setText("");
				redBallTable.clearAllHighlights();
				blueBallTable.clearAllHighlights();
				changeTextSumMoney();

			}
		});
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
	}

	/**
	 * 付磊 胆拖机选
	 */
	private void create_SSQ_DANTUO_JIXUAN() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_ssq_dantuo_jixuan, null);

		title.setText("双色球胆拖机选");
		
		PublicMethod.recycleBallTable(redBallTable);
		redBallTable = new BallTable(iV, R.id.table_red_danma, RED_BALL_START);
		
		PublicMethod.recycleBallTable(redTuoBallTable);
		redTuoBallTable = new BallTable(iV, R.id.table_red_tuoma,RED_TUO_BALL_START);
		
		PublicMethod.recycleBallTable(blueBallTable);
		blueBallTable = new BallTable(iV, R.id.table_blue_dantuo,BLUE_BALL_START);

		mTextSumMoney = (TextView) iV.findViewById(R.id.ssq_dantuo_text_sum_money);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));

		mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.ssq_dantuo_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		mSeekBarQishu = (SeekBar) iV.findViewById(R.id.ssq_dantuo_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) iV
				.findViewById(R.id.ssq_dantuo_text_beishu_change);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) iV
				.findViewById(R.id.ssq_dantuo_text_qishu_change);
		mTextQishu.setText("" + iProgressQishu);
		// true - 倍数 false-期数
		setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_subtract_beishu, iV, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_add_beishu, iV, 1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_subtract_qihao, iV, -1,
				mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.ssq_dantuo_seekbar_add_qihao, iV, 1,
				mSeekBarQishu, false);

		ssq_b_touzhu_dantuo = (ImageButton) iV
				.findViewById(R.id.ssq_dantuo_b_touzhu);
		ssq_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		imgJixuan = (ImageButton) iV.findViewById(R.id.ssq_dantuo_img_jixuan);

		imgJixuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChooseNumberDialogSSQ iChooseNumberDialog = new ChooseNumberDialogSSQ(
						SsqActivity.this, 1, SsqActivity.this);
				iChooseNumberDialog.show();
			}
		});
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		ChooseNumberDialogSSQ iChooseNumberDialog = new ChooseNumberDialogSSQ(
				SsqActivity.this, 1, SsqActivity.this);
		iChooseNumberDialog.show();

	}

	/**
	 * 付磊 直选机选
	 */
	private void create_SSQ_ZHIXUAN_JIXUAN() {
		buyView.removeAllViews();
		Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT).show();
		balls = new Vector<Balls>();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_zhixuan_jixuan, null);

			title.setText("双色球机选");
			// 初始化spinner
			jixuanZhu = (Spinner) iV.findViewById(R.id.layout_zhixuan_jixuan_spinner);
			jixuanZhu.setSelection(4);
			jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = jixuanZhu.getSelectedItemPosition();
					if (isOnclik) {
						zhuView.removeAllViews();
						balls = new Vector();
						for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
							Balls ball = new Balls();
							balls.add(ball);
						}
						createTable(zhuView);
					} else {
						isOnclik = true;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
			zhuView = (LinearLayout) iV.findViewById(R.id.layout_zhixuan_linear_zhuma);

			int index = jixuanZhu.getSelectedItemPosition() + 1;
			for (int i = 0; i < index; i++) {
				Balls ball = new Balls();
				balls.add(ball);
			}

			createTable(zhuView);
			sensor.onVibrator();// 震动
		mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		mSeekBarQishu = (SeekBar) iV.findViewById(R.id.ssq_fushi_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) iV.findViewById(R.id.ssq_fushi_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_beishu, iV, -1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_beishu, iV, 1,
				mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_subtract_qihao, iV, -1,
				mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.ssq_fushi_seekbar_add_qishu, iV, 1,
				mSeekBarQishu, false);
		ssq_b_touzhu_fushi = (ImageButton) iV
				.findViewById(R.id.ssq_fushi_b_touzhu);
		ssq_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton jixuan = (ImageButton) iV
				.findViewById(R.id.jixuan_new_select);
		jixuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				zhuView.removeAllViews();
				balls = new Vector();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = new Balls();
					balls.add(ball);
				}
				createTable(zhuView);
			}
		});
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
	}

	/**
	 * 初始化顶部的button
	 * 
	 * @return void
	 */
	private void initButtons() {

		initTopButtons();
		commit();

	}

	private void initTopButtons() {
		// topBar = (HorizontalScrollView) findViewById(R.id.topBar);
		topButtonGroup = (RadioGroup) findViewById(R.id.topMenu);

		defaultOffShade = RadioStateDrawable.SHADE_GRAY;
		defaultOnShade = RadioStateDrawable.SHADE_YELLOW;
		topButtonGroup.setOnCheckedChangeListener(this);
		topButtonLayoutParams = new RadioGroup.LayoutParams(
		/* 320/5 */RadioGroup.LayoutParams.FILL_PARENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 添加顶部按钮，并显示单式玩法View
	 * 
	 * @return void
	 */
	private void commit() {
		
//		topButtonGroup.removeAllViews();
		
		if (isJiXuanDanTuo) {
			topButtonIdOn[2] = R.drawable.dantuo_b;
			topButtonIdOff[2] = R.drawable.dantuo;
		} else {
			topButtonIdOn[2] = R.drawable.jixuan_b;
			topButtonIdOff[2] = R.drawable.jixuan;
		}
		if (isJiXuanZhiXuan) {
			topButtonIdOn[1] = R.drawable.zhixuan_b;
			topButtonIdOff[1] = R.drawable.zhixuan;
		} else {
			topButtonIdOn[1] = R.drawable.jixuan_b;
			topButtonIdOff[1] = R.drawable.jixuan;
		}
		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width = screen_width / iButtonNum;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < iButtonNum; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			// topButtonIdOff[i], this, 160, 72);
			tabButton.setState(topButtonIdOn[i], topButtonIdOff[i], 3);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			topButtonGroup.addView(tabButton, i, topButtonLayoutParams);

		}
	}

	/**
	 * 设置顶部按钮的位置
	 * 
	 * @param int index 顶部按钮的索引
	 * 
	 * @return void
	 */
	public void setCurrentTab(int index) {

		switch (index) {
		case 0:// 购彩
			break;
		case 1:// 直选
			iCurrentButton = PublicConst.BUY_SSQ_ZHIXUAN;
			createBuyView(iCurrentButton);
			break;
		case 2: // 胆拖
			iCurrentButton = PublicConst.BUY_SSQ_DANTUO;
			createBuyView(iCurrentButton);
			break;
		case 3:// 合买

			break;
		}
	}

	/**
	 * 顶部按钮变化的回调函数
	 * 
	 * @param RadioGroup
	 *            group 这个参数不用管它
	 * 
	 * @param int checkedId 高亮button的id
	 * 
	 * @return void
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

		switch (checkedId) {
		case 0:// 购彩
			sensor.stopAction();
			Intent intent1 = new Intent(SsqActivity.this, RuyicaiAndroid.class);
			startActivity(intent1);
			finish();
			break;
		case 1:// 直选
			iCurrentButton = PublicConst.BUY_SSQ_ZHIXUAN;
			isJiXuanZhiXuan = !isJiXuanZhiXuan;
			if (!isJiXuanZhiXuan && !isJiXuanDanTuo) {
				isJiXuanDanTuo = true;
			}
			createBuyView(iCurrentButton);
			break;
		case 2: // 胆拖
			sensor.stopAction();
			iCurrentButton = PublicConst.BUY_SSQ_DANTUO;
			isJiXuanDanTuo = !isJiXuanDanTuo;
			if (!isJiXuanZhiXuan && !isJiXuanDanTuo) {
				isJiXuanZhiXuan = true;
			}
			createBuyView(iCurrentButton);
			break;
		case 3:// 合买
			// sensor.stopAction();
			// Intent intent = new Intent(SsqActivity.this, JoinHall.class);
			// startActivity(intent);
			// topButtonGroup.check(1);
			break;
		}
	}

	/**
	 * 获取当前高亮button的id
	 * 
	 * @return int id
	 */
	public int getCurrentTab() {
		return topButtonGroup.getCheckedRadioButtonId();
	}

	/**
	 * SeekBar改变时的回调函数
	 * 
	 * @param SeekBar
	 *            seekBar 发生变化的SeekBar实例
	 * 
	 * @param int progress 变化后的位置值
	 * 
	 * @param boolean fromUser 这个参数不用管它
	 * 
	 * @return void
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		// 单式
		case R.id.ssq_fushi_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			if (!isJiXuanZhiXuan)
				changeTextSumMoney();
			break;
		case R.id.ssq_dantuo_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.ssq_fushi_seek_qishu:
		case R.id.ssq_dantuo_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
	}

	// Seekbar回调函数 暂时不用

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	// Seekbar回调函数 暂时不用

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * 响应小球被点击的回调函数
	 * 
	 * @param View
	 *            v 被点击的view
	 * @return void
	 */
	@Override
	public void onClick(View v) {
		int iBallId = v.getId(); //

		if (iBallId >= RED_BALL_START && iBallId < BLUE_BALL_START) {
			int iBallViewId = v.getId() - RED_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 0, iBallViewId);// 点红球组
			}
		} else if (iBallId >= BLUE_BALL_START && iBallId < RED_TUO_BALL_START) {
			int iBallViewId = v.getId() - BLUE_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 1, iBallViewId);// 点蓝球组
			}
		} else {
			int iBallViewId = v.getId() - RED_TUO_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 2, iBallViewId);// 点蓝球组
			}
		}

		changeTextSumMoney();
	}

	/**
	 * 双色球注数的计算方法
	 * 
	 * @return int 返回注数
	 */
	private int getZhuShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_SSQ_DANSHI:
			iReturnValue = mSeekBarBeishu.getProgress();
			iSendZhushu = 1;
			break;
		case PublicConst.BUY_SSQ_ZHIXUAN:
			int iRedBalls = redBallTable.getHighlightBallNums();
			int iBlueBalls = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getSSQFSZhuShu(iRedBalls, iBlueBalls);
			break;
		case PublicConst.BUY_SSQ_DANTUO:
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getSSQDTZhuShu(iRedHighlights,
					iRedTuoHighlights, iBlueHighlights);
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	/**
	 * 双色球期数的计算方法
	 * 
	 * @return int 返回注数
	 */
	private int getQiShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_SSQ_DANSHI:
			iReturnValue = mSeekBarQishu.getProgress();
			iSendZhushu = 1;
			break;
		case PublicConst.BUY_SSQ_ZHIXUAN:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		case PublicConst.BUY_SSQ_DANTUO:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	/**
	 * 胆拖玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * @param int aRedTuoBalls 红球拖码个数
	 * @param int aBlueBalls 篮球个数
	 * @return long 注数
	 */
	private long getSSQDTZhuShu(int aRedBalls, int aRedTuoBalls, int aBlueBalls) {// 得到双色球胆拖的注数
		long ssqZhuShu = 0L;
		iSendZhushu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0 && aBlueBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(6 - aRedBalls, aRedTuoBalls)
					* PublicMethod.zuhe(1, aBlueBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(6 - aRedBalls, aRedTuoBalls)
					* PublicMethod.zuhe(1, aBlueBalls);
		}
		return ssqZhuShu;
	}

	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getSSQFSZhuShu(int aRedBalls, int aBlueBalls) {
		long ssqZhuShu = 0L;
		iSendZhushu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(6, aRedBalls)
					* PublicMethod.zuhe(1, aBlueBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(6, aRedBalls)
					* PublicMethod.zuhe(1, aBlueBalls);
		}
		return ssqZhuShu;
	}

	private int getBeishu() {
		return mSeekBarBeishu.getProgress();

	}

	private int getQishu() {
		return mSeekBarQishu.getProgress();

	}

	// private boolean getCheckBox() {
	// return mCheckBox.isChecked();
	// }

	// fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	private void setSeekWhenAddOrSub(int idFind, View iV, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) iV.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}

				}
			}
		});
	}

	/**
	 * 改变提示信息
	 * 
	 * @return void
	 */
	public void changeTextSumMoney() {
		switch (iCurrentButton) {
		case PublicConst.BUY_SSQ_DANSHI:
			// 单式View
			if (redBallTable.getHighlightBallNums() < 6) {
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_red_number));
			} else {
				if (blueBallTable.getHighlightBallNums() < 1) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_blue_number));
				} else {
					int iZhuShu = getZhuShu();
					String iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2)
							+ "元";
					mTextSumMoney.setText(iTempString);
				}
			}

			break;
		case PublicConst.BUY_SSQ_ZHIXUAN: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();

			// 红球数 不足
			if (iRedHighlights < 6) {
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_red_number));
			}
			// 红球数达到最低要求
			else if (iRedHighlights == 6) {
				if (iBlueHighlights < 1) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_blue_number));
				} else if (iBlueHighlights == 1) {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为单式，共" + iZhuShu + "注，共"
							+ (iZhuShu * 2) + "元";
					mTextSumMoney.setText(iTempString);
				} else {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为蓝复式，共" + iZhuShu + "注，共"
							+ (iZhuShu * 2) + "元";
					mTextSumMoney.setText(iTempString);
				}
			}
			// 红球复式
			else {
				if (iBlueHighlights < 1) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_blue_number));
				} else if (iBlueHighlights == 1) {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为红复式，共" + iZhuShu + "注，共"
							+ (iZhuShu * 2) + "元";
					mTextSumMoney.setText(iTempString);
				} else {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为全复式，共" + iZhuShu + "注，共"
							+ (iZhuShu * 2) + "元";
					mTextSumMoney.setText(iTempString);
				}
			}
			break;
		}
		case PublicConst.BUY_SSQ_DANTUO: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();

			if (iRedHighlights + iRedTuoHighlights < 7) {
				mTextSumMoney.setText(getResources().getString(
						R.string.choose_number_dialog_tip5));
			} else if (iBlueHighlights < 1) {
				mTextSumMoney.setText(getResources().getString(
						R.string.choose_number_dialog_tip6));
			} else {
				int iZhuShu = getZhuShu();
				String iTempString = "当前为胆拖，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
			}

			break;
		}
		default:
			break;
		}
	}

	/**
	 * 根据玩法改变当前View 及响应
	 * 
	 * @param aWhichTopButton
	 *            当前顶部标签位置
	 * @param aWhichGroupBall
	 *            第几组小球，如单式共两组小球，从0开始计数
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id+1
	 */
	private void changeBuyViewByRule(int aWhichTopButton, int aWhichGroupBall,
			int aBallViewId) {
		switch (aWhichTopButton) {
		case PublicConst.BUY_SSQ_DANSHI:
			// 单式View中，有2个ball table
			buy_DANSHI(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_SSQ_ZHIXUAN:
			buy_ZHIXUAN(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_SSQ_DANTUO:
			buy_DANTUO(aWhichGroupBall, aBallViewId);
			break;
		default:
			break;
		}
	}

	/**
	 * 胆拖玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * 
	 * @param int aBallViewId 小球id
	 * 
	 * @return void
	 */
	private void buy_DANTUO(int aWhichGroupBall, int aBallViewId) {

		if (aWhichGroupBall == 0) { // 红球 胆区 最多5个小球
			int iChosenBallSum = 5;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& redTuoBallTable.getOneBallStatue(aBallViewId) !=0) {
				redTuoBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,getResources().getString(R.string.ssq_toast_danma_title), Toast.LENGTH_SHORT).show();
			}
			// 记录到输入框fulei

			String red_zhuma_string = "  ";
			int[] redZhuMa = redBallTable.getHighlightBallNOs();
			for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
				red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
				if (i != redBallTable.getHighlightBallNOs().length - 1)
					red_zhuma_string = red_zhuma_string + ",";
			}

			if (red_zhuma_string.equals("  ")) {
				redDanEdit.setText("");
				redDanEdit.setHint(getResources().getString(
						R.string.ssq_dantuo_edit_Prompt_red_danma));
			} else {
				redDanEdit.setText(red_zhuma_string);
			}
			// fulei把记录存入输入框
			String red_tuo_zhuma_string = "  ";
			int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
			for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
				red_tuo_zhuma_string += PublicMethod.getZhuMa(redTuoZhuMa[i]);
				if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
					red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
			}
			if (red_tuo_zhuma_string.equals("  ")) {
				redTuoEdit.setText("");
				redTuoEdit.setHint(getResources().getString(
						R.string.ssq_dantuo_edit_Prompt_red_tuoma));
			} else {
				redTuoEdit.setText(red_tuo_zhuma_string);
			}

		} else if (aWhichGroupBall == 1) { // 篮球区
			int iChosenBallSum = 16;
			int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

			// 记录到输入框fulei
			String blue_zhuma_string = "  ";
			int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
			for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
				blue_zhuma_string += PublicMethod.getZhuMa(blueZhuMa[i]);
				if (i != blueBallTable.getHighlightBallNOs().length - 1)
					blue_zhuma_string = blue_zhuma_string + ",";
			}

			if (blue_zhuma_string.equals("  ")) {
				blueEdit.setText("");
				blueEdit.setHint(getResources().getString(
						R.string.ssq_dantuo_edit_Prompt_blue));
			} else {
				blueEdit.setText(blue_zhuma_string);
			}

		} else if (aWhichGroupBall == 2) { // 红球 拖区 最多20个小球
			int iChosenBallSum = 20;
			int isHighLight = redTuoBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& redBallTable.getOneBallStatue(aBallViewId) != 0) {

				redBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,getResources().getString(R.string.ssq_toast_tuoma_title), Toast.LENGTH_SHORT).show();
			}
			// 记录到输入框fulei

			String red_zhuma_string = "  ";
			int[] redZhuMa = redBallTable.getHighlightBallNOs();
			for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
				red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
				if (i != redBallTable.getHighlightBallNOs().length - 1)
					red_zhuma_string = red_zhuma_string + ",";
			}
			//
			if (red_zhuma_string.equals("  ")) {
				redDanEdit.setText("");
				redDanEdit.setHint(getResources().getString(
						R.string.ssq_dantuo_edit_Prompt_red_danma));
			} else {
				redDanEdit.setText(red_zhuma_string);
			}
			// fulei把记录存入输入框
			String red_tuo_zhuma_string = "  ";
			int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
			for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
				red_tuo_zhuma_string += PublicMethod.getZhuMa(redTuoZhuMa[i]);
				if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
					red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
			}
			if (red_tuo_zhuma_string.equals("  ")) {
				redTuoEdit.setText("");
				redTuoEdit.setHint(getResources().getString(
						R.string.ssq_dantuo_edit_Prompt_red_tuoma));
			} else {
				redTuoEdit.setText(red_tuo_zhuma_string);
			}
		}
	}

	/**
	 * 复式玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * @param int aBallViewId 小球id
	 * @return void
	 */
	private void buy_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {

		if (aWhichGroupBall == 0) { // 红球区
			int iChosenBallSum = 20;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

			// 记录到输入框fuleiz
			String red_zhuma_string = "  ";
			int[] redZhuMa = redBallTable.getHighlightBallNOs();
			for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
				red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
				if (i != redBallTable.getHighlightBallNOs().length - 1)
					red_zhuma_string = red_zhuma_string + ",";
			}

			if (red_zhuma_string.equals("  ")) {
				redEdit.setText("");
				redEdit.setHint(getResources().getString(
						R.string.ssq_zhixuan_edit_Prompt_red));
			} else {
				redEdit.setText(red_zhuma_string);
			}

		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 16;
			int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

			// 记录到输入框fulei
			String blue_zhuma_string = "  ";
			int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
			for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
				blue_zhuma_string += PublicMethod.getZhuMa(blueZhuMa[i]);
				if (i != blueBallTable.getHighlightBallNOs().length - 1) {
					blue_zhuma_string = blue_zhuma_string + ",";
				}
			}
			if (blue_zhuma_string.equals("  ")) {
				blueEdit.setText("");
				blueEdit.setHint(getResources().getString(
						R.string.ssq_zhixuan_edit_Prompt_blue));
			} else {
				blueEdit.setText(blue_zhuma_string);
			}
		}
	}

	/**
	 * 单式玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * @param int aBallViewId 小球id
	 * @return void
	 */
	private void buy_DANSHI(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			int iChosenBallSum = 6;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 1;
			int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
					aBallViewId);

		}
	}

	public static int /* String */getDisplayMetrics(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		float density = dm.density;
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;

		return screenWidth;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_ssq, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("************featureId " + featureId);
		switch (item.getItemId()) {
		case R.id.ssq_menu_confirm:
			beginTouZhu();
			break;
		case R.id.ssq_menu_reselect_num:
			beginReselect();
			break;
		case R.id.ssq_menu_game_introduce:
			showGameIntroduction();

			break;
		case R.id.ssq_menu_cancel:
			this.finish();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public int getChangingConfigurations() {
		// TODO Auto-generated method stub
		return super.getChangingConfigurations();
	}

	public void onCancelClick() {

	}

	/**
	 * 对话框回调函数
	 * 
	 * @param int[] aNums 对话框返回的数值
	 * @return BallTable
	 */
	public void onOkClick(int[] aNums) {

		if (aNums.length == 2) {
			iFushiRedBallNumber = aNums[0];
			iFushiBlueBallNumber = aNums[1];
			redBallTable.randomChooseConfigChange(iFushiRedBallNumber, 0);

			blueBallTable.randomChooseConfigChange(iFushiBlueBallNumber, 1);
			changeTextSumMoney();
		} else if (aNums.length == 3) {
			iDantuoRedDanNumber = aNums[0];
			iDantuoRedTuoNumber = aNums[1];
			iDantuoBlueNumber = aNums[2];

			int[] iTotalRandoms = PublicMethod.getRandomsWithoutCollision(
					aNums[0] + aNums[1], 0, 32);
			int[] iTotalRandomsBlue = PublicMethod.getRandomsWithoutCollision(
					aNums[2], 0, 15);

			if (isJiXuanDanTuo) {
				redBallTable.removeView();
				redTuoBallTable.removeView();
				blueBallTable.removeView();
				iScreenWidth = PublicMethod.getDisplayWidth(this);
				int[] iTotalRandomsRed = new int[iDantuoRedDanNumber];
				int[] iTotalRandomsRedTuo = new int[iDantuoRedTuoNumber];
				for (int i = 0; i < iDantuoRedDanNumber; i++) {
					iTotalRandomsRed[i] = iTotalRandoms[i];
				}
				for (int i = iDantuoRedDanNumber; i < (iDantuoRedDanNumber + iDantuoRedTuoNumber); i++) {
					iTotalRandomsRedTuo[i - iDantuoRedDanNumber] = iTotalRandoms[i];
				}
				iTotalRandomsRed = PublicMethod
						.orderby(iTotalRandomsRed, "abc");
				iTotalRandomsRedTuo = PublicMethod.orderby(iTotalRandomsRedTuo,"abc");
				iTotalRandomsBlue = PublicMethod.orderby(iTotalRandomsBlue,"abc");
				PublicMethod.makeBallTableJiXuan(redBallTable, iScreenWidth,redBallResId, iTotalRandomsRed, this);
				PublicMethod.makeBallTableJiXuan(redTuoBallTable, iScreenWidth,redBallResId, iTotalRandomsRedTuo, this);
				PublicMethod.makeBallTableJiXuan(blueBallTable, iScreenWidth,redBallResId, iTotalRandomsBlue, this);

				changeTextSumMoney();
			} else {
				redBallTable.clearAllHighlights();
				redTuoBallTable.clearAllHighlights();
				int i;

				for (i = iDantuoRedDanNumber; i < iDantuoRedDanNumber
						+ iDantuoRedTuoNumber; i++) {

					int isHighLight = redTuoBallTable.changeBallState(20,
							iTotalRandoms[i]);

				}
				blueBallTable.randomChooseConfigChange(iDantuoBlueNumber, 1);
				changeTextSumMoney();
			}

		}

	}

	// 开始投注 响应点击投注按钮 和 menu里面的投注选项
	private void beginTouZhu() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		// fqc eidt 2010/7/13 修改双色球玩法
        //
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				SsqActivity.this, "addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(SsqActivity.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			if (iCurrentButton == PublicConst.BUY_SSQ_DANSHI) { // 1为单式投注
				int iZhuShu = getZhuShu();
				if (redBallTable.getHighlightBallNums() != 6
						&& blueBallTable.getHighlightBallNums() != 1) {
					alert1("请选择6个红球和1个蓝球");
				} else if (redBallTable.getHighlightBallNums() != 6) {
					alert1("请选择6个红球");
				} else if (blueBallTable.getHighlightBallNums() != 1) {
					alert1("请选择1个蓝球");
				} else if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {
					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlert();
					alert(sTouzhuAlert);
				}
			}
			if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
				if (isJiXuanZhiXuan) {
					if (balls.size() == 0) {
						alert1("请至少选择1注彩票");
					} else {
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlertJixuan();
						alert_jixuan(sTouzhuAlert);
					}
				} else {
					int iZhuShu = getZhuShu();
					if (redBallTable.getHighlightBallNums() < 6
							&& blueBallTable.getHighlightBallNums() < 1) {
						alert1("请至少选择6个红球和1个蓝球	");
					} else if (redBallTable.getHighlightBallNums() < 6) {
						alert1("请选择至少6个红球");
					} else if (blueBallTable.getHighlightBallNums() < 1) {
						alert1("请选择1个蓝球");
					}

					else if (iZhuShu * 2 > 100000) {
						dialogExcessive();
					} else {
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlert();
						alert(sTouzhuAlert);
					}
				}
			}
			if (iCurrentButton == PublicConst.BUY_SSQ_DANTUO) {
				int iZhuShu = getZhuShu();
				// //周黎鸣 7.4 代码修改：添加登陆的判断

				int redNumber = redBallTable.getHighlightBallNums();
				int redTuoNumber = redTuoBallTable.getHighlightBallNums();
				int blueNumber = blueBallTable.getHighlightBallNums();
				if (redNumber + redTuoNumber > 25
						|| redNumber + redTuoNumber < 7 || redNumber < 1
						|| redNumber > 5 || blueNumber < 1 || blueNumber > 16
						|| redTuoNumber < 2 || redTuoNumber > 20) {
					alert1("请选择:\n1~5个红色胆码；\n" + " 2~20个红色拖码；\n"
							+ " 1~16个蓝色球；\n" + " 胆码与拖码个数之和在7~25之间");
				} else if (iZhuShu * 2 > 100000) {
					dialogExcessive();
				} else {

					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlert();
					// 应该调用胆拖的方法 陈晨 20100713
					alert_dantuo(sTouzhuAlert);
				}
			}
		}
	}

	/**
	 * 点击menu的重新机选 调用重新机选的方法
	 */
	private void beginReselect() {

		if (iCurrentButton == PublicConst.BUY_SSQ_DANSHI) {
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
		} else if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
		} else if (iCurrentButton == PublicConst.BUY_SSQ_DANTUO) {
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
			redTuoBallTable.clearAllHighlights();
		}
	}

	/**
	 * 点击menu里面的玩法介绍，通过对话框显示
	 */
	private void showGameIntroduction() {
		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_ssq.html";

		webView.loadUrl(url);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("玩法介绍");
		builder.setView(webView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();
	}

	// fqc delete 删除取消按钮 7/14/2010
	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert1(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
		dialog.show();

	}

	/**
	 * 单复式投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert(String string) {

		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								if (iCurrentButton == PublicConst.BUY_SSQ_DANSHI) {
									ssq_b_touzhu_danshi.setClickable(false);
									ssq_b_touzhu_danshi
											.setImageResource(R.drawable.touzhuup_n);
								} else if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
									ssq_b_touzhu_fushi.setClickable(false);
									ssq_b_touzhu_fushi
											.setImageResource(R.drawable.touzhuup_n);
								}

								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
								// 加入是否改变切入点判断 陈晨 8.11
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									String str = "00";

									@Override
									public void run() {
										int iRedHighlights = redBallTable
												.getHighlightBallNums();
										int iBlueHighlights = blueBallTable
												.getHighlightBallNums();
										// int

										// 判断是双色球单式还是复式
										if (iRedHighlights == 6
												&& iBlueHighlights == 1) {
											String zhuma = zhuma_danshi();
											str = payNew(zhuma, "" + iQiShu,
													iZhuShu * 200 * iQiShu + "");
										} else if ((iRedHighlights > 6 && iBlueHighlights == 1)
												|| (iRedHighlights == 6 && iBlueHighlights > 1)
												|| (iRedHighlights > 6 && iBlueHighlights > 1)) {//
											String zhuma_fushi = zhuma_fushi();
											str = payNew(zhuma_fushi, ""
													+ iQiShu, iZhuShu * 200
													* iQiShu + "");
										}

										if (iCurrentButton == PublicConst.BUY_SSQ_DANSHI) {
											ssq_b_touzhu_danshi
													.setClickable(true);
										} else if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {
											ssq_b_touzhu_fushi
													.setClickable(true);
										}
										Message msg1 = new Message();
										msg1.what = 10;
										handler.sendMessage(msg1);
										if (str.equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (str.equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (str.equals("040006")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (str.equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (str.equals("040007")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (str.equals("4444")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										} else if (str.equals("00")) {
											Message msg = new Message();
											msg.what = 8;
											handler.sendMessage(msg);
										} else {
											Message msg = new Message();
											msg.what = 9;
											handler.sendMessage(msg);
										}
									}

								});
								t.start();
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
		dialog.show();

	}

	/**
	 * 胆拖投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert_dantuo(String string) {

		Builder dialog = new AlertDialog.Builder(this).setMessage(string).setTitle("您选择的是")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ssq_b_touzhu_dantuo
								.setImageResource(R.drawable.touzhuup_n);
						ssq_b_touzhu_dantuo.setClickable(false);

						showDialog(DIALOG1_KEY); // 显示网络提示框
						// 加入是否改变切入点判断 陈晨 8.11
						Thread t = new Thread(new Runnable() {
							int iZhuShu = getZhuShu();
							int iQiShu = getQiShu();
							String str = "00";

							@Override
							public void run() {

								String zhuma_dantuo = zhuma_dantuo();
								str = payNew(zhuma_dantuo, iQiShu + "", iZhuShu
										* 200 * iQiShu + "");
								ssq_b_touzhu_dantuo.setClickable(true);
								Message msg1 = new Message();
								msg1.what = 10;
								handler.sendMessage(msg1);
								if (str.equals("000000")) {
									Message msg = new Message();
									msg.what = 6;
									handler.sendMessage(msg);
								} else if (str.equals("070002")) {
									Message msg = new Message();
									msg.what = 7;
									handler.sendMessage(msg);
								} else if (str.equals("040006")) {
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								} else if (str.equals("1007")) {
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								} else if (str.equals("040007")) {
									Message msg = new Message();
									msg.what = 4;
									handler.sendMessage(msg);
								} else if (str.equals("4444")) {
									Message msg = new Message();
									msg.what = 3;
									handler.sendMessage(msg);
								} else if (str.equals("00")) {
									Message msg = new Message();
									msg.what = 8;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = 9;
									handler.sendMessage(msg);
								}
							}
						});
						t.start();
					}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
		dialog.show();

	}

	// 投注新接口 20100711陈晨
	/**
	 * 投注新接口
	 * 
	 * @param bets
	 *            注码
	 * @param count
	 *            期数
	 * @param amount
	 *            投注总金额
	 * @return
	 */
	protected String payNew(String bets, String count, String amount) {
		BettingInterface betting = BettingInterface.getInstance();

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String error_code = betting.bettingNew(bets, count, amount, sessionid);

		return error_code;
	}

	/**
	 * 单式投注时投注码参数
	 * 
	 * @return
	 */
	public String zhuma_danshi() {

		int zhumablue[] = null;
		int zhuma[] = null;
		int beishu = mSeekBarBeishu.getProgress();
		String t_str = "1512-F47104-";
		t_str += "00-";

		if (isJiXuanZhiXuan) {
			int zhushu = balls.size();
			if (zhushu < 10) {
				t_str += "0"+zhushu;
			}
			if (zhushu >= 10) {
				t_str += zhushu;
			}
	        t_str+="-";
			for (int j = 0; j < balls.size(); j++) {
				   t_str+="00";
				if (beishu < 10) {
					t_str += "0" +beishu;
				} else {
					t_str += "" + beishu;
				}
				zhuma = balls.get(j).getRed();
				zhumablue = balls.get(j).getBlue();
				for (int i = 0; i < zhuma.length; i++) {
					int zhuRed = zhuma[i] + 1;
					if (zhuRed >= 10) {
						t_str += zhuRed;
					} else if (zhuma[i] < 10) {
						t_str += "0" + zhuRed;
					}
				}
				int zhuBlue = zhumablue[0] + 1;
				if (zhuBlue >= 10) {
					t_str += "~" + zhuBlue;
				} else if (zhumablue[0] < 10) {
					t_str += "~0" + zhuBlue;
				}
				t_str += "^";
			}
		} else {

			t_str += "01-00";
			if (beishu < 10) {
				t_str += "0" +beishu;
			} else {
				t_str += "" + beishu;
			}
			zhuma = redBallTable.getHighlightBallNOs();
			zhumablue = blueBallTable.getHighlightBallNOs();
			for (int i = 0; i < zhuma.length; i++) {
				if (zhuma[i] >= 10) {
					t_str += zhuma[i];
				} else if (zhuma[i] < 10) {
					t_str += "0" + zhuma[i];
				}

			}
			if(zhumablue.length>0){
				if (zhumablue[0] >= 10) {
					t_str += "~" + zhumablue[0];
				} else if (zhumablue[0] < 10) {
					t_str += "~0" + zhumablue[0];
				}
			}
			t_str += "^";
		}

		return t_str;

	}

	/**
	 * 复式投注时投注码参数
	 * 
	 * @return
	 */
	public String zhuma_fushi() {
		int beishu = mSeekBarBeishu.getProgress();
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iBlueHighlights = blueBallTable.getHighlightBallNums();
		zhuma = redBallTable.getHighlightBallNOs();
		int zhumablue[] = blueBallTable.getHighlightBallNOs();
		String t_str = "1512-F47104-";
		if (iRedHighlights > 6 && iBlueHighlights == 1) {
			t_str += "10-01-10";
		} else if (iBlueHighlights > 1 && iRedHighlights == 6) {
			t_str += "20-01-20";
		} else if (iRedHighlights > 6 && iBlueHighlights > 1) {
			t_str += "30-01-30";
		}
		if (beishu < 10) {
			t_str += "0" + beishu;
		} else {
			t_str += "" + beishu;
		}
		t_str += "*";
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}

		}

		if (zhumablue[0] >= 10) {
			t_str += "~" + zhumablue[0];
		} else if (zhumablue[0] < 10) {
			t_str += "~0" + zhumablue[0];
		}
		for (int j = 1; j < zhumablue.length; j++) {
			if (zhumablue[j] >= 10) {
				t_str += +zhumablue[j];
			} else if (zhumablue[j] < 10) {
				t_str += "0" + zhumablue[j];
			}

		}
		t_str += "^";

		return t_str;
	}

	/**
	 * payNew 胆拖投注时投注码参数
	 * 
	 * @return
	 */
	public String zhuma_dantuo() {

		int beishu = mSeekBarBeishu.getProgress();

		int iBlueHighlights = blueBallTable.getHighlightBallNums();
		zhuma = redBallTable.getHighlightBallNOs();
		int[] tuozhuma = redTuoBallTable.getHighlightBallNOs();

		int zhumablue[] = blueBallTable.getHighlightBallNOs();
		String t_str = "1512-F47104-";
		if (iBlueHighlights == 1) {
			t_str += "40-01-40";

		} else if (iBlueHighlights > 1) {
			t_str += "50-01-50";
		}
		if (beishu < 10) {
			t_str += "0" + beishu;
		} else {
			t_str += "" + beishu;
		}
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "*";
		for (int j = 0; j < tuozhuma.length; j++) {
			if (tuozhuma[j] >= 10) {
				t_str += tuozhuma[j];
			} else if (tuozhuma[j] < 10) {
				t_str += "0" + tuozhuma[j];
			}
		}
		t_str += "~";
		for (int k = 0; k < zhumablue.length; k++) {
			if (zhumablue[k] >= 10) {
				t_str += +zhumablue[k];
			} else if (zhumablue[k] < 10) {
				t_str += "0" + zhumablue[k];
			}
			// t_str+="^";移到外面 陈晨 20100714
		}
		t_str += "^";
		return t_str;
	}

	// 网络连接提示框 2010/7/4 陈晨
	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
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
	 * 投注提示框中的信息
	 */
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string = red_zhuma_string + String.valueOf(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string = red_zhuma_string + ",";
			}
		}
		String blue_zhuma_string = " ";
		int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
			blue_zhuma_string = blue_zhuma_string
					+ String.valueOf(blueZhuMa[i]);
			if (i != blueBallTable.getHighlightBallNOs().length - 1) {
				blue_zhuma_string = blue_zhuma_string + ",";
			}
		}
		if (iCurrentButton == PublicConst.BUY_SSQ_ZHIXUAN) {

			return "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
					+ "\n" + "注码：" + red_zhuma_string + " | "
					+ blue_zhuma_string + "\n" + "确认支付吗？";

		} else {
			String red_tuo_zhuma_string = " ";
			int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
			for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
				red_tuo_zhuma_string = red_tuo_zhuma_string
						+ String.valueOf(redTuoZhuMa[i]);
				if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
					red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
			}
			return "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
					+ "\n" + "注码：" + "\n" + "红球胆码：" + red_zhuma_string + "\n"
					+ "红球拖码：" + red_tuo_zhuma_string + "\n" + "蓝球："
					+ blue_zhuma_string + "\n" + "确认支付吗？";
		}
	}

	/**
	 * 单笔投注大于10万元时的对话框
	 */
	private void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(SsqActivity.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于100000元");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	class Balls {
		int[] redNum = new int[6];
		int[] blueNum = new int[1];

		public Balls() {
			redNum = PublicMethod.getRandomsWithoutCollision(6, 0, 32);
			redNum = PublicMethod.orderby(redNum, "abc");
			blueNum = PublicMethod.getRandomsWithoutCollision(1, 0, 15);

		}

		public int[] getRed() {
			return redNum;

		}

		public int[] getBlue() {
			return blueNum;

		}

		public String getRedZhu() {
			String str = "";
			for (int i = 0; i < redNum.length; i++) {
				if (i != redNum.length - 1)
					str += PublicMethod.getZhuMa(redNum[i] + 1) + ",";
				else
					str += PublicMethod.getZhuMa(redNum[i] + 1);
			}
			return str;

		}

		public String getBlueZhu() {
			String str = "";
			for (int i = 0; i < blueNum.length; i++) {
				if (i != blueNum.length - 1)
					str += PublicMethod.getZhuMa(blueNum[i] + 1) + ",";
				else
					str += PublicMethod.getZhuMa(blueNum[i] + 1);
			}
			return str;

		}

	}

	/**
	 * 创建直选机选
	 */
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			
			BallTable ballTableRed = new BallTable(lines, RED_BALL_START);
			
			
			PublicMethod.makeBallTableJiXuan(ballTableRed, iScreenWidth,
					redBallResId, balls.get(i).getRed(), this);
			BallTable ballTableBlue = new BallTable(lines, BLUE_BALL_START);
			PublicMethod.makeBallTableJiXuan(ballTableBlue, iScreenWidth,
					blueBallResId, balls.get(i).getBlue(), this);
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhuView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhuView);
					} else {
						Toast.makeText(SsqActivity.this, getResources().getText(R.string.zhixuan_jixuan_toast),
								Toast.LENGTH_SHORT).show();

					}

				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			param.setMargins(15, 10, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
			layout.addView(lines);
		}

	}

	/**
	 * 直选机选投注提示框中的信息
	 */
	private String getTouzhuAlertJixuan() {
		String zhumaString = "";
		for (int i = 0; i < balls.size(); i++) {
			zhumaString += balls.get(i).getRedZhu() + "+"
					+ balls.get(i).getBlueZhu();
			if (i != balls.size() - 1) {
				zhumaString += "\n";
			}
		}
		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = balls.size() * beishu;
		return "注数："
				+ balls.size()
				+ "注"
				+ "\n"
				+ // 注数不能算上倍数 陈晨 20100713
				"倍数：" + beishu + "倍" + "\n" + "追号："
				+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
				+ (balls.size() * 2 * beishu) + "元" + "\n" + "冻结金额："
				+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
				+ "\n" + "注码：" + "\n" + zhumaString + "\n" + "确认支付吗？";
	}

	/**
	 * 机选投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert_jixuan(String string) {
		sensor.stopAction();
		Dialog dialog = new AlertDialog.Builder(this).setMessage(string).setTitle("您选择的是")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						ssq_b_touzhu_fushi.setImageResource(R.drawable.touzhuup_n);
						ssq_b_touzhu_fushi.setClickable(false);

						showDialog(DIALOG1_KEY); // 显示网络提示框
						Thread t = new Thread(new Runnable() {
							int beishu = mSeekBarBeishu.getProgress();
							int iZhuShu = balls.size() * beishu;
							int iQiShu = getQiShu();
							String str = "00";

							//

							@Override
							public void run() {

								String zhuma = zhuma_danshi();
								str = payNew(zhuma, iQiShu + "", iZhuShu * 200* iQiShu + "");
								ssq_b_touzhu_fushi.setClickable(true);
								Message msg1 = new Message();
								msg1.what = 10;
								handler.sendMessage(msg1);

								if (str.equals("000000")) {
									Message msg = new Message();
									msg.what = 6;
									handler.sendMessage(msg);
								} else if (str.equals("070002")) {
									Message msg = new Message();
									msg.what = 7;
									handler.sendMessage(msg);
								} else if (str.equals("040006")) {
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								} else if (str.equals("1007")) {
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								} else if (str.equals("040007")) {
									Message msg = new Message();
									msg.what = 4;
									handler.sendMessage(msg);
								} else if (str.equals("4444")) {
									Message msg = new Message();
									msg.what = 3;
									handler.sendMessage(msg);
								} else if (str.equals("00")) {
									Message msg = new Message();
									msg.what = 8;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = 9;
									handler.sendMessage(msg);
								}
							}

						});
						t.start();
					}
				}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
	
							}
						}).create();
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});

	}

	class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			zhuView.removeAllViews();
			balls = new Vector<Balls>();
			for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
				Balls ball = new Balls();
				balls.add(ball);
			}
			createTable(zhuView);
		}
	}

	/**
	 * intent回调函数
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			beginTouZhu();
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//释放红球图片
		if(redBallTable != null && redBallTable.getBallViews()!= null){
			for (Iterator iterator = redBallTable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
		redBallTable = null;
		
		//释放蓝球图片
		if(blueBallTable != null && blueBallTable.getBallViews()!= null){
			for (Iterator iterator = blueBallTable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
		blueBallTable = null;
		
		if(redTuoBallTable != null && redTuoBallTable.getBallViews()!= null){
			for (Iterator iterator = redTuoBallTable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
		redTuoBallTable = null;
		
	}
	
	

}