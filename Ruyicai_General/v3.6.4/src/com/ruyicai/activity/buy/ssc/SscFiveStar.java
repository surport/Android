package com.ruyicai.activity.buy.ssc;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView.BufferType;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.zixuan.AddView.CodeInfo;
import com.ruyicai.code.ssc.FiveStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class SscFiveStar extends ZixuanAndJiXuan  {
	public boolean isjixuan=false;
	public int FIVESTARTYPE=0;
    public static int SSC_TYPE=4;//五星
    public static SscFiveStar self;
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String []{"五星直选","五星通选"};
		setContentView(R.layout.sscbuyview);
		sscCode = new FiveStarCode();
		self =this;
		highttype = "SSC";
		theMethodYouWantToCall();
		
	}
	public void theMethodYouWantToCall(){
        // do what ever you want here
		init();
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		onCheckAction(checkedId);
		isMissNet(new SscMissJson(),sellWay,false);//获取遗漏值
   }   
	public void onCheckAction(int checkedId){
		switch(checkedId){		
		case 0:
			radioId = 0;
			isjixuan=false;
			FIVESTARTYPE=Constants.SSC_FIVESTAR_ZHIXUAN;
			BallTable qiantable;
			BallTable baitable;
			BallTable shitable;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String wantitle = "万位区：";
			String qiantitle = "千位区：";
			String baititle = "百位区：";
			String shititle = "十位区：" ;
			String getitle = "个位区：";
			AreaNum[] areaNums= new AreaNum[5];
			areaNums[0] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, wantitle,false,true);
			areaNums[1] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, qiantitle,false,true);
			areaNums[2] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, baititle,false,true);
			areaNums[3] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, shititle,false,true);
			areaNums[4] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, getitle,false,true);
			createView(areaNums, sscCode,FIVE,true,checkedId,true);
			BallTable = areaNums[0].table;
			qiantable = areaNums[1].table;
			baitable = areaNums[2].table;
			shitable = areaNums[3].table;
		    getable = areaNums[4].table;
		break;
//		case 1:
//			radioId = 1;
//			isjixuan=true;
//			iProgressBeishu = 1;iProgressQishu = 1;
//			 SscBalls sscb = new SscBalls(5);
//			 createviewmechine(sscb,checkedId);
//		break;	
		case 1:
			radioId = 1;
			isjixuan=false;
			FIVESTARTYPE=Constants.SSC_FIVESTAR_TONGXUAN;
			BallTable qiantablet;
			BallTable baitablet;
			BallTable shitablet;
			BallTable getablet;
			iProgressBeishu = 1;iProgressQishu = 1;
			String wantitlet = "万位区";
			String qiantitlet = "千位区";
			String baititlet = "百位区";
			String shititlet = "十位区" ;
			String getitlet = "个位区";
			areaNums= new AreaNum[5];
			areaNums[0] = new AreaNum(10,10, 1, 9, BallResId, 0, 0,Color.RED, wantitlet,false,true);
			areaNums[1] = new AreaNum(10,10, 1, 9, BallResId, 0, 0,Color.RED, qiantitlet,false,true);
			areaNums[2] = new AreaNum(10,10, 1, 9, BallResId, 0, 0,Color.RED, baititlet,false,true);
			areaNums[3] = new AreaNum(10,10, 1, 9, BallResId, 0, 0,Color.RED, shititlet,false,true);
			areaNums[4] = new AreaNum(10,10, 1, 9, BallResId, 0, 0,Color.RED, getitlet,false,true);
			createView(areaNums, sscCode,FIVE_TONGXUAN,true,checkedId,true);
			BallTable = areaNums[0].table;
			qiantable = areaNums[1].table;
			baitable = areaNums[2].table;
			shitable = areaNums[3].table;
		    getable = areaNums[4].table;
	}
	}
	 protected void onResume() {
		    // TODO Auto-generated method stub
		    super.onResume();
			lotnoStr = Constants.LOTNO_SSC;
            
	}

	 public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();
		int[] wan = areaNums[0].table.getHighlightBallNOs();
		int[] qian =areaNums[1].table.getHighlightBallNOs();
		int[] bai = areaNums[2].table.getHighlightBallNOs();
		int[] shi = areaNums[3].table.getHighlightBallNOs();
		int[] ge =  areaNums[4].table.getHighlightBallNOs();

		return  "注码：" + "\n" + 
				 "万位："
				+ getStrZhuMa(wan) + "\n" + "千位：" + getStrZhuMa(qian) + "\n"
				+ "百位：" + getStrZhuMa(bai) + "\n" + "十位：" + getStrZhuMa(shi)
				+ "\n" + "个位：" + getStrZhuMa(ge);

	}
	  public String getZhuma(){
	 	   String zhuma="";
	 	   zhuma =   sscCode.zhuma(areaNums, iProgressBeishu, FIVESTARTYPE);
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
			iReturnValue=balls.size()*beishu;
		}else{
		int wan = areaNums[0].table.getHighlightBallNums();
	    int qian = areaNums[1].table.getHighlightBallNums();
		int bai = areaNums[2].table.getHighlightBallNums();
		int shi = areaNums[3].table.getHighlightBallNums();
		int ge = areaNums[4].table.getHighlightBallNums();
		int beishu = iProgressBeishu;
		iReturnValue = wan * qian * bai * shi * ge * beishu;}
		return iReturnValue;
	}

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		if(isjixuan){
			setZhuShu(balls.size());
			alertJX(); 
		}else{
			int iZhuShu = getZhuShu();
			int wan = areaNums[0].table.getHighlightBallNums();
			int qian = areaNums[1].table.getHighlightBallNums();
		    int bai = areaNums[2].table.getHighlightBallNums();
			int shi = areaNums[3].table.getHighlightBallNums();
		    int ge = areaNums[4].table.getHighlightBallNums();
			if (wan == 0 | qian == 0 | bai == 0 | shi == 0 | ge == 0) {
				isTouzhu = "请至少选择一注！";
			} else if (iZhuShu * 2 > MAX_ZHU) {
				isTouzhu = "false";
			} else{
				isTouzhu = "true";
			}
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
	/**
	 * 显示在输入框的注码
	 * 
	 */
	public void showEditText() {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		String zhumas = "";
		int num = 0;// 高亮小球数
		int length = 0;
		for (int j = 0; j < areaNums.length; j++) {
			BallTable ballTable = areaNums[j].table;
			int[] zhuMa = ballTable.getHighlightBallNOs();
			if (j != 0) {
				zhumas +=" , ";
			}
			for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
				if (highttype.equals("SSC")) {
					zhumas += (zhuMa[i]) + "";
				} else if (highttype.equals("DLC")) {
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
				}
			}
			num += zhuMa.length;
		}
		if (num == 0) {
			editZhuma.setText("");
			showEditTitle(this.type);
		} else {
			builder.append(zhumas);
			String zhuma[]=zhumas.split("\\,");
			for (int i = 0; i < zhuma.length; i++) {
				if (i != 0) {
					length += zhuma[i].length() + 1;
				} else {
					length += zhuma[i].length();
				}
				if (i != zhuma.length - 1) {
					builder.setSpan(new ForegroundColorSpan(Color.BLACK), length, length + 1, Spanned.SPAN_COMPOSING);
				}
				builder.setSpan(new ForegroundColorSpan(areaNums[i].textColor), length - zhuma[i].length(), length,
						Spanned.SPAN_COMPOSING);
			}
			editZhuma.setText(builder, BufferType.EDITABLE);
			showEditTitle(NULL);
		}
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
		betAndGift.setBatchcode(Ssc.batchCode);
	}
	
	void setLotoNoAndType(CodeInfo codeInfo){
		codeInfo.setLotoNo(Constants.LOTNO_SSC);
		if(radioId == 0){
			codeInfo.setTouZhuType("5start_zhixuan");
		}else{
			codeInfo.setTouZhuType("5start_tongxuan");
		}
		
	}
}
