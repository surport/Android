/**
 * 
 */
package com.ruyicai.activity.join;

import java.text.NumberFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.JoinInInterface;
import com.ruyicai.net.newtransaction.QueryJoinDetailInterface;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 合买详情
 * @author Administrator
 * 
 */
public class JoinDetailActivity extends Activity implements HandlerMsg{
	private TextView name,describe,atm,id,renAtm,baoAtm,progress,state,shengAtm,person,
	                 deduct,content,amountProgress,amountText,safeProgress,safeText,minText;
	private LinearLayout starLayout;
	private EditText amountEdit,safeAmtEdit;
	private ImageButton joinInImg;
	private ProgressDialog progressdialog;        
	private String caseId="",issue="";
	private String lotno = "F47104";
	private String phonenum,sessionid,userno,amount,safeAmt;
	JoinDetatil detatil = new JoinDetatil();
	MyHandler handler = new MyHandler(this);//自定义handler
	JSONObject json;
	boolean isJoinIn = false;
	boolean isVisable = false;
	String message;
	int bao=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.join_detail);
		getInfo();
		init();
		joinDetailNet();
	}
	public void getInfo(){
		Intent intent =getIntent();
		if(intent!=null){
			caseId = intent.getStringExtra(JoinInfoActivity.ID);
			lotno = intent.getStringExtra(JoinHallActivity.LOTNO);
			issue = intent.getStringExtra(JoinHallActivity.ISSUE);
		}
	}
	/**
	 * 初始化组件
	 */
	public void init() {
		TextView title = (TextView) findViewById(R.id.join_detail_text_title);
//		title.append("-"+PublicMethod.toLotno(lotno));
		Button imgRetrun = (Button) findViewById(R.id.join_detail_img_return);
		imgRetrun.setBackgroundResource(R.drawable.returnselecter);
		// ImageView的返回事件
		imgRetrun.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 JoinInfoActivity.isRefresh = false;
				 finish();
			}
		});
		name = (TextView) findViewById(R.id.join_detail_text_name);
		starLayout = (LinearLayout)findViewById(R.id.join_detail_linear_record);
		describe = (TextView) findViewById(R.id.join_detail_text_describe);
		atm = (TextView) findViewById(R.id.join_detail_text_atm);
		id = (TextView) findViewById(R.id.join_detail_text_num);
		baoAtm = (TextView) findViewById(R.id.join_detail_text_baodi_atm);
		renAtm = (TextView) findViewById(R.id.join_detail_text_rengou_atm);
		progress = (TextView) findViewById(R.id.join_detail_tex_progress);
		state = (TextView) findViewById(R.id.join_detail_text_state);
		shengAtm = (TextView) findViewById(R.id.join_detail_text_shengyu_atm);
		person = (TextView) findViewById(R.id.join_detail_text_person);
		deduct = (TextView) findViewById(R.id.join_detail_text_get);
		content = (TextView) findViewById(R.id.join_detail_text_context);
        amountProgress = (TextView) findViewById(R.id.join_detail_text_rengou_progress);        
        amountText = (TextView) findViewById(R.id.join_detail_text_rengou_sheng);
        safeProgress = (TextView) findViewById(R.id.join_detail_text_baodi_progress);
        safeText = (TextView) findViewById(R.id.join_detail_text_baodi_sheng);
        minText = (TextView) findViewById(R.id.join_detail_text_rengou_min);
        amountEdit = (EditText) findViewById(R.id.join_detail_edit_rengou);
        safeAmtEdit = (EditText) findViewById(R.id.join_detail_edit_baodi);
        joinInImg = (ImageButton) findViewById(R.id.join_detail_img_buy);
        joinInImg.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
                isLogin();       				
			}
		});
		amountEdit.addTextChangedListener(new TextWatcher() {
			
		
			public void afterTextChanged(Editable s) {
				String amount = amountEdit.getText().toString();
				String renAmt = leavMount(detatil.getShengAtm(),amountEdit.getText().toString());
				amountEdit.setClickable(true);
				amountEdit.setEnabled(true);		
				String amt = detatil.getAtm();
				amountProgress.setText("占总额" +  progress(isNull(amount),amt) + "%");// 总金额
				leavTextView(amountText,true);
				leavTextView(safeText,false);
		
			}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			public void onTextChanged(CharSequence s, int start, int before,int count) {
			}

		});
		safeAmtEdit.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				String amt = safeAmtEdit.getText().toString();
				amt = isNull(amt);
				String renAmt = leavMount(detatil.getShengAtm(),amountEdit.getText().toString());
				String baoAmt = leavMount(renAmt,detatil.getBaoAtm());
				safeAmtEdit.setClickable(true);
				safeAmtEdit.setEnabled(true);
			    safeProgress.setText("占总额" +  progress(isNull(amt),detatil.getAtm()) + "%");
			    if(Integer.parseInt(baoAmt)>0){
			    	leavTextView(safeText,false);
			    }
			}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {

			}
			public void onTextChanged(CharSequence s, int start, int before,int count) {
				if (s.length() > 0) {
				
				} else {
					safeProgress.setText("占总额0%");
				}
			}

		});
        amountProgress.setText("占总额" +  progress(amountEdit.getText().toString(),detatil.getAtm()) + "%");
        safeProgress.setText("占总额" +  progress(safeAmtEdit.getText().toString(),detatil.getAtm()) + "%");
		leavTextView(amountText,true);
		leavTextView(safeText,false);
		//linshi
	  	PublicMethod.createStar(starLayout,detatil.getCrown(),detatil.getCup(),detatil.getDiamond(),
      			detatil.getStar(),JoinDetailActivity.this);
	}
	public String  progress(String amt,String allAmt){
		if(allAmt.equals("0")){
		   return "0";
		}else{
		   float amount =  Integer.parseInt(amt);
		   float allAmount = Integer.parseInt(allAmt);
		   float progress = (amount/allAmount)*100;
		   NumberFormat   formatter   =   NumberFormat.getNumberInstance(); 
		   formatter.setMaximumFractionDigits(1); 
		   formatter.setMinimumFractionDigits(1); 
		   return  formatter.format(progress);
		}
	}
	public String leavMount(String allAmt,String amt){
		String amtStr="";
		amtStr = Integer.toString(Integer.parseInt(isNull(allAmt))-Integer.parseInt(isNull(amt)));
		return amtStr;
	}
	public void leavTextView(TextView text,boolean isRen){
        SpannableStringBuilder builder = new SpannableStringBuilder(); 
		String renAmt = leavMount(detatil.getShengAtm(),amountEdit.getText().toString());
		String amt = leavMount(renAmt,detatil.getBaoAtm());
		String baoAmt = leavMount(amt,safeAmtEdit.getText().toString());
		String textStr ="";
		int ren = Integer.parseInt(renAmt);
		if(isRen){
			if(ren<0){
				amountEdit.setText(detatil.getShengAtm());		
				textStr ="剩余￥0可认购";
			}else{
				textStr ="剩余￥"+renAmt+"可认购";
			}
		
		}else{
			safeAmtEdit.setEnabled(true);
			int bao = Integer.parseInt(baoAmt);
			if(bao<0){
				if(Integer.parseInt(amt)<0){
					safeAmtEdit.setText("0");
				}else {
					safeAmtEdit.setText(amt);
				}
				safeAmtEdit.setEnabled(false);
				this.bao=0;
				textStr = "剩余￥0可保底";
			}else if(bao == 0){
				safeAmtEdit.setEnabled(false);
				this.bao = Integer.parseInt(baoAmt);
				textStr = "剩余￥"+baoAmt+"可保底";
			}else{
				this.bao = Integer.parseInt(baoAmt);
				textStr = "剩余￥"+baoAmt+"可保底";
			}
		}
		builder.append(textStr);
		builder.setSpan(new ForegroundColorSpan(Color.RED), 2, textStr.length()-3, Spanned.SPAN_COMPOSING);  
	    text.setText(builder, BufferType.EDITABLE);
		
	}
	/**
	 *  从后台获取值
	 */
	public JoinDetatil getValue(){
		

		try{
			detatil.setName(json.getString("starter"));
			detatil.setStar("");
			detatil.setDescribe(json.getString("description"));
			detatil.setAtm(json.getString("totalAmt"));
			detatil.setId(json.getString("caseLotId"));
			detatil.setRenAtm(json.getString("buyAmt"));
			detatil.setBaoAtm(json.getString("safeAmt"));
			detatil.setProgress(json.getString("progress"));
			detatil.setState(json.getString("displayStateMemo"));
			detatil.setShengAtm(json.getString("remainderAmt"));
			detatil.setPerson(json.getString("participantCount"));
			detatil.setDeduct(json.getString("commisionRatio"));
			detatil.setContent(json.getString("content"));
			detatil.setMinAmt(json.getString("minAmt"));
            JSONObject displayIcon = json.getJSONObject("displayIcon");
            try{
            	detatil.setCup(displayIcon.getString("cup"));
            }catch(Exception e){
            }
            try{
            	detatil.setDiamond(displayIcon.getString("diamond"));
            }catch(Exception e){
            }
            try{
            	detatil.setStarNum(displayIcon.getString("goldStar"));
            }catch(Exception e){
            }
            try{
            	detatil.setCrown(displayIcon.getString("crown"));
            }catch(Exception e){
            }
		}catch(Exception e){
			
		}
        return detatil;
	}
	/**
	 * 赋值值
	 */
	public void setValue(JoinDetatil detatil){
		
		name.append(detatil.getName());
		describe.append(detatil.getDescribe());
		atm.append("￥"+detatil.getAtm());
		id.append(detatil.getId());
		baoAtm.append("￥"+detatil.getBaoAtm());
		renAtm.append("￥"+detatil.getRenAtm());
		progress.append(detatil.getProgress());
		state.append(detatil.getState());
		shengAtm.append("￥"+detatil.getShengAtm());
		person.append(detatil.getPerson());
		deduct.append(detatil.getDeduct());
		content.append(detatil.getContent());
        amountProgress.setText("占总额" +  progress(amountEdit.getText().toString(),detatil.getAtm()) + "%");
        safeProgress.setText("占总额" +  progress(safeAmtEdit.getText().toString(),detatil.getAtm()) + "%");
        showMinText(detatil.getMinAmt());
		leavTextView(amountText,true);
		leavTextView(safeText,false);
      	PublicMethod.createStar(starLayout,detatil.getCrown(),detatil.getCup(),detatil.getDiamond(),
      			detatil.getStarNum(),JoinDetailActivity.this);

	}
	/**
	 * 显示最低认购金额
	 */
	public void showMinText(String minText){
		String renAmt = leavMount(detatil.getShengAtm(),amountEdit.getText().toString());
		if(Integer.parseInt(minText)>Integer.parseInt(renAmt)||Integer.parseInt(minText)==0){
			isVisable = false;
			this.minText.setVisibility(TextView.GONE);
		}else{
			isVisable = true;
		    SpannableStringBuilder builder = new SpannableStringBuilder(); 
	        String textStr = "至少认购￥";
	        textStr += minText;
			builder.append(textStr);
			builder.setSpan(new ForegroundColorSpan(Color.RED), 4, textStr.length(), Spanned.SPAN_COMPOSING);  
		    this.minText.setText(builder, BufferType.EDITABLE);
		}
	}
	/**
	 * 判断是否登录
	 */
	public void isLogin(){
		getLoginInfo();
		if (sessionid == null || sessionid.equals("")) {
			Intent intentSession = new Intent(JoinDetailActivity.this,UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			 isJoinInNet();
		}
	}
	/**
	 * 获取登录信息
	 */
	public void getLoginInfo(){
		RWSharedPreferences shellRW = new RWSharedPreferences(JoinDetailActivity.this, "addInfo");
		phonenum = shellRW.getStringValue("phonenum");
		sessionid = shellRW.getStringValue("sessionid");
		userno = shellRW.getStringValue("userno");
	}
	/**
	 * 判断是否联网
	 */
	public void isJoinInNet(){
		amount = amountEdit.getText().toString();
		safeAmt = safeAmtEdit.getText().toString();
		String renAmt = leavMount(detatil.getShengAtm(),amountEdit.getText().toString());
			int amountInt = Integer.parseInt(isNull(amount));
			int safeAmtInt = Integer.parseInt(isNull(safeAmt));
			if(amount.equals("")&&safeAmt.equals("")){
				Toast.makeText(JoinDetailActivity.this, "认购金额或保底金额不能为空", Toast.LENGTH_SHORT).show();
			}else if(amountInt==0&&safeAmtInt==0){
				Toast.makeText(JoinDetailActivity.this, "认购金额和保底金额不能都为零", Toast.LENGTH_SHORT).show();
			}else if(!isVisable){
				joinInNet();
			}else if(safeAmtInt!=0&& amountInt==0){
				joinInNet();
			}else if(amountInt<Integer.parseInt(detatil.getMinAmt())){
				Toast.makeText(JoinDetailActivity.this, "请您至少认购"+detatil.getMinAmt()+"元", Toast.LENGTH_SHORT).show();
			}
//			else if(safeAmtInt>bao){
//				Toast.makeText(JoinDetailActivity.this, "保底金额不正确", Toast.LENGTH_SHORT).show();
//			}
			else{
				joinInNet();
			}
			
	}
	/**
	 * 参与合买
	 */
	public void joinInNet(){
		isJoinIn = true;
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = JoinInInterface.betLotJoin(userno, phonenum, caseId, PublicMethod.toFen(isNull(amount)), PublicMethod.toFen(isNull(safeAmt)));
					try {
						json = new JSONObject(str);
						message = json.getString("message");
						String error = json.getString("error_code");
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
	 * 联网查询
	 */
	public void joinDetailNet(){
		getLoginInfo();
		showDialog(0); // 显示网络提示框 2010/7/4
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
					str = QueryJoinDetailInterface.queryLotJoinDetail(caseId,userno,phonenum);
					try {
						json = new JSONObject(str);
						String msg = json.getString("message");
						String error = json.getString("error_code");
						handler.handleMsg(error,msg);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					progressdialog.dismiss();
			}

		});
		t.start();
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
	/**
	 * 参与合买成功
	 * @param title
	 * @param string
	 */
	public void succeedDialog(String title, String string) {

		Dialog dialog = new AlertDialog.Builder(this).setMessage(string)
				.setTitle(title).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								 JoinInfoActivity.isRefresh = true;
					             finish();
							}
						}).create();
		dialog.setCancelable(false);
		dialog.show();

	}
	/**
	 * 
	 * @author Administrator
	 *
	 */
	class JoinDetatil{
		private String name="",star="",describe="",atm="0",id="",renAtm="0",baoAtm="0",progress="",
		        state="",shengAtm="0",person="",deduct="",content="";
		String minAmt = "0";//最低认购金额
		String crown = "0";//皇冠
		String cup = "0";//奖杯
		String diamond= "0";//钻石
		String starNum = "0";//星
		public String getMinAmt() {
			return PublicMethod.toIntYuan(minAmt);
		}
		public void setMinAmt(String minAmt) {
			this.minAmt = minAmt;
		}
		public String getStarNum() {
			return starNum;
		}
		public void setStarNum(String starNum) {
			this.starNum = starNum;
		}
		public String getCrown() {
			return crown;
		}
		public void setCrown(String crown) {
			this.crown = crown;
		}
		public String getCup() {
			return cup;
		}
		public void setCup(String cup) {
			this.cup = cup;
		}
		public String getDiamond() {
			return diamond;
		}
		public void setDiamond(String diamond) {
			this.diamond = diamond;
		}
        public JoinDetatil(){
        	
        }
        
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getStar() {
			return star;
		}

		public void setStar(String star) {
			this.star = star;
		}

		public String getDescribe() {
			if(describe==null){
				return "";
			}else{
				return describe;
			}
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public String getAtm() {
			return atm;
		}

		public void setAtm(String atm) {
			this.atm = Integer.toString(Integer.parseInt(atm)/100);
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRenAtm() {
			return renAtm;
		}

		public void setRenAtm(String renAtm) {
			this.renAtm = Integer.toString(Integer.parseInt(renAtm)/100);
		}

		public String getBaoAtm() {
			return baoAtm;
		}

		public void setBaoAtm(String baoAtm) {
			this.baoAtm = Integer.toString(Integer.parseInt(baoAtm)/100);
		}

		public String getProgress() {
			return progress+"%";
		}

		public void setProgress(String progress) {
			this.progress = progress;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getShengAtm() {
			return shengAtm;
		}

		public void setShengAtm(String shengAtm) {
			this.shengAtm = Integer.toString(Integer.parseInt(shengAtm)/100);
		}

		public String getPerson() {
			return person;
		}

		public void setPerson(String person) {
			this.person = person+"人";
		}

		public String getDeduct() {
			return deduct;
		}

		public void setDeduct(String deduct) {
			this.deduct = deduct+"%";
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
	/**
	 *  网络连接框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: {
			progressdialog = new ProgressDialog(this);
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			return progressdialog;
		}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_0000()
	 */
	@Override
	public void errorCode_0000() {
		if(isJoinIn){
			succeedDialog("参与合买成功",message);
		}else{
            setValue(getValue());
		}     
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#errorCode_000000()
	 */
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see com.ruyicai.handler.HandlerMsg#getContext()
	 */
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}
    /**
     * 重写放回建
     */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		   case 4:
			     JoinInfoActivity.isRefresh = false;
				 finish();
           break;
		}
		return false;
	}
	/**
	 * 判断字符串是否是空值
	 * 
	 */
	public String isNull(String str){
		String string;
		if(str==null||str.equals("")){
			return "0";
		}else{
			return str;
		}
		
	}
}
