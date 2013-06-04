package com.palmdream.RuyicaiAndroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.BallBetPublicClass.BallHolder;
import com.palmdream.netintface.BettingInterface;
import com.palmdream.netintface.iHttp;

public class QLC extends Activity // 事件 回调函数
		implements OnClickListener, // 小球被点击 onClick
		SeekBar.OnSeekBarChangeListener, // SeekBar改变 onProgressChanged
		RadioGroup.OnCheckedChangeListener, // 机选复选框变化 onCheckedChangeListener
		MyDialogListener { // 对话框输入数值 onOKClick

	// 小球图的宽高
	public static final int BALL_WIDTH = 32;
	// 顶部标签个数
	private int iButtonNum = 3;

	// 首行button当前的位置
	private int iCurrentButton; // 0x55660001 --- QLC danshi
	private int tempCurrentButton;
	/*
	 * Button danshiButton; Button fushiButton; Button dantuoButton;
	 */
	public static final int WANFA_START_ID = 0x55550001;

	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;

	private int defaultOffShade;
	private int defaultOnShade;

	int topButtonStringId[] = { R.string.danshi, R.string.fushi,
			R.string.dantuo };

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
	Button qlc_btn_newSelect;
	CheckBox mCheckBox;

	int iProgressBeishu = 1;
	int iProgressQishu = 1;

	private int iScreenWidth;
	// Vector<OneBallView> redBallViewVector;
	BallTable redBallTable = null;
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };

	BallTable redTuoBallTable = null;
	private int redTuoBallResId[] = { R.drawable.grey, R.drawable.red };

	// Vector<OneBallView> blueBallViewVector;
	BallTable blueBallTable = null;
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

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
	private BallHolder mBallHolder = null;
	private BallHolder tempBallHolder = null;
	private BallBetPublicClass ballBetPublicClass = new BallBetPublicClass();

	private SensorManager mSensorManager;
	int lastAccelerometer = SensorManager.SENSOR_ACCELEROMETER;
	public int publicTopButton;
	/** Called when the activity is first created. */
	// 处理投注返回的信息
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 1:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败余额不足", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示注册成功
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "系统结算，请稍后", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "无空闲逻辑机", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示该号被暂停请联系客服
				break;
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功", Toast.LENGTH_LONG)
						.show();
//				投注成功后 清除高亮
				if(iCurrentButton==PublicConst.BUY_QLC_DANSHI||iCurrentButton==PublicConst.BUY_QLC_FUSHI){
					redBallTable.clearAllHighlights();
				}else if(iCurrentButton==PublicConst.BUY_QLC_DANTUO){
					redBallTable.clearAllHighlights();
					redTuoBallTable.clearAllHighlights();
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
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败", Toast.LENGTH_LONG)
						.show();
				// // tv.setText(result);
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
		super.onCreate(savedInstanceState);

		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		ImageButton returnBtn = (ImageButton) findViewById(R.id.goucaitouzhu_title_return);
		returnBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		TextView title = (TextView) findViewById(R.id.goucaitouzhu_title);
		title.setText(getResources().getString(R.string.qilecai));
		mHScrollView = (ScrollView) findViewById(R.id.scroll_global);
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initButtons();

		// ----- 默认 加载单式layout
		// iCurrentButton = PublicConst.BUY_QLC_DANSHI;
		// createBuyView(iCurrentButton);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		PublicMethod.myOutput("********onConfigurationChanged");

		mBallHolder.flag = 1;
		mBallHolder.DanShi.iBeiShu = getBeishu();
		mBallHolder.DanShi.iQiShu = getQishu();
		mBallHolder.DanShi.bCheckBox = getCheckBox();

		PublicMethod
				.myOutput("********mBallHolder.DanShi.bCheckBox            "
						+ mBallHolder.DanShi.bCheckBox);
		tempBallHolder = mBallHolder;
		tempCurrentButton = iCurrentButton;
		if (mBallHolder == null) {
			mBallHolder = ballBetPublicClass.new BallHolder();
			PublicMethod.myOutput("********onConfigurationChanged null");
		}
		initButtons();
		// setCurrentTab(0);
		iCurrentButton = tempCurrentButton;
		createBuyView(iCurrentButton);
		// 切换之后，显示高亮的radioButton
		showHighLight();

		PublicMethod.myOutput("********iCurrentButton" + iCurrentButton);

		mBallHolder = tempBallHolder;
		if (publicTopButton == PublicConst.BUY_QLC_DANSHI) {
			create_QLC_DANSHI();
			changeTextSumMoney();
		} else if (publicTopButton == PublicConst.BUY_QLC_FUSHI) {
			create_QLC_FUSHI();
			changeTextSumMoney();
		} else if (publicTopButton == PublicConst.BUY_QLC_DANTUO) {
			create_QLC_DANTUO();
			changeTextSumMoney();
		}
		mBallHolder.flag = 0;
		super.onConfigurationChanged(newConfig);
	}

	// @Override
	// public Object onRetainNonConfigurationInstance() {
	// TODO Auto-generated method stub
	// for(int i=0;i<33;i++){
	// PublicMethod.myOutput("******mBallHolder[i]" +"  "+i+"  " +
	// mBallHolder.DanShi.iRedBallStatus[i]);
	// }
	// mBallHolder.topButtonGroup = iCurrentButton;
	// return mBallHolder;
	// return super.onRetainNonConfigurationInstance();
	// }

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
		case PublicConst.BUY_QLC_DANSHI: // 单式
			create_QLC_DANSHI();
			break;
		case PublicConst.BUY_QLC_FUSHI: // 复式
			create_QLC_FUSHI();
			break;
		case PublicConst.BUY_QLC_DANTUO: // 胆拖
			create_QLC_DANTUO();
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
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_qlc_dantuo, null);
		{
			int redBallViewNum = 30;
			int redBallViewWidth = QLC.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			redBallTable = makeBallTable(iV, R.id.table_red_danma_qlc,
					iScreenWidth, redBallViewNum, redBallViewWidth,
					redBallResId, RED_BALL_START);
			redTuoBallTable = makeBallTable(iV, R.id.table_red_tuoma_qlc,
					iScreenWidth, redBallViewNum, redBallViewWidth,
					redBallResId, RED_TUO_BALL_START);

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

			qlc_btn_newSelect = (Button) iV
					.findViewById(R.id.qlc_dantuo_btn_newSelect);
			qlc_btn_newSelect.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					mBallHolder = ballBetPublicClass.new BallHolder();
					ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
							QLC.this, 1, QLC.this);
					iChooseNumberDialog.show();
				}

			});
			qlc_b_touzhu_dantuo = (ImageButton) iV
					.findViewById(R.id.qlc_dantuo_b_touzhu);
			qlc_b_touzhu_dantuo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		if (mBallHolder.flag == 1) {
			redBallTable
					.changeBallStateConfigChange(mBallHolder.DanShi.iRedBallStatus);
			redTuoBallTable
					.changeBallStateConfigChange(mBallHolder.DanShi.iTuoRedBallStatus);
			// blueBallTable.changeBallStateConfigChange(mBallHolder.DanShi.iBlueBallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.DanShi.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.DanShi.iQiShu);
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		mCheckBox = (CheckBox) this.findViewById(R.id.qlc_dantuo_cb_jixuan);
		if (mBallHolder.flag == 1) {
			mCheckBox.setChecked(mBallHolder.DanShi.bCheckBox);
			if (mBallHolder.DanShi.bCheckBox)
				qlc_btn_newSelect.setVisibility(View.VISIBLE);
			else
				qlc_btn_newSelect.setVisibility(View.INVISIBLE);
		}
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (mBallHolder.flag != 1) {
								// redBallTable.randomChoose("red");
								// blueBallTable.randomChoose("blue");
								ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
										QLC.this, 1, QLC.this);
								iChooseNumberDialog.show();
								qlc_btn_newSelect.setVisibility(View.VISIBLE);
								// }
							}
						}
						// fqc 20100714
						else {
							qlc_btn_newSelect.setVisibility(View.INVISIBLE);
						}
					}
				});
		/*
		 * //添加toggleButton的事件处理 ToggleButton toggleButton = (ToggleButton)
		 * this.findViewById(R.id.cb_jixuan_dantuo);
		 * toggleButton.setOnCheckedChangeListener(new
		 * OnCheckedChangeListener(){
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * });
		 */
	}

	/*
	 * 创建复式购彩View
	 * 
	 * @return void
	 */
	private void create_QLC_FUSHI() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_qlc_fushi, null);
		{
			int redBallViewNum = 30;
			int redBallViewWidth = QLC.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			redBallTable = makeBallTable(iV, R.id.table_red_fushi_qlc,
					iScreenWidth, redBallViewNum, redBallViewWidth,
					redBallResId, RED_BALL_START);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_fushi_qlc);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

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

			qlc_btn_newSelect = (Button) iV
					.findViewById(R.id.qlc_fushi_btn_newSelect);
			qlc_btn_newSelect.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					mBallHolder = ballBetPublicClass.new BallHolder();
					ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
							QLC.this, 0, QLC.this);
					iChooseNumberDialog.show();
				}

			});
			qlc_b_touzhu_fushi = (ImageButton) iV
					.findViewById(R.id.qlc_fushi_b_touzhu);
			qlc_b_touzhu_fushi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			redBallTable
					.changeBallStateConfigChange(mBallHolder.DanShi.iRedBallStatus);
			// blueBallTable.changeBallStateConfigChange(mBallHolder.DanShi.iBlueBallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.DanShi.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.DanShi.iQiShu);

		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		mCheckBox = (CheckBox) this.findViewById(R.id.qlc_fushi_cb_jixuan);
		if (mBallHolder.flag == 1) {
			mCheckBox.setChecked(mBallHolder.DanShi.bCheckBox);
			if (mBallHolder.DanShi.bCheckBox)
				qlc_btn_newSelect.setVisibility(View.VISIBLE);
			else
				qlc_btn_newSelect.setVisibility(View.INVISIBLE);
		}
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (mBallHolder.flag != 1) {// 是点击事件触发机选的checkbox，不是切屏触发后触发的事件。
								ChooseNumberDialogQLC iChooseNumberDialog = new ChooseNumberDialogQLC(
										QLC.this, 0, QLC.this);
								iChooseNumberDialog.show();
								// redBallTable.randomChoose("red");
								// blueBallTable.randomChoose("blue");
								qlc_btn_newSelect.setVisibility(View.VISIBLE);
								changeTextSumMoney();
							}
						} else {
							qlc_btn_newSelect.setVisibility(View.INVISIBLE);
						}
					}
				});
		/*
		 * //添加toggleButton的事件处理 ToggleButton toggleButton = (ToggleButton)
		 * this.findViewById(R.id.cb_jixuan_fushi);
		 * toggleButton.setOnCheckedChangeListener(new
		 * OnCheckedChangeListener(){
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub
		 * if(isChecked){ redBallTable.randomChoose("red");
		 * blueBallTable.randomChoose("blue"); changeTextSumMoney(); } } });
		 */
	}

	/*
	 * 创建单式购彩View
	 * 
	 * @return void
	 */
	private void create_QLC_DANSHI() {
		buyView.removeAllViews();
		// View iV = View.inflate(this, R.layout.layout_qlc_danshi, null);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_qlc_danshi, null);
		{
			int redBallViewNum = 30;
			int redBallViewWidth = QLC.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			redBallTable = makeBallTable(iV, R.id.table_red_qlc, iScreenWidth,
					redBallViewNum, redBallViewWidth, redBallResId,
					RED_BALL_START);
			// redBallTable.tableLayout.

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.qlc_danshi_text_sum_money);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV
					.findViewById(R.id.qlc_danshi_seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV
					.findViewById(R.id.qlc_danshi_seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV
					.findViewById(R.id.qlc_danshi_text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.qlc_danshi_text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			/*
			 * 点击加减图标，对seekbar进行设置：
			 * 
			 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加
			 * -1表示减 final SeekBar mSeekBar
			 * 
			 * @return void
			 */
			setSeekWhenAddOrSub(R.id.qlc_danshi_seekbar_subtract_beishu, iV,
					-1, mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_danshi_seekbar_add_beishu, iV, 1,
					mSeekBarBeishu, true);
			setSeekWhenAddOrSub(R.id.qlc_danshi_seekbar_subtract_qihao, iV, -1,
					mSeekBarQishu, false);
			setSeekWhenAddOrSub(R.id.qlc_danshi_seekbar_add_qihao, iV, 1,
					mSeekBarQishu, false);

			qlc_btn_newSelect = (Button) iV
					.findViewById(R.id.qlc_danshi_btn_newSelect);
			qlc_btn_newSelect.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					mBallHolder = ballBetPublicClass.new BallHolder();
					mBallHolder = redBallTable.randomChooseConfigChange(7,
							mBallHolder, 0);
					// mBallHolder =
					// blueBallTable.randomChooseConfigChange(1,mBallHolder,1);
					// redBallTable.randomChoose(7);
				}

			});
			// 投注button响应事件
			qlc_b_touzhu_danshi = (ImageButton) iV
					.findViewById(R.id.qlc_danshi_b_touzhu);
			qlc_b_touzhu_danshi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			redBallTable
					.changeBallStateConfigChange(mBallHolder.DanShi.iRedBallStatus);
			// blueBallTable.changeBallStateConfigChange(mBallHolder.DanShi.iBlueBallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.DanShi.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.DanShi.iQiShu);
			// iCB_Danshi.setChecked(mBallHolder.DanShi.bCheckBox);
			//			
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		mCheckBox = (CheckBox) this.findViewById(R.id.qlc_danshi_jixuan_cb);
		if (mBallHolder.flag == 1) {
			mCheckBox.setChecked(mBallHolder.DanShi.bCheckBox);
			if (mBallHolder.DanShi.bCheckBox)
				qlc_btn_newSelect.setVisibility(View.VISIBLE);
			else
				qlc_btn_newSelect.setVisibility(View.INVISIBLE);
		}
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							if (mBallHolder.flag != 1) {
								mBallHolder = ballBetPublicClass.new BallHolder();
								// redBallTable.randomChoose(7);
								// blueBallTable.randomChoose(1);
								mBallHolder = redBallTable
										.randomChooseConfigChange(7,
												mBallHolder, 0);
								//
								// mBallHolder =
								// blueBallTable.randomChooseConfigChange(1,mBallHolder,1);

								qlc_btn_newSelect.setVisibility(View.VISIBLE);
								// redBallTable.randomChoose("red");
								// blueBallTable.randomChoose("blue");
								changeTextSumMoney();
							}
						} else {
							qlc_btn_newSelect.setVisibility(View.INVISIBLE);
						}
					}
				});

		/*
		 * //添加toggleButton的事件处理 ToggleButton toggleButton = (ToggleButton)
		 * this.findViewById(R.id.cb_jixuan_danshi);
		 * toggleButton.setOnCheckedChangeListener(new
		 * OnCheckedChangeListener(){
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub
		 * if(isChecked){ redBallTable.randomChoose("red");
		 * blueBallTable.randomChoose("blue"); } } });
		 */
	}

	/*
	 * 初始化顶部的button
	 * 
	 * @return void
	 */
	private void initButtons() {
		if (mBallHolder != null)
			if (mBallHolder.flag == 1)
				initTopButtons();
		if (mBallHolder == null)
			initTopButtons();
		commit();
	}

	private void initTopButtons() {
		topBar = (HorizontalScrollView) findViewById(R.id.topBar);
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
			// int[] iconStates = { RadioStateDrawable.SHADE_GRAY,
			// RadioStateDrawable.SHADE_GREEN};
			// tabButton.setState(
			// getResources().getString(stringId[i]),iconStates[0]);
			// tabButton.setState(getResources().getString(stringId[i]),drawableId[i%2],
			// iconStates[0], iconStates[1]);
			tabButton.setState(getResources().getString(topButtonStringId[i]));
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			topButtonGroup.addView(tabButton, i, topButtonLayoutParams);
		}
		if (getLastNonConfigurationInstance() != null) {
			mBallHolder = (BallHolder) getLastNonConfigurationInstance();
			int buttonGroup = mBallHolder.topButtonGroup;
			PublicMethod.myOutput("*********buttonGroup " + buttonGroup);
			setCurrentTab(buttonGroup);
		} else {
			mBallHolder = ballBetPublicClass.new BallHolder();
			setCurrentTab(0);
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
		topButtonGroup.check(index);
		// startGroupActivity(titleList.get(index).toString(),
		// (Intent)intentList.get(index));
		switch (index) {
		case 0: // 单式
			iCurrentButton = PublicConst.BUY_QLC_DANSHI;
			createBuyView(iCurrentButton);
			break;
		case 1: // 复式
			iCurrentButton = PublicConst.BUY_QLC_FUSHI;
			createBuyView(iCurrentButton);
			break;
		case 2: // 胆拖
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
		mBallHolder = ballBetPublicClass.new BallHolder();

		switch (checkedId) {
		case 0: // 单式
			iCurrentButton = PublicConst.BUY_QLC_DANSHI;
			createBuyView(iCurrentButton);
			break;
		case 1: // 复式
			iCurrentButton = PublicConst.BUY_QLC_FUSHI;
			createBuyView(iCurrentButton);
			break;
		case 2: // 胆拖
			iCurrentButton = PublicConst.BUY_QLC_DANTUO;
			createBuyView(iCurrentButton);
			break;
		}
		// PublicMethod.myOutput("-----top"+mHScrollView.getTop());
		// mHScrollView.scrollBy(0, mHScrollView.getTop());
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
		case R.id.qlc_danshi_seek_beishu:
		case R.id.qlc_fushi_seek_beishu:
		case R.id.qlc_dantuo_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.qlc_danshi_seek_qishu:
		case R.id.qlc_fushi_seek_qishu:
		case R.id.qlc_dantuo_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		/*
		 * case R.id.seek_beishu_fushi: iProgressBeishu=iProgress;
		 * mTextBeishu.setText(""+iProgressBeishu); changeTextSumMoney(); break;
		 * 
		 * case R.id.seek_qishu_fushi: iProgressQishu=iProgress;
		 * mTextQishu.setText(""+iProgressQishu); break; case
		 * R.id.seek_beishu_dantuo: iProgressBeishu=iProgress;
		 * mTextBeishu.setText(""+iProgressBeishu); changeTextSumMoney(); break;
		 * case R.id.seek_qishu_dantuo: iProgressQishu=iProgress;
		 * mTextQishu.setText(""+iProgressQishu); break;
		 */
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
		case PublicConst.BUY_QLC_FUSHI:
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
		case PublicConst.BUY_QLC_FUSHI:
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

	private boolean getCheckBox() {
		return mCheckBox.isChecked();
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
		case PublicConst.BUY_QLC_FUSHI: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			// int iBlueHighlights = blueBallTable.getHighlightBallNums();

			// 红球数 不足//fqc edit 当个数满足个数的时候显示相应的提示
			if (iRedHighlights < 8) {
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_red_number));
			}
			// 红球数达到最低要求
			if (iRedHighlights >= 8) {
				// if(iBlueHighlights<1){
				// mTextSumMoney.setText(getResources().getString(R.string.please_choose_blue_number));
				// }
				// else if(iBlueHighlights==1){
				// int iZhuShu=getZhuShu();
				// String iTempString="当前为单式，共"+iZhuShu+"注，共"+(iZhuShu*2)+"元";
				// mTextSumMoney.setText(iTempString);
				// }
				// else{
				int iZhuShu = getZhuShu();
				String iTempString = "当前为复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元";
				mTextSumMoney.setText(iTempString);
				// }
			}
			// 红球复式
			// else{
			// if(iBlueHighlights<1){
			// mTextSumMoney.setText(getResources().getString(R.string.please_choose_blue_number));
			// }
			// else if(iBlueHighlights==1){
			// int iZhuShu=getZhuShu();
			// String iTempString="当前为红复式，共"+iZhuShu+"注，共"+(iZhuShu*2)+"元";
			// mTextSumMoney.setText(iTempString);
			// }
			// else{
			// int iZhuShu=getZhuShu();
			// String iTempString="当前为全复式，共"+iZhuShu+"注，共"+(iZhuShu*2)+"元";
			// mTextSumMoney.setText(iTempString);
			// }
			// }
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
			// else if(iBlueHighlights<1){
			// mTextSumMoney.setText(getResources().getString(R.string.choose_number_dialog_tip6));
			// }
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
		case PublicConst.BUY_QLC_FUSHI:
			buy_FUSHI(aWhichGroupBall, aBallViewId);
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
			if(redTuoBallTable.getHighlightBallNums()>20)
				iChosenBallSum = 26-redTuoBallTable.getHighlightBallNums();
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.myOutput("****isHighLight " + isHighLight
					+ "PublicConst.BALL_TO_HIGHLIGHT "
					+ PublicConst.BALL_TO_HIGHLIGHT);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 1;
				mBallHolder.DanShi.iTuoRedBallStatus[aBallViewId] = 0;
				redTuoBallTable.clearOnBallHighlight(aBallViewId);
				PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
						+ aBallViewId);
			} else
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 0;
		} else if (aWhichGroupBall == 1) { // 篮球区
			// int iChosenBallSum = 16;
			// int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
			// aBallViewId);
			// if(isHighLight == PublicConst.BALL_TO_HIGHLIGHT){
			// mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 1;
			// PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT" +
			// aBallViewId);
			// }
			// else mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 0;
		} else if (aWhichGroupBall == 2) { // 红球 拖区 最多29个小球
//			int iChosenBallSum = 29;
			int iChosenBallSum = 25;
			if(redBallTable.getHighlightBallNums()>1)
				iChosenBallSum = 26-redBallTable.getHighlightBallNums();
			int isHighLight = redTuoBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DanShi.iTuoRedBallStatus[aBallViewId] = 1;
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 0;
				redBallTable.clearOnBallHighlight(aBallViewId);
				PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
						+ aBallViewId);
			} else
				mBallHolder.DanShi.iTuoRedBallStatus[aBallViewId] = 0;
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
	private void buy_FUSHI(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			// 最多选取16个红球
			int iChosenBallSum = 16;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			PublicMethod.myOutput("****isHighLight " + isHighLight
					+ "PublicConst.BALL_TO_HIGHLIGHT "
					+ PublicConst.BALL_TO_HIGHLIGHT);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 1;
				PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
						+ aBallViewId);
			} else
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 0;
		} else if (aWhichGroupBall == 1) {
			// int iChosenBallSum = 16;
			// int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
			// aBallViewId);
			// if(isHighLight == PublicConst.BALL_TO_HIGHLIGHT){
			// mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 1;
			// PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT" +
			// aBallViewId);
			// }
			// else mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 0;
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
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 1;
				PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
						+ aBallViewId);
			} else
				mBallHolder.DanShi.iRedBallStatus[aBallViewId] = 0;
		} else if (aWhichGroupBall == 1) {
			// int iChosenBallSum = 1;
			// int isHighLight = blueBallTable.changeBallState(iChosenBallSum,
			// aBallViewId);
			// if(isHighLight == PublicConst.BALL_TO_HIGHLIGHT){
			// mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 1;
			// PublicMethod.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT" +
			// aBallViewId);
			// }
			// else mBallHolder.DanShi.iBlueBallStatus[aBallViewId] = 0;
		}
	}

	/*
	 * 创建BallTable
	 * 
	 * @param LinearLayout aParentView 上一级Layout
	 * 
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * 
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * 
	 * @param int aBallNum 小球个数
	 * 
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * 
	 * @param int[] aResId 小球图片Id
	 * 
	 * @param int aIdStart 小球Id起始数值
	 * 
	 * @return BallTable
	 */
	private BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int aFieldWidth, int aBallNum, int aBallViewWidth, int[] aResId,
			int aIdStart) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);
		// BallTable iBallTable=new BallTable(aLayoutId,aIdStart);

		int iBallNum = aBallNum;
		int iBallViewWidth = aBallViewWidth;
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;

		int viewNumPerLine = (iFieldWidth - scrollBarWidth)
				/ (iBallViewWidth + 2);
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (aBallViewWidth + 2)
				* viewNumPerLine) / 2;
		// PublicMethod.myOutput("--------margin:" + margin + " iFieldWidth" +
		// iFieldWidth +" iBallViewWidth:" + iBallViewWidth +" viewNumPerLine:"
		// + viewNumPerLine);
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "" + (iBallViewNo + 1);
				OneBallView tempBallView = new OneBallView(aParentView
						.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(this);

				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			iBallTable.tableLayout
					.addView(tableRow, new TableLayout.LayoutParams(
							PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(this);
			for (int col = 0; col < lastLineViewNum; col++) {
				// PublicMethod.myOutput("-----------"+iBallViewNo);
				String iStrTemp = "" + (iBallViewNo + 1);
				OneBallView tempBallView = new OneBallView(aParentView
						.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(this);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin + 1, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			iBallTable.tableLayout
					.addView(tableRow, new TableLayout.LayoutParams(
							PublicConst.FP, PublicConst.WC));
		}
		// PublicMethod.myOutput("-----w:"+iBallTable.tableLayout.getWidth()+"   h:"+iBallTable.tableLayout.getHeight());
		return iBallTable;
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
		mBallHolder = ballBetPublicClass.new BallHolder();
		// 复式 返回2个整型
		if (aNums.length == 2) {
			iFushiRedBallNumber = aNums[0];
			iFushiBlueBallNumber = aNums[1];
			// redBallTable.randomChoose(iFushiRedBallNumber);
			// blueBallTable.randomChoose(iFushiBlueBallNumber);
			mBallHolder = redBallTable.randomChooseConfigChange(
					iFushiRedBallNumber, mBallHolder, 0);
			changeTextSumMoney();
		} else if (aNums.length == 3) {
			iDantuoRedDanNumber = aNums[0];
			iDantuoRedTuoNumber = aNums[1];
			iDantuoBlueNumber = aNums[2];
			// 机选范围为29 wyl 20100714
			int[] iTotalRandoms = PublicMethod.getRandomsWithoutCollision(
					aNums[0] + aNums[1], 0, 29);

			// redBallTable.randomChoose(iDantuoRedDanNumber);
			// redTuoBallTable.randomChoose(iDantuoRedTuoNumber);
			redBallTable.clearAllHighlights();
			redTuoBallTable.clearAllHighlights();
			int i;
			for (i = 0; i < iDantuoRedDanNumber; i++) {
				// redBallTable.changeBallState(6, iTotalRandoms[i]);
				int isHighLight = redBallTable.changeBallState(6,
						iTotalRandoms[i]);
				PublicMethod.myOutput("****isHighLight " + isHighLight
						+ "PublicConst.BALL_TO_HIGHLIGHT "
						+ PublicConst.BALL_TO_HIGHLIGHT);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DanShi.iRedBallStatus[iTotalRandoms[i]] = 1;
					PublicMethod
							.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
									+ iTotalRandoms[i]);
				} else
					mBallHolder.DanShi.iRedBallStatus[iTotalRandoms[i]] = 0;
			}
			for (i = iDantuoRedDanNumber; i < iDantuoRedDanNumber
					+ iDantuoRedTuoNumber; i++) {
				// redTuoBallTable.changeBallState(20, iTotalRandoms[i]);
				int isHighLight = redTuoBallTable.changeBallState(29,
						iTotalRandoms[i]);
				PublicMethod.myOutput("****isHighLight " + isHighLight
						+ "PublicConst.BALL_TO_HIGHLIGHT "
						+ PublicConst.BALL_TO_HIGHLIGHT);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
					mBallHolder.DanShi.iTuoRedBallStatus[iTotalRandoms[i]] = 1;
					PublicMethod
							.myOutput("****danshi hongqiu BALL_TO_HIGHLIGHT"
									+ iTotalRandoms[i]);
				} else
					mBallHolder.DanShi.iTuoRedBallStatus[iTotalRandoms[i]] = 0;
			}
			// blueBallTable.randomChoose(iDantuoBlueNumber);
			changeTextSumMoney();
		}
		/*
		 * switch(iCurrentButton){ case PublicConst.BUY_QLC_DANSHI: break; case
		 * PublicConst.BUY_QLC_FUSHI: iFushiRedBallNumber = aRedNum;
		 * iFushiBlueBallNumber = aBlueNum; redBallTable.randomChoose(aRedNum);
		 * blueBallTable.randomChoose(aBlueNum); changeTextSumMoney(); break;
		 * case PublicConst.BUY_QLC_DANTUO: break; default: break; }
		 */
	}

	// 开始投注 响应点击投注按钮 和 menu里面的投注选项
	private void beginTouZhu() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(QLC.this,
				"addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
			int iZhuShu = getZhuShu();
			// 获取期数 陈晨 20100711
			int iQiShu = getQiShu();
			if (sessionIdStr.equals("")) {
				Intent intentSession = new Intent(QLC.this, UserLogin.class);
				startActivity(intentSession);
			} else if (redBallTable.getHighlightBallNums() < 7) {
				alert1("请选择7个红球");
			} else if (iZhuShu * 2 > 100000) {
				DialogExcessive();
			} else {
				// String iTempString="共"+iZhuShu+"注，共"+(iZhuShu*2)+"元";
				// alert("您选择的是"+iZhuShu+"注七乐彩单式彩票，"+"共计"+(iZhuShu*2)+"元");
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alert(sTouzhuAlert);
			}
		} else if (iCurrentButton == PublicConst.BUY_QLC_FUSHI) {
			int iZhuShu = getZhuShu();
			int iQiShu = getQiShu();
			if (sessionIdStr.equals("")) {
				Intent intentSession = new Intent(QLC.this, UserLogin.class);
				startActivity(intentSession);
			} else if (redBallTable.getHighlightBallNums() < 8
					|| redBallTable.getHighlightBallNums() > 16) {
				alert1("请至少选择8~16个红球");
			} else if (iZhuShu * 2 > 100000) {
				DialogExcessive();
			} else {
				// alert("您选择的是"+iZhuShu+"注七乐彩复式彩票，"+"共计"+(iZhuShu*2)+"元");
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alert(sTouzhuAlert);
			}
		} else if (iCurrentButton == PublicConst.BUY_QLC_DANTUO) {
			int iZhuShu = getZhuShu();
			int iQiShu = getQiShu();
			int redballno = redBallTable.getHighlightBallNums();
			int redtuoballno = redTuoBallTable.getHighlightBallNums();
			int redNumber = redBallTable.getHighlightBallNums();
			int redTuoNumber = redTuoBallTable.getHighlightBallNums();
			// int blueNumber = blueBallTable.getHighlightBallNums();
			if (sessionIdStr.equals("")) {
				Intent intentSession = new Intent(QLC.this, UserLogin.class);
				startActivity(intentSession);
			} else if ((redNumber < 1 || redNumber > 6)
					&& (redTuoNumber < 1 || redTuoNumber > 29)) {
				alert1("请选择1~6个红球胆码，1~29个红球拖码！");
			} else if (redNumber + redTuoNumber < 8) {
				alert1("红球胆码和红球拖码之和至少为8个！");
			} else if (iZhuShu <= 0) {
				alert1("胆码和拖码之和至少为8个！");
			} else if (iZhuShu * 2 > 100000) {
				DialogExcessive();
			} else {
				// 显示金额算上期数 陈晨 2010/7/11
				// String iTempString="共"+iZhuShu+"注，共"+(iZhuShu*2)+"元";
				// alert_dantuo("您选择的是"+iZhuShu+"注七乐彩胆拖彩票，"+"共计"+(iZhuShu*2)+"元");
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				// 应该调用胆拖的方法 陈晨 20100713
				alert_dantuo(sTouzhuAlert);
			}
		}
	}

	// 点击menu的重新机选 调用重新机选的方法
	private void beginReselect() {

		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI) {
			redBallTable.clearAllHighlights();
		} else if (iCurrentButton == PublicConst.BUY_QLC_FUSHI) {
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

								showDialog(DIALOG1_KEY);
								// TODO Auto-generated method stub
//								加入是否改变切入点判断 陈晨 8.11
								iHttp.whetherChange=false;
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									String str;

									@Override
									public void run() {
										// TODO Auto-generated method stub
										int iRedHighlights = redBallTable
												.getHighlightBallNums();
										// int iBlueHighlights =
										// blueBallTable.getHighlightBallNums();
										// int
										// iTuoRedHighlights=redTuoBallTable.getHighlightBallNums();
										// 判断是七乐彩单式还是复式
										if (iRedHighlights == 7) {
											String zhuma = zhuma_danshi();
											// PublicMethod.myOutput("----------------zhuma"+zhuma);
											// str=pay(zhuma, iZhuShu+"");
											str = pay(zhuma, iQiShu + "",
													iZhuShu * iQiShu * 200 + "");
											// FileIO file=new FileIO();
											// file.infoToWrite="----------------qilecaidanshitouzhu"+str;
											// file.write();
										} else if (iRedHighlights > 7) {
											String zhuma_fushi = zhuma_fushi();
											// str=pay(zhuma_fushi,iZhuShu+"");
											str = pay(zhuma_fushi, iQiShu + "",
													iZhuShu * iQiShu * 200 + "");
										}
										// else if(iTuoRedHighlights!=0)
										// else{
										// String zhuma_dantuo=zhuma_dantuo();
										// str=pay(zhuma_dantuo,iZhuShu+"");
										// }
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

		Builder dialog = new AlertDialog.Builder(this).setMessage(string)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						showDialog(DIALOG1_KEY);
						// TODO Auto-generated method stub
//						加入是否改变切入点判断 陈晨 8.11
						iHttp.whetherChange=false;
						Thread t = new Thread(new Runnable() {
							int iZhuShu = getZhuShu();
							int iQiShu = getQiShu();
							String str;

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String zhuma = zhuma_dantuo();
								// PublicMethod.myOutput("----------------zhuma"+zhuma);
								// str=pay(zhuma, iZhuShu+"");
								// 新接口 2010/7/11 陈晨
								str = pay(zhuma, iQiShu + "", iZhuShu * iQiShu
										* 200 + "");
								// FileIO file=new FileIO();
								// file.infoToWrite="----------------qilecaidanshitouzhu"+str;
								// file.write();
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
		BettingInterface betting = new BettingInterface();

		// String error_code=betting.Betting("15931199023", bets, bets_zhu_num,
		// "9E6764DA32B1BAD4ABBC41616992F82F");
		// String error_code=betting.Betting("13121795856", bets, bets_zhu_num,
		// "C8FAC57FDB8C68BE453B7D838F2E1D23");
		// String error_code=betting.Betting("13121795856", bets, bets_zhu_num,
		// "57637B1A23BFCB80A1096A5320F44425");
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");
		PublicMethod.myOutput("-------------touzhusessionid" + sessionid);
		PublicMethod.myOutput("-------------phonenum" + phonenum);

		// String error_code=betting.Betting(phonenum, bets,
		// bets_zhu_num,sessionid);
		String error_code = betting.BettingNew(bets, count, amount, sessionid);
		// FileIO file=new FileIO();
		// file.infoToWrite="----------------qilecaitouzhu"+error_code;
		// file.write();
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
		zhuma = redBallTable.getHighlightBallNOs();
		String t_str = "1512-F47102-";
		t_str += "00-";
		if (beishu < 10) {
			t_str += beishu;
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
		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
		}
		t_str += "^";
		PublicMethod.myOutput("-------------qilecai" + t_str);
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
		} else if (iCurrentButton == PublicConst.BUY_QLC_FUSHI)
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
				red_zhuma_string = red_zhuma_string + ".";
		}

		if (iCurrentButton == PublicConst.BUY_QLC_DANSHI
				|| iCurrentButton == PublicConst.BUY_QLC_FUSHI) {

			return "注码：" + red_zhuma_string + "\n" + "注数：" + iZhuShu
					/ mSeekBarBeishu.getProgress() + "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "注" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" 
					+"冻结金额："
					+(2* (mSeekBarQishu.getProgress()-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？";

		} else {
			String red_tuo_zhuma_string = " ";
			int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
			for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
				red_tuo_zhuma_string = red_tuo_zhuma_string
						+ String.valueOf(redTuoZhuMa[i]);
				if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
					red_tuo_zhuma_string = red_tuo_zhuma_string + ".";
			}
			return "注码：\n" + "   红球胆码：" + red_zhuma_string + "\n" + "   红球拖码："
					+ red_tuo_zhuma_string + "\n" + "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress() + "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "注" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" 
					+"冻结金额："
					+(2* (mSeekBarQishu.getProgress()-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？";
		}
	}

	/**
	 * 单笔投注大于10万元时的对话框
	 */
	private void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(QLC.this);
		builder.setTitle("投注失败");
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
}