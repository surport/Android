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
import com.ruyicai.code.ssc.TwoStarCode;
import com.ruyicai.constant.Constants;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.jixuan.Balls;
import com.ruyicai.jixuan.SscBalls;
import com.ruyicai.json.miss.MissConstant;
import com.ruyicai.json.miss.SscMissJson;
import com.ruyicai.json.miss.SscZMissJson;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.PublicMethod;

public class SscTwoStar extends ZixuanAndJiXuan {
	    public static final int SSC_TYPE =2;
	    public static int TWOSTARTYPE=0;
	    public boolean isjixuan=false;
        public static SscTwoStar self;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lotnoStr = Constants.LOTNO_SSC;
		childtype = new String []{"直选","组选","和值"};
		setContentView(R.layout.sscbuyview);
        sscCode = new TwoStarCode();
        self = this;
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
		
    }      
	public void onCheckAction(int checkedId){
		radioId = checkedId;
		switch(checkedId){		
		case 0:
			sellWay = MissConstant.SSC_5X_ZX;
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_ZHIXUAN;
			BallTable getable;
			iProgressBeishu = 1;iProgressQishu = 1;
			String shititle = "十位区：" ;
			String getitle = "个位区：";
		    areaNums = new AreaNum[2];
			areaNums[0] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, shititle,false,true);
			areaNums[1] = new AreaNum(10,10, 1,11, BallResId, 0, 0,Color.RED, getitle,false,true);
			createViewNew(areaNums, sscCode,TWO,true,checkedId);
			BallTable=areaNums[0].table;
		    getable = areaNums[1].table;
		    isMissNet(new SscMissJson(),MissConstant.SSC_5X_ZX,false);//获取遗漏值
		    isMissNet(new SscZMissJson(),MissConstant.SSC_MV_2ZHI_ZH,true);//获取遗漏值
		   
		break;
//		case 1:
//			isjixuan=true;
//			iProgressBeishu = 1;iProgressQishu = 1;
//			SscBalls  sscb = new SscBalls(2);
//			createviewmechine(sscb,checkedId);
//		break;	
		case 1:
			sellWay = MissConstant.SSC_MV_2ZX;
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_ZUXUAN;
			iProgressBeishu = 1;iProgressQishu = 1;
			areaNums= new AreaNum[1];
	        String  titlezu = "请选择投注号码";
	        areaNums[0] = new AreaNum(10,10,2,7, BallResId, 0, 0,Color.RED, titlezu,false,false);
			createViewNew(areaNums, sscCode,TWO_ZUXUAN,true,checkedId);
			BallTable=areaNums[0].table;
		    isMissNet(new SscMissJson(),MissConstant.SSC_MV_2ZX,false);//获取遗漏值
		    isMissNet(new SscZMissJson(),MissConstant.SSC_MV_2ZX_ZH,true);//获取遗漏值
		break;
		case 2:
			sellWay = MissConstant.SSC_MV_2ZXHZ;
			isjixuan=false;
			TWOSTARTYPE=Constants.SSC_TWOSTAR_HEZHI;
			iProgressBeishu = 1;iProgressQishu = 1;
	        String  titlehe = "请选择投注号码";
	        areaNums= new AreaNum[1];
	        areaNums[0] = new AreaNum(19,10,1,8, BallResId, 0, 0,Color.RED, titlehe,false,false);
			createView(areaNums, sscCode,TWO_HEZHI,true,checkedId,true);
			BallTable=areaNums[0].table;
			isMissNet(new SscMissJson(),MissConstant.SSC_MV_2ZXHZ,false);//获取遗漏值
        break;
	  }
	}
	
	    public int getZhuShu() {
		   int iReturnValue = 0;
		   int beishu = iProgressBeishu;
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
	 	   zhuma =   sscCode.zhuma(areaNums, iProgressBeishu, TWOSTARTYPE);
	 	   return zhuma;
	    }
		@Override
		public String getZhuma(Balls ball) {
			   String zhuma="";
			   zhuma = ball.getZhuma(null, SSC_TYPE);   
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
	public String isTouzhu() {
		// TODO Auto-generated method stub
		String isTouzhuStr = "";
			int iZhuShu = getZhuShu();
	        switch (TWOSTARTYPE){
			case Constants.SSC_TWOSTAR_ZUXUAN:
				if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum==0){
						isTouzhuStr = "请至少选择一注！";
					}else{
						isTouzhuStr = "true";
					}
				}else{
					int one = areaNums[0].table.getHighlightBallNums();
					if (one < 2) {
						isTouzhuStr = "请至少选择一注！";
					} else if (iZhuShu  > MAX_ZHU) {
						isTouzhuStr = "false";
					} else {
						isTouzhuStr = "true";
					}
				}
				break;
			case Constants.SSC_TWOSTAR_ZHIXUAN:
				if(isMove&&itemViewArray.get(newPosition).isZHmiss){
					int onClickNum = getClickNum();
					if(onClickNum==0){
						isTouzhuStr = "请至少选择一注！";
					}else{
						isTouzhuStr = "true";
					}
				}else{
					int shi = areaNums[0].table.getHighlightBallNums();
					int ge = areaNums[1].table.getHighlightBallNums();
					if (shi == 0 | ge == 0) {
						isTouzhuStr =  "请至少选择一注！";
					} else if (iZhuShu > MAX_ZHU) {
						isTouzhuStr = "false";
					} else if (shi + ge > 12) {
						isTouzhuStr =  "二组分位，小球的个数最大为12个！";
					} else {
						isTouzhuStr = "true";
					}
				}
				break;

			case Constants.SSC_TWOSTAR_HEZHI:
				int one2 = areaNums[0].table.getHighlightBallNums();
				if (one2 == 0) {
					isTouzhuStr = "请至少选择一注！";
				} else if (iZhuShu  > MAX_ZHU) {
					isTouzhuStr = "false";
				} else if (one2 > 8) {
					isTouzhuStr = "二组和值，小球的个数最大为8个！";
				} else {
					isTouzhuStr = "true";
				}
				break;
	        }
		  return isTouzhuStr;
	}
    
	public String getZxAlertZhuma() {
		int iZhuShu = getZhuShu();
     	switch (TWOSTARTYPE) {
		
		case Constants.SSC_TWOSTAR_ZUXUAN:
			int[] one = areaNums[0].table.getHighlightBallNOs();
			return "注码：" + "\n" 
					+ getStrZhuMa(one);
		
  
		case Constants.SSC_TWOSTAR_ZHIXUAN:
			int[] shi = areaNums[0].table.getHighlightBallNOs();
			int[] ge = areaNums[1].table.getHighlightBallNOs();
			return "注码：" + "\n" +"十位:"+getStrZhuMa(shi) + "\n" + "个位:" + getStrZhuMa(ge);

		case Constants.SSC_TWOSTAR_HEZHI:
			int[] one2 = areaNums[0].table.getHighlightBallNOs();
			return "注码：" + "\n" + getStrZhuMa(one2);

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
	protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	lotnoStr = Constants.LOTNO_SSC;
	}
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		// TODO Auto-generated method stub
		String iTempString = "";
		if(isMove&&itemViewArray.get(newPosition).isZHmiss){
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
				if(TWOSTARTYPE == Constants.SSC_TWOSTAR_ZHIXUAN){
					if(j==0){
						zhumas += "-,-,-,";
					}else{
						zhumas+=" , ";
					}
					
				}
				for (int i = 0; i < ballTable.getHighlightBallNOs().length; i++) {
					if(highttype.equals("SSC")){
					zhumas += (zhuMa[i])+"";
					}else if(highttype.equals("DLC")){
					zhumas += PublicMethod.getZhuMa(zhuMa[i]);
					}
					if (i != ballTable.getHighlightBallNOs().length - 1&&TWOSTARTYPE != Constants.SSC_TWOSTAR_ZHIXUAN) {
						zhumas += ",";
					}
				}
				num+=zhuMa.length;
			}
             if(num==0){
            	 editZhuma.setText("");
            	 showEditTitle(this.type);
             }else{
				builder.append(zhumas);
				String zhuma[] = null ;
				if(TWOSTARTYPE == Constants.SSC_TWOSTAR_ZHIXUAN){
					zhuma = zhumas.split("\\,");
					
				}else{
					zhuma = zhumas.split("\\|");
				}
				  for(int i=0;i<zhuma.length;i++){
						  if(i!=0){
								length += zhuma[i].length()+1;
							  }else{
								length += zhuma[i].length();
							  }
					     if(i!=zhuma.length-1&&!zhuma[i].equals("-")){	
							  builder.setSpan(new ForegroundColorSpan(Color.BLACK), length, length+1, Spanned.SPAN_COMPOSING);  
						  }
						  builder.setSpan(new ForegroundColorSpan(Color.RED), length-zhuma[i].length(), length, Spanned.SPAN_COMPOSING);  

				  }
			    editZhuma.setText(builder, BufferType.EDITABLE);
			    showEditTitle(NULL);
             }
	}
	
	void setLotoNoAndType(CodeInfo codeInfo){
		codeInfo.setLotoNo(Constants.LOTNO_SSC);
		if(radioId == 0){
			codeInfo.setTouZhuType("2start_zhixuan");
		}else if(radioId == 1){
			codeInfo.setTouZhuType("2start_zuxuan");
		}else if(radioId == 2){
			codeInfo.setTouZhuType("2start_hezhi");
		}
		
	}
}