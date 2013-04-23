package com.ruyicai.activity.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.handler.MyDialogListener;
import com.ruyicai.net.JrtLot;
import com.ruyicai.net.transaction.ChargeInterface;
import com.ruyicai.net.transaction.GetUserInfoInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

//充值
public class AccountRecharge extends Activity implements MyDialogListener {
	/** Called when the activity is first created. */
	private static final int BANK_CARD_PHONE_DIALOG = 1;
	private static final int PHONE_CARD_RECHARGE_DIALOG = 2;
	private static final int ZFB_RECHARGE_DIALOG = 3;
	private static final int PHONE_BANK_RECHARGE_DIALOG = 4;
	private static final int GAME_CARD_DIALOG = 5;
	private static final int COMPUTER_NET_RECHARGE_DIALOG = 6;
	private static final int BANK_CARD_PHONE_NO_DIALOG = 9;

	// 语音充值注册对话框
	private static final int BANK_PHONE_CARD_REGISTER_DIALOG = 8;
	private static final int PROGRESS_VALUE = 7;
	ProgressDialog progressDialog;

	List<Map<String, Object>> list;/* 列表适配器的数据源 */

	public final static String TITLE = "TITLE"; /* 标题 */
	public final static String BACK = "TITLE"; /* 背景 */
	private static final String IICON = "IICON";/* 箭头 */
	public static TextView text;
	// AccountRechargeDialog accountRechargeDialog;
	private String error_code;
	private String game_card_number_string;
	private String game_card_password_string;
	// private String game_card_recharge_value_string; 删掉 陈晨 20100913
	private String game_card_total_value_string;

	private String bankType = "CMBCHINA-WAP";
	private String bank_card_id;
	String phonenum;
	String sessionid;
	int iretrytimes = 2;
	String re;
	JSONObject obj;
	// private String phoneCardType;	
	// private String gameCardType;

	private String phoneCardType = "0206";
	private String phoneCardValue = "100";
	private String gameCardType = "0204";
	// 移动充值卡
	private final String YIDONG = "0203";
	// 联通充值卡
	private final String LIANTONG = "0206";
	// 电信充值卡
	private final String DIANXIN = "0221";
	// 征途卡
	private final String ZHENGTU = "0204";
	// 骏网一卡通
	private final String JUNWANG = "0201";
	// 盛大卡
	private final String SHENGDA = "0202";

	// ebay
	private final String EBAY = "y00003";
	// 支付宝
	// private final String Aplipay = ""

	// 招商银行
	private final String ZHAOSHANG = "01";
	// 建设银行
	private final String JIANSHE = "0102";
	// 工商银行
	private final String GONGSHANG = "0103";

	// 接入方式
	private String strAccesstype;
	// ebay支付方式
	private String strCardType = EBAY;
	// 银行标识
	private String strBankId = "";
	// 扩展参数
	private String strExpand = "";
	String bankCardNo = "";

	String url;
	public static boolean flag = false;
	EditText bank_card_phone_bankid;
	EditText bank_card_phone_phone_num;
	EditText bank_card_phone_name;
	ShellRWSharesPreferences shellRW;
	// String sessionIdStr; //周黎鸣 7.4 代码修改：添加登陆的代码
	// 处理请求支付返回的信息
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				alert("信息不能为空！");
				break;

			case 2:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "卡号或密码错误！", Toast.LENGTH_LONG).show();

				break;
			case 6:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				// //需要添加AlertDialog提示用户登录成功
				Toast.makeText(getBaseContext(), "充值请求已受理，请稍后查询余额。",Toast.LENGTH_LONG).show();
				break;
			case 7:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				// 30分钟后再次登录 陈晨 20100719
				Intent intentSession = new Intent(AccountRecharge.this,UserLogin.class);
				startActivityForResult(intentSession, 0);
				
				break;
			case 8:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "充值失败！", Toast.LENGTH_LONG)
						.show();
				break;
			// 新添的处理消息 2010/7/9陈晨
			case 10:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "请求已提交！", Toast.LENGTH_LONG).show();
				break;
			case 11:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				// 显示语音注册对话框
				showDialog(BANK_PHONE_CARD_REGISTER_DIALOG);
				Toast.makeText(getBaseContext(), "请提交详细信息！", Toast.LENGTH_LONG).show();
				break;
			case 12:
				// 取消联网对话框 2010/7/9 陈晨
				progressDialog.dismiss();
				PublicMethod.openUrlByString(AccountRecharge.this, url);
				break;
			case 13:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "暂不支持的卡号！", Toast.LENGTH_LONG).show();
				break;
			case 14:
				progressDialog.dismiss();// 未登录
				Intent intent1 = new Intent(UserLogin.UNSUCCESS);
				sendBroadcast(intent1);
				ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(AccountRecharge.this, "addInfo");
				shellRW.setUserLoginInfo("sessionid", "");
				Intent intent2 = new Intent(AccountRecharge.this,UserLogin.class);
				startActivity(intent2);
				break;
			case 15:
				progressDialog.dismiss();
				Toast.makeText(AccountRecharge.this, "系统结算！",Toast.LENGTH_SHORT).show();
				break;
			case 16:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常！", Toast.LENGTH_LONG).show();
				break;
			case 17:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "查询失败！", Toast.LENGTH_LONG).show();
				break;
			case 18:
				progressDialog.dismiss();
				showUserCenterBalanceInquiry();
				break;
			case 19:
				progressDialog.dismiss();
				dialogDNA(re);
				break;
			case 20:
				progressDialog.dismiss();
				showDialog(BANK_CARD_PHONE_NO_DIALOG); // 未绑定
				break;
			}
		}
	};

	// 周黎鸣 7.5 代码修改：添加退出检测的代码————标记

	// 周黎鸣 7.5 代码修改：添加退出检测
	public void onCancelClick() {
		// iCallOnKeyDownFlag=false;
	}

	// 周黎鸣 7.5 代码修改：添加退出检测
	public void onOkClick(int[] nums) {
		// 退出
		this.finish();
	}

	public void showAccountRechargeDialog(int i) {
		Dialog dialog = onCreateDialog(i);
		dialog.show();
		dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	protected Dialog onCreateDialog(int i) {
		/*
		 * //周黎鸣 7.4 代码修改：添加登陆的代码 ShellRWSharesPreferences pre = new
		 * ShellRWSharesPreferences(AccountRecharge.this , "addInfo");
		 * sessionIdStr = pre.getUserLoginInfo("sessionid");
		 */
		LayoutInflater factory = LayoutInflater.from(this);
		switch (i) {
		case BANK_CARD_PHONE_DIALOG:
			final View bank_card_phone_online_view = factory.inflate(
					R.layout.bank_card_phone_online_dialog, null);
			bank_card_phone_bankid = (EditText) bank_card_phone_online_view
					.findViewById(R.id.bank_card_phone_bankid);

			bank_card_phone_bankid.setText(bankCardNo);
			bank_card_phone_bankid.setEnabled(false);
			return new AlertDialog.Builder(this).setTitle(
					R.string.bank_cards_recharge).setView(
					bank_card_phone_online_view).setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 周黎鸣 7.4 代码修改：添加登陆的判断
							ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
									AccountRecharge.this, "addInfo");
							String sessionIdStr = pre
									.getUserLoginInfo("sessionid");
							if (sessionIdStr == null || sessionIdStr.equals("")) {
								Intent intentSession = new Intent(
										AccountRecharge.this, UserLogin.class);
								startActivity(intentSession);
							} else {
								// 银行卡语音充值网络连接
								beiginBankCardPhoneOnline(bank_card_phone_online_view);
							}

							/* User clicked OK so do some stuff */
						}
					}).setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked cancel so do some stuff */
						}
					}).create();
		case BANK_CARD_PHONE_NO_DIALOG:// 银行卡电话卡充值(未绑定)
			final View bank_card_phone_view = factory.inflate(R.layout.bank_card_phone_dialog, null);
			String phonenum = shellRW.getUserLoginInfo("phonenum");
			String name = shellRW.getUserLoginInfo("name");
		    bank_card_phone_phone_num = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_num);// 手机号
			bank_card_phone_name = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_name);// 姓名
			bank_card_phone_phone_num.setText(phonenum);
			bank_card_phone_name.setText(name);
			return new AlertDialog.Builder(this).setTitle(
					R.string.bank_cards_recharge).setView(bank_card_phone_view)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									// 周黎鸣 7.4 代码修改：添加登陆的判断
									ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
											AccountRecharge.this, "addInfo");
									String sessionIdStr = pre
											.getUserLoginInfo("sessionid");
									if (sessionIdStr == null
											|| sessionIdStr.equals("")) {
										Intent intentSession = new Intent(
												AccountRecharge.this,
												UserLogin.class);
										startActivity(intentSession);
									} else {
										// 银行卡语音充值网络连接
										beiginBankCardPhoneNo(bank_card_phone_view);
									}

									/* User clicked OK so do some stuff */
								}
							}).setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();
		case PHONE_CARD_RECHARGE_DIALOG:

			final View phone_card_recharg_view = factory.inflate(
					R.layout.phone_cards_recharge_dialog, null);
			final Spinner phone_card_spinner = (Spinner) phone_card_recharg_view
					.findViewById(R.id.phone_card_spinner);
			phone_card_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							// 点击下拉框。。。
							int position = phone_card_spinner
									.getSelectedItemPosition();
							PublicMethod.myOutput("*********position "
									+ position);
							if (position == 0) {
								phoneCardType = YIDONG;
							} else if (position == 1) {
								phoneCardType = LIANTONG;
							} else if (position == 2) {
								phoneCardType = DIANXIN;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// 没有任何的触发事件时

						}

					});
			final Spinner phone_card_value_spinner = (Spinner) phone_card_recharg_view
					.findViewById(R.id.phone_card_value_spinner);

			phone_card_value_spinner.setSelection(4);// 默认100元
			phone_card_value_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							// 点击下拉框。。。
							int position = phone_card_value_spinner
									.getSelectedItemPosition();
							PublicMethod.myOutput("*********position "
									+ position);
							String[] values = { "10", "20", "30", "50", "100",
									"200", "300", "500" };
							for (int i = 0; i < values.length; i++) {
								phoneCardValue = values[position];
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// 没有任何的触发事件时

						}

					});

			return new AlertDialog.Builder(this).setTitle(
					R.string.phone_cards_recharge).setView(
					phone_card_recharg_view).setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 周黎鸣 7.4 代码修改：添加登陆的判断
							ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
									AccountRecharge.this, "addInfo");
							String sessionIdStr = pre
									.getUserLoginInfo("sessionid");
							if (sessionIdStr == null || sessionIdStr.equals("")) {
								Intent intentSession = new Intent(
										AccountRecharge.this, UserLogin.class);
								startActivity(intentSession);
							} else {
								beginPhoneCardRecharge(phone_card_recharg_view);
							}
						}
					}).setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked cancel so do some stuff */
						}
					}).create();
		case ZFB_RECHARGE_DIALOG:

			final View zfb_recharge_view = factory.inflate(
					R.layout.zfb_recharge_dialog, null);
			return new AlertDialog.Builder(this).setTitle(
					R.string.zhfb_cards_recharge).setView(zfb_recharge_view)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 周黎鸣 7.4 代码修改：添加登陆的判断
									beginZFBRecharge(zfb_recharge_view);

								}
							}).setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();

		case PHONE_BANK_RECHARGE_DIALOG:

			final View phone_bank_recharg_view = factory.inflate(
					R.layout.phone_bank_recharg_dialog, null);

			final Spinner phone_bank_spinner = (Spinner) phone_bank_recharg_view
					.findViewById(R.id.phone_bank_spinner);
			phone_bank_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							// 点击下拉框。。。
							int position = phone_bank_spinner.getSelectedItemPosition();
							if (position == 0) {
								bankType = ZHAOSHANG;
							} else if (position == 1) {
								bankType = JIANSHE;
							} else {
								bankType = GONGSHANG;
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// 没有任何的触发事件时

						}

					});
			return new AlertDialog.Builder(this).setTitle(
					R.string.phone_bank_cards_recharge).setView(
					phone_bank_recharg_view).setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// 周黎鸣 7.4 代码修改：添加登陆的判断
							ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
									AccountRecharge.this, "addInfo");
							String sessionIdStr = pre
									.getUserLoginInfo("sessionid");
							if (sessionIdStr.equals("")) {
								Intent intentSession = new Intent(
										AccountRecharge.this, UserLogin.class);
								startActivity(intentSession);
							} else {
								beginPhoneBankRecharge(phone_bank_recharg_view);
							}
						}
					}).setNegativeButton(R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked cancel so do some stuff */
						}
					}).create();

		case GAME_CARD_DIALOG:

			final View game_card_view = factory.inflate(
					R.layout.game_card_dialog, null);
			final Spinner game_card_spinner = (Spinner) game_card_view
					.findViewById(R.id.game_card_spinner);
			game_card_spinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							// 点击下拉框。。。
							int position = game_card_spinner.getSelectedItemPosition();
							if (position == 0) {
								gameCardType = ZHENGTU;
							} else if (position == 1) {
								gameCardType = SHENGDA;
							} else
								gameCardType = JUNWANG;

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// 没有任何的触发事件时

						}
					});
			return new AlertDialog.Builder(this).setTitle(
					R.string.game_cards_recharge).setView(game_card_view)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 周黎鸣 7.14 代码修改：添加登陆的判断
									ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
											AccountRecharge.this, "addInfo");
									String sessionIdStr = pre
											.getUserLoginInfo("sessionid");
									if (sessionIdStr!=null&&sessionIdStr.equals("")) {
										Intent intentSession = new Intent(
												AccountRecharge.this,
												UserLogin.class);
										startActivity(intentSession);
									} else {
										beginGameCardRecharge(game_card_view);
									}
									/* User clicked OK so do some stuff */
								}
							}).setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();

		case COMPUTER_NET_RECHARGE_DIALOG:
			// fqc delete 删除取消按钮 7/14/2010
			final View computer_net_recharge_view = factory.inflate(
					R.layout.computer_net_recharge_dialog, null);
			return new AlertDialog.Builder(this).setTitle(
					R.string.computer_net_recharge).setView(
					computer_net_recharge_view).setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							/* User clicked OK so do some stuff */
						}
					}).create();

			// 创建 第一次使用语音充值时的注册对话框 陈晨2010/7/11
		case BANK_PHONE_CARD_REGISTER_DIALOG:

			final View phone_bank_card_view = factory.inflate(
					R.layout.bank_card_phone_register_dialog, null);
			return new AlertDialog.Builder(this).setTitle(
					R.string.bank_cards_recharge).setView(phone_bank_card_view)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									beginRegisterBankPhoneCard(phone_bank_card_view);
									/* User clicked OK so do some stuff */
								}
							}).setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked cancel so do some stuff */
								}
							}).create();
		case PROGRESS_VALUE:
			progressDialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressDialog.setMessage("网络连接中...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		}
		// startActivity(toWebIntent);
		return null;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
//		RuyicaiActivityManager.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.account_rechange_list_main);
		shellRW = new ShellRWSharesPreferences(AccountRecharge.this, "addInfo");
		ListView listView = (ListView) findViewById(R.id.account_rechange_listview);
		list = getListForUserCenterAdapter();
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.account_recharge_listviw_item, new String[] { TITLE,
						IICON }, new int[] {
						R.id.account_recharge_listview_text,
						R.id.account_recharge_iicon });
		listView.setAdapter(adapter);
		Drawable drawable = getResources().getDrawable(
				R.drawable.list_selector_red);
		listView.setSelector(drawable);
		PublicMethod.setmydividerHeight(listView);
		OnItemClickListener onItemClickListener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				text = (TextView) view.findViewById(R.id.account_recharge_listview_text);
				String textString = text.getText().toString();
				// 增加登录的判断 陈晨 20100715 登录之后直接跳到点击页面
				ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
						AccountRecharge.this, "addInfo");
				String sessionIdStr = pre.getUserLoginInfo("sessionid");
				if (getString(R.string.computer_net_recharge).equals(textString)) {
					showDialog(COMPUTER_NET_RECHARGE_DIALOG);

				} else {
					if (sessionIdStr == null || sessionIdStr.equals("")) {
						Intent intentSession = new Intent(AccountRecharge.this,UserLogin.class);
						startActivityForResult(intentSession, 0);
					} else {

						if (getString(R.string.bank_cards_recharge).equals(
								textString)) {
							// showDialog(BANK_CARD_PHONE_DIALOG);
							checkDNA();
						} else if (getString(R.string.phone_cards_recharge)
								.equals(textString)) {
							showAccountRechargeDialog(PHONE_CARD_RECHARGE_DIALOG);
						} else if (getString(R.string.zhfb_cards_recharge)
								.equals(textString)) {
							showAccountRechargeDialog(ZFB_RECHARGE_DIALOG);
						} else if (getString(R.string.phone_bank_cards_recharge)
								.equals(textString)) {
							showAccountRechargeDialog(PHONE_BANK_RECHARGE_DIALOG);
						} else if (getString(R.string.game_cards_recharge)
								.equals(textString)) {
							showAccountRechargeDialog(GAME_CARD_DIALOG);

						} else if (getString(R.string.ruyihelper_balanceInquiry)
								.equals(textString)) {
							ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
									AccountRecharge.this, "addInfo");
							phonenum = shellRW.getUserLoginInfo("phonenum");
							sessionid = shellRW.getUserLoginInfo("sessionid");
							if (sessionid==null||sessionid.equals("")) {
								Intent intentSession = new Intent(AccountRecharge.this, UserLogin.class);
								// startActivity(intentSession);
								startActivityForResult(intentSession, 0);
							} else {
								ShellRWSharesPreferences shellRW1 = new ShellRWSharesPreferences(
										AccountRecharge.this, "addInfo");
								phonenum = shellRW1
										.getUserLoginInfo("phonenum");
								sessionid = shellRW1
										.getUserLoginInfo("sessionid");
								// String
								// userId=shellRW.getUserLoginInfo("userId");
								// 余额查询

								showDialog(PROGRESS_VALUE);
								Thread t = new Thread(new Runnable() {
									public void run() {
										String error_code = "00";

											re = GetUserInfoInterface.findUserInfo(phonenum, sessionid);
											try {
												obj = new JSONObject(re);
												error_code = obj.getString("error_code");
											} catch (JSONException e) {
												
											}
										if (error_code.equals("0000")) {
											Message msg = new Message();
											msg.what = 18;
											handler.sendMessage(msg);

										} else if (error_code.equals("070002")) {
											Message msg = new Message();
											msg.what = 14;
											handler.sendMessage(msg);

										} else if (error_code.equals("4444")) {
											Message msg = new Message();
											msg.what = 15;
											handler.sendMessage(msg);

										} else if (error_code.equals("00")) {
											Message msg = new Message();
											msg.what = 16;
											handler.sendMessage(msg);
										} else {
											Message msg = new Message();
											msg.what = 17;
											handler.sendMessage(msg);
										}
									}
								});
								t.start();
							}
						}
					}

				}

			}

		};
		listView.setOnItemClickListener(onItemClickListener);

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		Log.v("AccountRecharge", "onResume");
		Constants.RUYIHELPERSHOWLISTTYPE=0;
	}

	private List<Map<String, Object>> getListForUserCenterAdapter() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		// 银行卡电话充值
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.bank_cards_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);

		// 手机话费充值卡
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.phone_cards_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);

		// 支付宝充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);

		// 手机银行充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.phone_bank_cards_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);

		// 游戏点卡充值
		map = new HashMap<String, Object>();

		map.put(TITLE, getString(R.string.game_cards_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);
		// 电脑网上充值
		map = new HashMap<String, Object>();
		// map.put(ICON, R.drawable.computer_online_recharge);
		map.put(TITLE, getString(R.string.computer_net_recharge));
		map.put(IICON, R.drawable.xiangyou);
		list.add(map);

		return list;
	}

	private void beginZFBRecharge(View Vi) {

		final EditText zfb_recharge_value = (EditText) Vi.findViewById(R.id.zfb_recharge_value);
		final String zfb_recharge_value_string = zfb_recharge_value.getText().toString();
		final EditText zfb_account_number = (EditText) Vi.findViewById(R.id.zfb_account_number);
		String zfb_account_number_string = zfb_account_number.getText().toString();

		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(AccountRecharge.this, "addInfo");
		String sessionIdStr = pre.getUserLoginInfo("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(AccountRecharge.this,
					UserLogin.class);
			startActivity(intentSession);
		} else {
			if (zfb_recharge_value_string.equals("")
					|| zfb_recharge_value_string.length() == 0
					|| zfb_account_number_string.equals("")
					|| zfb_account_number_string.length() == 0) {
				Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
			} else {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				showDialog(PROGRESS_VALUE);
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
								AccountRecharge.this, "addInfo");
						String phonenum = shellRW.getUserLoginInfo("phonenum");
						String sessionid = shellRW
								.getUserLoginInfo("sessionid");

						strAccesstype = "C";
						ChargeInterface zfbChargeInterface = ChargeInterface.getInstance();

						String error_code = zfbChargeInterface.phonebankcharge(strAccesstype, phonenum, "0300",
								zfb_recharge_value_string, "zfb001", strExpand,sessionid);

						if (error_code.equals("000000")) {
							url = zfbChargeInterface.url;
							shellRW.setUserLoginInfo("url", url);
							zfb_recharge_value.setText("");
							zfb_account_number.setText("");
							Message msg = new Message();
							msg.what = 12;
							handler.sendMessage(msg);

						} else if (error_code.equals("070002")) {
							zfb_recharge_value.setText("");
							zfb_account_number.setText("");
							Message msg = new Message();
							msg.what = 7;
							handler.sendMessage(msg);

						} else if (error_code.equals("00")) {
							zfb_recharge_value.setText("");
							zfb_account_number.setText("");
							Message msg = new Message();
							msg.what = 8;
							handler.sendMessage(msg);

						} else {
							zfb_recharge_value.setText("");
							zfb_account_number.setText("");
							Message msg = new Message();
							msg.what = 9;
							handler.sendMessage(msg);

						}
					}

				});
				t.start();

				/* User clicked OK so do some stuff */
			}
		}

	}

	private void beginPhoneBankRecharge(View Vi) {

		final EditText phone_bank_enter_value = (EditText) Vi
				.findViewById(R.id.phone_bank_enter_value);
		Editable phone_bank_value = phone_bank_enter_value.getText();
		final String phone_bank_value_string = String.valueOf(phone_bank_value);
		// 手机银行网络连接
		// fqc edit 7/13/2010 修改文本的输入要求 ：请输入充值金额(整数)：数字（整数）限输入小数点以下位数；
		if ((!(bankType.equals("")) && bankType != null)
				&& (!(phone_bank_value_string.equals("")) && phone_bank_value_string != null)) {
			if (Integer.parseInt(phone_bank_value_string) >= 1) {
				// 改为线程 200/7/9 cc
				showDialog(PROGRESS_VALUE);
				Thread t = new Thread(new Runnable() {

					@Override
					public void run() {
						ChargeInterface phonebankChargeInterface = ChargeInterface
								.getInstance();
						ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
								AccountRecharge.this, "addInfo");
						String phonenum = shellRW.getUserLoginInfo("phonenum");
						String sessionid = shellRW
								.getUserLoginInfo("sessionid");
						strAccesstype = "B";
						// 参数含义 0102 手机银行标识 10 充值金额 y00003 银行标识（默认）

						String error_code = phonebankChargeInterface
								.phonebankcharge(strAccesstype, phonenum,
										bankType, phone_bank_value_string,
										"y00003", strExpand, sessionid);

						if (error_code.equals("000000")) {
							url = phonebankChargeInterface.url;
							phone_bank_enter_value.setText("");
							Message msg = new Message();
							msg.what = 12;
							handler.sendMessage(msg);
							shellRW.setUserLoginInfo("url", url);

						} else if (error_code.equals("070002")) {
							phone_bank_enter_value.setText("");
							Message msg = new Message();
							msg.what = 7;
							handler.sendMessage(msg);

						} else if (error_code.equals("00")) {
							phone_bank_enter_value.setText("");
							Message msg = new Message();
							msg.what = 8;
							handler.sendMessage(msg);
							phone_bank_enter_value.setText("");

						} else {
							phone_bank_enter_value.setText("");
							Message msg = new Message();
							msg.what = 9;
							handler.sendMessage(msg);

						}
					}

				});
				t.start();

			} else
				Toast.makeText(this, "充值金额至少为1元！", Toast.LENGTH_LONG).show();
		} else
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
	}

	// 银行卡语音充值
	private void beiginBankCardPhoneOnline(View vi) {
		EditText bank_card_phone_recharge_value = (EditText) vi
				.findViewById(R.id.bank_card_phone_recharge_value);
		final String bank_card_phone_recharge_value_string = bank_card_phone_recharge_value
				.getText().toString();
		EditText bank_card_phone_phone_num = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_num);
		final String bank_card_phone_phone_num_string = bank_card_phone_phone_num
				.getText().toString();

		final String bank_card_phone_bankid_string = bank_card_phone_bankid
				.getText().toString();

		// 充值金额20元以上的整数金额：输入20以上整数数字
		// 手机号码：数字11位 ；

		// 银行卡语音充值网络连接

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				AccountRecharge.this, "addInfo");
		final String phonenum = shellRW.getUserLoginInfo("phonenum");
		final String sessionid = shellRW.getUserLoginInfo("sessionid");

		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
		// ui还缺少银行卡号
		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
		// ；
		if ((!(bank_card_phone_phone_num_string.equals("")) && bank_card_phone_phone_num_string != null)
				&& (!(bank_card_phone_recharge_value_string.equals("")) && bank_card_phone_recharge_value_string != null)
				&& (!(bank_card_phone_bankid_string.equals("")) && bank_card_phone_bankid_string != null)) {
			if (bank_card_phone_phone_num_string.length() == 11) {
				if (Integer.parseInt(bank_card_phone_recharge_value_string) >= 20) {

					String acceptphonenum = bank_card_phone_phone_num_string;
					strExpand = " , , , ," + acceptphonenum + ",true";

					bank_card_id = bank_card_phone_bankid_string;
					bank_phone_card_net(phonenum,
							bank_card_phone_recharge_value_string,
							bank_card_id, sessionid);
					bank_card_phone_recharge_value.setText("");
					bank_card_phone_phone_num.setText("");

				} else {
					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",Toast.LENGTH_LONG).show();
			}
		} else
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
		/* User clicked OK so do some stuff */

	}

	// 银行卡语音充值(绑定)
	private void beiginBankCardPhone(View vi) {
		EditText bank_card_phone_recharge_value = (EditText) vi
				.findViewById(R.id.bank_card_phone_recharge_value);
		final String bank_card_phone_recharge_value_string = bank_card_phone_recharge_value
				.getText().toString();
		EditText bank_card_phone_phone_num = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_num);
		final String bank_card_phone_phone_num_string = bank_card_phone_phone_num
				.getText().toString();
		EditText bank_card_phone_bankid = (EditText) vi
				.findViewById(R.id.bank_card_phone_bankid);
		final String bank_card_phone_bankid_string = bank_card_phone_bankid
				.getText().toString();

		// fqc edit 7/13/2010
		// 充值金额20元以上的整数金额：输入20以上整数数字
		// 手机号码：数字11位 ；
		PublicMethod
				.myOutput("************bank_card_phone_recharge_value_string  bank_card_phone_phone_num_string"
						+ bank_card_phone_recharge_value_string
						+ bank_card_phone_phone_num_string);

		// 银行卡语音充值网络连接

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
				AccountRecharge.this, "addInfo");
		final String phonenum = shellRW.getUserLoginInfo("phonenum");
		final String sessionid = shellRW.getUserLoginInfo("sessionid");

		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
		// ui还缺少银行卡号
		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
		// ；
		if ((!(bank_card_phone_phone_num_string.equals("")) && bank_card_phone_phone_num_string != null)
				&& (!(bank_card_phone_recharge_value_string.equals("")) && bank_card_phone_recharge_value_string != null)
				&& (!(bank_card_phone_bankid_string.equals("")) && bank_card_phone_bankid_string != null)) {
			if (bank_card_phone_phone_num_string.length() == 11) {
				if (Integer.parseInt(bank_card_phone_recharge_value_string) >= 20) {
					String acceptphonenum = bank_card_phone_phone_num_string;
					strExpand = " , , , ," + acceptphonenum + ",true,";

					bank_card_id = bank_card_phone_bankid_string;
					bank_phone_card_net(phonenum,
							bank_card_phone_recharge_value_string,
							bank_card_id, sessionid);
					bank_card_phone_recharge_value.setText("");
					bank_card_phone_phone_num.setText("");
					bank_card_phone_bankid.setText("");
				} else {
					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",
						Toast.LENGTH_LONG).show();
			}
		} else
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
		/* User clicked OK so do some stuff */

	}

	// 银行卡语音充值(未绑定)
	private void beiginBankCardPhoneNo(View vi) {
		EditText bank_card_phone_recharge_value = (EditText) vi
				.findViewById(R.id.bank_card_phone_recharge_value);// 金额
		final String value = bank_card_phone_recharge_value.getText()
				.toString();
		EditText bank_card_phone_bankid = (EditText) vi
				.findViewById(R.id.bank_card_phone_bankid);// 银行卡号
		final String bankid = bank_card_phone_bankid.getText().toString();
//		EditText bank_card_phone_name = (EditText) vi
//				.findViewById(R.id.bank_card_phone_phone_name);// 姓名
		final String name = bank_card_phone_name.getText().toString();
		EditText bank_card_phone_idcard = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
		final String idcard = bank_card_phone_idcard.getText().toString();
		EditText bank_card_phone_home = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
		final String home = bank_card_phone_home.getText().toString();
		EditText bank_card_phone_province = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_province);// 所在省
		final String province = bank_card_phone_province.getText().toString();
		EditText bank_card_phone_city = (EditText) vi
				.findViewById(R.id.bank_card_phone_phone_city);// 所在市
		final String city = bank_card_phone_city.getText().toString();
//		EditText bank_card_phone_phone_num = (EditText) vi
//				.findViewById(R.id.bank_card_phone_phone_num);// 手机号
		final String num = bank_card_phone_phone_num.getText().toString();

		// 银行卡语音充值网络连接
		final String phonenum = shellRW.getUserLoginInfo("phonenum");
		final String sessionid = shellRW.getUserLoginInfo("sessionid");

		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
		// ui还缺少银行卡号
		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
		// ；
		if ((!(num.equals("")) && num != null)
				&& (!(value.equals("")) && value != null)
				&& (!(bankid.equals("")) && bankid != null)
				&& (!(name.equals("")) && name != null)
				&& (!(idcard.equals("")) && idcard != null)
				&& (!(home.equals("")) && home != null)
				&& (!(province.equals("")) && province != null)
				&& (!(city.equals("")) && city != null)) {
			if (num.length() == 11) {
				if (Integer.parseInt(value) >= 20) {
					String acceptphonenum = num;
					strExpand = name + "," + idcard + "," + province + city
							+ "," + home + " ," + acceptphonenum + ",false";

					bank_card_id = bankid;
					bank_phone_card_net(phonenum, value, bank_card_id,sessionid);
					bank_card_phone_recharge_value.setText("");
					bank_card_phone_phone_num.setText("");
					bank_card_phone_bankid.setText("");
				} else {
					Toast.makeText(this, "至少充值金额为20元！", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getBaseContext(), "手机号长度必须为11位！",Toast.LENGTH_LONG).show();
			}
		} else{
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
		}

	}

	// 20100711 陈晨 银行语音注册框

	private void beginRegisterBankPhoneCard(View vi) {

		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(AccountRecharge.this, "addInfo");
		final String phonenum = shellRW.getUserLoginInfo("phonenum");
		final String sessionid = shellRW.getUserLoginInfo("sessionid");
		// 得到真实姓名、身份证号、开户银行所在地、开户银行所在地、银行卡号、金额、手机号 20100711
		EditText bank_card_phone_username = (EditText) vi.findViewById(R.id.bank_card_phone_user_name);
		final String bank_card_phone_username_string = bank_card_phone_username.getText().toString();

		EditText bank_card_phone_user_idnum = (EditText) vi.findViewById(R.id.bank_card_phone_user_idnum);
		final String bank_card_phone_user_idnum_string = bank_card_phone_user_idnum.getText().toString();

		EditText bank_card_phone_open_bank = (EditText) vi.findViewById(R.id.bank_card_phone_open_bank);
		final String bank_card_phone_open_bank_string = bank_card_phone_open_bank.getText().toString();

		EditText bank_card_phone_open_bankuser_address = (EditText) vi.findViewById(R.id.bank_card_phone_open_bankuser_address);
		final String bank_card_phone_open_bankuser_address_string = bank_card_phone_open_bankuser_address
				.getText().toString();

		EditText bank_card_phone_bankid = (EditText) vi.findViewById(R.id.bank_card_phone_bankid);
		final String bank_card_phone_bankid_string = bank_card_phone_bankid.getText().toString();

		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);
		final String bank_card_phone_recharge_value_string = bank_card_phone_recharge_value
				.getText().toString();

		EditText bank_card_phone_phone_num = (EditText) vi.findViewById(R.id.bank_card_phone_phone_num);
		final String bank_card_phone_phone_num_string = bank_card_phone_phone_num.getText().toString();

		strExpand = bank_card_phone_username_string + ","
				+ bank_card_phone_user_idnum_string + ","
				+ bank_card_phone_open_bank_string + ","
				+ bank_card_phone_open_bankuser_address_string + ","
				+ bank_card_phone_phone_num_string + ",false";

		bank_card_id = bank_card_phone_bankid_string;

		bank_phone_card_net(phonenum, bank_card_phone_recharge_value_string,bank_card_id, sessionid);

	}

	// 手机卡充值
	private void beginPhoneCardRecharge(View view) {

		final EditText phone_card_rechargecard_info = (EditText) view.findViewById(R.id.phone_card_rechargecard_info);
		final String phone_card_rechargecard_info_string = phone_card_rechargecard_info.getText().toString();
		final EditText phone_card_rechargecard_password = (EditText) view.findViewById(R.id.phone_card_rechargecard_password);
		final String phone_card_rechargecard_password_string = phone_card_rechargecard_password.getText().toString();

		
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this,"addInfo");
		final String phonenum = shellRW.getUserLoginInfo("phonenum");
		final String sessionid = shellRW.getUserLoginInfo("sessionid");
		final ChargeInterface phonecardChargeInterface = ChargeInterface.getInstance();

		// 手机充值卡充值
		// 参数含义：0203 充值卡类型 10 充值钱数 5000充值总额 10623260104706723 充值卡号
		// 261324590869999653 充值密码 y00003银行标识默认
		// ui缺少充值的钱数
		if ((!(phone_card_rechargecard_info_string.equals("")) && phone_card_rechargecard_info_string != null)
				&& (!(phone_card_rechargecard_password_string.equals("")) && phone_card_rechargecard_password_string != null)) {
			if (isCardString(phone_card_rechargecard_info_string)) {
				// 改为线程 陈晨 200/7/9
				showDialog(PROGRESS_VALUE);
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						String error_code = "";
						if (phoneCardType == DIANXIN) {
							error_code = phonecardChargeInterface
									.phonecardcharge(
											"C",
											phonenum,
											phoneCardType,
											phoneCardValue,
											phoneCardValue,
											phone_card_rechargecard_info_string,
											phone_card_rechargecard_password_string,
											"gyj001", "", sessionid);
						} else {
							error_code = phonecardChargeInterface
									.phonecardcharge(
											"C",
											phonenum,
											phoneCardType,
											phoneCardValue,
											phoneCardValue,
											phone_card_rechargecard_info_string,
											phone_card_rechargecard_password_string,
											"y00003", "", sessionid);
						}

						if (error_code.equals("000000")) {
							Message msg = new Message();
							msg.what = 6;
							handler.sendMessage(msg);
							// 陈晨 编辑框置为空 20100716
							phone_card_rechargecard_info.setText("");
							phone_card_rechargecard_password.setText("");

						} else if (error_code.equals("070002")) {
							Message msg = new Message();
							msg.what = 7;
							handler.sendMessage(msg);
						} else if (error_code.equals("00")) {
							Message msg = new Message();
							msg.what = 8;
							handler.sendMessage(msg);
							// 陈晨 编辑框置为空 20100716
							phone_card_rechargecard_info.setText("");
							phone_card_rechargecard_password.setText("");
						} else {
							Message msg = new Message();
							msg.what = 9;
							handler.sendMessage(msg);
							// 陈晨 编辑框置为空 20100716
							phone_card_rechargecard_info.setText("");
							phone_card_rechargecard_password.setText("");
						}
					}
				});
				t.start();
			} else {
				Toast.makeText(this, "充值卡序列号应为数字或字母！", Toast.LENGTH_LONG).show();
			}
		} else
			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
	}

	private void beginGameCardRecharge(View Vi) {

		final EditText game_card_number = (EditText) Vi
				.findViewById(R.id.game_card_number);
		game_card_number_string = game_card_number.getText().toString();

		final EditText game_card_password = (EditText) Vi
				.findViewById(R.id.game_card_password);
		game_card_password_string = game_card_password.getText().toString();

		final EditText game_card_total_value = (EditText) Vi.findViewById(R.id.game_card_total_value);
		game_card_total_value_string = game_card_total_value.getText().toString();
		
		if (game_card_number_string.equals("")
				|| game_card_password_string.equals("")
				|| game_card_total_value_string.equals("")) {
			Message msg = new Message();
			msg.what = 0;
			handler.sendMessage(msg);
		} else if (isCardString(game_card_number_string)) {

			showDialog(PROGRESS_VALUE);
			Thread t = new Thread(new Runnable() {
				public void run() {

					// 游戏点卡改成新接口
					ChargeInterface gameCardRecharge = ChargeInterface.getInstance();

					ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(
							AccountRecharge.this, "addInfo");

					String sessionid = shellRW.getUserLoginInfo("sessionid");
					String phonenum = shellRW.getUserLoginInfo("phonenum");

					error_code = gameCardRecharge.phonecardcharge("C",
							phonenum, gameCardType,
							game_card_total_value_string,
							game_card_total_value_string,
							game_card_number_string, game_card_password_string,
							"y00003", "", sessionid);
					if (error_code.equals("000000")) {
						Message msg = new Message();
						msg.what = 6;
						handler.sendMessage(msg);
						// 信息置为空 陈晨 20100716
						game_card_total_value.setText("");
						game_card_password.setText("");
						game_card_number.setText("");

						game_card_number_string = "";
						game_card_password_string = "";
						game_card_total_value_string = "";

					} else if (error_code.equals("070002")) {
						Message msg = new Message();
						msg.what = 7;
						handler.sendMessage(msg);
					} else if (error_code.equals("0012")) {
						Message msg = new Message();
						msg.what = 2;
						handler.sendMessage(msg);
						// 信息置为空 陈晨 20100716
						game_card_total_value.setText("");
						game_card_password.setText("");
						game_card_number.setText("");
					} else if (error_code.equals("4444")) {
						Message msg = new Message();
						msg.what = 3;
						// msg.getData().putString("get","系统结算，请稍后");
						handler.sendMessage(msg);
						// 信息置为空 陈晨 20100716
						game_card_total_value.setText("");
						game_card_password.setText("");
						game_card_number.setText("");
					} else if (error_code.equals("00")) {
						Message msg = new Message();
						msg.what = 8;
						handler.sendMessage(msg);
						// 信息置为空 陈晨 20100716
						game_card_total_value.setText("");
						game_card_password.setText("");
						game_card_number.setText("");

					} else {
						Message msg = new Message();
						msg.what = 9;
						handler.sendMessage(msg);
						// 信息置为空 陈晨 20100716
						game_card_total_value.setText("");
						game_card_password.setText("");
						game_card_number.setText("");
					}

				}

			});
			t.start();
		} else {
			Toast.makeText(getBaseContext(), "卡号格式输入不正确！", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 调用chargeInterface 银行卡语音充值联网
	 * @param phonenum 接收电话手机号
	 * @param bank_card_phone_totalAmount 充值金额
	 * @param bank_card_Num  银行卡号
	 * @param sessionid  登录的sessionid
	 */
	private void bank_phone_card_net(final String phonenum,
			final String bank_card_phone_totalAmount,
			final String bank_card_Num, final String sessionid) {
		showDialog(PROGRESS_VALUE);
		// 改为线程 陈晨 2010/7/9
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ChargeInterface phonecardChargeInterface = ChargeInterface.getInstance();
				// 钱数以分为单位传给后台 故加”00“ 陈晨 20100714
				String error_code = phonecardChargeInterface.phonecardcharge(
						"C", phonenum, "0101", bank_card_phone_totalAmount,
						bank_card_phone_totalAmount, bank_card_Num, "",
						"dna001", strExpand, sessionid);
				if (error_code.equals("00A3")) {
					bankCardNo = bank_card_Num;
					Message msg = new Message();
					msg.what = 10;
					handler.sendMessage(msg);
				} else if (error_code.equals("T437")
						|| error_code.equals("T438")) {
					Message msg = new Message();
					msg.what = 11;
					handler.sendMessage(msg);

				} else if (error_code.equals("070002")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);

				} else if (error_code.equals("T404")) {
					Message msg = new Message();
					msg.what = 13;
					handler.sendMessage(msg);
				} else if (error_code.equals("00")) {

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

	// 提示框
	private void alert(String string) {
		Builder dialog = new AlertDialog.Builder(this).setTitle(string)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		dialog.show();

	}

	private boolean isCardString(String cardNumber) {
		int length = cardNumber.length();
		boolean isRight = true;
		for (int i = 0; i < length - 1; i++) {
			if (cardNumber.charAt(i) < '0'
					|| (cardNumber.charAt(i) > '9' && cardNumber.charAt(i) < 'A')
					|| (cardNumber.charAt(i) > 'Z' && cardNumber.charAt(i) < 'a')
					|| (cardNumber.charAt(i) > 'z')) {
				isRight = false;
			}
		}
		return isRight;

	}

	/**
	 * intent回调函数
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			String textString = text.getText().toString();

			if (getString(R.string.bank_cards_recharge).equals(textString)) {
				checkDNA();
			} else if (getString(R.string.phone_cards_recharge).equals(
					textString)) {
				showAccountRechargeDialog(PHONE_CARD_RECHARGE_DIALOG);
			} else if (getString(R.string.zhfb_cards_recharge).equals(
					textString)) {
				showAccountRechargeDialog(ZFB_RECHARGE_DIALOG);
			} else if (getString(R.string.phone_bank_cards_recharge).equals(
					textString)) {
				showDialog(PHONE_BANK_RECHARGE_DIALOG);
			} else if (getString(R.string.game_cards_recharge).equals(
					textString)) {
				showAccountRechargeDialog(GAME_CARD_DIALOG);
			}
			break;
		}
	}

	// 用户中心-余额查询
	public void showUserCenterBalanceInquiry() {
		String deposit_amount = "";
		String Valid_amount = "";
		String freeze_amout = "";
		String totalBalance = "";

		try {
			// 20100710 cc 修改余额格式
			deposit_amount = PublicMethod.changeMoney(obj.getString("deposit_amount"));
			Valid_amount = PublicMethod.changeMoney(obj.getString("Valid_amount"));
			freeze_amout = PublicMethod.changeMoney(obj.getString("freeze_amout"));
			totalBalance = PublicMethod.changeMoney((Integer.parseInt(obj.getString("deposit_amount")) + Integer.parseInt(obj
					.getString("freeze_amout")))
					+ "");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		TextView textview = new TextView(this);
		textview.setText(getString(R.string.total_balance) + totalBalance
				+ getString(R.string.unit) + "\n"
				+ getString(R.string.freeze_amout) + freeze_amout
				+ getString(R.string.unit) + "\n"
				+ getString(R.string.usercenter_currentBalance)
				+ deposit_amount + getString(R.string.unit) + "\n"
				+ getString(R.string.usercenter_withdrawBalance) + Valid_amount
				+ getString(R.string.unit) + "\n"
				+ getString(R.string.usercenter_remind));

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		builder.setTitle(R.string.ruyihelper_balanceInquiry);
		builder.setView(textview);

		builder.setPositiveButton(R.string.ok,
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			});
		builder.show();
	}

	/**
	 * DNA充值账户绑定查询
	 */
	public void checkDNA() {
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
				AccountRecharge.this, "addInfo");
		final String sessionId = pre.getUserLoginInfo("sessionid");
		final String mobile = pre.getUserLoginInfo("phonenum");
		showDialog(PROGRESS_VALUE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String error_code = "00";
				re = JrtLot.checkType(mobile, sessionId);
				try {
					obj = new JSONObject(re);
					error_code = obj.getString("error_code");
				} catch (Exception e) {

				}

				if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);

				} else if (error_code.equals("000000")) {
					Message msg = new Message();
					msg.what = 19;
					handler.sendMessage(msg);

				} else if (error_code.equals("000047")) {
					Message msg = new Message();
					msg.what = 20;
					handler.sendMessage(msg);

				}

			}

		}).start();
	}

	/**
	 * 创建dna对话框
	 * @版本：
	 */
	public void dialogDNA(String str) {
		String bindState = "";
		// String mobile = "";// 手机号码
		// String name = "";// 开户姓名
		// String certId = "";// 开户身份证号
		// String bankAddress = "";// 开户行所在地
		// String certAddress = "";// 开户证件所在地
		// String bindDate = "";// 首次绑定时间

		try {
			JSONObject obj = new JSONObject(str);
			bindState = obj.getString("BindState");
			bankCardNo = obj.getString("BankCardNo");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bindState.equals("1")) {// 已经绑定

			showAccountRechargeDialog(BANK_CARD_PHONE_DIALOG);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(event.getRepeatCount()){
			case 0:
				ExitDialogFactory.createExitDialog(this);
				break;
		    }
			return false;
		}
	
}