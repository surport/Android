package com.ruyicai.activity.buy;
import java.util.Vector;

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
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.dlc.Dlc;
import com.ruyicai.activity.buy.ssc.Ssc;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.code.CodeInterface;
import com.ruyicai.code.ssc.OneStarCode;
import com.ruyicai.handler.HandlerMsg;
import com.ruyicai.handler.MyHandler;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.net.newtransaction.BetAndGiftInterface;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.SensorActivity;
import com.ruyicai.util.ShellRWSharesPreferences;

public class ZixuanAndJiXuan extends Activity implements OnCheckedChangeListener,OnClickListener,SeekBar.OnSeekBarChangeListener,HandlerMsg{
	protected int BallResId[] = { R.drawable.grey, R.drawable.red };
	protected LayoutInflater inflater;
	protected AreaInfo areaInfos[] = null;
	protected LinearLayout buyview;
	protected BallTable   BallTable ;
	protected RadioGroup group;
	protected CodeInterface sscCode = new OneStarCode();
	protected String []  childtype=null;
	protected boolean isbigsmall=false;
	public int iProgressBeishu = 1, iProgressQishu = 1;
	public BetAndGiftPojo betAndGift=new BetAndGiftPojo();//投注信息类
	MyHandler handler = new MyHandler(this);//自定义handler
	String phonenum,sessionId,userno;
	ProgressDialog progressdialog;
	public String codeStr;
	String lotno;
	public String highttype;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sscbuyview);
		childtype= new String[]{"直选","机选"};
		init();
		
	}
	public void init(){
	LinearLayout childtypes= (LinearLayout)findViewById(R.id.sscchildtype);
	childtypes.setVisibility(View.VISIBLE);
	childtypes.removeAllViews();
	buyview=(LinearLayout)findViewById(R.id.buyview);
	inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	group = new RadioGroup(this); 
	group.setOrientation(RadioGroup.HORIZONTAL);
	// 设置屏幕宽度
     for(int i = 0 ; i < childtype.length ; i++){
         RadioButton radio = new RadioButton(this);
         radio.setText(childtype[i]);
         radio.setTextColor(Color.BLACK);
         radio.setId(i);
         radio.setButtonDrawable(R.drawable.radio_select);
		 radio.setPadding(Constants.PADDING, 0, 10, 0);
         group.addView(radio);
         group.setOnCheckedChangeListener(this);
 	     group.check(0);
        }
          //将单选按钮组添加到布局中
    childtypes.addView(group);
	}
	/**
	 * 初始化选区
	 */
	public void initArea() {
		areaInfos = new AreaInfo[1];
        String title = "请选择投注号码" ;
		areaInfos[0] = new AreaInfo(10, 5, BallResId, 0, 0,Color.RED, title);
		
	}
	
	//单选框切换直选，机选
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){		
		case 0:
			iProgressBeishu = 1;iProgressQishu = 1;
			initArea();
			createView(areaInfos, sscCode,buyImplement);
			BallTable=areaNums[0].table;
		break;
		case 1:
			iProgressBeishu = 1;iProgressQishu = 1;
            SscBalls onestar = new SscBalls(1);
			createviewmechine(onestar,buyImplement);
		break;	
		}
			
	}
	
	private TextView mTextSumMoney;
	private ImageButton zixuanTouzhu;
	protected EditText editZhuma;
	public SeekBar mSeekBarBeishu, mSeekBarQishu;
	private TextView mTextBeishu, mTextQishu;
    public AreaNum areaNums[];
    public int iScreenWidth;
    private CodeInterface code;//注码接口类
    private BuyImplement buyImplement;//投注要实现的方法
    protected View view ;
	public Toast toast;
	private boolean toLogin = false;
	
	//创建直选页面
	public void createView(AreaInfo areaInfos[],CodeInterface code,BuyImplement buyImplement) {
	   buyview.removeAllViews();
	   View zhixuanview = inflater.inflate(R.layout.ssczhixuan, null);
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
		mTextSumMoney = (TextView)zhixuanview. findViewById(R.id.buy_zixuan_text_sum_money);
		editZhuma = (EditText) zhixuanview.findViewById(R.id.buy_zixuan_edit_zhuma);
		mTextSumMoney.setText(getResources().getString(R.string.please_choose_number));
		int tableLayoutIds[]={R.id.buy_zixuan_table_one,R.id.buy_zixuan_table_two,R.id.buy_zixuan_table_third,R.id.buy_zixuan_table_four,R.id.buy_zixuan_table_five};
		int textViewIds[]={R.id.buy_zixuan_text_one,R.id.buy_zixuan_text_two,R.id.buy_zixuan_text_third,R.id.buy_zixuan_text_four,R.id.buy_zixuan_text_five};
		//初始化选区
		for(int i=0;i<areaNums.length;i++){
        	areaNums[i]=new AreaNum(tableLayoutIds[i],textViewIds[i],zhixuanview);
			AreaInfo info = areaNums[i].info = areaInfos[i];
			if(i!=0){
				info.aIdStart=areaNums[i-1].info.areaNum+areaNums[i-1].info.aIdStart;
			}
			areaNums[i].table =  makeBallTable(areaNums[i].tableLayout, iScreenWidth, info.areaNum,info.ballResId, info.aIdStart, info.aBallViewText, this, this);
			areaNums[i].init();
		}
		mSeekBarBeishu = (SeekBar)zhixuanview. findViewById(R.id.buy_zixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar) zhixuanview.findViewById(R.id.buy_zixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView)zhixuanview. findViewById(R.id.buy_zixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView)zhixuanview. findViewById(R.id.buy_zixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(zhixuanview,R.id.buy_zixuan_img_subtract_beishu, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(zhixuanview,R.id.buy_zixuan_img_add_beishu, 1, mSeekBarBeishu,true);
		setSeekWhenAddOrSub(zhixuanview,R.id.buy_zixuan_img_subtract_qihao, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(zhixuanview,R.id.buy_zixuan_img_add_qishu, 1, mSeekBarQishu,false);
		zixuanTouzhu = (ImageButton) zhixuanview.findViewById(R.id.buy_zixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton again = (ImageButton) zhixuanview.findViewById(R.id.buy_zixuan_img_again);
		again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			  again();
			}
		});
	   buyview.addView(zhixuanview);
	}
	//创建机选页面

	private Spinner jixuanZhu;
	private LinearLayout zhumaView;
	private SsqSensor sensor = new SsqSensor(this);
	private boolean isOnclik = true;
	public Vector<Balls> balls = new Vector();
	protected Balls ballOne;
	public Toast toastjixuan;
	public void createviewmechine(Balls balles,BuyImplement buyImplement){
	   buyview.removeAllViews();
	   View jixuanview = inflater.inflate(R.layout.sscmechine, null);
	   buyview.addView(jixuanview);
	   sensor.startAction();
	   this.buyImplement = buyImplement;
	   this.ballOne = balles;
	   zhumaView = (LinearLayout)jixuanview. findViewById(R.id.buy_danshi_jixuan_linear_zhuma);
	   zhumaView.removeAllViews();
	   toastjixuan = Toast.makeText(this, "左右摇晃手机，重新选号！", Toast.LENGTH_SHORT);
	   toastjixuan.show();
	   balls = new Vector<Balls>();
			// 初始化spinner
			jixuanZhu = (Spinner) jixuanview.findViewById(R.id.buy_danshi_jixuan_spinner);
			if(isbigsmall){
			TextView textview = (TextView)jixuanview.findViewById(R.id.TextView01);
			textview.setText("注数:一注");
			jixuanZhu.setVisibility(View.GONE);	
			jixuanZhu.setSelection(0);
			}else{
			jixuanZhu.setSelection(4);
			jixuanZhu.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int position = jixuanZhu.getSelectedItemPosition();
					if (isOnclik) {
						zhumaView.removeAllViews();
						balls = new Vector();
						for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
							Balls ball = ballOne.createBalls();
							balls.add(ball);
						}
						createTable(zhumaView);
					} else {
						isOnclik = true;
					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
		}
   
		int index = jixuanZhu.getSelectedItemPosition() + 1;
		for (int i = 0; i < index; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
		sensor.onVibrator();// 震动
		mSeekBarBeishu = (SeekBar)jixuanview. findViewById(R.id.buy_danshi_jixuan_seek_beishu);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mSeekBarQishu = (SeekBar)jixuanview. findViewById(R.id.buy_danshi_jixuan_seek_qishu);
		mSeekBarQishu.setOnSeekBarChangeListener(this);
		mSeekBarQishu.setProgress(iProgressQishu);

		mTextBeishu = (TextView) jixuanview.findViewById(R.id.buy_danshi_jixuan_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		mTextQishu = (TextView)jixuanview.findViewById(R.id.buy_danshi_jixuan_text_qishu);
		mTextQishu.setText("" + iProgressQishu);
		/*
		 * 点击加减图标，对seekbar进行设置：
		 * 
		 * @param int idFind, 图标的id View iV, 当前的view final int isAdd, 1表示加 -1表示减
		 * final SeekBar mSeekBar
		 * 
		 * @return void
		 */
		setSeekWhenAddOrSub(jixuanview,R.id.buy_danshi_jixuan_img_subtract_beishu, -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(jixuanview,R.id.buy_danshi_jixuan_img_add_beishu, 1, mSeekBarBeishu,true);
		setSeekWhenAddOrSub(jixuanview,R.id.buy_danshi_jixuan_img_subtract_qihao, -1,mSeekBarQishu, false);
		setSeekWhenAddOrSub(jixuanview,R.id.buy_danshi_jixuan_img_add_qishu, 1, mSeekBarQishu,false);
		zixuanTouzhu = (ImageButton)jixuanview. findViewById(R.id.buy_danshi_jixuan_img_touzhu);
		zixuanTouzhu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				beginTouZhu();
			}
		});
		ImageButton again = (ImageButton) jixuanview.findViewById(R.id.buy_danshi_jixuan_img_again);
		again.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zhumaView.removeAllViews();
				balls = new Vector();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = ballOne.createBalls();
					balls.add(ball);
				}
				createTable(zhumaView);
			}
		});
	}
	//机选创建小球
	public void createTable(LinearLayout layout) {
		for (int i = 0; i < balls.size(); i++) {
			final int index = i;
			int iScreenWidth = PublicMethod.getDisplayWidth(this);
			LinearLayout lines = new LinearLayout(layout.getContext());
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
			    String color = (String) balls.get(i).getVColor().get(j);
			    TableLayout table;
			    if(isbigsmall){
				table = PublicMethod.makeBallTableJiXuanbigsmall(null,iScreenWidth,BallResId, balls.get(i).getBalls(j), this); 	
			    }else
			    {
				table = PublicMethod.makeBallTableJiXuan(null,iScreenWidth,BallResId, balls.get(i).getBalls(j),0, this);
			    }
				lines.addView(table);
			}	
			ImageButton delet = new ImageButton(lines.getContext());
			delet.setBackgroundResource(R.drawable.shanchu);
			delet.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (balls.size() > 1) {
						zhumaView.removeAllViews();
						balls.remove(index);
						isOnclik = false;
						jixuanZhu.setSelection(balls.size() - 1);
						createTable(zhumaView);
					} else {
						Toast.makeText(ZixuanAndJiXuan.this, getResources().getText(R.string.zhixuan_jixuan_toast),Toast.LENGTH_SHORT).show();

					}
   
				}
			});
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			param.setMargins(10, 5, 0, 0);
			lines.addView(delet, param);
			lines.setGravity(Gravity.CENTER_HORIZONTAL);
            if(i%2==0){
				lines.setBackgroundResource(R.drawable.jixuan_list_bg);
			}
            lines.setPadding(0, 3, 0, 0);
			layout.addView(lines);
			
		}

	}
	/**
	 * 投注方法
	 */
	private void beginTouZhu() {
		ShellRWSharesPreferences pre = new ShellRWSharesPreferences(ZixuanAndJiXuan.this, "addInfo");
		sessionId = pre.getUserLoginInfo("sessionid");
		phonenum = pre.getUserLoginInfo("phonenum");
		userno = pre.getUserLoginInfo("userno");
		if (sessionId.equals("")) {
			toLogin = true;
			Intent intentSession = new Intent(ZixuanAndJiXuan.this, UserLogin.class);
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
	private void setSeekWhenAddOrSub(View iv,int idFind, final int isAdd,
			final SeekBar mSeekBar, final boolean isBeiShu) {
		ImageButton subtractBeishuBtn = (ImageButton) iv.findViewById(idFind);
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
		case R.id.buy_danshi_jixuan_seek_beishu:
			iProgressBeishu = iProgress;
			mTextBeishu.setText("" + iProgressBeishu);
			break;
		case R.id.buy_danshi_jixuan_seek_qishu:
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
	 * 创建BallTable
	 * 
	 * @param LinearLayout
	 *            aParentView 上一级Layout
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
		showEditText(highttype);
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
	public void showEditText(String type){
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
					if(type.equals("SSC")){
					zhumas +=zhuMa[i]+"";
					}else if(type.equals("DLC")){
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
					}
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
	 * 直选机选投注提示框中的信息
	 */
	protected String getTouzhuAlertJixuan() {
		String zhumaString = "";
		for (int i = 0; i < balls.size(); i++) {
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
				zhumaString += balls.get(i).getSpecialShowZhuma(j);
				if(j!=balls.get(i).getVZhuma().size()-1){
					zhumaString += ",";
				}
			}
			if (i != balls.size() - 1) {
				zhumaString += "\n";
			}
		}
		codeStr = "注码：\n"+zhumaString+"\n";
		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = balls.size() * beishu;
		StringBuffer content =new StringBuffer();
		if(highttype.equals("SSC")){
		    content.append("第").append(Ssc.batchCode).append("期").append("\n").append("注数：").append(balls.size()).append("注").append("\n").append("倍数：").append(beishu)
		     .append("倍" ).append("\n").append("追号：").append(mSeekBarQishu.getProgress()).append("期").append("\n").append("金额：")
		     .append((balls.size() * 2 * beishu)).append("元").append("\n").append("冻结金额：").append((2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu))
		    .append("元").append("\n").append("注码：").append("\n").append(zhumaString).append("\n").append("确认支付吗？");
		 }else if(highttype.equals("DLC")){
			 content.append("第").append(Dlc.batchCode).append("期").append("\n").append("注数：").append(balls.size()).append("注").append("\n").append("倍数：").append(beishu)
		     .append("倍" ).append("\n").append("追号：").append(mSeekBarQishu.getProgress()).append("期").append("\n").append("金额：")
		     .append((balls.size() * 2 * beishu)).append("元").append("\n").append("冻结金额：").append((2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu))
		    .append("元").append("\n").append("注码：").append("\n").append(zhumaString).append("\n").append("确认支付吗？"); 
		 }
		
		 return content.toString();	
	}
	
	protected void alertJX(String string) {
		sensor.stopAction();
		Dialog dialog = new AlertDialog.Builder(this).setMessage(string).setTitle("您选择的是")
				.setNegativeButton(R.string.login_dialog_ok,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								touZhuNet();
							}
					}).setPositiveButton(R.string.login_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).create();
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				sensor.startAction();
			}
		});

	}
	protected void alertZX(String string) {
		Dialog dialog = new AlertDialog.Builder(this).setMessage(string).setTitle("您选择的是")
				.setNegativeButton(R.string.login_dialog_ok,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int which) {
								touZhuNet();
							}
					}).setPositiveButton(R.string.login_dialog_cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						}).create();
		dialog.show();
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});

	}
	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			str += (balls[i]);
			if (i != (balls.length - 1))
				str += ",";
		}
		return str;

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
							public void onClick(DialogInterface dialog,int which) {
							}
						});
		dialog.show();

	}
	/**
	 * 单笔投注大于10万元时的对话框
	 */
	public void dialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ZixuanAndJiXuan.this);
		builder.setTitle(getResources().getString(R.string.toast_touzhu_title).toString());
		builder.setMessage(R.string.sscmax10w);
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
	 * 重新方法
	 * 
	 */
	public void again(){
		if(areaNums!=null){
		for(int i=0;i<areaNums.length;i++){
            areaNums[i].table.clearAllHighlights();					
		}
		showEditText(highttype);
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!toLogin){
			sensor.stopAction();
			mSeekBarBeishu.setProgress(1);
			mSeekBarQishu.setProgress(1);
		}
	}
	
	/**
	 * 重新初始化机选界面
	 */
	public void againView(){
	    sensor.startAction();
	    sensor.onVibrator();// 震动
	    toastjixuan.show();
	    jixuanZhu.setSelection(4);
		zhumaView.removeAllViews();
		balls = new Vector();
		for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
			Balls ball = ballOne.createBalls();
			balls.add(ball);
		}
		createTable(zhumaView);
	}
	/**
	 * 实现震动的类
	 * @author Administrator
	 *
	 */
	class SsqSensor extends SensorActivity {

		public SsqSensor(Context context) {
			getContext(context);
		}

		@Override
		public void action() {
				zhumaView.removeAllViews();
				balls = new Vector<Balls>();
				for (int i = 0; i < jixuanZhu.getSelectedItemPosition() + 1; i++) {
					Balls ball = ballOne.createBalls();
					balls.add(ball);
				}
				createTable(zhumaView);
		}
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
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return null;
	}
	/**
	 * 初始化投注信息
	 */
	public void initBet(){
		betAndGift.setSessionid(sessionId);
		betAndGift.setPhonenum(phonenum);
		betAndGift.setUserno(userno);
	//	betAndGift.setBet_code(getZhuma());
		betAndGift.setBettype("bet");// 投注为bet,赠彩为gift 
		betAndGift.setLotmulti(""+iProgressBeishu);//lotmulti    倍数   投注的倍数
		betAndGift.setBatchnum(""+iProgressQishu);//batchnum    追号期数 默认为1（不追号）
		// amount      金额 单位为分（总金额）
        //lotno       彩种编号  投注彩种，如：双色球为F47104
		buyImplement.touzhuNet();
		
	}
	
	public void touZhuNet(){
		initBet();
		lotno = PublicMethod.toLotno(betAndGift.getLotno());
		betAndGift.setBatchcode(PublicMethod.toIssue(betAndGift.getLotno()));
		showDialog(0); // 显示网络提示框 2010/7/4
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
	@Override
	public void errorCode_0000() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		for(int i=0;i<areaNums.length;i++){
             areaNums[i].table.clearAllHighlights();
	     }
		editZhuma.setText("");
		PublicMethod.showDialog(ZixuanAndJiXuan.this,lotno+codeStr);
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
}
