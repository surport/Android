package com.ruyicai.activity.game.qlc;

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
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.game.fc3d.Fc3d;
import com.ruyicai.activity.game.ssq.SsqActivity;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.activity.home.ScrollableTabActivity;
import com.ruyicai.dialog.Accoutdialog;
import com.ruyicai.dialog.ChooseNumberDialogQLC;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.net.transaction.BettingInterface;
import com.ruyicai.net.transaction.SoftwareUpdateInterface;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;
import com.ruyicai.view.OneBallView;
import com.ruyicai.view.RadioStateDrawable;
import com.ruyicai.view.TabBarButton;

public class QLC extends Activity // 事件 回调函数
		implements OnClickListener, // 小球被点击 onClick
		SeekBar.OnSeekBarChangeListener, // SeekBar改变 onProgressChanged
		RadioGroup.OnCheckedChangeListener, // 机选复选框变化 onCheckedChangeListener
		MyDialogListener { // 对话框输入数值 onOKClick

	// 小球图的宽高
	public static final int BALL_WIDTH = 55;
	// 顶部标签个数
	private int iButtonNum = 3;

	// 首行button当前的位置
	private int iCurrentButton; // 0x55660001 --- QLC danshi
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

	private static final int DIALOG1_KEY = 0;
	ProgressDialog progressdialog;

	ScrollView mHScrollView;

	SeekBar mSeekBarBeishu;
	SeekBar mSeekBarQishu;

	TextView mTextBeishu;
	TextView mTextQishu;

	TextView mTextSumMoney;
	ImageButton qlc_b_touzhu_danshi;
	ImageButton qlc_b_touzhu_fushi;
	ImageButton qlc_b_touzhu_dantuo;

	int iProgressBeishu = 1;
	int iProgressQishu = 1;

	private int iScreenWidth;
	// Vector<OneBallView> redBallViewVector;
	BallTable redBallTable = null;
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };

	BallTable redTuoBallTable = null;

	// Vector<OneBallView> blueBallViewVector;
	BallTable blueBallTable = null;

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
	// 实现横纵的切换

	private SensorManager mSensorManager;
	int lastAccelerometer = SensorManager.SENSOR_ACCELEROMETER;
	public int publicTopButton;
	public int type;
	public static final int ZHIXUAN = 0;
	public static final int DANTUO = 1;
	/** Called when the activity is first created. */
	// fulei
	private String issue[] = new String[2];
	private TextView term;
	private TextView title;
	private TextView time;
	private EditText danEdit;
	private EditText tuoEdit;
	private EditText redEdit;
	private boolean isJiXuanDanTuo = true;// 是否是胆拖机选
	private boolean isJiXuanZhiXuan = false;// 是否是直选机选
	private SsqSensor sensor = new SsqSensor(this);
	private Spinner jixuanZhu;
	private Vector<Balls> balls = new Vector();
	private LinearLayout zhuView;
	private ImageButton imgJixuan;
	private boolean isOnclik = true;// 下拉框是否响应
	// 处理投注返回的信息
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:

				break;
			case 1:
				progressdialog.dismiss();
				if(isFinishing() == false){
				    Accoutdialog.getInstance().createAccoutdialog(QLC.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
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
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				// 投注成功后 清除高亮
				if (iCurrentButton == PublicConst.BUY_QLC_DANSHI
						|| iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
					if (isJiXuanZhiXuan) {
						zhuView.removeAllViews();
					} else {
						redEdit.setText("");
						redBallTable.clearAllHighlights();
						changeTextSumMoney();
					}

				} else if (iCurrentButton == PublicConst.BUY_QLC_DANTUO) {
					danEdit.setText("");
					tuoEdit.setText("");
					redBallTable.removeView();
					redTuoBallTable.removeView();
					changeTextSumMoney();
				}	
				Intent intent2 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent2);
				if(isFinishing() == false){
				    PublicMethod.showDialog(QLC.this);
				}
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(QLC.this, UserLogin.class);
				startActivity(intentSession);
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 8:
				progressdialog.dismiss();
				// progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG)
						.show();
				// // tv.setText(result);
				break;
			case 10:
				if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
					qlc_b_touzhu_danshi.setImageResource(R.drawable.imageselecter);
				} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
					qlc_b_touzhu_fushi.setImageResource(R.drawable.imageselecter);
				} else if (iCurrentButton == PublicConst.BUY_QLC_DANTUO) {
					qlc_b_touzhu_dantuo.setImageResource(R.drawable.imageselecter);
				}
				break;
			}
			//			
		}
	};

	/*
	 * 入口函数
	 * 
	 * @param savedInstanceState
	 * 
	 * @return void
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		// 获取期号和截止时间
		JSONObject qlcLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(Constants.LOTNO_QLC);
		if (qlcLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				issue[0] = qlcLotnoInfo.getString("batchCode");
				issue[1] = qlcLotnoInfo.getString("endTime");
				term.setText("第" + this.issue[0] + "期");
				time.setText("截止时间：" + this.issue[1]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			issue[0] = "";
			issue[1] = "";
		    PublicMethod.getIssue(Constants.LOTNO_QLC,term,time,new Handler());
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mHScrollView = (ScrollView) findViewById(R.id.scroll_global);
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initButtons();
		iCurrentButton = PublicConst.BUY_QLC_ZHIXUAN;
		createBuyView(iCurrentButton);

	}

	@Override
	public int getChangingConfigurations() {
		// TODO Auto-generated method stub
		return super.getChangingConfigurations();
	}

	@Override
	public Object getLastNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return super.getLastNonConfigurationInstance();
	}

	/*
	 * 创建购彩View，按照顶部按钮的状态创建
	 * 
	 * @param int aWhichBuy 对应View的Id
	 * 
	 * @return void
	 */
	private void createBuyView(int aWhichBuy) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		publicTopButton = aWhichBuy;
		switch (aWhichBuy) {

		case PublicConst.BUY_QLC_ZHIXUAN: // 直选

			if (isJiXuanZhiXuan) {
				sensor.startAction();
				create_QLC_ZHIXUAN_JIXUAN();
			} else {
				sensor.stopAction();

				create_QLC_ZHIXUAN();
			}
			topButtonGroup.setSelected(false);
			commit();
			break;
		case PublicConst.BUY_QLC_DANTUO: // 胆拖

			if (isJiXuanDanTuo) {

				create_QLC_DANTUO_JIXUAN();

			} else {

				create_QLC_DANTUO();

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
	private void create_QLC_DANTUO() {
		// topBar.setVisibility(View.GONE);
		type = DANTUO;
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_qlc_dantuo, null);
			title.setText("七乐彩胆拖");
			int redBallViewNum = 30;
			int redBallViewWidth = QLC.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			PublicMethod.recycleBallTable(redBallTable);
			redBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_red_danma_qlc, iScreenWidth, redBallViewNum, redBallResId, RED_BALL_START, 1, this,
					this);
			
			PublicMethod.recycleBallTable(redTuoBallTable);
			redTuoBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_red_tuoma_qlc, iScreenWidth, redBallViewNum,redBallResId, RED_TUO_BALL_START, 1,
					this, this);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.qlc_dantuo_text_sum_money);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV
					.findViewById(R.id.qlc_dantuo_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV
					.findViewById(R.id.qlc_dantuo_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV
					.findViewById(R.id.qlc_dantuo_text_beishu_change);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV
					.findViewById(R.id.qlc_dantuo_text_qishu_change);
			mTextQishu.setText("" + iProgressQishu);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_subtract_beishu, iV,
					-1, mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_add_qihao, iV, 1,
					mSeekBarQishu, false);

			qlc_b_touzhu_dantuo = (ImageButton) iV
					.findViewById(R.id.qlc_dantuo_b_touzhu);
			qlc_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			ImageButton newSelect = (ImageButton) iV
					.findViewById(R.id.qlc_dantuo_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					tuoEdit.setText("");
					danEdit.setText("");
					redBallTable.clearAllHighlights();
					redTuoBallTable.clearAllHighlights();
					changeTextSumMoney();
				}
			});
			final LinearLayout danArea = (LinearLayout) iV
					.findViewById(R.id.balls_layout_qlc_danma);
			final LinearLayout tuoArea = (LinearLayout) iV
					.findViewById(R.id.balls_layout_qlc_tuoma);
			danArea.setVisibility(LinearLayout.GONE);
			final TextView danText = (TextView) iV
					.findViewById(R.id.qlc_danma_text);
			final TextView tuoText = (TextView) iV
					.findViewById(R.id.qlc_tuoma_text);
			danEdit = (EditText) iV.findViewById(R.id.qlc_danma_edit);
			tuoEdit = (EditText) iV.findViewById(R.id.qlc_tuoma_edit);

			danEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						danEdit.setBackgroundResource(R.drawable.hongkuang);
						tuoArea.setVisibility(LinearLayout.GONE);
						danArea.setVisibility(LinearLayout.VISIBLE);
						danText.setTextColor(Color.RED);
					} else {
						danEdit.setBackgroundResource(R.drawable.huikuang);
						danText.setTextColor(Color.BLACK);
					}

				}
			});
			tuoEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if (hasFocus) {
						tuoEdit.setBackgroundResource(R.drawable.hongkuang);
						danArea.setVisibility(LinearLayout.GONE);
						tuoArea.setVisibility(LinearLayout.VISIBLE);
						tuoText.setTextColor(Color.RED);
					} else {
						tuoEdit.setBackgroundResource(R.drawable.huikuang);
						tuoText.setTextColor(Color.BLACK);
					}
				}
			});

		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/*
	 * 创建复式购彩View
	 * 
	 * @return void
	 */
	private void create_QLC_ZHIXUAN() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_qlc_fushi, null);
			title.setText("七乐彩直选");

			int redBallViewNum = 30;
			int redBallViewWidth = BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			PublicMethod.recycleBallTable(redBallTable);
			PublicMethod.recycleBallTable(blueBallTable);
			redBallTable = PublicMethod.makeBallTable(iV,R.id.table_red_fushi_qlc, iScreenWidth, redBallViewNum, redBallResId, RED_BALL_START, 1, this,this);

			mTextSumMoney = (TextView) iV.findViewById(R.id.text_sum_money_fushi_qlc);
			mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV
					.findViewById(R.id.qlc_fushi_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV
					.findViewById(R.id.qlc_fushi_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV
					.findViewById(R.id.qlc_fushi_text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.qlc_fushi_text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			/*
			 * 点击加减图标，对seekbar进行设置：
			 * 
			 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加
			 * -1表示减 final SeekBar mSeekBar
			 * 
			 * @return void
			 */
			setSeekWhenAddOrSub(R.id.qlc_fushi_seekbar_subtract_beishu, iV, -1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_fushi_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_fushi_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.qlc_fushi_seekbar_add_qishu, iV, 1,
					mSeekBarQishu, false);

			qlc_b_touzhu_fushi = (ImageButton) iV
					.findViewById(R.id.qlc_fushi_b_touzhu);
			qlc_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			ImageButton newSelect = (ImageButton) iV
					.findViewById(R.id.qlc_zhixuan_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					redEdit.setText("");
					redBallTable.clearAllHighlights();
					changeTextSumMoney();

				}
			});
			final TextView redText = (TextView) iV
					.findViewById(R.id.qlc_text_zhixuan_title);
			redEdit = (EditText) iV.findViewById(R.id.qlc_edit_zhixuan);

			redEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						redEdit.setBackgroundResource(R.drawable.hongkuang);
						redText.setTextColor(Color.RED);
					} else {
						redEdit.setBackgroundResource(R.drawable.huikuang);
						redText.setTextColor(Color.BLACK);
					}

				}
			});

		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/**
	 * 直选机选
	 * 
	 */
	private void create_QLC_ZHIXUAN_JIXUAN() {
		buyView.removeAllViews();
		PublicMethod.recycleBallTable(redBallTable);
		PublicMethod.recycleBallTable(blueBallTable);
		Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT).show();
		balls = new Vector<Balls>();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_zhixuan_jixuan, null);

			title.setText("七乐彩机选");
			// 初始化spinner
			jixuanZhu = (Spinner) iV
					.findViewById(R.id.layout_zhixuan_jixuan_spinner);
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
			zhuView = (LinearLayout) iV
					.findViewById(R.id.layout_zhixuan_linear_zhuma);

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

		qlc_b_touzhu_fushi = (ImageButton) iV
				.findViewById(R.id.ssq_fushi_b_touzhu);
		qlc_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
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
	 * 胆拖机选
	 * 
	 */
	private void create_QLC_DANTUO_JIXUAN() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_qlc_dantuo_jixuan, null);
			title.setText("七乐彩胆拖机选");
			
			PublicMethod.recycleBallTable(redBallTable);
			redBallTable = new BallTable(iV, R.id.table_red_danma_qlc,RED_BALL_START);
			PublicMethod.recycleBallTable(redTuoBallTable);
			redTuoBallTable = new BallTable(iV, R.id.table_red_tuoma_qlc,RED_TUO_BALL_START);

			mTextSumMoney = (TextView) iV.findViewById(R.id.qlc_dantuo_text_sum_money);
			mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.qlc_dantuo_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.qlc_dantuo_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV.findViewById(R.id.qlc_dantuo_text_beishu_change);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV
					.findViewById(R.id.qlc_dantuo_text_qishu_change);
			mTextQishu.setText("" + iProgressQishu);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_subtract_beishu, iV,
					-1, mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.qlc_dantuo_seekbar_add_qihao, iV, 1,
					mSeekBarQishu, false);

			qlc_b_touzhu_dantuo = (ImageButton) iV
					.findViewById(R.id.qlc_dantuo_b_touzhu);
			qlc_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			imgJixuan = (ImageButton) iV
					.findViewById(R.id.qlc_dantuo_img_jixuan);

			imgJixuan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
							QLC.this, 1, QLC.this);
					iChooseNumberDialog.show();
				}
			});

		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		// 弹出机选框
		ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
				QLC.this, 1, QLC.this);
		iChooseNumberDialog.show();

	}

	/*
	 * 初始化顶部的button
	 * 
	 * @return void
	 */
	private void initButtons() {

		initTopButtons();
		commit();
	}

	private void initTopButtons() {
//		topBar = (HorizontalScrollView) findViewById(R.id.topBar);
		topButtonGroup = (RadioGroup) findViewById(R.id.topMenu);

		defaultOffShade = RadioStateDrawable.SHADE_GRAY;
		defaultOnShade = RadioStateDrawable.SHADE_YELLOW;
		topButtonGroup.setOnCheckedChangeListener(this);
		topButtonLayoutParams = new RadioGroup.LayoutParams(/* 320/5 */64,
				RadioGroup.LayoutParams.WRAP_CONTENT);
	}

	/*
	 * 添加顶部按钮，并显示单式玩法View
	 * 
	 * @return void
	 */
	private void commit() {
		topButtonGroup.removeAllViews();
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
		int optimum_visible_items_in_portrait_mode = iButtonNum;

		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width;

		width = screen_width / iButtonNum;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < iButtonNum; i++) {
			TabBarButton tabButton = new TabBarButton(this);

			tabButton.setState(topButtonIdOn[i], topButtonIdOff[i],3);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			topButtonGroup.addView(tabButton, i, topButtonLayoutParams);
		}

	}

	/*
	 * 设置顶部按钮的位置
	 * 
	 * @param int index 顶部按钮的索引
	 * 
	 * @return void
	 */
	public void setCurrentTab(int index) {
		// topButtonGroup.check(index);
		// startGroupActivity(titleList.get(index).toString(),
		// (Intent)intentList.get(index));
		switch (index) {
		// case 0: // 单式
		// iCurrentButton = PublicConst.BUY_QLC_DANSHI;
		// createBuyView(iCurrentButton);
		// break;
		case 0: // 复式
			iCurrentButton = PublicConst.BUY_QLC_ZHIXUAN;
			createBuyView(iCurrentButton);
			break;
		case 1: // 胆拖
			iCurrentButton = PublicConst.BUY_QLC_DANTUO;
			createBuyView(iCurrentButton);
			break;
		}
	}

	/*
	 * 顶部按钮变化的回调函数
	 * 
	 * @param RadioGroup group 这个参数不用管它
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
			Intent intent1 = new Intent(QLC.this, RuyicaiAndroid.class);
			startActivity(intent1);
			finish();
			break;
		case 1:// 直选
			iCurrentButton = PublicConst.BUY_QLC_ZHIXUAN;
			isJiXuanZhiXuan = !isJiXuanZhiXuan;
			if (!isJiXuanZhiXuan && !isJiXuanDanTuo) {
				isJiXuanDanTuo = true;
			}
			createBuyView(iCurrentButton);
			break;
		case 2: // 胆拖
			sensor.stopAction();
			iCurrentButton = PublicConst.BUY_QLC_DANTUO;
			isJiXuanDanTuo = !isJiXuanDanTuo;
			if (!isJiXuanZhiXuan && !isJiXuanDanTuo) {
				isJiXuanZhiXuan = true;
			}
			createBuyView(iCurrentButton);
			break;
		case 3:// 合买
//			sensor.stopAction();
//			Intent intent = new Intent(QLC.this, JoinHall.class);
//			startActivity(intent);
//			topButtonGroup.check(1);
			break;
		}

		mHScrollView.fullScroll(ScrollView.FOCUS_UP);
	}

	/*
	 * 获取当前高亮button的id
	 * 
	 * @return int id
	 */
	public int getCurrentTab() {
		return topButtonGroup.getCheckedRadioButtonId();
	}

	/*
	 * SeekBar改变时的回调函数
	 * 
	 * @param SeekBar seekBar 发生变化的SeekBar实例
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
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		// 单式
		case R.id.qlc_fushi_seek_beishu:
		case R.id.qlc_dantuo_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.qlc_fushi_seek_qishu:
		case R.id.qlc_dantuo_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		// 单式
		case R.id.ssq_fushi_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			if (!isJiXuanZhiXuan)
				changeTextSumMoney();
			break;
		case R.id.ssq_fushi_seek_qishu:
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
		// TODO Auto-generated method stub

	}

	// Seekbar回调函数 暂时不用
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	/*
	 * 响应小球被点击的回调函数
	 * 
	 * @param View v 被点击的view
	 * 
	 * @return void
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int iBallId = v.getId();
		// red ball
		if (iBallId >= RED_BALL_START && iBallId < BLUE_BALL_START) {
			int iBallViewId = v.getId() - RED_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- red:" + iBallViewId
						+ " buttonnum:" + iCurrentButton);
				// redBallTable.ballViewVector.elementAt(iBallViewId).showNextId();
				changeBuyViewByRule(iCurrentButton, 0, iBallViewId);// 点红球组
			}
		} else if (iBallId >= BLUE_BALL_START && iBallId < RED_TUO_BALL_START) {
			int iBallViewId = v.getId() - BLUE_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				PublicMethod.myOutput("----- blue:" + iBallViewId);
				// blueBallTable.ballViewVector.elementAt(iBallViewId).showNextId();
				changeBuyViewByRule(iCurrentButton, 1, iBallViewId);// 点蓝球组
			}
		} else {
			int iBallViewId = v.getId() - RED_TUO_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				// PublicMethod.myOutput("----- blue:"+iBallViewId);
				// blueBallTable.ballViewVector.elementAt(iBallViewId).showNextId();
				changeBuyViewByRule(iCurrentButton, 2, iBallViewId);// 点蓝球组
			}
		}
		// text_sum_money
		changeTextSumMoney();
	}

	/*
	 * 双色球注数的计算方法
	 * 
	 * @return int 返回注数
	 */
	private int getZhuShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_QLC_DANSHI:
			iReturnValue = mSeekBarBeishu.getProgress();
			iSendZhushu = 1;
			break;
		case PublicConst.BUY_QLC_ZHIXUAN:
			int iRedBalls = redBallTable.getHighlightBallNums();
			// int iBlueBalls = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getQLCFSZhuShu(iRedBalls);
			// PublicMethod.myOutput("-----***"+iReturnValue+" "+iRedBalls+" "+iBlueBalls);
			break;
		case PublicConst.BUY_QLC_DANTUO:
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
			// int iBlueHighlights = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getQLCDTZhuShu(iRedHighlights,
					iRedTuoHighlights);
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	// 获取期数 2010/7/11 陈晨
	private int getQiShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_QLC_DANSHI:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		case PublicConst.BUY_QLC_ZHIXUAN:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		case PublicConst.BUY_QLC_DANTUO:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	/*
	 * 胆拖玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aRedTuoBalls 红球拖码个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getQLCDTZhuShu(int aRedBalls, int aRedTuoBalls) {// 得到双色球胆拖的注数
		long qlcZhuShu = 0L;
		iSendZhushu = 0L;
		if (aRedBalls > 0 && aRedTuoBalls > 0) {
			qlcZhuShu += (PublicMethod.zuhe(7 - aRedBalls, aRedTuoBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(7 - aRedBalls, aRedTuoBalls);
		}
		return qlcZhuShu;
	}

	/*
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getQLCFSZhuShu(int aRedBalls) {
		long qlcZhuShu = 0L;
		iSendZhushu = 0L;
		if (aRedBalls > 0) {
			qlcZhuShu += (PublicMethod.zuhe(7, aRedBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(7, aRedBalls);
		}
		return qlcZhuShu;

	}

	private long getSendZhushu() {
		return iSendZhushu;
	}

	private int getBeishu() {
		return mSeekBarBeishu.getProgress();

	}

	private int getQishu() {
		return mSeekBarQishu.getProgress();

	}

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

	/*
	 * 改变提示信息
	 * 
	 * @return void
	 */
	public void changeTextSumMoney() {
		switch (iCurrentButton) {
		case PublicConst.BUY_QLC_DANSHI:
			// 单式View
			if (redBallTable.getHighlightBallNums() < 7) {
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_red_number));
			} else {

				int iZhuShu = getZhuShu();
				String iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2)
						+ "元";
				mTextSumMoney.setText(iTempString);

			}
			break;
		case PublicConst.BUY_QLC_ZHIXUAN: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			// int iBlueHighlights = blueBallTable.getHighlightBallNums();

			// 红球数 不足//fqc edit 当个数满足个数的时候显示相应的提示
			if (iRedHighlights < 8) {
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_number));
			}
			// 红球数达到最低要求
			if (iRedHighlights == 7) {
				int iZhuShu = getZhuShu();
				String iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2)
						+ "元";
				mTextSumMoney.setText(iTempString);
			} else if (iRedHighlights >= 8) {

				int iZhuShu = getZhuShu();
				String iTempString = "当前为复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);

			}

			break;
		}
		case PublicConst.BUY_QLC_DANTUO: {
			int iRedDanHighlights = redBallTable.getHighlightBallNums();
			// int iBlueHighlights = blueBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();

			if (iRedDanHighlights + iRedTuoHighlights < 8) {
				mTextSumMoney.setText(getResources().getString(
						R.string.choose_number_dialog_tip5));
			}

			else {
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

	// 根据玩法改变当前View 及响应
	// param1 - aWhichTopButton 当前顶部标签位置
	// param2 - aWhichGroupBall 第几组小球，如单式共两组小球，从0开始计数
	// param3 - aBallViewId 被click小球的id，从0开始计数，小球上显示的数字为id+1
	private void changeBuyViewByRule(int aWhichTopButton, int aWhichGroupBall,
			int aBallViewId) {
		switch (aWhichTopButton) {
		case PublicConst.BUY_QLC_DANSHI:
			// 单式View中，有2个ball table
			buy_DANSHI(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_QLC_ZHIXUAN:
			buy_ZHIXUAN(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_QLC_DANTUO:
			buy_DANTUO(aWhichGroupBall, aBallViewId);
			break;
		default:
			break;
		}
	}

	/*
	 * 胆拖玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * 
	 * @param int aBallViewId 小球id
	 * 
	 * @return void
	 */
	private void buy_DANTUO(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球 胆区 最多6个小球
			int iChosenBallSum = 6;
			if (redTuoBallTable.getHighlightBallNums() > 20)
				iChosenBallSum = 26 - redTuoBallTable.getHighlightBallNums();
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
				danEdit.setText("");
				danEdit.setHint(getResources().getString(
						R.string.qlc_dantuo_edit_Prompt_danma));
			} else {
				danEdit.setText(red_zhuma_string);
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
				tuoEdit.setText("");
				tuoEdit.setHint(getResources().getString(
						R.string.qlc_dantuo_edit_Prompt_tuoma));
			} else {
				tuoEdit.setText(red_tuo_zhuma_string);
			}

		} else if (aWhichGroupBall == 1) { // 篮球区

		} else if (aWhichGroupBall == 2) { // 红球 拖区 最多29个小球

			int iChosenBallSum = 25;
			if (redBallTable.getHighlightBallNums() > 1)
				iChosenBallSum = 26 - redBallTable.getHighlightBallNums();
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
				danEdit.setText("");
				danEdit.setHint(getResources().getString(
						R.string.qlc_dantuo_edit_Prompt_danma));
			} else {
				danEdit.setText(red_zhuma_string);
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
				tuoEdit.setText("");
				tuoEdit.setHint(getResources().getString(
						R.string.qlc_dantuo_edit_Prompt_tuoma));
			} else {
				tuoEdit.setText(red_tuo_zhuma_string);
			}
		}
	}

	/*
	 * 复式玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * 
	 * @param int aBallViewId 小球id
	 * 
	 * @return void
	 */
	private void buy_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			// 最多选取16个红球
			int iChosenBallSum = 16;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.myOutput("****isHighLight " + isHighLight
					+ "PublicConst.BALL_TO_HIGHLIGHT "
					+ PublicConst.BALL_TO_HIGHLIGHT);

			// 记录到输入框fulei
			String red_zhuma_string = "  ";
			int[] redZhuMa = redBallTable.getHighlightBallNOs();
			for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
				red_zhuma_string += PublicMethod.getZhuMa(redZhuMa[i]);
				if (i != redBallTable.getHighlightBallNOs().length - 1)
					red_zhuma_string = red_zhuma_string + ",";
			}

			if (red_zhuma_string.equals("  ")) {
				redEdit.setText("");
			} else {
				redEdit.setText(red_zhuma_string);
			}

		} else if (aWhichGroupBall == 1) {

		}
	}

	/*
	 * 单式玩法改变View
	 * 
	 * @param int aWhichGroupBall 被点击的小球位置
	 * 
	 * @param int aBallViewId 小球id
	 * 
	 * @return void
	 */
	private void buy_DANSHI(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			int iChosenBallSum = 7;
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.myOutput("****isHighLight " + isHighLight
					+ "PublicConst.BALL_TO_HIGHLIGHT "
					+ PublicConst.BALL_TO_HIGHLIGHT);

		} else if (aWhichGroupBall == 1) {

		}
	}

	public static int /* String */getDisplayMetrics(Context cx) {
		String str = "";
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
		// TODO Auto-generated method stub
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_qlc, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.qlc_menu_confirm:
			beginTouZhu();
			break;
		case R.id.qlc_menu_reselect_num:
			beginReselect();
			break;
		case R.id.qlc_menu_game_introduce:
			showGameIntroduction();

			break;
		case R.id.qlc_menu_cancel:
			this.finish();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void onCancelClick() {
		// TODO Auto-generated method stub
		// PublicVarAndFun.OutPutString("--->>>Dialog Cancel");
	}

	/*
	 * 对话框回调函数
	 * 
	 * @param int[] aNums 对话框返回的数值
	 * 
	 * @return BallTable
	 */
	public void onOkClick(int[] aNums) {
		// TODO Auto-generated method stub

		// 复式 返回2个整型
		if (aNums.length == 2) {
			iFushiRedBallNumber = aNums[0];
			iFushiBlueBallNumber = aNums[1];
			// redBallTable.randomChoose(iFushiRedBallNumber);
			// blueBallTable.randomChoose(iFushiBlueBallNumber);
			redBallTable.randomChooseConfigChange(iFushiRedBallNumber, 0);
			changeTextSumMoney();
		} else if (aNums.length == 3) {
			iDantuoRedDanNumber = aNums[0];
			iDantuoRedTuoNumber = aNums[1];
			// iDantuoBlueNumber = aNums[2];
			// 机选范围为29 wyl 20100714
			int[] iTotalRandoms = PublicMethod.getRandomsWithoutCollision(
					aNums[0] + aNums[1], 0, 29);
			if (isJiXuanDanTuo) {
				redBallTable.removeView();
				redTuoBallTable.removeView();
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				int[] iTotalRandomsRedDan = new int[iDantuoRedDanNumber];
				int[] iTotalRandomsRedTuo = new int[iDantuoRedTuoNumber];
				for (int i = 0; i < iDantuoRedDanNumber; i++) {
					iTotalRandomsRedDan[i] = iTotalRandoms[i];
				}
				for (int i = iDantuoRedDanNumber; i < iDantuoRedDanNumber+ iDantuoRedTuoNumber; i++) {
					iTotalRandomsRedTuo[i - iDantuoRedDanNumber] = iTotalRandoms[i];
				}
				iTotalRandomsRedDan = PublicMethod.orderby(iTotalRandomsRedDan,"abc");
				iTotalRandomsRedTuo = PublicMethod.orderby(iTotalRandomsRedTuo,"abc");
				PublicMethod.makeBallTableJiXuan(redBallTable, iScreenWidth, redBallResId, iTotalRandomsRedDan,this);
				PublicMethod.makeBallTableJiXuan(redTuoBallTable, iScreenWidth, redBallResId, iTotalRandomsRedTuo,this);
				changeTextSumMoney();
			} else {
				redBallTable.clearAllHighlights();
				redTuoBallTable.clearAllHighlights();
				int i;
				for (i = 0; i < iDantuoRedDanNumber; i++) {
					int isHighLight = redBallTable.changeBallState(6,iTotalRandoms[i]);
				}
				for (i = iDantuoRedDanNumber; i < iDantuoRedDanNumber
						+ iDantuoRedTuoNumber; i++) {
					int isHighLight = redTuoBallTable.changeBallState(29,iTotalRandoms[i]);
				}
				changeTextSumMoney();
			}

		}

	}

	// 开始投注 响应点击投注按钮 和 menu里面的投注选项
	private void beginTouZhu() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(QLC.this,
				"addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (sessionIdStr == null || sessionIdStr.equals("")) {
			Intent intentSession = new Intent(QLC.this, UserLogin.class);
			startActivityForResult(intentSession,0);
		} else{
		 if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
			int iZhuShu = getZhuShu();
			// 获取期数 陈晨 20100711
			int iQiShu = getQiShu();
		 if (redBallTable.getHighlightBallNums() < 7) {
				alert1("请选择7个球");
			} else if (iZhuShu * 2 > 100000) {
				DialogExcessive();
			} else {
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alert(sTouzhuAlert);
			}
		} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
			if (isJiXuanZhiXuan) {
				if (sessionIdStr == null || sessionIdStr.equals("")) {
					Intent intentSession = new Intent(QLC.this, UserLogin.class);
					startActivity(intentSession);
					// 陈晨 20100728 判断条件加限制，可以选择6个红球1个蓝球
				} else if (balls.size() == 0) {
					alert1("请至少选择1注彩票");
				} else {

					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlertJixuan();
					alert_jixuan(sTouzhuAlert);
				}

			} else {
				int iZhuShu = getZhuShu();
				int iQiShu = getQiShu();
				 if (redBallTable.getHighlightBallNums() < 7
						|| redBallTable.getHighlightBallNums() > 16) {
					alert1("请至少选择7~16个球");
				} else if (iZhuShu * 2 > 100000) {
					DialogExcessive();
				} else {
					// alert("您选择的是"+iZhuShu+"注七乐彩复式彩票，"+"共计"+(iZhuShu*2)+"元");

					String sTouzhuAlert = "";
					sTouzhuAlert = getTouzhuAlert();
					alert(sTouzhuAlert);
				}
			}
		} else if (iCurrentButton == PublicConst.BUY_QLC_DANTUO) {
			int iZhuShu = getZhuShu();
			int iQiShu = getQiShu();
			int redballno = redBallTable.getHighlightBallNums();
			int redtuoballno = redTuoBallTable.getHighlightBallNums();
			int redNumber = redBallTable.getHighlightBallNums();
			int redTuoNumber = redTuoBallTable.getHighlightBallNums();
			// int blueNumber = blueBallTable.getHighlightBallNums();
			 if ((redNumber < 1 || redNumber > 6)&& (redTuoNumber < 1 || redTuoNumber > 29)) {
				alert1("请选择1~6个胆码，1~29个拖码！");
			} else if (redNumber + redTuoNumber < 8) {
				alert1("胆码和拖码之和至少为8个！");
			} else if (iZhuShu <= 0) {
				alert1("胆码和拖码之和至少为8个！");
			} else if (iZhuShu * 2 > 100000) {
				DialogExcessive();
			} else {
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alert_dantuo(sTouzhuAlert);
			}
		  }
		}
	}

	// 点击menu的重新机选 调用重新机选的方法
	private void beginReselect() {

		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
			redBallTable.clearAllHighlights();
		} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
			redBallTable.clearAllHighlights();
		} else if (iCurrentButton == PublicConst.BUY_QLC_DANTUO) {
			redBallTable.clearAllHighlights();
			redTuoBallTable.clearAllHighlights();
		}
	}

	// 点击menu里面的玩法介绍，通过对话框显示
	private void showGameIntroduction() {
		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_qlc.html";
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
	 * 七乐彩单复式投注调用函数
	 * 
	 * @param String
	 *            提示框信息
	 * @return
	 */
	private void alert(String string) {
		String zhuma = zhuma_danshi();

		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
									qlc_b_touzhu_danshi.setClickable(false);
									qlc_b_touzhu_danshi
											.setImageResource(R.drawable.touzhuup_n);
								} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
									qlc_b_touzhu_fushi.setClickable(false);
									qlc_b_touzhu_fushi
											.setImageResource(R.drawable.touzhuup_n);
								}
								showDialog(DIALOG1_KEY);
								// TODO Auto-generated method stub
								// 加入是否改变切入点判断 陈晨 8.11
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									String str = "00";

									@Override
									public void run() {
										// TODO Auto-generated method stub
										int iRedHighlights = redBallTable
												.getHighlightBallNums();

										// 判断是七乐彩单式还是复式
										if (iRedHighlights == 7) {
											String zhuma = zhuma_danshi();

											str = pay(zhuma, iQiShu + "",iZhuShu * iQiShu * 200 + "");

										} else if (iRedHighlights > 7) {
											String zhuma_fushi = zhuma_fushi();
											str = pay(zhuma_fushi, iQiShu + "",iZhuShu * iQiShu * 200 + "");
										}

										if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
											qlc_b_touzhu_danshi.setClickable(true);
										} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {
											qlc_b_touzhu_fushi.setClickable(true);
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
								// TODO Auto-generated method stub
							}

						});
		dialog.show();

	}

	/**
	 * 七乐彩胆拖投注调用函数
	 * 
	 * @param String
	 *            提示框信息
	 * @return
	 */
	private void alert_dantuo(String string) {
		// String zhuma=zhuma_danshi();

		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是").setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						qlc_b_touzhu_dantuo
								.setImageResource(R.drawable.touzhuup_n);
						qlc_b_touzhu_dantuo.setClickable(false);
						showDialog(DIALOG1_KEY);

						showDialog(DIALOG1_KEY);
						// TODO Auto-generated method stub
						// 加入是否改变切入点判断 陈晨 8.11
						Thread t = new Thread(new Runnable() {
							int iZhuShu = getZhuShu();
							int iQiShu = getQiShu();
							String str = "00";

							@Override
							public void run() {
								String zhuma = zhuma_dantuo();
								// 新接口 2010/7/11 陈晨
								str = pay(zhuma, iQiShu + "", iZhuShu * iQiShu
										* 200 + "");

								qlc_b_touzhu_dantuo.setClickable(true);
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
								// TODO Auto-generated method stub
							}

						});
		dialog.show();

	}

	// count 期数 amount 总钱数 新接口 陈晨20100711
	/**
	 * 投注新接口
	 * 
	 * @param bets
	 *            注码
	 * @param count
	 *            期数
	 * @param amount
	 *            投注总金额
	 * @param
	 * @param
	 */
	protected String pay(String bets, String count, String amount) {
		// TODO Auto-generated method stub
		BettingInterface betting = BettingInterface.getInstance();

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");

		String error_code = betting.bettingNew(bets, count, amount, sessionid);

		return error_code;
	}

	/**
	 * 七乐彩单式式注码格式
	 * 
	 * @param
	 * @return
	 */
	private String zhuma_danshi() {
		// int zhushu=getZhuShu();
		int beishu = getBeishu();
		int[] zhuma=null;
		String t_str = "1512-F47102-";
		if (isJiXuanZhiXuan) {
			int zhushu = balls.size() ;
			t_str += "00-";
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
					t_str += "0" + beishu;
				}
				if (beishu >= 10) {
					t_str += beishu;
				}
				zhuma = balls.get(j).getRed();
				for (int i = 0; i < zhuma.length; i++) {
					int zhu = zhuma[i] + 1;
					if (zhu >= 10) {
						t_str += zhu;
					} else if (zhu < 10) {
						t_str += "0" + zhu;
					}
				}
				t_str += "^";
			}
		} else {
			t_str += "00-";

			if (beishu < 10) {
				t_str += "0"+beishu;
			}
			if (beishu >= 10) {
				t_str += beishu;
			}
			t_str += "-00";
			if (beishu < 10) {
				t_str += "0" + beishu;
			}
			if (beishu >= 10) {
				t_str += beishu;
			}
			zhuma = redBallTable.getHighlightBallNOs();
			for (int i = 0; i < zhuma.length; i++) {
				if (zhuma[i] >= 10) {
					t_str += zhuma[i];
				} else if (zhuma[i] < 10) {
					t_str += "0" + zhuma[i];
				}
			}
			t_str += "^";
		}

		return t_str;
	}

	/**
	 * 七乐彩复式注码格式
	 * 
	 * @param
	 * @return
	 */
	private String zhuma_fushi() {
		int beishu = getBeishu();
		long sendzhushu = getSendZhushu();
		zhuma = redBallTable.getHighlightBallNOs();
		String t_str = "1512-F47102-";
		t_str += "10-";
		if (sendzhushu < 10) {
			t_str += "0" + sendzhushu;
		}
		if (sendzhushu >= 10) {
			t_str += sendzhushu;
		}
		t_str += "-10";
		if (beishu < 10) {
			t_str += "0" + beishu;
		}
		if (beishu >= 10) {
			t_str += beishu;
		}
		t_str += "*";
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "^";
		PublicMethod.myOutput("-----------------qilecaifushi" + t_str);
		return t_str;
	}

	/**
	 * 七乐彩胆拖注码格式
	 * 
	 * @param
	 * @return
	 */
	private String zhuma_dantuo() {
		int beishu = getBeishu();
		long sendzhushu = getSendZhushu();
		zhuma = redBallTable.getHighlightBallNOs();
		int[] zhumablue = redTuoBallTable.getHighlightBallNOs();
		String t_str = "1512-F47102-";
		t_str += "20-";
		if (sendzhushu < 10) {
			t_str += "0" + sendzhushu;
		}
		if (sendzhushu >= 10) {
			t_str += sendzhushu;
		}
		t_str += "-20";
		if (beishu < 10) {
			t_str += "0" + beishu;
		}
		if (beishu >= 10) {
			t_str += beishu;
		}

		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "*";
		for (int i = 0; i < zhumablue.length; i++) {
			if (zhumablue[i] >= 10) {
				t_str += zhumablue[i];
			} else if (zhumablue[i] < 10) {
				t_str += "0" + zhumablue[i];
			}
		}
		t_str += "^";
		PublicMethod.myOutput("-----------------qilecaifushi" + t_str);
		return t_str;
	}

	public void showHighLight() {
		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
			topButtonGroup.check(1);
			topButtonGroup.check(0);
			topButtonGroup.invalidate();
		} else if (iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN)
			topButtonGroup.check(1);
		else
			topButtonGroup.check(2);
		PublicMethod.myOutput("**********topButtonGroup.check(0);   "
				+ iCurrentButton + " PublicConst.BUY_QLC_DANSHI  "
				+ PublicConst.BUY_QLC_DANSHI);
	}

	/**
	 * 网络连接提示框
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
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

	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string = red_zhuma_string + String.valueOf(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1)
				red_zhuma_string = red_zhuma_string + ",";
		}

		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI
				|| iCurrentButton == PublicConst.BUY_QLC_ZHIXUAN) {

			return "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
					+ "\n" + "注码：" + "\n" + red_zhuma_string + "\n" + "确认支付吗？";

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
					+ "\n" + "注码：\n" + "胆码：" + red_zhuma_string + "\n" + "拖码："
					+ red_tuo_zhuma_string + "\n" + "确认支付吗？";
		}
	}

	/**
	 * 单笔投注大于10万元时的对话框
	 */
	private void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(QLC.this);
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
		int[] redNum = new int[7];

		public Balls() {
			redNum = PublicMethod.getRandomsWithoutCollision(7, 0, 29);
			redNum = PublicMethod.orderby(redNum, "abc");

		}

		public int[] getRed() {
			return redNum;

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

	}

	/**
	 * 手机震动
	 * 
	 * @author Administrator
	 * 
	 */
	class SsqSensor extends SensorActivity {

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.palmdream.netintface.SensorActivity#action()
		 */
		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
			zhuView.removeAllViews();
			balls = new Vector();
			for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
				Balls ball = new Balls();
				balls.add(ball);
			}
			createTable(zhuView);
		}
	}

	/**
	 * 创建直选机选界面
	 * 
	 * @作者：
	 * @日期：
	 * @参数：
	 * @返回值：
	 * @修改人：
	 * @修改内容：
	 * @修改日期：
	 * @版本：
	 */
	public void createTable(LinearLayout layout) {

		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			BallTable ballTableRed = new BallTable(lines, RED_BALL_START);
			PublicMethod.makeBallTableJiXuan(ballTableRed, iScreenWidth, redBallResId, balls.get(i)
									.getRed(), this);
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (balls.size() > 1) {
						zhuView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhuView);
					} else {
						Toast.makeText(
								QLC.this,
								getResources().getText(
										R.string.zhixuan_jixuan_toast),
								Toast.LENGTH_SHORT).show();
					}

				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		
			param.setMargins(15, 10, 0, 0);
			lines.addView(delet,param);
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
			zhumaString += balls.get(i).getRedZhu();
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
	 * 胆拖投注调用函数
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	private void alert_jixuan(String string) {
		sensor.stopAction();
		Dialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是").setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						qlc_b_touzhu_fushi
								.setImageResource(R.drawable.touzhuup_n);
						qlc_b_touzhu_fushi.setClickable(false);

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
								str = pay(zhuma, iQiShu + "", iZhuShu * 200
										* iQiShu + "");
								// }

								qlc_b_touzhu_fushi.setClickable(true);
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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensor.stopAction();
	}
	/**
	 * intent回调函数
	 * 
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
		        beginTouZhu();
			break;
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycleBallTable(redBallTable);
		recycleBallTable(redTuoBallTable);
		recycleBallTable(blueBallTable);
		
	}
	
	
	private void recycleBallTable(BallTable balltable){
		if(balltable != null && balltable.getBallViews()!= null){
			for (Iterator iterator = balltable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
	}

}