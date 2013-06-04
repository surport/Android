package com.ruyicai.activity.buy.dlt;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ToggleButton;

import com.palmdream.RuyicaiAndroid91.R;
import com.ruyicai.activity.buy.ZixuanActivity;
import com.ruyicai.code.dlt.DltDantuoSelectCode;
import com.ruyicai.interfaces.BuyImplement;
import com.ruyicai.pojo.AreaNum;
import com.ruyicai.pojo.BallTable;
import com.ruyicai.util.AreaInfo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicConst;
import com.ruyicai.util.PublicMethod;

public class DltDantuoSelect extends ZixuanActivity implements BuyImplement{
	
	private int redBallResId[] = { R.drawable.grey, R.drawable.red };
	private int blueBallResId[] = { R.drawable.grey, R.drawable.blue };

	private  ToggleButton  zhuijiatouzhu;
	private int singleLotteryValue = 2;
	AreaInfo[] dltDantuoAreaInfos = new AreaInfo[4];
	
	/**
	 * 大乐透胆托红球区和篮球区
	 */
	BallTable redBallTable,blueBallTable,redTuoBallTable,blueTuoBallTable;
	
	/**
	 * 实例化大乐透直选注码类
	 */
	DltDantuoSelectCode dltDantuocode = new DltDantuoSelectCode();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView scroller = (ScrollView) findViewById(R.id.buy_zixuan_scroll);
//		scroller.setPadding(0, 0, 0, 140);
		LinearLayout fifthLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_five);
		fifthLinear.setVisibility(LinearLayout.GONE);
		LinearLayout toggleLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_toggle);
		toggleLinear.setVisibility(LinearLayout.VISIBLE);
		LinearLayout fourLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_four);
		fourLinear.setVisibility(LinearLayout.VISIBLE);
		LinearLayout thirdLinear = (LinearLayout)findViewById(R.id.buy_zixuan_linear_third);
		thirdLinear.setVisibility(LinearLayout.VISIBLE);
		zhuijiatouzhu = (ToggleButton)findViewById(R.id.dlt_zhuijia);
		zhuijiatouzhu.setOnCheckedChangeListener(new OnCheckedChangeListener() {			

			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if(isChecked){
					singleLotteryValue = 3;
					betAndGift.setIssuper("0");
				}else{
					singleLotteryValue = 2;
					betAndGift.setIssuper("");
				}
				changeTextSumMoney();
			}
		});
		initDltNormalArea();
		createView(dltDantuoAreaInfos, dltDantuocode,this);
		redBallTable = areaNums[0].table;
		redTuoBallTable = areaNums[1].table;
		blueBallTable = areaNums[2].table;
		blueTuoBallTable = areaNums[3].table;
		
	}
	/**
	 * 初始化大了透直选选取
	 */
	public void initDltNormalArea(){
		String redTitle = getResources().getString(R.string.front_ball_danma).toString();
		String redtuoTitle = getResources().getString(R.string.front_ball_tuoma).toString();
        String blueTitle = getResources().getString(R.string.rear_ball_danma).toString();
        String bluetuoTitle = getResources().getString(R.string.rear_ball_tuoma).toString();
        dltDantuoAreaInfos[0] = new AreaInfo(35, 4, redBallResId, 0, 1,Color.RED,redTitle);
        dltDantuoAreaInfos[1] = new AreaInfo(35, 18, redBallResId, 0, 1,Color.RED,redtuoTitle);
        dltDantuoAreaInfos[2] = new AreaInfo(12, 1, blueBallResId, 0, 1,Color.BLUE,blueTitle);
        dltDantuoAreaInfos[3] = new AreaInfo(12, 12, blueBallResId, 0, 1,Color.BLUE,bluetuoTitle);
	}
	
	@Override
	public String textSumMoney(AreaNum[] areaNum, int iProgressBeishu) {
		int iRedHighlights = areaNum[0].table.getHighlightBallNums();
		int iRedTuoHighlights = areaNum[1].table.getHighlightBallNums();
		int iBlueHighlights = areaNum[2].table.getHighlightBallNums();
		int iBlueTuoHighlights = areaNum[3].table.getHighlightBallNums();
		StringBuffer alertString = new StringBuffer();
		if (iRedHighlights + iRedTuoHighlights < 6 ) {
			int num = 6-iRedHighlights-iRedTuoHighlights;
			alertString.append("至少还需要").append(num).append("个前区号码");
		}else if (iBlueTuoHighlights< 2) {
			int num = 2-iBlueTuoHighlights;
			alertString.append("至少还需要").append(num).append("个后区拖码");
		}else if (iRedHighlights < 1&&iBlueHighlights < 1){
			alertString.append("请至少选择一个前区胆码或后区胆码");
		}else {
			int iZhuShu = (int) getDltDTZhuShu(iRedHighlights, iRedTuoHighlights, iBlueHighlights, iBlueTuoHighlights, iProgressBeishu);
			alertString.append("共").append(iZhuShu).append("注，共").append(iZhuShu * singleLotteryValue).append("元");	
		}
		return alertString.toString(); 
	}

	@Override
	public void touzhuNet() {
		int zhuShu=getZhuShu();
		betAndGift.setSellway("0");//1代表机选 0代表自选
		betAndGift.setLotno(Constants.LOTNO_DLT);
		betAndGift.setAmount(""+zhuShu*singleLotteryValue*100);
		betAndGift.setAmt(singleLotteryValue);
	}
	@Override
	public void isTouzhu() {
		int iZhuShu = getZhuShu();
		int redNumber = redBallTable.getHighlightBallNums();
		int redTuoNumber = redTuoBallTable.getHighlightBallNums();
		int blueNumber = blueBallTable.getHighlightBallNums();
		int blueTuoNumber = blueTuoBallTable.getHighlightBallNums();
		if (redNumber + redTuoNumber < 6|| redNumber + redTuoNumber > 20
             || blueTuoNumber< 2 || blueTuoNumber + blueNumber >12) {
			alertInfo("请选择:\n前区号码6~20个;\n后区拖码2~12个");
		} else if (iZhuShu * 2 > 100000) {
			dialogExcessive();
		} else if (redNumber < 1&&blueNumber < 1){
			alertInfo("请至少选择一个前区胆码或后区胆码");
		}else {
			String sTouzhuAlert = "";
			sTouzhuAlert = getTouzhuAlert();
			alert(sTouzhuAlert,getZhuma());
		}
	}
	/**
	 * 投注信息提示框
	 * @return
	 */
	private String getTouzhuAlert() {
		int iZhuShu = getZhuShu();

		
		StringBuffer alertcontent = new StringBuffer();
		alertcontent.append("注数：").append(iZhuShu / mSeekBarBeishu.getProgress()).append("注").append("\n")
				.append("倍数：").append(mSeekBarBeishu.getProgress()).append("倍").append("\n")
				.append("追号：").append(mSeekBarQishu.getProgress()).append("期").append("\n")
				.append("金额：").append((iZhuShu * singleLotteryValue)).append("元").append("\n")
				.append("冻结金额：").append((singleLotteryValue * (mSeekBarQishu.getProgress() - 1) * iZhuShu)).append("元").append("\n");
		return  alertcontent.toString();
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
		String red_tuo_zhuma_string = " ";
		int[] redTuoZhuMa = redTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < redTuoBallTable.getHighlightBallNOs().length; i++) {
			red_tuo_zhuma_string = red_tuo_zhuma_string
					+ String.valueOf(redTuoZhuMa[i]);
			if (i != redTuoBallTable.getHighlightBallNOs().length - 1)
				red_tuo_zhuma_string = red_tuo_zhuma_string + ",";
		}
		String blue_tuo_zhuma_string = " ";
		int[] blueTuoZhuMa = blueTuoBallTable.getHighlightBallNOs();
		for (int i = 0; i < blueTuoBallTable.getHighlightBallNOs().length; i++) {
			blue_tuo_zhuma_string +=  String.valueOf(blueTuoZhuMa[i]);
			if (i != blueTuoBallTable.getHighlightBallNOs().length - 1)
				blue_tuo_zhuma_string += ",";
		}
		return "注码：\n" + "前区胆码：" + red_zhuma_string + "\n前区拖码："+ red_tuo_zhuma_string +"\n后区胆码："+blue_zhuma_string+"\n后区拖码："
		       +blue_tuo_zhuma_string;
		
	}
	/**
	 * 获取投注的注数信息
	 * @return
	 */
	private int getZhuShu() {
		int iZhuShu = 0;
		int iRedHighlights = redBallTable.getHighlightBallNums();
		int iRedTuoHighlights = redTuoBallTable.getHighlightBallNums();
		int iBlueHighlights = blueBallTable.getHighlightBallNums();
		int iBlueTuoHighlights = blueTuoBallTable.getHighlightBallNums();
		iZhuShu = (int) getDltDTZhuShu(iRedHighlights, iRedTuoHighlights, iBlueHighlights,iBlueTuoHighlights, iProgressBeishu);
		return iZhuShu;		
	}
	/**
	 * 大乐透胆拖投注的注数计算方法
	 * @param aRedBalls   前区胆码小球数
	 * @param aRedTuoBalls 前区拖码小球数
  	 * @param aBlueBalls 后区胆码小球数
	 * @param aBlueTuoBalls  后区拖码小球数
	 * @param iProgressBeishu 倍数
	 * @return
	 */
	private long getDltDTZhuShu(int aRedBalls, int aRedTuoBalls,int aBlueBalls, int aBlueTuoBalls,int iProgressBeishu) {// 得到大乐透胆拖的注数
		long dltZhuShu = 0L;
		dltZhuShu += (PublicMethod.zuhe(5 - aRedBalls, aRedTuoBalls)* PublicMethod.zuhe(2 - aBlueBalls, aBlueTuoBalls)*iProgressBeishu);
		return dltZhuShu;
	}
	/**
	 * 根据小球id判断是哪个选区，小球根据选区确定被覆盖问题
	 * 
	 * @param iBallId
	 */
	public void isBallTable(int iBallId){
		int nBallId = 0; 
		for(int i=0;i<areaNums.length;i++){
			nBallId = iBallId;
			iBallId = iBallId - areaNums[i].info.areaNum;
			if(iBallId<0){
				if(i == 0){
					int isHighLight = areaNums[0].table.changeBallState(areaNums[0].info.chosenBallSum, nBallId);
					if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[1].table.getOneBallStatue(nBallId) !=0) {
						areaNums[1].table.clearOnBallHighlight(nBallId);
						toast.setText(getResources().getString(R.string.dlt_toast_front_danma_title));
						toast.show();
					}	
				}else if(i == 1){
					int isHighLight = areaNums[1].table.changeBallState(areaNums[1].info.chosenBallSum, nBallId);
					if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[0].table.getOneBallStatue(nBallId) !=0) {
						areaNums[0].table.clearOnBallHighlight(nBallId);
						toast.setText(getResources().getString(R.string.dlt_toast_front_tuoma_title));
						toast.show();
					}
				}else if (i == 2) {
					int isHighLight = areaNums[2].table.changeBallState(areaNums[2].info.chosenBallSum, nBallId);
					if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[3].table.getOneBallStatue(nBallId) !=0) {
						areaNums[3].table.clearOnBallHighlight(nBallId);
						toast.setText(getResources().getString(R.string.dlt_toast_rear_danma_title));
						toast.show();
					}
				}else {
					int isHighLight = areaNums[3].table.changeBallState(areaNums[3].info.chosenBallSum, nBallId);				
			    	if (isHighLight == PublicConst.BALL_TO_HIGHLIGHT&& areaNums[2].table.getOneBallStatue(nBallId) !=0) {
						areaNums[2].table.clearOnBallHighlight(nBallId);
						toast.setText(getResources().getString(R.string.dlt_toast_rear_tuoma_title));
						toast.show();
				    }
				}
				
		    	break;

			}
		}
	}
}
