package com.ruyicai.activity.account;

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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.secure.AlipaySecurePayDialog;
import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.dialog.ExitDialogFactory;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.QueryDNAInterface;
import com.ruyicai.net.newtransaction.RechargeInterface;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
import com.umeng.analytics.MobclickAgent;

/**
 * 主页面 充值界面
 * @author Administrator
 *
 */
public class AccountActivity extends Activity  implements HandlerMsg {
	private final String YINTYPE = "0900";
	private String url="";
	private ProgressDialog progressdialog;
	private String re;
	private String sessionId="";
	private String phonenum="";
	private String userno="";
	private String RECHARGTYPE = "0";
	HandlerMsg msg;
	private String TITLE = "title";
	private String ISHANDINGFREE = "isHandingFree";
	private String  PICTURE = "";
	private String error_code;
	private String strExpand;
	private MyHandler handler = new MyHandler(this);
	private String message="";
	private String xml = "";
	private String isonkey="";
	
	private RelativeLayout top;
	private String textString;
	private Button returnButton;
	private String[] bankNames = {"中国工商银行","中国农业银行","中国建设银行","招商银行","中国邮政储蓄银行","华夏银行","兴业银行","中信银行","中国光大银行",
			                      "上海浦东发展银行","深圳发展银行"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        factory = LayoutInflater.from(this);
		List list = null;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.account_list_main);
		setTopText();
		ListView listView = (ListView) findViewById(R.id.account_rechange_listview);
		list = setContentForMainList();
		AccountAdapter adapter = new AccountAdapter(this,list); 
		String onkey = getIntent().getStringExtra("isonKey");
		if(onkey!=null&&!onkey.equals("")){
			isonkey = onkey;
		}
		top =(RelativeLayout)findViewById(R.id.main_buy_title);
		if(isonkey.equals("fasle")){
			top.setVisibility(View.VISIBLE);
			returnButton=(Button)findViewById(R.id.layout_main_img_return);
			returnButton.setVisibility(View.INVISIBLE);
			initReturn();
		}
		listView.setAdapter(adapter);
		AccountMainItemListener listener=new AccountMainItemListener(this);
        listView.setOnItemClickListener(listener);
	}
	
	protected  void initReturn(){
		returnButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					finish();
			}
		});
	}
	/**
	 * 设置头部text信息
	 * @author Administrator
	 *
	 */
	public void setTopText(){
		TextView textTop =  (TextView)findViewById(R.id.account_list_main_text);
		String text1 = getString(R.string.computer_rechargeinfo1);
		String text2 = getString(R.string.computer_address);
		String text3 = getString(R.string.computer_rechargeinfo2);
		Spanned spanned = Html.fromHtml("<a href=\"http://www.ruyicai.com\">"+text2+"</a>");
		textTop.append(text1);
		textTop.append(spanned);
		textTop.append(text3);
		textTop.setMovementMethod(LinkMovementMethod.getInstance());  
	}
	
	class AccountAdapter extends BaseAdapter{
		private LayoutInflater mInflater; // 扩充主列表布局
		private List<Map<String, Object>> mList;
		public AccountAdapter(Context context, List<Map<String, Object>> list) {
			mInflater = LayoutInflater.from(context);
			mList = list;
		}
		public int getCount() {
			return mList.size();
		}
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			String title = (String) mList.get(position).get(TITLE);
			Integer  iconid = (Integer)mList.get(position).get(PICTURE);
			String alertStr = (String)mList.get(position).get(ISHANDINGFREE);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.account_listviw_item,null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView.findViewById(R.id.account_recharge_listview_text);
				holder.isfreeHanding = (TextView)convertView.findViewById(R.id.ishandingfree);
				holder.lefticon = (ImageView)convertView.findViewById(R.id.account_recharge_type);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SpannableStringBuilder builder1 = new SpannableStringBuilder(); 
			String str1 = title;
			builder1.append(str1);
			if(position==0||position==1||position==2||position==3){
				String alertStr1 = "(免手续费)";
				builder1.append(alertStr1);
			    builder1.setSpan(new ForegroundColorSpan(Color.RED), builder1.length()-alertStr1.length(), builder1.length(), Spanned.SPAN_COMPOSING); 
			}else if(position==6){
				String alertStr1 = "(推荐)";
				builder1.append(alertStr1);
			    builder1.setSpan(new ForegroundColorSpan(Color.RED), builder1.length()-alertStr1.length(), builder1.length(), Spanned.SPAN_COMPOSING); 
			}
			holder.title.setText(builder1);
			holder.lefticon.setBackgroundResource(iconid);
			SpannableStringBuilder builder = new SpannableStringBuilder(); 
			String str = alertStr;
			builder.append(str);
			holder.isfreeHanding.setHint(builder);
			return convertView;
		}
		class ViewHolder{
			TextView title;
			ImageView lefticon;
			TextView isfreeHanding;
		}
	}
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//BY贺思明 2012-7-23
		initBankCardNoInfo();
		Constants.MEMUTYPE = 0;
	}
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//BY贺思明 2012-7-23
	}
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	LayoutInflater factory = null;
	private Dialog RechargeType=null;
	EditText bank_card_phone_bankid;
	EditText bank_card_phone_phone_num;
	EditText bank_card_phone_name;
	RWSharedPreferences shellRW;
	
	//银行卡电话充值弹出框(DNA)
    protected Dialog createBankCardPhoneDialog(){
      RECHARGTYPE = "01";	
	  final View bank_card_phone_online_view = factory.inflate(R.layout.account_bank_card_phone_online_dialog, null);
	  bank_card_phone_bankid = (EditText) bank_card_phone_online_view.findViewById(R.id.bank_card_phone_bankid);
      bank_card_phone_bankid.setText(bankCardNo);
      bank_card_phone_phone_num = (EditText)bank_card_phone_online_view.findViewById(R.id.bank_card_phone_phone_num);// 手机号
	  bank_card_phone_phone_num.setText(bindPhone);
	  bank_card_phone_bankid.setEnabled(false);
	  final Button ok = (Button)bank_card_phone_online_view.findViewById(R.id.ok);
		final Button canel =(Button)bank_card_phone_online_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr == null || sessionIdStr.equals("")) {
				    Intent intentSession = new Intent(AccountActivity.this, UserLogin.class);
					startActivity(intentSession);
				} else {
					// 银行卡语音充值网络连接
					beiginBankCardPhoneOnline(bank_card_phone_online_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RechargeType.dismiss();
			}
		});
		        RechargeType = new Dialog(this,R.style.dialog);
		        RechargeType.setContentView(bank_card_phone_online_view);  
		        return RechargeType;
	}
    
    //银行卡电话充值弹出框(DNA)（未绑定 ）
    protected Dialog createBankCardPhoneDialogNo(){
    	RECHARGTYPE = "01";
		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
    	final View bank_card_phone_view = factory.inflate(R.layout.account_bank_card_phone_dialog, null);
		String phonenum = pre.getStringValue(ShellRWConstants.MOBILEID);
	    bank_card_phone_phone_num = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_num);// 手机号
		bank_card_phone_name = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_name);// 姓名
		bank_card_phone_name.setText(name);
		bank_card_phone_phone_num.setText(phonenum);
 		EditText bank_card_phone_bankid = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_bankid);// 银行卡号
 		EditText bank_card_phone_idcard = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
 		EditText bank_card_phone_home = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
 		EditText bank_card_phone_province = (EditText) bank_card_phone_view.findViewById(R.id.bank_card_phone_phone_province);// 所在省
 		final Spinner spinner = (Spinner) bank_card_phone_view.findViewById(R.id.Spinner01);
 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,bankNames);
 		adapter.setDropDownViewResource(R.layout.myspinner_dropdown);   
 		spinner.setAdapter(adapter); 
 		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				int position = spinner.getSelectedItemPosition();
				bankName = bankNames[position];
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});
 		spinner.setSelection(1);
 		bank_card_phone_bankid.setText(bankCardNo);
 		bank_card_phone_idcard.setText(certid);
 		bank_card_phone_home.setText(certAddress);
 		bank_card_phone_province.setText(bankAddress);
	    final Button ok = (Button)bank_card_phone_view.findViewById(R.id.ok);
	    final Button canel =(Button)bank_card_phone_view.findViewById(R.id.canel);
			ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
					String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
					if (sessionIdStr!=null&&sessionIdStr.equals("")) {
						Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
						startActivity(intentSession);
					}else{
						beiginBankCardPhoneNo(bank_card_phone_view,bankName);
				    }
				}
			});
			canel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					RechargeType.dismiss();
				}
			});
	        RechargeType = new Dialog(this,R.style.dialog);
	        RechargeType.setContentView(bank_card_phone_view);
	        return RechargeType;
    }
    /**
     * 清空dna充值信息
     */
    public void initBankCardNoInfo(){
    	bankCardNo = "";
        name="";
        certid="";
        bankAddress="";
     	certAddress="";
    }
	private String phoneCardType = "0206";
	private String phoneCardValue = "100";
	private String gameCardType = "0204";
	// 移动充值卡
	private final String YIDONG = "0203";
	// 联通充值卡
    private final String LIANTONG = "0206";
	// 电信充值卡
	private final String DIANXIN = "0221";
	//电话卡充值弹出框
    protected Dialog createPhoneRechargeCardDialog(){
    	RECHARGTYPE = "02";
    	final View phone_card_recharg_view = factory.inflate(R.layout.account_phone_cards_recharge_dialog, null);
		final Spinner phone_card_spinner = (Spinner) phone_card_recharg_view.findViewById(R.id.phone_card_spinner);
		phone_card_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {

						// 点击下拉框。。。
						int position = phone_card_spinner.getSelectedItemPosition();
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
		
		final Spinner phone_card_value_spinner = (Spinner) phone_card_recharg_view.findViewById(R.id.phone_card_value_spinner);
		phone_card_value_spinner.setSelection(4);// 默认100元
		phone_card_value_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
                        int position = phone_card_value_spinner
								.getSelectedItemPosition();
						String[] values = { "10", "20", "30", "50", "100","200", "300", "500" };
						for (int i = 0; i < values.length; i++) {
							phoneCardValue = values[position];
						}
					}
					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
						}
                 });
		final Button ok = (Button)phone_card_recharg_view.findViewById(R.id.ok);
		final Button canel =(Button)phone_card_recharg_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
			    beginPhoneCardRecharge(phone_card_recharg_view);
			    }
			
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RechargeType.dismiss();
			}
		});
		        RechargeType = new Dialog(this,R.style.dialog);
		        RechargeType.setContentView(phone_card_recharg_view);
    	        return RechargeType;
    }
    boolean isWebView = false;//浏览器打开支付宝
    //支付宝充值弹出框
    protected Dialog createAlipayDialog(){
    	RECHARGTYPE = "05";
    	final View zfb_recharge_view = factory.inflate(R.layout.account_alipay_recharge_dialog, null);
    	final Button ok = (Button)zfb_recharge_view.findViewById(R.id.ok);
		final Button canel =(Button)zfb_recharge_view.findViewById(R.id.canel);
		ok.setText("wap支付");
		canel.setText("浏览器支付");
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isWebView = true;
				beginAlipayRecharge(zfb_recharge_view);
			}
		});
	   canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				RechargeType.dismiss();
				isWebView = false;
				beginAlipayRecharge(zfb_recharge_view);
			}
		});
        RechargeType = new Dialog(this,R.style.dialog);
        RechargeType.setContentView(zfb_recharge_view);
        return RechargeType;
    }
    //银联支付充值弹出框
    protected Dialog createYinpayDialog(){
    	RECHARGTYPE = "06";
    	final View view = factory.inflate(R.layout.account_alipay_recharge_dialog, null);
    	TextView title = (TextView) view.findViewById(R.id.zfb_text_title);
    	title.setText(getResources().getString(R.string.yin_bank_cards_recharge));
    	TextView alertText = (TextView) view.findViewById(R.id.zfb_text_alert);
    	alertText.setText("支持的银行>>");
    	alertText.setTextColor(Color.BLUE);
    	alertText.setTextSize(16);
    	alertText.setOnClickListener(textclick);
//    	alertText.setVisibility(TextView.GONE);
    	final Button ok = (Button)view.findViewById(R.id.ok);
		final Button canel =(Button)view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				MobclickAgent.onEvent(AccountActivity.this, "chongzhi ");//BY贺思明 2012-6-29
				beginYinpayRecharge(view);
			}
		});
	   canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
        RechargeType = new Dialog(this,R.style.dialog);
        RechargeType.setContentView(view);
        return RechargeType;
    }
    
 //支持银行dialog  view   
    public View getView(){
 		LayoutInflater factoryProtocol = LayoutInflater.from(this);
 		final View view = factoryProtocol.inflate(R.layout.user_login_protocol_dialog, null);
 		WebView webView = (WebView) view.findViewById(R.id.ruyipackage_webview);
 	    String iFileName = "accoutyin.html";
 		String url = "file:///android_asset/" + iFileName;
 		webView.loadUrl(url);
 	   return view;
    }
 //支持银行点击事件   
    OnClickListener textclick =new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new  AlertDialog.Builder(AccountActivity.this).setTitle("支持的银行")
			.setView(getView()).setPositiveButton(R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int whichButton) {

						}
					}).setNegativeButton(R.string.xitongshezhi_check_off,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
						}
					}).create().show();
		}
	};
	private final String ZHAOSHANG = "0101";
	// 建设银行
	private final String JIANSHE = "0102";
	// 工商银行
	private final String GONGSHANG = "0103";
	private String bankType = "CMBCHINA-WAP";
	//银行卡电话充值弹出框
    protected Dialog createPhoneBankRechargeDialog(){
    	RECHARGTYPE = "03";
    	final View phone_bank_recharg_view = factory.inflate(R.layout.account_phone_bank_recharg_dialog, null);
		final Spinner phone_bank_spinner = (Spinner) phone_bank_recharg_view.findViewById(R.id.phone_bank_spinner);
		phone_bank_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
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
					public void onNothingSelected(AdapterView<?> arg0) {
						// 没有任何的触发事件时
					}
				});
		final Button ok = (Button)phone_bank_recharg_view.findViewById(R.id.ok);
		final Button canel =(Button)phone_bank_recharg_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
					beginPhoneBankRecharge(phone_bank_recharg_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
        RechargeType = new Dialog(this,R.style.dialog);
        RechargeType.setContentView(phone_bank_recharg_view);    
        return RechargeType;
    }
	private final String ZHENGTU = "0204";
	// 骏网一卡通
	private final String JUNWANG = "0201";
	// 盛大卡
	private final String SHENGDA = "0202";
    
    //游戏点卡充值弹出框
    protected Dialog createGameCardDialog(){
    	RECHARGTYPE = "04";
    	final View game_card_view = factory.inflate(R.layout.account_game_card_dialog, null);
		final Spinner game_card_spinner = (Spinner) game_card_view.findViewById(R.id.game_card_spinner);
		game_card_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
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
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
		final Button ok = (Button)game_card_view.findViewById(R.id.ok);
		final Button canel =(Button)game_card_view.findViewById(R.id.canel);
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
				String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
				if (sessionIdStr!=null&&sessionIdStr.equals("")) {
					Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
					startActivity(intentSession);}
				else{
				beginGameCardRecharge(game_card_view);
				}
			}
		});
		canel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RechargeType.dismiss();
			}
		});
       RechargeType = new Dialog(this,R.style.dialog);
       RechargeType.setContentView(game_card_view);    
      
    	return RechargeType;
    }
//    //第一次DNA充值绑定弹出框
    protected Dialog createBankPhoneCardRegisterDialog(){
    	RECHARGTYPE = "01";//
    	final View phone_bank_card_view = factory.inflate(R.layout.account_bank_card_phone_register_dialog, null);
    	RechargeType = new AlertDialog.Builder(this).setTitle(R.string.bank_cards_recharge).setView(phone_bank_card_view).setPositiveButton(R.string.ok,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								beginRegisterBankPhoneCard(phone_bank_card_view);
								/* User clicked OK so do some stuff */
							}
						}).setNegativeButton(R.string.cancel,null).create();
    	return RechargeType;
    }
    //支付宝充值
     private void beginAlipayRecharge(View Vi) {//isWebView = false代表浏览器联网

		final EditText zfb_recharge_value = (EditText) Vi.findViewById(R.id.zfb_recharge_value);		
		final String zfb_recharge_value_string = zfb_recharge_value.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
		String sessionIdStr = pre.getStringValue(ShellRWConstants.SESSIONID);
		if (sessionIdStr.equals("")||sessionIdStr == null) {
			Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
			startActivity(intentSession);
		} else {
			if(zfb_recharge_value_string.equals("0")){
				Toast.makeText(this, "不能为0！", Toast.LENGTH_LONG).show();
				return;
			}
			if (zfb_recharge_value_string.equals("")|| zfb_recharge_value_string.length() == 0) {
				Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
			} else {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo =new RechargePojo();;
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype(RECHARGTYPE);
				rechargepojo.setCardtype("0300");
				if(isWebView){
					rechargepojo.setBankAccount("4");//4支付宝wap支付
				}else{
					rechargepojo.setBankAccount("3");//3支付宝浏览器支付
				}
				recharge(rechargepojo);
			}
		}
	}
     //银联充值
     private void beginYinpayRecharge(View Vi) {

		final EditText zfb_recharge_value = (EditText) Vi.findViewById(R.id.zfb_recharge_value);
		
		final String zfb_recharge_value_string = zfb_recharge_value.getText().toString();
		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
		String sessionIdStr = pre.getStringValue("sessionid");
		if (sessionIdStr.equals("")) {
			Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
			startActivity(intentSession);
		} else {
			if(zfb_recharge_value_string.equals("0")){
				Toast.makeText(this, "充值金额不能为0！", Toast.LENGTH_LONG).show();
				return;
			}
			if (zfb_recharge_value_string.equals("")|| zfb_recharge_value_string.length() == 0) {
				Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
			} else {
				// 支付宝充值网络获取
				// 改为线程 2010/7/9陈晨
				RechargePojo rechargepojo =new RechargePojo();;
				rechargepojo.setAmount(zfb_recharge_value_string);
				rechargepojo.setRechargetype(RECHARGTYPE);
				rechargepojo.setCardtype(YINTYPE);
				recharge(rechargepojo);
			}
		}
	}
     
     //电话银行充值
     private void beginPhoneBankRecharge(View Vi) {
 		final EditText phone_bank_enter_value = (EditText) Vi.findViewById(R.id.phone_bank_enter_value);
 		Editable phone_bank_value = phone_bank_enter_value.getText();
 		final String phone_bank_value_string = String.valueOf(phone_bank_value);
 		// 手机银行网络连接
 		if ((!(bankType.equals("")) && bankType != null)&& (!(phone_bank_value_string.equals("")) && phone_bank_value_string != null)) {
 		if (Integer.parseInt(phone_bank_value_string) >= 1) {
 			    RechargePojo rechargepojo =new RechargePojo();;
 			    rechargepojo.setCardtype(bankType);
 			    rechargepojo.setAmount(phone_bank_value_string);
 			    rechargepojo.setRechargetype(RECHARGTYPE);
 	            recharge(rechargepojo);
 		} else{
 				Toast.makeText(this, "充值金额至少为1元！", Toast.LENGTH_LONG).show();
 		}
 		} else{
 			
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 		}
 	}
      // 银行卡语音充值(DNA)
 	  private void beiginBankCardPhoneOnline(View vi) {
 		
 		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);
 		final String rechargevalue = bank_card_phone_recharge_value.getText().toString();
 		EditText bank_card_phone_phone_num = (EditText) vi.findViewById(R.id.bank_card_phone_phone_num);
 		final String cardphonenum = bank_card_phone_phone_num.getText().toString();
 		final String cardid = bank_card_phone_bankid.getText().toString();
 		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
 		// ；
 		if ((!(cardphonenum.equals("")) && cardphonenum != null)&& (!(rechargevalue.equals("")) && rechargevalue != null)&& (!(cardid.equals("")) && cardid != null)) {
 			if (cardphonenum.length() == 11) {
 				if (Integer.parseInt(rechargevalue) >= 20) {
		 			String	bank_card_id = cardid;
		 			RechargePojo rechargepojo =new RechargePojo();
		 			rechargepojo.setAmount(rechargevalue);
		 			rechargepojo.setCardno(bank_card_id);
		 			rechargepojo.setCardtype("0101");
		 			rechargepojo.setRechargetype(RECHARGTYPE);
		 			rechargepojo.setIswhite("true");
		 			rechargepojo.setPhonenum(cardphonenum);
		 			rechargepojo.setBankName(bankName);
		 		    recharge(rechargepojo);
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


 	// 银行卡语音充值(未绑定)DNA
 	private void beiginBankCardPhoneNo(View vi,String bankName) {
 		EditText bank_card_phone_recharge_value = (EditText) vi.findViewById(R.id.bank_card_phone_recharge_value);// 金额
 		final String value = bank_card_phone_recharge_value.getText().toString();
 		EditText bank_card_phone_bankid = (EditText) vi.findViewById(R.id.bank_card_phone_bankid);// 银行卡号
 		final String bankid = bank_card_phone_bankid.getText().toString();
 		final String name = bank_card_phone_name.getText().toString();
 		EditText bank_card_phone_idcard = (EditText) vi.findViewById(R.id.bank_card_phone_phone_idcard);// 身份证号
 		final String idcard = bank_card_phone_idcard.getText().toString();
 		EditText bank_card_phone_home = (EditText) vi.findViewById(R.id.bank_card_phone_phone_home);// 户籍所在地
 		final String home = bank_card_phone_home.getText().toString();
 		EditText bank_card_phone_province = (EditText) vi.findViewById(R.id.bank_card_phone_phone_province);// 所在省
 		final String province = bank_card_phone_province.getText().toString();
 		final String num = bank_card_phone_phone_num.getText().toString();

 		// 银行卡语音充值网络连接
 		// 需要传的参数 100 充值金额 106232601047067 银行卡号 acceptphonenum 接电话手机号
 		// ui还缺少银行卡号
 		// fqc edit 7/13/2010 手机卡在线充值的文本格式 充值金额20元以上的整数金额：输入20以上整数数字 手机号码：数字11位
 		if ((!(num.equals("")) && num != null)
 				&& (!(value.equals("")) && value != null)
 				&& (!(bankid.equals("")) && bankid != null)
 				&& (!(name.equals("")) && name != null)
 				&& (!(idcard.equals("")) && idcard != null)
 				&& (!(home.equals("")) && home != null)
 				&& (!(province.equals("")) && province != null)) {
 			if (num.length() == 11) {
 				if (Integer.parseInt(value) >= 20) {
 					String acceptphonenum = num;
 					strExpand = name + "," + idcard + "," + province
 							+ "," + home + " ," + acceptphonenum + ",false";

 		         	String 		bank_card_id = bankid;
 		         	RechargePojo rechargepojo =new RechargePojo();;
 		         	rechargepojo.setAmount(value);
 		         	rechargepojo.setCardno(bank_card_id);
 		 			rechargepojo.setCardtype("0101");
 		 			rechargepojo.setRechargetype(RECHARGTYPE);
 		 			rechargepojo.setName(name);
 		 	        rechargepojo.setCertid(idcard);
 		 	        rechargepojo.setAddressname(home);
 		 	        rechargepojo.setPhonenum(acceptphonenum);
 		 	        rechargepojo.setBankaddress(province);
 		 	        rechargepojo.setIswhite("false");
 		 	        rechargepojo.setBankName(bankName);
 		 		    recharge(rechargepojo);
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
 	/**
 	 * 开始注册银行电话卡
 	 * @param vi
 	 */
 	private void beginRegisterBankPhoneCard(View vi) {

 		RWSharedPreferences shellRW = new RWSharedPreferences(AccountActivity.this, "addInfo");
 		final String phonenum = shellRW.getStringValue("mobileid");
 		final String sessionid = shellRW.getStringValue("sessionid");
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

 	    String 	bank_card_id = bank_card_phone_bankid_string;
 	        RechargePojo rechargepojo =new RechargePojo();;
 	        rechargepojo.setName(bank_card_phone_username_string);
 	        rechargepojo.setCertid(bank_card_phone_user_idnum_string);
 	        rechargepojo.setCardtype("0101");
 	        rechargepojo.setBankaddress(bank_card_phone_open_bankuser_address_string);
 	        rechargepojo.setPhonenum(bank_card_phone_phone_num_string);
 	        rechargepojo.setAmount(bank_card_phone_recharge_value_string);
			rechargepojo.setCertid(bank_card_id);
			rechargepojo.setRechargetype(RECHARGTYPE);
		    recharge(rechargepojo);
 	}

 	/**
 	 *  手机卡充值
 	 * @param view
 	 */
 	private void beginPhoneCardRecharge(View view) {

 		final EditText phone_card_rechargecard_info = (EditText) view.findViewById(R.id.phone_card_rechargecard_info);
 		final String phone_card_rechargecard_info_string = phone_card_rechargecard_info.getText().toString();
 		final EditText phone_card_rechargecard_password = (EditText) view.findViewById(R.id.phone_card_rechargecard_password);
 		final String phone_card_rechargecard_password_string = phone_card_rechargecard_password.getText().toString();
 		// 手机充值卡充值
 		// 参数含义：0203 充值卡类型 10 充值钱数 5000充值总额 10623260104706723 充值卡号
 		// 261324590869999653 充值密码 y00003银行标识默认
 		// ui缺少充值的钱数
 		if ((!(phone_card_rechargecard_info_string.equals("")) && phone_card_rechargecard_info_string != null)
 				&& (!(phone_card_rechargecard_password_string.equals("")) && phone_card_rechargecard_password_string != null)) {
 			if (isCardString(phone_card_rechargecard_info_string)) {
 				// 改为线程 陈晨 200/7/9
 			RechargePojo rechargepojo =new RechargePojo();;
 			  rechargepojo.setRechargetype(RECHARGTYPE);
 			  rechargepojo.setCardtype(phoneCardType);
 			  rechargepojo.setAmount(phoneCardValue);
 			  rechargepojo.setCardno(phone_card_rechargecard_info_string);
 			  rechargepojo.setCardpwd(phone_card_rechargecard_password_string);
 			  recharge(rechargepojo);
 			} else {
 				Toast.makeText(this, "充值卡序列号应为数字或字母！", Toast.LENGTH_LONG).show();
 			}
 		} else
 			Toast.makeText(this, "不能为空！", Toast.LENGTH_LONG).show();
 	}

 	private void beginGameCardRecharge(View Vi) {

 	final  EditText game_card_number = (EditText) Vi.findViewById(R.id.game_card_number);
 	String	game_card_number_string = game_card_number.getText().toString();
 	final  EditText game_card_password = (EditText) Vi.findViewById(R.id.game_card_password);
 	String	game_card_password_string = game_card_password.getText().toString();
 	final  EditText game_card_total_value = (EditText) Vi.findViewById(R.id.game_card_total_value);
 	String game_card_total_value_string = game_card_total_value.getText().toString();
 		
 		if (game_card_number_string.equals("")|| game_card_password_string.equals("")|| game_card_total_value_string.equals("")) {
 			Message msg = new Message();
 			
 		} else if (isCardString(game_card_number_string)) {
 			RechargePojo rechargepojo =new RechargePojo();;
 			rechargepojo.setRechargetype(RECHARGTYPE);
 			rechargepojo.setCardtype(gameCardType);
 			rechargepojo.setAmount(game_card_total_value_string);
 			rechargepojo.setCardno(game_card_number_string);
 			rechargepojo.setCardpwd(game_card_password_string);
 			recharge(rechargepojo);
 		} else {
 			Toast.makeText(getBaseContext(), "卡号格式输入不正确！", Toast.LENGTH_LONG).show();
 		}
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
 	 * DNA充值账户绑定查询
 	 */
 	public void  checkDNA() {
 	 
 		RECHARGTYPE = "01";//为chenckDNA
 		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
 		final String sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
 		final String mobile = pre.getStringValue(ShellRWConstants.MOBILEID);
 		phonenum = mobile;
 		final String userno = pre.getStringValue("userno");
 		if(sessionId==null||sessionId.equals("")){
 				Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
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
						createBankCardPhoneDialogNo().show();
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

 	/**
 	 * 创建dna对话框
 	 * @版本：
 	 */
 	private String name="";
 	private String certid="";
 	private String bankAddress="";
 	private String certAddress="";
 	private String bankCardNo = "";
 	private String bindPhone = "";
 	private String bankName = "";
 	public void dialogDNA(String str) {
 		String bindState = "";
 		try {
 			JSONObject obj = new JSONObject(str);
 			bindState = obj.getString("bindstate");
 			bankCardNo = obj.getString("bankcardno");
 			name = obj.getString("name");
 			certid =obj.getString("certid");
 			bankAddress = obj.getString("bankaddress");
 			certAddress = obj.getString("addressname");	
 			bindPhone = obj.getString("phonenum");
 			bankName = obj.getString("bankname");
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		if (bindState.equals("1")) {// 已经绑定
 			createBankCardPhoneDialog().show();
 		}else if(bindState.equals("0")){
 			createBankCardPhoneDialogNo().show();
 		}
 	}
    //充值
    private void recharge(final RechargePojo rechargepojo) {
    	RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
		sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
		phonenum = pre.getStringValue(ShellRWConstants.MOBILEID);
		userno = pre.getStringValue(ShellRWConstants.USERNO);
		ConnectivityManager ConnMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ConnMgr.getActiveNetworkInfo();
		if(RECHARGTYPE.equals("05")){
		if(info.getExtraInfo()!=null&&info.getExtraInfo().equalsIgnoreCase("3gwap")){
        	 Toast.makeText(this, "提醒：检测到您的接入点为3gwap，可能无法正确充值,请切换到3gnet！", Toast.LENGTH_LONG).show();
		   }
		}

        showDialog(0); 
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				String error_code = "00";
			    message = "";
				// TODO Auto-generated method stub
				try{
						 rechargepojo.setSessionid(sessionId);
						 rechargepojo.setUserno(userno);
						 String re = RechargeInterface.getInstance().recharge(rechargepojo);
						
						 JSONObject	obj = new JSONObject(re);
						 error_code = obj.getString("error_code");
						 message = obj.getString("message");
						 if(error_code.equals("0000")){
							 if(RECHARGTYPE.equals("05")){
								 url = obj.getString("return_url"); 
							 }else if(RECHARGTYPE.equals("03")){
								 url = obj.getString("reqestUrl");  
							 }else if(RECHARGTYPE.equals("06")){
								 xml = obj.getString("value");  
							 }
						 }
					 }catch(JSONException e){
						e.printStackTrace();
					 }
					 if(error_code.equals("001400")){
						 handler.post(new Runnable() {
							public void run() {
								createBankPhoneCardRegisterDialog().show();	
							}
						 });
					 }else{
						 handler.handleMsg(error_code, message);
					 }
			     progressdialog.dismiss();
			}
		}).start();
		
	}


    
    //添加充值方式数据
	private List<Map<String, Object>> setContentForMainList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(2);
        
		Map<String, Object> map;
		// 支付宝安全支付
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_secure_recharge));
		map.put(PICTURE, R.drawable.recharge_alipay_safe);
		map.put(ISHANDINGFREE,getString(R.string.account_zfb_secure));
		list.add(map);
		// 支付宝充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.zhfb_cards_recharge));
		map.put(PICTURE, R.drawable.recharge_alipay);
		map.put(ISHANDINGFREE,getString(R.string.account_zfb_alert));
		list.add(map);
		
		//银联支付
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.yin_bank_cards_recharge));
		map.put(PICTURE,R.drawable.recharge_bank);
		map.put(ISHANDINGFREE,getString(R.string.account_yinlian_alert));
		list.add(map);
		// 银行卡电话充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.bank_cards_recharge));
		map.put(PICTURE,R.drawable.recharge_phone);
		map.put(ISHANDINGFREE,getString(R.string.account_card_alert));
		list.add(map);
		// 拉卡拉充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.la_ka_la_recharge));
		map.put(PICTURE,R.drawable.lakala_icon);
		map.put(ISHANDINGFREE,getString(R.string.la_ka_la_alert));
		list.add(map);
		// 手机充值卡充值
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.phone_cards_recharge));
		map.put(PICTURE, R.drawable.recharge_phonebank);
		map.put(ISHANDINGFREE,getString(R.string.account_phone_alert));
		list.add(map);
		
		//银行支付
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.account_chongzhi));
		map.put(PICTURE, R.drawable.account_chongzhi);
		map.put(ISHANDINGFREE,getString(R.string.account_chongzhi_alert));
		list.add(map);
		
		// 银行转账
		map = new HashMap<String, Object>();
		map.put(TITLE, getString(R.string.atm_recharge));
		map.put(PICTURE, R.drawable.recharge_atm);
		map.put(ISHANDINGFREE,getString(R.string.account_zhuanzhang_alert));
		list.add(map);
	    return list;
	}
	  
	
    /**
     * 重写放回建
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		   case 4:
			if(isonkey.equals("fasle")){
				this.finish();
			}else{
	        ExitDialogFactory.createExitDialog(this);
			}
           break;
		}
		return false;
	}
	/**
 	 * intent回调函数
 	 * 用户登录过后直接弹出充值框
 	 */
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		switch (resultCode) {
 		case RESULT_OK:
 			 onClickList(); 
 			break;
 		}
 	}

	private void onClickList() {
		Log.v("textString", textString);
		if ("手机充值卡充值".equals(textString)) {
			  Intent intent = new Intent(AccountActivity.this,PhoneCardRechargeActivity.class);
				startActivity(intent);
		} else if ("支付宝充值(免手续费)".equals(textString)) {
			Intent intent = new Intent(AccountActivity.this,AlipaySecureActivity.class);
			startActivity(intent);
		}else if ((getString(R.string.zhfb_cards_secure_recharge)+"(免手续费)").equals(textString)){//支付宝安全支付
			  createAlipaySecureDialog();
		} else if ((getString(R.string.bank_cards_recharge)+"(免手续费)").equals(textString)) {//银联语音支付
			if(isLogin()){
				Intent intent = new Intent(AccountActivity.this,YinDNAPayActivity.class);
				startActivity(intent);
			}
		}else if ((getString(R.string.la_ka_la_recharge)).equals(textString)) {//拉卡拉支付
			if(isLogin()){
				Intent intent = new Intent(AccountActivity.this,LakalaActivity.class);
				startActivity(intent);
			}
		}else if ("银联充值(免手续费)".equals(textString)) {//银联支付
			if(isLogin()){
				Intent intent = new Intent(AccountActivity.this,YinPayActivity.class);
				startActivity(intent);
			}
		}else if ((getString(R.string.account_chongzhi)+"(推荐)").equals(textString)) {//银行充值
		      createAccountDialog();  
		}else if (getString(R.string.atm_recharge).equals(textString)) {//银行转账
			
		      Intent intent3 = new Intent(AccountActivity.this, Accoutmovecash.class);
			  startActivity(intent3);	     
		}
	}
	public boolean isLogin(){
		boolean isLogin = false;
 		RWSharedPreferences pre = new RWSharedPreferences(this, "addInfo");
 		String userno = pre.getStringValue(ShellRWConstants.USERNO);
 		if(userno==null||userno.equals("")){
 			isLogin = false;
			Intent intentSession = new Intent(this,UserLogin.class);
			startActivityForResult(intentSession,0);
		}else{
			isLogin = true;
		}
 		return isLogin;
	}
 	
 	private void createAlipaySecureDialog(){
 		Intent intent = new Intent(this,AlipaySecureActivity.class);
// 		this.startActivity(intent);
 		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
 		final String sessionId = pre.getStringValue(ShellRWConstants.SESSIONID);
 		final String mobile = pre.getStringValue(ShellRWConstants.MOBILEID);
 		phonenum = mobile;
 		final String userno = pre.getStringValue(ShellRWConstants.USERNO);
 		if(sessionId==null||sessionId.equals("")){
			Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
			startActivityForResult(intentSession,0);
 		}else{
 		//检测是否安装了支付宝安全控件
			Intent alipay_secure = new Intent(AccountActivity.this,AlipaySecurePayDialog.class);
			startActivity(alipay_secure);
 		}
 	}
 	private void createAccountDialog(){
 		RWSharedPreferences pre = new RWSharedPreferences(AccountActivity.this, "addInfo");
 		final String userno = pre.getStringValue(ShellRWConstants.USERNO);
 		if(userno==null||userno.equals("")){
			Intent intentSession = new Intent(AccountActivity.this,UserLogin.class);
			startActivityForResult(intentSession,0);
 		}else{
			Intent alipay_secure = new Intent(AccountActivity.this,AccountYingActivity.class);
			startActivity(alipay_secure);
 		}
 	}
 	    
 	//主列表Item点击事件处理类
    public class AccountMainItemListener implements OnItemClickListener{
	    private Context context;
	    public AccountMainItemListener(Context context){
	    	    this.context = context;
	    }
		@Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		    TextView AccountType = (TextView) view.findViewById(R.id.account_recharge_listview_text);
			textString = AccountType.getText().toString();
			onClickList();
		}
    }
    
    //
	// the OnCancelListener for lephone platform.
	public static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}
	// 启动插件(进入支付页面)
	public final static String CMD_PAY_PLUGIN = "cmd_pay_plugin";
	// 启动插件(进入用户管理页面)
	public final static String CMD_USERS_PLUGIN = "cmd_user_plugin";
	private String MAIN_ACTION = "com.palmdream.RuyicaiAndroid.back.view";

    /**
     * 银联充值跳转到插件
     */
    public void turnYinView(String info){
		// 向插件提交3要素报文
		// *********************************************************************************//

		byte[] to_upomp = info.getBytes();
		// 此处的"com.gq.lthj.testcase"为商户的包名
		ComponentName com = new ComponentName(Constants.MERCHANT_PACKAGE,
				"com.unionpay.upomp.lthj.plugin.ui.MainActivity");
		// 设置Intent指向
		Intent intent = new Intent();
		intent.setComponent(com); // 启动插件(进入支付页面)
		intent.putExtra("action_cmd", CMD_PAY_PLUGIN);
		// 启动插件(进入用户管理页面)
		// intent. putExtra("action_cmd", CMD_USERS_PLUGIN);
		// 将封装好的xml报文传入bundle
		Bundle mbundle = new Bundle();
		// to_upomp为商户提交的XML
		mbundle.putByteArray("xml", to_upomp);
		// 注：此处的action是：商户的action
		mbundle.putString("merchantPackageName", MAIN_ACTION);
		// 将bundle置于intent中
		intent.putExtras(mbundle);
		// 使用intent跳转至手机POS
		startActivity(intent);
    }
   	    /**
    	 * 网络连接提示框
    	 */
    	protected Dialog onCreateDialog(int id) {
    		switch (id) {
    		case 0: {
    			progressdialog = new ProgressDialog(this);
    			// progressdialog.setTitle("Indeterminate");
    			progressdialog.setMessage("网络连接中...");
    			progressdialog.setIndeterminate(true);
    		}
    			progressdialog.setCancelable(true);
    			return progressdialog;
    		}
    		return null;
    	} 
		@Override
		public void errorCode_0000() {
			// TODO Auto-generated method stub
			if(RECHARGTYPE.equals("05")){//RECHARGTYPE="03"为手机银行充值确认已经干掉，这里注销
				if(isWebView){
					Intent intent = new Intent(AccountActivity.this,AccountDialog.class);
					intent.putExtra("accounturl", url);
					startActivity(intent);
				}else{
					PublicMethod.openUrlByString(AccountActivity.this, url);
				}
			
			}else if(RECHARGTYPE.equals("06")){
				turnYinView(xml);
			}
			 RechargeType.dismiss();
			 if(!RECHARGTYPE.equals("06")){
			 Toast.makeText(AccountActivity.this,message ,Toast.LENGTH_SHORT).show();
			}
			 finish();
		}
		@Override
		public void errorCode_000000() {
		}
		@Override
		public Context getContext() {
			// TODO Auto-generated method stub
			return this;
		}
}