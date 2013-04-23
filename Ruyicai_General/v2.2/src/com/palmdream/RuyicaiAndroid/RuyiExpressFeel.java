package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.netintface.BettingInterface;
import com.palmdream.netintface.GT;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

/**
 * 
 * @author wangyl zhoulm 如意传情、如意套餐、幸运选号操作
 * 
 */
public class RuyiExpressFeel extends Activity implements
		SeekBar.OnSeekBarChangeListener, MyDialogListener {

	/* 列表适配器adapter参数String[] From的代理词 */
	public final static String ICON = "ICON";/* 图标 */
	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String TITLETEXT = "TITLETEXT"; /* 标题描述 */
	public final static String SUBSCRIBEBUTTON = "SUBSCRIBEBUTTON";
	public final static String EDITBUTTON = "EDITBUTTON";
	public final static String UNSUBSCRIBEBUTTON = "UNSUBSCRIBEBUTTON";

	// 如意传情主列表各项ID
	public final static int ID_MAINRUYIEXPRESSFEEL = 1;/* 如意传情主列表 */
	public final static int ID_RUYIPACKAGE = 2; /* 如意套餐 */
	public final static int ID_LUCKNUMBER = 3;/* 幸运选号 */
	public final static int ID_RUYICHUANQING = 9;/* 如意传情 */
	public final static int ID_RUYICHUANQING_SSQ = 10;/* 如意传情双色球 */
	public final static int ID_RUYICHUANQING_FC3D = 11;/* 如意传情福彩3D */
	public final static int ID_RUYICHUANQING_QLC = 12;/* 如意传情七乐彩 */

	// 如意套餐彩种订购状态 true为订购，false为未订购
	// boolean ssqFlag = false ;/*双色球*/
	// boolean fc3dFlag = true;/*福彩3D*/
	// boolean qlcFlag = false;/*七乐彩*/
	boolean[] subscribeFlag = { true, true, true ,true,true};
	int viewid;
	boolean Flag;
	public final static int LAYOUT_INDEX1 = 0;
	// true 未订购 false 订购
	public Integer[] imageGroup1 = { R.drawable.sub1, R.drawable.edit2,
			R.drawable.unsub2 };
	public Integer[] imageGroup2 = { R.drawable.sub2, R.drawable.edit1,
			R.drawable.unsub1 };
	public Integer[] imageGroup = new Integer[3];
	TextView holoderTextView; // 套餐描述
	String[] holderText = new String[5];
	String text_str;

	RuyiPackageEfficientAdapter ruyiPackageAdapter;
	String packageName;// 套餐彩种名称
	int iPosition;
	boolean[][] states = new boolean[5][3];
	int iadapter;
	// 套餐几元的缓存 0709 wyl
	String ssqamount = "";
	String fc3damount = "";
	String qlcamount = "";
	//wangyl 8.27 add pl3 and dlt
	String pl3amount = "";
	String dltamount = "";

	private ImageButton btnreturn;/* 返回 */// 7.3代码修改：将Button换成ImageButton

	List<Map<String, Object>> list;/* 列表适配器的数据源 */
	TextView texttitle;/* 标题 */

	TextView title;/* 标题 */
	TextView textview;/* 标题描述 */
	Button subBtn;
	Button editBtn;
	Button unsubBtn;

	// 如意套餐对话框
	RadioGroup radioGroup;
	RadioButton rb10;
	RadioButton rb8;
	RadioButton rb6;
	RadioButton rb4;
	RadioButton rb2;
	TextView dialog_tips;
	Button ruyipackage_okBtn;
	Button ruyipackage_cancelBtn;
	String ruyipackage_str = "10";
	String ruyipackage_str_subscribe = "10";
	boolean editchanged = false;// true 为改变
	boolean subscribechanged = false;// true 为改变

	// 如意传情
	TextView ruyichuanqing_ssq;
	TextView ruyichuanqing_fc3d;
	TextView ruyichuanqing_qlc;
	//wangyl 8.25 添加排列三和大乐透
	TextView ruyichuanqing_dlt;//大乐透
	TextView ruyichuanqing_pl3;//排列三
	LinearLayout ruyichuanqing_sub_view;
	//wangyl 8.25 如意传情代码优化
	TextView[] textViews = new TextView[5];//如意传情标签组

	private static final String[] constrs = { "5", "4", "3", "2", "1" };
	private ArrayAdapter<String> adapter;
	//wangyl 8.25 以下注释可删除
	/*// 如意传情双色球
	Spinner spinner_ssq;
	String spinner_str_ssq;
	CheckBox cb_ssq_jixuan;
	CheckBox cb_ssq_zixuan;
	Button btn_sure_ssq;
	RelativeLayout relativeLayout_ssq;
	int isJixuanOrZixuan_ssq = 1;// 1为机选，2为自选
	boolean ischeck_ssq_jixuan = false;
	boolean ischeck_ssq_zixuan = true;
	// 如意传情福彩3D
	Spinner spinner_fc3d;
	String spinner_str_fc3d;
	CheckBox cb_fc3d_jixuan;
	CheckBox cb_fc3d_zixuan;
	Button btn_sure_fc3d;
	RelativeLayout relativeLayout_fc3d;
	int isJixuanOrZixuan_fc3d = 1;// 1为机选，2为自选
	boolean ischeck_fc3d = true;
	boolean ischeck_fc3d_jixuan = false;
	boolean ischeck_fc3d_zixuan = true;
	// 如意传情七乐彩
	Spinner spinner_qlc;
	String spinner_str_qlc;
	CheckBox cb_qlc_jixuan;
	CheckBox cb_qlc_zixuan;
	Button btn_sure_qlc;
	RelativeLayout relativeLayout_qlc;
	int isJixuanOrZixuan_qlc = 1;// 1为机选，2为自选
	boolean ischeck_qlc = true;
	boolean ischeck_qlc_jixuan = false;
	boolean ischeck_qlc_zixuan = true;*/
	
	boolean aState=true;//true 显示网络对话框 false 不显示

	int iCurrentBtFlag;
	int iCurrentId;
	int viewId;

	// 幸运选号
	public static final String[] chooseLuckLotteryNum_zhonglei = { "双色球",
			"福彩3D", "七乐彩" ,"排列三","超级大乐透"};

	public static final String[] chooseLuckLotteryNum_title = { "双色球",
		"福彩3D", "七乐彩", "排列三", "超级大"+"\n"+"乐透"};
	// 周黎鸣 7.7 代码修改：幸运选号新代码
	public final static int LAYOUT_INDEX = 0;
	public final int CHECKBOX_INDEX = 100;

	Cursor cursor;
	int id = 0;

	public Integer[] gridIcon;
	public String[] gridIconName;
	/* Button xingzuo_btn , shengxiao_btn ,xingming_btn ,shengri_btn; */
	ImageView xingzuo_btn, shengxiao_btn, xingming_btn, shengri_btn; // 测试代码
	String str;
	String type01, type02, type03, type04, type05, type06, type07, type08; // 彩票种类
	EditText editTextXingming, editTextYear, editTextMonth, editTextDay;
	AlertDialog.Builder builderXingming; // 姓名的对话框
	// Integer y , m , d; //年、月、日
	static int temp;
	int year, month, day;
	// 随机号
	int[] randomNumGroup_1_ssq, randomNumGroup_1_fc3d, randomNumGroup_1_qlc,
			randomNumGroup_2_ssq, randomNumGroup_2_fc3d, randomNumGroup_2_qlc,
			randomNumGroup_3_ssq, randomNumGroup_3_fc3d, randomNumGroup_3_qlc,
			randomNumGroup_4_ssq, randomNumGroup_4_fc3d, randomNumGroup_4_qlc,
			randomNumGroup_5_ssq, randomNumGroup_5_fc3d, randomNumGroup_5_qlc;
	// 周黎鸣 7.7 代码修改：添加幸运选号图标
	public static final String[] textContent = { "星座", "生肖", "姓名", "生日" };
	public static final Integer[] imageId = { R.drawable.xingzuo,
			R.drawable.shengxiao, R.drawable.xingming, R.drawable.shengri };

	String[] gridText = new String[12];
	Integer[] gridImage = new Integer[12];

	public static final Integer[] mIcon = { R.drawable.shuangseqiu,
			R.drawable.fucai, R.drawable.qilecai, R.drawable.pailiesan, R.drawable.daletou };    //zlm 8.9 添加排列三、超级大乐透图标
	public static final Integer[] mShengxiaoIcon = {// 显示的图片数组

	R.drawable.shengxiao_1_mouse, R.drawable.shengxiao_2_bull,
			R.drawable.shengxiao, R.drawable.shengxiao_4_rabbit,
			R.drawable.shengxiao_5_dragon, R.drawable.shengxiao_6_snake,
			R.drawable.a, R.drawable.shengxiao_8_sheep,
			R.drawable.shengxiao_9_monkey, R.drawable.shengxiao_10_chicken,
			R.drawable.shengxiao_11_dog, R.drawable.shengxiao_12_pig, };

	public static final Integer[] mXingzuoIcon = { R.drawable.xingzuo_shuiping,
			R.drawable.xingzuo_shuangyu, R.drawable.xingzuo_baiyang,
			R.drawable.xingzuo_jinniu, R.drawable.xingzuo_shuangzi,
			R.drawable.xingzuo_juxie, R.drawable.xingzuo_shizi,
			R.drawable.xingzuo_chunv, R.drawable.xingzuo_tianping,
			R.drawable.xingzuo_tianxie, R.drawable.xingzuo_sheshou,
			R.drawable.xingzuo_mojie };

	public static final String[] xingzuoName = { "水瓶座", "双鱼座", "白羊座", "金牛座",
			"双子座", "巨蟹座", "狮子座", "处女座", "天枰座", "天蝎座", "射手座", "魔蝎座" };

	public static final String[] shengxiaoName = { "鼠", "牛", "虎", "兔", "龙",
			"蛇", "马", "羊", "猴", "鸡", "狗", "猪" };

	/**
	 * 幸运选号(显示小球、注数、钱数的界面部分的定义)
	 */
	public static final int BALL_WIDTH = 28;
	private static int[] aRedColorResId = { R.drawable.red };
	private static int[] aBlueColorResId = { R.drawable.blue };

	// zlm 7.13 代码修改：判断小球是否正在画
	boolean isDrawing;
	LinearLayout[] layoutAll;
	int countLinearLayout = 0;
	int[][] receiveRandomNum = new int[5][];
	int iProgressJizhu = 1;
	int iProgressBeishu = 1;
	int iProgressQishu = 1;

	LinearLayout ballLayoutGroup;
	LinearLayout layout01; // 第一行小球布局
	LinearLayout layout02; // 第二行小球布局
	LinearLayout layout03; // 第三行小球布局
	LinearLayout layout04; // 第四行小球布局
	LinearLayout layout05; // 第五行小球布局

	SeekBar mSeekBarJizhu; // 注数
	SeekBar mSeekBarBeishu;// 倍数
	SeekBar mSeekBarQishu;// 期数

	TextView mTextMoney;
	TextView mTextJizhu; // 几注
	TextView mTextBeishu;// 倍数
	TextView mTextQishu;// 期数

	TextView agreeAndpayTitleView; // 幸运选号中确认页面的标题 周黎鸣 代码修改：7.3添加的代码

	LinearLayout agreePayBallLayout01; // 周黎鸣 代码修改：7.3添加的代码
	LinearLayout agreePayBallLayout02;
	LinearLayout agreePayBallLayout03;
	LinearLayout agreePayBallLayout04;
	LinearLayout agreePayBallLayout05;

	/**
	 * 幸运选号各项ID
	 */
	public final static int ID_CLLN_MAINLISTVIEW = 4;
	public final static int ID_CLLN_GRID_VIEW = 5;
	public final static int ID_CLLN_XINGMING_DIALOG_LISTVIEW = 6;
	public final static int ID_CLLN_SHENGRI_DIALOG_LISTVIEW = 7;
	public final static int ID_CLLN_BUTTON_SET = 8;
	public final static int ID_CLLN_SSQ_BUTTON_SET = 16;
	public final static int ID_CLLN_SHOWBALLMONRY = 17;
	public final static int ID_CLLN_SHOW_ZHIFU_DIALOG = 18;
	public final static int ID_CLLN_SHOW_TRADE_SUCCESS = 19;

	// 联网 2010/7/9陈晨
	private static final int PROGRESS_VALUE = 0;
	ProgressDialog progressDialog;
	PopupWindow popupwindow;
	TextView text, mingCheng;

	// 接口
	JSONArray jsonObject3;
	String lottery_type;
	int iretrytimes = 2;
	String re;
	String error_code;
	JSONObject obj;
	String phonenum;
	String sessionid;
	String tsubscribeIdssq;
	String tsubscribeIdfc3d;
	String tsubscribeIdqlc;
	//wangyl 8.27 add pl3 and dlt
	String tsubscribeIdpl3;
	String tsubscribeIddlt;
	String tsubscribeId;
	String textssq;
	String textfc3d;
	String textqlc;
	//wangyl 8.27 add pl3 and dlt
	String textpl3;
	String textdlt;
	ShellRWSharesPreferences shellRWtext;
	String lotterytype;
	String state;

	// 周黎鸣 7.5 代码修改：添加退出检测的代码————标记
	private int iQuitFlag = 0; // 代表退出
	// private boolean iCallOnKeyDownFlag=false;

	// 处理幸运选号投注返回码 2010/7/4 陈晨
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			case 0:
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				// 王艳玲7.9修改套餐接口
				progressDialog.dismiss();// 取消提示框
				// 将信息写入preferences 先清空，再写入 2010/7/10 陈晨
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						RuyiExpressFeel.this, "addInfo");
				if (iCurrentBtFlag == -1) {// 查询
					PublicMethod.myOutput("--------into chaxun-----"
							+ ssqamount + "---fc3damount-----" + fc3damount
							+ "---qlcamount----" + qlcamount);
					shellRW.setUserLoginInfo("ssqtext", "");
					shellRW.setUserLoginInfo("fc3dtext", "");
					shellRW.setUserLoginInfo("qlctext", "");
					//wangyl 8.27 add pl3 and dlt
					shellRW.setUserLoginInfo("pl3text", "");
					shellRW.setUserLoginInfo("dlttext", "");

					if (!ssqamount.equals("")) {
						// ShellRWSharesPreferences shellRW =new
						// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
						shellRW.setUserLoginInfo("ssqtext", ssqamount);
						PublicMethod.myOutput("-------------------handle------"
								+ shellRW.getUserLoginInfo("ssqtext"));
					}
					if (!fc3damount.equals("")) {
						// ShellRWSharesPreferences shellRW =new
						// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
						shellRW.setUserLoginInfo("fc3dtext", fc3damount);
						PublicMethod.myOutput("-------------------handle------"
								+ shellRW.getUserLoginInfo("fc3dtext"));
					}
					if (!qlcamount.equals("")) {
						// ShellRWSharesPreferences shellRW =new
						// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
						shellRW.setUserLoginInfo("qlctext", qlcamount);
						PublicMethod.myOutput("-------------------handle------"
								+ shellRW.getUserLoginInfo("qlctext"));
					}
					//wangyl 8.27 add pl3 and dlt
					if (!pl3amount.equals("")) {
						shellRW.setUserLoginInfo("pl3text", pl3amount);
						PublicMethod.myOutput("-------------------handle------"
								+ shellRW.getUserLoginInfo("pl3text"));
					}
					if (!dltamount.equals("")) {
						shellRW.setUserLoginInfo("dlttext", dltamount);
						PublicMethod.myOutput("-------------------handle------"
								+ shellRW.getUserLoginInfo("dlttext"));
					}
				} else if (iCurrentBtFlag == -2) {
					shellRW.setUserLoginInfo("ssqtext", "");
					shellRW.setUserLoginInfo("fc3dtext", "");
					shellRW.setUserLoginInfo("qlctext", "");
					//wangyl 8.27 add pl3 and dlt
					shellRW.setUserLoginInfo("pl3text", "");
					shellRW.setUserLoginInfo("dlttext", "");
				} else {
					int iTempIcon = (iCurrentId - 100) % 3;
					if (iTempIcon == 0) {// 订购
						PublicMethod.myOutput("------fff---" + iCurrentId + " "
								+ ruyipackage_str_subscribe);
						int iTempLine = (iCurrentId - 100) / 3;
						if (iTempLine == 0) {
							shellRW.setUserLoginInfo("ssqtext", ""
									+ ruyipackage_str_subscribe);
						} else if (iTempLine == 1) {
							shellRW.setUserLoginInfo("fc3dtext", ""
									+ ruyipackage_str_subscribe);
						} else if (iTempLine == 2) {
							shellRW.setUserLoginInfo("qlctext", ""
									+ ruyipackage_str_subscribe);
						}
						//wangyl 8.27 add pl3 and dlt
						else if(iTempLine == 3){
							shellRW.setUserLoginInfo("pl3text", ""+ ruyipackage_str_subscribe);
						}else if(iTempLine == 4){
							shellRW.setUserLoginInfo("dlttext", ""+ ruyipackage_str_subscribe);
						}
					} else if (iTempIcon == 1) { // 修改
						int iTempLine = (iCurrentId - 100) / 3;
						if (iTempLine == 0) {
							shellRW.setUserLoginInfo("ssqtext", ""
									+ ruyipackage_str);
						} else if (iTempLine == 1) {
							shellRW.setUserLoginInfo("fc3dtext", ""
									+ ruyipackage_str);
						} else if (iTempLine == 2) {
							shellRW.setUserLoginInfo("qlctext", ""
									+ ruyipackage_str);
						}
						//wangyl 8.27 add pl3 and dlt
						else if(iTempLine == 3){
							shellRW.setUserLoginInfo("pl3text", ""+ ruyipackage_str);
						}else if(iTempLine == 4){
							shellRW.setUserLoginInfo("dlttext", ""+ ruyipackage_str);
						}
					} else if (iTempIcon == 2) { // 退订
						int iTempLine = (iCurrentId - 100) / 3;
						if (iTempLine == 0) {
							shellRW.setUserLoginInfo("ssqtext", "");
						} else if (iTempLine == 1) {
							shellRW.setUserLoginInfo("fc3dtext", "");
						} else if (iTempLine == 2) {
							shellRW.setUserLoginInfo("qlctext", "");
						}
						//wangyl 8.27 add pl3 and dlt
						else if(iTempLine == 3){
							shellRW.setUserLoginInfo("pl3text", "");
						}else if(iTempLine == 4){
							shellRW.setUserLoginInfo("dlttext", "");
						}
					}
				}
				showRuyiPackageListView();
				break;
			case 1:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败余额不足！", Toast.LENGTH_LONG)
						.show();
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结束！", Toast.LENGTH_LONG)
						.show();
				break;
			case 3:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "系统结算，请稍后！", Toast.LENGTH_LONG)
						.show();
				break;
			case 4:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "无空闲逻辑机！", Toast.LENGTH_LONG)
						.show();
				break;
			// case 5:
			// //需要添加AlertDialog注册失败
			// break;
			case 6:
				// //需要添加AlertDialog提示用户登录成功
				progressDialog.dismiss();
				if(type06.equals("ssq") || type06.equals("fc3d") || type06.equals("qlc")){
				Toast.makeText(getBaseContext(), "投注成功！", Toast.LENGTH_LONG)
						.show();
				}else if(type06.equals("pl3") || type06.equals("cjdlt")){
					Toast.makeText(getBaseContext(), "投注已受理！", Toast.LENGTH_LONG)
					.show();
				}
				showListView(ID_CLLN_MAINLISTVIEW);      //zlm 7.28 代码修改       返回幸运选号主列表
				break;
			case 7:
				progressDialog.dismiss();

				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(RuyiExpressFeel.this,
						UserLogin.class);
				startActivity(intentSession);
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				// alert1("请登录");
				break;
			case 8:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG)
						.show();
				break;
			case 9:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "投注失败！", Toast.LENGTH_LONG)
						.show();
				break;
			// 新加的信息处理
			case 10:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "套餐已定制！", Toast.LENGTH_LONG)
						.show();
				break;
			case 11:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "用户余额不足！", Toast.LENGTH_LONG)
						.show();
				break;
			case 12:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "定制套餐失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 13:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "用户无套餐记录！", Toast.LENGTH_LONG)
						.show();
				break;
			case 14:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "退订失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 15:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "套餐修改失败！", Toast.LENGTH_LONG)
						.show();
				break;
			case 16:
//				查询完之后修改订购 陈晨 20100729
				progressDialog.dismiss();
				if (viewId == 101 || viewId == 104 || viewId == 107 || viewId == 110 || viewId == 113) {
				    showRuyiPackageEdit(viewId);
//					showRuyiPackageEdit(view);
				}
				// 退订
				if (viewId == 102 || viewId == 105 || viewId == 108 || viewId == 111 || viewId == 114) {
					// showRuyiPackageUnSubscribe(view,textView);
					showRuyiPackageUnSubscribe(viewId);
				}
//				showRuyiPackageUnSubscribe(viewId);
//				PackageUnSubscribe();
				break;
			case 17:
				 progressDialog.dismiss();
				 Toast.makeText(getBaseContext(), "投注成功！", Toast.LENGTH_LONG)
				.show();
				 break;
			case 18:
				 progressDialog.dismiss();
				 Toast.makeText(getBaseContext(), "彩票投注中！", Toast.LENGTH_LONG)
				.show();
				 break;
			//wangyl 8.30 体彩套餐订购接口
//			case 19:
//				 progressDialog.dismiss();
//				 Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG).show();
//				 break;	
			case 20:
				 progressDialog.dismiss();
				 Toast.makeText(getBaseContext(), "期号不合法！", Toast.LENGTH_LONG).show();
				 break;
			case 21:
				 progressDialog.dismiss();
				 Toast.makeText(getBaseContext(), "账户异常！", Toast.LENGTH_LONG).show();
				 break;
			}
			//				
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");

		super.onCreate(savedInstanceState);
		// 显示如意传情主列表
		showListView(ID_MAINRUYIEXPRESSFEEL);

	}

	/**
	 * 列表选择
	 * 
	 * @param int listviewid 列表ID
	 */
	private void showListView(int listviewid) {
		switch (listviewid) {
		// 如意传情主列表
		case ID_MAINRUYIEXPRESSFEEL:
			iQuitFlag = 0; // 周黎鸣 7.5 代码修改：代表退出
			// iCallOnKeyDownFlag=false;
			showMainRuyiExpressFeelListView();
			break;
		case ID_RUYIPACKAGE:
			iQuitFlag = 10; // 周黎鸣 7.5 代码修改：代表返回主列表
			// iCallOnKeyDownFlag=false;
			// handle处理获取套餐信息 0709 wyl
			// showRuyiPackageListView();
			//cc 8.11
//			showDialog(PROGRESS_VALUE);
			iCurrentBtFlag = -1;
			ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
					RuyiExpressFeel.this, "addInfo");
			sessionid = shellRW.getUserLoginInfo("sessionid");
			if (sessionid.equalsIgnoreCase("")) {
				iCurrentBtFlag = -2;
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
			iHttp.whetherChange=false;
			setFlag();
			break;
		case ID_CLLN_MAINLISTVIEW:
			iQuitFlag = 10; // 周黎鸣 7.5 代码修改：代表返回主列表
			// iCallOnKeyDownFlag=false;
			showCLLNMainListView();
			break;
		case ID_RUYICHUANQING:
			iQuitFlag = 10; // 周黎鸣 7.5 代码修改：代表返回主列表
			// iCallOnKeyDownFlag=false;
			// wangyl 7.12 判断是否登录
			ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
					RuyiExpressFeel.this, "addInfo");
			String sessionid = pre.getUserLoginInfo("sessionid");
			if (sessionid.equals("")) {
				Intent intentSession = new Intent(RuyiExpressFeel.this,
						UserLogin.class);
				startActivity(intentSession);
				return;
			} else {
				showRuYiChuanQing();
			}
			break;
			//wangyl 8.25 以下注释可删除
		/*case ID_RUYICHUANQING_SSQ:
			showRuyichuanqingSSQ();
			break;
		case ID_RUYICHUANQING_FC3D:
			showRuyichuanqingFC3D();
			break;
		case ID_RUYICHUANQING_QLC:
			showRuyichuanqingQLC();
			break;*/
		}

	}

	/**
	 * 幸运选号列表选择
	 * 
	 * @param listviewid
	 *            列表ID
	 * @param aGameType
	 *            彩票类型
	 */
	private void showXingYunXuanHaoListView(int listviewid, String aGameType) {
		switch (listviewid) {
		case ID_CLLN_SHOWBALLMONRY:
			iQuitFlag = 20; // 周黎鸣 7.5 代码修改：代表返回幸运选号主列表
			// iCallOnKeyDownFlag=false;
			chooseLuckNumShowBallMoney(aGameType);
			break;
		case ID_CLLN_SHOW_ZHIFU_DIALOG:
			showAgreeAndPayDialog(aGameType);
			break;
		case ID_CLLN_SHOW_TRADE_SUCCESS:
			showTradeSuccess();
			break;
		}
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 退出检测
	 * 
	 * @param keyCode
	 *            返回按键的号码
	 * @param event
	 *            事件
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>NoticePrizesOfLottery key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			break;
		}
			// 周黎鸣 7.8 代码修改：添加新的判断
		case 0x12345678: {
			/*
			 * if(iCallOnKeyDownFlag==false){ iCallOnKeyDownFlag=true;}
			 */
			switch (iQuitFlag) {
			case 0:
				WhetherQuitDialog iQuitDialog = new WhetherQuitDialog(this,
						this);
				iQuitDialog.show();
				break;
			case 10:
				setContentView(R.layout.ruyihelper_listview);
				showListView(ID_MAINRUYIEXPRESSFEEL);
				break;
			case 20:
				setContentView(R.layout.choose_luck_lottery_num_main);
				showListView(ID_CLLN_MAINLISTVIEW);
				break;
			}

			break;
		}
		}
		return false;
		// return super.onKeyDown(keyCode, event);
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 取消
	 */
	public void onCancelClick() {
		// TODO Auto-generated method stub
		// iCallOnKeyDownFlag=false;
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	/**
	 * 确定
	 */
	public void onOkClick(int[] nums) {
		// TODO Auto-generated method stub
		// 退出
		this.finish();
	}

	/**
	 *如意传情主列表
	 */
	private void showMainRuyiExpressFeelListView() {

		setContentView(R.layout.ruyihelper_listview);

		ListView listview = (ListView) findViewById(R.id.ruyihelper_listview_id);
		// 标题
		/*
		 * TextView title = new TextView(this);
		 * title.setText(getString(R.string.ruyichuangqing));
		 * title.setTextColor(Color.BLACK); LinearLayout myLinearLayout = new
		 * LinearLayout(this); LinearLayout.LayoutParams paramtitle = new
		 * LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
		 * LinearLayout.LayoutParams.WRAP_CONTENT );
		 * myLinearLayout.setGravity(Gravity.CENTER);
		 * myLinearLayout.addView(title,paramtitle);
		 * listview.addHeaderView(myLinearLayout);
		 * registerForContextMenu(listview);
		 */

		// 数据源
		list = getListForMainExpressFeelAdapter();

		// 适配器
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.ruyihelper_listview_icon_item, new String[] { ICON,
						TITLE }, new int[] { R.id.ruyihelper_icon,
						R.id.ruyihelper_icon_text });

		listview.setAdapter(adapter);

		OnItemClickListener clickListener = new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				texttitle = (TextView) view
						.findViewById(R.id.ruyihelper_icon_text);
				String str = texttitle.getText().toString();
				/* 如意传情 */
				if (getString(R.string.ruyichuangqing).equals(str)) {
					showListView(ID_RUYICHUANQING);
				}
				/* 如意套餐 */
				if (getString(R.string.ruyitaocan).equals(str)) {
					showListView(ID_RUYIPACKAGE);
				}
				/* 幸运选号 */
				if (getString(R.string.xingyunxuanhao).equals(str)) {
					showListView(ID_CLLN_MAINLISTVIEW);
				}
			}

		};
		listview.setOnItemClickListener(clickListener);

	}

	/**
	 * 如意套餐列表
	 */
	private void showRuyiPackageListView() {
		// setFlag(); // 获取订购状态
		setContentView(R.layout.ruyipackage_listview);

		// 标题
		// 周黎鸣7.3代码修改：将Button换成ImageButton
		ImageView imageview = (ImageView) findViewById(R.id.ruyipackage_btn_return);
		imageview.setOnClickListener(new ImageView.OnClickListener() {

			public void onClick(View v) {
				showListView(ID_MAINRUYIEXPRESSFEEL);
			}

		});

		PublicMethod.myOutput("---------setRuyiPackageListView--------");

		textview = (TextView) findViewById(R.id.ruyipackage_text);

		ListView listview = (ListView) findViewById(R.id.ruyipackage_listview_id);
		for (int ii = 0; ii < 3; ii++) {
			PublicMethod.myOutput("-----ff--" + subscribeFlag[ii]);
		}
		// 适配器
		ruyiPackageAdapter = new RuyiPackageEfficientAdapter(this);
		listview.setAdapter(ruyiPackageAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("TAG", "onItemClick id:" + arg2);
			}
		});

		listview.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Log.d("TAG", "onItemSelected id:" + arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Log.d("TAG", "onNothingSelected");
			}

		});

	}

	/*
	 * public void setRuyiPackageListView(){
	 * 
	 * setFlag();
	 * PublicMethod.myOutput("---------setRuyiPackageListView--------");
	 * 
	 * //怎样获得状态
	 * 
	 * LayoutInflater mInflater = LayoutInflater.from(this); View convertView =
	 * mInflater.inflate(R.layout.ruyipackage_listview, null); textview =
	 * (TextView) convertView.findViewById(R.id.ruyipackage_text);
	 * 
	 * ListView listview = (ListView)convertView.
	 * findViewById(R.id.ruyipackage_listview_id); for(int ii=0;ii<3;ii++){
	 * PublicMethod.myOutput("-----ff--"+subscribeFlag[ii]); } //适配器
	 * ruyiPackageAdapter = new RuyiPackageEfficientAdapter(this);
	 * 
	 * listview.setAdapter(ruyiPackageAdapter);
	 * 
	 * listview.setOnItemClickListener(new OnItemClickListener(){
	 * 
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { Log.d("TAG","onItemClick id:"+arg2); } });
	 * 
	 * listview.setOnItemSelectedListener(new OnItemSelectedListener(){
	 * 
	 * @Override public void onItemSelected(AdapterView<?> arg0, View arg1, int
	 * arg2, long arg3) { Log.d("TAG","onItemSelected id:"+arg2); }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> arg0) {
	 * Log.d("TAG","onNothingSelected"); }
	 * 
	 * }); }
	 */
	/**
	 * 如意套餐列表适配器
	 */
	public class RuyiPackageEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		Context context;

		public RuyiPackageEfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}

		@Override
		public int getCount() {
			return chooseLuckLotteryNum_zhonglei.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
					RuyiExpressFeel.this, "addInfo");
			sessionid = shellRW.getUserLoginInfo("sessionid");
			if (sessionid.equals("")) {
				holderText[0] = "";
				holderText[1] = "";
				holderText[2] = "";
				holderText[3] = "";
				holderText[4] = "";
			} else {
				// 显示已订购套餐
				ShellRWSharesPreferences shellRWtext = new ShellRWSharesPreferences(
						RuyiExpressFeel.this, "addInfo");
				textssq = shellRWtext.getUserLoginInfo("ssqtext");
				PublicMethod.myOutput("-------holderText [0]textssq--------"
						+ textssq);

				textfc3d = shellRWtext.getUserLoginInfo("fc3dtext");
				PublicMethod.myOutput("-------holderText [0]textfc3d--------"
						+ textfc3d);

				textqlc = shellRWtext.getUserLoginInfo("qlctext");
				PublicMethod.myOutput("-------holderText [0]textqlc--------"
						+ textqlc);
				textpl3 = shellRWtext.getUserLoginInfo("pl3text");
				PublicMethod.myOutput("-------holderText [0]textqlc--------"+ textpl3);
				
				textdlt = shellRWtext.getUserLoginInfo("dlttext");
				PublicMethod.myOutput("-------holderText [0]textqlc--------"+ textdlt);
				if (textssq != null && !textssq.equalsIgnoreCase("")) {
					holderText[0] = "机选每期" + textssq + "元套餐";
					subscribeFlag[0] = false;
					PublicMethod.myOutput("------holderText [0]--------"
							+ holderText[0]);
				} else {
					holderText[0] = "";
					subscribeFlag[0] = true;
					// holderText [0]="机选每期"+ssqamount+"元套餐";
					// PublicMethod.myOutput("------holderText [0]else--------"+holderText
					// [0]);
				}
				if (textfc3d != null && !textfc3d.equalsIgnoreCase("")) {
					holderText[1] = "机选每期" + textfc3d + "元套餐";
					subscribeFlag[1] = false;
					PublicMethod.myOutput("------holderText [1]--------"
							+ holderText[1]);
				} else {
					holderText[1] = "";
					subscribeFlag[2] = true;
					// holderText [1]="机选每期"+fc3damount+"元套餐";
					// PublicMethod.myOutput("------holderText [1]else--------"+holderText
					// [1]);
				}
				if (textqlc != null && !textqlc.equalsIgnoreCase("")) {
					holderText[2] = "机选每期" + textqlc + "元套餐";
					subscribeFlag[2] = false;
					PublicMethod.myOutput("------holderText [2]--------"
							+ holderText[2]);
				} else {
					holderText[2] = "";
					subscribeFlag[2] = true;
					// holderText [2]="机选每期"+qlcamount+"元套餐";
					// PublicMethod.myOutput("------holderText [2]else--------"+holderText
					// [2]);
				}
				//wangyl 8.27 add pl3 and dlt
				if (textpl3 != null && !textpl3.equalsIgnoreCase("")) {
					holderText[3] = "机选每期" + textpl3 + "元套餐";
					subscribeFlag[3] = false;
					PublicMethod.myOutput("------holderText [3]--------"+ holderText[3]);
				} else {
					holderText[3] = "";
					subscribeFlag[3] = true;
				}
				if (textdlt != null && !textdlt.equalsIgnoreCase("")) {
					holderText[4] = "机选每期" + textdlt + "元套餐";
					subscribeFlag[4] = false;
					PublicMethod.myOutput("------holderText [4]--------"+ holderText[4]);
				} else {
					holderText[4] = "";
					subscribeFlag[4] = true;
				}
			}

			// 与布局中的信息关联起来
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.ruyipackage_listview_item, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.ruyipackage_icon_id);
				holder.icon.setImageResource(mIcon[position]);
				holder.ruyipackage_title = (TextView) convertView
						.findViewById(R.id.ruyipackage_title);
				holder.ruyipackage_title
						.setText(chooseLuckLotteryNum_zhonglei[position]);
				holder.ruyipackage_text = (TextView) convertView
						.findViewById(R.id.ruyipackage_text);
				holder.ruyipackage_text.setText(holderText[position]);

				// 设置按钮
				holder.iButtonGroupLayout = (LinearLayout) convertView
						.findViewById(R.id.ruyipackage_listview_layout_button_group);
				holder.iButtonGroupLayout.setId(position + LAYOUT_INDEX1);
				iPosition = position;
				LinearLayout iImageButton = new LinearLayout(context);
				iImageButton.setOrientation(LinearLayout.HORIZONTAL);
				for (iadapter = 0; iadapter < 3; iadapter++) {
					ImageView iImage = new ImageView(context);
					PublicMethod.myOutput("-----iImage-------subscribeFlag["
							+ position + "]----------------"
							+ subscribeFlag[position]);
					// states = new boolean[3];
					// true为未订购
					if (subscribeFlag[position]) {
						imageGroup = imageGroup1;
						states[position][0] = true;
						states[position][1] = false;
						states[position][2] = false;
					} else {
						imageGroup = imageGroup2;
						states[position][0] = false;
						states[position][1] = true;
						states[position][2] = true;
					}
					iImage.setImageResource(imageGroup[iadapter]);
					iImage.setPadding(6, 0, 0, 0);
					iImage.setId(iadapter + 3 * position + 100);
					iImage.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// true为未订购
							// PublicMethod.myOutput("-----"+states.length+" "+iadapter);
							iCurrentBtFlag = iadapter;
							iCurrentId = arg0.getId();
							showRuyipackageImageGroup(arg0);
						}
					});
					iImageButton.addView(iImage);
				}
				holder.iButtonGroupLayout.addView(iImageButton);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView ruyipackage_title;
			TextView ruyipackage_text;
			LinearLayout iButtonGroupLayout;
		}
	}

// 陈晨 套餐查询 20100729
private void Qurey(){
	PublicMethod.myOutput("??????????");
	ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
			RuyiExpressFeel.this, "addInfo");
	phonenum = shellRW.getUserLoginInfo("phonenum");
	sessionid = shellRW.getUserLoginInfo("sessionid");
	// 先赋值为空
	ssqamount = "";
	fc3damount = "";
	qlcamount = "";
	//wangyl 8.27 add pl3 and dlt
	pl3amount = "";
	dltamount = "";
	if(aState=true){
	 showDialog(PROGRESS_VALUE);
	Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			while (iretrytimes < 3 && iretrytimes > 0) {
				try {
					try {
						PublicMethod.myOutput("enterrrrrrr");
						re = jrtLot.packageSelect(sessionid);
						PublicMethod
								.myOutput("--------phonenum----------showRuyiPackageListView-------------------"
										+ phonenum);
						PublicMethod
								.myOutput("---------sessionid------------showRuyiPackageListView-----------------"
										+ sessionid);
						PublicMethod.myOutput("---------sessionid------------showRuyiPackageListView-----------------"
								+ sessionid);
						PublicMethod.myOutput("-------re:" + re);
						PublicMethod.myOutput("-------re:" + re);

						obj = new JSONObject(re);

						error_code = obj.getString("error_code");
						// String state4 = obj.getString("state");//String
						// PublicMethod.myOutput("-------state------"+state);
						// PublicMethod.myOutput("-------error_code------"+error_code);
                        PublicMethod.myOutput("error_code"+error_code);
						if (error_code.equals("000000")) {
							tsubscribeId = obj.getString("tsubscribeId");
							String state4 = obj.getString("state");// String
							String lotterytype4 = obj.getString("lotNo");
							String amount_str = obj.getString("amount");
							int amount = Integer.parseInt(amount_str) / 100;
							PublicMethod.myOutput("-------state------"
									+ state);
							PublicMethod.myOutput("-------error_code------"
									+ error_code);
							PublicMethod
									.myOutput("-------lotterytype------"
											+ lotterytype);
							PublicMethod.myOutput("--------lotterytype----"+lotterytype);

							// 双色球
							if (state4.equals("正常")
									&& lotterytype4.equals("B001")) {
								// ssqFlag = true;
								subscribeFlag[0] = false;
								ssqamount = amount + "";
								// 第一次登录时记住套餐是多少元的
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textssq",
								// "机选每期"+ssqamount+"元套餐");
							} else {
								// ssqFlag = false;
								subscribeFlag[0] = true;
							}
							// 福彩3D
							if (state4.equals("正常")
									&& lotterytype4.equals("D3")) {
								// fc3dFlag = true;
								subscribeFlag[1] = false;
								fc3damount = amount + "";
								// 第一次登录时记住套餐是多少元的
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textfc3d",
								// "机选每期"+fc3damount+"元套餐");
							} else {
								// fc3dFlag = false;
								subscribeFlag[1] = true;
							}

							// 七乐彩
							if (state4.equals("正常")
									&& lotterytype4.equals("QL730")) {
								// qlcFlag = true;
								subscribeFlag[2] = false;
								qlcamount = amount + "";
								// 第一次登录时记住套餐是多少元的
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textqlc",
								// "机选每期"+qlcamount+"元套餐");
							} else {
								// qlcFlag = false;
								subscribeFlag[2] = true;
							}
							// wangyl 8.27 add pl3 and dlt
							// 排列三
							if (state4.equals("正常")
									&& lotterytype4.equals("PL3_33")) {
								subscribeFlag[3] = false;
								pl3amount = amount + "";
							} else {
								subscribeFlag[3] = true;
							}
							// 大乐透
							if (state4.equals("正常")
									&& lotterytype4.equals("DLT_23529")) {
								subscribeFlag[4] = false;
								dltamount = amount + "";
							} else {
								subscribeFlag[4] = true;
							}
							Message msg = new Message();
//							查询完再调用 退订 修改 20100729 陈晨
							msg.what = 16;
							handler.sendMessage(msg);
						} else if (error_code.equals("060004")||error_code.equals("070002")) {
//							cc 8.11
							iCurrentBtFlag = -1;
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
						// else if(error_code.equals("070002")){
						// Message msg=new Message();
						// msg.what=7;
						// handler.sendMessage(msg);
						// // 无法获取用户信息
						// }
						iretrytimes = 3;
					} catch (Exception e) {
						jsonObject3 = new JSONArray(re);

						PublicMethod
								.myOutput("-------------jsonObject3.length();--------------"
										+ jsonObject3.length());
						for (int i = 0; i < jsonObject3.length(); i++) {

							PublicMethod
									.myOutput("---------jsonObject---------"
											+ i);
							obj = jsonObject3.getJSONObject(i);
							// String state1 =
							// obj.getString("state");//String
							// // Toast.makeText(getBaseContext(), state,
							// Toast.LENGTH_SHORT);
							// String lotterytype1 = obj.getString("lotNo");
							// String amount_str = obj.getString("amount");
							// int amount =
							// Integer.parseInt(amount_str)/100;
							// PublicMethod.myOutput("-------lotterytype1------"+i+"------------"+lotterytype1);
							// PublicMethod.myOutput("-------state1------"+i+"------------"+state1);
							error_code = obj.getString("error_code");
							if (("000000").equals(error_code)) {

								// 将上面的移动到if中 2010/7/10 陈晨 只有成功时才能获取到下面的数据

								String state1 = obj.getString("state");// String
								// Toast.makeText(getBaseContext(), state,
								// Toast.LENGTH_SHORT);
								String lotterytype1 = obj
										.getString("lotNo");
								// String amount_str =
								// obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;
								PublicMethod
										.myOutput("-------lotterytype1------"
												+ i
												+ "------------"
												+ lotterytype1);
								PublicMethod.myOutput("-------state1------"
										+ i + "------------" + state1);
								PublicMethod.myOutput("--------lotterytype1----"+lotterytype1);
								// tsubscribeIdssq=obj.getString("tsubscribeId");
								// 双色球 将下面的移动到if中 2010/7/10 陈晨

								if (state1.equals("正常")
										&& lotterytype1.equals("B001")) {
									// ssqFlag = true;
									// 当正常时将钱数和流水号写入 2010/7/10 陈晨
									tsubscribeIdssq = obj
											.getString("tsubscribeId");
									PublicMethod.myOutput("----tsubscribeIdssq------"+tsubscribeIdssq);
									String amount_str = obj
											.getString("amount");
									int amount = Integer
											.parseInt(amount_str) / 100;
									subscribeFlag[0] = false;
									ssqamount = amount + "";
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textssq",
									// "机选每期"+ssqamount+"元套餐");
									PublicMethod
											.myOutput("-------subscribeFlag[0]------B001----------"
													+ ssqamount);
									PublicMethod
											.myOutput("-------subscribeFlag[0]------B001----------"
													+ subscribeFlag[0]);
									i = jsonObject3.length();
								} else {
									// ssqFlag = false;
									subscribeFlag[0] = true;
									ssqamount = "";
									PublicMethod
											.myOutput("-------subscribeFlag[0]------B001----------"
													+ subscribeFlag[0]);
								}
								PublicMethod
										.myOutput("-------tsubscribeId------"
												+ i
												+ "------------"
												+ tsubscribeId);

							}
							// PublicMethod.myOutput("-------error_code------"+i+"------------"+error_code);
							// 双色球
							// if (state.equals("正常") &&
							// lotterytype.equals("B001")) {
							// ssqFlag = true;
							// PublicMethod.myOutput("-------ssqFlag------"+i+"------------"+ssqFlag);
							// break;
							// }else{
							// ssqFlag = false;
							// }
							// if (state1.equals("正常") &&
							// lotterytype1.equals("B001")) {
							// //ssqFlag = true;
							// subscribeFlag[0]=false;
							// ssqamount = amount+"";
							// ShellRWSharesPreferences shellRW =new
							// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
							// shellRW.setUserLoginInfo("textssq",
							// "机选每期"+ssqamount+"元套餐");
							// PublicMethod.myOutput("-------subscribeFlag[0]------B001----------"+subscribeFlag[0]);
							// i=jsonObject3.length();
							// }else{
							// //ssqFlag = false;
							// subscribeFlag[0]=true;
							// PublicMethod.myOutput("-------subscribeFlag[0]------B001----------"+subscribeFlag[0]);
							// }

						}
						for (int i = 0; i < jsonObject3.length(); i++) {

							// PublicMethod.myOutput("---------jsonObject---------"+i);
							obj = jsonObject3.getJSONObject(i);
							// 移动到if语句中 2010/7/10 陈晨
							// String state2 = obj.getString("state");
							// String lotterytype2 = obj.getString("lotNo");
							// String amount_str = obj.getString("amount");
							// int amount =
							// Integer.parseInt(amount_str)/100;
							// PublicMethod.myOutput("-------lotterytype2------"+i+"------------"+lotterytype2);
							// PublicMethod.myOutput("-------state2------"+i+"------------"+state2);

							if (("000000").equals(error_code)) {
								// Message msg = new Message();
								// msg.what=0;
								// handler.sendMessage(msg);
								tsubscribeIdfc3d = obj
										.getString("tsubscribeId");
								// 将上面的移动到if中 2010/7/10 陈晨
								String state2 = obj.getString("state");
								String lotterytype2 = obj
										.getString("lotNo");
								// String amount_str =
								// obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;
								PublicMethod
										.myOutput("-------lotterytype2------"
												+ i
												+ "------------"
												+ lotterytype2);
								PublicMethod.myOutput("-------state2------"
										+ i + "------------" + state2);
								PublicMethod
										.myOutput("-------tsubscribeId------"
												+ i
												+ "------------"
												+ tsubscribeId);
								
								PublicMethod.myOutput("-------tsubscribeId------"
										+ i
										+ "------------"
										+ tsubscribeId);
								
								// 福彩3D 将下面的移动到if中 2010/7/10 陈晨
								if (state2.equals("正常")
										&& lotterytype2.equals("D3")) {
									// fc3dFlag = true;
									subscribeFlag[1] = false;
									// 当正常时将钱数和流水号写入 2010/7/10 陈晨
									String amount_str = obj
											.getString("amount");
									int amount = Integer
											.parseInt(amount_str) / 100;
									fc3damount = amount + "";
									tsubscribeIdfc3d = obj
											.getString("tsubscribeId");
									PublicMethod
											.myOutput("-------fc3damount---------D3-------"
													+ fc3damount);
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textfc3d",
									// "机选每期"+fc3damount+"元套餐");
									PublicMethod
											.myOutput("-------subscribeFlag[1]---------D3-------"
													+ subscribeFlag[1]);
									i = jsonObject3.length();
								} else {
									// fc3dFlag = false;
									subscribeFlag[1] = true;
									fc3damount = "";
									PublicMethod
											.myOutput("-------subscribeFlag[1]---------D3-------"
													+ subscribeFlag[1]);
								}
							}

							// error_code = obj.getString("error_code");
							// PublicMethod.myOutput("-------error_code------"+i+"------------"+error_code);

							// 福彩3D

							// if (state.equals("正常") &&
							// lotterytype.equals("D3")) {
							// fc3dFlag = true;
							// PublicMethod.myOutput("-------fc3dFlag------"+i+"------------"+fc3dFlag);
							// break;
							// }else{
							// fc3dFlag = false;
							// }
							// if (state2.equals("正常") &&
							// lotterytype2.equals("D3")) {
							// //fc3dFlag = true;
							// subscribeFlag[1]=false;
							// fc3damount = amount+"";
							// ShellRWSharesPreferences shellRW =new
							// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
							// shellRW.setUserLoginInfo("textfc3d",
							// "机选每期"+fc3damount+"元套餐");
							// PublicMethod.myOutput("-------subscribeFlag[1]---------D3-------"+subscribeFlag[1]);
							// i=jsonObject3.length();
							// }else{
							// //fc3dFlag = false;
							// subscribeFlag[1]=true;
							// PublicMethod.myOutput("-------subscribeFlag[1]---------D3-------"+subscribeFlag[1]);
							// }

						}
						for (int i = 0; i < jsonObject3.length(); i++) {

							// PublicMethod.myOutput("---------jsonObject---------"+i);
							obj = jsonObject3.getJSONObject(i);
							// String state3 = obj.getString("state");
							// String lotterytype3 = obj.getString("lotNo");
							// String amount_str = obj.getString("amount");
							// int amount =
							// Integer.parseInt(amount_str)/100;
							// PublicMethod.myOutput("-------lotterytype3------"+i+"------------"+lotterytype3);
							// PublicMethod.myOutput("-------state3------"+i+"------------"+state3);

							if (("000000").equals(error_code)) {
								// tsubscribeIdqlc=obj.getString("tsubscribeId");

								// 将上面的移动到if中 2010/7/10 陈晨
								String state3 = obj.getString("state");
								String lotterytype3 = obj
										.getString("lotNo");
								// String amount_str =
								// obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;

								PublicMethod
										.myOutput("-------lotterytype3------"
												+ i
												+ "------------"
												+ lotterytype3);
								PublicMethod.myOutput("-------state3------"
										+ i + "------------" + state3);

								// 七乐彩 将下面的移动到if中 2010/7/10 陈晨
								if (state3.equals("正常")
										&& lotterytype3.equals("QL730")) {
									// qlcFlag = true;
									// 当正常时将钱数和流水号写入
									tsubscribeIdqlc = obj
											.getString("tsubscribeId");
									subscribeFlag[2] = false;
									String amount_str = obj
											.getString("amount");
									int amount = Integer
											.parseInt(amount_str) / 100;
									qlcamount = amount + "";
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textqlc",
									// "机选每期"+qlcamount+"元套餐");
									PublicMethod
											.myOutput("-------subscribeFlag[2]-------qlcamount-----------"
													+ qlcamount);
									// PublicMethod.myOutput("-------subscribeFlag[2]--------shellRW.getUserLoginInfo-----------"+shellRW.getUserLoginInfo("textqlc"));
									PublicMethod
											.myOutput("-------subscribeFlag[2]--------QL730-----------"
													+ subscribeFlag[2]);
									i = jsonObject3.length();
								} else {
									// qlcFlag = false;
									subscribeFlag[2] = true;
									qlcamount = "";
									PublicMethod
											.myOutput("-------subscribeFlag[2]--------QL730-----------"
													+ subscribeFlag[2]);
								}
								PublicMethod
										.myOutput("-------tsubscribeId------"
												+ i
												+ "------------"
												+ tsubscribeId);
							}


						}
						//wangyl 8.27 add pl3 and dlt
						for (int i = 0; i < jsonObject3.length(); i++) {

							obj = jsonObject3.getJSONObject(i);

							if (("000000").equals(error_code)) {
								String state4 = obj.getString("state");
								String lotterytype4 = obj.getString("lotNo");

								if (state4.equals("正常")&& lotterytype4.equals("PL3_33")) {
									// 当正常时将钱数和流水号写入
									tsubscribeIdpl3 = obj.getString("tsubscribeId");
									subscribeFlag[3] = false;
									String amount_str = obj.getString("amount");
									int amount = Integer.parseInt(amount_str) / 100;
									pl3amount = amount + "";
									i = jsonObject3.length();
								} else {
									subscribeFlag[3] = true;
									pl3amount = "";
								}
							}


						}
						
						for (int i = 0; i < jsonObject3.length(); i++) {

							obj = jsonObject3.getJSONObject(i);

							if (("000000").equals(error_code)) {
								String state5 = obj.getString("state");
								String lotterytype5 = obj.getString("lotNo");

								if (state5.equals("正常")&& lotterytype5.equals("DLT_23529")) {
									// 当正常时将钱数和流水号写入
									tsubscribeIddlt = obj.getString("tsubscribeId");
									subscribeFlag[4] = false;
									String amount_str = obj.getString("amount");
									int amount = Integer.parseInt(amount_str) / 100;
									dltamount = amount + "";
									i = jsonObject3.length();
								} else {
									subscribeFlag[4] = true;
									dltamount = "";
								}
							}


						}
						
						
						
						// handel成功
						Message msg = new Message();
						msg.what = 16;
						handler.sendMessage(msg);
					}
					iretrytimes = 3;
				} catch (JSONException e) {
					e.printStackTrace();
					iretrytimes--;
					error_code="00";
				}
			}
			
			if(error_code.equalsIgnoreCase("00")&&iHttp.whetherChange == true){
				Message msg = new Message();
				msg.what = 8;
				handler.sendMessage(msg);
			}
//			加入是否改变切入点判断 陈晨 8.11
			if (iretrytimes == 0 && iHttp.whetherChange == false) {
				iHttp.whetherChange = true;
				if (iHttp.conMethord == iHttp.CMWAP) {
					iHttp.conMethord = iHttp.CMNET;
				} else {
					iHttp.conMethord = iHttp.CMWAP;
				}
				iretrytimes=2;
				PublicMethod.myOutput("=====qierudian=Qurey()====");
				Qurey();
			}
			iretrytimes = 2;
		
		}

	});
	t.start();
	}
	aState=true;
					
}
    /**
	 * 如意套餐中各个彩种的订购状态
	 */
	private void setFlag() {
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		// 先赋值为空
		ssqamount = "";
		fc3damount = "";
		qlcamount = "";
		//wangyl 8.27 add pl3 and dlt
		pl3amount = "";
		dltamount = "";
		showDialog(PROGRESS_VALUE);
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (iretrytimes < 3 && iretrytimes > 0) {
					try {
						try {
							re = jrtLot.packageSelect(sessionid);
							PublicMethod
									.myOutput("--------phonenum----------showRuyiPackageListView-------------------"
											+ phonenum);
							PublicMethod
									.myOutput("---------sessionid------------showRuyiPackageListView-----------------"
											+ sessionid);
							PublicMethod.myOutput("-------re:" + re);

							obj = new JSONObject(re);

							error_code = obj.getString("error_code");
							// String state4 = obj.getString("state");//String
							// PublicMethod.myOutput("-------state------"+state);
							// PublicMethod.myOutput("-------error_code------"+error_code);

							if (error_code.equals("000000")) {
								tsubscribeId = obj.getString("tsubscribeId");
								String state4 = obj.getString("state");// String
								String lotterytype4 = obj.getString("lotNo");
								String amount_str = obj.getString("amount");
								int amount = Integer.parseInt(amount_str) / 100;
								PublicMethod.myOutput("-------state------"
										+ state);
								PublicMethod.myOutput("-------error_code------"
										+ error_code);
								PublicMethod
										.myOutput("-------lotterytype------"
												+ lotterytype);

								// 双色球
								if (state4.equals("正常")
										&& lotterytype4.equals("B001")) {
									// ssqFlag = true;
									subscribeFlag[0] = false;
									ssqamount = amount + "";
									// 第一次登录时记住套餐是多少元的
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textssq",
									// "机选每期"+ssqamount+"元套餐");
								} else {
									// ssqFlag = false;
									subscribeFlag[0] = true;
								}
								// 福彩3D
								if (state4.equals("正常")
										&& lotterytype4.equals("D3")) {
									// fc3dFlag = true;
									subscribeFlag[1] = false;
									fc3damount = amount + "";
									// 第一次登录时记住套餐是多少元的
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textfc3d",
									// "机选每期"+fc3damount+"元套餐");
								} else {
									// fc3dFlag = false;
									subscribeFlag[1] = true;
								}

								// 七乐彩
								if (state4.equals("正常")
										&& lotterytype4.equals("QL730")) {
									// qlcFlag = true;
									subscribeFlag[2] = false;
									qlcamount = amount + "";
									// 第一次登录时记住套餐是多少元的
									// ShellRWSharesPreferences shellRW =new
									// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
									// shellRW.setUserLoginInfo("textqlc",
									// "机选每期"+qlcamount+"元套餐");
								} else {
									// qlcFlag = false;
									subscribeFlag[2] = true;
								}
								//wangyl 8.27 add pl3 and dlt
								// 排列三
								if (state4.equals("正常")
										&& lotterytype4.equals("PL3_33")) {
									subscribeFlag[3] = false;
									pl3amount = amount + "";
								} else {
									subscribeFlag[3] = true;
								}
								// 大乐透
								if (state4.equals("正常")
										&& lotterytype4.equals("DLT_23529")) {
									subscribeFlag[4] = false;
									dltamount = amount + "";
								} else {
									subscribeFlag[4] = true;
								}
								Message msg = new Message();
								msg.what = 0;
								handler.sendMessage(msg);
							} else if (error_code.equals("060004")) {
								Message msg = new Message();
								msg.what = 0;
								handler.sendMessage(msg);
							}
							// else if(error_code.equals("070002")){
							// Message msg=new Message();
							// msg.what=7;
							// handler.sendMessage(msg);
							// // 无法获取用户信息
							// }
							iretrytimes = 3;
						} catch (Exception e) {
							jsonObject3 = new JSONArray(re);

							PublicMethod
									.myOutput("-------------jsonObject3.length();--------------"
											+ jsonObject3.length());
							for (int i = 0; i < jsonObject3.length(); i++) {

								PublicMethod
										.myOutput("---------jsonObject---------"
												+ i);
								obj = jsonObject3.getJSONObject(i);
								// String state1 =
								// obj.getString("state");//String
								// // Toast.makeText(getBaseContext(), state,
								// Toast.LENGTH_SHORT);
								// String lotterytype1 = obj.getString("lotNo");
								// String amount_str = obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;
								// PublicMethod.myOutput("-------lotterytype1------"+i+"------------"+lotterytype1);
								// PublicMethod.myOutput("-------state1------"+i+"------------"+state1);
								error_code = obj.getString("error_code");
								if (("000000").equals(error_code)) {

									// 将上面的移动到if中 2010/7/10 陈晨 只有成功时才能获取到下面的数据

									String state1 = obj.getString("state");// String
									// Toast.makeText(getBaseContext(), state,
									// Toast.LENGTH_SHORT);
									String lotterytype1 = obj
											.getString("lotNo");
									// String amount_str =
									// obj.getString("amount");
									// int amount =
									// Integer.parseInt(amount_str)/100;
									PublicMethod
											.myOutput("-------lotterytype1------"
													+ i
													+ "------------"
													+ lotterytype1);
									PublicMethod.myOutput("-------state1------"
											+ i + "------------" + state1);
									// tsubscribeIdssq=obj.getString("tsubscribeId");
									// 双色球 将下面的移动到if中 2010/7/10 陈晨

									if (state1.equals("正常")
											&& lotterytype1.equals("B001")) {
										// ssqFlag = true;
										// 当正常时将钱数和流水号写入 2010/7/10 陈晨
										tsubscribeIdssq = obj
												.getString("tsubscribeId");
										String amount_str = obj
												.getString("amount");
										int amount = Integer
												.parseInt(amount_str) / 100;
										subscribeFlag[0] = false;
										ssqamount = amount + "";
										// ShellRWSharesPreferences shellRW =new
										// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
										// shellRW.setUserLoginInfo("textssq",
										// "机选每期"+ssqamount+"元套餐");
										PublicMethod
												.myOutput("-------subscribeFlag[0]------B001----------"
														+ ssqamount);
										PublicMethod
												.myOutput("-------subscribeFlag[0]------B001----------"
														+ subscribeFlag[0]);
										i = jsonObject3.length();
									} else {
										// ssqFlag = false;
										subscribeFlag[0] = true;
										ssqamount = "";
										PublicMethod
												.myOutput("-------subscribeFlag[0]------B001----------"
														+ subscribeFlag[0]);
									}
									PublicMethod
											.myOutput("-------tsubscribeId------"
													+ i
													+ "------------"
													+ tsubscribeId);

								}
								// PublicMethod.myOutput("-------error_code------"+i+"------------"+error_code);
								// 双色球
								// if (state.equals("正常") &&
								// lotterytype.equals("B001")) {
								// ssqFlag = true;
								// PublicMethod.myOutput("-------ssqFlag------"+i+"------------"+ssqFlag);
								// break;
								// }else{
								// ssqFlag = false;
								// }
								// if (state1.equals("正常") &&
								// lotterytype1.equals("B001")) {
								// //ssqFlag = true;
								// subscribeFlag[0]=false;
								// ssqamount = amount+"";
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textssq",
								// "机选每期"+ssqamount+"元套餐");
								// PublicMethod.myOutput("-------subscribeFlag[0]------B001----------"+subscribeFlag[0]);
								// i=jsonObject3.length();
								// }else{
								// //ssqFlag = false;
								// subscribeFlag[0]=true;
								// PublicMethod.myOutput("-------subscribeFlag[0]------B001----------"+subscribeFlag[0]);
								// }

							}
							for (int i = 0; i < jsonObject3.length(); i++) {

								// PublicMethod.myOutput("---------jsonObject---------"+i);
								obj = jsonObject3.getJSONObject(i);
								// 移动到if语句中 2010/7/10 陈晨
								// String state2 = obj.getString("state");
								// String lotterytype2 = obj.getString("lotNo");
								// String amount_str = obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;
								// PublicMethod.myOutput("-------lotterytype2------"+i+"------------"+lotterytype2);
								// PublicMethod.myOutput("-------state2------"+i+"------------"+state2);

								if (("000000").equals(error_code)) {
									tsubscribeIdfc3d = obj
											.getString("tsubscribeId");
									// 将上面的移动到if中 2010/7/10 陈晨
									String state2 = obj.getString("state");
									String lotterytype2 = obj
											.getString("lotNo");
									// String amount_str =
									// obj.getString("amount");
									// int amount =
									// Integer.parseInt(amount_str)/100;
									PublicMethod
											.myOutput("-------lotterytype2------"
													+ i
													+ "------------"
													+ lotterytype2);
									PublicMethod.myOutput("-------state2------"
											+ i + "------------" + state2);
									PublicMethod
											.myOutput("-------tsubscribeId------"
													+ i
													+ "------------"
													+ tsubscribeId);
									// 福彩3D 将下面的移动到if中 2010/7/10 陈晨
									if (state2.equals("正常")
											&& lotterytype2.equals("D3")) {
										// fc3dFlag = true;
										subscribeFlag[1] = false;
										// 当正常时将钱数和流水号写入 2010/7/10 陈晨
										String amount_str = obj
												.getString("amount");
										int amount = Integer
												.parseInt(amount_str) / 100;
										fc3damount = amount + "";
										tsubscribeIdfc3d = obj
												.getString("tsubscribeId");
										PublicMethod
												.myOutput("-------fc3damount---------D3-------"
														+ fc3damount);
										// ShellRWSharesPreferences shellRW =new
										// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
										// shellRW.setUserLoginInfo("textfc3d",
										// "机选每期"+fc3damount+"元套餐");
										PublicMethod
												.myOutput("-------subscribeFlag[1]---------D3-------"
														+ subscribeFlag[1]);
										i = jsonObject3.length();
									} else {
										// fc3dFlag = false;
										subscribeFlag[1] = true;
										fc3damount = "";
										PublicMethod
												.myOutput("-------subscribeFlag[1]---------D3-------"
														+ subscribeFlag[1]);
									}
								}

								// error_code = obj.getString("error_code");
								// PublicMethod.myOutput("-------error_code------"+i+"------------"+error_code);

								// 福彩3D

								// if (state.equals("正常") &&
								// lotterytype.equals("D3")) {
								// fc3dFlag = true;
								// PublicMethod.myOutput("-------fc3dFlag------"+i+"------------"+fc3dFlag);
								// break;
								// }else{
								// fc3dFlag = false;
								// }
								// if (state2.equals("正常") &&
								// lotterytype2.equals("D3")) {
								// //fc3dFlag = true;
								// subscribeFlag[1]=false;
								// fc3damount = amount+"";
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textfc3d",
								// "机选每期"+fc3damount+"元套餐");
								// PublicMethod.myOutput("-------subscribeFlag[1]---------D3-------"+subscribeFlag[1]);
								// i=jsonObject3.length();
								// }else{
								// //fc3dFlag = false;
								// subscribeFlag[1]=true;
								// PublicMethod.myOutput("-------subscribeFlag[1]---------D3-------"+subscribeFlag[1]);
								// }

							}
							for (int i = 0; i < jsonObject3.length(); i++) {

								// PublicMethod.myOutput("---------jsonObject---------"+i);
								obj = jsonObject3.getJSONObject(i);
								// String state3 = obj.getString("state");
								// String lotterytype3 = obj.getString("lotNo");
								// String amount_str = obj.getString("amount");
								// int amount =
								// Integer.parseInt(amount_str)/100;
								// PublicMethod.myOutput("-------lotterytype3------"+i+"------------"+lotterytype3);
								// PublicMethod.myOutput("-------state3------"+i+"------------"+state3);

								if (("000000").equals(error_code)) {
									// tsubscribeIdqlc=obj.getString("tsubscribeId");

									// 将上面的移动到if中 2010/7/10 陈晨
									String state3 = obj.getString("state");
									String lotterytype3 = obj
											.getString("lotNo");
									// String amount_str =
									// obj.getString("amount");
									// int amount =
									// Integer.parseInt(amount_str)/100;

									PublicMethod
											.myOutput("-------lotterytype3------"
													+ i
													+ "------------"
													+ lotterytype3);
									PublicMethod.myOutput("-------state3------"
											+ i + "------------" + state3);

									// 七乐彩 将下面的移动到if中 2010/7/10 陈晨
									if (state3.equals("正常")
											&& lotterytype3.equals("QL730")) {
										// qlcFlag = true;
										// 当正常时将钱数和流水号写入
										tsubscribeIdqlc = obj
												.getString("tsubscribeId");
										subscribeFlag[2] = false;
										String amount_str = obj
												.getString("amount");
										int amount = Integer
												.parseInt(amount_str) / 100;
										qlcamount = amount + "";
										// ShellRWSharesPreferences shellRW =new
										// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
										// shellRW.setUserLoginInfo("textqlc",
										// "机选每期"+qlcamount+"元套餐");
										PublicMethod
												.myOutput("-------subscribeFlag[2]-------qlcamount-----------"
														+ qlcamount);
										// PublicMethod.myOutput("-------subscribeFlag[2]--------shellRW.getUserLoginInfo-----------"+shellRW.getUserLoginInfo("textqlc"));
										PublicMethod
												.myOutput("-------subscribeFlag[2]--------QL730-----------"
														+ subscribeFlag[2]);
										i = jsonObject3.length();
									} else {
										// qlcFlag = false;
										subscribeFlag[2] = true;
										qlcamount = "";
										PublicMethod
												.myOutput("-------subscribeFlag[2]--------QL730-----------"
														+ subscribeFlag[2]);
									}
									// Message msg = new Message();
									// msg.what=0;
									// handler.sendMessage(msg);
									PublicMethod
											.myOutput("-------tsubscribeId------"
													+ i
													+ "------------"
													+ tsubscribeId);
								}

								// error_code = obj.getString("error_code");
								// PublicMethod.myOutput("-------error_code------"+i+"------------"+error_code);

								// 七乐彩
								// if (state.equals("正常") &&
								// lotterytype.equals("QL730")) {
								// qlcFlag = true;
								// PublicMethod.myOutput("-------qlcFlag------"+i+"------------"+qlcFlag);
								// break;
								// }else{
								// qlcFlag = false;
								// }
								// //七乐彩
								// if (state3.equals("正常") &&
								// lotterytype3.equals("QL730")) {
								// //qlcFlag = true;
								// subscribeFlag[2]=false;
								// qlcamount = amount+"";
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
								// shellRW.setUserLoginInfo("textqlc",
								// "机选每期"+qlcamount+"元套餐");
								// PublicMethod.myOutput("-------subscribeFlag[2]--------QL730-----------"+subscribeFlag[2]);
								// i=jsonObject3.length();
								// }else{
								// //qlcFlag = false;
								// subscribeFlag[2]=true;
								// PublicMethod.myOutput("-------subscribeFlag[2]--------QL730-----------"+subscribeFlag[2]);
								// }

							}
							//wangyl 8.27 add pl3 and dlt
							for (int i = 0; i < jsonObject3.length(); i++) {

								obj = jsonObject3.getJSONObject(i);

								if (("000000").equals(error_code)) {
									String state4 = obj.getString("state");
									String lotterytype4 = obj.getString("lotNo");

									if (state4.equals("正常")&& lotterytype4.equals("PL3_33")) {
										// 当正常时将钱数和流水号写入
										tsubscribeIdpl3 = obj.getString("tsubscribeId");
										subscribeFlag[3] = false;
										String amount_str = obj.getString("amount");
										int amount = Integer.parseInt(amount_str) / 100;
										pl3amount = amount + "";
										i = jsonObject3.length();
									} else {
										subscribeFlag[3] = true;
										pl3amount = "";
									}
								}


							}
							
							for (int i = 0; i < jsonObject3.length(); i++) {

								obj = jsonObject3.getJSONObject(i);

								if (("000000").equals(error_code)) {
									String state5 = obj.getString("state");
									String lotterytype5 = obj.getString("lotNo");

									if (state5.equals("正常")&& lotterytype5.equals("DLT_23529")) {
										// 当正常时将钱数和流水号写入
										tsubscribeIddlt = obj.getString("tsubscribeId");
										subscribeFlag[4] = false;
										String amount_str = obj.getString("amount");
										int amount = Integer.parseInt(amount_str) / 100;
										dltamount = amount + "";
										i = jsonObject3.length();
									} else {
										subscribeFlag[4] = true;
										dltamount = "";
									}
								}


							}
							// handel成功
							Message msg = new Message();
							msg.what = 0;
							handler.sendMessage(msg);
						}
						iretrytimes = 3;
					} catch (JSONException e) {
						e.printStackTrace();
						iretrytimes--;
                        error_code="00";
					}
				}
				if(error_code.equalsIgnoreCase("00")&&iHttp.whetherChange == true){
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);
				}
//				加入是否改变切入点判断 陈晨 8.11
				if (iretrytimes == 0 && iHttp.whetherChange == false) {
					iHttp.whetherChange = true;
					if (iHttp.conMethord == iHttp.CMWAP) {
						iHttp.conMethord = iHttp.CMNET;
					} else {
						iHttp.conMethord = iHttp.CMWAP;
					}
					iretrytimes=2;
					PublicMethod.myOutput("=====qierudian=setFlag()====");
					setFlag();
				}
				iretrytimes = 2;
			}

		});
		t.start();
	}

	/**
	 * 获得如意传情主列表适配器的数据源
	 */
	private List<Map<String, Object>> getListForMainExpressFeelAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		/* 如意传情 */
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(ICON, R.drawable.ruyichuanqing);
		map.put(TITLE, getString(R.string.ruyichuangqing));
		list.add(map);

		/* 如意套餐 */
		map = new HashMap<String, Object>();
		map.put(ICON, R.drawable.ruyitaocan);
		map.put(TITLE, getString(R.string.ruyitaocan));
		list.add(map);

		/* 幸运选号 */
		map = new HashMap<String, Object>();
		map.put(ICON, R.drawable.xingyunxuanhao);
		map.put(TITLE, getString(R.string.xingyunxuanhao));
		list.add(map);

		return list;
	}

	/**
	 * 获得如意套餐列表适配器的数据源
	 */
	private void showRuyipackageImageGroup(View view) {
//		int viewId = view.getId();
		viewId = view.getId();
		// 订购
		if (viewId == 100 || viewId == 103 || viewId == 106 || viewId == 109 || viewId == 112) {
			// showRuyiPackageSubscribe(view,textView);
//			showRuyiPackageSubscribe(view);
			iHttp.whetherChange=false;
			showRuyiPackageSubscribe(viewId);// cc 8.11
		}else{
//			先查询，之后修改订购 陈晨 20100729
			iHttp.whetherChange=false;
			Qurey();
		// 修改
//		if (viewId == 101 || viewId == 104 || viewId == 107) {
//			// showRuyiPackageEdit(view,textView);
//			 Qurey();
////			showRuyiPackageEdit(view);
//		}
//		// 退订
//		if (viewId == 102 || viewId == 105 || viewId == 108) {
//			// showRuyiPackageUnSubscribe(view,textView);
//			
////			showRuyiPackageUnSubscribe(view);
//			Qurey();
////			showRuyiPackageUnSubscribe(viewId);
		}
	}

	// 王艳玲7.9修改套餐接口
	/**
	 * 如意套餐中退订按钮的执行操作
	 * 
	 * @param view
	 *            被点击的按钮
	 */
//	退订修改参数  陈晨 20100729
//	private void showRuyiPackageUnSubscribe(View view/* ,TextView textView */) {
	private void showRuyiPackageUnSubscribe(int aViewId/* ,TextView textView */) {
		
//		boolean isatus = states[(view.getId() - 100) / 3][(view.getId() - 100) % 3];
		boolean isatus = states[(aViewId - 100) / 3][(aViewId - 100) % 3];
		PublicMethod.myOutput("------" + isatus);
		if (!isatus) {
			return;
		}
		viewid = aViewId;
		// LayoutInflater mInflater = LayoutInflater.from(this);
		// View convertView =
		// mInflater.inflate(R.layout.ruyipackage_listview_item, null);
		// textview =textView;

		// 暂时保存订购的套餐种类，并在下次登录的时候显示出来
		ShellRWSharesPreferences shellRWtext = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		textssq = shellRWtext.getUserLoginInfo("ssqtext");
		textfc3d = shellRWtext.getUserLoginInfo("fc3dtext");
		textqlc = shellRWtext.getUserLoginInfo("qlctext");
		//wangyl 8.27 add pl3 and dlt
		textpl3 = shellRWtext.getUserLoginInfo("pl3text");
		textdlt = shellRWtext.getUserLoginInfo("dlttext");

		if (aViewId == 102) {
			packageName = chooseLuckLotteryNum_zhonglei[0];// 双色球
			// textview.setText(textssq);
			lottery_type = "B001";
			holderText[0] = textssq;
			/*
			 * if(textssq.equals("unsubscribe")){ holderText[0]=""; }else{
			 * holderText[0] = textssq; }
			 */

			text_str = "机选每期" + holderText[0] + "元套餐";
			// text_str=ssqamount;
			tsubscribeId = tsubscribeIdssq;
		}
		if (aViewId == 105) {
			packageName = chooseLuckLotteryNum_zhonglei[1];// 福彩3D
			// textview.setText(textfc3d);
			lottery_type = "D3";
			holderText[1] = textfc3d;

			/*
			 * if(textfc3d.equals("unsubscribe")){ holderText[1]=""; }else{
			 * holderText[1] = textfc3d; }
			 */

			text_str = "机选每期" + holderText[1] + "元套餐";
			// text_str=fc3damount;
			tsubscribeId = tsubscribeIdfc3d;
		}
		if (aViewId == 108) {
			packageName = chooseLuckLotteryNum_zhonglei[2];// 七乐彩
			// textview.setText(textqlc);
			lottery_type = "QL730";
			holderText[2] = textqlc;
			/*
			 * if(textqlc.equals("unsubscribe")){ holderText[2]=""; }else{
			 * holderText[2] = textqlc; }
			 */
			text_str = "机选每期" + holderText[2] + "元套餐";
			// text_str=qlcamount;
			tsubscribeId = tsubscribeIdqlc;
		}
		//wangyl 8.27 add pl3 and dlt
		if (aViewId == 111) {
			packageName = chooseLuckLotteryNum_zhonglei[3];// 排列三
			lottery_type = "PL3_33";
			holderText[3] = textpl3;
			text_str = "机选每期" + holderText[3] + "元套餐";
			tsubscribeId = tsubscribeIdpl3;
		}
		if (aViewId == 114) {
			packageName = chooseLuckLotteryNum_zhonglei[4];// 大乐透
			lottery_type = "DLT_23529";
			holderText[4] = textdlt;
			text_str = "机选每期" + holderText[4] + "元套餐";
			tsubscribeId = tsubscribeIddlt;
		}

		// 周黎鸣 7.5 代码修改：添加登陆判断
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		if (sessionid.equals("")) {
			Intent intentSession = new Intent(RuyiExpressFeel.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RuyiExpressFeel.this);
			builder.setTitle(R.string.ruyipackage_unsubscribe);
			builder.setMessage(getString(R.string.ruyipackage_unsubscirbe_sure)
					+ "\n" + packageName + text_str);

			// 确定
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							 showDialog(PROGRESS_VALUE);
							Thread t = new Thread(new Runnable() {

								@Override
								public void run() {
									while (iretrytimes < 3 && iretrytimes > 0) {

										PublicMethod
												.myOutput("-------------tsubscribeId------------------"
														+ tsubscribeId);
										PublicMethod
												.myOutput("-------------sessionid------------------"
														+ sessionid);

										re = jrtLot.packageFreeze(sessionid,
												tsubscribeId);// login_User,

										try {
											obj = new JSONObject(re);
											error_code = obj
													.getString("error_code");
											PublicMethod
													.myOutput("---------unsub-----------"
															+ error_code);
											iretrytimes = 3;
										} catch (JSONException e) {
											e.printStackTrace();
											iretrytimes--;

										}
									}
									iretrytimes = 2;
									if (error_code.equals("000000")) {
										// Toast.makeText(RuyiExpressFeel.this,
										// "退订套餐成功！！",
										// Toast.LENGTH_SHORT).show();
										// textview.setText("");
										// Flag = false ;//应该修改该套餐的订购状态

										ShellRWSharesPreferences shellRWtextunsub = new ShellRWSharesPreferences(
												RuyiExpressFeel.this, "addInfo");
										// 双色球
										if (getString(R.string.shuangseqiu)
												.equals(packageName)) {
											shellRWtextunsub.setUserLoginInfo(
													"ssqtext", "");
										}
										// 福彩3D
										if (getString(R.string.fucai3d).equals(
												packageName)) {
											shellRWtextunsub.setUserLoginInfo(
													"fc3dtext", "");
										}
										// 七乐彩
										if (getString(R.string.qilecai).equals(packageName)) {
											shellRWtextunsub.setUserLoginInfo("qlctext", "");
										}
										
										//wangyl 8.27 add pl3 and dlt
										// 排列三
										if (getString(R.string.pailie3).equals(packageName)) {
											shellRWtextunsub.setUserLoginInfo("pl3text", "");
										}
										// 大乐透
										if (getString(R.string.daletou).equals(packageName)) {
											shellRWtextunsub.setUserLoginInfo("dlttext", "");
										}

										// showRuyiPackageOperaciton(title,textview,subBtn,editBtn,unsubBtn,Flag);
										// true 未订购 false 订购
										if (viewid == 102) {
											subscribeFlag[0] = true;
											holderText[0] = "";
										}
										if (viewid == 105) {
											subscribeFlag[1] = true;
											holderText[1] = "";
										}
										if (viewid == 108) {
											subscribeFlag[2] = true;
											holderText[2] = "";
										}
										//wangyl 8.27 add pl3 and dlt
										if (viewid == 111) {
											subscribeFlag[3] = true;
											holderText[3] = "";
										}
										if (viewid == 114) {
											subscribeFlag[4] = true;
											holderText[4] = "";
										}

										PublicMethod
												.myOutput("----unsub-----subscribeFlag[0]----------"
														+ subscribeFlag[0]);
										PublicMethod
												.myOutput("-----unsub----subscribeFlag[1]----------"
														+ subscribeFlag[1]);
										PublicMethod
												.myOutput("-----unsub----subscribeFlag[2]----------"
														+ subscribeFlag[2]);

										// setFlag();
										// showRuyiPackageListView();
										Message msg = new Message();
										msg.what = 0;
										handler.sendMessage(msg);

									} else if (error_code.equals("070002")) {
										Message msg = new Message();
										msg.what = 7;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "请登录", Toast.LENGTH_SHORT).show();
									}
									// else if(error_code.equals("350001")){
									// Message msg=new Message();
									// msg.what=10;
									// handler.sendMessage(msg);
									// // Toast.makeText(RuyiExpressFeel.this,
									// "套餐状态为暂停或注销，不能再次定制",
									// Toast.LENGTH_SHORT).show();
									// }
									else if (error_code.equals("060004")) {
										Message msg = new Message();
										msg.what = 13;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "套餐已定制", Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("00")) {
										Message msg = new Message();
										msg.what = 8;
										handler.sendMessage(msg);
									}
									// Toast.makeText(RuyiExpressFeel.this,
									// "网络异常", Toast.LENGTH_SHORT).show();
									else {
										Message msg = new Message();
										msg.what = 14;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "退订套餐失败！！",
										// Toast.LENGTH_SHORT).show();
									}
								}

							});
							t.start();

						}

					});
			// 取消
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

						}

					});
			builder.show();
		}

	}

	/**
	 * 如意套餐中修改按钮的执行操作
	 * 
	 * @param view
	 *            被点击的按钮
	 */
//	修改改变参数  陈晨 20100729
	private void showRuyiPackageEdit(int aViewId/* ,TextView textView */) {
		boolean isatus = states[( aViewId - 100) / 3][( aViewId - 100) % 3];
		PublicMethod.myOutput("------" + isatus);
		if (!isatus) {
			return;
		}
		viewid =  aViewId;
		// LayoutInflater mInflater = LayoutInflater.from(this);
		// View convertView =
		// mInflater.inflate(R.layout.ruyipackage_listview_item, null);
		// textview =textView;

		// 暂时保存订购的套餐种类，并在下次登录的时候显示出来

		if ( aViewId == 101) {
			packageName = chooseLuckLotteryNum_zhonglei[0];// 双色球
			// textview.setText(textssq);
			lottery_type = "B001";
			tsubscribeId = tsubscribeIdssq;
		}
		if ( aViewId == 104) {
			packageName = chooseLuckLotteryNum_zhonglei[1];// 福彩3D
			// textview.setText(textfc3d);
			lottery_type = "D3";
			tsubscribeId = tsubscribeIdfc3d;
		}
		if (aViewId == 107) {
			packageName = chooseLuckLotteryNum_zhonglei[2];// 七乐彩
			// textview.setText(textqlc);
			lottery_type = "QL730";
			tsubscribeId = tsubscribeIdqlc;
		}
		//wangyl 8.27 add pl3 and dlt
		if ( aViewId == 110) {
			packageName = chooseLuckLotteryNum_zhonglei[3];// 排列三
			lottery_type = "PL3_33";
			tsubscribeId = tsubscribeIdpl3;
			System.out.println("------------tsubscribeIdpl3-------------"+tsubscribeId);
		}
		if ( aViewId == 113) {
			packageName = chooseLuckLotteryNum_zhonglei[4];// 大乐透
			lottery_type = "DLT_23529";
			tsubscribeId = tsubscribeIddlt;
			System.out.println("------------tsubscribeIddlt-------------"+tsubscribeId);
		}
		// 周黎鸣 7.5 代码修改：添加登陆判断
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		if (sessionid.equals("")) {
			Intent intentSession = new Intent(RuyiExpressFeel.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View myView = factory.inflate(R.layout.ruyipackage_dialog, null);
			radioGroup = (RadioGroup) myView
					.findViewById(R.id.ruyipackage_radiogroup);
			rb10 = (RadioButton) myView.findViewById(R.id.ruyipackage_10);
			rb8 = (RadioButton) myView.findViewById(R.id.ruyipackage_8);
			rb6 = (RadioButton) myView.findViewById(R.id.ruyipackage_6);
			rb4 = (RadioButton) myView.findViewById(R.id.ruyipackage_4);
			rb2 = (RadioButton) myView.findViewById(R.id.ruyipackage_2);
			dialog_tips = (TextView) myView.findViewById(R.id.ruyipackage_tips);
			dialog_tips.setText("您确认将套餐修改为" + packageName + "10元套餐?");
			radioGroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							editchanged = true;
							if (checkedId == rb10.getId()) {
								dialog_tips.setText("您确认将套餐修改为" + packageName
										+ "10元套餐?");
								ruyipackage_str = "10";
							}
							if (checkedId == rb8.getId()) {
								dialog_tips.setText("您确认将套餐修改为" + packageName
										+ "8元套餐?");
								ruyipackage_str = "8";
							}
							if (checkedId == rb6.getId()) {
								dialog_tips.setText("您确认将套餐修改为" + packageName
										+ "6元套餐?");
								ruyipackage_str = "6";
							}
							if (checkedId == rb4.getId()) {
								dialog_tips.setText("您确认将套餐修改为" + packageName
										+ "4元套餐?");
								ruyipackage_str = "4";
							}
							if (checkedId == rb2.getId()) {
								dialog_tips.setText("您确认将套餐修改为" + packageName
										+ "2元套餐?");
								ruyipackage_str = "2";
							}
						}

					});
			if (!editchanged) {
				ruyipackage_str = "10";
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RuyiExpressFeel.this);
			builder.setTitle("请选择套餐总类");
			builder.setView(myView);
			// 确定
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							showDialog(PROGRESS_VALUE);
							Thread t = new Thread(new Runnable() {

								@Override
								public void run() {
									while (iretrytimes < 3 && iretrytimes > 0) {
										PublicMethod
												.myOutput("------edit-------lottery_type------------------"
														+ lottery_type);
										PublicMethod
												.myOutput("------edit-------ruyipackage_str------------------"
														+ ruyipackage_str);
										PublicMethod
												.myOutput("-----edit--------sessionid------------------"
														+ sessionid);
										re = jrtLot
												.packageUpdate(
														tsubscribeId,
														Integer
																.parseInt(ruyipackage_str)
																* 100 + "",
														sessionid);// login_User,
										editchanged = false;

										try {
											obj = new JSONObject(re);
											error_code = obj
													.getString("error_code");
											PublicMethod
													.myOutput("----------edit-----------"
															+ error_code);
											iretrytimes = 3;
										} catch (JSONException e) {
											e.printStackTrace();
											iretrytimes--;

										}
									}
									iretrytimes = 2;
									if (error_code.equals("000000")) {
										Message msg = new Message();
										msg.what = 0;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "修改套餐成功！！",
										// Toast.LENGTH_SHORT).show();
										// if(viewid==101){
										// if(!editchanged){
										// //textview.setText("机选每期10元套餐");
										// holderText[0] = "机选每期10元套餐";
										// }else{
										// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
										// holderText[0] =
										// "机选每期"+ruyipackage_str+"元套餐";
										// editchanged=false;
										// }
										// }
										// if(viewid==104){
										// if(!editchanged){
										// //textview.setText("机选每期10元套餐");
										// holderText[1] = "机选每期10元套餐";
										// }else{
										// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
										// holderText[1] =
										// "机选每期"+ruyipackage_str+"元套餐";
										// editchanged=false;
										// }
										// }
										// if(viewid==107){
										// if(!editchanged){
										// //textview.setText("机选每期10元套餐");
										// holderText[2] = "机选每期10元套餐";
										// }else{
										// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
										// holderText[2] =
										// "机选每期"+ruyipackage_str+"元套餐";
										// editchanged=false;
										// }
										// }
										// ShellRWSharesPreferences
										// shellRWtextedit =new
										// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
										// //双色球
										// if
										// (getString(R.string.shuangseqiu).equals(packageName))
										// {
										// shellRWtextedit.setUserLoginInfo("ssqtext",
										// "机选每期"+ruyipackage_str+"元套餐");
										// }
										// //福彩3D
										// if
										// (getString(R.string.fucai3d).equals(packageName))
										// {
										// shellRWtextedit.setUserLoginInfo("fc3dtext",
										// "机选每期"+ruyipackage_str+"元套餐");
										// }
										// //七乐彩
										// if
										// (getString(R.string.qilecai).equals(packageName))
										// {
										// shellRWtextedit.setUserLoginInfo("qlctext",
										// "机选每期"+ruyipackage_str+"元套餐");
										// // }
										// Message msg=new Message();
										// msg.what=0;
										// handler.sendMessage(msg);

										// showRuyiPackageListView();
									} else if (error_code.equals("070002")) {
										Message msg = new Message();
										msg.what = 7;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "请登录", Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("040101")) {
										Message msg = new Message();
										msg.what = 15;// 修改 套餐记录为空 陈晨2010/7/10
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "彩种不存在！", Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("00")) {
										Message msg = new Message();
										msg.what = 8;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "网络异常", Toast.LENGTH_SHORT).show();
									} else {
										Message msg = new Message();
										msg.what = 15;// 修改失败 陈晨2010/7/10
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "修改套餐失败！！",
										// Toast.LENGTH_SHORT).show();
									}
								}

							});
							t.start();
							// while(iretrytimes<3&&iretrytimes>0){
							// PublicMethod.myOutput("------edit-------lottery_type------------------"+lottery_type);
							// PublicMethod.myOutput("------edit-------ruyipackage_str_subscribe------------------"+ruyipackage_str_subscribe);
							// PublicMethod.myOutput("-----edit--------sessionid------------------"+sessionid);
							// re = jrtLot.packageUpdate(tsubscribeId,
							// Integer.parseInt(ruyipackage_str_subscribe)*100+"",
							// sessionid);//login_User,
							//					    		
							//					    		
							// try {
							// obj = new JSONObject(re);
							// error_code = obj.getString("error_code");
							// PublicMethod.myOutput("----------edit-----------"+error_code);
							// iretrytimes=3;
							// } catch (JSONException e) {
							// e.printStackTrace();
							// iretrytimes--;
							//									
							// }
							// }
							// iretrytimes=2;
							// if(error_code.equals("000000")){
							// Toast.makeText(RuyiExpressFeel.this, "修改套餐成功！！",
							// Toast.LENGTH_SHORT).show();
							// if(viewid==101){
							// if(!editchanged){
							// //textview.setText("机选每期10元套餐");
							// holderText[0] = "机选每期10元套餐";
							// }else{
							// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
							// holderText[0] = "机选每期"+ruyipackage_str+"元套餐";
							// editchanged=false;
							// }
							// }
							// if(viewid==104){
							// if(!editchanged){
							// //textview.setText("机选每期10元套餐");
							// holderText[1] = "机选每期10元套餐";
							// }else{
							// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
							// holderText[1] = "机选每期"+ruyipackage_str+"元套餐";
							// editchanged=false;
							// }
							// }
							// if(viewid==107){
							// if(!editchanged){
							// //textview.setText("机选每期10元套餐");
							// holderText[2] = "机选每期10元套餐";
							// }else{
							// //textview.setText("机选每期"+ruyipackage_str+"元套餐");
							// holderText[2] = "机选每期"+ruyipackage_str+"元套餐";
							// editchanged=false;
							// }
							// }
							// ShellRWSharesPreferences shellRWtextedit =new
							// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
							// //双色球
							// if
							// (getString(R.string.shuangseqiu).equals(packageName))
							// {
							// shellRWtextedit.setUserLoginInfo("ssqtext",
							// "机选每期"+ruyipackage_str+"元套餐");
							// }
							// //福彩3D
							// if
							// (getString(R.string.fucai3d).equals(packageName))
							// {
							// shellRWtextedit.setUserLoginInfo("fc3dtext",
							// "机选每期"+ruyipackage_str+"元套餐");
							// }
							// //七乐彩
							// if
							// (getString(R.string.qilecai).equals(packageName))
							// {
							// shellRWtextedit.setUserLoginInfo("qlctext",
							// "机选每期"+ruyipackage_str+"元套餐");
							// }
							// showRuyiPackageListView();
							// }else if (error_code.equals("070002")) {
							// Toast.makeText(RuyiExpressFeel.this, "请登录",
							// Toast.LENGTH_SHORT).show();
							// } else if (error_code.equals("040018")) {
							// Toast.makeText(RuyiExpressFeel.this, "彩种不存在！",
							// Toast.LENGTH_SHORT).show();
							// } else if(error_code.equals("350001")){
							// Toast.makeText(RuyiExpressFeel.this,
							// "套餐状态为暂停或注销，不能再次定制", Toast.LENGTH_SHORT).show();
							// }else if(error_code.equals("040003")){
							// Toast.makeText(RuyiExpressFeel.this, "期号不合法",
							// Toast.LENGTH_SHORT).show();
							// }else if(error_code.equals("350002")){
							// Toast.makeText(RuyiExpressFeel.this, "套餐已定制",
							// Toast.LENGTH_SHORT).show();
							// }else if(error_code.equals("350006")){
							// Toast.makeText(RuyiExpressFeel.this,
							// "套餐已定制，修改失败", Toast.LENGTH_SHORT).show();
							// }else if(error_code.equals("350003")){
							// Toast.makeText(RuyiExpressFeel.this,
							// "套餐定制失败，发生异常", Toast.LENGTH_SHORT).show();
							// } else if (error_code.equals("4444")) {
							// Toast.makeText(RuyiExpressFeel.this, "系统结算，请稍侯",
							// Toast.LENGTH_SHORT).show();
							// } else if(error_code.equals("00")){
							// Toast.makeText(RuyiExpressFeel.this, "网络异常",
							// Toast.LENGTH_SHORT).show();
							// }
							// else {
							// Toast.makeText(RuyiExpressFeel.this, "修改套餐失败！！",
							// Toast.LENGTH_SHORT).show();
							// }

						}

					});
			// 取消
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}

					});
			builder.show();

		}
	}

	// 王艳玲7.9修改套餐接口
	/**
	 * 如意套餐中订购按钮的执行操作
	 * 
	 * @param view
	 *            被点击的按钮
	 */
	private void showRuyiPackageSubscribe(int aViewId) {//View view
//		boolean isatus = states[(view.getId() - 100) / 3][(view.getId() - 100) % 3];
		boolean isatus = states[(aViewId - 100) / 3][(aViewId - 100) % 3];
		PublicMethod.myOutput("------" + isatus);
		if (!isatus) {
			return;
		}
		viewid = aViewId;//view.getId();

		// 暂时保存订购的套餐种类，并在下次登录的时候显示出来

		if (aViewId == 100) {
			packageName = chooseLuckLotteryNum_zhonglei[0];// 双色球
			// textview.setText(textssq);
			lottery_type = "B001";
			tsubscribeId = tsubscribeIdssq;

		}
		if (aViewId == 103) {
			packageName = chooseLuckLotteryNum_zhonglei[1];// 福彩3D
			// textview.setText(textfc3d);
			lottery_type = "D3";
			tsubscribeId = tsubscribeIdfc3d;

		}
		if (aViewId == 106) {
			packageName = chooseLuckLotteryNum_zhonglei[2];// 七乐彩
			// textview.setText(textqlc);
			lottery_type = "QL730";
			tsubscribeId = tsubscribeIdqlc;
		}
		//wangyl 8.27 add pl3 and dlt
		if (aViewId == 109) {
			packageName = chooseLuckLotteryNum_zhonglei[3];// 排列三
			lottery_type = "PL3_33";
			tsubscribeId = tsubscribeIdpl3;

		}
		if (aViewId == 112) {
			packageName = chooseLuckLotteryNum_zhonglei[4];// 大乐透
			lottery_type = "DLT_23529";
			tsubscribeId = tsubscribeIddlt;

		}

		// 周黎鸣 7.5 代码修改：添加登陆判断
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		if (sessionid.equals("")) {
			Intent intentSession = new Intent(RuyiExpressFeel.this,
					UserLogin.class);
			startActivityForResult(intentSession, 0);
			// startActivity(intentSession);
		} else {
			LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View myView = factory.inflate(R.layout.ruyipackage_dialog, null);
			radioGroup = (RadioGroup) myView
					.findViewById(R.id.ruyipackage_radiogroup);
			rb10 = (RadioButton) myView.findViewById(R.id.ruyipackage_10);
			rb8 = (RadioButton) myView.findViewById(R.id.ruyipackage_8);
			rb6 = (RadioButton) myView.findViewById(R.id.ruyipackage_6);
			rb4 = (RadioButton) myView.findViewById(R.id.ruyipackage_4);
			rb2 = (RadioButton) myView.findViewById(R.id.ruyipackage_2);
			dialog_tips = (TextView) myView.findViewById(R.id.ruyipackage_tips);
			dialog_tips.setText("您确认订购" + packageName + "10元套餐?");
			radioGroup
					.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {
							subscribechanged = true;
							if (checkedId == rb10.getId()) {
								dialog_tips.setText("您确认订购" + packageName
										+ "10元套餐?");
								ruyipackage_str_subscribe = "10";
							}
							if (checkedId == rb8.getId()) {
								dialog_tips.setText("您确认订购" + packageName
										+ "8元套餐?");
								ruyipackage_str_subscribe = "8";
							}
							if (checkedId == rb6.getId()) {
								dialog_tips.setText("您确认订购" + packageName
										+ "6元套餐?");
								ruyipackage_str_subscribe = "6";
							}
							if (checkedId == rb4.getId()) {
								dialog_tips.setText("您确认订购" + packageName
										+ "4元套餐?");
								ruyipackage_str_subscribe = "4";
							}
							if (checkedId == rb2.getId()) {
								dialog_tips.setText("您确认订购" + packageName
										+ "2元套餐?");
								ruyipackage_str_subscribe = "2";
							}
						}

					});

			if (!subscribechanged) {
				ruyipackage_str_subscribe = "10";
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(
					RuyiExpressFeel.this);
			builder.setTitle("请选择套餐总类");
			builder.setView(myView);
			// 确定
			builder.setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							showDialog(PROGRESS_VALUE);// 显示网络提示框 2010/7/10陈晨
							// 改为线程
							Thread t = new Thread(new Runnable() {

								@Override
								public void run() {
									while (iretrytimes < 3 && iretrytimes > 0) {

										ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
												RuyiExpressFeel.this, "addInfo");
										sessionid = shellRW.getUserLoginInfo("sessionid");
										phonenum = shellRW.getUserLoginInfo("phonenum");
										PublicMethod
												.myOutput("------sub-------lottery_type------------------"
														+ lottery_type);
										PublicMethod
												.myOutput("------sub-------ruyipackage_str_subscribe------------------"
														+ ruyipackage_str_subscribe);
										PublicMethod
												.myOutput("------sub-------sessionid------------------"
														+ sessionid);
										//wangyl 8.30 体彩与福彩订购接口不同
										if(lottery_type.equals("B001")||lottery_type.equals("D3")||lottery_type.equals("QL730")){
											
											re = jrtLot.packageDeal(lottery_type,Integer.parseInt(ruyipackage_str_subscribe)* 100 + "",sessionid);// login_User,
										
										}else if(lottery_type.equals("PL3_33")){
											
											re = jrtLot.packageDealTC(phonenum, "T01002", Integer.parseInt(ruyipackage_str_subscribe)* 100 + "", sessionid);
										
										}else if(lottery_type.equals("DLT_23529")){
											
											re = jrtLot.packageDealTC(phonenum, "T01001", Integer.parseInt(ruyipackage_str_subscribe)* 100 + "", sessionid);
										
										}
										
										
										subscribechanged = false;
										try {
											obj = new JSONObject(re);
											error_code = obj
													.getString("error_code");
											PublicMethod
													.myOutput("----------sub-----------"
															+ error_code);
											iretrytimes = 3;
										} catch (JSONException e) {
											e.printStackTrace();
											iretrytimes--;
										}
									}
//									加入是否改变切入点判断 陈晨 8.11
									if (iretrytimes == 0 && iHttp.whetherChange == false) {
										iHttp.whetherChange = true;
										if (iHttp.conMethord == iHttp.CMWAP) {
											iHttp.conMethord = iHttp.CMNET;
										} else {
											iHttp.conMethord = iHttp.CMWAP;
										}
										iretrytimes=2;
										PublicMethod.myOutput("=====qierudian=dinggou====");
										while(iretrytimes>0&&iretrytimes<3){
											ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
													RuyiExpressFeel.this, "addInfo");
											sessionid = shellRW
													.getUserLoginInfo("sessionid");
	
											PublicMethod
													.myOutput("------sub-------lottery_type------------------"
															+ lottery_type);
											PublicMethod
													.myOutput("------sub-------ruyipackage_str_subscribe------------------"
															+ ruyipackage_str_subscribe);
											PublicMethod
													.myOutput("------sub-------sessionid------------------"
															+ sessionid);
											//wangyl 8.30 体彩与福彩订购接口不同
											if(lottery_type.equals("B001")||lottery_type.equals("D3")||lottery_type.equals("QL730")){
												
												re = jrtLot.packageDeal(lottery_type,Integer.parseInt(ruyipackage_str_subscribe)* 100 + "",sessionid);// login_User,
											
											}else if(lottery_type.equals("PL3_33")){
												
												re = jrtLot.packageDealTC(phonenum, "T01002", Integer.parseInt(ruyipackage_str_subscribe)* 100 + "", sessionid);
											
											}else if(lottery_type.equals("DLT_23529")){
												
												re = jrtLot.packageDealTC(phonenum, "T01001", Integer.parseInt(ruyipackage_str_subscribe)* 100 + "", sessionid);
											
											}
	
											try {
												obj = new JSONObject(re);
												error_code = obj
														.getString("error_code");
												PublicMethod
														.myOutput("----------sub-----------"
																+ error_code);
												iretrytimes=3;
											}catch (JSONException e1) {
												iretrytimes--;
												e1.printStackTrace();
											}
										}
									}
									iretrytimes = 2;
									//wangyl 8.30 体彩与福彩订购接口不同
									if(lottery_type.equals("B001")||lottery_type.equals("D3")||lottery_type.equals("QL730")){
									if (error_code.equals("000000")) {
										// Toast.makeText(RuyiExpressFeel.this,
										// "购买套餐成功！！",
										// Toast.LENGTH_SHORT).show();
										Message msg = new Message();
										msg.what = 0;
										handler.sendMessage(msg);
										/*
										 * //Flag=true;//应该修改该套餐的订购状态 //false 订购
										 * if(viewid==100){
										 * subscribeFlag[0]=false;
										 * if(!subscribechanged){
										 * //textview.setText("机选每期10元套餐");
										 * holderText[0] ="机选每期10元套餐"; }else{
										 * //PublicMethod
										 * .myOutput("-------textview---------"
										 * +textview.getText().toString());
										 * //PublicMethod.myOutput(
										 * "-------ruyipackage_str_subscribe---------"
										 * +ruyipackage_str_subscribe); //
										 * textview
										 * .setText("机选每期"+ruyipackage_str_subscribe
										 * +"元套餐");holderText[0]="机选每期"+
										 * ruyipackage_str_subscribe+"元套餐";
										 * subscribechanged=false; } }
										 * if(viewid==103){
										 * subscribeFlag[1]=false;
										 * if(!subscribechanged){
										 * //textview.setText("机选每期10元套餐");
										 * holderText[1] ="机选每期10元套餐"; }else{
										 * //PublicMethod
										 * .myOutput("-------textview---------"
										 * +textview.getText().toString());
										 * //PublicMethod.myOutput(
										 * "-------ruyipackage_str_subscribe---------"
										 * +ruyipackage_str_subscribe); //
										 * textview
										 * .setText("机选每期"+ruyipackage_str_subscribe
										 * +"元套餐");holderText[1]="机选每期"+
										 * ruyipackage_str_subscribe+"元套餐";
										 * subscribechanged=false; } }
										 * if(viewid==106){
										 * subscribeFlag[2]=false;
										 * if(!subscribechanged){
										 * //textview.setText("机选每期10元套餐");
										 * holderText[2] ="机选每期10元套餐"; }else{
										 * //PublicMethod
										 * .myOutput("-------textview---------"
										 * +textview.getText().toString());
										 * //PublicMethod.myOutput(
										 * "-------ruyipackage_str_subscribe---------"
										 * +ruyipackage_str_subscribe); //
										 * textview
										 * .setText("机选每期"+ruyipackage_str_subscribe
										 * +"元套餐");holderText[2]="机选每期"+
										 * ruyipackage_str_subscribe+"元套餐";
										 * subscribechanged=false; } }
										 * //setFlag();PublicMethod.myOutput(
										 * "----sub-----subscribeFlag[0]----------"
										 * +subscribeFlag[0]);
										 * PublicMethod.myOutput
										 * ("-----sub----subscribeFlag[1]----------"
										 * +subscribeFlag[1]);
										 * PublicMethod.myOutput
										 * ("-----sub----subscribeFlag[2]----------"
										 * +subscribeFlag[2]);
										 */
										// showRuyiPackageListView();
										// Message msg=new Message();
										// msg.what=0;
										// handler.sendMessage(msg);

										/*
										 * if(!subscribechanged){
										 * //textview.setText("机选每期10元套餐");
										 * holderText ="机选每期10元套餐"; }else{
										 * //PublicMethod
										 * .myOutput("-------textview---------"
										 * +textview.getText().toString());
										 * //PublicMethod.myOutput(
										 * "-------ruyipackage_str_subscribe---------"
										 * +ruyipackage_str_subscribe); //
										 * textview
										 * .setText("机选每期"+ruyipackage_str_subscribe
										 * +"元套餐");holderText="机选每期"+
										 * ruyipackage_str_subscribe+"元套餐";
										 * subscribechanged=false; }
										 */

										// shellRWtextsub用于暂存订购的套餐种类，便于下次登录时可见
										// ShellRWSharesPreferences
										// shellRWtextsub =new
										// ShellRWSharesPreferences(RuyiExpressFeel.this,"addInfo");
										// //双色球
										// // 清空preferrence
										// shellRWtextsub.setUserLoginInfo("ssqtext",
										// "");
										// shellRWtextsub.setUserLoginInfo("fc3dtext",
										// "");
										// shellRWtextsub.setUserLoginInfo("qlctext",
										// "");
										//						
										// if
										// (getString(R.string.shuangseqiu).equals(packageName))
										// {
										// shellRWtextsub.setUserLoginInfo("ssqtext",
										// "机选每期"+ruyipackage_str_subscribe+"元套餐");
										//							
										// PublicMethod.myOutput("---sub----ssqtext---------"+shellRWtextsub.getUserLoginInfo("ssqtext"));
										// }
										// //福彩3D
										// if
										// (getString(R.string.fucai3d).equals(packageName))
										// {
										// shellRWtextsub.setUserLoginInfo("fc3dtext",
										// "机选每期"+ruyipackage_str_subscribe+"元套餐");
										// PublicMethod.myOutput("---sub----fc3dtext---------"+shellRWtextsub.getUserLoginInfo("fc3dtext"));
										// }
										// //七乐彩
										// if
										// (getString(R.string.qilecai).equals(packageName))
										// {
										// shellRWtextsub.setUserLoginInfo("qlctext",
										// "机选每期"+ruyipackage_str_subscribe+"元套餐");
										// PublicMethod.myOutput("---sub----qlctext---------"+shellRWtextsub.getUserLoginInfo("qlctext"));
										// }
										// 加入返回码的处理 2010/7/10陈晨

									} else if (error_code.equals("070002")) {
										Message msg = new Message();
										msg.what = 7;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "请登录", Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("350002")) {
										Message msg = new Message();
										msg.what = 10;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "套餐已定制", Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("040006")) {
										Message msg = new Message();
										msg.what = 11;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "套餐已定制，修改失败",
										// Toast.LENGTH_SHORT).show();
									} else if (error_code.equals("00")) {
										Message msg = new Message();
										msg.what = 8;
										handler.sendMessage(msg);
									}
									// Toast.makeText(RuyiExpressFeel.this,
									// "网络异常", Toast.LENGTH_SHORT).show();
									else {
										Message msg = new Message();
										msg.what = 12;
										handler.sendMessage(msg);
										// Toast.makeText(RuyiExpressFeel.this,
										// "购买套餐失败！！",
										// Toast.LENGTH_SHORT).show();
									}
									}else if(lottery_type.equals("PL3_33") ||lottery_type.equals("DLT_23529") ){
										//套餐定制成功
										if (error_code.equals("350002")) {
											Message msg = new Message();
											msg.what = 0;
											handler.sendMessage(msg);
										}
										//登录失败
										if (error_code.equals("07002")) {
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										}
										//期号不合法
										if (error_code.equals("040003")) {
											Message msg = new Message();
											msg.what = 20;
											handler.sendMessage(msg);
										}
										//套餐定制失败
										if (error_code.equals("350003")) {
											Message msg = new Message();
											msg.what = 12;
											handler.sendMessage(msg);
										}
										//账户异常
										if (error_code.equals("040002")) {
											Message msg = new Message();
											msg.what = 21;
											handler.sendMessage(msg);
										}
									
									}
								}

							});
							t.start();
						}

					});
			// 取消
			builder.setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
						}

					});
			builder.show();
		}

	}

	/**
	 * 幸运选号的主列表
	 */
	private void showCLLNMainListView() {

		setContentView(R.layout.choose_luck_lottery_num_main);

		ImageView tvreturn = (ImageView) findViewById(R.id.tv_choose_luck_lottery_num_return);
		tvreturn.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				showListView(ID_MAINRUYIEXPRESSFEEL);
			}
		});

		ListView listview = (ListView) findViewById(R.id.choose_luck_lottery_num_listview_id);

		ChooseLuckLotteryNum_EfficientAdapter adapter = new ChooseLuckLotteryNum_EfficientAdapter(
				this);
		listview.setAdapter(adapter);

		// 显示星座的图标

	}

	/**
	 * 彩种分类                                         zlm 8.11 代码修改
	 * @param v
	 */
	public void gameClassify(View v){
		if (Math.floor(v.getId() / 4) == 25) {
			type02 = "ssq";
		} else if (Math.floor(v.getId() / 4) == 26) {
			type02 = "fc3d";
		} else if (Math.floor(v.getId() / 4) == 27) {
			type02 = "qlc";
		} else if (Math.floor(v.getId() / 4) == 28) {    //排列三
			type02 = "pl3";
		} else if (Math.floor(v.getId() / 4) == 29) {    //超级大乐透
			type02 = "cjdlt";
		}
	}


	/*---------网格显示------------*/
	// 周黎鸣 7.7 代码修改：修改了showGridView()方法
	/**
	 * 网格显示---显示星座和生肖
	 * 
	 * @param v
	 *            视图
	 * @param gridIcon
	 *            星座/生肖的图片
	 * @param gridIconName
	 *            星座/生肖的名称
	 */
	private void showGridView(View v, Integer[] gridIcon, String[] gridIconName) {

		gameClassify(v);        //彩票种类分类

		gridImage = gridIcon;
		gridText = gridIconName;

		View popupView = this.getLayoutInflater().inflate(
				R.layout.choose_luck_lottery_num_main_grid, null);
		popupwindow = new PopupWindow(popupView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT, true);

		GridView gridview = (GridView) popupView
				.findViewById(R.id.chooose_luck_lottery_num_gridview_id);
		// 周黎鸣7.3代码修改：将“返回”Button换成ImageButton
		ImageButton button = (ImageButton) popupView
				.findViewById(R.id.chooose_luck_lottery_num_return);
		button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupwindow.dismiss();
			}

		});

		// 周黎鸣 7.8 代码修改：添加新的Grid适配器
		ChooseLuckLotteryNum_GridAdapter gridAdapter = new ChooseLuckLotteryNum_GridAdapter(
				this);
		gridview.setAdapter(gridAdapter);

		// 周黎鸣 7.7 代码修改
		if(type02.equalsIgnoreCase("ssq") || type02.equalsIgnoreCase("fc3d") || type02.equalsIgnoreCase("qlc")){
			popupwindow.showAsDropDown(v);
		} else if(type02.equalsIgnoreCase("pl3") || type02.equalsIgnoreCase("cjdlt")){
			popupwindow.showAtLocation(findViewById(R.id.choose_luck_lottery_num_listview_id) , Gravity.CENTER, 0, 0);
		}
	}

	/**
	 * 姓名选号
	 * 
	 * @param v
	 *            视图
	 */
	private void showXingMingGialog(View v) {
		gameClassify(v);        //彩票种类分类
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View textView = inflater.inflate(
				R.layout.choose_luck_lottery_num_xingming_dialog_layout, null);
		editTextXingming = (EditText) textView
				.findViewById(R.id.clln_xingming_edit_dialog_id);
		builderXingming = new AlertDialog.Builder(this);
		builderXingming.setCancelable(true);
		builderXingming.setTitle(R.string.qingshuruxingming);
		builderXingming.setView(textView);

		builderXingming.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						if (editTextXingming.length() == 0) {
							showAttentionImportNameDialog(type02);

						} else {
							showXingYunXuanHaoListView(ID_CLLN_SHOWBALLMONRY,
									type02);
						}
					}

				});
		builderXingming.setNegativeButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
					}

				});
		builderXingming.show();
	}

	// 周黎鸣7.3代码修改：将代码全换了，使用自带的日期显示器
	/*---------生日选号------------*/
	// 周黎鸣 7.7 代码修改：调整接收信息
	/**
	 * 生日选号
	 * 
	 * @param v
	 *            视图
	 */
	private void showShengRiDialog(View v) {
		gameClassify(v);        //彩票种类分类
		
		LayoutInflater inflater = LayoutInflater.from(this);
		View textView = inflater.inflate(
				R.layout.choose_luck_num_shengri_date_picker, null);
		DatePicker dp = (DatePicker) textView
				.findViewById(R.id.choose_luck_num_date_picker_id);
		Calendar calendar = Calendar.getInstance();
		int calYear = calendar.get(Calendar.YEAR);
		int calMonth = calendar.get(Calendar.MONTH);
		int calDay = calendar.get(Calendar.DAY_OF_MONTH);
		dp.init(calYear, calMonth, calDay, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle(R.string.qingshurushengri);
		builder.setView(textView);

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						showXingYunXuanHaoListView(ID_CLLN_SHOWBALLMONRY,
								type02);
					}

				});
		builder.setNegativeButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
					}

				});
		builder.show();
	}

	// 周黎鸣 7.8 代码修改：添加GridView适配器
	// GridView适配器 7.7 代码修改
	/**
	 * GridView适配器
	 */
	public class ChooseLuckLotteryNum_GridAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		Context context;

		public ChooseLuckLotteryNum_GridAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mXingzuoIcon.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.choose_luck_lottery_num_main_grid_specific,
						null);
				holder = new ViewHolder();
				// 设置按钮
				holder.iButtonGroupLayout = (LinearLayout) convertView
						.findViewById(R.id.choose_luck_num_gridview_layout_button_group);

				LinearLayout iImageButton = new LinearLayout(context);
				iImageButton.setOrientation(LinearLayout.VERTICAL);

				ImageView iImage = new ImageView(context);
				iImage.setImageResource(gridImage[position]);
				iImageButton.addView(iImage);

				TextView iText = new TextView(context);
				iText.setText(gridText[position]);
				iText.setGravity(Gravity.CENTER);
				iText.setTextColor(Color.BLACK);
				iImageButton.addView(iText);

				iImageButton.setId(position);
				iImageButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						popupwindow.dismiss();
						showXingYunXuanHaoListView(ID_CLLN_SHOWBALLMONRY,
								type02);
					}
				});

				holder.iButtonGroupLayout.addView(iImageButton);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			LinearLayout iButtonGroupLayout;
		}
	}

	/**
	 * 幸运选号的适配器
	 */
	public class ChooseLuckLotteryNum_EfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		Context context;
		int id;

		/*
		 * private Bitmap mShuangSeQiu; private Bitmap mFuCai; private Bitmap
		 * mQiLeCai;
		 */

		public ChooseLuckLotteryNum_EfficientAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
			/*
			 * mShuangSeQiu =
			 * BitmapFactory.decodeResource(context.getResources(),
			 * R.drawable.shuangseqiu); mFuCai =
			 * BitmapFactory.decodeResource(context.getResources(),
			 * R.drawable.fucai); mQiLeCai =
			 * BitmapFactory.decodeResource(context.getResources(),
			 * R.drawable.qilecai);
			 */
		}

		@Override
		public int getCount() {
			return chooseLuckLotteryNum_title.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		// 设置主列表布局中的详细内容
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			// 与布局中的信息关联起来
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.choose_luck_lottery_num_listview_layout_two,
						null);
				holder = new ViewHolder();
				holder.icon = (ImageView) convertView
						.findViewById(R.id.choose_luck_lottery_num_icon_id);
				holder.icon.setImageResource(mIcon[position]);
				holder.chooseLuckLotteryNum_zhonglei_View = (TextView) convertView
						.findViewById(R.id.choose_luck_lottery_num_zhonglei);
				holder.chooseLuckLotteryNum_zhonglei_View
						.setText(chooseLuckLotteryNum_title[position]);

				// 设置按钮
				holder.iButtonGroupLayout = (LinearLayout) convertView
						.findViewById(R.id.choose_luck_num_listview_layout_button_group);
				holder.iButtonGroupLayout.setId(position + LAYOUT_INDEX);

				int i;
				for (i = 0; i < 4; i++) {
					LinearLayout iImageButton = new LinearLayout(context);
					iImageButton.setOrientation(LinearLayout.VERTICAL);
					iImageButton.setPadding(5, 0, 0, 0);
					ImageView iImage = new ImageView(context);
					iImage.setImageResource(imageId[i]);
					iImageButton.addView(iImage);

					TextView iText = new TextView(context);
					iText.setText(textContent[i]);
					iText.setGravity(Gravity.CENTER);
					iText.setTextColor(Color.BLACK);
					iImageButton.addView(iText);

					iImageButton.setId(i + 4 * position + 100);
					if (i == 0) {
						iImageButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// id = arg0.getId() - LAYOUT_INDEX;
								showGridView(arg0, mXingzuoIcon, xingzuoName);
							}
						});
					} else if (i == 1) {
						iImageButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// id = arg0.getId() - LAYOUT_INDEX;
								showGridView(arg0, mShengxiaoIcon,
										shengxiaoName);
							}
						});
					} else if (i == 2) {
						iImageButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// id = arg0.getId() - LAYOUT_INDEX;
								showXingMingGialog(arg0);
							}
						});
					} else if (i == 3) {
						iImageButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								// id = arg0.getId() - LAYOUT_INDEX;
								showShengRiDialog(arg0);
							}
						});
					}

					holder.iButtonGroupLayout.addView(iImageButton);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			/*
			 * holder.chooseLuckLotteryNum_xingzuo_View.setText(chooseLuckLotteryNum_xingzuo
			 * [position]);holder.chooseLuckLotteryNum_shengxiao_View.setText(
			 * chooseLuckLotteryNum_shengxiao[position]);
			 * holder.chooseLuckLotteryNum_xingming_View
			 * .setText(chooseLuckLotteryNum_xingming[position]);
			 * holder.chooseLuckLotteryNum_shengri_View
			 * .setText(chooseLuckLotteryNum_shengri[position]);
			 */

			return convertView;
		}

		class ViewHolder {
			ImageView icon;
			TextView chooseLuckLotteryNum_zhonglei_View;
			LinearLayout iButtonGroupLayout;
		}

	}

	/**
	 * 如意传情主布局
	 */
	private void showRuYiChuanQing() {
		setContentView(R.layout.ruyichuanqing_layout_main);
		// 周黎鸣7.3代码修改：将Button换成ImageButton
		ImageView btn = (ImageView) findViewById(R.id.ruyichuanqing_return);
		btn.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				showListView(ID_MAINRUYIEXPRESSFEEL);
			}

		});
		ruyichuanqing_sub_view = (LinearLayout) findViewById(R.id.ruyichuanqing_layout_sub);

		ruyichuanqing_ssq = (TextView) findViewById(R.id.tv_ruyichuanqing_ssq);
		ruyichuanqing_fc3d = (TextView) findViewById(R.id.tv_ruyichuanqing_fc3d);
		ruyichuanqing_qlc = (TextView) findViewById(R.id.tv_ruyichuanqing_qlc);
		//wangyl 8.25 新增大乐透和排列三
		ruyichuanqing_dlt = (TextView) findViewById(R.id.tv_ruyichuanqing_dlt);
		ruyichuanqing_pl3 = (TextView) findViewById(R.id.tv_ruyichuanqing_pl3);
		
		textViews[0]=ruyichuanqing_ssq;
		textViews[1]=ruyichuanqing_fc3d;
		textViews[2]=ruyichuanqing_qlc;
		textViews[3]=ruyichuanqing_dlt;
		textViews[4]=ruyichuanqing_pl3;
		ruyichuanqing_ssq.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ints={1,0,0,0,0};
				setRuyichuanqingTabs(textViews,ints);
				setRuyichuanqingViews("ssq");
			}

		});

		ruyichuanqing_fc3d.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ints={0,1,0,0,0};
				setRuyichuanqingTabs(textViews,ints);
				setRuyichuanqingViews("fc3d");
			}

		});

		ruyichuanqing_qlc.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ints={0,0,1,0,0};
				setRuyichuanqingTabs(textViews,ints);
				setRuyichuanqingViews("qlc");
			}

		});
		ruyichuanqing_dlt.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ints={0,0,0,1,0};
				setRuyichuanqingTabs(textViews,ints);
				setRuyichuanqingViews("dlt");
			}

		});
		ruyichuanqing_pl3.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] ints={0,0,0,0,1};
				setRuyichuanqingTabs(textViews,ints);
				setRuyichuanqingViews("pl3");
			}

		});
		int[] ints={1,0,0,0,0};
		setRuyichuanqingTabs(textViews,ints);
		setRuyichuanqingViews("ssq");

	}
	//wangyl 8.11 代码优化 如意传情标签
	/**
	 * 根据状态设置TextView的背景图片
	 * @param textviews  标签组
	 * @param ints  标签点击状态，1为被点击，0为未点击
	 */
	private void setRuyichuanqingTabs(TextView[] textviews,int[] ints){
		for(int i=0;i<ints.length;i++){
			if(ints[i]==1){
				textviews[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_rectangle_user));
			}else if(ints[i]==0){
				textviews[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_rectangle_user_d));
			}
		}
	}
	
	//wangyl 8.11 代码优化 如意传情标签
	/**
	 * 显示各个彩种的view
	 * @param lotteryType  彩种
	 */
	private void setRuyichuanqingViews(String lotteryType){
		ruyichuanqing_sub_view.removeAllViews();
		RuyiExpressFeelView ruyiExpressFeelView = new RuyiExpressFeelView(this,lotteryType);
		View view = ruyiExpressFeelView.getView();
		ruyichuanqing_sub_view.addView(view);
	}
	
	//wangyl 8.25 以下注释可以删掉
	//----------------------------------------------------------------------------------------------
	/**
	 * 如意传情主布局
	 *//*
	private void showRuYiChuanQing() {
		setContentView(R.layout.ruyichuanqing_layout_main);
		// 周黎鸣7.3代码修改：将Button换成ImageButton
		ImageView btn = (ImageView) findViewById(R.id.ruyichuanqing_return);
		btn.setOnClickListener(new ImageView.OnClickListener() {

			@Override
			public void onClick(View v) {
				showListView(ID_MAINRUYIEXPRESSFEEL);
			}

		});
		ruyichuanqing_sub_view = (LinearLayout) findViewById(R.id.ruyichuanqing_layout_sub);

		ruyichuanqing_ssq = (TextView) findViewById(R.id.tv_ruyichuanqing_ssq);
		ruyichuanqing_fc3d = (TextView) findViewById(R.id.tv_ruyichuanqing_fc3d);
		ruyichuanqing_qlc = (TextView) findViewById(R.id.tv_ruyichuanqing_qlc);

		ruyichuanqing_ssq.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 * ruyichuanqing_ssq.setTextColor(Color.RED);
				 * ruyichuanqing_fc3d.setTextColor(Color.BLACK);
				 * ruyichuanqing_qlc.setTextColor(Color.BLACK);
				 
				// 王艳玲 7.12 如意传情标签不明显
				ruyichuanqing_ssq.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user));
				ruyichuanqing_fc3d.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				ruyichuanqing_qlc.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				showListView(ID_RUYICHUANQING_SSQ);
			}

		});

		ruyichuanqing_fc3d.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 * ruyichuanqing_ssq.setTextColor(Color.BLACK);
				 * ruyichuanqing_fc3d.setTextColor(Color.RED);
				 * ruyichuanqing_qlc.setTextColor(Color.BLACK);
				 
				// 王艳玲 7.12 如意传情标签不明显
				ruyichuanqing_ssq.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				ruyichuanqing_fc3d.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user));
				ruyichuanqing_qlc.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				showListView(ID_RUYICHUANQING_FC3D);
			}

		});

		ruyichuanqing_qlc.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				 * ruyichuanqing_ssq.setTextColor(Color.BLACK);
				 * ruyichuanqing_fc3d.setTextColor(Color.BLACK);
				 * ruyichuanqing_qlc.setTextColor(Color.RED);
				 
				// 王艳玲 7.12 如意传情标签不明显
				ruyichuanqing_ssq.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				ruyichuanqing_fc3d.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user_d));
				ruyichuanqing_qlc.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.frame_rectangle_user));
				showListView(ID_RUYICHUANQING_QLC);
			}

		});
		
		 * ruyichuanqing_ssq.setTextColor(Color.RED);
		 * ruyichuanqing_fc3d.setTextColor(Color.BLACK);
		 * ruyichuanqing_qlc.setTextColor(Color.BLACK);
		 
		// 王艳玲 7.12 如意传情标签不明显
		ruyichuanqing_ssq.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.frame_rectangle_user));
		ruyichuanqing_fc3d.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.frame_rectangle_user_d));
		ruyichuanqing_qlc.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.frame_rectangle_user_d));
		showListView(ID_RUYICHUANQING_SSQ);

	}

	*//**
	 * 如意传情双色球布局
	 *//*
	private void showRuyichuanqingSSQ() {

		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_qlc_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_qlc_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_fc3d_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_fc3d_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_ssq_layout_id) == null) {

			isJixuanOrZixuan_ssq = 1;
			ischeck_ssq_jixuan = false;// FALSE 选中 true没有选中
			ischeck_ssq_zixuan = true;

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout ssqlayout = (LinearLayout) inflate.inflate(
					R.layout.ruyichuanqing_ssq_layout_main, null);
			{
				relativeLayout_ssq = (RelativeLayout) ssqlayout
						.findViewById(R.id.ruyichuanqing_layout_ssq_zhushu);
				cb_ssq_jixuan = (CheckBox) ssqlayout
						.findViewById(R.id.ruyichuanqing_ssq_jixuan);
				cb_ssq_zixuan = (CheckBox) ssqlayout
						.findViewById(R.id.ruyichuanqing_ssq_zixuan);
				spinner_ssq = (Spinner) ssqlayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				btn_sure_ssq = (Button) ssqlayout
						.findViewById(R.id.ruyichuanqing_ssq_sure);
				cb_ssq_jixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									ischeck_ssq_jixuan = false;
									isJixuanOrZixuan_ssq = 1;
									cb_ssq_zixuan.setChecked(false);
									relativeLayout_ssq
											.setVisibility(View.VISIBLE);
								} else {
									ischeck_ssq_jixuan = true;
									relativeLayout_ssq
											.setVisibility(View.INVISIBLE);
								}
							}

						});
				cb_ssq_zixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									ischeck_ssq_zixuan = false;
									isJixuanOrZixuan_ssq = 2;
									cb_ssq_jixuan.setChecked(false);
									relativeLayout_ssq
											.setVisibility(View.INVISIBLE);
								} else {
									ischeck_ssq_zixuan = true;
								}
							}

						});

				spinner_ssq = (Spinner) ssqlayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				spinner_ssq
						.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								PublicMethod
										.myOutput("----------spinner_str------------"
												+ constrs[position]);
								spinner_str_ssq = constrs[position];// 用于传值,暂不用

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}

						});
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, constrs);
				adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_ssq.setAdapter(adapter);
				btn_sure_ssq.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 机选
						if (isJixuanOrZixuan_ssq == 1 && !ischeck_ssq_jixuan) {
							Intent intent = new Intent(RuyiExpressFeel.this,
									RuyiExpressFeelSuccess.class);
							Bundle Bundle = new Bundle();
							Bundle.putString("success", "ssqJixuan");
							Bundle.putString("ssqzhushu", spinner_str_ssq);
							intent.putExtras(Bundle);
							startActivity(intent);
						}
						// 自选
						else if (isJixuanOrZixuan_ssq == 2
								&& !ischeck_ssq_zixuan) {
							Intent intent_zixuan = new Intent(
									RuyiExpressFeel.this,
									RuyiExpressFeelSsqZixuan.class);
							startActivity(intent_zixuan);
						} else if (ischeck_ssq_jixuan && ischeck_ssq_zixuan) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									RuyiExpressFeel.this);
							builder.setTitle("请选择赠送方式");
							builder.setMessage("请选择赠送方式");
							// 确定
							builder.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}

									});
							builder.show();

						}
					}

				});

			}
			ruyichuanqing_sub_view.addView(ssqlayout);

		}
	}

	*//**
	 * 如意传情福彩3D布局
	 *//*
	private void showRuyichuanqingFC3D() {

		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_ssq_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_ssq_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_qlc_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_qlc_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_fc3d_layout_id) == null) {

			isJixuanOrZixuan_fc3d = 1;
			ischeck_fc3d_jixuan = false;// false 选中 true 没有选中
			ischeck_fc3d_zixuan = true;

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout fc3dlayout = (LinearLayout) inflate.inflate(
					R.layout.ruyichuanqing_fc3d_layout_main, null);
			{
				relativeLayout_fc3d = (RelativeLayout) fc3dlayout
						.findViewById(R.id.ruyichuanqing_layout_fc3d_zhushu);
				cb_fc3d_jixuan = (CheckBox) fc3dlayout
						.findViewById(R.id.ruyichuanqing_fc3d_jixuan);
				cb_fc3d_zixuan = (CheckBox) fc3dlayout
						.findViewById(R.id.ruyichuanqing_fc3d_zixuan);
				spinner_fc3d = (Spinner) fc3dlayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				btn_sure_fc3d = (Button) fc3dlayout
						.findViewById(R.id.ruyichuanqing_fc3d_sure);
				cb_fc3d_jixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									isJixuanOrZixuan_fc3d = 1;
									ischeck_fc3d_jixuan = false;
									cb_fc3d_zixuan.setChecked(false);
									relativeLayout_fc3d
											.setVisibility(View.VISIBLE);
								} else {
									ischeck_fc3d_jixuan = true;
									relativeLayout_fc3d
											.setVisibility(View.INVISIBLE);
								}
							}

						});
				cb_fc3d_zixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									isJixuanOrZixuan_fc3d = 2;
									ischeck_fc3d_zixuan = false;
									cb_fc3d_jixuan.setChecked(false);
									relativeLayout_fc3d
											.setVisibility(View.INVISIBLE);
								} else {
									ischeck_fc3d_zixuan = true;
								}
							}

						});

				spinner_fc3d = (Spinner) fc3dlayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				spinner_fc3d
						.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								PublicMethod
										.myOutput("----------spinner_str------------"
												+ constrs[position]);
								spinner_str_fc3d = constrs[position];// 用于传值,暂不用

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}

						});
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, constrs);
				adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_fc3d.setAdapter(adapter);
				btn_sure_fc3d.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 机选
						if (isJixuanOrZixuan_fc3d == 1 && !ischeck_fc3d_jixuan) {
							Intent intent = new Intent(RuyiExpressFeel.this,
									RuyiExpressFeelSuccess.class);
							Bundle Bundle = new Bundle();
							Bundle.putString("success", "fc3dJixuan");
							Bundle.putString("fc3dzhushu", spinner_str_fc3d);
							intent.putExtras(Bundle);
							startActivity(intent);
						}
						// 自选
						else if (isJixuanOrZixuan_fc3d == 2
								&& !ischeck_fc3d_zixuan) {
							Intent intent_zixuan = new Intent(
									RuyiExpressFeel.this,
									RuyiExpressFeelFc3dZixuan.class);
							startActivity(intent_zixuan);
						} else if (ischeck_fc3d_jixuan && ischeck_fc3d_zixuan) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									RuyiExpressFeel.this);
							builder.setTitle("请选择赠送方式");
							builder.setMessage("请选择赠送方式");
							// 确定
							builder.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}

									});
							builder.show();

						}
					}

				});

			}
			ruyichuanqing_sub_view.addView(fc3dlayout);

		}
	}

	private void showRuyichuanqingQLC() {

		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_ssq_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_ssq_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_fc3d_layout_id) != null) {
			ruyichuanqing_sub_view.removeView(ruyichuanqing_sub_view
					.findViewById(R.id.ruyichuanqing_fc3d_layout_id));
		}
		if (ruyichuanqing_sub_view
				.findViewById(R.id.ruyichuanqing_qlc_layout_id) == null) {

			isJixuanOrZixuan_qlc = 1;
			ischeck_qlc_jixuan = false;// false 选中
			ischeck_qlc_zixuan = true;

			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout qlclayout = (LinearLayout) inflate.inflate(
					R.layout.ruyichuanqing_qlc_layout_main, null);
			{
				relativeLayout_qlc = (RelativeLayout) qlclayout
						.findViewById(R.id.ruyichuanqing_layout_qlc_zhushu);
				cb_qlc_jixuan = (CheckBox) qlclayout
						.findViewById(R.id.ruyichuanqing_qlc_jixuan);
				cb_qlc_zixuan = (CheckBox) qlclayout
						.findViewById(R.id.ruyichuanqing_qlc_zixuan);
				spinner_qlc = (Spinner) qlclayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				btn_sure_qlc = (Button) qlclayout
						.findViewById(R.id.ruyichuanqing_qlc_sure);
				cb_qlc_jixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									isJixuanOrZixuan_qlc = 1;
									ischeck_qlc_jixuan = false;
									cb_qlc_zixuan.setChecked(false);
									relativeLayout_qlc
											.setVisibility(View.VISIBLE);
								} else {
									ischeck_qlc_jixuan = true;
									relativeLayout_qlc
											.setVisibility(View.INVISIBLE);
								}
							}

						});
				cb_qlc_zixuan
						.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								if (isChecked) {
									isJixuanOrZixuan_qlc = 2;
									ischeck_qlc_zixuan = false;
									cb_qlc_jixuan.setChecked(false);
									relativeLayout_qlc
											.setVisibility(View.INVISIBLE);
								} else {
									ischeck_qlc_zixuan = true;
								}
							}

						});

				spinner_qlc = (Spinner) qlclayout
						.findViewById(R.id.ruyichuanqing_spinner_zhushu);
				spinner_qlc
						.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent,
									View view, int position, long id) {
								PublicMethod
										.myOutput("----------spinner_str------------"
												+ constrs[position]);
								spinner_str_qlc = constrs[position];// 用于传值,暂不用

							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {

							}

						});
				adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_item, constrs);
				adapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_qlc.setAdapter(adapter);
				btn_sure_qlc.setOnClickListener(new Button.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 机选
						if (isJixuanOrZixuan_qlc == 1 && !ischeck_qlc_jixuan) {
							Intent intent = new Intent(RuyiExpressFeel.this,
									RuyiExpressFeelSuccess.class);
							Bundle Bundle = new Bundle();
							Bundle.putString("success", "qlcJixuan");
							Bundle.putString("qlczhushu", spinner_str_qlc);
							intent.putExtras(Bundle);
							startActivity(intent);
						}
						// 自选
						else if (isJixuanOrZixuan_qlc == 2
								&& !ischeck_qlc_zixuan) {
							Intent intent_zixuan = new Intent(
									RuyiExpressFeel.this,
									RuyiExpressFeelQlcZixuan.class);
							startActivity(intent_zixuan);
						} else if (ischeck_qlc_jixuan && ischeck_qlc_zixuan) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									RuyiExpressFeel.this);
							builder.setTitle("请选择赠送方式");
							builder.setMessage("请选择赠送方式");
							// 确定
							builder.setPositiveButton(R.string.ok,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}

									});
							builder.show();

						}
					}

				});

			}
			ruyichuanqing_sub_view.addView(qlclayout);

		}
	}*/
	//----------------------------------------------------------------------------------------------
	/**
	 * 幸运选号之选号部分
	 * 
	 * @param aGameType
	 *            彩票类型
	 */
	private void chooseLuckNumShowBallMoney(String aGameType) {
		setContentView(R.layout.choose_luck_num_trade_showball);

		// zlm 7.15 代码修改：添加小球是否可画的判断
		isDrawing = false;

		// zlm 7.13 代码修改：添加初始化
		iProgressJizhu = 1;
		iProgressBeishu = 1;
		iProgressQishu = 1;
		type05 = aGameType;

		int iStartZhuShu = iProgressJizhu;
		int iStartBeiShu = iProgressBeishu;
		int iStartQiShu = iProgressQishu;

		mTextMoney = (TextView) findViewById(R.id.choose_luck_num_show_money_total);
		mTextJizhu = (TextView) findViewById(R.id.choose_luck_num_text_zhushu);
		mTextBeishu = (TextView) findViewById(R.id.choose_luck_num_text_beishu);
		mTextQishu = (TextView) findViewById(R.id.choose_luck_num_text_qishu);

		String iTempString = "共" + iStartZhuShu + "注，共"
				+ (iStartZhuShu * iStartBeiShu * iStartQiShu * 2) + "元";
		mTextMoney.setText(iTempString);
		mTextJizhu.setText("" + iProgressJizhu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu.setText("" + iProgressQishu);

		mSeekBarJizhu = (SeekBar) findViewById(R.id.choose_luck_num_seek_jizhu);
		mSeekBarJizhu.setOnSeekBarChangeListener(this);
		mSeekBarJizhu.setProgress(iProgressJizhu);

		mSeekBarBeishu = (SeekBar) findViewById(R.id.choose_luck_num_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);

		mSeekBarQishu = (SeekBar) findViewById(R.id.choose_luck_num_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		// showSsqLayout01(type05);
		// zlm 7.16 代码修改：变换显示函数
		drawSuccess(iProgressJizhu);

		// 周黎鸣7.3代码修改：添加投注ImageButton的代码
		ImageButton subtractJiZhuBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_subtract_jizhu);
		// 周黎鸣7.3代码添加：添加选择注数的“减号”
		subtractJiZhuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			// zlm 7.13 代码修改：注数中减号的功能
			@Override
			public void onClick(View v) {
				iProgressJizhu--;
				PublicMethod.myOutput("----- ---***");
				Message msg = new Message();
				msg.what = -6;
				seekBarHandler.sendMessage(msg);
				mSeekBarJizhu.setProgress(iProgressJizhu);
			}

		});
		// 周黎鸣7.3代码添加：添加选择注数的“加号”
		ImageButton addJiZhuBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_add_jizhu);
		addJiZhuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			// zlm 7.13 代码修改：注数中加号的功能
			@Override
			public void onClick(View v) {
				// zlm 7.14 代码修改：增加判断
				if (iProgressJizhu < 5) {
					// iProgressJizhu++;

					Message msg = new Message();
					msg.what = -6;
					seekBarHandler.sendMessage(msg);
					mSeekBarJizhu.setProgress(++iProgressJizhu);
				}
			}

		});
		// 周黎鸣7.3代码添加：添加选择倍数的“减号”
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_subtract_beishu);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSeekBarBeishu.setProgress(--iProgressBeishu);
			}

		});
		// 周黎鸣7.3代码添加：添加选择倍数的“加号”
		ImageButton addBeishuBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_add_beishu);
		addBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSeekBarBeishu.setProgress(++iProgressBeishu);
			}

		});
		// 周黎鸣7.3代码添加：添加选择期数的“减号”
		ImageButton subtractQihaoBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_subtract_qishu);
		subtractQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSeekBarQishu.setProgress(--iProgressQishu);
			}

		});
		// 周黎鸣7.3代码添加：添加选择期数的“加号”
		ImageButton addQihaoBtn = (ImageButton) findViewById(R.id.choose_luck_num_seekbar_add_qishu);
		addQihaoBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				mSeekBarQishu.setProgress(++iProgressQishu);
			}

		});

		TextView titleTextView = (TextView) findViewById(R.id.choose_luck_num_gametype_title);
		if (aGameType.equalsIgnoreCase("ssq")) {
			titleTextView.setText(R.string.xyxh_shuangseqiu); // zlm 7.9
																// 代码修改：修改标题
		} else if (aGameType.equalsIgnoreCase("fc3d")) {
			titleTextView.setText(R.string.xyxh_fucai3d); // zlm 7.9 代码修改：修改标题
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			titleTextView.setText(R.string.xyxh_qilecai); // zlm 7.9 代码修改：修改标题
		} else if (aGameType.equalsIgnoreCase("pl3")) {
			titleTextView.setText(R.string.xyxh_pailiesan); // zlm 8.9 排列三
		} else if (aGameType.equalsIgnoreCase("cjdlt")) {
			titleTextView.setText(R.string.xyxh_chaojidaletou); // zlm 8.9 超级大乐透
		}
		// 周黎鸣7.3代码修改：添加投注ImageButton的代码
		ImageButton payImageBtn = new ImageButton(this);
		payImageBtn = (ImageButton) findViewById(R.id.choose_luck_num_touzhu);
		payImageBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 周黎鸣 7.4 代码修改：添加登陆的判断
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
						RuyiExpressFeel.this, "addInfo");
				String sessionidStr = shellRW.getUserLoginInfo("sessionid");
				if (sessionidStr.equals("")) {
					Intent intentSession = new Intent(RuyiExpressFeel.this,
							UserLogin.class);
					startActivity(intentSession);
				} else {
					showXingYunXuanHaoListView(ID_CLLN_SHOW_ZHIFU_DIALOG,
							type05);
				}
			}

		});

		// 周黎鸣7.4代码修改： 将Button换成ImageButton
		ImageButton returnBtn = new ImageButton(this);
		returnBtn = (ImageButton) findViewById(R.id.choose_luck_num_return);
		returnBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showListView(ID_CLLN_MAINLISTVIEW);
			}
		});
	}

	/**
	 * 获取小球的随机数/
	 * 
	 * @param aGameType
	 *            彩票类型
	 * @param aRandomNums
	 *            随机数的个数
	/**
	 * 获取小球的随机数/
	 * 
	 * @param aGameType
	 *            彩票类型
	 * @param aRandomNums
	 *            随机数的个数
	 * @return 随机数数组
	 */
	public int[] getBallNum(String aGameType, int aRandomNums) {
		int[] ballNumStr = new int[7];
		// int[] ballNumStr = {1,2,3,4,5,6,7};
		if (aGameType.equalsIgnoreCase("ssq")) {
			int[] iShowRedBallNum = PublicMethod.getRandomsWithoutCollision(
					aRandomNums - 1, 1, 33);
			int[] iShowBlueBallNum = PublicMethod.getRandomsWithoutCollision(1,
					1, 16);

			for (int i = 0; i < 6; i++) {
				ballNumStr[i] = iShowRedBallNum[i];
			}
			ballNumStr[6] = iShowBlueBallNum[0];
		} else if (aGameType.equalsIgnoreCase("fc3d") || aGameType.equalsIgnoreCase("pl3")) {  //zlm 排列三
			int[] iShowBallNum01 = PublicMethod.getRandomsWithoutCollision(
					aRandomNums - 2, 0, 9);
			int[] iShowBallNum02 = PublicMethod.getRandomsWithoutCollision(
					aRandomNums - 2, 0, 9);
			int[] iShowBallNum03 = PublicMethod.getRandomsWithoutCollision(
					aRandomNums - 2, 0, 9);
			ballNumStr[0] = iShowBallNum01[0];
			ballNumStr[1] = iShowBallNum02[0];
			ballNumStr[2] = iShowBallNum03[0];
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			int[] iShowRedBallNum = PublicMethod.getRandomsWithoutCollision(
					aRandomNums, 1, 30);
			ballNumStr = iShowRedBallNum;
		} else if (aGameType.equalsIgnoreCase("cjdlt")) {  //zlm 超级大乐透
			int[] iShowRedBallNum = PublicMethod.getRandomsWithoutCollision(
					aRandomNums - 2, 1, 35);
			int[] iShowBlueBallNum = PublicMethod.getRandomsWithoutCollision(2,
					1, 12);

			for (int i = 0; i < 5; i++) {
				ballNumStr[i] = iShowRedBallNum[i];
			}
			ballNumStr[5] = iShowBlueBallNum[0];
			ballNumStr[6] = iShowBlueBallNum[1];
		}

		return ballNumStr;
	}

	/**
	 * 幸运选号中所有小球的布局 zlm 7.13 代码修改：添加代码
	 * 
	 * @param aGameType
	 *            彩票类型
	 * @param layout
	 *            布局
	 * @return 随机数
	 */
	public int[] showAllBallLayout(String aGameType, LinearLayout layout) {
		// zlm 7.16 代码修改：添加小球是否可画的判断
		isDrawing = false;
		int[] numRandomGroup = new int[7];
		if (aGameType.equalsIgnoreCase("ssq")) {
			OneBallView showBallView;
			int[] group01 = new int[7];
			int[] group02 = new int[6];
			int[] group03 = new int[6];
			int[] group = new int[7];

			group01 = getBallNum(aGameType, 7);

			for (int i = 0; i < 6; i++) {
				group02[i] = group01[i];
			}

			group03 = sort(group02);

			for (int i = 0; i < 6; i++) {
				group[i] = group03[i];
			}

			group[6] = group01[6];
			numRandomGroup = group;
			// randomNumGroup_5_ssq = group;

			// int[] randomNumGroup ;
			// randomNumGroup_5_ssq = getBallNum(aGameType , 7);
			for (int i = 0; i < 6; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
			showBallView = new OneBallView(this);
			showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
					+ numRandomGroup[6], aBlueColorResId);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layout.addView(showBallView, lp);
			// ballLayoutGroup.addView(layout01, lp);
		} else if (aGameType.equalsIgnoreCase("fc3d")) {
			OneBallView showBallView;
			int[] group = new int[3];
			int[] group01 = new int[7];
			group01 = getBallNum(aGameType, 3);
			for (int i = 0; i < 3; i++) {
				group[i] = group01[i];
			}
			// int[] randomNumGroup ;
			numRandomGroup = group;
			// numRandomGroup = randomNumGroup_5_fc3d;
			for (int i = 0; i < 3; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			OneBallView showBallView;
			// int[] randomNumGroup ;
			numRandomGroup = sort(getBallNum(aGameType, 7));
			for (int i = 0; i < 7; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
			}
		} else if (aGameType.equalsIgnoreCase("pl3")) {       //zlm 排列三
			OneBallView showBallView;
			int[] group = new int[3];
			int[] group01 = new int[7];
			group01 = getBallNum(aGameType, 3);
			for (int i = 0; i < 3; i++) {
				group[i] = group01[i];
			}
			numRandomGroup = group;
			for (int i = 0; i < 3; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
			}
		} else if (aGameType.equalsIgnoreCase("cjdlt")) {             //zlm 超级大乐透
			OneBallView showBallView;
			int[] group01 = new int[7];
			int[] group02 = new int[5];
			int[] group03 = new int[5];
			int[] group04 = new int[2];
			int[] group05 = new int[2];
			int[] group = new int[7];

			group01 = getBallNum(aGameType, 7);

			for (int i = 0; i < 5; i++) {
				group02[i] = group01[i];
			}

			group03 = sort(group02);

			for (int i = 0; i < 5; i++) {
				group[i] = group03[i];
			}
			
			group04[0] = group01[5];
			group04[1] = group01[6];
			
			group05 = sort(group04);
			
			group[5] = group05[0];
			group[6] = group05[1];
			numRandomGroup = group;

			for (int i = 0; i < 5; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
			}
			for(int i = 5 ; i<7 ; i++){
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ numRandomGroup[i], aBlueColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				layout.addView(showBallView, lp);
			}
		} 
		return numRandomGroup;
	}

	// zlm 7.13 代码修改：小球绘画成功的函数
	/**
	 * 小球绘画成功
	 * 
	 * @param aChangeTo
	 *            seekBar的当前数值
	 */
	public void drawSuccess(int aChangeTo) {
		isDrawing = true;

		layoutAll = new LinearLayout[5];
		layoutAll[0] = (LinearLayout) findViewById(R.id.choose_luck_num_ball_linearlayout_01);
		layoutAll[1] = (LinearLayout) findViewById(R.id.choose_luck_num_ball_linearlayout_02);
		layoutAll[2] = (LinearLayout) findViewById(R.id.choose_luck_num_ball_linearlayout_03);
		layoutAll[3] = (LinearLayout) findViewById(R.id.choose_luck_num_ball_linearlayout_04);
		layoutAll[4] = (LinearLayout) findViewById(R.id.choose_luck_num_ball_linearlayout_05);
		int i;
		countLinearLayout = 0;
		for (i = 0; i < 5; i++) {
			if (layoutAll[i] != null && layoutAll[i].getChildCount() > 0) {
				countLinearLayout = countLinearLayout + 1; // zlm 7.14 代码修改：修改形式
			}
		}

		if (countLinearLayout < aChangeTo) {
			for (i = countLinearLayout; i < aChangeTo; i++) {
				// showAllBallLayout(type05 , layoutAll[i]);
				receiveRandomNum[i] = showAllBallLayout(type05, layoutAll[i]);
				layoutAll[i].invalidate();
			}
		} else {// if(countLinearLayout > iProgressJizhu)
			for (i = aChangeTo; i < countLinearLayout; i++) {
				layoutAll[i].removeAllViewsInLayout();
				layoutAll[i].invalidate();
			}
		}

		isDrawing = false;
		Message msg = new Message();
		msg.what = -6;
		seekBarHandler.sendMessage(msg);
	}

	// zlm 7.13 代码修改：handler消息处理
	/**
	 * handler消息处理
	 */
	Handler seekBarHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -6:
				if (isDrawing)
					return;
				else {
					if (countLinearLayout != iProgressJizhu)
						drawSuccess(iProgressJizhu);
				}
				break;
			}
		}
	};

	/**
	 * seekBar的处理 //zlm 7.13 代码修改：在seekBar中添加Handler消息
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.choose_luck_num_seek_jizhu:
			iProgressJizhu = iProgress;
			mTextJizhu.setText("" + iProgressJizhu);
			showTextSumMoney();

			Message msg = new Message();
			msg.what = -6;
			seekBarHandler.sendMessage(msg);
			break;

		case R.id.choose_luck_num_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			showTextSumMoney();
			break;

		case R.id.choose_luck_num_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			showTextSumMoney();
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// zlm 7.14 代码修改：添加代码
		Message msg = new Message();
		msg.what = -6;
		seekBarHandler.sendMessage(msg);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		// zlm 7.14 代码修改：添加代码
		Message msg = new Message();
		msg.what = -6;
		seekBarHandler.sendMessage(msg);
	}

	/**
	 * 注数和钱数
	 */
	public void showTextSumMoney() {
		int iZhuShu = iProgressJizhu;
		int iBeiShu = iProgressBeishu;
		int iQiShu = iProgressQishu;
		String iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * iBeiShu * 2)
				+ "元";// *iQiShu取消 zlm 20100713
		// mTextJizhu = (TextView)
		// findViewById(R.id.choose_luck_num_show_money_total);
		mTextMoney.setText(iTempString);
	}

	/**
	 * 幸运选号：最后的确认支付Dailog
	 */
	// 周黎鸣 7.3 代码修改：在该对话框中添加小球的显示
	private void showAgreeAndPayDialog(String aGameType) {
		type06 = aGameType;

		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR);
		int minute = calendar.get(Calendar.MINUTE);
		String[] gameType = {"ssq" , "fc3d" , "qlc" , "pl3" , "cjdlt"};
		int[] titleID = {R.string.shuangseqiu , R.string.fucai3d ,R.string.qilecai ,R.string.pailiesan , R.string.chaojidaletou};

		LayoutInflater inflater = LayoutInflater.from(this);
		View textView = inflater.inflate(
				R.layout.choose_luck_lottery_num_agree_and_pay_dialog, null);

		agreeAndpayTitleView = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_title);
		TextView tx01 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text01);
		tx01.setText("在" + hour + ":" + minute + "您的幸运选号是：" + "\n");

		TextView tx02 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text02);
		agreePayBallLayout01 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout01);
		agreePayBallLayout02 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout02);
		agreePayBallLayout03 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout03);
		agreePayBallLayout04 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout04);
		agreePayBallLayout05 = (LinearLayout) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_linearlayout05);

		// zlm 7.13 代码修改：变换变量                   zlm 8.11 代码修改
		for(int i = 0; i<5 ; i++){
			if(type06.equalsIgnoreCase(gameType[i])){
				agreeAndpayTitleView.setText(titleID[i]);
				showAgreeAndPayBall();
			}
		}

		TextView tx03 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text03);
		tx03.setText("注数：  " + "" + iProgressJizhu + "   注" + "\n");

		TextView tx04 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text04);
		tx04.setText("倍数：  " + "" + iProgressBeishu + "   倍" + "\n");

		TextView tx05 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text05);
		tx05.setText("追号：  " + "" + iProgressQishu + "   期" + "\n");

		TextView tx06 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text06);
		tx06.setText("总金额：  " + "" + iProgressJizhu * iProgressBeishu * 2
				+ "   元" + "\n");// *iProgressQishu 取消 zlm 20100713

		TextView tx07 = (TextView) textView
				.findViewById(R.id.choose_luck_lottery_num_agree_and_pay_dialog_text07);
		tx07.setText("确认支付吗？" + "\n");

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setView(textView);

		builder.setPositiveButton(R.string.zhifu,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						showDialog(PROGRESS_VALUE);// 显示网络提示框 陈晨2010/7/10
						Thread t = new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String strBet;
								String[] strBetTC = new String[2];
								// strBet = bet(type06 , iProgressJizhu ,
								// iProgressBeishu);
								// 新接口 陈晨20100711
								PublicMethod.myOutput("----iProgressQishu-----"
										+ iProgressQishu
										+ "---iProgressJizhu---"
										+ iProgressJizhu);
								if(type06.equals("ssq") || type06.equals("fc3d") || type06.equals("qlc") ){
								strBet = bet(type06, iProgressQishu,
										iProgressJizhu, iProgressBeishu);
								// if(strBet.equals("0000")){
								if (strBet.equals("000000")) {
									Message msg = new Message();
									msg.what = 6;
									handler.sendMessage(msg);
								} else if (strBet.equals("070002")) {
									Message msg = new Message();
									msg.what = 7;
									handler.sendMessage(msg);
								} else if (strBet.equals("040006")) {
									Message msg = new Message();
									msg.what = 1;
									handler.sendMessage(msg);
								} else if (strBet.equals("1007")) {
									Message msg = new Message();
									msg.what = 2;
									handler.sendMessage(msg);
								} else if (strBet.equals("040007")) {
									Message msg = new Message();
									msg.what = 4;
									handler.sendMessage(msg);
								} else if (strBet.equals("4444")) {
									Message msg = new Message();
									msg.what = 3;
									handler.sendMessage(msg);
								} else if (strBet.equals("00")) {
									Message msg = new Message();
									msg.what = 8;
									handler.sendMessage(msg);
								} else {
									Message msg = new Message();
									msg.what = 9;
									handler.sendMessage(msg);
								}
								} else if(type06.equals("pl3") || type06.equals("cjdlt")){
									strBetTC = betTC(type06, iProgressQishu,
											iProgressJizhu, iProgressBeishu);
									if (strBetTC[0].equals("0000")&&strBetTC[1].equals("000000")) {
										Message msg = new Message();
										msg.what = 6;
										handler.sendMessage(msg);
									} else if (strBetTC[0].equals("070002")) {
										Message msg = new Message();
										msg.what = 7;
										handler.sendMessage(msg);
									} 
									else if (strBetTC[0].equals("0000")&&strBetTC[1].equals("000001")) {
										Message msg = new Message();
										msg.what = 17;
										handler.sendMessage(msg);
									} else if (strBetTC[0].equals("1007")) {
										Message msg = new Message();
										msg.what = 2;
										handler.sendMessage(msg);
									} 
									else if (strBetTC[0].equals("040006")||strBetTC[0].equals("201015")) {
										Message msg = new Message();
										msg.what = 1;
										handler.sendMessage(msg);
									}  else if (strBetTC[1].equals("002002")) {
										Message msg = new Message();
										msg.what = 18;
										handler.sendMessage(msg);
									}
							 	   else if (strBetTC[0].equals("00")&&strBetTC[1].equals("00")) {
										Message msg = new Message();
										msg.what = 8;
										handler.sendMessage(msg);
									}else {
										Message msg = new Message();
										msg.what = 9;
										handler.sendMessage(msg);
									}

								}
							}

						});
						t.start();
						// showXingYunXuanHaoListView(ID_CLLN_SHOW_TRADE_SUCCESS
						// ,null);
					}

				});
		builder.setNegativeButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
					}

				});
		builder.show();
	}

	/**
	 * 确认支付页面显示小球
	 */
	public void showAgreeAndPayBall(){
		//zlm 8.11 代码修改
		LinearLayout[] agreePayBallLayoutGroup = {agreePayBallLayout01 , agreePayBallLayout02 ,agreePayBallLayout03 ,agreePayBallLayout04 ,agreePayBallLayout05};
		for(int i = 1 ; i<6 ; i++){
			if(iProgressJizhu == i){
				for(int j=0 ; j<i ;j++){
					showAgreeAndPayBallLayout(type06, agreePayBallLayoutGroup[j],
							receiveRandomNum[j]);
				}
			}
		}
	}
	/**
	 * 投注网络接口
	 * 
	 * @param aGameType
	 *            彩票类型
	 * @param zhuShu
	 * @param beiShu
	 * @return
	 */
	// 新接口 陈晨 20100711
	public String bet(String aGameType, int qiShu, int zhuShu, int beiShu) {
		// 周黎鸣 7.6 代码修改：添加代码
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		String sessionid = pre.getUserLoginInfo("sessionid");
		String phonenum = pre.getUserLoginInfo("phonenum");

		String error_code = "00";
		if (!phonenum.equals("") || phonenum != null) {
			if (aGameType.equalsIgnoreCase("ssq")) {
				int type = 0;
				int[][] iZhuShu;
				String strBets;
				// GT gt = new GT();
//				加入是否改变切入点判断 陈晨 8.11
				iHttp.whetherChange=false;
				BettingInterface betting = new BettingInterface();
				iZhuShu = changeShuZu(zhuShu, aGameType);
				strBets = GT.betCodeToString(type, zhuShu, "00", beiShu,
						iZhuShu);
				// 新接口 陈晨20100711
				// error_code=betting.Betting(phonenum, strBets, ""+zhuShu
				// ,sessionid);
				// 金额应该乘以倍数 陈晨 20100713
				error_code = betting.BettingNew(strBets, qiShu + "", zhuShu
						* 200 * iProgressBeishu * qiShu + "", sessionid);
			} else if (aGameType.equalsIgnoreCase("fc3d")) {
				int type = 1;
				int[][] iZhuShu;
				String strBets;
				// GT gt = new GT();
//				加入是否改变切入点判断 陈晨 8.11
				iHttp.whetherChange=false;
				BettingInterface betting = new BettingInterface();
				iZhuShu = changeShuZu(zhuShu, aGameType);
				strBets = GT.betCodeToString(type, zhuShu, "00", beiShu,
						iZhuShu);
				// error_code=betting.BettingNew(phonenum, strBets, ""+zhuShu
				// ,sessionid);
				// 新接口 陈晨 20100711
				// 金额应该乘以倍数 陈晨 20100713
				error_code = betting.BettingNew(strBets, qiShu + "", zhuShu
						* 200 * iProgressBeishu * qiShu + "", sessionid);
			} else if (aGameType.equalsIgnoreCase("qlc")) {
				int type = 2;
				int[][] iZhuShu;
				String strBets;
				// GT gt = new GT();
				iHttp.whetherChange=false;
				BettingInterface betting = new BettingInterface();
				iZhuShu = changeShuZu(zhuShu, aGameType);
				strBets = GT.betCodeToString(type, zhuShu, "00", beiShu,
						iZhuShu);
				// error_code = betting.Betting(phonenum, strBets, ""+zhuShu
				// ,sessionid);
				// 新接口 陈晨 20100711
				// 金额应该乘以倍数 陈晨 20100713
				error_code = betting.BettingNew(strBets, qiShu + "", zhuShu
						* 200 * iProgressBeishu * qiShu + "", sessionid);
			}
		}
		return error_code;
	}

	/**
	 * 体彩投注网络接口
	 * @param aGameType
	 * @param qiShu
	 * @param zhuShu
	 * @param beiShu
	 * @return
	 */
	public String[] betTC(String aGameType, int qiShu, int zhuShu, int beiShu){
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				RuyiExpressFeel.this, "addInfo");
		String sessionid = pre.getUserLoginInfo("sessionid");
		String phonenum = pre.getUserLoginInfo("phonenum");

		String[] error_code = new String[2];
		
		if (aGameType.equalsIgnoreCase("pl3")) {
			int type = 3;
			int[][] iZhuShu;
			String strBets;
			// GT gt = new GT();
			iHttp.whetherChange=false;
			BettingInterface betting = new BettingInterface();
			iZhuShu = changeShuZu(zhuShu, aGameType);
			strBets = GT.betCodeToStringTC(type ,iZhuShu);
			// error_code = betting.Betting(phonenum, strBets, ""+zhuShu
			// ,sessionid);
			// 新接口 陈晨 20100711
			// 金额应该乘以倍数 陈晨 20100713
			System.out.println("---strBets---"+strBets+"-----beiShu---"+beiShu+"-----zhuShu"+zhuShu);
			error_code = betting.BettingTC(phonenum, "T01002", strBets, beiShu+"", zhuShu*2*beiShu+"", "2", qiShu+"",sessionid);
		} else if (aGameType.equalsIgnoreCase("cjdlt")) {
			int type = 4;
			int[][] iZhuShu;
			String strBets;
			// GT gt = new GT();
			iHttp.whetherChange=false;
			BettingInterface betting = new BettingInterface();
			iZhuShu = changeShuZu(zhuShu, aGameType);
			strBets = GT.betCodeToStringTC(type ,iZhuShu);
			// error_code = betting.Betting(phonenum, strBets, ""+zhuShu
			// ,sessionid);
			// 新接口 陈晨 20100711
			// 金额应该乘以倍数 陈晨 20100713
			System.out.println("---strBets---"+strBets+"-----beiShu---"+beiShu+"-----zhuShu"+zhuShu);
			error_code = betting.BettingTC(phonenum, "T01001", strBets, beiShu+"", zhuShu*2*beiShu+"", "2",qiShu+"", sessionid);
		}
		return error_code;
	}
	/**
	 * 把随机得到的数组变换成二维数组
	 * 
	 * @param jiZhu
	 * @param aGameType
	 * @return
	 */
	public int[][] changeShuZu(int jiZhu, String aGameType) {
		int[][] zhuShu = null;
		if (aGameType.equalsIgnoreCase("ssq") || aGameType.equalsIgnoreCase("qlc") || aGameType.equalsIgnoreCase("cjdlt")) {
			switch (jiZhu) {
			case 1:
				zhuShu = new int[1][7];
				zhuShu[0] = receiveRandomNum[0];
				break;
			case 2:
				zhuShu = new int[2][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				break;
			case 3:
				zhuShu = new int[3][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				break;
			case 4:
				zhuShu = new int[4][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				break;
			case 5:
				zhuShu = new int[5][7];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				zhuShu[4] = receiveRandomNum[4];
				break;
			}
		} else if (aGameType.equalsIgnoreCase("fc3d") || aGameType.equalsIgnoreCase("pl3")) {
			switch (jiZhu) {
			case 1:
				zhuShu = new int[1][3];
				zhuShu[0] = receiveRandomNum[0];
				break;
			case 2:
				zhuShu = new int[2][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				break;
			case 3:
				zhuShu = new int[3][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				break;
			case 4:
				zhuShu = new int[4][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				break;
			case 5:
				zhuShu = new int[5][3];
				zhuShu[0] = receiveRandomNum[0];
				zhuShu[1] = receiveRandomNum[1];
				zhuShu[2] = receiveRandomNum[2];
				zhuShu[3] = receiveRandomNum[3];
				zhuShu[4] = receiveRandomNum[4];
				break;
			}
		}
		return zhuShu;
	}

	/**
	 * 提示用户输入姓名的对话框
	 */
	private void showAttentionImportNameDialog(String aGameType) {
		type07 = aGameType;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle("您输入的信息有误，内容不能为空，请重新输入姓名！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						showXingYunXuanHaoListView(
								ID_CLLN_XINGMING_DIALOG_LISTVIEW, type07);
					}

				});

		builder.show();
	}

	/**
	 * 提示用户输入生日
	 */
	private void showAttentionImportShengriDialog(String aGameType) {
		type08 = aGameType;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle("您输入的信息有误，请重新输入生日！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						showXingYunXuanHaoListView(
								ID_CLLN_SHENGRI_DIALOG_LISTVIEW, type08);
					}

				});

		builder.show();
	}

	/**
	 * 提示支付成功对话框
	 */
	private void showTradeSuccess() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle("您投注成功！");

		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						showListView(ID_CLLN_MAINLISTVIEW);
					}

				});
		builder.setNegativeButton(R.string.str_return,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 返回
					}

				});
		builder.show();
	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 *            数组
	 * @return 排序好的数组
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;

		for (int i = 0; i < t_s.length; i++) {
			PublicMethod.myOutput("----------------------t_s0000" + i + "----"
					+ t_s[i]);
		}
		// int t_a = 0;
		for (int i = 0; i < t_s.length; i++) {
			for (int j = i + 1; j < t_s.length; j++) {
				if (t_s[i] > t_s[j]) {
					temp = t_s[i];
					t_s[i] = t_s[j];
					t_s[j] = temp;
				}
			}
		}
		for (int i = 0; i < t_s.length; i++) {
			PublicMethod.myOutput("----------------------t_s" + i + t_s[i]);
		}
		return t_s;
	}

	/**
	 * 把随机选的号码变成字符串
	 * 
	 * @param aGameType
	 * @param aRandomNum
	 *            随机数
	 * @return 随机数字符串
	 */
	public String changeGroupToStr(String aGameType, int[] aRandomNum) {
		String changeStr = null;
		if (aGameType.equalsIgnoreCase("ssq")) {
			changeStr = "" + aRandomNum[0] + "," + "" + aRandomNum[1] + ","
					+ "" + aRandomNum[2] + "," + "" + aRandomNum[3] + "," + ""
					+ aRandomNum[4] + "," + "" + aRandomNum[5] + "," + ""
					+ aRandomNum[6];
		} else if (aGameType.equalsIgnoreCase("fc3d")) {
			changeStr = "" + aRandomNum[0] + "," + "" + aRandomNum[1] + ","
					+ "" + aRandomNum[2];
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			changeStr = "" + aRandomNum[0] + "," + "" + aRandomNum[1] + ","
					+ "" + aRandomNum[2] + "," + "" + aRandomNum[3] + "," + ""
					+ aRandomNum[4] + "," + "" + aRandomNum[5] + "," + ""
					+ aRandomNum[6];
		}
		return changeStr;
	}

	/**
	 * 再确认支付页面中添加小球布局
	 * 
	 * @param aGameType
	 * @param aLinearLayout
	 *            小球的布局
	 * @param aRandomNum
	 *            随机数
	 */
	// 周黎鸣 7.3 代码添加：添加了在支付对话框中小球的显示
	public void showAgreeAndPayBallLayout(String aGameType,
			LinearLayout aLinearLayout, int[] aRandomNum) {

		if (aGameType.equalsIgnoreCase("ssq")) {
			OneBallView showBallView;
			for (int i = 0; i < 6; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
			showBallView = new OneBallView(this);
			showBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + aRandomNum[6],
					aBlueColorResId);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			aLinearLayout.addView(showBallView, lp);
		} else if (aGameType.equalsIgnoreCase("fc3d") || aGameType.equalsIgnoreCase("pl3")) {
			OneBallView showBallView;
			for (int i = 0; i < 3; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
				// PublicMethod.myOutput("----------------showBall");
			}
		} else if (aGameType.equalsIgnoreCase("qlc")) {
			OneBallView showBallView;
			for (int i = 0; i < 7; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}
		} else if (aGameType.equalsIgnoreCase("cjdlt")) {       //zlm 超级大乐透
			OneBallView showBallView;
			for (int i = 0; i < 5; i++) {
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, ""
						+ aRandomNum[i], aRedColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}
			for (int i = 5 ; i<7 ; i++){
				showBallView = new OneBallView(this);
				showBallView.initBall(BALL_WIDTH, BALL_WIDTH, "" + aRandomNum[i],
						aBlueColorResId);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				aLinearLayout.addView(showBallView, lp);
			}
		}
	}

	// 刷新套餐查询页面 陈晨 20100713
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			// iCurrentBtFlag=-1代表查询状态 20100713 陈晨
			iCurrentBtFlag = -1;
			setFlag();
			// showRuyiPackageListView();
			break;
		default:
			Toast.makeText(RuyiExpressFeel.this, "未登录成功", Toast.LENGTH_SHORT)
					.show();
			break;
		}
	}

	/**
	 * 网络连接对话框
	 * 
	 * @param id
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case PROGRESS_VALUE: {
			progressDialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressDialog.setMessage("网络连接中...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		}
		}
		return null;
	}

}
