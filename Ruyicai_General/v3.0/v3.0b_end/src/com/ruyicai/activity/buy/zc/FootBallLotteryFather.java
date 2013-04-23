package com.ruyicai.activity.buy.zc;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.zc.pojo.TeamInfo;
import com.ruyicai.activity.common.UserLogin;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.RWSharedPreferences;
/**
 * 足彩父类
 * @author miao
 */
public abstract class FootBallLotteryFather extends Activity implements OnClickListener,SeekBar.OnSeekBarChangeListener{
	String	batchCode;
    public int iScreenWidth;
    public SeekBar mSeekBarBeishu;
    public int iProgressBeishu = 1;
    protected TextView mTextBeishu;
    String sessionid,phonenum,userno;
    private boolean toLogin = false;
    private BuyImplement buyImplement;//投注要实现的方法
    BetAndGiftPojo betPojo = new BetAndGiftPojo();
	public boolean isGift = false;//是否赠送
	public boolean isJoin = false;//是否合买
	public boolean isTouzhu = false;
	Button layout_football_issue;
	TextView layout_football_time;
	/**
	 * 足彩投注按钮
	 */
	ImageButton sfc_btn_touzhu;
	ListView footBallList;
	String qihaoxinxi[] = new String[4];// 存放期号，截止时间，彩种
	Vector<TeamInfo> teamInfos = new Vector<TeamInfo>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		iScreenWidth = PublicMethod.getDisplayWidth(this);
		setContentView(R.layout.buy_footballlottery_layout);
		layout_football_issue = (Button)findViewById(R.id.layout_football_issue);
		layout_football_time = (TextView)findViewById(R.id.layout_football_time);
		footBallList = (ListView)findViewById(R.id.buy_footballlottery_list);
		createVeiw();
	}
	public	void initBatchCode(final String lotteryType){
		JSONObject footballLotnoInfo = PublicMethod.getCurrentLotnoBatchCode(lotteryType);
		if (footballLotnoInfo != null) {
			// 成功获取到了期号信息
			try {
				batchCode = footballLotnoInfo.getString("batchCode");
				qihaoxinxi[0] = batchCode;
				qihaoxinxi[1] = footballLotnoInfo.getString("endTime");
				qihaoxinxi[2] = lotteryType;

			} catch (JSONException e) {
				qihaoxinxi[0] = "";
				qihaoxinxi[1] = "";
				qihaoxinxi[2] = lotteryType;
			}
		}	
	}
	/**
	 * 初始化足彩期数和截止时间信息
	 */
	abstract void initBatchCodeView();
	/**
	 * 投注方法
	 */
	public void beginTouZhu() {
		RWSharedPreferences pre = new RWSharedPreferences(
				FootBallLotteryFather.this, "addInfo");
		sessionid = pre.getStringValue("sessionid");
		phonenum = pre.getStringValue("phonenum");
		userno = pre.getStringValue("userno");
		if (sessionid.equals("")) {
			toLogin = true;
			Intent intentSession = new Intent(FootBallLotteryFather.this, UserLogin.class);
			startActivityForResult(intentSession, 0);
		} else {
			toLogin = false;
			buyImplement.isTouzhu();
			
		}

	}
	/**
	 *创建购彩页面
	 */
	public void createVeiw() {
		mSeekBarBeishu = (SeekBar) findViewById(R.id.buy_footballlottery_seekbar_muti);
		mSeekBarBeishu.setOnSeekBarChangeListener(this);
		iProgressBeishu = 1;
		mSeekBarBeishu.setProgress(iProgressBeishu);
		mTextBeishu = (TextView) findViewById(R.id.buy_footballlottery_text_beishu);
		mTextBeishu.setText("" + iProgressBeishu);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_subtract_beishu,  -1,mSeekBarBeishu, true);
		setSeekWhenAddOrSub(R.id.buy_footballlottery_img_add_beishu,  1,mSeekBarBeishu, true);

		sfc_btn_touzhu = (ImageButton) findViewById(R.id.buy_footballlottery_img_touzhu);
		sfc_btn_touzhu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				beginTouZhu(); // 1表示当前为单式
			}
		});
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
				} else {}
			}
		});
	}
	/**
	 * <b>设置足彩界面队列列表子选项背景的方法</b>
	 * @param linear  子列表对应的LinearLayout
	 * @param position	子列表的位置
	 */
	public void setFootballListItemBackground(LinearLayout linear,int position){
		if(position%2==0){
			Drawable drawable = getResources().getDrawable(R.drawable.list_ou);
			linear.setBackgroundDrawable(drawable);
		}else{
			Drawable drawable = getResources().getDrawable(R.drawable.list_bg_white);
			linear.setBackgroundDrawable(drawable);
		}
	}
	/**
	 * 创建足彩进球彩BallTable
	 * @param LinearLayout  aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	 BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int aFieldWidth, int aBallNum, int[] aResId,int aIdStart) {

		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);
		// BallTable iBallTable=new BallTable(aLayoutId,aIdStart);
		int iBallNum = aBallNum;
		int iFieldWidth = aFieldWidth;
		/** 滚动条的宽度 */
		int scrollBarWidth = 6;
		/** 每一行的小球数量 */
		int viewNumPerLine = 4;
		int iBallViewWidth = (iFieldWidth - scrollBarWidth)/viewNumPerLine-2;
		/** 行的数量 */
		int lineNum = iBallNum / viewNumPerLine;
		/** 最后一行的view的数目 */
		int lastLineViewNum = iBallNum % viewNumPerLine;
		/** 空白的大小 */
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* viewNumPerLine) / 2;
		int iBallViewNo = 0;

		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				/** 设置显示的数字 */
				String iStrTemp = "" + (iBallViewNo + 1);
				if (iStrTemp.equals("1") ) {
					iStrTemp = "0";
				} else if (iStrTemp.equals("2")) {
					iStrTemp = "1";
				} else if (iStrTemp.equals("3") ) {
					iStrTemp = "2";
				} else {
					iStrTemp = "3+";
				}
				/** 实例化一个BallView对象 */
				OneBallView tempBallView = new OneBallView(aParentView
						.getContext());
				/** 为这个tempView设置一个Id */
				tempBallView.setId(aIdStart + iBallViewNo);
				/** 将这个小球初始化出来 */
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,aResId);

				/** 为初始化的小球设置监听 */
				tempBallView.setOnClickListener(this);
				/*** 将小球tempView添加到Table中 */
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					/** 设置TableRow四个方向的空白像素 */
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				/** iBallViewNo自增，循环设置小球的属性 */
				iBallViewNo++;
			}
			/** 新建的TableRow添加到TableLayout */
			iBallTable.tableLayout.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
		return iBallTable;
	}
	/**
	 * 创建足彩六场半BallTable
	 * @param LinearLayout aParentView 上一级Layout
	 * @param int aLayoutId 当前BallTable的LayoutId
	 * @param int aFieldWidth BallTable区域的宽度（如屏幕宽度）
	 * @param int aBallNum 小球个数
	 * @param int aBallViewWidth 小球视图的宽度（图片宽度）
	 * @param int[] aResId 小球图片Id
	 * @param int aIdStart 小球Id起始数值
	 * @return BallTable
	 */
	BallTable makeBallTable(LinearLayout aParentView, int aLayoutId,
			int aFieldWidth, int[] aResId,int aIdStart) {

		BallTable iBallTable = new BallTable(aParentView, aLayoutId, aIdStart);

		int iBallViewWidth = aFieldWidth/3-2;
		int iFieldWidth = aFieldWidth;
		/** 滚动条的宽度 */
		int scrollBarWidth = 6;
		/** 每一行的小球数量 */
		int viewNumPerLine = 3;
		/** 行的数量 */
		int lineNum = 1;
	
		/** 空白的大小 */
		int margin = (iFieldWidth - scrollBarWidth - (iBallViewWidth + 2)* viewNumPerLine) / 2;
		int iBallViewNo = 0;

		for (int row = 0; row < lineNum; row++) {
			TableRow tableRow = new TableRow(aParentView.getContext());
			for (int col = 0; col < viewNumPerLine; col++) {
				/** 设置显示的数字 */
				String iStrTemp = "" + (iBallViewNo + 1);
				if (iStrTemp.equals("1")) {
					iStrTemp = "3";
				} else if (iStrTemp.equals("2")) {
					iStrTemp = "1";
				} else {
					iStrTemp = "0";
				}
				/** 实例化一个BallView对象 */
				OneBallView tempBallView = new OneBallView(aParentView.getContext());
				/** 为这个tempView设置一个Id */
				tempBallView.setId(aIdStart + iBallViewNo);
				/** 将这个小球初始化出来 */
				tempBallView.initBall(iBallViewWidth, iBallViewWidth, iStrTemp,	aResId);

				/** 为初始化的小球设置监听 */
				tempBallView.setOnClickListener(this);
				/*** 将小球tempView添加到Table中 */
				iBallTable.addBallView(tempBallView);

				TableRow.LayoutParams lp = new TableRow.LayoutParams();
				if (col == 0) {
					/** 设置TableRow四个方向的空白像素 */
					lp.setMargins(margin + 1, 1, 1, 1);
				} else if (col == viewNumPerLine - 1) {
					lp.setMargins(1, 1, margin + scrollBarWidth + 1, 1);
				} else
					lp.setMargins(1, 1, 1, 1);
				tableRow.addView(tempBallView, lp);
				/** iBallViewNo自增，循环设置小球的属性 */
				iBallViewNo++;
			}
			/** 新建的TableRow添加到TableLayout */
			iBallTable.tableLayout.addView(tableRow, new TableLayout.LayoutParams(PublicConst.FP, PublicConst.WC));
		}
	
		return iBallTable;
	}
	 /**
	 * 单笔投注大于2万元时的对话框
	 */
	protected void DialogExcessive() {
		AlertDialog.Builder builder = new AlertDialog.Builder(FootBallLotteryFather.this);
		builder.setTitle("投注失败");
		builder.setMessage("单笔投注不能大于2万元");
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
	 * 提示信息
	 */
	public void changeTextSumMoney(int iZhuShu) {

		StringBuffer touzhuAlert = new StringBuffer();
		
		if (iZhuShu != 0) {
			touzhuAlert.append("共").append(iZhuShu).append("注，共").append((iZhuShu * 2)).append("元");
			Toast.makeText(FootBallLotteryFather.this, touzhuAlert.toString(), Toast.LENGTH_SHORT).toString();
		} else {
			Toast.makeText(FootBallLotteryFather.this, getResources().getString(R.string.please_choose_number), Toast.LENGTH_SHORT).toString();
		}
	}
	/**
	 * 小球被点击事件
	 * 
	 * @param v
	 */
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	String formatBatchCode(String batchCode){
		return "第"+batchCode+"期";
	}
	String formatEndtime(String endTime){
		return "截止时间："+endTime;
	}
	
	

}
