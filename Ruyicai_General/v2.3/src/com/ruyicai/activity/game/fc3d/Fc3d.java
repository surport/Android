package com.ruyicai.activity.game.fc3d;

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
import android.graphics.Color;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.RuyicaiActivityManager;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.game.ssq.SsqActivity;
import com.ruyicai.activity.home.RuyicaiAndroid;
import com.ruyicai.activity.home.ScrollableTabActivity;
import com.ruyicai.dialog.Accoutdialog;
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

/**
 * 
 * @author 王艳玲 福彩3D玩法操作
 * 
 */
public class Fc3d extends Activity implements OnClickListener,
		SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener,
		MyDialogListener {

	// 顶部标签个数
	private int iButtonNum = 4;   

	// 首行button
	private int iCurrentButton;

	private HorizontalScrollView topBar;
	private RadioGroup.LayoutParams topButtonLayoutParams;
	private RadioGroup topButtonGroup;
	private RadioGroup topHeButtonGroup;
	private RadioGroup topZuButtonGroup;	

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
	private Vector<Balls> balls = new Vector();
	private LinearLayout zhuView;
	private Spinner jixuanZhu;
	private boolean isZhiXuanJiXuan = false;
	private boolean isZu3JiXuan = true;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean onTouch = false;
	private final int ZU = 0;
	private final int HE = 1;
	private final int DF = 2;
	private final int TOP = 3;
	private boolean isOnclik = true;// 下拉框是否响应
	private ImageButton dfButton;
	private boolean isDanshi=true;

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
				    Accoutdialog.getInstance().createAccoutdialog(Fc3d.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
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
				Toast.makeText(getBaseContext(), "系统结算，请稍后！",Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "无空闲逻辑机！", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示该号被暂停请联系客服
				break;
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				if(isFinishing() == false){
				    PublicMethod.showDialog(Fc3d.this);
				}
				// 清除高亮小球 陈晨 20100728
				if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {
					if (isZhiXuanJiXuan) {
						zhuView.removeAllViews();
					} else {
						baiEdit.setText("");
						shiEdit.setText("");
						geEdit.setText("");
						Fc3dZhixuanBaiweiBallTable.clearAllHighlights();
						Fc3dZhixuanShiweiBallTable.clearAllHighlights();
						Fc3dZhixuanGeweiBallTable.clearAllHighlights();
					}

				} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {
					if (bZu3Danshi) {
						if (isZu3JiXuan) {
							zhuView.removeAllViews();
						} else {
							baiEdit.setText("");
							shiEdit.setText("");
							geEdit.setText("");
							Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
							Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
							Fc3dBZu3DanshiBallTable.clearAllHighlights();
						}

					}
					if (bZu3Fushi) {
						hezhiEdit.setText("");
						Fc3dZu3FushiBallTable.clearAllHighlights();
					}
				} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
					hezhiEdit.setText("");
					Fc3dZu6BallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_FC3D_DANTUO) {
					Fc3dDantuoDanmaBallTable.clearAllHighlights();
					Fc3dDantuoTuomaBallTable.clearAllHighlights();
				} else if (iCurrentButton == PublicConst.BUY_FC3D_HEZHI) {
					if (iWhich == 10) {
						hezhiEdit.setText("");
						Fc3dHezhiZhixuanBallTable.clearAllHighlights();
					}
					if (iWhich == 11) {
						hezhiEdit.setText("");
						Fc3dHezhiZu3BallTable.clearAllHighlights();
					}
					if (iWhich == 12) {
						hezhiEdit.setText("");
						Fc3dHezhiZu6BallTable.clearAllHighlights();
					}
				}
				changeTextSumMoney(iWhich);
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(Fc3d.this, UserLogin.class);
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
		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);

		title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		// 获取期号和截止时间
		// issue = PublicMethod.getLotno("information4", this);

		// 获取期号和截止时间
		JSONObject fc3dLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(Constants.LOTNO_FC3D);
		if (fc3dLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				issue[0] = fc3dLotnoInfo.getString("batchCode");
				issue[1] = fc3dLotnoInfo.getString("endTime");
				term.setText("第" + this.issue[0] + "期");
				time.setText("截止时间：" + this.issue[1]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			issue[0] = "";
			issue[1] = "";
		    PublicMethod.getIssue(Constants.LOTNO_FC3D,term,time,new Handler());
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 初始化单复式按钮
		dfButton = (ImageButton) findViewById(R.id.danfushi);
		dfButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isDanshi){
					dfButton.setBackgroundResource(R.drawable.danshi_pl3);
					bZu3Danshi = true;
					bZu3Fushi = false;
					iCurrentButton = PublicConst.BUY_FC3D_ZU3;
					create_FC3D_ZU3();
				}else{
					dfButton.setBackgroundResource(R.drawable.fushi_pl3);
					 bZu3Danshi = false;
					 bZu3Fushi = true;
					 iCurrentButton = PublicConst.BUY_FC3D_ZU3;
					 create_FC3D_ZU3();
				}
				isDanshi=!isDanshi;
			}
		});

		// 7.3代码修改：“返回”Button换成ImageButton

		mHScrollView = (ScrollView) findViewById(R.id.scroll_global);
		// 获取主布局id
		buyView = (LinearLayout) findViewById(R.id.layout_buy);
		// ----- 初始化顶部按钮
		initTopButtons();
		iCurrentButton = PublicConst.BUY_FC3D_ZHIXUAN;
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
		case PublicConst.BUY_FC3D_ZHIXUAN:
			if (isZhiXuanJiXuan) {
				sensor.startAction();// 启动晃动机选
				create_FC3D_ZHIXUAN_JIXUAN();
			} else {
				sensor.stopAction();
				create_FC3D_ZHIXUAN();
			}
			topButtonGroup.setSelected(false);
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			break;
		case PublicConst.BUY_FC3D_ZU3:
			if (isZu3JiXuan) {
				sensor.startAction();// 启动晃动机选
				create_FC3D_ZU3_JIXUAN();
			} else {
				sensor.stopAction();
				bZu3Danshi = true;
				bZu3Fushi = false;
				create_FC3D_ZU3();
			}
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			break;
		case PublicConst.BUY_FC3D_ZU6:
			sensor.stopAction();
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
			create_FC3D_ZU6();
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			create_FC3D_DANTUO();
			break;
		case PublicConst.BUY_FC3D_HEZHI:
			sensor.stopAction();
			changeImg(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup);
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
		sensor.stopAction();// 停止晃动机选
		buyView.removeAllViews();
		type = HEZHI;
		topHeButtonGroup.setVisibility(RadioGroup.VISIBLE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout iV = (RelativeLayout) inflate.inflate(
				R.layout.layout_fc3d_hezhi_tab, null);

		// 将View加入主框架
		buyView.addView(iV, new RelativeLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));
		create_FC3D_HEZHI_ZHIXUAN();

	}

	/**
	 * 福彩3D和值组6
	 */
	private void create_FC3D_HEZHI_ZU6() {
		sensor.stopAction();// 停止晃动机选
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
				title.setText("福彩3D和值组6");
				int redBallViewNum = 22;
				iScreenWidth = PublicMethod.getDisplayWidth(this);
				
				PublicMethod.recycleBallTable(Fc3dHezhiZu6BallTable);
				Fc3dHezhiZu6BallTable = PublicMethod.makeBallTable(iV,
						R.id.table_hezhi_zu6, iScreenWidth, redBallViewNum, Fc3dBallResId,
						RED_FC3D_HEZHI_ZU6_START, 3, this, this);

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

				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu6);
				touzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
				newSelect = (ImageButton) iV
						.findViewById(R.id.fc3d_zhixuan_new_select);
				newSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {// 重选
						Fc3dHezhiZu6BallTable.clearAllHighlights();
						hezhiEdit.setText("");
						mTextSumMoney.setText(getResources().getString(
								R.string.please_choose_number));
					}
				});
				final TextView hezhiText = (TextView) iV
						.findViewById(R.id.fc3d_text_hezhi);
				hezhiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_hezhi);

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
			// 将View加入主框架
			buyView.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,buyView.getLayoutParams().height));

		}
	}

	/**
	 * 福彩3D和值组3
	 */
	private void create_FC3D_HEZHI_ZU3() {
		sensor.stopAction();// 停止晃动机选
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
			LinearLayout iV = (LinearLayout) inflate.inflate(R.layout.layout_fc3d_hezhi_zu3, null);
				title.setText("福彩3D和值组3");
				int redBallViewNum = 26;
				iScreenWidth = PublicMethod.getDisplayWidth(this);

				PublicMethod.recycleBallTable(Fc3dHezhiZu3BallTable);
				Fc3dHezhiZu3BallTable = PublicMethod.makeBallTable(iV,R.id.table_hezhi_zu3, iScreenWidth, redBallViewNum, Fc3dBallResId,
						RED_FC3D_HEZHI_ZU3_START, 1, this, this);

				ImageButton subtractBeishuBtn = (ImageButton) iV.findViewById(R.id.fc3d_seekbar_subtract_hezhi_zu3_beishu);
				subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

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

				touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_hezhi_zu3);
				touzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
				newSelect = (ImageButton) iV
						.findViewById(R.id.fc3d_zhixuan_new_select);
				newSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {// 重选
						Fc3dHezhiZu3BallTable.clearAllHighlights();
						hezhiEdit.setText("");
						mTextSumMoney.setText(getResources().getString(
								R.string.please_choose_number));
					}
				});
				final TextView hezhiText = (TextView) iV
						.findViewById(R.id.fc3d_text_hezhi);
				hezhiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_hezhi);

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
			// 将View加入主框架
			buyView
					.addView(iV, new LinearLayout.LayoutParams(buyView
							.getLayoutParams().width,
							buyView.getLayoutParams().height));

		}
	}

	/**
	 * 福彩3D和值直选
	 */
	private void create_FC3D_HEZHI_ZHIXUAN() {
		sensor.stopAction();// 停止晃动机选
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
				title.setText("福彩3D和值直选");
				int redBallViewNum = 28;
				iScreenWidth = PublicMethod.getDisplayWidth(this);
				
				PublicMethod.recycleBallTable(Fc3dHezhiZhixuanBallTable);
				Fc3dHezhiZhixuanBallTable = PublicMethod.makeBallTable(iV,
						R.id.table_hezhi_zhixuan, iScreenWidth, redBallViewNum, Fc3dBallResId,
						RED_FC3D_HEZHI_ZHIXUAN_START, 0, this, this);

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

				touzhu = (ImageButton) iV
						.findViewById(R.id.b_touzhu_hezhi_zhixuan);
				touzhu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						beginTouZhu();
					}

				});
				newSelect = (ImageButton) iV
						.findViewById(R.id.fc3d_zhixuan_new_select);
				newSelect.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {// 重选
						Fc3dHezhiZhixuanBallTable.clearAllHighlights();
						hezhiEdit.setText("");
						mTextSumMoney.setText(getResources().getString(
								R.string.please_choose_number));
					}
				});

				final TextView hezhiText = (TextView) iV
						.findViewById(R.id.fc3d_text_hezhi);
				hezhiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_hezhi);

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
	 * 福彩3D直选
	 */
	private void create_FC3D_ZHIXUAN() {
		sensor.stopAction();// 停止晃动机选
		buyView.removeAllViews();
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		topZuButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);

		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_zhixuan, null);
			title.setText("福彩3D直选");
			int redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			PublicMethod.recycleBallTable(Fc3dZhixuanBaiweiBallTable);
			Fc3dZhixuanBaiweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_baiwei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_BAIWEI_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dZhixuanShiweiBallTable);
			Fc3dZhixuanShiweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_shiwei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_SHIWEI_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dZhixuanGeweiBallTable);
			Fc3dZhixuanGeweiBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_zhixuan_gewei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZHIXUAN_GEWEI_START, 0, this, this);

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

			ImageButton subtractQihaoBtn = (ImageButton) iV.findViewById(R.id.fc3d_seekbar_subtract_zhixuan_qihao);
			subtractQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {
				@Override
				public void onClick(View v) {
					mSeekBarQishu.setProgress(--iProgressQishu);
				}
			});

			ImageButton addQihaoBtn = (ImageButton) iV.findViewById(R.id.fc3d_seekbar_add_zhixuan_qihao);
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

			newSelect = (ImageButton) iV
					.findViewById(R.id.fc3d_zhixuan_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					baiEdit.setText("");
					shiEdit.setText("");
					geEdit.setText("");
					Fc3dZhixuanBaiweiBallTable.clearAllHighlights();
					Fc3dZhixuanShiweiBallTable.clearAllHighlights();
					Fc3dZhixuanGeweiBallTable.clearAllHighlights();
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final LinearLayout baiArea = (LinearLayout) iV
					.findViewById(R.id.fc3d_bai_balls_layout);
			final LinearLayout shiArea = (LinearLayout) iV
					.findViewById(R.id.fc3d_shi_balls_layout);
			final LinearLayout geArea = (LinearLayout) iV
					.findViewById(R.id.fc3d_ge_balls_layout);
			shiArea.setVisibility(LinearLayout.GONE);
			geArea.setVisibility(LinearLayout.GONE);
			final TextView baiText = (TextView) iV
					.findViewById(R.id.fc3d_text_bai);
			final TextView shiText = (TextView) iV
					.findViewById(R.id.fc3d_text_shi);
			final TextView geText = (TextView) iV
					.findViewById(R.id.fc3d_text_ge);
			baiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_bai);
			shiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_shi);
			geEdit = (EditText) iV.findViewById(R.id.fc3d_edit_ge);

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
		// 将View加入主框架
		buyView.addView(iV, new LinearLayout.LayoutParams(buyView
				.getLayoutParams().width, buyView.getLayoutParams().height));

	}

	/**
	 * 福彩3D胆拖
	 */
	private void create_FC3D_DANTUO() {
		sensor.stopAction();// 停止晃动机选
		buyView.removeAllViews();
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_dantuo, null);
		{
			int redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			PublicMethod.recycleBallTable(Fc3dDantuoDanmaBallTable);
			Fc3dDantuoDanmaBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_dantuo_danma, iScreenWidth, redBallViewNum,Fc3dBallResId,
					RED_FC3D_DANTUO_DANMA_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dDantuoTuomaBallTable);
			Fc3dDantuoTuomaBallTable = PublicMethod.makeBallTable(iV,
					R.id.table_dantuo_tuoma, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_DANTUO_TUOMA_START, 0, this, this);

			mTextSumMoney = (TextView) iV
					.findViewById(R.id.text_sum_money_dantuo);
			mTextSumMoney.setText(getResources().getString(
					R.string.please_choose_number));

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

			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_dantuo);
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

	}

	/**
	 * 福彩3D组6
	 */
	private void create_FC3D_ZU6() {
		sensor.stopAction();// 停止晃动机选
		buyView.removeAllViews();
		type = ZUXUAN;
		topZuButtonGroup.setVisibility(RadioGroup.VISIBLE);
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.GONE);

		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout iV = (LinearLayout) inflate.inflate(
				R.layout.layout_fc3d_zu6, null);
		{
			title.setText("福彩3D组6");
			int redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			PublicMethod.recycleBallTable(Fc3dZu6BallTable);
			Fc3dZu6BallTable = PublicMethod.makeBallTable(iV, R.id.table_zu6,
					iScreenWidth, redBallViewNum, 
					Fc3dBallResId, RED_FC3D_ZU6_START, 0, this, this);

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

			touzhu = (ImageButton) iV.findViewById(R.id.b_touzhu_zu6);
			touzhu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					beginTouZhu();
				}

			});
			newSelect = (ImageButton) iV
					.findViewById(R.id.fc3d_zhixuan_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					Fc3dZu6BallTable.clearAllHighlights();
					hezhiEdit.setText("");
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final TextView hezhiText = (TextView) iV
					.findViewById(R.id.fc3d_text_hezhi);
			hezhiEdit = (EditText) iV.findViewById(R.id.fc3d_edit_hezhi);

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
	 * 福彩3D组3
	 */
	private void create_FC3D_ZU3() {
		sensor.stopAction();// 停止晃动机选
		// 王艳玲 7.6 点击投注按钮，提示条件不明确
		buyView.removeAllViews();
		type = ZUXUAN;
		topZuButtonGroup.setVisibility(RadioGroup.VISIBLE);
		topHeButtonGroup.setVisibility(RadioGroup.GONE);
		dfButton.setVisibility(ImageButton.VISIBLE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inflate.inflate(R.layout.layout_fc3d_zu3,
				null);
		{
			title.setText("福彩3D组3");
			redBallViewNum = 10;
			iScreenWidth = PublicMethod.getDisplayWidth(this);
			
			
			PublicMethod.recycleBallTable(Fc3dZhixuanGeweiBallTable);
			Fc3dA1Zu3DanshiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_danshi_baiwei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_BAIWEI_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dA2Zu3DanshiBallTable);
			Fc3dA2Zu3DanshiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_danshi_shiwei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_SHIWEI_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dBZu3DanshiBallTable);
			Fc3dBZu3DanshiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_danshi_gewei, iScreenWidth, redBallViewNum, Fc3dBallResId,
					RED_FC3D_ZU3_DANSHI_GEWEI_START, 0, this, this);
			
			PublicMethod.recycleBallTable(Fc3dZu3FushiBallTable);
			Fc3dZu3FushiBallTable = PublicMethod.makeBallTable(linearLayout,
					R.id.table_zu3_fushi, iScreenWidth, redBallViewNum, Fc3dBallResId, RED_FC3D_ZU3_FUSHI_START,
					0, this, this);

			ImageButton subtractBeishuBtn = (ImageButton) linearLayout.findViewById(R.id.fc3d_seekbar_subtract_zu3_beishu);
			subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

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

			mTextSumMoney = (TextView) linearLayout.findViewById(R.id.text_sum_money_zu3);
			mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));

			mSeekBarBeishu = (SeekBar) linearLayout.findViewById(R.id.seek_beishu);
			mSeekBarBeishu.setOnSeekBarChangeListener(this);
			mSeekBarBeishu.setProgress(iProgressBeishu);

			mSeekBarQishu = (SeekBar) linearLayout.findViewById(R.id.seek_qishu);
			mSeekBarQishu.setOnSeekBarChangeListener(this);
			mSeekBarQishu.setProgress(iProgressQishu);

			mTextBeishu = (TextView) linearLayout.findViewById(R.id.text_beishu);
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
					.findViewById(R.id.fc3d_zhixuan_new_select);
			newSelect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {// 重选
					Fc3dA1Zu3DanshiBallTable.clearAllHighlights();
					Fc3dA2Zu3DanshiBallTable.clearAllHighlights();
					Fc3dBZu3DanshiBallTable.clearAllHighlights();
					Fc3dZu3FushiBallTable.clearAllHighlights();
					baiEdit.setText("");
					shiEdit.setText("");
					geEdit.setText("");
					hezhiEdit.setText("");
					mTextSumMoney.setText(getResources().getString(
							R.string.please_choose_number));
				}
			});
			final LinearLayout danshiArea = (LinearLayout) linearLayout.findViewById(R.id.danshi_balls_layout);
			final LinearLayout fushiArea = (LinearLayout) linearLayout.findViewById(R.id.fushi_balls_layout);
			final LinearLayout baiArea = (LinearLayout) linearLayout.findViewById(R.id.fc3d_bai_balls_layout);
			final LinearLayout shiArea = (LinearLayout) linearLayout.findViewById(R.id.fc3d_shi_balls_layout);
			final LinearLayout geArea = (LinearLayout) linearLayout.findViewById(R.id.fc3d_ge_balls_layout);
			final TextView baiText = (TextView) linearLayout.findViewById(R.id.fc3d_text_bai);
			final TextView shiText = (TextView) linearLayout.findViewById(R.id.fc3d_text_shi);
			final TextView geText = (TextView) linearLayout.findViewById(R.id.fc3d_text_ge);
			baiEdit = (EditText) linearLayout.findViewById(R.id.fc3d_edit_bai);
			shiEdit = (EditText) linearLayout.findViewById(R.id.fc3d_edit_shi);
			geEdit = (EditText) linearLayout.findViewById(R.id.fc3d_edit_ge);

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
					.findViewById(R.id.fc3d_text_hezhi);
			hezhiEdit = (EditText) linearLayout
					.findViewById(R.id.fc3d_edit_hezhi);

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
	private void create_FC3D_ZU3_JIXUAN() {
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

			title.setText("福彩3D组三机选");
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
	private void create_FC3D_ZHIXUAN_JIXUAN() {
		sensor.startAction();// 启动晃动机选
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

			title.setText("福彩3D机选");
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
//		topBar = (HorizontalScrollView) findViewById(R.id.topBar);
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

		commit(iButtonNum, topButtonIdOn, topButtonIdOff, topButtonGroup, TOP);
		commit(2, zuTopButtonIdOn, zuTopButtonIdOff, topZuButtonGroup, ZU);
		commit(3, heTopButtonIdOn, heTopButtonIdOff, topHeButtonGroup, HE);

	}

	/**
	 * RadioGroup提交操作
	 * 
	 * @param void
	 */
	public void commit(int iButtonNum, int[] topButtonIdon,
			int[] topButtonIdonoff, RadioGroup top, int type) {
		top.removeAllViews();
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
			}else{
				width = PublicConst.IMG_WITH + 220;
			}
			break;
		case TOP:
			width = screen_width / iButtonNum;
			break;
		}
		
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < iButtonNum; i++) {

			TabBarButton tabButton = new TabBarButton(this);

			tabButton.setState(topButtonIdon[i], topButtonIdonoff[i],4);
			tabButton.setId(i);
			if (i != iButtonNum - 1 || type == TOP||Constants.SCREEN_WIDTH != 320) {
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
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
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

		tabButton.setState(topButtonIdon[1], topButtonIdonoff[1],4);
		tabButton.setId(1);
		topButtonLayoutParams = new RadioGroup.LayoutParams(width,
				RadioGroup.LayoutParams.WRAP_CONTENT);
		top.addView(tabButton, 1, topButtonLayoutParams);

		TabBarButton tabButtonTwo = new TabBarButton(this);
		tabButtonTwo.setState(topButtonIdon[2], topButtonIdonoff[2],4);
		tabButtonTwo.setId(2);
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU6) {
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
			iCurrentButton = PublicConst.BUY_FC3D_ZHIXUAN;
			createBuyView(iCurrentButton);
		} else if (type == ZUXUAN) {
			topButtonGroup.check(1);// 组3
			iCurrentButton = PublicConst.BUY_FC3D_ZU6;
			createBuyView(iCurrentButton);
		} else if (type == HEZHI) {// 和值-直选
			topButtonGroup.check(index);
			// create_FC3D_HEZHI_ZHIXUAN();
			iCurrentButton = PublicConst.BUY_FC3D_HEZHI;
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
				Intent intent1 = new Intent(Fc3d.this, RuyicaiAndroid.class);
				startActivity(intent1);
				finish();
				break;
			case 1:// 直选
				iCurrentButton = PublicConst.BUY_FC3D_ZHIXUAN;
				isZhiXuanJiXuan = !isZhiXuanJiXuan;
				if (!isZhiXuanJiXuan && !isZu3JiXuan) {
					isZu3JiXuan = true;
				}
				createBuyView(iCurrentButton);
				break;
			case 2:// 组选
				iCurrentButton = PublicConst.BUY_FC3D_ZU3;
				isZu3JiXuan = !isZu3JiXuan;
				if (!isZhiXuanJiXuan && !isZu3JiXuan) {
					isZhiXuanJiXuan = true;
				}
				topZuButtonGroup.check(0);
				createBuyView(iCurrentButton);
				break;
			case 3:// 和值
				sensor.stopAction();
				iCurrentButton = PublicConst.BUY_FC3D_HEZHI;

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
				create_FC3D_HEZHI_ZHIXUAN();
				break;
			// 和值-组3
			case 1:

				create_FC3D_HEZHI_ZU3();
				break;
			// 和值-组6
			case 2:
				create_FC3D_HEZHI_ZU6();
				break;
			}
			break;
		case R.id.zuxuanMenu:
			switch (checkedId) {
			// 组3
			case 0:
				iCurrentButton = PublicConst.BUY_FC3D_ZU3;
				createBuyView(iCurrentButton);
				break;
			// 组6
			case 1:
				iCurrentButton = PublicConst.BUY_FC3D_ZU6;
				createBuyView(iCurrentButton);
				break;
			}
			break;
		// case R.id.danfushiMenu:
		// switch (checkedId) {
		// // 组3单式
		// case 0:
		// bZu3Danshi = true;
		// bZu3Fushi = false;
		// iCurrentButton = PublicConst.BUY_FC3D_ZU3;
		// create_FC3D_ZU3();
		// break;
		// // 组三复式
		// case 1:
		// bZu3Danshi = false;
		// bZu3Fushi = true;
		// iCurrentButton = PublicConst.BUY_FC3D_ZU3;
		// create_FC3D_ZU3();
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
			// 福彩3D组六
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
			if (isZhiXuanJiXuan) {
				iReturnValue = balls.size();
			}
			break;

		// 福彩3D组3复式
		case PublicConst.BUY_FC3D_ZU3:
			if (isZu3JiXuan) {
				iReturnValue = balls.size();
			}else if (bZu3Danshi) {// 添加组3单式注数获取20100912 陈晨
				// 单式注数永为1
				iReturnValue = 1;
			}else if (bZu3Fushi) {// 组3 复式注数的获取 20100912 陈晨
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
		int[] BallNos = Fc3dHezhiZhixuanBallTable.getHighlightBallNOs();// 被选择小球的号码（点击1，获得1）
		int[] BallNoZhushus = { 1, 3, 6, 10, 15, 21, 28, 36, 45, 55, 63, 69,
				73, 75, 75, 73, 69, 63, 55, 45, 36, 28, 21, 15, 10, 6, 3, 1 };// 0~27

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
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
		int[] BallNos = Fc3dHezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）
		int[] BallNoZhushus = { 1, 1, 2, 3, 4, 5, 7, 8, 9, 10, 10, 10, 10, 9,
				8, 7, 5, 4, 3, 2, 1, 1 };// 3~24

		for (int i = 0; i < BallNos.length; i++) {
			for (int j = 0; j < BallNoZhushus.length; j++) {
				if (j == BallNos[i] - 3) {// 因为数组是从0开始的，小球实际Id从1开始，故减去1
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

				int iZhuShu = getZhuShu() * iProgressBeishu;
				String temp = "当前玩法为和值直选，共"
						+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
				mTextSumMoney.setText(temp);
			}
			// 福彩3D和值组3
			if (aWhichGroupBall == 11) {
				int[] BallNos = Fc3dHezhiZu3BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得3）

				int iZhuShu = getZhuShu() * iProgressBeishu;
				String temp = "当前玩法为和值组3，共"
						+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
				mTextSumMoney.setText(temp);
			}
			// 福彩3D和值组6
			if (aWhichGroupBall == 12) {
				int[] BallNos = Fc3dHezhiZu6BallTable.getHighlightBallNOs();// 被选择小球的号码（点击3，获得1）

				int iZhuShu = getZhuShu() * iProgressBeishu;
				String temp = "当前玩法为和值组6，共"
						+ (iZhuShu + "注，共" + (iZhuShu * 2) + "元");
				mTextSumMoney.setText(temp);
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
			// wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZu6BallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZu6BallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu6ZhuMa = Fc3dHezhiZu6BallTable.getHighlightBallNOs();
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
	private void buy_FC3D_HEZHI_ZU3(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 11) { // 和值组3
			int iChosenBallSum = 1;// 只允许选择1个
			// wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZu3BallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZu3BallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu3ZhuMa = Fc3dHezhiZu3BallTable.getHighlightBallNOs();
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
	private void buy_FC3D_HEZHI_ZHIXUAN(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 10) { // 和值直选
			int iChosenBallSum = 1;// 只允许选择1个
			// wangyl 7.29 修改1693 解决若已有1小球高亮，其他小球不可点
			Fc3dHezhiZhixuanBallTable.clearAllHighlights();
			int isHighLight = Fc3dHezhiZhixuanBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zhiZhuMa = Fc3dHezhiZhixuanBallTable.getHighlightBallNOs();
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

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] baiZhuMa = Fc3dZhixuanBaiweiBallTable.getHighlightBallNOs();
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
			int isHighLight = Fc3dZhixuanShiweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] shiZhuMa = Fc3dZhixuanShiweiBallTable.getHighlightBallNOs();
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
			int isHighLight = Fc3dZhixuanGeweiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] geZhuMa = Fc3dZhixuanGeweiBallTable.getHighlightBallNOs();
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

				Fc3dDantuoTuomaBallTable.clearOnBallHighlight(aBallViewId);
			}
		}
		if (aWhichGroupBall == 6) { // 拖码1~9个
			int iChosenBallSum = 9;
			int isHighLight = Fc3dDantuoTuomaBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT) {

				Fc3dDantuoDanmaBallTable.clearOnBallHighlight(aBallViewId);
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

			String zhuma_string = "  ";
			int[] zu6ZhuMa = Fc3dZu6BallTable.getHighlightBallNOs();
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
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT&&Fc3dBZu3DanshiBallTable.getOneBallStatue(aBallViewId)!=0) {
				Fc3dBZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,getResources().getString(R.string.pl3_toast_bai_title), Toast.LENGTH_SHORT).show();
			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 1) {
			int iChosenBallSum = 1;
			// 第三个不可以重复
			// 每次点击后记住小球的状态
			int isHighLightA1 = Fc3dA1Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			int isHighLightA2 = Fc3dA2Zu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLightA1 == PublicConst.BALL_TO_HIGHLIGHT
					&& isHighLightA2 == PublicConst.BALL_TO_HIGHLIGHT&&Fc3dBZu3DanshiBallTable.getOneBallStatue(aBallViewId)!=0) {
				Fc3dBZu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,getResources().getString(R.string.pl3_toast_bai_title), Toast.LENGTH_SHORT).show();
			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 2) {
			int iChosenBallSum = 1;
			// 组3,前两组相同，第三组不能与之相同

			int isHighLight = Fc3dBZu3DanshiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);
			if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&&Fc3dA1Zu3DanshiBallTable.getOneBallStatue(aBallViewId)!=0) {
				Fc3dA1Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Fc3dA2Zu3DanshiBallTable.clearOnBallHighlight(aBallViewId);
				Toast.makeText(this,getResources().getString(R.string.pl3_toast_ge_title), Toast.LENGTH_SHORT).show();

			}
			isZu3Danshi();
		} else if (aWhichGroupBall == 3) {
			int iChosenBallSum = 10;
			int isHighLight = Fc3dZu3FushiBallTable.changeBallState(
					iChosenBallSum, aBallViewId);

			// 记录到输入框fulei
			String zhuma_string = "  ";
			int[] zu3ZhuMa = Fc3dZu3FushiBallTable.getHighlightBallNOs();
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
		int[] baiZhuMa = Fc3dA1Zu3DanshiBallTable.getHighlightBallNOs();
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
		int[] shiZhuMa = Fc3dA2Zu3DanshiBallTable.getHighlightBallNOs();
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
		int[] geZhuMa = Fc3dBZu3DanshiBallTable.getHighlightBallNOs();
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
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(Fc3d.this,
				"addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		// 获取期数 陈晨 20100711
		int iQiShu = getQiShu();
		// 投注时判断是否登录
		if (sessionIdStr == null || sessionIdStr.equals("")) {
			Intent intentSession = new Intent(Fc3d.this, UserLogin.class);
			startActivityForResult(intentSession,0);
		} else {

			// 福彩3D直选
			if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) {
				if (isZhiXuanJiXuan) {
			
						// 陈晨 20100728 判断条件加限制，可以选择6个红球1个蓝球
				    if (balls.size() == 0) {
						PublicMethod.alertJiXuan("请至少选择1注彩票", this);
					} else {
						String sTouzhuAlert = "";
						sTouzhuAlert = getTouzhuAlertJixuan();
						alert(sTouzhuAlert);
					}
				} else {
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
					int[] geweis = Fc3dZhixuanGeweiBallTable
							.getHighlightBallNOs();
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
						DialogTouzhu("请在百位，十位，个位均至少选择一个小球后再投注");
					} else {
						int iZhuShu = getZhuShu() * iProgressBeishu;
						// alert("您选择的是"+iZhuShu+"注福彩3D直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						// wangyl 7.23 投注不能大于600注
						if (iZhuShu / iProgressBeishu > 600) {
							DialogZhixuan();
						} else if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {
							alert(  "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + "\n" + "百位：" + baiweistr + "\n"
									+ "十位：" + shiweistr + "\n" + "个位："
									+ geweistr + "\n" + "确认支付吗？");
						}
					}
				}
			}
			// 福彩3D组3
			if (iCurrentButton == PublicConst.BUY_FC3D_ZU3) {

				int iZhuShu = 0;
			   if (isZu3JiXuan) {
					
					// 陈晨 20100728 判断条件加限制，可以选择6个红球1个蓝球
				   if (balls.size() == 0) {
					 PublicMethod.alertJiXuan("请至少选择1注彩票", this);
				    } else {
					  String zhuma=zhuma_fc3d();
					  Log.e("zhuma====",zhuma);
					  String sTouzhuAlert = "";
					  sTouzhuAlert = getTouzhuAlertJixuan();
					  alert(sTouzhuAlert);
				    }
			    }else if (bZu3Danshi) {
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
									+ "";// 前2位相同
							String geweistr = Fc3dBZu3DanshiBallTable
									.getHighlightBallNOs()[0]
									+ "";// 前2位相同
							// alert("您选择的是"+iZhuShu+"注福彩3D组3单式彩票，"+"共计"+(iZhuShu*2)+"元");
							// wangyl 7.13 投注不能大于10万
							if (iZhuShu * 2 > 100000) {
								DialogExcessive();
							} else {
								// wangyl 7.13 注数错误
								alert( "注数：" + 1 + "注"
										+ "\n" + "倍数：" + iProgressBeishu + "倍"
										+ "\n" + "追号：" + iProgressQishu + "期"
										+ "\n" + "金额：" + iZhuShu * 2 + "元"
										+ "\n" + "冻结金额："
										+ (2 * (iProgressQishu - 1) * iZhuShu)
										+ "元" + "\n" + "注码：" + baiweistr + "," + baiweistr + ","
										+ geweistr + "\n" + "确认支付吗？");
							}
						}
				}else if (bZu3Fushi) {
					if (Fc3dZu3FushiBallTable.getHighlightBallNums() < 2) {
						DialogTouzhu("请至少选择2个小球后再投注");
					} else {
						// wangyl 7.12 修改确定投注成功后的对话框
						int[] fushiNums = Fc3dZu3FushiBallTable
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

						// alert("您选择的是"+iZhuShu+"注福彩3D组3复式彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {

							alert( "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n" + "确认支付吗？");
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
						fushiStr += (fushiNums[i]) + ",";
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

						alert(  "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" + "冻结金额："
								+ (2 * (iProgressQishu - 1) * iZhuShu) + "元"
								+ "\n" + "注码：" + fushiStr + "\n" + "确认支付吗？");
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
					danmastr += (danma[i] - 1) + ",";
					if (i == danmaNums - 1) {
						danmastr = danmastr.substring(0, danmastr.length() - 1);
					}
				}
				for (int i = 0; i < tuomaNums; i++) {
					tuomastr += (tuoma[i] - 1) + ",";
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

						alert(  "注数："
								+ iZhuShu / iProgressBeishu + "注" + "\n"
								+ "倍数：" + iProgressBeishu + "倍" + "\n" + "追号："
								+ iProgressQishu + "期" + "\n" + "金额："
								+ iZhuShu * 2 + "元" + "\n" + "冻结金额："
								+ (2 * (iProgressQishu - 1) * iZhuShu) + "元"
								+ "\n" +"注码：" + "\n" + "胆码：" + danmastr + "\n"
								+ "拖码：" + tuomastr + "\n" +  "确认支付吗？");
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
								+ "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {

							alert( "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n" + "确认支付吗？");
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
						// wangyl 7.12 修改确定投注成功后的对话框
						String fushiStr = Fc3dHezhiZu3BallTable
								.getHighlightBallNOs()[0]
								+ "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {

							alert( "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" + "注码：" + fushiStr + "\n" + "确认支付吗？");
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
								+ "";
						// alert("您选择的是"+iZhuShu+"注福彩3D和值直选彩票，"+"共计"+(iZhuShu*2)+"元");
						// wangyl 7.13 投注不能大于10万
						if (iZhuShu * 2 > 100000) {
							DialogExcessive();
						} else {

							alert( "注数："
									+ iZhuShu / iProgressBeishu + "注" + "\n"
									+ "倍数：" + iProgressBeishu + "倍" + "\n"
									+ "追号：" + iProgressQishu + "期" + "\n"
									+ "金额：" + iZhuShu * 2 + "元" + "\n"
									+ "冻结金额："
									+ (2 * (iProgressQishu - 1) * iZhuShu)
									+ "元" + "\n" +"注码：" + fushiStr + "\n" +  "确认支付吗？");
						}
					}

				}
			}
		}
	}

	// 选择小球
	public void onOkClick(int aRedNum, int aBlueNum) {
	}

	public void onOkClick(int[] aReturn) {
		switch (iCurrentButton) {
		case PublicConst.BUY_FC3D_ZHIXUAN:
			Fc3dZhixuanBaiweiBallTable.randomChooseConfigChangeFc3d(aReturn[0],
					7, null);
			Fc3dZhixuanShiweiBallTable.randomChooseConfigChangeFc3d(aReturn[1],
					8, null);
			Fc3dZhixuanGeweiBallTable.randomChooseConfigChangeFc3d(aReturn[2],
					9, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_ZU3:
			Fc3dZu3FushiBallTable.randomChooseConfigChangeFc3d(aReturn[0], 3,
					null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_ZU6:
			Fc3dZu6BallTable.randomChooseConfigChangeFc3d(aReturn[0], 4, null);
			changeTextSumMoney(0);
			break;
		case PublicConst.BUY_FC3D_DANTUO:
			Fc3dDantuoDanmaBallTable.clearAllHighlights();
			Fc3dDantuoTuomaBallTable.clearAllHighlights();

			// 为避免胆码和拖码重复，先获得一组随机数，在分别分给胆码和拖码
			int[] randomNums = PublicMethod.getRandomsWithoutCollision(
					aReturn[0] + aReturn[1], 0, 9);

			Fc3dDantuoDanmaBallTable.randomChooseConfigChangeFc3d(aReturn[0],
					5, randomNums);
			Fc3dDantuoTuomaBallTable.randomChooseConfigChangeFc3d(aReturn[1],
					6, randomNums);

			changeTextSumMoney(0);
			break;
		default:
			break;
		}
	}

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
		BettingInterface betting = BettingInterface.getInstance();

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,
				"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");

		String error_code = betting.bettingNew(bets, count, amount, sessionid);
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
		if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN && !isZhiXuanJiXuan) {

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
				String[] str = { "01", "0" + (zhuma_baiwei[0]), "^", "01",
						"0" + (zhuma_shiwei[0]), "^", "01",
						"0" + (zhuma_gewei[0]) };
				zhuma = str;
			} else {
				// 3D直选复式注码玩法 2010/7/4 陈晨
				if (zhuma_baiwei.length != 0 && zhuma_shiwei.length != 0&& zhuma_gewei.length != 0) {
					typeCode = zxfs;
					String[] str = new String[zhuma_baiwei.length
							+ zhuma_shiwei.length + zhuma_gewei.length + 5];
					if (zhuma_baiwei.length < 10) {
						str[0] = "0" + zhuma_baiwei.length;
					} else {
						str[0] = zhuma_baiwei.length + "";
					}
					for (int i = 0; i < zhuma_baiwei.length; i++) {
						str[i + 1] = "0" + (zhuma_baiwei[i]);
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
								+ (zhuma_shiwei[i]);
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
								+ (zhuma_gewei[i]);
					}
					zhuma = str;
				}

			}

		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && !isZu3JiXuan) {
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
					String[] str = { "0" + (zhuma_zu3danshi_A1[0]),
							"0" + (zhuma_zu3danshi_A2[0]),
							"0" + (zhuma_zu3danshi_A3[0]) };
					zhuma = str;
				} else {
					String[] str = { "0" + (zhuma_zu3danshi_A3[0]),
							"0" + (zhuma_zu3danshi_A2[0]),
							"0" + (zhuma_zu3danshi_A1[0]) };
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
					str[i + 1] = "0" + (zhuma_zu3fushi[i]);
				}
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
					str[i + 1] = "0" + (zhuma_zu6danfushi[i]);
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
					str[i + 1] = "0" + (zhuma_zu6danfushi[i]);
				}

			}

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
				str[i] = "0" + (zhuma_dantuo_danma[i]);
			}
			str[zhuma_dantuo_danma.length] = "*";
			for (int j = 0; j < zhuma_dantuo_tuoma.length; j++) {
				str[zhuma_dantuo_danma.length + 1 + j] = "0"
						+ (zhuma_dantuo_tuoma[j]);
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
					String[] str = { "0" + (zhuma_zhixuanhezhi[0]) };
					zhuma = str;
				} else {
					String[] str = { "" + (zhuma_zhixuanhezhi[0]) };

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
					String[] str = { "0" + (zhuma_zu6hezhi[0]) };
					zhuma = str;
				} else {
					String[] str = { "" + (zhuma_zu6hezhi[0]) };
					zhuma = str;
				}
			}

		} else if ((iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN)
				&& isZhiXuanJiXuan) {

			typeCode = zxfs;
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && isZu3JiXuan) {

			typeCode = z3ds;
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

		if ((iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN) && isZhiXuanJiXuan) {// 直选单式机选
			t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-";
			for (int i = 0; i < balls.size(); i++) {
				t_str+="00"+beishu_;
				int bai = balls.get(i).getBai()[0] + 1;
				int shi = balls.get(i).getShi()[0] + 1;
				int ge = balls.get(i).getGe()[0] + 1;
				t_str +=  "0" + bai + "0" + shi + "0" + ge + "^";
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && isZu3JiXuan) {// 组3单式机选,百位和十位相同
			t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-";
			for (int i = 0; i < balls.size(); i++) {
				t_str+="01"+beishu_;
				int bai = balls.get(i).getBai()[0] + 1;
				int shi = bai;
				int ge = balls.get(i).getGe()[0] + 1;
				// 需要排序
				if (bai < ge) {
					t_str += "0" + bai + "0" + shi + "0" + ge + "^";
				} else {
					t_str += "0" + ge + "0" + shi + "0" + bai + "^";
				}
			}

		} else {
			t_str += CityCode + DDD_falg + typeCode + "-" + zhushu + "-" + typeCode
			+ beishu_;
			for (int i = 0; i < zhuma.length; i++) {
				t_str += zhuma[i];
			}
			t_str += endCode;
		}

		return t_str;
	}

	/**
	 * 投注不满足条件时弹出的对话框
	 * 
	 * @param message
	 */
	private void DialogTouzhu(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Fc3d.this);
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
		AlertDialog.Builder builder = new AlertDialog.Builder(Fc3d.this);
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

	// wangyl 7.23 福彩3D投注注数大于600注时的对话框
	/**
	 * 福彩3D直选投注注数大于600注时的对话框
	 */
	private void DialogZhixuan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Fc3d.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title).toString());
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
	 * MENU里的玩法介绍
	 */
	private void dialogGameIntroduction() {

		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_fc3d.html";
		;
		webView.loadUrl(url);

		AlertDialog.Builder builder = new AlertDialog.Builder(Fc3d.this);
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
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQiShu();
									String str = "00";

									@Override
									public void run() {

										String zhuma = zhuma_fc3d();

										// TODO Auto-generated method stub
										str = payNew(zhuma, iQiShu + "",iZhuShu * iProgressBeishu * 200* iQiShu + "");
										touzhu.setClickable(true);
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
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
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
			if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && isZu3JiXuan) {
			    int[] baiGeNum = PublicMethod.getRandomsWithoutCollision(2, -1, 8);
			    baiNum = new int[1];    
			    shiNum = new int[1];   
			    geNum = new int[1];   
			    shiNum[0]=baiNum[0]=baiGeNum[0];
			    geNum[0]=baiGeNum[1];
			} else if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN && isZhiXuanJiXuan) {
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
					RED_FC3D_ZHIXUAN_BAIWEI_START);
			BallTable shiweiBallTable = new BallTable(lines,
					RED_FC3D_ZHIXUAN_SHIWEI_START);
			BallTable geweiBallTable = new BallTable(lines,
					RED_FC3D_ZHIXUAN_GEWEI_START);
			if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && isZu3JiXuan) {
				PublicMethod.makeBallTableJiXuan(baiweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getBai(),this);
				PublicMethod.makeBallTableJiXuan(shiweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getBai(),this);
				PublicMethod.makeBallTableJiXuan(geweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getGe(),this);
			} else if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN && isZhiXuanJiXuan) {
				PublicMethod.makeBallTableJiXuan(baiweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getBai(),this);
				PublicMethod.makeBallTableJiXuan(shiweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getShi(),this);
				PublicMethod.makeBallTableJiXuan(geweiBallTable, iScreenWidth, Fc3dBallResId, balls.get(i).getGe(),this);
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
								Fc3d.this,
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
	 * 直选机选投注提示框中的信息
	 */
	private String getTouzhuAlertJixuan() {
		String zhumaString = "";
		if (iCurrentButton == PublicConst.BUY_FC3D_ZU3 && isZu3JiXuan) {
			for (int i = 0; i < balls.size(); i++) {
				zhumaString += balls.get(i).getBaiZhu() + ","
						+ balls.get(i).getBaiZhu() + ","
						+ balls.get(i).getGeZhu() + "\n";
			}
		} else if (iCurrentButton == PublicConst.BUY_FC3D_ZHIXUAN
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
				+ "\n" + "注码：" + "\n" + zhumaString + "确认支付吗？";
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
	
	private void recycleBallTable(BallTable balltable){
		if(balltable != null && balltable.getBallViews()!= null){
			for (Iterator iterator = balltable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		showMemory();
		super.onDestroy();
		recycleBallTable(Fc3dA1Zu3DanshiBallTable);
		recycleBallTable(Fc3dA2Zu3DanshiBallTable);
		recycleBallTable(Fc3dBZu3DanshiBallTable);
		recycleBallTable(Fc3dZu3FushiBallTable);
		recycleBallTable(Fc3dZu6BallTable);
		
		recycleBallTable(Fc3dDantuoDanmaBallTable);
		recycleBallTable(Fc3dDantuoTuomaBallTable);
		
		recycleBallTable(Fc3dZhixuanBaiweiBallTable);
		recycleBallTable(Fc3dZhixuanShiweiBallTable);
		recycleBallTable(Fc3dZhixuanGeweiBallTable);
		
		recycleBallTable(Fc3dHezhiZhixuanBallTable);
		recycleBallTable(Fc3dHezhiZu3BallTable);
		recycleBallTable(Fc3dHezhiZu6BallTable);
		
		showMemory();
	}
	
	private void showMemory() {
		 ActivityManager actMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
             ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
             actMgr.getMemoryInfo(memoryInfo);
	}
}