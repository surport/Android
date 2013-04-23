package com.ruyicai.activity.buy.ssc;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.code.ssc.BigSamllCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.pojo.OneBallView;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class SscBigSmall extends ZixuanAndJiXuan {
	public boolean isjixuan=false;
	public static final int SSC_TYPE=5;//大小双单
	public static SscBigSmall self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		isbigsmall= true;
		childtype = new String []{"直选","机选"};
		setContentView(R.layout.sscbuyview);
		sscCode = new BigSamllCode();
		self = this;
		init();
		highttype = "SSC";
	}
	
	public void theMethodYouWantToCall(){
        // do what ever you want here
		init();
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		super.onCheckedChanged(group, checkedId);
		switch(checkedId){		
		case 0:
			isjixuan = false;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String shititle = "十位区" ;
			String getitle = "个位区";
			AreaNum[] areaNums= new AreaNum[2];
			areaNums[0] = new AreaNum(4,10, 1, BallResId, 0, 0,Color.RED, shititle);
			areaNums[1] = new AreaNum(4,10, 1, BallResId, 0, 0,Color.RED, getitle);
			createView(areaNums, sscCode,BIG_SMALL,true);
			BallTable = areaNums[0].table;
		    getable = areaNums[1].table;
		break;
		case 1:
			isjixuan=true;
			iProgressBeishu = 1;iProgressQishu = 1;
			  SscBalls sscb = new SscBalls();
			 createviewmechine(sscb);
		break;	
	}
}
	 protected void onResume() {
		    // TODO Auto-generated method stub
		    super.onResume();
	 }
	 @Override

	public BallTable makeBallTable(TableLayout tableLayout, int aFieldWidth,int aBallNum, int[] aResId, int aIdStart, int aBallViewText,
			Context context, OnClickListener onclick,boolean isTen) {
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
				switch (col) {
				case 0:
					iStrTemp = "大";
					break;
				case 1:
					iStrTemp = "小";
					break;
				case 2:
					iStrTemp = "单";
					break;
				case 3:
					iStrTemp = "双";
					break;
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
				switch (col) {
				case 0:
					iStrTemp = "大";
					break;
				case 1:
					iStrTemp = "小";
					break;
				case 2:
					iStrTemp = "单";
					break;
				case 3:
					iStrTemp = "双";
					break;
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
				int[] zhuMa = ballTable.getHighlightBallNOsbigsmall();
				if(j!=0){
				    zhumas +=" + ";
				}
				for (int i = 0; i < ballTable.getHighlightBallNOsbigsmall().length; i++) {
					zhumas += PublicMethod.getbigsmalZhumastr(zhuMa[i]);
					if (i != ballTable.getHighlightBallNOsbigsmall().length - 1){
						zhumas += ",";
					}
				}
				num+=zhuMa.length;
			}
             if(num==0){
            	 editZhuma.setText("");
            	 showEditTitle(BIG_SMALL);
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
					  builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor), length-zhuma[i].length(), length, Spanned.SPAN_COMPOSING);  

				  }
			    editZhuma.setText(builder, BufferType.EDITABLE);
			    showEditTitle(NULL);
             }
	}
	  public String getZhuma(){
	 	   String zhuma="";
	 	    zhuma =  sscCode.zhuma(areaNums, iProgressBeishu, 0);
	 	   return zhuma;
	    }
		@Override
		public String getZhuma(Balls ball) {
			   String zhuma="";
			   zhuma = ball.getZhuma(null, SSC_TYPE);   
			   return zhuma;
		}
	public int getZhuShu() {
		int iReturnValue = 0;
		if(isjixuan){
			int beishu = iProgressBeishu;
			iReturnValue =balls.size()*	beishu;
		}else{
		int shi = areaNums[0].table.getHighlightBallNums();
		int ge =  areaNums[1].table.getHighlightBallNums();
		int beishu = iProgressBeishu;
		iReturnValue = shi * ge * beishu;
		}
		return iReturnValue;
	}
	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();

		int[] shi = areaNums[0].table.getHighlightBallNOsbigsmall();
		int[] ge = areaNums[1].table.getHighlightBallNOsbigsmall();

		return  "注码：" + "\n" +  "十位："
				+ getStrZhuMa(shi) + "\n" + "个位：" + getStrZhuMa(ge) ;

	}
	
	private String getTouzhuAlertjixuan() {
		 String zhumaString = "";
		for (int i = 0; i < balls.size(); i++) {
			for(int j=0;j<balls.get(i).getVZhuma().size();j++){
				zhumaString +=getStrZhuMajixuan(balls.get(i).getBalls(j));
				if(j!=balls.get(i).getVZhuma().size()-1){
					zhumaString += ",";
				}
			}
			if (i != balls.size() - 1) {
				zhumaString += "\n";
			}
		}
		int beishu = mSeekBarBeishu.getProgress();
		int iZhuShu = balls.size() * beishu;
		return  "第" +Ssc.batchCode+"期\n" 
				+ "注数："
				+ iZhuShu / mSeekBarBeishu.getProgress()
				+ "注"
				+ "\n"
				+ // 注数不能算上倍数 陈晨 20100713
				"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
				+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
				+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
				+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+"\n"
				+"注码" + "\n" + zhumaString+"\n"
				 + "确认支付吗？";

	}
	public String getStrZhuMa(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			switch (balls[i]) {
			case 1:
				str += "大";// 大
				break;
			case 2:
				str += "小";// 小
				break;
			case 3:
				str += "单";// 单
				break;
			case 4:
				str += "双";// 双
				break;
			}
		}
		return str;
	}
	
	public String getStrZhuMajixuan(int balls[]) {
		String str = "";
		for (int i = 0; i < balls.length; i++) {
			switch (balls[i]+1) {
			case 1:
				str += "大";// 大
				break;
			case 2:
				str += "小";// 小
				break;
			case 3:
				str += "单";// 单
				break;
			case 4:
				str += "双";// 双
				break;
			}
		}
		return str;

	}
	
	public String isTouzhu() {
		String isTouzhu = "";
			int iZhuShu = getZhuShu();
			int shi = areaNums[0].table.getHighlightBallNums();
			int ge = areaNums[1].table.getHighlightBallNums();
			if (shi == 0 | ge == 0) {
				isTouzhu = "您还没有进行选择！";
			} else if (iZhuShu * 2 > 20000) {
//				dialogExcessive();
				isTouzhu = "false";
			} else {
				isTouzhu = "true";
		}
		return isTouzhu;
	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String iTempString;
		int iZhuShu = getZhuShu();
		if (iZhuShu != 0) {
		    iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		} else {
			iTempString = getResources().getString(R.string.please_choose_number);
		}
		return iTempString;
	}
	@Override
	public void touzhuNet() {
		// TODO Auto-generated method stub
		int zhuShu=getZhuShu();
		if(isjixuan){
		betAndGift.setSellway("1")	;
		}else{
		betAndGift.setSellway("0");}//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSC);
		betAndGift.setBet_code(getZhuma());
		betAndGift.setAmount(""+zhuShu*200);
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
			iBallId = iBallId - areaNums[i].areaNum;
			if(iBallId<0){
				  areaNums[i].table.changeBallState(areaNums[i].chosenBallSum, nBallId);
				  break;
			}

	     }

	}
	
}
