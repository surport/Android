package com.palmdream.RuyicaiAndroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.People;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.netintface.GT;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

/**
 * 
 * @author wangyl 如意传情机选或自选操作成功后跳转的页面
 * 
 */
public class RuyiExpressFeelSuccess extends Activity {

	EditText messageET;
	EditText phoneET;
	ListView phoneListView;

	String phoneString = "1";
	String nameString = "";

	Button phoneAddBtn;
	Button phoneBtn;
	Button okBtn;
	Button cancelBtn;
	Button phoneDelBtn;
	TextView phoneDelTV;
	TextView nameDelTV;
	boolean confirm = true;// 确认电话号码不为空，应默认为false

	public final static String NAME = "NAME";/* 图标 */
	public final static String PHONE = "PHONE";/* 图标 */
	public final static String DELETEBTN = "DELETEBTN"; /* 标题 */
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(10);/* 列表适配器的数据源 */
	Map<String, Object> map;
	// 王艳玲 7.8 修改电话薄
	int listSize;
	RuyiExpressFeelSuccessEfficientAdapter adapter;// 适配器
	public final static int LAYOUT_INDEX = 0;// 适配器holder的layout的ID
	LinearLayout buttonGroup; // 适配器holder欲添加的button的layout
	int iPosition;// 全局holder的position
	String holderphone;// 适配器holder里的电话号码

	public static final int PICK_CONTACT_SUBACTIVITY = 2;
	private static final int DIALOG1_KEY = 0;
	ProgressDialog progressDialog;
	int iRetrytimes = 2;

	String successStr;// 判读是从那个activity传过来的
	// int[][] randomNums = new int[5][7];//机选随机数
	int[][] randomNums;
	// 双色球
	int[] redBallNums = new int[33];// 双色球红球
	int[] blueBallNums = new int[16];// 双色球篮球
	// 福彩3D
	int[] baiBallNums = new int[10];
	int[] shiBallNums = new int[10];
	int[] geBallNums = new int[10];
//	排列三
	int[] baiBallNumsPl3 = new int[10];
	int[] shiBallNumsPl3 = new int[10];
	int[] geBallNumsPl3 = new int[10];
//	大乐透
	int[] redBallNumsDlt = new int[35]; 
	int[] blueBallNumsDlt = new int[12];
	// 七乐彩
	int[] qlcBallNums = new int[33];
	int iBeishu;
	int iZhushu;

	Vector phoneVector;
	String iNumberInET;
	String iTextInET;
	String batchCode;
	Boolean isGenerate;
	String playName;// 彩种
	int sendNo = 0;
	Boolean iFushi;
	// String phonenum;

	/**
	 * 处理信息
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// String result=msg.getData().getString("get");
			switch (msg.what) {
			case -2:
				progressDialog.dismiss(); // wyl 7.21 发送短信提示
				Toast.makeText(RuyiExpressFeelSuccess.this, "赠送彩票成功！发送短信失败",
						Toast.LENGTH_LONG).show();
				break;
			case -1:// 全部搞定
				progressDialog.dismiss();
				// wangyl 7.12 修改赠送成功后点击确定时返回的页面
				if ("ssqJixuan".equalsIgnoreCase(successStr)
						|| "fc3dJixuan".equalsIgnoreCase(successStr)
						|| "qlcJixuan".equalsIgnoreCase(successStr)
						|| "pl3Jixuan".equalsIgnoreCase(successStr)
						|| "dltJixuan".equalsIgnoreCase(successStr)) {
					finish();
				}
				if ("ssqZixuan".equalsIgnoreCase(successStr)
						|| "fc3dZixuan".equalsIgnoreCase(successStr)
						|| "qlcZixuan".equalsIgnoreCase(successStr)
						|| "pl3Zixuan".equalsIgnoreCase(successStr)
						|| "dltZixuan".equalsIgnoreCase(successStr)) {

					RuyiExpressFeelSuccess.this.setResult(RESULT_OK);
					RuyiExpressFeelSuccess.this.finish();
				}
				Toast.makeText(RuyiExpressFeelSuccess.this, "赠送彩票成功！",
						Toast.LENGTH_LONG).show();
				break;
			case 0: {// 发短信线程
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
						RuyiExpressFeelSuccess.this, "addInfo");
				final String phonenum = pre.getUserLoginInfo("phonenum");
				int iTempNumbersSum = phoneVector.size();
				if (!iNumberInET.equalsIgnoreCase("")) {
					iTempNumbersSum++;
				}

				if (iTempNumbersSum > 0) {
					final String[] iTempNumbers = new String[iTempNumbersSum];
					int i1;
					for (i1 = 0; i1 < phoneVector.size(); i1++) {
						iTempNumbers[i1] = (String) phoneVector.elementAt(i1);
					}
					if (!iNumberInET.equalsIgnoreCase("")) {
						iTempNumbers[i1] = iNumberInET;
					}
					new Thread() {
						public void run() {
							boolean sendOk = true;
							for (int i = 0; i < iTempNumbers.length; i++) {
								String content = null;
								if (isGenerate == true) {
									content = "您的好友" + phonenum + "送您"
											+ playName + batchCode + "期彩票"
											+ iZhushu + "注:"
											+ ((String) iNumbers.elementAt(i))
											+ "留言如下："
											+ messageET.getText().toString()
											+ ",手机登录wap.51500.cn查询";

								} else {
									content = "您的好友" + phonenum + "送您"
											+ playName + batchCode + "期彩票"
											+ iZhushu + "注:"
											+ ((String) iNumbers.elementAt(0))
											+ "留言如下："
											+ messageET.getText().toString()
											+ ",手机登录wap.51500.cn查询";
								}
								// =iTextInET;
								String code = iTempNumbers[i];
								PublicMethod.myOutput("-----code------" + code);
								PublicMethod.myOutput("------handle-------"
										+ content);
								sendOk = PublicMethod.sendSMS(code, content);// (String)iNumbers.elementAt(i));//
								if (sendOk == false) {
									break;
								}
								// iPVAF.sendSMS("13466697879","success");
							}
							if (sendOk) {
								Message mg = Message.obtain();
								mg.what = -1;
								handler.sendMessage(mg);
							} else {
								Message mg = Message.obtain();
								mg.what = -2;
								handler.sendMessage(mg);
							}
						}
					}.start();
				} else {
					Message mg = Message.obtain();
					mg.what = -1;
					handler.sendMessage(mg);
				}
				break;
			}
			case 1:
				progressDialog.dismiss();
				Toast.makeText(RuyiExpressFeelSuccess.this, "用户余额不足",
						Toast.LENGTH_LONG).show();
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "扣款失败", Toast.LENGTH_LONG)
						.show();
				break;
			case 3:
				progressDialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(RuyiExpressFeelSuccess.this,
						UserLogin.class);
				startActivity(intentSession);
				// Toast.makeText(getBaseContext(), "请登录",
				// Toast.LENGTH_LONG).show();
				break;
			case 4:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				break;
			case 5:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "赠送失败", Toast.LENGTH_LONG)
						.show();
				break;
			case 6:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "该期已结", Toast.LENGTH_LONG)
						.show();
				break;
			case 7:
				progressDialog.dismiss();
				if ("pl3Jixuan".equalsIgnoreCase(successStr)
						|| "dltJixuan".equalsIgnoreCase(successStr)) {
					finish();
				}
				if ( "pl3Zixuan".equalsIgnoreCase(successStr)
						|| "dltZixuan".equalsIgnoreCase(successStr)) {

					RuyiExpressFeelSuccess.this.setResult(RESULT_OK);
					RuyiExpressFeelSuccess.this.finish();
				}
				
				Toast.makeText(getBaseContext(), "赠送请求已受理", Toast.LENGTH_LONG)
						.show();
				break;
			case 8:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "正在赠送中", Toast.LENGTH_LONG)
						.show();
				break;
			}
			//				
		}
	};

	Vector iNumbers = null;

	private String getRandomString(int[][] aRandoms) {
		String iReturn = "";
		for (int i = 0; i < aRandoms.length; i++) {
			int[] iLines = aRandoms[i];
			int i1 = 0;
			for (; i1 < iLines.length - 1; i1++) {
				iReturn += "" + iLines[i1] + ",";
			}
			iReturn += "" + iLines[i1] + "|";
		}
		PublicMethod.myOutput("-----iReturn-------" + iReturn);
		return iReturn;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 改变实例化位置
		phoneVector = new Vector();

		// ----- 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Bundle bundle = this.getIntent().getExtras();
		successStr = bundle.getString("success");
		PublicMethod.myOutput("-------------successStr----------------------"
				+ successStr);
		if ("ssqJixuan".equals(successStr)) {
			int ssqzhushu = Integer.parseInt(bundle.getString("ssqzhushu"));
			PublicMethod.myOutput("-----------ssqzhushu------------------"
					+ ssqzhushu);
			// randomNums=getRandromNums("ssq",ssqzhushu);
			iZhushu = ssqzhushu;
			// String[][] ssqcode=GT.betCodeToString(0, 5, , 0, randomNums);
			// for(int i=0;i<ssqzhushu;i++){
			// if(randomNums[i]!=null){
			// for(int j=0;j<7;j++){
			// PublicMethod.myOutput("-------randomNums["+i+"]["+j+"]------------"+randomNums[i][j]);
			// }
			// }
			// }
		}
		if ("fc3dJixuan".equals(successStr)) {
			int fc3dzhushu = Integer.parseInt(bundle.getString("fc3dzhushu"));
			PublicMethod.myOutput("-----------fc3dzhushu------------------"
					+ fc3dzhushu);
			// randomNums=getRandromNums("fc3d",fc3dzhushu);
			iZhushu = fc3dzhushu;

			// for(int i=0;i<fc3dzhushu;i++){
			// if(randomNums[i]!=null){
			// for(int j=0;j<3;j++){
			// PublicMethod.myOutput("-------randomNums["+i+"]["+j+"]------------"+randomNums[i][j]);
			// }
			// }
			// }
		}

		if ("qlcJixuan".equals(successStr)) {
			int qlczhushu = Integer.parseInt(bundle.getString("qlczhushu"));
			PublicMethod.myOutput("-----------qlczhushu------------------"
					+ qlczhushu);
			// randomNums=getRandromNums("qlc",qlczhushu); 后面再产生随机号码2010/7/9陈晨
			iZhushu = qlczhushu;
			// for(int i=0;i<qlczhushu;i++){
			// if(randomNums[i]!=null){
			// for(int j=0;j<7;j++){
			// PublicMethod.myOutput("-------randomNums["+i+"]["+j+"]------------"+randomNums[i][j]);
			// }
			// }
			// }
		}if ("pl3Jixuan".equals(successStr)) {
			int pl3zhushu = Integer.parseInt(bundle.getString("pl3zhushu"));
			PublicMethod.myOutput("-----------pl3zhushu------------------"
					+ pl3zhushu);
			// randomNums=getRandromNums("qlc",qlczhushu); 后面再产生随机号码2010/7/9陈晨
			iZhushu = pl3zhushu;

			// for(int i=0;i<qlczhushu;i++){
			// if(randomNums[i]!=null){
			// for(int j=0;j<7;j++){
			// PublicMethod.myOutput("-------randomNums["+i+"]["+j+"]------------"+randomNums[i][j]);
			// }
			// }
			// }
		}if ("dltJixuan".equals(successStr)) {
			int dltzhushu = Integer.parseInt(bundle.getString("dltzhushu"));
			PublicMethod.myOutput("-----------qlczhushu------------------"
					+ dltzhushu);
			// randomNums=getRandromNums("qlc",qlczhushu); 后面再产生随机号码2010/7/9陈晨
			iZhushu = dltzhushu;

			// for(int i=0;i<qlczhushu;i++){
			// if(randomNums[i]!=null){
			// for(int j=0;j<7;j++){
			// PublicMethod.myOutput("-------randomNums["+i+"]["+j+"]------------"+randomNums[i][j]);
			// }
			// }
			// }
		}
		if ("ssqZixuan".equals(successStr)) {
			int[] tempRedBall = sort(bundle.getIntArray("redBall"));
			redBallNums = tempRedBall;
			int[] tempBlueBall = sort(bundle.getIntArray("blueBall"));
			blueBallNums = tempBlueBall;
			for (int i = 0; i < tempRedBall.length; i++) {
				PublicMethod.myOutput("------redBallNums[" + i + "]---------"
						+ redBallNums[i]);
			}
			for (int i = 0; i < tempBlueBall.length; i++) {
				PublicMethod.myOutput("------blueBallNums[" + i + "]---------"
						+ blueBallNums[i]);
			}
			iBeishu = bundle.getInt("ssqzixuanbeishu");
			iZhushu = bundle.getInt("ssqzixuanzhushu");
			PublicMethod.myOutput("------iBeishu---------" + iBeishu);
			PublicMethod.myOutput("------iZhushu---------" + iZhushu);

		}

		if ("fc3dZixuan".equals(successStr)) {
			int[] tempBaiBall = sort(bundle.getIntArray("baiBall"));
			baiBallNums = tempBaiBall;
			int[] tempShiBall = sort(bundle.getIntArray("shiBall"));
			shiBallNums = tempShiBall;
			int[] tempGeBall = sort(bundle.getIntArray("geBall"));
			geBallNums = tempGeBall;
			for (int i = 0; i < tempBaiBall.length; i++) {
				PublicMethod.myOutput("------baiBallNums[" + i + "]---------"
						+ baiBallNums[i]);
			}
			for (int i = 0; i < tempShiBall.length; i++) {
				PublicMethod.myOutput("------shiBallNums[" + i + "]---------"
						+ shiBallNums[i]);
			}
			for (int i = 0; i < tempGeBall.length; i++) {
				PublicMethod.myOutput("------geBallNums[" + i + "]---------"
						+ geBallNums[i]);
			}
			iBeishu = bundle.getInt("fc3dzixuanbeishu");
			iZhushu = bundle.getInt("fc3dzixuanzhushu");
			PublicMethod.myOutput("------iBeishu---------" + iBeishu);
			PublicMethod.myOutput("------iZhushu---------" + iZhushu);

		}

		if ("qlcZixuan".equals(successStr)) {
			int[] tempQlcBall = sort(bundle.getIntArray("qlcBall"));
			qlcBallNums = tempQlcBall;
			for (int i = 0; i < tempQlcBall.length; i++) {
				PublicMethod.myOutput("------qlcBallNums[" + i + "]---------"
						+ qlcBallNums[i]);
			}
			iBeishu = bundle.getInt("qlczixuanbeishu");
			iZhushu = bundle.getInt("qlczixuanzhushu");
			PublicMethod.myOutput("------iBeishu---------" + iBeishu);
			PublicMethod.myOutput("------iZhushu---------" + iZhushu);

		}if ("pl3Zixuan".equals(successStr)) {
			int[] tempBaiBall = sort(bundle.getIntArray("baiBall"));
			baiBallNumsPl3 = tempBaiBall;
			int[] tempShiBall = sort(bundle.getIntArray("shiBall"));
			shiBallNumsPl3 = tempShiBall;
			int[] tempGeBall = sort(bundle.getIntArray("geBall"));
			geBallNumsPl3 = tempGeBall;
			for (int i = 0; i < tempBaiBall.length; i++) {
				PublicMethod.myOutput("------baiBallNumsPl3[" + i + "]---------"
						+ baiBallNumsPl3[i]);
			}
			for (int i = 0; i < tempShiBall.length; i++) {
				PublicMethod.myOutput("------shiBallNumsPl3[" + i + "]---------"
						+ shiBallNumsPl3[i]);
			}
			for (int i = 0; i < tempGeBall.length; i++) {
				PublicMethod.myOutput("------geBallNumsPl3[" + i + "]---------"
						+ geBallNumsPl3[i]);
			}
			iBeishu = bundle.getInt("pl3zixuanbeishu");
			iZhushu = bundle.getInt("pl3zixuanzhushu");
			PublicMethod.myOutput("------iBeishu---------" + iBeishu);
			PublicMethod.myOutput("------iZhushu---------" + iZhushu);

		}if ("dltZixuan".equals(successStr)) {
			int[] tempRedBall = sort(bundle.getIntArray("redBall"));
			redBallNumsDlt = tempRedBall;
			int[] tempBlueBall = sort(bundle.getIntArray("blueBall"));
			blueBallNumsDlt = tempBlueBall;
			for (int i = 0; i < tempRedBall.length; i++) {
				PublicMethod.myOutput("------redBallNumsDlt[" + i + "]---------"
						+ redBallNumsDlt[i]);
			}
			for (int i = 0; i < tempBlueBall.length; i++) {
				PublicMethod.myOutput("------blueBallNumsDlt[" + i + "]---------"
						+ blueBallNumsDlt[i]);
			}
			iBeishu = bundle.getInt("dltzixuanbeishu");
			iZhushu = bundle.getInt("dltzixuanzhushu");
			PublicMethod.myOutput("------iBeishu---------" + iBeishu);
			PublicMethod.myOutput("------iZhushu---------" + iZhushu);

		}
		setContentView(R.layout.ruyichuanqing_success_layout);

		messageET = (EditText) findViewById(R.id.ruyichuanqing_success_message_text);
		phoneET = (EditText) findViewById(R.id.ruyichuanqing_success_phonenumber_text);

		phoneListView = (ListView) findViewById(R.id.ruyichuanqing_success_phonenumber_listview);

		phoneAddBtn = (Button) findViewById(R.id.ruyichuanqing_phone_add);
		phoneAddBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// wangyl 7.14 判断手机号码长度 手机号码：可输入11位数字；
				if (phoneET.length() < 11) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RuyiExpressFeelSuccess.this);
					builder.setTitle("请输入正确手机号码");
					builder.setMessage("请输入11位手机号码");
					// 确定
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}

							});
					builder.show();
				} else {
					if (phoneString == "1" || phoneString.equalsIgnoreCase("1")) {// phoneString==1从编辑框添加
						// wangyl 7.13 解决电话薄删除号码时总是删除最后一个的问题
						list = addOrDeleteResultForAdapter(1, "", phoneET
								.getText().toString(), 0);
					} else {// 否则从电话薄添加
						// wangyl 7.13 解决电话薄删除号码时总是删除最后一个的问题
						list = addOrDeleteResultForAdapter(1, nameString,
								phoneString, 0);
					}

				}
				phoneString = "1";
				setListViewAdapter(list);
			}

		});

		phoneBtn = (Button) findViewById(R.id.ruyichuanqing_phone_btn);
		phoneBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// wangyl 7.15联想乐phone暂不支持此功能
				
				  Uri uri = Uri.parse("content://contacts/people"); Intent
				  intent = new Intent(Intent.ACTION_PICK,uri);
				  startActivityForResult(intent,PICK_CONTACT_SUBACTIVITY);
				 
//				Toast.makeText(getBaseContext(), "暂不支持此功能", Toast.LENGTH_LONG)
//						.show();
			}

		});

		okBtn = (Button) findViewById(R.id.ruyichuanqing_success_ok_btn);
		okBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 7.28 wangyl 输入手机号码限制
				if (phoneET.length() < 11 && phoneVector.isEmpty() == true) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							RuyiExpressFeelSuccess.this);
					builder.setTitle("请输入正确手机号码");
					builder.setMessage("请输入11位手机号码");
					// 确定
					builder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}

							});
					builder.show();
				} else {
					if (phoneET.length() < 11 && phoneET.length() > 0) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RuyiExpressFeelSuccess.this);
						builder.setTitle("请输入正确手机号码");
						builder.setMessage("请输入11位手机号码");
						// 确定
						builder.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}

								});
						builder.show();
					}else{
						ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
								RuyiExpressFeelSuccess.this, "addInfo");
						String sessionid = pre.getUserLoginInfo("sessionid");
						// String phonenum=pre.getUserLoginInfo("phonenum");
						// String betcode = null;
						// String error_code = "00";
						// fyj edit 错误的登录逻辑
						if (sessionid.equals("")) {
							Intent intentSession = new Intent(
									RuyiExpressFeelSuccess.this,
									UserLogin.class);
							startActivity(intentSession);
							return;
							}else if(jrtLot.ifPerfectIfo(RuyiExpressFeelSuccess.this, sessionid)){//身份验证
								if (confirm) {
								// 需要判断后弹出提示框
								// 周黎鸣 7.5 代码修改：添加登陆判断
								// ShellRWSharesPreferences shellRW =new
								// ShellRWSharesPreferences(RuyiExpressFeelSuccess.this,"addInfo");
								// String
								// sessionid=shellRW.getUserLoginInfo("sessionid");
								// else{
								// 7.7 如意传情 倍数固定为1 将原来的改为线程 陈晨7/8
								// ------------怎样回到主列表
								showDialog(DIALOG1_KEY); // DIALOG1_KEY 联网
								iHttp.whetherChange=false;
								Thread t = new Thread(new Runnable() {
									ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
											RuyiExpressFeelSuccess.this, "addInfo");
									String sessionid = pre
											.getUserLoginInfo("sessionid");
									String phonenum = pre
											.getUserLoginInfo("phonenum");
									String betcode = "";
									String error_code = "999999,";
	
									JSONObject obj;
	
									@Override
									public void run() {
										// if("ssqJixuan".equals(successStr)){
										// betcode=GT.betCodeToString(0, iZhushu,
										// "00", 1,
										// randomNums);
										// PublicMethod.myOutput("-----------betcodessq==="+betcode);
										// }else
										// if("fc3dJixuan".equals(successStr)){
										// betcode=GT.betCodeToString(1, iZhushu,
										// "00", 1,
										// randomNums);
										// PublicMethod.myOutput("-----------betcodessq==="+betcode);
										// }else if("qlcJixuan".equals(successStr)){
										// betcode=GT.betCodeToString(2, iZhushu,
										// "00", 1,
										// randomNums);
										// PublicMethod.myOutput("-----------betcodessq==="+betcode);
										// }else if("ssqZixuan".equals(successStr)){
										// betcode=zhumaZiXuan("F47104",iBeishu,0);
										// PublicMethod.myOutput("-----------betcodessqzixuan==="+betcode);
										// }else
										// if("fc3dZixuan".equals(successStr)){
										// betcode=zhumaZiXuan("F47103",iBeishu,iZhushu);
										// PublicMethod.myOutput("-----------betcodefc3dzixuan==="+betcode);
										// }else if("qlcZixuan".equals(successStr)){
										// betcode=zhumaZiXuan("F47102",iBeishu,iZhushu);
										// PublicMethod.myOutput("-----------betcodeqlczixuan==="+betcode);
										// }
										String aToPhoneNum = "";
	//									while (iRetrytimes < 3 && iRetrytimes > 0) {
											//						
	//										try {
												// 获取接受手机号
												String toPhoneNum = "";
												betcode="";
												iNumberInET = "";
												iTextInET = messageET.getText()
														.toString();
	
												// if(phoneET.getText().toString()!=""||phoneET.getText().toString()!=null){
	
											// 判断如果编辑框中有手机号则加入到数组中 陈晨 2010/7/8
												if("ssqJixuan".equals(successStr)||"fc3dJixuan".equals(successStr)
														||"qlcJixuan".equals(successStr)||"ssqZixuan".equals(successStr)
														||"fc3dZixuan".equals(successStr)||"qlcZixuan".equals(successStr)){
												if (!phoneET.getText().toString()
														.equals("")) {// phoneVector.isEmpty()==true&&
													// if(phoneVector.isEmpty()==false){
													toPhoneNum += phoneET.getText()
															.toString()
															+ ",";
													iNumberInET = phoneET.getText()
															.toString();
													// }
													// else{
													// toPhoneNum+=phoneET.getText().toString()+",";
													// }
												}
	
												// 如果动态数组不为空则获取手机号码 陈晨 2010/7/8
												if (phoneVector.isEmpty() == false) {
													PublicMethod
															.myOutput(">>>>>>>>>>>>>>>>>"
																	+ phoneVector
																			.size());
													for (int i = 0; i < phoneVector
															.size(); i++) {
														toPhoneNum += phoneVector
																.elementAt(i)
																.toString()
																+ ",";
													}
													// toPhoneNum+=phoneVector.elementAt(phoneVector.size()-1).toString();
												}
	
												if (iNumbers != null) {
													iNumbers.removeAllElements();
												} else {
													iNumbers = new Vector();
											}
												}else if("pl3Jixuan".equals(successStr)||"dltJixuan".equals(successStr)
														||"pl3Zixuan".equals(successStr)||"dltZixuan".equals(successStr)){
													
													if (!phoneET.getText().toString()
															.equals("")&&phoneVector.isEmpty() == false) {// phoneVector.isEmpty()==true&&
														// if(phoneVector.isEmpty()==false){
														toPhoneNum += phoneET.getText()
																.toString()
																+ ",";
														iNumberInET = phoneET.getText()
																.toString();
														// }
														// else{
														// toPhoneNum+=phoneET.getText().toString()+",";
														// }
													}if(!phoneET.getText().toString()
															.equals("")&&phoneVector.isEmpty() == true){
														toPhoneNum += phoneET.getText()
														.toString();
												       iNumberInET = phoneET.getText()
														.toString();
													}
		
													// 如果动态数组不为空则获取手机号码 陈晨 2010/7/8
													if (phoneVector.isEmpty() == false) {
														PublicMethod
																.myOutput(">>>>>>>>>>>>>>>>>"
																		+ phoneVector
																				.size());
														for (int i = 0; i < phoneVector
																.size()-1; i++) {
															toPhoneNum += phoneVector
																	.elementAt(i)
																	.toString()
																	+ ",";
														}
														 toPhoneNum+=phoneVector.elementAt(phoneVector.size()-1).toString();
													}
		
													if (iNumbers != null) {
														iNumbers.removeAllElements();
													} else {
														iNumbers = new Vector();
													}
												}
												if ("ssqJixuan".equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														PublicMethod
																.myOutput("***********"
																		+ phoneVector
																				.size());
	
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															int[][] iTempRand = getRandromNums(
																	"ssq", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															System.out
																	.println("---------iTempRandString----------"
																			+ iTempRandString);
															iNumbers
																	.add(iTempRandString);
															betcode += GT
																	.betCodeToString(
																			0,
																			iZhushu,
																			"00",
																			1,
																			iTempRand)
																	+ ",";
	
														}
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
														sendNo = phoneVector.size() + 1;
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															int[][] iTempRand = getRandromNums(
																	"ssq", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															iNumbers
																	.add(iTempRandString);
															betcode += GT
																	.betCodeToString(
																			0,
																			iZhushu,
																			"00",
																			1,
																			iTempRand)
																	+ ",";
														}
														// betcode+=GT.betCodeToString(0,
														// iZhushu, "00", 1,
														// getRandromNums("ssq",iZhushu));
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
														sendNo = phoneVector.size();
													}
	
													playName = "双色球";
													isGenerate = true;
	
												} else if ("fc3dJixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															int[][] iTempRand = getRandromNums(
																	"fc3d", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															iNumbers
																	.add(iTempRandString);
															// 投注注码和赠送注码一致 陈晨
															// 20100728
															betcode += GT
																	.betCodeToString(
																			1,
																			iZhushu,
																			"00",
																			1,
																			//														
																			iTempRand)
																	+ ",";
														}
														// betcode+=GT.betCodeToString(0,
														// iZhushu, "00", 1,
														// getRandromNums("ssq",iZhushu));
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
	
														sendNo = phoneVector.size() + 1;
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															int[][] iTempRand = getRandromNums(
																	"fc3d", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															iNumbers
																	.add(iTempRandString);
															betcode += GT
																	.betCodeToString(
																			1,
																			iZhushu,
																			"00",
																			1,
																			iTempRand)
																	+ ",";
														}
														sendNo = phoneVector.size();
														// betcode+=GT.betCodeToString(0,
														// iZhushu, "00", 1,
														// getRandromNums("ssq",iZhushu));
													System.out
															.println("-----------betcodfc3djixuan==="
																	+ betcode);
													}
													playName = "福彩3D";
													// betcode=GT.betCodeToString(1,
													// iZhushu, "00", 1,
													// randomNums);
													PublicMethod
															.myOutput("-----------betcodessq==="
																	+ betcode);
													isGenerate = true;
												} else if ("qlcJixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															int[][] iTempRand = getRandromNums(
																	"qlc", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															iNumbers
																	.add(iTempRandString);
															// 投注注码和赠送注码一致 陈晨
															// 20100728
															betcode += GT
																	.betCodeToString(
																			2,
																			iZhushu,
																			"00",
																			1,
																			iTempRand)
																	+ ",";
														}
														// betcode+=GT.betCodeToString(0,
														// iZhushu, "00", 1,
														// getRandromNums("ssq",iZhushu));
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
														sendNo = phoneVector.size() + 1;
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															int[][] iTempRand = getRandromNums(
																	"qlc", iZhushu);
															String iTempRandString = getRandomString(iTempRand);
															iNumbers
																	.add(iTempRandString);
															// 投注注码和赠送注码一致 陈晨
															// 20100728
															betcode += GT
																	.betCodeToString(
																			2,
																			iZhushu,
																			"00",
																			1,
																			iTempRand)
																	+ ",";
														}
														sendNo = phoneVector.size();
														// betcode+=GT.betCodeToString(0,
														// iZhushu, "00", 1,
														// getRandromNums("ssq",iZhushu));
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													}
												playName = "七乐彩";
												// betcode=GT.betCodeToString(2,
												// iZhushu, "00", 1, randomNums);
												System.out.println("-----------betcodeQlcjixuan==="
																+ betcode);
												isGenerate = true;
												}else if ("pl3Jixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨  大乐透机选一注
													iBeishu=1;
													int[][] iTempRand = getRandromNums(
															"pl3", iZhushu);
													String iTempRandString = getRandomString(iTempRand);
													iNumbers
															.add(iTempRandString);
													betcode += GT
													.betCodeToStringTC(
															3,
															//														
															iTempRand);
													
	//												if (!phoneET.getText()
	//														.toString().equals("")) {
	//													for (int i = 0; i < phoneVector
	//															.size() ; i++) {//+ 1
	//														int[][] iTempRand = getRandromNums(
	//																"pl3", iZhushu);
	//														String iTempRandString = getRandomString(iTempRand);
	//														iNumbers
	//																.add(iTempRandString);
	//														// 投注注码和赠送注码一致 陈晨
	//														// 20100728
	//														betcode += GT
	//																.betCodeToStringTC(
	//																		3,
	//																		//														
	//																		iTempRand)
	//																+ ";";
	//													}
	//													int[][] iTempRand = getRandromNums(
	//															"pl3", iZhushu);
	//													String iTempRandString = getRandomString(iTempRand);
	//													iNumbers
	//															.add(iTempRandString);
	//													// 投注注码和赠送注码一致 陈晨
	//													// 20100728
	//													betcode += GT
	//															.betCodeToStringTC(
	//																	3,
	//																	//														
	//																	iTempRand);
	//													// betcode+=GT.betCodeToString(0,
	//													// iZhushu, "00", 1,
	//													// getRandromNums("ssq",iZhushu));
	//													PublicMethod
	//															.myOutput("-----------betcodepl3==="
	//																	+ betcode);
	//													System.out
	//															.println("----betcode--------"
	//																	+ betcode);
	//
	//													sendNo = phoneVector.size() + 1;
	//												} else {
	//													for (int i = 0; i < phoneVector
	//															.size()-1; i++) {
	//														int[][] iTempRand = getRandromNums(
	//																"fc3d", iZhushu);
	//														String iTempRandString = getRandomString(iTempRand);
	//														iNumbers
	//																.add(iTempRandString);
	//														betcode += GT
	//																.betCodeToStringTC(
	//																		3,
	//																		iTempRand)
	//																+ ";";
	//													}
	//													int[][] iTempRand = getRandromNums(
	//															"fc3d", iZhushu);
	//													String iTempRandString = getRandomString(iTempRand);
	//													iNumbers
	//															.add(iTempRandString);
	//													betcode += GT
	//															.betCodeToStringTC(
	//																	3,
	//																	iTempRand);
	//													sendNo = phoneVector.size();
	//													// betcode+=GT.betCodeToString(0,
	//													// iZhushu, "00", 1,
	//													// getRandromNums("ssq",iZhushu));
	//													PublicMethod
	//															.myOutput("-----------betcodepl3==="
	//																	+ betcode);
	//												}
													
													playName = "排列三";
													// betcode=GT.betCodeToString(1,
													// iZhushu, "00", 1,
													// randomNums);
													PublicMethod
															.myOutput("-----------betcodepl3==="
																	+ betcode);
													isGenerate = false;
													iFushi=false;
												} else if ("dltJixuan".equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													iBeishu=1;
													
													int[][] iTempRand = getRandromNums(
															"dlt", iZhushu);
													String iTempRandString = getRandomString(iTempRand);
													System.out
															.println("---------iTempRandString----------"
																	+ iTempRandString);
													iNumbers
															.add(iTempRandString);
													betcode += GT
															.betCodeToStringTC(
																	4,
																	iTempRand);
	
	//												if (!phoneET.getText()
	//														.toString().equals("")) {
	//													PublicMethod
	//															.myOutput("***********"
	//																	+ phoneVector
	//																			.size());
	//
	//													for (int i = 0; i < phoneVector
	//															.size() ; i++) {//+ 1
	//														int[][] iTempRand = getRandromNums(
	//																"dlt", iZhushu);
	//														String iTempRandString = getRandomString(iTempRand);
	//														System.out
	//																.println("---------iTempRandString----------"
	//																		+ iTempRandString);
	//														iNumbers
	//																.add(iTempRandString);
	//														betcode += GT
	//																.betCodeToStringTC(
	//																		4,
	//																		iTempRand)
	//																+ ";";
	//
	//													}
	//													int[][] iTempRand = getRandromNums(
	//															"dlt", iZhushu);
	//													String iTempRandString = getRandomString(iTempRand);
	//													System.out
	//															.println("---------iTempRandString----------"
	//																	+ iTempRandString);
	//													iNumbers
	//															.add(iTempRandString);
	//													betcode += GT
	//															.betCodeToStringTC(
	//																	4,
	//																	iTempRand);
	//													PublicMethod
	//															.myOutput("-----------betcodedlt==="
	//																	+ betcode);
	//													sendNo = phoneVector.size() + 1;
	//												} else {
	//													for (int i = 0; i < phoneVector
	//															.size()-1; i++) {
	//														int[][] iTempRand = getRandromNums(
	//																"dlt", iZhushu);
	//														String iTempRandString = getRandomString(iTempRand);
	//														iNumbers
	//																.add(iTempRandString);
	//														betcode += GT
	//																.betCodeToStringTC(
	//																		4,
	//																		iTempRand)
	//																+ ";";
	//													}
	//													int[][] iTempRand = getRandromNums(
	//															"dlt", iZhushu);
	//													String iTempRandString = getRandomString(iTempRand);
	//													iNumbers
	//															.add(iTempRandString);
	//													betcode += GT
	//															.betCodeToStringTC(
	//																	4,
	//																	iTempRand);
	//													// betcode+=GT.betCodeToString(0,
	//													// iZhushu, "00", 1,
	//													// getRandromNums("ssq",iZhushu));
	//													PublicMethod
	//															.myOutput("-----------betcodedlt==="
	//																	+ betcode);
	//													sendNo = phoneVector.size();
	//												}
	
													playName = "超级大乐透";
													isGenerate = false;
													iFushi=false;
												} else if ("ssqZixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															// iNumbers.add(zhumaZiXuan("F47104",iBeishu,0));
															betcode += zhumaZiXuan(
																	"F47104",
																	iBeishu, 0)
																	+ ",";
														}
														sendNo = phoneVector.size() + 1;
														// betcode+=zhumaZiXuan("F47104",iBeishu,0);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															// iNumbers.add(zhumaZiXuan("F47104",iBeishu,0));
															betcode += zhumaZiXuan(
																	"F47104",
																	iBeishu, 0)
																	+ ",";
														}
														sendNo = phoneVector.size();
														// betcode+=zhumaZiXuan("F47104",iBeishu,0);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													}
													// betcode=zhumaZiXuan("F47104",iBeishu,0);
													playName = "双色球";
													PublicMethod
															.myOutput("-----------betcodessqzixuan==="
																	+ betcode);
													isGenerate = false;
												} else if ("fc3dZixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															// iNumbers.add(zhumaZiXuan("F47103",iBeishu,iZhushu));
															betcode += zhumaZiXuan(
																	"F47103",
																	iBeishu,
																	iZhushu)
																	+ ",";
														}
														sendNo = phoneVector.size() + 1;
														// betcode+=zhumaZiXuan("F47103",iBeishu,iZhushu);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															// iNumbers.add(zhumaZiXuan("F47103",iBeishu,iZhushu));
															System.out
																	.println("-----iZhushu--------"
																			+ iZhushu);
															betcode += zhumaZiXuan(
																	"F47103",
																	iBeishu,
																	iZhushu)
																	+ ",";
														}
														sendNo = phoneVector.size();
														// betcode+=zhumaZiXuan("F47103",iBeishu,iZhushu);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													}
													playName = "福彩3D";
													isGenerate = false;
													// betcode=zhumaZiXuan("F47103",iBeishu,iZhushu);
													PublicMethod
															.myOutput("-----------betcodefc3dzixuan==="
																	+ betcode);
												} else if ("qlcZixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													if (!phoneET.getText()
															.toString().equals("")) {
														for (int i = 0; i < phoneVector
																.size() + 1; i++) {
															System.out
																	.println("-----iZhushuqlc-------"
																			+ iZhushu);
															betcode += zhumaZiXuan(
																	"F47102",
																	iBeishu,
																	iZhushu)
																	+ ",";
														}
														sendNo = phoneVector.size() + 1;
														// betcode+=zhumaZiXuan("F47102",iBeishu,iZhushu);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													} else {
														for (int i = 0; i < phoneVector
																.size(); i++) {
															betcode += zhumaZiXuan(
																	"F47102",
																	iBeishu,
																	iZhushu)
																	+ ",";
														}
														sendNo = phoneVector.size();
														// betcode+=zhumaZiXuan("F47102",iBeishu,iZhushu);
														PublicMethod
																.myOutput("-----------betcodessq==="
																		+ betcode);
													}
													playName = "七乐彩";
													isGenerate = false;
													// betcode=zhumaZiXuan("F47102",iBeishu,iZhushu);
													PublicMethod
															.myOutput("-----------betcodeqlczixuan==="
																	+ betcode);
												}
	//											cc 8.25
												else if ("pl3Zixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													betcode += zhumaZiXuan(
															"T01002",
															iBeishu,
															iZhushu);
	//												if (!phoneET.getText()
	//														.toString().equals("")) {
	//													for (int i = 0; i < phoneVector
	//															.size() + 1; i++) {
	//														// iNumbers.add(zhumaZiXuan("F47103",iBeishu,iZhushu));
	//														betcode += zhumaZiXuan(
	//																"T01002",
	//																iBeishu,
	//																iZhushu);
	//													}
	//													sendNo = phoneVector.size() + 1;
	//													// betcode+=zhumaZiXuan("F47103",iBeishu,iZhushu);
	//													PublicMethod
	//															.myOutput("-----------betcodepl3==="
	//																	+ betcode);
	//												} else {
	//													for (int i = 0; i < phoneVector
	//															.size(); i++) {
	//														// iNumbers.add(zhumaZiXuan("F47103",iBeishu,iZhushu));
	//														System.out
	//																.println("-----iZhushu--------"
	//																		+ iZhushu);
	//														betcode += zhumaZiXuan(
	//																"T01002",
	//																iBeishu,
	//																iZhushu);
	//													}
	//													sendNo = phoneVector.size();
	//													// betcode+=zhumaZiXuan("F47103",iBeishu,iZhushu);
	//													PublicMethod
	//															.myOutput("-----------betcodepl3==="
	//																	+ betcode);
	//												}
													playName = "排列三";
													isGenerate = false;
													// betcode=zhumaZiXuan("F47103",iBeishu,iZhushu);
													PublicMethod
															.myOutput("-----------betcodePl3zixuan==="
																	+ betcode);
												}
												else if ("dltZixuan"
														.equals(successStr)) {
													// 赠送多人时产生多组注码 200/7/9陈晨
													betcode += zhumaZiXuan(
															"T01001",
															iBeishu, 0);
	//												if (!phoneET.getText()
	//														.toString().equals("")) {
	//													for (int i = 0; i < phoneVector
	//															.size() + 1; i++) {
	//														// iNumbers.add(zhumaZiXuan("F47104",iBeishu,0));
	//														betcode += zhumaZiXuan(
	//																"T01001",
	//																iBeishu, 0);
	//													}
	//													sendNo = phoneVector.size() + 1;
	//													// betcode+=zhumaZiXuan("F47104",iBeishu,0);
	//													PublicMethod
	//															.myOutput("-----------betcodedlt==="
	//																	+ betcode);
	//												} else {
	//													for (int i = 0; i < phoneVector
	//															.size(); i++) {
	//														// iNumbers.add(zhumaZiXuan("F47104",iBeishu,0));
	//														betcode += zhumaZiXuan(
	//																"T01001",
	//																iBeishu, 0);
	//													}
	//													sendNo = phoneVector.size();
	//													// betcode+=zhumaZiXuan("F47104",iBeishu,0);
	//													PublicMethod
	//															.myOutput("-----------betcodedlt==="
	//																	+ betcode);
	//												}
													// betcode=zhumaZiXuan("F47104",iBeishu,0);
													playName = "超级大乐透";
													PublicMethod
															.myOutput("-----------betcodedltzixuan==="
																	+ betcode);
													isGenerate = false;
											}
												// String re=jrtLot.gift(phonenum,
												// toPhoneNum,
												// "1512-F47104-00-05-0001031719222430~01^0001081115162030~05^0001020304162226~05^0001010305212228~10^0001010508192331~13^,1512-F47104-00-05-0001040912222426~09^0001030817222630~02^0001171924282930~01^0001030608171831~11^0001030415232429~07^,",
												// 2*iZhushu+"00", sessionid);
												if("ssqJixuan".equals(successStr)||"fc3dJixuan".equals(successStr)
														||"qlcJixuan".equals(successStr)||"ssqZixuan".equals(successStr)
														||"fc3dZixuan".equals(successStr)||"qlcZixuan".equals(successStr)){
													while (iRetrytimes < 3 && iRetrytimes > 0) {
												try {		
												aToPhoneNum=toPhoneNum;
												String re = jrtLot.gift(phonenum,
														toPhoneNum, betcode, 2
																* sendNo * iZhushu
																+ "00", sessionid);
												PublicMethod
														.myOutput("----------phonenum"
																+ toPhoneNum
																+ "------------"
																+ phoneET.getText()
																		.toString());
												obj = new JSONObject(re);
												String codeResponse1 = obj
												.getString("error_code");
												int index=0;
												for(int i=0 ;i<codeResponse1.length();i++){
													if(codeResponse1.charAt(i) == ','){
														index = i;
														i=codeResponse1.length();
													}else{
														index=codeResponse1.length();
													}
	
												}
											String codeResponse=codeResponse1.substring(0,index);
												// 获取返回码的前6个字符
												if (codeResponse.length() >= 6) {
													error_code = codeResponse
															.substring(0, 6);
												} else {
													// 增加返回码判断 陈晨 20100716
													error_code = codeResponse
															.substring(0, 4);
												}
	
												PublicMethod
														.myOutput("--------------error_code"
																+ error_code);
												iRetrytimes = 3;
											} catch (JSONException e) {
												e.printStackTrace();
												iRetrytimes--;
											}
										}
										if (iRetrytimes == 0 && iHttp.whetherChange == false) {
											iHttp.whetherChange = true;
											if (iHttp.conMethord == iHttp.CMWAP) {
												iHttp.conMethord = iHttp.CMNET;
											} else {
												iHttp.conMethord = iHttp.CMWAP;
											}
											iRetrytimes=2;
											while(iRetrytimes>0&&iRetrytimes<3){
												String re = jrtLot.gift(phonenum,
														aToPhoneNum, betcode, 2
																* sendNo * iZhushu
																+ "00", sessionid);
												PublicMethod
														.myOutput("----------aToPhoneNum"
																+ aToPhoneNum
																+ "------------"
																+ phoneET.getText()
																		.toString());
												
												try {
													obj = new JSONObject(re);
													String codeResponse1;
													String codeResponse;
													codeResponse1 = obj
															.getString("error_code");
													int index=0;
													for(int i=0 ;i<betcode.length();i++){
														if(codeResponse1.charAt(i) == ','){
															index = i;
															i=betcode.length();
														}
													}
												codeResponse=codeResponse1.substring(0,index);
												// 获取返回码的前6个字符
												if (codeResponse.length() >= 6) {
													error_code = codeResponse
															.substring(0, 6);
												} else {
													// 增加返回码判断 陈晨 20100716
													error_code = codeResponse
															.substring(0, 4);
												}
												iRetrytimes=3;
												} catch (JSONException e) {
													iRetrytimes--;
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
										iRetrytimes = 2;
										PublicMethod.myOutput("??????????"
												+ error_code);
										if (error_code.equalsIgnoreCase("000000")) {// equals("000000")
											// -----------怎样回到主列表
											try {
												batchCode = obj
														.getString("sell_term_code");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
	
											Message msg = new Message();
											msg.what = 0;// 成功
											handler.sendMessage(msg);
										} else if (error_code.equals("040006")) {
											Message msg = new Message();
											msg.what = 1;// 用户余额不足
											handler.sendMessage(msg);
	
										} else if (error_code.equals("040016")) {
											Message msg = new Message();
											msg.what = 2;// 扣款失败
											handler.sendMessage(msg);
	
										} else if (error_code.equals("070002")) {
											Message msg = new Message();
											msg.what = 3;// 用户未登录
											handler.sendMessage(msg);
	
										} else if (error_code.equals("001007")
												|| error_code.equals("1007")) {
											Message msg = new Message();
											msg.what = 6;// 期结
											handler.sendMessage(msg);
	
										} else if (error_code.equals("999999")) {
											Message msg = new Message();
											msg.what = 4;// 网络异常
											handler.sendMessage(msg);
	
										} else {
											Message msg = new Message();
											msg.what = 5;// 赠送失败
											handler.sendMessage(msg);
										}
										}else if("pl3Jixuan".equals(successStr)||"dltJixuan".equals(successStr)
												||"pl3Zixuan".equals(successStr)||"dltZixuan".equals(successStr)){
											String lotNo="";
											String error_code_DYJ="999999";
											 String re ;
											if("pl3Jixuan".equals(successStr)||"pl3Zixuan".equals(successStr)){
												lotNo="T01002";
											}else{
												lotNo="T01001";
											}
											while (iRetrytimes < 3 && iRetrytimes > 0) {
											try {		
												aToPhoneNum=toPhoneNum;
												if(iFushi==false){
													PublicMethod
													.myOutput("--------------danshi");
												  re = jrtLot.giftTC(phonenum, toPhoneNum, lotNo, betcode, iBeishu+"", 2*iZhushu+"00", "2", iZhushu+"",sessionid,messageET.getText().toString());//*sendNo//
												}else{
													PublicMethod
													.myOutput("--------------fushi");
													 re = jrtLot.giftTC(phonenum, toPhoneNum, lotNo, betcode, iBeishu+"", 2*iZhushu+"00", "2", "",sessionid,messageET.getText().toString());//*sendNo//
												}
												PublicMethod
														.myOutput("----------phonenum"
																+ toPhoneNum
																+ "------------"
																+ phoneET.getText()
																		.toString()+"---lotNo----"+lotNo+"---iBeishu-----"+iBeishu+"--amount--"+(2*iZhushu+"00")+"--iZhushu-----"+iZhushu+"---"+messageET.getText().toString());
												obj = new JSONObject(re);
												String codeResponse1 = obj
												.getString("error_code");
												error_code=codeResponse1;
												error_code_DYJ = obj.getString("error_code_DYJ");
												
	
												PublicMethod
														.myOutput("--------------error_code"
																+ error_code);
												iRetrytimes = 3;
											} catch (JSONException e) {
												if(error_code.equals("999999")&&obj!=null){
													try {
														error_code_DYJ = obj.getString("error_code_DYJ");
													} catch (JSONException e1) {
														e1.printStackTrace();
													}
												}
												if(error_code.equals("999999")&&error_code_DYJ.equals("999999")){
												  iRetrytimes--;
												}else{
													iRetrytimes=3;
												}
												
											}
										}
										if (iRetrytimes == 0 && iHttp.whetherChange == false) {
											iHttp.whetherChange = true;
											if (iHttp.conMethord == iHttp.CMWAP) {
												iHttp.conMethord = iHttp.CMNET;
											} else {
												iHttp.conMethord = iHttp.CMWAP;
											}
											iRetrytimes=2;
											while(iRetrytimes>0&&iRetrytimes<3){
												if(iFushi==false){
													  re = jrtLot.giftTC(phonenum, toPhoneNum, lotNo, betcode, iBeishu+"", 2*iZhushu+"00", "2", iZhushu+"",sessionid,messageET.getText().toString());//*sendNo//
													}else{
														 re = jrtLot.giftTC(phonenum, toPhoneNum, lotNo, betcode, iBeishu+"", 2*iZhushu+"00", "2", "",sessionid,messageET.getText().toString());//*sendNo//
													}
												PublicMethod
														.myOutput("----------aToPhoneNum"
																+ aToPhoneNum
																+ "------------"
																+ phoneET.getText()
																		.toString()+"-----------");
												
												try {
													obj = new JSONObject(re);
													String codeResponse1;
													String codeResponse;
													codeResponse1 = obj
															.getString("error_code");
													error_code=codeResponse1;
													error_code_DYJ = obj.getString("error_code_DYJ");
													
												iRetrytimes=3;
												} catch (JSONException e) {
													if(error_code.equals("999999")&&obj!=null){
														try {
															error_code_DYJ = obj.getString("error_code_DYJ");
														} catch (JSONException e1) {
															e1.printStackTrace();
														}
													}
													if(error_code.equals("999999")&&error_code_DYJ.equals("999999")){
													  iRetrytimes--;
													}else{
														iRetrytimes=3;
													}
												}
											}
	
										}
										iRetrytimes = 2;
										PublicMethod.myOutput("??????????"
												+ error_code);
										if (error_code.equalsIgnoreCase("0000")&&error_code_DYJ.equals("000001")||error_code_DYJ.equals("000000")) {// equals("000000")
											// -----------怎样回到主列表
	
											Message msg = new Message();
											msg.what = 7;// 成功
											handler.sendMessage(msg);
										} else if (error_code.equals("040006")||error_code.equals("201015")) {
											Message msg = new Message();
											msg.what = 1;// 用户余额不足
											handler.sendMessage(msg);
	
										} else if (error_code.equals("070002")) {
											Message msg = new Message();
											msg.what = 3;// 用户未登录
											handler.sendMessage(msg);
	
										} else if (error_code.equals("001007")
												|| error_code.equals("1007")||error_code_DYJ.equals("1006")) {
											Message msg = new Message();
											msg.what = 6;// 期结
											handler.sendMessage(msg);
	
										} else if(error_code_DYJ.equals("002002")){
											Message msg = new Message();
											msg.what = 8;// 投注中
											handler.sendMessage(msg);
											
										}else if (error_code.equals("999999")) {
											Message msg = new Message();
											msg.what = 4;// 网络异常
											handler.sendMessage(msg);
	
										} else {
											Message msg = new Message();
											msg.what = 5;// 赠送失败
											handler.sendMessage(msg);
										}
										}
									}
								});
								t.start();
	
							}
						}
					}
				}
			}
//			 }
		});

		cancelBtn = (Button) findViewById(R.id.ruyichuanqing_success_cancel_btn);
		cancelBtn.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});

	}

	/**
	 * 获得各个彩种相应注数的随机注码
	 * 
	 * @param lotteryType
	 *            彩种
	 * @param iZhushu
	 *            注数
	 * @return
	 */
	private int[][] getRandromNums(String lotteryType, int iZhushu) {
		int randomNumbers[][] = new int[1][7];
		// int [][] randomNumbers = new int[iZhushu][7];
		if ("ssq".equals(lotteryType)) {
			randomNumbers = new int[iZhushu][7];
			for (int i = 0; i < iZhushu; i++) {
				int[] redrandoms = sort(PublicMethod
						.getRandomsWithoutCollision(6, 1, 33));
				int[] bluerandoms = PublicMethod.getRandomsWithoutCollision(1,
						1, 16);
				for (int j = 0; j < 6; j++) {
					randomNumbers[i][j] = redrandoms[j];
				}
				randomNumbers[i][6] = bluerandoms[0];
			}
		}

		if ("fc3d".equals(lotteryType)) {
			randomNumbers = new int[iZhushu][3];
			for (int i = 0; i < iZhushu; i++) {
				int[] randoms = PublicMethod
						.getRandomsWithoutCollision(3, 0, 9);
				randomNumbers[i] = sort(randoms);
			}
		}

		if ("qlc".equals(lotteryType)) {
			randomNumbers = new int[iZhushu][7];
			for (int i = 0; i < iZhushu; i++) {
				int[] randoms = PublicMethod.getRandomsWithoutCollision(7, 1,
						30);
				randomNumbers[i] = sort(randoms);
			}
		}
		if("pl3".equals(lotteryType)){
			randomNumbers = new int[iZhushu][3];
			for (int i = 0; i < iZhushu; i++) {
				int[] randoms = PublicMethod
						.getRandomsWithoutCollision(3, 0, 9);
				randomNumbers[i] = sort(randoms);
			}
		}
		if("dlt".equals(lotteryType)){
			randomNumbers = new int[iZhushu][7];
			for (int i = 0; i < iZhushu; i++) {
				int[] redrandoms = sort(PublicMethod
						.getRandomsWithoutCollision(5, 1, 35));
				int[] bluerandoms = PublicMethod.getRandomsWithoutCollision(2,
						1, 12);
				for (int j = 0; j < 5; j++) {
					randomNumbers[i][j] = redrandoms[j];
				}
				randomNumbers[i][5] = bluerandoms[0];
				randomNumbers[i][6] = bluerandoms[1];
			}
		}

		return randomNumbers;
	}

	/**
	 * 数组排序
	 * 
	 * @param t
	 * @return
	 */
	public static int[] sort(int t[]) {
		int t_s[] = t;
		int temp;
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

	// wangyl 7.13 解决电话薄删除号码时总是删除最后一个的问题
	/**
	 * 添加或删除数据源
	 * 
	 * @param int iOperactionFlag 1为添加，2为删除
	 * @param int position 删除的位置
	 * @return List<Map<String, Object>> 返回数据源
	 */
	private List<Map<String, Object>> addOrDeleteResultForAdapter(
			int iOperactionFlag, String name, String phone, int position) {

		if (iOperactionFlag == 1) {
			// 如果不为空则加进去
			if (!phone.equals("")) {
				phoneVector.addElement(phone);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(NAME, name);
			map.put(PHONE, phone);
			map.put(DELETEBTN, "-");

			list.add(map);
		} else if (iOperactionFlag == 2) {
			// wangyl 7.13 以下注释可删除
			/*
			 * Map<String,Object> map = new HashMap<String, Object>();
			 * map.put(NAME,name); map.put(PHONE,phone); map.put(DELETEBTN,
			 * "-"); int i;
			 */

			// for(i=0;i<phoneVector.size();i++)
			// PublicMethod.myOutput("---------"+i+" "+(String)phoneVector.elementAt(i));
			// PublicMethod.myOutput("---------***"+phone);
			// phoneVector.removeElement(phone);

			// wangyl 7.13 以下注释可删除
			// for(i=0;i<phoneVector.size();i++)
			// PublicMethod.myOutput("---------"+i+" "+(String)phoneVector.elementAt(i));
			/*
			 * if(list.size()>0){ for(i=0;i<list.size();i++){
			 * if(list.get(i).get(PHONE)==map.get(PHONE)){ list.remove(i); } } }
			 */

			// wangyl 7.13 解决电话薄删除号码时总是删除最后一个的问题
			phone = list.get(position - 100).get("PHONE").toString();
			phoneVector.removeElement(phone);

			list.remove(position - 100);
		}
		listSize = list.size();
		return list;
	}

	/**
	 * 设置适配器和监听器
	 */
	// 王艳玲 7.8 修改电话薄
	private void setListViewAdapter(List<Map<String, Object>> resultList) {

		phoneET.setText("");

		adapter = new RuyiExpressFeelSuccessEfficientAdapter(this, resultList);
		phoneListView.setAdapter(adapter);

	}

	/**
	 * 适配器
	 */
	// 王艳玲 7.8 修改电话薄
	public class RuyiExpressFeelSuccessEfficientAdapter extends BaseAdapter {

		private LayoutInflater mInflater; // 扩充主列表布局
		Context context;
		int listSize;
		List<Map<String, Object>> resultList;

		public RuyiExpressFeelSuccessEfficientAdapter(Context context,
				List<Map<String, Object>> resultList) {
			mInflater = LayoutInflater.from(context);
			this.context = context;
			this.listSize = resultList.size();
			this.resultList = resultList;
		}

		@Override
		public int getCount() {
			return listSize;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		/**
		 * 设置主列表布局中的详细内容
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			iPosition = position;
			// 与布局中的信息关联起来
			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.ruyichuanqing_listview_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.ruyichuanqing_listview_item_name);
				holder.phone = (TextView) convertView
						.findViewById(R.id.ruyichuanqing_listview_item_phone);
				holder.name.setText(list.get(position).get(NAME).toString());
				holder.name.setTextSize(16);
				holder.phone.setText(list.get(position).get(PHONE).toString());
				holder.phone.setTextSize(16);
				holderphone = holder.phone.getText().toString();
				// 设置按钮
				holder.iButtonGroupLayout = (LinearLayout) convertView
						.findViewById(R.id.ruyichuanqing_button_group);
				holder.iButtonGroupLayout.setId(position + LAYOUT_INDEX);

				buttonGroup = new LinearLayout(context);
				buttonGroup.setOrientation(LinearLayout.VERTICAL);

				Button btn = new Button(context);
				btn.setText("-");
				btn.setId(position + 100);
				buttonGroup.addView(btn);

				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 删除本行
						// wangyl 7.13 解决电话薄删除号码时总是删除最后一个的问题
						list = addOrDeleteResultForAdapter(2, "", holderphone,
								arg0.getId());
						setListViewAdapter(list);
					}
				});

				holder.iButtonGroupLayout.addView(buttonGroup);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			return convertView;
		}

		class ViewHolder {
			TextView name;
			TextView phone;
			LinearLayout iButtonGroupLayout;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PICK_CONTACT_SUBACTIVITY:
			if (data == null)
				break;
			final Uri uriRet = data.getData();
			if (uriRet != null) {
				try {
					Cursor c = managedQuery(uriRet, null, null, null, null);
					c.moveToFirst();
					String strName = c.getString(c
							.getColumnIndexOrThrow(People.NAME));
					String strPhone = c.getString(c
							.getColumnIndexOrThrow(People.NUMBER));
					if (strPhone == null) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								RuyiExpressFeelSuccess.this);
						builder.setTitle("无手机号码");
						builder.setMessage("该联系人无手机号码");
						// 确定
						builder.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}

								});
						builder.show();
					} else {
						// phoneET.setText(strName);
						// wangyl 7.14 为了限制手机号码长度 手机号码：可输入11位数字；
						phoneET.setText(strPhone);
						phoneString = strPhone;
						nameString = strName;
					}

				} catch (Exception e) {

				}
			}
			break;
		}

	}

	// 自选注码
	String typeCode;// 玩法标识

	/**
	 * 赠送彩票的自选注码格式
	 * 
	 * @param playname
	 *            彩种
	 * @param beishu
	 *            倍数
	 * @param zhushu
	 *            注数
	 */
	public String zhumaZiXuan(String playname, int beishu, int zhushu) {
		PublicMethod.myOutput("?????????" + beishu + "????????" + zhushu);
		String str = "";
		String iBallNum = "";

		if (playname.equals("F47104")) {
			str += "1512-F47104-";
			if (redBallNums.length == 6 && blueBallNums.length == 1) {
				str += "00-01-00";
			} else if (redBallNums.length > 6 && blueBallNums.length == 1) {
				str += "10-01-10";
			} else if (redBallNums.length == 6 && blueBallNums.length > 1) {
				str += "20-01-20";
			} else if (redBallNums.length > 6 && blueBallNums.length > 1) {
				str += "30-01-30";
			}
			if (beishu < 10) {
				str += "0" + beishu;
			} else if (beishu >= 10) {
				str += beishu;
			}
			if (redBallNums.length > 6 || blueBallNums.length > 1) {
				str += "*";
			}
			iBallNum = "红球：";
			for (int i = 0; i < redBallNums.length; i++) {
				if (redBallNums[i] < 10) {
					str += "0" + redBallNums[i];
				} else if (redBallNums[i] >= 10) {
					str += redBallNums[i];
				}

				iBallNum += redBallNums[i] + ",";
				// iNumbers.add(redBallNums[i]);

			}
			str += "~";
			iBallNum += "蓝球:";
			for (int i = 0; i < blueBallNums.length; i++) {
				if (blueBallNums[i] < 10) {
					str += "0" + blueBallNums[i];
				} else if (blueBallNums[i] >= 10) {
					str += blueBallNums[i];
				}
				iBallNum += blueBallNums[i] + ",";
			}
			iNumbers.add(iBallNum);
			str += "^";
		} else if (playname.equals("F47103")) {
			str += "1512-F47103-";
			String[] zhuma = null;
			if (baiBallNums.length == 1 && shiBallNums.length == 1
					&& geBallNums.length == 1) {
				typeCode = "00";

				String[] str_ = { "0" + (baiBallNums[0] - 1),
						"0" + (shiBallNums[0] - 1), "0" + (geBallNums[0] - 1) };
				iBallNum = "百位：";
				iBallNum += (baiBallNums[0] - 1);
				// =改为+= 陈晨 20100730
				iBallNum += "十位：";
				iBallNum += (shiBallNums[0] - 1);
				// =改为+= 陈晨 20100730
				iBallNum += "个位：";
				iBallNum += (geBallNums[0] - 1);
				iNumbers.add(iBallNum);
				// iNumbers.add((baiBallNums[0]-1));
				// iNumbers.add((shiBallNums[0]-1));
				// iNumbers.add((geBallNums[0]-1));

				zhuma = str_;
			} else {
				// 3D直选复式注码玩法 2010/7/4 陈晨
				if (baiBallNums.length != 0 && shiBallNums.length != 0
						&& geBallNums.length != 0) {
					typeCode = "20";
					String[] str_fu = new String[baiBallNums.length
							+ shiBallNums.length + geBallNums.length + 5];
					if (baiBallNums.length < 10) {
						str_fu[0] = "0" + baiBallNums.length;
					} else {
						str_fu[0] = baiBallNums.length + "";
					}
					iBallNum = "百位：";
					for (int i = 0; i < baiBallNums.length; i++) {
						str_fu[i + 1] = "0" + (baiBallNums[i] - 1);
						iBallNum += (baiBallNums[i] - 1) + ",";
						// iNumbers.add((baiBallNums[i]-1));
					}
					str_fu[baiBallNums.length + 1] = "^";

					if (shiBallNums.length < 10) {
						str_fu[baiBallNums.length + 2] = "0"
								+ shiBallNums.length;
					} else {
						str_fu[baiBallNums.length + 2] = shiBallNums.length
								+ "";
					}
					// =改为+= 陈晨 20100730
					iBallNum += "十位：";
					for (int i = 0; i < shiBallNums.length; i++) {
						str_fu[baiBallNums.length + 3 + i] = "0"
								+ (shiBallNums[i] - 1);
						iBallNum += (shiBallNums[i] - 1) + ",";
						// iNumbers.add((shiBallNums[i]-1));
					}
					str_fu[baiBallNums.length + shiBallNums.length + 3] = "^";
					if (geBallNums.length < 10) {
						str_fu[baiBallNums.length + shiBallNums.length + 4] = "0"
								+ geBallNums.length;
					} else {
						str_fu[baiBallNums.length + shiBallNums.length + 4] = geBallNums.length
								+ "";
					}
					// =改为+= 陈晨 20100730
					iBallNum += "个位：";
					for (int i = 0; i < geBallNums.length; i++) {
						str_fu[baiBallNums.length + shiBallNums.length + 5 + i] = "0"
								+ (geBallNums[i] - 1);
						iBallNum += (geBallNums[i] - 1) + ",";
						// iNumbers.add((geBallNums[i]-1));
					}
					zhuma = str_fu;
				}
				iNumbers.add(iBallNum);

				// }
			}
			String beishu_ = "";
			if (beishu < 10) {
				beishu_ += "0" + beishu;
				PublicMethod.myOutput("?????????beishu" + beishu_);
			} else if (beishu >= 10) {
				beishu_ += "" + beishu;
			}
			String zhushu_ = "";
			if (zhushu < 10) {
				zhushu_ += "0" + zhushu;
				PublicMethod.myOutput("?????????zhushu_" + zhushu_);
			} else if (zhushu >= 10) {
				zhushu_ += "" + zhushu;
			}

			if (baiBallNums.length == 1 && shiBallNums.length == 1
					&& geBallNums.length == 1) {
				str += typeCode + "-" + beishu_ + "-" + typeCode + beishu_;
			} else {
				str += typeCode + "-" + zhushu_ + "-" + typeCode + beishu_;
			}
			for (int i = 0; i < zhuma.length; i++) {
				str += zhuma[i];
			}
			str += "^";
		} else if (playname.equals("F47102")) {
			str += "1512-F47102-";
			if (qlcBallNums.length == 7) {
				typeCode = "00";

			} else if (qlcBallNums.length > 7) {
				typeCode = "10";
			}
			String zhushu_ = "";
			if (zhushu < 10) {
				zhushu_ += "0" + zhushu;
			} else if (zhushu >= 10) {
				zhushu_ += "" + zhushu;
			}

			String beishu_ = "";
			if (beishu < 10) {
				beishu_ += "0" + beishu;
			} else if (beishu >= 10) {
				beishu_ += "" + beishu;
			}
			if (qlcBallNums.length == 7) {
				str += typeCode + "-" + zhushu_ + "-" + typeCode + beishu_;
			} else if (qlcBallNums.length > 7) {
				str += typeCode + "-" + zhushu_ + "-" + typeCode + beishu_
						+ "*";
			}
			for (int i = 0; i < qlcBallNums.length; i++) {
				if (qlcBallNums[i] < 10) {
					str += "0" + qlcBallNums[i];
				} else if (qlcBallNums[i] >= 10) {
					str += qlcBallNums[i];
				}
				// 七乐彩注码不需要”-1“去掉，陈晨 20100730
				iBallNum += qlcBallNums[i] + ",";
				// iNumbers.add(qlcBallNums[i]);
			}
			iNumbers.add(iBallNum);
			str += "^";
		}else if(playname.equals("T01002")){
			if(baiBallNumsPl3.length>1||shiBallNumsPl3.length>1||geBallNumsPl3.length>1){
				iFushi=true;
			}else{
				iFushi=false;
			}
			iBallNum = "百位：";
			str+="1|";
			for(int i=0;i<baiBallNumsPl3.length;i++){
				str += (baiBallNumsPl3[i]-1)+"";
				iBallNum+=(baiBallNumsPl3[i]-1)+",";
			}
			iBallNum += "十位：";
			str+=",";
			for(int i=0;i<shiBallNumsPl3.length;i++){
				str += (shiBallNumsPl3[i]-1)+"";
				iBallNum+=(shiBallNumsPl3[i]-1)+",";
			}
			str+=",";	
			iBallNum += "个位：";
			for(int i=0;i<geBallNumsPl3.length;i++){
				str += (geBallNumsPl3[i]-1)+"";
				iBallNum+=(geBallNumsPl3[i]-1)+",";
			}
			iNumbers.add(iBallNum);
			
		}else if(playname.equals("T01001")){
			if(redBallNumsDlt.length==5&&blueBallNumsDlt.length==2){
				iFushi=false;
			}else{
				iFushi=true;
			}
			
			iBallNum = "前区：";
			for (int i = 0; i < redBallNumsDlt.length-1; i++) {
				if (redBallNumsDlt[i] < 10) {
					str += "0" + redBallNumsDlt[i]+" ";
				} else if (redBallNumsDlt[i] >= 10) {
					str += redBallNumsDlt[i]+" ";
				}

				iBallNum += redBallNumsDlt[i] + ",";
				// iNumbers.add(redBallNums[i]);

			}
			if (redBallNumsDlt[redBallNumsDlt.length-1] < 10) {
				str += "0" + redBallNumsDlt[redBallNumsDlt.length-1];
			} else if (redBallNumsDlt[redBallNumsDlt.length-1] >= 10) {
				str += redBallNumsDlt[redBallNumsDlt.length-1];
			}

			iBallNum += redBallNumsDlt[redBallNumsDlt.length-1] + ",";
			
			str+="-";
			iBallNum += "后区:";
			for (int i = 0; i < blueBallNumsDlt.length-1; i++) {
				if (blueBallNumsDlt[i] < 10) {
					str += "0" + blueBallNumsDlt[i]+" ";
				} else if (blueBallNumsDlt[i] >= 10) {
					str += blueBallNumsDlt[i]+" ";
				}
				iBallNum += blueBallNumsDlt[i] + ",";
			}
			if (blueBallNumsDlt[blueBallNumsDlt.length-1] < 10) {
				str += "0" + blueBallNumsDlt[blueBallNumsDlt.length-1];
			} else if (blueBallNumsDlt[blueBallNumsDlt.length-1] >= 10) {
				str += blueBallNumsDlt[blueBallNumsDlt.length-1];
			}
			iBallNum += blueBallNumsDlt[blueBallNumsDlt.length-1] + ",";
			iNumbers.add(iBallNum);
		}
		// }
		return str;
	}
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
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
