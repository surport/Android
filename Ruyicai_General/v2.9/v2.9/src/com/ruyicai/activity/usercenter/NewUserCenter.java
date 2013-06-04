package com.ruyicai.activity.usercenter;

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
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.account.AccountActivity;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.join.JoinCheckActivity;
import com.ruyicai.activity.more.FeedBack;
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
import com.ruyicai.util.Constants;
import com.ruyicai.util.ShellRWSharesPreferences;
/**
 * 用户中心
 * @author Administrator
 */
public class NewUserCenter extends Activity{
	
	ProgressDialog dialog;
	private Dialog nicknameDialog;
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
	protected	String phonenum,sessionid,userno,certid,mobileid,name;
	protected LinearLayout usecenerLinear;
	private final int DIALOG_BINDED = 1,DIALOG_BINDPHONE = 2;
	protected Button returnButton;
	protected TextView	titleTextView;
	private boolean isgetscroe=true;//是否获取积分
//	private ShellRWSharesPreferences shellRW;
	String  jsonString;
	private String textStr;
	//用户积分。
	private String nickname="";
    private String balance="";
    private String score="";
    private String mobileiduser="";
    private String crididuser="";
    private ImageView scoreshow,cridbindim,phonebindim;
    private TextView nicknamecontent,mobilecontent,balacecontent,pointcontent, cridbindtx,phonebindtx;
    //用户中心功能区
    protected LinearLayout rechargelayout,cashlayout,detaillayout,pointlayout,winlayout,bettinglayout,numsearchlayout,giftlayout,myhemailayout,passwordlayout,critidlayout,mobilbindlayout,feedbacklayout;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayout);
		inituserpoint();
		dialog = UserCenterDialog.onCreateDialog(this);
//		usecenerLinear = (LinearLayout)findViewById(R.id.usercenterContent);
		returnButton = (Button)findViewById(R.id.layout_usercenter_img_return);
		titleTextView = (TextView)findViewById(R.id.usercenter_mainlayou_text_title);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		scoreshow = (ImageView)findViewById(R.id.userillustrations);
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this, "addInfo");
	    sessionid = shellRW.getUserLoginInfo("sessionid");
		initReturn();
		initsroreshow();
		initPojo();
//		usecenerLinear.addView(showView(NewUserCenter.this));
		initFuctionLayout();
	}
	protected  void initReturn(){
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
	}
	
	
	  private String  getnickname(String nickname){
	    	if(nickname.equals("")){
	    		return nickname;
	    	}else if(nickname.length()<=6){
	    		return nickname;
	    	}else{
	    		String name = nickname.substring(0,5)+"**";
	    		return name;
	    	}
	    	
	    }
	protected void initsroreshow() {
		    scoreshow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			isgetscroe=false;
		    LayoutInflater inflater = (LayoutInflater) NewUserCenter.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View   view = inflater.inflate(R.layout.userscroe_show, null);
		    WebView web = (WebView)view.findViewById(R.id.userscore_webview);
		    Button close = (Button)view.findViewById(R.id.usercenter_scrore_back);
		    String iFileName = "userscore_show.html";
			String url = "file:///android_asset/" + iFileName;
		    final Dialog dialog  = new Dialog(NewUserCenter.this,R.style.dialog);
		    dialog.setContentView(view);  
		    web.loadUrl(url);
		    close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
		    dialog.show();
			}
		});
	}

	//用户中心功能区
	protected void initFuctionLayout(){
		rechargelayout =(LinearLayout)findViewById(R.id.recharge_layout);
		cashlayout =(LinearLayout)findViewById(R.id.cash_layout);
		detaillayout =(LinearLayout)findViewById(R.id.detail_layout);
		pointlayout =(LinearLayout)findViewById(R.id.point_layout);
		winlayout =(LinearLayout)findViewById(R.id.win_layout);
		bettinglayout =(LinearLayout)findViewById(R.id.betting_layout);
		numsearchlayout =(LinearLayout)findViewById(R.id.numsearch_layout);
		giftlayout =(LinearLayout)findViewById(R.id.gift_layout);
		myhemailayout =(LinearLayout)findViewById(R.id.myhemai_layout);
		passwordlayout =(LinearLayout)findViewById(R.id.password_layout);
		critidlayout =(LinearLayout)findViewById(R.id.critid_layout);
		mobilbindlayout =(LinearLayout)findViewById(R.id.mobilebind_layout);
		feedbacklayout =(LinearLayout)findViewById(R.id.feedback_layout);
		clicklayout(cashlayout, "账户提现");
		clicklayout(detaillayout, "账户明细");
		clicklayout(winlayout, "中奖查询");
		clicklayout(pointlayout, "我的积分");
		clicklayout(bettinglayout, "投注查询");
		clicklayout(numsearchlayout, "追号查询");
		clicklayout(giftlayout, "赠送查询");
		clicklayout(passwordlayout, "密码修改");
		clicklayout(critidlayout, "身份证号绑定");
		clicklayout(mobilbindlayout, "手机号绑定");
		rechargelayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				Intent intent =new Intent(NewUserCenter.this,AccountActivity.class);
				intent.putExtra("isonKey", "fasle");
				startActivity(intent);
			}
		});
		myhemailayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			if(sessionid.equals("")||sessionid==null){
				Intent intent = new Intent(NewUserCenter.this, UserLogin.class);	
				startActivity(intent);
			}else{
			Intent intent = new Intent(NewUserCenter.this, JoinCheckActivity.class);	
			startActivity(intent);
			}
			}
		});
		feedbacklayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					getFeedbackListNet();
			}
		});
    
	}
	private void getFeedbackListNet() {
		dialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Constants.feedBackData = FeedBackListInterface.getInstance().getFeedbackList("0", "10",userno);
				try {
					Message msg = new Message();
					JSONObject feedjson = new JSONObject(Constants.feedBackData);
					String errorCode = feedjson.getString("error_code");
					if(errorCode.equals("0000")){
						Constants.feedBackJSONArray = feedjson.getJSONArray("result");
						if(Constants.feedBackJSONArray.length()==0){
							msg.what = 13;
						}else{
							msg.what = 11;
							msg.obj =  Constants.feedBackJSONArray;
						}
					}else{
						msg.what = 13;
						msg.obj =  feedjson.getString("message");
					}
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
		}).start();
		
	}
//用户积分显示控件	
	protected  void inituserpoint() {
	 nicknamecontent = (TextView)findViewById(R.id.nickcontent);
	 mobilecontent = (TextView)findViewById(R.id.mobilecontent);
	 balacecontent = (TextView)findViewById(R.id.lesscontent);
	 pointcontent = (TextView)findViewById(R.id.pointscontent);
	 cridbindtx = (TextView)findViewById(R.id.critidbindtx);
	 cridbindim = (ImageView)findViewById(R.id.critidbindimmmmm);
	 phonebindtx = (TextView)findViewById(R.id.phonebindtx);
	 phonebindim = (ImageView)findViewById(R.id.phonebindim);
	 
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
				Intent intent = new Intent(NewUserCenter.this,WinPrizeActivity.class);
				intent.putExtra("winjson",(String) msg.obj);
				startActivity(intent);
				break;
			case 3:
				dialog.dismiss();
				Intent intentbalance = new Intent(NewUserCenter.this,BalanceQueryActivity.class);
				intentbalance.putExtra("balancejson",(String) msg.obj);
				startActivity(intentbalance);
				break;
			case 4:
				dialog.dismiss();
				Intent intentbet = new Intent(NewUserCenter.this,BetQueryActivity.class);
				intentbet.putExtra("betjson",(String) msg.obj);
				startActivity(intentbet);
				break;
			case 5:
				dialog.dismiss();
				Intent intentgift = new Intent(NewUserCenter.this,GiftQueryActivity.class);
				intentgift.putExtra("giftjson",(String) msg.obj);
				startActivity(intentgift);
				break;
			case 6:
				dialog.dismiss();
				Intent intentaccount = new Intent(NewUserCenter.this,AccountDetailsActivity.class);
				intentaccount.putExtra("allaccountjson",(String) msg.obj);
				startActivity(intentaccount);
				break;
			case 7:
				dialog.dismiss();
				Intent intentTrack = new Intent(NewUserCenter.this,TrackQueryActivity.class);
				intentTrack.putExtra("trackjson",(String) msg.obj);
				startActivity(intentTrack);
				break;
			case 8:
				//积分
				if(nickname.equals("")){
				   nicknamecontent.setEnabled(true);
				   nicknamecontent.setText("(点击设置)");	
				   nicknamecontent.setOnClickListener(nicknameclick);
				}else {
				nicknamecontent.setText(getnickname(nickname));
				nicknamecontent.setEnabled(false);
				}
				if(mobileiduser.equals("")){
				mobilecontent.setEnabled(true);
				mobilecontent.setText("(立即绑定)");
				phonebindtx.setText("未绑定手机");		
				phonebindtx.setTextColor(Color.rgb(99, 99, 99));;
				phonebindim.setImageResource(R.drawable.phonebindno);
				mobilecontent.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(NewUserCenter.this,BindPhonenumActivity.class);
						startActivity(intent);	
					}
				});
				}else {
				mobilecontent.setEnabled(false);
				phonebindtx.setText("已绑定手机");
				phonebindtx.setTextColor(Color.rgb(33, 66, 33));
				phonebindim.setImageResource(R.drawable.phonebind);
				mobilecontent.setText(mobileiduser);
				}
				
				if(crididuser.equals("")){
					cridbindtx.setText("未绑定身份证");
					cridbindtx.setTextColor(Color.rgb(99, 99, 99));
					cridbindim.setImageResource(R.drawable.crididbindno);
				}else{
					cridbindtx.setText("已绑定身份证");
					cridbindtx.setTextColor(Color.rgb(33, 66, 33));
					cridbindim.setImageResource(R.drawable.crididbind);
				}
				balacecontent.setText(balance);
				pointcontent.setText(score);
				break;
			case 9:
				break;
			case 10:
				dialog.dismiss();
				Intent intentscroe = new Intent(NewUserCenter.this,UserScoreActivity.class);
				intentscroe.putExtra("scroe",(String) msg.obj);
				intentscroe.putExtra("myscroe", score);
				startActivity(intentscroe);
				break;
			case 11:
				dialog.dismiss();
				Intent feedListIntent = new Intent(NewUserCenter.this,FeedbackListActivity.class);
				feedListIntent.putExtra("feedBackArray","" + msg.obj);
				startActivity(feedListIntent);
				break;
			case 12:
				dialog.dismiss();
				Toast.makeText(NewUserCenter.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				break;
			case 13:
				dialog.dismiss();
				Toast.makeText(NewUserCenter.this, (String) msg.obj, Toast.LENGTH_LONG).show();
				Intent toFeedIntent = new Intent(NewUserCenter.this,FeedBack.class);
				startActivity(toFeedIntent);
				break;
			}
		}
	};
  //如果昵称未设置点击事件  
	OnClickListener nicknameclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  LayoutInflater inflater = (LayoutInflater) NewUserCenter.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    View view = inflater.inflate(R.layout.usercenter_bindphone, null);
			    nicknameDialog = new Dialog(NewUserCenter.this,R.style.dialog);
			    nicknameDialog.setContentView(view);
			    TextView title = (TextView)view.findViewById(R.id.usercenter_bindphonetitle);
			    TextView nicknamelable = (TextView)view.findViewById(R.id.usercenter_bindphonelabel);
			    nicknamelable.setText("昵称：");
			    final  EditText nickname = (EditText)view.findViewById(R.id.usercenter_edittext_bindphoneContext);
				Button cancle = (Button)view.findViewById(R.id.usercenter_bindphone_back);
				Button submit = (Button)view.findViewById(R.id.usercenter_bindphone_ok);
				submit.setText("提交");
				submit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final String nickname2=nickname.getText().toString();
						
						showDialog(0);
					    new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
							jsonString = SetupnicknameInterface.getInstance().setupnickname(userno, nickname2);
							try {
							JSONObject nicknameup = new JSONObject(jsonString);
							String errorcode = nicknameup.getString("error_code");
							final String message = nicknameup.getString("message");
							if(errorcode.equals("0000")){
							handler.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
							    Toast.makeText(NewUserCenter.this, message, Toast.LENGTH_LONG).show();	
							    nicknamecontent.setEnabled(false);
							    dialog.dismiss();
							    nicknameDialog.dismiss();
							    getusermessage();
								}
						        	});
						     	}else{
						     	handler.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										Toast.makeText(NewUserCenter.this, message, Toast.LENGTH_LONG).show();	
										dialog.dismiss();
									    nicknameDialog.dismiss();	
								     		
									}
								});
						     }
							}catch(Exception e){
								
						      	}
							}
						}).start();
						
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
	public void setnc(){
		phonebindtx.setTextColor(999999);
		phonebindim.setImageResource(R.drawable.phonebindno);
		cridbindtx.setText("");
		cridbindtx.setTextColor(Color.rgb(33, 66, 33));
		cridbindtx.setBackgroundResource(R.drawable.crididbindno);
	}
	//用户点击进入相关页面监听器
	protected void clicklayout(LinearLayout layout,final String str){
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
   public void isLogin(){
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		certid = shellRW.getUserLoginInfo("certid");
		name  = shellRW.getUserLoginInfo("name");
		mobileid = shellRW.getUserLoginInfo("mobileid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(this,UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			userCenterDetail();
		}
   }
   //获取用户信息
   private void getusermessage(){
	 nicknamecontent.setText("查询中...");
	 mobilecontent.setText("查询中...");
	 balacecontent.setText("查询中...");
	 pointcontent.setText("查询中...");
//	showDialog(0);  
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
		 	jsonString = QueryintegrationInterface.getInstance().queryintegration(phonenum, sessionid, userno);
		 	Message msg = new Message();
		 	try{
		 		JSONObject json = new JSONObject(jsonString);
		 		nickname = json.getString("nickName");
		 		mobileiduser = json.getString("mobileId");
		 		crididuser = json.getString("certId");
		 		balance = json.getString("balance");
		 		score = json.getString("score");
		 		msg.what=8;
		 		handler.sendMessage(msg);
		 	}catch(JSONException e){
		 		msg.what=9;
		 		handler.sendMessage(msg);
		 	}
		}
	}).start();
   }
 
	/**
	 *  将每个联网联网提出来 查询信息列表对应选项
	 * @param str
	 */
   private void initPojo(){
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		userno = shellRW.getUserLoginInfo("userno");
	}
   private void userCenterDetail() {
	    String str = textStr ;
		// 余额查询
	    if (this.getString(R.string.ruyihelper_balanceInquiry).equals(str)) {
	       isgetscroe=false;
		   showDialog(0);
			new Thread(new Runnable() {
				public void run() {
					jsonString = BalanceQueryInterface.balanceQuery(userno, sessionid, phonenum);
					Message msg = new Message();
					try {
						JSONObject aa = new JSONObject(jsonString);
						String errcode = aa.getString("error_code");
						String message = aa.getString("message");
						if(errcode.equals("0047")){
								msg.what = 1;
								msg.obj = message;
								handler.sendMessage(msg);					
							}else if(errcode.equals("0000")){
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
			isgetscroe=false;
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
					if(errcode.equals("0047")){
						
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);					
					}else if(errcode.equals("0000")){
							msg.what = 2;
							msg.obj = jsonString;
							handler.sendMessage(msg);
					}else{
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
		if (this.getString(R.string.usercenter_bettingDetails).equals(str)) {
			isgetscroe=false;
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
						if(errcode.equals("0047")){
								msg.what = 1;
								msg.obj = message;
								handler.sendMessage(msg);					
							}else if(errcode.equals("0000")){
								msg.what = 4;
								msg.obj = jsonString;
								handler.sendMessage(msg);
						}else{
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
		if (this.getString(R.string.usercenter_giftCheck).equals(str)) {
		    isgetscroe=false;
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
						if(errcode.equals("0047")){
								msg.what = 5;
								msg.obj = message;
								handler.sendMessage(msg);					
							}else if(errcode.equals("0000")){
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
						if(errcode.equals("0047")){
						
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);					
						}else if(errcode.equals("0000")){
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
			 isgetscroe=false;
			Log.v("mingxi","mingxi");
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
						if(errcode.equals("0047")){
						
							msg.what = 1;
							msg.obj = message;
							handler.sendMessage(msg);					
						}else if(errcode.equals("0000")){
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
			Intent intent = new Intent(NewUserCenter.this, AccountWithdrawActivity.class);
			startActivity(intent);	
		}
		// 密码修改
		if (this.getString(R.string.usercenter_passwordChange).equals(str)) {
			isgetscroe=false;
			Intent intent = new Intent(NewUserCenter.this,ChangePasswordActivity.class);
			startActivity(intent);			
		}
		//绑定手机号
		if (this.getString(R.string.usercenter_bindphonenum).equals(str)) {
			if(mobileid == null||mobileid.equals("")){
				Intent intent = new Intent(NewUserCenter.this,BindPhonenumActivity.class);
				startActivity(intent);	
			}else{
				Intent intentbalance = new Intent(NewUserCenter.this,HadBindedPhoneOrUnBindPhone.class);
				intentbalance.putExtra("mobileid",mobileid);
				startActivity(intentbalance);
			}
			
		}
		//绑定身份证
		if (this.getString(R.string.usercenter_bindID).equals(str)) {
			Log.v("certid111","certid="+certid);
			if(certid == null||certid.equals("")||certid.equals("null")){
				Intent intent = new Intent(NewUserCenter.this,BindIDActivity.class);
				startActivity(intent);	
			}else{
				Intent intentbalance = new Intent(NewUserCenter.this,HadBindedID.class);
				intentbalance.putExtra("certid",certid);
				intentbalance.putExtra("name",name);
				startActivity(intentbalance);
			}
			
		}
		//积分明细
		if(str.equals("我的积分")){
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
					if(errcode.equals("0047")){
						msg.what = 1;
						msg.obj = message;
						handler.sendMessage(msg);					
			    	}else if(errcode.equals("0000")){
						msg.what = 10;
						msg.obj = jsonString;
						handler.sendMessage(msg);
				    }else if(errcode.equals("9999")){
					
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
	/**   NH	H	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
     * 重写返回建JH
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		finish();
		return false;
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(sessionid!=null&&!sessionid.equals("")){
			if(isgetscroe){
			getusermessage();
			                 }
			}
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	@Override
	protected void onPause() {
		super.onPause();
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
       switch(resultCode){
       case RESULT_OK:
   		   isLogin();
    	   break;
       }
	}
}
