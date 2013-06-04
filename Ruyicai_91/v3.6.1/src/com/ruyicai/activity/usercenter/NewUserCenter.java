package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.account.AccountListActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.join.JoinCheckActivity;
import com.ruyicai.activity.more.FeedBack;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.dialog.LogOutDialog;
import com.ruyicai.dialog.MessageDialog;
import com.ruyicai.dialog.MyDialogListener;
import com.ruyicai.net.newtransaction.AccountDetailQueryInterface;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.FeedBackListInterface;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.QueryintegrationInterface;
import com.ruyicai.net.newtransaction.SetupnicknameInterface;
import com.ruyicai.net.newtransaction.TrackQueryInterface;
import com.ruyicai.net.newtransaction.UserScoreDetailQueryInterface;
import com.ruyicai.net.newtransaction.WinQueryInterface;
import com.ruyicai.net.newtransaction.pojo.AccountDetailQueryPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.net.newtransaction.pojo.UserScroeDetailQueryPojo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;
import com.umeng.xp.view.T;

/**
 * 用户中心
 * 
 * @author Administrator
 */
public class NewUserCenter extends Activity implements MyDialogListener {
	LogOutDialog logOutDialog;// 注销框
	ProgressDialog dialog, progressDialog;
	private Dialog nicknameDialog;
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
	protected String phonenum, sessionid, userno, certid, mobileid, name;
	protected LinearLayout usecenerLinear;
	private final int DIALOG_BINDED = 1, DIALOG_BINDPHONE = 2;
	protected Button returnButton;
	protected TextView titleTextView;
	private boolean isgetscroe = true;// 是否获取积分
	// private ShellRWSharesPreferences shellRW;
	String jsonString;
	private String textStr;
	// 用户积分。
	private String username = "", nickname = "", balance = "", score = "", mobileiduser = "", crididuser = "",
			isAgency = "";// 1代表有，0代表没有
	private ImageView scoreshow, cridbindim, phonebindim;
	private TextView nicknamecontent, mobilecontent, balacecontent, pointcontent, cridbindtx, phonebindtx;
	private ListView usermoneylist, usercaipiaolist, usersetlist;
	Activity context;
	RWSharedPreferences shellRW;
	private LinearLayout usercentertop,usercenterrigist_no;
	private Button  register,login;
	private TextView registertextview,logintextview;

	// 用户中心功能区
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayout);
		context = this;
		usercentertop=(LinearLayout)findViewById(R.id.usercentertable);
		usercenterrigist_no=(LinearLayout)findViewById(R.id.regist_no);
		inituserpoint();
		dialog = UserCenterDialog.onCreateDialog(this);

		// usecenerLinear = (LinearLayout)findViewById(R.id.usercenterContent);
		returnButton = (Button) findViewById(R.id.layout_usercenter_img_return);
		titleTextView = (TextView) findViewById(R.id.usercenter_mainlayou_text_title);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		scoreshow = (ImageView) findViewById(R.id.userillustrations);
		shellRW = new RWSharedPreferences(this, "addInfo");
		initlogin_no();
		initReturn();
		initsroreshow();
		initPojo();
		// usecenerLinear.addView(showView(NewUserCenter.this));
		initFuctionLayout();
		MobclickAgent.onEvent(this,"yonghuzhongxin" ); //BY贺思明 点击主导航上的“用户中心”。

	}
	
	
    protected void 	initlogin_no(){
    	 register=(Button)findViewById(R.id.usercenter_regiset);
    	 login=(Button)findViewById(R.id.usercenter_login);
    	 login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NewUserCenter.this, UserLogin.class);
				startActivity(intent);
			}
		 });
    	 register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NewUserCenter.this, UserLogin.class);
				intent.putExtra("regist", "regist");
				startActivity(intent);
			}
		});
    	 logintextview=(TextView)findViewById(R.id.usercenterlogin);
    	 registertextview=(TextView)findViewById(R.id.usercenterregister);
    	 logintextview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NewUserCenter.this, UserLogin.class);
				startActivity(intent);
			}
		 });
    	 registertextview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(NewUserCenter.this, UserLogin.class);
				intent.putExtra("regist", "regist");
				startActivity(intent);
			}
		});
    
    }

	protected void initReturn() {
		returnButton.setText("注销");
		returnButton.setVisibility(View.VISIBLE);
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				logOutDialog = LogOutDialog.createDialog(context);
				logOutDialog.setOnClik(NewUserCenter.this);
			}
		});
	}

	private String getnickname(String nickname) {
		if (nickname.length() <= 6) {
			return nickname;
		} else {
			String name = nickname.substring(0, 5) + "**";
			return name;
		}

	}

	protected void initsroreshow() {
		scoreshow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new ScroesRules(NewUserCenter.this);
			}
		});
	}

	// 用户中心功能区
	protected void initFuctionLayout() {
		List<Map<String, Object>> list1 = getListForUsermonyeAdapter();
		List<Map<String, Object>> list2 = getListForAccountAdapter();
		List<Map<String, Object>> list3 = getListForusersetAdapter();
		UerCenterAdapter adapter1 = new UerCenterAdapter(this, list1);
		UerCenterAdapter adapter2 = new UerCenterAdapter(this, list2);
		UerCenterAdapter adapter3 = new UerCenterAdapter(this, list3);
		usermoneylist = (ListView) findViewById(R.id.usermoneylist);
		usercaipiaolist = (ListView) findViewById(R.id.usercaipiaolist);
		usersetlist = (ListView) findViewById(R.id.usersetlist);
		usermoneylist.setAdapter(adapter1);
		usercaipiaolist.setAdapter(adapter2);
		usersetlist.setAdapter(adapter3);
		usermoneylist.setOnItemClickListener(clickListener);
		usercaipiaolist.setOnItemClickListener(clickListener);
		usersetlist.setOnItemClickListener(clickListener);
		setListViewHeightBasedOnChildren(usermoneylist);
		setListViewHeightBasedOnChildren(usersetlist);

	}

	// 设置listview 高度
	public void setListViewHeightBasedOnChildren(ListView listView) {

		ListAdapter listAdapter; // 取得listview绑定的适配器

		if (listView.getAdapter() == null) {

			return;

		}
		listAdapter = listView.getAdapter();

		ViewGroup.LayoutParams params = listView.getLayoutParams(); // 取得listview所在布局的参数

		params.height = PublicMethod.getPxInt(40, this) * (listAdapter.getCount());

		listView.setLayoutParams(params); // 改变listview所在布局的参数
	}

	private void getFeedbackListNet() {
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Constants.feedBackData = FeedBackListInterface.getInstance().getFeedbackList("0", "10", userno);
				try {
					Message msg = new Message();
					JSONObject feedjson = new JSONObject(Constants.feedBackData);
					String errorCode = feedjson.getString("error_code");
					if (errorCode.equals("0000")) {
						Constants.feedBackJSONArray = feedjson.getJSONArray("result");
						if (Constants.feedBackJSONArray.length() == 0) {
							msg.what = 13;
						} else {
							msg.what = 11;
							msg.obj = Constants.feedBackJSONArray;
						}
					} else {
						msg.what = 13;
						msg.obj = feedjson.getString("message");
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	// 用户积分显示控件
	protected void inituserpoint() {
		nicknamecontent = (TextView) findViewById(R.id.nickcontent);
		mobilecontent = (TextView) findViewById(R.id.mobilecontent);
		balacecontent = (TextView) findViewById(R.id.lesscontent);
		pointcontent = (TextView) findViewById(R.id.pointscontent);
		cridbindtx = (TextView) findViewById(R.id.critidbindtx);
		cridbindim = (ImageView) findViewById(R.id.critidbindimmmmm);
		phonebindtx = (TextView) findViewById(R.id.phonebindtx);
		phonebindim = (ImageView) findViewById(R.id.phonebindim);
		clickTextView(phonebindtx, this.getString(R.string.usercenter_bindphonenum));
		clickTextView(cridbindtx, this.getString(R.string.usercenter_bindID));
	}

	/**
	 * 处理登录的消息及注册的消息
	 */
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Toast.makeText(NewUserCenter.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 2:
				dialog.dismiss();
				Intent intent = new Intent(NewUserCenter.this, WinPrizeActivity.class);
				intent.putExtra("winjson", (String) msg.obj);
				startActivity(intent);
				break;
			case 3:
				dialog.dismiss();
				Intent intentbalance = new Intent(NewUserCenter.this, BalanceQueryActivity.class);
				intentbalance.putExtra("balancejson", (String) msg.obj);
				startActivity(intentbalance);
				break;
			case 4:
				dialog.dismiss();
				Intent intentbet = new Intent(NewUserCenter.this, BetQueryActivity.class);
				intentbet.putExtra("betjson", (String) msg.obj);
				startActivity(intentbet);
				break;
			case 5:
				dialog.dismiss();
				Intent intentgift = new Intent(NewUserCenter.this, GiftQueryActivity.class);
				intentgift.putExtra("giftjson", (String) msg.obj);
				startActivity(intentgift);
				break;
			case 6:
				dialog.dismiss();
				Intent intentaccount = new Intent(NewUserCenter.this, AccountDetailsActivity.class);
				intentaccount.putExtra("allaccountjson", (String) msg.obj);
				startActivity(intentaccount);
				break;
			case 7:
				dialog.dismiss();
				Intent intentTrack = new Intent(NewUserCenter.this, TrackQueryActivity.class);
				intentTrack.putExtra("trackjson", (String) msg.obj);
				startActivity(intentTrack);
				break;
			case 8:
				// 积分
				if (nickname.equals("")) {
					nicknamecontent.setEnabled(true);
					nicknamecontent.setTextColor(Color.BLUE);
					nicknamecontent.setText("(点击设置)");
					nicknamecontent.setOnClickListener(nicknameclick);
				} else {
					nicknamecontent.setText(getnickname(nickname));
					nicknamecontent.setEnabled(false);
					nicknamecontent.setTextColor(Color.BLACK);
				}
				mobilecontent.setTextColor(Color.BLACK);
				mobilecontent.setText(getnickname(username));
				if (mobileiduser.equals("")) {
					phonebindtx.setText("未绑定手机");
					phonebindtx.setTextColor(Color.rgb(99, 99, 99));
					phonebindim.setImageResource(R.drawable.phonebindno);
					phonebindtx.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(NewUserCenter.this, BindPhonenumActivity.class);
							startActivity(intent);
						}
					});
				} else {
					phonebindtx.setText("已绑定手机");
					phonebindtx.setTextColor(Color.rgb(33, 66, 33));
					phonebindim.setImageResource(R.drawable.phonebind);
				}

				if (crididuser.equals("")) {
					cridbindtx.setText("未绑定身份证");
					cridbindtx.setTextColor(Color.rgb(99, 99, 99));
					cridbindim.setImageResource(R.drawable.crididbindno);
				} else {
					cridbindtx.setText("已绑定身份证");
					cridbindtx.setTextColor(Color.rgb(33, 66, 33));
					cridbindim.setImageResource(R.drawable.crididbind);
				}
				balacecontent.setText(balance);
				pointcontent.setText(score);
				initFuctionLayout();
				break;
			case 9:
				break;
			case 10:
				dialog.dismiss();
				Intent intentscroe = new Intent(NewUserCenter.this, UserScoreActivity.class);
				intentscroe.putExtra("scroe", (String) msg.obj);
				intentscroe.putExtra("myscroe", score);
				startActivity(intentscroe);
				break;
			case 11:
				dialog.dismiss();
				Intent feedListIntent = new Intent(NewUserCenter.this, FeedbackListActivity.class);
				feedListIntent.putExtra("feedBackArray", "" + msg.obj);
				startActivity(feedListIntent);
				break;
			case 12:
				dialog.dismiss();
				Toast.makeText(NewUserCenter.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 13:
				dialog.dismiss();
				Toast.makeText(NewUserCenter.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				Intent toFeedIntent = new Intent(NewUserCenter.this, FeedBack.class);
				startActivity(toFeedIntent);
				break;
			}
		}
	};
	// 如果昵称未设置点击事件
	OnClickListener nicknameclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) NewUserCenter.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.usercenter_bindphone, null);
			nicknameDialog = new Dialog(NewUserCenter.this, R.style.dialog);
			nicknameDialog.setContentView(view);
			TextView title = (TextView) view.findViewById(R.id.usercenter_bindphonetitle);
			TextView nicknamelable = (TextView) view.findViewById(R.id.usercenter_bindphonelabel);
			nicknamelable.setText("昵称：");
			final EditText nickname = (EditText) view.findViewById(R.id.usercenter_edittext_bindphoneContext);
			Button cancle = (Button) view.findViewById(R.id.usercenter_bindphone_back);
			Button submit = (Button) view.findViewById(R.id.usercenter_bindphone_ok);
			submit.setText("提交");
			submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					final String nickname2 = nickname.getText().toString();
					if (nicknameIsRight(nickname2)) {
						showDialog(0);
						new Thread(new Runnable() {
	
							@Override
							public void run() {
								// TODO Auto-generated method stub
								jsonString = SetupnicknameInterface.getInstance()
										.setupnickname(userno, nickname2);
								try {
									JSONObject nicknameup = new JSONObject(
											jsonString);
									String errorcode = nicknameup
											.getString("error_code");
									final String message = nicknameup
											.getString("message");
									if (errorcode.equals("0000")) {
										handler.post(new Runnable() {
	
											@Override
											public void run() {
												// TODO Auto-generated method stub
												Toast.makeText(NewUserCenter.this,
														message, Toast.LENGTH_LONG)
														.show();
												nicknamecontent.setEnabled(false);
												dialog.dismiss();
												nicknameDialog.dismiss();
												getusermessage();
											}
										});
									} else {
										handler.post(new Runnable() {
	
											@Override
											public void run() {
												// TODO Auto-generated method stub
												Toast.makeText(NewUserCenter.this,
														message, Toast.LENGTH_LONG)
														.show();
												dialog.dismiss();
												nicknameDialog.dismiss();
	
											}
										});
									}
								} catch (Exception e) {
	

								}
							}
						}).start();
					}
				}
			});
			cancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					nicknameDialog.dismiss();
				}
			});
			title.setText("设置昵称");
			nicknameDialog.show();
		}
	};

	private boolean nicknameIsRight(String nickname) {
		boolean isRight = true;

		int chineseNum = (nickname.getBytes().length - nickname.length()) / 2;
		int charNum = nickname.length() - chineseNum;
		int positonNum = chineseNum * 2 + charNum;

		if (positonNum < 4 || positonNum > 16) {
			isRight = false;
			Toast.makeText(this, "昵称应该为4-16个字符", 1).show();
		}else if(nickname.indexOf(" ") > -1 || nickname.indexOf("　") > -1){
			isRight = false;
			Toast.makeText(this, "您输入的昵称含有非法字符，请重新输入。", 1).show();
		}

		return isRight;
	}

	public void setnc() {
		phonebindtx.setTextColor(999999);
		phonebindim.setImageResource(R.drawable.phonebindno);
		cridbindtx.setText("");
		cridbindtx.setTextColor(Color.rgb(33, 66, 33));
		cridbindtx.setBackgroundResource(R.drawable.crididbindno);
	}

	// 用户点击进入相关页面监听器
	protected void clickTextView(TextView layout, final String str) {
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textStr = str;
				isLogin();
			}
		});
	}

	/**
	 * 判断是否登陆
	 */
	public void isLogin() {
		RWSharedPreferences shellRW = new RWSharedPreferences(this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		certid = shellRW.getStringValue("certid");
		name = shellRW.getStringValue("name");
		mobileid = shellRW.getStringValue("mobileid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			userCenterDetail();
		}
	}

	// 获取用户信息
	private void getusermessage() {
		nicknamecontent.setText("查询中...");
		mobilecontent.setText("查询中...");
		balacecontent.setText("查询中...");
		pointcontent.setText("查询中...");
		// showDialog(0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				jsonString = QueryintegrationInterface.getInstance().queryintegration(phonenum, sessionid, userno);
				Message msg = new Message();
				try {
					JSONObject json = new JSONObject(jsonString);
					nickname = json.getString("nickName");
					crididuser = json.getString("certId");
					balance = json.getString("bet_balance");
					score = json.getString("score");
					username = json.getString("userName");
					mobileiduser = json.getString("mobileId");
					isAgency = json.getString("agencyChargeRight");
					msg.what = 8;
					handler.sendMessage(msg);
				} catch (JSONException e) {
					msg.what = 9;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	/**
	 * 将每个联网联网提出来 查询信息列表对应选项
	 * 
	 * @param str
	 */
	private void initPojo() {
		phonenum = shellRW.getStringValue(ShellRWConstants.PHONENUM);
		sessionid = shellRW.getStringValue(ShellRWConstants.SESSIONID);
		userno = shellRW.getStringValue(ShellRWConstants.USERNO);
	}

	private void userCenterDetail() {
		String str = textStr;
		// 代理充值
		if (str.equals(getString(R.string.user_agency))) {
			Intent intent = new Intent(NewUserCenter.this, UserAgencyActivity.class);
			startActivity(intent);
		}
		// 充值
		if (str.equals("账户充值")) {
			Intent intent = new Intent(NewUserCenter.this, AccountListActivity.class);
			intent.putExtra("isonKey", "fasle");
			startActivity(intent);
		}
		// 合买查询
		if (str.equals("我的合买")) {
			Intent intent = new Intent(NewUserCenter.this, JoinCheckActivity.class);
			startActivity(intent);
		}
		// 留言
		if (str.equals("我的留言")) {
			getFeedbackListNet();
		}
		// 余额查询
		if (this.getString(R.string.ruyihelper_balanceInquiry).equals(str)) {
			isgetscroe = false;
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					jsonString = BalanceQueryInterface.balanceQuery(userno, sessionid, phonenum);
					Message msg = new Message();
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 3;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						msg.what = 4;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
		// 中奖查询
		if (this.getString(R.string.usercenter_winningCheck).equals(str)) {
			isgetscroe = false;
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
					winQueryPojo.setUserno(userno);
					winQueryPojo.setSessionid(sessionid);
					winQueryPojo.setPhonenum(phonenum);
					winQueryPojo.setPageindex("0");
					winQueryPojo.setMaxresult("10");
					winQueryPojo.setType("win");
					Message msg = new Message();
					jsonString = WinQueryInterface.getInstance().winQuery(winQueryPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {

							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 2;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						} else {
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						msg.what = 2;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
		// 投注查询
		if (("投注记录").equals(str)) {
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					BetAndWinAndTrackAndGiftQueryPojo betQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
					betQueryPojo.setUserno(userno);
					betQueryPojo.setSessionid(sessionid);
					betQueryPojo.setPhonenum(phonenum);
					betQueryPojo.setPageindex("0");
					betQueryPojo.setMaxresult("10");
					betQueryPojo.setType("bet");

					Message msg = new Message();
					jsonString = BetQueryInterface.getInstance().betQuery(betQueryPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 4;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						} else {
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						msg.what = 2;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
		// 赠送查询
		if (("赠送查询").equals(str)) {
			isgetscroe = false;
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					BetAndWinAndTrackAndGiftQueryPojo giftQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
					giftQueryPojo.setUserno(userno);
					giftQueryPojo.setSessionid(sessionid);
					giftQueryPojo.setPhonenum(phonenum);
					giftQueryPojo.setPageindex("1");
					giftQueryPojo.setMaxresult("10");
					giftQueryPojo.setType("gift");

					Message msg = new Message();
					jsonString = GiftQueryInterface.getInstance().giftQuery(giftQueryPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {
							msg.what = 5;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 5;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
					}
				}
			}).start();
		}
		// 追号查询
		if (this.getString(R.string.usercenter_trackNumberInquiry).equals(str)) {
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					BetAndWinAndTrackAndGiftQueryPojo winQueryPojo = new BetAndWinAndTrackAndGiftQueryPojo();
					winQueryPojo.setUserno(userno);
					winQueryPojo.setSessionid(sessionid);
					winQueryPojo.setPhonenum(phonenum);
					winQueryPojo.setPageindex("0");
					winQueryPojo.setMaxresult("10");
					winQueryPojo.setType("track");

					Message msg = new Message();
					jsonString = TrackQueryInterface.getInstance().trackQuery(winQueryPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {

							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 7;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {
						msg.what = 7;
						msg.obj = jsonString;
						handler.sendMessage(msg);
					}
				}
			}).start();
		}
		// 账户明细
		if (this.getString(R.string.usercenter_accountDetails).equals(str)) {
			isgetscroe = false;
			showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					AccountDetailQueryPojo accountDetailPojo = new AccountDetailQueryPojo();
					accountDetailPojo.setUserno(userno);
					accountDetailPojo.setSessionid(sessionid);
					accountDetailPojo.setPhonenum(phonenum);
					accountDetailPojo.setPageindex("0");
					accountDetailPojo.setTransactiontype("0");
					accountDetailPojo.setMaxresult("10");
					accountDetailPojo.setType("new");

					Message msg = new Message();
					jsonString = AccountDetailQueryInterface.getInstance().accountDetailQuery(accountDetailPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {

							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 6;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {

					}
				}
			}).start();
		}
		// 账户提现
		if (this.getString(R.string.ruyihelper_accountWithdraw).equals(str)) {
			if(crididuser.equals("")){
				MessageDialog dialog=new MessageDialog(this, "提示", "为了保障您的账户安全，请您先完成身份信息后再进行提现");
				dialog.showDialog();
			}else{
			Intent intent = new Intent(NewUserCenter.this, AccountWithdrawActivity.class);
			startActivity(intent);
			}
		}
		// 密码修改
		if (this.getString(R.string.usercenter_passwordChange).equals(str)) {
			isgetscroe = false;
			Intent intent = new Intent(NewUserCenter.this, ChangePasswordActivity.class);
			startActivity(intent);
		}
		// 绑定手机号
		if (this.getString(R.string.usercenter_bindphonenum).equals(str)) {
			Log.e(mobileid, mobileid + "Log.e(certid,certid);");
			if (mobileid == null || mobileid.equals("")) {
				Intent intent = new Intent(NewUserCenter.this, BindPhonenumActivity.class);
				startActivity(intent);
			} else {
				Intent intentbalance = new Intent(NewUserCenter.this, HadBindedPhoneOrUnBindPhone.class);
				intentbalance.putExtra("mobileid", mobileid);
				startActivity(intentbalance);
			}

		}
		// 绑定身份证
		if (this.getString(R.string.usercenter_bindID).equals(str)) {
			if (certid == null || certid.equals("") || certid.equals("null")) {
				Intent intent = new Intent(NewUserCenter.this, BindIDActivity.class);
				startActivity(intent);
			} else {
				Intent intentbalance = new Intent(NewUserCenter.this, HadBindedID.class);
				intentbalance.putExtra("certid", certid);
				intentbalance.putExtra("name", name);
				startActivity(intentbalance);
			}

		}
		// 积分明细
		if (str.equals("我的积分")) {
			showDialog(0);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					UserScroeDetailQueryPojo scroeDetailPojo = new UserScroeDetailQueryPojo();
					scroeDetailPojo.setUserno(userno);
					scroeDetailPojo.setSessionid(sessionid);
					scroeDetailPojo.setPhonenum(phonenum);
					scroeDetailPojo.setPageindex("0");
					scroeDetailPojo.setMaxresult("10");
					scroeDetailPojo.setType("scoreDetail");
					Message msg = new Message();
					jsonString = UserScoreDetailQueryInterface.getInstance().scroeDetailQuery(scroeDetailPojo);
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if (errcode.equals("0047")) {
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						} else if (errcode.equals("0000")) {
							msg.what = 10;
							msg.obj = jsonString;
							handler.sendMessage(msg);
						} else if (errcode.equals("9999")) {

							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);
						}
					} catch (Exception e) {

					}
				}
			}).start();
		}
		this.finishActivity(0);
	}

	/**
	 * 用户中心的适配器
	 */
	public class UerCenterAdapter extends BaseAdapter {
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;

		public UerCenterAdapter(Context context, List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}

		public int getCount() {
			return mList.size();
		}

		public Object getItem(int position) {
			return mList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			String title = (String) mList.get(position).get(TITLE);
			Integer iconid = (Integer) mList.get(position).get(IICON);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.usercenter_listitem, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.usercenter_item_text);
				holder.title.setText(title);
				holder.lefticon = (ImageView) convertView.findViewById(R.id.usercenter_item_lefticon);
				holder.lefticon.setBackgroundResource(iconid);
				holder.righticon = (ImageView) convertView.findViewById(R.id.usercenter_item_righticon);
				holder.righticon.setVisibility(ImageView.VISIBLE);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}

		class ViewHolder {
			TextView title;
			ImageView lefticon;
			ImageView righticon;
		}
	}

	/**
	 * 用户点击列表子项的监听器，实现对点击子项上text获取
	 */
	OnItemClickListener clickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			TextView text = (TextView) view.findViewById(R.id.usercenter_item_text);
			textStr = text.getText().toString();
			isLogin();
		}
	};

	/**
	 * 获得用户资金数据
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForUsermonyeAdapter() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		if (isAgency.equals("1")) {
			String[] usercenterQueryInfoTitles = { "账户充值", "账户提现", "账户明细", "资金详情", "我的积分",
					getString(R.string.user_agency) };
			int[] usercenterQueryInfoIcons = { R.drawable.putinmoney, R.drawable.tixianw, R.drawable.zhanghumingxi,
					R.drawable.zijinxiangqing, R.drawable.usercroe, R.drawable.user_agency };
			for (int i = 0; i < usercenterQueryInfoTitles.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, usercenterQueryInfoTitles[i]);
				map.put(IICON, usercenterQueryInfoIcons[i]);
				list.add(map);
			}
		} else {
			String[] usercenterQueryInfoTitles = { "账户充值", "账户提现", "账户明细", "资金详情", "我的积分" };
			int[] usercenterQueryInfoIcons = { R.drawable.putinmoney, R.drawable.tixianw, R.drawable.zhanghumingxi,
					R.drawable.zijinxiangqing, R.drawable.usercroe };
			for (int i = 0; i < usercenterQueryInfoTitles.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, usercenterQueryInfoTitles[i]);
				map.put(IICON, usercenterQueryInfoIcons[i]);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 获得我的彩票数据
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForAccountAdapter() {
		String[] titles = { "中奖查询", "投注记录", "追号查询", "赠送查询", "我的合买" };
		int[] accountDetailInfoIcons = { R.drawable.zhoangjiangchaxun, R.drawable.touzhujilu, R.drawable.zhuihaochaxun,
				R.drawable.zengcaichaxun, R.drawable.wodehemai };
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		for (int i = 0; i < titles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, titles[i]);
			map.put(IICON, accountDetailInfoIcons[i]);
			list.add(map);
		}
		return list;
	}

	/**
	 * 获得用户设置数据
	 * 
	 * @return
	 */
	protected List<Map<String, Object>> getListForusersetAdapter() {
		String[] titles = { "密码修改", "身份证绑定", "手机号绑定", "我的留言" };
		String[] titles1 = { "身份证绑定", "手机号绑定", "我的留言" };

		int[] accountDetailInfoIcons = { R.drawable.mimaxiugai, R.drawable.sfzbd, R.drawable.mobilebindlable,
				R.drawable.myliuyan };
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		if (PublicConst.isthirdlogin) {
			for (int i = 0; i < titles1.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, titles1[i]);
				map.put(IICON, accountDetailInfoIcons[i]);
				list.add(map);
			}
		} else {
			for (int i = 0; i < titles.length; i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put(TITLE, titles[i]);
				map.put(IICON, accountDetailInfoIcons[i]);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * NH H 重写返回建JH
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		ExitDialogFactory.createExitDialog(this);
		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);// BY贺思明 2012-7-24
		initPojo();
		if (sessionid != null && !sessionid.equals("")) {
			getusermessage();
			usercentertop.setVisibility(View.VISIBLE);
			usercenterrigist_no.setVisibility(View.GONE);
		}else{
			usercentertop.setVisibility(View.INVISIBLE);
			usercenterrigist_no.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
//		PublicMethod.myOutLog("onRestart", "onRestart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);// BY贺思明 2012-7-24
		PublicMethod.myOutLog("onPause", "onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			dialog = new ProgressDialog(this);
			dialog.setMessage("网络连接中...");
			dialog.setIndeterminate(true);
			return dialog;
		}
		}
		return null;
	}

	/**
	 * 从上一个activity返回当前activity执行的方法
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case RESULT_OK:
			isLogin();
			break;
		}
	}

	@Override
	public void onOkClick() {
		// TODO Auto-generated method stub
		Constants.hasLogin = false;
		PublicConst.isthirdlogin = false;
		NewUserCenter.this.finish();
		logOutDialog.clearLastLoginInfo();
	}

	@Override
	public void onCancelClick() {
		// TODO Auto-generated method stub

	}
}
