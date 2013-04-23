/**
 * 
 */
package com.ruyicai.activity.buy.fc3d;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.fc3d.Fc3dZiZhiXuanCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

/**
 * @author Administrator
 *
 */
public class Fc3dZiZhiXuan extends ZixuanActivity implements BuyImplement{
	private int ballResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[3];// 3个选区
	private Fc3dZiZhiXuanCode fc3dCode = new Fc3dZiZhiXuanCode();
	BallTable baiBallTable ;
	BallTable shiBallTable ;
	BallTable geBallTable ;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initArea();
		createView(areaInfos, fc3dCode,this,false);
		LinearLayout thirdLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_third);
		thirdLinear.setVisibility(LinearLayout.VISIBLE);
		baiBallTable = areaNums[0].table;
		shiBallTable = areaNums[1].table;
		geBallTable = areaNums[2].table;
	}
	/**
	 * 初始化选区
	 */
	public void initArea() {
        String baiTitle = getResources().getString(R.string.fc3d_text_bai_title).toString();
        String shiTitle = getResources().getString(R.string.fc3d_text_shi_title).toString();
        String geTitle = getResources().getString(R.string.fc3d_text_ge_title).toString();

		areaInfos[0] = new AreaInfo(10, 10, ballResId, 0, 0,Color.RED,baiTitle);
		areaInfos[1] = new AreaInfo(10, 10, ballResId, 0, 0,Color.RED,shiTitle);
		areaInfos[2] = new AreaInfo(10, 10, ballResId, 0, 0,Color.RED,geTitle);
	}
	/**
	 * 判断是否满足投注条件
	 */
	public void isTouzhu() {
		// TODO Auto-generated method stub
		int baiweiNums = baiBallTable.getHighlightBallNums();
        int shiweiNums = shiBallTable.getHighlightBallNums();
        int geweiNums = geBallTable.getHighlightBallNums();
        int[] baiweis = baiBallTable.getHighlightBallNOs();
        int[] shiweis = shiBallTable.getHighlightBallNOs();
        int[] geweis = geBallTable.getHighlightBallNOs();
        String baiweistr = "";
        String shiweistr = "";
        String geweistr = "";
        for (int i = 0; i < baiweiNums; i++) {
	        baiweistr += (baiweis[i]) + ",";
	        if (i == baiweiNums - 1) {
		     baiweistr = baiweistr.substring(0, baiweistr.length() - 1);
	        } 
         }
        for (int i = 0; i < shiweiNums; i++) {
	         shiweistr += (shiweis[i]) + ",";
	         if (i == shiweiNums - 1) {
		      shiweistr = shiweistr.substring(0, shiweistr.length() - 1);
	        }
         }
        for (int i = 0; i < geweiNums; i++) {
	         geweistr += (geweis[i]) + ",";
	        if (i == geweiNums - 1) {
		     geweistr = geweistr.substring(0,geweistr.length() - 1);
	         }
        }
       if (baiweiNums < 1 || shiweiNums < 1 || geweiNums < 1) {
	       alertInfo("请在百位，十位，个位均至少选择一个小球后再投注");
        } else {
	    int iZhuShu = getZhuShu();
       if (iZhuShu / iProgressBeishu > 600) {
		      dialogZhixuan();
	    } else if (iZhuShu * 2 > 100000) {
		      dialogExcessive();
	    } else {
	    	 setZhuShu(iZhuShu);
		     alert("注码：" + "\n" + "百位：" + baiweistr + "\n"
				+ "十位：" + shiweistr + "\n" + "个位："
				+ geweistr);
	          }
         }
		
	}

	 /**
	    * 点击小球提示金额
	    * @param areaNum
	    * @param iProgressBeishu
	    * @return
	  */
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		String mTextSumMoney="";
		// TODO Auto-generated method stub
		if (baiBallTable.getHighlightBallNums() >0 && shiBallTable.getHighlightBallNums() >0&& geBallTable.getHighlightBallNums() >0)  {
			int iZhuShu = getZhuShu() ;
			mTextSumMoney = "共" + iZhuShu + "注，共" + (iZhuShu * 2) + "元";
		} else if(baiBallTable.getHighlightBallNums()==0){
			mTextSumMoney = "至少还需要1个百位小球";
		} else if(shiBallTable.getHighlightBallNums()==0){
			mTextSumMoney = "至少还需要1个十位小球";
		} else if(geBallTable.getHighlightBallNums()==0){
			mTextSumMoney = "至少还需要1个个位小球";
		}
		return mTextSumMoney;
	}
	/**
	 * 获取注数
	 */
	public int getZhuShu(){
		int iReturnValue = 0;
			iReturnValue = baiBallTable.getHighlightBallNums()* shiBallTable.getHighlightBallNums()* geBallTable.getHighlightBallNums();
		return iReturnValue * iProgressBeishu;
		
	}

	 /**
	  * 投注联网
	  */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_FC3D);
	}
}
