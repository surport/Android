/**
 * 
 */
package com.ruyicai.activity.usercenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.join.JoinCheckActivity;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.CancelWithdrawInterface;
import com.ruyicai.net.newtransaction.ChangePasswordInterface;
import com.ruyicai.net.newtransaction.ChangeWithdrawInterface;
import com.ruyicai.net.newtransaction.QueryJoinCheckInterface;
import com.ruyicai.net.newtransaction.QueryLatelyWithdrawInterface;
import com.ruyicai.net.newtransaction.pojo.ChangeWithdrawPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 账户提现
 * @author Administrator
 *
 */
public class AccountWithdrawActivity extends Activity implements HandlerMsg{
	private String re;
	private JSONObject obj;	
	private String phonenum,sessionid,userno;
	private boolean start = true;	
	private int state = 0;
	TextView nameText,bankText,addText,numText,moneyText;//待审核

	Vector WinningVector;
	String bankName = "00";
	String moneyNum = "00";
	String bankaccount;
	String name;
	String areaName;
	Button b_canceltranking;
	JSONObject json;
	MyHandler handler;//自定义handler
	Handler handlerTwo = new Handler();
	private ProgressDialog progressdialog;
	AccountInfo info = new AccountInfo();
	//
    private static final String[] allBankName={"中国工商银行","招商银行","中国建设银行","中国农业银行","交通银行","上海浦东发展银行"
    	                                        ,"广东发展银行","中国光大银行","兴业银行","深圳发展银行","中国民生银行"};   
    String allName[] = null;
    List allcountries=null;   
    ArrayAdapter<String> adapter; 
    boolean isQuery=true;//是否是查询提现
    String message= "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		handler = new MyHandler(this);
		getInfo();
//		setContentView(getMoneyDialog());
		stateNet();
	}
	/**
	 * 获取登录后的信息
	 */
	public void getInfo(){
		 ShellRWSharesPreferences shellRW = new ShellRWSharesPreferences(AccountWithdrawActivity.this, "addInfo");
		 phonenum = shellRW.getUserLoginInfo("phonenum");
		 sessionid = shellRW.getUserLoginInfo("sessionid");
		 userno = shellRW.getUserLoginInfo("userno");
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
									setContentView(getMoneyDialog());
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
	public void cancelNet(){
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = CancelWithdrawInterface.cancelWithdraw(userno, sessionid, phonenum,info.getCashdetailId());
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
	 * 提现窗口
	 */
	protected View getMoneyDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View getView = inflater.inflate(R.layout.get_money_submit, null);
		final EditText name = (EditText) getView.findViewById(R.id.get_money_name_edit);
		final Spinner money_brank = (Spinner) getView.findViewById(R.id.get_money_bank_spinner);
		//
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
		final EditText money_bank_start = (EditText) getView.findViewById(R.id.get_money_bank_start_edit);
		final EditText money_bank_num = (EditText) getView.findViewById(R.id.get_money_bank_num_edit);
		final EditText money = (EditText) getView.findViewById(R.id.get_money_money_edit);
        Button submit = (Button) getView.findViewById(R.id.get_money_img_submit);
        submit.setBackgroundResource(R.drawable.join_info_btn_selecter);
        Button cancel = (Button) getView.findViewById(R.id.get_money_img_back);
        cancel.setBackgroundResource(R.drawable.join_info_btn_selecter);
        submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				ChangeWithdrawPojo changeWithdPojo = new ChangeWithdrawPojo();
				String nameStr = name.getText().toString();
				String moneyStr = money.getText().toString();
				String bankNameStr = bankName;
				String bankAreaStr = money_bank_start.getText().toString();
				String bankNoStr  = money_bank_num.getText().toString();
				changeWithdPojo.setUserno(userno);
				changeWithdPojo.setPhonenum(phonenum);
				changeWithdPojo.setSessionid(sessionid);
				changeWithdPojo.setBankname(bankNameStr);
				if( nameStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "持卡人名不能为空！", Toast.LENGTH_SHORT).show();
				}else if( bankAreaStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "开卡地址不能为空！", Toast.LENGTH_SHORT).show();
				}else if(bankNoStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "银行卡号不能为空！", Toast.LENGTH_SHORT).show();
				}else if(moneyStr.equals("")){
					Toast.makeText(AccountWithdrawActivity.this, "提现金额不能为空！", Toast.LENGTH_SHORT).show();
				}else if(Integer.parseInt(moneyStr)<1){
					Toast.makeText(AccountWithdrawActivity.this, "至少提现一元！", Toast.LENGTH_SHORT).show();
				}else{
					changeWithdPojo.setName(nameStr);
					changeWithdPojo.setAmount(PublicMethod.toFen(moneyStr));
					changeWithdPojo.setAraeaname(bankAreaStr);
					changeWithdPojo.setBankcardno(bankNoStr);
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
        money_bank_start.append(info.getAdd());
        money_bank_num.append(info.getNum());
        money .append(info.getMoney());
		return getView;
	}


	/**
	 * 提现待审核窗口
	 */
	protected View waitDialog() {
		start = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		View waitView = inflater.inflate(R.layout.get_money_stating, null);
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
				setContentView(getMoneyDialog());			
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancelNet();
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
				setContentView(getMoneyDialog());
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
				setContentView(getMoneyDialog());			
			}
		});
        cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();		
			}
		});
		return successView;
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
		if(isQuery){
			isQuery = false;
			info = createAccountInfo(json);
			state = Integer.parseInt(info.getState());
			switch (state) {
			case 0:// 提现窗口
				setContentView(getMoneyDialog());
				break;
			case 31:// 修改提现窗口
//				changeDialog();
				break;
			case 1:// 待审核
				setContentView(waitDialog());
				break;
			case 102:// 审核中
				setContentView(waitingDialog());
				break;
			case 103:// 审核中
				setContentView(waitingDialog());
				break;
			case 104:// 驳回
				setContentView(failDialog());
				break;
			case 106:// 驳回
				setContentView(failDialog());
				break;
			case 105:// 已通过审核
				setContentView(successDialog());
				break;
			}	
		}else{
			Toast.makeText(AccountWithdrawActivity.this,message, Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	public void errorCode_000000() {
	}

	public Context getContext() {
		return this;
	}
	/**
	 * 提现信息类
	 * @author Administrator
	 *
	 */
	class AccountInfo{
		String state="0",nameStr = "",bank = "",add ="",num = "",money ="0",cashdetailId;
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
}
