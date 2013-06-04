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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.netintface.BettingInterface;
import com.palmdream.netintface.iHttp;

/**
 * 
 * @author 王艳玲 福彩3D玩法操作
 * 
 */
public class FC3DTest extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
		MyDialogListener {

	// 小球图的宽高
	public static final int BALL_WIDTH = 29;
	// 顶部标签个数
	private int iButtonNum = 5;

	// 首行button
	private int iCurrentButton;

	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;

	private int defaultOffShade;
	private int defaultOnShade;

	int topButtonStringId[] = { R.string.zhixuan, R.string.zuxuan3,
			R.string.zuxuan6, R.string.dantuo, R.string.hezhi };

	// 福彩3D组3
	public static final int RED_FC3D_ZU3_DANSHI_BAIWEI_START = 0x70000000;
	public static final int RED_FC3D_ZU3_DANSHI_SHIWEI_START = 0x70000010;
	public static final int RED_FC3D_ZU3_DANSHI_GEWEI_START = 0x70000020;
	public static final int RED_FC3D_ZU3_FUSHI_START = 0x70000030;
	// 福彩3D组6
	public static final int RED_FC3D_ZU6_START = 0x70000040;
	// 福彩3D胆拖
	public static final int RED_FC3D_DANTUO_DANMA_START = 0x70000050;
	public static final int RED_FC3D_DANTUO_TUOMA_START = 0x70000060;
	// 福彩3D直选
	public static final int RED_FC3D_ZHIXUAN_BAIWEI_START = 0x70000070;
	public static final int RED_FC3D_ZHIXUAN_SHIWEI_START = 0x70000080;
	public static final int RED_FC3D_ZHIXUAN_GEWEI_START = 0x70000090;
	// 福彩3D和值
	public static final int RED_FC3D_HEZHI_ZHIXUAN_START = 0x70000100;
	public static final int RED_FC3D_HEZHI_ZU3_START = 0x70000200;
	public static final int RED_FC3D_HEZHI_ZU6_START = 0x70000303;

	// 网络提示框
	private static final int DIALOG1_KEY = 0;
	ProgressDialog progressdialog;

	ScrollView mHScrollView;

	SeekBar mSeekBarBeishu;// 倍数
	SeekBar mSeekBarQishu;// 期数

	TextView mTextBeishu;// 倍数
	TextView mTextQishu;// 期数

	int iWhich = 0;

	// SeekBar默认为1
	int iProgressBeishu = 1;
	int iProgressQishu = 1;
	// 机选全局复选框
	CheckBox mCheckBox;

	TextView mTextSumMoney;// 总金额
	private int iScreenWidth;

	// 福彩3D组3单式前2位相同
	BallTable Fc3dA1Zu3DanshiBallTable = null;
	BallTable Fc3dA2Zu3DanshiBallTable = null;
	// 福彩3D组3单式第三位
	BallTable Fc3dBZu3DanshiBallTable = null;
	// 福彩3D组3复式
	BallTable Fc3dZu3FushiBallTable = null;
	// 福彩3D组6单复式
	BallTable Fc3dZu6BallTable = null;
	// 福彩3D胆拖
	BallTable Fc3dDantuoDanmaBallTable = null;
	BallTable Fc3dDantuoTuomaBallTable = null;
	// 福彩3D直选
	BallTable Fc3dZhixuanBaiweiBallTable = null;// 直选百位
	BallTable Fc3dZhixuanShiweiBallTable = null;// 直选十位
	BallTable Fc3dZhixuanGeweiBallTable = null;// 直选个位
	// 福彩3D和值
	BallTable Fc3dHezhiZhixuanBallTable = null;// 和值直选
	BallTable Fc3dHezhiZu3BallTable = null;// 和值组3
	BallTable Fc3dHezhiZu6BallTable = null;// 和值组6

	// 福彩3D和值三个二级tab按钮
	TextView Fc3d_hezhi_zhixuan;
	TextView Fc3d_hezhi_zu3;
	TextView Fc3d_hezhi_zu6;

	// 组3单复式
	boolean bZu3Danshi = true;
	boolean bZu3Fushi = false;

	private int Fc3dBallResId[] = { R.drawable.grey, R.drawable.red };

	// 福彩3D组3玩法按钮
	RadioGroup radiogroup;
	RadioButton danshirbtn;
	RadioButton fushirbtn;
	int redBallViewNum;
	int redBallViewWidth;
	LinearLayout linearLayout;

	Button zhixuanNewSelectbtn;
	Button zu3NewSelectbtn;
	Button zu6NewSelectbtn;
	Button dantuoNewSelectbtn;
	Button hezhiZhixuanNewSelectbtn;
	Button hezhiZu3NewSelectbtn;
	Button hezhiZu6NewSelectbtn;

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	LinearLayout buyView;
	// 实现横纵的切换
	private BallHolderFc3d mBallHolder = null;
	private BallHolderFc3d tempBallHolder = null;
	private int tempCurrentButton;
	public int publicTopButton;
	public int tempCurrentWhich;

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
//				清除高亮小球 陈晨 20100728
				if(iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN){
					Fc3dZhixuanBaiweiBallTable.clearAllHighlights();
					Fc3dZhixuanShiweiBallTable.clearAllHighlights();
					Fc3dZhixuanGeweiBallTable.clearAllHighlights();
				}else if(iCurrentButton == PublicConst.BUY_FC3D_ZU3){
					if (bZu3Danshi){
						Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
						Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
						Fc3dBZu3DanshiBallTable.clearAllHighlights();
					   }
						if (bZu3Fushi){
						Fc3dZu3FushiBallTable.clearAllHighlights();
					   }
				}else if(iCurrentButton == PublicConst.BUY_FC3D_ZU6){
					Fc3dZu6BallTable.clearAllHighlights();
				}else if(iCurrentButton == PublicConst.BUY_FC3D_DANTUO){
					Fc3dDantuoDanmaBallTable.clearAllHighlights();
					Fc3dDantuoTuomaBallTable.clearAllHighlights();
				}else if(iCurrentButton == PublicConst.BUY_FC3D_HEZHI){
					if(iWhich == 10){
					  Fc3dHezhiZhixuanBallTable.clearAllHighlights();
					}
					if(iWhich == 11){
					  Fc3dHezhiZu3BallTable.clearAllHighlights();
					}
					if(iWhich == 12){
					  Fc3dHezhiZu6BallTable.clearAllHighlights();
					}
				}
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(FC3DTest.this,
						UserLogin.class);
				startActivity(intentSession);
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 8:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败", Toast.LENGTH_LONG)
						.show();
				// // tv.setText(result);
			}
			//				
		}
	};

	/**
	 * Called when the activity is first created.
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);

		// 7.3代码修改：“返回”Button换成ImageButton
		ImageButton returnBtn = (ImageButton) findViewById(R.id.goucaitouzhu_title_return);
		returnBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		TextView title = (TextView) findViewById(R.id.goucaitouzhu_title);
		title.setText(getResources().getString(R.string.fucai3d));

		mHScrollView = (ScrollView) findViewById(R.id.scroll_global);
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initButtons();

		// ----- 默认 加载单式layout
		// iCurrentButton = PublicConst.BUY_SSQ_DANSHI;
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
	public Object onRetainNonConfigurationInstance() {
		mBallHolder.topButtonGroup = iCurrentButton;
		return mBallHolder;
	}

	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		mBallHolder.flag = 1;
		switch (iCurrentButton) {
		case PublicConst.BUY_FC3D_ZHIXUAN:
			mBallHolder.ZhixuanBallGroup.iBeiShu = getBeishu();
			mBallHolder.ZhixuanBallGroup.iQiShu = getQishu();
			mBallHolder.ZhixuanBallGroup.bCheckBox = getCheckBox();
			break;
		case PublicConst.BUY_FC3D_ZU3:
			mBallHolder.Zu3BallGroup.iBeiShu = getBeishu();
			mBallHolder.Zu3BallGroup.iQiShu = getQishu();
			mBallHolder.Zu3BallGroup.bCheckBox = getCheckBox();
			mBallHolder.Zu3BallGroup.bRadioBtnDanshi = getZu3DanshiRadioButton();
			mBallHolder.Zu3BallGroup.bRadioBtnFushi = getZu3FushiRadioButton();
			break;
		case PublicConst.BUY_FC3D_ZU6:
			mBallHolder.Zu6BallGroup.iBeiShu = getBeishu();
			mBallHolder.Zu6BallGroup.iQiShu = getQishu();
			mBallHolder.Zu6BallGroup.bCheckBox = getCheckBox();
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			mBallHolder.DantuoBallGroup.iBeiShu = getBeishu();
			mBallHolder.DantuoBallGroup.iQiShu = getQishu();
			mBallHolder.DantuoBallGroup.bCheckBox = getCheckBox();
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			if (iWhich == 10) {
				mBallHolder.HezhiZhixuanBallGroup.iBeiShu = getBeishu();
				mBallHolder.HezhiZhixuanBallGroup.iQiShu = getQishu();
				mBallHolder.HezhiZhixuanBallGroup.bCheckBox = getCheckBox();
			}
			if (iWhich == 11) {
				mBallHolder.HezhiZu3BallGroup.iBeiShu = getBeishu();
				mBallHolder.HezhiZu3BallGroup.iQiShu = getQishu();
				mBallHolder.HezhiZu3BallGroup.bCheckBox = getCheckBox();
			}
			if (iWhich == 12) {
				mBallHolder.HezhiZu6BallGroup.iBeiShu = getBeishu();
				mBallHolder.HezhiZu6BallGroup.iQiShu = getQishu();
				mBallHolder.HezhiZu6BallGroup.bCheckBox = getCheckBox();
			}
			break;
		}

		tempBallHolder = mBallHolder;
		tempCurrentButton = iCurrentButton;
		tempCurrentWhich = iWhich;
		if (mBallHolder == null) {
			mBallHolder = new BallHolderFc3d();
		}
		initButtons();
		// setCurrentTab(0);
		iCurrentButton = tempCurrentButton;

		createBuyView(iCurrentButton);
		// 切换之后，显示高亮的radioButton
		showHighLight();

		mBallHolder = tempBallHolder;
		iWhich = tempCurrentWhich;
		if (publicTopButton == PublicConst.BUY_FC3D_ZHIXUAN) {
			create_FC3D_ZHIXUAN();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_FC3D_ZU3) {
			create_FC3D_ZU3();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_FC3D_ZU6) {
			create_FC3D_ZU6();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_FC3D_DANTUO) {
			create_FC3D_DANTUO();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_FC3D_HEZHI) {
			create_FC3D_HEZHI();
		}
		mBallHolder.flag = 0;

	}

	/**
	 * 创建购彩View
	 * 
	 * @param int aWhichBuy 创建何种玩法的标签Id
	 */
	private void createBuyView(int aWhichBuy) {
		iProgressBeishu = 1;
		iProgressQishu = 1;
		publicTopButton = aWhichBuy;
		switch (aWhichBuy) {
		case PublicConst.BUY_FC3D_ZHIXUAN:
			create_FC3D_ZHIXUAN();
			break;
		case PublicConst.BUY_FC3D_ZU3:
			create_FC3D_ZU3();
			break;
		case PublicConst.BUY_FC3D_ZU6:
			create_FC3D_ZU6();
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			create_FC3D_DANTUO();
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			create_FC3D_HEZHI();
			break;
		default:
			break;
		}
	}

	/**
	 * 福彩3D和值
	 */
	private void create_FC3D_HEZHI() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout iV = (RelativeLayout) inflate.inflate(
				R.layout.layout_fc3d_hezhi_tab, null);
		{
			Fc3d_hezhi_zhixuan = (TextView) iV.findViewById(R.id.hezhi_zhixuan);
			Fc3d_hezhi_zu3 = (TextView) iV.findViewById(R.id.hezhi_zu3);
			Fc3d_hezhi_zu6 = (TextView) iV.findViewById(R.id.hezhi_zu6);
			Fc3d_hezhi_zhixuan
					.setOnClickListener(new TextView.OnClickListener() {

						public void onClick(View v) {
							if (mBallHolder.flag != 1) {
								mBallHolder = new BallHolderFc3d();
								create_FC3D_HEZHI_ZHIXUAN();
								/*
								 * Fc3d_hezhi_zhixuan.setTextColor(Color.GREEN);
								 * Fc3d_hezhi_zu3.setTextColor(Color.RED);
								 * Fc3d_hezhi_zu6.setTextColor(Color.RED);
								 */
								Fc3d_hezhi_zhixuan
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user));
								Fc3d_hezhi_zu3
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user_d));
								Fc3d_hezhi_zu6
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user_d));
							}

						}

					});

			Fc3d_hezhi_zu3.setOnClickListener(new TextView.OnClickListener() {

				public void onClick(View v) {
					if (mBallHolder.flag != 1) {
						mBallHolder = new BallHolderFc3d();
						create_FC3D_HEZHI_ZU3();
						/*
						 * Fc3d_hezhi_zhixuan.setTextColor(Color.RED);
						 * Fc3d_hezhi_zu3.setTextColor(Color.GREEN);
						 * Fc3d_hezhi_zu6.setTextColor(Color.RED);
						 */
						Fc3d_hezhi_zhixuan
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						Fc3d_hezhi_zu3.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.frame_rectangle_user));
						Fc3d_hezhi_zu6
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
					}
				}

			});

			Fc3d_hezhi_zu6.setOnClickListener(new TextView.OnClickListener() {

				public void onClick(View v) {
					if (mBallHolder.flag != 1) {
						mBallHolder = new BallHolderFc3d();
						create_FC3D_HEZHI_ZU6();
						/*
						 * Fc3d_hezhi_zhixuan.setTextColor(Color.RED);
						 * Fc3d_hezhi_zu3.setTextColor(Color.RED);
						 * Fc3d_hezhi_zu6.setTextColor(Color.GREEN);
						 */
						Fc3d_hezhi_zhixuan
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						Fc3d_hezhi_zu3
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						Fc3d_hezhi_zu6.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.frame_rectangle_user));
					}
				}

			});

		}
		// 将View加入主框架
		buyView.addView(iV, new RelativeLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		if (mBallHolder != null) {
			if (mBallHolder.flag == 1) {
				if (iWhich == 10) {
					Fc3d_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
					Fc3d_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					Fc3d_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					create_FC3D_HEZHI_ZHIXUAN();
				}
				if (iWhich == 11) {
					create_FC3D_HEZHI_ZU3();
					Fc3d_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					Fc3d_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
					Fc3d_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
				}
				if (iWhich == 12) {
					create_FC3D_HEZHI_ZU6();
					Fc3d_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					Fc3d_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					Fc3d_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
				}
			} else {
				Fc3d_hezhi_zhixuan.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user));
				Fc3d_hezhi_zu3.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				Fc3d_hezhi_zu6.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				create_FC3D_HEZHI_ZHIXUAN();
			}
		}
	}

	/**
	 * 福彩3D和值组6
	 */
	private void create_FC3D_HEZHI_ZU6() {
		iWhich = 12;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.fc3d_hezhi_zu3_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zu3_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zhixuan_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zhixuan_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zu6_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_fc3d_hezhi_zu6, null);
			{

				int redBallViewNum = 22;
				int redBallViewWidth = FC3DTest.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				Fc3dHezhiZu6BallTable = makeBallTable(iV, R.id.table_hezhi_zu6,
						iScreenWidth, redBallViewNum, redBallViewWidth,
						Fc3dBallResId, RED_FC3D_HEZHI_ZU6_START, 3);

				hezhiZu6NewSelectbtn = (Button) iV
						.findViewById(R.id.fc3d_hezhi_zu6_newselected_btn);
				hezhiZu6NewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									// 王艳玲 7.10 修改随机数范围
									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													21);
									Fc3dHezhiZu6BallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 12,
													iHighlightBallId);
									changeTextSumMoney(12);

								}
							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zu6_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zu6_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zu6_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zu6_qihao);
				addQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(++iProgressQishu);
							}

						});

				mTextSumMoney = (TextView) iV
						.findViewById(R.id.text_sum_money_hezhi_zu6);
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_number));

				mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
				mSeekBarBeishu.setOnSeekBarChangeListener(this);
				mSeekBarBeishu.setProgress(iProgressBeishu);

				mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
				mSeekBarQishu.setOnSeekBarChangeListener(this);
				mSeekBarQishu.setProgress(iProgressQishu);

				mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
				mTextBeishu.setText("" + iProgressBeishu);
				mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
				mTextQishu.setText("" + iProgressQishu);

				ImageButton b_touzhu_ddd_zu6hezhi = (ImageButton) iV
						.findViewById(R.id.b_touzhu_hezhi_zu6);
				b_touzhu_ddd_zu6hezhi.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
			}
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

			mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_hezhi_zu6);
			mCheckBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							if (isChecked) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									// 王艳玲 7.10 修改随机数范围
									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													21);
									Fc3dHezhiZu6BallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 12,
													iHighlightBallId);
									changeTextSumMoney(12);

								}
								hezhiZu6NewSelectbtn
										.setVisibility(View.VISIBLE);
							} else {
								hezhiZu6NewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				Fc3dHezhiZu6BallTable
						.changeBallStateConfigChange(mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus);
				mSeekBarBeishu
						.setProgress(mBallHolder.HezhiZu6BallGroup.iBeiShu);
				mSeekBarQishu.setProgress(mBallHolder.HezhiZu6BallGroup.iQiShu);
				mCheckBox.setChecked(mBallHolder.HezhiZu6BallGroup.bCheckBox);
				if (mBallHolder.HezhiZu6BallGroup.bCheckBox) {
					hezhiZu6NewSelectbtn.setVisibility(View.VISIBLE);
				} else {
					hezhiZu6NewSelectbtn.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	/**
	 * 福彩3D和值组3
	 */
	private void create_FC3D_HEZHI_ZU3() {
		iWhich = 11;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.fc3d_hezhi_zhixuan_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zhixuan_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zu6_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zu6_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zu3_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_fc3d_hezhi_zu3, null);
			{

				int redBallViewNum = 26;
				int redBallViewWidth = FC3DTest.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				Fc3dHezhiZu3BallTable = makeBallTable(iV, R.id.table_hezhi_zu3,
						iScreenWidth, redBallViewNum, redBallViewWidth,
						Fc3dBallResId, RED_FC3D_HEZHI_ZU3_START, 1);

				hezhiZu3NewSelectbtn = (Button) iV
						.findViewById(R.id.fc3d_hezhi_zu3_newselected_btn);
				hezhiZu3NewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									// 王艳玲 7.10 修改随机数范围
									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													25);
									Fc3dHezhiZu3BallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 11,
													iHighlightBallId);
									changeTextSumMoney(11);

								}
							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zu3_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zu3_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zu3_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zu3_qihao);
				addQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(++iProgressQishu);
							}

						});

				mTextSumMoney = (TextView) iV
						.findViewById(R.id.text_sum_money_hezhi_zu3);
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_number));

				mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
				mSeekBarBeishu.setOnSeekBarChangeListener(this);
				mSeekBarBeishu.setProgress(iProgressBeishu);

				mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
				mSeekBarQishu.setOnSeekBarChangeListener(this);
				mSeekBarQishu.setProgress(iProgressQishu);

				mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
				mTextBeishu.setText("" + iProgressBeishu);
				mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
				mTextQishu.setText("" + iProgressQishu);

				ImageButton b_touzhu_ddd_hezhizu3 = (ImageButton) iV
						.findViewById(R.id.b_touzhu_hezhi_zu3);
				b_touzhu_ddd_hezhizu3.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
			}
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

			mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_hezhi_zu3);
			mCheckBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							if (isChecked) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									// 王艳玲 7.10 修改随机数范围
									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													25);
									Fc3dHezhiZu3BallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 11,
													iHighlightBallId);
									changeTextSumMoney(11);

								}
								hezhiZu3NewSelectbtn
										.setVisibility(View.VISIBLE);
							} else {
								hezhiZu3NewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				Fc3dHezhiZu3BallTable
						.changeBallStateConfigChange(mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus);
				mSeekBarBeishu
						.setProgress(mBallHolder.HezhiZu3BallGroup.iBeiShu);
				mSeekBarQishu.setProgress(mBallHolder.HezhiZu3BallGroup.iQiShu);
				mCheckBox.setChecked(mBallHolder.HezhiZu3BallGroup.bCheckBox);
				if (mBallHolder.HezhiZu3BallGroup.bCheckBox) {
					hezhiZu3NewSelectbtn.setVisibility(View.VISIBLE);
				} else {
					hezhiZu3NewSelectbtn.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	/**
	 * 福彩3D和值直选
	 */
	private void create_FC3D_HEZHI_ZHIXUAN() {
		iWhich = 10;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.fc3d_hezhi_zu3_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zu3_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zu6_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.fc3d_hezhi_zu6_linearlayout));
		}
		if (buyView.findViewById(R.id.fc3d_hezhi_zhixuan_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_fc3d_hezhi_zhixuan, null);
			{

				int redBallViewNum = 28;
				int redBallViewWidth = FC3DTest.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				Fc3dHezhiZhixuanBallTable = makeBallTable(iV,
						R.id.table_hezhi_zhixuan, iScreenWidth, redBallViewNum,
						redBallViewWidth, Fc3dBallResId,
						RED_FC3D_HEZHI_ZHIXUAN_START, 0);

				hezhiZhixuanNewSelectbtn = (Button) iV
						.findViewById(R.id.fc3d_hezhi_zhixuan_newselected_btn);
				hezhiZhixuanNewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													27);
									Fc3dHezhiZhixuanBallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 10,
													iHighlightBallId);
									changeTextSumMoney(10);
								}

							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zhixuan_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zhixuan_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zhixuan_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.fc3d_seekbar_add_hezhi_zhixuan_qihao);
				addQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(++iProgressQishu);
							}

						});

				mTextSumMoney = (TextView) iV
						.findViewById(R.id.text_sum_money_hezhi_zhixuan);
				mTextSumMoney.setText(getResources().getString(
						R.string.please_choose_number));

				mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
				mSeekBarBeishu.setOnSeekBarChangeListener(this);
				mSeekBarBeishu.setProgress(iProgressBeishu);

				mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
				mSeekBarQishu.setOnSeekBarChangeListener(this);
				mSeekBarQishu.setProgress(iProgressQishu);

				mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
				mTextBeishu.setText("" + iProgressBeishu);
				mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
				mTextQishu.setText("" + iProgressQishu);
				ImageButton b_touzhu_ddd_zhixuanhezhi = (ImageButton) iV
						.findViewById(R.id.b_touzhu_hezhi_zhixuan);
				b_touzhu_ddd_zhixuanhezhi
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								beginTouZhu();
							}

						});

			}
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

			mCheckBox = (CheckBox) this
					.findViewById(R.id.cb_jixuan_hezhi_zhixuan);
			mCheckBox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {

							if (isChecked) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();

									int[] iHighlightBallId = PublicMethod
											.getRandomsWithoutCollision(1, 0,
													27);
									Fc3dHezhiZhixuanBallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 10,
													iHighlightBallId);
									changeTextSumMoney(10);
								}
								hezhiZhixuanNewSelectbtn
										.setVisibility(View.VISIBLE);
							} else {
								hezhiZhixuanNewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				Fc3dHezhiZhixuanBallTable
						.changeBallStateConfigChange(mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus);
				mSeekBarBeishu
						.setProgress(mBallHolder.HezhiZhixuanBallGroup.iBeiShu);
				mSeekBarQishu
						.setProgress(mBallHolder.HezhiZhixuanBallGroup.iQiShu);
				mCheckBox
						.setChecked(mBallHolder.HezhiZhixuanBallGroup.bCheckBox);
				if (mBallHolder.HezhiZhixuanBallGroup.bCheckBox) {
					hezhiZhixuanNewSelectbtn.setVisibility(View.VISIBLE);
				} else {
					hezhiZhixuanNewSelectbtn.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	/**
	 * 福彩3D直选
	 */
	private void create_FC3D_ZHIXUAN() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_zhixuan, null);
		{
			int redBallViewNum = 10;
			int redBallViewWidth = FC3DTest.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			Fc3dZhixuanBaiweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_baiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_BAIWEI_START, 0);
			Fc3dZhixuanShiweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_shiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_SHIWEI_START, 0);
			Fc3dZhixuanGeweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_gewei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_GEWEI_START, 0);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_zhixuan);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			ImageButton subtractBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_zhixuan_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_zhixuan_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_zhixuan_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_zhixuan_qihao);
			addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarQishu.setProgress(++iProgressQishu);
				}

			});

			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
			mTextQishu.setText("" + iProgressQishu);

			zhixuanNewSelectbtn = (Button) iV
					.findViewById(R.id.fc3d_zhixuan_newselected_btn);
			zhixuanNewSelectbtn
					.setOnClickListener(new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
									FC3DTest.this, 1, FC3DTest.this);
							iChooseNumberDialog.show();
						}

					});

			ImageButton b_touzhu_ddd_zhixuan = (ImageButton) iV
					.findViewById(R.id.b_touzhu_zhixuan);
			b_touzhu_ddd_zhixuan
					.setOnClickListener(new ImageButton.OnClickListener() {
						@Override
						public void onClick(View v) {
							// 王艳玲 7.8 加menu
							beginTouZhu();
						}

					});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		// 机选
		mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_zhixuan);
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						if (isChecked) {
							if (mBallHolder.flag != 1) {
								ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
										FC3DTest.this, 1, FC3DTest.this);
								iChooseNumberDialog.show();
								zhixuanNewSelectbtn.setVisibility(View.VISIBLE);
							}
						} else {
							zhixuanNewSelectbtn.setVisibility(View.INVISIBLE);
						}
					}
				});

		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			Fc3dZhixuanBaiweiBallTable
					.changeBallStateConfigChange(mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus);
			Fc3dZhixuanShiweiBallTable
					.changeBallStateConfigChange(mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus);
			Fc3dZhixuanGeweiBallTable
					.changeBallStateConfigChange(mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.ZhixuanBallGroup.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.ZhixuanBallGroup.iQiShu);
			mCheckBox.setChecked(mBallHolder.ZhixuanBallGroup.bCheckBox);
			if (mBallHolder.ZhixuanBallGroup.bCheckBox) {
				zhixuanNewSelectbtn.setVisibility(View.VISIBLE);
			} else {
				zhixuanNewSelectbtn.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 福彩3D胆拖
	 */
	private void create_FC3D_DANTUO() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_dantuo, null);
		{
			int redBallViewNum = 10;
			int redBallViewWidth = FC3DTest.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			Fc3dDantuoDanmaBallTable = makeBallTable(iV,
					R.id.table_dantuo_danma, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_DANTUO_DANMA_START, 0);
			Fc3dDantuoTuomaBallTable = makeBallTable(iV,
					R.id.table_dantuo_tuoma, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_DANTUO_TUOMA_START, 0);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_dantuo);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			dantuoNewSelectbtn = (Button) iV
					.findViewById(R.id.fc3d_dantuo_newselected_btn);
			dantuoNewSelectbtn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
							FC3DTest.this, 4, FC3DTest.this);
					iChooseNumberDialog.show();
				}

			});

			ImageButton subtractBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_dantuo_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_dantuo_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_dantuo_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_dantuo_qihao);
			addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarQishu.setProgress(++iProgressQishu);
				}

			});

			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			ImageButton b_ddd_dantuo = (ImageButton) iV
					.findViewById(R.id.b_touzhu_dantuo);
			b_ddd_dantuo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_dantuo);
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (mBallHolder.flag != 1) {
								ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
										FC3DTest.this, 4, FC3DTest.this);
								iChooseNumberDialog.show();
								dantuoNewSelectbtn.setVisibility(View.VISIBLE);
							}
						} else {
							dantuoNewSelectbtn.setVisibility(View.INVISIBLE);
						}
					}
				});

		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			Fc3dDantuoDanmaBallTable
					.changeBallStateConfigChange(mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus);
			Fc3dDantuoTuomaBallTable
					.changeBallStateConfigChange(mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.DantuoBallGroup.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.DantuoBallGroup.iQiShu);
			mCheckBox.setChecked(mBallHolder.DantuoBallGroup.bCheckBox);
			if (mBallHolder.DantuoBallGroup.bCheckBox) {
				dantuoNewSelectbtn.setVisibility(View.VISIBLE);
			} else {
				dantuoNewSelectbtn.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 福彩3D组6
	 */
	private void create_FC3D_ZU6() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_zu6, null);
		{
			int redBallViewNum = 10;
			int redBallViewWidth = FC3DTest.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			Fc3dZu6BallTable = makeBallTable(iV, R.id.table_zu6, iScreenWidth,
					redBallViewNum, redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZU6_START, 0);

			zu6NewSelectbtn = (Button) iV
					.findViewById(R.id.fc3d_zu6_newselected_btn);
			zu6NewSelectbtn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
							FC3DTest.this, 3, FC3DTest.this);
					iChooseNumberDialog.show();
				}

			});

			ImageButton subtractBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_zu6_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_zu6_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_subtract_zu6_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) iV
					.findViewById(R.id.fc3d_seekbar_add_zu6_qihao);
			addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarQishu.setProgress(++iProgressQishu);
				}

			});

			mTextSumMoney = (TextView) iV.findViewById(R.id.text_sum_money_zu6);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) iV.findViewById(R.id.seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) iV.findViewById(R.id.seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) iV.findViewById(R.id.text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) iV.findViewById(R.id.text_qishu);
			mTextQishu.setText("" + iProgressQishu);
			ImageButton b_ddd_zu6_touzhu = (ImageButton) iV
					.findViewById(R.id.b_touzhu_zu6);
			b_ddd_zu6_touzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_zu6);
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (mBallHolder.flag != 1) {
								ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
										FC3DTest.this, 3, FC3DTest.this);
								iChooseNumberDialog.show();
								zu6NewSelectbtn.setVisibility(View.VISIBLE);
							}
						} else {
							zu6NewSelectbtn.setVisibility(View.INVISIBLE);
						}
					}
				});

		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			Fc3dZu6BallTable
					.changeBallStateConfigChange(mBallHolder.Zu6BallGroup.iZu6BallStatus);
			mSeekBarBeishu.setProgress(mBallHolder.Zu6BallGroup.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.Zu6BallGroup.iQiShu);
			mCheckBox.setChecked(mBallHolder.Zu6BallGroup.bCheckBox);
			if (mBallHolder.Zu6BallGroup.bCheckBox) {
				zu6NewSelectbtn.setVisibility(View.VISIBLE);
			} else {
				zu6NewSelectbtn.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 福彩3D组3
	 */
	private void create_FC3D_ZU3() {
		// 王艳玲 7.6 点击投注按钮，提示条件不明确
		bZu3Danshi = true;
		bZu3Fushi = false;
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inflate.inflate(R.layout.layout_fc3d_zu3,
				null);
		{
			redBallViewNum = 10;
			int redBallViewWidth = FC3DTest.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			Fc3dA1Zu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_baiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_BAIWEI_START, 0);
			Fc3dA2Zu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_shiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_SHIWEI_START, 0);
			Fc3dBZu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_gewei, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_GEWEI_START, 0);
			Fc3dZu3FushiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_fushi, iScreenWidth, redBallViewNum,
					redBallViewWidth, Fc3dBallResId, RED_FC3D_ZU3_FUSHI_START,
					0);
			// 默认是单式，所以复式不可用
			for (int i = 0; i < redBallViewNum; i++) {
				Fc3dZu3FushiBallTable.ballViewVector.elementAt(i).setGrey();
				Fc3dZu3FushiBallTable.ballViewVector.elementAt(i).setEnabled(
						false);
			}
			radiogroup = (RadioGroup) linearLayout
					.findViewById(R.id.radiogroupid);
			danshirbtn = (RadioButton) linearLayout
					.findViewById(R.id.radio_zu3_danshi);
			fushirbtn = (RadioButton) linearLayout
					.findViewById(R.id.radio_zu3_fushi);

			radiogroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							if (checkedId == danshirbtn.getId()) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									bZu3Danshi = true;
									bZu3Fushi = false;
									for (int i = 0; i < redBallViewNum; i++) {
										mTextSumMoney
												.setText(getResources()
														.getString(
																R.string.please_choose_number));
										Fc3dA1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										Fc3dA2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										Fc3dBZu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										Fc3dZu3FushiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										Fc3dZu3FushiBallTable.ballViewVector
												.elementAt(i).setGrey();
									}
								}
							}
							if (checkedId == fushirbtn.getId()) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									bZu3Danshi = false;
									bZu3Fushi = true;
									for (int i = 0; i < redBallViewNum; i++) {
										mTextSumMoney
												.setText(getResources()
														.getString(
																R.string.please_choose_number));
										Fc3dA1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										Fc3dA2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										Fc3dBZu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										Fc3dA1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										Fc3dA2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										Fc3dBZu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										Fc3dZu3FushiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
									}
								}
							}
						}

					});

			ImageButton subtractBeishuBtn = (ImageButton) linearLayout
					.findViewById(R.id.fc3d_seekbar_subtract_zu3_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) linearLayout
					.findViewById(R.id.fc3d_seekbar_add_zu3_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) linearLayout
					.findViewById(R.id.fc3d_seekbar_subtract_zu3_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) linearLayout
					.findViewById(R.id.fc3d_seekbar_add_zu3_qihao);
			addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarQishu.setProgress(++iProgressQishu);
				}

			});

			mTextSumMoney = (TextView) linearLayout
					.findViewById(R.id.text_sum_money_zu3);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) linearLayout
					.findViewById(R.id.seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) linearLayout
					.findViewById(R.id.seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) linearLayout
					.findViewById(R.id.text_beishu);
			mTextBeishu.setText("" + iProgressBeishu);
			mTextQishu = (TextView) linearLayout.findViewById(R.id.text_qishu);
			mTextQishu.setText("" + iProgressQishu);

			zu3NewSelectbtn = (Button) linearLayout
					.findViewById(R.id.fc3d_zu3_newselected_btn);
			zu3NewSelectbtn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (bZu3Danshi) {
						if (mBallHolder.flag != 1) {
							mBallHolder = new BallHolderFc3d();
							Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
							Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
							Fc3dBZu3DanshiBallTable.clearAllHighlights();

							// 前两位相同，第三位不同 获取随机数放到randomChooseConfigChangeFc3d
							int[] randomNums = PublicMethod
									.getRandomsWithoutCollision(2, 0, 9);// 获得2位从0~9的随机数
							mBallHolder = Fc3dA1Zu3DanshiBallTable
									.randomChooseConfigChangeFc3d(1,
											mBallHolder, 0, randomNums);
							mBallHolder = Fc3dA2Zu3DanshiBallTable
									.randomChooseConfigChangeFc3d(1,
											mBallHolder, 1, randomNums);
							mBallHolder = Fc3dBZu3DanshiBallTable
									.randomChooseConfigChangeFc3d(1,
											mBallHolder, 2, randomNums);
							changeTextSumMoney(0);
						}
					}

					if (bZu3Fushi) {
						if (mBallHolder.flag != 1) {
							ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
									FC3DTest.this, 2, FC3DTest.this);
							iChooseNumberDialog.show();
						}
					}

				}

			});

			ImageButton b_ddd_zu3 = (ImageButton) linearLayout
					.findViewById(R.id.b_touzhu_zu3);
			b_ddd_zu3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
		}
		// 将View加入主框架
		buyView.addView(linearLayout, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

		mCheckBox = (CheckBox) this.findViewById(R.id.cb_jixuan_zu3);
		mCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							if (bZu3Danshi) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderFc3d();
									Fc3dA1Zu3DanshiBallTable
											.clearAllHighlights();
									Fc3dA2Zu3DanshiBallTable
											.clearAllHighlights();
									Fc3dBZu3DanshiBallTable
											.clearAllHighlights();

									// 前两位相同，第三位不同
									// 获取随机数放到randomChooseConfigChangeFc3d
									int[] randomNums = PublicMethod
											.getRandomsWithoutCollision(2, 0, 9);// 获得2位从0~9的随机数
									mBallHolder = Fc3dA1Zu3DanshiBallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 0, randomNums);
									mBallHolder = Fc3dA2Zu3DanshiBallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 1, randomNums);
									mBallHolder = Fc3dBZu3DanshiBallTable
											.randomChooseConfigChangeFc3d(1,
													mBallHolder, 2, randomNums);
									changeTextSumMoney(0);
								}
							}
							if (bZu3Fushi) {
								if (mBallHolder.flag != 1) {
									ChooseNumberDialog3D iChooseNumberDialog = new ChooseNumberDialog3D(
											FC3DTest.this, 2, FC3DTest.this);
									iChooseNumberDialog.show();
								}
							}
							zu3NewSelectbtn.setVisibility(View.VISIBLE);
						} else {
							zu3NewSelectbtn.setVisibility(View.INVISIBLE);
						}
					}
				});

		// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
		if (mBallHolder.flag == 1) {
			danshirbtn.setChecked(mBallHolder.Zu3BallGroup.bRadioBtnDanshi);
			fushirbtn.setChecked(mBallHolder.Zu3BallGroup.bRadioBtnFushi);
			if (mBallHolder.Zu3BallGroup.bRadioBtnDanshi) {
				Fc3dA1Zu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3A1BallStatus);
				Fc3dA2Zu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3A2BallStatus);
				Fc3dBZu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3BBallStatus);
				Fc3dZu3FushiBallTable.clearAllHighlights();
			}
			if (mBallHolder.Zu3BallGroup.bRadioBtnFushi) {
				Fc3dZu3FushiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3FushiBallStatus);
				Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
				Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
				Fc3dBZu3DanshiBallTable.clearAllHighlights();
			}

			mSeekBarBeishu.setProgress(mBallHolder.Zu3BallGroup.iBeiShu);
			mSeekBarQishu.setProgress(mBallHolder.Zu3BallGroup.iQiShu);
			mCheckBox.setChecked(mBallHolder.Zu3BallGroup.bCheckBox);
			if (mBallHolder.Zu3BallGroup.bCheckBox) {
				zu3NewSelectbtn.setVisibility(View.VISIBLE);
			} else {
				zu3NewSelectbtn.setVisibility(View.INVISIBLE);
			}

		}
	}

	/**
	 * 初始化buttons
	 * 
	 * @param void
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

	/**
	 * RadioGroup提交操作
	 * 
	 * @param void
	 */
	public void commit() {
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
			mBallHolder = (BallHolderFc3d) getLastNonConfigurationInstance();
			int buttonGroup = mBallHolder.topButtonGroup;
			PublicMethod.myOutput("*********buttonGroup " + buttonGroup);
			setCurrentTab(buttonGroup);
		} else {
			mBallHolder = new BallHolderFc3d();
			// 默认当前标签
			setCurrentTab(0);
		}
	}

	/**
	 * 设置当前标签
	 * 
	 * @param index
	 *            当前标签的Id
	 */
	public void setCurrentTab(int index) {
		topButtonGroup.check(index);
		switch (index) {
		case 0:
			iCurrentButton = PublicConst.BUY_FC3D_ZHIXUAN;
			createBuyView(iCurrentButton);
			break;
		case 1:
			iCurrentButton = PublicConst.BUY_FC3D_ZU3;
			createBuyView(iCurrentButton);
			break;
		case 2:
			iCurrentButton = PublicConst.BUY_FC3D_ZU6;
			createBuyView(iCurrentButton);
			break;
		case 3:
			iCurrentButton = PublicConst.BUY_FC3D_DANTUO;
			createBuyView(iCurrentButton);
			break;
		case 4:
			iCurrentButton = PublicConst.BUY_FC3D_HEZHI;
			createBuyView(iCurrentButton);
			break;
		}
	}

	/**
	 * 重写RadioGroup监听方法onCheckedChanged
	 * 
	 * @param RadioGroup
	 *            RadioGroup
	 * @param int checkedId 当前被选择的RadioId
	 */
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		mBallHolder = new BallHolderFc3d();
		switch (checkedId) {
		// 直选
		case 0:
			iCurrentButton = PublicConst.BUY_FC3D_ZHIXUAN;
			createBuyView(iCurrentButton);
			break;
		// 组3
		case 1:
			iCurrentButton = PublicConst.BUY_FC3D_ZU3;
			createBuyView(iCurrentButton);
			break;
		// 组6
		case 2:
			iCurrentButton = PublicConst.BUY_FC3D_ZU6;
			createBuyView(iCurrentButton);
			break;
		// 胆拖
		case 3:
			iCurrentButton = PublicConst.BUY_FC3D_DANTUO;
			createBuyView(iCurrentButton);
			break;
		// 和值
		case 4:
			iCurrentButton = PublicConst.BUY_FC3D_HEZHI;
			createBuyView(iCurrentButton);
			break;
		}
		mHScrollView.fullScroll(ScrollView.FOCUS_UP);
	}

	public int getCurrentTab() {
		return topButtonGroup.getCheckedRadioButtonId();
	}

	/**
	 * 重写onProgressChanged 通过改变SeekBar的progress（倍数）实时改变注数
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			if (iWhich == 0) {
				changeTextSumMoney(0);
			} else if (iWhich == 10) {
				changeTextSumMoney(10);
			} else if (iWhich == 11) {
				changeTextSumMoney(11);
			} else if (iWhich == 12) {
				changeTextSumMoney(12);
			}

			break;
		case R.id.seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}
	}

	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * 响应小球被点击的回调函数
	 * 
	 * @param View
	 *            v 被点击的view
	 * @return void
	 */
	public void onClick(View v) {
		if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {
			// 福彩3D和值直选
			if (v.getId() < RED_FC3D_HEZHI_ZU3_START
					&& v.getId() >= RED_FC3D_HEZHI_ZHIXUAN_START) {
				int iBallViewId = v.getId() - RED_FC3D_HEZHI_ZHIXUAN_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 10, iBallViewId);
				}
				if (Fc3dHezhiZhixuanBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(10);
				}

			}
			// 福彩3D和值组3
			if (v.getId() < RED_FC3D_HEZHI_ZU6_START
					&& v.getId() >= RED_FC3D_HEZHI_ZU3_START) {
				int iBallViewId = v.getId() - RED_FC3D_HEZHI_ZU3_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 11, iBallViewId);
				}
				if (Fc3dHezhiZu3BallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(11);
				}
			}
			// 福彩3D和值组6
			if (v.getId() >= RED_FC3D_HEZHI_ZU6_START) {
				int iBallViewId = v.getId() - RED_FC3D_HEZHI_ZU6_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 12, iBallViewId);
				}
				if (Fc3dHezhiZu6BallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(12);
				}
			}

		} else {
			// 福彩3D组3单式第一组
			if (v.getId() < RED_FC3D_ZU3_DANSHI_SHIWEI_START
					&& v.getId() >= RED_FC3D_ZU3_DANSHI_BAIWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZU3_DANSHI_BAIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 0, iBallViewId);
				}
				if (Fc3dA1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dA2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dBZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D组3单式第二组
			if (v.getId() < RED_FC3D_ZU3_DANSHI_GEWEI_START
					&& v.getId() >= RED_FC3D_ZU3_DANSHI_SHIWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZU3_DANSHI_SHIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 1, iBallViewId);
				}
				if (Fc3dA1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dA2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dBZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D组3单式第三组
			if (v.getId() < RED_FC3D_ZU3_FUSHI_START
					&& v.getId() >= RED_FC3D_ZU3_DANSHI_GEWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZU3_DANSHI_GEWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 2, iBallViewId);
				}
				if (Fc3dA1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dA2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| Fc3dBZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D组3复式
			if (v.getId() < RED_FC3D_ZU6_START
					&& v.getId() >= RED_FC3D_ZU3_FUSHI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZU3_FUSHI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 3, iBallViewId);
				}
				if (Fc3dZu3FushiBallTable.getHighlightBallNums() >= 0
						&& Fc3dZu3FushiBallTable.getHighlightBallNums() < 2) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D组6
			if (v.getId() < RED_FC3D_DANTUO_DANMA_START
					&& v.getId() >= RED_FC3D_ZU6_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZU6_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 4, iBallViewId);
				}
				if (Fc3dZu6BallTable.getHighlightBallNums() >= 0
						&& Fc3dZu6BallTable.getHighlightBallNums() < 3) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D胆码
			if (v.getId() >= RED_FC3D_DANTUO_DANMA_START
					&& v.getId() < RED_FC3D_DANTUO_TUOMA_START) {
				int iBallViewId = v.getId() - RED_FC3D_DANTUO_DANMA_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 5, iBallViewId);
				}
				if (Fc3dDantuoDanmaBallTable.getHighlightBallNums() == 0
						|| Fc3dDantuoTuomaBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D拖码
			if (v.getId() < RED_FC3D_ZHIXUAN_BAIWEI_START
					&& v.getId() >= RED_FC3D_DANTUO_TUOMA_START) {
				int iBallViewId = v.getId() - RED_FC3D_DANTUO_TUOMA_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 6, iBallViewId);
				}
				if (Fc3dDantuoDanmaBallTable.getHighlightBallNums() == 0
						|| Fc3dDantuoTuomaBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D直选百位
			if (v.getId() < RED_FC3D_ZHIXUAN_SHIWEI_START
					&& v.getId() >= RED_FC3D_ZHIXUAN_BAIWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZHIXUAN_BAIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 7, iBallViewId);
				}
				if (Fc3dZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}

			}
			// 福彩3D十位
			if (v.getId() < RED_FC3D_ZHIXUAN_GEWEI_START
					&& v.getId() >= RED_FC3D_ZHIXUAN_SHIWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZHIXUAN_SHIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 8, iBallViewId);
				}
				if (Fc3dZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 福彩3D个位
			if (v.getId() < RED_FC3D_HEZHI_ZHIXUAN_START
					&& v.getId() >= RED_FC3D_ZHIXUAN_GEWEI_START) {
				int iBallViewId = v.getId() - RED_FC3D_ZHIXUAN_GEWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 9, iBallViewId);
				}
				if (Fc3dZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| Fc3dZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
		}
	}

	/**
	 * 获得各种玩法的注数
	 * 
	 * @return 返回注数
	 */
	private int getZhuShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		// 福彩3D直选复式
		case PublicConst.BUY_FC3D_ZHIXUAN:
			iReturnValue = Fc3dZhixuanBaiweiBallTable.getHighlightBallNums()
					* Fc3dZhixuanShiweiBallTable.getHighlightBallNums()
					* Fc3dZhixuanGeweiBallTable.getHighlightBallNums();
			break;

		// 福彩3D组3复式
		case PublicConst.BUY_FC3D_ZU3:
			// 添加组3单式注数获取20100912 陈晨
			if (bZu3Danshi) {
				// 单式注数永为1
				iReturnValue = 1;
			}
			// 组3 复式注数的获取 20100912 陈晨
			if (bZu3Fushi) {
				int iZu3Balls = Fc3dZu3FushiBallTable.getHighlightBallNums();
				iReturnValue = (int) getFc3dZu3FushiZhushu(iZu3Balls);
			}
			break;
		// 福彩3D组6复式
		case PublicConst.BUY_FC3D_ZU6:
			int iZu6Balls = Fc3dZu6BallTable.getHighlightBallNums();
			iReturnValue = (int) getFc3dZu6FushiZhushu(iZu6Balls);
			break;
		// 福彩3D胆拖
		case PublicConst.BUY_FC3D_DANTUO:
			int danmas = Fc3dDantuoDanmaBallTable.getHighlightBallNums();// 1~2
			int tuomas = Fc3dDantuoTuomaBallTable.getHighlightBallNums();// 1~9
			iReturnValue = (int) getFc3dDantuoZhushu(danmas, tuomas);
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			// 福彩3D和值直选
			if (iWhich == 10) {
				iReturnValue = getFc3dZhixuanHezhiZhushu();
			} else if (iWhich == 11) {
				iReturnValue = getFc3dZu3HezhiZhushu();
			} else if (iWhich == 12) {
				iReturnValue = getFc3dZu6HezhiZhushu();
			}
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	/**
	 * 获得各种玩法的期数
	 * 
	 * @return 返回期数
	 */
	private int getQiShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		// 福彩3D直选复式
		case PublicConst.BUY_FC3D_ZHIXUAN:
			iReturnValue = mSeekBarQishu.getProgress();
			break;

		// 福彩3D组3复式
		case PublicConst.BUY_FC3D_ZU3:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		// 福彩3D组6复式
		case PublicConst.BUY_FC3D_ZU6:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		// 福彩3D胆拖
		case PublicConst.BUY_FC3D_DANTUO:
			iReturnValue = mSeekBarQishu.getProgress();
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			// 福彩3D和值直选
			if (iWhich == 10) {
				iReturnValue = mSeekBarQishu.getProgress();
			} else if (iWhich == 11) {
				iReturnValue = mSeekBarQishu.getProgress();
			} else if (iWhich == 12) {
				iReturnValue = mSeekBarQishu.getProgress();
			}
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	/**
	 * 获得福彩3D胆拖玩法注数
	 * 
	 * @param danmas
	 *            胆码个数
	 * @param tuomas
	 *            拖码个数
	 * @return 返回注数
	 */
	private long getFc3dDantuoZhushu(int danmas, int tuomas) {
		long tempzhushu = 0l;
		if (danmas > 0 && tuomas > 0) {
			tempzhushu += PublicMethod.zuhe(3 - danmas, tuomas) * 6;
		}
		return tempzhushu;
	}

	/**
	 * 获得福彩3D组6复式注数
	 * 
	 * @param iZu6balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getFc3dZu6FushiZhushu(int iZu6balls) {
		long tempzhushu = 0l;
		if (iZu6balls > 0) {
			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
		}
		return tempzhushu;

	}

	/**
	 * 获得福彩3D组3复式注数
	 * 
	 * @param iZu3balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getFc3dZu3FushiZhushu(int iZu3balls) {
		long tempzhushu = 0l;
		if (iZu3balls > 0) {
			tempzhushu += PublicMethod.zuhe(2, iZu3balls) * 2;
		}
		return tempzhushu;

	}

	/**
	 * 获得福彩3D直选和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZhixuanHezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = Fc3dHezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球号码从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值直选，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组3和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu3HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = Fc3dHezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5,
				5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组3，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得福彩3D组6和值注数
	 * 
	 * @return 返回注数
	 */
	private int getFc3dZu6HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = Fc3dHezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					// 删除倍数 cc 20100713
					iZhuShu = BallNoZhushus[j];// *iProgressBeishu;
					// String temp = "当前玩法为和值组6，共"
					// +(iZhuShu+"注，共"+(iZhuShu*2)+"元");
					// mTextSumMoney.setText(temp);
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 显示各种玩法的注数与金额
	 * 
	 * @param aWhichGroupBall
	 *            被选中的BallTable（主要针对福彩3D和值玩法，0为其他玩法，10为福彩3D和值直选，11为福彩3D组3和值组3，12为福彩3D和值组6
	 *            ）
	 */
	public void changeTextSumMoney(int aWhichGroupBall) {
		switch (iCurrentButton) {
		// 福彩3D直选
		case PublicConst.BUY_FC3D_ZHIXUAN:
			if (Fc3dZhixuanBaiweiBallTable.getHighlightBallNums() == 1
					&& Fc3dZhixuanShiweiBallTable.getHighlightBallNums() == 1
					&& Fc3dZhixuanGeweiBallTable.getHighlightBallNums() == 1) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为直选单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			} else if (Fc3dZhixuanBaiweiBallTable.getHighlightBallNums() > 1
					|| Fc3dZhixuanShiweiBallTable.getHighlightBallNums() > 1
					|| Fc3dZhixuanGeweiBallTable.getHighlightBallNums() > 1) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为直选复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			break;
		// 福彩3D组3
		case PublicConst.BUY_FC3D_ZU3:
			if (Fc3dA1Zu3DanshiBallTable.getHighlightBallNums() == 1
					&& Fc3dA2Zu3DanshiBallTable.getHighlightBallNums() == 1
					&& Fc3dBZu3DanshiBallTable.getHighlightBallNums() == 1) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组3单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			if (Fc3dZu3FushiBallTable.getHighlightBallNums() > 1) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组3复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			break;
		// 福彩3D组6
		case PublicConst.BUY_FC3D_ZU6:
			if (Fc3dZu6BallTable.getHighlightBallNums() == 3) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组6单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			if (Fc3dZu6BallTable.getHighlightBallNums() > 3) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组6复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");

			}
			break;
		// 福彩3D和值
		case PublicConst.BUY_FC3D_HEZHI:
			// 福彩3D和值直选
			if (aWhichGroupBall == 10) {
				int[] BallNos = Fc3dHezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
				int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55,
						63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15,
						10, 6, 3, 1 };// 0~27

				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球号码从1开始，故减去1
							int iZhuShu = BallNoZhushus[j] * iProgressBeishu;
							String temp = "当前玩法为和值直选，共"
									+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
							mTextSumMoney.setText(temp);
						}
					}
				}
			}
			// 福彩3D和值组3
			if (aWhichGroupBall == 11) {
				int[] BallNos = Fc3dHezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
				int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5,
						5, 4, 5, 5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26

				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
							int iZhuShu = BallNoZhushus[j] * iProgressBeishu;
							String temp = "当前玩法为和值组3，共"
									+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
							mTextSumMoney.setText(temp);
						}
					}
				}
			}
			// 福彩3D和值组6
			if (aWhichGroupBall == 12) {
				int[] BallNos = Fc3dHezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）
				int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10,
						10, 9, 8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
							int iZhuShu = BallNoZhushus[j] * iProgressBeishu;
							String temp = "当前玩法为和值组6，共"
									+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
							mTextSumMoney.setText(temp);
						}
					}
				}
			}
			break;
		// 福彩3D胆拖
		case PublicConst.BUY_FC3D_DANTUO:
			int danmas = Fc3dDantuoDanmaBallTable.getHighlightBallNums();// 1~2
			int tuomas = Fc3dDantuoTuomaBallTable.getHighlightBallNums();// 1~9
			if (danmas + tuomas < 3) {
				mTextSumMoney.setText(getResources().getString(
						R.string.dantuo_dialog_tips));
			}
			if (danmas > 0 && danmas < 3 && tuomas > 0
					&& (danmas + tuomas) >= 3 && (danmas + tuomas) <= 10) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为胆拖，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			break;
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
		case PublicConst.BUY_FC3D_ZU3:
			buy_FC3D_ZU3(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_FC3D_ZU6:
			buy_FC3D_ZU6(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			buy_FC3D_DANTUO(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_FC3D_ZHIXUAN:
			buy_FC3D_ZHIXUAN(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			// 当前iCurrentButton为BUY_FC3D_HEZHI
			if (aWhichGroupBall == 10) {
				buy_FC3D_HEZHI_ZHIXUAN(aWhichGroupBall, aBallViewId);
			}
			if (aWhichGroupBall == 11) {
				buy_FC3D_HEZHI_ZU3(aWhichGroupBall, aBallViewId);
			}
			if (aWhichGroupBall == 12) {
				buy_FC3D_HEZHI_ZU6(aWhichGroupBall, aBallViewId);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 和值组6
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从3开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_HEZHI_ZU6(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 12) { // 和值组6
			int iChosenBallSum = 1;// 只允许选择1个
			//wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZu6BallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZu6BallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.HezhiZu6BallGroup.iHezhiZu6BallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 和值组3
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_HEZHI_ZU3(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 11) { // 和值组3
			int iChosenBallSum = 1;// 只允许选择1个
			//wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZu3BallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZu3BallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.HezhiZu3BallGroup.iHezhiZu3BallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 和值直选
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_HEZHI_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 10) { // 和值直选
			int iChosenBallSum = 1;// 只允许选择1个
			//wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZhixuanBallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZhixuanBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[aBallViewId] = 0;
			}

		}
	}

	/**
	 * 福彩3D直选
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 7) { // 百位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = Fc3dZhixuanBaiweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus[aBallViewId] = 0;
			}
		}
		if (aWhichGroupBall == 8) { // 十位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = Fc3dZhixuanShiweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus[aBallViewId] = 0;
			}
		}
		if (aWhichGroupBall == 9) { // 个位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = Fc3dZhixuanGeweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 福彩3D胆拖
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_DANTUO(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 5) { // 胆码1~2个
			int iChosenBallSum = 2;
			int isHighLight = Fc3dDantuoDanmaBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus[aBallViewId] = 1;
				mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus[aBallViewId] = 0;
				Fc3dDantuoTuomaBallTable.clearOnBallHighlight(aBallViewId);
			} else {
				mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus[aBallViewId] = 0;
			}
		}
		if (aWhichGroupBall == 6) { // 拖码1~9个
			int iChosenBallSum = 9;
			int isHighLight = Fc3dDantuoTuomaBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus[aBallViewId] = 1;
				mBallHolder.DantuoBallGroup.iDantuoDanmaBallStatus[aBallViewId] = 0;
				Fc3dDantuoDanmaBallTable.clearOnBallHighlight(aBallViewId);
			} else {
				mBallHolder.DantuoBallGroup.iDantuoTuomaBallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 福彩3D组6
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_ZU6(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 4) { // 组6
			int iChosenBallSum = 9;
			int isHighLight = Fc3dZu6BallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu6BallGroup.iZu6BallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.Zu6BallGroup.iZu6BallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 福彩3D组3
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_FC3D_ZU3(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = Fc3dA1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = Fc3dA2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 1;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 1;
				Fc3dBZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = Fc3dA1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = Fc3dA2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 1;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 1;
				Fc3dBZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 2) {
			int iChosenBallSum = 1;
			// 组3,前两组相同，第三组不能与之相同

			int isHighLight = Fc3dBZu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 1;
				Fc3dA1Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Fc3dA2Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 3) {
			int iChosenBallSum = 10;
			int isHighLight = Fc3dZu3FushiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.Zu3BallGroup.iZu3FushiBallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 创建BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @param int aBallViewText 0:小球从0开始显示,1:小球从1开始显示 ,3小球从3开始显示(福彩3D和值组6从3开始)
	 * @return BallTable
	 */
	private BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int aFieldWidth, int aBallNum, int aBallViewWidth, int[] aResId,
			int aIdStart, int aBallViewText) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);

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
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
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
			iBallTable.tableLayout.addView(tableRow,
					new TableLayout.LayoutParams(FP, WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(this);
			for (int col = 0; col < lastLineViewNum; col++) {

				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
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
			iBallTable.tableLayout.addView(tableRow,
					new TableLayout.LayoutParams(FP, WC));
		}
		return iBallTable;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_fc3d, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("************featureId " + featureId);
		switch (item.getItemId()) {
		// 确定投注
		case R.id.fc3d_confirm:
			beginTouZhu();
			break;
		// 重新机选
		case R.id.fc3d_reselect_num:
			beginReselect();
			break;
		// 玩法介绍
		case R.id.fc3d_game_introduce:
			dialogGameIntroduction();
			break;
		// 取消
		case R.id.fc3d_cancel:
			this.finish();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public void onCancelClick() {

	}

	/**
	 * menu里的重新选号
	 */
	private void beginReselect() {

		// 福彩3D直选
		if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {

			Fc3dZhixuanBaiweiBallTable.clearAllHighlights();
			Fc3dZhixuanShiweiBallTable.clearAllHighlights();
			Fc3dZhixuanGeweiBallTable.clearAllHighlights();

		}
		// 福彩3D组3
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {

			Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
			Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
			Fc3dBZu3DanshiBallTable.clearAllHighlights();
			Fc3dZu3FushiBallTable.clearAllHighlights();

		}
		// 福彩3D组6
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {

			Fc3dZu6BallTable.clearAllHighlights();

		}
		// 福彩3D胆拖
		if (iCurrentButton == PublicConst.BUY_FC3D_DANTUO) {

			Fc3dDantuoDanmaBallTable.clearAllHighlights();
			Fc3dDantuoTuomaBallTable.clearAllHighlights();

		}
		// 福彩3D和值
		if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {
			// 福彩3D和值直选
			if (iWhich == 10) {

				Fc3dHezhiZhixuanBallTable.clearAllHighlights();

			}
			// 福彩3D和值组3
			if (iWhich == 11) {

				Fc3dHezhiZu3BallTable.clearAllHighlights();

			}
			// 福彩3D和值组6
			if (iWhich == 12) {

				Fc3dHezhiZu6BallTable.clearAllHighlights();

			}
		}

	}

	/**
	 * 各种玩法的投注方法
	 */
	private void beginTouZhu() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				FC3DTest.this, "addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		// 获取期数 陈晨 20100711
		int iQiShu = getQiShu();
		// 投注时判断是否登录
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(FC3DTest.this, UserLogin.class);
			startActivity(intentSession);
		} else {
			// 福彩3D直选
			if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {

				int baiweiNums = Fc3dZhixuanBaiweiBallTable
						.getHighlightBallNums();
				int shiweiNums = Fc3dZhixuanShiweiBallTable
						.getHighlightBallNums();
				int geweiNums = Fc3dZhixuanGeweiBallTable
						.getHighlightBallNums();
				// wangyl 7.12 修改确定投注成功后的对话框
				int[] baiweis = Fc3dZhixuanBaiweiBallTable
						.getHighlightBallNOs();
				int[] shiweis = Fc3dZhixuanShiweiBallTable
						.getHighlightBallNOs();
				int[] geweis = Fc3dZhixuanGeweiBallTable.getHighlightBallNOs();
				String baiweistr = "";
				String shiweistr = "";
				String geweistr = "";
				for (int i = 0; i < baiweiNums; i++) {
					baiweistr += (baiweis[i] - 1) + ".";
					if (i == baiweiNums - 1) {
						baiweistr = baiweistr.substring(0,
								baiweistr.length() - 1);
					}
				}
				for (int i = 0; i < shiweiNums; i++) {
					shiweistr += (shiweis[i] - 1) + ".";
					if (i == shiweiNums - 1) {
						shiweistr = shiweistr.substring(0,
								shiweistr.length() - 1);
					}
				}
				for (int i = 0; i < geweiNums; i++) {
					geweistr += (geweis[i] - 1) + ".";
					if (i == geweiNums - 1) {
						geweistr = geweistr.substring(0, geweistr.length() - 1);
					}
				}
				if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
					DialogTouzhu("请在百位，十位，个位均至少选择一个小球后再投注");
				} else {
					int iZhuShu = getZhuShu() * iProgressBeishu;
					// alert("您选择的是"+iZhuShu+"注福彩3D直选彩票，"+"共计"+(iZhuShu*2)+"元");
					// wangyl 7.13 投注不能大于10万
					//wangyl 7.23 投注不能大于600注
					if(iZhuShu/iProgressBeishu>600){
						DialogZhixuan();
					} else if(iZhuShu*2>100000){
						DialogExcessive();
					} else {
						alert("注码：" + "\n" + "   百位：" + baiweistr + "\n"
								+ "   十位：" + shiweistr + "\n" + "   个位："
								+ geweistr + "\n" + "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" 
								+"冻结金额："
								+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？");
					}
				}

			}
			// 福彩3D组3
			if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {

				int iZhuShu = 0;
				if (bZu3Danshi) {
					int baiweiNums = Fc3dA1Zu3DanshiBallTable
							.getHighlightBallNums();
					int shiweiNums = Fc3dA2Zu3DanshiBallTable
							.getHighlightBallNums();
					int geweiNums = Fc3dBZu3DanshiBallTable
							.getHighlightBallNums();

					if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
						DialogTouzhu("请再百位，十位， 个位中均选择一个小球后再投注");
					} else if (baiweiNums == 1 && shiweiNums == 1
							&& geweiNums == 1) {
						// iZhuShu=mSeekBarBeishu.getProgress();
						// String zhuma=zhuma_fc3d();
						// String str=pay(zhuma, iZhuShu+"");
						// 显示用户选中的信息 2010/7/4 陈晨
						iZhuShu = mSeekBarBeishu.getProgress();
						// wangyl 7.12 修改确定投注成功后的对话框
						// 王艳玲 20100714 将上面的移下来
						String baiweistr = Fc3dA1Zu3DanshiBallTable
								.getHighlightBallNOs()[0]
								- 1 + "";// 前2位相同
						String geweistr = Fc3dBZu3DanshiBallTable
								.getHighlightBallNOs()[0]
								- 1 + "";// 前2位相同
						// alert("您选择的是"+iZhuShu+"注福彩3D组3单式彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							// wangyl 7.13 注数错误
							alert("注码：" + baiweistr + "." + baiweistr + "."
									+ geweistr + "\n" + "注数：" + 1 + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+"冻结金额："
									+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"
									+ "确认支付吗？");
						}
					}
				}
				if (bZu3Fushi) {
					if (Fc3dZu3FushiBallTable.getHighlightBallNums() < 2) {
						DialogTouzhu("请至少选择2个小球后再投注");
					} else {
						// wangyl 7.12 修改确定投注成功后的对话框
						int[] fushiNums = Fc3dZu3FushiBallTable
								.getHighlightBallNOs();
						String fushiStr = "";
						for (int i = 0; i < fushiNums.length; i++) {
							fushiStr += (fushiNums[i] - 1) + ".";
							if (i == fushiNums.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						iZhuShu = getZhuShu() * iProgressBeishu;

						// alert("您选择的是"+iZhuShu+"注福彩3D组3复式彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							alert("注码：" + fushiStr + "\n" + "注数：" + iZhuShu
									/ iProgressBeishu + "注" + "\n" + "倍数："
									+ iProgressBeishu + "倍" + "\n" + "追号："
									+ iProgressQishu + "期" + "\n" + "金额："
									+ iZhuShu * 2 + "元" + "\n" 
									+"冻结金额："
									+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？");
						}
					}
				}

			}
			// 福彩3D组6
			if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {

				if (Fc3dZu6BallTable.getHighlightBallNums() < 3) {
					DialogTouzhu("请至少选择3个小球后再投注");
				} else {
					// wangyl 7.12 修改确定投注成功后的对话框
					int[] fushiNums = Fc3dZu6BallTable.getHighlightBallNOs();
					String fushiStr = "";
					for (int i = 0; i < fushiNums.length; i++) {
						fushiStr += (fushiNums[i] - 1) + ".";
						if (i == fushiNums.length - 1) {
							fushiStr = fushiStr.substring(0,
									fushiStr.length() - 1);
						}
					}
					int iZhuShu = getZhuShu() * iProgressBeishu;

					// alert("您选择的是"+iZhuShu+"注福彩3D组6彩票，"+"共计"+(iZhuShu*2)+"元");
					// wangyl 7.13 投注不能大于10万
					if (iZhuShu * 2 > 100000) {
						DialogExcessive();
					} else {
						alert("注码：" + fushiStr + "\n" + "注数：" + iZhuShu
								/ iProgressBeishu + "注" + "\n" + "倍数："
								+ iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" 
								+"冻结金额："
								+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？");
					}
				}

			}
			// 福彩3D胆拖
			if (iCurrentButton == PublicConst.BUY_FC3D_DANTUO) {

				int danmaNums = Fc3dDantuoDanmaBallTable.getHighlightBallNums();
				int tuomaNums = Fc3dDantuoTuomaBallTable.getHighlightBallNums();
				// wangyl 7.12 修改确定投注成功后的对话框
				int[] danma = Fc3dDantuoDanmaBallTable.getHighlightBallNOs();
				int[] tuoma = Fc3dDantuoTuomaBallTable.getHighlightBallNOs();
				String danmastr = "";
				String tuomastr = "";
				for (int i = 0; i < danmaNums; i++) {
					danmastr += (danma[i] - 1) + ".";
					if (i == danmaNums - 1) {
						danmastr = danmastr.substring(0, danmastr.length() - 1);
					}
				}
				for (int i = 0; i < tuomaNums; i++) {
					tuomastr += (tuoma[i] - 1) + ".";
					if (i == tuomaNums - 1) {
						tuomastr = tuomastr.substring(0, tuomastr.length() - 1);
					}
				}

				if (danmaNums < 1 || tuomaNums < 1
						|| (danmaNums + tuomaNums) < 3) {
					DialogTouzhu("请至少选择1个胆码， 1个拖码(且胆码与拖码的个数不少于3个)后再投注");
				} else {
					int iZhuShu = getZhuShu() * iProgressBeishu;
					// alert("您选择的是"+iZhuShu+"注福彩3D胆拖彩票，"+"共计"+(iZhuShu*2)+"元");
					// wangyl 7.13 投注不能大于10万
					if (iZhuShu * 2 > 100000) {
						DialogExcessive();
					} else {
						alert("注码：" + "\n" + "   胆码：" + danmastr + "\n"
								+ "   拖码：" + tuomastr + "\n" + "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" 
								+"冻结金额："
								+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？");
					}
				}

			}
			// 福彩3D和值
			if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {
				// 福彩3D和值直选
				if (iWhich == 10) {

					if (Fc3dHezhiZhixuanBallTable.getHighlightBallNums() < 1) {
						DialogTouzhu("请选择小球号码后再投注");
					} else if (Fc3dHezhiZhixuanBallTable.getHighlightBallNums() == 1) {
						// wangyl 7.13 配合陈晨投注时用
						int iZhuShu = getZhuShu() * iProgressBeishu;
						// wangyl 7.12 修改确定投注成功后的对话框
						String fushiStr = Fc3dHezhiZhixuanBallTable
								.getHighlightBallNOs()[0]
								- 1 + "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							alert("注码：" + fushiStr + "\n" + "注数：" + iZhuShu
									/ iProgressBeishu + "注" + "\n" + "倍数："
									+ iProgressBeishu + "倍" + "\n" + "追号："
									+ iProgressQishu + "期" + "\n" + "金额："
									+ iZhuShu * 2 + "元" + "\n" 
									+"冻结金额："
									+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"+ "确认支付吗？");
						}
					}

				}
				// 福彩3D和值组3
				if (iWhich == 11) {

					if (Fc3dHezhiZu3BallTable.getHighlightBallNums() < 1) {
						DialogTouzhu("请选择小球号码后再投注");
					} else if (Fc3dHezhiZu3BallTable.getHighlightBallNums() == 1) {
						// 显示用户选中的信息
						// wangyl 7.13 配合陈晨投注时用
						int iZhuShu = getZhuShu() * iProgressBeishu;
						// alert("您选择的是"+iZhuShu+"注福彩3D和值组3彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.12 修改确定投注成功后的对话框
						String fushiStr = Fc3dHezhiZu3BallTable
								.getHighlightBallNOs()[0]
								+ "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							alert("注码：" + fushiStr + "\n" + "注数：" + iZhuShu
									/ iProgressBeishu + "注" + "\n" + "倍数："
									+ iProgressBeishu + "倍" + "\n" + "追号："
									+ iProgressQishu + "期" + "\n" + "金额："
									+ iZhuShu * 2 + "元" + "\n" 
									+"冻结金额："
									+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"
									+ "确认支付吗？");
						}
					}

				}
				// 福彩3D和值组6
				if (iWhich == 12) {

					if (Fc3dHezhiZu6BallTable.getHighlightBallNums() < 1) {
						DialogTouzhu("请选择小球号码后再投注");
					} else if (Fc3dHezhiZu6BallTable.getHighlightBallNums() == 1) {
						// wangyl 7.13 配合陈晨投注时用
						int iZhuShu = getZhuShu() * iProgressBeishu;
						// alert("您选择的是"+iZhuShu+"注福彩3D和值组6彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.12 修改确定投注成功后的对话框
						String fushiStr = Fc3dHezhiZu6BallTable
								.getHighlightBallNOs()[0]
								+ 2 + "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							alert("注码：" + fushiStr + "\n" + "注数：" + iZhuShu
									/ iProgressBeishu + "注" + "\n" + "倍数："
									+ iProgressBeishu + "倍" + "\n" + "追号："
									+ iProgressQishu + "期" + "\n" + "金额："
									+ iZhuShu * 2 + "元" + "\n" 
									+"冻结金额："
									+(2* (iProgressQishu-1)*iZhuShu)+"元"+"\n"
									+ "确认支付吗？");
						}
					}

				}
			}
		}
	}

	// 选择小球
	public void onOkClick(int aRedNum, int aBlueNum) {
	}

	@Override
	// public void onOkClick(int baiweiNum, int shiweiNum, int geweiNum) {
	// public void onOkClick(int[] aReturn) {
	// switch(iCurrentButton){
	// case PublicConst.BUY_FC3D_ZHIXUAN:
	// Fc3dZhixuanBaiweiBallTable.randomChoose(baiweiNum);
	// Fc3dZhixuanShiweiBallTable.randomChoose(shiweiNum);
	// Fc3dZhixuanGeweiBallTable.randomChoose(geweiNum);
	// changeTextSumMoney(0);
	// break;
	// case PublicConst.BUY_FC3D_ZU3:
	// Fc3dZu3FushiBallTable.randomChoose(baiweiNum);
	// changeTextSumMoney(0);
	// break;
	// case PublicConst.BUY_FC3D_ZU6:
	// Fc3dZu6BallTable.randomChoose(baiweiNum);
	// changeTextSumMoney(0);
	// break;
	// case PublicConst.BUY_FC3D_DANTUO:
	// Fc3dDantuoDanmaBallTable.clearAllHighlights();
	// Fc3dDantuoTuomaBallTable.clearAllHighlights();
	//    		
	// //为避免胆码和拖码重复，先获得一组随机数，在分别分给胆码和拖码
	// int[] randomNums =
	// PublicMethod.getRandomsWithoutCollision(baiweiNum+shiweiNum,0, 9);
	// for(int i=0;i<randomNums.length;i++){
	// if(i<=baiweiNum-1){
	// Fc3dDantuoDanmaBallTable.changeBallState(baiweiNum, randomNums[i]);
	// }else{
	// Fc3dDantuoTuomaBallTable.changeBallState(shiweiNum, randomNums[i]);
	// }
	// }
	// changeTextSumMoney(0);
	// break;
	// default:
	// break;
	// }
	// }
	public void onOkClick(int[] aReturn) {
		mBallHolder = new BallHolderFc3d();
		switch (iCurrentButton) {
		case PublicConst.BUY_FC3D_ZHIXUAN:
			mBallHolder = Fc3dZhixuanBaiweiBallTable
					.randomChooseConfigChangeFc3d(aReturn[0], mBallHolder, 7,
							null);
			mBallHolder = Fc3dZhixuanShiweiBallTable
					.randomChooseConfigChangeFc3d(aReturn[1], mBallHolder, 8,
							null);
			mBallHolder = Fc3dZhixuanGeweiBallTable
					.randomChooseConfigChangeFc3d(aReturn[2], mBallHolder, 9,
							null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_ZU3:
			mBallHolder = Fc3dZu3FushiBallTable.randomChooseConfigChangeFc3d(
					aReturn[0], mBallHolder, 3, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_ZU6:
			mBallHolder = Fc3dZu6BallTable.randomChooseConfigChangeFc3d(
					aReturn[0], mBallHolder, 4, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			Fc3dDantuoDanmaBallTable.clearAllHighlights();
			Fc3dDantuoTuomaBallTable.clearAllHighlights();

			// 为避免胆码和拖码重复，先获得一组随机数，在分别分给胆码和拖码
			int[] randomNums = PublicMethod.getRandomsWithoutCollision(
					aReturn[0] + aReturn[1], 0, 9);

			mBallHolder = Fc3dDantuoDanmaBallTable
					.randomChooseConfigChangeFc3d(aReturn[0], mBallHolder, 5,
							randomNums);
			mBallHolder = Fc3dDantuoTuomaBallTable
					.randomChooseConfigChangeFc3d(aReturn[1], mBallHolder, 6,
							randomNums);

			changeTextSumMoney(0);
			break;
		default:
			break;
		}
	}

	// 投注调用的方法
	// protected String pay(String bets,String bets_zhu_num) {
	// // TODO Auto-generated method stub
	// BettingInterface betting=new BettingInterface();
	//			
	// //String error_code=betting.Betting("15931199023", bets, bets_zhu_num,
	// "9E6764DA32B1BAD4ABBC41616992F82F");
	// //String error_code=betting.Betting("13121795856", bets, bets_zhu_num,
	// "909CA37CE1360C76836103F37DEC479A");
	//	
	// // String error_code=betting.Betting("13121795856", bets, bets_zhu_num,
	// "57637B1A23BFCB80A1096A5320F44425");
	// ShellRWSharesPreferences shellRW =new
	// ShellRWSharesPreferences(this,"addInfo");
	// String sessionid=shellRW.getUserLoginInfo("sessionid");
	// String phonenum=shellRW.getUserLoginInfo("phonenum");
	// PublicMethod.myOutput("-------------touzhusessionid"+sessionid);
	// PublicMethod.myOutput("-------------phonenum"+phonenum);
	//		
	// String error_code=betting.Betting(phonenum, bets,
	// bets_zhu_num,sessionid);
	// // FileIO file=new FileIO();
	// // file.infoToWrite="----------------qilecaitouzhu"+error_code;
	// // file.write();
	// return error_code;
	// }
	// 使用新接口 陈晨 2010/7/11
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
	protected String payNew(String bets, String count, String amount) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("----------amount--------" + amount + "----count"
				+ count);
		BettingInterface betting = new BettingInterface();

		// String error_code=betting.Betting("15931199023", bets, bets_zhu_num,
		// "9E6764DA32B1BAD4ABBC41616992F82F");
		// String error_code=betting.Betting("13121795856", bets, bets_zhu_num,
		// "C8FAC57FDB8C68BE453B7D838F2E1D23");
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");

		PublicMethod.myOutput("-------------touzhusessionid" + sessionid);
		PublicMethod.myOutput("-------------phonenum" + phonenum);

		String error_code = betting.BettingNew(bets, count, amount, sessionid);
		return error_code;
	}

	String CityCode = "1512-";// 城市编号
	String DDD_falg = "F47103-";// 彩种编号
	String typeCode = "";// 玩法
	String staticCode = "-01-";
	String endCode = "^";// 结束标志
	String dateCode = "0";// 数据
	String sendCode = "";

	// 每种玩法的编号
	String zxds = "00";// 单选
	String z3ds = "01";// 组3
	String z6ds = "02";// 组6
	String zxHHH = "10";// 直和值
	String z3HHH = "11";// 组3值
	String z6HHH = "12";// 组6值
	String zxfs = "20";// 单选复式
	String z3fs = "31";// 组3复式
	String z6fs = "32";// 组6复式
	String dt = "54";// 但托

	//	
	/**
	 * 获取每一个种类的注码
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	private String zhuma_fc3d() {
		String[] zhuma = null;
		String t_str = "";
		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = getZhuShu();
		// 判断所在福彩3D的种类
		// 福彩3D直选复式玩法
		if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {

			int[] zhuma_baiwei = Fc3dZhixuanBaiweiBallTable
					.getHighlightBallNOs();
			int[] zhuma_shiwei = Fc3dZhixuanShiweiBallTable
					.getHighlightBallNOs();
			for (int i = 0; i < zhuma_shiwei.length; i++) {
				PublicMethod.myOutput("--------------------------+======"
						+ zhuma_shiwei[i]);
			}
			int[] zhuma_gewei = Fc3dZhixuanGeweiBallTable.getHighlightBallNOs();
			if (zhuma_baiwei.length == 1 && zhuma_shiwei.length == 1
					&& zhuma_gewei.length == 1) {
				typeCode = zxfs;
				// 直选单式注码格式有变化 陈晨 2010/7/11
				String[] str = { "01", "0" + (zhuma_baiwei[0] - 1), "^", "01",
						"0" + (zhuma_shiwei[0] - 1), "^", "01",
						"0" + (zhuma_gewei[0] - 1) };
				zhuma = str;
			} else {
				// 3D直选复式注码玩法 2010/7/4 陈晨
				if (zhuma_baiwei.length != 0 && zhuma_shiwei.length != 0
						&& zhuma_gewei.length != 0) {
					typeCode = zxfs;
					String[] str = new String[zhuma_baiwei.length
							+ zhuma_shiwei.length + zhuma_gewei.length + 5];
					if (zhuma_baiwei.length < 10) {
						str[0] = "0" + zhuma_baiwei.length;
					} else {
						str[0] = zhuma_baiwei.length + "";
					}
					for (int i = 0; i < zhuma_baiwei.length; i++) {
						str[i + 1] = "0" + (zhuma_baiwei[i] - 1);
					}
					str[zhuma_baiwei.length + 1] = "^";

					if (zhuma_shiwei.length < 10) {
						str[zhuma_baiwei.length + 2] = "0"
								+ zhuma_shiwei.length;
					} else {
						str[zhuma_baiwei.length + 2] = zhuma_shiwei.length + "";
					}
					for (int i = 0; i < zhuma_shiwei.length; i++) {
						str[zhuma_baiwei.length + 3 + i] = "0"
								+ (zhuma_shiwei[i] - 1);
					}
					str[zhuma_baiwei.length + zhuma_shiwei.length + 3] = "^";
					if (zhuma_gewei.length < 10) {
						str[zhuma_baiwei.length + zhuma_shiwei.length + 4] = "0"
								+ zhuma_gewei.length;
					} else {
						str[zhuma_baiwei.length + zhuma_shiwei.length + 4] = zhuma_gewei.length
								+ "";
					}
					for (int i = 0; i < zhuma_gewei.length; i++) {
						str[zhuma_baiwei.length + zhuma_shiwei.length + 5 + i] = "0"
								+ (zhuma_gewei[i] - 1);
					}
					zhuma = str;
				}

			}
			// else
			// if(zhuma_baiwei.length>1||zhuma_shiwei.length>1&&zhuma_gewei.length>1){
			// String[]
			// str={"0"+(zhuma_baiwei[0]-1),"0"+(zhuma_shiwei[0]-1),"0"+(zhuma_gewei[0]-1)};
			// zhuma=str;
			// }

		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {
			if (bZu3Danshi) {
				typeCode = z3ds;
				int[] zhuma_zu3danshi_A1 = Fc3dA1Zu3DanshiBallTable
						.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A2 = Fc3dA2Zu3DanshiBallTable
						.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A3 = Fc3dBZu3DanshiBallTable
						.getHighlightBallNOs();
				// 需要排序
				if (zhuma_zu3danshi_A1[0] < zhuma_zu3danshi_A3[0]) {
					String[] str = { "0" + (zhuma_zu3danshi_A1[0] - 1),
							"0" + (zhuma_zu3danshi_A2[0] - 1),
							"0" + (zhuma_zu3danshi_A3[0] - 1) };
					zhuma = str;
				} else {
					String[] str = { "0" + (zhuma_zu3danshi_A3[0] - 1),
							"0" + (zhuma_zu3danshi_A2[0] - 1),
							"0" + (zhuma_zu3danshi_A1[0] - 1) };
					zhuma = str;
				}
				// zhuma=str;
			}
			if (bZu3Fushi) {
				typeCode = z3fs;
				int[] zhuma_zu3fushi = Fc3dZu3FushiBallTable
						.getHighlightBallNOs();
				// 之前组3投注码格式有误 陈晨20100712
				int highlightballnum = Fc3dZu3FushiBallTable
						.getHighlightBallNums();
				String[] str = new String[highlightballnum + 1];

				if (highlightballnum < 10) {
					str[0] = "0" + highlightballnum;
				} else if (highlightballnum > 9) {
					str[0] = "" + highlightballnum;
				}
				for (int i = 0; i < zhuma_zu3fushi.length; i++) {
					str[i + 1] = "0" + (zhuma_zu3fushi[i] - 1);
				}
				// 注码有误 删除
				// String[]
				// str={"02","0"+(zhuma_zu3fushi[0]-1),"0"+(zhuma_zu3fushi[1]-1)};
				zhuma = str;
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
			int num = Fc3dZu6BallTable.getHighlightBallNums();
			int[] zhuma_zu6danfushi = Fc3dZu6BallTable.getHighlightBallNOs();
			String[] str = new String[zhuma_zu6danfushi.length + 1];
			if (num < 4) {
				typeCode = z6ds;
				for (int i = 0; i < zhuma_zu6danfushi.length; i++) {
					str[0] = "";
					str[i + 1] = "0" + (zhuma_zu6danfushi[i] - 1);
				}
			} else if (num > 3) {
				typeCode = z6fs;
				int highlightballnum = Fc3dZu6BallTable.getHighlightBallNums();
				if (highlightballnum < 10) {
					str[0] = "0" + highlightballnum;
				} else if (highlightballnum > 9) {
					str[0] = "" + highlightballnum;
				}
				for (int i = 0; i < zhuma_zu6danfushi.length; i++) {
					str[i + 1] = "0" + (zhuma_zu6danfushi[i] - 1);
				}

			}
			// int[] zhuma_zu6danfushi=Fc3dZu6BallTable.getHighlightBallNOs();
			// String[] str=new String[zhuma_zu6danfushi.length];
			// for(int i=0;i<zhuma_zu6danfushi.length;i++){
			// str[i]="0"+(zhuma_zu6danfushi[i]-1);
			// }
			zhuma = str;
		} else if (iCurrentButton == PublicConst.BUY_FC3D_DANTUO) {
			typeCode = dt;
			int[] zhuma_dantuo_danma = Fc3dDantuoDanmaBallTable
					.getHighlightBallNOs();
			int[] zhuma_dantuo_tuoma = Fc3dDantuoTuomaBallTable
					.getHighlightBallNOs();
			String[] str = new String[zhuma_dantuo_danma.length
					+ zhuma_dantuo_tuoma.length + 1];
			for (int i = 0; i < zhuma_dantuo_danma.length; i++) {
				str[i] = "0" + (zhuma_dantuo_danma[i] - 1);
			}
			str[zhuma_dantuo_danma.length] = "*";
			for (int j = 0; j < zhuma_dantuo_tuoma.length; j++) {
				str[zhuma_dantuo_danma.length + 1 + j] = "0"
						+ (zhuma_dantuo_tuoma[j] - 1);
			}
			zhuma = str;
		} else if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {

			// 判断是和值直选 还是和值组3 还是和值组6
			if (iWhich == 10) {
				typeCode = zxHHH;
				// 判断是一位数还是两位 陈晨 20100714
				int[] zhuma_zhixuanhezhi = Fc3dHezhiZhixuanBallTable
						.getHighlightBallNOs();
				if (zhuma_zhixuanhezhi[0] - 1 < 10) {
					String[] str = { "0" + (zhuma_zhixuanhezhi[0] - 1) };
					zhuma = str;
				} else {
					String[] str = { "" + (zhuma_zhixuanhezhi[0] - 1) };

					zhuma = str;
				}
			} else if (iWhich == 11) {
				typeCode = z3HHH;
				int[] zhuma_zu3hezhi = Fc3dHezhiZu3BallTable
						.getHighlightBallNOs();
				// 判断是一位数还是两位 陈晨 20100714
				if (zhuma_zu3hezhi[0] < 10) {
					String[] str = { "0" + zhuma_zu3hezhi[0] };
					zhuma = str;
				} else {
					String[] str = { "" + zhuma_zu3hezhi[0] };
					zhuma = str;
				}
			} else if (iWhich == 12) {
				typeCode = z6HHH;
				int[] zhuma_zu6hezhi = Fc3dHezhiZu6BallTable
						.getHighlightBallNOs();
				// 判断是一位数还是两位 陈晨 20100714
				if (zhuma_zu6hezhi[0] + 2 < 10) {
					String[] str = { "0" + (zhuma_zu6hezhi[0] + 2) };
					zhuma = str;
				} else {
					String[] str = { "" + (zhuma_zu6hezhi[0] + 2) };
					zhuma = str;
				}
			}

		}
		// 加上注数 2010/7/11/ 陈晨
		String zhushu = "";
		if (iZhuShu < 10) {
			PublicMethod.myOutput("---------izhushu" + iZhuShu);
			zhushu += "0" + iZhuShu;
		} else if (iZhuShu >= 10) {
			PublicMethod.myOutput("---------izhushu" + iZhuShu);
			zhushu += "" + iZhuShu;
		}
		String beishu_ = "";
		if (beishu < 10) {
			beishu_ += "0" + beishu;
		} else if (beishu >= 10) {
			beishu_ += "" + beishu;
		}
		// 之前构造注码格式没有加注数 陈晨 2010/7/11
		t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-" + typeCode
				+ beishu_;

		for (int i = 0; i < zhuma.length; i++) {
			t_str += zhuma[i];
		}
		t_str += endCode;
		PublicMethod.myOutput("-------------zhuma_zhixuan" + t_str);
		return t_str;
	}

	/**
	 * 投注不满足条件时弹出的对话框
	 * 
	 * @param message
	 */
	private void DialogTouzhu(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(FC3DTest.this);
		builder.setTitle(getResources()
				.getString(R.string.please_choose_number));
		builder.setMessage(message);
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}

	// wangyl 7.13 单笔投注大于10万元时的对话框
	/**
	 * 单笔投注大于10万元时的对话框
	 */
	private void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(FC3DTest.this);
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
	//wangyl 7.23 福彩3D投注注数大于600注时的对话框
	/**
	 * 福彩3D直选投注注数大于600注时的对话框
	 */
	private void  DialogZhixuan(){
		AlertDialog.Builder builder = new AlertDialog.Builder(FC3DTest.this);
		builder.setTitle("投注失败");
		builder.setMessage("请选择不大于600注投注");
		//确定
		builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
			}
			
		});
		builder.show();
	}
	/**
	 * MENU里的玩法介绍
	 */
	private void dialogGameIntroduction() {

		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_fc3d.html";
		;
		webView.loadUrl(url);

		AlertDialog.Builder builder = new AlertDialog.Builder(FC3DTest.this);
		builder.setTitle("玩法介绍");
		builder.setView(webView);
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
	 * 进行横纵屏切换时获得的倍数
	 */
	private int getBeishu() {
		return mSeekBarBeishu.getProgress();

	}

	/**
	 * 进行横纵屏切换时获得的期数
	 */
	private int getQishu() {
		return mSeekBarQishu.getProgress();

	}

	/**
	 * 进行横纵屏切换时机选复选框的状态
	 */
	private boolean getCheckBox() {
		return mCheckBox.isChecked();
	}

	/**
	 * 组3进行横纵屏切换时单式RadioButton的状态
	 */
	private boolean getZu3DanshiRadioButton() {
		return danshirbtn.isChecked();
	}

	/**
	 * 组3进行横纵屏切换时复式RadioButton的状态
	 */
	private boolean getZu3FushiRadioButton() {
		return fushirbtn.isChecked();
	}

	/**
	 * 进行横纵屏切换时设置当前顶部标签状态
	 * 
	 */
	public void showHighLight() {
		// 福彩3D直选
		if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {
			topButtonGroup.check(1);// 没有改变不触发事件，因为默认是0
			topButtonGroup.check(0);
			topButtonGroup.invalidate();
		}
		// 福彩3D组3
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {
			topButtonGroup.check(1);
			topButtonGroup.invalidate();
		}
		// 福彩3D组6
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
			topButtonGroup.check(2);
			topButtonGroup.invalidate();
		}
		// 福彩3D胆拖
		if (iCurrentButton == PublicConst.BUY_FC3D_DANTUO) {
			topButtonGroup.check(3);
			topButtonGroup.invalidate();
		}
		// 福彩3D和值
		if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {
			topButtonGroup.check(4);
			topButtonGroup.invalidate();
		}
	}

	/**
	 * @author WangYanling 记录每个页面及是否切换横纵屏
	 */
	public class BallHolderFc3d {
		// 福彩3D直选
		BallGroup ZhixuanBallGroup = new BallGroup();
		// 福彩3D组3
		BallGroup Zu3BallGroup = new BallGroup();
		// 福彩组6
		BallGroup Zu6BallGroup = new BallGroup();
		// 福彩胆拖
		BallGroup DantuoBallGroup = new BallGroup();
		// 和值
		BallGroup HezhiZhixuanBallGroup = new BallGroup();
		BallGroup HezhiZu3BallGroup = new BallGroup();
		BallGroup HezhiZu6BallGroup = new BallGroup();

		// 顶部标签RadioGroup
		int topButtonGroup;
		// 是否切换横纵屏 1为是，0为否
		int flag = 0;

	}

	/**
	 * @author WangYanling
	 * @category 记录当前页面控件的各个状态以及被选中的小球在ballTable中的index
	 */
	public class BallGroup {
		// 福彩3D直选
		int[] iZhixuanBaiweiBallStatus = new int[10];
		int[] iZhixuanShiweiBallStatus = new int[10];
		int[] iZhixuanGeweiBallStatus = new int[10];
		// 福彩3D组3
		int[] iZu3A1BallStatus = new int[10];
		int[] iZu3A2BallStatus = new int[10];
		int[] iZu3BBallStatus = new int[10];
		int[] iZu3FushiBallStatus = new int[10];
		boolean bRadioBtnDanshi;
		boolean bRadioBtnFushi;
		// 福彩组6
		int[] iZu6BallStatus = new int[10];
		// 福彩胆拖
		int[] iDantuoDanmaBallStatus = new int[10];
		int[] iDantuoTuomaBallStatus = new int[10];
		// 福彩和值直选
		int[] iHezhiZhixuanBallStatus = new int[28];
		int[] iHezhiZu3BallStatus = new int[26];
		int[] iHezhiZu6BallStatus = new int[22];
		// 倍数
		int iBeiShu;
		// 期数
		int iQiShu;
		// 机选复选框
		boolean bCheckBox;
	}

	// 提示框 用来确定是否投注
	/**
	 * 投注时调用的函数
	 * 
	 * @param
	 * @param
	 * @param
	 * @param
	 * @param
	 */
	private void alert(String string) {

		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								showDialog(DIALOG1_KEY);
//								加入是否改变切入点判断 陈晨 8.11
								iHttp.whetherChange=false;
								// TODO Auto-generated method stub
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									String str;

									@Override
									public void run() {
										String zhuma = zhuma_fc3d();
										// TODO Auto-generated method stub
										PublicMethod.myOutput("@@@@@@@@@"
												+ zhuma);
										// str=pay(zhuma,iZhuShu+"");
										// 投注新接口 陈晨 20100711 钱数计算有问题 2010/7/13
										// 陈晨
										str = payNew(zhuma, iQiShu + "",
												iZhuShu * iProgressBeishu * 200
														* iQiShu + "");
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

}