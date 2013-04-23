package com.ruyicai.activity.game.dlt;

/**
 * @Title 大乐透玩法 Activity类
 * @Description: 
 * @Copyright: Copyright (c) 2009
 * @Company: PalmDream
 * @author FanYaJun
 * @version 1.0 20100618
 */

import java.util.Iterator;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.game.ssq.SsqActivity;
import com.ruyicai.dialog.Accoutdialog;
import com.ruyicai.net.transaction.BettingInterface;
import com.ruyicai.pojo.BallBetPublicClass;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;
import com.ruyicai.view.OneBallView;

public class DLT extends Activity /*事件 回调函数*/implements OnClickListener, /*小球被点击 onClick*/SeekBar.OnSeekBarChangeListener /* SeekBar改变 onProgressChanged*/{ // 对话框输入数值 onOKClick

	EditText dlt12xuan2edit;
    LinearLayout dltjixuanlinear;
    
	EditText qianquedit;
	EditText houquedit;
	EditText qian_dan;	//胆托的四个EditText
	EditText qian_tuo;
	EditText hou_dan;
	EditText hou_tuo;	
	// 小球图的宽高
	static final int BALL_WIDTH = 55;
	int redBallViewNum = 35;
	int blueBallViewNum = 12;
	// 顶部标签个数
	int iButtonNum = 2;
	Button goucaidating_button;
	Button dltzhixuan_button;
	Button dltdantuo_button;
	Button dlt12xuan2_button;

	//追加投注
	ToggleButton zhuijiatouzhu;
	// 首行button当前的位置
    int iCurrentButton; // 0x55660001 --- DLT danshi
	int tempCurrentButton;
	// 声明一个显示期号，和时间的数组
    String issue[] = new String[2];
	public static final int WANFA_START_ID = 0x55550001;  
	
	 int defaultOffShade;
	 int defaultOnShade;
	
	 

	public static final int RED_BALL_START = 0x80000001;// 小球起始ID
	public static final int RED_TUO_BALL_START = 0x82000001;
	public static final int BLUE_BALL_START = 0x81000001;
	public static final int BLUE_TUO_BALL_START = 0x83000001;

    static final int DIALOG1_KEY = 0;// 进度条的值2010/7/4
	ProgressDialog progressdialog;
    Spinner dltjixuanzhu;
	ScrollView mHScrollView;

	SeekBar mSeekBarBeishu;
	SeekBar mSeekBarQishu;

	TextView mTextBeishu;
	TextView mTextQishu;

	TextView mTextSumMoney;
    int mTimesMoney = 2;

	Button dlt_b_touzhu_fushi;
	ImageButton dlt_b_touzhu_dantuo,dlt_b_touzhu_twoin12,dlt_btn_newSelect;
	CheckBox mCheckBox;
	int iProgressBeishu = 1;
	int iProgressQishu = 1;
	LinearLayout dlt_downbutton;

	 int iScreenWidth;
	// Vector<OneBallView> redBallViewVector;
	BallTable redBallTable = null;
    int redBallResId[] = { R.drawable.grey, R.drawable.red };
	BallTable redTuoBallTable = null;
	// Vector<OneBallView> blueBallViewVector;
	BallTable blueBallTable = null;
	int blueBallResId[] = { R.drawable.grey, R.drawable.blue };
	BallTable blueTuoBallTable = null;
	
	String red_zhuma_string = " ";/*投注提示的四个zhuma文本*/
	String blue_zhuma_string = " ";
	String red_tuo_zhuma_string = " ";
	String blue_tuo_zhuma_string = " ";
   

	 final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	 final int FP = ViewGroup.LayoutParams.FILL_PARENT;

	 LinearLayout buyView;	
	 int iFushiRedBallNumber;// 复式 小球个数（机选）
	 int iFushiBlueBallNumber;
	// 胆拖 小球个数（机选）
	 int iDantuoRedDanNumber;
	 int iDantuoRedTuoNumber;
	 int iDantuoBlueNumber;
	 int iDantuoBlueTuoNumber;
	// 12选2小球个数（机选）
    int iTwoin12BlueBallNumber;
	// 显示彩种、期号和时间的TextView
    protected TextView lottery_title;
    private TextView term;
    private TextView time;
	// 注码
	public static int zhuma[] = null;
	// 不计算倍数的当前注数 用于构造请求
	 long iSendZhushu = 0;
    BallBetPublicClass ballBetPublicClass = new BallBetPublicClass();
	public int publicTopButton;
	public int type = 0;
	public static final int ZHIXUAN = 0;
	public static final int DANTUO = 1;
	/** Called when the activity is first created. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 1:
				progressdialog.dismiss();
				if(isFinishing() == false){
				    Accoutdialog.getInstance().createAccoutdialog(DLT.this, getResources().getString(R.string.goucai_Account_dialog_msg).toString());
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
				Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "投注成功，出票成功！",Toast.LENGTH_LONG).show();
				clearAllBalls(iCurrentButton);
				Intent intent = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent);
				break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressdialog.dismiss();
				if(isFinishing() == false){
				    PublicMethod.showDialog(DLT.this);
				}
				clearAllBalls(iCurrentButton);
				Intent intent2 = new Intent(UserLogin.SUCCESS);
				sendBroadcast(intent2);
				break;
			case 7:
				progressdialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(DLT.this, UserLogin.class);
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
				break;
			}		
		}
	};
	/* 清空小球*/
	void clearAllBalls(int iCurrentButton){
		switch (iCurrentButton) {
		case PublicConst.BUY_DLT_ZHIXUAN:
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
			qianquedit.setText("");
			houquedit.setText("");
			break;
		case PublicConst.BUY_DLT_DANTUO:
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
			redTuoBallTable.clearAllHighlights();
			blueTuoBallTable.clearAllHighlights();
			qian_dan.setText("");
			qian_tuo.setText("");
			hou_dan.setText("");
			hou_tuo.setText("");
			break;
		case  PublicConst.BUY_DLT_TWOIN12:
			blueBallTable.clearAllHighlights();
			dlt12xuan2edit.setText("");
			break;
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		//RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ----- 加载框架 layout
		setContentView(R.layout.layout_main_buy);
		lottery_title = (TextView) findViewById(R.id.layout_main_text_title);
		term = (TextView) findViewById(R.id.layout_main_text_issue);
		time = (TextView) findViewById(R.id.layout_main_text_time);
		// 获取期号和截止时间
		JSONObject dltLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(Constants.LOTNO_DLT);
		if(dltLotnoInfo!=null){
			//成功获取到了期号信息
			try {
				issue[0] = dltLotnoInfo.getString("batchCode");
				issue[1] = dltLotnoInfo.getString("endTime");
				term.setText("第" + this.issue[0] + "期");
				time.setText("截止时间：" + this.issue[1]);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			issue[0] = "";
			issue[1] = "";
		    PublicMethod.getIssue(Constants.LOTNO_SSQ,term,time,new Handler());
		}
		iScreenWidth = PublicMethod.getDisplayWidth(this);
        dlt_downbutton = (LinearLayout)findViewById(R.id.si_ge_button);
        dlt_downbutton.setVisibility(LinearLayout.VISIBLE);
		buyView = (LinearLayout) findViewById(R.id.layout_buy);// 获取主布局id
		initSiGeButton();
	}
	void initSiGeButton(){
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout sigebuttonl = (LinearLayout) inflate.inflate(R.layout.layout_dlt_downbutton, null);
	    goucaidating_button = (Button)sigebuttonl.findViewById(R.id.sb_goucaidating);
	    dltzhixuan_button = (Button)sigebuttonl.findViewById(R.id.sb_dltzhixuan);
	    dltdantuo_button = (Button)sigebuttonl.findViewById(R.id.sb_dltdantuo);
	    dlt12xuan2_button = (Button)sigebuttonl.findViewById(R.id.sb_dlt12xuan2);
	    dlt_downbutton.addView(sigebuttonl);
	}
	
	/** SeekBar改变时的回调函数 
	 * @param SeekBar seekBar 发生变化的SeekBar实例 
	 * @param int progress变化后的位置值 
	 * @param boolean fromUser 这个参数不用管它
	 * @return void
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		if (progress < 1) {
			seekBar.setProgress(1);
		}
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {	// 单式
		case R.id.dlt_zhixuan_seek_beishu:
		case R.id.dlt_dantuo_seek_beishu:
		case R.id.dlt_twoin12_seek_beishu:
		case R.id.dlt_12xuan2_jixuan_seek_beishu:
		case R.id.dlt_zhixuan_jixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.dlt_zhixuan_seek_qishu:
		case R.id.dlt_dantuo_seek_qishu:
		case R.id.dlt_twoin12_seek_qishu:
		case R.id.dlt_12xuan2_jixuan_seek_qishu:
		case R.id.dlt_zhixuan_jixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		}
	}

	// Seekbar回调函数 暂时不用
	public void onStartTrackingTouch(SeekBar seekBar) {}

	// Seekbar回调函数 暂时不用
	public void onStopTrackingTouch(SeekBar seekBar) {}

	/** 响应小球被点击的回调函数 
	 */
	public void onClick(View v) {
		
		int iBallId = v.getId();
		if (iBallId >= RED_BALL_START && iBallId < BLUE_BALL_START) {
			Log.v("BALLID", v.getId()+"");
			int iBallViewId = v.getId() - RED_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 0, iBallViewId);// 点红球组
			}
		} else if (iBallId >= BLUE_BALL_START && iBallId < RED_TUO_BALL_START) {
			Log.v("BALLIDBLUE", v.getId()+"");
			int iBallViewId = v.getId() - BLUE_BALL_START;
			Log.v("iBallViewId", iBallViewId+"");
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 1, iBallViewId);// 点蓝球组
			}
		} else if (iBallId >= RED_TUO_BALL_START&& iBallId < BLUE_TUO_BALL_START) {
			int iBallViewId = v.getId() - RED_TUO_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 2, iBallViewId);// 点蓝球组
			}
		} else {
			int iBallViewId = v.getId() - BLUE_TUO_BALL_START;
			if (iBallViewId < 0) {
				return;
			} else {
				changeBuyViewByRule(iCurrentButton, 3, iBallViewId);// 点蓝球组
			}
		}
		changeTextSumMoney();
	}

	/** 大乐透注数的计算方法
	 *  @return int 返回注数
	 */
	  int getZhuShu() {
		int iReturnValue = 0;
		switch (iCurrentButton) {
		case PublicConst.BUY_DLT_ZHIXUAN:
			int iRedBalls = redBallTable.getHighlightBallNums();
			int iBlueBalls = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getDLTFSZhuShu(iRedBalls, iBlueBalls);
			Log.v("-BUY_DLT_FUSHI----***" , iReturnValue + "%%% " + iRedBalls+ " %%%" + iBlueBalls);
			break;
		case PublicConst.BUY_DLT_DANTUO:
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();
			int iBlueTuoHighlights = blueTuoBallTable.getHighlightBallNums();
			iReturnValue = (int) getDLTDTZhuShu(iRedHighlights,iRedTuoHighlights, iBlueHighlights, iBlueTuoHighlights);
			break;
		case PublicConst.BUY_DLT_TWOIN12:

			int iBlueBalls12 = blueBallTable.getHighlightBallNums();
			iReturnValue = (int) getDLT12ZhuShu(iBlueBalls12);
			Log.v("-BUY_DLT_TWOIN12----***" ,iReturnValue + "%%% "+ iBlueBalls12);
			break;
		}
		return iReturnValue;
	}
	 /**得到大乐透12选2的注数
	  * @param int aBlueBalls 蓝球注数
	  */
 long getDLT12ZhuShu(int aBlueBalls) {
		long dltZhuShu = 0L;
		iSendZhushu = 0L;
		if (aBlueBalls >= 2) {
			dltZhuShu += (PublicMethod.zuhe(2, aBlueBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(2, aBlueBalls);
		}
		return dltZhuShu;
	}

	/** 胆拖玩法注数计算方法 
	 * @param int aRedBalls 红球个数 
	 * @param int aRedTuoBalls 红球拖码个数
	 * @param int aBlueBalls 蓝球个数 
	 * @return long 注数
	 */
	protected long getDLTDTZhuShu(int aRedBalls, int aRedTuoBalls,int aBlueBalls, int aBlueTuoBalls) {// 得到大乐透胆拖的注数
		long dltZhuShu = 0L;
		iSendZhushu = 0L;
		dltZhuShu += (PublicMethod.zuhe(5 - aRedBalls, aRedTuoBalls)* PublicMethod.zuhe(2 - aBlueBalls, aBlueTuoBalls) * iProgressBeishu);
		iSendZhushu = (PublicMethod.zuhe(5 - aRedBalls, aRedTuoBalls) * PublicMethod.zuhe(2 - aBlueBalls, aBlueTuoBalls));
		return dltZhuShu;
	}

	/** 复式玩法注数计算方法 
	 * @param int aRedBalls 红球个数 
	 * @param int aBlueBalls 蓝球个数 
	 * @return long 注数
	 */
	 long getDLTFSZhuShu(int aRedBalls, int aBlueBalls) {
		long dltZhuShu = 0L;
		iSendZhushu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			dltZhuShu += (PublicMethod.zuhe(5, aRedBalls)* PublicMethod.zuhe(2, aBlueBalls) * iProgressBeishu);
			iSendZhushu = PublicMethod.zuhe(5, aRedBalls)* PublicMethod.zuhe(2, aBlueBalls);
		}
		return dltZhuShu;
	}

	 int getBeishu() {
		return mSeekBarBeishu.getProgress();
	}
	 int getQishu() {
		return mSeekBarQishu.getProgress();
	}
	 boolean getCheckBox() {
		return mCheckBox.isChecked();
	}

	// fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	protected void setSeekWhenAddOrSub(int idFind, View iV, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) iV.findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {
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
	 * @return void
	 */
	public void changeTextSumMoney() {
		switch (iCurrentButton) {
		case PublicConst.BUY_DLT_ZHIXUAN: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();

			// 红球数 不足
			if (iRedHighlights < 5) {
				mTextSumMoney.setText(getResources().getString(R.string.please_choose_red_number_dlt));
			}
			// 红球数达到最低要求
			else if (iRedHighlights == 5) {
				if (iBlueHighlights < 2) {
					mTextSumMoney.setText(getResources().getString(R.string.please_choose_blue_number_dlt));
				} else if (iBlueHighlights == 2) {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为单式，共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
					mTextSumMoney.setText(iTempString);
				} else {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为后区复式，共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
					mTextSumMoney.setText(iTempString);
				}
			}
			// 红球复式
			else {
				if (iBlueHighlights < 2) {
					mTextSumMoney.setText(getResources().getString(R.string.please_choose_blue_number_dlt));
				} else if (iBlueHighlights == 2) {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为前区复式，共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
					mTextSumMoney.setText(iTempString);
				} else {
					int iZhuShu = getZhuShu();
					String iTempString = "当前为全复式，共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
					mTextSumMoney.setText(iTempString);
				}
			}
			break;
		}
		case PublicConst.BUY_DLT_DANTUO: {
			int iRedHighlights = redBallTable.getHighlightBallNums();
			int iBlueHighlights = blueBallTable.getHighlightBallNums();
			int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
			int iBlueTuoHighlights = blueTuoBallTable.getHighlightBallNums();

			if (iRedHighlights + iRedTuoHighlights < 6) {
				mTextSumMoney.setText(getResources().getString(R.string.choose_number_dialog_tip10));
			} else if (iBlueHighlights + iBlueTuoHighlights < 2) {
				mTextSumMoney.setText(getResources().getString(R.string.choose_number_dialog_tip11));
			} else {
				int iZhuShu = getZhuShu();
				String iTempString = "当前为胆拖，共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
				mTextSumMoney.setText(iTempString);
			}
			break;
		}
		case PublicConst.BUY_DLT_TWOIN12:
			// 单式View
			if (blueBallTable.getHighlightBallNums() < 2) {
				mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
			} else {
				int iZhuShu = getZhuShu();
				String iTempString = "共" + iZhuShu + "注，共"+ (iZhuShu * mTimesMoney) + "元";
				mTextSumMoney.setText(iTempString);
			}
			break;
		default:
			break;
		}
	}

	/**根据玩法改变当前View 及响应
	*@param1 - aWhichTopButton 当前顶部标签位置
	*@param2 - aWhichGroupBall 第几组小球，如单式共两组小球，从0开始计数
    *@param3 - aBallViewId 被click小球的id，从0开始计数，小球上显示的数字为id+1
    **/
	protected void changeBuyViewByRule(int aWhichTopButton, int aWhichGroupBall,int aBallViewId) {
		switch (aWhichTopButton) {
		case PublicConst.BUY_DLT_ZHIXUAN:
			buy_ZhiXuan(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_DLT_DANTUO:
			buy_DANTUO(aWhichGroupBall, aBallViewId);
			break;
		case PublicConst.BUY_DLT_TWOIN12:
			buy_TWOIN12(aWhichGroupBall, aBallViewId);
			break;
		default:
			break;
		}
	}

	/*
	 * 12选2玩法改变View @param int aWhichGroupBall 被点击的小球位置 @param int aBallViewId
	 * 小球id @return void
	 */
	 void buy_TWOIN12(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 1) {
		    int 	iChosenBallSum=12;
		    blueBallTable.changeBallState(iChosenBallSum,aBallViewId);
			wenzixianshi(dlt12xuan2edit,blueBallTable,getString(R.string.dlt_12xuan2_edit_text));
			
		}
	       	
	}

	/*
	 * 胆拖玩法改变View @param int aWhichGroupBall 被点击的小球位置 @param int aBallViewId
	 * 小球id @return void
	 */
	 void buy_DANTUO(int aWhichGroupBall, int aBallViewId) {

			if (aWhichGroupBall == 0) { // 红球 胆区 最多4个小球
				int iChosenBallSum = 4;
				if (redTuoBallTable.getHighlightBallNums() > 18)
					iChosenBallSum = 22 - redTuoBallTable.getHighlightBallNums();
				// 每次点击后记住小球的状态
				int isHighLight = redBallTable.changeBallState(iChosenBallSum,aBallViewId);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& redTuoBallTable.getOneBallStatue(aBallViewId) != 0) {
					redTuoBallTable.clearOnBallHighlight(aBallViewId);
					Toast.makeText(DLT.this, "选号重复，前区托码被覆盖", Toast.LENGTH_SHORT).show();
				}
				
				
				wenzixianshi(qian_dan,redBallTable,getString(R.string.dlt_dantou_front_dan_edit_text));
				wenzixianshi(qian_tuo,redTuoBallTable,getString(R.string.dlt_dantou_rear_dan_edit_text));
			} else if (aWhichGroupBall == 1) { // 蓝球区胆区
				int iChosenBallSum = 1;
				int isHighLight= blueBallTable.changeBallState(iChosenBallSum,aBallViewId);;
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& blueTuoBallTable.getOneBallStatue(aBallViewId) != 0) {
					blueTuoBallTable.clearOnBallHighlight(aBallViewId);
					Toast.makeText(DLT.this, "选号重复，后区托码被覆盖", Toast.LENGTH_SHORT).show();
				}
				if(blueBallTable.getHighlightBallNums()>iChosenBallSum){
					Toast.makeText(DLT.this, "后区胆码不能超过两个", Toast.LENGTH_SHORT).show();
				}
				wenzixianshi(hou_tuo,blueTuoBallTable,getString(R.string.dlt_dantou_rear_tuo_edit_text));
				wenzixianshi(hou_dan,blueBallTable,getString(R.string.dlt_dantou_front_tuo_edit_text));

			} else if (aWhichGroupBall == 2) { // 红球 拖区 最多34个小球
				int iChosenBallSum = 22 - redBallTable.getHighlightBallNums();
				int isHighLight = redTuoBallTable.changeBallState(iChosenBallSum,aBallViewId);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& redBallTable.getOneBallStatue(aBallViewId) != 0) {
					redBallTable.clearOnBallHighlight(aBallViewId);
					Toast.makeText(DLT.this, "选号重复，前区胆码被覆盖", Toast.LENGTH_SHORT).show();
				}
				wenzixianshi(qian_tuo,redTuoBallTable,getString(R.string.dlt_dantou_rear_dan_edit_text));
				wenzixianshi(qian_dan,redBallTable,getString(R.string.dlt_dantou_front_dan_edit_text));
			} else if (aWhichGroupBall == 3) { // 蓝球 拖区 最多5个小球
				int iChosenBallSum = 12;
				int isHighLight = blueTuoBallTable.changeBallState(iChosenBallSum,aBallViewId);
				if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& blueBallTable.getOneBallStatue(aBallViewId) != 0) {
					blueBallTable.clearOnBallHighlight(aBallViewId);
					Toast.makeText(DLT.this, "选号重复，后区胆码被覆盖", Toast.LENGTH_SHORT).show();
				}
				wenzixianshi(hou_tuo,blueTuoBallTable,getString(R.string.dlt_dantou_rear_tuo_edit_text));
				wenzixianshi(hou_dan,blueBallTable,getString(R.string.dlt_dantou_front_tuo_edit_text));
			}
		}

	/**
	 * 复式玩法改变View @param int aWhichGroupBall 被点击的小球位置 @param int aBallViewId
	 * 小球id @return void
	 */
	 void buy_ZhiXuan(int aWhichGroupBall, int aBallViewId) {
		if (aWhichGroupBall == 0) { // 红球区
			int iChosenBallSum = 22;
			// 每次点击后记住小球的状态
			int isHighLight = redBallTable.changeBallState(iChosenBallSum,aBallViewId);
			
		} else if (aWhichGroupBall == 1) {//蓝球区
			int iChosenBallSum = 12;
			int isHighLight = blueBallTable.changeBallState(iChosenBallSum,aBallViewId);
						
		}		
		wenzixianshi(qianquedit,redBallTable,getString(R.string.dlt_zhixuan_front_edit_text));
		wenzixianshi(houquedit,blueBallTable,getString(R.string.dlt_zhixuan_rear_edit_text));
	}
	 
	 /*设置输入框EditText的显示*/
void wenzixianshi(EditText edit,BallTable aa,String moren){
	String red_zhuma_string="";
	int[] qiuma = aa.getHighlightBallNOs();
	if (red_zhuma_string.equals("  ")) {
		red_zhuma_string = moren;
	}else{
		for (int i = 0; i < qiuma.length; i++) {
			red_zhuma_string += PublicMethod.getZhuMa(qiuma[i]);
			if (i != qiuma.length - 1)
				red_zhuma_string = red_zhuma_string + ",";
		}
	}
	edit.setText(red_zhuma_string);
}

	/**
	 * 创建BallTable 
	 * @param LinearLayout aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth  BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度） 
	 * @param int[] aResId 小球图片Id 
	 * @param int aIdStart 小球Id起始数值	 * 
	 * @return BallTable
	 */
	 BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,int aFieldWidth, int aBallNum, 
			 int[] aResId,int aIdStart) {
		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);
		int iBallNum = aBallNum;
	
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		//定义没行小球的个数为7
		int viewNumPerLine = 8;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth)/viewNumPerLine-2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "" + (iBallViewNo + 1);
				OneBallView tempBallView = new OneBallView(aParentView.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
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
			iBallTable.tableLayout.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(this);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "" + (iBallViewNo + 1);
				OneBallView tempBallView = new OneBallView(aParentView.getContext());
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
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
			iBallTable.tableLayout.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	public static int /* String */getDisplayMetrics(Context cx) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = cx.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_dlt, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	/*设置菜单中的菜单项，被点击后的点击事件*/
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.dlt_menu_confirm:
			beginTouZhu();
			break;
		case R.id.dlt_menu_reselect_num:
			beginReselect();
			break;
		case R.id.dlt_menu_game_introduce:
			showGameIntroduction();
			break;
		case R.id.dlt_menu_cancel:
			this.finish();
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	public int getChangingConfigurations() {
		return super.getChangingConfigurations();
	}
	public void onCancelClick() {
	}

	
	// 开始投注 响应点击投注按钮 和 menu里面的投注选项
	 void beginTouZhu () {
			// 周黎鸣 7.4 代码修改：添加登陆的判断// fqc eidt 2010/7/13 修改大乐透玩法
			ShellRWSharesPreferences pre = new ShellRWSharesPreferences(DLT.this,"addInfo");
	        String sessionIdStr = pre.getUserLoginInfo("sessionid");
			int iZhuShu = getZhuShu();
			if (sessionIdStr.equals("")||sessionIdStr==null) {
				Intent intentSession = new Intent(DLT.this, UserLogin.class);
				startActivity(intentSession);
			} else{
			        int redNumber;
		        	int redTuoNumber;
		        	int blueNumber;
		        	int blueTuoNumber;
		         switch (iCurrentButton) {
		         case PublicConst.BUY_DLT_ZHIXUAN:
		        	     redNumber = redBallTable.getHighlightBallNums();
		        	     blueNumber = blueBallTable.getHighlightBallNums();
			          Log.v("redBallTable.getHighlightBallNums() < 5",""+redBallTable.getHighlightBallNums());
	                  if ((redNumber < 5 ||redNumber > 22)&&(blueNumber < 2 || blueNumber > 12)) {
			           	alert1("请选择5~22个前区号码和2~12个后区号码");
			          } else if (redNumber < 5) {
			        	alert1("请选择至少5个前区号码");
			          } else if (blueNumber < 2) {
				        alert1("请选择至少2个后区号码");
			          } else if (iZhuShu > 10000) {
			        	DialogExcessive();
		            	} else {
		
			        	String sTouzhuAlert =  getTouzhuAlert();
			        	alertZhixuan(sTouzhuAlert);
			         }
	                  break;
		         case PublicConst.BUY_DLT_DANTUO:
			         redNumber = redBallTable.getHighlightBallNums();
	     	         redTuoNumber = redTuoBallTable.getHighlightBallNums();
	     	         blueNumber = blueBallTable.getHighlightBallNums();
	     	         blueTuoNumber = blueTuoBallTable.getHighlightBallNums();
			      if (redNumber + redTuoNumber < 6||blueTuoNumber + blueNumber < 2) {
				        alert1("请选择:\n"+ " 前区号码6~20个；\n" + " 后区号码2~12个；\n");
			         }else if (iZhuShu > 10000) {
				        DialogExcessive();
			         } else{
			            String zhuma_zhixuan = zhuma_dantuo();
			            Log.e("dantuo====", zhuma_zhixuan);
				        String sTouzhuAlert = getTouzhuAlert();
				        alert_dantuo(sTouzhuAlert);// 应该调用胆拖的方法 陈晨 20100713
			         }
			      break;
		      case PublicConst.BUY_DLT_TWOIN12: 
			          blueNumber = blueBallTable.getHighlightBallNums();
			       if (blueNumber < 2) {
				      alert1("请选择2~12个小球");
			       } else if (iZhuShu > 10000) {
				      DialogExcessive();
			       } else{
		             String zhuma_zhixuan = zhuma_twoin12();
		             Log.e("twoin12====", zhuma_zhixuan);
				     String sTouzhuAlert = getTouzhuAlert();
				     alertTwoIn12(sTouzhuAlert);
			       }	
			         break;
		         }
			}
	}
	// 点击menu的重新机选 调用重新机选的方法	
	 void beginReselect() {
		 if (iCurrentButton == PublicConst.BUY_DLT_ZHIXUAN) {
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
			qianquedit.setText("");
			houquedit.setText("");
		} else if (iCurrentButton == PublicConst.BUY_DLT_DANTUO) {
			redBallTable.clearAllHighlights();
			blueBallTable.clearAllHighlights();
			redTuoBallTable.clearAllHighlights();
			blueTuoBallTable.clearAllHighlights();
			qian_dan.setText("");
			qian_tuo.setText("");
			hou_dan.setText("");
			hou_tuo.setText("");
		} else if (iCurrentButton == PublicConst.BUY_DLT_TWOIN12) {
			blueBallTable.clearAllHighlights();
			dlt12xuan2edit.setText("");
		}
		
		 
	}
	// 点击menu里面的玩法介绍，通过对话框显示
   void showGameIntroduction() {
		WebView webView = new WebView(this);
		String url = "file:///android_asset/ruyihelper_gameIntroduction_dlt.html";
		webView.loadUrl(url);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("玩法介绍");
		builder.setView(webView);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {}
		});
		builder.show();
	}
	// 提示框1 用来提醒选球规则	// fqc delete 删除取消按钮 7/14/2010
	protected void alert1(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {}
							});
		dialog.show();
	}
	/*  提示框 用来确定是否投注*/
	 void alertTwoIn12(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQishu();
									String[] strCode = null;
									int iBeiShu = mSeekBarBeishu.getProgress();
									public void run() {
										
											String zhuma = zhuma_twoin12();
											Log.v("----------------zhuma", zhuma+"");
											strCode = payNew(zhuma, ""+ iBeiShu,iZhuShu * 2 + "", iQiShu+ "");
										
										if (strCode[0].equals("0000")&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")&& strCode[1].equals("000001")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040006")|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("002002")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("00")&& strCode[1].equals("00")) {
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
						}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {}
						});
		dialog.show();
	}
	 /**
	  * 大乐透直选投注时投注码参数
	  * @return
	  */
	public String zhuma_zhixuan() {
		int zhuma[] = redBallTable.getHighlightBallNOs();
		int zhumablue[] = blueBallTable.getHighlightBallNOs();
		String t_str = "";

		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
			if (i != zhuma.length - 1){
				t_str += " ";
			}else{
				t_str += "-";
			}
		}
		for (int j = 0; j < zhumablue.length; j++) {
			if (zhumablue[j] >= 10) {
				t_str += zhumablue[j];
			} else if (zhumablue[j] < 10) {
				t_str += "0" + zhumablue[j];
			}
			if (j != zhumablue.length - 1){
				t_str += " ";
			}
		}

		return t_str;
	}
	
	/* 大乐透直选提示框 用来确定是否投注*/
	 void alertZhixuan(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle("您选择的是")
				.setMessage(string).setPositiveButton("确定",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
								Thread t = new Thread(new Runnable() {
									int iZhuShu = getZhuShu();
									int iQiShu = getQishu();
									String[] strCode = null;
									int iBeiShu = mSeekBarBeishu.getProgress();
									public void run() {
										
										String zhuma_zhixuan = zhuma_zhixuan();
										strCode = payNew(zhuma_zhixuan,"" + iBeiShu, iZhuShu* mTimesMoney + "",iQiShu + "");
										if (strCode[0].equals("0000")&& strCode[1].equals("000000")) {
											Message msg = new Message();
											msg.what = 6;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("070002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("0000")&& strCode[1].equals("000001")) {
											Message msg = new Message();
											msg.what = 4;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("1007")) {
											Message msg = new Message();
											msg.what = 2;
											handler.sendMessage(msg);
										} else if (strCode[1].equals("002002")) {
											Message msg = new Message();
											msg.what = 3;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("040006")|| strCode[0].equals("201015")) {
											Message msg = new Message();
											msg.what = 1;
											handler.sendMessage(msg);
										} else if (strCode[0].equals("00")&& strCode[1].equals("00")) {
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
						}).setNegativeButton("取消",	new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {}
						});
		dialog.show();
	}
	 
	 /*提示框 dantuo投注*/
	protected void alert_dantuo(String string) {
		Builder dialog = new AlertDialog.Builder(this).setMessage(string).setTitle("您选择的是")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dlt_b_touzhu_dantuo.setClickable(false);
						dlt_b_touzhu_dantuo.setBackgroundResource(R.drawable.touzhuup_n);
						showDialog(DIALOG1_KEY); // 显示网络提示框
						Thread t = new Thread(new Runnable() {
							int iZhuShu = getZhuShu();
							int iQiShu = getQishu();
							int iBeiShu = mSeekBarBeishu.getProgress();
							String[] strCode = null;
							public void run() {
							String zhuma_dantuo = zhuma_dantuo();
							
							strCode = payNew(zhuma_dantuo,iBeiShu + "", iZhuShu * mTimesMoney + "",iQiShu + "");
								
							dlt_b_touzhu_dantuo.setClickable(true);

							if (strCode[0].equals("0000")&& strCode[1].equals("000000")) {
								Message msg = new Message();
								msg.what = 6;
								handler.sendMessage(msg);
							} else if (strCode[0].equals("070002")) {
								Message msg = new Message();
								msg.what = 7;
								handler.sendMessage(msg);
							} else if (strCode[0].equals("0000")&& strCode[1].equals("000001")) {
								Message msg = new Message();
								msg.what = 4;
								handler.sendMessage(msg);
							} else if (strCode[0].equals("1007")) {
								Message msg = new Message();
								msg.what = 2;
								handler.sendMessage(msg);
							} else if (strCode[1].equals("002002")) {
								Message msg = new Message();
								msg.what = 3;
								handler.sendMessage(msg);
							} else if (strCode[0].equals("040006")) {
								Message msg = new Message();
								msg.what = 1;
								handler.sendMessage(msg);
							} else if (strCode[0].equals("00")&& strCode[1].equals("00")) {
								Message msg = new Message();
								msg.what = 8;
								handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = 9;
								handler.sendMessage(msg);								}
							}
						});
						t.start();
					}
				}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {}
						});
		dialog.show();
	}
	/**
	 * 大乐透用户支付方法
	 * @param betCode  注码
	 * @param lotMulti  倍数
	 * @param amount   金额
	 * @param qiShu  期数
	 * @return
	 */
	protected String[] payNew(String betCode, String lotMulti, String amount,String qiShu) {
		String[] error_code = null;
		BettingInterface betting = BettingInterface.getInstance();

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,"addInfo");
		String sessionid = shellRW.getUserLoginInfo("sessionid");
		String phonenum = shellRW.getUserLoginInfo("phonenum");

		error_code = betting.BettingTC(phonenum, "T01001", betCode,lotMulti, amount, ""+mTimesMoney, qiShu, sessionid);
		Log.e(":payNew++++dantuo",""+betCode);
		Log.e(":payNew++++dantuo",""+mTimesMoney);
		return error_code;
	}	

	// 胆拖投注时投注码参数
	public String zhuma_dantuo() {
		int[] zhuma = redBallTable.getHighlightBallNOs();
		int[] tuozhuma = redTuoBallTable.getHighlightBallNOs();
		int[] zhumablue = blueBallTable.getHighlightBallNOs();
		int[] tuozhumablue = blueTuoBallTable.getHighlightBallNOs();
		String t_str = "";

		for (int i = 0; i < zhuma.length; i++) {
			if (zhuma[i] >= 10) {
				t_str += zhuma[i];
			} else if (zhuma[i] < 10) {
				t_str += "0" + zhuma[i];
			}
			if (i != zhuma.length - 1){
				t_str += " ";
			}

		}
		t_str += "$";
		for (int j = 0; j < tuozhuma.length; j++) {
			if (tuozhuma[j] >= 10) {
				t_str += tuozhuma[j];
			} else if (tuozhuma[j] < 10) {
				t_str += "0" + tuozhuma[j];
			}
			if (j != tuozhuma.length - 1){
				t_str += " ";
			}else{
				t_str += "-";
			}
		}
		for (int k = 0; k < zhumablue.length; k++) {
			if (zhumablue[k] >= 10) {
				t_str += +zhumablue[k];
			} else if (zhumablue[k] < 10) {
				t_str += "0" + zhumablue[k];
			}
			if (k != zhumablue.length - 1){
				t_str += " ";
			}
		}
		t_str += "$";
		for (int j = 0; j < tuozhumablue.length; j++) {
			if (tuozhumablue[j] >= 10) {
				t_str += tuozhumablue[j];
			} else if (tuozhumablue[j] < 10) {
				t_str += "0" + tuozhumablue[j];
			}
			if (j != tuozhumablue.length - 1){
				t_str += " ";
			}
		}

		return t_str;
	}

	public String zhuma_twoin12() {
		int[] zhumablue = blueBallTable.getHighlightBallNOs();
		String t_str = "";

		for (int i = 0; i < zhumablue.length; i++) {
			if (zhumablue[i] >= 10) {
				t_str += zhumablue[i];
			} else if (zhumablue[i] < 10) {
				t_str += "0" + zhumablue[i];
			}
			if (i != zhumablue.length - 1){
				t_str += " ";
			}
		}
		return t_str;
	}
	// 网络连接提示框 2010/7/4 陈晨
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
	
	/*投注提示框*/
	protected String getTouzhuAlert() {
        String	alert_touzhu_string = "" ;
		int iZhuShu = getZhuShu();
		int qiShu = getQishu();		
		switch (iCurrentButton) {
		case PublicConst.BUY_DLT_ZHIXUAN:			
			alert_touzhu_string = "注数："+ iZhuShu / mSeekBarBeishu.getProgress()+ "注"+ "\n"
			+ "倍数："/* 注数不能算上倍数 陈晨 20100713*/ + mSeekBarBeishu.getProgress() + "倍" + "\n" 
			+ "追号："+ mSeekBarQishu.getProgress() + "期" + "\n" 
			+ "金额："+ (iZhuShu * mTimesMoney) + "元" + "\n"
			+ "冻结金额："+ ((qiShu - 1) * iZhuShu * mTimesMoney) + "元" + "\n"+"注码：\n" + getRedZhuMa() + " | " + getBlueZhuMa()+ "\n"
			+ "确认支付吗？";
			break;
		case PublicConst.BUY_DLT_TWOIN12:
			alert_touzhu_string = "注数："+ iZhuShu / mSeekBarBeishu.getProgress()+ "注"+ "\n"
			+ "倍数："/* 注数不能算上倍数 陈晨 20100713*/ + mSeekBarBeishu.getProgress() + "倍" + "\n" 
			+ "追号："+ mSeekBarQishu.getProgress() + "期" + "\n"
			+ "金额："+ (iZhuShu * mTimesMoney) + "元" + "\n"
			+ "冻结金额："+ ((qiShu - 1) * iZhuShu * mTimesMoney) + "元" + "\n"
			+ "注码：" + getBlueZhuMa() + "\n"
			+ "确认支付吗？";
			break;
		case PublicConst.BUY_DLT_DANTUO:			
			alert_touzhu_string = "注数："+ iZhuShu / mSeekBarBeishu.getProgress()+ "注"+ "\n"
					+ "倍数："/*注数不能算上倍数 陈晨 20100713*/ + mSeekBarBeishu.getProgress() + "倍" + "\n" 
					+ "追号："+ mSeekBarQishu.getProgress() + "期" + "\n" 
					+ "金额："+ (iZhuShu * mTimesMoney) + "元" + "\n" 
					+ "冻结金额："+ ((qiShu - 1) * iZhuShu * mTimesMoney) + "元" + "\n"
					+ "注码：" + "\n" 
			        + "   前区胆码：" + getRedZhuMa() + "\n"
					+ "   前区拖码：" + getRedTuoZhuMa() + "\n" 
					+ "   后区胆码："	+ getBlueZhuMa() + "\n"
					+ "   后区拖码："	+ getBlueTuoZhuMa() + "\n"
					+ "确认支付吗？";		
			break;
		}
		return alert_touzhu_string;		
	}
	/**
	 * 分别获取红球上的注码*/
	String getRedZhuMa(){
		red_zhuma_string = "";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redZhuMa.length; i++) {
			red_zhuma_string = red_zhuma_string + String.valueOf(redZhuMa[i]);
			if (i != redZhuMa.length - 1){
				red_zhuma_string = red_zhuma_string + ".";
			}
		}
		return red_zhuma_string;		
	}
	/**
	 * 分别获取篮球小球上的注码*/
	String getBlueZhuMa(){
		blue_zhuma_string = "";
		int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
	    for (int i = 0; i <blueZhuMa.length; i++) {
		blue_zhuma_string +=  String.valueOf(blueZhuMa[i]);
		if (i != blueZhuMa.length - 1)
			blue_zhuma_string = blue_zhuma_string + ".";
	    }
		return blue_zhuma_string;		
	}
	/**
	 * 分别获取蓝托小球上的注码*/
	String getBlueTuoZhuMa(){
		blue_tuo_zhuma_string = "";
		int[] blueTuoZhuMa = blueTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueTuoZhuMa.length; i++) {
			blue_tuo_zhuma_string +=String.valueOf(blueTuoZhuMa[i]);
			if (i != blueTuoZhuMa.length - 1)
				blue_tuo_zhuma_string = blue_tuo_zhuma_string + ".";
		}
		return blue_tuo_zhuma_string;		
	}
	/**
	 * 分别获取红托小球上的注码*/
	String getRedTuoZhuMa(){
		red_tuo_zhuma_string = "";
		int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < redTuoZhuMa.length; i++) {
			red_tuo_zhuma_string = red_tuo_zhuma_string
					+ String.valueOf(redTuoZhuMa[i]);
			if (i != redTuoZhuMa.length - 1)
				red_tuo_zhuma_string = red_tuo_zhuma_string + ".";
		}
		return red_tuo_zhuma_string;		
	}
/** 单笔投注大于10万元时的对话框*/
	protected void DialogExcessive() {
		String dlt10w = getString(R.string.dltdayu10w);
		AlertDialog.Builder builder = new AlertDialog.Builder(DLT.this);
		builder.setTitle(getString(R.string.toast_touzhu_title));
		builder.setMessage(dlt10w);
		builder.setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {}
				});
		builder.show();
	}
	/**
	 * 回收小球
	 */
	@Override
	protected void onDestroy() {
		showMemory();
		super.onDestroy();
		recycleBallTable(redBallTable);
		recycleBallTable(blueBallTable);
		recycleBallTable(redTuoBallTable);
		recycleBallTable(blueTuoBallTable);
		
		showMemory();
	}
	
	/**
	 *回首小球的方法
	 * @param balltable  需回收的小球的table
	 */
	private void recycleBallTable(BallTable balltable){
		if(balltable != null && balltable.getBallViews()!= null){
			for (Iterator iterator = balltable.getBallViews().iterator(); iterator.hasNext();) {
				OneBallView ball = (OneBallView) iterator.next();
				ball.recycleBitmaps();
			}
		}
	}
	private void showMemory() {
		 ActivityManager actMgr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
             ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
             actMgr.getMemoryInfo(memoryInfo);
	}

}