package com.ruyicai.activity.buy.ssc;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.code.ssc.ThreeStarCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;

public class SscThreeStar extends ZixuanAndJiXuan implements BuyImplement {
	public boolean isjixuan=false;
	public static final int SSC_TYPE=3;
	public static SscThreeStar self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String []{"直选","机选"};
		setContentView(R.layout.sscbuyview);
		sscCode = new ThreeStarCode();
		self =this;
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
			isjixuan=false;
			BallTable shitable;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String baititle = "百位区";
			String shititle = "十位区" ;
			String getitle = "个位区";
			areaInfos= new AreaInfo[3];
			areaInfos[0] = new AreaInfo(10, 8, BallResId, 0, 0,Color.RED, baititle);
			areaInfos[1] = new AreaInfo(10, 8, BallResId, 0, 0,Color.RED, shititle);
			areaInfos[2] = new AreaInfo(10, 8, BallResId, 0, 0,Color.RED, getitle);
			createView(areaInfos, sscCode,this,ZixuanAndJiXuan.THREE);
			BallTable = areaNums[0].table;
			shitable = areaNums[1].table;
		    getable = areaNums[2].table;
		break;
		case 1:
			 isjixuan=true;
			 iProgressBeishu = 1;iProgressQishu = 1;
			 SscBalls sscb = new SscBalls(3);
			 createviewmechine(sscb,this);
		break;	
	}

}    
	  public String getZhuma(){
	 	   String zhuma="";
	 	   if(isjixuan){
	 	    zhuma = SscBalls.getzhuma(balls, SSC_TYPE);
	 	   }else{
	 	   zhuma =   sscCode.zhuma(areaNums, iProgressBeishu, 0);
	 	   }
	 	   return zhuma;
	    }
	  private int getZhuShu() {
		    int iReturnValue = 0;
		    if(isjixuan){
		    	int beishu = mSeekBarBeishu.getProgress();
		    	iReturnValue=balls.size()*beishu;
		    }else{
		    int bai =areaNums[0].table.getHighlightBallNums();
		    int shi =areaNums[1].table.getHighlightBallNums();
			int ge = areaNums[2].table.getHighlightBallNums();
			int beishu = iProgressBeishu;
		    iReturnValue =bai*shi* ge * beishu;
		    }
			return iReturnValue;
		}
	  
	  protected void onResume() {
		    // TODO Auto-generated method stub
		    super.onResume();
		   
		    }

	@Override
	public void isTouzhu() {
		  int bai =areaNums[0].table.getHighlightBallNums();
		  int shi =areaNums[1].table.getHighlightBallNums();
		  int ge = areaNums[2].table.getHighlightBallNums();
		int iZhuShu = getZhuShu();
		
		if(isjixuan){
			setZhuShu(balls.size());
			alertJX(); 
		}else{
		if (bai == 0 | shi == 0 | ge == 0) {
			alertInfo("请至少选择一注！");
		} else if (iZhuShu * 2 > 20000) {
			dialogExcessive();
		} else if (bai + shi + ge > 24) {
			alertInfo("三星直选，小球的个数最大为24个！");
		} else {
			setZhuShu(iZhuShu);
			alertZX(getZxAlertZhuma()); 
		}
		}
		   
		
	}
	
	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();
		int[] bai = areaNums[0].table.getHighlightBallNOs();
		int[] shi = areaNums[1].table.getHighlightBallNOs();
		int[] ge = areaNums[2].table.getHighlightBallNOs();
	    return  "注码：" + "\n" 
					+ "百位："
					+ getStrZhuMa(bai) + "\n" + "十位：" + getStrZhuMa(shi) + "\n"
					+ "个位：" + getStrZhuMa(ge);

	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
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
}

