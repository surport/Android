package com.ruyicai.activity.buy.ssc;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroidxuancai.R;
import com.ruyicai.activity.buy.high.ZixuanAndJiXuan;
import com.ruyicai.activity.buy.miss.BuyViewItemMiss;
import com.ruyicai.code.ssc.ThreeStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.custom.jc.button.MyButton;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.json.miss.SscZMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;

public class SscThreeStar extends ZixuanAndJiXuan  {
	public boolean isjixuan=false;
	public static final int SSC_TYPE=3;
	public static SscThreeStar self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String []{"直选"};
		setContentView(R.layout.sscbuyview);
		sscCode = new ThreeStarCode();
		self =this;
		highttype = "SSC";
		theMethodYouWantToCall();
	}
	public void theMethodYouWantToCall(){
        // do what ever you want here
		init();
		childtypes.setVisibility(View.GONE);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		onCheckAction(checkedId);
		
    }    
	public void onCheckAction(int checkedId){
		switch(checkedId){		
		case 0:
			radioId = 0;
			isjixuan=false;
			BallTable shitable;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String baititle = "百位区：";
			String shititle = "十位区：" ;
			String getitle = "个位区：";
			AreaNum[] areaNums = new AreaNum[3];
			areaNums[0] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, baititle,false,true);
			areaNums[1] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, shititle,false,true);
			areaNums[2] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, getitle,false,true);
			createViewNew(areaNums, sscCode,ZixuanAndJiXuan.THREE,true,checkedId);
			BallTable = areaNums[0].table;
			shitable = areaNums[1].table;
		    getable = areaNums[2].table;
		    isMissNet(new SscMissJson(),MissConstant.SSC_5X_ZX,false);//获取遗漏值
		    isMissNet(new SscZMissJson(),MissConstant.SSC_MV_3ZHI_ZH,true);//获取遗漏值
		break;
//		case 1:
//			 radioId = 1;
//			 isjixuan=true;
//			 iProgressBeishu = 1;iProgressQishu = 1;
//			 SscBalls sscb = new SscBalls(3);
//			 createviewmechine(sscb,checkedId);
//		break;	
		}
	}
	  public String getZhuma(){
	 	   String zhuma="";
	 	   zhuma =   sscCode.zhuma(areaNums, iProgressBeishu, 0);
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
			lotnoStr = Constants.LOTNO_SSC;
  }

	@Override
	public String isTouzhu() {
		String isTouzhu = "";
		if(isMove&&itemViewArray.get(newPosition).isZHmiss){
			int onClickNum = getClickNum();
			if(onClickNum==0){
				isTouzhu = "请至少选择一注！";
			}else{
				isTouzhu = "true";
			}
		}else{
			int bai =areaNums[0].table.getHighlightBallNums();
			int shi =areaNums[1].table.getHighlightBallNums();
			int ge = areaNums[2].table.getHighlightBallNums();
			int iZhuShu = getZhuShu();
			if (bai == 0 | shi == 0 | ge == 0) {
				isTouzhu = "请至少选择一注！";
			} else if (iZhuShu > MAX_ZHU) {
				isTouzhu = "false";
			} 
//			else if (bai + shi + ge > 24) {
//				isTouzhu = "三星直选，小球的个数最大为24个！";
//			} 
			else {
				isTouzhu = "true";
			}
			}		   
		return isTouzhu;
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
		String iTempString = "";
		if(isMove&&itemViewArray.get(getNewPosition()).isZHmiss){
			int onClickNum = getClickNum();
			if(onClickNum == 0){
				iTempString = getResources().getString(R.string.please_choose_number);
			}else{
				iTempString = "共" + onClickNum + "注，共" + (onClickNum * 2) + "元";
			}
		}else{
			int iZhuShu = getZhuShu();
			if (iZhuShu != 0) {
			    iTempString = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
			} else {
				iTempString = getResources().getString(R.string.please_choose_number);
			}
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
		betAndGift.setBatchcode(Ssc.batchCode);
	}
}

