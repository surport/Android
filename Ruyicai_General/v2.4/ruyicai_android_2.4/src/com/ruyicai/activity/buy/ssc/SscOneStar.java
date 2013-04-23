package com.ruyicai.activity.buy.ssc;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanAndJiXuan;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.util.Constants;

public class SscOneStar extends ZixuanAndJiXuan implements BuyImplement{
	    public static SscOneStar self;
	    private boolean isjixuan=false;
	    public static final int SSC_TYPE = 1;//一星
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		childtype= new String[]{"直选","机选"};
		setContentView(R.layout.sscbuyview);
		self = this;
		init();
		highttype = "SSC";
	}	
	
	public void theMethodYouWantToCall(){
        // do what ever you want here
		init();
    }

	@Override
	protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
	}
	//单选框切换直选，机选
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){		
		case 0:
			isjixuan=false;
			iProgressBeishu = 1;iProgressQishu = 1;
			initArea();
			createView(areaInfos, sscCode,this);
			BallTable=areaNums[0].table;
		break;
		case 1:
			isjixuan=true;
			iProgressBeishu = 1;iProgressQishu = 1;
			SscBalls sscb  = new SscBalls(1);
			createviewmechine(sscb,this);
		break;	
		}
			
	}
    @Override
    protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    }

	@Override
	public void isTouzhu() {
		// TODO Auto-generated method stub
		if(isjixuan){
			String sTouzhuAlert = "";
			sTouzhuAlert = getTouzhuAlertJixuan();
			alert(sTouzhuAlert); 
		}else{
			int ge = BallTable.getHighlightBallNums();
			int iZhuShu = getZhuShu();
			if (ge == 0) {
				alertInfo("请至少选择一注！");
			} else if (iZhuShu * 2 > 20000) {
				dialogExcessive();
			} else if (ge > 5) {
				alertInfo("一星直选，小球的个数最大为5个！");
			} else {
				String sTouzhuAlert = "";
				sTouzhuAlert = getTouzhuAlert();
				alert(sTouzhuAlert); 
			}
			
		}
		
		
	}
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();
		String zhuma_string="";
		int[]  ZhuMa = BallTable.getHighlightBallNOs();
		for (int i = 0; i < BallTable.getHighlightBallNOs().length; i++) {
			    zhuma_string = zhuma_string + String.valueOf(ZhuMa[i]);
			if (i != BallTable.getHighlightBallNOs().length - 1) {
				zhuma_string = zhuma_string + ",";
			}
		}
		return      "第" +Ssc.batchCode + "期\n" 
					+ "注数："
					+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"+ "\n" 
					+"注码：" + "\n" + "个位：" + zhuma_string
					+ "\n" + "确认支付吗？";
		
	}
		

   private int getZhuShu() {
	int iReturnValue = 0;
	
	if(isjixuan){
	int beishu = mSeekBarBeishu.getProgress();	
	iReturnValue = balls.size()*beishu;
	}else{
	int ge = areaNums[0].table.getHighlightBallNums();
	int beishu = mSeekBarBeishu.getProgress();
    iReturnValue = ge * beishu;
    }
	
	return iReturnValue;
}   
   
   public String getZhuma(){
	   String zhuma="";
	   if(isjixuan){
	    zhuma = SscBalls.getzhuma(balls, SSC_TYPE);   
	   }else{
	   zhuma = sscCode.zhuma(areaNums, iProgressBeishu, 0);
	   }
	   return zhuma;
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
