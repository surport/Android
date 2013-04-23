package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.net.newtransaction.AccountDetailQueryInterface;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.net.newtransaction.BetQueryInterface;
import com.ruyicai.net.newtransaction.GiftQueryInterface;
import com.ruyicai.net.newtransaction.TrackQueryInterface;
import com.ruyicai.net.newtransaction.WinQueryInterface;
import com.ruyicai.net.newtransaction.pojo.AccountDetailQueryPojo;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ShellRWSharesPreferences;
/**
 * 用户中心
 * @author Administrator
 */
public class NewUserCenter extends Activity{
	
	ProgressDialog dialog;
	private List<Map<String, Object>> list;/* 列表适配器的数据源 */
	private static final String IICON = "IICON";
	private final static String TITLE = "TITLE"; /* 标题 */
	protected	String phonenum,sessionid,userno,certid;
	protected LinearLayout usecenerLinear;
	private final int DIALOG_FORGET_PASSWORD = 1;
	protected Button returnButton;
	protected TextView	titleTextView;
	String  jsonString;
	private String textStr;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.usercenter_mainlayout);
		
		usecenerLinear = (LinearLayout)findViewById(R.id.usercenterContent);
		returnButton = (Button)findViewById(R.id.layout_usercenter_img_return);
		titleTextView = (TextView)findViewById(R.id.usercenter_mainlayou_text_title);
		returnButton.setBackgroundResource(R.drawable.returnselecter);
		initReturn();
		usecenerLinear.addView(showView(NewUserCenter.this));
	}
	protected  void initReturn(){
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
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
			}
		}
	};
	   /**
	    * 用户中心列表
	    */
   protected View showView(Context context){
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (LinearLayout) inflate.inflate(R.layout.usercenter_layout, null);
		/* 用户中心查询信息列表 */
		ListView queryinfolist = (ListView) view.findViewById(R.id.usercenter_listview_queryinfo);
		list = getListForUserCenterAdapter(context);
		UerCenterAdapter adapter = new UerCenterAdapter(this,list);
		queryinfolist.setAdapter(adapter);
		queryinfolist.setOnItemClickListener(clickListener);
		/* 账户管理列表 */
		ListView manageaccountlist = (ListView) view.findViewById(R.id.usercenter_listview_manageaccount);
		list = getListForAccountAdapter();
		UerCenterAdapter adapterTwo = new UerCenterAdapter(this,list);
		manageaccountlist.setAdapter(adapterTwo);
		manageaccountlist.setOnItemClickListener(clickListener);
		return view;
   	}
   /**
    * 用户点击列表子项的监听器，实现对点击子项上text获取
    */
   OnItemClickListener clickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			TextView text = (TextView) view.findViewById(R.id.usercenter_item_text);
			textStr = text.getText().toString();
			isLogin();
		}
	};
	/**
	 *  获得用户中心列表适配器的数据源
	 * @return
	 */
	protected List<Map<String, Object>> getListForUserCenterAdapter(Context context) {
		String[] usercenterQueryInfoTitles = { context.getString(R.string.usercenter_winningCheck),
				this.getString(R.string.usercenter_bettingDetails),
				this.getString(R.string.usercenter_trackNumberInquiry),
				this.getString(R.string.ruyihelper_balanceInquiry),
				this.getString(R.string.usercenter_giftCheck) };
		int[] usercenterQueryInfoIcons = {R.drawable.usercenter_ico_winprize,R.drawable.usercenter_ico_betquery,
				R.drawable.usercenter_ico_track,R.drawable.usercenter_ico_balance,R.drawable.usercenter_ico_gift};
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
		for (int i = 0; i < usercenterQueryInfoTitles.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(TITLE, usercenterQueryInfoTitles[i]);
			map.put(IICON, usercenterQueryInfoIcons[i]);
			list.add(map);
		}
		return list;
	}
	/**
	 *  账户管理数据源
	 * @return
	 */
	protected List<Map<String, Object>> getListForAccountAdapter() {
		String[] titles = {this.getString(R.string.usercenter_accountDetails),
				this.getString(R.string.ruyihelper_accountWithdraw),
				this.getString(R.string.usercenter_passwordChange),this.getString(R.string.usercenter_bindID)};
		int[] accountDetailInfoIcons = {R.drawable.usercenter_ico_accountdetail,
				R.drawable.usercenter_ico_withdraw,
				R.drawable.usercenter_ico_changpw,R.drawable.usercenter_ico_bindid};
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
			Integer  iconid = (Integer)mList.get(position).get(IICON);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.usercenter_listitem,null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.usercenter_item_text);
				holder.title.setText(title);
				holder.lefticon = (ImageView)convertView.findViewById(R.id.usercenter_item_lefticon);
				holder.lefticon.setBackgroundResource(iconid);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			return convertView;
		}
		class ViewHolder {
			TextView title;
			ImageView lefticon;
		}
	}
   /**
    * 判断是否登陆
    */
   public void isLogin(){
		ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(this, "addInfo");
		phonenum = shellRW.getUserLoginInfo("phonenum");
		sessionid = shellRW.getUserLoginInfo("sessionid");
		certid = shellRW.getUserLoginInfo("certid");
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(this,UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			userCenterDetail();
		}
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
	    initPojo();
		// String userId=shellRW.getUserLoginInfo("userId");
		// 余额查询
	    if (this.getString(R.string.ruyihelper_balanceInquiry).equals(str)) {
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
			Intent intent = new Intent(NewUserCenter.this,ChangePasswordActivity.class);
			startActivity(intent);			
		}
		if (this.getString(R.string.usercenter_bindID).equals(str)) {
			Log.v("certid","certid="+certid);
			if(certid == null||certid == ""||certid=="null"){
				Intent intent = new Intent(NewUserCenter.this,BindIDActivity.class);
				startActivity(intent);	
			}else{
				showDialog(DIALOG_FORGET_PASSWORD);
			}
			
		}
		this.finishActivity(0);
	}
	/**
     * 重写返回建
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
	protected Dialog onCreateDialog(int id) {
		 dialog = new ProgressDialog(this);
      switch (id) {
      case DIALOG_FORGET_PASSWORD:
			LayoutInflater factory = LayoutInflater.from(this);
			final View forgetPwd = factory.inflate(R.layout.alert_dialog_forget_password, null);
			TextView remindText = (TextView)forgetPwd.findViewById(R.id.remind_forgetPasOrBind);
			remindText.setText(getString(R.string.usercenter_hadbindedremind)+"\n"+Html.fromHtml(getString(R.string.usercenter_hadbindedremindPh)));
			return new AlertDialog.Builder(NewUserCenter.this).setTitle(R.string.usercenter_bindIDdialogtitle)
					.setView(forgetPwd).setNegativeButton(R.string.return_button,new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
								}
							}).create();
          case 0: {
          
              dialog.setTitle("联网提示");
              dialog.setMessage("网络连接中......");
              dialog.setIndeterminate(true);
              dialog.setCancelable(true);
              return dialog;
          }
      }
      return dialog;
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
