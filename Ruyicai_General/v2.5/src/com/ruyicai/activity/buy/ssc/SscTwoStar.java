package com.ruyicai.activity.buy.ssc;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.code.ssc.TwoStarCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

public class SscTwoStar extends ZixuanAndJiXuan implements BuyImplement{
	    public static final int SSC_TYPE =2;
	    public static int TWOSTARTYPE=0;
	    public boolean isjixuan=false;
        public static SscTwoStar self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		childtype = new String []{"直选","机选","组选","和值"};
		setContentView(R.layout.sscbuyview);
        sscCode = new TwoStarCode();
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
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_ZHIXUAN;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String shititle = "十位区" ;
			String getitle = "个位区";
			areaInfos= new AreaInfo[2];
			areaInfos[0] = new AreaInfo(10, 6, BallResId, 0, 0,Color.RED, shititle);
			areaInfos[1] = new AreaInfo(10, 6, BallResId, 0, 0,Color.RED, getitle);
			createView(areaInfos, sscCode,this);
			BallTable=areaNums[0].table;
		    getable = areaNums[1].table;
		break;
		case 1:
			isjixuan=true;
			iProgressBeishu = 1;iProgressQishu = 1;
			SscBalls  sscb = new SscBalls(2);
			createviewmechine(sscb,this);
		break;	
		case 2:
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_ZUXUAN;
			iProgressBeishu = 1;iProgressQishu = 1;
			areaInfos= new AreaInfo[1];
	        String  titlezu = "请选择投注号码";
			areaInfos[0] = new AreaInfo(10, 7, BallResId, 0, 0,Color.RED, titlezu);
			createView(areaInfos, sscCode,this);
			BallTable=areaNums[0].table;
		break;
		case 3:
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_HEZHI;
			iProgressBeishu = 1;iProgressQishu = 1;
	        String  titlehe = "请选择投注号码";
	        areaInfos= new AreaInfo[1];
			areaInfos[0] = new AreaInfo(19, 8, BallResId, 0, 0,Color.RED, titlehe);
			createView(areaInfos, sscCode,this);
			BallTable=areaNums[0].table;
        break;
	}

}      
	    protected void onResume() {
		    // TODO Auto-generated method stub
		    super.onResume();
		
		    }
	    private int getZhuShu() {
		   int iReturnValue = 0;
		   int beishu = mSeekBarBeishu.getProgress();
		   if(isjixuan){
			  iReturnValue =balls.size()*beishu; 
		   }else{
		   switch(TWOSTARTYPE){
		   case Constants.SSC_TWOSTAR_ZHIXUAN:
			  int shi = areaNums[0].table.getHighlightBallNums();
			  int ge = areaNums[1].table.getHighlightBallNums();
		      iReturnValue = shi*ge * beishu;
			break;
		   case Constants.SSC_TWOSTAR_ZUXUAN:
			  int one = areaNums[0].table.getHighlightBallNums();
			  iReturnValue = (int) (PublicMethod.zuhe(2, one) * beishu);
			break;
		   case Constants.SSC_TWOSTAR_HEZHI:
			  iReturnValue = getSscHeZhiZhushu() * beishu; 
		   }
		   }
		   return iReturnValue;
		}
	    
	   public String getZhuma(){
	 	   String zhuma="";
	 	   if(isjixuan){
	 	    zhuma = SscBalls.getzhuma(balls, SSC_TYPE);
	 	   }else{
	 	   zhuma =   sscCode.zhuma(areaNums, iProgressBeishu, TWOSTARTYPE);
	 	   }
	 	   return zhuma;
	    }
	   private int getSscHeZhiZhushu() {
			int iZhuShu = 0;
			int[] BallNos = areaNums[0].table.getHighlightBallNOs();// 被选择小球的号码（点击1，获得0），故实际选择的减去1
			int[] BallNoZhushus={1,1,2,2,3,3,4,4,5,5,5,4,4,3,3,2,2,1,1};
			

			for (int i = 0; i < BallNos.length; i++) {
				for (int j = 0; j < BallNoZhushus.length; j++) {
					if (j == BallNos[i]) {// 因为数组是从0开始的，小球号码从1开始，故减去1
						// 删除倍数 cc 20100713
						iZhuShu += BallNoZhushus[j];// *iProgressBeishu;
					}
				}
			}
			return iZhuShu;
		}
	@Override
	public void isTouzhu() {
		// TODO Auto-generated method stub
		if(isjixuan){
			String sTouzhuAlert = "";
			sTouzhuAlert = getTouzhuAlertJixuan();
			alertjx(sTouzhuAlert); 
		}else{
			int iZhuShu = getZhuShu();
			boolean isTouzhu = false;
	        switch (TWOSTARTYPE){
			case Constants.SSC_TWOSTAR_ZUXUAN:
				int one = areaNums[0].table.getHighlightBallNums();
				if (one < 2) {
					alertInfo("请至少选择一注！");
				} else if (iZhuShu * 2 > 20000) {
					dialogExcessive();
				} else if (one > 7) {
					alertInfo("二星组选，小球的个数最大为7个！");
				} else {
					isTouzhu = true;
				}
				break;
			case Constants.SSC_TWOSTAR_ZHIXUAN:
				int shi = areaNums[0].table.getHighlightBallNums();
				int ge = areaNums[1].table.getHighlightBallNums();
				if (shi == 0 | ge == 0) {
					alertInfo("请至少选择一注！");
				} else if (iZhuShu * 2 > 20000) {
					dialogExcessive();
				} else if (shi + ge > 12) {
					alertInfo("二组分位，小球的个数最大为12个！");
				} else {
					isTouzhu = true;
				}
				   
				break;

			case Constants.SSC_TWOSTAR_HEZHI:
				int one2 = areaNums[0].table.getHighlightBallNums();
				if (one2 == 0) {
					alertInfo("请至少选择一注！");
				} else if (iZhuShu * 2 > 20000) {
					dialogExcessive();
				} else if (one2 > 8) {
					alertInfo("二组和值，小球的个数最大为8个！");
				} else {
					isTouzhu = true;
				}
				break;
	        }
		    	if (isTouzhu) {

				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alertzx(sTouzhuAlert);
			}
		}
	}
    
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
     	switch (TWOSTARTYPE) {
		
		case Constants.SSC_TWOSTAR_ZUXUAN:
			int[] one = areaNums[0].table.getHighlightBallNOs();
			return "第" +Ssc.batchCode+"期\n" 
					+ "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
					+"注码：" + "\n" 
					+ getStrZhuMa(one)
					+ "\n" + "确认支付吗？";
		
  
		case Constants.SSC_TWOSTAR_ZHIXUAN:
			int[] shi = areaNums[0].table.getHighlightBallNOs();
			int[] ge = areaNums[1].table.getHighlightBallNOs();
			return 
			"第" +Ssc.batchCode+"期\n" 
			+ "注数："
			+ iZhuShu / mSeekBarBeishu.getProgress()
			+ "注"
			+ "\n"
			+ // 注数不能算上倍数 陈晨 20100713
			"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
			+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
			+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
			+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
			+"注码：" + "\n" +"十位:"+getStrZhuMa(shi) + "\n" + "个位:" + getStrZhuMa(ge)
			+ "\n" + "确认支付吗？";

		case Constants.SSC_TWOSTAR_HEZHI:
			int[] one2 = areaNums[0].table.getHighlightBallNOs();
			return  "第" +Ssc.batchCode+"期\n" 
					+ "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
					+"注码：" + "\n" + getStrZhuMa(one2) 
					+ "\n" + "确认支付吗？";

		default:
			break;
		}
		return null;
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
}