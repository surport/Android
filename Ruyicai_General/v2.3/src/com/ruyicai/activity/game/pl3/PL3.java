package com.ruyicai.activity.game.pl3;

import java.util.Iterator;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.view.View.OnFocusChangeListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.game.fc3d.Fc3d;
import com.ruyicai.activity.game.qlc.QLC;
import com.ruyicai.activity.game.ssq.SsqActivity;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.activity.home.ScrollableTabActivity;
import com.ruyicai.dialog.Accoutdialog;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.net.JrtLot;
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

/**
 * 
 * @author 王艳玲 排列三玩法操作
 * 
 */
public class PL3 extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
		MyDialogListener {

	// 小球图的宽高
	public static final int BALL_WIDTH = 55;
	// 顶部标签个数
	private int iButtonNum = 4;

	// 首行button
	private int iCurrentButton;

	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;

	private int defaultOffShade;
	private int defaultOnShade;

	int topButtonIdOn[] = { R.drawable.goucai_pl3_b, R.drawable.zhixuan_pl3_b,
			R.drawable.zuxuan_pl3_b, R.drawable.hezhi_pl3_b };
	int zuTopButtonIdOn[] = { R.drawable.zu3_b, R.drawable.zu6_b };
	int heTopButtonIdOn[] = { R.drawable.hezhizhixuan_b, R.drawable.hezhizu3_b,
			R.drawable.hezhizu6_b };
	int dfTopButtonIdOn[] = { R.drawable.danshi_pl3 };

	int topButtonIdOff[] = { R.drawable.goucai_pl3, R.drawable.zhixuan_pl3,
			R.drawable.zuxuan_pl3, R.drawable.hezhi_pl3 };
	int zuTopButtonIdOff[] = { R.drawable.zu3, R.drawable.zu6 };
	int heTopButtonIdOff[] = { R.drawable.hezhizhixuan, R.drawable.hezhizu3,
			R.drawable.hezhizu6 };
	int dfTopButtonIdOff[] = { R.drawable.fushi_pl3 };

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

	// 组3单复式
	boolean bZu3Danshi = true;
	boolean bZu3Fushi = false;

	private int PL3BallResId[] = { R.drawable.grey, R.drawable.red };

	// 排列三组3玩法按钮

	int redBallViewNum;
	LinearLayout linearLayout;

	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	LinearLayout buyView;

	private int tempCurrentButton;
	public int publicTopButton;
	public int tempCurrentWhich;
	public int type;
	public static final int ZHIXUAN = 0;
	public static final int ZUXUAN = 1;
	public static final int HEZHI = 2;
	private ImageButton touzhu;
	// fulei
	private ImageButton newSelect;
	private String issue[] = new String[2];
	private TextView term;
	private TextView title;
	private TextView time;
	private EditText baiEdit;
	private EditText shiEdit;
	private EditText geEdit;
	private EditText hezhiEdit;
	// private Vector<Balls> balls = new Vector();
	private LinearLayout zhuView;
	private Spinner jixuanZhu;
	private boolean isZhiXuanJiXuan = false;
	private boolean isZu3JiXuan = true;
	private RadioGroup topZuButtonGroup;
	private RadioGroup topHeButtonGroup;
	private Vector<Balls> balls = new Vector();
	private final int ZU = 0;
	private final int HE = 1;
	private final int DF = 2;
	private final int TOP = 3;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;// 下拉框是否响应
	private ImageButton dfButton;
	private boolean isDanshi = true;
	// private SsqSensor sensor = new SsqSensor(this);
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
				if(isFinishing() == false){
					Accoutdialog.getInstance().createAccoutdialog(PL3.this,getResources().getString(
									R.string.goucai_Account_dialog_msg).toString());
				}
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
				if(isFinishing() == false){
				    PublicMethod.showDialog(PL3.this);
				}
				// //需要添加AlertDialog提示该号被暂停请联系客服
				// 清除高亮小球 陈晨 20100728
				if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
					if (isZhiXuanJiXuan) {
						zhuView.removeAllViews();
					} else {
						baiEdit.setText("");
						shiEdit.setText("");
						geEdit.setText("");
						PL3ZhixuanBaiweiBallTable.clearAllHighlights();
						PL3ZhixuanShiweiBallTable.clearAllHighlights();
						PL3ZhixuanGeweiBallTable.clearAllHighlights();
					}

				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
					if (bZu3Danshi) {
						if (isZu3JiXuan) {
							zhuView.removeAllViews();
						} else {
							baiEdit.setText("");
							shiEdit.setText("");
							geEdit.setText("");
							PL3A1Zu3DanshiBallTable.clearAllHighlights();
							PL3A2Zu3DanshiBallTable.clearAllHighlights();
							PL3BZu3DanshiBallTable.clearAllHighlights();
						}

					}
					if (bZu3Fushi) {
						hezhiEdit.setText("");
						PL3Zu3FushiBallTable.clearAllHighlights();
					}
				} else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
					hezhiEdit.setText("");
					PL3Zu6BallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_PL3_HEZHI) {
					if (iWhich == 10) {
						hezhiEdit.setText("");
						PL3HezhiZhixuanBallTable.clearAllHighlights();
					}
					if (iWhich == 11) {
						hezhiEdit.setText("");
						PL3HezhiZu3BallTable.clearAllHighlights();
					}
					if (iWhich == 12) {
						hezhiEdit.setText("");
						PL3HezhiZu6BallTable.clearAllHighlights();
					}
				}
				changeTextSumMoney(iWhich);
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				break;
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
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
				Intent intent2 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent2);
				PublicMethod.showDialog(PL3.this);
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(PL3.this, UserLogin.class);
				startActivity(intentSession);

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
			}
			//				
		}
	};

	/**
	 * Called when the activity is first created.
	 * 
	 */
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);

		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);

		// 初始化单复式按钮
		dfButton = (ImageButton) findViewById(R.id.danfushi);
		dfButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isDanshi) {
					dfButton.setBackgroundResource(R.drawable.danshi_pl3);
					bZu3Danshi = true;
					bZu3Fushi = false;
					iCurrentButton = PublicConst.BUY_PL3_ZU3;
					create_PL3_ZU3();
				} else {
					dfButton.setBackgroundResource(R.drawable.fushi_pl3);
					bZu3Danshi = false;
					bZu3Fushi = true;
					iCurrentButton = PublicConst.BUY_PL3_ZU3;
					create_PL3_ZU3();
				}
				isDanshi = !isDanshi;
			}
		});
		// 获取期号和截止时间
		JSONObject pl3LotnoInfo =PublicMethod.getCurrentLotnoBatchCode(Constants.LOTNO_PL3);

		if (pl3LotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				issue[0] = pl3LotnoInfo.getString("batchCode");
				issue[1] = pl3LotnoInfo.getString("endTime");
				term.setText("第" + this.issue[0] + "期");
				time.setText("截止时间：" + this.issue[1]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			issue[0] = "";
			issue[1] = "";
			PublicMethod.getIssue(Constants.LOTNO_PL3, term, time,new Handler());
		}

		mHScrollView = (ScrollView) findViewById(R.id.scroll_global);
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initTopButtons();
		iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
		createBuyView(iCurrentButton);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensor.stopAction();
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
			if (isZhiXuanJiXuan) {
				sensor.startAction();
				create_PL3_ZHIXUAN_JIXUAN();
			} else {
				sensor.stopAction();
				create_PL3_ZHIXUAN();
			}
			topButtonGroup.setSelected(false);
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			break;
		case PublicConst.BUY_PL3_ZU3:
			if (isZu3JiXuan) {
				sensor.startAction();
				create_PL3_ZU3_JIXUAN();
			} else {
				sensor.stopAction();
				bZu3Danshi = true;
				bZu3Fushi = false;
				create_PL3_ZU3();
			}
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			break;
		case PublicConst.BUY_PL3_ZU6:
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);

			create_PL3_ZU6();
			break;
		case PublicConst.BUY_PL3_HEZHI:
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
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
		recycleResources();
		sensor.stopAction();// 启动晃动机选
		buyView.removeAllViews();
		topHeButtonGroup.setVisibility(RadioGroup.VISIBLE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout iV = (RelativeLayout) inflate.inflate(
				R.layout.layout_pl3_hezhi_tab, null);

		// 将View加入主框架
		buyView.addView(iV, new RelativeLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		create_PL3_HEZHI_ZHIXUAN();
	}

	/**
	 * 排列三和值组6
	 */
	private void create_PL3_HEZHI_ZU6() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
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
				title.setText("排列三和值组6");
				int redBallViewNum = 22;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PL3HezhiZu6BallTable = PublicMethod.makeBallTable(iV,
						R.id.table_hezhi_zu6, iScreenWidth, redBallViewNum,
						PL3BallResId, RED_PL3_HEZHI_ZU6_START, 3, this, this);

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

				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu6);
				touzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
				newSelect = (ImageButton) iV.findViewById(R.id.pl3_new_select);
				newSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {// 重选
						PL3HezhiZu6BallTable.clearAllHighlights();
						hezhiEdit.setText("");
						mTextSumMoney.setText(getResources().getString(
								R.string.please_choose_number));
					}
				});
				final TextView hezhiText = (TextView) iV
						.findViewById(R.id.pl3_text_hezhi);
				hezhiEdit = (EditText) iV.findViewById(R.id.pl3_edit_hezhi);

				hezhiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							hezhiEdit
									.setBackgroundResource(R.drawable.hongkuang);
							hezhiText.setTextColor(Color.RED);
						} else {
							hezhiEdit
									.setBackgroundResource(R.drawable.huikuang);
							hezhiText.setTextColor(Color.BLACK);
						}
					}
				});
			}
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

		}
	}

	/**
	 * 排列三和值组3
	 */
	private void create_PL3_HEZHI_ZU3() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
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
				title.setText("排列三和值组3");
				int redBallViewNum = 26;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PL3HezhiZu3BallTable = PublicMethod.makeBallTable(iV,
						R.id.table_hezhi_zu3, iScreenWidth, redBallViewNum,
						PL3BallResId, RED_PL3_HEZHI_ZU3_START, 1, this, this);

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

				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu3);
				touzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
				newSelect = (ImageButton) iV.findViewById(R.id.pl3_new_select);
				newSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {// 重选
						PL3HezhiZu3BallTable.clearAllHighlights();
						hezhiEdit.setText("");
						mTextSumMoney.setText(getResources().getString(
								R.string.please_choose_number));
					}
				});
				final TextView hezhiText = (TextView) iV
						.findViewById(R.id.pl3_text_hezhi);
				hezhiEdit = (EditText) iV.findViewById(R.id.pl3_edit_hezhi);

				hezhiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						// TODO Auto-generated method stub
						if (hasFocus) {
							hezhiEdit
									.setBackgroundResource(R.drawable.hongkuang);
							hezhiText.setTextColor(Color.RED);
						} else {
							hezhiEdit
									.setBackgroundResource(R.drawable.huikuang);
							hezhiText.setTextColor(Color.BLACK);
						}
					}
				});
			}
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

		}
	}

	/**
	 * 排列三和值直选
	 */
	private void create_PL3_HEZHI_ZHIXUAN() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
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
			title.setText("排列三和值直选");
			int redBallViewNum = 28;

			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3HezhiZhixuanBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_hezhi_zhixuan, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_HEZHI_ZHIXUAN_START, 0, this, this);

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
			addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

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
			addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

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

			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zhixuan);
			touzhu.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			newSelect = (ImageButton) iV.findViewById(R.id.pl3_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					PL3HezhiZhixuanBallTable.clearAllHighlights();
					hezhiEdit.setText("");
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});

			final TextView hezhiText = (TextView) iV
					.findViewById(R.id.pl3_text_hezhi);
			hezhiEdit = (EditText) iV.findViewById(R.id.pl3_edit_hezhi);

			hezhiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						hezhiEdit.setBackgroundResource(R.drawable.hongkuang);
						hezhiText.setTextColor(Color.RED);
					} else {
						hezhiEdit.setBackgroundResource(R.drawable.huikuang);
						hezhiText.setTextColor(Color.BLACK);
					}
				}
			});

			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

		}
	}

	/**
	 * 排列三直选
	 */
	private void create_PL3_ZHIXUAN() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
		buyView.removeAllViews();
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_pl3_zhixuan, null);
		{
			title.setText("排列三直选");
			int redBallViewNum = 10;

			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3ZhixuanBaiweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_baiwei, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZHIXUAN_BAIWEI_START, 0, this, this);
			PL3ZhixuanShiweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_shiwei, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZHIXUAN_SHIWEI_START, 0, this, this);
			PL3ZhixuanGeweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_gewei, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZHIXUAN_GEWEI_START, 0, this, this);

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
			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_zhixuan);
			touzhu.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 王艳玲 7.8 加menu
					beginTouZhu();
				}

			});
			newSelect = (ImageButton) iV.findViewById(R.id.pl3_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					baiEdit.setText("");
					shiEdit.setText("");
					geEdit.setText("");
					PL3ZhixuanBaiweiBallTable.clearAllHighlights();
					PL3ZhixuanShiweiBallTable.clearAllHighlights();
					PL3ZhixuanGeweiBallTable.clearAllHighlights();
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final LinearLayout baiArea = (LinearLayout) iV
					.findViewById(R.id.pl3_bai_balls_layout);
			final LinearLayout shiArea = (LinearLayout) iV
					.findViewById(R.id.pl3_shi_balls_layout);
			final LinearLayout geArea = (LinearLayout) iV
					.findViewById(R.id.pl3_ge_balls_layout);
			shiArea.setVisibility(LinearLayout.GONE);
			geArea.setVisibility(LinearLayout.GONE);
			final TextView baiText = (TextView) iV
					.findViewById(R.id.pl3_text_bai);
			final TextView shiText = (TextView) iV
					.findViewById(R.id.pl3_text_shi);
			final TextView geText = (TextView) iV
					.findViewById(R.id.pl3_text_ge);
			baiEdit = (EditText) iV.findViewById(R.id.pl3_edit_bai);
			shiEdit = (EditText) iV.findViewById(R.id.pl3_edit_shi);
			geEdit = (EditText) iV.findViewById(R.id.pl3_edit_ge);

			baiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						baiEdit.setBackgroundResource(R.drawable.hongkuang);
						baiArea.setVisibility(LinearLayout.VISIBLE);
						shiArea.setVisibility(LinearLayout.GONE);
						geArea.setVisibility(LinearLayout.GONE);
						baiText.setTextColor(Color.RED);
					} else {
						baiEdit.setBackgroundResource(R.drawable.huikuang);
						baiText.setTextColor(Color.BLACK);
					}
				}
			});
			shiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						shiEdit.setBackgroundResource(R.drawable.hongkuang);
						shiArea.setVisibility(LinearLayout.VISIBLE);
						baiArea.setVisibility(LinearLayout.GONE);
						geArea.setVisibility(LinearLayout.GONE);
						shiText.setTextColor(Color.RED);
					} else {
						shiEdit.setBackgroundResource(R.drawable.huikuang);
						shiText.setTextColor(Color.BLACK);
					}

				}
			});
			geEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if (hasFocus) {
						geEdit.setBackgroundResource(R.drawable.hongkuang);
						geArea.setVisibility(LinearLayout.VISIBLE);
						shiArea.setVisibility(LinearLayout.GONE);
						baiArea.setVisibility(LinearLayout.GONE);
						geText.setTextColor(Color.RED);
					} else {
						geEdit.setBackgroundResource(R.drawable.huikuang);
						geText.setTextColor(Color.BLACK);
					}
				}
			});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/**
	 * 排列三组6
	 */
	private void create_PL3_ZU6() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
		buyView.removeAllViews();
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.VISIBLE);
		dfButton.setVisibility(ImageButton.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_pl3_zu6, null);
		{
			title.setText("排列三组6");
			int redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3Zu6BallTable = PublicMethod.makeBallTable(iV, R.id.table_zu6,
					iScreenWidth, redBallViewNum, PL3BallResId,
					RED_PL3_ZU6_START, 0, this, this);

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

			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_zu6);
			touzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			newSelect = (ImageButton) iV.findViewById(R.id.pl3_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					PL3Zu6BallTable.clearAllHighlights();
					hezhiEdit.setText("");
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final TextView hezhiText = (TextView) iV
					.findViewById(R.id.pl3_text_hezhi);
			hezhiEdit = (EditText) iV.findViewById(R.id.pl3_edit_hezhi);

			hezhiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						hezhiEdit.setBackgroundResource(R.drawable.hongkuang);
						hezhiText.setTextColor(Color.RED);
					} else {
						hezhiEdit.setBackgroundResource(R.drawable.huikuang);
						hezhiText.setTextColor(Color.BLACK);
					}
				}
			});
		}
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/**
	 * 排列三组3
	 */
	private void create_PL3_ZU3() {
		recycleResources();
		sensor.stopAction();// 启动晃动机选
		// 王艳玲 7.6 点击投注按钮，提示条件不明确
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.VISIBLE);
		dfButton.setVisibility(ImageButton.VISIBLE);
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inflate.inflate(R.layout.layout_pl3_zu3,
				null);
		{
			title.setText("排列三组3");
			redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);

			PL3A1Zu3DanshiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_danshi_baiwei, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZU3_DANSHI_BAIWEI_START, 0, this,
					this);
			PL3A2Zu3DanshiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_danshi_shiwei, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZU3_DANSHI_SHIWEI_START, 0, this,
					this);
			PL3BZu3DanshiBallTable = PublicMethod
					.makeBallTable(linearLayout, R.id.table_zu3_danshi_gewei,
							iScreenWidth, redBallViewNum, PL3BallResId,
							RED_PL3_ZU3_DANSHI_GEWEI_START, 0, this, this);
			PL3Zu3FushiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_fushi, iScreenWidth, redBallViewNum,
					PL3BallResId, RED_PL3_ZU3_FUSHI_START, 0, this, this);

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

			touzhu = (ImageButton) linearLayout.findViewById(R.id.b_touzhu_zu3);
			touzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});

			newSelect = (ImageButton) linearLayout
					.findViewById(R.id.pl3_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					PL3A1Zu3DanshiBallTable.clearAllHighlights();
					PL3A2Zu3DanshiBallTable.clearAllHighlights();
					PL3BZu3DanshiBallTable.clearAllHighlights();
					PL3Zu3FushiBallTable.clearAllHighlights();
					baiEdit.setText("");
					shiEdit.setText("");
					geEdit.setText("");
					hezhiEdit.setText("");
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final LinearLayout danshiArea = (LinearLayout) linearLayout
					.findViewById(R.id.danshi_balls_layout);
			final LinearLayout fushiArea = (LinearLayout) linearLayout
					.findViewById(R.id.fushi_balls_layout);
			final LinearLayout baiArea = (LinearLayout) linearLayout
					.findViewById(R.id.pl3_bai_balls_layout);
			final LinearLayout shiArea = (LinearLayout) linearLayout
					.findViewById(R.id.pl3_shi_balls_layout);
			final LinearLayout geArea = (LinearLayout) linearLayout
					.findViewById(R.id.pl3_ge_balls_layout);
			final TextView baiText = (TextView) linearLayout
					.findViewById(R.id.pl3_text_bai);
			final TextView shiText = (TextView) linearLayout
					.findViewById(R.id.pl3_text_shi);
			final TextView geText = (TextView) linearLayout
					.findViewById(R.id.pl3_text_ge);
			baiEdit = (EditText) linearLayout.findViewById(R.id.pl3_edit_bai);
			shiEdit = (EditText) linearLayout.findViewById(R.id.pl3_edit_shi);
			geEdit = (EditText) linearLayout.findViewById(R.id.pl3_edit_ge);

			baiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						baiEdit.setBackgroundResource(R.drawable.hongkuang);
						baiArea.setVisibility(LinearLayout.VISIBLE);
						shiArea.setVisibility(LinearLayout.GONE);
						geArea.setVisibility(LinearLayout.GONE);

						baiText.setTextColor(Color.RED);
					} else {
						baiEdit.setBackgroundResource(R.drawable.huikuang);
						baiText.setTextColor(Color.BLACK);
					}
				}
			});
			shiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						shiEdit.setBackgroundResource(R.drawable.hongkuang);
						shiArea.setVisibility(LinearLayout.VISIBLE);
						baiArea.setVisibility(LinearLayout.GONE);
						geArea.setVisibility(LinearLayout.GONE);

						shiText.setTextColor(Color.RED);
					} else {
						shiEdit.setBackgroundResource(R.drawable.huikuang);
						shiText.setTextColor(Color.BLACK);
					}

				}
			});
			geEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub

					if (hasFocus) {
						geEdit.setBackgroundResource(R.drawable.hongkuang);
						geArea.setVisibility(LinearLayout.VISIBLE);
						shiArea.setVisibility(LinearLayout.GONE);
						baiArea.setVisibility(LinearLayout.GONE);

						geText.setTextColor(Color.RED);
					} else {
						geEdit.setBackgroundResource(R.drawable.huikuang);
						geText.setTextColor(Color.BLACK);
					}
				}
			});
			final TextView hezhiText = (TextView) linearLayout
					.findViewById(R.id.pl3_text_hezhi);
			hezhiEdit = (EditText) linearLayout
					.findViewById(R.id.pl3_edit_hezhi);

			hezhiEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if (hasFocus) {
						hezhiEdit.setBackgroundResource(R.drawable.hongkuang);
						hezhiText.setTextColor(Color.RED);

					} else {
						hezhiEdit.setBackgroundResource(R.drawable.huikuang);
						hezhiText.setTextColor(Color.BLACK);
					}
				}
			});

			if (bZu3Danshi) {
				shiArea.setVisibility(LinearLayout.GONE);
				geArea.setVisibility(LinearLayout.GONE);
				fushiArea.setVisibility(LinearLayout.GONE);
			} else if (bZu3Fushi) {
				fushiArea.setVisibility(LinearLayout.VISIBLE);
				danshiArea.setVisibility(LinearLayout.GONE);
			}
		}
		// 将View加入主框架
		buyView.addView(linearLayout, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/**
	 *组3单式机选
	 */
	private void create_PL3_ZU3_JIXUAN() {
		recycleResources();
		sensor.startAction();// 启动晃动机选
		buyView.removeAllViews();
		Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT).show();
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);
		balls = new Vector<Balls>();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_zhixuan_jixuan, null);
		{

			title.setText("排列三组三机选");
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
		}

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

		touzhu = (ImageButton) iV.findViewById(R.id.ssq_fushi_b_touzhu);
		touzhu.setOnClickListener(new OnClickListener() {
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
	 * 直选机选
	 */
	private void create_PL3_ZHIXUAN_JIXUAN() {
		recycleResources();
		sensor.startAction();
		buyView.removeAllViews();
		Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT).show();
		balls = new Vector<Balls>();
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_zhixuan_jixuan, null);
		{

			title.setText("排列三机选");
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
		}

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

		touzhu = (ImageButton) iV.findViewById(R.id.ssq_fushi_b_touzhu);
		touzhu.setOnClickListener(new OnClickListener() {
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
	 * 初始化buttons
	 * 
	 * @param void
	 */
	private void initTopButtons() {
		topButtonGroup = (RadioGroup) findViewById(R.id.topMenu);
		topButtonGroup.setOnCheckedChangeListener(this);
		topHeButtonGroup = (RadioGroup) findViewById(R.id.hezhiMenu);
		topHeButtonGroup.setOnCheckedChangeListener(this);
		topZuButtonGroup = (RadioGroup) findViewById(R.id.zuxuanMenu);
		topZuButtonGroup.setOnCheckedChangeListener(this);
		defaultOffShade = RadioStateDrawable.SHADE_GRAY;
		defaultOnShade = RadioStateDrawable.SHADE_YELLOW;

		topButtonLayoutParams = new RadioGroup.LayoutParams(/* 320/5 */64,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		commit(4, topButtonIdOn, topButtonIdOff, topButtonGroup, TOP);
		commit(2, zuTopButtonIdOn, zuTopButtonIdOff, topZuButtonGroup, ZU);
		commit(3, heTopButtonIdOn, heTopButtonIdOff, topHeButtonGroup, HE);
	}

	/**
	 * RadioGroup提交操作
	 * 
	 * @param void
	 */
	public void commit(int iButtonNum, int[] topButtonIdon, int[] topButtonOff,
			RadioGroup top, int type) {
		top.removeAllViews();
		if (isZhiXuanJiXuan) {
			topButtonIdOn[1] = R.drawable.zhixuan_b;
			topButtonIdOff[1] = R.drawable.zhixuan;
		} else {
			topButtonIdOn[1] = R.drawable.jixuan_b;
			topButtonIdOff[1] = R.drawable.jixuan;
		}
		if (isZu3JiXuan || iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			topButtonIdOn[2] = R.drawable.zuxuan_pl3_b;
			topButtonIdOff[2] = R.drawable.zuxuan_pl3;
		} else {
			topButtonIdOn[2] = R.drawable.jixuan_b;
			topButtonIdOff[2] = R.drawable.jixuan;
		}
		int optimum_visible_items_in_portrait_mode = iButtonNum;
		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width;
		width = screen_width / iButtonNum;
		RadioStateDrawable.other_width = width;
		RadioStateDrawable.other_screen_width = screen_width;
		switch (type) {
		case HE:
			width = PublicConst.IMG_WITH;
			break;
		case ZU:
			if (Constants.SCREEN_WIDTH == 320) {
				width = PublicConst.IMG_WITH + 130;
			} else {
				width = PublicConst.IMG_WITH + 220;
			}
			break;
		case TOP:
			width = screen_width / iButtonNum;
			break;
		}

		for (int i = 0; i < iButtonNum; i++) {
			TabBarButton tabButton = new TabBarButton(this);
			tabButton.setState(topButtonIdon[i], topButtonOff[i], 4);
			tabButton.setId(i);
			if (i != iButtonNum - 1 || type == TOP
					|| Constants.SCREEN_WIDTH != 320) {
				topButtonLayoutParams = new RadioGroup.LayoutParams(width,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				top.addView(tabButton, i, topButtonLayoutParams);
			} else {
				topButtonLayoutParams = new RadioGroup.LayoutParams(
						PublicConst.IMG_WITH,
						RadioGroup.LayoutParams.WRAP_CONTENT);
				top.addView(tabButton, i, topButtonLayoutParams);
			}

		}

	}

	/**
	 * 更换底部选项组的图片
	 * 
	 */
	public void changeImg(int iButtonNum, int[] topButtonIdon,
			int[] topButtonIdonoff, RadioGroup top) {
		top.removeViews(1, 2);
		if (isZhiXuanJiXuan) {
			topButtonIdOn[1] = R.drawable.zhixuan_pl3_b;
			topButtonIdOff[1] = R.drawable.zhixuan_pl3;
		} else {
			topButtonIdOn[1] = R.drawable.jixuan_pl3_b;
			topButtonIdOff[1] = R.drawable.jixuan_pl3;
		}
		if (isZu3JiXuan) {
			topButtonIdOn[2] = R.drawable.zuxuan_pl3_b;
			topButtonIdOff[2] = R.drawable.zuxuan_pl3;
		} else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			topButtonIdOn[2] = R.drawable.zuxuan_pl3_b;
			topButtonIdOff[2] = R.drawable.zuxuan_pl3_b;
		} else {
			topButtonIdOn[2] = R.drawable.jixuan_pl3_b;
			topButtonIdOff[2] = R.drawable.jixuan_pl3;
		}

		int screen_width = getWindowManager().getDefaultDisplay().getWidth();
		int width = screen_width / iButtonNum;

		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		TabBarButton tabButton = new TabBarButton(this);

		tabButton.setState(topButtonIdon[1], topButtonIdonoff[1], 4);
		tabButton.setId(1);
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		top.addView(tabButton, 1, topButtonLayoutParams);

		TabBarButton tabButtonTwo = new TabBarButton(this);
		tabButtonTwo.setState(topButtonIdon[2], topButtonIdonoff[2], 4);
		tabButtonTwo.setId(2);
		if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			tabButtonTwo.setEnabled(false);
		}
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		top.addView(tabButtonTwo, 2, topButtonLayoutParams);

	}

	/**
	 * 设置当前标签
	 * 
	 * @param index
	 *            当前标签的Id
	 */
	public void setCurrentTab(int index) {

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

		switch (group.getId()) {
		case R.id.topMenu:

			switch (checkedId) {
			case 0:// 购彩
				sensor.stopAction();
				Intent intent1 = new Intent(PL3.this, RuyicaiAndroid.class);
				startActivity(intent1);
				finish();
				break;
			case 1:// 直选
				iCurrentButton = PublicConst.BUY_PL3_ZHIXUAN;
				isZhiXuanJiXuan = !isZhiXuanJiXuan;
				if (!isZhiXuanJiXuan && !isZu3JiXuan) {
					isZu3JiXuan = true;
				}
				createBuyView(iCurrentButton);
				break;
			case 2:// 组选
				iCurrentButton = PublicConst.BUY_PL3_ZU3;
				isZu3JiXuan = !isZu3JiXuan;
				if (!isZhiXuanJiXuan && !isZu3JiXuan) {
					isZhiXuanJiXuan = true;
				}
				topZuButtonGroup.check(0);
				createBuyView(iCurrentButton);
				break;
			case 3:// 和值
				sensor.stopAction();
				iCurrentButton = PublicConst.BUY_PL3_HEZHI;
				if (!isZhiXuanJiXuan) {
					isZhiXuanJiXuan = true;
				}
				if (!isZu3JiXuan) {
					isZu3JiXuan = true;
				}
				topHeButtonGroup.check(0);
				createBuyView(iCurrentButton);
				break;
			}
			break;
		case R.id.hezhiMenu:
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

			break;
		case R.id.zuxuanMenu:
			switch (checkedId) {
			// 组3
			case 0:
				sensor.stopAction();
				iCurrentButton = PublicConst.BUY_PL3_ZU3;
				createBuyView(iCurrentButton);
				break;
			// 组6
			case 1:
				sensor.stopAction();
				iCurrentButton = PublicConst.BUY_PL3_ZU6;
				createBuyView(iCurrentButton);
				break;
			}
			break;
		// case R.id.danfushiMenu:
		// switch (checkedId) {
		// // 组3单式
		// case 0:
		// sensor.stopAction();
		// bZu3Danshi = true;
		// bZu3Fushi = false;
		// iCurrentButton = PublicConst.BUY_PL3_ZU3;
		// create_PL3_ZU3();
		// break;
		// // 组三复式
		// case 1:
		// sensor.stopAction();
		// bZu3Danshi = false;
		// bZu3Fushi = true;
		// iCurrentButton = PublicConst.BUY_PL3_ZU3;
		// create_PL3_ZU3();
		// break;
		// }
		// break;
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
		// 单式
		case R.id.ssq_fushi_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			if (!isZhiXuanJiXuan)
				changeTextSumMoney(iWhich);
			break;
		case R.id.ssq_fushi_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
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
			if (isZhiXuanJiXuan) {
				iReturnValue = balls.size();
			}else{
				iReturnValue = PL3ZhixuanBaiweiBallTable.getHighlightBallNums()
				* PL3ZhixuanShiweiBallTable.getHighlightBallNums()
				* PL3ZhixuanGeweiBallTable.getHighlightBallNums();
			}
			break;

		// 排列三组3复式
		case PublicConst.BUY_PL3_ZU3:
			// 添加组3单式注数获取20100912 陈晨
			if (isZu3JiXuan) {
				iReturnValue = balls.size();
			}else if (bZu3Danshi) {
					// 单式注数永为1
					iReturnValue = 1;
			}else if (bZu3Fushi) {//复式注数
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
				if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
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
				if (j == BallNos[i] - 1) {// 因为数组是从0开始的，小球实际Id从0开始
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
				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
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
			int iZhuShu = getZhuShu();
			// 排列三和值直选
			if (aWhichGroupBall == 10) {
				int[] BallNos = PL3HezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
				String temp = "当前玩法为和值直选，共" + iZhuShu * iProgressBeishu + "注，共"
						+ (iZhuShu * iProgressBeishu * 2) + "元";
				mTextSumMoney.setText(temp);
			}
			// 排列三和值组3
			if (aWhichGroupBall == 11) {
				int[] BallNos = PL3HezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）

				String temp = "当前玩法为和值组3，共" + iZhuShu * iProgressBeishu + "注，共"
						+ (iZhuShu * iProgressBeishu * 2) + "元";
				mTextSumMoney.setText(temp);
			}
			// 排列三和值组6
			if (aWhichGroupBall == 12) {
				int[] BallNos = PL3HezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）

				String temp = "当前玩法为和值组6，共" + iZhuShu * iProgressBeishu + "注，共"
						+ (iZhuShu * iProgressBeishu * 2) + "元";
				mTextSumMoney.setText(temp);
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

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu6ZhuMa = PL3HezhiZu6BallTable.getHighlightBallNOs();
			for (int i = 0; i < zu6ZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(zu6ZhuMa[i]);
				if (i != zu6ZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				hezhiEdit.setText("");
			} else {
				hezhiEdit.setText(zhuma_string);
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

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu3ZhuMa = PL3HezhiZu3BallTable.getHighlightBallNOs();
			for (int i = 0; i < zu3ZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(zu3ZhuMa[i]);
				if (i != zu3ZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				hezhiEdit.setText("");
			} else {
				hezhiEdit.setText(zhuma_string);
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

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zhiZhuMa = PL3HezhiZhixuanBallTable.getHighlightBallNOs();
			for (int i = 0; i < zhiZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(zhiZhuMa[i]);
				if (i != zhiZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				hezhiEdit.setText("");
			} else {
				hezhiEdit.setText(zhuma_string);
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

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] baiZhuMa = PL3ZhixuanBaiweiBallTable.getHighlightBallNOs();
			for (int i = 0; i < baiZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(baiZhuMa[i]);
				if (i != baiZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				baiEdit.setText("");
			} else {
				baiEdit.setText(zhuma_string);
			}
		}
		if (aWhichGroupBall == 8) { // 十位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = PL3ZhixuanShiweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] shiZhuMa = PL3ZhixuanShiweiBallTable.getHighlightBallNOs();
			for (int i = 0; i < shiZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(shiZhuMa[i]);
				if (i != shiZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				shiEdit.setText("");
			} else {
				shiEdit.setText(zhuma_string);
			}
		}
		if (aWhichGroupBall == 9) { // 个位
			int iChosenBallSum = 10;
			// 每次点击后记住小球的状态
			int isHighLight = PL3ZhixuanGeweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] geZhuMa = PL3ZhixuanGeweiBallTable.getHighlightBallNOs();
			for (int i = 0; i < geZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(geZhuMa[i]);
				if (i != geZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				geEdit.setText("");
			} else {
				geEdit.setText(zhuma_string);
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

			String zhuma_string = "  ";
			int[] zu6ZhuMa = PL3Zu6BallTable.getHighlightBallNOs();
			for (int i = 0; i < zu6ZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(zu6ZhuMa[i]);
				if (i != zu6ZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				hezhiEdit.setText("");
			} else {
				hezhiEdit.setText(zhuma_string);
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
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT
					&& PL3BZu3DanshiBallTable.getOneBallStatue(aBallViewId) != 0) {
				PL3BZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,
						getResources().getString(R.string.pl3_toast_bai_title),
						Toast.LENGTH_SHORT).show();
			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = PL3A1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = PL3A2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT
					&& PL3BZu3DanshiBallTable.getOneBallStatue(aBallViewId) != 0) {
				PL3BZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,
						getResources().getString(R.string.pl3_toast_bai_title),
						Toast.LENGTH_SHORT).show();
			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 2) {
			int iChosenBallSum = 1;
			// 组3,前两组相同，第三组不能与之相同

			int isHighLight = PL3BZu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT
					&& PL3A1Zu3DanshiBallTable.getOneBallStatue(aBallViewId) != 0) {
				PL3A1Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				PL3A2Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,
						getResources().getString(R.string.pl3_toast_ge_title),
						Toast.LENGTH_SHORT).show();

			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 3) {

			int iChosenBallSum = 10;
			int isHighLight = PL3Zu3FushiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu3ZhuMa = PL3Zu3FushiBallTable.getHighlightBallNOs();
			for (int i = 0; i < zu3ZhuMa.length; i++) {
				zhuma_string += PublicMethod.getZhuMa(zu3ZhuMa[i]);
				if (i != zu3ZhuMa.length - 1)
					zhuma_string = zhuma_string + ",";
			}
			if (zhuma_string.equals("  ")) {
				hezhiEdit.setText("");
			} else {
				hezhiEdit.setText(zhuma_string);
			}
		}
	}

	/**
	 * 组三单式输入框判断
	 */
	public void isZu3Danshi() {
		// 百位
		String zhuma_string = "  ";
		int[] baiZhuMa = PL3A1Zu3DanshiBallTable.getHighlightBallNOs();
		for (int i = 0; i < baiZhuMa.length; i++) {
			zhuma_string += PublicMethod.getZhuMa(baiZhuMa[i]);
			if (i != baiZhuMa.length - 1)
				zhuma_string = zhuma_string + ",";
		}
		if (zhuma_string.equals("  ")) {
			baiEdit.setText("");
		} else {
			baiEdit.setText(zhuma_string);
		}
		// 十位
		String shi_zhuma_string = "  ";
		int[] shiZhuMa = PL3A2Zu3DanshiBallTable.getHighlightBallNOs();
		for (int i = 0; i < shiZhuMa.length; i++) {
			shi_zhuma_string += PublicMethod.getZhuMa(shiZhuMa[i]);
			if (i != shiZhuMa.length - 1)
				shi_zhuma_string = shi_zhuma_string + ",";
		}
		if (shi_zhuma_string.equals("  ")) {
			shiEdit.setText("");
		} else {
			shiEdit.setText(zhuma_string);
		}
		// 个位
		String ge_zhuma_string = "  ";
		int[] geZhuMa = PL3BZu3DanshiBallTable.getHighlightBallNOs();
		for (int i = 0; i < geZhuMa.length; i++) {
			ge_zhuma_string += PublicMethod.getZhuMa(geZhuMa[i]);
			if (i != geZhuMa.length - 1)
				ge_zhuma_string = ge_zhuma_string + ",";
		}
		if (ge_zhuma_string.equals("  ")) {
			geEdit.setText("");
		} else {
			geEdit.setText(ge_zhuma_string);
		}
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
		if (sessionIdStr == null || sessionIdStr.equals("")) {
			Intent intentSession = new Intent(PL3.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {

			// 排列三直选
			if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN) {
				if (isZhiXuanJiXuan) {
					if (balls.size() == 0) {
						PublicMethod.alertJiXuan("请至少选择1注彩票", this);
					} else {
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlertJixuan();
						alert(sTouzhuAlert);
					}
				} else {
					int baiweiNums = PL3ZhixuanBaiweiBallTable.getHighlightBallNums();
					int shiweiNums = PL3ZhixuanShiweiBallTable.getHighlightBallNums();
					int geweiNums = PL3ZhixuanGeweiBallTable.getHighlightBallNums();
					int[] baiweis = PL3ZhixuanBaiweiBallTable.getHighlightBallNOs();
					int[] shiweis = PL3ZhixuanShiweiBallTable.getHighlightBallNOs();
					int[] geweis = PL3ZhixuanGeweiBallTable.getHighlightBallNOs();
					String baiweistr = "";
					String shiweistr = "";
					String geweistr = "";
					for (int i = 0; i < baiweiNums; i++) {
						baiweistr += (baiweis[i]) + ",";
						if (i == baiweiNums - 1) {
							baiweistr = baiweistr.substring(0, baiweistr
									.length() - 1);
						}
					}
					for (int i = 0; i < shiweiNums; i++) {
						shiweistr += (shiweis[i]) + ",";
						if (i == shiweiNums - 1) {
							shiweistr = shiweistr.substring(0, shiweistr
									.length() - 1);
						}
					}
					for (int i = 0; i < geweiNums; i++) {
						geweistr += (geweis[i]) + ",";
						if (i == geweiNums - 1) {
							geweistr = geweistr.substring(0,
									geweistr.length() - 1);
						}
					}
					if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
						alertDialog("请选择号码", "请在百位，十位，个位均至少选择一个小球后再投注");
					} else if ((baiweiNums + shiweiNums + geweiNums) > 24) {
						alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "小球个数不超过24个");
					} else {
						int iZhuShu = getZhuShu() * iProgressBeishu;
						if (iZhuShu / iProgressBeishu > 600) {
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "请选择不大于600注投注");
						} else if (iZhuShu * 2 > 100000) {
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
						} else {

							alert("注数：" + iZhuShu / iProgressBeishu + "注"
									+ "\n" + "倍数：" + iProgressBeishu + "倍"
									+ "\n" + "追号：" + iProgressQishu + "期"
									+ "\n" + "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + "\n" + "百位："
									+ baiweistr + "\n" + "十位：" + shiweistr
									+ "\n" + "个位：" + geweistr + "\n" + "确认支付吗？");
						}
					}
				}
			}
			// 排列三组3
			if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
				int iZhuShu = 0;
			if (isZu3JiXuan) {
					if (balls.size() == 0) {
						PublicMethod.alertJiXuan("请至少选择1注彩票", this);
					} else {
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlertJixuan();
						alert(sTouzhuAlert);
					}
			}else if(bZu3Danshi){
					int baiweiNums = PL3A1Zu3DanshiBallTable.getHighlightBallNums();
			        int shiweiNums = PL3A2Zu3DanshiBallTable.getHighlightBallNums();
			        int geweiNums = PL3BZu3DanshiBallTable.getHighlightBallNums();

			        if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
				         alertDialog("请选择号码", "请再百位，十位， 个位中均选择一个小球后再投注");
			          } else if (baiweiNums == 1 && shiweiNums == 1&& geweiNums == 1) {
				        iZhuShu = mSeekBarBeishu.getProgress();
				        String baiweistr = PL3A1Zu3DanshiBallTable.getHighlightBallNOs()[0]+ "";// 前2位相同
				        String geweistr = PL3BZu3DanshiBallTable.getHighlightBallNOs()[0]+ "";// 前2位相同
				    if (iZhuShu * 2 > 100000) {
					    alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
				    } else {

					  alert("注数：" + 1 + "注" + "\n" + "倍数："
							+ iProgressBeishu + "倍" + "\n" + "追号："
							+ iProgressQishu + "期" + "\n" + "金额："
							+ iZhuShu * 2 + "元" + "\n" + "冻结金额："
							+ (2 * (iProgressQishu - 1) * iZhuShu)
							+ "元" + "\n" + "注码：" + baiweistr + ","
							+ baiweistr + "," + geweistr + "\n"
							+ "确认支付吗？");
				       }
			       }
		    }else if(bZu3Fushi) {
					if (PL3Zu3FushiBallTable.getHighlightBallNums() < 2) {
						alertDialog("请选择号码", "请至少选择2个小球后再投注");
					} else {
						int[] fushiNums = PL3Zu3FushiBallTable
								.getHighlightBallNOs();
						String fushiStr = "";
						for (int i = 0; i < fushiNums.length; i++) {
							fushiStr += (fushiNums[i]) + ",";
							if (i == fushiNums.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						iZhuShu = getZhuShu() * iProgressBeishu;
						if (iZhuShu * 2 > 100000) {
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
						} else {

							alert("注数：" + iZhuShu / iProgressBeishu + "注"
									+ "\n" + "倍数：" + iProgressBeishu + "倍"
									+ "\n" + "追号：" + iProgressQishu + "期"
									+ "\n" + "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n"
									+ "确认支付吗？");
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
						fushiStr += (fushiNums[i]) + ",";
						if (i == fushiNums.length - 1) {
							fushiStr = fushiStr.substring(0,
									fushiStr.length() - 1);
						}
					}
					int iZhuShu = getZhuShu() * iProgressBeishu;

					if (iZhuShu * 2 > 100000) {
						alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
					} else {

						alert("注数：" + iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" + "冻结金额："
								+ (2 * (iProgressQishu - 1) * iZhuShu) + "元"
								+ "\n" + "注码：" + fushiStr + "\n" + "确认支付吗？");
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
							fushiStr += (zhuma_zhixuanhezhi[i]) + ",";
							if (i == zhuma_zhixuanhezhi.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						if (iZhuShu * 2 > 100000) {
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
						} else {

							alert("注数：" + iZhuShu / iProgressBeishu + "注"
									+ "\n" + "倍数：" + iProgressBeishu + "倍"
									+ "\n" + "追号：" + iProgressQishu + "期"
									+ "\n" + "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n"
									+ "确认支付吗？");
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
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
						} else {

							alert("注数：" + iZhuShu / iProgressBeishu + "注"
									+ "\n" + "倍数：" + iProgressBeishu + "倍"
									+ "\n" + "追号：" + iProgressQishu + "期"
									+ "\n" + "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n"
									+ "确认支付吗？");
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
							fushiStr += (zhuma_zu6hezhi[i]) + ",";
							if (i == zhuma_zu6hezhi.length - 1) {
								fushiStr = fushiStr.substring(0, fushiStr
										.length() - 1);
							}
						}
						if (iZhuShu * 2 > 100000) {
							alertDialog(getResources().getString(R.string.toast_touzhu_title).toString(), "单笔投注不能大于100000元");
						} else {

							alert("注数：" + iZhuShu / iProgressBeishu + "注"
									+ "\n" + "倍数：" + iProgressBeishu + "倍"
									+ "\n" + "追号：" + iProgressQishu + "期"
									+ "\n" + "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n"
									+ "确认支付吗？");
						}
					}

				}
			}
		}
	}

	@Override
	public void onOkClick(int[] aReturn) {

		switch (iCurrentButton) {
		case PublicConst.BUY_PL3_ZHIXUAN:
			PL3ZhixuanBaiweiBallTable.randomChooseConfigChangePL3(aReturn[0],
					7, null);
			PL3ZhixuanShiweiBallTable.randomChooseConfigChangePL3(aReturn[1],
					8, null);
			PL3ZhixuanGeweiBallTable.randomChooseConfigChangePL3(aReturn[2], 9,
					null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_ZU3:
			PL3Zu3FushiBallTable.randomChooseConfigChangePL3(aReturn[0], 3,
					null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_ZU6:
			PL3Zu6BallTable.randomChooseConfigChangePL3(aReturn[0], 4, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_PL3_HEZHI:
			if (iWhich == 10) {
				PL3HezhiZhixuanBallTable.randomChooseConfigChangePL3(
						aReturn[0], 10, null);
				changeTextSumMoney(10);
				break;
			}
			if (iWhich == 11) {
				PL3HezhiZu3BallTable.randomChooseConfigChangePL3(aReturn[0],
						11, null);
				changeTextSumMoney(11);
				break;
			}
			if (iWhich == 12) {
				PL3HezhiZu6BallTable.randomChooseConfigChangePL3(aReturn[0],
						12, null);
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
		BettingInterface betting = BettingInterface.getInstance();
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");

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
					.getHighlightBallNOs();
			int[] zhuma_shiwei = PL3ZhixuanShiweiBallTable
					.getHighlightBallNOs();
			int[] zhuma_gewei = PL3ZhixuanGeweiBallTable.getHighlightBallNOs();
		
			if (isZhiXuanJiXuan) {
				for (int i = 0; i < balls.size(); i++) {
					strZM += zhixuan;
					int bai = balls.get(i).getBai()[0] + 1;
					int shi = balls.get(i).getShi()[0] + 1;
					int ge = balls.get(i).getGe()[0] + 1;
					strZM += bai + "," + shi + "," + ge;
					if (i != balls.size() - 1) {
						strZM += ";";
					}
				}
			} else {
				strZM = zhixuan;
				if (zhuma_baiwei.length > 0 && zhuma_shiwei.length > 0
						&& zhuma_gewei.length > 0) {
					for (int i = 0; i < zhuma_baiwei.length; i++) {
						strZM += (zhuma_baiwei[i]) + "";
					}
					strZM += ",";
					for (int i = 0; i < zhuma_shiwei.length; i++) {
						strZM += (zhuma_shiwei[i]) + "";
					}
					strZM += ",";
					for (int i = 0; i < zhuma_gewei.length; i++) {
						strZM += (zhuma_gewei[i]) + "";
					}

				}
			}
		}
		// 排列三组三玩法
		else if (iCurrentButton == PublicConst.BUY_PL3_ZU3) {
			if (isZu3JiXuan) {
				for (int i = 0; i < balls.size(); i++) {
					strZM += zuxuan;
					int bai = balls.get(i).getBai()[0] + 1;
					int ge = balls.get(i).getGe()[0] + 1;
					strZM += ge + "," + bai + "," + bai;
					if (i != balls.size() - 1) {
						strZM += ";";
					}
				}
			}else if (bZu3Danshi) {// 单式
					strZM = zuxuan;
					int[] zhuma_zu3danshi_A1 = PL3A1Zu3DanshiBallTable.getHighlightBallNOs();
			        int[] zhuma_zu3danshi_A2 = PL3A2Zu3DanshiBallTable.getHighlightBallNOs();
			        int[] zhuma_zu3danshi_A3 = PL3BZu3DanshiBallTable
					.getHighlightBallNOs();
				
					if (zhuma_zu3danshi_A1.length > 0
							&& zhuma_zu3danshi_A2.length > 0
							&& zhuma_zu3danshi_A3.length > 0) {
						strZM += (zhuma_zu3danshi_A3[0]) + ","
								+ (zhuma_zu3danshi_A1[0]) + ","
								+ (zhuma_zu3danshi_A1[0]);
				}

			}else if (bZu3Fushi) {// 复式也叫组三包号
				strZM = zu3fushi;
				int[] zhuma_zu3fushi = PL3Zu3FushiBallTable
						.getHighlightBallNOs();
				if (zhuma_zu3fushi.length > 0) {
					for (int i = 1; i < zhuma_zu3fushi.length + 1; i++) {
						strZM += zhuma_zu3fushi[i - 1] + "";
					}
				}

			}

		}
		// 排列三组六玩法
		else if (iCurrentButton == PublicConst.BUY_PL3_ZU6) {
			int[] zhuma_zu6danfushi = PL3Zu6BallTable.getHighlightBallNOs();
			if (zhuma_zu6danfushi.length == 3) {// 单式
				strZM = zuxuan + (zhuma_zu6danfushi[0]) + ","
						+ (zhuma_zu6danfushi[1]) + "," + (zhuma_zu6danfushi[2]);
				PublicMethod.myOutput("------zhuma------" + strZM);
			}
			if (zhuma_zu6danfushi.length > 3) {// 复式也叫组六包号
				strZM = zu6fushi;
				for (int i = 1; i < zhuma_zu6danfushi.length + 1; i++) {
					strZM += zhuma_zu6danfushi[i - 1] + "";
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
					strZM += (zhuma_zhixuanhezhi[i]) + ",";
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
					strZM += (zhuma_zu6hezhi[i]) + ",";
					if (i == zhuma_zu6hezhi.length - 1) {
						strZM = strZM.substring(0, strZM.length() - 1);
					}
				}
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
		public BallGroup ZhixuanBallGroup = new BallGroup();
		// 排列三组3
		public BallGroup Zu3BallGroup = new BallGroup();
		// 排列三组6
		public BallGroup Zu6BallGroup = new BallGroup();
		// 和值
		public BallGroup HezhiZhixuanBallGroup = new BallGroup();
		public BallGroup HezhiZu3BallGroup = new BallGroup();
		public BallGroup HezhiZu6BallGroup = new BallGroup();

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
		public int[] iZhixuanBaiweiBallStatus = new int[10];
		public int[] iZhixuanShiweiBallStatus = new int[10];
		public int[] iZhixuanGeweiBallStatus = new int[10];
		// 排列三组3
		public int[] iZu3A1BallStatus = new int[10];
		public int[] iZu3A2BallStatus = new int[10];
		public int[] iZu3BBallStatus = new int[10];
		public int[] iZu3FushiBallStatus = new int[10];
		boolean bRadioBtnDanshi;
		boolean bRadioBtnFushi;
		// 排列三组6
		public int[] iZu6BallStatus = new int[10];
		// 排列三和值直选
		public int[] iHezhiZhixuanBallStatus = new int[28];
		public int[] iHezhiZu3BallStatus = new int[26];
		public int[] iHezhiZu6BallStatus = new int[22];
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
	    sensor.stopAction();
	    Dialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								touzhu.setImageResource(R.drawable.touzhuup_n);
								touzhu.setClickable(false);

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

										// 投注新接口 陈晨 20100711 钱数计算有问题 2010/7/13
										// 陈晨
										strCode = payNew(zhuma, iBeiShu + "",iZhuShu * iProgressBeishu * 2+ "", iQiShu + "");

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
											msg.what = 7;
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

						}).create();
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (isZhiXuanJiXuan||isZu3JiXuan) {
					sensor.startAction();
				}
			}
		});

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
	 * 机选小球
	 * 
	 * @author Administrator
	 * 
	 */
	class Balls {
		int[] baiNum;
		int[] shiNum;
		int[] geNum;

		public Balls() {
			if (iCurrentButton == PublicConst.BUY_PL3_ZU3 && isZu3JiXuan) {
			    int[] baiGeNum = PublicMethod.getRandomsWithoutCollision(2, -1, 8);
			    baiNum = new int[1];    
			    shiNum = new int[1];   
			    geNum = new int[1];   
			    shiNum[0]=baiNum[0]=baiGeNum[0];
			    geNum[0]=baiGeNum[1];
			} else if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN && isZhiXuanJiXuan) {
				baiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
				shiNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
				geNum = PublicMethod.getRandomsWithoutCollision(1, -1, 8);
			}

		}

		public int[] getBai() {
			return baiNum;

		}

		public int[] getShi() {
			return shiNum;

		}

		public int[] getGe() {
			return geNum;

		}

		public String getBaiZhu() {
			String str = "";
			for (int i = 0; i < baiNum.length; i++) {
				if (i != baiNum.length - 1)
					str += PublicMethod.getZhuMa(baiNum[i] + 1) + ",";
				else
					str += PublicMethod.getZhuMa(baiNum[i] + 1);
			}
			return str;

		}

		public String getShiZhu() {
			String str = "";
			for (int i = 0; i < shiNum.length; i++) {
				if (i != shiNum.length - 1)
					str += PublicMethod.getZhuMa(shiNum[i] + 1) + ",";
				else
					str += PublicMethod.getZhuMa(shiNum[i] + 1);
			}
			return str;

		}

		public String getGeZhu() {
			String str = "";
			for (int i = 0; i < geNum.length; i++) {
				if (i != geNum.length - 1)
					str += PublicMethod.getZhuMa(geNum[i] + 1) + ",";
				else
					str += PublicMethod.getZhuMa(geNum[i] + 1);
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
			BallTable baiweiBallTable = new BallTable(lines,
					RED_PL3_ZHIXUAN_BAIWEI_START);
			BallTable shiweiBallTable = new BallTable(lines,
					RED_PL3_ZHIXUAN_SHIWEI_START);
			BallTable geweiBallTable = new BallTable(lines,
					RED_PL3_ZHIXUAN_GEWEI_START);
			if (iCurrentButton == PublicConst.BUY_PL3_ZU3 && isZu3JiXuan) {
				PublicMethod.makeBallTableJiXuan(baiweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getBai(), this);
				PublicMethod.makeBallTableJiXuan(shiweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getBai(), this);
				PublicMethod.makeBallTableJiXuan(geweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getGe(), this);
			} else if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN
					&& isZhiXuanJiXuan) {
				PublicMethod.makeBallTableJiXuan(baiweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getBai(), this);
				PublicMethod.makeBallTableJiXuan(shiweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getShi(), this);
				PublicMethod.makeBallTableJiXuan(geweiBallTable, iScreenWidth,
						PL3BallResId, balls.get(i).getGe(), this);
			}

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
								PL3.this,
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
		if (iCurrentButton == PublicConst.BUY_PL3_ZU3 && isZu3JiXuan) {
			for (int i = 0; i < balls.size(); i++) {
				zhumaString += balls.get(i).getBaiZhu() + ","
						+ balls.get(i).getBaiZhu() + ","
						+ balls.get(i).getGeZhu() + "\n";
			}
		} else if (iCurrentButton == PublicConst.BUY_PL3_ZHIXUAN
				&& isZhiXuanJiXuan) {
			for (int i = 0; i < balls.size(); i++) {
				zhumaString += balls.get(i).getBaiZhu() + ","
						+ balls.get(i).getShiZhu() + ","
						+ balls.get(i).getGeZhu() + "\n";
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
	 * 手机震动类
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

	/**
	 * // 排列三组3单式前2位相同 BallTable PL3A1Zu3DanshiBallTable = null; BallTable
	 * PL3A2Zu3DanshiBallTable = null; // 排列三组3单式第三位 BallTable
	 * PL3BZu3DanshiBallTable = null; // 排列三组3复式 BallTable PL3Zu3FushiBallTable
	 * = null; // 排列三组6单复式 BallTable PL3Zu6BallTable = null; // 排列三直选 BallTable
	 * PL3ZhixuanBaiweiBallTable = null;// 直选百位 BallTable
	 * PL3ZhixuanShiweiBallTable = null;// 直选十位 BallTable
	 * PL3ZhixuanGeweiBallTable = null;// 直选个位 // 排列三和值 BallTable
	 * PL3HezhiZhixuanBallTable = null;// 和值直选 BallTable PL3HezhiZu3BallTable =
	 * null;// 和值组3 BallTable PL3HezhiZu6BallTable = null;// 和值组6
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		recycleResources();
		showMemory();
	}

	private void recycleResources() {
		PublicMethod.recycleBallTable(PL3A1Zu3DanshiBallTable);
		PublicMethod.recycleBallTable(PL3A2Zu3DanshiBallTable);

		PublicMethod.recycleBallTable(PL3BZu3DanshiBallTable);

		PublicMethod.recycleBallTable(PL3Zu3FushiBallTable);

		PublicMethod.recycleBallTable(PL3Zu6BallTable);

		PublicMethod.recycleBallTable(PL3ZhixuanBaiweiBallTable);
		PublicMethod.recycleBallTable(PL3ZhixuanShiweiBallTable);
		PublicMethod.recycleBallTable(PL3ZhixuanGeweiBallTable);

		PublicMethod.recycleBallTable(PL3HezhiZhixuanBallTable);
		PublicMethod.recycleBallTable(PL3HezhiZu3BallTable);
		PublicMethod.recycleBallTable(PL3HezhiZu6BallTable);
	}

	private void showMemory() {
		ActivityManager actMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		actMgr.getMemoryInfo(memoryInfo);

	}
}