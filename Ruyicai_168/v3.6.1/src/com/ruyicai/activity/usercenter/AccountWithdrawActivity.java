/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
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
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid168.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BalanceQueryInterface;
import com.ruyicai.net.newtransaction.CancelWithdrawInterface;
import com.ruyicai.net.newtransaction.ChangeWithdrawInterface;
import com.ruyicai.net.newtransaction.QueryDNAInterface;
import com.ruyicai.net.newtransaction.QueryLatelyWithdrawInterface;
import com.ruyicai.net.newtransaction.QueryMoneyInterface;
import com.ruyicai.net.newtransaction.pojo.ChangeWithdrawPojo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 账户提现
 * @author Administrator
 *
 */
public class AccountWithdrawActivity extends Activity implements HandlerMsg{
	private String re;
	private String[] titles = {"提现到银行卡","提现到支付宝","提现记录"};
	private JSONObject obj;	
	private String phonenum,sessionid,userno;
	private boolean start = true;	
	private int state = 0;
	boolean isfirst = true;
	private LinearLayout cashrecode;
	View tabSpecLinearView;//子列表的ListView
    ListView tabSpecListView;//子列表的ListView
    ProgressBar progressbar;
   	View view;
   	private int TianxianOrRecord = 0;//when it value = 1 is Tixian else if value = 2 is record
   	private int recordChange = 0;//提现记录是否在提现是发生变化 变化则为1 不变化 则为0
	TextView nameText,bankText,addText,numText,moneyText;//待审核
	TabHost mTabHost;
	private LayoutInflater mInflater = null;
	Vector WinningVector;
	String bankName = "00";
	String moneyNum = "00";
	String bankaccount;
	String name;
	String areaName;
	Button b_canceltranking;
	JSONObject json;
	private int allpages=0;
	private int pageindex=0;
	MyHandler handler;//自定义handler
	Handler handlerTwo = new Handler();
	private ProgressDialog progressdialog;
	AccountInfo info = new AccountInfo();
	List<csahInfo> cashdatalist = new ArrayList<csahInfo>(); 
	private int[] linearId = {R.id.usercenterscroedetail,R.id.usercenterscroe_zfb,R.id.usercenterscroechange};
	boolean zhankai = true;//提现记录失败展开原因
    private static final String[] allBankName={"中国工商银行","中国农业银行","中国建设银行","中国民生银行","招商银行","中国邮政储蓄银行","交通银行",
    	                                       "兴业银行","中信银行","中国光大银行","广东发展银行","上海浦东发展银行","深圳发展银行","杭州银行"};   
    String allName[] = null;
    List allcountries=null;   
    ArrayAdapter<String> adapter; 
    CashcodeAdapter adaptercash;
    boolean isQuery=true;//是否是查询提现
    String message= "";
    RWSharedPreferences shellRW;
    String drawBStr;//可提现金额 "￥元"
    String drawMoney;//可提现金额￥
    TextView drawbalanceText ;
    private boolean isBindState = false; 
    private Context context;
	 EditText namezfb;
	 EditText money_bank_num ;
	 EditText money;
	 EditText passwordzfb;
	 boolean  isgetzfb=false;
	private final int MAX_AMT = 50000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.getmoneymain);
		context = this;
		mTabHost = (TabHost) findViewById(R.id.usercenter_tab_host);
		mTabHost.setup();
		mInflater = LayoutInflater.from(this);
		for(int i=0;i<titles.length;i++){
			addTab(i); 
		}
		mTabHost.setOnTabChangedListener(scroeTabChangedListener);
		handler = new MyHandler(this);
		shellRW = new RWSharedPreferences(AccountWithdrawActivity.this, "addInfo");
		getInfo();
		checkDNA();
//		stateNet();
	}
	
	
	
	/**
	 * TabHost切换监听器
	 */
	TabHost.OnTabChangeListener scroeTabChangedListener = new TabHost.OnTabChangeListener() {
		public void onTabChanged(String tabId) {	
				if(tabId.equals(titles[0])){
					TianxianOrRecord = 0;
//				initLinear(scroedetail, linearId[0], view);
				}else if(tabId.equals(titles[1])){
					if(isBindState){
						Toast.makeText(context, "由于您使用DNA充值已绑定了提现银行卡，无法提现到支付宝账号", Toast.LENGTH_SHORT).show();
					}else{
						getDialogZFB();
					}
				}else if(tabId.equals(titles[2])){
					TianxianOrRecord = 1;
					if(recordChange == 1){
						recordChange = 0;
						cashdatalist.clear();
						QeryNet(0);
					}else if(isfirst){
						QeryNet(pageindex);
					}else{
						if(cashdatalist.size()!=0){
							initLinear(cashrecode, linearId[2], initLinearView(cashdatalist));
						}
					}
			
					}
				}
		
	};
	private csahInfo cashinfo;
	
	public void addTab(int index){
		View indicatorTab = mInflater.inflate(R.layout.layout_nav_item, null);
		ImageView img = (ImageView) indicatorTab.findViewById(R.id.layout_nav_item);
		TextView title = (TextView) indicatorTab.findViewById(R.id.layout_nav_icon_title);
		img.setBackgroundResource(R.drawable.tab_buy_selector);
		title.setText(titles[index]);
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titles[index]).setIndicator(indicatorTab).setContent(linearId[index]);
		mTabHost.addTab(tabSpec);
	}
	
	/**
	 * 初始化LinearLayout,为TabHost下的LinearLayout添加View
	 * @param linear  要初始化的LinearLayout
	 * @param linearid LinearLayout对应的Id
	 * @param view    要添加的View
	 */
	private void initLinear(LinearLayout linear,int linearid,View view) {
		linear = (LinearLayout)findViewById(linearid);
		linear.removeAllViews();
		linear.addView(view);
	}
	
	/**
	 * 初始化账户明细TabHost子布局
	 * @param pageindex   子项对应的页数
	 * @param allpagenum  子项对应的总页数
	 * @param typelist    子项对应的List
	 * @param isfirst     确定子项是不是初次联网（TabHost切换的联网）
	 * @return  初始化之后的view
	 */
	private View  initLinearView(final List typelist){
		LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		tabSpecLinearView = (LinearLayout) inflate.inflate(R.layout.usercenter_listview_layout, null);
		tabSpecListView = (ListView) tabSpecLinearView.findViewById(R.id.usercenter_listview_queryinfo);
		initListView(tabSpecListView,cashdatalist);
		return tabSpecLinearView;
	}
	/**
	 * 初始化ListView列表
	 * @param listview
	 * @param list
	 */
	private void initListView(ListView listview,List list){
		LayoutInflater	mInflater = LayoutInflater.from(this);
		view = mInflater.inflate(R.layout.lookmorebtn, null);
	    progressbar=(ProgressBar)view.findViewById(R.id.getmore_progressbar);
	    adaptercash = new CashcodeAdapter(this,list);
	    listview.addFooterView(view);
	    listview.setDivider(null);
	    listview.setDividerHeight(5);
		listview.setAdapter(adaptercash);	
        view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				view.setEnabled(false);
				// TODO Auto-generated method stub
	            addmore();

				
			}
		});
	}
	
	private  void addmore(){
		
		 pageindex++;
        if(pageindex>allpages-1){
        	pageindex=allpages-1;
       	 progressbar.setVisibility(view.INVISIBLE);
       	 view.setEnabled(true);
			 Toast.makeText(AccountWithdrawActivity.this, R.string.usercenter_hasgonelast, Toast.LENGTH_SHORT).show();  
		   }else{  	    
	         QeryNet(pageindex);
		   }
     
	}
	/**
	 * 获取登录后的信息
	 */
	public void getInfo(){
		 drawBStr = shellRW.getStringValue(Constants.DRAWBALANCE);//账户可提现余额
		 phonenum = shellRW.getStringValue("phonenum");
		 sessionid = shellRW.getStringValue("sessionid");
		 userno = shellRW.getStringValue("userno");
	}
	/**
	 * 创建提现信息类
	 */
	public AccountInfo createAccountInfo(JSONObject json){
		AccountInfo info = new AccountInfo();
		try {
			info.setState(json.getString("stat"));
			info.setNameStr(json.getString("name"));
			info.setMoney(json.getString("amount"));
			info.setNum(json.getString("bankcardno"));
			info.setAllbankname(json.getString("allbankname"));
			info.setAdd(json.getString("areaname"));
			info.setBank(json.getString("bankname"));
			info.setCashdetailId(json.getString("cashdetailid"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    return info;
	}
	/**
	 * 查询提现状态
	 */
	public void stateNet(){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = QueryLatelyWithdrawInterface.queryLatelyWithdraw(userno, sessionid, phonenum);
					try {
						String msg="";
						String error="";
						json=new JSONObject(str);
						msg = json.getString("message");
						error = json.getString("error_code");
						if(error.equals("0")||error.equals("0047")){
							handlerTwo.post(new Runnable() {
								public void run() {
									checkDNA();
								}
							});
							handler.handleMsg(error,msg);
						}else{
							handler.handleMsg(error,msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressdialog.dismiss();
			}

		});
		t.start();
	}
//查询提现记录
	public void QeryNet(final int pageindex){
		showDialog(0); // 显示网络提示框 2010/7/4
		TianxianOrRecord = 1;
		// 加入是否改变切入点判断 陈晨 8.11
		Log.v("page", pageindex+"");
		Thread t = new Thread(new Runnable(){
			String str = "00";
			@Override
			public void run() {
					str = QueryMoneyInterface.QueryCash(userno, phonenum, String.valueOf(pageindex), "10");
					try {
						String msg="";
						String error="";
						json=new JSONObject(str);
						message = json.getString("message");
						error = json.getString("error_code");
						if(error.equals("0")||error.equals("0047")){
							handler.handleMsg(error,message);
						}else if(error.equals("0000")){
							encodejson(json.toString());
							isfirst=false;
							handlerTwo.post(new Runnable() {						
								public void run() {
									
									if(pageindex==0){
										initLinear(cashrecode, linearId[2], initLinearView(cashdatalist));	
									}else{
										adaptercash.notifyDataSetChanged();
									}
									view.setEnabled(true);
									
								}
							});
							handler.handleMsg(error,message);
						}else {
							handler.handleMsg(error,message);

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(progressdialog!=null){
					progressdialog.dismiss();
					}
			}

		});
		t.start();
	}
	/**
	 * 账户提现联网
	 * 
	 */
	public void accountWithdrawNet(final ChangeWithdrawPojo changeWithdPojo){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = ChangeWithdrawInterface.changeWithdraw(changeWithdPojo);
					try {
						String msg="";
						String error="";
						if(str.equals("00")){
							error = str;
						}else{
							json=new JSONObject(str);
							message = json.getString("message");
							error = json.getString("error_code");
						}
						handler.handleMsg(error,message);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressdialog.dismiss();
			}

		});
		t.start();
	} 
	/**
	 * 取消提现联网
	 */
	public void cancelNet(final String id){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = CancelWithdrawInterface.cancelWithdraw(userno, sessionid, phonenum,id);
					try {
						String msg="";
						String error="";
						if(str.equals("00")){
							error = str;
						}else{
							json=new JSONObject(str);
							message = json.getString("message");
							error = json.getString("error_code");
						}
						handler.handleMsg(error,message);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressdialog.dismiss();
			}

		});
		t.start();
	}
	/**
 	 * DNA充值账户绑定查询
 	 */
 	public void  checkDNA() {
 		RWSharedPreferences pre = new RWSharedPreferences(AccountWithdrawActivity.this, "addInfo");
 		final String sessionId = pre.getStringValue("sessionid");
 		final String mobile = pre.getStringValue("mobileid");
 		final String userno = pre.getStringValue("userno");
 		if(sessionId==null||sessionId.equals("")){
 				Intent intentSession = new Intent(AccountWithdrawActivity.this,UserLogin.class);
 				startActivityForResult(intentSession,0);
 		}else{
 		showDialog(0);
 		new Thread(new Runnable() {
 			@Override
 			public void run() {
 			String error_code = "00";
 			String message = "";
 				try {
	 				 re = QueryDNAInterface.getInstance().queryDNA(mobile, sessionId, userno);
	 				 JSONObject	obj = new JSONObject(re);
	 				 error_code = obj.getString("error_code");
	 				 message = obj.getString("message");
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 			    if(error_code.equals("0047")){
 			    handler.post(new Runnable() {
					public void run() {
						getMoneyDialog();
					}
				});
 			    }else if(error_code.equals("0000")){
 				    handler.post(new Runnable() {
 						public void run() {
 							dialogDNA(re);
 						}
 					});
 			    }else{
 			    	handler.handleMsg(error_code, message);
 			    }
 			   progressdialog.dismiss();
 			}
 		}).start();
 		}
 	}
 	public void dialogDNA(String str) {
 		String bindState = "";
 		try {
 			JSONObject obj = new JSONObject(str);
 			bindState = obj.getString("bindstate");
 		if (bindState.equals("1")) {// 已经绑定
 			isBindState = true;
 			info.setBindState(obj.getString("bindstate"));
 			info.setNum(obj.getString("bankcardno"));
 			info.setNameStr(obj.getString("name"));
 			info.setAdd(obj.getString("bankaddress"));
 			info.setBankName(obj.getString("bankname"));
 		}else{
 			isBindState = false;
 		}
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		getMoneyDialog();
 	}
	/**
	 * 提现窗口
	 */
	protected void getDialogZFB() {
		start = true;
		isgetzfb=true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View getView = inflater.inflate(R.layout.get_money_submit, null);
		LinearLayout layout1 = (LinearLayout) getView.findViewById(R.id.LinearLayout_one);
		LinearLayout layout2 = (LinearLayout) getView.findViewById(R.id.LinearLayout_two);
		LinearLayout layout3 = (LinearLayout) getView.findViewById(R.id.LinearLayout_third);
		TextView userpass=(TextView)getView.findViewById(R.id.get_money_password_text);
		layout2.setVisibility(View.GONE);
        
		drawbalanceText = (TextView) getView.findViewById(R.id.withdraw_drawbalance); 
		if(drawBStr == null||drawBStr == ""||drawBStr == "null"){
			drawBStr = "联网获取失败";
		}
		drawbalanceText.setText("您的账户可提现余额为："+drawBStr);
		drawMoney = drawBStr.substring(0, drawBStr.length()-1);
		final TextView nameText = (TextView) getView.findViewById(R.id.get_money_name_text);
		final TextView bankText = (TextView) getView.findViewById(R.id.get_money_bank_num_text);
		 namezfb = (EditText) getView.findViewById(R.id.get_money_name_edit);
		 money_bank_num = (EditText) getView.findViewById(R.id.get_money_bank_num_edit);
		 money = (EditText) getView.findViewById(R.id.get_money_money_edit);
		 passwordzfb = (EditText) getView.findViewById(R.id.get_money_password_edit);
	   	 money_bank_num.setInputType(InputType.TYPE_CLASS_TEXT);
	 	 if(PublicConst.isthirdlogin){
	 		passwordzfb.setVisibility(View.GONE);
	 		userpass.setVisibility(View.GONE);
		 }
		nameText.setText("真实姓名：");
		bankText.setText("支付宝账号：");
        Button submit = (Button) getView.findViewById(R.id.get_money_img_submit);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) getView.findViewById(R.id.get_money_img_back);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        final float drawmoney = Float.parseFloat(drawMoney);
        if(drawmoney<10){
        	money.setText(drawMoney);
        	money.setEnabled(false);
        	if(drawmoney!=0.00){
        		Toast.makeText(AccountWithdrawActivity.this, "您的提现金额低于10元，需一次性提清！", Toast.LENGTH_LONG).show();
        	}
        }
        money.addTextChangedListener(PublicMethod.twoDigitsDecimal);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				ChangeWithdrawPojo changeWithdPojo = new ChangeWithdrawPojo();
				String nameStr = namezfb.getText().toString();
				String moneyStr = money.getText().toString();
				String passwordStr =passwordzfb.getText().toString();
				String bankNameStr = bankName;
//				String bankAreaStr = money_bank_start.getText().toString();
				String bankNoStr  = money_bank_num.getText().toString();
				changeWithdPojo.setUserno(userno);
				changeWithdPojo.setPhonenum(phonenum);
				changeWithdPojo.setSessionid(sessionid);
//				Log.e("amoit", PublicMethod.doubleToFen(moneyStr)+"======"+moneyStr);

				if(info.getBankName().equals("")||info.getBankName().equals("null")){
					changeWithdPojo.setBankname(bankNameStr);
				}else{
					changeWithdPojo.setBankname(info.getBankName());
				}
				if( nameStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "真实姓名名不能为空！", Toast.LENGTH_SHORT).show();
				}else if(bankNoStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "支付宝账号不能为空！", Toast.LENGTH_SHORT).show();
				}else if(moneyStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "提现金额不能为空！", Toast.LENGTH_LONG).show();
				}else if(Float.parseFloat(moneyStr)<10&&drawmoney>10.00){
		        	Toast.makeText(AccountWithdrawActivity.this, "单笔提现金额最少10元！", Toast.LENGTH_LONG).show();
				}
				else if(drawmoney==0.00){
					Toast.makeText(AccountWithdrawActivity.this, "您的可提现金额为0，不能提现！", Toast.LENGTH_LONG).show();
				}else if(passwordStr.equals("")&&PublicConst.isthirdlogin==false){
					Toast.makeText(AccountWithdrawActivity.this, "用户密码不能为空！", Toast.LENGTH_SHORT).show();
				}else if(Float.parseFloat(moneyStr)>Float.parseFloat(drawMoney)){
					Toast.makeText(AccountWithdrawActivity.this, "您提现的金额大于您的可提现余额！", Toast.LENGTH_SHORT).show();
				}else{
					changeWithdPojo.setType("2");//1:普通提现;2支付宝提现
					changeWithdPojo.setName(nameStr);
					changeWithdPojo.setAmount(PublicMethod.intToFen(moneyStr));
					changeWithdPojo.setAraeaname("");
					changeWithdPojo.setBankcardno(bankNoStr);
					changeWithdPojo.setPassword(passwordStr);
					accountWithdrawNet(changeWithdPojo);
					
				}
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
       LinearLayout layout = (LinearLayout)findViewById(linearId[1]);
       layout.removeAllViews();
       layout.addView(getView);
	}
 	String dnaRemind = "1、单笔提现金额最少10元；可提现金额小于10元时，申请提现时，需要一次性提清。<br>"+
	"2、提现只能提到银行卡上，暂不支持信用卡提现。<br>"+
	"3、为了防止少数用户利用信用卡套现和洗钱行为，保证正常用户的资金安全，本软件针对提款做出以下规定：累计充值资金消费未满30%，可提现金额为累计充值资金的70%；累计充值资金消费达到30%，不受此限制。<br>"+
	"4、银行卡提现不收取手续费。用工商、农业、建设、招商银行的银行卡16:00前的提款申请：当天到账；16:00后的提款申请：第二天到账。使用其它银行卡的提现，到账时间加一天。如有疑问，请致电客服热线：4006651000或01088860239<br>"+
	"5、单笔提现金额最高5万元";
	/**
	 * 提现窗口
	 */
	protected void getMoneyDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View getView = inflater.inflate(R.layout.get_money_submit, null);
		LinearLayout layout1 = (LinearLayout) getView.findViewById(R.id.LinearLayout_one);
		LinearLayout layout2 = (LinearLayout) getView.findViewById(R.id.LinearLayout_two);
		LinearLayout layout3 = (LinearLayout) getView.findViewById(R.id.LinearLayout_third);
		TextView textView = (TextView)getView.findViewById(R.id.TextView07);
		TextView userpass=(TextView)getView.findViewById(R.id.get_money_password_text);
		textView.setText(Html.fromHtml(dnaRemind));
		final EditText name = (EditText) getView.findViewById(R.id.get_money_name_edit);
		final Spinner money_brank = (Spinner) getView.findViewById(R.id.get_money_bank_spinner);
		
		//TextView dnaRemind = (TextView)findViewById(R.id.TextView07);
	    allcountries=new ArrayList<String>();
	    if(info.getAllbankname()!=null&&info.getAllbankname().length>0){
	    	allName = info.getAllbankname();
	    }else{
	    	allName = allBankName;
	    }
        for(int i=0;i<allName.length;i++){   
            allcountries.add(allName[i]);   
        }   
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,allcountries);   
        adapter.setDropDownViewResource(R.layout.myspinner_dropdown);   
        money_brank.setAdapter(adapter);
        //
		money_brank.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				int position = money_brank.getSelectedItemPosition();
				bankName = allName [position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
		drawbalanceText = (TextView) getView.findViewById(R.id.withdraw_drawbalance); 
		if(drawBStr == null||drawBStr == ""||drawBStr == "null"){
			drawBStr = "联网获取失败";
		}
		drawbalanceText.setText("您的账户可提现余额为："+drawBStr);
		drawMoney = drawBStr.substring(0, drawBStr.length()-1);
//		final EditText money_bank_start = (EditText) getView.findViewById(R.id.get_money_bank_start_edit);
		final EditText money_bank_num = (EditText) getView.findViewById(R.id.get_money_bank_num_edit);
		final EditText money = (EditText) getView.findViewById(R.id.get_money_money_edit);
		final EditText password = (EditText) getView.findViewById(R.id.get_money_password_edit);
		if(PublicConst.isthirdlogin){
			password.setVisibility(View.GONE);
			userpass.setVisibility(View.GONE);
		}
        Button submit = (Button) getView.findViewById(R.id.get_money_img_submit);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) getView.findViewById(R.id.get_money_img_back);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        final float drawmoney = Float.parseFloat(drawMoney);
        if(drawmoney<10){
        	money.setText(drawMoney);
        	money.setEnabled(false);
        	if(drawmoney!=0.00){
        		Toast.makeText(AccountWithdrawActivity.this, "您的提现金额低于10元，需一次性提清！", Toast.LENGTH_LONG).show();
        	}
        }
        money.addTextChangedListener(PublicMethod.twoDigitsDecimal);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				ChangeWithdrawPojo changeWithdPojo = new ChangeWithdrawPojo();
				String nameStr = name.getText().toString();
				String moneyStr = money.getText().toString();
				String passwordStr =password.getText().toString();
				String bankNameStr = bankName;
//				String bankAreaStr = money_bank_start.getText().toString();
				String bankNoStr  = money_bank_num.getText().toString();
				changeWithdPojo.setUserno(userno);
				changeWithdPojo.setPhonenum(phonenum);
				changeWithdPojo.setSessionid(sessionid);
				if(info.getBankName().equals("")||info.getBankName().equals("null")){
					changeWithdPojo.setBankname(bankNameStr);
				}else{
					changeWithdPojo.setBankname(info.getBankName());
				}
				if( nameStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "持卡人名不能为空！", Toast.LENGTH_SHORT).show();
				}else if(bankNoStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "银行卡号不能为空！", Toast.LENGTH_SHORT).show();
				}else if(moneyStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "提现金额不能为空！", Toast.LENGTH_LONG).show();
				}else if(Float.parseFloat(moneyStr)<10&&drawmoney>10.00){
		        	Toast.makeText(AccountWithdrawActivity.this, "单笔提现金额最少10元！", Toast.LENGTH_LONG).show();
				}else if(drawmoney==0.00){
					Toast.makeText(AccountWithdrawActivity.this, "您的可提现金额为0，不能提现！", Toast.LENGTH_LONG).show();
				}else if(passwordStr.equals("")&&PublicConst.isthirdlogin==false){
					Toast.makeText(AccountWithdrawActivity.this, "用户密码不能为空！", Toast.LENGTH_SHORT).show();
				}else if(Float.parseFloat(moneyStr)>Float.parseFloat(drawMoney)){
					Toast.makeText(AccountWithdrawActivity.this, "您提现的金额大于您的可提现余额！", Toast.LENGTH_SHORT).show();
				}else if(Float.parseFloat(moneyStr)>MAX_AMT){
					Toast.makeText(AccountWithdrawActivity.this, "单笔提现金额最高5万元！", Toast.LENGTH_SHORT).show();
				}else{
					changeWithdPojo.setType("1");//1:普通提现;2支付宝提现
					changeWithdPojo.setName(nameStr);
					changeWithdPojo.setAmount(PublicMethod.intToFen(moneyStr));
					changeWithdPojo.setAraeaname("");
					changeWithdPojo.setBankcardno(bankNoStr);
					changeWithdPojo.setPassword(passwordStr);
					accountWithdrawNet(changeWithdPojo);
					
				}
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        name.append(info.getNameStr());
//        money_bank_start.append(info.getAdd());
        money_bank_num.append(info.getNum());
        if(info.getBindState().equals("1")){//dna是否绑定
        	layout1.setVisibility(LinearLayout.GONE);
        	layout3.setVisibility(LinearLayout.GONE);
        	if(info.getBankName().equals("")||info.getBankName().equals("null")){
        		layout2.setVisibility(LinearLayout.VISIBLE);
        	}else{
        		layout2.setVisibility(LinearLayout.GONE);
        	}
        	money_bank_num.setEnabled(false);
        }
       LinearLayout layout = (LinearLayout)findViewById(linearId[0]);
       layout.removeAllViews();
       layout.addView(getView);
	}
	

	/**
	 * 提现待审核窗口
	 */
	protected View waitDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View waitView = inflater.inflate(R.layout.get_money_stating, null);
		TextView textView = (TextView)waitView.findViewById(R.id.get_money_stating_remind);
		textView.setText(Html.fromHtml(dnaRemind));
        nameText = (TextView) waitView.findViewById(R.id.get_money_name);
        bankText = (TextView) waitView.findViewById(R.id.get_money_bank);
        addText = (TextView) waitView.findViewById(R.id.get_money_bank_address);
        numText = (TextView) waitView.findViewById(R.id.get_money_bank_num);
        moneyText = (TextView) waitView.findViewById(R.id.get_money_money);
        Button submit = (Button) waitView.findViewById(R.id.get_money_img_submit);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) waitView.findViewById(R.id.get_money_img_cancel);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button exit = (Button) waitView.findViewById(R.id.get_money_img_exit);
        exit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				setContentView(getMoneyDialog());
				checkDNA();
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				cancelNet();
			}
		});
        exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			 finish();	
			}
		});
        nameText.append(info.getNameStr());
        bankText.append(info.getBank());
        addText.append(info.getAdd());
        numText.append(info.getNum());
        moneyText.append(info.getMoney());
		return waitView;
	}
	/**
	 * 提现审核中窗口
	 */
	protected View waitingDialog() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View waitingView = inflater.inflate(R.layout.get_money_state, null);
		TextView content = (TextView) waitingView.findViewById(R.id.get_money_state_text);
		content.setText(R.string.waiting_text);
        Button submit = (Button) waitingView.findViewById(R.id.get_money_img_submit);
        submit.setVisibility(ImageButton.GONE);
        Button cancel = (Button) waitingView.findViewById(R.id.get_money_img_cancel);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 finish();		
			}
		});
		return waitingView;
	}
	/**
	 * 提现失败窗口
	 */
	protected View failDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View failView = inflater.inflate(R.layout.get_money_state, null);
		TextView content = (TextView) failView.findViewById(R.id.get_money_state_text);
		content.setText(R.string.fail_text);
        Button submit = (Button) failView.findViewById(R.id.get_money_img_submit);
        submit.setText(R.string.usercenter_withdrawAnew);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) failView.findViewById(R.id.get_money_img_cancel);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
//				setContentView(getMoneyDialog());
				checkDNA();
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		return failView;
	}
	/**
	 * 已通过审核窗口
	 */
	protected View successDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View successView = inflater.inflate(R.layout.get_money_state, null);
		TextView content = (TextView) successView.findViewById(R.id.get_money_state_text);
		content.setText(R.string.success_text);
        Button submit = (Button) successView.findViewById(R.id.get_money_img_submit);
        submit.setText(R.string.usercenter_withdrawAnew);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) successView.findViewById(R.id.get_money_img_cancel);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				setContentView(getMoneyDialog());	
				checkDNA();
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();		
			}
		});
		return successView;
	}
//取消提现窗口
	protected void CannalDialog(final String id) {
		final Dialog dialog =new Dialog(this,R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View successView = inflater.inflate(R.layout.base_dialog_default_view, null);
		TextView title = (TextView) successView.findViewById(R.id.zfb_text_title);
		TextView alertText = (TextView) successView.findViewById(R.id.zfb_text_alert);
		title.setText("提示");
		alertText.setText("是否取消提现");
        Button submit = (Button) successView.findViewById(R.id.ok);
        submit.setText("确定");
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) successView.findViewById(R.id.canel);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancelNet(id);
				dialog.cancel();
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
        dialog.setContentView(successView);
        dialog.show();
	}
	/**
	 *  网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setTitle(R.string.usercenter_netDialogTitle);
			progressdialog.setMessage(getString(R.string.usercenter_netDialogRemind));
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}

	public void errorCode_0000() {
//		if(isQuery){
//			isQuery = false;
//			info = createAccountInfo(json);
//			state = Integer.parseInt(info.getState());
//			switch (state) {
//			case 0:// 提现窗口
//				setContentView(getMoneyDialog());
//				checkDNA();
//				break;
//			case 31:// 修改提现窗口
////				changeDialog();
//				break;
//			case 1:// 待审核
//				setContentView(waitDialog());
//				break;
//			case 102:// 审核中
//				setContentView(waitingDialog());
//				break;
//			case 103:// 审核中
//				setContentView(waitingDialog());
//				break;
//			case 104:// 驳回
//				setContentView(failDialog());
//				break;
//			case 106:// 驳回
//				setContentView(failDialog());
//				break;
//			case 105:// 已通过审核
//				setContentView(successDialog());
//				break;
//			}	
//		}else{
		switch (TianxianOrRecord) {
		case 0:
			updateDrawBalance();
			checkDNA();
			recordChange = 1;
			if(isgetzfb){
				  namezfb.setText("");
				  money_bank_num.setText("");
				  money.setText("");
				  passwordzfb.setText("");
			}
			break;
		case 1:
			break;
		case 2:
			cashdatalist.clear();
			QeryNet(0);
			break;
		}
		Toast.makeText(AccountWithdrawActivity.this,message, Toast.LENGTH_SHORT).show();
//			finish();
//		}

	}

	public void errorCode_000000() {
		
	}

	public Context getContext() {
		return this;
	}
	
	 public void encodejson(String json) {
		  
		  try {
			  JSONObject  cashobj = new JSONObject(json);
			  allpages = Integer.parseInt(cashobj.getString("totalPage"));
			  String cashrecode = cashobj.getString("result");
			  JSONArray cashobjarray = new JSONArray(cashrecode);
			  for(int i=0;i<cashobjarray.length();i++){
				try{
					csahInfo cashinfo = new csahInfo();
					String state = cashobjarray.getJSONObject(i).getString("state");
					cashinfo.setCashtime(cashobjarray.getJSONObject(i).getString("cashTime"));
					cashinfo.setCashstate(cashobjarray.getJSONObject(i).getString("stateMemo"));
					cashinfo.setCashamt(cashobjarray.getJSONObject(i).getString("amount"));
					cashinfo.setReason(cashobjarray.getJSONObject(i).getString("rejectReason"));
					cashinfo.setState(state);
					if(state.equals("1")){
						cashinfo.setVisible(1);
					}else if(state.equals("104")){
						cashinfo.setVisible(104);
					}
					cashinfo.setCashid(cashobjarray.getJSONObject(i).getString("cashdetailid"));
					cashdatalist.add(cashinfo);			
				}catch (Exception e) {
				}
			 }
			 
		  	 } catch (JSONException e) {
		  		try {
						JSONObject winprizejson = new JSONObject(json);
		  		} catch (JSONException e1) {
		  		}
		    }
	     }
	/**
	 * 提现纪律信息类
	 * @author Administrator
	 *
	 */
	 
	class csahInfo{
		public int getVisible() {
			return visible;
		}
		public void setVisible(int visible) {
			this.visible = visible;
		}
		String cashstate ="" ;//提现状态
		String cashtime ="";//提现时间
		String cashamt="";//提现金额
		String reason=""; //失败原因
		String state="";//提现状态 数值表示
		String cashid="";//id
		int visible = 0;
		public String getCashid() {
			return cashid;
		}
		public void setCashid(String cashid) {
			this.cashid = cashid;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getCashstate() {
			return cashstate;
		}
		public void setCashstate(String cashstate) {
			this.cashstate = cashstate;
		}
		public String getCashtime() {
			return cashtime;
		}
		public void setCashtime(String cashtime) {
			this.cashtime = cashtime;
		}
		public String getCashamt() {
			return cashamt;
		}
		public void setCashamt(String cashamt) {
			this.cashamt = cashamt;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
	}
	/**
	 * 提现信息类
	 * @author Administrator
	 *
	 */
	class AccountInfo{
		String state="0",nameStr = "",bank = "",add ="",num = "",money ="0",cashdetailId;
		private String bindState ="";//1是绑定，0是未绑定dna
		private String bankName = "";
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public String getBindState() {
			return bindState;
		}
		public void setBindState(String bindState) {
			this.bindState = bindState;
		}
		public String getCashdetailId() {
			return cashdetailId;
		}
		public void setCashdetailId(String cashdetailId) {
			this.cashdetailId = cashdetailId;
		}
		String allbankname[] = null;
		public AccountInfo(){
			
		}
		public void setState(String state){
			this.state = state;
		}
		public String getState(){
			return state;
		}
		public void setNameStr(String nameStr) {
			this.nameStr = nameStr;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public void setAdd(String add) {
			this.add = add;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getNameStr() {
			return nameStr;
		}

		public String getBank() {
			return bank;
		}

		public String getAdd() {
			return add;
		}

		public String getNum() {
			return num;
		}

		public String[] getAllbankname() {
		    return allbankname;
		}
		public void setAllbankname(String allbankname) {
			 
			this.allbankname = allbankname.split(",");
		}
		public String getMoney() {
			return PublicMethod.formatMoney(money);
		}
	}
	
	/**
	 * 中奖查询的适配器
	 */
	public class CashcodeAdapter extends BaseAdapter {
		        
		private LayoutInflater mInflater; // 扩充主列表布局
			private List<csahInfo> mList;
			public CashcodeAdapter(Context context, List<csahInfo> list) {
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
				//如果是竞彩则彩种的期号是不能显示的，在这里要加上所有的竞彩彩种编号		
			    final String cashtime = (String) mList.get(position).getCashtime();
			    final int visible = (Integer)mList.get(position).getVisible();
			    final String cashamt = (String) mList.get(position).getCashamt();
			    final String cashreason = (String) mList.get(position).getReason();
			    final String cashstate =(String) mList.get(position).getCashstate();
			    final String state =(String)mList.get(position).getState();
			    final String cashid=(String)mList.get(position).getCashid();;
				if (convertView == null) {
					convertView = mInflater.inflate(R.layout.get_money_listitem,null);
					holder = new ViewHolder();
					holder.cashtime = (TextView)convertView.findViewById(R.id.getmoneyoftime);
					holder.cashamt =(TextView)convertView.findViewById(R.id.getmoneyamtdata);
					holder.check = (Button)convertView.findViewById(R.id.chechbutton);
					holder.cashstate =(TextView)convertView.findViewById(R.id.getmoneystat);
					holder.reason = (TextView)convertView.findViewById(R.id.shibaitext);
					holder.layout =(LinearLayout)convertView.findViewById(R.id.shibailayout);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				final ViewHolder holder2 = holder;

		        holder.cashtime.setText(cashtime);
		        holder.cashamt.setText(PublicMethod.toYuan(cashamt));
		        holder.reason.setText(cashreason);
		        holder.cashstate.setText(cashstate);
		        initCashstateAndCheckBtn(holder, visible);
		        holder.check.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					TianxianOrRecord = 2;
					if(state.equals("104")){
						if(zhankai){
						holder2.layout.setVisibility(View.VISIBLE) ; 
						holder2.check.setBackgroundResource(R.drawable.tixian_zhankai);
						zhankai =false;
						}else{
						holder2.layout.setVisibility(View.GONE) ;
						holder2.check.setBackgroundResource(R.drawable.shouqi);
						zhankai =true;
						}
					
					}else if(state.equals("1")){
						CannalDialog(cashid);
					}
						
					}
				});
				return convertView;
			}
		
		
		
	}
	class ViewHolder {
		TextView cashtime;
		TextView cashamt;
		TextView cashstate;
		TextView reason;
		Button   check;
		LinearLayout layout;
	}
	private void initCashstateAndCheckBtn(ViewHolder holder,
			final int mask) {
		if(mask == 104){
        	holder.check.setMaxHeight(20);
        	holder.cashstate.setTextColor(Color.rgb(163, 163, 163));
        	holder.check.setBackgroundResource(R.drawable.shouqi);
        	holder.check.setText("");
        	holder.check.setVisibility(View.VISIBLE);
        }else if(mask == 1){
        	holder.cashstate.setTextColor(Color.rgb(190, 108, 22));
        	holder.check.setText("取消");
        	holder.check.setBackgroundResource(R.drawable.quxiao_normal);
        	holder.check.setVisibility(View.VISIBLE);
        }else {
        	holder.cashstate.setTextColor(Color.rgb(21, 83, 26));
        	holder.check.setVisibility(View.GONE);
        }
	}
	/**
	 * 更新可提现余额
	 */
	private void updateDrawBalance(){
		Handler handler = new Handler();
		showDialog(0);
		handler.post(new Runnable() {
			public void run() {
				String allBalanceMsg = BalanceQueryInterface.balanceQuery(userno, sessionid, phonenum);
				try{
					JSONObject balancejson = new JSONObject(allBalanceMsg);
					String errorCode = balancejson.getString("error_code");
					if(errorCode.equals("0000")){
						drawBStr = balancejson.getString("drawbalance");
						 shellRW.putStringValue(Constants.DRAWBALANCE,drawBStr);
					}else{
						drawBStr = "联网获取失败";
					}
				}catch (JSONException e) {
					// TODO: handle exception
					drawBStr = "联网获取失败";
				}
				drawbalanceText.setText("您的账户可提现余额为："+drawBStr);
				progressdialog.dismiss();
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-24
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-24
	}
}
