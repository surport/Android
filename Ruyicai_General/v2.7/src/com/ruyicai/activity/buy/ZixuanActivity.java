/**
 * 
 */
package com.ruyicai.activity.buy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.activity.gift.GiftActivity;
import com.ruyicai.activity.join.JoinStartActivity;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.ShellRWSharesPreferences;

/**
 * 自选购彩父类
 * 
 * @author Administrator
 * 
 */
public class ZixuanActivity extends Activity implements OnClickListener,SeekBar.OnSeekBarChangeListener,HandlerMsg{

	private TextView mTextSumMoney;
	private ImageButton zixuanTouzhu;
	private EditText editZhuma;
	protected RadioGroup group;//时时彩单选按钮
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	protected TextView mTextBeishu;
	private TextView mTextQishu;
	public int iProgressBeishu = 1, iProgressQishu = 1;
    public AreaNum areaNums[];
    public int iScreenWidth;
    private CodeInterface code;//注码接口类
    private BuyImplement buyImplement;//投注要实现的方法
    protected View view ;
	public Toast toast;
	private boolean toLogin = false;
	ProgressDialog progressdialog;
	private static final int DIALOG1_KEY = 0;// 进度条的值2010/7/4
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	MyHandler handler = new MyHandler(this);//自定义handler
	String phonenum,sessionId,userno;
	public boolean isGift = false;//是否赠送
	public boolean isJoin = false;//是否合买
	public boolean isTouzhu = false;//是否投注
	RadioButton  check;
	RadioButton  joinCheck;
	RadioButton  touzhuCheck;
	String codeStr;
	String lotno;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ----- 设置全屏
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflate.inflate(R.layout.buy_zixuan_activity, null);
		setContentView(view);
	}

	/**
	 * 创建购彩界面
	 * @param areaInfos 选球区
	 * @param code      对应的注码类
	 * @param buyImplement  购彩要实现的方法
	 */
	public void createView(AreaInfo areaInfos[],CodeInterface code,BuyImplement buyImplement) {
		if(areaNums!=null){
			for(int i=0;i<areaNums.length;i++){
				areaNums[i].tableLayout.removeAllViews();
				areaNums[i].textTitle.setText("");
			}
		}

	    areaNums = new AreaNum[areaInfos.length];
	    this.code = code;
	    this.buyImplement = buyImplement;
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		mTextSumMoney = (TextView) findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) findViewById(R.id.buy_zixuan_edit_zhuma);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
		int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five,R.id.buy_zixuan_table_six,R.id.buy_zixuan_table_seven};
		int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five,R.id.buy_zixuan_text_six,R.id.buy_zixuan_text_seven};
		//初始化选区
		for(int i=0;i<areaNums.length;i++){
        	areaNums[i]=new AreaNum(tableLayoutIds[i],textViewIds[i],view);
			AreaInfo info = areaNums[i].info = areaInfos[i];
			if(i!=0){
				info.aIdStart=areaNums[i-1].info.areaNum+areaNums[i-1].info.aIdStart;
			}
			areaNums[i].table =  makeBallTable(areaNums[i].tableLayout, iScreenWidth, info.areaNum,info.ballResId, info.aIdStart, info.aBallViewText, this, this);
			areaNums[i].init();
		}		
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView) findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
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
		zixuanTouzhu = (ImageButton) findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton again = (ImageButton) findViewById(R.id.buy_zixuan_img_again);
		again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			  again();
			}
		});
		
	}
 
	/**
	 * 投注方法
	 */
	public void beginTouZhu() {
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(
		ZixuanActivity.this, "addInfo");
		sessionId = pre.getUserLoginInfo("sessionid");
		phonenum = pre.getUserLoginInfo("phonenum");
		userno = pre.getUserLoginInfo("userno");
		if (sessionId.equals("")) {
			toLogin = true;
			Intent intentSession = new Intent(ZixuanActivity.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			toLogin = false;
			buyImplement.isTouzhu();
		}
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

	/**
	 * seekBar组件的监听器
	 */
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (progress < 1)
			seekBar.setProgress(1);
		int iProgress = seekBar.getProgress();

		switch (seekBar.getId()) {
		case R.id.buy_zixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			changeTextSumMoney();
			break;
		case R.id.buy_zixuan_seek_qishu:
			iProgressQishu = iProgress;
			mTextQishu.setText("" + iProgressQishu);
			break;
		default:
			break;
		}

	}

	/**
	 * 计算注数和金额的方法
	 * 
	 */
	public void changeTextSumMoney() {     
		String text = buyImplement.textSumMoney(areaNums,iProgressBeishu);
		if(toast == null){
			toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
			toast.show();
		}else{
			toast.setText(text);
			toast.show();
		}

	}
	/**
	 *  注码 
	 *  
	 */
	public String getZhuMa(){
		String zhuma = code.zhuma(areaNums,iProgressBeishu,0);
		return zhuma;
	}

	/**
	 * 创建BallTable
	 * @param LinearLayout      aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	public BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick) {
		TableLayout tabble = tableLayout;
		BallTable iBallTable = new BallTable(aIdStart,context);
		int iBallNum = aBallNum;

		int viewNumPerLine = 8; // 定义没行小球的个数为7
		int iFieldWidth = aFieldWidth;
		int scrollBarWidth = 6;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth) / viewNumPerLine
				- 2;
		int lineNum = iBallNum / viewNumPerLine;
		int lastLineViewNum = iBallNum % viewNumPerLine;
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)
				* viewNumPerLine) / 2;
		int iBallViewNo = 0;
		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(context);

			for (int col = 0; col < viewNumPerLine; col++) {
				String iStrTemp = "";
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);// 小球从0开始
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		if (lastLineViewNum > 0) {
			TableRow tableRow = new TableRow(context);
			for (int col = 0; col < lastLineViewNum; col++) {
				String iStrTemp = "";
				// PublicMethod.myOutput("-----------"+iBallViewNo);
				if (aBallViewText == 0) {
					iStrTemp = "" + (iBallViewNo);
				} else if (aBallViewText == 1) {
					iStrTemp = "" + (iBallViewNo + 1);// 小球从1开始
				} else if (aBallViewText == 3) {
					iStrTemp = "" + (iBallViewNo + 3);// 小球从3开始
				}
				OneBallView tempBallView = new OneBallView(context);
				tempBallView.setId(aIdStart + iBallViewNo);
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,
						aResId);
				tempBallView.setOnClickListener(onclick);
				iBallTable.addBallView(tempBallView);
				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					lp.setMargins(margin, 1, 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				iBallViewNo++;
			}
			// 新建的TableRow添加到TableLayout
			tabble.addView(tableRow, new TableLayout.LayoutParams(
					PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}

	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int iBallId = v.getId();
		isBallTable(iBallId);
		showEditText();
		changeTextSumMoney();

	}
	/**
	 * 根据小球id判断是哪个选区
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].info.areaNum;
			if(iBallId<0){
				  areaNums[i].table.changeBallState(areaNums[i].info.chosenBallSum, nBallId);
				  break;
			}

	     }

	}
	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText(){
         SpannableStringBuilder builder = new SpannableStringBuilder(); 
		 String zhumas = "";
		 int num = 0;//高亮小球数
		 int length = 0;
			for(int j=0;j<areaNums.length;j++){
				BallTable ballTable = areaNums[j].table;
				int[] zhuMa = ballTable.getHighlightBallNOs();
				if(j!=0){
				    zhumas +=" + ";
				}
				for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
					if (i != ballTable.getHighlightBallNOs().length - 1){
						zhumas += ",";
					}
				}
				num+=zhuMa.length;
			}
             if(num==0){
            	 editZhuma.setText("");
             }else{
				builder.append(zhumas);
				String zhuma[]=zhumas.split("\\+");
				  for(int i=0;i<zhuma.length;i++){
					  if(i!=0){
							length += zhuma[i].length()+1;
						  }else{
							length += zhuma[i].length();
						  }
				     if(i!=zhuma.length-1){	
						  builder.setSpan(new ForegroundColorSpan(Color.BLACK), length, length+1, Spanned.SPAN_COMPOSING);  
					  }
					  builder.setSpan(new ForegroundColorSpan(areaNums[i].info.textColor), length-zhuma[i].length(), length, Spanned.SPAN_COMPOSING);  

				  }
			    editZhuma.setText(builder, BufferType.EDITABLE);
             }
	}
	/**
	 * 单复式投注调用函数
	 * @param string  显示框信息
	 * @return
	 */
	public void alert(String string,final String zhuma) {
		codeStr = zhuma;
		isGift = false;
		isJoin = false;
		isTouzhu = true;
		LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View v= inflater.inflate(R.layout.alert_dialog_touzhu, null);
		final AlertDialog dialog = new AlertDialog.Builder(this).setTitle("您选择的是").setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which) {
							
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,int which) {

							}
						}).create();
		dialog.show();
		TextView text =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_one);
		text.setText(string);
		TextView textZhuma =(TextView) v.findViewById(R.id.alert_dialog_touzhu_text_two);
		textZhuma.setText(zhuma);
		Button cancel = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_cancel);
		Button ok = (Button) v.findViewById(R.id.alert_dialog_touzhu_button_ok);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				initBet();
				// TODO Auto-generated method stub
				if(isGift){
					toActivity(zhuma);
				}else if(isJoin){
					toJoinActivity();
				}else if(isTouzhu){
					touZhuNet();
				}
			}
		});
		check = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu_check);
		joinCheck = (RadioButton) v.findViewById(R.id.alert_dialog_join_check);
		touzhuCheck = (RadioButton) v.findViewById(R.id.alert_dialog_touzhu1_check);
		touzhuCheck.setChecked(true);
		TextView textAlert = (TextView) v.findViewById(R.id.alert_dialog_touzhu_text_alert);
		if(iProgressQishu>1){
			check.setVisibility(CheckBox.GONE);
			joinCheck.setVisibility(CheckBox.GONE);
			touzhuCheck.setVisibility(CheckBox.GONE);
			textAlert.setVisibility(TextView.VISIBLE);
		}else{
			check.setPadding(50, 0, 0, 0);
		    check.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
                        isGift = isChecked;
				}
			});
			joinCheck.setPadding(50, 0, 0, 0);
		    joinCheck.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    joinCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
                        isJoin = isChecked;
				}
			});
			touzhuCheck.setPadding(50, 0, 0, 0);
		    touzhuCheck.setButtonDrawable(R.drawable.check_select);
			// 实现记住密码 和 复选框的状态
		    touzhuCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					    isTouzhu = isChecked;
				}
			});
		}

		dialog.getWindow().setContentView(v);
	}
	public void toActivity(String zhuma){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
		   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		            objStream.writeObject(betAndGift);
		  } catch (IOException e) {
		   return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(ZixuanActivity.this, GiftActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  intent.putExtra("zhuma", zhuma);
		  startActivity(intent); 


	}
	public void toJoinActivity(){
		  ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		   try {
		   ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
		            objStream.writeObject(betAndGift);
		  } catch (IOException e) {
		   return;  // should not happen, so donot do error handling
		  }
		  
		  Intent intent = new Intent(ZixuanActivity.this,JoinStartActivity.class);
		  intent.putExtra("info", byteStream.toByteArray());
		  startActivity(intent); 


	}
	
	/**
	 * 投注联网
	 */
	public void touZhuNet(){
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		showDialog(DIALOG1_KEY); // 显示网络提示框 2010/7/4
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
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
		betAndGift.setBet_code(getZhuMa());
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		
		// amount      金额 单位为分（总金额）
        //lotno       彩种编号  投注彩种，如：双色球为F47104
		buyImplement.touzhuNet();
		
	}
	/**
	 * 提示框1 用来提醒选球规则
	 * 
	 * @param string
	 *            显示框信息
	 * @return
	 */
	public void alertInfo(String string) {   
		Builder dialog = new AlertDialog.Builder(this).setTitle("请选择号码")
				.setMessage(string).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}

						});
		dialog.show();

	}
	/**
	 * 单笔投注大于10万元时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ZixuanActivity.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("单笔投注不能大于100000元");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}
	// wangyl 7.23 福彩3D投注注数大于600注时的对话框
	/**
	 * 福彩3D直选投注注数大于600注时的对话框
	 */
	public void dialogZhixuan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ZixuanActivity.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title).toString());
		builder.setMessage("请选择不大于600注投注");
		// 确定
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}

				});
		builder.show();
	}
	/**
	 * 网络连接提示框
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG1_KEY: {
			progressdialog = new ProgressDialog(this);
			// progressdialog.setTitle("Indeterminate");
			progressdialog.setMessage("网络连接中...");
			progressdialog.setIndeterminate(true);
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 重新方法
	 * 
	 */
	public void again(){
		if(areaNums!=null){
		for(int i=0;i<areaNums.length;i++){
            areaNums[i].table.clearAllHighlights();					
		}
		showEditText();
		mSeekBarBeishu.setProgress(1);
		mSeekBarQishu.setProgress(1);
		}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			beginTouZhu();
			break;
		}
	}
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!toLogin){
			again();
		}
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void errorCode_0000() {
		// TODO Auto-generated method stub
		for(int i=0;i<areaNums.length;i++){
             areaNums[i].table.clearAllHighlights();
	     }
		showEditText();
	    PublicMethod.showDialog(ZixuanActivity.this,lotno+codeStr);
	    
	       
	}


	public void errorCode_000000() {
		// TODO Auto-generated method stub
		
	}

	public Context getContext() {
		// TODO Auto-generated method stub
		return this;
	}

}
