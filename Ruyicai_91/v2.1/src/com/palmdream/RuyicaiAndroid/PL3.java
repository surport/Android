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
import android.util.Log;
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
import com.palmdream.netintface.jrtLot;

/**
 * 
 * @author 王艳玲 排列三玩法操作
 * 
 */
public class PL3 extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
		MyDialogListener {

	// 小球图的宽高
	public static final int BALL_WIDTH = 29;
	// 顶部标签个数
	private int iButtonNum = 2;

	// 首行button
	private int iCurrentButton;

	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;

	private int defaultOffShade;
	private int defaultOnShade;

	int topButtonStringId[] = { R.string.zu_xuan, R.string.hezhi };
	int zuTopButtonStringId[] = { R.string.zuxuan3, R.string.zuxuan6 };
	int heTopButtonStringId[] = { R.string.hezhi_zhixuan, R.string.hezhi_zu3,
			R.string.hezhi_zu6 };
	int topButtonIdOn[] = { R.drawable.zuxuan_b, R.drawable.hezhi_b };
	int zuTopButtonIdOn[] = { R.drawable.zu3_b, R.drawable.zu6_b };
	int heTopButtonIdOn[] = { R.drawable.hezhizhixuan_b, R.drawable.hezhizu3_b,
			R.drawable.hezhizu6_b };
	int topButtonIdOff[] = { R.drawable.zuxuan, R.drawable.hezhi };
	int zuTopButtonIdOff[] = { R.drawable.zu3, R.drawable.zu6 };
	int heTopButtonIdOff[] = { R.drawable.hezhizhixuan, R.drawable.hezhizu3,
			R.drawable.hezhizu6 };

	// 排列三组3
	public static final int RED_PL3_ZU3_DANSHI_BAIWEI_START = 0x71000000;
	public static final int RED_PL3_ZU3_DANSHI_SHIWEI_START = 0x71000010;
	public static final int RED_PL3_ZU3_DANSHI_GEWEI_START = 0x71000020;
	public static final int RED_PL3_ZU3_FUSHI_START = 0x71000030;
	// 排列三组6
	public static final int RED_PL3_ZU6_START = 0x71000040;
	// 排列三直选
	public static final int RED_PL3_ZHIXUAN_BAIWEI_START = 0x71000070;
	public static final int RED_PL3_ZHIXUAN_SHIWEI_START = 0x71000080;
	public static final int RED_PL3_ZHIXUAN_GEWEI_START = 0x71000090;
	// 排列三和值
	public static final int RED_PL3_HEZHI_ZHIXUAN_START = 0x71000100;
	public static final int RED_PL3_HEZHI_ZU3_START = 0x71000200;
	public static final int RED_PL3_HEZHI_ZU6_START = 0x71000303;

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

	// 排列三组3单式前2位相同
	BallTable PL3A1Zu3DanshiBallTable = null;
	BallTable PL3A2Zu3DanshiBallTable = null;
	// 排列三组3单式第三位
	BallTable PL3BZu3DanshiBallTable = null;
	// 排列三组3复式
	BallTable PL3Zu3FushiBallTable = null;
	// 排列三组6单复式
	BallTable PL3Zu6BallTable = null;
	// 排列三直选
	BallTable PL3ZhixuanBaiweiBallTable = null;// 直选百位
	BallTable PL3ZhixuanShiweiBallTable = null;// 直选十位
	BallTable PL3ZhixuanGeweiBallTable = null;// 直选个位
	// 排列三和值
	BallTable PL3HezhiZhixuanBallTable = null;// 和值直选
	BallTable PL3HezhiZu3BallTable = null;// 和值组3
	BallTable PL3HezhiZu6BallTable = null;// 和值组6

	// 排列三和值三个二级tab按钮
	TextView PL3_hezhi_zhixuan;
	TextView PL3_hezhi_zu3;
	TextView PL3_hezhi_zu6;

	// 组3单复式
	boolean bZu3Danshi = true;
	boolean bZu3Fushi = false;

	private int PL3BallResId[] = { R.drawable.grey, R.drawable.red };

	// 排列三组3玩法按钮
	RadioGroup radiogroup;
	RadioButton danshirbtn;
	RadioButton fushirbtn;
	int redBallViewNum;
	int redBallViewWidth;
	LinearLayout linearLayout;

	Button zhixuanNewSelectbtn;
	Button zu3NewSelectbtn;
	Button zu6NewSelectbtn;
	Button hezhiZhixuanNewSelectbtn;
	Button hezhiZu3NewSelectbtn;
	Button hezhiZu6NewSelectbtn;

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	LinearLayout buyView;
	// 实现横纵的切换
	private BallHolderPL3 mBallHolder = null;
	private BallHolderPL3 tempBallHolder = null;
	private int tempCurrentButton;
	public int publicTopButton;
	public int tempCurrentWhich;
	public int type;
	public static final int ZHIXUAN = 0;
	public static final int ZUXUAN = 1;
	public static final int HEZHI = 2;
	private ImageButton touzhu;

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
				Toast
						.makeText(getBaseContext(), "投注失败余额不足！",
								Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示注册成功
				break;
			case 2:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束！", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 3:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功，出票成功！",
						Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示该号被暂停请联系客服
				// 清除高亮小球 陈晨 20100728
				if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
					PL3ZhixuanBaiweiBallTable.clearAllHighlights();
					PL3ZhixuanShiweiBallTable.clearAllHighlights();
					PL3ZhixuanGeweiBallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
					if (bZu3Danshi) {
						PL3A1Zu3DanshiBallTable.clearAllHighlights();
						PL3A2Zu3DanshiBallTable.clearAllHighlights();
						PL3BZu3DanshiBallTable.clearAllHighlights();
					}
					if (bZu3Fushi) {
						PL3Zu3FushiBallTable.clearAllHighlights();
					}
				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
					PL3Zu6BallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
					if (iWhich == 10) {
						PL3HezhiZhixuanBallTable.clearAllHighlights();
					}
					if (iWhich == 11) {
						PL3HezhiZu3BallTable.clearAllHighlights();
					}
					if (iWhich == 12) {
						PL3HezhiZu6BallTable.clearAllHighlights();
					}
				}
				break;
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注已受理！", Toast.LENGTH_LONG)
						.show();
				// 清除高亮小球 陈晨 20100728
				if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
					PL3ZhixuanBaiweiBallTable.clearAllHighlights();
					PL3ZhixuanShiweiBallTable.clearAllHighlights();
					PL3ZhixuanGeweiBallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
					if (bZu3Danshi) {
						PL3A1Zu3DanshiBallTable.clearAllHighlights();
						PL3A2Zu3DanshiBallTable.clearAllHighlights();
						PL3BZu3DanshiBallTable.clearAllHighlights();
					}
					if (bZu3Fushi) {
						PL3Zu3FushiBallTable.clearAllHighlights();
					}
				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
					PL3Zu6BallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
					if (iWhich == 10) {
						PL3HezhiZhixuanBallTable.clearAllHighlights();
					}
					if (iWhich == 11) {
						PL3HezhiZu3BallTable.clearAllHighlights();
					}
					if (iWhich == 12) {
						PL3HezhiZu6BallTable.clearAllHighlights();
					}
				}
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(PL3.this, UserLogin.class);
				startActivity(intentSession);
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 8:
				progressdialog.dismiss();
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
				touzhu.setImageResource(R.drawable.imageselecter);
				break;
			case 11:
				progressdialog.dismiss();// 未登录
				Intent intent1 = new Intent(UserLogin.UNSUCCESS);
				sendBroadcast(intent1);
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						PL3.this, "addInfo");
				shellRW.setUserLoginInfo("sessionid", "");
				Intent intent2 = new Intent(PL3.this, UserLogin.class);
				startActivity(intent2);
				break;
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
				switch (type) {
				case ZHIXUAN:
					finish();
					break;
				case ZUXUAN:
					type = ZHIXUAN;
					// ----- 初始化顶部按钮
					commit(iButtonNum, topButtonIdOn, topButtonIdOff);
					break;
				case HEZHI:
					type = ZHIXUAN;
					// ----- 初始化顶部按钮
					commit(iButtonNum, topButtonIdOn, topButtonIdOff);
					break;
				}
			}

		});
		TextView title = (TextView) findViewById(R.id.goucaitouzhu_title);
		title.setText(getResources().getString(R.string.pailie3));

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
		case PublicConst.BUY_PL3_ZHIXUAN:
			mBallHolder.ZhixuanBallGroup.iBeiShu = getBeishu();
			mBallHolder.ZhixuanBallGroup.iQiShu = getQishu();
			mBallHolder.ZhixuanBallGroup.bCheckBox = getCheckBox();
			break;
		case PublicConst.BUY_PL3_ZU3:
			mBallHolder.Zu3BallGroup.iBeiShu = getBeishu();
			mBallHolder.Zu3BallGroup.iQiShu = getQishu();
			mBallHolder.Zu3BallGroup.bCheckBox = getCheckBox();
			mBallHolder.Zu3BallGroup.bRadioBtnDanshi = getZu3DanshiRadioButton();
			mBallHolder.Zu3BallGroup.bRadioBtnFushi = getZu3FushiRadioButton();
			break;
		case PublicConst.BUY_PL3_ZU6:
			mBallHolder.Zu6BallGroup.iBeiShu = getBeishu();
			mBallHolder.Zu6BallGroup.iQiShu = getQishu();
			mBallHolder.Zu6BallGroup.bCheckBox = getCheckBox();
			break;
		case PublicConst.BUY_PL3_HEZHI:
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
			mBallHolder = new BallHolderPL3();
		}
		initButtons();
		// setCurrentTab(0);
		iCurrentButton = tempCurrentButton;

		createBuyView(iCurrentButton);
		// 切换之后，显示高亮的radioButton
		showHighLight();

		mBallHolder = tempBallHolder;
		iWhich = tempCurrentWhich;
		if (publicTopButton == PublicConst.BUY_PL3_ZHIXUAN) {
			create_PL3_ZHIXUAN();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_PL3_ZU3) {
			create_PL3_ZU3();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_PL3_ZU6) {
			create_PL3_ZU6();
			changeTextSumMoney(0);
		} else if (publicTopButton == PublicConst.BUY_PL3_HEZHI) {
			create_PL3_HEZHI();
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
		case PublicConst.BUY_PL3_ZHIXUAN:
			create_PL3_ZHIXUAN();
			break;
		case PublicConst.BUY_PL3_ZU3:
			create_PL3_ZU3();
			break;
		case PublicConst.BUY_PL3_ZU6:
			create_PL3_ZU6();
			break;
		case PublicConst.BUY_PL3_HEZHI:
			create_PL3_HEZHI();
			break;
		default:
			break;
		}
	}

	/**
	 * 排列三和值
	 */
	private void create_PL3_HEZHI() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout iV = (RelativeLayout) inflate.inflate(
				R.layout.layout_pl3_hezhi_tab, null);
		{
			PL3_hezhi_zhixuan = (TextView) iV.findViewById(R.id.hezhi_zhixuan);
			PL3_hezhi_zu3 = (TextView) iV.findViewById(R.id.hezhi_zu3);
			PL3_hezhi_zu6 = (TextView) iV.findViewById(R.id.hezhi_zu6);
			PL3_hezhi_zhixuan
					.setOnClickListener(new TextView.OnClickListener() {

						public void onClick(View v) {
							if (mBallHolder.flag != 1) {
								mBallHolder = new BallHolderPL3();
								create_PL3_HEZHI_ZHIXUAN();
								PL3_hezhi_zhixuan
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user));
								PL3_hezhi_zu3
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user_d));
								PL3_hezhi_zu6
										.setBackgroundDrawable(getResources()
												.getDrawable(
														R.drawable.frame_rectangle_user_d));
							}

						}

					});

			PL3_hezhi_zu3.setOnClickListener(new TextView.OnClickListener() {

				public void onClick(View v) {
					if (mBallHolder.flag != 1) {
						mBallHolder = new BallHolderPL3();
						create_PL3_HEZHI_ZU3();
						PL3_hezhi_zhixuan
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						PL3_hezhi_zu3.setBackgroundDrawable(getResources()
								.getDrawable(R.drawable.frame_rectangle_user));
						PL3_hezhi_zu6
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
					}
				}

			});

			PL3_hezhi_zu6.setOnClickListener(new TextView.OnClickListener() {

				public void onClick(View v) {
					if (mBallHolder.flag != 1) {
						mBallHolder = new BallHolderPL3();
						create_PL3_HEZHI_ZU6();
						PL3_hezhi_zhixuan
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						PL3_hezhi_zu3
								.setBackgroundDrawable(getResources()
										.getDrawable(
												R.drawable.frame_rectangle_user_d));
						PL3_hezhi_zu6.setBackgroundDrawable(getResources()
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
					PL3_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
					PL3_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					PL3_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					create_PL3_HEZHI_ZHIXUAN();
				}
				if (iWhich == 11) {
					create_PL3_HEZHI_ZU3();
					PL3_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					PL3_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
					PL3_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
				}
				if (iWhich == 12) {
					create_PL3_HEZHI_ZU6();
					PL3_hezhi_zhixuan.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					PL3_hezhi_zu3.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user_d));
					PL3_hezhi_zu6.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.frame_rectangle_user));
				}
			} else {
				PL3_hezhi_zhixuan.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user));
				PL3_hezhi_zu3.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.frame_rectangle_user_d));
				PL3_hezhi_zu6.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.frame_rectangle_user_d));
				create_PL3_HEZHI_ZHIXUAN();
			}
		}
	}

	/**
	 * 排列三和值组6
	 */
	private void create_PL3_HEZHI_ZU6() {
		iWhich = 12;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.pl3_hezhi_zu3_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zu3_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zhixuan_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zhixuan_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zu6_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_pl3_hezhi_zu6, null);
			{

				int redBallViewNum = 22;
				int redBallViewWidth = PL3.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PL3HezhiZu6BallTable = makeBallTable(iV, R.id.table_hezhi_zu6,
						iScreenWidth, redBallViewNum, redBallViewWidth,
						PL3BallResId, RED_PL3_HEZHI_ZU6_START, 3);

				hezhiZu6NewSelectbtn = (Button) iV
						.findViewById(R.id.pl3_hezhi_zu6_newselected_btn);
				hezhiZu6NewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
										PL3.this, 6, PL3.this);
								iChooseNumberDialog.show();
							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zu6_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zu6_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zu6_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zu6_qihao);
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

				// ImageButton b_touzhu_ddd_zu6hezhi = (ImageButton) iV
				// .findViewById(R.id.b_touzhu_hezhi_zu6);
				// b_touzhu_ddd_zu6hezhi.setOnClickListener(new
				// OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// beginTouZhu();
				// }
				//
				// });
				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu6);
				touzhu.setOnClickListener(new OnClickListener() {
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
									ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
											PL3.this, 6, PL3.this);
									iChooseNumberDialog.show();
									hezhiZu6NewSelectbtn
											.setVisibility(View.VISIBLE);
								}
							} else {
								hezhiZu6NewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				PL3HezhiZu6BallTable
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
	 * 排列三和值组3
	 */
	private void create_PL3_HEZHI_ZU3() {
		iWhich = 11;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.pl3_hezhi_zhixuan_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zhixuan_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zu6_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zu6_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zu3_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_pl3_hezhi_zu3, null);
			{

				int redBallViewNum = 26;
				int redBallViewWidth = PL3.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PL3HezhiZu3BallTable = makeBallTable(iV, R.id.table_hezhi_zu3,
						iScreenWidth, redBallViewNum, redBallViewWidth,
						PL3BallResId, RED_PL3_HEZHI_ZU3_START, 1);

				hezhiZu3NewSelectbtn = (Button) iV
						.findViewById(R.id.pl3_hezhi_zu3_newselected_btn);
				hezhiZu3NewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
										PL3.this, 5, PL3.this);
								iChooseNumberDialog.show();
							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zu3_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zu3_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zu3_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zu3_qihao);
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

				// ImageButton b_touzhu_ddd_hezhizu3 = (ImageButton) iV
				// .findViewById(R.id.b_touzhu_hezhi_zu3);
				// b_touzhu_ddd_hezhizu3.setOnClickListener(new
				// OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// beginTouZhu();
				// }
				//
				// });
				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu3);
				touzhu.setOnClickListener(new OnClickListener() {
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
									ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
											PL3.this, 5, PL3.this);
									iChooseNumberDialog.show();
									hezhiZu3NewSelectbtn
											.setVisibility(View.VISIBLE);
								}
							} else {
								hezhiZu3NewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				PL3HezhiZu3BallTable
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
	 * 排列三和值直选
	 */
	private void create_PL3_HEZHI_ZHIXUAN() {
		iWhich = 10;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		if (buyView.findViewById(R.id.pl3_hezhi_zu3_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zu3_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zu6_linearlayout) != null) {
			buyView.removeView(buyView
					.findViewById(R.id.pl3_hezhi_zu6_linearlayout));
		}
		if (buyView.findViewById(R.id.pl3_hezhi_zhixuan_linearlayout) == null) {

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout iV = (LinearLayout) inflate.inflate(
					R.layout.layout_pl3_hezhi_zhixuan, null);
			{

				int redBallViewNum = 28;
				int redBallViewWidth = PL3.BALL_WIDTH;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PL3HezhiZhixuanBallTable = makeBallTable(iV,
						R.id.table_hezhi_zhixuan, iScreenWidth, redBallViewNum,
						redBallViewWidth, PL3BallResId,
						RED_PL3_HEZHI_ZHIXUAN_START, 0);

				hezhiZhixuanNewSelectbtn = (Button) iV
						.findViewById(R.id.pl3_hezhi_zhixuan_newselected_btn);
				hezhiZhixuanNewSelectbtn
						.setOnClickListener(new Button.OnClickListener() {

							@Override
							public void onClick(View v) {
								ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
										PL3.this, 4, PL3.this);
								iChooseNumberDialog.show();
							}

						});

				ImageButton subtractBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zhixuan_beishu);
				subtractBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(--iProgressBeishu);
							}

						});

				ImageButton addBeishuBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zhixuan_beishu);
				addBeishuBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarBeishu.setProgress(++iProgressBeishu);
							}

						});

				ImageButton subtractQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_subtract_hezhi_zhixuan_qihao);
				subtractQihaoBtn
						.setOnClickListener(new ImageButton.OnClickListener() {

							@Override
							public void onClick(View v) {
								mSeekBarQishu.setProgress(--iProgressQishu);
							}

						});

				ImageButton addQihaoBtn = (ImageButton) iV
						.findViewById(R.id.pl3_seekbar_add_hezhi_zhixuan_qihao);
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
				// ImageButton b_touzhu_ddd_zhixuanhezhi = (ImageButton) iV
				// .findViewById(R.id.b_touzhu_hezhi_zhixuan);
				// b_touzhu_ddd_zhixuanhezhi
				// .setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View v) {
				// beginTouZhu();
				// }
				//
				// });
				touzhu = (ImageButton) iV
						.findViewById(R.id.b_touzhu_hezhi_zhixuan);
				touzhu.setOnClickListener(new OnClickListener() {
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
									ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
											PL3.this, 4, PL3.this);
									iChooseNumberDialog.show();
									hezhiZhixuanNewSelectbtn
											.setVisibility(View.VISIBLE);
								}
							} else {
								hezhiZhixuanNewSelectbtn
										.setVisibility(View.INVISIBLE);
							}
						}
					});
			// 当屏幕切换的时候绘图以后，把已经高亮的小球显示出来
			if (mBallHolder.flag == 1) {
				PL3HezhiZhixuanBallTable
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
	 * 排列三直选
	 */
	private void create_PL3_ZHIXUAN() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_pl3_zhixuan, null);
		{
			int redBallViewNum = 10;
			int redBallViewWidth = PL3.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3ZhixuanBaiweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_baiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZHIXUAN_BAIWEI_START, 0);
			PL3ZhixuanShiweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_shiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZHIXUAN_SHIWEI_START, 0);
			PL3ZhixuanGeweiBallTable = makeBallTable(iV,
					R.id.table_zhixuan_gewei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZHIXUAN_GEWEI_START, 0);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_zhixuan);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

			ImageButton subtractBeishuBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_subtract_zhixuan_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_add_zhixuan_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_subtract_zhixuan_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_add_zhixuan_qihao);
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
					.findViewById(R.id.pl3_zhixuan_newselected_btn);
			zhixuanNewSelectbtn
					.setOnClickListener(new Button.OnClickListener() {

						@Override
						public void onClick(View v) {
							ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
									PL3.this, 1, PL3.this);
							iChooseNumberDialog.show();
						}

					});

			// ImageButton b_touzhu_ddd_zhixuan = (ImageButton) iV
			// .findViewById(R.id.b_touzhu_zhixuan);
			// b_touzhu_ddd_zhixuan
			// .setOnClickListener(new ImageButton.OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// // 王艳玲 7.8 加menu
			// beginTouZhu();
			// }
			//
			// });
			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_zhixuan);
			touzhu.setOnClickListener(new ImageButton.OnClickListener() {
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
								ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
										PL3.this, 1, PL3.this);
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
			PL3ZhixuanBaiweiBallTable
					.changeBallStateConfigChange(mBallHolder.ZhixuanBallGroup.iZhixuanBaiweiBallStatus);
			PL3ZhixuanShiweiBallTable
					.changeBallStateConfigChange(mBallHolder.ZhixuanBallGroup.iZhixuanShiweiBallStatus);
			PL3ZhixuanGeweiBallTable
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
	 * 排列三组6
	 */
	private void create_PL3_ZU6() {
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_pl3_zu6, null);
		{
			int redBallViewNum = 10;
			int redBallViewWidth = PL3.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3Zu6BallTable = makeBallTable(iV, R.id.table_zu6, iScreenWidth,
					redBallViewNum, redBallViewWidth, PL3BallResId,
					RED_PL3_ZU6_START, 0);

			zu6NewSelectbtn = (Button) iV
					.findViewById(R.id.pl3_zu6_newselected_btn);
			zu6NewSelectbtn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
							PL3.this, 3, PL3.this);
					iChooseNumberDialog.show();
				}

			});

			ImageButton subtractBeishuBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_subtract_zu6_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_add_zu6_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_subtract_zu6_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) iV
					.findViewById(R.id.pl3_seekbar_add_zu6_qihao);
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
			// ImageButton b_ddd_zu6_touzhu = (ImageButton) iV
			// .findViewById(R.id.b_touzhu_zu6);
			// b_ddd_zu6_touzhu.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// beginTouZhu();
			// }
			//
			// });
			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_zu6);
			touzhu.setOnClickListener(new OnClickListener() {

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
								ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
										PL3.this, 3, PL3.this);
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
			PL3Zu6BallTable
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
	 * 排列三组3
	 */
	private void create_PL3_ZU3() {
		// 王艳玲 7.6 点击投注按钮，提示条件不明确
		bZu3Danshi = true;
		bZu3Fushi = false;
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inflate.inflate(R.layout.layout_pl3_zu3,
				null);
		{
			redBallViewNum = 10;
			int redBallViewWidth = PL3.BALL_WIDTH;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3A1Zu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_baiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZU3_DANSHI_BAIWEI_START, 0);
			PL3A2Zu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_shiwei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZU3_DANSHI_SHIWEI_START, 0);
			PL3BZu3DanshiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_danshi_gewei, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId,
					RED_PL3_ZU3_DANSHI_GEWEI_START, 0);
			PL3Zu3FushiBallTable = makeBallTable(linearLayout,
					R.id.table_zu3_fushi, iScreenWidth, redBallViewNum,
					redBallViewWidth, PL3BallResId, RED_PL3_ZU3_FUSHI_START, 0);
			// 默认是单式，所以复式不可用
			for (int i = 0; i < redBallViewNum; i++) {
				PL3Zu3FushiBallTable.ballViewVector.elementAt(i).setGrey();
				PL3Zu3FushiBallTable.ballViewVector.elementAt(i).setEnabled(
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
									mBallHolder = new BallHolderPL3();
									bZu3Danshi = true;
									bZu3Fushi = false;
									for (int i = 0; i < redBallViewNum; i++) {
										mTextSumMoney
												.setText(getResources()
														.getString(
																R.string.please_choose_number));
										PL3A1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										PL3A2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										PL3BZu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
										PL3Zu3FushiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										PL3Zu3FushiBallTable.ballViewVector
												.elementAt(i).setGrey();
									}
								}
							}
							if (checkedId == fushirbtn.getId()) {
								if (mBallHolder.flag != 1) {
									mBallHolder = new BallHolderPL3();
									bZu3Danshi = false;
									bZu3Fushi = true;
									for (int i = 0; i < redBallViewNum; i++) {
										mTextSumMoney
												.setText(getResources()
														.getString(
																R.string.please_choose_number));
										PL3A1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										PL3A2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										PL3BZu3DanshiBallTable.ballViewVector
												.elementAt(i).setEnabled(false);
										PL3A1Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										PL3A2Zu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										PL3BZu3DanshiBallTable.ballViewVector
												.elementAt(i).setGrey();
										PL3Zu3FushiBallTable.ballViewVector
												.elementAt(i).setEnabled(true);
									}
								}
							}
						}

					});

			ImageButton subtractBeishuBtn = (ImageButton) linearLayout
					.findViewById(R.id.pl3_seekbar_subtract_zu3_beishu);
			subtractBeishuBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarBeishu.setProgress(--iProgressBeishu);
						}

					});

			ImageButton addBeishuBtn = (ImageButton) linearLayout
					.findViewById(R.id.pl3_seekbar_add_zu3_beishu);
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

				@Override
				public void onClick(View v) {
					mSeekBarBeishu.setProgress(++iProgressBeishu);
				}

			});

			ImageButton subtractQihaoBtn = (ImageButton) linearLayout
					.findViewById(R.id.pl3_seekbar_subtract_zu3_qihao);
			subtractQihaoBtn
					.setOnClickListener(new ImageButton.OnClickListener() {

						@Override
						public void onClick(View v) {
							mSeekBarQishu.setProgress(--iProgressQishu);
						}

					});

			ImageButton addQihaoBtn = (ImageButton) linearLayout
					.findViewById(R.id.pl3_seekbar_add_zu3_qihao);
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
					.findViewById(R.id.pl3_zu3_newselected_btn);
			zu3NewSelectbtn.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (bZu3Danshi) {
						if (mBallHolder.flag != 1) {
							mBallHolder = new BallHolderPL3();
							PL3A1Zu3DanshiBallTable.clearAllHighlights();
							PL3A2Zu3DanshiBallTable.clearAllHighlights();
							PL3BZu3DanshiBallTable.clearAllHighlights();

							// 前两位相同，第三位不同 获取随机数放到randomChooseConfigChangePL3
							int[] randomNums = PublicMethod
									.getRandomsWithoutCollision(2, 0, 9);// 获得2位从0~9的随机数
							mBallHolder = PL3A1Zu3DanshiBallTable
									.randomChooseConfigChangePL3(1,
											mBallHolder, 0, randomNums);
							mBallHolder = PL3A2Zu3DanshiBallTable
									.randomChooseConfigChangePL3(1,
											mBallHolder, 1, randomNums);
							mBallHolder = PL3BZu3DanshiBallTable
									.randomChooseConfigChangePL3(1,
											mBallHolder, 2, randomNums);
							changeTextSumMoney(0);
						}
					}

					if (bZu3Fushi) {
						if (mBallHolder.flag != 1) {
							ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
									PL3.this, 2, PL3.this);
							iChooseNumberDialog.show();
						}
					}

				}

			});

			// ImageButton b_ddd_zu3 = (ImageButton) linearLayout
			// .findViewById(R.id.b_touzhu_zu3);
			// b_ddd_zu3.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// beginTouZhu();
			// }
			//
			// });
			touzhu = (ImageButton) linearLayout.findViewById(R.id.b_touzhu_zu3);
			touzhu.setOnClickListener(new OnClickListener() {

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
									mBallHolder = new BallHolderPL3();
									PL3A1Zu3DanshiBallTable
											.clearAllHighlights();
									PL3A2Zu3DanshiBallTable
											.clearAllHighlights();
									PL3BZu3DanshiBallTable.clearAllHighlights();

									// 前两位相同，第三位不同
									// 获取随机数放到randomChooseConfigChangePL3
									int[] randomNums = PublicMethod
											.getRandomsWithoutCollision(2, 0, 9);// 获得2位从0~9的随机数
									mBallHolder = PL3A1Zu3DanshiBallTable
											.randomChooseConfigChangePL3(1,
													mBallHolder, 0, randomNums);
									mBallHolder = PL3A2Zu3DanshiBallTable
											.randomChooseConfigChangePL3(1,
													mBallHolder, 1, randomNums);
									mBallHolder = PL3BZu3DanshiBallTable
											.randomChooseConfigChangePL3(1,
													mBallHolder, 2, randomNums);
									changeTextSumMoney(0);
								}
							}
							if (bZu3Fushi) {
								if (mBallHolder.flag != 1) {
									ChooseNumberDialogPL3 iChooseNumberDialog = new ChooseNumberDialogPL3(
											PL3.this, 2, PL3.this);
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
				PL3A1Zu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3A1BallStatus);
				PL3A2Zu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3A2BallStatus);
				PL3BZu3DanshiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3BBallStatus);
				PL3Zu3FushiBallTable.clearAllHighlights();
			}
			if (mBallHolder.Zu3BallGroup.bRadioBtnFushi) {
				PL3Zu3FushiBallTable
						.changeBallStateConfigChange(mBallHolder.Zu3BallGroup.iZu3FushiBallStatus);
				PL3A1Zu3DanshiBallTable.clearAllHighlights();
				PL3A2Zu3DanshiBallTable.clearAllHighlights();
				PL3BZu3DanshiBallTable.clearAllHighlights();
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
		commit(iButtonNum, topButtonIdOn, topButtonIdOff);
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
	public void commit(int iButtonNum, int[] topButtonIdOn, int[] topButtonOff) {
		topButtonGroup.removeAllViews();
		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width;
		width = screen_width / iButtonNum;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		// topButtonLayoutParams = new RadioGroup.LayoutParams(width,
		// RadioGroup.LayoutParams.WRAP_CONTENT);
		topButtonLayoutParams = new RadioGroup.LayoutParams(96,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < iButtonNum; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			// tabButton.setState(getResources().getString(topButtonStringId[i]));
			tabButton.setState(topButtonIdOn[i], topButtonOff[i]);
			tabButton.setId(i);
			tabButton.setGravity(Gravity.CENTER);
			topButtonGroup.addView(tabButton, i, topButtonLayoutParams);
		}

		if (getLastNonConfigurationInstance() != null) {
			mBallHolder = (BallHolderPL3) getLastNonConfigurationInstance();
			int buttonGroup = mBallHolder.topButtonGroup;
			setCurrentTab(buttonGroup);
		} else {
			mBallHolder = new BallHolderPL3();
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
		// topButtonGroup.check(index);
		// switch (index) {
		// case 0:
		// iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
		// createBuyView(iCurrentButton);
		// break;
		// case 1:
		// iCurrentButton = PublicConst.BUY_PL3_ZU3;
		// createBuyView(iCurrentButton);
		// break;
		// case 2:
		// iCurrentButton = PublicConst.BUY_PL3_ZU6;
		// createBuyView(iCurrentButton);
		// break;
		// case 3:
		// iCurrentButton = PublicConst.BUY_PL3_HEZHI;
		// createBuyView(iCurrentButton);
		// break;
		// }
		if (type == ZHIXUAN) {
			iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
			createBuyView(iCurrentButton);
		} else if (type == ZUXUAN) {
			topButtonGroup.check(1);// 组3
			iCurrentButton = PublicConst.BUY_PL3_ZU6;
			createBuyView(iCurrentButton);
		} else if (type == HEZHI) {// 和值-直选
			topButtonGroup.check(index);
			// create_FC3D_HEZHI_ZHIXUAN();
			iCurrentButton = PublicConst.BUY_PL3_HEZHI;
			createBuyView(iCurrentButton);
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

		mBallHolder = new BallHolderPL3();
		if (type == ZHIXUAN) {
			switch (checkedId) {
			// 组选
			case 0:
				type = ZUXUAN;
				// ----- 初始化顶部按钮
				initTopButtons();
				commit(2, zuTopButtonIdOn, zuTopButtonIdOff);

				break;
			// 和值
			case 1:
				type = HEZHI;
				// ----- 初始化顶部按钮
				commit(3, heTopButtonIdOn, heTopButtonIdOff);
				break;
			}
		} else if (type == ZUXUAN) {
			switch (checkedId) {
			// 组三
			case 0:
				iCurrentButton = PublicConst.BUY_PL3_ZU3;
				createBuyView(iCurrentButton);
				break;
			// 组六
			case 1:
				iCurrentButton = PublicConst.BUY_PL3_ZU6;
				createBuyView(iCurrentButton);
				break;
			}
		} else if (type == HEZHI) {
			switch (checkedId) {
			// 和值-直选
			case 0:
				create_PL3_HEZHI_ZHIXUAN();
				break;
			// 和值-组3
			case 1:
				create_PL3_HEZHI_ZU3();
				break;
			// 和值-组6
			case 2:
				create_PL3_HEZHI_ZU6();
				break;
			}
		}
		// switch (checkedId) {
		// // 直选
		// case 0:
		// iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
		// createBuyView(iCurrentButton);
		// break;
		// // 组3
		// case 1:
		// iCurrentButton = PublicConst.BUY_PL3_ZU3;
		// createBuyView(iCurrentButton);
		// break;
		// // 组6
		// case 2:
		// iCurrentButton = PublicConst.BUY_PL3_ZU6;
		// createBuyView(iCurrentButton);
		// break;
		// // 和值
		// case 3:
		// iCurrentButton = PublicConst.BUY_PL3_HEZHI;
		// createBuyView(iCurrentButton);
		// break;
		// }
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
		if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
			// 排列三和值直选
			if (v.getId() < RED_PL3_HEZHI_ZU3_START
					&& v.getId() >= RED_PL3_HEZHI_ZHIXUAN_START) {
				int iBallViewId = v.getId() - RED_PL3_HEZHI_ZHIXUAN_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 10, iBallViewId);
				}
				if (PL3HezhiZhixuanBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(10);
				}

			}
			// 排列三和值组3
			if (v.getId() < RED_PL3_HEZHI_ZU6_START
					&& v.getId() >= RED_PL3_HEZHI_ZU3_START) {
				int iBallViewId = v.getId() - RED_PL3_HEZHI_ZU3_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 11, iBallViewId);
				}
				if (PL3HezhiZu3BallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(11);
				}
			}
			// 排列三和值组6
			if (v.getId() >= RED_PL3_HEZHI_ZU6_START) {
				int iBallViewId = v.getId() - RED_PL3_HEZHI_ZU6_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 12, iBallViewId);
				}
				if (PL3HezhiZu6BallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(12);
				}
			}

		} else {
			// 排列三组3单式第一组
			if (v.getId() < RED_PL3_ZU3_DANSHI_SHIWEI_START
					&& v.getId() >= RED_PL3_ZU3_DANSHI_BAIWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZU3_DANSHI_BAIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 0, iBallViewId);
				}
				if (PL3A1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3A2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3BZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 排列三组3单式第二组
			if (v.getId() < RED_PL3_ZU3_DANSHI_GEWEI_START
					&& v.getId() >= RED_PL3_ZU3_DANSHI_SHIWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZU3_DANSHI_SHIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 1, iBallViewId);
				}
				if (PL3A1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3A2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3BZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 排列三组3单式第三组
			if (v.getId() < RED_PL3_ZU3_FUSHI_START
					&& v.getId() >= RED_PL3_ZU3_DANSHI_GEWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZU3_DANSHI_GEWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 2, iBallViewId);
				}
				if (PL3A1Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3A2Zu3DanshiBallTable.getHighlightBallNums() == 0
						|| PL3BZu3DanshiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 排列三组3复式
			if (v.getId() < RED_PL3_ZU6_START
					&& v.getId() >= RED_PL3_ZU3_FUSHI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZU3_FUSHI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 3, iBallViewId);
				}
				if (PL3Zu3FushiBallTable.getHighlightBallNums() >= 0
						&& PL3Zu3FushiBallTable.getHighlightBallNums() < 2) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 排列三组6
			if (v.getId() < RED_PL3_ZHIXUAN_BAIWEI_START
					&& v.getId() >= RED_PL3_ZU6_START) {
				int iBallViewId = v.getId() - RED_PL3_ZU6_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 4, iBallViewId);
				}
				if (PL3Zu6BallTable.getHighlightBallNums() >= 0
						&& PL3Zu6BallTable.getHighlightBallNums() < 3) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}

			// 排列三直选百位
			if (v.getId() < RED_PL3_ZHIXUAN_SHIWEI_START
					&& v.getId() >= RED_PL3_ZHIXUAN_BAIWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZHIXUAN_BAIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 7, iBallViewId);
				}
				if (PL3ZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}

			}
			// 排列三十位
			if (v.getId() < RED_PL3_ZHIXUAN_GEWEI_START
					&& v.getId() >= RED_PL3_ZHIXUAN_SHIWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZHIXUAN_SHIWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 8, iBallViewId);
				}
				if (PL3ZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				} else {
					changeTextSumMoney(0);
				}
			}
			// 排列三个位
			if (v.getId() < RED_PL3_HEZHI_ZHIXUAN_START
					&& v.getId() >= RED_PL3_ZHIXUAN_GEWEI_START) {
				int iBallViewId = v.getId() - RED_PL3_ZHIXUAN_GEWEI_START;
				if (iBallViewId < 0) {
					return;
				} else {
					changeBuyViewByRule(iCurrentButton, 9, iBallViewId);
				}
				if (PL3ZhixuanBaiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanShiweiBallTable.getHighlightBallNums() == 0
						|| PL3ZhixuanGeweiBallTable.getHighlightBallNums() == 0) {
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
		// 排列三直选复式
		case PublicConst.BUY_PL3_ZHIXUAN:
			iReturnValue = PL3ZhixuanBaiweiBallTable.getHighlightBallNums()
					* PL3ZhixuanShiweiBallTable.getHighlightBallNums()
					* PL3ZhixuanGeweiBallTable.getHighlightBallNums();
			break;

		// 排列三组3复式
		case PublicConst.BUY_PL3_ZU3:
			// 添加组3单式注数获取20100912 陈晨
			if (bZu3Danshi) {
				// 单式注数永为1
				iReturnValue = 1;
			}
			// 组3 复式注数的获取 20100912 陈晨
			if (bZu3Fushi) {
				int iZu3Balls = PL3Zu3FushiBallTable.getHighlightBallNums();
				iReturnValue = (int) getPL3Zu3FushiZhushu(iZu3Balls);
			}
			break;
		// 排列三组6复式
		case PublicConst.BUY_PL3_ZU6:
			int iZu6Balls = PL3Zu6BallTable.getHighlightBallNums();
			iReturnValue = (int) getPL3Zu6FushiZhushu(iZu6Balls);
			break;
		case PublicConst.BUY_PL3_HEZHI:
			// 排列三和值直选
			if (iWhich == 10) {
				iReturnValue = getPL3ZhixuanHezhiZhushu();
			} else if (iWhich == 11) {
				iReturnValue = getPL3Zu3HezhiZhushu();
			} else if (iWhich == 12) {
				iReturnValue = getPL3Zu6HezhiZhushu();
			}
			break;
		default:
			break;
		}
		return iReturnValue;
	}

	// wangyl 8.11 暂不支持期数
	/**
	 * 获得各种玩法的期数
	 * 
	 * @return 返回期数
	 */
	private int getQiShu() {
		int iReturnValue = 0;
		iReturnValue = mSeekBarQishu.getProgress();
		/*
		 * switch (iCurrentButton) { // 排列三直选复式 case
		 * PublicConst.BUY_PL3_ZHIXUAN: iReturnValue =
		 * mSeekBarQishu.getProgress(); break;
		 * 
		 * // 排列三组3复式 case PublicConst.BUY_PL3_ZU3: iReturnValue =
		 * mSeekBarQishu.getProgress(); break; // 排列三组6复式 case
		 * PublicConst.BUY_PL3_ZU6: iReturnValue = mSeekBarQishu.getProgress();
		 * break; case PublicConst.BUY_PL3_HEZHI: // 排列三和值直选 if (iWhich == 10) {
		 * iReturnValue = mSeekBarQishu.getProgress(); } else if (iWhich == 11)
		 * { iReturnValue = mSeekBarQishu.getProgress(); } else if (iWhich ==
		 * 12) { iReturnValue = mSeekBarQishu.getProgress(); } break; default:
		 * break; }
		 */
		return iReturnValue;
	}

	/**
	 * 获得排列三组6复式注数
	 * 
	 * @param iZu6balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getPL3Zu6FushiZhushu(int iZu6balls) {
		long tempzhushu = 0l;
		if (iZu6balls > 0) {
			tempzhushu += PublicMethod.zuhe(3, iZu6balls);
		}
		return tempzhushu;

	}

	/**
	 * 获得排列三组3复式注数
	 * 
	 * @param iZu3balls
	 *            选择小球个数
	 * @return 返回注数
	 */
	private long getPL3Zu3FushiZhushu(int iZu3balls) {
		long tempzhushu = 0l;
		if (iZu3balls > 0) {
			tempzhushu += PublicMethod.zuhe(2, iZu3balls) * 2;
		}
		return tempzhushu;

	}

	/**
	 * 获得排列三直选和值注数
	 * 
	 * @return 返回注数
	 */
	private int getPL3ZhixuanHezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = PL3HezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球号码从1开始，故减去1
					iZhuShu += BallNoZhushus[j];
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得排列三组3和值注数
	 * 
	 * @return 返回注数
	 */
	private int getPL3Zu3HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = PL3HezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5, 5, 4, 5,
				5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					iZhuShu += BallNoZhushus[j];
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 获得排列三组6和值注数
	 * 
	 * @return 返回注数
	 */
	private int getPL3Zu6HezhiZhushu() {
		int iZhuShu = 0;
		int[] BallNos = PL3HezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
					iZhuShu += BallNoZhushus[j];
				}
			}
		}
		return iZhuShu;
	}

	/**
	 * 显示各种玩法的注数与金额
	 * 
	 * @param aWhichGroupBall
	 *            被选中的BallTable（主要针对排列三和值玩法，0为其他玩法，10为排列三和值直选，11为排列三组3和值组3，12为排列三和值组6
	 *            ）
	 */
	public void changeTextSumMoney(int aWhichGroupBall) {
		switch (iCurrentButton) {
		// 排列三直选
		case PublicConst.BUY_PL3_ZHIXUAN:
			if (PL3ZhixuanBaiweiBallTable.getHighlightBallNums() == 1
					&& PL3ZhixuanShiweiBallTable.getHighlightBallNums() == 1
					&& PL3ZhixuanGeweiBallTable.getHighlightBallNums() == 1) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为直选单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			} else if (PL3ZhixuanBaiweiBallTable.getHighlightBallNums() > 1
					|| PL3ZhixuanShiweiBallTable.getHighlightBallNums() > 1
					|| PL3ZhixuanGeweiBallTable.getHighlightBallNums() > 1) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为直选复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			break;
		// 排列三组3
		case PublicConst.BUY_PL3_ZU3:
			if (PL3A1Zu3DanshiBallTable.getHighlightBallNums() == 1
					&& PL3A2Zu3DanshiBallTable.getHighlightBallNums() == 1
					&& PL3BZu3DanshiBallTable.getHighlightBallNums() == 1) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组3单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			if (PL3Zu3FushiBallTable.getHighlightBallNums() > 1) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组3复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			break;
		// 排列三组6
		case PublicConst.BUY_PL3_ZU6:
			if (PL3Zu6BallTable.getHighlightBallNums() == 3) {
				int iZhuShu = iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组6单式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");
			}
			if (PL3Zu6BallTable.getHighlightBallNums() > 3) {
				int iZhuShu = getZhuShu() * iProgressBeishu;
				mTextSumMoney.setText("当前玩法为组6复式，共" + iZhuShu + "注，共"
						+ (iZhuShu * 2) + "元");

			}
			break;
		// 排列三和值
		case PublicConst.BUY_PL3_HEZHI:
			// 排列三和值直选
			if (aWhichGroupBall == 10) {
				int[] BallNos = PL3HezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
				int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55,
						63, 69, 73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15,
						10, 6, 3, 1 };// 0~27
				int iZhuShu = 0;
				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球号码从1开始，故减去1
							iZhuShu += BallNoZhushus[j];
							String temp = "当前玩法为和值直选，共" + iZhuShu
									* iProgressBeishu + "注，共"
									+ (iZhuShu * iProgressBeishu * 2) + "元";
							mTextSumMoney.setText(temp);
						}
					}
				}
			}
			// 排列三和值组3
			if (aWhichGroupBall == 11) {
				int[] BallNos = PL3HezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
				int[] BallNoZhushus = { 1, 2, 1, 3, 3, 3, 4, 5, 4, 5, 5, 4, 5,
						5, 4, 5, 5, 4, 5, 4, 3, 3, 3, 1, 2, 1 };// 1~26
				int iZhuShu = 0;
				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
							iZhuShu += BallNoZhushus[j];
							String temp = "当前玩法为和值组3，共" + iZhuShu
									* iProgressBeishu + "注，共"
									+ (iZhuShu * iProgressBeishu * 2) + "元";
							mTextSumMoney.setText(temp);
						}
					}
				}
			}
			// 排列三和值组6
			if (aWhichGroupBall == 12) {
				int[] BallNos = PL3HezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）
				int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10,
						10, 9, 8, 7, 5, 4, 3, 2, 1, 1 };// 3~24
				int iZhuShu = 0;
				for (int i = 0; i < BallNos.length; i++) {
					for (int j = 0; j < BallNoZhushus.length; j++) {
						if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
							iZhuShu += BallNoZhushus[j];
							String temp = "当前玩法为和值组6，共" + iZhuShu
									* iProgressBeishu + "注，共"
									+ (iZhuShu * iProgressBeishu * 2) + "元";
							mTextSumMoney.setText(temp);
						}
					}
				}
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
		case PublicConst.BUY_PL3_ZU3:
			buy_PL3_ZU3(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_PL3_ZU6:
			buy_PL3_ZU6(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_PL3_ZHIXUAN:
			buy_PL3_ZHIXUAN(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_PL3_HEZHI:
			// 当前iCurrentButton为BUY_PL3_HEZHI
			if (aWhichGroupBall == 10) {
				buy_PL3_HEZHI_ZHIXUAN(aWhichGroupBall, aBallViewId);
			}
			if (aWhichGroupBall == 11) {
				buy_PL3_HEZHI_ZU3(aWhichGroupBall, aBallViewId);
			}
			if (aWhichGroupBall == 12) {
				buy_PL3_HEZHI_ZU6(aWhichGroupBall, aBallViewId);
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
	private void buy_PL3_HEZHI_ZU6(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 12) { // 和值组6
			int iChosenBallSum = 9;
			int isHighLight = PL3HezhiZu6BallTable.changeBallState(
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
	private void buy_PL3_HEZHI_ZU3(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 11) { // 和值组3
			int iChosenBallSum = 9;
			int isHighLight = PL3HezhiZu3BallTable.changeBallState(
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
	private void buy_PL3_HEZHI_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 10) { // 和值直选
			int iChosenBallSum = 9;
			int isHighLight = PL3HezhiZhixuanBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.HezhiZhixuanBallGroup.iHezhiZhixuanBallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 排列三直选
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_PL3_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 7) { // 百位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = PL3ZhixuanBaiweiBallTable.changeBallState(
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
			int isHighLight = PL3ZhixuanShiweiBallTable.changeBallState(
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
			int isHighLight = PL3ZhixuanGeweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.ZhixuanBallGroup.iZhixuanGeweiBallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 排列三组6
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_PL3_ZU6(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 4) { // 组6
			int iChosenBallSum = 9;
			int isHighLight = PL3Zu6BallTable.changeBallState(iChosenBallSum,
					aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu6BallGroup.iZu6BallStatus[aBallViewId] = 1;
			} else {
				mBallHolder.Zu6BallGroup.iZu6BallStatus[aBallViewId] = 0;
			}
		}
	}

	/**
	 * 排列三组3
	 * 
	 * @param aWhichGroupBall
	 *            第几组小球
	 * @param aBallViewId
	 *            被click小球的id，从0开始计数，小球上显示的数字为id
	 */
	private void buy_PL3_ZU3(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = PL3A1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = PL3A2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 1;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 1;
				PL3BZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = PL3A1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = PL3A2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 1;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 1;
				PL3BZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 2) {
			int iChosenBallSum = 1;
			// 组3,前两组相同，第三组不能与之相同

			int isHighLight = PL3BZu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 1;
				PL3A1Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				PL3A2Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				mBallHolder.Zu3BallGroup.iZu3A1BallStatus[aBallViewId] = 0;
				mBallHolder.Zu3BallGroup.iZu3A2BallStatus[aBallViewId] = 0;

			} else {
				mBallHolder.Zu3BallGroup.iZu3BBallStatus[aBallViewId] = 0;
			}
		} else if (aWhichGroupBall == 3) {
			int iChosenBallSum = 10;
			int isHighLight = PL3Zu3FushiBallTable.changeBallState(
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
	 * @param int aBallViewText 0:小球从0开始显示,1:小球从1开始显示 ,3小球从3开始显示(排列三和值组6从3开始)
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
		menuInflater.inflate(R.menu.menu_pl3, menu);
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
		case R.id.pl3_confirm:
			beginTouZhu();
			break;
		// 重新机选
		case R.id.pl3_reselect_num:
			beginReselect();
			break;
		// 玩法介绍
		case R.id.pl3_game_introduce:
			dialogGameIntroduction();
			break;
		// 取消
		case R.id.pl3_cancel:
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

		// 排列三直选
		if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
			PL3ZhixuanBaiweiBallTable.clearAllHighlights();
			PL3ZhixuanShiweiBallTable.clearAllHighlights();
			PL3ZhixuanGeweiBallTable.clearAllHighlights();
		}
		// 排列三组3
		if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
			PL3A1Zu3DanshiBallTable.clearAllHighlights();
			PL3A2Zu3DanshiBallTable.clearAllHighlights();
			PL3BZu3DanshiBallTable.clearAllHighlights();
			PL3Zu3FushiBallTable.clearAllHighlights();
		}
		// 排列三组6
		if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			PL3Zu6BallTable.clearAllHighlights();
		}
		// 排列三和值
		if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
			// 排列三和值直选
			if (iWhich == 10) {
				PL3HezhiZhixuanBallTable.clearAllHighlights();
			}
			// 排列三和值组3
			if (iWhich == 11) {
				PL3HezhiZu3BallTable.clearAllHighlights();
			}
			// 排列三和值组6
			if (iWhich == 12) {
				PL3HezhiZu6BallTable.clearAllHighlights();
			}
		}

	}

	/**
	 * 各种玩法的投注方法
	 */
	private void beginTouZhu() {
		// 周黎鸣 7.4 代码修改：添加登陆的判断
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(PL3.this,
				"addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		// 获取期数 陈晨 20100711
		int iQiShu = getQiShu();
		// 投注时判断是否登录
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(PL3.this, UserLogin.class);
			startActivity(intentSession);
		} else {
			// 排列三直选
			if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {

				int baiweiNums = PL3ZhixuanBaiweiBallTable
						.getHighlightBallNums();
				int shiweiNums = PL3ZhixuanShiweiBallTable
						.getHighlightBallNums();
				int geweiNums = PL3ZhixuanGeweiBallTable.getHighlightBallNums();
				int[] baiweis = PL3ZhixuanBaiweiBallTable.getHighlightBallNOs();
				int[] shiweis = PL3ZhixuanShiweiBallTable.getHighlightBallNOs();
				int[] geweis = PL3ZhixuanGeweiBallTable.getHighlightBallNOs();
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
					alertDialog("请选择号码", "请在百位，十位，个位均至少选择一个小球后再投注");
				} else if ((baiweiNums + shiweiNums + geweiNums) > 24) {
					alertDialog("投注失败", "小球个数不超过24个");
				} else {
					int iZhuShu = getZhuShu() * iProgressBeishu;
					if (iZhuShu / iProgressBeishu > 600) {
						alertDialog("投注失败", "请选择不大于600注投注");
					} else if (iZhuShu * 2 > 100000) {
						alertDialog("投注失败", "单笔投注不能大于100000元");
					} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

						alert("注码：" + "\n" + "   百位：" + baiweistr + "\n"
								+ "   十位：" + shiweistr + "\n" + "   个位："
								+ geweistr + "\n" + "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额：" + iZhuShu
								* 2 + "元" + "\n" + "冻结金额："
								+ (2 * (iProgressQishu - 1) * iZhuShu) + "元"
								+ "\n" + "确认支付吗？");
					}
				}

			}
			// 排列三组3
			if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {

				int iZhuShu = 0;
				if (bZu3Danshi) {
					int baiweiNums = PL3A1Zu3DanshiBallTable
							.getHighlightBallNums();
					int shiweiNums = PL3A2Zu3DanshiBallTable
							.getHighlightBallNums();
					int geweiNums = PL3BZu3DanshiBallTable
							.getHighlightBallNums();

					if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
						alertDialog("请选择号码", "请再百位，十位， 个位中均选择一个小球后再投注");
					} else if (baiweiNums == 1 && shiweiNums == 1
							&& geweiNums == 1) {
						iZhuShu = mSeekBarBeishu.getProgress();
						String baiweistr = PL3A1Zu3DanshiBallTable
								.getHighlightBallNOs()[0]
								- 1 + "";// 前2位相同
						String geweistr = PL3BZu3DanshiBallTable
								.getHighlightBallNOs()[0]
								- 1 + "";// 前2位相同
						if (iZhuShu * 2 > 100000) {
							alertDialog("投注失败", "单笔投注不能大于100000元");
						} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

							alert("注码：" + baiweistr + "." + baiweistr + "."
									+ geweistr + "\n" + "注数：" + 1 + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "确认支付吗？");
						}
					}
				}
				if (bZu3Fushi) {
					if (PL3Zu3FushiBallTable.getHighlightBallNums() < 2) {
						alertDialog("请选择号码", "请至少选择2个小球后再投注");
					} else {
						int[] fushiNums = PL3Zu3FushiBallTable
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
						if (iZhuShu * 2 > 100000) {
							alertDialog("投注失败", "单笔投注不能大于100000元");
						} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

							alert("注码：" + fushiStr + "\n" + "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "确认支付吗？");
						}
					}
				}

			}
			// 排列三组6
			if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {

				if (PL3Zu6BallTable.getHighlightBallNums() < 3) {
					alertDialog("请选择号码", "请至少选择3个小球后再投注");
				} else {
					int[] fushiNums = PL3Zu6BallTable.getHighlightBallNOs();
					String fushiStr = "";
					for (int i = 0; i < fushiNums.length; i++) {
						fushiStr += (fushiNums[i] - 1) + ".";
						if (i == fushiNums.length - 1) {
							fushiStr = fushiStr.substring(0,
									fushiStr.length() - 1);
						}
					}
					int iZhuShu = getZhuShu() * iProgressBeishu;

					if (iZhuShu * 2 > 100000) {
						alertDialog("投注失败", "单笔投注不能大于100000元");
					} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

						alert("注码：" + fushiStr + "\n" + "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" + "冻结金额："
								+ (2 * (iProgressQishu - 1) * iZhuShu) + "元"
								+ "\n" + "确认支付吗？");
					}
				}

			}
			// 排列三和值
			if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
				// 排列三和值直选
				if (iWhich == 10) {

					if (PL3HezhiZhixuanBallTable.getHighlightBallNums() < 1) {
						alertDialog("请选择号码", "请选择小球号码后再投注");
					} else if (PL3HezhiZhixuanBallTable.getHighlightBallNums() <= 9) {
						int iZhuShu = getZhuShu() * iProgressBeishu;
						String fushiStr = "";
						int[] zhuma_zhixuanhezhi = PL3HezhiZhixuanBallTable
								.getHighlightBallNOs();
						for (int i = 0; i < zhuma_zhixuanhezhi.length; i++) {
							fushiStr += (zhuma_zhixuanhezhi[i] - 1) + ",";
							if (i == zhuma_zhixuanhezhi.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						if (iZhuShu * 2 > 100000) {
							alertDialog("投注失败", "单笔投注不能大于100000元");
						} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

							alert("注码：" + fushiStr + "\n" + "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "确认支付吗？");
						}
					}

				}
				// 排列三和值组3
				if (iWhich == 11) {

					if (PL3HezhiZu3BallTable.getHighlightBallNums() < 1) {
						alertDialog("请选择号码", "请选择小球号码后再投注");
					} else if (PL3HezhiZu3BallTable.getHighlightBallNums() <= 9) {
						// 显示用户选中的信息
						int iZhuShu = getZhuShu() * iProgressBeishu;
						String fushiStr = "";
						int[] zhuma_zu3hezhi = PL3HezhiZu3BallTable
								.getHighlightBallNOs();
						for (int i = 0; i < zhuma_zu3hezhi.length; i++) {
							fushiStr += (zhuma_zu3hezhi[i]) + ",";
							if (i == zhuma_zu3hezhi.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						if (iZhuShu * 2 > 100000) {
							alertDialog("投注失败", "单笔投注不能大于100000元");
						} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

							alert("注码：" + fushiStr + "\n" + "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "确认支付吗？");
						}
					}

				}
				// 排列三和值组6
				if (iWhich == 12) {

					if (PL3HezhiZu6BallTable.getHighlightBallNums() < 1) {
						alertDialog("请选择号码", "请选择小球号码后再投注");
					} else if (PL3HezhiZu6BallTable.getHighlightBallNums() <= 9) {
						int iZhuShu = getZhuShu() * iProgressBeishu;
						String fushiStr = "";
						int[] zhuma_zu6hezhi = PL3HezhiZu6BallTable
								.getHighlightBallNOs();
						for (int i = 0; i < zhuma_zu6hezhi.length; i++) {
							fushiStr += (zhuma_zu6hezhi[i] + 2) + ",";
							if (i == zhuma_zu6hezhi.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						if (iZhuShu * 2 > 100000) {
							alertDialog("投注失败", "单笔投注不能大于100000元");
						} else if (jrtLot.ifPerfectIfo(this, sessionIdStr)) {

							alert("注码：" + fushiStr + "\n" + "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "确认支付吗？");
						}
					}

				}
			}
		}
	}

	@Override
	public void onOkClick(int[] aReturn) {
		mBallHolder = new BallHolderPL3();
		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZHIXUAN:
			mBallHolder = PL3ZhixuanBaiweiBallTable
					.randomChooseConfigChangePL3(aReturn[0], mBallHolder, 7,
							null);
			mBallHolder = PL3ZhixuanShiweiBallTable
					.randomChooseConfigChangePL3(aReturn[1], mBallHolder, 8,
							null);
			mBallHolder = PL3ZhixuanGeweiBallTable.randomChooseConfigChangePL3(
					aReturn[2], mBallHolder, 9, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_ZU3:
			mBallHolder = PL3Zu3FushiBallTable.randomChooseConfigChangePL3(
					aReturn[0], mBallHolder, 3, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_ZU6:
			mBallHolder = PL3Zu6BallTable.randomChooseConfigChangePL3(
					aReturn[0], mBallHolder, 4, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_HEZHI:
			if (iWhich == 10) {
				mBallHolder = PL3HezhiZhixuanBallTable
						.randomChooseConfigChangePL3(aReturn[0], mBallHolder,
								10, null);
				changeTextSumMoney(10);
				break;
			}
			if (iWhich == 11) {
				mBallHolder = PL3HezhiZu3BallTable.randomChooseConfigChangePL3(
						aReturn[0], mBallHolder, 11, null);
				changeTextSumMoney(11);
				break;
			}
			if (iWhich == 12) {
				mBallHolder = PL3HezhiZu6BallTable.randomChooseConfigChangePL3(
						aReturn[0], mBallHolder, 12, null);
				changeTextSumMoney(12);
				break;
			}

		default:
			break;
		}
	}

	// 使用新接口 陈晨 2010/7/11
	/**
	 * 投注新接口
	 * 
	 * @param bets注码
	 * @param count期数
	 * @param amount投注总金额
	 */
	protected String[] payNew(String betCode, String lotMulti, String amount,
			String qiShu) {
		// TODO Auto-generated method stub
		BettingInterface betting = new BettingInterface();
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");

		PublicMethod.myOutput("-------------touzhusessionid" + sessionid);
		PublicMethod.myOutput("-------------phonenum" + phonenum);

		String[] error_code = betting.BettingTC(phonenum, "T01002", betCode,
				lotMulti, amount, "", qiShu, sessionid);
		return error_code;
	}

	// 每种玩法的编号
	String zhixuan = "1|";// 直选
	String zuxuan = "6|";// 组选单式（包括组三和组六）
	String zu3fushi = "F3|";// 组三复式
	String zu6fushi = "F6|";// 组六复式
	String zxHHH = "S1|";// 直选和值
	String z3HHH = "S3|";// 组3和值
	String z6HHH = "S6|";// 组6和值

	/**
	 * 获取每一个种类的注码
	 * 
	 */
	private String zhuma_PL3() {
		String strZM = "";
		// 排列三直选玩法
		if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {

			int[] zhuma_baiwei = PL3ZhixuanBaiweiBallTable
					.getHighlightBallNOs();// 获得的号码需要减去1
			int[] zhuma_shiwei = PL3ZhixuanShiweiBallTable
					.getHighlightBallNOs();
			int[] zhuma_gewei = PL3ZhixuanGeweiBallTable.getHighlightBallNOs();

			if (zhuma_baiwei.length > 0 && zhuma_shiwei.length > 0
					&& zhuma_gewei.length > 0) {
				strZM = zhixuan;
				for (int i = 0; i < zhuma_baiwei.length; i++) {
					strZM += (zhuma_baiwei[i] - 1) + "";
				}
				strZM += ",";
				for (int i = 0; i < zhuma_shiwei.length; i++) {
					strZM += (zhuma_shiwei[i] - 1) + "";
				}
				strZM += ",";
				for (int i = 0; i < zhuma_gewei.length; i++) {
					strZM += (zhuma_gewei[i] - 1) + "";
				}
				PublicMethod.myOutput("------zhuma------" + strZM);
			}
		}
		// 排列三组三玩法
		else if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
			if (bZu3Danshi) {// 单式
				int[] zhuma_zu3danshi_A1 = PL3A1Zu3DanshiBallTable
						.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A2 = PL3A2Zu3DanshiBallTable
						.getHighlightBallNOs();
				int[] zhuma_zu3danshi_A3 = PL3BZu3DanshiBallTable
						.getHighlightBallNOs();
				if (zhuma_zu3danshi_A1.length > 0
						&& zhuma_zu3danshi_A2.length > 0
						&& zhuma_zu3danshi_A3.length > 0) {
					strZM = zuxuan + (zhuma_zu3danshi_A3[0] - 1) + ","
							+ (zhuma_zu3danshi_A1[0] - 1) + ","
							+ (zhuma_zu3danshi_A1[0] - 1);
				}
				PublicMethod.myOutput("------zhuma------" + strZM);
			}
			if (bZu3Fushi) {// 复式也叫组三包号
				strZM = zu3fushi;
				int[] zhuma_zu3fushi = PL3Zu3FushiBallTable
						.getHighlightBallNOs();
				if (zhuma_zu3fushi.length > 0) {
					for (int i = 1; i < zhuma_zu3fushi.length + 1; i++) {
						strZM += zhuma_zu3fushi[i - 1] - 1 + "";
					}
				}
				PublicMethod.myOutput("------zhuma------" + strZM);
			}
		}
		// 排列三组六玩法
		else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			int[] zhuma_zu6danfushi = PL3Zu6BallTable.getHighlightBallNOs();
			if (zhuma_zu6danfushi.length == 3) {// 单式
				strZM = zuxuan + (zhuma_zu6danfushi[0] - 1) + ","
						+ (zhuma_zu6danfushi[1] - 1) + ","
						+ (zhuma_zu6danfushi[2] - 1);
				PublicMethod.myOutput("------zhuma------" + strZM);
			}
			if (zhuma_zu6danfushi.length > 3) {// 复式也叫组六包号
				strZM = zu6fushi;
				for (int i = 1; i < zhuma_zu6danfushi.length + 1; i++) {
					strZM += zhuma_zu6danfushi[i - 1] - 1 + "";
				}
				PublicMethod.myOutput("------zhuma------" + strZM);
			}

		}
		// 排列三和值玩法
		else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
			// 判断是和值直选 还是和值组3 还是和值组6
			if (iWhich == 10) {
				strZM = zxHHH;
				int[] zhuma_zhixuanhezhi = PL3HezhiZhixuanBallTable
						.getHighlightBallNOs();
				for (int i = 0; i < zhuma_zhixuanhezhi.length; i++) {
					strZM += (zhuma_zhixuanhezhi[i] - 1) + ",";
					if (i == zhuma_zhixuanhezhi.length - 1) {
						strZM = strZM.substring(0, strZM.length() - 1);
					}
				}
				PublicMethod.myOutput("------zhuma------" + strZM);

			} else if (iWhich == 11) {
				strZM = z3HHH;
				int[] zhuma_zu3hezhi = PL3HezhiZu3BallTable
						.getHighlightBallNOs();
				for (int i = 0; i < zhuma_zu3hezhi.length; i++) {
					strZM += (zhuma_zu3hezhi[i]) + ",";
					if (i == zhuma_zu3hezhi.length - 1) {
						strZM = strZM.substring(0, strZM.length() - 1);
					}
				}
				PublicMethod.myOutput("------zhuma------" + strZM);

			} else if (iWhich == 12) {
				strZM = z6HHH;
				int[] zhuma_zu6hezhi = PL3HezhiZu6BallTable
						.getHighlightBallNOs();
				for (int i = 0; i < zhuma_zu6hezhi.length; i++) {
					strZM += (zhuma_zu6hezhi[i] + 2) + ",";
					if (i == zhuma_zu6hezhi.length - 1) {
						strZM = strZM.substring(0, strZM.length() - 1);
					}
				}
				PublicMethod.myOutput("------zhuma------" + strZM);
			}

		}
		return strZM;
	}

	/**
	 * 投注不满足条件时弹出的对话框
	 * 
	 * @param message
	 */
	private void alertDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(PL3.this);
		builder.setTitle(title);
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

	/**
	 * MENU里的玩法介绍
	 */
	private void dialogGameIntroduction() {

		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_pl3.html";
		webView.loadUrl(url);

		AlertDialog.Builder builder = new AlertDialog.Builder(PL3.this);
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
		// 排列三直选
		if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
			topButtonGroup.check(1);// 没有改变不触发事件，因为默认是0
			topButtonGroup.check(0);
			topButtonGroup.invalidate();
		}
		// 排列三组3
		if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
			topButtonGroup.check(1);
			topButtonGroup.invalidate();
		}
		// 排列三组6
		if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			topButtonGroup.check(2);
			topButtonGroup.invalidate();
		}
		// 排列三和值
		if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
			topButtonGroup.check(4);
			topButtonGroup.invalidate();
		}
	}

	/**
	 * @author WangYanling 记录每个页面及是否切换横纵屏 排列三和值横纵屏需要重新写，因为支持多个选号
	 *         ，所以未做处理，与3D不同，3D支持一个选号。
	 */
	public class BallHolderPL3 {
		// 排列三直选
		BallGroup ZhixuanBallGroup = new BallGroup();
		// 排列三组3
		BallGroup Zu3BallGroup = new BallGroup();
		// 排列三组6
		BallGroup Zu6BallGroup = new BallGroup();
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
		// 排列三直选
		int[] iZhixuanBaiweiBallStatus = new int[10];
		int[] iZhixuanShiweiBallStatus = new int[10];
		int[] iZhixuanGeweiBallStatus = new int[10];
		// 排列三组3
		int[] iZu3A1BallStatus = new int[10];
		int[] iZu3A2BallStatus = new int[10];
		int[] iZu3BBallStatus = new int[10];
		int[] iZu3FushiBallStatus = new int[10];
		boolean bRadioBtnDanshi;
		boolean bRadioBtnFushi;
		// 排列三组6
		int[] iZu6BallStatus = new int[10];
		// 排列三和值直选
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
								touzhu.setImageResource(R.drawable.touzhuup_n);
								touzhu.setClickable(false);

								iHttp.whetherChange = false;
								showDialog(DIALOG1_KEY);
								// TODO Auto-generated method stub
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									int iBeiShu = getBeishu();
									String[] strCode = null;

									@Override
									public void run() {
										String zhuma = zhuma_PL3();
										// TODO Auto-generated method stub
										PublicMethod.myOutput("@@@@@@@@@"
												+ zhuma);
										// str=pay(zhuma,iZhuShu+"");
										// 投注新接口 陈晨 20100711 钱数计算有问题 2010/7/13
										// 陈晨
										strCode = payNew(zhuma, iBeiShu + "",
												iZhuShu * iProgressBeishu * 2
														+ "", iQiShu + "");

										touzhu.setClickable(true);

										Message msg1 = new Message();
										msg1.what = 10;
										handler.sendMessage(msg1);

										if (strCode[0].equals("0000")
												&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 11;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")
												&& strCode[1].equals("000001")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040006")
												|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("002002")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										}
										// else if (str.equals("002002")) {
										// Message msg = new Message();
										// msg.what = 3;
										// handler.sendMessage(msg);
										// }
										else if (strCode[0].equals("00")
												&& strCode[1].equals("00")) {
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
			// progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}

}