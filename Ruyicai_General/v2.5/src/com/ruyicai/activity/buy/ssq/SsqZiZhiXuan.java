/**
 * 
 */
package com.ruyicai.activity.buy.ssq;

import android.graphics.Color;
import android.os.Bundle;

import com.palmdream.RuyicaiAndroid.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.ssq.SsqZiZhiXuanCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;

/**
 * 双色球自选直选
 * 
 * @author Administrator
 * 
 */
public class SsqZiZhiXuan extends ZixuanActivity implements BuyImplement{
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };// 选区小球切换图片
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };// 选区小球切换图片
	private AreaInfo areaInfos[] = new AreaInfo[2];// 2个选区
	private SsqZiZhiXuanCode ssqCode = new SsqZiZhiXuanCode();
	BallTable redBallTable ;
	BallTable blueBallTable;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initArea();
		createView(areaInfos, ssqCode,this);
		redBallTable = areaNums[0].table;
		blueBallTable = areaNums[1].table;
	}

	/**
	 * 初始化选区
	 */
	public void initArea() {
        String redTitle = getResources().getString(R.string.ssq_zhixuan_text_red_title).toString();
        String blueTitle = getResources().getString(R.string.ssq_zhixuan_text_blue_title).toString();
		areaInfos[0] = new AreaInfo(33, 20, redBallResId, 0, 1,Color.RED,redTitle);
		areaInfos[1] = new AreaInfo(16, 16, blueBallResId, 0, 1,Color.BLUE,blueTitle);
	}

	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


    /**
     * 判断是否满足投注条件    
     */
	public void isTouzhu() {
		// TODO Auto-generated method stub

		int iZhuShu = getZhuShu();
		if (redBallTable.getHighlightBallNums() < 6 && blueBallTable.getHighlightBallNums() < 1) {
			alertInfo("请至少选择6个红球和1个蓝球	");
		} else if (redBallTable.getHighlightBallNums() < 6) {
			alertInfo("请选择至少6个红球");
		} else if (blueBallTable.getHighlightBallNums() < 1) {
			alertInfo("请选择1个蓝球");
		} else if (iZhuShu * 2 > 100000) {
			dialogExcessive();
		} else {
			String sTouzhuAlert = "";
			sTouzhuAlert = getTouzhuAlert();
			alert(sTouzhuAlert,getZhuma());
		}
	}
	/**
	 * 投注提示框中的信息
	 */
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();

	  return "注数："+ iZhuShu / mSeekBarBeishu.getProgress()
					+ "注"
					+ "\n"
					+ // 注数不能算上倍数 陈晨 20100713
					"倍数：" + mSeekBarBeishu.getProgress() + "倍" + "\n" + "追号："
					+ mSeekBarQishu.getProgress() + "期" + "\n" + "金额："
					+ (iZhuShu * 2) + "元" + "\n" + "冻结金额："
					+ (2 * (mSeekBarQishu.getProgress() - 1) * iZhuShu) + "元"
					+ "\n" ;

	}
	/**
	 * 投注提示框中的信息(注码)
	 * @return
	 */
	public String getZhuma(){
		String red_zhuma_string = " ";
		int[] redZhuMa = redBallTable.getHighlightBallNOs();
		for (int i = 0; i < redBallTable.getHighlightBallNOs().length; i++) {
			red_zhuma_string = red_zhuma_string + String.valueOf(redZhuMa[i]);
			if (i != redBallTable.getHighlightBallNOs().length - 1) {
				red_zhuma_string = red_zhuma_string + ",";
			}
		}
		String blue_zhuma_string = " ";
		int[] blueZhuMa = blueBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueBallTable.getHighlightBallNOs().length; i++) {
			blue_zhuma_string = blue_zhuma_string
					+ String.valueOf(blueZhuMa[i]);
			if (i != blueBallTable.getHighlightBallNOs().length - 1) {
				blue_zhuma_string = blue_zhuma_string + ",";
			}
		}
		return "注码：\n" + red_zhuma_string + " | "+ blue_zhuma_string + "\n";
		
	}
	/**
	 * 计算注数
	 */
	public int getZhuShu(){
		int iZhuShu = 0;
		int iRedHighlights = areaNums[0].table.getHighlightBallNums();
		int iBlueHighlights = areaNums[1].table.getHighlightBallNums();
		iZhuShu = (int) getSSQZXZhuShu(iRedHighlights,iBlueHighlights,iProgressBeishu);
		return iZhuShu;		
	}
    /**
     * 投注联网
     */
	public void touzhuNet() {
		// TODO Auto-generated method stub
		int zhuShu=getZhuShu();
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_SSQ);
		betAndGift.setAmount(""+zhuShu*200);
	}
    /**
     * 选择小球提示金额
     */
	public String textSumMoney(AreaNum areaNum[], int iProgressBeishu) {
		// TODO Auto-generated method stub
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iBlueHighlights = areaNum[1].table.getHighlightBallNums();
		String iTempString = "";
		int iZhuShu = 0;
		// 红球数 不足
		if (iRedHighlights < 6) {
//			return context.getResources().getString(R.string.please_choose_red_number);
			int num = 6-iRedHighlights;
			return "至少还需"+num+"个红球";
		}
		// 红球数达到最低要求
		else if (iRedHighlights == 6) {
			if (iBlueHighlights < 1) {
//				return	context.getResources().getString(R.string.please_choose_blue_number);
				return "至少还需1个蓝球";
			} else if (iBlueHighlights == 1) {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights,iBlueHighlights,iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
			} else {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights,iBlueHighlights,iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
			}
		}
		// 红球复式
		else {
			if (iBlueHighlights < 1) {
//				return getResources().getString(R.string.please_choose_blue_number);
				return "至少还需1个蓝球";
			} else if (iBlueHighlights == 1) {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights,iBlueHighlights,iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
			} else {
				iZhuShu = (int) getSSQZXZhuShu(iRedHighlights,iBlueHighlights,iProgressBeishu);
				iTempString = "共" + iZhuShu + "注，共"+ (iZhuShu * 2) + "元";
			}
		}
		return iTempString;
	}
	
	/**
	 * 复式玩法注数计算方法
	 * 
	 * @param int aRedBalls 红球个数
	 * 
	 * @param int aBlueBalls 篮球个数
	 * 
	 * @return long 注数
	 */
	private long getSSQZXZhuShu(int aRedBalls, int aBlueBalls,int iProgressBeishu) {
		long ssqZhuShu = 0L;
		if (aRedBalls > 0 && aBlueBalls > 0) {
			ssqZhuShu += (PublicMethod.zuhe(6, aRedBalls)* PublicMethod.zuhe(1, aBlueBalls) * iProgressBeishu);
		}
		return ssqZhuShu;
	}


}
