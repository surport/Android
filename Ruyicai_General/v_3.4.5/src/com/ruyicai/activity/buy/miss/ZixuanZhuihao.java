package com.ruyicai.activity.buy.miss;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ApplicationAddview;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.usercenter.UserCenterDialog;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.getRecoveryBatchCode;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;

public class ZixuanZhuihao extends Activity implements HandlerMsg,OnSeekBarChangeListener{
	String phonenum,sessionId,userno;
	protected ProgressDialog progressdialog;
	private BetAndGiftPojo betAndGift;// 投注信息类
	String lotno;
	TextView alertText;
	TextView issueText;
	Button codeInfo;
	MyHandler handler = new MyHandler(this);//自定义handler
	TextView textAlert;
	TextView textZhuma;
    TextView textTitle;	
    LinearLayout zhuiqishezhi;
    Button zhuiqi;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected EditText mTextBeishu,mTextQishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	TextView zhushu;
	TextView jine;
	TextView caizhong;
	private boolean toLogin = false;
	public boolean isTouzhu = false;//是否投注
	ArrayList<String> batchcodes = new ArrayList<String>();
	int state=0;
	public ArrayList<Checktouinfo> subscribeInfocheck = new ArrayList<Checktouinfo>();
	Checktouinfo checkinfo[];
	private AddViewMiss addviewmiss;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_zhuihao);
		ApplicationAddview app=(ApplicationAddview)getApplicationContext();
		betAndGift=app.getPojo();
		addviewmiss=app.getAddviewmiss();
		init();
	}
	
	private void init(){
		initImageView();
		if(betAndGift.isZhui()){
			initZhuiJia();
		}
		zhuiqishezhi=(LinearLayout)findViewById(R.id.zhuiqishezhi);
		zhuiqi=(Button)findViewById(R.id.zhuiqi);
		zhuiqi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(state==0){
				getbatchcodes();
				state=2;
				}else if(state==1){
					zhuiqishezhi.setVisibility(View.VISIBLE);
					state=2;
				}else if(state==2){
					zhuiqishezhi.setVisibility(View.GONE);
					state=1;

				}
			}
		});
		zhushu = (TextView)findViewById(R.id.alert_dialog_touzhu_textview_zhushu);
		jine=(TextView) findViewById(R.id.alert_dialog_touzhu_text_one);
		caizhong=(TextView)findViewById(R.id.alert_dialog_touzhu_textview_caizhong);
		caizhong.setText(PublicMethod.toLotno(betAndGift.getLotno()));
		issueText =(TextView) findViewById(R.id.alert_dialog_touzhu_textview_qihao);
		textZhuma =(TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma);
		textTitle = (TextView) findViewById(R.id.alert_dialog_touzhu_text_zhuma_title);
		addviewmiss.getCodeList().get(addviewmiss.getSize()-1).setTextCodeColor(textZhuma);
		issueText.setText("第"+PublicMethod.toIssue(betAndGift.getLotno())+"期");
		textTitle.setText("注码："+"共有"+addviewmiss.getSize()+"笔投注");
		getTouzhuAlert();
		Button cancel = (Button) findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) findViewById(R.id.alert_dialog_touzhu_button_ok);
		codeInfo = (Button) findViewById(R.id.alert_dialog_touzhu_btn_look_code);
		isCodeText(codeInfo);
		CheckBox checkPrize = (CheckBox) findViewById(R.id.alert_dialog_touzhu_check_prize);
		checkPrize.setChecked(true);
		checkPrize.setButtonDrawable(R.drawable.check_on_off);
		checkPrize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					betAndGift.setPrizeend("1");
				} else {
					betAndGift.setPrizeend("0");
				}
			}
		});
		codeInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addviewmiss.createCodeInfoDialog(ZixuanZhuihao.this);
				addviewmiss.showDialog();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			  finish();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				RWSharedPreferences pre = new RWSharedPreferences(ZixuanZhuihao.this, "addInfo");
				sessionId = pre.getStringValue("sessionid");
				phonenum = pre.getStringValue("phonenum");
				userno = pre.getStringValue("userno");
				if (userno.equals("")) {
					toLogin = true;
					Intent intentSession = new Intent(ZixuanZhuihao.this, UserLogin.class);
					startActivityForResult(intentSession, 0);
				} else {
					touZhu();
				}
			}
		});
		
	}
	
	
	private void isCodeText(Button codeInfo) {
	if(addviewmiss.getSize()>1){
		codeInfo.setVisibility(Button.VISIBLE);
	}else{
		codeInfo.setVisibility(Button.GONE);
	}
}
	
	/**
	 * 显示追加投注
	 * @param view
	 */
	private void initZhuiJia() {
		LinearLayout toggleLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		ToggleButton zhuijiatouzhu = (ToggleButton)findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {			

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if(isChecked){
					betAndGift.setAmt(3);
					betAndGift.setIssuper("0");
				}else{
					betAndGift.setIssuper("");
					betAndGift.setAmt(2);
				}
				addviewmiss.setCodeAmt(betAndGift.getAmt());
				getTouzhuAlert();
			}
		});
	}
	
	
	
	public void getbatchcodes() {
		final Handler hand = new Handler();
		final ProgressDialog dialog = UserCenterDialog.onCreateDialog(this);
		dialog.show();
		batchcodes.clear();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String bathcode = getRecoveryBatchCode.getInstance().getCode(
						betAndGift.getLotno(), String.valueOf(iProgressQishu));

				try {
					JSONObject json = new JSONObject(bathcode);
					JSONArray array = json.getJSONArray("result");
					String errorcode=json.getString("error_code");
				 final	String message=json.getString("message");
					dialog.dismiss();
					if(errorcode.equals("0000")){
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String betcode = obj.getString("batchCode");
						batchcodes.add(betcode);
					}
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							getviewofzhuiqi();
						}
					});
			}else{
				hand.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Toast.makeText(ZixuanZhuihao.this, message,
								Toast.LENGTH_SHORT).show();
					}
				});
				state=0;
				   
			}

				} catch (JSONException e) {
					// TODO: handle exception
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Toast.makeText(ZixuanZhuihao.this, "期号获取失败",
									Toast.LENGTH_SHORT).show();
						}
					});
					state=0;
				}
			}
		}).start();

	}


	
    private   void getviewofzhuiqi(){
    	checkinfo = new Checktouinfo[iProgressQishu];
		for (int i = 0; i < iProgressQishu; i++) {
			checkinfo[i] = new Checktouinfo();
			final int index = i;
			LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflate.inflate(R.layout.zhuihaoshezhi, null);
			zhuiqishezhi.addView(view);
			TextView text1 = (TextView) view.findViewById(R.id.textView1);
			final TextView text3 = (TextView) view.findViewById(R.id.textView3);
			final EditText edit = (EditText) view.findViewById(R.id.edittext1);
			edit.setText(String.valueOf(iProgressBeishu));
			edit.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					if(edit.getText().toString().equals("")){
					    Toast.makeText(ZixuanZhuihao.this, "倍数不能为空", Toast.LENGTH_SHORT).show();
					    edit.setText("1");
			         	}else if (!PublicMethod.isNumeric(edit.getText().toString())) {
						Toast.makeText(ZixuanZhuihao.this, "请输入数字", Toast.LENGTH_SHORT).show();
					    edit.setText("1");
						}else if (Integer.valueOf(edit.getText().toString())>9999) {
							Toast.makeText(ZixuanZhuihao.this, "超过倍数上限9999", Toast.LENGTH_SHORT).show();
						    edit.setText("1");
						}else {
						text3.setText(addviewmiss.getAllAmt()
								* Integer.valueOf(edit.getText().toString())
								+ "元");
						checkinfo[index].setBeishu(edit.getText().toString());
						checkinfo[index].setAmt(Integer.valueOf(text3.getText().toString().replace("元", ""))*100+"");
						getTouzhuAlert();
					}
				}
			});
			text3.setText(addviewmiss.getAllAmt()
					* Integer.valueOf(edit.getText().toString()) + "元");
			checkinfo[i].setAmt(Integer.valueOf(text3.getText().toString().replace("元", ""))*100+"");
			text1.setText(batchcodes.get(i) + "期");
			checkinfo[i].setBatchcode(batchcodes.get(i));
			checkinfo[i].setBeishu(edit.getText().toString());
			CheckBox check = (CheckBox) view.findViewById(R.id.checkBox1);
			check.setChecked(true);
			if (check.isChecked()) {
				checkinfo[i].setState("1");
			}
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					// TODO Auto-generated method stub
					if (isChecked) {
						checkinfo[index].setState("1");
						getTouzhuAlert();
					} else {
						checkinfo[index].setState("0");
						getTouzhuAlert();
					}

				}
			});
			subscribeInfocheck.add(checkinfo[i]);
		}
	}
       
       class Checktouinfo {
   		String state = "";
   		String batchcode = "";
   		String beishu = "";
   		String amt = "";

   		public String getState() {
   			return state;
   		}

   		public void setState(String state) {
   			this.state = state;
   		}

   		public String getBatchcode() {
   			return batchcode;
   		}

   		public void setBatchcode(String batchcode) {
   			this.batchcode = batchcode;
   		}

   		public String getBeishu() {
   			return beishu;
   		}

   		public void setBeishu(String beishu) {
   			this.beishu = beishu;
   		}

   		public String getAmt() {
   			return amt;
   		}

   		public void setAmt(String amt) {
   			this.amt = amt;
   		}
   	}
       
       
   	private String getSubstringforset() {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < subscribeInfocheck.size(); i++) {
			Checktouinfo check = subscribeInfocheck.get(i);
			if (check.state.equals("1")) {
				str.append(check.getBatchcode()).append(",")
						.append(check.getAmt()).append(",")
						.append(check.getBeishu()).append("!");
			}
		}
		String strset = str.toString()
				.substring(0, str.toString().length() - 1);
		return strset;

	}
	/**
	 * 投注提示框中的信息
	 */
	public void getTouzhuAlert(){
//		zhushu.setText(addview.getAllZhu() + "注     " );
		zhushu.setText(addviewmiss.getAllZhu() + "注     " );
		if(state==0||state==1){
			jine.setText("金额："+iProgressQishu * addviewmiss.getAllAmt() * iProgressBeishu+ "元");
			}else if(state==2){
			jine.setText("金额："+getSubstringforamt()+ "元");
			}
//		return "注数："
//				+ addview.getAllZhu() + "注     " 
//				+ "金额：" + 
//				+ iProgressQishu * addview.getAllAmt() * iProgressBeishu
//				+ "元"; 
	}
	
	
	private int getSubstringforamt() {
		int amt=0;
		for (int i = 0; i < subscribeInfocheck.size(); i++) {
			Checktouinfo check = subscribeInfocheck.get(i);
			if (check.state.equals("1")) {
				amt+=Integer.valueOf(check.getAmt());
			}
		}
		return amt/100;

	}
	/**
	 * 投注方法
	 */
	private void touZhu() {
		toLogin = false;
		initBet();
		// TODO Auto-generated method stub
	    touZhuNet();
//		clearProgress();
	}
	
	
	/**
	 * 清空倍数和期数的进度条
	 */
	public void clearProgress(){
		iProgressBeishu = 1;
		iProgressQishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu.setProgress(iProgressQishu);
		addviewmiss.clearInfo();
		addviewmiss.updateTextNum();
	}
	
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		progressdialog = UserCenterDialog.onCreateDialog(this);
		progressdialog.show();
		// 加入是否改变切入点判断 陈晨 8.11
		Thread t = new Thread(new Runnable() {
			String str = "00";
			@Override
			public void run() {
				str = BetAndGiftInterface.getInstance().betOrGift(betAndGift);
				try {
					JSONObject obj = new JSONObject(str);		
					String msg = obj.getString("message");
					String error = obj.getString("error_code");
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
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setIsSellWays("1");
		betAndGift.setAmount(""+addviewmiss.getAllAmt()*iProgressBeishu*100);
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		betAndGift.setBet_code(addviewmiss.getTouzhuCode(iProgressBeishu,betAndGift.getAmt()*100));
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		if(PublicMethod.toIssue(betAndGift.getLotno())!=null){
			betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));//期号
			}
		if (state == 2) {
			betAndGift.setSubscribeInfo(getSubstringforset());
		} else {
			betAndGift.setSubscribeInfo("");
		}
	}
	/**
	 * 初始化倍数和期数进度条
	 * @param view
	 */
    public void initImageView(){
		mSeekBarBeishu = (SeekBar)findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar)findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);
		
		mTextBeishu = (EditText) findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (EditText)findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText(""+iProgressQishu);
		
		PublicMethod.setEditOnclick(mTextBeishu,mSeekBarBeishu,new Handler());
		PublicMethod.setEditOnclick(mTextQishu,mSeekBarQishu,new Handler());
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_beishu, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,true);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_subtract_qihao, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,false);
    }
   
   /**
	 * fqc edit 添加一个参数 isBeiShu 来判断当前是倍数还是期数 。
	 * 
	 * @param idFind
	 * @param iV
	 * @param isAdd
	 * @param mSeekBar
	 * @param isBeiShu
	 */
   
	protected void setSeekWhenAddOrSub(int idFind, final int isAdd,final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) findViewById(idFind);
		subtractBeishuBtn.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isBeiShu) {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressBeishu);
					} else {
						mSeekBar.setProgress(--iProgressBeishu);
					}
					iProgressBeishu = mSeekBar.getProgress();
				} else {
					if (isAdd == 1) {
						mSeekBar.setProgress(++iProgressQishu);
					} else {
						mSeekBar.setProgress(--iProgressQishu);
					}
					iProgressQishu = mSeekBar.getProgress();
				}
			}
		});
	}
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		clearProgress();
		showfenxdialog();
		}
	 public void showfenxdialog(){
		    LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View view = inflate.inflate(R.layout.touzhu_succe,null);
		    final AlertDialog dialog = new AlertDialog.Builder(this).create();
		    ImageView image = (ImageView) view.findViewById(R.id.touzhu_succe_img);
		    Button ok = (Button) view.findViewById(R.id.touzhu_succe_button_sure);
		    Button share = (Button) view.findViewById(R.id.touzhu_succe_button_share);
	        image.setImageResource(R.drawable.succee);
	        ok.setBackgroundResource(R.drawable.loginselecter);
	        share.setBackgroundResource(R.drawable.loginselecter);
	        ok.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.cancel();
					ZixuanZhuihao.this.finish();
				}
			});
	        share.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.cancel();
					ZixuanZhuihao.this.finish();
					Intent intent=new Intent(Intent.ACTION_SEND);  
					intent.setType("text/plain");  
					intent.putExtra(Intent.EXTRA_SUBJECT, "分享给朋友");  
					intent.putExtra(Intent.EXTRA_TEXT, "我刚刚使用如意彩手机客户端购买了一张彩票:"+"快来参与吧！官网www.ruyicai.com");  
					startActivity(Intent.createChooser(intent,"请选择分享方式")); 
				}
			});		   
	        dialog.show();
	        dialog.getWindow().setContentView(view);
	    }
	
	@Override
	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
//		 TODO Auto-generated method stub
		if (progress < 1)
		seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();
		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			state=0;
			zhuiqishezhi.removeAllViews();
			subscribeInfocheck.clear();
			checkinfo = null;
			zhuiqishezhi.setVisibility(View.VISIBLE);		
//			changeTextSumMoney();
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			state=0;
			zhuiqishezhi.removeAllViews();
			subscribeInfocheck.clear();
			checkinfo = null;
			zhuiqishezhi.setVisibility(View.VISIBLE);		
			break;
		}
        getTouzhuAlert();
	
}
		

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

}
