/**
 * Copyright 2010 PalmDream
 * All right reserved.
 * Development History:
 * Date             Author          Version            Modify
 * 2010-5-18        fqc              1.5                none
 */

package com.palmdream.RuyicaiAndroid;

import java.util.Calendar;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.netintface.LoginInterface;
import com.palmdream.netintface.RegisterInterface;
import com.palmdream.netintface.iHttp;
import com.palmdream.netintface.jrtLot;

public class UserLogin extends Activity implements TextWatcher {
	public static final String SUCCESS = "loginsuccess";
	public static final String UNSUCCESS = "unloginsuccess";

	private static final int DIALOG_FORGET_PASSWORD = 1;
	// cc 20100711 网络提示框
	private static final int PROGRESS_VALUE = 0;
	ProgressDialog progressDialog;
	// private EditText phoneNumSaved;
	private ShellRWSharesPreferences shellRW;
	private CheckBox remPwd_checkBox;
	private EditText phoneNum_edit;
	private EditText password_edit;
	private ShellRWSharesPreferences shellRW_connetHint;
	private boolean phoneNumLength = false;
	boolean b = false;
	boolean isConfigChange = false;
	public int configFlag;// 0表示登录，1表示注册
	int age;
	String phonenum;
	String password;
	boolean on = false;
	boolean turn = true;
	/**
	 * 处理登录的消息及注册的消息
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String result = msg.getData().getString("get");
			switch (msg.what) {
			// case 0:
			// Toast.makeText(getBaseContext(), "网络异常",
			// Toast.LENGTH_LONG).show();
			// //需要添加AlertDialog提示网络异常
			// break;
			case 1:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示注册成功
				// 20100710 add by fyj 注册成功后，界面自动返回登录页面
				// turnToLogin();
				regToLogin();
				break;
			case 2:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "用户已注册", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示用户已注册
				break;
			case 3:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "系统结算，请稍后", Toast.LENGTH_LONG)
						.show();
				// 需要添加AlertDialog提示系统结算，请稍后
				break;
			case 4:
				Toast.makeText(getBaseContext(), "该号被暂停请联系客服",
						Toast.LENGTH_LONG).show();
				// //需要添加AlertDialog提示该号被暂停请联系客服
				break;
			case 5:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "用户已注册", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog注册失败
				break;
			case 6:
				progressDialog.dismiss();
				// // tv.setText(result);
				// //需要添加AlertDialog提示用户登录成功
				// progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "登录成功", Toast.LENGTH_LONG)
						.show();
				break;
			case 7:
				// 需要添加AlertDialog提示密码错误
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "密码错误", Toast.LENGTH_LONG)
						.show();
				break;
			case 8:
				progressDialog.dismiss();
				// progressdialog.dismiss();
				Toast.makeText(getBaseContext(), "网络异常", Toast.LENGTH_LONG)
						.show();
				// //需要添加AlertDialog提示登录失败
				break;
			case 9:
				progressDialog.dismiss();
				Toast.makeText(getBaseContext(), "登录失败", Toast.LENGTH_LONG)
						.show();
				break;
			case 10:
				progressDialog.dismiss();
				if (b == true) {
					// fyj 20100712 发送登录成功的广播
					Intent intent = new Intent(SUCCESS);
					sendBroadcast(intent);

					UserLogin.this.setResult(RESULT_OK);
					Log.e("RESULT_OK===", "" + RESULT_OK);
					UserLogin.this.finish();
				}
				break;
			}
			//				
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_FORGET_PASSWORD:
			LayoutInflater factory = LayoutInflater.from(this);
			final View forgetPwd = factory.inflate(
					R.layout.alert_dialog_forget_password, null);
			return new AlertDialog.Builder(UserLogin.this).setIcon(
					R.drawable.star_big_on).setTitle(R.string.forget_password)
					.setView(forgetPwd).setPositiveButton(R.string.giveCall,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent myIntentDial = new Intent(
											"android.intent.action.CALL", Uri
													.parse("tel:4006651000"));
									startActivity(myIntentDial);
								}
							}).setNegativeButton(R.string.return_button,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
			// 网络连接提示框 2010/7/11 陈晨

		case PROGRESS_VALUE: {
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("提示");
			progressDialog.setMessage("网络连接中...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			return progressDialog;
		}
			// }

		}
		return null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		isConfigChange = true;
		if (configFlag == 0) {
			turnToLogin();
		} else if (configFlag == 1) {
			turnToReg();
		}
		isConfigChange = false;
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Initialization of the Activity after it is first created. Must at least
	 * call {@link android.app.Activity#setContentView(int)} to describe what is
	 * to be displayed in the screen.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			on = bundle.getBoolean("switch");// 读出数据
		}

		if (on) {
			turnToReg();
		} else {
			turnToLogin();
		}

	}

	/**
	 * 登录对话框
	 */
	private void turnToLogin() {
		turn = false;
		// pv统计
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					jrtLot.setPara(5, jrtLot.channel_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		configFlag = 0;
		setContentView(R.layout.alert_dialog_login_entry);

		shellRW = new ShellRWSharesPreferences(this, "addInfo");

		// 记住电话号码 和手机号 和是否记住密码的状态
		remPwd_checkBox = (CheckBox) findViewById(R.id.remember_password_checkBox);
		phoneNum_edit = (EditText) findViewById(R.id.phoneNum_edit);
		password_edit = (EditText) findViewById(R.id.password_edit);
		PublicMethod.myOutput("--------------"
				+ shellRW.getUserLoginInfo("remPwd_checkBox"));

		// fyj edit 逻辑错误 20100709 判断 checkbox记录的信息是否可用
		// fqc eidt 记住密码是在点击了记住密码的复选框 和 点击登录 时进入到preference password键值中
		// fqc edit 在屏幕切换的时候密码计入到preference中的passwordConfig中随时 记住密码
		String iTempCheck = shellRW.getUserLoginInfo("remPwd_checkBox");
		String iTempPass = shellRW.getUserLoginInfo("password");
		if (isConfigChange == true) {
			if (shellRW.getUserLoginInfo("passwordConfig") != null)
				password_edit.setText(shellRW
						.getUserLoginInfo("passwordConfig"));
			else
				password_edit.setText("");
			if (shellRW.getUserLoginInfo("remPwd_checkBox") != null) {
				if (shellRW.getUserLoginInfo("remPwd_checkBox") == "true")
					remPwd_checkBox.setChecked(true);
			} else
				remPwd_checkBox.setText("");
		} else if (iTempCheck != null && iTempPass != null) {
			if (iTempCheck.equalsIgnoreCase("true")) {
				password_edit.setText(iTempPass);
				remPwd_checkBox.setChecked(true);
			} else {
				password_edit.setText("");
			}
		} else {
			password_edit.setText("");
		}
		if (shellRW.getUserLoginInfo("phoneNumber") != null) {
			phoneNum_edit.setText(shellRW.getUserLoginInfo("phoneNumber"));
		}
		/*
		 * if(shellRW.getUserLoginInfo("password") != null){ EditText
		 * password_edit = (EditText) findViewById(R.id.password_edit);
		 * password_edit.setText(shellRW.getUserLoginInfo("password")); }
		 * if(shellRW
		 * .getUserLoginInfo("remPwd_checkBox").equalsIgnoreCase("true")){
		 * remPwd_checkBox.setChecked(true); }
		 */

		phoneNum_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				PublicMethod
						.myOutput("***********addTextChangedListener afterTextChanged");
				String phoneNumber = phoneNum_edit.getText().toString();
				shellRW.setUserLoginInfo("phoneNumber", phoneNumber);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		});
		password_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String password = password_edit.getText().toString();
				shellRW.setUserLoginInfo("passwordConfig", password);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

		});

		// 实现记住密码 和 复选框的状态
		remPwd_checkBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						rem_password_num(isChecked);
					}
				});

		// 点击忘记密码，弹出提示对话框
		Button forget_password = (Button) findViewById(R.id.forgetPwd_button);
		forget_password.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_FORGET_PASSWORD);
			}
		});
		/* 当点击手机号文本框的时候，清除里面的内容 */
		final EditText phone_name_Text = (EditText) findViewById(R.id.phoneNum_edit);
		phone_name_Text.setOnClickListener(new EditText.OnClickListener() {

			@Override
			public void onClick(View v) {
				phone_name_Text.setText("");
			}

		});
		// 判断手机号的长度，长度不是11位的时候提示错误。
		phone_name_Text.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				PublicMethod.myOutput("--------hasFocus--" + hasFocus);
				if (!hasFocus) {
					Editable editText = phone_name_Text.getText();
					String editTextString = editText.toString();
					PublicMethod.myOutput("----------editTextString.length()"
							+ editTextString.length());
					if (editText.length() != 11) {
						showHint();
						// Toast.makeText(this,
						// "the phone number long is wrong!",
						// Toast.LENGTH_LONG);
					}

				}
			}
		});
		/* 当点击密码文本框的时候，清除里面的内容 */
		// final EditText password_text = (EditText)
		// findViewById(R.id.password_view);
		// password_text.setOnClickListener(new EditText.OnClickListener(){
		//
		// @Override
		// public void onClick(View v) {
		// password_text.setText("");
		// }
		//        	
		// });
		// 点击登录按钮，实现登录
		Button login = (Button) findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beginLogin();
				/*
				 * Intent i = new Intent();
				 * 
				 * Bundle b = new Bundle(); b.putString("CALCULATION", value);
				 * 
				 * i.putExtras(b); this.setResult(RESULT_OK, i);
				 */

			}
		});

		// 点击注册按钮时，跳转到注册页面
		Button register = (Button) findViewById(R.id.register_button);
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				turnToReg();
			}
		});
		// 点击返回按钮，返回上一页
		// Button login_return = (Button)
		// findViewById(R.id.login_return_button);
		// login_return.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent();
		// UserLogin.this.setResult(0); // 未登录
		// UserLogin.this.finish();
		// }
		//
		// });
		ImageView login_return = (ImageView) findViewById(R.id.usercenter_btn_return);
		login_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				UserLogin.this.setResult(0); // 未登录
				UserLogin.this.finish();
			}

		});
	}

	private void showHint() {
		PublicMethod.myOutput("-----------show Toast");
		// Toast toast = new Toast(Login.this);
		Toast.makeText(this, "手机号长度必须为11位!", Toast.LENGTH_LONG).show();
		phoneNumLength = false;
		// toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
		// toast.setText("the phone number long is wrong!");
	}

	/**
	 * 登录联网操作
	 */

	private void beginLogin() {
		// showDialog(DIALOG_PROGRESS);
		EditText password_edit = (EditText) findViewById(R.id.password_edit);
		Editable password = password_edit.getText();
		final String password_string = String.valueOf(password);
		if (remPwd_checkBox.isChecked())
			shellRW.setUserLoginInfo("password", password_string);
		// 判断手机号长度是否正确
		EditText phone_name_Text = (EditText) findViewById(R.id.phoneNum_edit);
		Editable editText = phone_name_Text.getText();
		String editTextString = editText.toString();
		PublicMethod.myOutput("----------editTextString.length()"
				+ editTextString.length());

		if (editText.length() != 11) {
			Toast.makeText(this, "手机号长度必须为11位!", Toast.LENGTH_LONG).show();
		}
		// fqc edit 7/3/2010 判断密码长度 必须在6~15之间
		EditText password_text = (EditText) findViewById(R.id.password_edit);
		int passwordLength = password_text.getText().toString().length();
		if (passwordLength < 6 || passwordLength > 15) {
			Toast.makeText(this, "密码必须为6~15位", Toast.LENGTH_LONG).show();
		}
		if (editText.length() == 11 && 6 <= passwordLength
				&& passwordLength <= 15) {//
			// Toast.makeText(this, "the phone number Length is right!",
			// Toast.LENGTH_LONG).show();

			// TODO Auto-generated method stub

			// 以下是登录时调用的代码
			showDialog(PROGRESS_VALUE);
			PublicMethod.myOutput("--????????????????-");
			iHttp.whetherChange = false;
			Thread t = new Thread(new Runnable() {
				public void run() {
					// Looper.prepare();

					// try{LoginInterface loginInterface;
					LoginInterface loginInterface = new LoginInterface();
					String error_code = loginInterface.userlogin(String
							.valueOf(phoneNum_edit.getText()), password_string);
					// String error_code =
					// loginInterface.userlogin("13651262537","123456");
					// String user=obj.getString("username");
					PublicMethod.myOutput("---" + error_code);
					if (error_code.equals("0000")) {
						// Message msg=new Message();
						// msg.what=6;
						// handler.sendMessage(msg);
						String sessionid = loginInterface.sessionid;
						shellRW = new ShellRWSharesPreferences(UserLogin.this,
								"addInfo");
						shellRW.setUserLoginInfo("sessionid", sessionid);
						shellRW.setUserLoginInfo("phonenum", phoneNum_edit
								.getText().toString());
						PublicMethod.myOutput("-----------sessionid"
								+ shellRW.getUserLoginInfo("sessionid"));
						PublicMethod.myOutput("-----------phonenum"
								+ shellRW.getUserLoginInfo("phonenum"));
						b = true;

						Message msg = new Message();
						msg.what = 10;
						handler.sendMessage(msg);
					} else if (error_code.equals("070002")) {
						Message msg = new Message();
						msg.what = 7;
						handler.sendMessage(msg);
					} else if (error_code.equals("4444")) {
						Message msg = new Message();
						msg.what = 3;
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
	}

	/**
	 * 注册成功后直接登录
	 * 
	 */
	private void regToLogin() {
		showDialog(PROGRESS_VALUE);
		PublicMethod.myOutput("--????????????????-");
		iHttp.whetherChange = false;
		Thread t = new Thread(new Runnable() {
			public void run() {
				// Looper.prepare();

				// try{LoginInterface loginInterface;
				LoginInterface loginInterface = new LoginInterface();
				String error_code = loginInterface
						.userlogin(phonenum, password);
				// String error_code =
				// loginInterface.userlogin("13651262537","123456");
				// String user=obj.getString("username");
				PublicMethod.myOutput("---" + error_code);
				if (error_code.equals("0000")) {
					// Message msg=new Message();
					// msg.what=6;
					// handler.sendMessage(msg);
					String sessionid = loginInterface.sessionid;
					shellRW = new ShellRWSharesPreferences(UserLogin.this,
							"addInfo");
					shellRW.setUserLoginInfo("sessionid", sessionid);
					shellRW.setUserLoginInfo("phonenum", phonenum);
					PublicMethod.myOutput("-----------sessionid"
							+ shellRW.getUserLoginInfo("sessionid"));
					PublicMethod.myOutput("-----------phonenum"
							+ shellRW.getUserLoginInfo("phonenum"));
					b = true;

					Message msg = new Message();
					msg.what = 10;
					handler.sendMessage(msg);
				} else if (error_code.equals("070002")) {
					Message msg = new Message();
					msg.what = 7;
					handler.sendMessage(msg);
				} else if (error_code.equals("4444")) {
					Message msg = new Message();
					msg.what = 3;
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

	/**
	 * 注册框
	 */
	private void turnToReg() {
		// pv统计
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					jrtLot.setPara(5, jrtLot.channel_id);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		configFlag = 1;
		setContentView(R.layout.user_register);
		Button register_button = (Button) findViewById(R.id.register_button_);
		register_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 首先判断注册时两次输入的密码是否一致 ，然后再进行注册
				EditText register_password_edit = (EditText) findViewById(R.id.register_password_edit);
				Editable register_password = register_password_edit.getText();
				String register_password_string = String
						.valueOf(register_password);

				EditText register_confirm_password_edit = (EditText) findViewById(R.id.register_confirm_password_edit);
				Editable register_confirm_password = register_confirm_password_edit
						.getText();
				String register_confirm_password_string = String
						.valueOf(register_confirm_password);
				EditText register_phone_Text = (EditText) findViewById(R.id.register_username_edit);
				String register_phone_Text_string = register_phone_Text
						.getText().toString();
				EditText password_text = (EditText) findViewById(R.id.register_password_edit);
				int passwordLength = password_text.getText().toString()
						.length();

				EditText register_id = (EditText) findViewById(R.id.register_id_num_edit);
				String register_id_string = register_id.getText().toString();
				int register_id_string_len = register_id_string.length();
				boolean register_id_isRight = true;

				if (register_id_string_len != 18
						&& register_id_string_len != 15)
					register_id_isRight = false;

				if (register_id_string_len == 18
						|| register_id_string_len == 15) {
					String register_id_substring = register_id_string
							.substring(0, register_id_string_len - 2);

					if (register_id_isRight == true) {
						for (int i = 0; i < register_id_string_len - 2; i++) {
							if (register_id_substring.charAt(i) < '0'
									|| register_id_substring.charAt(i) > '9')
								register_id_isRight = false;
						}
						if (register_id_string
								.charAt(register_id_string_len - 1) != 'x'
								&& register_id_string
										.charAt(register_id_string_len - 1) != 'X'
								&& (register_id_string
										.charAt(register_id_string_len - 1) > '9' || register_id_string
										.charAt(register_id_string_len - 1) < '0')) {
							register_id_isRight = false;
						}
					}
				}
				// 判断是否满18
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				if (register_id_string_len == 15) {
					age = Integer.parseInt("19"
							+ register_id_string.substring(6, 8));
				} else if (register_id_string_len == 18) {
					age = Integer.parseInt(register_id_string.substring(6, 10));
				}
				if (year - age < 18) {
					Toast.makeText(getBaseContext(), "未满18岁!",
							Toast.LENGTH_LONG).show();
					register_id_isRight = false;
				}
				// fqc edit 7/3/2010 判断输入是否符合要求
				if (register_phone_Text_string.length() != 11) {
					Toast.makeText(getBaseContext(), "手机号长度必须为11位!",
							Toast.LENGTH_LONG).show();
				}
				if (passwordLength < 6 || passwordLength > 15) {
					Toast.makeText(getBaseContext(), "密码必须为6~15位",
							Toast.LENGTH_LONG).show();
				}
				if (!register_password_string
						.equals(register_confirm_password_string))
					Toast.makeText(getBaseContext(), "两次密码输入不同",
							Toast.LENGTH_LONG).show();
				if (register_id_isRight == false) {

					Toast.makeText(getBaseContext(), "身份证号码有误",
							Toast.LENGTH_LONG).show();
				}
				if (register_password_string
						.equals(register_confirm_password_string)
						&& register_phone_Text_string.length() == 11
						&& passwordLength >= 6 && passwordLength <= 15
				// && register_id_isRight == true
				) {
					// Toast.makeText(getBaseContext(),
					// "passwords are same",Toast.LENGTH_LONG).show();
					beginRegister();

				}
			}
		});
		// Intent in = new Intent(this, Register.class);
		// startActivity(in);
		// this.finish();
		// Button back_to_login = (Button) findViewById(R.id.back_to_login);
		// back_to_login.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// turnToLogin();
		// }
		// });
		ImageView login_return = (ImageView) findViewById(R.id.usercenter_btn_return);
		login_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Intent i = new Intent();
				// UserLogin.this.setResult(0); // 未登录
				// UserLogin.this.finish();
				if (turn) {
					finish();
				} else {
					turnToLogin();
				}

			}

		});

	}

	// 当选中记住用户密码的按钮时
	private void rem_password_num(boolean isChecked) {
		EditText password_edit = (EditText) findViewById(R.id.password_edit);
		String password_string = password_edit.getText().toString();
		if (isChecked) {
			shellRW.setUserLoginInfo("remPwd_checkBox", "true");
			shellRW.setUserLoginInfo("password", password_string);
		} else
			shellRW.setUserLoginInfo("remPwd_checkBox", "false");
		shellRW.setUserLoginInfo("password", "");
	}

	// 点击注册，完成注册
	// 以下是注册时调用的代码
	// TODO Auto-generated method stub
	/**
	 * 注册联网操作
	 */
	private void beginRegister() {

		EditText register_id_num_edit = (EditText) findViewById(R.id.register_id_num_edit);
		Editable register_id_num = register_id_num_edit.getText();
		final String register_id = String.valueOf(register_id_num);

		EditText register_username_edit = (EditText) findViewById(R.id.register_username_edit);
		Editable register_username = register_username_edit.getText();
		final String register_phone_num = String.valueOf(register_username);
		EditText register_password_edit = (EditText) findViewById(R.id.register_password_edit);
		Editable register_password = register_password_edit.getText();
		final String register_password_ = String.valueOf(register_password);
		showDialog(PROGRESS_VALUE);
		Thread regthread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// try{

				RegisterInterface registerInterface = new RegisterInterface();
				// String
				// error_code=registerInterface.register(String.valueOf(username_edit.getText()),
				// String.valueOf(register_password_edit.getText()),
				// String.valueOf(register_id_num_edit.getText()));
				String error_code = registerInterface.userregister(
						register_phone_num, register_password_, register_id);
				PublicMethod.myOutput("---------" + error_code);
				if (error_code.equals("0000")) {
					phonenum = register_phone_num;
					password = register_password_;
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} else if (error_code.equals("0013")) {
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				} else if (error_code.equals("4444")) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else if (error_code.equals("000012")) {
					Message msg = new Message();
					msg.what = 4;
					handler.sendMessage(msg);
				} else if (error_code.equals("00")) {
					Message msg = new Message();
					msg.what = 8;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				}
				// }catch(Exception e){
				// Message msg=new Message();
				// msg.what=0;
				// handler.sendMessage(msg);
				// }
			}
			// #if PolishDevice != Motorola/A1200

		});
		regthread.start();

	}

	/**
	 * 实现电话号码的记忆功能
	 */
	@Override
	protected void onResume() {
		super.onResume();

		SharedPreferences prefs = getPreferences(0);
		String restoredText = prefs.getString("phoneNumText", null);
		if (restoredText != null && !on) {
			phoneNum_edit.setText(restoredText, TextView.BufferType.EDITABLE);

			int selectionStart = prefs.getInt("selection-start", -1);
			int selectionEnd = prefs.getInt("selection-end", -1);
			if (selectionStart != -1 && selectionEnd != -1) {
				phoneNum_edit.setSelection(selectionStart, selectionEnd);
			}
		}
	}

	/**
	 * Any time we are paused we need to save away the current state, so it will
	 * be restored correctly when we are resumed.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (!on) {
			SharedPreferences.Editor editor = getPreferences(0).edit();
			editor
					.putString("phoneNumText", phoneNum_edit.getText()
							.toString());
			editor.putInt("selection-start", phoneNum_edit.getSelectionStart());
			editor.putInt("selection-end", phoneNum_edit.getSelectionEnd());
			editor.commit();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		PublicMethod.myOutput("--->>NoticePrizesOfLottery key:"
				+ String.valueOf(keyCode));
		switch (keyCode) {
		case 4: {
			if (configFlag == 1) {
				Intent intent = new Intent(this, UserLogin.class);
				this.startActivity(intent);
				break;
			}
		}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void afterTextChanged(Editable s) {
		PublicMethod.myOutput("***********afterTextChanged");
		String phoneNumber = phoneNum_edit.getText().toString();
		shellRW.setUserLoginInfo("phoneNumber", phoneNumber);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	// 记住手机号
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		PublicMethod.myOutput("***********onTextChanged");
	}
}
